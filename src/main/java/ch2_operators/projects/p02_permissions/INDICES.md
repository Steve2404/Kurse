# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Lire et afficher un mode

<details><summary>Indice 1</summary>

Dans un triplet de 3 bits, `r` vaut 4 (`100`), `w` vaut 2 (`010`), `x` vaut 1 (`001`). `(bits & 4) != 0` répond « le bit de lecture est-il allumé ? ». Les parenthèses autour de `bits & 4` sont indispensables.

</details>

<details><summary>Indice 2</summary>

- La méthode triplet commence par `"" +`, pour que les trois `char` (`'r'` ou `'-'`…) se **concatènent** au lieu de s'additionner.
- Pour le mode complet : `triplet(mode >> 6 & 7) + triplet(mode >> 3 & 7) + triplet(mode & 7)`.
- L'affichage octal vient de `Integer.toOctalString`.

</details>

---

## Étape 2 — Les masques et les commandes `chmod`

<details><summary>Indice 1</summary>

Numérote les 9 bits de 8 (lecture du propriétaire) à 0 (exécution des autres). Écriture du propriétaire = bit 7 = `1 << 7`. Lecture des autres = bit 2 = `1 << 2`.

</details>

<details><summary>Indice 2</summary>

Le « pas » bit à bit est `~` : `0666 & ~umask` garde les bits de 0666 qui ne sont **pas** dans le umask. Pour la question, regarde ce que fait `x ^ m ^ m` sur un seul bit, avec m = 1 : il change, puis change encore.

</details>

---

## Étape 3 — La décision d'accès

<details><summary>Indice 1</summary>

Le groupe n est membre si `(groups & 1 << n) != 0`. Le décalage `<<` passe avant `&`, donc pas besoin de parenthèses autour de `1 << n`. En revanche, il en faut autour du `&`, à cause du `!=`.

</details>

<details><summary>Indice 2</summary>

Le choix du triplet : `owner ? mode >>> 6 & 7 : inGroup ? mode >> 3 & 7 : mode & 7`. Le même ternaire, avec des textes à la place des nombres, donne le « en tant que … ». L'écriture est autorisée si le bit 2 du triplet choisi est allumé.

</details>

---

## Étape 4 — Les pièges de `~`, `>>` et `>>>`

<details><summary>Indice 1</summary>

`~x` inverse **les 32 bits**, pas seulement les 9 du mode. En complément à deux, inverser tous les bits revient à calculer `-x - 1`.

</details>

<details><summary>Indice 2</summary>

`-16` en binaire : 28 bits à 1, puis `0000`. `>>> 28` pousse tout de 28 rangs vers la droite et fait entrer des zéros à gauche. Il ne reste donc que les 4 bits de tête. Pour `>> 2`, ce sont des 1 qui entrent.

</details>
