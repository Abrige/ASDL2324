package it.unicam.cs.asdl2324.es12;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 *
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 *
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 *
 * @author @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class PrimMST<L> {

    private List<GraphNode<L>> coda;
    private Set<GraphNode<L>> visitati;

    /*
     * In particolare: si deve usare una coda con priorità che può semplicemente
     * essere realizzata con una List<GraphNode<L>> e si deve mantenere un
     * insieme dei nodi già visitati
     */

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMST() {
        coda = new ArrayList<GraphNode<L>>();
        visitati = new HashSet<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. Dopo l'esecuzione del metodo nei nodi del grafo il campo
     * previous deve contenere un puntatore a un nodo in accordo all'albero di
     * copertura minimo calcolato, la cui radice è il nodo sorgente passato.
     *
     * @param g un grafo non orientato, pesato, con pesi non negativi
     * @param s il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *          dell'albero di copertura minimo. Tale nodo sarà la radice
     *          dell'albero di copertura trovato
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     * con pesi negativi
     */
    public void computeMSP(Graph<L> g, GraphNode<L> s) {
        if (g == null || s == null) {
            throw new NullPointerException("Grafo o nodo sorgente nullo");
        }
        if (!g.containsNode(s)) {
            throw new IllegalArgumentException("Nodo sorgente non esiste");
        }

        for (GraphNode<L> node : g.getNodes()) {
            node.setColor(GraphNode.COLOR_WHITE);
            node.setFloatingPointDistance(Double.POSITIVE_INFINITY);
            node.setPrevious(null);
        }
        Set<GraphEdge<L>> edges = g.getEdges();
        for (GraphEdge<L> edge : edges) {
            if (edge.getWeight() < 0 || Double.isNaN(edge.getWeight())) {
                throw new IllegalArgumentException("Grafo non pesato");
            }
        }
        s.setFloatingPointDistance(0);
        coda.addAll(g.getNodes());

        while (!coda.isEmpty()) {
            GraphNode<L> u = extractMin();
            visitati.add(u);
            u.setColor(GraphNode.COLOR_BLACK);
            Set<GraphEdge<L>> archi = g.getEdgesOf(u);

            for (GraphEdge<L> edge : archi) {
                GraphNode<L> v = edge.getNode1().equals(u) ? edge.getNode2() : edge.getNode1();
                double weight = edge.getWeight();


                if (coda.contains(v) && weight < v.getFloatingPointDistance()) {
                    v.setFloatingPointDistance(weight);
                    v.setPrevious(u);
                    u.setColor(GraphNode.COLOR_BLACK);

                    if (!coda.contains(v)) {
                        coda.add(v);
                    }
                }
            }
        }
    }

    // EXTRACT-MIN(Q) method on slides
    private GraphNode<L> extractMin() {
        GraphNode<L> min = coda.get(0);

        for (int i = 1; i < coda.size(); i++) {
            if (coda.get(i).getFloatingPointDistance() < min.getFloatingPointDistance()) {
                min = coda.get(i);
            }
        }

        coda.remove(min);
        return min;
    }
}