package it.unicam.cs.asdl2324.es1;
/**
 * 
 */

/**
 * Un oggetto di questa classe permette di rappresentare una equazione di
 * secondo grado e di trovarne le soluzioni reali. I valori dei parametri
 * dell'equazione possono cambiare nel tempo. All'inizio e ogni volta che viene
 * cambiato un parametro la soluzione dell'equazione non esiste e deve essere
 * calcolata con il metodo <code>solve()</code>. E' possibile sapere se al
 * momento la soluzione dell'equazione esiste con il metodo
 * <code>isSolved()</code>. Qualora la soluzione corrente non esista e si tenti
 * di ottenerla verrà lanciata una eccezione.
 *
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a
 *         Casa
 *
 */
public class EquazioneSecondoGradoModificabileConRisolutore {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    private static final double EPSILON = 1.0E-15;

    private double a;

    private double b;

    private double c;

    private boolean solved;

    private SoluzioneEquazioneSecondoGrado lastSolution;

    /**
     * Costruisce una equazione di secondo grado modificabile. All'inizio
     * l'equazione non è risolta.
     *
     * @param a
     *              coefficiente del termine x^2, deve essere diverso da zero.
     * @param b
     *              coefficiente del termine x
     * @param c
     *              termine noto
     * @throws IllegalArgumentException
     *                                      se il parametro <code>a</code> è
     *                                      zero
     *
     */
    public EquazioneSecondoGradoModificabileConRisolutore(double a, double b,
            double c) {
        if (Math.abs(a) < EPSILON) // controllo se uguale a zero
            throw new IllegalArgumentException("L'equazione di secondo grado"
                    + " non può avere coefficiente a uguale a zero");
        this.a = a;
        this.b = b;
        this.c = c;
        this.solved = false;
    }


    /**
     * @return il valore corrente del parametro a
     */
    public double getA() {
        return a;
    }

    /**
     * Cambia il parametro a di questa equazione. Dopo questa operazione
     * l'equazione andrà risolta di nuovo.
     *
     * @param a
     *              il nuovo valore del parametro a
     * @throws IllegalArgumentException
     *                                      se il nuovo valore è zero
     */
    public void setA(double a) {
        if (Math.abs(a) < EPSILON) // controllo se uguale a zero
            throw new IllegalArgumentException("L'equazione di secondo grado"
                    + " non può avere coefficiente a uguale a zero");
        //controllo se il valore che è stato inserito è uguale al valore già presente nella variabile di istanza
        //Math.abs(this.a - a) < EPSILON ~ this.a == a
        //controllo inoltre lo stato della soluzione dell'equazione
        //questo per fare in modo che se una equazione viene risolta
        //e poi viene inserito lo stesso valore non debba essere
        //risolta nuovamente
        if(!(Math.abs(this.a - a) < EPSILON && solved)){
            this.a = a;
            this.solved = false;
        }
    }

    /**
     * @return il valore corrente del parametro b
     */
    public double getB() {
        return b;
    }

    /**
     * Cambia il parametro b di questa equazione. Dopo questa operazione
     * l'equazione andrà risolta di nuovo.
     *
     * @param b
     *              il nuovo valore del parametro b
     */
    public void setB(double b) {
        //controllo se il valore che è stato inserito è uguale al valore già
        //presente nella variabile di istanza
        //Math.abs(this.b - b) < EPSILON ~ this.b == b
        //controllo inoltre lo stato della soluzione dell'equazione
        //questo per fare in modo che se una equazione viene risolta
        //e poi viene inserito lo stesso valore non debba essere
        //risolta nuovamente
        if(!(Math.abs(this.b - b) < EPSILON && solved)){
            this.b = b;
            this.solved = false;
        }
    }

    /**
     * @return il valore corrente del parametro c
     */
    public double getC() {
        return c;
    }

    /**
     * Cambia il parametro c di questa equazione. Dopo questa operazione
     * l'equazione andrà risolta di nuovo.
     *
     * @param c
     *              il nuovo valore del parametro c
     */
    public void setC(double c) {
        //controllo se il valore che è stato inserito è uguale al valore già presente nella variabile di istanza
        //Math.abs(this.c - c) < EPSILON ~ this.c == c
        //controllo inoltre lo stato della soluzione dell'equazione
        //questo per fare in modo che se una equazione viene risolta
        //e poi viene inserito lo stesso valore non debba essere
        //risolta nuovamente
        if(!(Math.abs(this.c - c) < EPSILON && solved)){
            this.c = c;
            this.solved = false;
        }
    }

    /**
     * Determina se l'equazione, nel suo stato corrente, è già stata risolta.
     *
     * @return true se l'equazione è risolta, false altrimenti
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Risolve l'equazione risultante dai parametri a, b e c correnti. Se
     * l'equazione era già stata risolta con i parametri correnti non viene
     * risolta di nuovo.
     */
    public void solve() {
        //crea una copia dell'equazione in un oggetto EquazioneDiSecondoGrado
        if(!solved) {
            EquazioneSecondoGrado equazione = new EquazioneSecondoGrado(this.a, this.b, this.c);
            //calcola il delta
            double delta = this.b * this.b - 4 * this.a * this.c;
            // delta == 0
            if (Math.abs(delta) < EPSILON) {
                this.lastSolution = new SoluzioneEquazioneSecondoGrado(equazione, (-this.b) / (2 * this.a));
                this.solved = true;
            }
            // delta < 0
            else if (delta < 0) {
                // ritorna la soluzione vuota
                this.lastSolution = new SoluzioneEquazioneSecondoGrado(equazione);
                this.solved = true;
            }
            // delta > 0
            else if (delta > 0) {
                double tmp = Math.sqrt(delta);
                this.lastSolution = new SoluzioneEquazioneSecondoGrado(equazione, ((-this.b) + tmp) / (2 * this.a),
                        ((-this.b) - tmp) / (2 * this.a));
                this.solved = true;
            }
        }

    }

    /**
     * Restituisce la soluzione dell'equazione risultante dai parametri
     * correnti. L'equazione con i parametri correnti deve essere stata
     * precedentemente risolta.
     * 
     * @return la soluzione
     * @throws IllegalStateException
     *                                   se l'equazione risulta non risolta,
     *                                   all'inizio o dopo il cambiamento di
     *                                   almeno uno dei parametri
     */
    public SoluzioneEquazioneSecondoGrado getSolution() {
        //controllo lo stato di soluzione dell'equazione
        if(!solved)
            throw new IllegalStateException("L'equazione non è ancora stata risolta");
        return this.lastSolution;
    }

}
