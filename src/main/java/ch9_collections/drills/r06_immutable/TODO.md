# Drill de rappel 6 — Taille fixe, immuable, vue non modifiable

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 8 min, puis 4 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall06`** dans le paquet `ch9_collections.drills.r06_immutable`.

## Défis

- ☐ **D01.** `String[] array = {"a", "b", "c"}` et `fixed = Arrays.asList(array)`.
  1. `fixed.set(0, "A")` ;
  2. `array[2] = "C"`.
  
  Affiche `fixed`, puis `Arrays.toString(array)`.
  → `D01 : [A, b, C] [A, b, C]`
- ☐ **D02.** `source = new ArrayList<>(List.of("x", "y"))`.
  1. `copy = List.copyOf(source)` ;
  2. `view = Collections.unmodifiableList(source)` ;
  3. `source.add("z")`.
  
  Affiche `copy`, `view`, puis leurs tailles.
  → `D02 : [x, y] [x, y, z] 2 3`
- ☐ **D03.** `of = List.of(3, 1, 2)`, `set = Set.of("solo")` et `map = Map.of("a", 1, "b", 2)`. Affiche :
  - `of` ;
  - `set` ;
  - `new TreeMap<>(map)` ;
  - `of.contains(2)` ;
  - `map.get("b")`.
  → `D03 : [3, 1, 2] [solo] {a=1, b=2} true 2`
- ☐ **D04.** Pour trier `of`, copie-la dans une `ArrayList`, puis `Collections.sort`. Affiche `of`, puis la copie.
  → `D04 : [3, 1, 2] [1, 2, 3]`
- ☐ **D05.** `entries = Map.ofEntries(Map.entry("k1", 10), Map.entry("k2", 20))`. Affiche :
  - `entries.size()` ;
  - `entries.get("k2")` ;
  - `Map.entry("cle", "valeur")` ;
  - `List.copyOf(Set.of(42))`.
  → `D05 : 2 20 cle=valeur [42]`
- ☐ **D06.** Affiche :
  - la taille de `Collections.emptyList()` ;
  - `Collections.nCopies(2, "ab")` ;
  - `Collections.singletonList(7)`.
  → `D06 : 0 [ab, ab] [7]`

## Expériences (hors sortie attendue)

Pour chaque ligne, dis si elle **compile**, puis ce qui se passe à l'exécution :

1. `fixed.add("d");`
2. `List.of(1, 2).set(0, 9);`
3. `view.add("w");`
4. `List.of("a", null);`
5. `Set.of("a", "a");`
6. `Map.of("k", 1, "k", 2);`
7. `List.copyOf(Arrays.asList("a", null));`

## Sortie attendue complète

```
D01 : [A, b, C] [A, b, C]
D02 : [x, y] [x, y, z] 2 3
D03 : [3, 1, 2] [solo] {a=1, b=2} true 2
D04 : [3, 1, 2] [1, 2, 3]
D05 : 2 20 cle=valeur [42]
D06 : 0 [ab, ab] [7]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Création | `set` | `add` / `remove` | `null` | Suit la source ? |
|---|---|---|---|---|
| `Arrays.asList(array)` | oui | `UnsupportedOperationException` | oui | oui, le **tableau** |
| `List.of`, `Set.of`, `Map.of` | `UnsupportedOperationException` | `UnsupportedOperationException` | NPE | — |
| `List.copyOf(c)` | `UnsupportedOperationException` | `UnsupportedOperationException` | NPE | **non** (une copie) |
| `Collections.unmodifiableList(l)` | `UnsupportedOperationException` | `UnsupportedOperationException` | oui | **oui** (une vue) |

- **Les doublons :** `Set.of` et `Map.of` lèvent `IllegalArgumentException`.
- `Map.of` accepte jusqu'à 10 paires ; au-delà, `Map.ofEntries(Map.entry(…), …)`.

</details>
