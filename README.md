# Cache Memory Simulator (Naive, Optimal, FIFO, LIFO)

Questo progetto consiste in un simulatore computazionale scritto in **Java** per la valutazione e il benchmark prestazionale di diverse politiche di rimpiazzamento della memoria cache. 

Il simulatore mette a confronto due algoritmi classici (*First-In-First-Out* e *Last-In-First-Out*) con la politica teorica ottimale (*Furthest-in-Future*), implementata attraverso un'architettura software ottimizzata.

## Caratteristiche del Progetto

* **Algoritmo Optimal:** Superamento dell'approccio *Naive* a scansione lineare (O(k*n^2)) a favore di un'architettura integrata a costo logaritmico (O(n lg k)).
* **Strutture Dati Custom:** Core della cache sviluppato da zero integrando un **Albero Rosso-Nero** (per la ricerca rapida delle risorse in cache) e un **Max-Heap** (per l'estrazione istantanea della risorsa futura più distante) dotati di puntatori incrociati per mantenere legate le due strutture.
* **Pre-processing ad alte prestazioni:** Uso combinato di una HashMap di consultazione associata a LinkedList posizionali per mappare lo storico futuro delle richieste in tempo ammortizzato O(1).
* **Modularità e Disaccoppiamento:** Architettura software basata sulla netta separazione delle politiche. Le logiche di sfratto (eviction) sono isolate dai moduli di simulazione, garantendo un'elevata estensibilità (basso accoppiamento).

## Architettura del Codice

Il codice si divide in moduli specializzati:
* CacheManager: Il motore centrale che gestisce la simulazione, legge la traccia di input ed esegue i cicli di accesso.
* RBTree / MaxHeap: Strutture dati custom accoppiate per gestire lo stato della cache in tempo logaritmico.
* Naive / Optimal / FIFO / LIFO: Classi disaccoppiate che implementano le specifiche politiche di rimpiazzamento.

## Prova Sperimentale

Il simulatore è stato validato eseguendo più test sulla **Java Virtual Machine (JVM)**, con array di richieste di lunghezza fino a $N = 500.000$ e dimensioni della cache fino a $K = 200$. 

Al fine di eliminare il rumore statistico del sistema operativo e l'overhead di warm-up del compilatore JIT, i risultati prestazionali esportati rappresentano la **media aritmetica di 15 esecuzioni consecutive** per ciascun set di dati.