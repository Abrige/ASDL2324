/**
 * 
 */
package it.unicam.cs.asdl2324.es12;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementazione della classe astratta {@code Graph<L>} che realizza un grafo
 * non orientato. Per la rappresentazione viene usata una variante della
 * rappresentazione a liste di adiacenza. A differenza della rappresentazione
 * standard si usano strutture dati più efficienti per quanto riguarda la
 * complessità in tempo della ricerca se un nodo è presente (pseudocostante, con
 * tabella hash) e se un arco è presente (pseudocostante, con tabella hash). Lo
 * spazio occupato per la rappresentazione risultà tuttavia più grande di quello
 * che servirebbe con la rappresentazione standard.
 * 
 * Le liste di adiacenza sono rappresentate con una mappa (implementata con
 * tabelle hash) che associa ad ogni nodo del grafo i nodi adiacenti. In questo
 * modo il dominio delle chiavi della mappa è il set dei nodi, su cui è
 * possibile chiamare il metodo contains per testare la presenza o meno di un
 * nodo. Ad ogni chiave della mappa, cioè ad ogni nodo del grafo, non è
 * associata una lista concatenata dei nodi collegati, ma un set di oggetti
 * della classe GraphEdge<L> che rappresentano gli archi connessi al nodo: in
 * questo modo la rappresentazione riesce a contenere anche l'eventuale peso
 * dell'arco (memorizzato nell'oggetto della classe GraphEdge<L>). Per
 * controllare se un arco è presente basta richiamare il metodo contains in
 * questo set. I test di presenza si basano sui metodi equals ridefiniti per
 * nodi e archi nelle classi GraphNode<L> e GraphEdge<L>.
 * 
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                etichette dei nodi del grafo
 */
public class MapAdjacentListUndirectedGraph<L> extends Graph<L> {

    /*
     * Le liste di adiacenza sono rappresentate con una mappa. Ogni nodo viene
     * associato con l'insieme degli archi uscenti. Nel caso in cui un nodo non
     * abbia archi uscenti è associato con un insieme vuoto.
     */
    private final Map<GraphNode<L>, Set<GraphEdge<L>>> adjacentLists;

    /**
     * Crea un grafo vuoto.
     */
    public MapAdjacentListUndirectedGraph() {
        // Inizializza la mappa con la mappa vuota
        this.adjacentLists = new HashMap<GraphNode<L>, Set<GraphEdge<L>>>();
    }

    @Override
    public int nodeCount() {
        return this.adjacentLists.size();
    }

    @Override
    public int edgeCount() {
        int count = 0;
        // Scorro le entry della map
        for (Map.Entry<GraphNode<L>, Set<GraphEdge<L>>> entry : this.adjacentLists
                .entrySet())
            // per ogni entry sommo il numero di archi
            count += entry.getValue().size();
        // ritorno la metà in quanto gli archi appaiono due volte in un grafo
        // non orientato
        return count / 2;
    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa grafi non orientati
        return false;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.adjacentLists.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Inserimento di nodo null");
        // Cerco il nodo
        if (this.adjacentLists.keySet().contains(node))
            return false;
        // Aggiungo il nodo con insieme degli archi associati vuoto
        this.adjacentLists.put(node, new HashSet<GraphEdge<L>>());
        return true;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException(
                    "Tentativo di rimuovere un nodo null");
        throw new UnsupportedOperationException(
                "Rimozione dei nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException("Ricerca di nodo nullo");
        // Utilizzo il contains del keyset
        return this.adjacentLists.keySet().contains(node);
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di ricercare un nodo con etichetta null");
        // Ricerco un nodo con l'etichetta L nell'insieme dei nodi
        for (GraphNode<L> n : this.adjacentLists.keySet())
            // Controllo l'etichetta
            if (n.getLabel().equals(label))
                return n;
        // l'etichetta non è presente nell'insieme dei nodi
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null)
            throw new NullPointerException(
                    "Tentativo di ricercare un nodo con etichetta null");
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        throw new UnsupportedOperationException(
                "Ricerca dei nodi con indice non supportata");
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException(
                    "Ricerca di nodi adiacenti a un nodo nullo");
        if (!this.containsNode(node))
            throw new IllegalArgumentException(
                    "Ricerca di nodi adiacenti a un nodo non esistente");
        // Creo il set risultato
        Set<GraphNode<L>> r = new HashSet<GraphNode<L>>();
        // Scorro il set degli archi adiacenti e colleziono i nodi trovati in un
        // set
        for (GraphEdge<L> e : this.adjacentLists.get(node)) {
            if (!e.getNode1().equals(node))
                r.add(e.getNode1());
            if (!e.getNode2().equals(node))
                r.add(e.getNode2());
        }
        return r;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Ricerca dei nodi predecessori non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // Creo l'insieme risultato
        Set<GraphEdge<L>> r = new HashSet<GraphEdge<L>>();
        // Scorro la mappa e colleziono tutti gli archi, quelli ripetuti non
        // saranno inseriti due volte, basandosi sull'equals tra archi
        for (Map.Entry<GraphNode<L>, Set<GraphEdge<L>>> entry : this.adjacentLists
                .entrySet())
            r.addAll(entry.getValue());
        return r;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // Controlli richiesti
        if (edge == null)
            throw new NullPointerException("Inserimento di arco nullo");
        if (edge.isDirected())
            throw new IllegalArgumentException(
                    "Inserimento di un arco orientato in un grafo non orientato");
        if (!this.containsNode(edge.getNode1())
                || !this.containsNode(edge.getNode2()))
            throw new IllegalArgumentException(
                    "Inserimento di un arco in cui almeno uno dei due nodi "
                            + "collegati non esiste in questo grafo");
        // Controllo se l'arco è già presente
        if (this.containsEdge(edge))
            return false;
        // Inserisco l'arco nel set di archi di ogni nodo collegato
        this.adjacentLists.get(edge.getNode1()).add(edge);
        this.adjacentLists.get(edge.getNode2()).add(edge);
        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException(
                "Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        if (edge == null)
            throw new NullPointerException("Ricerca di arco nullo");
        // Controllo esistenza nodi
        if (!this.containsNode(edge.getNode1())
                || !this.containsNode(edge.getNode2()))
            throw new IllegalArgumentException(
                    "Ricerca di un arco in cui almeno uno dei due nodi "
                            + "collegati non esiste in questo grafo");
        // cerco se l'arco si trova nel set collegato a uno dei due nodi
        return this.adjacentLists.get(edge.getNode1()).contains(edge);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null)
            throw new NullPointerException(
                    "Ricerca degli archi di un nodo nullo");
        if (!this.containsNode(node))
            throw new IllegalArgumentException(
                    "Ricerca degli archi di un nodo non esistente");
        // Consulto la mappa
        return this.adjacentLists.get(node);
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Archi entranti non significativi in un grafo non orientato");
    }

}
