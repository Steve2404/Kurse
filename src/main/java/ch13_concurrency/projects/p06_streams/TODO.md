# Projet 6 — Les streams parallèles (ce qui reste juste, ce qui ne l'est plus)

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 13) :**
- `parallel()`, `parallelStream()`, `sequential()`, `isParallel()` ;
- **`reduce`** :
  - l'**identité** doit être neutre ;
  - l'accumulateur et le combineur doivent être **associatifs** ;
  - la forme à 3 arguments `(identité, accumulateur, combineur)` ;
- **`collect`** à 3 arguments `(fournisseur, accumulateur, combineur)`, qui conserve l'ordre de rencontre ;
- **l'ordre :**
  - `forEach` (ordre quelconque) contre `forEachOrdered` ;
  - `findFirst` contre `findAny` ;
  - `unordered()` ;
- **les collecteurs concurrents** : `groupingByConcurrent`, `toConcurrentMap` (des `ConcurrentMap`) ;
- **les tableaux :** `Arrays.parallelSort` et `Arrays.parallelPrefix`.

Côté algorithmes :
- compter **exactement** les points entiers d'un disque, pour en tirer une approximation de π. On ne fait que des additions entières : le résultat ne dépend pas du découpage ;
- la plus longue **suite de Collatz**, avec un comparateur **total** (à égalité, le plus petit départ), pour un résultat unique.

**Ce que TU crées :** dans `ch13_concurrency.projects.p06_streams` : **`ParallelLab`** (le `main`).

**Règle du crescendo :** chapitres 1 à 13.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch13-p06 -sourcepath src/main/java src/main/java/ch13_concurrency/projects/p06_streams/ParallelLab.java
java "-Duser.language=fr" -cp build/ch13-p06 ch13_concurrency.projects.p06_streams.ParallelLab
```

---

## Tableau de bord

### ☐ Étape 1 — Des calculs parallèles exacts

```
points entiers du disque de rayon 20000 : 1256636857 (sequentiel identique true), pi ~ 3.141592
Collatz : la plus longue suite part de 837799 (525 termes)
```

**📖 La leçon : les streams parallèles.** `.parallel()` (ou `liste.parallelStream()`) découpe un stream en morceaux, traités par plusieurs threads, puis recombine les résultats. Le code ne change presque pas :

```java
IntStream.rangeClosed(1, 100).parallel().sum()        // 5050, comme en séquentiel
List.of(3, 1, 2).parallelStream().sorted().toList()   // [1, 2, 3]
```

Pour que le résultat soit **le même** qu'en séquentiel, les opérations doivent être sans effet de bord, et les combinaisons **associatives** : `(a + b) + c == a + (b + c)`.

**👉 À toi :**

- **`static long isqrt(long n)`** : part de `(long) Math.sqrt(n)`, puis corrige d'une unité dans un sens ou dans l'autre.
- **`static long latticePoints(LongStream xs, long r)`** : pour chaque x, `2 * isqrt(r² - x²) + 1`, puis la somme.
  - Appelle-la sur `LongStream.rangeClosed(-r, r)`, une fois séquentielle, une fois `.parallel()` ;
  - affiche π avec `String.format(Locale.ROOT, "%.6f", points / r²)`.
- **`static int collatzLength(long n)`** : le nombre de termes jusqu'à 1 (n compris).
  - `IntStream.range(1, Data.COLLATZ_LIMIT).parallel()`, transformé en paires `int[] {n, longueur}` ;
  - puis `.max(…)`, avec le comparateur longueur, puis `-n`.
- **Question :** pourquoi une somme de `double` en parallèle peut-elle différer du séquentiel, mais pas une somme de `long` ?

### ☐ Étape 2 — `reduce` et `collect`

```
reduce : identite 0 -> 5050 / 5050 ; identite 10 -> sequentiel 5060, parallele different true
lettres 178 ; collect parallele dans l'ordre true, debut [LE, PARALLELE, NE, GARANTIT]
```

**📖 Rappel :** `reduce` et `collect` à 3 arguments (chapitre 10, projet 4). En parallèle, l'identité sert **une fois par morceau**, et le combiner recolle les morceaux.

**👉 À toi :**

- **Sur `List<Integer>` 1..100** :
  - `reduce(0, Integer::sum)`, en séquentiel puis en parallèle ;
  - puis `reduce(10, Integer::sum)`, en séquentiel, et en parallèle (affiche seulement si le résultat **diffère**).
- **Sur les mots de `Data.TEXT`** (découpé sur l'espace) :
  - `reduce(0, (acc, w) -> acc + w.length(), Integer::sum)` ;
  - `map(String::toUpperCase).collect(ArrayList::new, ArrayList::add, ArrayList::addAll)` ;
  - compare avec la version séquentielle, puis affiche les 4 premiers.

### ☐ Étape 3 — L'ordre

```
forEachOrdered [1, 2, ..., 12] ; forEach : memes elements true, taille 12
findFirst 3, findAny present true, unordered().limit(10) donne 10 elements ; isParallel false puis true, sequential() false
```

**📖 La leçon : l'ordre en parallèle.**
- `forEachOrdered` respecte l'ordre de la source, même en parallèle ; `forEach` ne le respecte pas ;
- `findFirst()` rend toujours le premier élément qui convient ; `findAny()` en rend un, n'importe lequel ;
- `unordered()` dit au stream que l'ordre n'a pas d'importance, ce qui peut accélérer certaines opérations.

```java
List<Integer> l = Collections.synchronizedList(new ArrayList<>());
IntStream.rangeClosed(1, 5).parallel().forEachOrdered(l::add);   // [1, 2, 3, 4, 5]
```

**👉 À toi :**

- **`forEach` contre `forEachOrdered`** : sur `IntStream.rangeClosed(1, 12).parallel()`, l'un avec `forEach`, l'autre avec `forEachOrdered`, chacun dans une liste synchronisée. Compare la première, **triée**, avec la seconde.
- **Sur `IntStream.range(0, 1_000).parallel()`** :
  - `filter(n -> n % 7 == 3)`, puis `findFirst()` et `findAny().isPresent()` ;
  - `.unordered().limit(10).count()`.
- **`isParallel()`** : sur `words.stream()`, avant puis après `.parallel()`. Puis `words.parallelStream().sequential().isParallel()`.

### ☐ Étape 4 — Collecteurs concurrents et tableaux

```
groupingByConcurrent (longueur -> nombre) {1=2, 2=7, ...} ; toConcurrentMap, mots repetes {le=3, parallele=2}
parallelSort [0, 1, ..., 11] ; parallelPrefix (sommes cumulees) [1, 3, 6, 10, 15, 21, 28, 36]
```

**📖 La leçon : collecteurs concurrents et tableaux.** `Collectors.groupingByConcurrent` et `toConcurrentMap` remplissent **une seule** `ConcurrentMap` partagée, au lieu de fusionner des `Map` par morceaux. `Arrays.parallelSort` trie un tableau en parallèle, et `Arrays.parallelPrefix` cumule ses cases.

**👉 À toi :**

- **Les collecteurs concurrents :**
  - `Collectors.groupingByConcurrent(String::length)`, puis le nombre de mots par longueur, dans une `TreeMap` ;
  - `Collectors.toConcurrentMap(w -> w, w -> 1, Integer::sum)`, puis les mots vus plus d'une fois, dans une `TreeMap`.
- **Les tableaux :**
  - `IntStream.range(0, 12).map(i -> (i * 7 + 3) % 12).toArray()`, trié par `Arrays.parallelSort` ;
  - `IntStream.rangeClosed(1, 8).toArray()`, passé à `Arrays.parallelPrefix(…, Integer::sum)`.
- **Expériences :**
  - remplace `Integer::sum` par `(a, b) -> a - b` dans un `reduce` parallèle : le résultat est-il stable ?
  - ajoute dans une `ArrayList` ordinaire depuis un `forEach` parallèle : que se passe-t-il ?

---

## Checklist (vérifiée par `Check`)

- `Data.RADIUS`, `Data.COLLATZ_LIMIT`, `Data.TEXT` ;
- `.parallel()`, `.parallelStream()`, `.isParallel()`, `.sequential()`, `.max(`, `.thenComparing(` ;
- `reduce(0, Integer::sum)`, `reduce(10, Integer::sum)`, `reduce(0, (acc, w) -> acc + w.length(), Integer::sum)`, `.collect(ArrayList::new, ArrayList::add, ArrayList::addAll)` ;
- `.forEach(`, `.forEachOrdered(`, `.findFirst()`, `.findAny()`, `.unordered()` ;
- `Collectors.groupingByConcurrent(`, `Collectors.toConcurrentMap(`, `ConcurrentMap<` ;
- `Arrays.parallelSort(`, `Arrays.parallelPrefix(`, `String.format(Locale.ROOT`.

---

## Sortie attendue complète

```
points entiers du disque de rayon 20000 : 1256636857 (sequentiel identique true), pi ~ 3.141592
Collatz : la plus longue suite part de 837799 (525 termes)
reduce : identite 0 -> 5050 / 5050 ; identite 10 -> sequentiel 5060, parallele different true
lettres 178 ; collect parallele dans l'ordre true, debut [LE, PARALLELE, NE, GARANTIT]
forEachOrdered [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12] ; forEach : memes elements true, taille 12
findFirst 3, findAny present true, unordered().limit(10) donne 10 elements ; isParallel false puis true, sequential() false
groupingByConcurrent (longueur -> nombre) {1=2, 2=7, 3=4, 4=3, 5=2, 6=2, 7=4, 8=4, 9=5, 11=1} ; toConcurrentMap, mots repetes {le=3, parallele=2}
parallelSort [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11] ; parallelPrefix (sommes cumulees) [1, 3, 6, 10, 15, 21, 28, 36]
```
