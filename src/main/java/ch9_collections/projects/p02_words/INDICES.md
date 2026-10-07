# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Compter et indexer

<details><summary>Indice 1</summary>

`text.toLowerCase().split("[^a-z]+")` coupe sur tout ce qui n'est pas une lettre minuscule. Le premier morceau peut être vide si le texte commence par un séparateur, d'où le test `isEmpty()`.

</details>

<details><summary>Indice 2</summary>

- `merge(w, 1, Integer::sum)` : si la clé est absente, elle prend 1 ; sinon, `ancien + 1`.
- `computeIfAbsent(w, k -> new TreeSet<>())` rend le set (créé si besoin), sur lequel on fait `.add(d)`.

</details>

---

## Étape 2 — Top-k, index, requêtes

<details><summary>Indice 1</summary>

Une `PriorityQueue` construite avec un comparateur garde le **plus petit** (selon lui) en tête. `poll()` retire ce plus petit. Avec « le plus faible d'abord », il reste les k plus forts.

</details>

<details><summary>Indice 2</summary>

Requêtes : `p = q.split(" ")`, une copie de l'ensemble de `p[0]`, puis un `switch (p[1])` avec `retainAll`, `addAll` ou `removeAll` sur l'ensemble de `p[2]`. Utilise `getOrDefault(…, Set.of())` pour un mot absent.

</details>

---

## Étape 3 — Anagrammes, `merge` qui supprime, les trois `Set`

<details><summary>Indice 1</summary>

Anagrammes : `char[] c = w.toCharArray(); Arrays.sort(c);`, puis `groups.computeIfAbsent(new String(c), k -> new ArrayList<>()).add(w)`. Deux anagrammes ont la même clé.

</details>

<details><summary>Indice 2</summary>

Le remappage de `merge` reçoit l'ancienne valeur et la valeur passée : `(old, delta) -> old + delta == 0 ? null : old + delta`. Rendre `null` **supprime** la clé.

</details>
