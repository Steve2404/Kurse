# Projet 3 — Les graphes (`Queue`, `Deque`, `PriorityQueue`)

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 9) :**
- un graphe représenté par **`Map<String, List<String>>`** ou `Map<String, Map<String, Integer>>` (`TreeMap` pour un ordre stable) ;
- **`Queue`** : `offer`, `poll`, `add`, `remove`, `peek` ;
- **`Deque`** comme **pile** (`push`, `pop`, `peek`) et comme **file à deux bouts** (`offerFirst`, `offerLast`, `pollFirst`, `pollLast`, `peekFirst`, `peekLast`) ;
- **`ArrayDeque`**, et **`LinkedList`** pour reconstruire un chemin ;
- **`PriorityQueue`** : ordre naturel, ou un `Comparator` sur un **record** ;
- `Set.add` qui rend `false` pour un élément déjà présent.

Côté algorithmes :
- **tri topologique de Kahn** (et détection de cycle) ;
- **parcours en profondeur itératif** ;
- **parcours en largeur** (le moins d'étapes) ;
- **Dijkstra** (le moins de km) ;
- **composantes connexes** ;
- **maximum sur fenêtre glissante** avec une deque monotone.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch9_collections.projects.p03_graphs`, la classe **`Graphs`**, avec un record `Step(String city, int km)` imbriqué.

**Règle du crescendo :** chapitres 1 à 9. Pas de stream.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch9-p03 -sourcepath src/main/java src/main/java/ch9_collections/projects/p03_graphs/Graphs.java
java "-Duser.language=fr" -cp build/ch9-p03 ch9_collections.projects.p03_graphs.Graphs
```

**À quoi sert ce projet ?** Un **graphe** : des points (des cours, des villes) reliés par des flèches ou des routes. On le range dans une `Map` : chaque point → la liste (ou la `Map`) de ses voisins.

---

## Tableau de bord

### ☐ Étape 1 — Le tri topologique

```
ordre des cours : [bases, algo, io, poo, collections, jdbc, lambdas, streams, concurrence]
avec un cycle : [] -> cycle detecte
```

**📖 La leçon : `Queue`, une file d'attente.** On ajoute à la fin, on sert au début. Deux familles de méthodes font la même chose, mais réagissent différemment quand c'est impossible (file vide…) :

| Action | Famille « valeur spéciale » | Famille « exception » |
|---|---|---|
| ajouter | `offer(x)` | `add(x)` |
| retirer le premier | `poll()` | `remove()` |
| regarder le premier | `peek()` | `element()` |

```java
Queue<String> file = new ArrayDeque<>();
file.offer("Lea");
file.offer("Tom");
file.peek()      // "Lea" : sans retirer
file.poll()      // "Lea" : retiré
file.poll()      // "Tom"
file.poll()      // null : la file est vide
```

Une `PriorityQueue` (projet 2, étape 1) est aussi une `Queue` : `poll()` rend le plus petit.

**👉 À toi :**

- **`static List<String> topoSort(String[] edges)`** (`A>B` = A avant B) :
  1. une `TreeMap<String, List<String>> next`, et une `TreeMap<String, Integer> inDegree` (avec `putIfAbsent(A, 0)` et `merge(B, 1, Integer::sum)`) ;
  2. une **`Queue<String> ready = new PriorityQueue<>()`** des sommets de degré entrant 0 : à égalité, l'ordre alphabétique ;
  3. on retire un sommet, on l'ajoute à l'ordre, puis on décrémente ses successeurs avec `merge(n, -1, Integer::sum)`. S'il tombe à 0 : `offer`.
- **Le cycle :** si l'ordre contient moins de sommets que le graphe (4 pour `Data.CYCLIC`), affiche `cycle detecte`.

### ☐ Étape 2 — Trois parcours

```
voisins de Lyon : {Geneve=150, Marseille=315, Paris=465} ; profondeur depuis Paris : [Paris, Lille, Bruxelles, Lyon, Geneve, Marseille, Toulouse, Bordeaux, Nantes]
moins d'etapes Lille-Toulouse : [Lille, Paris, Lyon, Marseille, Toulouse]
moins de km : Lille > Paris > Nantes > Bordeaux > Toulouse = 1200 km (9 villes fixees)
composantes : [[Ajaccio, Bastia], [Bordeaux, Bruxelles, Geneve, Lille, Lyon, Marseille, Nantes, Paris, Toulouse]]
```

**📖 La leçon : `Deque`, une file à deux bouts, et une pile.** Une `Deque` (« deck ») ajoute et retire aux **deux** bouts : `offerFirst`, `offerLast`, `pollFirst`, `pollLast`, `peekFirst`, `peekLast`. Elle sert aussi de **pile** (le dernier arrivé sort le premier) avec `push`, `pop` et `peek` :

```java
Deque<String> pile = new ArrayDeque<>();
pile.push("a"); pile.push("b"); pile.push("c");
pile            // [c, b, a] : le sommet est à gauche
pile.pop()      // "c"
pile.peek()     // "b"
```

**Parcours en profondeur** = une **pile** ; **parcours en largeur** = une **file**. Dessine un petit graphe et déroule les deux à la main.

**📖 Rappel :** des `TreeMap` imbriquées : `Map<String, Map<String, Integer>>` (ville → (voisine → km)). `computeIfAbsent` crée la `Map` intérieure au besoin (projet 2, étape 1).

**👉 À toi :**

- **`static Map<String, Map<String, Integer>> roads()`** construit le graphe à double sens (des `TreeMap` imbriquées).
- **`dfs(g, start)`** utilise une **`Deque<String>` comme pile** :
  - `pop`, et ignore une ville déjà vue ;
  - empile les voisins **à l'envers**, pour visiter dans l'ordre alphabétique.
- **`bfs(g, from, to)`** :
  - une `Queue<String>` (`ArrayDeque`, `offer`/`poll`) ;
  - une `Map<String, String> parent` ;
  - le chemin se reconstruit dans une `LinkedList` avec `addFirst`.
- **`dijkstra(g, from, to)`** :
  - `PriorityQueue<Step>` triée par `Comparator.comparingInt(Step::km).thenComparing(Step::city)` ;
  - on ignore une entrée périmée (`km > dist`) ;
  - on retire avec `remove()` et on ajoute avec `add()` (la famille « exception » de `Queue`) ;
  - chaque entrée non périmée compte une ville fixée, **arrivée comprise** ; on s'arrête après avoir fixé l'arrivée.
  - Affiche `A > B > … = N km (M villes fixees)`.
- **`components(g)`** : pour chaque ville non visitée (`visited.add(x)` rend `true`), un parcours avec `pollFirst` et `offerLast`. Les villes vont dans un `TreeSet`.
- **Les appels du `main` :** `g.get("Lyon")`, `dfs(g, "Paris")`, `bfs(g, Data.FROM, Data.TO)`, `dijkstra(g, Data.FROM, Data.TO)`, `components(g)`.
- **Question :** pourquoi le parcours en largeur et Dijkstra donnent-ils deux chemins différents ?

### ☐ Étape 3 — Fenêtre glissante et API `Deque`

```
max glissant (3) : [12, 12, 12, 8, 8, 8, 9, 9]
deque : sommet z, pop z, pollLast c, reste [a, b], peekLast b ; vide : poll null, peek null
```

**📖 Rappel :** les méthodes aux deux bouts d'une `Deque` (étape 2). La fenêtre glissante (chapitre 4, projet 7, étape 3).

**👉 À toi :**

- **`windowMax(int[] a, int k)`** : une `Deque<Integer>` d'**indices** aux valeurs décroissantes. Pour chaque i :
  1. retire en tête les indices sortis de la fenêtre (`peekFirst`, `pollFirst`) ;
  2. retire en queue ceux dont la valeur est `<= a[i]` (`peekLast`, `pollLast`) ;
  3. `offerLast(i)` ;
  4. dès que `i >= k - 1`, le maximum est `a[peekFirst()]`.
  - Appel : `windowMax(Data.MEASURES, Data.WINDOW)`.
  - **Question :** pourquoi est-ce O(n), et non O(n·k) ?
- **La démonstration de l'API** :
  1. `offerFirst("b")`, `offerFirst("a")`, `offerLast("c")`, puis `push("z")` ;
  2. affiche `peek()`, `pop()`, `pollLast()`, le reste, puis `peekLast()` ;
  3. sur une `ArrayDeque` vide, affiche `poll()` et `peek()`.
- **Expérience :** `remove()`, `element()` et `pop()` sur une deque vide : quelle exception ? Et `new ArrayDeque<String>().offer(null)` ?

---

## Checklist (vérifiée par `Check`)

- `Data.COURSES` et `Data.ROADS` ;
- `Map<String, List<String>>` ;
- `Queue<String> ready = new PriorityQueue<>()`, `Deque<String> stack = new ArrayDeque<>()` ;
- `push`, `pop`, `offer`, `poll` ;
- `record Step(` et `Comparator.comparingInt(Step::km)` ;
- `peekFirst`, `peekLast`, `pollFirst`, `pollLast`, `offerLast`, `offerFirst` ;
- `LinkedList<String> path`, `visited.add(`.

---

## Sortie attendue complète

```
ordre des cours : [bases, algo, io, poo, collections, jdbc, lambdas, streams, concurrence]
avec un cycle : [] -> cycle detecte
voisins de Lyon : {Geneve=150, Marseille=315, Paris=465} ; profondeur depuis Paris : [Paris, Lille, Bruxelles, Lyon, Geneve, Marseille, Toulouse, Bordeaux, Nantes]
moins d'etapes Lille-Toulouse : [Lille, Paris, Lyon, Marseille, Toulouse]
moins de km : Lille > Paris > Nantes > Bordeaux > Toulouse = 1200 km (9 villes fixees)
composantes : [[Ajaccio, Bastia], [Bordeaux, Bruxelles, Geneve, Lille, Lyon, Marseille, Nantes, Paris, Toulouse]]
max glissant (3) : [12, 12, 12, 8, 8, 8, 9, 9]
deque : sommet z, pop z, pollLast c, reste [a, b], peekLast b ; vide : poll null, peek null
```
