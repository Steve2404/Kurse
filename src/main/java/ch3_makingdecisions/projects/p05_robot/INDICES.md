# Projet 5 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 4. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les directions et les obstacles

<details><summary>Indice 1</summary>

`dx("E")` vaut 1, `dx("O")` vaut −1, sinon 0. `dy("N")` vaut 1, `dy("S")` vaut −1, sinon 0. Une direction est inconnue si **les deux** valent 0.

</details>

<details><summary>Indice 2</summary>

Obstacle : `(3 * cx + 5 * cy) % 11 == 0`, **et** la case n'est pas (0, 0). Écris cette exception avec `&& !(…)`.

</details>

---

## Étape 2 — La boucle des commandes

<details><summary>Indice 1</summary>

`for (int i = 3; i < args.length; i += 2)` : `args[i]` est la direction, `args[i + 1]` le nombre de pas. Un compteur séparé numérote les commandes (1, 2, 3…).

</details>

<details><summary>Indice 2</summary>

La position, la batterie et le total de pas sont des champs `static`, lus aussi par la méthode qui dessine la carte.

</details>

---

## Étape 3 — Les pas : trois sorties différentes

<details><summary>Indice 1</summary>

Place l'étiquette `commands:` sur le `for`. Dans le `while (done < steps)`, calcule la case **suivante** `(nx, ny)` **avant** de bouger, puis teste-la.

</details>

<details><summary>Indice 2</summary>

- Mur : `nx < 0 || ny < 0 || nx >= width || ny >= height`. Pour le mur comme pour l'obstacle, affiche le message, puis `continue commands;`.
- Batterie : `if (battery == 0) { …; break commands; }`, testée **en premier**.
- Après le `while`, la ligne « arrive en » n'est atteinte que si la commande s'est terminée normalement.

</details>

---

## Étape 4 — Le bilan et la carte

<details><summary>Indice 1</summary>

La distance de Manhattan au départ (0, 0) vaut `x + y`, puisque x et y ne sont jamais négatifs ici.

</details>

<details><summary>Indice 2</summary>

`for (int row = height - 1; row >= 0; row--)`, puis `for (int col = 0; col < width; col++)`. Chaque ligne commence par `row + " "`. La chaîne `if` / `else if` teste R, puis S, puis #.

</details>
