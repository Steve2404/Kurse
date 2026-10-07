# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Le tri topologique

<details><summary>Indice 1</summary>

`next.computeIfAbsent(A, k -> new ArrayList<>()).add(B)`. Pour les degrés, `putIfAbsent(A, 0)` (A existe même s'il n'a aucun prédécesseur) et `merge(B, 1, Integer::sum)`.

</details>

<details><summary>Indice 2</summary>

`merge(n, -1, Integer::sum)` **rend** la nouvelle valeur : `if (inDegree.merge(n, -1, Integer::sum) == 0) ready.offer(n);`.

</details>

---

## Étape 2 — Trois parcours

<details><summary>Indice 1</summary>

Le graphe : `g.computeIfAbsent(A, k -> new TreeMap<>()).put(B, km)`, et la même chose de B vers A. Les `TreeMap` donnent les voisins dans l'ordre alphabétique.

</details>

<details><summary>Indice 2</summary>

- `dfs` : `push(start)`, puis `while` : `pop`, `continue` si déjà vu, puis empile les voisins de la fin vers le début.
- `bfs` : `parent.put(from, null)`, puis le chemin `for (String c = to; c != null; c = parent.get(c)) path.addFirst(c);`.
- `dijkstra` : `if (s.km() > dist.get(s.city())) continue;`.

</details>

---

## Étape 3 — Fenêtre glissante et API `Deque`

<details><summary>Indice 1</summary>

La deque contient des **indices**. En tête : un indice `<= i - k` est sorti de la fenêtre. En queue : un indice dont la valeur est `<= a[i]` ne sera plus jamais le max, tant que `a[i]` est là.

</details>

<details><summary>Indice 2</summary>

`push` = `addFirst`, `pop` = `removeFirst`, `peek` = `peekFirst`. La famille `offer`/`poll`/`peek` rend `false` ou `null` au lieu de lever une exception.

</details>
