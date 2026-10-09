# Projet 10 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Fibonacci

<details><summary>Indice 1</summary>

Deux variables `previous = 0` (F(0)) et `current = 1` (F(1)). À chaque tour : `next = previous + current`, puis on décale.

</details>

<details><summary>Indice 2</summary>

Après `n` tours, `previous` vaut F(n). Vérifie à la main pour n = 0 (aucun tour : 0) et n = 1 (un tour : 1).

</details>

---

## Étape 2 — Le rendu de monnaie

<details><summary>Indice 1</summary>

Remplis `best` d'une valeur « impossible » (`Integer.MAX_VALUE`), sauf `best[0] = 0`. Ne fais jamais `best[a - c] + 1` sur une case impossible : elle déborderait.

</details>

<details><summary>Indice 2</summary>

`countWays` : `ways[0] = 1`, puis `for (int c : coins) for (int a = c; a <= amount; a++) ways[a] += ways[a - c];`.

</details>

---

## Étape 3 — Deux mots

<details><summary>Indice 1</summary>

Une table `int[a.length() + 1][b.length() + 1]` ; les boucles vont de 1 à la longueur **incluse**, et la lettre de la case `(i, j)` est `a.charAt(i - 1)` (le décalage d'un cran à cause de la ligne 0).

</details>

<details><summary>Indice 2</summary>

Pour remonter la LCS : `while (i > 0 && j > 0)`, ajoute les lettres égales à un `StringBuilder`, puis **retourne-le** à la fin (on l'a construit à l'envers).

</details>

---

## Étape 4 — Le sac à dos

<details><summary>Indice 1</summary>

Un seul tableau `best` de taille `capacity + 1`, rempli de 0. Une boucle sur les objets, et à l'intérieur une boucle sur les capacités.

</details>

<details><summary>Indice 2</summary>

`for (int w = capacity; w >= weights[k]; w--) best[w] = Math.max(best[w], best[w - weights[k]] + values[k]);`.

</details>

---

## Étape 5 — Croissant, et la grille

<details><summary>Indice 1</summary>

`tails` a la taille du tableau, et une variable `length` (0 au départ). Pour chaque `v`, cherche par dichotomie, dans `tails[0, length[`, la première case `>= v` (c'est le `lowerBound` du projet 1).

</details>

<details><summary>Indice 2</summary>

Pour la grille : `long[] paths` d'une ligne. Case bloquée : 0 ; coin de départ : 1 ; sinon `paths[c] += paths[c - 1]` (la case du dessus est déjà dans `paths[c]`).

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quel cas impossible, quel ordre de boucles, quel objet réutilisable, quelle égalité dans la remontée, quelle valeur égale dans la sous-suite croissante n'est pas testé ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Fibonacci rend F(n + 1) au lieu de F(n).
2. Le rendu de monnaie utilise une case impossible (elle déborde).
3. Un rendu impossible donne 0 au lieu de −1.
4. Le nombre de façons compte les ordres (boucle des sommes à l'extérieur).
5. La LCS avance mal quand les lettres sont égales.
6. La remontée de la LCS va à gauche à égalité au lieu d'en haut.
7. La LCS n'est pas retournée (elle est à l'envers).
8. La distance d'édition oublie la colonne de départ.
9. La distance d'édition paie un remplacement même pour deux lettres égales.
10. Le sac à dos parcourt les capacités dans l'ordre croissant (objets réutilisés).
11. La sous-suite croissante accepte deux valeurs égales.
12. Une case de départ bloquée est ignorée.
13. `fibonacci(-1)` n'est plus refusé.

</details>
