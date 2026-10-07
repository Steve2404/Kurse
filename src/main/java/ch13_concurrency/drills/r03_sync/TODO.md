# Drill de rappel 3 — `synchronized`, atomiques, verrous, barrière

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall03`** dans le paquet `ch13_concurrency.drills.r03_sync`, avec :
  - deux champs `static int counter` et `static int classCounter` ;
  - `static synchronized void incrementStatic()`, qui fait `classCounter++` ;
  - `static void runAll(int n, Runnable task) throws Exception` : soumet n fois la tâche à un pool de 4 threads, attend tous les `Future`, puis l'arrête.

**Les notions de ce drill ont été apprises dans :** projet 3 (étapes 1, 3 et 4) et projet 5 (étapes 2 et 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r03_sync` → **New** → **Java Class** → `Recall03`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall03`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall03`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `runAll(4, …)` : 10 000 fois, `synchronized (lock) { counter++; }`, puis `incrementStatic()`.
  → `D01 : 40000 40000`
- ☐ **D02.** `AtomicInteger a = new AtomicInteger(5)`. Affiche, dans l'ordre :
  - `incrementAndGet()`, `getAndIncrement()`, `get()` ;
  - `addAndGet(10)`, `getAndSet(0)` ;
  - `compareAndSet(0, 100)`, puis `compareAndSet(0, 200)` ;
  - `updateAndGet(x -> x / 2)`, `accumulateAndGet(3, Math::max)`.
  → `D02 : 6 6 7 17 17 true false 50 50`
- ☐ **D03.** `runAll(8, …)` : 1 000 fois `total.addAndGet(2)` (un `AtomicLong`). Puis `AtomicBoolean flag` : `getAndSet(true)`, puis `get()`.
  → `D03 : 16000 false true`
- ☐ **D04.** Deux parties :
  - `runAll(4, …)` : 5 000 fois `lock()`, `guarded[0]++`, et `unlock()` dans un `finally` ;
  - ensuite, `main` tient un autre `ReentrantLock held`. Un thread essaie `tryLock()`, puis `tryLock(10, MILLISECONDS)`. Après son `join()`, `main` fait `unlock()`, puis `tryLock()`.
  
  Affiche le compteur, les deux essais du thread, l'essai de `main`, puis `getHoldCount()`.
  → `D04 : 20000 false false true 1`
- ☐ **D05.** Un `ReentrantReadWriteLock`. Pris deux fois en **lecture** par `main`, puis :
  - `getReadLockCount()` ;
  - un autre thread essaie `writeLock().tryLock()` ;
  - après deux `unlock` de lecture, `main` fait `writeLock().tryLock()`, puis `isWriteLocked()`.
  → `D05 : 2 false true true`
- ☐ **D06.** `new CyclicBarrier(3, rounds::incrementAndGet)` ; `runAll(3, …)` où chaque tâche fait `await()` 4 fois. Affiche `rounds`, `getParties()`, puis `isBroken()`.
  → `D06 : 4 3 false`

## Expériences (hors sortie attendue)

1. Dans D01, synchronise sur `new Object()` **dans** la boucle : quel résultat ? Pourquoi ?
2. Une méthode `synchronized` d'instance et une `static synchronized` s'excluent-elles ?
3. `unlock()` sur un verrou que tu ne tiens pas : quelle exception ?
4. Dans D06, mets `runAll(2, …)` avec 3 parties : que se passe-t-il ?

## Sortie attendue complète

```
D01 : 40000 40000
D02 : 6 6 7 17 17 true false 50 50
D03 : 16000 false true
D04 : 20000 false false true 1
D05 : 2 false true true
D06 : 4 3 false
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **`synchronized`** :
  - une méthode d'instance verrouille `this` ; une méthode `static` verrouille `NomClasse.class` ;
  - un bloc `synchronized (obj)` verrouille `obj` ;
  - le verrou est **réentrant**, et toujours relâché à la sortie, même par exception.
- **Les atomiques** (`AtomicInteger`, `AtomicLong`, `AtomicBoolean`, `AtomicReference`) :
  - `get`/`set` ;
  - `incrementAndGet` (renvoie après) et `getAndIncrement` (renvoie avant) ;
  - `addAndGet`/`getAndAdd`, `getAndSet` ;
  - `compareAndSet(attendu, nouveau)` ;
  - `updateAndGet(f)` et `accumulateAndGet(x, f)`.
- **`Lock`** (`ReentrantLock`) :
  - `lock()`, puis `try { … } finally { unlock(); }` ;
  - `tryLock()` rend `false` tout de suite ; `tryLock(n, unité)` attend au plus n ;
  - le verrou doit être relâché **autant de fois** qu'il a été pris (`getHoldCount`) ;
  - `new ReentrantLock(true)` : équitable.
- **`ReadWriteLock`** : plusieurs lecteurs **ou** un seul écrivain.
- **`CyclicBarrier(n, action)`** : n threads se retrouvent ; l'action s'exécute une fois par tour ; la barrière est réutilisable.

</details>
