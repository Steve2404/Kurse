# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Life.java`](Life.java).
>
> Les valeurs et les exceptions ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Du texte à la grille

**Le code de l'étape :**

```java
static boolean[][] parse(String[] rows) {
    boolean[][] grid = new boolean[rows.length][rows[0].length()];
    for (int r = 0; r < rows.length; r++) {
        for (int c = 0; c < rows[r].length(); c++) {
            grid[r][c] = rows[r].charAt(c) == '#';
        }
    }
    return grid;
}
```

**Questions :**
- **`new boolean[3][4]` juste après création :** toutes les cases valent **`false`**. Les cases d'un tableau reçoivent la valeur par défaut de leur type, comme les champs : `0`, `false`, `null`.
- **`grid[r][c]` :** le **premier** indice désigne la **ligne**. Un tableau 2D est un tableau de lignes, et `grid[r]` est la ligne r (elle-même un `boolean[]`).
- **`grid.length`** est le nombre de lignes (3), **`grid[0].length`** est le nombre de colonnes de la ligne 0 (4). Vérifié.

**`length` contre `length()` :** sur un tableau, c'est un **champ** (`grid.length`) ; sur un `String`, une **méthode** (`s.length()`). Piège d'examen classique.

---

## Étape 2 — Compter les voisines

**Le code de l'étape :**

```java
static int neighbours(boolean[][] grid, int r, int c) {
    int count = 0;
    for (int dr = -1; dr <= 1; dr++) {
        for (int dc = -1; dc <= 1; dc++) {
            int nr = r + dr;
            int nc = c + dc;
            if ((dr == 0 && dc == 0) || nr < 0 || nc < 0 || nr >= grid.length || nc >= grid[nr].length) {
                continue;
            }
            if (grid[nr][nc]) {
                count++;
            }
        }
    }
    return count;
}
```

**Expérience — sans le test des bords :** pour une cellule de la ligne 0, `dr = -1` donne `nr = -1` :

```
java.lang.ArrayIndexOutOfBoundsException: Index -1 out of bounds for length …
```

(vérifié : `Index -1 out of bounds for length 3` sur une grille de 3 lignes.) Java vérifie **chaque** accès à un tableau : un indice négatif ou ≥ `length` lève toujours cette exception.

**Le court-circuit protège l'accès :** dans `nr >= grid.length || nc >= grid[nr].length`, la lecture `grid[nr]` n'a lieu que si `nr` est déjà valide. Si l'un des tests précédents est vrai, `||` s'arrête.

---

## Étape 3 — Une génération

**Le code de l'étape :**

```java
static boolean[][] next(boolean[][] grid) {
    boolean[][] result = new boolean[grid.length][grid[0].length];
    for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[r].length; c++) {
            int n = neighbours(grid, r, c);
            result[r][c] = grid[r][c] ? n == 2 || n == 3 : n == 3;
        }
    }
    return result;
}

static int alive(boolean[][] grid) {
    int count = 0;
    for (boolean[] row : grid) {
        for (boolean cell : row) {
            count += cell ? 1 : 0;
        }
    }
    return count;
}
```

**Le piège :** si l'on écrivait dans `grid` pendant le parcours, la cellule (r, c+1) compterait ses voisines avec une cellule (r, c) **déjà** passée à la génération suivante. Le résultat serait un mélange des deux générations. Toutes les naissances et les morts doivent se décider à partir de **la même** grille.

**Le for-each imbriqué :** la boucle extérieure parcourt les **lignes** (`boolean[] row`), et l'intérieure les **cellules** de chaque ligne (`boolean cell`).

**La génération 1 du planeur, à la main :** les cellules vivantes de départ sont (1,2), (2,3), (3,1), (3,2) et (3,3). Après une génération : (2,1), (2,3), (3,2), (3,3) et (4,2). Le planeur a « basculé ». Après 4 générations, il a la même forme, décalée d'**une case en bas et à droite**.

---

## Étape 4 — Comparer des tableaux

**Le code de l'étape :**

```java
boolean[][] blinker = parse(Data.BLINKER);
boolean[][] once = next(blinker);
boolean[][] twice = next(once);
System.out.println("clignotant : 1 etape identique " + Arrays.deepEquals(blinker, once) + ", 2 etapes identique "
        + Arrays.deepEquals(blinker, twice) + ", Arrays.equals(lignes) " + Arrays.equals(blinker, twice)
        + ", equals " + blinker.equals(twice));
System.out.println("ligne 2 apres 1 etape : " + Arrays.toString(once[2]) + ", dimensions " + once.length + "x" + once[0].length);
```

**Les trois comparaisons :**

| Méthode | Ce qu'elle compare | `blinker` contre `twice` |
|---|---|---|
| `Arrays.deepEquals` | descend dans les sous-tableaux et compare les **valeurs** | `true` |
| `Arrays.equals` | compare les cases **un niveau** seulement. Ici, les cases sont des **références** de lignes (`boolean[]`), et ces lignes sont des objets différents | `false` |
| `equals` | celui d'`Object` : compare les **références** des deux tableaux | `false` |

Vérifié sur un exemple réduit (`int[][] a = {{1}}, b = {{1}}`) : `Arrays.equals(a, b)` vaut `false`, `Arrays.deepEquals(a, b)` vaut `true`, `a.equals(b)` vaut `false`, et `Arrays.equals(a[0], b[0])` vaut `true` (un niveau suffit sur des `int[]`).

**Question — un tableau a-t-il un `toString` utile ?** **Non.** Il hérite de celui d'`Object`, qui affiche le type et un code de hachage. Vérifié : `System.out.println(once[2])` affiche par exemple `[Z@5a07e868`, où `[Z` signifie « tableau de `boolean` ». Il faut `Arrays.toString(once[2])` pour `[false, true, true, true, false]`, ou `Arrays.deepToString(grille)` pour un tableau 2D.
