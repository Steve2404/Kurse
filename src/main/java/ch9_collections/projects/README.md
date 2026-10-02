# Chapitre 9 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et la règle du crescendo) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Data.java` : les données ;
- `Check.java` : le correcteur ;
- `solution/` : à n'ouvrir qu'à la fin.

**Tous les types, c'est toi qui les crées.**

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_inventory` — inventaire | `List`, `ArrayList`, `LinkedList`, `removeIf`, `replaceAll`, `subList`, itérateurs, `Collections`, listes figées | `Inventory` | le piège `remove(int)`, retrait pendant un parcours, analyse ABC |
| ☐ | `p02_words` — analyse de textes | `HashMap`, `LinkedHashMap`, `TreeMap`, `merge`, `compute…`, les trois `Set`, `retainAll` | `Words` | index inversé, requêtes booléennes, top-k avec un tas, anagrammes |
| ☐ | `p03_graphs` — graphes | `Queue`, `Deque` (pile et file), `PriorityQueue` sur un record | `Graphs` | tri topologique de Kahn, DFS, BFS, Dijkstra, composantes, max sur fenêtre glissante |
| ☐ | `p04_generics` — laboratoire des génériques | record, classe, interface et méthodes génériques, bornes, jokers, effacement | `GenericsLab` | tas générique, tri fusion générique, cache LRU (`LinkedHashMap`), PECS |
| ☐ | `p05_ranking` — classements | `Comparable`, `Comparator` composé, `nullsLast`, `NavigableMap`, `NavigableSet` | `Ranking` | rangs, piège du `TreeSet`, fusion d'intervalles, planning glouton, médiane à deux tas |
| ☐ | `p06_editor` — éditeur de texte | `Deque` (undo/redo, récents), `NavigableSet.subSet`, pile de caractères | `Editor` | annuler/rétablir, autocomplétion, parenthèses, correcteur orthographique |
| ☐ | `p07_social` — **capstone** réseau social | tout le chapitre, Union-Find générique, `Set.copyOf` | `Social` | suggestions d'amis, BFS, fusion de k fils triés avec un tas, tendances |
