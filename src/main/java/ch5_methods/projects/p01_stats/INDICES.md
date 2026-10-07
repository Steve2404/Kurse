# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — `sum` et `count` : un varargs est un tableau

<details><summary>Indice 1</summary>

Dans la méthode, `int... values` s'utilise **exactement** comme un `int[]` : `values.length`, `values[i]`, for-each.

</details>

<details><summary>Indice 2</summary>

`count` teste `values == null` **avant** de lire `values.length`. Pour l'appel, `(int[]) null` dit à `javac` que `null` **est** le tableau.

</details>

---

## Étape 2 — Un paramètre fixe avant le varargs

<details><summary>Indice 1</summary>

`average` = `(first + sum(rest)) / (1.0 + rest.length)` : le `1.0` force une division en `double`. Tu peux réutiliser `sum`.

</details>

<details><summary>Indice 2</summary>

`round2(x)` = `Math.round(x * 100) / 100.0`. `max(first, rest...)` part de `first` et compare chaque élément de `rest` avec `Math.max`.

</details>

---

## Étape 3 — Rendre des tableaux, sans abîmer celui de l'appelant

<details><summary>Indice 1</summary>

`modes` : deux passages sur la copie triée, avec la technique des séries du chapitre 4. Le 1er mesure la plus longue série. Le 2e recopie dans `result` chaque valeur dont la série a cette longueur. Termine par `Arrays.copyOf(result, k)`.

</details>

<details><summary>Indice 2</summary>

- **Lomuto :** `pivot = a[high]`, `i = low`. Pour `j` de `low` à `high - 1`, si `a[j] < pivot`, alors `swap(a, i++, j)`. Puis `swap(a, i, high)`, et on rend `i`.
- **Quickselect :** `while (true)`. Si `p == k - 1`, c'est la réponse. Sinon, on resserre `low` ou `high`.

</details>

---

## Étape 4 — Varargs de tableaux et d'objets

<details><summary>Indice 1</summary>

`concat` : une 1re boucle calcule la taille totale. Une 2e boucle copie avec `System.arraycopy(a, 0, out, pos, a.length)` et avance `pos`.

</details>

<details><summary>Indice 2</summary>

`describe` : `if (items == null) return "tableau null";`. Sinon, `items.length + " element(s) :"`, puis `' '` et chaque élément. `append(o)` affiche `null` pour un élément `null`.

</details>

---

## Étape 5 — Un histogramme et une sortie anticipée

<details><summary>Indice 1</summary>

Le plus haut niveau : `max(counts[0], counts)`. Puis `for (int level = top; level >= 1; level--)`, et pour chaque valeur : `c >= level ? " ## " : "    "`.

</details>

<details><summary>Indice 2</summary>

Construis chaque ligne dans un `StringBuilder` qui commence par `String.format("%2d |", level)`, et affiche `line.toString().stripTrailing()`.

</details>
