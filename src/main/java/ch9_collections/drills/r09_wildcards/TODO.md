# Drill de rappel 9 — Les jokers (`?`, `extends`, `super`)

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall09`** dans le paquet `ch9_collections.drills.r09_wildcards`, avec ces méthodes `static` :
  - `int count(List<?> list)` ;
  - `String types(List<?> list)` : la première lettre du nom simple de la classe de chaque élément ;
  - `double total(List<? extends Number> list)` : la somme des `doubleValue()` ;
  - `void addNumbers(List<? super Integer> list)` : ajoute 1, puis 2 ;
  - `<T extends Comparable<? super T>> T min(List<? extends T> list)`.

## Défis

- ☐ **D01.** `ints = List.of(1, 2, 3)` et `strings = List.of("a", "b")`. Affiche :
  - `count(ints)` ;
  - `count(strings)` ;
  - `types(List.of(1, "x", 2.0))`.
  → `D01 : 3 2 ISD`
- ☐ **D02.** `doubles = List.of(1.5, 2.5)`. Affiche `total(ints)`, puis `total(doubles)`.
  → `D02 : 6.0 4.0`
- ☐ **D03.** Deux listes :
  - `List<Number> numbers = new ArrayList<>()` ;
  - `List<Object> objects = new ArrayList<>(List.of("debut"))`.
  
  Appelle `addNumbers` sur chacune, puis affiche-les.
  → `D03 : [1, 2] [debut, 1, 2]`
- ☐ **D04.** `min(List.of(5, 3, 8))`, puis `min(List.of("pomme", "kiwi"))`.
  → `D04 : 3 kiwi`
- ☐ **D05.** Deux variables à joker :
  - `List<? extends Number> readOnly = ints`, puis `Number firstNumber = readOnly.get(0)` ;
  - `List<? super Integer> writeOnly = numbers`, puis `writeOnly.add(99)` et `Object got = writeOnly.get(0)`.
  
  Affiche `firstNumber`, `numbers`, puis `got`.
  → `D05 : 1 [1, 2, 99] 1`

## Expériences (hors sortie attendue)

Pour chaque ligne, dis si elle **compile** :

1. `List<Number> n = ints;`
2. `readOnly.add(4);` et `readOnly.add(null);`
3. `Integer x = writeOnly.get(0);`
4. `addNumbers(new ArrayList<Double>());`
5. `List<?> any = strings; any.add("c");`
6. `total(strings);`

## Sortie attendue complète

```
D01 : 3 2 ISD
D02 : 6.0 4.0
D03 : [1, 2] [debut, 1, 2]
D04 : 3 kiwi
D05 : 1 [1, 2, 99] 1
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Type | On peut lire… | On peut ajouter… | Accepte… |
|---|---|---|---|
| `List<?>` | des `Object` | rien (sauf `null`) | toute liste |
| `List<? extends Number>` | des `Number` | rien (sauf `null`) | `List<Integer>`, `List<Double>`, `List<Number>` |
| `List<? super Integer>` | des `Object` | des `Integer` | `List<Integer>`, `List<Number>`, `List<Object>` |
| `List<Number>` | des `Number` | des `Number` | **seulement** `List<Number>` |

**PECS** (*Producer Extends, Consumer Super*) : si la liste te **donne** des éléments, `extends` ; si elle en **reçoit**, `super`.

**L'invariance :** `List<Integer>` n'est **pas** une `List<Number>`, alors qu'un `Integer[]` est un `Number[]`.

</details>
