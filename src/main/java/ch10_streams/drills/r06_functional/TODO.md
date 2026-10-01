# Drill de rappel 6 — Les interfaces fonctionnelles primitives

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall06`**.
- **Chaque fonction est une variable déclarée avec son type exact.** C'est toi qui trouves ce type : l'énoncé ne le nomme jamais. `Check` vérifie que tous les types attendus apparaissent.
- Écris une ligne `Dxx : ` par défi.

## Défis

- ☐ **D01.** Trois tests sur des primitifs :
  - « un `int` est pair » ;
  - « un `long` dépasse un milliard » ;
  - « un `double` est inférieur à 10 ».
  
  Affiche, dans l'ordre :
  - pair(4) ;
  - la **négation** de pair, appliquée à 4 ;
  - « dépasse » sur 3 000 000 000 ;
  - « < 10 **et** > 5 » sur 7.5 ;
  - le nombre de pairs dans `NUMBERS`, obtenu en passant le test pair à `filter`.
  → `D01 : true false true true 3`
- ☐ **D02.** Deux opérations `int → int` : « carré » et « +1 ». Affiche :
  - carré **puis** +1, sur 3 ;
  - +1 **puis** carré, sur 3, avec l'autre méthode de composition ;
  - l'opération identité appliquée à 42.
  → `D02 : 10 16 42`
- ☐ **D03.** Une opération `(int, int) → int` qui calcule le PGCD. Utilise-la pour réduire 12, 18, 24, puis appelle-la directement sur 35 et 21.
  → `D03 : 6 7`
- ☐ **D04.** Une fonction `int → String` qui produit n étoiles. Affiche-la sur 3, puis passe-la à `mapToObj` sur 1..3.
  → `D04 : *** [*, **, ***]`
- ☐ **D05.** Trois résultats :
  - une fonction `String → int` (la longueur), appliquée à `"lambda"` ;
  - une fonction `(String, String) → int` (somme des longueurs), appliquée à `"java"` et `"stream"` ;
  - la longueur max de `WORDS`, en passant la première fonction à `mapToInt`.
  → `D05 : 6 10 9`
- ☐ **D06.** Deux fonctions sur un `int` :
  - `int → long`, le cube (attention au débordement), appliquée à 2000 ;
  - `int → double`, la moitié, appliquée à 7.
  → `D06 : 8000000000 3.5`
- ☐ **D07.** Deux fonctions qui rendent un `int` :
  - `double → int`, le plancher, appliquée à -2.5 ;
  - `long → int`, le nombre de chiffres, appliquée au cube de D06.
  → `D07 : -3 10`
- ☐ **D08.** Quatre fournisseurs sans argument :
  - un `int` (42) ;
  - un `long` (1790000000) ;
  - un `double` (π, affiché tronqué en `int`) ;
  - un `boolean` (« le fournisseur d'`int` donne plus de 40 »).
  → `D08 : 42 1790000000 3 true`
- ☐ **D09.** Deux consommateurs :
  - un consommateur d'`int` qui ajoute à un `StringBuilder`, passé à `forEach` sur les 3 premiers de `NUMBERS` ;
  - l'accumulateur `(StringBuilder, int)` d'un `IntStream.collect`, qui produit `NUMBERS` en CSV.
  → `D09 : 538 5,3,8,1,9,2,8,7`
- ☐ **D10.** Trois fonctions qui rendent un `double` :
  - `(Integer, Integer) → double`, le ratio de 1 et 4 ;
  - `(double, double) → double`, la moyenne de 1 et 2 ;
  - `double → double`, « fois 2 », composée deux fois et appliquée à 1.5.
  → `D10 : 0.25 1.5 6.0`
- ☐ **D11.** Une opération `(long, long) → long` qui multiplie. Utilise-la pour réduire 1..15 (15!).
  → `D11 : 1307674368000`

## Sortie attendue complète

```
D01 : true false true true 3
D02 : 10 16 42
D03 : 6 7
D04 : *** [*, **, ***]
D05 : 6 10 9
D06 : 8000000000 3.5
D07 : -3 10
D08 : 42 1790000000 3 true
D09 : 538 5,3,8,1,9,2,8,7
D10 : 0.25 1.5 6.0
D11 : 1307674368000
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**La règle de nommage :**
- `IntXxx` **reçoit** un `int` ;
- `ToIntXxx` **renvoie** un `int` ;
- `XxxToYyy` reçoit `Xxx` et renvoie `Yyy`.

| Interface | Méthode | Signature |
|---|---|---|
| `IntPredicate` / `LongPredicate` / `DoublePredicate` | `test` | `int → boolean` |
| `IntUnaryOperator` (Long…, Double…) | `applyAsInt` | `int → int` |
| `IntBinaryOperator` (Long…, Double…) | `applyAsInt` | `(int, int) → int` |
| `IntFunction<R>` (Long…, Double…) | `apply` | `int → R` |
| `ToIntFunction<T>` (ToLong…, ToDouble…) | `applyAsInt` | `T → int` |
| `ToIntBiFunction<T, U>` (ToLong…, ToDouble…) | `applyAsInt` | `(T, U) → int` |
| `IntToLongFunction`, `IntToDoubleFunction` | `applyAsLong` / `applyAsDouble` | |
| `LongToIntFunction`, `LongToDoubleFunction` | `applyAsInt` / `applyAsDouble` | |
| `DoubleToIntFunction`, `DoubleToLongFunction` | `applyAsInt` / `applyAsLong` | |
| `IntSupplier`, `LongSupplier`, `DoubleSupplier`, `BooleanSupplier` | `getAsInt` / `getAsLong` / `getAsDouble` / `getAsBoolean` | `() → x` |
| `IntConsumer` (Long…, Double…) | `accept` | `int → void` |
| `ObjIntConsumer<T>` (ObjLong…, ObjDouble…) | `accept` | `(T, int) → void` |

**Pièges :**
- Il n'existe **pas** de `IntToIntFunction` : c'est `IntUnaryOperator`.
- `BooleanSupplier` est le **seul** type « booléen » de la liste, à part les `Predicate`.
- Les méthodes par défaut :
  - `andThen` et `compose` existent sur les `UnaryOperator` primitifs ;
  - `and`, `or` et `negate` existent sur les `Predicate` ;
  - `identity()` est statique sur `IntUnaryOperator`.

</details>
