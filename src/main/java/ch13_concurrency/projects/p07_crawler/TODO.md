# Projet 7 (CAPSTONE) — Le robot d'indexation et le planificateur

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).

**Notions visées :** tout le chapitre 13, plus le **`ScheduledExecutorService`** :
- `Executors.newScheduledThreadPool` ;
- `schedule(Callable, délai, unité)`, qui rend un `ScheduledFuture` ;
- `scheduleAtFixedRate` (cadence fixe) contre `scheduleWithFixedDelay` (délai fixe **entre** deux exécutions) ;
- une tâche périodique ne s'arrête que si on l'**annule** (`cancel`) ou si on arrête le planificateur.

Côté algorithme : un **robot d'indexation** concurrent.
- C'est un parcours en **largeur**, niveau par niveau ; chaque niveau est téléchargé **en parallèle** avec `invokeAll`.
- Les pages déjà vues sont éliminées par un `Set` concurrent : `add` ne rend `true` qu'**une** fois.
- Les pages cassées sont récupérées par `ExecutionException`.
- Un **index inversé** concurrent est construit : mot → pages.

**Ce que TU crées :** dans `ch13_concurrency.projects.p07_crawler` : `Crawler` et **`CrawlerApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 13.

---

## Tableau de bord

### ☐ Étape 1 — Le robot

- **`Crawler(ExecutorService pool)`** :
  - `Set<String> visited = ConcurrentHashMap.newKeySet()` ;
  - `Map<String, Set<String>> index`, une `ConcurrentHashMap` ;
  - `AtomicInteger fetched` ;
  - `Set<String> broken`, un `TreeSet` (rempli par le seul thread `main`).
- **`private List<String> fetch(String page) throws InterruptedException`** :
  1. `Thread.sleep(1)` (la latence) ;
  2. `fetched.incrementAndGet()` ;
  3. pour chaque mot de `Data.words(page)` : `index.computeIfAbsent(mot, k -> ConcurrentHashMap.newKeySet()).add(page)` ;
  4. rend `Data.links(page)`, qui lève une exception pour une page cassée.
- **`List<Integer> crawl(String start, int maxDepth) throws InterruptedException`** : `level = [start]`, et `start` dans `visited`. Pour chaque profondeur ≤ `maxDepth`, tant que le niveau n'est pas vide :
  1. note la taille du niveau ;
  2. crée une `Callable<List<String>>` par page, puis `pool.invokeAll(tasks)` ;
  3. pour chaque `Future`, dans l'ordre : chaque lien tel que `visited.add(lien)` rend `true` va dans le niveau suivant ;
  4. une `ExecutionException` ajoute `<page> (<message de la cause>)` à `broken`.
- **Les accesseurs :** `fetched()`, `visited()`, `broken()`, `index()`.
- **Question :** pourquoi `if (!visited.contains(l)) visited.add(l)` serait-il faux, en concurrence ?

### ☐ Étape 2 — Le programme

```
termine true ; pages par niveau [1, 3, 9, 22, 28], decouvertes 80, telechargees 63
pages cassees [p13 (404 p13), p26 (404 p26), ...]
index (mot=pages) : atome=13 carte=10 ...
mot le plus present : pool -> [p15, p18, p25, p28, p35]...
```
- **Le parcours :**
  1. un pool de `Data.THREADS` ;
  2. `crawl(Data.START, Data.MAX_DEPTH)` dans un `try`, avec `shutdown()` dans le `finally` ;
  3. affiche `awaitTermination(10 s)`, la liste par niveau, `visited()` et `fetched()`.
- **Les résultats :**
  - les pages cassées ;
  - l'index copié dans une `TreeMap`, chaque ensemble en `TreeSet`, et le nombre de pages par mot ;
  - le mot présent sur le plus de pages (à égalité, l'ordre alphabétique), avec ses 5 premières pages et `...`.
- **Question :** pourquoi « découvertes » (80) est-il plus grand que « téléchargées » (63) ?

### ☐ Étape 3 — Le planificateur

```
rapport : 80 pages
periodiques : au moins 5 battements true, au moins 3 sondages true, annulees true true
planificateur arrete true
```
- **`Executors.newScheduledThreadPool(2)`**, dans un `try` / `finally { shutdown(); }` :
  1. `schedule(() -> "rapport : " + crawler.visited() + " pages", 20, TimeUnit.MILLISECONDS)` → un `ScheduledFuture<String>` ;
  2. `scheduleAtFixedRate(…, 0, 5, MILLISECONDS)` : incrémente `beats` (un `AtomicInteger`), puis `countDown()` d'une `CountDownLatch(5)` ;
  3. `scheduleWithFixedDelay(…, 0, 5, MILLISECONDS)` : idem, avec `polls` et une `CountDownLatch(3)`.
- **Ensuite :**
  1. affiche `report.get()` ;
  2. attends les deux `CountDownLatch` ;
  3. `cancel(false)` sur les deux tâches périodiques ;
  4. affiche `beats >= 5`, `polls >= 3`, et `isCancelled()` des deux ;
  5. après le `finally`, `awaitTermination(5 s)`.
- **Questions :**
  - Pourquoi n'affiche-t-on pas le nombre **exact** de battements ?
  - Quelle différence entre `AtFixedRate` et `WithFixedDelay` si une exécution dure plus longtemps que la période ?

---

## Checklist (vérifiée par `Check`)

- `Data.MAX_DEPTH`, `Data.THREADS`, `Data.START`, `Data.links(`, `Data.words(` ;
- `ConcurrentHashMap.newKeySet()`, `.computeIfAbsent(`, `AtomicInteger`, `.invokeAll(`, `catch (ExecutionException`, `Callable<List<String>>` ;
- `Executors.newScheduledThreadPool(`, `.schedule(`, `.scheduleAtFixedRate(`, `.scheduleWithFixedDelay(`, `ScheduledFuture<`, `.cancel(false)` ;
- `CountDownLatch`, `.shutdown()`, `.awaitTermination(`.

---

## Sortie attendue complète

```
termine true ; pages par niveau [1, 3, 9, 22, 28], decouvertes 80, telechargees 63
pages cassees [p13 (404 p13), p26 (404 p26), p52 (404 p52), p65 (404 p65), p78 (404 p78)]
index (mot=pages) : atome=13 carte=10 file=13 flux=12 java=13 module=12 pool=15 tache=13 thread=12 verrou=13
mot le plus present : pool -> [p15, p18, p25, p28, p35]...
rapport : 80 pages
periodiques : au moins 5 battements true, au moins 3 sondages true, annulees true true
planificateur arrete true
```
