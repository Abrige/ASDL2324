package it.unicam.cs.asdl2324.es5;

import java.util.*;


/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 */
public class Aula implements Comparable<Aula> {
    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    // Insieme delle facilities di quest'aula
    private final Set<Facility> facilities;

    // Insieme delle prenotazioni per quest'aula, segue l'ordinamento naturale
    // delle prenotazioni
    private final SortedSet<Prenotazione> prenotazioni;

    /**
     * Costruisce una certa aula con nome e location. Il set delle facilities è
     * vuoto. L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                     il nome dell'aula
     * @param location
     *                     la location dell'aula
     * 
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location) {
        //controllo che i valori passati non siano nulli
        if(nome == null || location == null)
            throw new NullPointerException("Non possono essere passati valori nulli");
        //inizializzo tutte le variabili di istanza
        this.nome = nome;
        this.location = location;
        this.facilities = new HashSet<Facility>();
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /**
     * Costruisce una certa aula con nome, location e insieme delle facilities.
     * L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                       il nome dell'aula
     * @param location
     *                       la location dell'aula
     * @param facilities
     *                       l'insieme delle facilities dell'aula
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location, Set<Facility> facilities) {
        //controllo che i valori passati non siano nulli
        if(nome == null || location == null || facilities == null)
            throw new NullPointerException("Non possono essere passati valori nulli");
        this.nome = nome;
        this.location = location;
        this.facilities = facilities;
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /*
     * Ridefinire in accordo con equals
     */
    @Override
    public int hashCode() {
        //ritorno l'hash code delle stringhe già implementato
        //nella classe String
        return nome.hashCode();
    }

    /* Due aule sono uguali se e solo se hanno lo stesso nome */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Aula))
            return false;
        //faccio il cast dell'oggetto passato come argomento
        Aula aula = (Aula) obj;
        //utilizzo il metodo equals già presente nell'oggetto String
        //per verificare l'uguaglianza di due aule che si basa solamente sullo stesso nome
        return this.nome.equals(aula.nome);
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        //ritorno il compare tra stringhe già implementato
        //nella classe String
        return this.nome.compareTo(o.nome);
    }

    /**
     * @return the facilities
     */
    public Set<Facility> getFacilities() {
        return facilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the prenotazioni
     */
    public SortedSet<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        //controllo che l'oggetto passato non sia nullo
        if(f == null)
            throw new NullPointerException("Non possono essere passati valori nulli");
        //uso il metodo add già definito nell HashSet
        return facilities.add(f);
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
     * 
     * @param ts
     *               il time slot da controllare
     * 
     * @return true se l'aula risulta libera per tutto il periodo del time slot
     *         specificato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    //se sbagliato ha una esecuzione più lunga di 10ms
    public boolean isFree(TimeSlot ts) {
        /*
         * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in
         * maniera efficiente: poiché le prenotazioni sono in ordine crescente
         * di time slot se arrivo a una prenotazione che segue il time slot
         * specificato posso concludere che l'aula è libera nel time slot
         * desiderato e posso interrompere la ricerca
         */
        //controllo che l'oggetto passato non sia nullo
        if(ts == null)
            throw new NullPointerException("Non possono essere passati valori nulli");
        /*
        //  ---------- PROVA ----------
        //verifico se il primo elemento timeSlot dell'array è più grande del timeSlot passato come parametro
        //cioè se viene dopo nel tempo e in tal caso ritorno che l'aula è libera
        if(this.prenotazioni.first().getTimeSlot().compareTo(ts) > 0 ||
                this.prenotazioni.last().getTimeSlot().compareTo(ts) < 0)
            return true;
        //  ---------- PROVA ----------
        */
        //scorro nell'array delle prenotazioni
        for(Prenotazione pren : this.prenotazioni ){
            //se trovo il timeSlot già occupato
            if(pren.getTimeSlot().overlapsWith(ts))
                return false;
        }
        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Set<Facility> requestedFacilities) {
        //controllo che l'oggetto passato non sia nullo
        if(requestedFacilities == null)
            throw new NullPointerException("Non possono essere passati valori nulli");
        //faccio il cast del Set generico passato come parametro a un HashSet
        HashSet<Facility> facility = (HashSet<Facility>) requestedFacilities;
        //chiama il metodo containsAll definito in java.utils.AbstractCollections
        //che ritorna true se tutti i due array contengono gli stessi elementi
        return this.facilities.containsAll(facility);
    }

    /**
     * Prenota l'aula controllando eventuali sovrapposizioni.
     * 
     * @param ts
     * @param docente
     * @param motivo
     * @throws IllegalArgumentException
     *                                      se la prenotazione comporta una
     *                                      sovrapposizione con un'altra
     *                                      prenotazione nella stessa aula.
     * @throws NullPointerException
     *                                      se una qualsiasi delle informazioni
     *                                      richieste è nulla.
     */
    public void addPrenotazione(TimeSlot ts, String docente, String motivo) {
        //controllo che l'oggetto passato non sia nullo
        if(ts == null || docente == null || motivo == null)
            throw new NullPointerException("Non possono essere passati valori nulli");
        //controlla che il TimeSlot inserito non sia occupato
        if(!(this.isFree(ts)))
            throw new IllegalArgumentException("TimeSlot passato non valido");
        //aggiunge un oggetto prenotazione all'array di prenotazioni
        this.prenotazioni.add(new Prenotazione(this, ts, docente, motivo ));
    }

    /**
     * Cancella una prenotazione di questa aula.
     * 
     * @param p
     *              la prenotazione da cancellare
     * @return true se la prenotazione è stata cancellata, false se non era
     *         presente.
     * @throws NullPointerException
     *                                  se la prenotazione passata è null
     */
    public boolean removePrenotazione(Prenotazione p) {
        //controllo che l'oggetto passato non sia nullo
        if(p == null)
            throw new NullPointerException("Non possono essere passati valori nulli");
        //uso il metodo remove già definito nel sortedSet che ritorna true
        //se l'elemento era presente, false altrimenti
        return this.prenotazioni.remove(p);

    }

    /**
     * Rimuove tutte le prenotazioni di questa aula che iniziano prima (o
     * esattamente in) di un punto nel tempo specificato.
     * 
     * @param timePoint
     *                      un certo punto nel tempo
     * @return true se almeno una prenotazione è stata cancellata, false
     *         altrimenti.
     * @throws NullPointerException
     *                                  se il punto nel tempo passato è nullo.
     */
    //se sbagliato ha una esecuzione più lunga di 10ms
    public boolean removePrenotazioniBefore(GregorianCalendar timePoint) {
        /*
         * NOTA: sfruttare l'ordinamento tra le prenotazioni per rispondere in
         * maniera efficiente: poiché le prenotazioni sono in ordine crescente
         * di time slot se ho raggiunto una prenotazione con tempo di inizio
         * maggiore del tempo indicato posso smettere la procedura
         */
        //controllo che l'oggetto passato non sia nullo
        if(timePoint == null)
            throw new NullPointerException("Non possono essere passati valori nulli");
        //inizializzo un flag che dichiara se è stato trovato ed eliminato qualcosa
        boolean somethingRemoved = false;
        //creo un nuovo iteratore
        Iterator<Prenotazione> iterator = this.prenotazioni.iterator();
        //creo un oggetto di tipo prenotazione
        Prenotazione p;
        //uso l'iteratore per scorrere nell'array
        while(iterator.hasNext()){
            //incremento l'iteratore e inserisco l'attuale oggetto nell'oggetto prenotazione precedentemente creato
            p = iterator.next();
            if(p.getTimeSlot().getStart().compareTo(timePoint) > 0)
                break;
            //controllo che la prenotazione che fa parte dell'array prenotazioni venga prima del timeSlot passato
            //tramite il compare to già definito nella classe gregorianCalendar
            if(p.getTimeSlot().getStart().compareTo(timePoint) <= 0){
                //rimuovo l'oggetto dall'array
                iterator.remove();
                //reimposto il flag
                somethingRemoved = true;
            }
        }
        //ritorno il flag
        return somethingRemoved;
    }
}
