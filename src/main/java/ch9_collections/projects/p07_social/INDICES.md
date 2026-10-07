# Projet 7 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 6. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le graphe et les communautés

<details><summary>Indice 1</summary>

`find(x)` : `p = parent.get(x)`. Si p n'est pas x, alors `p = find(p)` puis `parent.put(x, p)` (c'est la compression), et l'on rend p. `union` relie les deux **racines**.

</details>

<details><summary>Indice 2</summary>

`groups()` : `byRoot.computeIfAbsent(find(x), k -> new TreeSet<>()).add(x)` pour chaque membre. Puis une `TreeMap` clé = premier élément du groupe, valeur = le groupe, ce qui trie les groupes.

</details>

---

## Étape 2 — Suggestions et séparation

<details><summary>Indice 1</summary>

`Map.Entry.<String, Integer>comparingByValue()` : le type est donné **explicitement**, sinon `.reversed()` ne saurait pas sur quoi il travaille (l'inférence ne remonte pas la chaîne).

</details>

<details><summary>Indice 2</summary>

Les degrés : `degree.put(me, 0)`, puis le BFS du projet 3. Un voisin non encore dans `degree` reçoit `degree.get(c) + 1`.

</details>

---

## Étape 3 — Le fil d'actualité

<details><summary>Indice 1</summary>

`byAuthor.values().forEach(list -> list.sort(newestFirst))` trie chaque liste **en place**. Les sources sont les amis de `ME` plus `ME` lui-même.

</details>

<details><summary>Indice 2</summary>

Un curseur est **immuable** (c'est un record) : pour « avancer », on crée `new Cursor(c.posts(), c.index() + 1)` et on le remet dans le tas.

</details>

---

## Étape 4 — Tendances

<details><summary>Indice 1</summary>

C'est le même tas min de taille k qu'au projet 2 (le top des mots), avec `Map.Entry.comparingByValue()`.

</details>

<details><summary>Indice 2</summary>

`Comparator.comparing((String m) -> friends.get(m).size())` : le type du paramètre est écrit **dans** la lambda, pour que `.reversed()` puisse s'appliquer.

</details>
