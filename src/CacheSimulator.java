import cache.*;
import utils.FileUtils;
import java.util.Scanner;

public class CacheSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String firstLine = "N;K;MissOptimal;MissNaive;MissFIFO;MissLIFO;TimeOptimal(ms);TimeNaive(ms);TimeFIFO(ms);TimeLIFO(ms)";

        System.out.println("=================================================");
        System.out.println("               SIMULATORE DI CACHE               ");
        System.out.println("=================================================");
        System.out.println("1) Modalità Presentazione");
        System.out.println("2) Modalità Live");
        System.out.print("Scegli un'opzione (1 o 2): ");

        int scelta = scanner.nextInt();
        System.out.println("=================================================\n");

        if (scelta == 1) {

            String[] files = {
                    "input/n5000_m200.txt",
                    "input/n25000_m200.txt",
                    "input/n50000_m200.txt",
                    "input/n250000_m200.txt",
                    "input/n500000_m200.txt"
            };

            /*
             * String[] files = {
             * "input/n5000_m400.txt",
             * "input/n25000_m400.txt",
             * "input/n50000_m400.txt",
             * "input/n250000_m400.txt",
             * "input/n500000_m400.txt"
             * };
             */

            int[] lengths = { 5000, 25000, 50000, 250000, 500000 };

            // DIMENSIONE CACHE
            int k = 100;

            // NUMERO RISORSE DISTINTE
            int numDistinctResources = 400;

            String csvPath = "output/risultati_benchmark.csv";

            System.out.println("> ESECUZIONE FILE PRE-CONFIGURATI\n");
            System.out.printf("> Dimensione Cache (K): " + k + "\n");
            System.out.println("> Numero risorse distinte (M): " + numDistinctResources + "\n");

            FileUtils.writeCsvLine(csvPath, firstLine, false);

            // Intestazione tabella
            System.out.printf("%-20s %-10s %-15s %-15s %-15s %-15s\n", "File Elaborato", "N", "T_Optimal(ms)",
                    "T_Naive(ms)", "T_FIFO(ms)", "T_LIFO(ms)");
            System.out.println(
                    "-------------------------------------------------------------------------------------------------");

            // Scorrimento e risoluzione dei file
            for (int i = 0; i < files.length; i++) {
                String pathAttuale = files[i];
                int numRequests = lengths[i]; // Dichiarazione esplicita della lunghezza attuale

                // Caricamento file
                String[] richieste = FileUtils.readRequests(pathAttuale, numRequests);

                OptimalCacheManager optM = new OptimalCacheManager(richieste, k);
                long start = System.nanoTime();
                for (int j = 0; j < richieste.length; j++) {
                    optM.access(richieste[j]);
                }
                long tOptimal = System.nanoTime() - start;

                NaiveCacheManager naiM = new NaiveCacheManager(richieste, k);
                start = System.nanoTime();
                for (int j = 0; j < richieste.length; j++) {
                    naiM.access(richieste[j]);
                }
                long tNaive = System.nanoTime() - start;

                FifoCacheManager fifoM = new FifoCacheManager(richieste, k);
                start = System.nanoTime();
                for (int j = 0; j < richieste.length; j++) {
                    fifoM.access(richieste[j]);
                }
                long tFifo = System.nanoTime() - start;

                LifoCacheManager lifoM = new LifoCacheManager(richieste, k);
                start = System.nanoTime();
                for (int j = 0; j < richieste.length; j++) {
                    lifoM.access(richieste[j]);
                }
                long tLifo = System.nanoTime() - start;

                // Salvataggio dati sul csv in output
                String data = String.format("%d;%d;%d;%d;%d;%d;%.3f;%.3f;%.3f;%.3f",
                        numRequests, k,
                        optM.getCacheMissCount(), naiM.getCacheMissCount(),
                        fifoM.getCacheMissCount(), lifoM.getCacheMissCount(),
                        tOptimal / 1000000.0, tNaive / 1000000.0,
                        tFifo / 1000000.0, tLifo / 1000000.0);

                FileUtils.writeCsvLine(csvPath, data, true);

                // Stampa dei tempi di esecuzione (ms)
                // Il replace serve per togliere /input del percorso file
                System.out.printf("%-20s %-10d %-15.2f %-15.2f %-15.2f %-15.2f\n",
                        pathAttuale.replace("input/", ""), numRequests,
                        tOptimal / 1000000.0, tNaive / 1000000.0, tFifo / 1000000.0, tLifo / 1000000.0);
            }

            System.out.println(
                    "-------------------------------------------------------------------------------------------------");
            System.out.println("Dati della simulazione presenti in: " + csvPath);

        } else if (scelta == 2) {
            System.out.println("> ESECUZIONE PER FILE GENERATO RANDOMICAMENTE\n");

            System.out.print("1. Inserisci il nome del file da creare (es: test_live): ");
            String filename = scanner.next();
            String filepath = "input/" + filename + ".txt";

            System.out.print("2. Quante richieste totali deve contenere la sequenza (N)? ");
            int nRequests = scanner.nextInt();

            System.out.print("3. Quale deve essere la dimensione della cache (K)? ");
            int dimCache = scanner.nextInt();

            System.out.print("4. Quante risorse distinte devono esserci nel test? ");
            int nResources = scanner.nextInt();

            System.out.println("\n> Generazione del file casuale in corso...");
            FileUtils.createRandomTestFile(filepath, nRequests, nResources);
            System.out.println("> File creato con successo in: " + filepath);

            System.out.println("> Caricamento del file e avvio simulazione...");
            String[] requests = FileUtils.readRequests(filepath, nRequests);

            OptimalCacheManager optimalM = new OptimalCacheManager(requests, dimCache);
            long start = System.nanoTime();
            for (int i = 0; i < requests.length; i++)
                optimalM.access(requests[i]);
            long tOptimal = System.nanoTime() - start;

            NaiveCacheManager naiveM = new NaiveCacheManager(requests, dimCache);
            start = System.nanoTime();
            for (int i = 0; i < requests.length; i++)
                naiveM.access(requests[i]);
            long tNaive = System.nanoTime() - start;

            FifoCacheManager fifoM = new FifoCacheManager(requests, dimCache);
            start = System.nanoTime();
            for (int i = 0; i < requests.length; i++)
                fifoM.access(requests[i]);
            long tFifo = System.nanoTime() - start;

            LifoCacheManager lifoM = new LifoCacheManager(requests, dimCache);
            start = System.nanoTime();
            for (int i = 0; i < requests.length; i++)
                lifoM.access(requests[i]);
            long tLifo = System.nanoTime() - start;

            System.out.println("\n=================================================");
            System.out.println("           RISULTATI DELLA SIMULAZIONE           ");
            System.out.println("=================================================");
            System.out.printf("> Risorse totali elaborate (N): %d\n", nRequests);
            System.out.printf("> Dimensione Cache (K): %d\n", dimCache);
            System.out.printf("> Numero di risorse distinte (M): %d\n", nResources);
            System.out.printf("%-15s %-15s %-15s\n", "Algoritmo", "Cache Miss", "Tempo CPU (ms)");
            System.out.println("-------------------------------------------------");
            System.out.printf("%-15s %-15d %-15.3f\n", "Optimal", optimalM.getCacheMissCount(), tOptimal / 1000000.0);
            System.out.printf("%-15s %-15d %-15.3f\n", "Naive", naiveM.getCacheMissCount(), tNaive / 1000000.0);
            System.out.printf("%-15s %-15d %-15.3f\n", "FIFO", fifoM.getCacheMissCount(), tFifo / 1000000.0);
            System.out.printf("%-15s %-15d %-15.3f\n", "LIFO", lifoM.getCacheMissCount(), tLifo / 1000000.0);
            System.out.println("-------------------------------------------------");

            String results = String.format("%d;%d;%d;%d;%d;%d;%.3f;%.3f;%.3f;%.3f", nRequests, dimCache,
                    optimalM.getCacheMissCount(), naiveM.getCacheMissCount(),
                    fifoM.getCacheMissCount(), lifoM.getCacheMissCount(),
                    tOptimal / 1000000.0, tNaive / 1000000.0,
                    tFifo / 1000000.0, tLifo / 1000000.0);
            String csvPath = "output/" + filename + ".csv";
            FileUtils.writeCsvLine(csvPath, firstLine, false);
            FileUtils.writeCsvLine(csvPath, results, true);
            System.out.println("Dati della simulazione inseriti in: " + filename + ".csv");

        } else {
            System.out.println("Opzione non valida. Riavvia il programma.");
        }

        scanner.close();
    }
}