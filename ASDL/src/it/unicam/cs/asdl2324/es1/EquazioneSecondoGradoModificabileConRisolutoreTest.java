package it.unicam.cs.asdl2324.es1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a
 *         Casa
 *
 */
class EquazioneSecondoGradoModificabileConRisolutoreTest {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    static final double EPSILON = 1.0E-15;

    @Test
    final void testEquazioneSecondoGradoModificabileConRisolutore() {
        // controllo che il valore zero su a lanci l'eccezione
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1,
                        1));
        // devo controllare che comunque nel caso normale il costruttore
        // funziona
        EquazioneSecondoGradoModificabileConRisolutore eq = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        // Controllo che all'inizio l'equazione non sia risolta
        assertFalse(eq.isSolved());
    }

    @Test
    final void testGetA() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                x, 1, 1);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getA());
        // in generale si dovrebbe usare assertTrue(Math.abs(x -
        // e1.getA())<EPSILON) ma in
        // questo caso il valore che testiamo non ha subito manipolazioni quindi
        // la sua rappresentazione sarà la stessa di quella inserita nel
        // costruttore senza errori di approssimazione

    }

    @Test
    final void testSetA() {
        // TODO implementare
        double x = 1.0;
        //creo un nuovo oggetto generico da testare
        EquazioneSecondoGradoModificabileConRisolutore e2 =
                new EquazioneSecondoGradoModificabileConRisolutore(2,5,4);
        //controllo che lo stato dell'equazione sia giusto (not solved)
        assertFalse(e2.isSolved());
        //imposto un nuovo valore per il campo a
        e2.setA(x);
        //controllo che il nuovo valore inserito in a sia effettivamente stato cambiato ( x == a )
        assertTrue(Math.abs(x - e2.getA()) < EPSILON);
        //controllo che lo stato dell'equazione sia giusto (not solved)
        assertFalse(e2.isSolved());
        //controllo che creando un'equazione con coefficiente a = 0 venga correttamente lanciato l'errore
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1, 1));
    }

    @Test
    final void testGetB() {
        // TODO implementare
        double x = 10;
        //creo un nuovo oggetto generico da testare
        EquazioneSecondoGradoModificabileConRisolutore e4 =
                new EquazioneSecondoGradoModificabileConRisolutore(1, x, 1);
        //controllo che i valori restituiti siano uguali a quelli inseriti ( x == b )
        assertTrue(Math.abs(x - e4.getB()) < EPSILON);
    }

    @Test
    final void testSetB() {
        // TODO implementare
        double x = 1.0;
        //creo un nuovo oggetto generico da testare
        EquazioneSecondoGradoModificabileConRisolutore e5 =
                new EquazioneSecondoGradoModificabileConRisolutore(2,5,4);
        //controllo che lo stato dell'equazione sia giusto (not solved)
        assertFalse(e5.isSolved());
        //imposto un nuovo valore per il campo b
        e5.setB(x);
        //controllo che il nuovo valore inserito in a sia effettivamente stato cambiato ( x == b )
        assertTrue(Math.abs(x - e5.getB()) < EPSILON);
        //controllo che lo stato dell'equazione sia giusto (not solved)
        assertFalse(e5.isSolved());
    }

    @Test
    final void testGetC() {
        // TODO implementare
        double x = 10;
        //creo un nuovo oggetto generico da testare
        EquazioneSecondoGradoModificabileConRisolutore e6 =
                new EquazioneSecondoGradoModificabileConRisolutore(1, 1, x);
        //controllo che i valori restituiti siano uguali a quelli inseriti ( x == c )
        assertTrue(Math.abs(x - e6.getC()) < EPSILON);
    }

    @Test
    final void testSetC() {
        // TODO implementare
        double x = 1.0;
        //creo un nuovo oggetto generico da testare
        EquazioneSecondoGradoModificabileConRisolutore e7 =
                new EquazioneSecondoGradoModificabileConRisolutore(2,5,4);
        //controllo che l'equazione risulti non risolta
        assertFalse(e7.isSolved());
        //imposto un nuovo valore per il campo c
        e7.setC(x);
        //controllo che il nuovo valore inserito in a sia effettivamente stato cambiato ( x == c )
        assertTrue(Math.abs(x - e7.getC()) < EPSILON);
        //controllo che l'equazione risulti non risolta
        assertFalse(e7.isSolved());
    }

    @Test
    final void testIsSolved() {
        // TODO implementare
        //creo un nuovo oggetto generico da testare
        EquazioneSecondoGradoModificabileConRisolutore e8 =
                new EquazioneSecondoGradoModificabileConRisolutore(2,5,4);
        //controllo che l'equazione risulti non risolta
        assertFalse(e8.isSolved());
        //risolvo l'equazione
        e8.solve();
        //controllo che lo stato della soluzione dell'equazione sia cambiato
        assertTrue(e8.isSolved());
        //cambio la variabile c
        e8.setC(20);
        //controllo che lo stato della soluzione dell'equazione sia cambiato
        assertFalse(e8.isSolved());
        //risolvo l'equazione
        e8.solve();
        //controllo che lo stato della soluzione dell'equazione sia cambiato
        assertTrue(e8.isSolved());
    }

    @Test
    final void testSolve() {
        EquazioneSecondoGradoModificabileConRisolutore e3 =
                new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 3);
        // controllo semplicemente che la chiamata a solve() non generi errori
        e3.solve();
        // i test con i valori delle soluzioni vanno fatti nel test del metodo
        // getSolution()
    }

    @Test
    final void testGetSolution() {
        // TODO implementare

        // equazione senza soluzioni reali
        EquazioneSecondoGradoModificabileConRisolutore eq1 =
                new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        //risolvo l'equazione senza soluzioni reali
        eq1.solve();
        assertTrue(eq1.getSolution().isEmptySolution());

        // equazione con due soluzioni reali distinte
        EquazioneSecondoGradoModificabileConRisolutore eq2 =
                new EquazioneSecondoGradoModificabileConRisolutore(1, -3, 2);
        //risolvo l'equazione con due soluzioni reali distinte
        eq2.solve();
        //controllo che non sia stata classificata come soluzione vuota
        assertFalse(eq2.getSolution().isEmptySolution());
        //controllo che non sia stata classificata come unica soluzione
        assertFalse(eq2.getSolution().isOneSolution());
        //controllo che i calcoli siano stati effettuati correttamente
        //verifico sia in s1 che in s2 poiché non so in quale delle due la soluzione verrà contenuta
        assertTrue(eq2.getSolution().getS1() == 1 || eq2.getSolution().getS1() == 2);
        assertTrue(eq2.getSolution().getS2() == 1 || eq2.getSolution().getS2() == 2);
        //verifico la stessa cosa ma con maggiore precisione utilizzando la epsilon
        assertTrue(Math.abs(eq2.getSolution().getS1() - 1) < EPSILON
                || Math.abs(eq2.getSolution().getS1() - 2) < EPSILON);
        assertTrue(Math.abs(eq2.getSolution().getS2() - 1) < EPSILON
                || Math.abs(eq2.getSolution().getS2() - 2) < EPSILON);
        assertFalse(Math.abs(eq2.getSolution().getS1() - eq2.getSolution().getS2()) < EPSILON);

        // equazione con due soluzioni reali coincidenti
        EquazioneSecondoGradoModificabileConRisolutore eq3 =
                new EquazioneSecondoGradoModificabileConRisolutore(1, -2, 1);
        //risolvo l'equazione
        eq3.solve();
        //controllo che l'equazione non venga identificata come vuota
        assertFalse(eq3.getSolution().isEmptySolution());
        //controllo che l'equazione venga identificata come unica soluzione
        assertTrue(eq3.getSolution().isOneSolution());
        //controllo che l'equazione abbia il risultato giusto
        assertTrue(Math.abs(eq3.getSolution().getS1() - 1) < EPSILON);

        // equazione con due soluzioni reali con decimali e parametro a diverso da 1
        EquazioneSecondoGradoModificabileConRisolutore eq4 =
                new EquazioneSecondoGradoModificabileConRisolutore(2, -10, 2);
        //risolvo l'equazione
        eq4.solve();
        //controllo che l'equazione non venga identificata come vuota
        assertFalse(eq4.getSolution().isEmptySolution());
        //controllo che l'equazione venga identificata come unica soluzione
        assertFalse(eq4.getSolution().isOneSolution());
        //controllo che l'equazione abbia i risultati giusti
        assertTrue(Math.abs(eq4.getSolution().getS1() - 4.7912878474779195) < EPSILON
                || Math.abs(eq4.getSolution().getS1() - 0.20871215252208009) < EPSILON);
        assertTrue(Math.abs(eq4.getSolution().getS2() - 4.7912878474779195) < EPSILON
                || Math.abs(eq4.getSolution().getS2() - 0.20871215252208009) < EPSILON);

        //inizializzo le variabile per creare l'equazione
        int a = 10, b = 2, c = 0;
        //creo una equazione casuale le cui soluzione sono ( x1 = 0, x2 = 0.2 )
        EquazioneSecondoGradoModificabileConRisolutore eq5 =
                new EquazioneSecondoGradoModificabileConRisolutore(a, b, c);
        //controllo che chiamando la soluzione dell'equazione questa lanci un'eccezione
        assertThrows(IllegalStateException.class, () -> eq5.getSolution());
        //risolvo l'equazione
        eq5.solve();
        //controllo che chiamando la soluzione dell'equazione questa non lanci un'eccezione
        assertDoesNotThrow(() -> eq5.getSolution());
        //## Prova parametro a ##
        //cambio un parametro dell'equazione con lo stesso
        eq5.setA(a);
        //controllo che avendo messo lo stesso parametro di prima non lanci un errore alla chiamata della soluzione
        assertDoesNotThrow(() -> eq5.getSolution());
        //cambio il valore del coefficiente a con un valore diverso dal precedente ( a != 2)
        eq5.setA(2);
        //controllo che chiamando la soluzione venga lanciato un errore dato che l'equazione non è ancora stata risolta
        assertThrows(IllegalStateException.class, () -> eq5.getSolution());
        //## Prova parametro b ##
        //risolvo l'equazione
        eq5.solve();
        //cambio un parametro dell'equazione (risolta) con lo stesso
        eq5.setB(b);
        //controllo che avendo messo lo stesso parametro di prima non lanci un errore alla chiamata della soluzione
        assertDoesNotThrow(() -> eq5.getSolution());
        //cambio il valore del coefficiente a con un valore diverso dal precedente ( b != 2 )
        eq5.setB(3);
        //controllo che chiamando la soluzione venga lanciato un errore dato che l'equazione non è ancora stata risolta
        assertThrows(IllegalStateException.class, () -> eq5.getSolution());
        //## Prova parametro c ##
        //risolvo l'equazione
        eq5.solve();
        //cambio un parametro dell'equazione (risolta) con lo stesso
        eq5.setC(c);
        //controllo che avendo messo lo stesso parametro di prima non lanci un errore alla chiamata della soluzione
        assertDoesNotThrow(() -> eq5.getSolution());
        //cambio il valore del coefficiente a con un valore diverso dal precedente ( c != 0 )
        eq5.setC(1);
        //controllo che chiamando la soluzione venga lanciato un errore dato che l'equazione non è ancora stata risolta
        assertThrows(IllegalStateException.class, () -> eq5.getSolution());
    }

}
