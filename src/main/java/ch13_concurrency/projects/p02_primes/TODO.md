# Projet 2 — Le crible parallèle (`ExecutorService`, `Callable`, `Future`)

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 13) :**
- **`Executors`** : `newFixedThreadPool`, `newSingleThreadExecutor` ;
- **`Callable<T>`** contre `Runnable` ;
- **soumettre :**
  - `submit` rend un `Future` ;
  - `execute` ne rend rien ;
  - `invokeAll` attend **toutes** les tâches, et rend les `Future` **dans l'ordre** ;
  - `invokeAny` rend le résultat d'**une** tâche réussie ;
- **`Future`** :
  - `get()` et `get(délai, unité)` (avec `TimeoutException`) ;
  - `cancel(true)`, `isCancelled()`, `isDone()` ;
  - `ExecutionException`, qui enveloppe l'exception de la tâche ;
- **l'arrêt :**
  - `shutdown()` (toujours, dans un `finally`) ;
  - `awaitTermination`, `isShutdown`, `isTerminated` ;
  - `RejectedExecutionException` ;
  - `shutdownNow()`, qui rend les tâches jamais lancées et **interrompt** celles en cours.

Côté algorithme : le **crible d'Ératosthène segmenté**. On calcule d'abord les premiers jusqu'à √HIGH. Chaque segment raye ensuite les multiples dans sa propre fenêtre. On fusionne enfin les segments : nombre total de premiers, et **plus grand écart** entre deux premiers consécutifs, y compris d'un segment à l'autre.

**Ce que TU crées :** dans `ch13_concurrency.projects.p02_primes` :
- le record `Segment` et la classe `SieveTask` ;
- **`PrimeLab`** (le `main`).

**Règle du crescendo :** chapitres 1 à 13.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch13-p02 -sourcepath src/main/java src/main/java/ch13_concurrency/projects/p02_primes/PrimeLab.java
java "-Duser.language=fr" -cp build/ch13-p02 ch13_concurrency.projects.p02_primes.PrimeLab
```

---

## Tableau de bord

### ☐ Étape 1 — Le crible segmenté

**📖 La leçon : `Callable`, une tâche qui rend un résultat.** Un `Runnable` ne rend rien. Une `Callable<T>` a une méthode `T call()`, qui **rend** une valeur et peut lancer une exception vérifiée.

**📖 Rappel :** le crible d'Ératosthène (chapitre 4, projet 7, étape 4).

**👉 À toi :**

- **`record Segment(int from, int to, int count, int first, int last, int maxGap)`**, avec `Segment merge(Segment next)`. L'écart `next.first - last` compte aussi.
- **`SieveTask implements Callable<Segment>`**, construite avec `(from, to, int[] basePrimes)` :
  - `static int[] primesUpTo(int n)` : le crible classique ;
  - **`call()`** :
    1. pour chaque premier de base p, avec p² < to, raye les multiples à partir de `max(p², premier multiple ≥ from)` ;
    2. puis compte les non-rayés ≥ 2, en notant le premier, le dernier et le plus grand écart.

### ☐ Étape 2 — Soumettre et combiner

```
premiers dans [1000000, 3000000) par segment : 17971 17686 17453 17325 17148 16991 16921 16823
total 138318, premier 1000003, dernier 2999999, plus grand ecart 148
invokeAll : 138318 (identique true)
invokeAny : miroir C
```

**📖 La leçon : un pool de threads (`ExecutorService`).** Créer un thread par tâche coûte cher. Un **pool** garde quelques threads prêts, et leur distribue les tâches :

```java
ExecutorService pool = Executors.newFixedThreadPool(2);     // 2 threads, réutilisés
try {
    Future<Integer> f = pool.submit(() -> 6 * 7);           // envoie la tâche, rend tout de suite un Future
    f.get()                                                 // 42 : attend le résultat
    List<Future<String>> toutes = pool.invokeAll(List.of(() -> "pain", () -> "brioche"));  // attend tout
} finally {
    pool.shutdown();                                        // indispensable (voir l'étape 4)
}
```

Un **`Future`** est un « ticket » : le résultat n'est peut-être pas encore prêt, `get()` attend qu'il le soit.

**👉 À toi :**

- **Les données :**
  - `basePrimes = primesUpTo((int) Math.sqrt(Data.HIGH) + 1)` ;
  - `Data.SEGMENTS` segments de même largeur dans `[Data.LOW, Data.HIGH)`, le dernier allant jusqu'à `HIGH` ;
  - un pool de `Data.THREADS` threads.
- **Dans un `try` (avec `finally { pool.shutdown(); }`)** :
  1. `submit` chaque tâche dans une `List<Future<Segment>>`, puis `get()` **dans l'ordre**, en cumulant avec `merge` ;
  2. `pool.invokeAll(tasks)`, puis la somme des `count` et la comparaison ;
  3. pour chaque `"X:etat"` de `Data.MIRRORS`, une `Callable<String>`. Si l'état finit par `panne`, elle lève `IllegalStateException`, sinon elle rend `"miroir X"`. Passe la liste à `invokeAny`.
- **Question :** que ferait `invokeAny` si **toutes** les tâches échouaient ?

### ☐ Étape 3 — Délai, annulation, échec

```
get(50 ms) : TimeoutException, cancel true, isCancelled true, isDone true
tache en echec : ExecutionException <- ArithmeticException: / by zero
submit(Runnable).get() = null
```

**📖 La leçon : une tâche qui échoue.** L'exception d'une tâche arrive dans un **autre** thread. `get()` la renvoie à celui qui attend, **enveloppée** dans une `ExecutionException` :

```java
Future<String> boom = pool.submit(() -> { if (true) throw new IllegalStateException("four froid"); return "x"; });
try {
    boom.get();
} catch (ExecutionException e) {
    e.getCause().getMessage()    // "four froid"
}
```

`get(délai, unité)` attend au plus ce délai, puis lance une `TimeoutException`. `cancel(true)` annule une tâche, en l'interrompant si elle tourne.

**👉 À toi :**

- **Le délai :** une tâche qui dort 10 s et rend `"trop tard"`.
  - `get(50, TimeUnit.MILLISECONDS)` → `catch (TimeoutException e)` : note son nom simple ;
  - puis `cancel(true)`, `isCancelled()` et `isDone()`.
- **L'échec :** une tâche qui calcule `10 / (Data.SEGMENTS - Data.SEGMENTS)`. Son `get()` → `catch (ExecutionException e)` : le nom simple, puis la cause (nom simple et message).
- **Le `Runnable` :** `submit(() -> System.out.print(""))`, puis `get()`.

### ☐ Étape 4 — Arrêter

```
pool : isShutdown true, awaitTermination true, isTerminated true
soumission apres shutdown : RejectedExecutionException
shutdownNow : 3 taches jamais lancees, journal [bloquante, bloquante interrompue]
```

**📖 La leçon : arrêter un pool.**
- `shutdown()` : plus de nouvelles tâches, mais celles en cours finissent ;
- `awaitTermination(délai, unité)` : attend la fin, au plus ce délai ; rend `true` si tout est fini ;
- `shutdownNow()` : interrompt les tâches en cours, et rend celles qui n'avaient pas encore commencé.

`CountDownLatch(n)` est un compte à rebours : `countDown()` le diminue, `await()` attend qu'il arrive à 0. Pratique pour attendre qu'une tâche ait **commencé**.

**👉 À toi :**

- **Après le `finally` :**
  - `isShutdown()`, `awaitTermination(5, TimeUnit.SECONDS)` et `isTerminated()` ;
  - puis une soumission, qui doit lever une exception.
- **Un `newSingleThreadExecutor()`** et un journal synchronisé :
  1. `execute` une tâche qui note `bloquante`, fait `started.countDown()` (une `CountDownLatch(1)`), puis dort 10 s. Si elle est interrompue, elle note `bloquante interrompue` ;
  2. puis `execute` trois tâches `t1`, `t2`, `t3` ;
  3. `started.await()`, puis `shutdownNow()` (garde la liste rendue), puis `awaitTermination`.
- **Questions :**
  - Pourquoi `t1`, `t2` et `t3` n'apparaissent-ils jamais dans le journal ?
  - Que se passe-t-il si on oublie `shutdown()` ?

---

## Checklist (vérifiée par `Check`)

- `Data.LOW`, `Data.HIGH`, `Data.SEGMENTS`, `Data.THREADS`, `Data.MIRRORS` ;
- `implements Callable<Segment>`, `record Segment(` ;
- `Executors.newFixedThreadPool(`, `Executors.newSingleThreadExecutor()`, `.submit(`, `Future<Segment>`, `.get()`, `.invokeAll(`, `.invokeAny(` ;
- `.get(50, TimeUnit.MILLISECONDS)`, `catch (TimeoutException`, `.cancel(true)`, `.isCancelled()`, `.isDone()`, `catch (ExecutionException` ;
- `.shutdown()`, `.isShutdown()`, `.awaitTermination(`, `.isTerminated()`, `catch (RejectedExecutionException`, `.execute(`, `.shutdownNow()` ;
- `CountDownLatch`, `finally`.

---

## Sortie attendue complète

```
premiers dans [1000000, 3000000) par segment : 17971 17686 17453 17325 17148 16991 16921 16823
total 138318, premier 1000003, dernier 2999999, plus grand ecart 148
invokeAll : 138318 (identique true)
invokeAny : miroir C
get(50 ms) : TimeoutException, cancel true, isCancelled true, isDone true
tache en echec : ExecutionException <- ArithmeticException: / by zero
submit(Runnable).get() = null
pool : isShutdown true, awaitTermination true, isTerminated true
soumission apres shutdown : RejectedExecutionException
shutdownNow : 3 taches jamais lancees, journal [bloquante, bloquante interrompue]
```
