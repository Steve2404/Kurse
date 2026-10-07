# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Pair`, `Range`, `Heap`, `Algos`, `Cache`, `Transformer` et `GenericsLab`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur des classes de test réduites.

---

## Étape 1 — `Pair` et `Range`

**Le code :** [`Pair.java`](Pair.java) et [`Range.java`](Range.java).

**`swap()` change les types :** un `Pair<String, Integer>` devient un `Pair<Integer, String>`. Le compilateur le sait, et `swapped.first()` est un `Integer`, **sans cast**.

**Question — pourquoi une méthode static ne peut pas utiliser A et B ?** A et B sont fixés **par objet** : un `Pair<String, Integer>` et un `Pair<Double, Boolean>` ont des A différents. Une méthode static n'a pas d'objet, donc pas de A. Vérifié :

```
error: non-static type variable A cannot be referenced from a static context
```

D'où `<X>`, déclaré par la méthode elle-même. `Pair.<Integer>twin(7)` donne X **explicitement**. Sans cette précision, il est **déduit** de l'argument.

**La borne `T extends Comparable<T>`** limite T aux types comparables. `Integer` et `String` conviennent.

**Expérience — `new Range<>(new Object(), new Object())`** (vérifié) :

```
error: cannot infer type arguments for Range<>
  reason: inference variable T has incompatible bounds
```

`Object` n'implémente pas `Comparable` : aucun T ne peut respecter la borne.

---

## Étape 2 — Le tas et le tri génériques

**Le code :** [`Heap.java`](Heap.java), puis `mergeSort` et `binarySearch` d'[`Algos.java`](Algos.java).

**`Comparator<? super T>`** : pour trier des `Integer`, un `Comparator<Number>` ou un `Comparator<Object>` convient aussi. Qui sait comparer des `Number` sait comparer des `Integer`. C'est le côté **consommateur** de PECS : le comparateur **reçoit** des T.

**`numbers.forEach(minHeap::push)`** : une référence de méthode sur un objet précis, utilisée comme `Consumer<Integer>`.

**Le tas des mots** : longueur croissante, puis ordre alphabétique **inverse** à égalité. `set` passe avant `map` (3 lettres chacun), et c'est le **même** code de tas que pour les nombres.

---

## Étape 3 — Bornes et jokers

**Le code :** `max`, `argMax`, `sum`, `fillSquares`, `copy`, `describe` et `repeat` d'[`Algos.java`](Algos.java).

**`<T extends Comparable<? super T>>`** : la borne de `Collections.max`. Le `? super T` accepte un T qui hérite son `compareTo` d'un parent. Par exemple, `java.sql.Timestamp` est comparable en tant que `java.util.Date`.

**Questions (PECS) :**
- **`sum` ne peut pas faire `values.add(…)`.** `Collection<? extends Number>` peut être une `List<Integer>` **ou** une `List<Double>`. Ajouter un `Integer` dans une `List<Double>` serait faux, donc `javac` refuse tout `add` (sauf `null`). Vérifié avec `v.add(1)` : `error: incompatible types: int cannot be converted to CAP#1`. On peut **lire** des `Number`, car ce que contient la collection est forcément un `Number`.
- **`fillSquares` ne lit que des `Object`.** `List<? super Integer>` peut être une `List<Integer>`, une `List<Number>` ou une `List<Object>`. Ce qu'on y lit n'est garanti que d'être un `Object`. Vérifié avec `Integer x = t.get(0)` : `error: incompatible types: CAP#1 cannot be converted to Integer`. En revanche, on peut y **ajouter** des `Integer`.

**`describe(Collection<?>)`** : le joker seul signifie « une collection de quelque chose ». On n'y lit que des `Object`, d'où `getClass()`.

---

## Étape 4 — Cache LRU, interface générique, effacement

**Le code :** [`Cache.java`](Cache.java), [`Transformer.java`](Transformer.java) et la fin du `main`.

**La trace LRU (capacité 3) :**
- `a`, `b` et `c` sont des échecs.
- Le 2e `a` est un succès, et `get` le remet en **dernier** : `[b, c, a]`.
- `d` est un échec, et la plus ancienne (`b`) sort : `[c, a, d]`.
- Et ainsi de suite, pour **2 succès et 8 échecs**.

`containsKey` ne compte pas comme un accès, et ne change donc pas l'ordre : c'est `get` qui le fait.

**Hériter d'une classe générique :** `Cache<K, V> extends LinkedHashMap<K, V>` transmet ses paramètres au parent. `removeEldestEntry` est un **point d'extension** prévu par `LinkedHashMap`.

**`Transformer.then` introduit un 3e type C** : une méthode **générique** dans une interface générique. `String`, puis `Integer` (`length`), puis `String` (étoiles).

**L'effacement :** `new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass()` vaut **`true`**. À l'exécution, il n'existe **qu'une** classe `ArrayList` : les paramètres de type sont **effacés** après la compilation.

**Expériences :**

| Expérience | Erreur de `javac` (vérifiée) | Pourquoi |
|---|---|---|
| `new T()` | `unexpected type` `required: class` `found: type parameter T` | à l'exécution, T est effacé : on ne sait pas quelle classe instancier |
| `new T[3]` | `generic array creation` | même raison, le tableau ne saurait pas quel type vérifier |
| `f(List<String>)` et `f(List<Integer>)` | `name clash: f(List<Integer>) and f(List<String>) have the same erasure` | effacées, les deux deviennent `f(List)` |
| `List<Number> n = new ArrayList<Integer>();` | `incompatible types: ArrayList<Integer> cannot be converted to List<Number>` | les génériques sont **invariants**. Sinon, on pourrait ajouter un `Double` dans une liste d'`Integer`. Il faut écrire `List<? extends Number>` |
