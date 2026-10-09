# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Sorting.java`](Sorting.java) et [`Runner.java`](Runner.java), et les tests de référence dans [`SortingTest.java`](SortingTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**. Les temps dépendent de la machine : ce sont des **ordres de grandeur**.

---

## Étape 1 — Le tri par insertion

**Le code :** `insertionSort`, et le test `sortsEveryTrickyArray` avec ses méthodes `tricky()` et `sorters()`.

**Question — `{4, 3, 2, 1}` :** le 3 recule d'une case (1 décalage), le 2 de deux (2), le 1 de trois (3) : **6 décalages**, soit n(n−1)/2 pour n = 4. C'est le pire des cas.

---

## Étape 2 — Le tri fusion

**Le code :** les deux méthodes `mergeSort` sur les tableaux.

**L'expérience** (vérifiée, après un tour d'échauffement) :

| n (au hasard) | insertion | fusion |
|---|---|---|
| 10 000 | 3 ms | 0 ms |
| 20 000 | 15 ms | 1 ms |
| 40 000 | 64 ms | 3 ms |
| 80 000 | 263 ms | 5 ms |

Quand `n` double, le temps du tri par insertion est multiplié par **environ 4** : c'est la signature de **O(n²)** (2² = 4). Celui du tri fusion est à peine plus que doublé : **O(n log n)**. À un million d'éléments, l'insertion prendrait environ 40 secondes (263 ms × 12,5²), et le tri fusion quelques dizaines de millisecondes.

---

## Étape 3 — Le tri rapide

**Le code :** les deux méthodes `quickSort` et `swap`.

**L'expérience** (vérifiée) : la version naïve, sur un tableau **déjà trié**, prend 8 ms pour 5 000 nombres, 18 ms pour 10 000 et 74 ms pour 20 000 (environ ×4 quand n double : O(n²)), alors qu'elle trie les mêmes tailles **au hasard** en 0 ou 1 ms. À **100 000** nombres triés, elle plante : **`StackOverflowError`**. Chaque appel ne retire qu'un élément, donc la récursion descend à 100 000 niveaux, et la pile d'appels de Java déborde (quelques milliers à quelques dizaines de milliers de niveaux selon la machine).

Remarque : avec la partition en **trois** de la solution, prendre le premier élément comme pivot ne suffit **pas** à la rendre quadratique sur un tableau trié, car les échanges de la partition mélangent le tableau au passage (vérifié en écrivant ce projet : ce mutant-là a été retiré, il ne ralentissait pas assez). Le pivot au hasard reste la bonne habitude : il protège contre **toutes** les entrées pièges.

---

## Étape 4 — Le tri par comptage

**Le code :** `countingSort`.

**Question — des `int` quelconques :** le tableau `counts` aurait une case par valeur possible, de 0 à `max`. Pour des `int` quelconques, `max` peut valoir 2 milliards : 8 Go de compteurs pour trier dix nombres, et des négatifs impossibles à ranger. Le tri par comptage ne vaut que pour des valeurs **petites** et **entières** (des notes, des âges, des codes de 0 à 100).

---

## Étape 5 — La stabilité

**Le code :** `Runner` et le `mergeSort` générique ; le test `genericMergeSortIsStable` attend `[Eve, Bob, Dan, Ana, Cid]`.

**Question — `<=` et `<` :** à égalité, `<=` prend l'élément de la moitié **gauche**, qui était **avant** dans la liste de départ : l'ordre d'origine est gardé. Avec `<`, à égalité, on prend celui de **droite** : il passe devant un élément égal qui était avant lui, et l'ordre de départ est perdu (c'est le mutant 8 : Dan passerait avant Bob).

---

## Étape 6 — Les mutants

Les 10 mutants sont tués par les tests de référence (vérifié). Deux mutants de la première version de `Check` ont été retirés, car **équivalents** :
- le pivot pris au premier élément (voir l'étape 3) ;
- rendre `list` au lieu d'une copie pour une liste de moins de 2 éléments : comme on ne modifie jamais ces petites listes, le résultat ne change pas.

---

## Expériences de fin de projet

1. Avec les égaux envoyés à droite (vérifié), le tri rapide plante sur `{4, 4, 4, 4}` avec une **`StackOverflowError`** : tous les éléments sont égaux au pivot et partent à droite, et l'appel récursif reçoit **le même** morceau, indéfiniment. Les tests d'un million de nombres échouent aussi (`StackOverflowError`, ou `execution timed out after 3000 ms` sur les doublons).
2. Avec `< 0`, le test de stabilité échoue (mutant 8) : `Dan` passe devant `Bob`, et `Cid` devant `Ana`.
