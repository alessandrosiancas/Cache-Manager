package cache;

public class FifoCacheManager {
    private final int k; // dimensione della cache
    private final String[] cache; // cache
    private int size = 0; // elementi in cache
    private int cacheMissCount = 0; // numero di cache miss
    private int evicted = 0; // indice circolare per lo sfratto

    public FifoCacheManager(String[] requests, int k) {
        this.k = k;
        this.cache = new String[k]; // cache inizialmente vuota
    }

    public void access(String ri) {
        boolean found = false;
        for (int i = 0; i < size; i++)
            if (cache[i].equals(ri)) {
                found = true;
                break;
            }

        // Cache hit
        if (found) {
            // System.out.println("[FIFO] cache hit per '" + ri + "'");
        }
        // Cache miss: bisogna vedere se c'è spazio
        else {
            cacheMissCount++;

            // Slot libero
            if (size < k) {
                // System.out.println("[FIFO] cache miss, inserito '" + ri + "' in uno slot
                // libero");
                cache[size] = ri;
                size++;
            }

            // Nessuno slot libero: rimozione necessaria
            else {
                // System.out.println("[FIFO] cache miss, rimosso '" + cache[evicted] + "'");
                cache[evicted] = ri;
                evicted = (evicted + 1) % k; // mod k per la gestione circolare
            }
        }
    }

    public int getCacheMissCount() {
        return cacheMissCount;
    }
}