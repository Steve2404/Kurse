# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — La famille `show`

<details><summary>Indice 1</summary>

Chaque surcharge rend simplement le **nom** de son type de paramètre : `static String show(int x) { return "int"; }`. Tout le travail est dans les **appels** du `main`.

</details>

<details><summary>Indice 2</summary>

Les trois phases de `javac`, dans l'ordre : 1) correspondance exacte ou **élargissement** (primitif ou de référence), sans boxing ni varargs ; 2) avec boxing et unboxing ; 3) avec varargs. Dès qu'une phase trouve une méthode, les suivantes ne sont pas essayées.

</details>

---

## Étape 2 — `box`, `pick`, `text`

<details><summary>Indice 1</summary>

`box('c')` : un `char` s'élargit en `long` dès la phase 1. `box(5.0)` : aucun élargissement de `double` vers `long`, donc on passe en phase 2, où `Double` est un `Object`.

</details>

<details><summary>Indice 2</summary>

Pour `null`, toutes les versions à référence conviennent. Java prend la **plus spécifique**, celle dont le type est un sous-type de toutes les autres.

</details>

---

## Étape 3 — `sum`, `add`, et `static` à côté d'instance

<details><summary>Indice 1</summary>

`sum(1, 2)` correspond à `sum(int, int)` dès la phase 1. Le varargs n'est essayé qu'en phase 3.

</details>

<details><summary>Indice 2</summary>

`twice` d'instance s'appelle `new Printer().twice("ab")`, et `twice` static s'appelle `Printer.twice(21)`.

</details>

---

## Étape 4 — Le sérialiseur JSON

<details><summary>Indice 1</summary>

Les tableaux : un `StringBuilder("[")`, puis chaque élément précédé de `","` sauf le premier, puis `']'`. Dans `toJson(int[])`, l'appel `toJson(values[i])` choisit `toJson(int)`.

</details>

<details><summary>Indice 2</summary>

- `toJson(Object)` : une chaîne `if / else if` qui teste `o == null`, puis `o instanceof String s`, puis `Integer i`… et rappelle la bonne surcharge (`toJson(s)`, `toJson(i.intValue())`…).
- `field` : `toJson(key) + ":" + json`.
- `object` : `"{" + String.join(",", fields) + "}"`.

</details>
