package it.unicam.cs.asdl2324.es6;

import java.util.*;

/**
 * Lista concatenata singola che non accetta valori null, ma permette elementi
 * duplicati. Le seguenti operazioni non sono supportate:
 * 
 * <ul>
 * <li>ListIterator<E> listIterator()</li>
 * <li>ListIterator<E> listIterator(int index)</li>
 * <li>List<E> subList(int fromIndex, int toIndex)</li>
 * <li>T[] toArray(T[] a)</li>
 * <li>boolean containsAll(Collection<?> c)</li>
 * <li>addAll(Collection<? extends E> c)</li>
 * <li>boolean addAll(int index, Collection<? extends E> c)</li>
 * <li>boolean removeAll(Collection<?> c)</li>
 * <li>boolean retainAll(Collection<?> c)</li>
 * </ul>
 * 
 * L'iteratore restituito dal metodo {@code Iterator<E> iterator()} è fail-fast,
 * cioè se c'è una modifica strutturale alla lista durante l'uso dell'iteratore
 * allora lancia una {@code ConcurrentMopdificationException} appena possibile,
 * cioè alla prima chiamata del metodo {@code next()}.
 * 
 * @author Luca Tesei
 *
 * @param <E>
 *                il tipo degli elementi della lista
 */
public class SingleLinkedList<E> implements List<E> {

    private int size;

    private Node<E> head;

    private Node<E> tail;

    private int numeroModifiche;

    /**
     * Crea una lista vuota.
     */
    public SingleLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
        this.numeroModifiche = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. E' dichiarata static perché
     * gli oggetti della classe Node<E> non hanno bisogno di accedere ai campi
     * della classe principale per funzionare.
     */

    //è static perché non ci serve tenere conto di quale oggetto
    //single linked list ha creato questo nodo
    private static class Node<E> {
        private E item; //puntatore all'oggetto della lista, cioè puntatore del nodo all'interno della lista

        private Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }

    }

    /*
     * Classe che realizza un iteratore per SingleLinkedList.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la lista è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     * 
     * La classe è non-static perché l'oggetto iteratore, per funzionare
     * correttamente, ha bisogno di accedere ai campi dell'oggetto della classe
     * principale presso cui è stato creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;

        private int numeroModificheAtteso;

        private Itr() {
            // All'inizio non è stato fatto nessun next
            this.lastReturned = null;
            this.numeroModificheAtteso = SingleLinkedList.this.numeroModifiche;
        }

        @Override
        public boolean hasNext() {
            if (this.lastReturned == null)
                // sono all'inizio dell'iterazione
                return SingleLinkedList.this.head != null;
            else
                // almeno un next è stato fatto
                return lastReturned.next != null;

        }

        @Override
        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != SingleLinkedList.this.numeroModifiche) {
                throw new ConcurrentModificationException(
                        "Lista modificata durante l'iterazione");
            }
            // controllo hasNext()
            if (!hasNext())
                throw new NoSuchElementException(
                        "Richiesta di next quando hasNext è falso");
            // c'è sicuramente un elemento di cui fare next
            // aggiorno lastReturned e restituisco l'elemento next
            if (this.lastReturned == null) {
                // sono all’inizio e la lista non è vuota
                this.lastReturned = SingleLinkedList.this.head;
                return SingleLinkedList.this.head.item;
            } else {
                // non sono all’inizio, ma c’è ancora qualcuno
                lastReturned = lastReturned.next;
                return lastReturned.item;
            }

        }

    }

    /*
     * Una lista concatenata è uguale a un'altra lista se questa è una lista
     * concatenata e contiene gli stessi elementi nello stesso ordine.
     * 
     * Si noti che si poteva anche ridefinire il metodo equals in modo da
     * accettare qualsiasi oggetto che implementi List<E> senza richiedere che
     * sia un oggetto di questa classe:
     * 
     * obj instanceof List
     * 
     * In quel caso si può fare il cast a List<?>:
     * 
     * List<?> other = (List<?>) obj;
     * 
     * e usando l'iteratore si possono tranquillamente controllare tutti gli
     * elementi (come è stato fatto anche qui):
     * 
     * Iterator<E> thisIterator = this.iterator();
     * 
     * Iterator<?> otherIterator = other.iterator();
     * 
     * ...
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof SingleLinkedList))
            return false;
        SingleLinkedList<?> other = (SingleLinkedList<?>) obj;
        // Controllo se entrambe liste vuote
        if (head == null) {
            if (other.head != null)
                return false;
            else
                return true;
        }
        // Liste non vuote, scorro gli elementi di entrambe
        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = other.iterator();
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            E o1 = thisIterator.next();
            // uso il polimorfismo di Object perché non conosco il tipo ?
            Object o2 = otherIterator.next();
            // il metodo equals che si usa è quello della classe E
            if (!o1.equals(o2))
                return false;
        }
        // Controllo che entrambe le liste siano terminate
        return !(thisIterator.hasNext() || otherIterator.hasNext());
    }

    /*
     * L'hashcode è calcolato usando gli hashcode di tutti gli elementi della
     * lista.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        // implicitamente, col for-each, uso l'iterator di questa classe
        for (E e : this)
            hashCode = 31 * hashCode + e.hashCode();
        return hashCode;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        //se l'oggetto passato è nullo
        if(o == null)
            throw new NullPointerException("I parametri passati non possono essere null");
        //creo un nuovo iteratore
        Iterator<E> iterator = this.iterator();
        //creo un nuovo oggetto di appoggio
        E e1;
        //itero nella linked list
        while(iterator.hasNext()){
            //assegno l'attuale oggetto all'oggetto di appoggio
            e1 = iterator.next();
            //uso l'equals definito all'interno di SingleLinkedList e non di object
            //che fa direttamente dentro il cast dell'oggetto ecc...
            if(e1.equals(o))
                return true;
        }
        return false;
    }

    @Override
    public boolean add(E e) {
        if(e == null)
            throw new NullPointerException("I parametri passati non possono essre null");
        //creo un nuovo nodo con il valore pasasto come parametro
        Node<E> n1 = new Node<>(e, null);
        //controllo se la lista è vuota
        if(this.isEmpty()){
            this.head = n1;
            this.tail = n1;
        }
        //tutti gli altri casi (lo aggiungo alla fine)
        else {
            this.tail.next = n1;
            this.tail = n1;
        }
        this.numeroModifiche++;
        this.size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        //se l'oggetto passato è null
        if(o == null)
            throw new NullPointerException("I parametri passati non possono essere null");
        //se la lista è vuota
        if(size == 0)
            return false;
        //creo due nodi di copia della lista
        Node<E> predecessore = null;
        Node<E> attuale = this.head;
        //creo un iteratore
        Iterator<E> iterator = this.iterator();
        //creo una variabile di appoggio
        E e1;
        //scorro lungo la lista
        while(iterator.hasNext()){
            e1 = iterator.next();
            //se la lista contiene quell'oggetto
            if(e1.equals(o)){
                //se la lista contiene solo l'elemento da rimuovere
                if(predecessore == null && attuale.next == null){
                    this.head = null;
                    this.tail = null;
                }//se l'oggetto da rimuovere è il primo della lista
                else if(predecessore == null && attuale.next != null){
                    this.head = attuale.next;
                }//se l'oggetto da rimuovere è l'ultimo
                else if(attuale.next == null){
                    predecessore.next = null;
                    this.tail = predecessore;
                }//tutti gli altri casi (ad esempio quando sta in mezzo alla lista)
                else{
                    predecessore.next = attuale.next;
                }
                this.size--; //decremento l'ampiezza della lista
                return true;
            }
            predecessore = attuale;
            attuale = attuale.next;
        }
        return false;
    }

    @Override
    public void clear() {
        //creo un iteratore
        Iterator<E> iterator = this.iterator();
        //scorro lungo la lista
        while(iterator.hasNext()){
            //chiamo il metodo remove(Object o)
            this.remove(iterator.next());
        }
    }

    @Override
    public E get(int index) {
        //controllo se l'indice passato è valido
        if(index >= size || index < 0)
            throw new IndexOutOfBoundsException("Indice non valido");
        int count = index; //copia del parametro index
        Iterator<E> iterator = this.iterator(); //creo un iteratore
        E e1; //variabile di appoggio
        //scorro lungo la lista
        while(iterator.hasNext()){
            //assegno il valore attuale alla variabile di appoggio
            e1 = iterator.next();
            //se il contatore è arrivato a 0 vuol dire che abbiamo
            //trovato l'indice dell'elemento che cercavamo quindi lo ritorno
            if(count == 0){
                return e1;
            }
            count--; //decremento il contatore
        }
        return null;
    }

    @Override
    public E set(int index, E element) {
        //controllo se l'indice passato è valido
        if(index >= size || index < 0)
            throw new IndexOutOfBoundsException("Indice non valido");
        //se l'oggetto passato è null
        if(element == null)
            throw new NullPointerException("I parametri passati non possono essere null");
        E removed = this.remove(index); //inserisco il risultato di remove a l'index passato
        this.add(index, element); //aggiungo l'elemento a quell'index
        return removed; //ritorno l'elemento che c'era prima in quella posizione
    }

    @Override
    public void add(int index, E element) {
        if(element == null)
            throw new NullPointerException("null parameter");
        Node<E> precedente = null;
        Node<E> attuale = this.head;
        if(index < 0 || attuale == null)
            throw new IndexOutOfBoundsException("null");
        Node <E> n1 = new Node<E>(element, null);
        int count = index;
        //vogliamo aggiungere il nodo nell'ultimo slot
        if(index >= size){
            tail.next = n1;
            tail = n1;
        }
        Iterator<E> iterator = this.iterator();
        //scorro lungo la lista
        while(iterator.hasNext()){
            iterator.next();
            if(count == 0) {
                //voglio aggiungere il nodo nel primo slot
                if(precedente == null){
                    n1.next = head;
                    this.head = n1;
                }else{
                    precedente.next = n1;
                    n1.next = attuale;
                }
                //numeroModifiche potrebbe dare errore!
                numeroModifiche++;
                size++;
                return;
            }
            precedente = attuale;
            attuale = attuale.next;
            count--;
        }
    }

    @Override
    public E remove(int index) {
        if(index >= size || index < 0)
            throw new IndexOutOfBoundsException("Indice non valido");
        Iterator<E> iterator = this.iterator();
        E q1;
        //scorro lungo la lista
        while(iterator.hasNext()){
            q1 = iterator.next();
            //se l'indice è uguale a 0, rimuovo il nodo
            if(index == 0){
                this.remove(q1);
                return q1;
            }
            index--;
        }
        return null;
    }

    @Override
    public int indexOf(Object o) {
        //se l'oggetto è null
        if(o == null)
            throw new NullPointerException("I parametri passati non possono essere null");
        int  count = 0;
        Iterator<E> iterator = this.iterator();
        E q1;
        //scorro lungo la lista
        while(iterator.hasNext()) {
            q1 = iterator.next();
            if(q1.equals(o))
                return count;
            count++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o == null)
            throw new NullPointerException("I parametri passati non possono essere null");
        int lastIndex = -1; // Inizializzo l'indice con un valore che rappresenta l'assenza dell'oggetto
        int count = 0;
        Iterator<E> iterator = this.iterator();
        E q1;
        //scorro lungo la lista
        while(iterator.hasNext()) {
            q1 = iterator.next();
            if(q1.equals(o)){
                lastIndex = count;
            }
            count++;
        }
        return lastIndex;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size]; // Crea un array con la dimensione della lista

        Node<E> attuale = head;
        int index = 0;

        while (attuale != null) {
            array[index] = attuale.item; // Copia l'elemento corrente nella posizione corrispondente dell'array
            attuale = attuale.next;
            index++;
        }
        return array;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }
}
