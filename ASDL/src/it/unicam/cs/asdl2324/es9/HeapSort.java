/**
 * 
 */
package it.unicam.cs.asdl2324.es9;

import java.util.List;

/**
 * Classe che implementa un algoritmo di ordinamento basato su heap.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 */
public class HeapSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    private int numeroConfronti;
    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        // Nota: usare una variante dei metodi della classe
        // MaxHeap in modo da implementare l'algoritmo utilizzando solo un array
        // (arraylist) e alcune variabili locali di appoggio (implementazione
        // cosiddetta "in loco" o "in place", si veda
        // https://it.wikipedia.org/wiki/Algoritmo_in_loco)

        //controllo parametri
        if(l == null)
            throw new NullPointerException("Parametro passato nullo");
        if(l.isEmpty())
            return new SortingAlgorithmResult<E>(l, 0);

        this.numeroConfronti = 0; //inizializza la variabile
        this.heapSort(l); //chiama l'heapSort in loco sulla lista
        return new SortingAlgorithmResult<E>(l, numeroConfronti); //ritorna un nuovo oggetto risultato
    }

    /*
     * Ordina la lista in loco tramite l'heapSort
     */
    private void heapSort(List<E> list){
        this.buildMaxHeap(list); //crea un MaxHeap dalla lista data in loco
        //scorre lungo tutta la lista diventata un heap
        for(int i = list.size() - 1; i >= 0; i--){
            this.numeroConfronti++;
            this.swap(list, i, 0); //scambia il primo elemento con i
            this.heapify(list, 0, i); //heapizza dalla root fino a i perchè nelle ultime posizione verrano collocati gli elementi ordinati
        }
    }
    /*
     * Data una lista la trasforma in un maxHeap in loco
     */
    private void buildMaxHeap(List<E> list){
        for(int i = (list.size() / 2) - 1; i >= 0; i--){
            this.heapify(list, i, list.size());
            this.numeroConfronti++;
        }
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(List<E> list, int i, int size) {
        int left = this.leftIndex(i); //calcola il figlio sinistro di i
        int right = this.rightIndex(i); //calcola il figlio destro di i
        int largest = i; //imposta i come l'elemento maggiore (per ora)

        //se il figlio sinistro di i ha un indice minore dell'indice massimo dell'array (sarebbe size - 1)
        //il che vuol dire se il figlio sinistro esiste ed è maggiore del suo parent
        if(left < size && list.get(left).compareTo(list.get(i)) > 0){
            largest = left; //l'indice dell'elemento maggiore diventa l'indice del figlio sinistro
        }
        //se il figlio destro di i ha un indice minore dell'indice massimo dell'array (sarebbe size - 1)
        //il che vuol dire se il figlio destro esiste ed è maggiore del suo parent
        if(right < size && list.get(right).compareTo(list.get(largest)) > 0){
            largest = right; //l'indice dell'elemento maggiore diventa l'indice del figlio destro
        }
        //se siamo nel caso in cui un figlio di i era maggiore
        if(largest != i){
            this.swap(list, i, largest); //scambia l'elemento maggiore e lo mette al posto di i
            this.heapify(list, largest, size); //chiama ricorsivamente l'heapify sul nodo che ha ricevuto lo scambio con il padre
        }
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
     * Metodo che dati due indici della lista
     * scambia gli oggetti a quegli indici.
     */
    private void swap(List<E> list, int i, int j) {
        E temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    @Override
    public String getName() {
        return "HeapSort";
    }

}
