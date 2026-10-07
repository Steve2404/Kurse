# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les outils

<details><summary>Indice 1</summary>

Répéter : partir de `""` et concaténer le caractère `times` fois. Compter les chiffres : partir de 1 et diviser par 10 tant que le nombre est ≥ 10.

</details>

<details><summary>Indice 2</summary>

Aligner à droite : `repeat(' ', largeur - chiffres(valeur)) + valeur`.

</details>

---

## Étape 2 — Pyramide et losange creux

<details><summary>Indice 1</summary>

Pyramide, ligne r de 1 à n : `n - r` espaces, puis `2r - 1` étoiles. Vérifie-le : ligne 1 → 4 espaces, 1 étoile ; ligne 5 → 0 espace, 9 étoiles.

</details>

<details><summary>Indice 2</summary>

Losange, indice `row` de −(n − 1) à n − 1 : `half = n - 1 - |row|`, avec `|row|` écrit `row < 0 ? -row : row`. On écrit d'abord `n - 1 - half` espaces, puis une étoile. Si `half > 0`, on ajoute `2 * half - 1` espaces et une seconde étoile.

</details>

---

## Étape 3 — Damier et croix

<details><summary>Indice 1</summary>

Damier : `(r + c) % 2 == 0 ? '#' : '.'`. Croix : la case (r, c) est sur la diagonale principale si `c == r`, sur l'autre si `c == n - 1 - r`.

</details>

<details><summary>Indice 2</summary>

- Le `if` de la croix teste « sur **aucune** des deux » : `c != r && c != n - 1 - r`. Il ajoute un espace puis fait `continue`.
- Après ce `if`, un ternaire imbriqué choisit entre `'+'`, `'\\'` et `'/'`.

</details>

---

## Étape 4 — La table de multiplication alignée

<details><summary>Indice 1</summary>

L'en-tête commence par `"   |"`, puis chaque numéro de colonne aligné sur 4. La ligne de séparation est `"---+"` suivie de `4 × n` tirets.

</details>

<details><summary>Indice 2</summary>

Chaque ligne : numéro aligné sur 2, puis `" |"`, puis chaque produit `r * c` aligné sur 4.

</details>

---

## Étape 5 — Le triangle de Pascal

<details><summary>Indice 1</summary>

Pour chaque ligne r de 0 à n, commence avec `2 × (n − r)` espaces (dans la solution, `pascal(n + 1)` est appelée avec n + 1 lignes). Puis une boucle k de 0 à r, avec une variable `value` qui part de 1.

</details>

<details><summary>Indice 2</summary>

Dans la boucle : ajoute `value` aligné sur 4, **puis** calcule le suivant `value = value * (r - k) / (k + 1);`. `value` peut être un `long` : il faut alors un cast `(int)` pour la méthode d'alignement.

</details>
