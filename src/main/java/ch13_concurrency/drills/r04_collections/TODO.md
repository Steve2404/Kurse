# Drill de rappel 4 — Les collections concurrentes

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall04`** dans le paquet `ch13_concurrency.drills.r04_collections`. Le `main` déclare `throws Exception`.
- Ajoute `static String removeDuringLoop(Integer target)` :
  - elle parcourt `new ArrayList<>(List.of(1, 2, 3))` en for-each, et retire `target` quand elle le rencontre ;
  - elle rend `"ok" + liste`, ou le nom simple de l'exception.

## Défis

- ☐ **D01.** Une `ConcurrentHashMap` et 4 tâches sur un pool. Chacune fait `merge(w, 1, Integer::sum)` pour chaque mot de `"a b a c b a"`. Affiche la map en `TreeMap`.
  → `D01 : {a=12, b=8, c=4}`
- ☐ **D02.** Sur la même map et une `Collections.synchronizedMap(new HashMap<>())`. Affiche :
  - `put("x", null)` sur la `ConcurrentHashMap` (attrape l'exception) ;
  - après `put("k", null)` sur la map synchronisée, son `containsKey("k")` ;
  - puis `getOrDefault("z", 0)`, `putIfAbsent("a", 99)` et `computeIfAbsent("z", k -> 1)`.
  → `D02 : NullPointerException true 0 12 1`
- ☐ **D03.** Affiche :
  - `removeDuringLoop(1)` puis `removeDuringLoop(2)` ;
  - une `CopyOnWriteArrayList` `[1, 2, 3]` dont on retire 2 pendant le for-each ;
  - une `CopyOnWriteArraySet` de `List.of("b", "a", "b")`.
  → `D03 : ConcurrentModificationException ok[1, 3] [1, 3] [b, a]`
- ☐ **D04.** Une `ConcurrentSkipListMap` de `{30=c, 10=a, 20=b}` et un `ConcurrentSkipListSet` de `pomme`, `kiwi`, `abricot`. Affiche :
  - la map, `firstKey()`, `headMap(25)`, `ceilingKey(15)` ;
  - le set, puis `last()`.
  → `D04 : {10=a, 20=b, 30=c} 10 {10=a, 20=b} 20 [abricot, kiwi, pomme] pomme`
- ☐ **D05.** Une `LinkedBlockingQueue<>(2)` : `put("x")`. Affiche :
  - `offer("y")`, puis `offer("z", 10, MILLISECONDS)` ;
  - `take()`, puis `poll(10 ms)` deux fois.
  → `D05 : true false x y null`
- ☐ **D06.** Une `LinkedBlockingDeque` : `offerFirst(2)`, `offerFirst(1)`, `offerLast(3)`, `putLast(4)`. Une `ConcurrentLinkedDeque` de `[1, 2, 3]`. Affiche :
  - la deque bloquante, `pollLast()`, `takeFirst()` ;
  - puis `pollLast()` et `peekFirst()` de l'autre.
  → `D06 : [1, 2, 3, 4] 4 1 3 1`

## Expériences (hors sortie attendue)

1. Pourquoi `removeDuringLoop(2)` ne lève-t-il **pas** d'exception ? Essaie avec `List.of(1, 2, 3, 4)`.
2. Dans D03, le for-each sur la `CopyOnWriteArrayList` voit-il l'élément retiré ?
3. `ConcurrentHashMap.getOrDefault` et `merge` sont-ils atomiques ? Et `if (!map.containsKey(k)) map.put(k, v)` ?
4. `take()` sur une file vide, sans autre thread : que se passe-t-il ?

## Sortie attendue complète

```
D01 : {a=12, b=8, c=4}
D02 : NullPointerException true 0 12 1
D03 : ConcurrentModificationException ok[1, 3] [1, 3] [b, a]
D04 : {10=a, 20=b, 30=c} 10 {10=a, 20=b} 20 [abricot, kiwi, pomme] pomme
D05 : true false x y null
D06 : [1, 2, 3, 4] 4 1 3 1
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Interface | Concurrente | Particularité |
|---|---|---|
| `Map` | `ConcurrentHashMap` | pas de `null` ; `merge`, `compute…`, `putIfAbsent` atomiques |
| `SortedMap`/`NavigableMap` | `ConcurrentSkipListMap` | triée |
| `SortedSet`/`NavigableSet` | `ConcurrentSkipListSet` | trié |
| `List` | `CopyOnWriteArrayList` | chaque écriture copie ; l'itérateur voit une photo |
| `Set` | `CopyOnWriteArraySet` | ordre d'insertion, pas de doublon |
| `Queue` | `ConcurrentLinkedQueue` | non bloquante |
| `Deque` | `ConcurrentLinkedDeque` | non bloquante |
| `BlockingQueue` | `LinkedBlockingQueue` | `put`/`take` bloquent ; `offer`/`poll` avec délai |
| `BlockingDeque` | `LinkedBlockingDeque` | `putFirst`/`takeLast`… |

- **Les enveloppes** `Collections.synchronizedList/Map/Set(…)` protègent chaque appel, mais **pas** l'itération : il faut `synchronized (liste) { … }`.
- **`ConcurrentModificationException`** : la structure a été modifiée pendant un parcours par itérateur, même par un seul thread.

</details>
