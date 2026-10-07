# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Du texte à la grille

<details><summary>Indice 1</summary>

`new boolean[rows.length][rows[0].length()]`. Le premier indice est la **ligne**. Une cellule est vivante si `rows[r].charAt(c) == '#'`.

</details>

<details><summary>Indice 2</summary>

`grid.length` est le nombre de lignes. `grid[0].length` est la longueur de la ligne 0 (pas de parenthèses : c'est un champ de tableau, pas la méthode `length()` de `String`).

</details>

---

## Étape 2 — Compter les voisines

<details><summary>Indice 1</summary>

`for (int dr = -1; dr <= 1; dr++)` puis `for (int dc = -1; dc <= 1; dc++)`. La voisine est en `(r + dr, c + dc)`.

</details>

<details><summary>Indice 2</summary>

Une seule condition pour le `continue` : `(dr == 0 && dc == 0) || nr < 0 || nc < 0 || nr >= grid.length || nc >= grid[nr].length`. L'ordre compte : les tests `< 0` passent **avant** `grid[nr]`.

</details>

---

## Étape 3 — Une génération

<details><summary>Indice 1</summary>

La méthode de génération crée `new boolean[grid.length][grid[0].length]`, la remplit en **lisant** l'ancienne grille, puis la rend. Le `main` fait `world = next(world);`.

</details>

<details><summary>Indice 2</summary>

La règle en une ligne : `result[r][c] = grid[r][c] ? n == 2 || n == 3 : n == 3;`.

</details>

---

## Étape 4 — Comparer des tableaux

<details><summary>Indice 1</summary>

Fais évoluer le clignotant une fois (`once`), puis une 2e fois (`twice`). Compare `blinker` à `once` et à `twice` avec `Arrays.deepEquals`, puis `blinker` à `twice` avec `Arrays.equals` et avec `equals`.

</details>

<details><summary>Indice 2</summary>

Un `boolean[][]` est un tableau dont chaque case est une **référence** vers un `boolean[]`. Demande-toi ce que compare chacune des trois méthodes : les références des lignes, ou ce qu'il y a dedans ?

</details>
