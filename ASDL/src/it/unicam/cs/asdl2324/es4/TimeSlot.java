/**
 * 
 */
package it.unicam.cs.asdl2324.es4;

// TODO completare gli import se necessario

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Un time slot è un intervallo di tempo continuo che può essere associato ad
 * una prenotazione. Gli oggetti della classe sono immutabili. Non sono ammessi
 * time slot che iniziano e finiscono nello stesso istante.
 *
 * @author Luca Tesei
 *
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /**
     * Rappresenta la soglia di tolleranza da considerare nella sovrapposizione
     * di due Time Slot. Se si sovrappongono per un numero di minuti minore o
     * uguale a questa soglia allora NON vengono considerati sovrapposti.
     */
    public static final int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 5;

    private final GregorianCalendar start;

    private final GregorianCalendar stop;

    /**
     * Crea un time slot tra due istanti di inizio e fine
     *
     * @param start
     *                  inizio del time slot
     * @param stop
     *                  fine del time slot
     * @throws NullPointerException
     *                                      se uno dei due istanti, start o
     *                                      stop, è null
     * @throws IllegalArgumentException
     *                                      se start è uguale o successivo a
     *                                      stop
     */
    public TimeSlot(GregorianCalendar start, GregorianCalendar stop) {
        // TODO implementare
        if(start == null || stop == null)
            throw new NullPointerException("Il valore non può essere nullo");
        if(start.compareTo(stop) >= 0)
            throw new IllegalArgumentException("Lasso di tempo inserito non valido");
        //assegnazione delle variabili
        this.start = start;
        this.stop = stop;
    }

    /**
     * @return the start
     */
    public GregorianCalendar getStart() {
        return start;
    }

    /**
     * @return the stop
     */
    public GregorianCalendar getStop() {
        return stop;
    }

    /*
     * Un time slot è uguale a un altro se rappresenta esattamente lo stesso
     * intervallo di tempo, cioè se inizia nello stesso istante e termina nello
     * stesso istante.
     */
    @Override
    public boolean equals(Object obj) {
        // TODO implementare
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof TimeSlot))
            return false;
        //faccio il cast dell'oggetto passato generico Object
        //a un oggetto di tipo TimeSlot
        TimeSlot other = (TimeSlot) obj;
        //controllo l'uguaglianza tra start e stop
        return this.start.equals(other.start) && this.stop.equals(other.stop);
    }

    /*
     * Il codice hash associato a un timeslot viene calcolato a partire dai due
     * istanti di inizio e fine, in accordo con i campi usati per il metodo
     * equals.
     */
    @Override
    public int hashCode() {
        // TODO implementare
        //uso la funzione hashcode presente nella ClasseGregorian calendar
        //unisco l'hashcode dello start e l'hashcode dello stop
        return this.start.hashCode() + this.stop.hashCode();
    }

    /*
     * Un time slot precede un altro se inizia prima. Se due time slot iniziano
     * nello stesso momento quello che finisce prima precede l'altro. Se hanno
     * stesso inizio e stessa fine sono uguali, in compatibilità con equals.
     */
    @Override
    public int compareTo(TimeSlot o) {
        // TODO implementare
        if (o == null)
            throw new NullPointerException("Tentativo di confrontare con null");
        //uso il compare già definito nella classe GregorianCalendar
        //sia per lo start che per lo stop
        if(this.start.compareTo(o.start) == 0){
            return this.stop.compareTo(o.stop);
        }else
            return this.start.compareTo(o.start);
    }

    /**
     * Determina il numero di minuti di sovrapposizione tra questo timeslot e
     * quello passato.
     *
     * @param o
     *              il time slot da confrontare con questo
     * @return il numero di minuti di sovrapposizione tra questo time slot e
     *         quello passato, oppure -1 se non c'è sovrapposizione. Se questo
     *         time slot finisce esattamente al millisecondo dove inizia il time
     *         slot <code>o</code> non c'è sovrapposizione, così come se questo
     *         time slot inizia esattamente al millisecondo in cui finisce il
     *         time slot <code>o</code>. In questi ultimi due casi il risultato
     *         deve essere -1 e non 0. Nel caso in cui la sovrapposizione non è
     *         di un numero esatto di minuti, cioè ci sono secondi e
     *         millisecondi che avanzano, il numero dei minuti di
     *         sovrapposizione da restituire deve essere arrotondato per difetto
     * @throws NullPointerException
     *                                      se il time slot passato è nullo
     * @throws IllegalArgumentException
     *                                      se i minuti di sovrapposizione
     *                                      superano Integer.MAX_VALUE
     */
    public int getMinutesOfOverlappingWith(TimeSlot o) {
        // TODO implementare
        if(o == null)
            throw new NullPointerException("Non puoi passare valori nulli");
        //caso in cui non si sovrappongono, se coincidono non sono sovrapposti
        if(start.compareTo(o.stop) >= 0 || stop.compareTo(o.start) <= 0)
            return -1;
        //creo un spazio temporale di sovrapposizione
        GregorianCalendar overlapStart;
        GregorianCalendar overlapStop;
        if(this.start.compareTo(o.start) > 0)
            overlapStart = this.start;
        else
            overlapStart = o.start;
        if(this.stop.compareTo(o.stop) < 0)
            overlapStop = this.stop;
        else
            overlapStop = o.stop;
        //calcolo il valore in millisecondi della sovrapposizione
        long millisDiff = overlapStop.getTimeInMillis() - overlapStart.getTimeInMillis();
        //trasformo i millisecondi della differenza in minuti e la approssimo in minuti
        //poi lo arrotondo sempre per difetto facendo il cast a long
        long minutesDiff = (long) (millisDiff / (60 * 1000)); // Converti in minuti
        //controllo che non superi il valore massimo int
        if(minutesDiff > Integer.MAX_VALUE)
            throw new IllegalArgumentException("La sovrapposizione ha superato il limite consentito");
        //ritorno il numero di minuti di sovrapposizione tra i due slot come int
        return (int) minutesDiff;
    }

    /**
     * Determina se questo time slot si sovrappone a un altro time slot dato,
     * considerando la soglia di tolleranza.
     *
     * @param o
     *              il time slot che viene passato per il controllo di
     *              sovrapposizione
     * @return true se questo time slot si sovrappone per più (strettamente) di
     *         MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti a quello passato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean overlapsWith(TimeSlot o) {
        // TODO implementare
        if(o == null)
            throw new NullPointerException("L'oggetto passato non può essere nullo");
        //verifica se i minuti di sovrapposizione sono maggiori della tolleranza
        return this.getMinutesOfOverlappingWith(o) > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING;
    }

    /*
     * Ridefinisce il modo in cui viene reso un TimeSlot con una String.
     *
     * Esempio 1, stringa da restituire: "[4/11/2019 11.0 - 4/11/2019 13.0]"
     *
     * Esempio 2, stringa da restituire: "[10/11/2019 11.15 - 10/11/2019 23.45]"
     *
     * I secondi e i millisecondi eventuali non vengono scritti.
     */
    @Override
    public String toString() {
        // TODO implementare
        //stampa la data in formato [DD/MM/YYYY hh.mm - DD/MM/YYYY hh.mm]
        return "[" +
                //aggiungo 1 al mese perchè i mesi vanno da 0 a 11 e non da 1 a 12
                this.start.get(Calendar.DAY_OF_MONTH) + "/" + (this.start.get(Calendar.MONTH) + 1) + "/" + this.start.get(Calendar.YEAR)
                + " " +
                this.start.get(Calendar.HOUR_OF_DAY) + "." + this.start.get(Calendar.MINUTE)
                //HOUR_OF_DAY restituisce il formato in 24H
                //HOUR restituisce quello in 12H
                + " - " +

                this.stop.get(Calendar.DAY_OF_MONTH) + "/" + (this.stop.get(Calendar.MONTH) + 1) + "/" + this.stop.get(Calendar.YEAR)
                + " " +
                this.stop.get(Calendar.HOUR_OF_DAY) + "." + this.stop.get(Calendar.MINUTE)
                + "]";
    }

}
