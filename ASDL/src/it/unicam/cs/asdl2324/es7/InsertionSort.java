package it.unicam.cs.asdl2324.es7;

import java.util.List;

/**
 * Implementazione dell'algoritmo di Insertion Sort integrata nel framework di
 * valutazione numerica. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 * @param <E>
 *                Una classe su cui sia definito un ordinamento naturale.
 */
public class InsertionSort<E extends Comparable<E>>
        implements SortingAlgorithm<E> {
    private int countCompare; //variabile d'istanza per il numero di compare effettuati dall'algoritmo
    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null) //se la lista passata è null
            throw new NullPointerException("Non può essere nullo");
        if(l.size() <= 1) //se la dimensione della lista è <= 1
            //ritorno una soluzione con la stessa lista e 0 come numero di compare
            return new SortingAlgorithmResult<>(l, 0);
        this.countCompare = 0; //inizializzo il contatore dei compare
        this.insertionSort(l); //chiamo il metodo per il sorting della lista
        //ritorno l'oggetto risultato con i parametri calcolati
        return new SortingAlgorithmResult<>(l, countCompare);
    }

    public String getName() {
        return "InsertionSort";
    }

    private void insertionSort(List<E> list){
        int i; //dichiaro una variabile che verrà usata come indice della lista
        //scorro lungo la lista a partire dal secondo elemento fino all'ultimo
        for(int j = 1; j < list.size(); j++){
            E key = list.get(j); //assegno l'j-esimo oggetto della lista a una variabile
            i = j - 1; //i punta l'oggetto precedente di quello a cui punta j nella lista
            //faccio il compare tra l'i-esimo oggetto (cioè quello prima di j)
            //con la key di prima (che punta l'oggetto successivo di i quindi j)
            while(i >= 0 && list.get(i).compareTo(key) > 0){
                this.countCompare++; //incremento il contatore dei compare
                list.set(i + 1, list.get(i)); //scambio l'elemento i precedente con il suo successivo
                i--; //decremento il contatore i
            }
            list.set(i + 1, key); //assegno all'(i + 1) elemento il valore della variabile key
        }
    }
}
