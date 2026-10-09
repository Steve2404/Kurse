# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Windows.java`](Windows.java) et [`Interval.java`](Interval.java), et les tests de référence dans [`WindowsTest.java`](WindowsTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**. Les temps dépendent de la machine : ce sont des **ordres de grandeur**.

---

## Étape 1 — Deux pointeurs aux deux bouts

**Le débordement :** `(Integer.MAX_VALUE - 1) + Integer.MAX_VALUE` vaut 2³² − 3 ; en `int`, cela fait le tour et donne exactement **−3**. Sans le `long`, `pairWithSum` trouverait une « paire » de somme −3 (c'est le mutant 1, tué par `pairWithSumDoesNotOverflow`).

**Question — pourquoi trié :** tout le raisonnement repose sur « le livre de droite est le **plus épais** qui reste » et « celui de gauche, le **plus fin** ». Si la somme est trop petite avec le plus épais, elle l'est avec tous les autres : on peut éliminer le livre de gauche **sans l'essayer** avec les autres. Sans tri, cette élimination serait fausse : il faudrait essayer toutes les paires.

---

## Étape 2 — Deux pointeurs dans le même sens

**Le code :** `removeDuplicates` ; avec `{1, 1, 2, 3, 3, 3, 7}`, il rend 4 et le tableau commence par `{1, 2, 3, 7}`.

---

## Étape 3 — La fenêtre de taille fixe

Avec `{2, 9, -1, 5, 8, -6, 3}` : `k = 1` → **9** ; `k = 2` → **13** (5 + 8) ; `k = 3` → **13** (9 − 1 + 5, ou −1 + 5 + 8 = 12, et 5 + 8 − 6 = 7 : le maximum est 13) ; `k = 4` → **21** (9 − 1 + 5 + 8) ; `k = 7` → **20** (tout le tableau). (En écrivant ce projet, le test de référence attendait d'abord 15 pour `k = 3`, calculé trop vite : le code avait raison.)

**L'expérience** (vérifiée, `k = n / 2`) :

| n | version naïve | fenêtre glissante |
|---|---|---|
| 20 000 | 22 ms | 0 ms |
| 40 000 | 87 ms | 0 ms |
| 80 000 | 345 ms | 0 ms |

La version naïve fait (n − k + 1) × k ≈ n²/4 additions : ×4 quand `n` double. La fenêtre glissante en fait n, trop peu pour être mesuré. Les deux donnent le même résultat.

---

## Étape 4 — La fenêtre de taille variable

**Question — `"abba"` sans `Math.max` :** à `right = 2` (le 2e `b`), la gauche saute à 2 (après le 1er `b`) : fenêtre `"b"`. À `right = 3` (le 2e `a`), le 1er `a` était à l'indice 0 : sans `Math.max`, la gauche **recule** à 1, et la fenêtre devient `"bba"`, longueur **3**, qui contient deux `b`. Avec `Math.max(2, 0 + 1)`, la gauche reste à 2 : fenêtre `"ba"`, longueur **2**, la bonne réponse. C'est le mutant 6.

Avec `{2, 3, 1, 2, 4, 3}` : cible 7 → **2** (`4 + 3`) ; 4 → **1** (`4`) ; 15 → **6** (tout le tableau) ; 16 → **0** ; 11 → **5** (`2 + 3 + 1 + 2 + 4 = 12`, ou `3 + 1 + 2 + 4 + 3 = 13`).

---

## Étape 5 — Les intervalles

**Le code :** `merge`, `maxNonOverlapping` et `minRooms`.

**Question — trier par fin :** garder la réunion qui finit le plus tôt laisse **le plus de temps libre** après elle. Quelle que soit la meilleure solution, on peut remplacer sa première réunion par celle-ci sans rien gâcher (c'est l'argument d'**échange** qui prouve qu'un glouton est juste). Contre-exemple pour le tri par **début** (vérifié) : `[0, 10[`, `[1, 2[`, `[3, 4[`. Trié par début, le glouton prend `[0, 10[` (il commence en premier), et plus rien ne rentre : **1** réunion. Trié par fin, il prend `[1, 2[` puis `[3, 4[` : **2** réunions.

---

## Étape 6 — Les mutants

Les 12 mutants sont tués par les tests de référence (vérifié). La première version du mutant 3 (comparer à `sorted[i - 1]` au lieu de `sorted[kept - 1]`) était **équivalente** : la case `i - 1` contient toujours, à ce moment-là, la dernière valeur gardée. Il a été remplacé.

---

## Expériences de fin de projet

1. Avec `<` (vérifié, c'est le mutant 11), les réunions bout à bout `[9, 10[`, `[10, 11[`, `[11, 12[` demandent **2** salles au lieu d'**une** : la salle libérée à 10 h n'est pas reprise par la réunion de 10 h.
2. Avec un `if`, la gauche n'avance que d'un cran par tour, et elle prend du retard : pour la cible 7, la fenêtre `4 + 3` (longueur 2) n'est jamais examinée, et la fonction rend **4** au lieu de 2 (c'est le mutant 7).
