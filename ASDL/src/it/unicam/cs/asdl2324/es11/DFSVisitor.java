package it.unicam.cs.asdl2324.es11;
/**
 * Classe singoletto che fornisce lo schema generico di visita Depth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class DFSVisitor<L> {

    // Variabile "globale" per far andare avanti il tempo durante la DFS e
    // assegnare i relativi tempi di scoperta e di uscita dei nodi
    // E' protected per permettere il test JUnit
    protected int time;

    /**
     * Esegue la visita in profondità di un certo grafo. Setta i valori seguenti
     * valori associati ai nodi: tempo di scoperta, tempo di fine visita,
     * predecessore. Ogni volta che un nodo viene visitato viene eseguito il
     * metodo visitNode sul nodo. In questa classe il metodo non fa niente,
     * basta creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *              il grafo da visitare.
     * @throws NullPointerException
     *                                  se il grafo passato è null
     */
    public void DFSVisit(Graph<L> g) {
        // NOTA: inizializza il grafo e chiama la recDFS sui nodi in un ordine

        // qualsiasi per calcolare la "foresta" DFS
        if(g == null)
            throw new NullPointerException("Parametro nullo non valido");
        //scorre in tutti i nodi presenti nel grafo
        for(GraphNode<L> current : g.getNodes()){
            current.setColor(GraphNode.COLOR_WHITE); //imposta il colore del nodo a bianco
            current.setPrevious(null); //imposta il precedente a null
        }
        this.time = 0; //inizializza il time a 0
        //scorre in tutti i nodi del grafo
        for(GraphNode<L> current : g.getNodes()){
            //se il nodo attuale è bianco
            if(current.getColor() == GraphNode.COLOR_WHITE)
                //chiama il metodo interno (DFS-visit(u) sulle slide)
                this.recDFS(g, current);
        }
    }

    /*
     * Esegue la DFS ricorsivamente sul nodo passato.
     * 
     * @param g il grafo
     * 
     * @param u il nodo su cui parte la DFS
     */
    protected void recDFS(Graph<L> g, GraphNode<L> u) {
        // NOTA: chiamare il metodo visitNode alla "scoperta" di un nuovo nodo

        this.time++; //incrementa di uno il time
        u.setEnteringTime(time); //imposta il tempo di ingresso del nodo passato
        u.setColor(GraphNode.COLOR_GREY); //imposta il colore del nodo a grigio
        //scorre sulla lista di nodi adiacenti del nodo passato
        for(GraphNode<L> current : g.getAdjacentNodesOf(u)){
            //se il nodo corrente nello scorrimento è bianco quindi è adiacente al nodo passato come parametro
            //ma non è ancora stato visitato
            if(current.getColor() == GraphNode.COLOR_WHITE){
                current.setPrevious(u); //imposta come precedente del corrente il nodo passato come parametro
                this.recDFS(g, current); //chiamata ricorsiva sul nodo corrente trovato bianco
            }
        }
        this.visitNode(u); //chiamo il metodo interno visitNode quando il nodo passa da grigio a nero
        u.setColor(GraphNode.COLOR_BLACK); //imposta il colore del nodo a nero (quindi come visitato)
        this.time++; //incrementa il contatore del tempo
        u.setExitingTime(this.time); //imposta il tempo di uscita appena visitato
    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la DFS nel momento in cui il colore passa da grigio
     * a nero. Ridefinire il metodo in una sottoclasse per effettuare azioni
     * specifiche.
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
