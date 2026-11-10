package datastructures;

public class RBNode {
    public String key; // nome della risorsa ("d" oppure "b")
    public RBNode left, right, parent;
    public boolean color; // true=RED, false=BLACK
    public HeapNode heapNode; // riferimento al nodo del max-heap

    public RBNode(String key) {
        this.key = key;
        this.color = true; // nodo nuovo RED
    }
}