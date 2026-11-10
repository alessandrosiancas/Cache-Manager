package cache;

public class LifoCacheManager {
    private final int k; // dimensione della cache
    private final String[] cache; // cache
    private int size = 0; // elementi in cache
    private int cacheMissCount = 0; // numero di cache miss

    public LifoCacheManager(String[] requests, int k) {
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
            // System.out.println("[LIFO] cache hit per '" + ri + "'");
        }
        // Cache miss: bisogna vedere se c'è spazio
        else {
            cacheMissCount++;

            // Slot libero
            if (size < k) {
                // System.out.println("[LIFO] cache miss, inserito '" + ri + "' in uno slot
                // libero");
                cache[size] = ri;
                size++;
            }

            // Nessuno slot libero: rimozione necessaria
            else {
                // System.out.println("[LIFO] cache miss, rimosso '" + cache[k - 1] + "'");
                cache[k - 1] = ri; // ri viene messo nell'ultima posizione occupata
            }
        }
    }

    public int getCacheMissCount() {
        return cacheMissCount;
    }
}