package it.unicam.cs.asdl2324.es1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * Front-end testuale per permettere l'uso delle classi di <i>core business
 * logic</i> che risolvono le equazioni di secondo grado, in particolare le
 * classi EquazioneSecondoGradoModificabileConRisolutore e
 * SoluzioneEquazioneSecondoGrado.
 *
 * @author Luca Tesei
 */
public class EquazioniModificabiliTextualFrontEnd {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    private static final double EPSILON = 1.0E-15;

    public static void main(String[] args) {
        EquazioneSecondoGradoModificabileConRisolutore eq = null;
        boolean terminate = false;
        BufferedReader input = new BufferedReader(
                new InputStreamReader(System.in));
        double a = 1; // valori iniziali non significativi
        double b = 1;
        double c = 1;
        while (!terminate) {
            // leggo il parametro a
            boolean retry = false;
            do {
                System.out.println(
                        "Inserisci il valore del parametro a e premi INVIO");
                try {
                    String aInput = input.readLine();
                    a = Double.parseDouble(aInput);
                    // controllo se il valore inserito è zero
                    if (Math.abs(a) < EPSILON) {
                        System.out.println(
                                "Errore: Il valore del parametro a non può essere zero! Ritenta...");
                        retry = true;
                    }
                    /*
                     * --> punto in cui è stato cambiato il file originale
                     * il motivo del bug era che il flag "retry" una volta
                     * entrato nel caso dell'eccezione e diventato TRUE
                     * non aveva modo per tornare FALSE, poichè lo stato
                     * FALSE lo acquisiva solamente durante la sua dichiarazione.
                     * E' bastato rimettere il flag "retry" a FALSE una volta letto
                     * il valore da tastiera all' interno dell'else (che verifica i casi in cui
                     * il numero inserito sia diverso da zero) per continuare a
                     * far funzionare il loop.
                     * Nonostante ciò il catch exception funziona lo stesso perchè
                     * qualora ci dovesse essere una eccezione, cioè il simbolo inserito
                     * non è uno tra quelli consentiti, il flag "retry" torna a FALSE.
                     * Mettere lì una modica della variabile booleana in ogni caso non indica che
                     * da lì in poi il flag sarà sempre a FALSE anzi potrà tornare TRUE incontrando
                     * un'eccezione.
                     */
                    else{
                        retry = false;
                    }
                } catch (IOException e) {
                    System.err.println("Errore di Input/Output!");
                    System.exit(1);
                } catch (NumberFormatException e) {
                    System.out.println(
                            "Errore: Il valore del parametro a deve essere un numero! Ritenta...");
                    retry = true;
                }
            } while (retry);
            // leggo il parametro b
            do {
                System.out.println(
                        "Inserisci il valore del parametro b e premi INVIO");
                try {
                    String aInput = input.readLine();
                    b = Double.parseDouble(aInput);
                    /*
                     * --> punto in cui è stato cambiato il file originale
                     * il motivo del bug era che il flag "retry" una volta
                     * entrato nel caso dell'eccezione e diventato TRUE
                     * non aveva modo per tornare FALSE, poichè lo stato
                     * FALSE lo acquisiva solamente durante la sua dichiarazione.
                     * E' bastato rimettere il flag "retry" a FALSE una volta letto
                     * il valore da tastiera per continuare a far funzionare il loop.
                     * Nonostante ciò il catch exception funziona lo stesso perchè
                     * qualora ci dovesse essere una eccezione, cioè il simbolo inserito
                     * non è uno tra quelli consentiti, il flag "retry" torna a FALSE.
                     * Mettere lì una modica della variabile booleana in ogni caso non indica che
                     * da lì in poi il flag sarà sempre a FALSE anzi potrà tornare TRUE incontrando
                     * un'eccezione.
                     */
                    retry = false;
                } catch (IOException e) {
                    System.err.println("Errore di Input/Output!");
                    System.exit(1);
                } catch (NumberFormatException e) {
                    System.out.println(
                            "Errore: Il valore del parametro b deve essere un numero! Ritenta...");
                    retry = true;
                }
            } while (retry);
            // leggo il parametro c
            do {
                System.out.println(
                        "Inserisci il valore del parametro c e premi INVIO");
                try {
                    String aInput = input.readLine();
                    c = Double.parseDouble(aInput);
                    /*
                     * --> punto in cui è stato cambiato il file originale
                     * il motivo del bug era che il flag "retry" una volta
                     * entrato nel caso dell'eccezione e diventato TRUE
                     * non aveva modo per tornare FALSE, poichè lo stato
                     * FALSE lo acquisiva solamente durante la sua dichiarazione.
                     * E' bastato rimettere il flag "retry" a FALSE una volta letto
                     * il valore da tastiera per continuare a far funzionare il loop.
                     * Nonostante ciò il catch exception funziona lo stesso perchè
                     * qualora ci dovesse essere una eccezione, cioè il simbolo inserito
                     * non è uno tra quelli consentiti, il flag "retry" torna a FALSE.
                     * Mettere lì una modica della variabile booleana in ogni caso non indica che
                     * da lì in poi il flag sarà sempre a FALSE anzi potrà tornare TRUE incontrando
                     * un'eccezione.
                     */
                    retry = false;
                } catch (IOException e) {
                    System.err.println("Errore di Input/Output!");
                    System.exit(1);
                } catch (NumberFormatException e) {
                    System.out.println(
                            "Errore: Il valore del parametro c deve essere un numero! Ritenta...");
                    retry = true;
                }
            } while (retry);
            // Se non esiste, creo l'equazione, altrimenti
            // aggiorno i parametri
            if (eq == null)
                eq = new EquazioneSecondoGradoModificabileConRisolutore(a, b,
                        c);
            else {
                eq.setA(a);
                eq.setB(b);
                eq.setC(c);
            }
            // Risolvo l'equazione corrente
            eq.solve();
            // Prendo la soluzione corrente
            SoluzioneEquazioneSecondoGrado sol = eq.getSolution();
            // Comunico la soluzione all'utente
            System.out.println(sol);
            // Chiedo se l'utente vuole continuare con un'altra equazione
            System.out.println(
                    "Vuoi risolvere un'altra equazione? Inserisci 's' o 'S' per continuare, qualsiasi altro carattere per uscire e premi INVIO");
            String resp = null;
            try {
                String aInput = input.readLine();
                resp = aInput.trim().toUpperCase();
            } catch (IOException e) {
                System.err.println("Errore di Input/Output!");
                System.exit(1);
            }
            if (!resp.equals("S"))
                terminate = true;
        }
    }

}
