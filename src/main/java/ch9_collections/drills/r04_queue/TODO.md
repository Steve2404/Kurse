# Drill de rappel 4 — `Queue`, `Deque`, `PriorityQueue`

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall04`** dans le paquet `ch9_collections.drills.r04_queue`.

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

**`PriorityQueue`** : la **tête** est le plus petit élément (selon l'ordre), mais l'itération et le `toString` ne sont **pas** triés. Pour un tas max : `Collections.reverseOrder()`.

</details>
