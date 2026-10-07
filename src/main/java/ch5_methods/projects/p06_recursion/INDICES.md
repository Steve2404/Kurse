# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.
>
> **Pour toute méthode récursive,** pose-toi deux questions : 1) quel est le **cas de base**, qui se résout sans s'appeler ? 2) comment le problème se ramène-t-il à un problème **plus petit** du même type ?

---

## Étape 1 — Les bases

<details><summary>Indice 1</summary>

- `factorial(n)` = `n <= 1 ? 1 : n * factorial(n - 1)`.
- `digitSum(n)` = `n < 10 ? n : n % 10 + digitSum(n / 10)`.
- `gcd(a, b)` = `b == 0 ? a : gcd(b, a % b)`.
- `binary(n)` = `n < 2 ? "" + n : binary(n / 2) + n % 2`.

</details>

<details><summary>Indice 2</summary>

`palindrome(s)` : vrai si la longueur est < 2. Sinon, il faut que la première lettre égale la dernière **et** que `palindrome(s.substring(1, length - 1))` soit vrai. `power` : cas de base `n == 0` → 1, et compte chaque `*` dans le champ static.

</details>

---

## Étape 2 — Fibonacci : le coût des appels répétés

<details><summary>Indice 1</summary>

`fibNaive(n)` : `calls++`, puis `n < 2 ? n : fibNaive(n - 1) + fibNaive(n - 2)`.

</details>

<details><summary>Indice 2</summary>

`fibMemo` : `calls++`. Si `n < 2`, rends n. Si `memo[n] != 0`, rends `memo[n]`. Sinon, calcule, range dans `memo[n]`, puis rends. Le tableau est créé **une fois** par l'appelant : `new long[26]`.

</details>

---

## Étape 3 — Diviser pour régner

<details><summary>Indice 1</summary>

`mergeSort` : cas de base `high - low < 1` (0 ou 1 élément). `mid = (low + high) / 2`, puis les deux appels, puis la fusion de `[low, mid]` et `[mid + 1, high]` dans `merged`, et `System.arraycopy(merged, 0, a, low, merged.length)`.

</details>

<details><summary>Indice 2</summary>

- `quickSort` : cas de base `low >= high`. Lomuto comme au projet 1 (compte chaque `a[j] < pivot`), puis `quickSort(a, low, i - 1)` et `quickSort(a, i + 1, high)`.
- `search` : cas de base `low > high`, qui rend `-(low + 1)`.

</details>

---

## Étape 4 — Hanoï, sous-ensembles, combinaisons

<details><summary>Indice 1</summary>

`hanoi(n, from, to, via)` : `hanoi(n - 1, from, via, to)`, puis le déplacement du disque n de `from` à `to`, puis `hanoi(n - 1, via, to, from)`. Un compteur static numérote les déplacements.

</details>

<details><summary>Indice 2</summary>

- `subsets(items, index, current, out)` : si `index == length`, ajoute `{current}`. Sinon, appelle **sans** l'élément, puis **avec**.
- `combinations` : `for (int i = start; i <= n - k + 1; i++) count += combinations(i + 1, n, k - 1, current + i, out);`.

</details>

---

## Étape 5 — Les N reines

<details><summary>Indice 1</summary>

Cas de base : `row == n`, une solution complète, donc rends 1. Sinon, pour chaque colonne c, si `safe(cols, row, c)`, pose `cols[row] = c` et additionne `queens(row + 1, …)`.

</details>

<details><summary>Indice 2</summary>

`safe` compare avec chaque ligne `r < row` déjà posée : même colonne (`cols[r] == c`) ou même diagonale (`Math.abs(cols[r] - c) == row - r`).

</details>

---

## Étape 6 — Les îles et la monnaie

<details><summary>Indice 1</summary>

`fill` : 0 si hors grille ou pas `#`. Sinon, `g[r][c] = '~'`, puis `1 + fill(bas) + fill(haut) + fill(droite) + fill(gauche)`. Le `main` appelle `fill` sur **chaque** case : une taille > 0 signale une nouvelle île.

</details>

<details><summary>Indice 2</summary>

- `ways` : si `amount == 0`, rends 1. Si `amount < 0` ou s'il n'y a plus de pièces, rends 0. Sinon, la mémo, puis `ways(i, amount - coins[i]) + ways(i + 1, amount)`.
- `fewest` : le minimum, sur chaque pièce ≤ montant, de `1 + fewest(montant - pièce)`.

</details>
