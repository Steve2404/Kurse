# Chapitre 13 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, la règle du crescendo, et la **règle d'or : une sortie déterministe**) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Data.java` : les données ;
- `Check.java` : le correcteur ;
- `solution/` : à n'ouvrir qu'à la fin.

**Tous les types, c'est toi qui les crées.**

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_downloader` — téléchargeur en morceaux | `Thread` et `Runnable`, `start`/`run`/`join`, les 6 états, `interrupt`, daemon | `Downloader` | fusion de morceaux (série qui traverse la frontière), observer `BLOCKED` et `WAITING` |
| ☐ | `p02_primes` — crible parallèle | `ExecutorService`, `Callable`, `Future`, `invokeAll`/`invokeAny`, délai, `ExecutionException`, arrêt | `PrimeLab` | crible segmenté et plus grand écart, `shutdownNow` |
| ☐ | `p03_bank` — banque concurrente | `synchronized`, atomiques, `ReentrantLock`, `tryLock`, réentrance, `volatile` | `BankLab` | 20 000 virements, soldes identiques au séquentiel, interblocage évité |
| ☐ | `p04_logs` — analyse de journaux | `BlockingQueue`, producteurs et consommateurs, pilule empoisonnée, collections concurrentes, `CopyOnWrite`, CME | `LogPipeline` | arrêt propre du pipeline, agrégats concurrents triés |
| ☐ | `p05_life` — jeu de la vie | `CyclicBarrier`, action de barrière, barrière cassée, `reset` | `ParallelLife` | phases synchronisées, résultat identique au séquentiel |
| ☐ | `p06_streams` — streams parallèles | `parallel`, `reduce` (identité, combineur), `collect`, ordre, `findAny`, collecteurs concurrents, `parallelSort` | `ParallelLab` | calculs exacts quel que soit le découpage (points du disque, Collatz) |
| ☐ | `p07_crawler` — **capstone** robot d'indexation | tout, plus `ScheduledExecutorService` | `CrawlerApp` | parcours en largeur parallèle, pages vues une seule fois, index concurrent |
| ☐ | `p08_async` — **bonus** agence asynchrone (au-delà de l'examen) | `CompletableFuture`, Fork/Join (`RecursiveTask`/`RecursiveAction`), `ThreadLocal`, `Semaphore`, `CountDownLatch` | `AsyncLab` | devis asynchrones composés, sous-tableau maximal en diviser pour régner |
