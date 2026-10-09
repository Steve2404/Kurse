# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — La puissance rapide

<details><summary>Indice 1</summary>

Cas de base : `exp == 0` rend 1. Sinon, calcule **une seule fois** `half = power(base, exp / 2)` et garde-le dans une variable : deux appels récursifs referaient tout le travail.

</details>

<details><summary>Indice 2</summary>

`return exp % 2 == 0 ? half * half : half * half * base;`

</details>

---

## Étape 2 — Les permutations

<details><summary>Indice 1</summary>

Une méthode privée récursive `permute(items, used, current, result)`. Quand `current` a la taille de `items`, ajoute une **copie** au résultat.

</details>

<details><summary>Indice 2</summary>

Dans la boucle, pour chaque `i` non utilisé : `used[i] = true; current.add(items.get(i));` puis l'appel récursif, puis `current.remove(current.size() - 1); used[i] = false;`.

</details>

---

## Étape 3 — Sous-ensembles, combinaisons, parenthèses

<details><summary>Indice 1</summary>

Pour les sous-ensembles : une méthode `subsets(items, index, current, result)` ; quand `index == items.size()`, une copie ; sinon, un appel **sans** l'élément `index`, puis on l'ajoute, un appel **avec**, et on le retire.

</details>

<details><summary>Indice 2</summary>

Pour les parenthèses : compte les ouvertes et les fermées déjà posées ; un `StringBuilder` qu'on allonge (`append`) puis qu'on raccourcit (`deleteCharAt(sb.length() - 1)`).

</details>

---

## Étape 4 — Les N reines

<details><summary>Indice 1</summary>

Une méthode `queens(n, row, cols, diag, anti)` qui rend le nombre de solutions à partir de la ligne `row` : 1 si `row == n`, sinon la somme sur les colonnes possibles.

</details>

<details><summary>Indice 2</summary>

Les tableaux de diagonales ont une taille `2 * n` : `row - col + n` va de 1 à 2n−1, `row + col` de 0 à 2n−2. Marque les trois cases, appelle, puis **démarque** les trois.

</details>

---

## Étape 5 — Le sudoku

<details><summary>Indice 1</summary>

Une méthode `allowed(grid, r, c, v)` qui regarde la ligne, la colonne et le carré en une seule boucle de 9 : `grid[r][i]`, `grid[i][c]`, et `grid[br + i / 3][bc + i % 3]`.

</details>

<details><summary>Indice 2</summary>

Pour vérifier la grille de départ avec `allowed` : retire temporairement le chiffre de sa case (sinon il se trouverait lui-même), vérifie, puis remets-le. Pour le retour arrière, numérote les cases de 0 à 80 : `r = case / 9`, `c = case % 9`.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quel exposant (énorme, impair), quel ordre exact de la liste, quelle limite (cible 0, grille fausse mais complétable) n'est pas testé ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. La puissance est calculée en `exp` appels (O(n), et la pile déborde pour un milliard).
2. Pour un exposant impair, la puissance oublie un carré.
3. Les permutations ajoutent la liste en cours au lieu d'une copie.
4. Les permutations ne libèrent pas l'élément après l'avoir essayé.
5. Les sous-ensembles sont produits dans le mauvais ordre et abîmés.
6. Les combinaisons interdisent de réutiliser un candidat.
7. Les combinaisons repartent du plus petit candidat (doublons).
8. Les parenthèses peuvent se fermer sans ouverte en attente.
9. Les reines ignorent les diagonales `/`.
10. Les reines oublient de libérer leur diagonale `/`.
11. Le sudoku ne remet pas une case à 0 en revenant en arrière.
12. Le sudoku regarde le mauvais carré 3 × 3.
13. Le sudoku ne vérifie plus les chiffres de départ.

</details>
