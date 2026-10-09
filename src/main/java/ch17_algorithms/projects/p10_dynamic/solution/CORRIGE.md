# Projet 10 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Dynamic.java`](Dynamic.java), et les tests de référence dans [`DynamicTest.java`](DynamicTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**, et recoupés avec des résultats connus. Les temps dépendent de la machine : ce sont des **ordres de grandeur**.

---

## Étape 1 — Fibonacci

**L'expérience** (vérifiée) :

| n | appels | temps |
|---|---|---|
| 30 | 2 692 537 | 2 ms |
| 35 | 29 860 703 | 26 ms |
| 40 | 331 160 281 | 304 ms |

Chaque fois que `n` augmente de 5, le nombre d'appels est multiplié par environ **11** (le nombre d'or à la puissance 5) : la croissance est **exponentielle**. F(50) prendrait environ 40 secondes, F(92) des milliers d'années. La version dynamique fait 92 additions.

---

## Étape 2 — Le rendu de monnaie

`minCoins({1, 3, 4}, 6)` = **2** (3 + 3 ; le glouton donnerait 4 + 1 + 1). Les façons de faire 5 avec `{1, 2, 5}` : **4** (5 ; 2 + 2 + 1 ; 2 + 1 + 1 + 1 ; 1 + 1 + 1 + 1 + 1). Les 73 682 façons de faire 2 livres sont un résultat connu (le problème 31 du Projet Euler).

**Question — les sommes à l'extérieur :** on compterait les **suites ordonnées** de pièces, pas les combinaisons. Pour `{1, 2}` et 3, cela donne **3** (1 + 1 + 1, 1 + 2, 2 + 1) au lieu de 2 (vérifié). Avec les pièces à l'extérieur, une fois qu'on est passé aux pièces de 2, on ne revient jamais ajouter une pièce de 1 après : `2 + 1` n'est jamais construit, seulement `1 + 2`.

---

## Étape 3 — Deux mots

Les résultats (vérifiés) : LCS de `"ABCBDAB"` et `"BDCABA"` = 4, `"BCBA"` ; LCS de `"chien"` et `"niche"` = `"che"` ; distances : `chien`/`chine` = 2, `kitten`/`sitting` = 3, vide/`abc` = 3, `sunday`/`saturday` = 3.

---

## Étape 4 — Le sac à dos

Quatre objets (poids 1, 3, 4, 5 ; valeurs 1, 4, 5, 7) dans 7 kg : **9** (les objets de 3 et 4 kg). Remarque : un sac à dos **avec** réutilisation trouverait aussi 9 ici (3 + 3 + 1 : 4 + 4 + 1). C'est pour cela que le test de l'objet de 2 kg dans un sac de 4 kg est indispensable. Il manquait dans la première version des tests de référence, et le mutant 10 survivait.

---

## Étape 5 — Croissant, et la grille

`longestIncreasing({10, 9, 2, 5, 3, 7, 101, 18})` = 4 (par exemple 2, 3, 7, 18). La grille 18 × 18 : il faut 17 pas à droite et 17 en bas, dans n'importe quel ordre, soit C(34, 17) = **2 333 606 220** chemins (vérifié par le calcul et par le code).

**Question — la version « bête » :** comparer chaque nombre à tous les précédents coûte n²/2 comparaisons : pour un million de nombres, 500 milliards, soit plusieurs minutes. La version avec `tails` fait une dichotomie par nombre : 20 millions d'opérations, quelques dizaines de millisecondes.

---

## Étape 6 — Les mutants

Les 13 mutants sont tués par les tests de référence (vérifié).

---

## Expériences de fin de projet

1. Dans l'ordre croissant (vérifié), le sac à dos rend **6** pour un objet de 2 kg (valeur 3) dans un sac de 4 kg : il prend le même objet **deux fois**. `best[2]` vaut déjà 3 (l'objet pris) quand on calcule `best[4] = best[2] + 3`.
2. Sans `reverse()` (vérifié), `lcs` rend **`"ABCB"`** : les lettres de `"BCBA"`, dans l'ordre de la remontée, c'est-à-dire à l'envers.
