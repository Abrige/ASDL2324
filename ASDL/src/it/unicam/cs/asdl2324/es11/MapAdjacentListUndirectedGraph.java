/**
 * 
 */
package it.unicam.cs.asdl2324.es11;

import java.util.*;

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
        this.adjacentLists = new HashMap<>();
    }

    @Override
    public int nodeCount() {
        return this.adjacentLists.size(); //ritorna il numero di key-value presenti nella mappa
    }

    //NON è ottimizzato
    @Override
    public int edgeCount() {
        //crea un iteratore sui valori della mappa VALUE
        Iterator<Set<GraphEdge<L>>> iterator = this.adjacentLists.values().iterator();
        Set<GraphEdge<L>> currentSet; //variabile di appoggio che contiene il set corrente nello scorrimento
        Set<GraphEdge<L>> edgeSet = new HashSet<>(); //nuovo set che conterrà tutti gli edges distinti
        //scorre
        while(iterator.hasNext()){
            //prende la value del set attuale (che nel nostro caso sarebbe un set contente i nodi adiacenti)
             currentSet = iterator.next();
             if(currentSet == null)
                 continue;
             //aggiunge il set attuale al set di edges distinti (quindi i doppioni non verranno aggiunti)
             edgeSet.addAll(currentSet);
        }
        //ritorna la grandezza del set di edges distinti quindi quanti archi ci sono
        return edgeSet.size();
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
        //se è vuoto ritorna un set vuoto
        if(this.isEmpty())
            return new HashSet<>();
        return this.adjacentLists.keySet(); //ritorna un set di chiavi (nel nostro caso le chiavi sono i nodi)
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        //controllo parametri
        if(node == null)
            throw new NullPointerException("Parametro nullo non valido");
        //se il nodo non era presente
        if(!(this.adjacentLists.containsKey(node))){
            this.adjacentLists.put(node, null); //inserisce il nodo nella mappa
            return true;
        }
        return false;
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
        //controllo parametri
        if (node == null)
            throw new NullPointerException("Parametro nullo non valido");
        //ricerca il nodo nelle key della hashmap e in caso positivo ritorna true
        return this.adjacentLists.containsKey(node);
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        //controllo parametri
        if (label == null)
            throw new NullPointerException("Parametro nullo non valido");
        //crea un iteratore che scorre nelle KEY dell'hashmap
        Iterator<GraphNode<L>> iterator = this.adjacentLists.keySet().iterator();
        GraphNode<L> current; //variabile di appoggio per il nodo corrente durante l'iterazione
        while(iterator.hasNext()){
            current = iterator.next();
            //caso in cui il nodo corrente ha label uguale alla label passata come parametro
            if(current.getLabel().equals(label))
                return current;
        }
        //se non è stato trovato nessun nodo che ha la label passata ritorna null
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
        if(!(this.containsNode(node)))
            throw new IllegalArgumentException("Argomento passato non valido");
        //controllo parametri
        if (node == null)
            throw new NullPointerException("Parametro nullo non valido");
        if(this.adjacentLists.get(node) == null)
            return new HashSet<>();
        //iteratore nella lista di archi adiacenti del nodo passato
        Iterator<GraphEdge<L>> iterator = this.adjacentLists.get(node).iterator();
        GraphEdge<L> currentEdge; //variabile di appoggio per gli archi
        GraphNode<L> currentNode; //variabile di appoggio per i nodi
        Set<GraphNode<L>> adjacentNodesSet = new HashSet<>(); //set che verrà popolato con i nodi adiacenti
        //scorre nel set di archi del nodo passato
        while(iterator.hasNext()){
            currentEdge = iterator.next();
            //prende il secondo riferimento dell'arco supponendo che come primo ci sia il nodo passato
            currentNode = currentEdge.getNode2();
            adjacentNodesSet.add(currentNode); //aggiunge il nodo al set
        }
        return adjacentNodesSet; //ritorna il set di nodi adiacenti
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Non è un grafo orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        //crea un iteratore sui valori della mappa VALUE
        Iterator<Set<GraphEdge<L>>> iterator = this.adjacentLists.values().iterator();
        Set<GraphEdge<L>> currentSet; //variabile di appoggio che contiene il set corrente nello scorrimento
        Set<GraphEdge<L>> edgeSet = new HashSet<>(); //nuovo set che conterrà tutti gli edges distinti
        //scorre
        while(iterator.hasNext()){
            //prende la value del set attuale (che nel nostro caso sarebbe un set contente i nodi adiacenti)
            currentSet = iterator.next();
            if(currentSet == null)
                continue;
            //aggiunge il set attuale al set di edges distinti (quindi i doppioni non verranno aggiunti)
            edgeSet.addAll(currentSet);
        }
        //ritorna il set di edges distinti
        return edgeSet;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if(edge == null)
            throw new NullPointerException("Argomento nullo non valido");
        if(edge.getNode1() == null || edge.getNode2() == null)
            throw new IllegalArgumentException("Argomento passato non valido");
        if(edge.isDirected() != this.isDirected())
            throw new IllegalArgumentException("Argomento passato non valido");
        //se l'arco era già presente
        if(this.containsEdge(edge))
            return false;

        //crea delle copie dei set con gli archi
        Set<GraphEdge<L>> edgeSet1 = this.adjacentLists.get(edge.getNode1());
        Set<GraphEdge<L>> edgeSet2 = this.adjacentLists.get(edge.getNode2());
        if(edgeSet1 == null) {
            edgeSet1 = new HashSet<>();
        }
        if(edgeSet2 == null){
            edgeSet2 = new HashSet<>();
        }
        edgeSet1.add(edge); //aggiunge l'arco al set degli archi del primo nodo
        edgeSet2.add(edge); //aggiunge l'arco al set degli archi del secondo nodo
        this.adjacentLists.put(edge.getNode1(), edgeSet1); //inserisce il nuovo arco nella lista di archi del primo nodo
        this.adjacentLists.put(edge.getNode2(), edgeSet2); //inserisce il nuovo arco nella lista di archi del primo nodo
        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException(
                "Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        if(edge == null)
            throw new NullPointerException("Argomento nullo non valido");
        if(!(this.containsNode(edge.getNode1()) && this.containsNode(edge.getNode2())))
            throw new IllegalArgumentException("Argomento passato non valido");

        //crea un iteratore sui valori della mappa VALUE
        Iterator<Set<GraphEdge<L>>> iterator = this.adjacentLists.values().iterator();
        Set<GraphEdge<L>> currentSet; //variabile di appoggio che contiene il set corrente nello scorrimento
        while(iterator.hasNext()){
            //prende la value del set attuale (che nel nostro caso sarebbe un set contente i nodi adiacenti)
            currentSet = iterator.next();
            if(currentSet == null)
                continue;
            //controlla se nel set di archi è presente l'arco passato
            if(currentSet.contains(edge))
                return true;
        }
        return false;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        //controllo parametri
        if(!(this.containsNode(node)))
            throw new IllegalArgumentException("Parametro passato non valido");
        if(node == null)
             throw new NullPointerException("Parametri nulli non validi");
        Set<GraphEdge<L>> edgeSet = this.adjacentLists.get(node);
        //se non è ancora stato inizializzato il set di archi
        if(edgeSet == null)
            return new HashSet<>(); //ritorna un hashset vuoto
        return edgeSet; //altrimenti ritorna il set di archi che ha quel nodo
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Archi entranti non significativi in un grafo non orientato");
    }

}
