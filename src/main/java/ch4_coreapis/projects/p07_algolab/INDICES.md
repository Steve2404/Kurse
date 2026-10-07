# Projet 7 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Deux tris à la main, puis dédoublonner

<details><summary>Indice 1</summary>

Insertion : pour chaque `i` à partir de 1, mémorise `key = a[i]`. Puis `j = i - 1`, et `while (j >= 0 && a[j] > key)` : décale `a[j]` en `a[j + 1]`, puis `j--`. Enfin, `a[j + 1] = key`.

</details>

<details><summary>Indice 2</summary>

- Sélection : `min = i`, puis une boucle `j` de `i + 1` à la fin qui met à jour `min`. On échange seulement `if (min != i)`.
- Dédoublonnage : `k = 1`. Pour chaque `i` à partir de 1, si `a[i] != a[k - 1]`, alors `a[k++] = a[i]`.

</details>

---

## Étape 2 — Deux pointeurs et borne inférieure

<details><summary>Indice 1</summary>

`while (left < right)`. Quand la somme est trouvée, mémorise les deux valeurs. Puis avance `left` tant qu'il vaut la même chose, et recule `right` de même, toujours avec `left < right`.

</details>

<details><summary>Indice 2</summary>

Borne inférieure : `low = 0`, `high = length` (exclu). `while (low < high)` : si `sorted[mid] < q`, alors `low = mid + 1` ; sinon `high = mid` (pas `mid - 1` : `mid` peut être la réponse). À la fin, la réponse est `low`.

</details>

---

## Étape 3 — Sommes préfixes, fenêtre glissante, Kadane

<details><summary>Indice 1</summary>

Fenêtre : somme les `WINDOW` premiers. Puis, pour `i` de `WINDOW` à la fin : `window += n[i] - n[i - WINDOW]`. Si c'est mieux, le début est `i - WINDOW + 1`.

</details>

<details><summary>Indice 2</summary>

Kadane : `current` et `max` partent de `p[0]`. Si `current + p[i] < p[i]`, on **recommence** à `i` (`current = p[i]`, `start = i`). Sinon, on prolonge. Si `current > max`, on retient `max`, `from = start` et `to = i`.

</details>

---

## Étape 4 — Le crible d'Ératosthène

<details><summary>Indice 1</summary>

Barre 0 et 1 à la main. `for (int i = 2; i * i <= limite; i++)`, et si `i` n'est pas barré : `for (int m = i * i; m <= limite; m += i)`.

</details>

<details><summary>Indice 2</summary>

Les premiers se lisent dans une 2e boucle : tout `i` non barré. Pour la liste avec des virgules, ajoute `","` avant chaque nombre **sauf le premier**. Jumeaux : `i + 2 <= limite && !barré[i + 2]`.

</details>

---

## Étape 5 — Matrices

<details><summary>Indice 1</summary>

Le résultat a les dimensions **inversées** : `new int[cols][rows]`. Transposée : `t[c][r] = m[r][c]`. Rotation : `rot[c][rows - 1 - r] = m[r][c]`.

</details>

<details><summary>Indice 2</summary>

Spirale, dans un `while (top <= bottom && left <= right)` : la ligne du haut de gauche à droite, puis `top++` ; la colonne de droite vers le bas, puis `right--` ; `if (top <= bottom)`, la ligne du bas vers la gauche, puis `bottom--` ; `if (left <= right)`, la colonne de gauche vers le haut, puis `left++`.

</details>

---

## Étape 6 — Le triangle de Pascal

<details><summary>Indice 1</summary>

`int[][] tri = new int[n][];`, puis `tri[r] = new int[r + 1];`, `tri[r][0] = tri[r][r] = 1`, et pour `c` de 1 à `r - 1` : `tri[r][c] = tri[r - 1][c - 1] + tri[r - 1][c]`.

</details>

<details><summary>Indice 2</summary>

`int width = Arrays.toString(tri[n - 1]).length();` puis `" ".repeat((width - texte.length()) / 2) + texte`.

</details>

---

## Étape 7 — Le plus court chemin dans un labyrinthe (BFS)

<details><summary>Indice 1</summary>

Repère `S` et `E` avec `indexOf` sur chaque ligne, et code-les `r * cols + c`. Initialise : `queue[tail++] = start; distance[start] = 0;`.

</details>

<details><summary>Indice 2</summary>

- Dans la boucle, chaque voisin valide avec `distance[next] == -1` reçoit `distance[next] = distance[cell] + 1`, `previous[next] = cell`, puis il est enfilé.
- Le tracé : `for (int cell = previous[end]; cell != start; cell = previous[cell])`.

</details>
