# Drill de rappel 5 — Streams primitifs et table des conversions

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall05`**.
- Utilise `Data.NUMBERS` et `Data.WORDS`.
- Écris une ligne `Dxx : ` par défi.

## Défis

- ☐ **D01.** Trois résultats :
  - la somme de 1 à 5 avec la borne de fin **exclue** ;
  - la même avec la borne **incluse** ;
  - 20! calculé sur un `LongStream`, en un `reduce`.
  → `D01 : 10 15 2432902008176640000`
- ☐ **D02.** Sur `NUMBERS` : la somme, la moyenne (lue sans `Optional`), puis la moyenne d'un `IntStream` vide (affichée telle quelle).
  → `D02 : 43 5.375 OptionalDouble.empty`
- ☐ **D03.** Trois extrêmes :
  - le max de `NUMBERS` ;
  - le min de `LongStream` 4 et 9 ;
  - le max de `DoubleStream` 1.5 et 2.5.
  
  Chacun est lu avec sa bonne méthode.
  → `D03 : 9 4 2.5`
- ☐ **D04.** Un objet de statistiques de chaque sorte :
  - `int` sur `NUMBERS` : affiche `min-max` ;
  - `long` sur 10 et 20 : affiche la somme ;
  - `double` sur 0.5 et 1.5 : affiche la moyenne.
  → `D04 : 1-9 30 1.0`
- ☐ **D05.** Les longueurs des mots de `WORDS` deviennent des barres de `#`. Garde les 2 premières. Passe par un `IntStream` puis reviens aux objets.
  → `D05 : [######, ######]`
- ☐ **D06.** Les pairs de 1 à 10, en `List<Integer>`.
  → `D06 : [2, 4, 6, 8, 10]`
- ☐ **D07.** Sur `Integer.MAX_VALUE` et 1 :
  - la somme **élargie en `long`** ;
  - la somme **en `int`** ;
  - puis 1 et 2 élargis en `double`, divisés par 4 et additionnés.
  → `D07 : 2147483648 -2147483648 0.75`
- ☐ **D08.** Trois conversions depuis un `Stream` d'objets ou un `DoubleStream` :
  - la somme des longueurs de `a`, `bb`, `ccc`, en `long` ;
  - la somme de `"1.5"` et `"2.5"` parsés en `double` ;
  - 1.9 et 2.9 tronqués en `int` puis additionnés.
  → `D08 : 6 4.0 3`
- ☐ **D09.** La somme de `[[1, 2], [3]]`, en aplatissant **directement** vers un `IntStream`.
  → `D09 : 6`
- ☐ **D10.** Les puissances de 3 inférieures ou égales à 100, avec un `IntStream` qui porte sa condition. Puis la somme de trois `7` générés.
  → `D10 : [1, 3, 9, 27, 81] 21`
- ☐ **D11.** Chaque lettre de `"OCP"` est décalée de +1. Recolle le résultat en `String`.
  → `D11 : PDQ`
- ☐ **D12.** Le max de `NUMBERS` boxé en `Stream<Integer>` (il te faut un comparateur, lequel ?). Puis la moyenne de 1, 2, 2 arrondie à 3 décimales avec `Math.round`.
  → `D12 : 9 1.667`

## Sortie attendue complète

```
D01 : 10 15 2432902008176640000
D02 : 43 5.375 OptionalDouble.empty
D03 : 9 4 2.5
D04 : 1-9 30 1.0
D05 : [######, ######]
D06 : [2, 4, 6, 8, 10]
D07 : 2147483648 -2147483648 0.75
D08 : 6 4.0 3
D09 : 6
D10 : [1, 3, 9, 27, 81] 21
D11 : PDQ
D12 : 9 1.667
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Table des conversions** (ligne = source, colonne = cible) :

| de \ vers | `Stream<T>` | `IntStream` | `LongStream` | `DoubleStream` |
|---|---|---|---|---|
| `Stream<T>` | `map` | `mapToInt` | `mapToLong` | `mapToDouble` |
| `IntStream` | `mapToObj`, `boxed` | `map` | `mapToLong`, `asLongStream` | `mapToDouble`, `asDoubleStream` |
| `LongStream` | `mapToObj`, `boxed` | `mapToInt` | `map` | `mapToDouble`, `asDoubleStream` |
| `DoubleStream` | `mapToObj`, `boxed` | `mapToInt` | `mapToLong` | `map` |

**Les types de retour à connaître :**

| Méthode | `IntStream` | `LongStream` | `DoubleStream` |
|---|---|---|---|
| `sum()` | `int` | `long` | `double` |
| `max()` / `min()` | `OptionalInt` | `OptionalLong` | `OptionalDouble` |
| `average()` | **`OptionalDouble`** | **`OptionalDouble`** | `OptionalDouble` |
| `summaryStatistics()` | `IntSummaryStatistics` | `LongSummaryStatistics` | `DoubleSummaryStatistics` |

**Les fabriques :**
- `range(a, b)` exclut `b` ; `rangeClosed(a, b)` inclut `b`. Elles n'existent que sur `IntStream` et `LongStream`.
- `of`, `iterate` (2 et 3 arguments), `generate`, `empty`.

**Autres points :**
- `String.chars()` rend un `IntStream`. `flatMapToInt` aplatit directement en `IntStream`.
- `sum()` d'un `IntStream` **déborde** silencieusement.

</details>
