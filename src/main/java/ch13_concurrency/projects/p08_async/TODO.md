# Projet 8 (BONUS) — L'agence asynchrone (`CompletableFuture`, Fork/Join, `ThreadLocal`, `Semaphore`)

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).
> **Bonus :** ces outils vont **au-delà** de l'examen OCP 17. Fais ce projet après p01 à p07, pour devenir à l'aise avec la concurrence « moderne » de Java.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

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

**C'est le projet-bilan du chapitre 13.** Il ajoute quelques outils nouveaux, expliqués étape par étape. Pour le reste, relis la leçon d'origine :

| Tu dois… | Leçon à relire |
|---|---|
| un pool, `shutdown` dans un `finally` | projet 2, étapes 2 et 4 |
| les exceptions enveloppées | projet 2, étape 3 |
| des atomiques (`addAndGet`, `accumulateAndGet`) | projet 3, étape 4 |
| `CountDownLatch` | projet 2, étape 4 |
| la fusion de résumés par morceaux | projet 1, étape 1 |

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch13-p08 -sourcepath src/main/java src/main/java/ch13_concurrency/projects/p08_async/AsyncLab.java
java "-Duser.language=fr" -cp build/ch13-p08 ch13_concurrency.projects.p08_async.AsyncLab
```

---

## Tableau de bord

### ☐ Étape 1 — Les devis asynchrones

```
devis : Rome 465 EUR = 439 CHF
devis : Atlantis indisponible (aucun vol pour Atlantis)
```

**📖 La leçon : `CompletableFuture`, enchaîner des calculs asynchrones.** Une `CompletableFuture` est un `Future` qu'on peut **enchaîner**, comme un `Optional` ou un stream :

```java
CompletableFuture<Integer> pain = CompletableFuture.supplyAsync(() -> 3, pool);    // calculé sur le pool
CompletableFuture<Integer> beurre = CompletableFuture.supplyAsync(() -> 2, pool);
String r = pain.thenCombine(beurre, Integer::sum)       // attend les deux, puis les additionne
        .thenApply(n -> n + " euros")                   // transforme le résultat
        .join();                                        // "5 euros" : attend la fin
String e = CompletableFuture.supplyAsync(() -> { if (true) throw new IllegalStateException("rupture"); return "x"; }, pool)
        .exceptionally(ex -> "indisponible : " + ex.getCause().getMessage())   // rattrape l'erreur
        .join();                                        // "indisponible : rupture"
```

**👉 À toi :**

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

**📖 Rappel :** `join()` et `get()` attendent tous deux la fin. Les autres méthodes de l'étape (`handle`, `completeOnTimeout`, `orTimeout`, `anyOf`, `thenAccept`, `thenRun`, `complete`) sont décrites dans l'étape elle-même : essaie chacune sur un petit exemple avant de les assembler.

**👉 À toi :**

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

**📖 La leçon : trois outils de coordination.**
- `ThreadLocal<T>` : chaque thread a **sa propre** valeur. `ThreadLocal.withInitial(() -> 0)` donne la valeur de départ ;
- `Semaphore(n)` : `n` permis. `acquire()` en prend un (et attend s'il n'y en a plus), `release()` le rend. Au plus `n` threads à la fois dans la zone protégée ;
- `CountDownLatch(n)` : un compte à rebours (projet 2).

```java
static final ThreadLocal<Integer> PANIER = ThreadLocal.withInitial(() -> 0);
// un thread fait PANIER.set(5) : dans ce thread, PANIER.get() vaut 5 ; dans main, il vaut toujours 0
```

**👉 À toi :**

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

**📖 La leçon : Fork/Join, diviser pour régner en parallèle.** Une `RecursiveTask<T>` coupe son travail en deux, envoie une moitié à un autre thread (`fork()`), calcule l'autre elle-même (`compute()`), puis attend la première (`join()`) :

```java
class Somme extends RecursiveTask<Long> {
    final int de, a;
    Somme(int de, int a) { this.de = de; this.a = a; }
    @Override
    protected Long compute() {
        if (a - de <= 1000) {                        // assez petit : on calcule directement
            long s = 0;
            for (int i = de; i < a; i++) s += i;
            return s;
        }
        int milieu = (de + a) / 2;
        Somme gauche = new Somme(de, milieu);
        gauche.fork();                               // la gauche part sur un autre thread
        long droite = new Somme(milieu, a).compute();
        return gauche.join() + droite;
    }
}
new ForkJoinPool(4).invoke(new Somme(0, 1_000_000))   // 499999500000
```

Une `RecursiveAction` fait la même chose sans rendre de résultat.

**👉 À toi :**

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
