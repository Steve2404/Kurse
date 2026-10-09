# Projet 9 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Ranger un graphe

<details><summary>Indice 1</summary>

`private record Edge(int to, int weight) {}` et `private final List<List<Edge>> adjacency = new ArrayList<>();`, rempli de `n` listes vides dans le constructeur.

</details>

<details><summary>Indice 2</summary>

`addUndirected(a, b, w)` appelle simplement `addEdge(a, b, w)` puis `addEdge(b, a, w)` : le contrôle du poids est ainsi écrit une seule fois.

</details>

---

## Étape 2 — Le parcours en largeur

<details><summary>Indice 1</summary>

`boolean[] seen`, une `ArrayDeque<Integer>` comme file (`add` à la fin, `poll` au début). Marque `seen[start]` avant la boucle, et chaque voisin **quand tu l'ajoutes**.

</details>

<details><summary>Indice 2</summary>

Pour le chemin : un tableau `previous` rempli d'une valeur « jamais vu » (−2), `previous[from] = -1`. À la fin, remonte `for (int v = to; v != -1; v = previous[v])`, puis `Collections.reverse`.

</details>

---

## Étape 3 — Les composantes

<details><summary>Indice 1</summary>

Une boucle sur tous les sommets ; pour chaque sommet pas encore vu : `composantes++`, puis un parcours depuis lui qui marque tous les sommets atteints.

</details>

<details><summary>Indice 2</summary>

Le parcours avec une pile : `stack.push(s)` ; `while (!stack.isEmpty())` : `pop`, puis `push` des voisins pas encore vus (marqués au moment du `push`).

</details>

---

## Étape 4 — Le tri topologique

<details><summary>Indice 1</summary>

Un tableau `inDegree` : pour chaque arête `u → v`, `inDegree[v]++`. Mets dans une `PriorityQueue<Integer>` tous les sommets à 0.

</details>

<details><summary>Indice 2</summary>

Après avoir retiré `u`, pour chaque arête `u → v` : `if (--inDegree[v] == 0) ready.add(v);`. À la fin, si la liste n'a pas tous les sommets, il y a un cycle. `hasCycle` peut attraper l'exception de `topologicalOrder`.

</details>

---

## Étape 5 — Dijkstra

<details><summary>Indice 1</summary>

`dist` rempli de `Long.MAX_VALUE`, `dist[start] = 0`, et un tas `new PriorityQueue<long[]>((x, y) -> Long.compare(x[1], y[1]))` qui contient `{start, 0}`.

</details>

<details><summary>Indice 2</summary>

À chaque sortie `{u, d}` : si `d > dist[u]`, l'entrée est périmée, `continue`. Sinon, pour chaque arête : `candidate = dist[u] + poids` ; s'il est plus petit que `dist[v]`, mets-le à jour et ajoute `{v, candidate}` au tas.

</details>

---

## Étape 6 — Union-find et Kruskal

<details><summary>Indice 1</summary>

`parent[i] = i` et `size[i] = 1` au départ. `find` : remonte jusqu'à la racine, puis refais le chemin en rattachant chaque sommet à la racine.

</details>

<details><summary>Indice 2</summary>

Kruskal : `int[][] sorted = edges.clone();` puis `Arrays.sort(sorted, Comparator.comparingInt(e -> e[2]))`. Pour chaque arête, `if (uf.union(a, b)) cost += coût;`. À la fin, plus d'un groupe : non connexe.

</details>

---

## Étape 7 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quel ordre exact, quel sommet de départ, quel chemin à retourner, quel petit graphe piège (le plus rapide n'est pas le plus court en arêtes), quelle entrée à ne pas modifier n'est pas testé ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Le BFS utilise une pile au lieu d'une file.
2. `hops` donne 1 (au lieu de 0) au sommet de départ, et donc +1 partout.
3. Le chemin n'est pas retourné (il va de l'arrivée au départ).
4. Les composantes comptent toujours 1.
5. Le tri topologique prend le plus **grand** numéro prêt.
6. Un cycle d'un seul sommet n'est pas détecté.
7. Dijkstra ne met à jour un sommet que la première fois qu'il le voit.
8. Dijkstra oublie la durée déjà parcourue (il ne garde que la dernière arête).
9. Dijkstra ignore aussi les entrées à jour : rien n'est relâché au-delà du départ.
10. Un poids négatif est accepté.
11. Une union inutile diminue quand même le nombre de groupes.
12. Kruskal trie les arêtes par sommet au lieu de coût.
13. Un graphe en deux morceaux est accepté par Kruskal.
14. Kruskal trie le tableau reçu (il le modifie).

</details>
