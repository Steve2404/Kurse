# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Backtracking.java`](Backtracking.java), et les tests de référence dans [`BacktrackingTest.java`](BacktrackingTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**. Les temps dépendent de la machine : ce sont des **ordres de grandeur**.

---

## Étape 1 — La puissance rapide

**L'expérience** (vérifiée) : la version naïve rend **1** pour l'exposant 5 000, mais plante avec **`StackOverflowError`** pour 1 000 000 : un million d'appels imbriqués dépassent la pile d'appels. La version rapide n'en fait que 30 pour un milliard (log₂(10⁹) ≈ 30). Le test « 1 à la puissance un milliard » est justement là pour attraper une puissance naïve (mutant 1).

---

## Étape 2 — Les permutations

**L'expérience** (vérifiée) : avec `current` au lieu d'une copie, le résultat vaut **`[[], [], [], [], [], []]`** : six fois **la même** liste, que les « défaire » ont vidée à la fin.

**Question — combien :** n! permutations (n choix pour la 1re place, n−1 pour la 2e…) : 6 pour 3, 5040 pour 7, 362 880 pour 9. Chaque permutation coûte n pour être copiée : **O(n × n!)**. Aucun algorithme ne peut faire mieux, puisqu'il faut toutes les écrire. (Vérifié : les 362 880 permutations de 9 éléments prennent environ 40 ms.)

---

## Étape 3 — Sous-ensembles, combinaisons, parenthèses

Les résultats exacts (vérifiés) : `combinationSum({2, 3, 6, 7}, 7)` = `[[2, 2, 3], [7]]` ; `combinationSum({5, 3, 2}, 8)` = `[[2, 2, 2, 2], [2, 3, 3], [3, 5]]` ; `parentheses(3)` = `[((())), (()()), (())(), ()(()), ()()()]` ; `parentheses(4)` en compte 14 ; `parentheses(0)` = `[""]` (une seule façon de ne rien écrire).

**Question — `i` et pas `i + 1` :** passer `i` permet de **réutiliser** le même candidat (`[2, 2, 3]`) ; `i + 1` l'interdirait (mutant 6). Passer `0` permettrait de revenir à un candidat plus petit, et produirait `[2, 2, 3]`, `[2, 3, 2]`, `[3, 2, 2]` : des doublons (mutant 7).

---

## Étape 4 — Les N reines

Les nombres de solutions (vérifiés) : 4 → 2, 6 → 4, 8 → **92**, 10 → 724, 12 → 14 200 (en environ 40 ms), 13 → 73 712.

**L'expérience** (vérifiée) : sans les diagonales `/`, on trouve **2113** « solutions » pour 8 reines au lieu de 92 : des placements où deux reines se menacent sur une diagonale `/`.

---

## Étape 5 — Le sudoku

La grille d'Arto Inkala est résolue en quelques millisecondes (vérifié : 3 ms). Le retour arrière avec élagage est bien plus rapide que sa réputation : chaque chiffre impossible est refusé tout de suite.

**Question — la grille presque vide :** les deux autres grilles fausses n'ont **pas de solution** de toute façon : sans la vérification de départ, le retour arrière échoue quand même, et rend `false` par hasard. La grille presque vide, elle, se **complète** très bien si l'on ne regarde pas les chiffres de départ (le retour arrière ne vérifie que les chiffres qu'**il** pose). Elle est le seul test où l'oubli de la vérification change le résultat. C'est le mutant 13, qui survivait à la première version des tests de référence.

---

## Étape 6 — Les mutants

Les 13 mutants sont tués par les tests de référence (vérifié).

---

## Expériences de fin de projet

1. Sans la remise à 0 (vérifié), **aucune** des deux grilles ne se résout : `solveSudoku` rend `false` (`expected: <true> but was: <false>`). En revenant en arrière, la case garde son dernier chiffre essayé (9, ou un autre) : les cases précédentes croient ce chiffre pris, et refusent des valeurs pourtant possibles.
2. (Raisonnement, à vérifier dans ton débogueur.) Quand `"(())"` est ajoutée, `parens` est appelée **5 fois** l'une dans l'autre : une fois au départ (chaîne vide), puis une fois par caractère ajouté. Sous elles, tu vois `parentheses`, puis ta méthode de test.
