package it.unicam.cs.asdl2324.es11;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe singoletto che fornisce lo schema generico di visita Breadth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class BFSVisitor<L> {

    /**
     * Esegue la visita in ampiezza di un certo grafo a partire da un nodo
     * sorgente. Setta i valori seguenti valori associati ai nodi: distanza
     * intera, predecessore. La distanza indica il numero minimo di archi che si
     * devono percorrere dal nodo sorgente per raggiungere il nodo e il
     * predecessore rappresenta il padre del nodo in un albero di copertura del
     * grafo. Ogni volta che un nodo viene visitato viene eseguito il metodo
     * visitNode sul nodo. In questa classe il metodo non fa niente, basta
     * creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *                   il grafo da visitare.
     * @param source
     *                   il nodo sorgente.
     * @throws NullPointerException
     *                                      se almeno un valore passato è null
     * @throws IllegalArgumentException
     *                                      se il nodo sorgente non appartiene
     *                                      al grafo dato
     */
    public void BFSVisit(Graph<L> g, GraphNode<L> source) {
        // NOTA: chiamare il metodo visitNode quando un nodo passa da grigio a nero

        if(g == null || source == null)
            throw new NullPointerException("Parametri nulli non validi");
        if(!(g.containsNode(source)))
            throw new IllegalArgumentException("Nodo non appartenente al grafo");

        //for each nodo u nel Grafo, inizializza i nodi con questi parametri
        for(GraphNode<L> current :  g.getNodes()){
            //se è il vertice sorgente skippa la procedura
            if(current.equals(source))
                continue;
            current.setColor(GraphNode.COLOR_WHITE); //setta il colore del nodo a bianco
            //setta la distanza del nodo approssimativamente ad infinito
            current.setIntegerDistance(Integer.MAX_VALUE);
            current.setPrevious(null);
        }
        //setta il source a grey
        source.setColor(GraphNode.COLOR_GREY);
        //setta la distanza del source a 0
        source.setIntegerDistance(0);
        //setta il parent del source a null
        source.setPrevious(null);

        Queue<GraphNode<L>> queue = new LinkedList<>(); //inizializza la coda
        queue.add(source); //aggiunge come primo elemento in coda il source del grafo

        GraphNode<L> dequeuedNode; //variabile di appoggio per il ciclo
        while(!(queue.isEmpty())){
            dequeuedNode = queue.poll(); //facciamo il deque dalla Queue
            //per ogni nodo adiacente al nodo attuale
            for(GraphNode<L> current : g.getAdjacentNodesOf(dequeuedNode)){
                // se l'attuale nodo è bianco
                if(current.getColor() == GraphNode.COLOR_WHITE){
                    current.setColor(GraphNode.COLOR_GREY); //imposta il colore del nodo a grigio
                    //imposta la distanza di questo nodo come la somma del dequeuedNode + 1
                    //quindi in sostanza aumenta di uno la distanza del nodo dequeuedNode
                    current.setIntegerDistance(dequeuedNode.getIntegerDistance() + 1);
                    //imposta il precedente dell'attuale come il nodo dequeuedNode
                    current.setPrevious(dequeuedNode);
                    queue.add(current); //aggiunge alla coda questo nodo
                }
            }
            this.visitNode(dequeuedNode); //chiamo il metodo interno visitNode quando il nodo passa da grigio a nero
            dequeuedNode.setColor(GraphNode.COLOR_BLACK); //imposta il colore a nero
        }



    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la BFS quando i nodi passano da grigio a nero.
     * Ridefinire il metodo in una sottoclasse per effettuare azioni specifiche.
     * 
     * @param n
     *              il nodo visitato
     */
    public void visitNode(GraphNode<L> n) {
        /*
         * In questa classe questo metodo non fa niente. Esso può essere
         * ridefinito in una sottoclasse per fare azioni particolari.
         */
    }

}
