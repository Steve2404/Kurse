# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — La structure

<details><summary>Indice 1</summary>

`insert` itératif : si l'arbre est vide, `root = new Node(v)`. Sinon, descends à partir de `root` dans un `while (true)`. Si `v` égale la valeur du nœud, `return false`. Si le fils du bon côté est `null`, crée-le là.

</details>

<details><summary>Indice 2</summary>

- `Cursor` : un `pushLeft(Node n)` qui empile `n`, puis `n.left`, etc., jusqu'à `null`.
- `next()` : `Node n = stack[--top]; pushLeft(n.right); return n.value;`.
- Dans le constructeur, `SortedTree.this.root` désigne la racine de l'arbre **englobant**.

</details>

---

## Étape 2 — Le dessin et les niveaux

<details><summary>Indice 1</summary>

`visitSideways` appelle une méthode `private sideways(Node n, int depth, Visitor v)` : d'abord `sideways(n.right, depth + 1, v)`, puis `v.visit(n.value, depth)`, puis la gauche.

</details>

<details><summary>Indice 2</summary>

`levels()` : une file `Node[]` et un `int[] depth` de même taille, avec `head` et `tail`. Mémorise le niveau courant : quand la profondeur change, ajoute `" | "` avant la valeur, sinon `" "`.

</details>

---

## Étape 3 — Requêtes

<details><summary>Indice 1</summary>

La classe locale s'écrit **dans** `countBetween`, comme une classe normale : `class RangeCounter { int count; void walk(Node n) { … } }`. Puis `RangeCounter c = new RangeCounter(); c.walk(root); return c.count;`.

</details>

<details><summary>Indice 2</summary>

- `floor` : `for (Node n = root; n != null; n = v < n.value ? n.left : n.right)`. Une valeur `< v` est une candidate, qu'on retient avant de descendre à droite.
- `commonAncestor` : à gauche si les deux sont plus petits, à droite si les deux sont plus grands, sinon c'est ce nœud.

</details>

---

## Étape 4 — Supprimer et rééquilibrer

<details><summary>Indice 1</summary>

`private Node remove(Node n, int v)` rend la **nouvelle racine** du sous-arbre. On l'utilise ainsi : `n.left = remove(n.left, v)`. Si `n` a au plus un enfant, rends cet enfant (et `size--`).

</details>

<details><summary>Indice 2</summary>

Deux enfants : `succ` = le nœud le plus à gauche de `n.right`. Copie `succ.value` dans `n`, puis `n.right = remove(n.right, succ.value)`. `balanced` : une méthode `private static Node build(int[] a, int lo, int hi)`.

</details>
