package cache;

public class NaiveCacheManager {
    private final int k; // dimensione della cache
    private final String[] cache; // cache
    private final String[] requests; // array delle richieste
    private int size = 0; // elementi in cache
    private int cacheMissCount = 0; // numero di cache miss
    private int time = 0; // scorrimento temporale (da 0 in poi)

    // L'equivalente di INITIALIZE-CACHE(R,k)
    public NaiveCacheManager(String[] requests, int k) {
        this.k = k;
        this.requests = requests;
        this.cache = new String[k]; // cache vuota
    }

    // L'equivalente di ACCESS(ri)
    public void access(String ri) {
        // Verifica se ri è nella cache
        boolean found = false;
        for (int i = 0; i < size; i++)
            if (cache[i].equals(ri)) {
                found = true;
                break;
            }

        // Cache hit
        if (found) {
            // System.out.println("[NAIVE] cache hit per '" + ri + "'");
        }
        // Cache miss: bisogna vedere se c'è spazio
        else {
            cacheMissCount++;

            // Slot libero
            if (size < k) {
                // System.out.println("[NAIVE] cache miss, inserito '" + ri + "' in uno slot
                // libero");
                cache[size] = ri;
                size++;
            }

            // Nessuno slot libero: rimozione necessaria
            else {
                int evictedInd = -1;
                int evictedNextCall = -1;
                // Scorrimento elementi in cache
                for (int j = 0; j < k; j++) {
                    String rj = cache[j];

                    // Cerco la prossima chiamata di rj
                    int rjNextCall = Integer.MAX_VALUE; // time=INF
                    for (int t = time + 1; t < requests.length; t++)
                        if (requests[t].equals(rj)) {
                            rjNextCall = t;
                            break;
                        }

                    if (rjNextCall > evictedNextCall) {
                        evictedInd = j;
                        evictedNextCall = rjNextCall;
                    }
                }

                // Cerco la prossima chiamata di ri
                int riNextCall = Integer.MAX_VALUE;
                for (int t = time + 1; t < requests.length; t++)
                    if (requests[t].equals(ri)) {
                        riNextCall = t;
                        break;
                    }

                // Scelgo se fare lo sfratto o meno
                if (evictedNextCall <= riNextCall) {
                    // System.out.println("[NAIVE] cache miss, nessun elemento rimosso");
                } else {
                    // System.out.println("[NAIVE] cache miss, rimosso '" + cache[evictedInd] +
                    // "'");
                    cache[evictedInd] = ri;
                }
            }
        }

        // Si passa al prossimo istante di tempo
        time++;
    }

    public int getCacheMissCount() {
        return cacheMissCount;
    }
}