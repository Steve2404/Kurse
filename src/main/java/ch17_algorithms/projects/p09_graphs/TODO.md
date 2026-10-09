# Projet 9 — Le métro de la ville (les graphes)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 17) :**
- **un graphe** : des sommets et des arêtes ; orienté ou non, pondéré ou non ; les **listes d'adjacence** ;
- **le parcours en largeur** (*BFS*, une file) : l'ordre de visite, le plus petit nombre d'arêtes, le chemin lui-même ;
- **le parcours en profondeur** (*DFS*, une pile) : les **composantes connexes**, sans récursion ;
- **le tri topologique** (Kahn) et la **détection de cycle** ;
- **Dijkstra** : le plus court chemin pondéré, avec un tas ;
- **union-find** et l'**arbre couvrant minimal** (Kruskal).

**Ce que TU crées :** dans `ch17_algorithms.projects.p09_graphs` :
- **`Graph`** et **`UnionFind`** (signatures imposées) ;
- tes tests : **`GraphTest`** et **`UnionFindTest`**.

**Règle du crescendo :** tout Java 17, JUnit et Mockito ; `ArrayDeque` et `PriorityQueue` sont les outils de ce projet. Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : un réseau d'amis, et des recettes de cuisine.

> **🧰 Tes outils pour ce projet**
>
> - **Dessine le graphe** de tes tests sur papier, avec le numéro de chaque sommet. Les tests de référence utilisent un métro **non orienté** de 8 stations (temps en minutes), avec ces arêtes, **dans cet ordre** : 0-1 (2), 1-2 (2), 2-3 (2), 0-4 (5), 4-3 (1), 1-5 (7), 5-3 (1), 6-7 (1). Les stations 6 et 7 forment une ligne isolée. Dessine-le toi-même : c'est déjà un bon exercice.

---

## Tableau de bord

### ☐ Étape 1 — Ranger un graphe

**📖 La leçon : qui est relié à qui.** Un réseau d'amis : chaque personne est un **sommet**, chaque amitié une **arête**. Pour chaque personne, on garde la **liste de ses amis** : ce sont les **listes d'adjacence**. Une matrice (une case par paire de personnes) prendrait n² cases, presque toutes vides ; les listes n'en prennent que V + E (sommets + arêtes).

Une arête peut être **orientée** (« A suit B » n'implique pas « B suit A ») ou non (une ligne de métro va dans les deux sens : deux arêtes orientées). Elle peut avoir un **poids** (des minutes, des kilomètres).

**👉 À toi :** **`public final class Graph`** : les sommets sont numérotés de 0 à `n - 1`.
- **`public Graph(int vertices)`** et **`public int size()`** ;
- **`public void addEdge(int from, int to, int weight)`** : une arête orientée ; un poids négatif lance `IllegalArgumentException("poids negatif : " + weight)` ;
- **`public void addUndirected(int a, int b, int weight)`** : deux arêtes, aller et retour.

Range chaque arête dans un petit `record` privé (destination, poids), et les arêtes de chaque sommet dans une liste, **dans l'ordre d'ajout**.

### ☐ Étape 2 — Le parcours en largeur

**📖 La leçon : les cercles d'amis.** Pour trouver à combien de « poignées de main » quelqu'un se trouve de toi : d'abord tes amis (distance 1), puis leurs amis pas encore vus (distance 2), et ainsi de suite. C'est le **parcours en largeur** : une **file** (premier entré, premier sorti). On sort un sommet, et on met à la fin de la file ses voisins **pas encore vus**.

**Piège :** on marque un sommet « vu » **au moment où on l'ajoute** à la file. Si on attendait de le sortir, il pourrait être ajouté plusieurs fois.

Le BFS donne, en prime, le **plus petit nombre d'arêtes** depuis le départ : chaque sommet découvert depuis `u` est à `distance(u) + 1`. Pour retrouver le **chemin** lui-même, on retient pour chaque sommet **d'où l'on venait** (`previous`), puis on remonte depuis l'arrivée, et on retourne la liste.

**👉 À toi :**
- **`public List<Integer> bfsOrder(int start)`** : l'ordre de visite (les voisins dans l'ordre d'ajout des arêtes) ;
- **`public int[] hops(int start)`** : le nombre minimal d'arêtes vers chaque sommet, −1 s'il est inaccessible ;
- **`public List<Integer> fewestStops(int from, int to)`** : un chemin avec le moins d'arêtes possible (de `from` à `to`, extrémités comprises), ou une liste vide s'il n'y en a pas.

**Tes tests :** sur le métro dessiné : l'ordre de visite depuis 0 et depuis 6 ; les distances depuis 0 ; les chemins de 0 à 3, de 0 à 0, de 0 à 7 (aucun), de 2 à 5.

**❓ Question :** pourquoi le BFS trouve-t-il le plus petit nombre d'arêtes, mais pas forcément le trajet le plus **rapide** en minutes ?

### ☐ Étape 3 — Le parcours en profondeur : les composantes

**📖 La leçon : explorer jusqu'au bout.** Le parcours en **profondeur** suit un chemin le plus loin possible avant de revenir en arrière : c'est le retour arrière du projet 6. On l'écrit souvent récursivement, mais sur un graphe d'un million de sommets en ligne, la récursion descendrait un million de fois : la pile d'appels déborde. On remplace la récursion par une **pile explicite** (`ArrayDeque` avec `push` et `pop`).

Une **composante connexe** est un groupe de sommets reliés entre eux. Pour les compter : pour chaque sommet pas encore vu, on lance un parcours (qui marque toute sa composante), et on compte un groupe de plus.

**👉 À toi :** **`public int countComponents()`** (le graphe est supposé non orienté), avec une pile explicite.

**Tes tests :** le métro (2 composantes) ; un graphe de 5 sommets sans arête (5).

**🧪 Expérience :** dans `Mesure`, écris un DFS **récursif** et lance-le sur une ligne d'un million de sommets. Que se passe-t-il ?

### ☐ Étape 4 — L'ordre des recettes : le tri topologique

**📖 La leçon : ce qui doit venir avant.** Dans une recette, on ne peut pas monter la mayonnaise avant d'avoir séparé les œufs. Les étapes forment un graphe **orienté** : une arête `a → b` veut dire « `a` avant `b` ». Un **tri topologique** donne un ordre qui respecte toutes les flèches. L'algorithme de **Kahn** :
1. compter, pour chaque sommet, ses flèches **entrantes** ;
2. les sommets à 0 flèche entrante sont **prêts** ;
3. retirer un sommet prêt, l'écrire, et retirer ses flèches sortantes : des voisins deviennent prêts.

S'il reste des sommets qui ne deviennent **jamais** prêts, ils s'attendent les uns les autres : c'est un **cycle**, et aucun ordre n'existe.

**👉 À toi :**
- **`public List<Integer> topologicalOrder()`** : quand plusieurs sommets sont prêts, prends le **plus petit numéro** (une `PriorityQueue`) ; s'il y a un cycle : `IllegalStateException("cycle")` ;
- **`public boolean hasCycle()`**.

**Tes tests :** un graphe de 5 cours avec prérequis (`0 → 2`, `1 → 2`, `2 → 3`, `1 → 4`, `4 → 3`) : l'ordre exact, pas de cycle ; puis on ajoute `3 → 1` : l'exception et le cycle ; une boucle sur un seul sommet.

### ☐ Étape 5 — Le plus rapide : Dijkstra

**📖 La leçon : fixer le plus proche.** Pour les trajets les plus **rapides** (des arêtes de durées différentes), Edsger Dijkstra : on garde la meilleure durée connue vers chaque sommet. À chaque tour, on prend le sommet **non fixé le plus proche** (un **tas**, projet 8) : sa durée ne peut plus baisser, car tout autre chemin passe par un sommet plus lointain. On **relâche** alors ses arêtes : si passer par lui améliore un voisin, on note la nouvelle durée et on remet le voisin dans le tas.

Un sommet peut se retrouver plusieurs fois dans le tas (avec d'anciennes durées) : à la sortie, on ignore une entrée **périmée** (plus grande que la durée connue). Et Dijkstra ne marche **pas** avec des poids négatifs : un sommet « fixé » pourrait encore s'améliorer.

**👉 À toi :** **`public long[] dijkstra(int start)`** : la durée minimale vers chaque sommet, `Long.MAX_VALUE` s'il est inaccessible. Dans le tas, des `long[]` `{sommet, durée}`.

**Tes tests :** les durées depuis 0 sur le métro (calcule-les à la main) ; un petit graphe où le chemin le plus rapide a **plus** d'arêtes que celui du BFS ; un poids négatif refusé ; un test de vitesse : une ligne d'un million de sommets (composantes, distances, chemin) et une grille de 300 × 300 avec Dijkstra, en moins de 4 secondes.

### ☐ Étape 6 — Union-find et Kruskal

**📖 La leçon : des groupes qui fusionnent.** Union-find répond très vite à deux questions : « ces deux-là sont-ils dans le même groupe ? » (`find`) et « fusionne leurs groupes » (`union`). Chaque groupe est un petit arbre, dont la **racine** représente le groupe. Deux astuces le rendent presque O(1) :
- **la compression des chemins** : après un `find`, chaque sommet traversé est rattaché directement à la racine ;
- **l'union par taille** : le petit arbre passe sous le grand, pour que les arbres restent bas.

L'**arbre couvrant minimal** (Kruskal) : pour relier toutes les villes au moindre coût, on trie les routes de la moins chère à la plus chère, et on garde une route **seulement si** elle relie deux groupes encore séparés (sinon elle formerait une boucle inutile).

**👉 À toi :** **`public final class UnionFind`** :
- **`public UnionFind(int n)`**, **`public int find(int x)`**, **`public boolean union(int a, int b)`** (rend `false` s'ils étaient déjà ensemble), **`public boolean connected(int a, int b)`**, **`public int count()`** (le nombre de groupes) ;
- **`public static long minimumSpanningTreeCost(int n, int[][] edges)`** (chaque arête `{a, b, coût}`) : le coût total minimal ; sans modifier le tableau reçu ; si tout ne peut pas être relié, `IllegalStateException("graphe non connexe")`.

**Tes tests :** quelques unions (dont une inutile), le nombre de groupes ; un petit Kruskal (calcule-le) et un graphe d'un seul sommet ; le tableau reçu non modifié ; un graphe non connexe ; un million d'unions et un Kruskal de 300 000 arêtes en moins de 3 secondes.

### ☐ Étape 7 — La vitesse, et les mutants

**👉 À toi :** lance `Check`, et tue les **14** mutants.

### Expériences (hors sortie attendue)

1. Dans `Mesure`, écris Dijkstra **sans tas** : à chaque tour, cherche le sommet le plus proche en parcourant **tout** le tableau des durées. Chronomètre-le sur une grille de 100 × 100, puis de 200 × 200.
2. Pour le métro, compare `fewestStops(0, 3)` et les durées de `dijkstra(0)` : le trajet avec le moins d'arrêts est-il le plus rapide ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** les deux classes et leurs dix-sept signatures exactes, `ArrayDeque`, `PriorityQueue`.
- **Tes tests :** au moins **12** tests, `@BeforeEach`, `@Test`, `assertEquals(`, `assertThrows(`, `assertTimeoutPreemptively(` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse comprise**.
- **Les 14 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p09_graphs ===
[PASS] tes tests sur TON code : 15 tests, 15 reussis
[PASS] tes tests sur le code de REFERENCE : 15 tests, 15 reussis
[PASS] les tests de REFERENCE sur TON code : 15 tests, 15 reussis
   mutant 1 : tue (par breadthFirstOrder)
   …
[PASS] mutants : 14/14 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
