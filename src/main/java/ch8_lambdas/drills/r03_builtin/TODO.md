# Drill de rappel 3 — Les interfaces fonctionnelles du JDK

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris** (`java.util.function`).
- Crée la classe **`Recall03`** dans le paquet `ch8_lambdas.drills.r03_builtin`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 3), projet 2 et projet 3 (étapes 1 et 2). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r03_builtin` → **New** → **Java Class** → `Recall03`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall03`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall03`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
