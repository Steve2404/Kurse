# Drill de rappel 3 — Les interfaces fonctionnelles du JDK

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris** (`java.util.function`).
- Crée la classe **`Recall03`** dans le paquet `ch8_lambdas.drills.r03_builtin`.

## Défis

- ☐ **D01.** Deux `Supplier` :
  - `Supplier<String> greeting = () -> "salut"` ;
  - `Supplier<StringBuilder> builder = StringBuilder::new`.
  
  Affiche `greeting.get()`, `builder.get().append("x").length()`, puis `builder.get() != builder.get()`.
  → `D01 : salut 1 true`
- ☐ **D02.** Deux consommateurs qui écrivent dans un `StringBuilder log` :
  - `Consumer<String> add`, qui ajoute `s` puis `;` ;
  - `BiConsumer<String, Integer> repeat`, qui ajoute `s.repeat(n)` puis `;`.
  
  Appelle `add.accept("a")`, puis `repeat.accept("b", 3)`, puis affiche `log`.
  → `D02 : a;bbb;`
- ☐ **D03.** Deux prédicats :
  - `Predicate<String> isLong` (longueur > 4), testé sur `"lambda"` puis `"java"` ;
  - `BiPredicate<String, Integer> hasLength`, testé sur `("java", 4)`.
  → `D03 : true false true`
- ☐ **D04.** Deux fonctions :
  - `Function<String, Integer> length = String::length`, appliquée à `"fonction"` ;
  - `BiFunction<String, String, String> join = (x, y) -> x + "-" + y`, appliquée à `("a", "b")`.
  → `D04 : 8 a-b`
- ☐ **D05.** Deux opérateurs :
  - `UnaryOperator<String> upper = String::toUpperCase`, appliqué à `"ok"` ;
  - `BinaryOperator<Integer> sum = Integer::sum`, appliqué à `(20, 22)`.
  → `D05 : OK 42`
- ☐ **D06.** `Function<String, String> asFunction = upper;` et `BiFunction<Integer, Integer, Integer> asBiFunction = sum;` (sans cast). Applique-les à `"a"`, puis à `(1, 2)`.
  → `D06 : A 3`

## Expériences (hors sortie attendue)

1. `Supplier<String> s = () -> { };` : quelle erreur ?
2. `Consumer<String> c = s -> s.length();` compile-t-il ? Pourquoi ?
3. `Predicate<String> p = s -> s;` : quelle erreur ?
4. `UnaryOperator<String> u = s -> s.length();` : quelle erreur ?

## Sortie attendue complète

```
D01 : salut 1 true
D02 : a;bbb;
D03 : true false true
D04 : 8 a-b
D05 : OK 42
D06 : A 3
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Interface | Méthode | Forme |
|---|---|---|
| `Supplier<T>` | `get()` | `() -> T` |
| `Consumer<T>` | `accept(T)` | `T -> void` |
| `BiConsumer<T, U>` | `accept(T, U)` | `(T, U) -> void` |
| `Predicate<T>` | `test(T)` | `T -> boolean` |
| `BiPredicate<T, U>` | `test(T, U)` | `(T, U) -> boolean` |
| `Function<T, R>` | `apply(T)` | `T -> R` |
| `BiFunction<T, U, R>` | `apply(T, U)` | `(T, U) -> R` |
| `UnaryOperator<T>` | `apply(T)` | `T -> T` (étend `Function<T, T>`) |
| `BinaryOperator<T>` | `apply(T, T)` | `(T, T) -> T` (étend `BiFunction<T, T, T>`) |

**Le piège `Consumer` :** une expression qui rend une valeur (`s.length()`) est acceptée, et la valeur est ignorée. Une lambda qui ne rend rien ne convient pas à un `Supplier`.

</details>
