# Drill de rappel 4 — `Queue`, `Deque`, `PriorityQueue`

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall04`** dans le paquet `ch9_collections.drills.r04_queue`.

**Les notions de ce drill ont été apprises dans :** projet 2 (étape 1), projet 3 et projet 5 (étape 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r04_queue` → **New** → **Java Class** → `Recall04`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall04`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall04`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `Queue<String> q = new LinkedList<>()`, puis `offer("a")`, `add("b")` et `offer("c")`.

  Affiche `peek()`, `element()`, `poll()`, `remove()`, puis `q`.
  → `D01 : a a a b [c]`
- ☐ **D02.** `Queue<String> empty = new ArrayDeque<>()`. Affiche `peek()`, `poll()`, `offer("x")`, puis `size()`.
  → `D02 : null null true 1`
- ☐ **D03.** `Deque<Integer> stack = new ArrayDeque<>()`, puis `push` de 1, 2 et 3.

  Affiche `stack`, `peek()`, `pop()`, puis `stack`.
  → `D03 : [3, 2, 1] 3 3 [2, 1]`
- ☐ **D04.** `Deque<String> d = new ArrayDeque<>()`, puis `offerFirst("b")`, `offerLast("c")`, `addFirst("a")` et `addLast("d")`. Affiche, dans cet ordre :
  - `d` ;
  - `peekFirst() + peekLast()` ;
  - `pollFirst() + pollLast()` ;
  - `d` ;
  - `removeFirst()` ;
  - `getLast()`.
  → `D04 : [a, b, c, d] ad ad [b, c] b c`
- ☐ **D05.** Deux tas :
  - `pq = new PriorityQueue<>(List.of(5, 1, 4, 2))`, vidé avec `poll` dans un `StringBuilder` ;
  - `maxPq = new PriorityQueue<>(Collections.reverseOrder())`, puis `addAll(List.of(5, 1, 4, 2))`.
  
  Affiche le `StringBuilder`, `maxPq.poll() + "" + maxPq.poll()` (deux `poll` collés), puis `maxPq.peek()`.
  → `D05 : 1245 54 2`
- ☐ **D06.** Une `PriorityQueue<String>` triée par longueur, puis par ordre alphabétique (une lambda). Ajoute-lui `List.of("ccc", "a", "bb", "aa")`.

  Affiche trois `poll()`, puis `size()`.
  → `D06 : a aa bb 1`
- ☐ **D07.** `Deque<String> trail = new ArrayDeque<>(List.of("x", "y", "z"))`.
  1. Parcours-la à l'envers avec `descendingIterator()`, en concaténant les éléments ;
  2. affiche la concaténation, `contains("y")`, `removeLastOccurrence("y")`, puis `trail`.
  → `D07 : zyx true true [x, z]`

## Expériences (hors sortie attendue)

1. `System.out.println(new PriorityQueue<>(List.of(5, 1, 4, 2)))` : l'affichage est-il trié ? Pourquoi ?
2. `new ArrayDeque<String>().element()`, `.remove()`, `.pop()` : quelles exceptions ?
3. `new ArrayDeque<String>().offer(null)`, contre `new LinkedList<String>().offer(null)`.

## Sortie attendue complète

```
D01 : a a a b [c]
D02 : null null true 1
D03 : [3, 2, 1] 3 3 [2, 1]
D04 : [a, b, c, d] ad ad [b, c] b c
D05 : 1245 54 2
D06 : a aa bb 1
D07 : zyx true true [x, z]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| | Lève une exception | Rend une valeur spéciale |
|---|---|---|
| ajouter | `add` | `offer` (`false`) |
| retirer la tête | `remove` | `poll` (`null`) |
| lire la tête | `element` | `peek` (`null`) |

**`Deque`** :
- versions `First` et `Last` de chaque méthode : `addFirst`/`offerFirst`, `removeFirst`/`pollFirst`, `getFirst`/`peekFirst` ;
- **la pile** : `push` = `addFirst`, `pop` = `removeFirst` (exception si vide), `peek` = `peekFirst` ;
- `ArrayDeque` refuse `null`.

**Parcourir une `Deque` à l'envers :** `descendingIterator()`. `removeFirstOccurrence` et `removeLastOccurrence` retirent une valeur précise.

**`PriorityQueue`** : la **tête** est le plus petit élément (selon l'ordre), mais l'itération et le `toString` ne sont **pas** triés. Pour un tas max : `Collections.reverseOrder()`.

</details>
