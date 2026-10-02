# Drill de rappel 8 — Choisir la bonne interface

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall08`** dans le paquet `ch8_lambdas.drills.r08_choose`.
- **Pour chaque lambda donnée, c'est toi qui choisis le type de la variable** : l'interface du JDK la plus précise, sans boxing quand c'est possible. Les types attendus sont vérifiés par `Check`.

## Défis

- ☐ **D01.** Trois fournisseurs : `() -> "t"`, `() -> 2.5` (un `double`) et `() -> false`.
  → `D01 : t 2.5 false`
- ☐ **D02.** Deux consommateurs qui écrivent dans un `StringBuilder out` :
  - `out::append`, pour un `String` ;
  - `(a, n) -> out.append(n).append(a)`, pour un `String` et un `Integer`.
  
  Appelle le premier avec `"x"` et le second avec `("y", 2)`, puis affiche `out`.
  → `D02 : x2y`
- ☐ **D03.** Trois tests :
  - `String::isBlank`, sur `" "` ;
  - `(str, ch) -> str.indexOf(ch) >= 0`, avec un `String` et un `Character`, sur `("java", 'v')` ;
  - `n -> n > 0`, sur un `int` : −1.
  → `D03 : true true false`
- ☐ **D04.** Quatre fonctions :
  - `str -> str.charAt(0)`, sur `"zeta"` ;
  - `String::substring`, avec un `String` et un `Integer`, sur `("lambda", 3)` ;
  - `str -> str + "?"`, sur `"quoi"` ;
  - `String::concat`, sur `("con", "cat")`.
  → `D04 : z bda quoi? concat`
- ☐ **D05.** Trois fonctions :
  - `str -> str.length() / 2.0`, sur `"abc"` ;
  - `String::compareTo`, sur `("b", "a")` ;
  - `(a, b) -> a % b`, sur deux `int` : (17, 5).
  → `D05 : 1.5 1 2`

## Expériences (hors sortie attendue)

1. Pour D04, essaie `Function<String, String>` pour `String::substring` : quelle erreur ?
2. Pour D05, essaie `BiFunction<String, String, Integer>` pour `String::compareTo` : ça compile ? Qu'est-ce que `ToIntBiFunction` évite ?
3. `str -> str + "?"` convient-il aussi à `Function<String, String>` ? Alors pourquoi préférer `UnaryOperator` ?

## Sortie attendue complète

```
D01 : t 2.5 false
D02 : x2y
D03 : true true false
D04 : z bda quoi? concat
D05 : 1.5 1 2
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Méthode : regarde la forme de la lambda, puis choisis.**

| Paramètres | Résultat | Interface |
|---|---|---|
| aucun | T / `int` / `long` / `double` / `boolean` | `Supplier<T>` / `IntSupplier` / `LongSupplier` / `DoubleSupplier` / `BooleanSupplier` |
| T | rien | `Consumer<T>` |
| T, U | rien | `BiConsumer<T, U>` |
| T | `boolean` | `Predicate<T>` |
| T, U | `boolean` | `BiPredicate<T, U>` |
| `int` | `boolean` | `IntPredicate` |
| T | R | `Function<T, R>` (si R = T : `UnaryOperator<T>`) |
| T, U | R | `BiFunction<T, U, R>` (si tout est T : `BinaryOperator<T>`) |
| T | `int` / `double` | `ToIntFunction<T>` / `ToDoubleFunction<T>` |
| T, U | `int` | `ToIntBiFunction<T, U>` |
| `int`, `int` | `int` | `IntBinaryOperator` |

</details>
