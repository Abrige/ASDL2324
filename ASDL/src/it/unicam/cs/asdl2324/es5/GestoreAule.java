package it.unicam.cs.asdl2324.es5;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Un gestore di aule gestisce un insieme di aule e permette di cercare aule
 * libere con certe caratteristiche fra quelle che gestisce.
 * 
 * @author Luca Tesei
 *
 */
public class GestoreAule {

    private final Set<Aula> aule;

    /**
     * Crea un gestore vuoto.
     */
    public GestoreAule() {
        this.aule = new HashSet<Aula>();
    }

    /**
     * Aggiunge un'aula al gestore.
     * 
     * @param a
     *              una nuova aula
     * @return true se l'aula è stata aggiunta, false se era già presente.
     * @throws NullPointerException
     *                                  se l'aula passata è nulla
     */
    public boolean addAula(Aula a) {
        // TODO implementare
        //controllo che l'oggetto passato non sia nullo
        if(a == null)
            throw new NullPointerException("Non possono essere passati valori nulli");
        //usiamo il metodo add già definito nella classe HashSet
        return aule.add(a);
    }

    /**
     * @return the aule
     */
    public Set<Aula> getAule() {
        return aule;
    }

    /**
     * Cerca tutte le aule che soddisfano un certo insieme di facilities e che
     * siano libere in un time slot specificato.
     * 
     * @param requestedFacilities
     *                                insieme di facilities richieste che
     *                                un'aula deve soddisfare
     * @param ts
     *                                il time slot in cui un'aula deve essere
     *                                libera
     * 
     * @return l'insieme di tutte le aule gestite da questo gestore che
     *         soddisfano tutte le facilities richieste e sono libere nel time
     *         slot indicato. Se non ci sono aule che soddisfano i requisiti
     *         viene restituito un insieme vuoto.
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  passate è nulla
     */
    public Set<Aula> cercaAuleLibere (Set<Facility> requestedFacilities, TimeSlot ts) {
        // TODO implementare
        //controllo che l'oggetto passato non sia nullo
        if(requestedFacilities == null || ts == null)
            throw new NullPointerException("Non possono essere passati valori nulli");
        //creo un nuovo array di aule che soddisfano i requisiti come parametri
        //che verrà poi popolato con le aule che effettivamente li soddisfano
        HashSet<Aula> auleUtilizzabili = new HashSet<Aula>();
        //creo l'iteratore
        Iterator<Aula> iterator = this.aule.iterator();
        //creo un oggetto di tipo Aula
        Aula aula;
        //creo il loop
        while(iterator.hasNext()){
            //incremento l'iteratore e lo assegno all'oggetto aula
            aula = iterator.next();
            //controllo se l'aula in questione contiene tutte le facilities
            //richieste dall'array passato come parametro al metodo
            if(aula.getFacilities().containsAll(requestedFacilities))
                //controllo che l'aula in questione sia libera
                //nel TimeSlot inserito come parametro al metodo
                if(aula.isFree(ts))
                    //aggiungo l'aula in questione all'array di aule disponibili
                    //con le caratteristiche ricercate
                    auleUtilizzabili.add(aula);
        }
        //ritorno ll'array di aule utilizzabili
        return auleUtilizzabili;
    }

}
