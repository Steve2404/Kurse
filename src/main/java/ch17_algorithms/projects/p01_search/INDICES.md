# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — La recherche linéaire

<details><summary>Indice 1</summary>

Une boucle `for` sur les indices, et un `return i` dès que la case vaut `key`. Après la boucle : rien trouvé.

</details>

<details><summary>Indice 2</summary>

Le pire des cas, c'est quand la clé est absente (ou à la dernière case) : on regarde **toutes** les cases.

</details>

---

## Étape 2 — La dichotomie

<details><summary>Indice 1</summary>

La boucle est `while (lo <= hi)` : quand `lo` dépasse `hi`, il ne reste aucune case possible. À cet instant, `lo` est exactement l'endroit où la clé aurait dû être.

</details>

<details><summary>Indice 2</summary>

`return -(lo + 1);` après la boucle. Pourquoi le « + 1 » ? Pense au point d'insertion 0.

</details>

---

## Étape 3 — Les bornes

<details><summary>Indice 1</summary>

Pour `lowerBound` : `hi = sorted.length`, boucle `while (lo < hi)`, et quand `sorted[mid] >= key`, on fait `hi = mid` (pas `mid - 1` : `mid` est peut-être la réponse).

</details>

<details><summary>Indice 2</summary>

`count` n'a pas de boucle à elle : c'est une soustraction de deux bornes.

</details>

---

## Étape 4 — Chercher avec une question

<details><summary>Indice 1</summary>

Comme `binary`, mais quand `isBad.test(mid)` est vrai, on **retient** `mid` (c'est peut-être la première) et on continue à chercher **à gauche** (`hi = mid - 1`).

</details>

<details><summary>Indice 2</summary>

Pour compter les appels dans le test : `AtomicInteger calls = new AtomicInteger();` et, dans la lambda, `calls.incrementAndGet();` avant de rendre la réponse. Une variable locale `int` ne marcherait pas : une lambda ne peut pas la modifier (chapitre 8).

</details>

---

## Étape 5 — La racine carrée entière

<details><summary>Indice 1</summary>

Même schéma que `firstBad` : la question monotone est « `mid * mid <= n` ? » (vrai, vrai, vrai… puis faux). On cherche le **dernier** vrai : on retient `mid` et on va à droite.

</details>

<details><summary>Indice 2</summary>

`mid <= n / mid` remplace `mid * mid <= n` sans débordement, mais divise par `mid` : traite `mid == 0` à part (`mid == 0 || mid <= n / mid`).

</details>

---

## Étape 6 — La dichotomie sur la réponse

<details><summary>Indice 1</summary>

Le glouton qui compte les jours : un compteur de jours (1 au départ), une charge (0). Pour chaque colis, si `charge + colis` dépasse la capacité, nouveau jour et charge remise à 0 ; puis on ajoute le colis.

</details>

<details><summary>Indice 2</summary>

La dichotomie cherche la **plus petite** capacité qui suffit : forme `lowerBound` (`while (lo < hi)`, `hi = mid` quand ça suffit, `lo = mid + 1` sinon).

</details>

---

## Étape 7 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quelle limite (tableau vide, une case, la dernière case, une clé absente), quel débordement, quel cas du camion n'est pas testé ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. `binary` boucle avec `lo < hi` au lieu de `lo <= hi` (elle ne regarde jamais la dernière case possible).
2. `binary` rend −1 au lieu de `-(point d'insertion) - 1`.
3. `lowerBound` utilise `<=` au lieu de `<` (elle devient une borne supérieure).
4. `upperBound` démarre avec `hi = length - 1` (« aucun » n'est plus possible).
5. `firstBad` calcule le milieu avec `(lo + hi) / 2` (il déborde pour un `n` énorme).
6. `isqrt` compare `mid * mid <= n` (il déborde pour un grand `n`).
7. `minCapacity` part du plus **petit** colis au lieu du plus gros.
8. Le glouton change de jour quand le camion est plein **pile** (`>=` au lieu de `>`).
9. `isqrt(-1)` n'est plus refusé.

</details>
