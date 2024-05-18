package it.unicam.cs.asdl2324.mp2;

import java.util.*;

//ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author Luca Tesei (template) Mattia Brizi, mattia.brizi@studenti.unicam.it (implementazione)
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMST<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMST() {
        this.disjointSets = new ForestDisjointSets<>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     *
     * @param g un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     * copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     * con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        // controllo parametro
        if(g == null)
            throw new NullPointerException("Parametro nullo non valido");
        if(g.isDirected())
            throw new IllegalArgumentException("Grafo orientato non valido");
        // per ogni arco del grafo controlla se rispetta le regole dell'algoritmo
        for(GraphEdge<L> currentEdge : g.getEdges()){
            if(!(currentEdge.hasWeight()) || currentEdge.getWeight() < 0)
                throw new IllegalArgumentException("Grafo con tipo di archi non valido");
        }

        // azzera l'oggetto
        this.disjointSets.clear();

        // per ogni nodo del grafo
        for (GraphNode<L> currentNode : g.getNodes()) {
            // crea un set con il nodo
            this.disjointSets.makeSet(currentNode);
        }

        // crea un ArrayList del set di archi dal grafo
        ArrayList<GraphEdge<L>> edgeList = new ArrayList<>(g.getEdges());
        edgeList.sort(new Comparatore<>()); // ordina in maniera crescente la lista di archi in base al peso
        // crea il set che conterrà gli archi per il minimum spanning tree
        HashSet<GraphEdge<L>> a = new HashSet<>();

        // per ogni arco nella lista di archi ordinata
        for (GraphEdge<L> currentEdge : edgeList) {
            // se il rappresentante del primo nodo dell'arco è diverso dal secondo
            // cioè se non sono nello stesso sotto albero
            if (this.disjointSets.findSet(currentEdge.getNode1()) !=
                    this.disjointSets.findSet(currentEdge.getNode2())) {
                a.add(currentEdge); // aggiunge l'arco corrente al set
                // unisce i due nodi costituenti dell'arco
                this.disjointSets.union(currentEdge.getNode1(), currentEdge.getNode2());
            }
        }
        // ritorna il set a cioè il set che contiene gli archi per il minimum spanning tree
        return a;
    }

    /*
    * Classe che implementa l'interfaccia comparator.
    * Serve a far si che gli archi possano essere ordinati.
    */
    private static class Comparatore<L> implements Comparator<GraphEdge<L>> {
        @Override
        public int compare(GraphEdge<L> edge1, GraphEdge<L> edge2) {
            // compara i due valori double
            return Double.compare(edge1.getWeight(), edge2.getWeight());
        }
    }
}