package it.unicam.cs.asdl2324.es2;
/**
 * Un oggetto cassaforte con combinazione ha una manopola che può essere
 * impostata su certe posizioni contrassegnate da lettere maiuscole. La
 * serratura si apre solo se le ultime tre lettere impostate sono uguali alla
 * combinazione segreta.
 * 
 * @author Luca Tesei
 */
public class CombinationLock {

    // TODO inserire le variabili istanza che servono
    //stringa che determina la combinazione della cassaforte
    private String theCombination;
    //array di caratteri per immagazzinare la combinazione inserita
    private char[] insertedCombination;
    //stato della cassaforte (aperta o chiusa)
    private boolean open;
    //stato della cassaforte (bloccata o sbloccata)
    private boolean locked;
    /**
     * Costruisce una cassaforte <b>aperta</b> con una data combinazione
     * 
     * @param aCombination
     *                         la combinazione che deve essere una stringa di 3
     *                         lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     *        stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public CombinationLock(String aCombination) {
        // TODO implementare
        //verifica la lunghezza della stringa
        if(aCombination.length() != 3)
            throw new IllegalArgumentException("La stringa deve essere di 3 caratteri");
        //scorre su tutti gli elementi della stringa
        for(int i = 0; i < aCombination.length(); i++){
            //controlla che non ci siano caratteri non consentiti
            if (aCombination.charAt(i) < 'A' || aCombination.charAt(i) > 'Z')
                throw new IllegalArgumentException("Caratteri non consentiti");
        }
        //inizializza le variabili
        theCombination = aCombination;
        insertedCombination = new char[3];
        open = true;
    }

    /**
     * Imposta la manopola su una certaposizione.
     * 
     * @param aPosition
     *                      un carattere lettera maiuscola su cui viene
     *                      impostata la manopola
     * @throws IllegalArgumentException
     *                                      se il carattere fornito non è una
     *                                      lettera maiuscola dell'alfabeto
     *                                      inglese
     */
    public void setPosition(char aPosition) {
        // TODO implementare
        //controlla che il carattere inserito sia un carattere valido
        if (aPosition < 'A' || aPosition > 'Z')
            throw new IllegalArgumentException("Carattere non consentito");
        //piccolo algoritmo che aggiunge il carattere inserito sempre alla fine dell'array
        insertedCombination[0] = insertedCombination[1];
        insertedCombination[1] = insertedCombination[2];
        insertedCombination[2] = aPosition;
        //imposta lo stato del blocco della cassaforte su falso,
        //questo perché nel momento in cui si setta un nuovo carattere
        //sarà possibile nuovamente tentare di aprire la cassaforte
        locked = false;
    }

    /**
     * Tenta di aprire la serratura considerando come combinazione fornita le
     * ultime tre posizioni impostate. Se l'apertura non va a buon fine le
     * lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     */
    public void open() {
        // TODO implementare
        //controlla se la cassaforte è aperta o chiusa
        if(!locked){
            //scorre lungo tutta la stringa della combinazione della cassaforte
            for(int i = 0; i < theCombination.length(); i++){
                //controlla che la combinazione inserita sia uguale alla combinazione della cassaforte
                if(theCombination.charAt(i) != insertedCombination[i]){
                    open = false; //setta lo stato della cassaforte a chiuso
                    insertedCombination = new char[3]; //resetta la combinazione inserita
                    return; //esce dal metodo
                }
            }
            open = true; //setta lo stato della cassaforte ad aperto
        }
    }

    /**
     * Determina se la cassaforte è aperta.
     * 
     * @return true se la cassaforte è attualmente aperta, false altrimenti
     */
    public boolean isOpen() {
        // TODO implementare
        return open; //ritorna lo stato dell'apertura della cassaforte
    }

    /**
     * Chiude la cassaforte senza modificare la combinazione attuale. Fa in modo
     * che se si prova a riaprire subito senza impostare nessuna nuova posizione
     * della manopola la cassaforte non si apre. Si noti che se la cassaforte
     * era stata aperta con la combinazione giusta le ultime posizioni impostate
     * sono proprio la combinazione attuale.
     */
    public void lock() {
        // TODO implementare
        open = false; //imposta lo stato della cassaforte su chiusa

        //imposta lo stato della cassaforte su bloccata
        //in modo che se si provi a riaprire senza cambiare
        //la sequenza di caratteri inseriti questa non si apra
        locked = true;
    }

    /**
     * Chiude la cassaforte e modifica la combinazione. Funziona solo se la
     * cassaforte è attualmente aperta. Se la cassaforte è attualmente chiusa
     * rimane chiusa e la combinazione non viene cambiata, ma in questo caso le
     * le lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     * 
     * @param aCombination
     *                         la nuova combinazione che deve essere una stringa
     *                         di 3 lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     *        stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public void lockAndChangeCombination(String aCombination) {
        // TODO implementare
        //verifica che la cassaforte sia aperta
        if(open){
            //controlla la dimensione della combinazione
            if(aCombination.length() != 3)
                throw new IllegalArgumentException("Lunghezza stringa non valida");
            //scorre per la stringa della combinazione inserita
            for(int i = 0; i < aCombination.length(); i++){
                //controlla che tutti i caratteri inseriti siano validi
                if (aCombination.charAt(i) < 'A' || aCombination.charAt(i) > 'Z')
                    throw new IllegalArgumentException("Caratteri non consentiti");
            }
            this.lock(); //blocca la cassaforte chiamando lo stesso metodo della classe
            theCombination = aCombination; //setta la nuova combinazione
        }else{
            insertedCombination = new char[3]; //resetta la combinazione inserita
        }
    }
}