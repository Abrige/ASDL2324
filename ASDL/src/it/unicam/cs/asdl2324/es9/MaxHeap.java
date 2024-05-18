package it.unicam.cs.asdl2324.es9;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che implementa uno heap binario che può contenere elementi non nulli
 * possibilmente ripetuti.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 * @param <E>
 *                il tipo degli elementi dello heap, che devono avere un
 *                ordinamento naturale.
 */
public class MaxHeap<E extends Comparable<E>> {

    /*
     * L'array che serve come base per lo heap
     */
    private ArrayList<E> heap;

    /**
     * Costruisce uno heap vuoto.
     */
    public MaxHeap() {
        this.heap = new ArrayList<E>();
    }

    /**
     * Costruisce uno heap a partire da una lista di elementi.
     *
     * @param list
     *                 lista di elementi
     * @throws NullPointerException
     *                                  se la lista è nulla
     */
    public MaxHeap(List<E> list){
        //controllo dei parametri
        if(list == null)
            throw new NullPointerException("Parametro passato nullo");
        this.heap = new ArrayList<E>(list); //copiamo l'array
        //cicliamo dal valore mezzano dell'array (arrotondato per difetto) fino al primo elemento
        for(int i = this.heap.size() / 2; i >= 0; i--){
            this.heapify(i); //chiamiamo l'heapify per ogni indice dell'array
        }
    }

    /**
     * Restituisce il numero di elementi nello heap.
     * 
     * @return il numero di elementi nello heap
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Determina se lo heap è vuoto.
     * 
     * @return true se lo heap è vuoto.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /**
     * Inserisce un elemento nello heap
     * 
     * @param el
     *               l'elemento da inserire
     * @throws NullPointerException
     *                                  se l'elemento è null
     * 
     */
    public void insert(E el) {
        //controlla i parametri
        if(el == null)
            throw new NullPointerException("Parametro passato nullo");
        //aggiunge l'elemento in coda all'heap
        this.heap.add(el);
        //indice dell'elemento appena aggiunto
        int i = this.size() - 1;
        //finchè l'indice i non è il primo elemento (root dell'heap) ed è più grande del suo parent
        while(i > 0 && this.heap.get(this.parentIndex(i)).compareTo(this.heap.get(i)) < 0){
            this.swap(i, parentIndex(i)); //scambia i con il suo parent
            i = parentIndex(i); //riassegno il nuovo indice di i
        }
    }
    /*
    * Metodo che dati due indici della lista
    * scambia gli oggetti a quegli indici.
    */
    private void swap(int i, int j) {
        E temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        return (2 * i) + 1;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int rightIndex(int i) {
        return (2 * i) + 2;
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int parentIndex(int i) {
        return (i - 1) / 2;
    }

    /**
     * Ritorna l'elemento massimo senza toglierlo.
     * 
     * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
     */
    public E getMax() {
        //se l'heap è vuoto ritorna null
        if(this.isEmpty())
            return null;
        //altrimento ritorna il primo oggetto dell'heap cioè nel caso di un MaxHeap il massimo
        return heap.get(0);
    }

    /**
     * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non
     * è più presente nello heap.
     * 
     * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
     */
    public E extractMax() {
        //se l'heap è vuoto ritorna null
        if(this.isEmpty())
            return null;
        //variabile temporanea che contiene il massimo dell'albero
        E max = this.getMax();
        //imposta come nuova root l'ultimo elemento dell'heap
        this.heap.set(0, this.heap.get(this.heap.size() - 1));
        //rimuove l'elemento che è diventato root dalla fine dell'heap
        this.heap.remove(this.heap.size() - 1);
        //chiama l'heapify dalla root
        this.heapify(0);
        return max; //ritorna il valore massimo calcolato prima
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(int i) {
        int left = this.leftIndex(i); //calcola il figlio sinistro di i
        int right = this.rightIndex(i); //calcola il figlio destro di i
        int largest = i; //imposta i come l'elemento maggiore (per ora)

        //se il figlio sinistro di i ha un indice minore dell'indice massimo dell'array (sarebbe size - 1)
        //il che vuol dire se il figlio sinistro esiste
        //ed è maggiore del suo parent
        if(left < this.size() && this.heap.get(left).compareTo(this.heap.get(i)) > 0){
            largest = left; //l'indice dell'elemento maggiore diventa l'indice del figlio sinistro
        }
        //se il figlio destro di i ha un indice minore dell'indice massimo dell'array (sarebbe size - 1)
        //il che vuol dire se il figlio destro esiste
        //ed è maggiore del suo parent
        if(right < this.size() && this.heap.get(right).compareTo(this.heap.get(largest)) > 0){
            largest = right; //l'indice dell'elemento maggiore diventa l'indice del figlio destro
        }
        //se siamo nel caso in cui un figlio di i era maggiore
        if(largest != i){
            this.swap(i, largest); //scambia l'elemento maggiore e lo mette al posto di i
            this.heapify(largest); //chiama ricorsivamente l'heapify
        }
    }
    
    /**
     * Only for JUnit testing purposes.
     * 
     * @return the arraylist representing this max heap
     */
    protected ArrayList<E> getHeap() {
        return this.heap;
    }
}
