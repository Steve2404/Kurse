# Drill de rappel 5 — Les tableaux

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall05`** dans le paquet `ch4_coreapis.drills.r05_arrays`.
- Les déclarations « bizarres » sont exigées : `int b[] = {…}` et `int[] rows[] = {…}`.

## Défis

- ☐ **D01.** Trois tableaux :
  - `a`, un `new int[3]` ;
  - `b`, déclaré `int b[]` et initialisé par `{4, 5, 6}` ;
  - `c`, un `new int[] {7, 8}`.
  
  Affiche `a` et `b` avec `Arrays.toString`, puis la longueur de `c`.
  → `D01 : [0, 0, 0] [4, 5, 6] 2`
- ☐ **D02.** Les valeurs par défaut de quatre tableaux :
  - un `String[2]` ;
  - un `boolean[2]` ;
  - un `double[1]` ;
  - un `char[1]`, dont tu affiches la case 0 **convertie en `int`**.
  → `D02 : [null, null] [false, false] [0.0] 0`
- ☐ **D03.** Deux grilles :
  - `grid = new int[2][3]`, avec `grid[1][2] = 9` ;
  - `rows`, déclaré `int[] rows[]` et initialisé par `{{1}, {2, 3}, {4, 5, 6}}`.
  
  Affiche `grid.length`, `grid[0].length`, `Arrays.deepToString(grid)`, `rows[2].length` et `rows[1][1]`.
  → `D03 : 2 3 [[0, 0, 0], [0, 0, 9]] 3 3`
- ☐ **D04.** `jagged = new int[3][]`. Remplis :
  - la ligne 0 avec un `new int[1]` ;
  - la ligne 2 avec `{1, 2}` ;
  - laisse la ligne 1 vide.
  
  Affiche la ligne 2, `jagged[1] == null` et la longueur.
  → `D04 : [1, 2] true 3`
- ☐ **D05.** `original = {1, 2, 3}`, `alias = original`, `copy = original.clone()`, puis `alias[0] = 99`. Affiche :
  - `original` ;
  - `copy` ;
  - `original == alias` ;
  - `original.equals(copy)`.
  → `D05 : [99, 2, 3] [1, 2, 3] true false`
- ☐ **D06.** Sur `numbers = {1, 2, 3}` :
  1. un for-each fait `n = n * 10` ;
  2. puis une boucle indexée double chaque case.
  
  Affiche le tableau.
  → `D06 : [2, 4, 6]`
- ☐ **D07.** Trois résultats :
  - `Object[] objects = new String[] {"x", "y"}` : sa longueur et sa case 1 ;
  - la somme de toutes les cases de `rows`, avec deux for-each imbriqués.
  → `D07 : 2 y 21`
- ☐ **D08.** Sur `word = {'j', 'a', 'v', 'a'}` :
  - `new String(word)` ;
  - `String.valueOf(word, 1, 2)` ;
  - `Arrays.toString("a-b".toCharArray())`.
  → `D08 : java av [a, -, b]`

## Expériences (hors sortie attendue)

1. `int[] x = new int[];`, `int[2] y;` et `int[] z = new int[2] {1, 2};` : que dit `javac` pour chacune ?
2. `objects[0] = Integer.valueOf(1);` dans D07 : ça compile ? Et à l'exécution ? (`ArrayStoreException`)
3. `int[] a, b[];` : quels sont les types de `a` et de `b` ?
4. `System.out.println(numbers)` : pourquoi voit-on `[I@…` ?

## Sortie attendue complète

```
D01 : [0, 0, 0] [4, 5, 6] 2
D02 : [null, null] [false, false] [0.0] 0
D03 : 2 3 [[0, 0, 0], [0, 0, 9]] 3 3
D04 : [1, 2] true 3
D05 : [99, 2, 3] [1, 2, 3] true false
D06 : [2, 4, 6]
D07 : 2 y 21
D08 : java av [a, -, b]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**La déclaration :**
- les `[]` se placent après le type **ou** après le nom : `int[] a`, `int a[]`, `int[] a[]` (un tableau 2D) ;
- `int[] a, b[];` : `a` est en 1D, `b` en 2D.

**La création :**
- `new int[3]` : avec une taille ;
- `new int[] {…}` : **sans** taille ;
- `{…}` seul : uniquement dans la déclaration ;
- en 2D, la première dimension suffit : `new int[3][]`.

**Les valeurs par défaut :**
- `0`, `0.0`, `false`, `'\u0000'` ;
- `null` pour les références.

**`length` :**
- c'est un **champ**, sans parenthèses (contrairement à `String.length()`) ;
- hors limites : `ArrayIndexOutOfBoundsException`.

**Les références :**
- `=` copie la référence ;
- `clone()` fait une copie superficielle ;
- `equals` d'un tableau est hérité d'`Object` : c'est `==` ;
- le for-each travaille sur une **copie** de la valeur.

**La covariance :**
- un `String[]` est un `Object[]` ;
- y mettre un autre type lève une `ArrayStoreException`.

</details>
