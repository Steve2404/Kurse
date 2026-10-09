# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Search.java`](Search.java), et les tests de référence dans [`SearchTest.java`](SearchTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**, sur la solution et sur de petits programmes d'essai. Les temps mesurés dépendent de la machine : ce sont des **ordres de grandeur**.

---

## Étape 1 — La recherche linéaire

**Question — les comparaisons :** au pire, **n** comparaisons (la clé est absente, ou dans la dernière case) ; au mieux, **une seule** (la clé est dans la première case). La complexité se donne en général pour le **pire des cas** : O(n).

---

## Étape 2 — La dichotomie

**Question — pourquoi `- 1` :** sans le `- 1`, un point d'insertion de **0** donnerait `-0`, c'est-à-dire **0**, qu'on confondrait avec « trouvé à l'indice 0 ». Avec `-(0) - 1 = -1`, tout résultat négatif veut dire « absent », et l'appelant retrouve le point d'insertion avec `-(résultat) - 1`.

**Question — combien de tours :** chaque tour divise par deux le nombre de cases possibles. 2²⁰ = 1 048 576, donc **20 tours** au plus pour un million de cases.

---

## Étape 3 — Les bornes

Avec `{2, 4, 4, 4, 7, 9, 12}` : pour 4, `lowerBound` = 1, `upperBound` = 4, `count` = 3 ; pour 5 (absent), les deux valent 4 et `count` = 0 ; pour 1, les deux valent 0 ; pour 13, les deux valent 7 (la longueur). Vérifié par le test `lowerAndUpperBounds`.

**Question — la différence :** `lowerBound` avance (`lo = mid + 1`) quand `sorted[mid] < key` ; `upperBound`, quand `sorted[mid] <= key`. Avec `<=`, on saute aussi les cases **égales** à la clé : on s'arrête après le dernier doublon au lieu de s'arrêter sur le premier. C'est le mutant 3.

---

## Étape 4 — Chercher avec une question

**L'expérience** (vérifiée) : `(1_500_000_000 + 2_000_000_000) / 2` affiche **−397483648** : la somme (3,5 milliards) dépasse `Integer.MAX_VALUE` (2 147 483 647) et « fait le tour » vers les négatifs. `1_500_000_000 + (2_000_000_000 - 1_500_000_000) / 2` affiche **1750000000**, le bon milieu. Avec le premier calcul, `firstBad(Integer.MAX_VALUE, …)` tourne **sans fin** (c'est le mutant 5) : d'où le délai de 2 secondes dans le test.

Le nombre d'appels pour `n = Integer.MAX_VALUE` : 31 tours au plus (2³¹ ≈ 2,1 milliards), donc 31 appels ; le test accepte jusqu'à 32.

---

## Étape 5 — La racine carrée entière

**L'expérience** (vérifiée) : `3037000500L * 3037000500L` affiche **−9223372036709301616**. Le vrai résultat (9 223 372 037 000 250 000) dépasse `Long.MAX_VALUE` : il devient négatif, donc « plus petit que n ». Une comparaison `mid * mid <= n` se trompe alors, et la dichotomie part dans le mauvais sens (le mutant 6 ne s'arrête même plus). `mid <= n / mid` ne calcule jamais de nombre plus grand que `n`.

---

## Étape 6 — La dichotomie sur la réponse

Les colis 1 à 10 (somme 55) : en 1 jour, **55** ; en 2 jours, **28** (1 à 7 = 28, puis 8 + 9 + 10 = 27) ; en 5 jours, **15** (1-5, 6-7, 8, 9, 10) ; en 10 jours, **10** ; en 20 jours, **10** aussi (on ne peut pas descendre sous le plus gros colis).

**Question — la complexité :** la dichotomie porte sur l'intervalle [plus gros colis, S], soit au plus `S` valeurs : environ **log₂(S)** tours. Chaque tour vérifie une capacité en parcourant les `n` colis. Total : **O(n log S)**. Avec 100 000 colis de 500 (S = 50 millions), cela fait environ 26 tours de 100 000 : instantané (test `minCapacityIsFastOnBigInputs`).

---

## Étape 7 — Les mutants

Les 9 mutants sont tués par les tests de référence (vérifié). Les mutants 5 et 6 provoquent des boucles infinies. Avec les délais de 2 secondes des tests de référence, `Check` les tue en 2 secondes ; sans délai dans les tests, il attend ses 20 secondes de sécurité (vérifié : sur la première version des tests, sans délai, la vérification complète durait 44 secondes au lieu de 9).

---

## Expériences de fin de projet

1. **La forme de la croissance** (vérifiée sur la machine de l'auteur, après un tour d'échauffement) :

   | n | 1000 × `linear` | 1000 × `binary` |
   |---|---|---|
   | 100 000 | 5 ms | 135 µs |
   | 1 000 000 | 65 ms | 113 µs |
   | 10 000 000 | 1461 ms | 441 µs |

   Quand `n` est multiplié par 10, le temps de `linear` est multiplié par **10 à 20** (O(n), plus les effets de la mémoire cache sur les gros tableaux) ; celui de `binary` reste presque **le même** (O(log n) : 17, 20, puis 24 tours). Les microsecondes de `binary` sont trop petites pour être mesurées précisément : seul l'ordre de grandeur compte.
2. Avec `linear` dans le test de vitesse, le test échoue avec `execution timed out after 3000 ms` (vérifié avec un million de recherches linéaires dans un million de cases).
