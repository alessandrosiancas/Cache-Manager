package datastructures;

public class HeapNode {
    public int key; // priorità del nodo (istante prossima chiamata)
    public String value; // nome risorsa
    public int heapIndex; // indice del nodo nell'array (ArrayList)
    public RBNode rbNode; // riferimento al nodo nell'albero rosso-nero

    // Memorizzare heapIndex rende più semplice individuare un elemento, evitando la
    // scansione lineare dell'ArrayList

    public HeapNode(int key, String value) {
        this.key = key;
        this.value = value;
        this.heapIndex = -1; // nodo inizialmente non inserito
    }
}