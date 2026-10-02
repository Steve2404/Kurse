# Drill de rappel 8 — Déclarer des génériques

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall08`** dans le paquet `ch9_collections.drills.r08_generics`.
- Dans le **même fichier**, crée ces types package-private :
  - **`class Box<T>`** :
    - un champ `final T value` et un constructeur ;
    - `T get()` ;
    - `<R> Box<R> map(Function<? super T, ? extends R> f)`.
  - **`interface Container<T>`** :
    - `T content()` ;
    - `default String describe()`, qui rend `"contient " + nom simple de la classe du contenu`.
  - **`class NumberBox implements Container<Double>`**, construite avec un `double`.
  - **`record Holder<K, V>(K key, V value)`**, avec `Holder<V, K> swap()`.
- Dans `Recall08`, trois méthodes `static` génériques :
  - `<T> T first(List<T>)` ;
  - `<T extends Comparable<T>> T larger(T a, T b)`, qui rend a si a ≥ b ;
  - `<K, V> String entry(K, V)`, qui rend `"k=v"`.

## Défis

- ☐ **D01.** `Box<String> s = new Box<>("java")` et `Box<Integer> i = new Box<>(42)`. Affiche :
  - `s.get()` ;
  - `i.get() + 1` ;
  - `s.map(String::length).get()`.
  → `D01 : java 43 4`
- ☐ **D02.** Affiche :
  - `first(List.of("a", "b"))` ;
  - `Recall08.<Integer>first(List.of(7, 8))` ;
  - `larger("pomme", "kiwi")` ;
  - `larger(3, 9)` ;
  - `entry("age", 30)`.
  → `D02 : a 7 pomme 9 age=30`
- ☐ **D03.** `Container<Double> c = new NumberBox(2.5)`. Affiche `c.content()`, puis `c.describe()`.
  → `D03 : 2.5 contient Double`
- ☐ **D04.** `h = new Holder<>("k", 1)`. Affiche `h`, puis `h.swap()`.
  → `D04 : Holder[key=k, value=1] Holder[key=1, value=k]`
- ☐ **D05.** Deux listes : une `List<String>` et une `List<Integer>` (des `ArrayList`). Affiche `ls.getClass() == li.getClass()`, puis le nom simple de la classe.
  → `D05 : true ArrayList`
- ☐ **D06.** `var inferred = new Box<>(List.of(1, 2))`. Affiche `inferred.get().size()`, puis `new Box<>('c').get()`.
  → `D06 : 2 c`

## Expériences (hors sortie attendue)

Pour chaque ligne, dis si elle **compile** :

1. `larger(new Object(), new Object())` ;
2. dans `Box` : `T t = new T();` et `T[] arr = new T[2];` ;
3. `static T cached;` dans `Box<T>` ;
4. `Box<int> b;` ;
5. `if (s instanceof Box<String>) { }`, puis la même ligne avec `Box<?>` ;
6. deux surcharges `void f(Box<String>)` et `void f(Box<Integer>)`.

## Sortie attendue complète

```
D01 : java 43 4
D02 : a 7 pomme 9 age=30
D03 : 2.5 contient Double
D04 : Holder[key=k, value=1] Holder[key=1, value=k]
D05 : true ArrayList
D06 : 2 c
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Les déclarations :**
  - `class Box<T>`, `interface Container<T>`, `record Holder<K, V>` ;
  - méthode générique : `<T>` **avant** le type de retour (`static <T> T first(…)`) ;
  - appel explicite : `Recall08.<Integer>first(…)`.
- **Implémenter une interface générique :** on fixe le type (`implements Container<Double>`), ou on le garde (`class Box2<T> implements Container<T>`).
- **Les bornes :** `<T extends Comparable<T>>`. Plusieurs bornes : `<T extends Number & Comparable<T>>` (la classe d'abord).
- **L'effacement de type :** à l'exécution, `T` devient sa borne (`Object` par défaut). D'où les interdits :
  - `new T()` et `new T[]` ;
  - un champ `static T` ;
  - `instanceof Box<String>` (mais `instanceof Box<?>` est permis) ;
  - deux surcharges qui ne diffèrent que par le paramètre de type ;
  - les types primitifs comme arguments de type.

</details>
