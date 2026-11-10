package cache;

import datastructures.*;
import java.util.HashMap;
import java.util.LinkedList;

public class OptimalCacheManager {
    private final int k; // dimensione massima della cache (k)
    private final RBTree T; // albero RB (T)
    private final MaxHeap P; // max-heap (P)
    private final HashMap<String, LinkedList<Integer>> H; // tavola hash delle liste (H, S)

    private int cacheMissCount = 0;

    // L'equivalente di INITIALIZE-CACHE(R,k)
    public OptimalCacheManager(String[] requests, int k) {
        this.k = k;
        this.T = new RBTree();
        this.P = new MaxHeap();
        this.H = new HashMap<>();

        // Costruzione della tavola hash e liste
        for (int i = 0; i < requests.length; i++) {
            String ri = requests[i];

            // Se ri non è presente in H
            if (!H.containsKey(ri)) { // HASH-SEARCH(ri) == NIL
                H.put(ri, new LinkedList<>()); // nuova lista
            }

            H.get(ri).add(i + 1); // istante di tempo i+1 perché parto da i=0
        }
    }

    // L'equivalente di ACCESS(ri)
    public void access(String ri) {
        // Recupero la lista associata alla risorsa
        LinkedList<Integer> S_ind = H.get(ri);
        int time = Integer.MAX_VALUE; // time=INF

        // Rimuovo la testa
        if (S_ind != null && !S_ind.isEmpty())
            S_ind.removeFirst();

        // Se S_ind non è vuota: time=testa di S_ind
        if (S_ind != null && !S_ind.isEmpty())
            time = S_ind.getFirst();

        RBNode ri_node = T.search(ri);

        // Cache hit
        if (ri_node != T.getNil()) {
            // System.out.println("[OPTIMAL] cache hit per '" + ri + "'");

            // Aggiornamento della priorità nel max-heap
            HeapNode event = ri_node.heapNode;
            P.changePriority(event, time);
        }

        // Cache miss con almeno uno slot libero
        else if (T.treeSize < k) {
            cacheMissCount++;
            // System.out.println("[OPTIMAL] cache miss, inserito '" + ri + "' in uno slot
            // libero");

            // Creazione nodi nell'albero RB e nel max-heap
            RBNode z = new RBNode(ri);
            HeapNode event = new HeapNode(time, ri);

            // Assegnazione riferimenti reciproci
            z.heapNode = event;
            event.rbNode = z;

            // Inserimetno nelle relative strutture
            T.insert(z);
            P.insert(event);
        }

        // Cache miss e nessuno slot libero
        else {
            cacheMissCount++;
            HeapNode event = P.maximum(); // event = MAXIMUM(P)

            // Se il tempo dell'elemento più lontano è minore o uguale a quello corrente
            if (event.key <= time) {
                // System.out.println("[OPTIMAL] cache miss, nessun elemento rimosso");
            }
            // Scelta golosa: tolgo l'elemento che verrà richiamato più lontano nel tempo
            else {
                // System.out.println("[OPTIMAL] cache miss, rimosso '" + event.value + "'");

                // Rimozione dell'elemento nel max-heap e nell'albero RB (grazie al riferimento)
                event = P.extractMax();
                T.delete(event.rbNode);

                // Setting del nuovo elemento per il max-heap
                event.key = time;
                event.value = ri;

                // Setting del nuovo elemento per l'albero RB
                RBNode z = new RBNode(ri);
                z.heapNode = event;
                event.rbNode = z;

                // Inserimento nelle due strutture
                P.insert(event);
                T.insert(z);
            }
        }
    }

    public int getCacheMissCount() {
        return cacheMissCount;
    }
}