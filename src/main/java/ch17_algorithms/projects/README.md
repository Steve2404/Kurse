# Chapitre 17 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et **comment `Check` vérifie la vitesse**) est décrit dans `../PARCOURS.md`.

Chaque projet est une **famille d'algorithmes**, dans une application. Dans son dossier : `TODO.md` (l'énoncé, avec une leçon par étape), `INDICES.md`, `Check.java`, et `solution/` (avec `CORRIGE.md`), à n'ouvrir qu'à la fin.

**Tout le code et tous les tests, c'est toi qui les écris.** Les tests de référence de `Check` vérifient aussi la **vitesse** : un algorithme juste mais trop lent échoue.

| ☐ | Projet | Algorithmes | Ce qui est dur |
|---|---|---|---|
| ☐ | `p01_search` — chercher vite | complexité O, dichotomie, bornes, recherche avec une question, dichotomie sur la réponse | l'invariant, les débordements `(lo + hi) / 2` et `mid * mid` |
| ☐ | `p02_sorting` — le classement du marathon | insertion, fusion, rapide (partition en trois), comptage, tri stable générique | le pivot, les doublons, la stabilité |
| ☐ | `p03_windows` — capteurs et salles | deux pointeurs, fenêtre fixe et variable, intervalles, glouton, balayage | la gauche qui ne recule jamais, la fin exclue |
| ☐ | `p04_hashing` — le moteur de recherche | hachage, ta propre `HashMap`, cache LRU | collisions, facteur de charge, liste doublement chaînée |
| ☐ | `p05_stacks` — la calculatrice | piles, files, gare de triage, pile monotone, coût amorti | l'ordre des opérandes, l'associativité, l'analyse amortie |
| ☐ | `p06_backtracking` — planning et sudoku | puissance rapide, permutations, sous-ensembles, combinaisons, N reines, sudoku | copier, défaire, élaguer |
| ☐ | `p07_trees` — l'annuaire | arbre binaire de recherche, suppression, parcours, intervalles, ancêtre commun | le successeur, l'arbre dégénéré |
| ☐ | `p08_heaps` — les urgences | tas binaire, tri par tas, `PriorityQueue`, top k, fusion de k listes, médiane | descendre vers le plus petit, deux tas |
| ☐ | `p09_graphs` — le métro | BFS, DFS sans récursion, tri topologique, Dijkstra, union-find, Kruskal | marquer à l'entrée, les entrées périmées |
| ☐ | `p10_dynamic` — caisse et correcteur | Fibonacci, monnaie, LCS, Levenshtein, sac à dos, sous-suite croissante, grille | la case, la relation, l'ordre |
| ☐ | `p11_delivery` — **capstone** GPS du livreur | Dijkstra + Held-Karp (masques de bits) + glouton avec tas, test contre un oracle | assembler, reconnaître, prouver |
