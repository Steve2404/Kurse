# Projet 8 (BONUS) — L'agence asynchrone (`CompletableFuture`, Fork/Join, `ThreadLocal`, `Semaphore`)

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).
> **Bonus :** ces outils vont **au-delà** de l'examen OCP 17. Fais ce projet après p01 à p07, pour devenir à l'aise avec la concurrence « moderne » de Java.

**Notions visées :**
- **`CompletableFuture`** (programmation asynchrone) :
  - lancer : `supplyAsync(…, executor)` ;
  - transformer : `thenApply` ;
  - **combiner** deux résultats indépendants : `thenCombine` ;
  - **enchaîner** une autre opération asynchrone : `thenCompose` ;
  - consommer : `thenAccept`, `thenRun` ;
  - les erreurs : `exceptionally`, `handle` ;
  - tout attendre : `allOf` ; le premier terminé : `anyOf` ;
  - `join` (lève `CompletionException`) contre `get` (lève `ExecutionException`) ;
  - les délais : `completeOnTimeout`, `orTimeout` ;
  - `complete`, `completedFuture` ;
- **Fork/Join** :
  - `ForkJoinPool` (« vol de travail ») ;
  - `RecursiveTask<T>` (avec résultat) et `RecursiveAction` (sans) ;
  - `fork()`, `join()`, `compute()`, `invokeAll(…)`, `invoke(…)` ;
  - le seuil sous lequel on calcule directement ;
- **`ThreadLocal`** : une valeur **par thread** (`withInitial`, `get`, `set`, `remove`) ;
- **`Semaphore`** (n permis : `acquire`, `release`, `tryAcquire`, `availablePermits`) et **`CountDownLatch`** (un compte à rebours : `countDown`, `await`, `getCount`).

Côté algorithmes :
- un **devis de voyage** asynchrone : le vol et l'hôtel en parallèle, une remise, une conversion de devise elle-même asynchrone, et un repli en cas d'erreur ;
- le **sous-tableau de somme maximale** en « diviser pour régner ». Chaque moitié rend 4 nombres (total, meilleur préfixe, meilleur suffixe, meilleur), et la fusion se fait en temps constant. On compare avec l'algorithme de **Kadane**.

**Ce que TU crées :** dans `ch13_concurrency.projects.p08_async` :
- `TravelAgency` ;
- `MaxSubarrayTask` (avec son record `Summary`) et `ClampAction` ;
- **`AsyncLab`** (le `main`).

---

## Tableau de bord

### ☐ Étape 1 — Les devis asynchrones

```
devis : Rome 465 EUR = 439 CHF
devis : Atlantis indisponible (aucun vol pour Atlantis)
```
- **`TravelAgency(Executor executor)`**, avec une méthode `static void latency()` qui fait `Thread.sleep(5)`. En cas d'interruption, elle remet le drapeau (`Thread.currentThread().interrupt()`).
  - **`CompletableFuture<Integer> flight(String city)`** : `supplyAsync` sur l'executor. Il rend le prix de `Data.FLIGHTS`, ou lève `IllegalStateException("aucun vol pour " + city)` ;
  - **`hotel(city)`** : `Data.HOTELS.get(city) * Data.NIGHTS` ;
  - **`toChf(int euros)`** : asynchrone, `euros * Data.CHF_PER_MILLE / 1000` ;
  - **`CompletableFuture<String> quote(String city)`** :
    1. `flight(city).thenCombine(hotel(city), Integer::sum)` ;
    2. `.thenApply` : −10 % (`* 9 / 10`) si le total dépasse 500 ;
    3. `.thenCompose` : `toChf`, puis le texte `<ville> <eur> EUR = <chf> CHF` ;
    4. `.exceptionally(e -> <ville> + " indisponible (" + e.getCause().getMessage() + ")")`.
- **Dans `main`** (un pool fixe de 4 threads, arrêté dans un `finally`) :
  1. lance un `quote` par ville de `Data.TRIPS` ;
  2. puis `CompletableFuture.allOf(...).join()` ;
  3. puis affiche chaque devis **dans l'ordre** (`join()`).
- **Question :** pourquoi `thenCompose` plutôt que `thenApply` pour la conversion ? Quel serait le type obtenu avec `thenApply` ?

### ☐ Étape 2 — Erreurs, délais, enchaînements

```
erreurs : handle erreur IllegalStateException ; join CompletionException <- IllegalStateException ; get ExecutionException
delais : valeur par defaut, orTimeout TimeoutException ; anyOf cache ; chaine [recu 42, fini] ; complete a la main isDone true
```
- **Les erreurs :**
  - `flight("Atlantis").handle((prix, e) -> …)` : `"prix " + prix`, ou `"erreur " + <nom simple de la cause>` ;
  - `join()` dans un `catch (CompletionException e)` ;
  - `get()` dans un `catch (ExecutionException e)`.
- **Les délais :**
  - `new CompletableFuture<String>().completeOnTimeout("valeur par defaut", 20, MILLISECONDS).join()` ;
  - `new CompletableFuture<String>().orTimeout(20, MILLISECONDS).join()`, en attrapant `CompletionException` (nom de la cause).
- **`anyOf`** de `completedFuture("cache")` et d'une future jamais terminée.
- **Un enchaînement :** `supplyAsync(() -> 21, pool).thenApply(n -> n * 2).thenAccept(n -> events.add("recu " + n)).thenRun(() -> events.add("fini")).join()`.
- **À la main :** une future que tu termines par `complete("a la main")`, puis `join()` et `isDone()`.

### ☐ Étape 3 — `ThreadLocal`, `Semaphore`, `CountDownLatch`

```
ThreadLocal : total des increments 800, valeur de main 42 puis apres remove 0
Semaphore(2) : au plus 2 a la fois true, permis disponibles 2 ; CountDownLatch 6 -> 0, tryAcquire(3) false
```
- **`static final ThreadLocal<Integer> PER_THREAD = ThreadLocal.withInitial(() -> 0)`** :
  - 8 tâches sur le pool font chacune 100 fois `PER_THREAD.set(PER_THREAD.get() + 1)`, puis `total.addAndGet(100)` ;
  - ensuite, dans `main`, `set(42)`, `get()`, `remove()`, puis de nouveau `get()`.
- **`Semaphore(2)` et deux `CountDownLatch`** (`startGate` à 1, `done` à 6) : 6 tâches, sur un pool de 6 threads.
  1. Chaque tâche attend `startGate`, puis `acquire()`. Dans la section critique, elle :
     - fait `maxInside.accumulateAndGet(inside.incrementAndGet(), Math::max)` ;
     - dort 10 ms ;
     - puis décrémente `inside` ;
  2. puis `release()` dans un `finally` ;
  3. et `done.countDown()` dans un `finally` extérieur.
  
  `main` note `done.getCount()`, fait `startGate.countDown()`, puis `done.await()`. Il affiche `maxInside <= 2`, `availablePermits()`, les deux comptes, puis `tryAcquire(3)`.
- **Question :** pourquoi n'affiche-t-on pas `maxInside` lui-même ?

### ☐ Étape 4 — Fork/Join

```
Fork/Join : sous-tableau maximal 5604, total 1703 ; Kadane sequentiel 5604 identique true
RecursiveAction : min -50, max 50 ; parallelisme du pool 4
```
- **`MaxSubarrayTask extends RecursiveTask<Summary>`**, avec `record Summary(long total, long prefix, long suffix, long best)`, `static Summary of(int)` et `Summary merge(Summary right)` :
  - `total = total + right.total` ;
  - `prefix = max(prefix, total + right.prefix)` ;
  - `suffix = max(right.suffix, right.total + suffix)` ;
  - `best = max(best, right.best, suffix + right.prefix)`.
- **`compute()`** :
  - sous `THRESHOLD = 10_000` éléments, calcule directement (`Data.delta(i)`) ;
  - sinon, coupe en deux : `left.fork()`, `right.compute()`, puis `left.join().merge(r)`.
- **`static long kadane(int size)`** : la version séquentielle, pour comparer.
- **`ClampAction extends RecursiveAction`** : ramène chaque valeur d'un `int[]` dans `[-50, 50]`. En dessous de 50 000 éléments elle travaille directement, sinon elle fait `invokeAll(gauche, droite)`.
- **Dans `main`** :
  1. `new ForkJoinPool(4)` ;
  2. `invoke(new MaxSubarrayTask(0, Data.SIZE))` ;
  3. puis un tableau rempli par `Arrays.setAll(values, Data::delta)`, et `invoke(new ClampAction(...))` ;
  4. affiche le min, le max et `getParallelism()` ;
  5. `shutdown()`.
- **Question :** pourquoi `left.fork(); right.compute(); left.join()`, et pas `left.fork(); right.fork(); left.join(); right.join()` ?

---

## Pour aller plus loin : les threads virtuels (Java 21)

Ton JDK est Java **17** : `Thread.ofVirtual()` et `Executors.newVirtualThreadPerTaskExecutor()` n'existent pas encore. Avec Java 21, des **millions** de threads très légers peuvent bloquer (`sleep`, I/O) sans coûter un thread système chacun. L'équivalent le plus proche en Java 17 : un pool + `CompletableFuture`, comme dans ce projet.

---

## Checklist (vérifiée par `Check`)

- les `Data.…` utilisées ;
- `CompletableFuture.supplyAsync(`, `.thenCombine(`, `.thenApply(`, `.thenCompose(`, `.exceptionally(`, `CompletableFuture.allOf(`, `.handle(` ;
- `catch (CompletionException`, `catch (ExecutionException` ;
- `.completeOnTimeout(`, `.orTimeout(`, `CompletableFuture.anyOf(`, `CompletableFuture.completedFuture(`, `.thenAccept(`, `.thenRun(`, `.complete(` ;
- `ThreadLocal.withInitial(`, `.remove()` ;
- `new Semaphore(2)`, `.acquire()`, `.release()`, `.availablePermits()`, `.tryAcquire(` ;
- `new CountDownLatch(`, `.countDown()`, `.getCount()` ;
- `extends RecursiveTask<`, `extends RecursiveAction`, `.fork()`, `.join()`, `invokeAll(new ClampAction(`, `new ForkJoinPool(4)`, `.invoke(`, `.getParallelism()`.

---

## Sortie attendue complète

```
devis : Rome 465 EUR = 439 CHF
devis : Lisbonne 350 EUR = 330 CHF
devis : Atlantis indisponible (aucun vol pour Atlantis)
devis : Oslo 639 EUR = 603 CHF
erreurs : handle erreur IllegalStateException ; join CompletionException <- IllegalStateException ; get ExecutionException
delais : valeur par defaut, orTimeout TimeoutException ; anyOf cache ; chaine [recu 42, fini] ; complete a la main isDone true
ThreadLocal : total des increments 800, valeur de main 42 puis apres remove 0
Semaphore(2) : au plus 2 a la fois true, permis disponibles 2 ; CountDownLatch 6 -> 0, tryAcquire(3) false
Fork/Join : sous-tableau maximal 5604, total 1703 ; Kadane sequentiel 5604 identique true
RecursiveAction : min -50, max 50 ; parallelisme du pool 4
```
