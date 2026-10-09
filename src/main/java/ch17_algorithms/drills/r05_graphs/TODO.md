# Drill de rappel 5 — Les gabarits de graphes

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p09.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée **`public final class Recall05`** dans le paquet `ch17_algorithms.drills.r05_graphs`, avec les méthodes **`public static`** ci-dessous, **exactement** avec ces signatures.
- Chaque graphe arrive comme une **liste d'arêtes** : `edges[i] = {a, b}` ou `{a, b, poids}` ; les sommets vont de 0 à `n - 1`. À toi de construire les listes d'adjacence (une petite méthode privée commune).
- Tu n'écris pas de tests : les tests de référence (dans `solution/`) vérifient ton code.

**Les notions de ce drill ont été apprises dans :** projet 9 (étapes 2 et 4 à 6). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : crée `Recall05`, écris les méthodes dans l'ordre (une valeur bidon pour un défi pas fini), `// D03 : ✗` après 3 minutes bloqué, lance `Check.java`, puis la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `int[] bfsHops(int n, int[][] edges, int start)` : graphe **non orienté** ; le nombre minimal d'arêtes depuis `start`, −1 si inaccessible (une ligne d'un million de sommets en moins de 3 secondes).
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** `int components(int n, int[][] edges)` : graphe non orienté ; le nombre de composantes connexes, avec **union-find**.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `List<Integer> topologicalOrder(int n, int[][] edges)` : graphe **orienté** ; l'algorithme de Kahn, plus petit sommet prêt d'abord ; une liste **vide** s'il y a un cycle.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `long[] dijkstra(int n, int[][] edges, int start)` : graphe orienté pondéré ; les durées minimales, `Long.MAX_VALUE` si inaccessible.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `long minimumSpanningTree(int n, int[][] edges)` : graphe non orienté pondéré ; le coût de l'arbre couvrant minimal (Kruskal), ou −1 s'il n'est pas connexe ; sans modifier le tableau reçu.
  → `d05 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **Adjacence** : `List<List<int[]>>` de `{voisin, poids}` ; non orienté = ajouter dans les deux listes.
- **BFS** : `dist` rempli de −1, `dist[start] = 0`, une **file** ; un voisin à −1 reçoit `dist[u] + 1` et entre dans la file (marqué à l'entrée).
- **Union-find** : `parent[i] = i` ; `find` remonte (avec compression : `parent[x] = parent[parent[x]]`) ; une union réussie diminue le nombre de groupes.
- **Kahn** : degrés entrants ; une `PriorityQueue` des sommets à 0 ; retirer, écrire, décrémenter les voisins ; incomplet = cycle.
- **Dijkstra** : `dist` à `Long.MAX_VALUE`, un tas de `{sommet, durée}` ; ignorer les entrées périmées (`durée > dist[u]`) ; relâcher : `dist[u] + poids < dist[v]` → mettre à jour et ajouter.
- **Kruskal** : copier, trier par poids, union-find ; garder une arête qui relie deux groupes ; connexe si on a gardé `n - 1` arêtes.

</details>
