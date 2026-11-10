package datastructures;

import java.util.ArrayList;

public class MaxHeap {
    private final ArrayList<HeapNode> heap;

    public MaxHeap() {
        this.heap = new ArrayList<>();
    }

    // Restituisce il massimo (la radice), se esiste
    public HeapNode maximum() {
        if (heap.isEmpty())
            return null;
        return heap.get(0);
    } // Θ(1)

    // Rimuove e restituisce il massimo (la radice)
    public HeapNode extractMax() {
        if (heap.isEmpty()) // heap vuoto
            return null;

        HeapNode max = heap.get(0);
        HeapNode lastNode = heap.get(heap.size() - 1);

        // Copio l'ultimo elemento nella radice
        heap.set(0, lastNode);
        lastNode.heapIndex = 0;

        // Rimuovo l'ultimo elemento
        heap.remove(heap.size() - 1);

        // Risolvo eventuali violazioni
        if (!heap.isEmpty()) // per ArrayList devo verificare che l'heap non sia vuoto
            maxHeapify(0);

        max.heapIndex = -1;
        return max;
    } // O(log k)

    // Inserisce il nodo in fondo e lo fa risalire
    public void insert(HeapNode node) {
        heap.add(node);
        node.heapIndex = heap.size() - 1;

        int i = node.heapIndex; // oppure heap.size()-1
        while (i > 0 && heap.get(parent(i)).key < heap.get(i).key) {
            swap(i, parent(i)); // scambio il nodo con il genitore
            i = parent(i);
        }
    } // O(log k)

    // L'equivalente di INCREASE-KEY, il cambio nome è solo per comprensibilità
    public void changePriority(HeapNode node, int newKey) {
        int i = node.heapIndex;
        if (i == -1 || newKey <= node.key) // controllo di sicurezza
            return;

        node.key = newKey;
        while (i > 0 && heap.get(parent(i)).key < heap.get(i).key) {
            swap(i, parent(i));
            i = parent(i);
        }
    } // O(log k)

    // Ripristina la proprietà di max-heap scorrendo verso il basso (sift-down)
    private void maxHeapify(int i) {
        int l = left(i);
        int r = right(i);
        int max = i;

        if (l < heap.size() && heap.get(l).key > heap.get(max).key) {
            max = l;
        }
        if (r < heap.size() && heap.get(r).key > heap.get(max).key) {
            max = r;
        }

        if (max != i) {
            swap(i, max); // scambio il nodo col massimo individuato
            maxHeapify(max); // chiamata sul nodo scambiato per gestire nuove possibili violazioni
        }
    } // O(log k)

    // Definisco lo scambio appoggiandomi a metodi di ArrayList
    private void swap(int i, int j) {
        HeapNode temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);

        // Aggiornamento dei nuovi indici dei nodi
        heap.get(i).heapIndex = i;
        heap.get(j).heapIndex = j;
    } // O(1)

    // Gli indici partono da 0, non più da 1, bisogna ridefinire dei calcoli
    private int parent(int i) {
        return (i - 1) / 2;
    } // Θ(1)

    private int left(int i) {
        return (2 * i) + 1;
    } // Θ(1)

    private int right(int i) {
        return (2 * i) + 2;
    } // Θ(1)

    public boolean isEmpty() {
        return heap.isEmpty(); // opera con l'operazione dell'ArrayList
    } // Θ(1)
}