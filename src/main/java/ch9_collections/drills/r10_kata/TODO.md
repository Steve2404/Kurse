# Drill de rappel 10 — Kata mixte du chapitre 9

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall10`** dans le paquet `ch9_collections.drills.r10_kata`.
- Ajoute la méthode `static <T extends Comparable<? super T>> List<T> topTwo(List<? extends T> values)`, qui rend les deux plus grands éléments **distincts**, du plus grand au plus petit. Passe par un `TreeSet` et `pollLast`.

## Défis

- ☐ **D01.** `nums = new ArrayList<>(List.of(4, 8, 15, 16, 23, 42))`, puis :
  1. retire la **valeur** 15 ;
  2. retire l'élément d'**indice** 0 ;
  3. `removeIf(n -> n > 40)`.
  → `D01 : [8, 16, 23]`
- ☐ **D02.** Compte la première lettre des mots `java map jdk list lambda`, dans une `TreeMap<Character, Integer>` avec `merge`.
  → `D02 : {j=2, l=2, m=1}`
- ☐ **D03.** Évalue l'expression postfixée `"3 4 + 2 *"` avec une `Deque<Integer>` utilisée comme pile :
  - un `switch` sur le jeton (`+`, `*`, ou un nombre) ;
  - à la fin, affiche `pop()`, puis `isEmpty()`.
  → `D03 : 14 true`
- ☐ **D04.** `names = new ArrayList<>(List.of("eve", "Bob", "alice", "Dan"))`, triée par longueur, puis par `String.CASE_INSENSITIVE_ORDER`. Affiche :
  - `names` ;
  - `topTwo(List.of(3, 9, 1, 7))` ;
  - `topTwo(names)`.
  → `D04 : [Bob, Dan, eve, alice] [9, 7] [eve, alice]`
- ☐ **D05.** `grades = new TreeMap<>(Map.of(0, "F", 50, "D", 60, "C", 70, "B", 85, "A"))`.
  1. Pour 42, 50, 68 et 91, ajoute la note `floorEntry(score).getValue()` ;
  2. affiche ensuite la taille de `headMap(60)`, puis `ceilingKey(61)`.
  → `D05 : FDCA 2 70`

## Expériences (hors sortie attendue)

1. Pourquoi `topTwo(names)` rend-il `eve` avant `alice`, et non `alice` (le plus long) ?
2. Remplace `floorEntry` par `ceilingEntry` : quelles notes obtiens-tu ? Que se passe-t-il pour 91 ?

## Sortie attendue complète

```
D01 : [8, 16, 23]
D02 : {j=2, l=2, m=1}
D03 : 14 true
D04 : [Bob, Dan, eve, alice] [9, 7] [eve, alice]
D05 : FDCA 2 70
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Choisir sa collection :**

| Besoin | Choix |
|---|---|
| accès par indice, ordre d'insertion | `ArrayList` |
| unicité sans ordre / dans l'ordre d'insertion / triée | `HashSet` / `LinkedHashSet` / `TreeSet` |
| clé → valeur, mêmes trois ordres | `HashMap` / `LinkedHashMap` / `TreeMap` |
| pile, file à deux bouts | `ArrayDeque` (`push`/`pop`, `offerFirst`/`pollLast`…) |
| toujours extraire le plus petit | `PriorityQueue` |
| « la plus grande clé ≤ x » (barème, tranches) | `TreeMap.floorEntry` / `floorKey` |

</details>
