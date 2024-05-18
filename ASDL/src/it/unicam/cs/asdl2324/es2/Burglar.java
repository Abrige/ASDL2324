package it.unicam.cs.asdl2324.es2;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 * 
 * @author Luca Tesei
 *
 */
public class Burglar {

    private CombinationLock aCombinationLock;
    private int attempts;
    private boolean unlocked;

    /**
     * Costruisce uno scassinatore per una certa cassaforte.
     * 
     * @param aCombinationLock
     * @throw NullPointerException se la cassaforte passata è nulla
     */
    public Burglar(CombinationLock aCombinationLock) {
        // TODO implementare
        //lancia un eccezione se l'oggetto passato è nullo
        if(aCombinationLock == null)
            throw new NullPointerException("L'oggetto inserito è nullo");
        //inizializzo le variabili di istanza
        this.aCombinationLock = aCombinationLock;
        this.attempts = 0;
        this.unlocked = false;
    }

    /**
     * Forza la cassaforte e restituisce la combinazione.
     * 
     * @return la combinazione della cassaforte forzata.
     */
    public String findCombination() {
        // TODO implementare
        //creo le variabili che popolerò con i caratteri
        // da passare al metodo setPosition()
        char firstCharacter;
        char secondCharacter;
        char thirdCharacter;
        //inizializzo la variabile che conterrà la
        //combinazione giust, in modo che se per qualche
        //problema non venga sovrascritta dal valore corretto
        //venga restituita la stringa "errore"
        String theCombination = "errore";
        //scorre tutte le possibilità per il primo carattere
        for(int i = 65; i <= 90; i++){
            firstCharacter = (char) i; //assegno alla variabile il valore corrente del char
            aCombinationLock.setPosition(firstCharacter); //inserisco il primo valore
            //scorre tutte le possibilità per il secondo carattere
            for(int j = 65; j <= 90; j++){
                secondCharacter = (char)j; //assegno alla variabile il valore corrente del char
                aCombinationLock.setPosition(secondCharacter); //inserisco il secondo valore
                for(int l = 65; l <= 90; l++){
                    this.attempts++; //aumento il contatore dei tentativi
                    thirdCharacter = (char)l; //assegno alla variabile il valore corrente del char
                    aCombinationLock.setPosition(thirdCharacter); //inserisco il terzo valore
                    aCombinationLock.open(); //chiamo il metodo che apre la cassaforte
                    //verifico se la cassaforte si sia aperta e quindi ho azzeccato la combinazione
                    if(aCombinationLock.isOpen()){
                        //trasformo i caratteri nella stringa della combinazione corretta trovata
                        theCombination = (Character.toString(firstCharacter) +
                                Character.toString(secondCharacter) +
                                Character.toString(thirdCharacter));
                        unlocked = true;
                        return theCombination;
                    }else{
                        //è necessario questo passaggio perchè nel momento in cui chiamiamo
                        //il metodo open() i caratteri inseriti vengono resettati
                        aCombinationLock.setPosition(firstCharacter); //reinserisco il primo valore
                        aCombinationLock.setPosition(secondCharacter); //reinserisco il secondo valore
                    }
                }
            }
        }
        return null;
    }

    /**
     * Restituisce il numero di tentativi che ci sono voluti per trovare la
     * combinazione. Se la cassaforte non è stata ancora forzata restituisce -1.
     * 
     * @return il numero di tentativi che ci sono voluti per trovare la
     *         combinazione, oppure -1 se la cassaforte non è stata ancora
     *         forzata.
     */
    public long getAttempts() {
        // TODO implementare
        //controlla se la cassaforte sia stata sbloccata correttamente
        if(unlocked)
            return this.attempts; //ritorna il numero di tentavi necessari per scassinare la cassaforte
        //se la cassaforte non è ancora stata
        //scassinata ritorna un valore negativo
        return -1;
    }
}
