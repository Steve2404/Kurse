# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Ordre naturel et comparateurs

<details><summary>Indice 1</summary>

`implements Comparable<Player>`, et `compareTo` rend `name.compareTo(other.name)`. `Collections.sort(liste)` sans comparateur utilise cet ordre.

</details>

<details><summary>Indice 2</summary>

`Comparator.nullsLast(Comparator.reverseOrder())` : les `null` à la fin, les autres du plus grand au plus petit. On le passe en 2e argument de `comparing(Player::bonus, …)`.

</details>

---

## Étape 2 — Le piège et la navigation

<details><summary>Indice 1</summary>

Pour un `TreeSet`, deux éléments sont « égaux » si le **comparateur** rend 0. `equals` n'est pas consulté.

</details>

<details><summary>Indice 2</summary>

- `floorKey(k)` donne la plus grande clé ≤ k, et `ceilingKey(k)` la plus petite ≥ k. `lowerKey` et `higherKey` sont **strictement** plus petites ou plus grandes.
- `headMap(k)` exclut k, et `tailMap(k)` l'inclut.

</details>

---

## Étape 3 — Intervalles et médiane

<details><summary>Indice 1</summary>

Fusion : si `merged` n'est pas vide et que le dernier a `end() >= i.start()`, retire-le (`remove(size - 1)`), puis ajoute `new Interval(last.start(), Math.max(last.end(), i.end()))`.

</details>

<details><summary>Indice 2</summary>

Médiane : après l'ajout, si `low.size() > high.size() + 1`, alors `high.offer(low.poll())`. Si `high.size() > low.size()`, alors `low.offer(high.poll())`.

</details>
