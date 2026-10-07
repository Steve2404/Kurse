# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Graphs.java`](Graphs.java).
>
> Les valeurs et les exceptions ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Le tri topologique

**Le code :** la méthode `topoSort`.

**Kahn avec une `PriorityQueue` :** c'est le même algorithme qu'au chapitre 7 (le tableur), mais les collections remplacent les tableaux. La `PriorityQueue<String>` sans comparateur utilise l'**ordre naturel** des `String`. Parmi les cours prêts, le premier alphabétiquement passe d'abord, ce qui rend la sortie déterministe.

**`merge` rend la nouvelle valeur :** on décrémente et on teste en une seule expression.

**Le cycle :** dans `a>b, b>c, c>a, c>d`, aucun sommet n'a un degré entrant nul. `ready` est vide dès le départ, et l'ordre est **vide** : 0 < 4 sommets, donc cycle.

---

## Étape 2 — Trois parcours

**Le code :** `roads`, `dfs`, `bfs`, `dijkstra` et `components`.

**Une structure, trois usages :**

| Parcours | Structure | Méthodes |
|---|---|---|
| profondeur | `Deque` comme **pile** | `push`, `pop` |
| largeur | `Queue` (une `ArrayDeque`) comme **file** | `offer`, `poll` |
| Dijkstra | `PriorityQueue` | `add`, `remove` |

**`Map<String, Map<String, Integer>>`** : la ville, puis ses voisins, puis les kilomètres. `g.get("Lyon")` affiche directement `{Geneve=150, Marseille=315, Paris=465}`.

**Question — pourquoi BFS et Dijkstra donnent deux chemins différents ?** Ils ne minimisent pas la même chose :
- le **BFS** minimise le nombre d'**étapes** : Lille, Paris, Lyon, Marseille, Toulouse fait 4 étapes, pour 225 + 465 + 315 + 405 = 1410 km ;
- **Dijkstra** minimise les **kilomètres** : Lille, Paris, Nantes, Bordeaux, Toulouse fait aussi 4 étapes, mais seulement 225 + 385 + 345 + 245 = **1200 km**.

Le BFS ignore les poids : pour lui, toutes les routes « valent » 1.

**Les entrées périmées de Dijkstra :** une ville peut être ajoutée plusieurs fois à la file, à chaque fois qu'on trouve un chemin plus court. Seule la **première** sortie compte, car c'est la plus courte. Les autres sont ignorées (`km > dist`). Comme la `PriorityQueue` ne permet pas de **modifier** la priorité d'un élément, c'est la façon classique de faire.

**`visited.add(x)` comme test :** `add` rend `true` si l'élément était absent. On teste et on marque en une opération.

**Les composantes :** Ajaccio et Bastia ne sont reliées à aucune ville du continent. Elles forment une composante séparée.

---

## Étape 3 — Fenêtre glissante et API `Deque`

**Le code :** `windowMax` et la fin du `main`.

**Question — pourquoi O(n) ?** Chaque indice entre **une fois** dans la deque (`offerLast`) et en sort **au plus une fois** (`pollFirst` ou `pollLast`). Les `while` internes, cumulés sur toute la boucle, font donc au plus n retraits en tout. La version naïve recalcule le max de chaque fenêtre, en O(n·k).

**La trace de la démonstration :**
- `offerFirst(b)` donne `[b]`, `offerFirst(a)` donne `[a, b]`, `offerLast(c)` donne `[a, b, c]`, et `push(z)` donne `[z, a, b, c]`.
- `peek` voit `z`, `pop` retire `z`, `pollLast` retire `c` : il reste `[a, b]`, et `peekLast` voit `b`.

**Les deux familles de l'API `Queue`/`Deque` :**

| Action | Lève une exception | Rend une valeur spéciale |
|---|---|---|
| ajouter | `add`, `addFirst`, `push` | `offer`, `offerFirst`, `offerLast` (rend `false`) |
| retirer | `remove`, `removeFirst`, `pop` | `poll`, `pollFirst`, `pollLast` (rend `null`) |
| regarder | `element`, `getFirst` | `peek`, `peekFirst`, `peekLast` (rend `null`) |

**Expérience (vérifiée) :**
- `remove()`, `element()` et `pop()` sur une deque vide lèvent **`NoSuchElementException`** ;
- `poll()` et `peek()` rendent `null` ;
- `new ArrayDeque<String>().offer(null)` lève une **`NullPointerException`** : `ArrayDeque` refuse `null`, justement parce que `null` sert de valeur spéciale pour dire « vide ».
