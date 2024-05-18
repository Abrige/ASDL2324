/**
 * 
 */
package it.unicam.cs.asdl2324.es10;

import java.util.*;

/**
 * Realizza un insieme tramite una tabella hash con indirizzamento primario (la
 * funzione di hash primario deve essere passata come parametro nel costruttore
 * e deve implementare l'interface PrimaryHashFunction) e liste di collisione.
 * 
 * La tabella, poiché implementa l'interfaccia Set<E> non accetta elementi
 * duplicati (individuati tramite il metodo equals() che si assume sia
 * opportunamente ridefinito nella classe E) e non accetta elementi null.
 * 
 * La tabella ha una dimensione iniziale di default (16) e un fattore di
 * caricamento di defaut (0.75). Quando il fattore di bilanciamento effettivo
 * eccede quello di default la tabella viene raddoppiata e viene fatto un
 * riposizionamento di tutti gli elementi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class CollisionListResizableHashTable<E> implements Set<E> {

    /*
     * La capacità iniziale. E' una potenza di due e quindi la capacità sarà
     * sempre una potenza di due, in quanto ogni resize raddoppia la tabella.
     */
    private static final int INITIAL_CAPACITY = 16;

    /*
     * Fattore di bilanciamento di default. Tipico valore.
     */
    private static final double LOAD_FACTOR = 0.75;

    /*
     * Numero di elementi effettivamente presenti nella hash table in questo
     * momento. ATTENZIONE: questo valore è diverso dalla capacity, che è la
     * lunghezza attuale dell'array di Object che rappresenta la tabella.
     */
    private int size;

    /*
     * L'idea è che l'elemento in posizione i della tabella hash è un bucket che
     * contiene null oppure il puntatore al primo nodo di una lista concatenata
     * di elementi. Si può riprendere e adattare il proprio codice della
     * Esercitazione 6 che realizzava una lista concatenata di elementi
     * generici. La classe interna Node<E> è ripresa proprio da lì.
     * 
     * ATTENZIONE: la tabella hash vera e propria può essere solo un generico
     * array di Object e non di Node<E> per una impossibilità del compilatore di
     * accettare di creare array a runtime con un tipo generics. Ciò infatti
     * comporterebbe dei problemi nel sistema di check dei tipi Java che, a
     * run-time, potrebbe eseguire degli assegnamenti in violazione del tipo
     * effettivo della variabile. Quindi usiamo un array di Object che
     * riempiremo sempre con null o con puntatori a oggetti di tipo Node<E>.
     * 
     * Per inserire un elemento nella tabella possiamo usare il polimorfismo di
     * Object:
     * 
     * this.table[i] = new Node<E>(item, next);
     * 
     * ma quando dobbiamo prendere un elemento dalla tabella saremo costretti a
     * fare un cast esplicito:
     * 
     * Node<E> myNode = (Node<E>) this.table[i];
     * 
     * Ci sarà dato un warning di cast non controllato, ma possiamo eliminarlo
     * con un tag @SuppressWarning,
     */
    private Object[] table;

    /*
     * Funzione di hash primaria usata da questa hash table. Va inizializzata nel
     * costruttore all'atto di creazione dell'oggetto.
     */
    private final PrimaryHashFunction phf;

    /*
     * Contatore del numero di modifiche. Serve per rendere l'iterator
     * fail-fast.
     */
    private int modCount;

    // I due metodi seguenti sono di comodo per gestire la capacity e la soglia
    // oltre la quale bisogna fare il resize.

    /* Numero di elementi della tabella corrente */
    private int getCurrentCapacity() {
        return this.table.length;
    }

    /*
     * Valore corrente soglia oltre la quale si deve fare la resize,
     * getCurrentCapacity * LOAD_FACTOR
     */
    private int getCurrentThreshold() {
        return (int) (getCurrentCapacity() * LOAD_FACTOR);
    }

    /**
     * Costruisce una Hash Table con capacità iniziale di default e fattore di
     * caricamento di default.
     */
    public CollisionListResizableHashTable(PrimaryHashFunction phf) {
        this.phf = phf;
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente
         * 
         */

        //calcola l'hash dell'oggetto passato
        int objectHash = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        //se la cella è vuota, l'oggetto non è presente
        if(this.table[objectHash] == null)
            return false;
        //prende il nodo già presente in quella cella dell'array che rappresenta la testa della lista di collisione
        Node<E> current = (Node<E>) this.table[objectHash]; //cast dell'oggetto contenuto nella lista a nodo
        //scorre nella lista concatenata
        while(current != null){
            //se l'oggetto attuale è uguale all'oggetto passato
            if(current.item.equals(o))
                return true; //ferma il ciclo, oggetto trovato
            current = current.next; //prossimo oggetto nella lista concatenata
        }
        return false; //oggetto non trovato
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui inserire
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve inserire l'elemento o
         * nella lista concatenata lì presente. Se vuota, si crea la lista
         * concatenata e si inserisce l'elemento, che sarà l'unico.
         * 
         */
        // ATTENZIONE, si inserisca prima il nuovo elemento e poi si controlli
        // se bisogna fare resize(), cioè se this.size >
        // this.getCurrentThreshold()

        //se l'oggetto era già contenuto
        if(this.contains(e))
            return false;

        //calcola l'hash dell'oggetto passato
        int objectHash = this.phf.hash(e.hashCode(), this.getCurrentCapacity()); // sarebbe come fare --> h'(x) = (x, m)

        //caso in cui in quella posizione non c'era nessun altro elemento
        if(this.table[objectHash] == null) {
            this.table[objectHash] = new Node<>(e, null); //crea un nuovo nodo e lo inserisce nella cella adatta
        }
        //caso in cui c'era già un oggetto
        else{
            //prende il nodo già presente in quella cella dell'array che rappresenta la testa della lista di collisione
            Node<E> current = (Node<E>) this.table[objectHash]; //cast dell'oggetto contenuto nella lista a nodo
            //scorre fino a trovare l'ultimo nodo della lista di collisione
            while(current.next != null){
                current = current.next; //prossimo oggetto nella lista concatenata
            }
            //inserisce un nodo nella lista di collisione
            current.next = new Node<>(e, null); //inserisce un nuovo nodo in coda alla lista
        }
        this.size++; //aggiorna il numero di elementi presenti nella tabella hash
        //se la size è maggiore del fattore di caricamento
        if(this.size() > this.getCurrentThreshold()){
            this.resize(); //chiama il metodo interno resize per aumentare la dimensione dell'array
        }
        this.modCount++; //aggiorna il numero di modifiche
        return true;
    }

    /*
     * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da
     * chiamare quando this.size diventa maggiore di getCurrentThreshold()
     */
    private void resize() {
        this.table = Arrays.copyOf(this.table, this.getCurrentCapacity() * 2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente. Se presente, l'elemento deve essere
         * eliminato dalla lista concatenata
         * 
         */
        // ATTENZIONE: la rimozione, in questa implementazione, **non** comporta
        // mai una resize "al ribasso", cioè un dimezzamento della tabella se si
        // scende sotto il fattore di bilanciamento desiderato.

        //caso in cui l'oggetto non è presente
        if(!(this.contains(o)))
            return false;

        //caso in cui c'è almeno un elemento
        int objectHash = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        //caso in cui c'era già un oggetto
        Node<E> current = (Node<E>) this.table[objectHash]; //cast dell'oggetto nell'array
        Node<E> beforeCurrent = current; //oggetto precedente
        while(current.next != null){
            beforeCurrent = current; //oggetto precedente
            current = current.next; //prossimo oggetto nella lista concatenata
        }
        //elimina l'elemento
        beforeCurrent.next = null;
        this.size--; //aggiorna il numero di elementi presenti nella tabella hash
        this.modCount++; //aggiorna il numero di modifiche
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // utilizzare un iteratore della collection e chiamare il metodo
        // contains
        Iterator<?> collectionIterator = c.iterator(); //crea un iteratore per la collezione
        while(collectionIterator.hasNext()){
            //se l'oggetto non è contenuto
            if(!(this.contains(collectionIterator.next())))
                return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection<? extends E> c) {
        // utilizzare un iteratore della collection e chiamare il metodo add
        Iterator<?> collectionIterator = c.iterator();
        //scorre tutti gli oggetti della collection passata
        while(collectionIterator.hasNext()){
            Object current = collectionIterator.next();
            if(this.contains(current))
                continue;
            //se l'oggetto non è contenuto
            this.add((E) current); //aggiunge l'attuale oggetto della collezione alla mappa
        }
        this.modCount++; //aggiorna il numero di modifiche
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // utilizzare un iteratore della collection e chiamare il metodo remove
        Iterator<?> collectionIterator = c.iterator();
        //scorre tutti gli oggetti della collection passata
        while(collectionIterator.hasNext()){
            //se l'oggetto non è contenuto
            this.remove(collectionIterator.next()); //rimuove l'attuale oggetto della collezione alla mappa
        }
        this.modCount++; //aggiorna il numero di modifiche
        return true;
    }

    @Override
    public void clear() {
        // Ritorno alla situazione iniziale
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. Lo specificatore è protected
     * solo per permettere i test JUnit.
     */
    protected static class Node<E> {
        protected E item; //contenuto del nodo

        protected Node<E> next; //puntatore al prossimo nodo della lista

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    /*
     * Classe che realizza un iteratore per questa hash table. L'ordine in cui
     * vengono restituiti gli oggetti presenti non è rilevante, ma ogni oggetto
     * presente deve essere restituito dall'iteratore una e una sola volta.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la tabella è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned; //ultimo oggetto ritornato

        private final int numeroModificheAtteso; //numero di modifiche della tabella alla creazione dell'iteratore
        private int currentTableIndex; //indice attuale nella tabella

        private Itr() {
            // All'inizio non è stato fatto nessun next
            this.lastReturned = null;
            this.numeroModificheAtteso = modCount;
            this.currentTableIndex = 0;
        }

        @Override
        public boolean hasNext() {
            int index = this.currentTableIndex;
            int capacity = CollisionListResizableHashTable.this.getCurrentCapacity();
            //cicla incrementando l'indice della tabella per controllare se ci sono altri oggetti dopo il precedente
            while(index < capacity){
                //se troviamo una posizione nella mappa che ha ancora un oggetto
                if(CollisionListResizableHashTable.this.table[index] != null)
                    return true;
                index++; //incremento dell'indice
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            //se si è modificato qualcosa nella tabella durante l'iterazione
            if(this.numeroModificheAtteso != CollisionListResizableHashTable.this.modCount)
                throw new ConcurrentModificationException("Tabella modificata durante l'iterazione");
            //se non ci sono più oggetti
            if (!(this.hasNext()))
                throw new NoSuchElementException("Richiesta di next quando hasNext è falso");

            //se siamo arrivati alla fine della lista di collisione
            if(lastReturned == null || lastReturned.next == null) {
                Node<E> current;
                int capacity = CollisionListResizableHashTable.this.getCurrentCapacity();
                //scorre negli elementi della tabella
                while(this.currentTableIndex < capacity) {
                    //se in quella posizione c'è ancora un elemento
                    if(CollisionListResizableHashTable.this.table[currentTableIndex] != null) {
                        current = (Node<E>) CollisionListResizableHashTable.this.table[currentTableIndex];
                        lastReturned = current; //ritorna il puntatore di quell'elemento (che sarebbe il primo elemento della lista di collisione)
                    }
                    currentTableIndex++; //incrementa il contatore della tabella
                }
                return lastReturned.item; //ritorna l'oggetto contenuto dal nodo
            }
            //se l'ultimo nodo ha un next
            else{
                lastReturned = lastReturned.next; //prende il prossimo della lista
                return lastReturned.item; //ritorna l'oggetto contenuto dal nodo
            }
        }

    }

    /*
     * Only for JUnit testing purposes.
     */
    protected Object[] getTable() {
        return this.table;
    }

    /*
     * Only for JUnit testing purposes.
     */
    protected PrimaryHashFunction getPhf() {
        return this.phf;
    }

}
