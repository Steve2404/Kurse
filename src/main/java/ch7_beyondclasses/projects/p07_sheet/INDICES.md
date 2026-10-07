# Projet 7 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 6. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les briques

<details><summary>Indice 1</summary>

`Ref.parse("B3")` : `new Ref(text.charAt(0) - 'A', Integer.parseInt(text.substring(1)) - 1)`. `toString` : `"" + (char) ('A' + col) + (row + 1)`. Le `"" +` est indispensable, sinon le `char` s'additionne.

</details>

<details><summary>Indice 2</summary>

Un record imbriqué dans une interface est implicitement `static` (et `public`). `sealed interface Expr { record Num(double value) implements Expr { } … }` : sans `permits`, les sous-types autorisés sont ceux **du même fichier**.

</details>

---

## Étape 2 — La feuille

<details><summary>Indice 1</summary>

C'est le même analyseur que celui du projet 6 du chapitre 6. `factor` teste dans l'ordre : `(`, puis `text.startsWith("SUM(", pos)`, puis une lettre (une référence), puis un nombre.

</details>

<details><summary>Indice 2</summary>

`SUM(A1:A3)` : `colon = text.indexOf(':', pos)` et `close = text.indexOf(')', pos)`. Les deux références sont `text.substring(pos + 4, colon)` et `text.substring(colon + 1, close)`. Puis `pos = close + 1`.

</details>

---

## Étape 3 — Recalculer dans le bon ordre

<details><summary>Indice 1</summary>

Pour chaque formule, `Expr.references(f.expr(), deps, 0)` donne les cellules dont elle dépend. Pour chacune, `g.add(dep, cetteCellule)`. La file de Kahn démarre avec tous les ids de degré entrant 0.

</details>

<details><summary>Indice 2</summary>

- Dans la boucle : défile `u` et calcule sa valeur. Puis, pour chaque `v` avec `edge[u][v]`, décrémente `inDegree[v]` ; s'il tombe à 0, enfile `v`.
- Après la boucle, tout id avec `inDegree > 0` est en cycle.
- `Evaluator` lit `values`, qu'il faut donc remplir **dans l'ordre** de Kahn.

</details>

---

## Étape 4 — Modifier et recalculer

<details><summary>Indice 1</summary>

`set` remplace simplement la cellule. `recalculate()` refait **tout** le graphe et l'ordre : rien n'est à mettre à jour à la main.

</details>

<details><summary>Indice 2</summary>

Pour l'expérience sur `Mod`, demande-toi quel `permits` Java déduit **implicitement** quand la clause est absente.

</details>
