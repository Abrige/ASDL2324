/**
 * 
 */
package it.unicam.cs.asdl2324.mp2;

import java.util.*;

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 * 
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * 
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 * 
 * @author Luca Tesei (template) Mattia Brizi, mattia.brizi@studenti.unicam.it (implementazione)
 *
 * 
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<>();
        this.nodesIndex = new HashMap<>();
    }

    @Override
    public int nodeCount() {
        return this.nodesIndex.size(); //ritorna il numero di nodi nel grafo
    }

    @Override
    public int edgeCount() {
        int edgeCounter = 0;
        // scorre in tutte le arrayList presenti nell'ArrayList
        for(ArrayList<GraphEdge<L>> currentArrayList : this.matrix){
            for(GraphEdge<L> currentEdge : currentArrayList){
                // somma la size di ogni ArrayList contenuto nell'ArrayList (quindi ogni riga)
                // questo perché nelle righe sono contenuti effettivamente gli oggetti GraphEdge<L>
                if(currentEdge != null)
                    edgeCounter++;
            }
        }
        // ritorna il numero di archi contenuti nella matrice di adiacenza
        // (considerando che se tra due nodi non c'è un arco il valore
        // inserito nella matrice è null e quindi non viene contato tra
        // gli altri che invece hanno un valore inserito all'interno in
        // base alla coppia di nodi)
        // diviso due perché essendo un grafo orientato lo stesso arco compare 2 volte
        return (edgeCounter / 2);
    }

    @Override
    public void clear() {
        this.matrix.clear(); // resetta la matrice di adiacenza
        this.nodesIndex.clear(); // resetta la mappa di nodi
    }

    @Override
    public boolean isDirected() {
        // in questo caso il grafo non è orientato quindi ritorna false
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        // controllo parametro
        if(node == null)
            throw new NullPointerException("Parametro nullo non ammesso");

        // #CASO: se il nodo era già presente
        if(this.nodesIndex.containsKey(node))
            return false;

        // #CASO: se il nodo NON era presente
        // Aggiornamento HashMap
        int nodeIndex = this.nodesIndex.size();
        this.nodesIndex.put(node, nodeIndex); //inserisce il nodo nella mappa con l'indice corretto

        // aggiunge un elemento alla fine di ogni riga
        for(ArrayList<GraphEdge<L>> currentArrayList : this.matrix){
            currentArrayList.add(null);
        }

        // creo e inizializzo con null l'ArrayList per l'ultima riga
        ArrayList<GraphEdge<L>> edgeList = new ArrayList<>();

        for(int i = 0; i < nodeIndex + 1; i++){
            edgeList.add(null);
        }

        // aggiunge una nuova riga con la grandezza corretta
        this.matrix.add(edgeList);
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        // controllo parametro
        if(label == null)
            throw new NullPointerException("Parametro nullo non ammesso");
        // #CASO: se il nodo era già presente
        if(this.getNode(label) != null)
            return false;
        // #CASO: se il nodo NON era già presente
        // chiama il metodo sopra addNode passandogli un nodo con il label
        return this.addNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        // #CASO: se il nodo NON è presente
        if(this.getNode(node) == null)
            throw new IllegalArgumentException("Nodo non presente in questo grafo");
        // controllo parametro
        if(node == null)
            throw new NullPointerException("Parametro nullo non ammesso");

        // #CASO: se il nodo è presente
        // rimuove il nodo dalla HashMap
        int indexOfRemovedNode = this.nodesIndex.remove(node); // contiene l'indice del nodo rimosso

        // Reimposta tutti gli indici dei nodi in modo tale da continuare ad essere contigui anche
        // dopo la rimozione di un nodo
        // scorre nelle coppie di valori della HashMap
        int index; // variabile di appoggio
        for(Map.Entry<GraphNode<L>, Integer> currentEntry : this.nodesIndex.entrySet()){
            // contiene l'indice dell'attuale nodo
            index = currentEntry.getValue();
            // per ogni indice maggiore dell'indice del nodo appena rimosso
            if(index > indexOfRemovedNode){
                // decrementiamo il suo indice di 1
                currentEntry.setValue(index - 1);
            }
        }


        // Ri-organizza la matrice di adiacenza alla rimozione di un nodo
        // rimuove la i-esima riga
        this.matrix.remove(indexOfRemovedNode);

        // rimuove la i-esima colonna della matrice rimanente
        Iterator<ArrayList<GraphEdge<L>>> iterator = this.matrix.iterator(); // iteratore di sulle ArrayList
        ArrayList<GraphEdge<L>> currentEdgeArray; // oggetto di appoggio
        while(iterator.hasNext()){
            currentEdgeArray = iterator.next();
            // per ogni array list, quindi per ogni riga,
            // rimuove l'elemento nella i-esima posizione (quindi la colonna)
            currentEdgeArray.remove(indexOfRemovedNode);
        }

    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        // #CASO: se il nodo NON è presente nel grafo
        if(this.getNode(label) == null)
            throw new IllegalArgumentException("Nodo con questa etichetta non presente nel grafo");
        // controllo parametro
        if(label == null)
            throw new NullPointerException("Parametro nullo non valido");

        // #CASO: se il nodo è presente nel grafo
        // chiama il metodo sovrastante passandogli un nodo con la label passata
        this.removeNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        // controllo parametro
        // verifica che l'indice passato sia compreso tra { (0) e ((n° di nodi) - 1) }
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice passato non valido");
        // chiama il metodo di questa classe dove gli passa il nodo invece che l'indice
        this.removeNode(this.getNode(i));
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        // controllo parametro
        if(node == null)
            throw new NullPointerException("Parametro nullo non ammesso");

        // #CASO: se il nodo NON è presente nel grafo
        if(!(this.nodesIndex.containsKey(node)))
            return null;

        // #CASO: se il nodo è presente nel grafo
        // per ogni nodo presente nel grafo
        for(GraphNode<L> currentNode : this.nodesIndex.keySet()){
            // se il nodo passato come parametro è uguale al nodo corrente
            if(currentNode.equals(node))
                return currentNode; // ritorna il puntatore al nodo corrente del grafo
        }
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        // controllo parametro
        if(label == null)
            throw new NullPointerException("Parametro nullo non ammesso");
        // chiama il metodo di questa classe dove gli passa il nodo invece che l'indice
        return this.getNode(new GraphNode<>(label));
    }

    @Override
    public GraphNode<L> getNode(int i) {
        // controllo parametro
        // verifica che l'indice passato sia compreso tra { (0) e ((n° di nodi) - 1) }
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice passato non valido");

        // cerca il nodo che ha quell'indice nella mappa e se non c'è ritorna null
        GraphNode<L> aNode = null;
        for(Map.Entry<GraphNode<L>, Integer> currentEntry : this.nodesIndex.entrySet()){
            if(currentEntry.getValue().equals(i)) {
                aNode = currentEntry.getKey();
                break;
            }
        }
        return aNode;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        // controllo parametro
        if(node == null)
            throw new NullPointerException("Parametro nullo non ammesso");
        if(this.getNode(node) == null)
            throw new IllegalArgumentException("Nodo non presente in questo grafo");

        // ritorna la value assegnata a quel particolare nodo
        return this.nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        // controllo parametro
        if(label == null)
            throw new NullPointerException("Parametro nullo non ammesso");
        if(this.getNode(label) == null)
            throw new IllegalArgumentException("Nodo non presente in questo grafo");

        // ritorna la value assegnata a quel particolare nodo dopo aver trovato il nodo con quella label
        return this.nodesIndex.get(this.getNode(label));
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.nodesIndex.keySet(); // ritorna un set con tutti i nodi
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        // controllo parametro
        if(edge == null)
            throw new NullPointerException("Parametro nullo non ammesso");
        if(this.getNode(edge.getNode1()) == null || this.getNode(edge.getNode2()) == null)
            throw new IllegalArgumentException("Almeno uno dei due nodi non esiste nel grafo ");
        if(edge.isDirected() != this.isDirected())
            throw new IllegalArgumentException("Tipologia di arco passato non valida");

        // #CASO: se l'arco era già presente nel grafo
        if(this.getEdge(edge) != null)
            return false;

        // #CASO: se l'arco NON era già presente nel grafo
        int node1Index = this.getNodeIndexOf(edge.getNode1()); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(edge.getNode2()); //contiene l'indice del secondo nodo nella HashMap

        // inserisce nella matrice l'arco
        this.matrix.get(node1Index).set(node2Index, edge);
        this.matrix.get(node2Index).set(node1Index, edge);
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // controllo parametri
        if(node1 == null || node2 == null)
            throw new NullPointerException("Parametri nulli non ammessi");
        if(this.getNode(node1) == null || this.getNode(node2) == null)
            throw new IllegalArgumentException("Nodi non presenti nel grafo");

        // #CASO: se l'arco era già presente nel grafo
        if(this.getEdge(node1, node2) != null)
            return false;

        // #CASO: se l'arco NON era già presente nel grafo
        int node1Index = this.getNodeIndexOf(node1); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(node1); //contiene l'indice del secondo nodo nella HashMap

        // inserisce nella matrice l'arco
        this.matrix.get(node1Index).set(node2Index, new GraphEdge<>(node1, node2, this.isDirected()));
        this.matrix.get(node2Index).set(node1Index, new GraphEdge<>(node2, node1, this.isDirected()));
        return true;
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        // controllo parametri
        if(node1 == null || node2 == null)
            throw new NullPointerException("Parametri nulli non ammessi");
        if(this.getNode(node1) == null || this.getNode(node2) == null)
            throw new IllegalArgumentException("Nodi non presenti nel grafo");

        // #CASO: se l'arco era già presente nel grafo
        if(this.getEdge(node1, node2) != null)
            return false;

        // #CASO: se l'arco NON era già presente nel grafo
        int node1Index = this.getNodeIndexOf(node1); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(node2); //contiene l'indice del secondo nodo nella HashMap

        // inserisce nella matrice l'arco
        this.matrix.get(node1Index).set(node2Index, new GraphEdge<>(node1, node2, this.isDirected(), weight));
        this.matrix.get(node2Index).set(node1Index, new GraphEdge<>(node2, node1, this.isDirected(), weight));
        return true;
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        // controllo parametri
        if(label1 == null || label2 == null)
            throw new NullPointerException("Parametri nulli non ammessi");
        if(this.getNode(label1) == null || this.getNode(label2) == null)
            throw new IllegalArgumentException("Nodi con queste etichette non presenti nel grafo");

        // #CASO: se l'arco era già presente nel grafo
        if(this.getEdge(label1, label2) != null)
            return false;

        // #CASO: se l'arco NON era già presente nel grafo
        int node1Index = this.getNodeIndexOf(label1); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(label2); //contiene l'indice del secondo nodo nella HashMap

        // inserisce nella matrice l'arco
        this.matrix.get(node1Index).set(node2Index, new GraphEdge<>(new GraphNode<>(label1), new GraphNode<>(label2), this.isDirected()));
        this.matrix.get(node2Index).set(node1Index, new GraphEdge<>(new GraphNode<>(label2), new GraphNode<>(label1), this.isDirected()));
        return true;

    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        // controllo parametri
        if(label1 == null || label2 == null)
            throw new NullPointerException("Parametri nulli non ammessi");
        if(this.getNode(label1) == null || this.getNode(label2) == null)
            throw new IllegalArgumentException("Nodi con queste etichette non presenti nel grafo");

        // #CASO: se l'arco era già presente nel grafo
        if(this.getEdge(label1, label2) != null)
            return false;

        // #CASO: se l'arco NON era già presente nel grafo
        int node1Index = this.getNodeIndexOf(label1); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(label2); //contiene l'indice del secondo nodo nella HashMap

        // inserisce nella matrice l'arco
        this.matrix.get(node1Index).set(node2Index, new GraphEdge<>(new GraphNode<>(label1), new GraphNode<>(label2), this.isDirected(), weight));
        this.matrix.get(node2Index).set(node1Index, new GraphEdge<>(new GraphNode<>(label2), new GraphNode<>(label1), this.isDirected(), weight));
        return true;
    }

    @Override
    public boolean addEdge(int i, int j) {
        // controllo parametri
        // verifica che gli indici passati siano compresi tra { (0) e ((n° di nodi) - 1) }
        if(i < 0 || i >= this.nodeCount() || j < 0 || j >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice passato non valido");

        // #CASO: se l'arco era già presente nel grafo
        if(this.getEdge(i, j) != null)
            return false;

        // #CASO: se l'arco NON era già presente nel grafo
        // inserisce nella matrice l'arco
        this.matrix.get(i).set(j, new GraphEdge<>(this.getNode(i), this.getNode(j), this.isDirected()));
        this.matrix.get(j).set(i, new GraphEdge<>(this.getNode(j), this.getNode(i), this.isDirected()));
        return true;
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        // controllo parametri
        // verifica che gli indici passati siano compresi tra { (0) e ((n° di nodi) - 1) }
        if(i < 0 || i >= this.nodeCount() || j < 0 || j >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice passato non valido");

        // #CASO: se l'arco era già presente nel grafo
        if(this.getEdge(i, j) != null)
            return false;

        // #CASO: se l'arco NON era già presente nel grafo
        // inserisce nella matrice l'arco
        this.matrix.get(i).set(j, new GraphEdge<>(this.getNode(i), this.getNode(j), this.isDirected(), weight));
        this.matrix.get(j).set(i, new GraphEdge<>(this.getNode(j), this.getNode(i), this.isDirected(), weight));
        return true;
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        // controllo parametro
        if(this.getEdge(edge) == null)
            throw new IllegalArgumentException("L'arco passato non esiste nel grafo");
        if(this.getNode(edge.getNode1()) == null || this.getNode(edge.getNode2()) == null)
            throw new IllegalArgumentException("Nodi dell'arco non presenti nel grafo");
        if(edge == null)
            throw new NullPointerException("Parametro nullo non valido");

        // se l'arco era presente e quindi si può rimuovere
        int node1Index = this.getNodeIndexOf(edge.getNode1()); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(edge.getNode2()); //contiene l'indice del secondo nodo nella HashMap

        // rimuove l'arco nella matrice
        this.matrix.get(node1Index).set(node2Index, null);
        this.matrix.get(node2Index).set(node1Index, null);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        // controllo parametri
        if(this.getEdge(node1, node2) == null)
            throw new IllegalArgumentException("Arco non presente nel grafo");
        if(this.getNode(node1) == null || this.getNode(node2) == null)
            throw new IllegalArgumentException("Nodi non presenti nel grafo");
        if(node1 == null || node2 == null)
            throw new NullPointerException("Parametri nulli non ammessi");

        // se l'arco era presente e quindi si può rimuovere
        int node1Index = this.getNodeIndexOf(node1); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(node2); //contiene l'indice del secondo nodo nella HashMap

        // rimuove l'arco nella matrice
        this.matrix.get(node1Index).set(node2Index, null);
        this.matrix.get(node2Index).set(node1Index, null);
    }

    @Override
    public void removeEdge(L label1, L label2) {
        //controllo parametri
        if(this.getEdge(label1, label2) == null)
            throw new IllegalArgumentException("Arco non presente nel grafo");
        if(this.getNode(label1) == null || this.getNode(label2) == null)
            throw new IllegalArgumentException("Nodi non presenti nel grafo");
        if(label1 == null || label2 == null)
            throw new NullPointerException("Parametri nulli non ammessi");

        // se l'arco era presente e quindi si può rimuovere
        int node1Index = this.getNodeIndexOf(label1); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(label2); //contiene l'indice del secondo nodo nella HashMap

        // rimuove l'arco nella matrice
        this.matrix.get(node1Index).set(node2Index, null);
        this.matrix.get(node2Index).set(node1Index, null);
    }

    @Override
    public void removeEdge(int i, int j) {
        // controllo parametri
        if(this.getEdge(i, j) == null)
            throw new IllegalArgumentException("Arco non presente nel grafo");
        // verifica che gli indici passati siano compresi tra { (0) e ((n° di nodi) - 1) }
        if(i < 0 || i >= this.nodeCount() || j < 0 || j >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice passato non valido");

        // se l'arco era presente e quindi si può rimuovere
        // rimuove l'arco nella matrice
        this.matrix.get(i).set(j, null);
        this.matrix.get(j).set(i, null);
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        //controllo parametro
        if(edge == null)
            throw new NullPointerException("Parametri nulli non ammessi");
        if(this.getNode(edge.getNode1()) == null || this.getNode(edge.getNode2()) == null)
            throw new IllegalArgumentException("Nodi dell'arco non presenti nel grafo");

        // variabili di appoggio
        int node1Index = this.getNodeIndexOf(edge.getNode1()); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(edge.getNode2()); //contiene l'indice del secondo nodo nella HashMap

        // prende l'arco nella matrice
        return this.matrix.get(node1Index).get(node2Index);
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        //controllo parametri
        if(node1 == null || node2 == null)
            throw new NullPointerException("Parametri nulli non ammessi");
        if(this.getNode(node1) == null || this.getNode(node2) == null)
            throw new IllegalArgumentException("Nodi non presenti nel grafo");

        // variabili di appoggio
        int node1Index = this.getNodeIndexOf(node1); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(node2); //contiene l'indice del secondo nodo nella HashMap

        // prende l'arco nella matrice
        return this.matrix.get(node1Index).get(node2Index);
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        // controllo parametri
        if(label1 == null || label2 == null)
            throw new NullPointerException("Parametri nulli non ammessi");
        // se una delle due etichette passate non è presente nel grafo
        if(this.getNode(label1) == null || this.getNode(label2) == null)
            throw new IllegalArgumentException("Nodi con queste etichette non presenti nel grafo");

        // variabili di appoggio
        int node1Index = this.getNodeIndexOf(label1); //contiene l'indice del primo nodo nella HashMap
        int node2Index = this.getNodeIndexOf(label2); //contiene l'indice del secondo nodo nella HashMap

        // prende l'arco nella matrice
        return this.matrix.get(node1Index).get(node2Index);
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        // controllo parametri
        // verifica che gli indici passati siano compresi tra { (0) e ((n° di nodi) - 1) }
        if(i < 0 || i >= this.nodeCount() || j < 0 || j >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice passato non valido");

        // prende l'arco nella matrice
        return this.matrix.get(i).get(j);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        // controllo parametri
        // se il nodo passato non è presente nel grafo
        if(this.getNode(node) == null)
            throw new IllegalArgumentException("Nodo non presente in questo grafo");
        if(node == null)
            throw new NullPointerException("Parametro nullo non ammesso");

        // variabile di appoggio
        int nodeIndex = this.getNodeIndexOf(node); //contiene l'indice del nodo nella HashMap

        // set di appoggio che conterrà tutti gli archi
        HashSet<GraphNode<L>> adjacentNodeSet = new HashSet<>();
        // scorre lungo la riga di nodi adiacenti del nodo passato
        for(GraphEdge<L> currentEdge : this.matrix.get(nodeIndex)){
            // se esiste arco in quello slot
            if(currentEdge != null){
                // se il primo nodo è uguale al nostro nodo allora il nodo adiacente è il secondo
                if(currentEdge.getNode1().equals(node))
                    // aggiunge il secondo nodo dell'arco nella matrice di adiacenza al set di nodi adiacenti
                    adjacentNodeSet.add(currentEdge.getNode2());
                // se il secondo nodo è uguale al nostro nodo allora il nodo adiacente è il primo
                else
                    // aggiunge il secondo nodo dell'arco nella matrice di adiacenza al set di nodi adiacenti
                    adjacentNodeSet.add(currentEdge.getNode1());
            }
        }
        // ritorna il set di nodi adiacenti
        return adjacentNodeSet;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        //controllo parametro
        if(this.getNode(label) == null)
            throw new IllegalArgumentException("Nodo non presente in questo grafo");
        if(label == null)
            throw new NullPointerException("Parametro nullo non ammesso");

        // variabile di appoggio
        int nodeIndex = this.getNodeIndexOf(label); //contiene l'indice del nodo nella HashMap
        // variabile di appoggio che punta al nodo avente la label passata
        GraphNode<L> labelNode = this.getNode(label);
        // set di appoggio che conterrà tutti gli archi
        HashSet<GraphNode<L>> adjacentNodeSet = new HashSet<>();
        // scorre lungo la riga di nodi adiacenti del nodo passato
        for(GraphEdge<L> currentEdge : this.matrix.get(nodeIndex)){
            // se esiste arco in quello slot
            if(currentEdge != null){
                // se il primo nodo è uguale al nostro nodo allora il nodo adiacente è il secondo
                if(currentEdge.getNode1().equals(labelNode))
                    // aggiunge il secondo nodo dell'arco nella matrice di adiacenza al set di nodi adiacenti
                    adjacentNodeSet.add(currentEdge.getNode2());
                    // se il secondo nodo è uguale al nostro nodo allora il nodo adiacente è il primo
                else
                    // aggiunge il secondo nodo dell'arco nella matrice di adiacenza al set di nodi adiacenti
                    adjacentNodeSet.add(currentEdge.getNode1());
            }
        }
        // ritorna il set di nodi adiacenti
        return adjacentNodeSet;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        //controllo parametri
        //verifica che gli indici passati siano compresi tra { (0) e ((n° di nodi) - 1) }
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice passato non valido");

        // variabile di appoggio che punta al nodo avente la label passata
        GraphNode<L> labelNode = this.getNode(i);
        // set di appoggio che conterrà tutti gli archi
        HashSet<GraphNode<L>> adjacentNodeSet = new HashSet<>();
        // scorre lungo la riga di nodi adiacenti del nodo passato
        for(GraphEdge<L> currentEdge : this.matrix.get(i)){
            // se esiste arco in quello slot
            if(currentEdge != null){
                // se il primo nodo è uguale al nostro nodo allora il nodo adiacente è il secondo
                if(currentEdge.getNode1().equals(labelNode))
                    // aggiunge il secondo nodo dell'arco nella matrice di adiacenza al set di nodi adiacenti
                    adjacentNodeSet.add(currentEdge.getNode2());
                    // se il secondo nodo è uguale al nostro nodo allora il nodo adiacente è il primo
                else
                    // aggiunge il secondo nodo dell'arco nella matrice di adiacenza al set di nodi adiacenti
                    adjacentNodeSet.add(currentEdge.getNode1());
            }
        }
        // ritorna il set di nodi adiacenti
        return adjacentNodeSet;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        //controllo parametro
        if(this.getNode(node) == null)
            throw new IllegalArgumentException("Nodo non presente in questo grafo");
        if(node == null)
            throw new NullPointerException("Parametro nullo non ammesso");

        // variabile di appoggio
        int nodeIndex = this.getNodeIndexOf(node); //contiene l'indice del nodo nella HashMap

        // set di appoggio che conterrà tutti gli archi
        HashSet<GraphEdge<L>> edgeSet = new HashSet<>();
        // scorre lungo la riga di nodi adiacenti del nodo passato
        for(GraphEdge<L> currentEdge : this.matrix.get(nodeIndex)){
            // se esiste un arco in quello slot
            if(currentEdge != null)
                // aggiunge l'arco al set di archi adiacenti
                edgeSet.add(currentEdge);
        }
        return edgeSet; // ritorna il set di archi

    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        //controllo parametro
        if(this.getNode(label) == null)
            throw new IllegalArgumentException("Nodo non presente in questo grafo");
        if(label == null)
            throw new NullPointerException("Parametro nullo non ammesso");

        // variabile di appoggio
        int nodeIndex = this.getNodeIndexOf(label); //contiene l'indice del nodo nella HashMap

        // set di appoggio che conterrà tutti gli archi
        HashSet<GraphEdge<L>> edgeSet = new HashSet<>();
        // scorre lungo le colonne della riga del nodo passato
        for(GraphEdge<L> currentEdge : this.matrix.get(nodeIndex)){
            // se esiste un arco in quello slot
            if(currentEdge != null)
                // aggiunge l'arco al set di archi adiacenti
                edgeSet.add(currentEdge);
        }
        return edgeSet; // ritorna il set di archi adiacenti
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        //controllo parametro
        //verifica che l'indice passato sia compreso tra { (0) e ((n° di nodi) - 1) }
        if(i < 0 || i >= this.nodeCount())
            throw new IndexOutOfBoundsException("Indice passato non valido");

        // set di appoggio che conterrà tutti gli archi
        HashSet<GraphEdge<L>> edgeSet = new HashSet<>();
        // scorre lungo le colonne della riga del nodo passato
        for(GraphEdge<L> currentEdge : this.matrix.get(i)){
            // se esiste un arco in quello slot
            if(currentEdge != null)
                // aggiunge l'arco al set di archi adiacenti
                edgeSet.add(currentEdge);
        }
        return edgeSet; // ritorna il set di archi

    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // set di appoggio che conterrà tutti gli archi
        HashSet<GraphEdge<L>> edgeSet = new HashSet<>();
        // scorre in tutte le righe della matrice
        for(ArrayList<GraphEdge<L>> currentArrayList : this.matrix) {
            // scorre in tutte le colonne della matrice
            for(GraphEdge<L> currentEdge : currentArrayList){
                // se esiste un arco in quello slot
                if(currentEdge != null)
                    edgeSet.add(currentEdge); // aggiunge quell'arco al set di archi
            }
        }
        return edgeSet; // ritorna il set di archi
    }
}
