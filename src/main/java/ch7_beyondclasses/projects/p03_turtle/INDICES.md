# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les commandes

<details><summary>Indice 1</summary>

Chaque type cité dans `permits` doit être `final`, `sealed` ou `non-sealed`. Un record est **implicitement `final`**. Une classe ouverte à l'héritage s'écrit `non-sealed`.

</details>

<details><summary>Indice 2</summary>

- `Repeat.size()` : la somme des `size()` du corps, multipliée par `times`.
- `depth()` : 1 + le maximum des `r.depth()` pour chaque `c instanceof Repeat r` du corps.
- `Square.expand()` : `new Command[] {new Repeat(4, new Command[] {new Move(side), Turn.RIGHT})}`.

</details>

---

## Étape 2 — Analyser le programme

<details><summary>Indice 1</summary>

`parseBlock()` : `while (pos < tokens.length && !tokens[pos].equals("]"))`. On lit le mot, puis un `switch` expression rend la commande. Pour un nombre, `Integer.parseInt(tokens[pos++])`.

</details>

<details><summary>Indice 2</summary>

Branche `REPEAT` : lis le nombre, `pos++` pour sauter `[`, puis `Command[] body = parseBlock();`, puis `pos++` pour sauter `]`, puis `yield new Repeat(times, body);`. Les commandes s'accumulent dans un tableau qui double de taille.

</details>

---

## Étape 3 — Dessiner

<details><summary>Indice 1</summary>

`Direction.turn` : `values()[Math.floorMod(ordinal() + quarters, 4)]`. `floorMod(-1, 4)` vaut 3, alors que `-1 % 4` vaut −1.

</details>

<details><summary>Indice 2</summary>

`run` : `if (c instanceof Move m) …` suivi d'un `else if` par type. `Repeat` : deux boucles imbriquées (`times`, puis le corps) qui rappellent `run`. `Macro` : `for (Command inner : macro.expand()) run(inner);`.

</details>
