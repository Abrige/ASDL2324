package it.unicam.cs.asdl2324.mp2;

import java.util.*;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template) Mattia Brizi, mattia.brizi@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa a ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }

    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {
        this.currentElements = new HashMap<>();
    }

    @Override
    public boolean isPresent(E e) {
        // controllo parametro
        if(e == null)
            throw new NullPointerException("Parametro nullo non valido");
        // cerca nell'hash map se il valore passato esiste
        return this.currentElements.containsKey(e);
    }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
        // controllo parametro
        if(e == null)
            throw new NullPointerException("Parametro nullo non valido");
        if(this.isPresent(e))
            throw new IllegalArgumentException("Elemento già presente");
        // inserisce l'elemento nell'HashMap
        this.currentElements.put(e, new Node<>(e));
    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
        /*
        per ogni nodo che non è direttamente collegato al nodo padre, viene eliminato l'attuale predecessore
        di quel nodo e sostituito con il corretto, cioè il nodo padre, questo per mantenere il tempo di accesso al
        rappresentante di ogni nodo costante, cioè O(1)
         */

        // controllo parametro
        if(e == null)
            throw new NullPointerException("Parametro nullo non valido");
        // variabile di appoggio che contiene il risultato della ricerca del parametro nella HashMap
        Node<E> aNode = this.currentElements.get(e);
        // se il nodo non è presente
        if(aNode == null)
            return null;
        // se il nodo è lui stesso il rappresentante
        if(aNode != aNode.parent)
            // chiamata ricorsiva
            aNode.parent = this.currentElements.get(this.findSet(aNode.parent.item));
        return aNode.parent.item;
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {
        // controllo parametri
        if(e1 == null || e2 == null)
            throw new NullPointerException("Parametri nulli non validi");
        if(!(this.isPresent(e1) && this.isPresent(e2)))
            throw new IllegalArgumentException("Almeno uno dei due elementi non è presente");
        // se i due rappresentanti sono uguali quindi gli elementi fanno parte dello stesso insieme
        if(findSet(e1) == findSet(e2))
            return;
        this.link(findSet(e1), findSet(e2)); // chiama un metodo privato di questa classe
    }

    private void link(E x, E y){
        Node<E> r1 = this.currentElements.get(x); // prende il nodo del rappresentante 1 nella HashMap
        Node<E> r2 = this.currentElements.get(y); // prende il nodo del rappresentante 2 nella HashMap

        // se il primo rappresentante ha rango maggiore
        if(r1.rank > r2.rank)
            r2.parent = r1; // il primo rappresentante diventa il nuovo rappresentante
        // se il secondo rappresentante ha rango maggiore
        else{
            r1.parent = r2; // il secondo rappresentante diventa il nuovo rappresentante
            if(r1.rank == r2.rank) // se hanno ìl rango uguale il
                r2.rank++; // incremento del rango del secondo rappresentante
        }
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
        // crea il nuovo set che verrà popolato con i rappresentanti
        HashSet<E> representatives = new HashSet<>();
        // per ogni elemento nella HashMap
        for(E current : this.currentElements.keySet()){
            representatives.add(this.findSet(current)); // prende il rappresentante e lo aggiunge al set
        }
        // ritorna il set popolato con i rappresentanti
        return representatives;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
        // controllo parametro
        if(e == null )
            throw new NullPointerException("Parametro nullo non valido");
        if(!(this.isPresent(e)))
            throw new IllegalArgumentException("Elemento non presente");
        // crea il nuovo set che verrà popolato con gli elementi
        HashSet<E> elements = new HashSet<>();
        E rappresentante = this.findSet(e); // variabile di appoggio al rappresentante dell'elemento passato
        // scorre in ogni elemento della HashMap
        for(E current : this.currentElements.keySet()){
            // se il rappresentante di quell'oggetto è lo stesso dell'oggetto passato
            if(this.findSet(current) == rappresentante)
                // aggiunge l'elemento al set
                elements.add(current);
        }
        return elements; // ritorna gli elementi
    }

    @Override
    public void clear() {
        // elimina tutti gli elementi dalla HashMap
        this.currentElements.clear();
    }
}
