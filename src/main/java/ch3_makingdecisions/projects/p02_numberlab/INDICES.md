# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les nombres premiers

<details><summary>Indice 1</summary>

Une étiquette se place **juste avant** la boucle qu'elle nomme : `outer:` sur la ligne au-dessus du `for` des candidats. Dans la boucle des diviseurs, `continue outer;` abandonne le candidat en cours.

</details>

<details><summary>Indice 2</summary>

Après la boucle des diviseurs, il n'y a pas de test à faire. Si l'on arrive là, c'est qu'aucun `continue outer` n'a été exécuté : le nombre est premier, on l'ajoute au texte.

</details>

---

## Étape 2 — Les nombres parfaits

<details><summary>Indice 1</summary>

Commence la somme à 1 (1 divise tout nombre > 1), puis teste `d` de 2 tant que `d * d <= n`. Quand `d` divise `n`, `n / d` le divise aussi.

</details>

<details><summary>Indice 2</summary>

Ajoute `d`, puis ajoute `n / d` **seulement s'il est différent de `d`**. Pour 36, le diviseur 6 donne 36 / 6 = 6 : on ne le compte qu'une fois.

</details>

---

## Étape 3 — La conjecture de Collatz

<details><summary>Indice 1</summary>

Une méthode qui reçoit le départ et rend le nombre d'étapes, avec un `while (n != 1)`. Dans le `main`, une boucle sur les départs garde le meilleur avec un `if (steps > bestSteps)` : le `>` strict garde le **premier** à égalité.

</details>

<details><summary>Indice 2</summary>

Le paramètre de la méthode est un `long`. Le pas s'écrit avec un ternaire : `n = n % 2 == 0 ? n / 2 : 3 * n + 1;`. Pour la question sur `do/while`, déroule le départ 1 à la main avec les deux boucles.

</details>

---

## Étape 4 — Palindromes et nombres d'Armstrong

<details><summary>Indice 1</summary>

Retourner : `reversed = reversed * 10 + n % 10;` puis `n /= 10;`, dans un `do { … } while (n > 0);`. Un palindrome est égal à son retourné.

</details>

<details><summary>Indice 2</summary>

Pour Armstrong, une boucle `for (int rest = n; rest > 0; rest /= 10)` donne chaque chiffre avec `rest % 10`. Le cube s'écrit `digit * digit * digit`.

</details>

---

## Étape 5 — Euclide et la recherche à double boucle

<details><summary>Indice 1</summary>

Dans le `while (b != 0)` d'Euclide, il faut une variable temporaire : `r = a % b; a = b; b = r;`. À la sortie, `a` est le PGCD. Pour le PPCM, divise **avant** de multiplier.

</details>

<details><summary>Indice 2</summary>

Recherche : étiquette `search:` sur le `for (x …)`. La boucle intérieure part de `y = x`. Elle contient d'abord le test d'égalité (`break search;`), puis le test de dépassement (`break;`).

</details>

---

## Étape 6 — FizzBuzz, version `switch`

<details><summary>Indice 1</summary>

Le code vaut `(i % 3 == 0 ? 1 : 0) + (i % 5 == 0 ? 2 : 0)`. Il ne peut valoir que 0, 1, 2 ou 3.

</details>

<details><summary>Indice 2</summary>

`String word = switch (code) { case 1 -> "Fizz"; case 2 -> "Buzz"; case 3 -> "FizzBuzz"; default -> "" + i; };`.

</details>
