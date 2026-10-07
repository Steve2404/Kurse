# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — L'arbre

<details><summary>Indice 1</summary>

Une expression est un **arbre** : `3*x` est un nœud `Mul` dont les enfants sont `Num(3)` et `Var`. Chaque méthode (`eval`, `derive`, `size`) appelle la même méthode sur ses enfants, puis combine les résultats.

</details>

<details><summary>Indice 2</summary>

- `Num.show()` : `value == Math.rint(value) ? String.valueOf((long) value) : String.valueOf(value)`.
- `BinaryOp.show()` : `"(" + left + " " + symbol() + " " + right + ")"`. Chaque sous-classe ne fournit que `symbol()`.

</details>

---

## Étape 2 — L'analyseur

<details><summary>Indice 1</summary>

`expr()` : `Expr e = term();`, puis `while` on voit `+` ou `-` : avance, lis `r = term()`, et fais `e = new Add(e, r)` (ou `Sub`). Le nouveau nœud **englobe** l'ancien `e` : c'est ce qui rend l'opération associative à gauche.

</details>

<details><summary>Indice 2</summary>

- `atom()` : si `(`, avance, `expr()`, puis avance encore pour sauter `)`. Si `x`, avance et rends `new Var()`. Sinon, lis un nombre.
- `number()` : avance tant que c'est un chiffre ou un point, puis `Double.parseDouble(text.substring(start, pos))`.

</details>

---

## Étape 3 — Dériver et simplifier

<details><summary>Indice 1</summary>

`simplify()` d'un `BinaryOp` : `l = left.simplify(); r = right.simplify();`, puis les règles dans l'ordre. Par défaut, `new Add(l, r)`. Le cas `Num op Num` s'écrit `l instanceof Num a && r instanceof Num b`.

</details>

<details><summary>Indice 2</summary>

`derive()` ne simplifie pas : il construit l'arbre brut (d'où 23 nœuds). C'est `simplify()` sur ce résultat qui donne la forme courte. `size()` se compte sur les deux.

</details>

---

## Étape 4 — Newton et polymorphisme

<details><summary>Indice 1</summary>

`fp = f.derive().simplify();` une seule fois avant la boucle. Puis 6 fois : `x = x - f.eval(x) / fp.eval(x);`.

</details>

<details><summary>Indice 2</summary>

La dernière ligne : `e.getClass().getSimpleName()`, puis `e`, puis `e.simplify()` et `e.simplify().getClass().getSimpleName()`.

</details>
