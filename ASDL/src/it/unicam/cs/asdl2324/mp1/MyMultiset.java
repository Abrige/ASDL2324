package it.unicam.cs.asdl2324.mp1;

import java.util.*;


/**
 *
 * Il multiset viene implementato usando la struttura dati HashMap<K, V> definita nelle collections
 * di java. Come valore K cioè come chiavi della mappa usa il generics E passato durante la creazione
 * del multiset, mentre per il parametro V usa gli integer, parametro che viene utilizzato per contare
 * le occorrenze di ogni oggetto.
 * La scelta progettuale fatta per questo Multiset prevede di non contenere un oggetto nel momento in
 * cui le sue occorrenze siano 0 e quindi di eliminarlo.
 * L'iteratore è una classe interna e non supporta il remove().
 * 
 * @author Luca Tesei (template)
 *         Mattia Brizi, mattia.brizi@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi del multiset
 */
public class MyMultiset<E> implements Multiset<E> {

    //usa una HashMap per come struttura dati per contenere le coppie di valori (Oggetto, Occorrenze)
    private final HashMap<E, Integer> map;
    //tiene traccia della grandezza del Set
    private int size;
    //serve all'iteratore per controllare che non vengano effettuate modifiche
    private int numeroModifiche;

    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
        //inizializzazione delle variabili di istanza
        this.map = new HashMap<>();
        this.size = 0;
        this.numeroModifiche = 0;
    }
    /*
     * Classe che realizza un iteratore per MyMultiset.
     * L'iteratore è fail-fast, cioè lancia una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la lista è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     *
     * La classe è non-static perché l'oggetto iteratore, per funzionare
     * correttamente, ha bisogno di accedere ai campi dell'oggetto della classe
     * principale presso cui è stato creato.
     */
    private class MultiSetIterator implements Iterator<E> {
        //istanzia una Map.Entry che serve a mantenere una coppia di valori
        //variabile che ci servirà per contenere una copia della coppia di valori
        //attualmente in "uso"
        private Map.Entry<E, Integer> currentEntry;
        //istanzia un iteratore dello stesso tipo della currentEntry
        private final Iterator<Map.Entry<E, Integer>> entryIterator;
        //usiamo questa variabile per tenere traccia di quante occorrenze di quell'oggetto ci sono
        private int count;
        //usiamo questa variabile per verificare che non siano state effettuate modifiche durante l'iterazione
        private final int numeroModificheAtteso;

        public MultiSetIterator() {
            //inizializza questa variabile come l'iteratore sulla entry della mappa
            this.entryIterator = MyMultiset.this.map.entrySet().iterator();
            //inizializza questa variabile con le attuali modifiche fatte finora al set
            this.numeroModificheAtteso = MyMultiset.this.numeroModifiche;
        }

        @Override
        public boolean hasNext() {
            //verifichiamo sia che abbiamo finito gli oggetti di quel tipo grazie a count
            //sia che siano finiti effettivamente tutti gli oggetti
            return this.count > 0 || this.entryIterator.hasNext();
        }

        @Override
        public E next() {
            //se è stata effettuata una modifica
            if(this.numeroModificheAtteso != MyMultiset.this.numeroModifiche)
                throw new ConcurrentModificationException("Lista modificata durante l'iterazione");
            if(!hasNext())
                throw new NoSuchElementException("Elementi finiti");
            //caso in cui abbiamo finito gli oggetti del tipo precedente
            //o è la prima iterazione
            if (count == 0) {
                //assegna al valore della coppia (Entry) una coppia della Set
                this.currentEntry = this.entryIterator.next();
                //prende il numero di occorrenze dell'oggetto
                this.count = this.currentEntry.getValue();
            }
            //decrementa il contatore delle occorrenze dell'oggetto
            this.count--;
            //ritorna l'oggetto
            return this.currentEntry.getKey();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operazione non supportata");
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int count(Object element) {
        //controllo dei parametri passati
        if(element == null)
            throw new NullPointerException("L'argomento non può essere null");
        //Questa funzione ritorna o il numero di occorrenze
        //di quell'elemento se presente o un numero di default se non presente.
        //Se l'oggetto non è contenuto le occorrenze sono 0
        //perché la scelta progettuale è quella di NON contenere un oggetto
        //se le sue occorrenze sono 0
        return this.map.getOrDefault((E)element, 0);
    }

    @Override
    public int add(E element, int occurrences) {
        //controllo se i parametri passati rispettano le regole
        //uso il metodo count di questa classe per sapere quante occorrenze ci sono
        //di quell'elemento ricordando che 0 == l'elemento non è fisicamente presente
        long longCount = this.count(element);
        if (occurrences < 0 || (longCount + occurrences > Integer.MAX_VALUE))
            throw new IllegalArgumentException("Argomenti non validi");
        if(element == null)
            throw new NullPointerException("L'argomento non può essere null");
        //fa il downcast per occupare meno memoria tanto siamo sicuri che
        //sia entro i limiti di int
        int intCount = (int) longCount;
        //caso in cui non si deve aggiungere niente
        if(occurrences == 0)
            return intCount;
        //se count è diverso da 0 il che vuol dire che c'è già una key
        if(intCount != 0)
            map.put(element, intCount + occurrences);
        //caso in cui non c'erano occorrenze di quell'elemento
        //quindi il count era 0
        else
            map.put(element, occurrences);
        //aumento il contatore di quanti elementi sono stati passati
        this.size += occurrences;
        //aumento il contatore del numero modifiche del MultiSet
        this.numeroModifiche++;
        //ritorna le precedenti occorrenze dell'oggetto passato
        return intCount;
    }

    @Override
    public void add(E element) {
        //contro quante volte l'elemento è presente nella lista
        long occurences = this.count(element);
        //se le occorrenze di quell'elemento + 1 superano il massimo valore per gli int
        if(occurences + 1 > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Argomento non valido");
        //controllo se i parametri passati rispettano le regole
        if(element == null)
            throw new NullPointerException("L'argomento non può essere null");
        //se l'elemento c'era aggiorna il suo contatore di 1, altrimenti lo imposta a 1 (0 + 1)
        map.put(element, map.getOrDefault(element, 0) + 1);
        //aumento il contatore del numero modifiche del MultiSet
        this.numeroModifiche++;
        //aumento il contatore del MultiSet
        this.size++;
    }

    @Override
    public int remove(Object element, int occurrences) {
        //controllo i parametri
        if(occurrences < 0)
            throw new IllegalArgumentException("Argomento non valido");
        if(element == null)
            throw new NullPointerException("L'argomento non può essere null");
        //se l'oggetto passato non era contenuto
        if(!(this.contains(element)))
            return 0;
        //conto le occorrenze dell'elemento passato ricordando che se è 0
        //vuol dire che l'elemento non è presente nella mappa
        int count = this.count(element);
        //se le occorrenze da cancellare passate sono 0
        if(occurrences == 0)
            return count;
        //caso in cui le occorrenze presenti sono minori del numero da rimuovere
        if(count < occurrences) {
            map.put((E) element, 0);
            this.size -= count;
        }
        //caso in cui le occorrenze presenti sono maggiori del numero da rimuovere
        else if(count > occurrences){
            map.put((E) element, count - occurrences);
            this.size -= occurrences;
        }
        //caso in cui le occorrenze da rimuovere sono uguali alle occorrenze presenti
        else{
            map.remove((E) element);
            this.size -= occurrences;
        }
        //aumento il contatore del numero modifiche del MultiSet
        this.numeroModifiche++;
        //ritorna il numero di ricorrenze di quell'oggetto prima della rimozione
        return count;
    }

    @Override
    public boolean remove(Object element) {
        //Controllo i parametri
        if(element == null)
            throw new NullPointerException("L'argomento non può essere null");
        //se l'oggetto passato non era contenuto
        if(!(this.contains(element)))
            return false;
        //conto le occorrenze dell'elemento passato ricordando che se è 0
        //vuol dire che l'elemento non è presente nella mappa
        //in questo specifico caso controllando prima che l'elemento sia presente
        //ci assicuriamo che count sia != 0
        int count = this.count(element);
        //caso in cui levando quella occorrenza il numero delle occorrenze di
        //quell'oggetto diventi == 0
        if((count - 1) == 0){
            //rimuoviamo quell'elemento dalla mappa
            map.remove((E) element);
        }
        //in tutti gli altri casi
        else{
            //decrementiamo di uno le occorrenze di quell'elemento
            map.put((E) element, count - 1);
        }
        //decrementiamo di uno il contatore
        this.size--;
        //aumento il contatore del numero modifiche del MultiSet
        this.numeroModifiche++;
        return true ;
    }

    @Override
    public int setCount(E element, int count) {
        //controlli per i parametri passati
        if(count < 0)
            throw new IllegalArgumentException("Argomento non valido");
        if(element == null)
            throw new NullPointerException("L'argomento non può essere null");

        //calcolo le occorrenze dell'elemento ricordando che se sono 0
        //vuol dire che l'elemento NON è presente
        int mySetOccurences = this.count(element);
        //caso in cui il valore passato sia 0 e non ci sono occorrenze dell'elemento
        if(count == 0 && mySetOccurences == 0)
            return 0;
        //caso in cui il valore da settare di quell'oggetto sarebbe 0
        if(count == 0){
            //rimuoviamo l'elemento
            this.map.remove(element);
            //decrementiamo la size
            this.size -= mySetOccurences;
            //aumento il contatore del numero modifiche del MultiSet
            this.numeroModifiche++;
            //ritorniamo le precedenti ricorrenze
            return mySetOccurences;
        }
        //se l'elemento non era presente
        if(mySetOccurences == 0){
            //incrementa il contatore della grandezza del Multiset
            //del numero di occorrenze aggiunte
            this.size += count;
            //inserisco l'elemento nel set
            map.put(element, count);
            //aumento il contatore del numero modifiche del MultiSet
            this.numeroModifiche++;
        }
        //se l'elemento già c'era
        else{
            //se siamo nel caso in cui l'oggetto era presente nel set
            //e il numero degli oggetti presenti è diverso
            //dal numero di oggetti da impostare
            if(count != mySetOccurences){
                //rimuove il numero delle occorrenze precedente di quell'elemento
                //e aggiunge le occorrenze desiderate
                this.size += count - mySetOccurences;
                //aumento il contatore del numero modifiche del MultiSet
                this.numeroModifiche++;
                //aggiorna il value di quella key con quello passato
                map.put(element, count);
            }
        }
        //ritorniamo il numero di occorrenze precedenti al set
        return mySetOccurences;
    }

    @Override
    public Set<E> elementSet() {
        //usa il metodo della HashMap che ritorna un Set<E>
        //con tutte le chiavi della mappa, quindi nel nostro caso
        //gli oggetti (ma non il numero delle occorrenze)
        if(this.isEmpty())
            return new HashSet<>();
        return this.map.keySet();
    }

    @Override
    public Iterator<E> iterator() {
        return new MultiSetIterator();
    }

    @Override
    public boolean contains(Object element) {
        //controllo il parametro
        if(element == null)
            throw new NullPointerException("L'argomento non può essere null");
        //questo utilizzo presuppone che se le occorrenze
        //dell'elemento sono 0 questo non sia presente nell'insieme
        //quindi venga utilizzata la strategia del remove
        return this.map.containsKey((E)element);
    }

    @Override
    public void clear() {
        //chiama il metodo clear della mappa
        this.map.clear();
        //resetta la size del Set
        this.size = 0;
        //aumento il contatore del numero modifiche del MultiSet
        this.numeroModifiche++;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    /*
     * Due multinsiemi sono uguali se e solo se contengono esattamente gli
     * stessi elementi (utilizzando l'equals della classe E) con le stesse
     * molteplicità.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        /*
         * Questo equals() fa esattamente quello che farebbero
         * le due righe di codice qua sotto (in effetti è una variante)
         *
         * MyMultiset<?> other = (MyMultiset<?>) obj;
         * return this.map.equals(other)
         *
         * Quindi è in accordo con l'hashCode() di questa classe
         * definito sotto
         */

        //controllo parametro
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(!(obj instanceof MyMultiset<?>))
            return false;
        //cast dell'oggetto passato
        MyMultiset<?> other = (MyMultiset<?>) obj;
        //se entrambi i multiset sono vuoti
        if(this.size == 0 && other.size() == 0)
            return true;
        //crea gli iteratori per ognuno dei due MultiSet
        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = other.iterator();
        //confronta che elemento per elemento siano uguali
        while(thisIterator.hasNext() && otherIterator.hasNext()){
            E e1 = thisIterator.next();
            Object e2 = otherIterator.next();
            //se anche sono un elemento è sbagliato e non è esattamente dove dovrebbe essere
            if(!e1.equals(e2)){
                return false;
            }
        }
        //verifica se entrambe le liste sono terminate e ritorna il risultato
        return !(thisIterator.hasNext() || otherIterator.hasNext());
    }

    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.map.hashCode();
    }
}
