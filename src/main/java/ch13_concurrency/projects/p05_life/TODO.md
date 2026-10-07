# Projet 5 — Le jeu de la vie en parallèle (`CyclicBarrier`)

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 13) :**
- **`CyclicBarrier`** :
  - n parties ; `await()` attend que **toutes** soient arrivées ;
  - l'**action de barrière**, exécutée une fois par tour, par le dernier thread arrivé ;
  - la barrière se **réarme** toute seule ;
- `getParties()`, `getNumberWaiting()`, `isBroken()`, `reset()` ;
- **`BrokenBarrierException`** : si une partie est interrompue, les autres sont prévenues ;
- **le piège :** il faut **au moins** autant de threads dans le pool que de parties, sinon tout le monde attend pour toujours.

Côté algorithme : **le jeu de la vie de Conway**, sur un tore (les bords se rejoignent).
- Chaque ouvrier calcule **sa bande de lignes** de la génération suivante.
- À la barrière, les deux grilles sont **échangées** et la population est notée.
- Le résultat doit être **identique** au calcul séquentiel.

**Ce que TU crées :** dans `ch13_concurrency.projects.p05_life` : `Life` et **`ParallelLife`** (le `main`).

**Règle du crescendo :** chapitres 1 à 13.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch13-p05 -sourcepath src/main/java src/main/java/ch13_concurrency/projects/p05_life/ParallelLife.java
java "-Duser.language=fr" -cp build/ch13-p05 ch13_concurrency.projects.p05_life.ParallelLife
```

---

## Tableau de bord

### ☐ Étape 1 — Les règles

**📖 Rappel :** le jeu de la vie (chapitre 4, projet 3) : on calcule la génération suivante dans une **autre** grille, pour ne pas fausser le comptage des voisines.

**👉 À toi :**

- **`final class Life`** :
  - `static boolean nextState(boolean[][] g, int r, int c)` : compte les 8 voisines, avec l'indice `(r + dr + n) % n`. Une cellule vivante survit avec 2 ou 3 voisines ; une morte naît avec exactement 3 ;
  - `static void step(boolean[][] current, boolean[][] next, int fromRow, int toRow)` ;
  - `static int population(boolean[][] g)` ;
  - `static long fingerprint(boolean[][] g)` : `h = 17`, puis pour chaque cellule `h = h * 31 + (vivante ? 1 : 0)`.

### ☐ Étape 2 — Les générations en parallèle

```
populations (generation 0 a 40) : [576, 603, 214, 132, ...]
finale 104, empreinte 2050180327243196825, identique au sequentiel true
barriere : parties 4, en attente 0, cassee false
```

**📖 La leçon : une barrière avec une action.** `new CyclicBarrier(n, action)` : quand le n-ième thread arrive à `await()`, l'**action** s'exécute **une fois**, puis tous les threads repartent. La barrière est « cyclique » : elle resert à chaque tour.

```java
AtomicInteger tours = new AtomicInteger();
CyclicBarrier b = new CyclicBarrier(3, () -> tours.incrementAndGet());
// 3 threads qui font chacun deux fois b.await() : tours vaut 2 à la fin
```

**👉 À toi :**

- **Les grilles :** deux champs `static boolean[][] current` et `next`. La grille initiale vient de `Data.alive(r, c)`, sur `Data.SIZE` × `Data.SIZE`.
- **Les populations :** une liste synchronisée, qui reçoit d'abord la population initiale.
- **La barrière :** `new CyclicBarrier(Data.WORKERS, () -> { … })`. L'action échange `current` et `next`, puis ajoute la population de `current`.
- **Les ouvriers :** un pool de `Data.WORKERS` threads. L'ouvrier w traite les lignes `[w * bande, …)`, le dernier allant jusqu'à la fin. Il répète `Data.GENERATIONS` fois `Life.step(current, next, from, to)`, puis `barrier.await()`.
- **La fin :**
  1. attends les `Future`, puis `shutdown` ;
  2. refais les générations **sans thread**, et compare les empreintes ;
  3. affiche l'état de la barrière.
- **Questions :**
  - Pourquoi un ouvrier ne peut-il pas lire `current` pendant que l'action de barrière l'échange ?
  - Que se passe-t-il avec un pool de 3 threads pour 4 parties ?

### ☐ Étape 3 — La barrière cassée

```
barriere cassee : l'autre thread [interrompu], main BrokenBarrierException, isBroken true puis apres reset false
```

**📖 La leçon : une barrière cassée.** Si un thread qui attend à la barrière est interrompu, la barrière est **cassée** (`isBroken()`), et les autres reçoivent une `BrokenBarrierException`. `reset()` la remet à neuf.

**👉 À toi :**

1. Une `CyclicBarrier(2)` ;
2. un thread qui fait `await()`. S'il reçoit `InterruptedException`, il note `interrompu` ; s'il reçoit `BrokenBarrierException`, il note `cassee` ;
3. attends que `getNumberWaiting()` vaille 1 ;
4. `interrupt()` ce thread, puis `join()` ;
5. `main` fait `await()` à son tour, et attrape `BrokenBarrierException` ;
6. note `isBroken()`, puis `reset()`, puis de nouveau `isBroken()`.

---

## Checklist (vérifiée par `Check`)

- `Data.SIZE`, `Data.GENERATIONS`, `Data.WORKERS`, `Data.alive(` ;
- `new CyclicBarrier(Data.WORKERS, () ->`, `.await()`, `.getParties()`, `.getNumberWaiting()`, `.isBroken()`, `catch (BrokenBarrierException`, `.reset()` ;
- `.interrupt()`, `Executors.newFixedThreadPool(Data.WORKERS)`.

---

## Sortie attendue complète

```
populations (generation 0 a 40) : [576, 603, 214, 132, 105, 116, 105, 98, 102, 101, 110, 110, 129, 114, 130, 130, 128, 132, 138, 133, 143, 136, 127, 131, 130, 130, 131, 137, 123, 104, 99, 96, 89, 92, 89, 95, 92, 90, 95, 95, 104]
finale 104, empreinte 2050180327243196825, identique au sequentiel true
barriere : parties 4, en attente 0, cassee false
barriere cassee : l'autre thread [interrompu], main BrokenBarrierException, isBroken true puis apres reset false
```
