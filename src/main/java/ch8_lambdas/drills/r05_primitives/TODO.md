# Drill de rappel 5 — Les interfaces fonctionnelles primitives

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall05`** dans le paquet `ch8_lambdas.drills.r05_primitives`.

## Défis

- ☐ **D01.** Trois fournisseurs :
  - `IntSupplier answer = () -> 42` ;
  - `LongSupplier big = () -> 1L << 40` ;
  - `BooleanSupplier yes = () -> true`.
  
  Utilise `getAsInt`, `getAsLong` et `getAsBoolean`.
  → `D01 : 42 1099511627776 true`
- ☐ **D02.** Avec `IntPredicate even`, `IntUnaryOperator square` et `IntBinaryOperator max = Math::max`, affiche :
  - `even.test(7)` et `even.negate().test(7)` ;
  - `square.applyAsInt(9)` ;
  - `square.andThen(n -> n + 1).applyAsInt(3)` ;
  - `max.applyAsInt(4, 9)`.
  → `D02 : false true 81 10 9`
- ☐ **D03.** Trois fonctions :
  - `ToIntFunction<String> length = String::length`, sur `"lambda"` ;
  - `ToIntBiFunction<String, String> both` (somme des longueurs), sur `("ab", "cde")` ;
  - `IntFunction<String> stars`, sur 4.
  → `D03 : 6 5 ****`
- ☐ **D04.** Trois fonctions :
  - `IntToDoubleFunction half = n -> n / 2.0`, sur 7 ;
  - `DoubleToIntFunction floor = d -> (int) Math.floor(d)`, sur −2.5 ;
  - `DoubleBinaryOperator avg`, sur (3, 4).
  → `D04 : 3.5 -3 3.5`
- ☐ **D05.** `ObjIntConsumer<StringBuilder> append = (s, n) -> s.append(n).append(',')`, appelé pour i de 1 à 3 avec i².
  → `D05 : 1,4,9,`

## Expériences (hors sortie attendue)

1. `IntSupplier s = () -> 4L;` : quelle erreur ? Et `LongSupplier l = () -> 4;` ?
2. `IntFunction<String> f` utilise `apply`, mais `ToIntFunction<String>` utilise `applyAsInt` : pourquoi cette différence de nom ?
3. Existe-t-il une `BooleanUnaryOperator` ou une `CharPredicate` dans le JDK ?
4. `IntPredicate p = Integer::isEven;` : quelle erreur ? (Cette méthode n'existe pas.)

## Sortie attendue complète

```
D01 : 42 1099511627776 true
D02 : false true 81 10 9
D03 : 6 5 ****
D04 : 3.5 -3 3.5
D05 : 1,4,9,
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

Les versions primitives existent pour **`int`, `long` et `double`** (et `BooleanSupplier`). Elles évitent le boxing.

| Forme | `int` | Méthode |
|---|---|---|
| fournisseur | `IntSupplier` | `getAsInt()` |
| consommateur | `IntConsumer` | `accept(int)` |
| prédicat | `IntPredicate` | `test(int)` |
| opérateurs | `IntUnaryOperator`, `IntBinaryOperator` | `applyAsInt` |
| int → objet | `IntFunction<R>` | `apply(int)` |
| objet → int | `ToIntFunction<T>`, `ToIntBiFunction<T, U>` | `applyAsInt` |
| int → autre primitif | `IntToDoubleFunction`, `IntToLongFunction` | `applyAsDouble`, `applyAsLong` |
| objet + int | `ObjIntConsumer<T>` | `accept(T, int)` |

**La règle des noms :** la méthode s'appelle `applyAsX` / `getAsX` quand le **résultat** est un primitif X.

**Pour `long` et `double` :** le même schéma (`LongSupplier`, `DoubleUnaryOperator`, `ToDoubleFunction`…).

**`boolean` :** seulement `BooleanSupplier` (`getAsBoolean`).

</details>
