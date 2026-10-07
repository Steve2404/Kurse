# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Des calculs parallèles exacts

<details><summary>Indice 1</summary>

`isqrt` : pars de `s = (long) Math.sqrt(n)`. Tant que `s * s > n`, enlève 1 ; tant que `(s + 1) * (s + 1) <= n`, ajoute 1. `Math.sqrt` travaille en `double` et peut se tromper d'une unité sur de grands nombres.

</details>

<details><summary>Indice 2</summary>

Pour Collatz : `mapToObj(n -> new int[] {n, collatzLength(n)})`, puis `max` avec un comparateur sur la case 1, puis sur l'**opposé** de la case 0 (à égalité, le plus petit n gagne).

</details>

---

## Étape 2 — `reduce` et `collect`

<details><summary>Indice 1</summary>

En parallèle, l'identité est utilisée **une fois par morceau**. Une identité qui n'est pas neutre (10 pour une somme) est donc ajoutée plusieurs fois.

</details>

<details><summary>Indice 2</summary>

`collect(ArrayList::new, ArrayList::add, ArrayList::addAll)` : chaque morceau remplit **sa propre** liste, puis les listes sont recollées **dans l'ordre**.

</details>

---

## Étape 3 — L'ordre

<details><summary>Indice 1</summary>

`forEach` en parallèle traite les éléments dans un ordre quelconque ; `forEachOrdered` respecte l'ordre de la source. Trie la liste du `forEach` avant de comparer.

</details>

<details><summary>Indice 2</summary>

`findFirst()` respecte l'ordre, même en parallèle ; `findAny()` peut rendre n'importe quel élément qui convient : on n'affiche donc que `isPresent()`.

</details>

---

## Étape 4 — Collecteurs concurrents et tableaux

<details><summary>Indice 1</summary>

`groupingByConcurrent` rend une `ConcurrentMap` sans ordre garanti : copie-la dans une `TreeMap` avant de l'afficher.

</details>

<details><summary>Indice 2</summary>

`Arrays.parallelPrefix(t, Integer::sum)` remplace chaque case par la somme de toutes les cases jusqu'à elle.

</details>
