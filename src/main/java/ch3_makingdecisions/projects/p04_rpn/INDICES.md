# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les registres et la pile

<details><summary>Indice 1</summary>

Quatre champs `static Number x = 0;` (et y, z, t). Monter : `t = z; z = y; y = x; x = valeur;`, **dans cet ordre**, du haut vers le bas, pour ne rien écraser.

</details>

<details><summary>Indice 2</summary>

Descendre après une opération : `x = résultat; y = z; z = t;`. T n'est pas modifié : il garde sa valeur, qui est donc « recopiée » dans Z.

</details>

---

## Étape 2 — Lire un nombre : le piège du ternaire

<details><summary>Indice 1</summary>

`double value = Double.parseDouble(token);` Le nombre n'a pas de partie décimale si `value == (int) value`.

</details>

<details><summary>Indice 2</summary>

Dans le `if`, renvoie `Integer.valueOf((int) value)` ; sinon, `Double.valueOf(value)`. Pour comprendre le piège, rappelle-toi ce que fait Java avec `int` et `double` dans une même opération.

</details>

---

## Étape 3 — Le calcul : le pattern matching

<details><summary>Indice 1</summary>

`if (a instanceof Integer i && b instanceof Integer j) { return switch (op) { … }; }`. Dans le switch, `i + j` déballe automatiquement les deux `Integer`.

</details>

<details><summary>Indice 2</summary>

La branche division : `default -> { if (i % j == 0) { yield i / j; } yield (double) i / j; }`. Après le `if`, le cas général fait `double p = a.doubleValue();` (de même pour `b`), puis un second switch.

</details>

---

## Étape 4 — Zéro et changement de signe : la portée de flux

<details><summary>Indice 1</summary>

Si `n` **n'est pas** un `Double`, c'est un `Integer` : renvoie `n.intValue() == 0`. Sinon, la suite de la méthode peut utiliser `d`.

</details>

<details><summary>Indice 2</summary>

`CHS` : `if (n instanceof Integer i) return -i; else if (n instanceof Double d) return -d;` et un dernier `return n;` pour que la méthode compile.

</details>

---

## Étape 5 — La boucle et le `switch` des commandes

<details><summary>Indice 1</summary>

`case "+", "-", "x" -> dropAfterOperation(compute(token, y, x));`. Attention à l'ordre : on calcule **Y op X**. Le `default` du switch traite les nombres.

</details>

<details><summary>Indice 2</summary>

- Une variable `note` (vide par défaut) reçoit le message d'erreur de la division par zéro.
- `SWAP` a besoin d'une variable temporaire.
- `DROP` revient à « descendre » avec Y comme nouveau X.

</details>
