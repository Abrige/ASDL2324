/**
 * 
 */
package it.unicam.cs.asdl2324.es7;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {
    private int countCompare; //variabile d'istanza per il numero di compare effettuati dall'algoritmo
    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null) //se la lista passata è null
            throw new NullPointerException("Non può essere null");
        if(l.size() <= 1) //se la dimensione della lista è <= 1
            //ritorno una soluzione con la stessa lista e 0 come numero di compare
            return new SortingAlgorithmResult<>(l, 0);
        countCompare = 0; //inizializzo il contatore dei compare
        this.mergeSort(l); //chiamo il metodo per il sorting della lista
        //ritorno l'oggetto risultato con i parametri calcolati
        return new SortingAlgorithmResult<>(l, this.countCompare);
    }

    public String getName() {
        return "MergeSort";
    }

    private void mergeSort(List<E> list){
        int listSize = list.size(); //contiene la dimensione dell'array da ordinare

        //caso base --> se la lista ha dimensione <= di 1
        //(cioè se siamo arrivati nel punto in cui c'è un solo elemento nella lista)
        if(listSize <= 1)
            return;

        int mid = listSize / 2; //variabile che tiene conto dell'indice della mediana dell'array

        //creo il primo sottoarray, quello di sinistra
        ArrayList<E> leftHalf = new ArrayList<E>(list.subList(0,mid)); //mid è escluso
        //creo il secondo sottoarray, quello di destra
        ArrayList<E> rightHalf = new ArrayList<E>(list.subList(mid, listSize)); //listSize è escluso

        mergeSort(leftHalf); //chiamo ricorsivamente il metodo sulla prima metà del sottoarray creato
        mergeSort(rightHalf); //chiamo ricorsivamente il metodo sulla seconda metà del sottoarray creato

        merge(list, leftHalf, rightHalf);

    }

    private void merge(List<E> list, List<E> leftHalf, List<E> rightHalf){
        //variabili che contengono la dimensione degli array di destra e sinistra
        int leftSize = leftHalf.size();
        int rightSize = rightHalf.size();

        int i = 0, j = 0, k = 0; //inizializzo i contatori

        while(i < leftSize && j < rightSize){
            this.countCompare++; //incremento il contatore dei compare
            E leftElementI = leftHalf.get(i); //assegno ad una variabile l'i-esimo elemento della lista di sinistra
            E rightElementJ = rightHalf.get(j); //assegno ad una variabile l'i-esimo elemento della lista di sinistra
            //se l'i-esimo elemento della lista di sinistra è più piccolo o uguale
            //del j-esimo elemento della lista di destra
            if(leftElementI.compareTo(rightElementJ) <= 0){
                list.set(k, leftElementI); //inserisco i-esimo elemento del sottoarray di sinistra nel super array
                i++; //aumento il contatore dell'array di sinistra
            }
            else{ //se invece l'elemento di sinistra è più grande
                list.set(k, rightElementJ); //inserisco j-esimo elemento del sottoarray di destra nel super array
                j++; //aumento il contatore dell'array di destra
            }
            k++; //aumento il contatore dell'array che conterrà entrambi gli array
        }

        //ci sono rimasti due casi da controllare,
        //quando un array ha meno elementi dell'altro dobbiamo aggiungere
        //anche l'ultmo elemento escluso ovviamente, perciò popoliamo
        //il vettore finale con gli elementi rimasti dell'array
        //che ancora non si è svuotato
        while(i < leftSize){
            list.set(k, leftHalf.get(i)); //inserisco i-esimo elemento del sottoarray di sinistra nel super array
            i++; //aumento il contatore dell'array di sinistra
            k++; //aumento il contatore dell'array che conterrà entrambi gli array
        }
        while(j < rightSize){
            list.set(k, rightHalf.get(j)); //inserisco j-esimo elemento del sottoarray di destra nel super array
            j++; //aumento il contatore dell'array di destra
            k++; //aumento il contatore dell'array che conterrà entrambi gli array
        }
    }
}
