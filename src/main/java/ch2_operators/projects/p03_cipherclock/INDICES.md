# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Le modulo toujours positif

<details><summary>Indice 1</summary>

`v % b` est entre −(b − 1) et b − 1. Ajoute `b` : le résultat est maintenant entre 1 et 2b − 1, donc positif. Il ne reste qu'à le ramener sous `b`.

</details>

<details><summary>Indice 2</summary>

La forme : `(v % b + b) % b`. Vérifie-la pour −3 : (−3 + 26) % 26 = 23. Et pour 29 : (3 + 26) % 26 = 3.

</details>

---

## Étape 2 — Le chiffre de César

<details><summary>Indice 1</summary>

`c - 'A' + key` est un `int`. Passe-le dans ta méthode `mod`, rajoute `'A'`, puis convertis en `char` avec un cast qui porte sur **toute** l'expression, entre parenthèses.

</details>

<details><summary>Indice 2</summary>

Pour le mot : commence par `"" +` avant le premier `shift(...)`. Dès que le premier opérande est un `String`, chaque `+` suivant concatène. Pour l'aller-retour : une clé de −26 est un tour complet de l'alphabet.

</details>

---

## Étape 3 — L'horloge modulaire

<details><summary>Indice 1</summary>

Total = heure × 60 + minute + décalage. Minutes dans le jour = `mod(total, MINUTES_PER_DAY)`. Heure affichée = minutes dans le jour / 60 ; minutes affichées = minutes dans le jour % 60.

</details>

<details><summary>Indice 2</summary>

- Jours = `(total - minutesDansLeJour) / MINUTES_PER_DAY`. Pour −75 : `mod` donne 1365, et (−75 − 1365) / 1440 = −1 exactement.
- Deux chiffres : `n < 10 ? "0" + n : "" + n`.

</details>

---

## Étape 4 — Les débordements

<details><summary>Indice 1</summary>

Un `byte` va de −128 à 127. Quand on dépasse, on « fait le tour » : on retire 256 jusqu'à rentrer dans l'intervalle. Pour un `short`, on retire 65536.

</details>

<details><summary>Indice 2</summary>

- `max + 1L` : le littéral `long` fait **promouvoir** toute l'addition en `long`.
- Le cast d'un `double` trop grand vers `int` donne `Integer.MAX_VALUE`.
- Pour la question sur `0.1f + 0.2f`, compare le nombre de chiffres qu'affiche un `float` à celui qu'affiche un `double`.

</details>
