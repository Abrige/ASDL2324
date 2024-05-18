package it.unicam.cs.asdl2324.mp1;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Gli insiemi disgiunti vengono implementati utilizzando una linkedList
 * in cui vengono contenuti esclusivamente i rappresentanti di ogni insieme.
 * Questo perché grazie ai riferimenti presenti in ogni oggetto rappresentante
 * dell'insieme, riusciamo comunque a ottenere ogni elemento di ogni insieme.
 * 
 * @author Luca Tesei (template)
 *         Mattia Brizi, mattia.brizi@studenti.unicam.it (implementazione)
 *
 */
public class LinkedListDisjointSets implements DisjointSets {

    //usiamo una linked list come collezione per gli insiemi
    private final LinkedList<DisjointSetElement> linkedList;
    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
        this.linkedList = new LinkedList<>(); //creazione di una nuova istanza per la linkedlist
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
        return e.getRef1() != null;
    }

    /*
     * Nella rappresentazione con liste concatenate un nuovo insieme disgiunto è
     * rappresentato da una lista concatenata che contiene l'unico elemento. Il
     * rappresentante deve essere l'elemento stesso e la cardinalità deve essere
     * 1.
     */
    @Override
    public void makeSet(DisjointSetElement e) {
        //controllo se i parametri passati rispettano le regole
        if(e == null)
            throw new NullPointerException("L'argomento non può essere null");
        if(this.isPresent(e))
            throw new IllegalArgumentException("Elemento già presente");
        e.setRef1(e); //imposta il representative come l'oggetto passato
        e.setRef2(null); //imposta il "next" elemento a null
        e.setNumber(1);//imposta la cardinalità del disjoint set a 1
        this.linkedList.add(e); //infine aggiunge l'elemento alla lista
    }

    /*
     * Nella rappresentazione con liste concatenate per trovare il
     * rappresentante di un elemento basta far riferimento al suo puntatore
     * ref1.
     */
    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {
        //controllo se i parametri passati rispettano le regole
        if(e == null)
            throw new NullPointerException("L'argomento non può essere null");
        if(!(this.isPresent(e)))
            throw new IllegalArgumentException("Elemento non presente");
        //tempo Theta(1) per ottenere il rappresentante di un qualsiasi oggetto
        return e.getRef1();
    }

    /*
     * Dopo l'unione di due insiemi effettivamente disgiunti il rappresentante
     * dell'insieme unito è il rappresentate dell'insieme che aveva il numero
     * maggiore di elementi tra l'insieme di cui faceva parte {@code e1} e
     * l'insieme di cui faceva parte {@code e2}. Nel caso in cui entrambi gli
     * insiemi avevano lo stesso numero di elementi il rappresentante
     * dell'insieme unito è il rappresentante del vecchio insieme di cui faceva
     * parte {@code e1}.
     * 
     * Questo comportamento è la risultante naturale di una strategia che
     * minimizza il numero di operazioni da fare per realizzare l'unione nel
     * caso di rappresentazione con liste concatenate.
     * 
     */
    @Override
    public void union(DisjointSetElement e1, DisjointSetElement e2) {
        //se uno dei due elementi è null
        if(e1 == null || e2 == null)
            throw new NullPointerException("Gli argomenti non possono essere null");
        //se e1 o e2 non sono presenti in nessun insieme
        if(!(this.isPresent(e1) || this.isPresent(e2)))
            throw new IllegalArgumentException("Argomenti passati non validi");

        //prendo i rappresentanti dei due elementi
        DisjointSetElement r1 = this.findSet(e1);
        DisjointSetElement r2 = this.findSet(e2);

        //uso == come indicato nel file IstruzioniMP1 a causa delle
        //proprietà intrinseche dell'oggetto DisjointSetElement
        if(r1 == r2)
            return; //non fa niente

        //ottengo le cardinalità dei due insiemi
        int r1Card = r1.getNumber();
        int r2Card = r2.getNumber();

        //se il secondo set ha cardinalità maggiore
        if(r2Card > r1Card){
            DisjointSetElement r2SecondElement = r2.getRef2();
            //setting del rappresentante
            r2.setRef2(r1); //imposta il puntatore del secondo elemento al vecchio rappresentante
            r2.setNumber(r2Card + r1Card); //aggiorno la cardinalità dell'insieme come somma delle due cardinalità
            //setting del vecchio rappresentante
            r1.setRef1(r2); //aggiorna il puntatore "Rappresentante" del secondo oggetto puntando r1
            r1.setNumber(0); //azzera la cardinalità del secondo rappresentante
            this.linkedList.remove(r1); //lo rimuove dal set dei rappresentanti

            DisjointSetElement r1Copy = r1; //variabile di appoggio

            //scorre nell'insieme finché non trova l'ultimo elemento
            while(r1Copy.getRef2() != null){
                r1Copy = r1Copy.getRef2();
                r1Copy.setRef1(r2); //Setta il rappresentante di ogni ognuno ad r2
            }
            r1Copy.setRef2(r2SecondElement);
            r1Copy.setRef1(r2); //Setta il rappresentante ad r2
        }
        //se hanno la stessa cardinalità o il primo ha cardinalità maggiore
        //si comportano allo stesso modo
        else{
            DisjointSetElement r1SecondElement = r1.getRef2();
            //setting del rappresentante
            r1.setRef2(r2); //imposta il puntatore del secondo elemento al vecchio rappresentante
            r1.setNumber(r1Card + r2Card); //aggiorno la cardinalità dell'insieme come somma delle due cardinalità
            //setting del vecchio rappresentante
            r2.setRef1(r1); //aggiorna il puntatore "Rappresentante" del secondo oggetto puntando r1
            r2.setNumber(0); //azzera la cardinalità del secondo rappresentante
            this.linkedList.remove(r2); //lo rimuove dal set dei rappresentanti

            DisjointSetElement r2Copy = r2; //variabile di appoggio

            //scorre nell'insieme finché non trova l'ultimo elemento
            while(r2Copy.getRef2() != null){
                r2Copy = r2Copy.getRef2();
                r2Copy.setRef1(r1); //Setta il rappresentante di ogni ognuno ad r1
            }
            r2Copy.setRef2(r1SecondElement);
            r2Copy.setRef1(r1); //Setta il rappresentante ad r1
        }
    }

    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives() {
        return new HashSet<>(this.linkedList);
    }
    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(DisjointSetElement e) {
        //controllo se i parametri passati rispettano le regole
        if(e == null)
            throw new NullPointerException("L'argomento non può essere null");
        if(!(this.isPresent(e)))
            throw new IllegalArgumentException("Elemento non presente");
        DisjointSetElement e1 = this.findSet(e); //oggetto di appoggio
        HashSet<DisjointSetElement> set = new HashSet<>();
        do{
            set.add(e1);
            e1 = e1.getRef2();
        }while(e1 != null);
        return set;
    }
    @Override
    public int getCardinalityOfSetContaining(DisjointSetElement e) {
        //se l'oggetto passato è null
        if(e == null)
            throw new NullPointerException("L'argomento non può essere null");
        //se non è presente
        if(!(this.isPresent(e)))
            throw new IllegalArgumentException("Elemento non presente");
        //ritorna il number del Rappresentante di un insieme disgiunto
        //che tiene conto della cardinalità dell'insieme
        return e.getRef1().getNumber();
    }

}
