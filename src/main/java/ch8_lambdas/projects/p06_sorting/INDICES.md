# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — `Person` et `Order`

<details><summary>Indice 1</summary>

`then(next)` : `return (a, b) -> { int c = compare(a, b); return c != 0 ? c : next.compare(a, b); };`. `compare` sans préfixe désigne celui de `this`.

</details>

<details><summary>Indice 2</summary>

`by(key)` : `(a, b) -> Integer.compare(key.applyAsInt(a), key.applyAsInt(b))`. Évite `a - b` : la soustraction de deux grands `int` peut déborder.

</details>

---

## Étape 2 — Le trieur

<details><summary>Indice 1</summary>

`counting(order)` : `return (a, b) -> { comparisons++; return order.compare(a, b); };`. Chaque tri enveloppe son ordre **une fois** au début, puis compare avec l'ordre enveloppé.

</details>

<details><summary>Indice 2</summary>

`mergeSort` : `input.clone()`, puis une méthode récursive `sort(a, tmp, lo, hi, o)`. La fusion se fait dans `tmp`, puis `System.arraycopy(tmp, lo, a, lo, hi - lo + 1)`. `comparisons()` lit le champ, le remet à 0, puis rend l'ancienne valeur.

</details>

---

## Étape 3 — Les tris

<details><summary>Indice 1</summary>

`Person::name` convient à `Function<Person, String>`, et `Person::age` à `ToIntFunction<Person>` : ce sont les accesseurs générés du record.

</details>

<details><summary>Indice 2</summary>

Une méthode `static String names(Person[] people)` affiche les noms séparés par des espaces. Appelle `sorter.comparisons()` juste après chaque tri que tu mesures, et aussi pour **remettre à zéro** avant une mesure.

</details>

---

## Étape 4 — Sélection, dichotomie, capture

<details><summary>Indice 1</summary>

`top` : `input.clone()`, puis k passes. À la passe i, trouve l'indice du meilleur entre i et la fin, échange-le avec i, et rends `Arrays.copyOf(a, k)`.

</details>

<details><summary>Indice 2</summary>

`search` : `lo = 0`, `hi = length`, et `while (lo < hi)` avec `(lo + hi) >>> 1`. Si la clé du milieu est `< target`, alors `lo = mid + 1`, sinon `hi = mid`. Pour 30 ans, absent, le point d'insertion est 4, d'où −5.

</details>
