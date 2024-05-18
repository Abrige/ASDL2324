/**
 * 
 */
package it.unicam.cs.asdl2324.es7;

import java.util.List;

/**
 * Implementazione del QuickSort con scelta della posizione del pivot fissa.
 * L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    private int countCompare; //variabile d'istanza per il numero di compare effettuati dall'algoritmo

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null) //se la lista passata è null
            throw new NullPointerException("Non può essere nullo");
        if(l.size() <= 1) //se la dimensione della lista è <= 1
            //ritorno una soluzione con la stessa lista e 0 come numero di compare
            return new SortingAlgorithmResult<>(l, 0);
        this.countCompare = 0; //inizializzo il contatore dei compare
        this.quickSort(l, 0 , l.size()-1); //chiamo il quickSort sulla lista
        return new SortingAlgorithmResult<>(l, countCompare); //ritorno l'oggetto soluzione con la lista ordinata
    }

    @Override
    public String getName() {
        return "QuickSort";
    }

    private void quickSort(List<E> list, int lowIndex, int highIndex) {
        //caso base in cui c'è solo un elemento o nessuno
        if(lowIndex >= highIndex)
            return;

        //come pivot prendiamo l'ultimo elemento dell'array
        E pivot = list.get(highIndex);

        //partition della lista che ritorna l'indice di dove viene posizionato il pivot nella lista
        int leftPointer = partition(list, lowIndex, highIndex, pivot);

        //chiamata ricorsiva sulle due liste (la maggiore e la minore del pivot)
        this.quickSort(list, lowIndex, leftPointer - 1);
        this.quickSort(list, leftPointer + 1, highIndex);
    }
    private int partition(List<E> list, int lowIndex, int highIndex, E pivot) {
        //variabili che useremo come indici della lista
        int leftPointer = lowIndex;
        int rightPointer = highIndex;

        //cicla finchè il puntatore che parte da sinistra è où piccolo di quello che parte da destra
        while(leftPointer < rightPointer){
            this.countCompare++;
            //se list[leftPointer] <= pivot && leftPointer < rightPointer allora incrementa il puntatore
            //il che vuol dire che se l'indice di sinistra punta ad elementi più piccoli del pivot li lascia li dove sono
            while(list.get(leftPointer).compareTo(pivot) <= 0 && leftPointer < rightPointer){
                leftPointer++;
            }
            //se list[rightPointer] >= pivot && leftPointer > rightPointer allora decremente il puntatore
            //il che vuol dire che se l'indice di destra punta ad elementi più grandi del pivot li lascia li dove sono
            while(list.get(rightPointer).compareTo(pivot) >= 0 && leftPointer < rightPointer){
                rightPointer--;
            }
            //faccio lo swap tra l'elemento a sinistra e quello di destra
            //quando trovo un elemento nella posizione sbagliata lo scambio e lo metto nella lista corretta
            this.swap(list, leftPointer, rightPointer);
        }
        //faccio lo swap tra l'elemento a sinistra e l'ultimo di destra
        //cioè posiziono il pivot al suo posto finale nella lista
        this.swap(list, leftPointer, highIndex);
        //ritorno l'indice della lista che punta al posto del pivot
        return leftPointer;
    }

    //creo un metodo per scambiare la posizione di due oggetti nella lista
    private void swap(List<E> list, int index1, int index2){
        //creo una variabile temporanea per uno dei due oggetti da scambiare
        E temp = list.get(index1);
        //scambio gli oggetti
        list.set(index1, list.get(index2));
        list.set(index2, temp);
    }
}
