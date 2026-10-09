# 🏠 Palais mental — chapitre 8 : la chambre 2, stations 7 à 12

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Les stations 1 à 6 de la chambre 2 gardent le chapitre 7.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 7. 🚪 L'armoire — l'écriture d'une lambda

- **Image :** dans l'armoire, une **flèche `->`** suspendue comme un cintre. À gauche, les **paramètres** ; s'il n'y en a **qu'un, sans type**, il peut retirer ses **parenthèses** comme on retire un manteau. À droite, le corps : en **une seule expression**, il est nu ; dès qu'il met des **accolades** `{ }`, il doit s'habiller complètement, avec **`return`** et des **points-virgules**. Les paramètres portent tous un type, ou **tous `var`**, ou **aucun** : jamais un mélange.
- **À retenir :**
  - `x -> x * 2`, `(x, y) -> x + y`, `(int x) -> x`, `() -> 42` ;
  - parenthèses facultatives **seulement** pour un paramètre unique **sans type** ;
  - avec `{ }` : `return` (si une valeur est attendue) et `;` obligatoires ;
  - types : tous écrits, tous `var`, ou aucun ;
  - un paramètre ne peut pas avoir le **nom d'une variable locale** déjà visible.
- **Mon image :** …

### 8. 🪞 Le miroir — l'interface fonctionnelle

- **Image :** le miroir ne reflète **qu'un seul visage** : une interface fonctionnelle a **une seule méthode abstraite**. Les visages **`default`**, **`static`** et ceux copiés d'**`Object`** (`equals`, `toString`, `hashCode`) ne comptent pas. Un **autocollant `@FunctionalInterface`** sur le miroir fait **sonner une alarme** s'il y a deux visages.
- **À retenir :**
  - interface fonctionnelle = **exactement une** méthode abstraite ;
  - les méthodes `default`, `static`, `private`, et les méthodes publiques d'`Object` redéclarées **ne comptent pas** ;
  - `@FunctionalInterface` est facultative mais fait échouer la compilation si la règle n'est pas respectée ;
  - une lambda ne peut s'affecter qu'à une interface fonctionnelle.
- **Mon image :** …

### 9. 🖥️ Le bureau — les interfaces fonctionnelles de base

- **Image :** sur le bureau, **cinq employés** et leur unique mot. Le **fournisseur** `Supplier` ne prend rien et crie **« get ! »**. Le **mangeur** `Consumer` avale tout et dit **« accept »** sans rien rendre. Le **juge** `Predicate` frappe son marteau : **« test ! »**, vrai ou faux. Le **traducteur** `Function` prend un mot et en rend un autre : **« apply »**. Ses deux cousins jumeaux `UnaryOperator` (même type en entrée et en sortie) et `BinaryOperator` (deux entrées, même type). Chacun a un **jumeau `Bi…`** qui prend **deux** arguments.
- **À retenir :**
  - `Supplier<T>` : `T get()` ;
  - `Consumer<T>` : `void accept(T)` ; `BiConsumer<T,U>` ;
  - `Predicate<T>` : `boolean test(T)` ; `BiPredicate<T,U>` ;
  - `Function<T,R>` : `R apply(T)` ; `BiFunction<T,U,R>` ;
  - `UnaryOperator<T>` : `T apply(T)` ; `BinaryOperator<T>` : `T apply(T, T)`.
- **Mon image :** …

### 10. 🪑 La chaise — les variantes primitives

- **Image :** sur la chaise, les mêmes employés **en tenue de sport** (primitifs), et ils **ajoutent le type à leur cri** : le fournisseur crie **« getAsInt ! »**, `BooleanSupplier` crie **« getAsBoolean ! »**. Le traducteur qui **finit** en `int` (`ToIntFunction`) crie **« applyAsInt »**. Celui qui **part** d'un `int` (`IntFunction<R>`) crie juste « apply ».
- **À retenir :**
  - `IntSupplier.getAsInt()`, `BooleanSupplier.getAsBoolean()`, `DoubleSupplier.getAsDouble()` ;
  - `IntPredicate.test(int)`, `IntConsumer.accept(int)`, `IntUnaryOperator.applyAsInt(int)`, `IntBinaryOperator.applyAsInt(int, int)` ;
  - `IntFunction<R>.apply(int)` (rend un objet) contre `ToIntFunction<T>.applyAsInt(T)` (rend un `int`) ;
  - mêmes familles pour `long` et `double` ; pas de `BooleanFunction`.
- **Mon image :** …

### 11. 🪟 La fenêtre — les références de méthode

- **Image :** par la fenêtre, **quatre panneaux `::`**. Le **panneau d'usine** `Math::abs` (méthode **statique**). Le **panneau avec ton nom** `texte::startsWith` (méthode d'**un objet précis**). Le **panneau générique** `String::isEmpty` (méthode d'instance, l'objet **arrive en premier paramètre**). Le **panneau de chantier** `StringBuilder::new` (un **constructeur**).
- **À retenir :**
  - statique : `Math::abs` ≡ `x -> Math.abs(x)` ;
  - instance d'un objet donné : `s::startsWith` ≡ `p -> s.startsWith(p)` ;
  - instance d'un objet reçu : `String::isEmpty` ≡ `s -> s.isEmpty()` ;
  - constructeur : `StringBuilder::new` ≡ `() -> new StringBuilder()` ;
  - une référence de méthode ne prend **pas** de parenthèses ni d'arguments.
- **Mon image :** …

### 12. 🟫 Le tapis — la capture et les méthodes de combinaison

- **Image :** une lambda saute sur le tapis et **photographie** les variables autour d'elle. Elle ne peut photographier que des variables **figées** (effectivement finales) : si quelqu'un modifie la variable, **avant ou après**, la photo **brûle** (ne compile pas). Sur le tapis, des **rails** relient les employés : `and`, `or`, `negate` pour les juges ; `andThen` pour les mangeurs et les traducteurs ; et **`compose`**, le rail **à l'envers**, où l'argument passe **en premier**.
- **À retenir :**
  - une lambda n'utilise que des variables locales **effectivement finales** (jamais réaffectées, ni avant ni après) ; les champs, eux, peuvent changer ;
  - `Predicate` : `and`, `or`, `negate` ;
  - `Consumer.andThen` ; `Function.andThen(g)` = `f` puis `g` ; `f.compose(g)` = `g` **puis** `f` ;
  - `Function.identity()`.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 7 : `x, y -> x + y` compile-t-il ?
2. Station 8 : une interface avec une méthode abstraite et `boolean equals(Object o);` est-elle fonctionnelle ?
3. Station 9 : quelle est la méthode d'un `Supplier` ? d'un `Predicate` ?
4. Station 10 : quelle est la méthode d'un `ToIntFunction` ?
5. Station 11 : à quelle lambda correspond `String::length` ?
6. Station 12 : dans `f.compose(g)`, qui s'exécute en premier ?
