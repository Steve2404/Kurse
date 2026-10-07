# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Trier sans abîmer l'original

<details><summary>Indice 1</summary>

`Arrays.copyOf(tableau, tableau.length)` crée un **nouveau** tableau avec les mêmes valeurs. Trie la copie.

</details>

<details><summary>Indice 2</summary>

`Arrays.toString(t)` affiche un tableau sous la forme `[47, 58, …]`.

</details>

---

## Étape 2 — Statistiques et types de `Math`

<details><summary>Indice 1</summary>

Moyenne : `(double) sum / a.length`. Médiane sur le tableau **trié**, avec n pair : `(sorted[n / 2 - 1] + sorted[n / 2]) / 2.0`.

</details>

<details><summary>Indice 2</summary>

Écart type : une 2e boucle qui accumule `Math.pow(note - mean, 2)`, puis `Math.sqrt(somme / n)`. `Math.min` et `Math.max` dans la 1re boucle donnent le minimum et le maximum, en partant de `Integer.MAX_VALUE` et `Integer.MIN_VALUE`.

</details>

---

## Étape 3 — Rechercher

<details><summary>Indice 1</summary>

`low = 0`, `high = length - 1`, `while (low <= high)`. Milieu : `(low + high) >>> 1`. Selon la comparaison, `low = mid + 1` ou `high = mid - 1`.

</details>

<details><summary>Indice 2</summary>

Quand la boucle s'arrête sans avoir trouvé, `low` **est** le point d'insertion. Rends `-(low + 1)`.

</details>

---

## Étape 4 — Fusionner, faire tourner, comparer

<details><summary>Indice 1</summary>

Fusion : trois indices `i`, `j`, `k`. `while (i < a.length && j < b.length) result[k++] = a[i] <= b[j] ? a[i++] : b[j++];`, puis deux boucles pour vider le reste.

</details>

<details><summary>Indice 2</summary>

Rotation à droite de k : inverse **tout** le tableau, puis inverse les k premières cases, puis inverse le reste. Écris une méthode `reverse(t, de, à)` qui échange les cases deux à deux.

</details>

---

## Étape 5 — Noms, podium, tableaux irréguliers, valeurs par défaut

<details><summary>Indice 1</summary>

Podium : la note du rang r est `sorted[sorted.length - 1 - r]`. Une boucle sur `Data.GROUP_A` retrouve **tous** les élèves qui ont cette note (même indice dans `Data.NAMES`).

</details>

<details><summary>Indice 2</summary>

`new double[2][]` crée un tableau de **2 références** de lignes, sans créer les lignes. `Arrays.fill(t, 7)` remplit toutes les cases.

</details>
