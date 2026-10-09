# Projet 9 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`Graph.java`](Graph.java) et [`UnionFind.java`](UnionFind.java), les tests de référence dans [`GraphTest.java`](GraphTest.java) et [`UnionFindTest.java`](UnionFindTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**. Les temps dépendent de la machine : ce sont des **ordres de grandeur**.

---

## Étape 1 — Ranger un graphe

**Le code :** le constructeur, `addEdge` et `addUndirected`.

---

## Étape 2 — Le parcours en largeur

Sur le métro (vérifié) : `bfsOrder(0)` = `[0, 1, 4, 2, 5, 3]` ; `hops(0)` = `[0, 1, 2, 2, 1, 2, -1, -1]` ; `fewestStops(0, 3)` = `[0, 4, 3]` ; `fewestStops(2, 5)` = `[2, 1, 5]`.

**Question — le moins d'arêtes, pas le plus rapide :** le BFS compte les **arêtes**, sans regarder leur poids : une arête de 10 minutes compte autant qu'une de 1 minute. Dans le petit graphe des tests (`0 → 2` en 10 minutes ; `0 → 1 → 2` en 1 + 1), le BFS choisit `[0, 2]` (une seule arête), mais le trajet le plus rapide est `0 → 1 → 2`, en 2 minutes. Pour des poids, il faut Dijkstra.

---

## Étape 3 — Les composantes

**L'expérience** (vérifiée) : le DFS récursif sur une ligne d'un million de sommets plante avec **`StackOverflowError`**. La version avec une pile explicite traite la même ligne sans problème (test de vitesse) : la pile `ArrayDeque` vit dans le tas de la JVM, bien plus grand que la pile d'appels.

---

## Étape 4 — Le tri topologique

Pour les 5 cours (vérifié) : `[0, 1, 2, 4, 3]`. Au départ, 0 et 1 sont prêts : on prend 0. Le cours 2 attend encore 1 ; on prend 1, ce qui libère 2 et 4 ; on prend 2 (le plus petit), puis 4, puis 3.

---

## Étape 5 — Dijkstra

Depuis 0, sur le métro (vérifié) : 0 → **0**, 1 → **2**, 2 → **4**, 3 → **6** (par 1, 2 : 2 + 2 + 2, ou par 4 : 5 + 1), 4 → **5**, 5 → **7** (par 3 : 6 + 1, et non 2 + 7 = 9 par la ligne directe 1-5), 6 et 7 → inaccessibles.

---

## Étape 6 — Union-find et Kruskal

Le petit Kruskal (vérifié) : les arêtes de coût 1 (0-1), 2 (1-2), puis 3 (0-2) est **rejetée** (0 et 2 sont déjà reliés), puis 4 (2-3) : total **7**.

---

## Étape 7 — Les mutants

Les 14 mutants sont tués par les tests de référence (vérifié). En écrivant ce projet, deux mutants ont **survécu** et ont été remplacés :
- ne pas ignorer les entrées périmées du tas ;
- remplacer le tas par une simple file.

Pourquoi ils survivaient : avec cette version de Dijkstra (sans tableau de sommets « fixés », qui relâche à nouveau un sommet chaque fois qu'il s'améliore), le résultat reste **juste** quel que soit l'ordre de sortie : c'est seulement la **vitesse** qui change. Sur la grille de test, la différence était trop petite pour dépasser le délai. Une leçon : un algorithme peut être juste et lent, et seuls des tests de vitesse bien choisis le voient. Un mutant de la toute première version, qui faisait tourner le BFS sans fin, épuisait la mémoire (`OutOfMemoryError`) : remplacé aussi, car il mettait toute la vérification en danger.

---

## Expériences de fin de projet

1. Dijkstra **sans tas** (vérifié) : **93 ms** pour 10 000 sommets, **1307 ms** pour 40 000 (×14 pour 4 fois plus de sommets : O(V²)). Avec le tas, la grille de 90 000 sommets passe dans le test de vitesse avec le reste, en bien moins d'une seconde : O(E log V).
2. Pour le métro : `fewestStops(0, 3)` = `[0, 4, 3]` (2 arrêts, 5 + 1 = **6** minutes) ; `dijkstra(0)[3]` vaut aussi **6**. Ici, les deux trajets prennent le même temps (l'autre trajet, `0 → 1 → 2 → 3`, prend aussi 6 minutes). En général, ce n'est pas vrai : voir le petit graphe de l'étape 2.
