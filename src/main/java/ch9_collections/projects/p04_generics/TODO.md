# Projet 4 — Le laboratoire des génériques

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 9) :** **déclarer** des génériques :
- un **record générique** `Pair<A, B>`, avec une méthode qui rend `Pair<B, A>` ;
- une **méthode `static` générique** `<X> Pair<X, X> twin(X)`, et l'appel explicite `Pair.<Integer>twin(7)` ;
- un **type borné** `Range<T extends Comparable<T>>` ;
- une **classe générique** `Heap<T>` qui reçoit un `Comparator<? super T>` : on ne peut pas créer de `T[]` (effacement), donc on utilise une `List<T>` ;
- **étendre une classe générique** : `Cache<K, V> extends LinkedHashMap<K, V>` (avec `removeEldestEntry`) ;
- une **interface générique** `Transformer<A, B>`, avec une méthode `default` **elle-même générique** `<C>` ;
- les **jokers** : `?`, `? extends T` (producteur), `? super T` (consommateur) — la règle PECS ;
- la **borne récursive** `<T extends Comparable<? super T>>` ;
- l'**effacement de type**.

Côté algorithmes :
- un **tas binaire générique** ;
- un **tri fusion générique** stable ;
- une **dichotomie générique** ;
- un **cache LRU**.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch9_collections.projects.p04_generics` :
- `Pair`, `Range`, `Heap`, `Cache`, `Transformer`, `Algos` ;
- **`GenericsLab`** (le `main`).

**Règle du crescendo :** chapitres 1 à 9. Pas de stream.

---

## Tableau de bord

### ☐ Étape 1 — `Pair` et `Range`

```
paires : Pair[first=age, second=30] Pair[first=30, second=age] Pair[first=x, second=x] Pair[first=7, second=7] Integer
intervalles : Range[low=13, high=19] true false true false
```
- **`record Pair<A, B>(A first, B second)`** :
  - `Pair<B, A> swap()` ;
  - `public static <X> Pair<X, X> twin(X value)`.
  - **Question :** pourquoi une méthode `static` ne peut-elle pas utiliser A et B ?
- **Dans `main`** :
  - `p = new Pair<>("age", 30)`, puis `p.swap()` ;
  - `Pair.twin("x")` et `Pair.<Integer>twin(7)` ;
  - le nom simple de la classe de `swapped.first()`.
- **`record Range<T extends Comparable<T>>(T low, T high)`** :
  - le constructeur compact échange low et high si nécessaire ;
  - `boolean contains(T)`.
  - Teste `new Range<>(19, 13)` avec 15 et 20, puis `new Range<>("c", "m")` avec `"java"` et `"python"`.
  - **Expérience :** `new Range<>(new Object(), new Object())` : quelle erreur ?

### ☐ Étape 2 — Le tas et le tri génériques

```
tas : 1 3 7 11 19 25 30 42 | set map deque lambda generique collection
tri fusion : [map, set, deque, lambda, generique, collection] [1, 3, 7, 11, 19, 25, 30, 42] ; recherche 25 -> 5, 20 -> -6
```
- **`class Heap<T>`** :
  - `private final List<T> items` et `private final Comparator<? super T> order` ;
  - `push`, `pop`, `size`, `isEmpty`.
  - Un `Heap<Integer>` avec `Comparator.naturalOrder()`, et un `Heap<String>` avec `comparingInt(String::length).thenComparing(Comparator.reverseOrder())`.
  - Remplis-les avec `numbers.forEach(minHeap::push)`, puis vide-les.
- **Dans `final class Algos`**, avec un constructeur privé :
  - `public static <T> List<T> mergeSort(List<T> list, Comparator<? super T> cmp)`, récursif et stable (`<= 0`) ;
  - `public static <T> int binarySearch(List<? extends T> sorted, T key, Comparator<? super T> cmp)`.
  - Trie les mots par longueur, puis les nombres. Cherche 25, puis 20, dans les nombres triés.

### ☐ Étape 3 — Bornes et jokers

```
bornes : max 42 set, argMax collection, somme 138.0 4.0
jokers : [1, 4, 9, 16] [1, 4, 9, 16, texte] | 5 elements : Integer Integer Integer Integer String | [ab, ab, ab]
```
- **Dans `Algos`** :
  - `<T extends Comparable<? super T>> T max(Collection<? extends T> values)` ;
  - `<K, V extends Comparable<? super V>> K argMax(Map<K, V> map)` ;
  - `double sum(Collection<? extends Number>)` ;
  - `void fillSquares(List<? super Integer> target, int n)` ;
  - `<T> void copy(List<? super T> dst, List<? extends T> src)` ;
  - `String describe(Collection<?> c)` ;
  - `<T> List<T> repeat(T value, int times)`.
- **Les appels :**
  - `max(numbers)` et `max(words)` ;
  - `argMax` d'une `TreeMap` mot → longueur ;
  - `sum(numbers)` et `sum(List.of(1.5, 2.5))` ;
  - `fillSquares(squares, 4)`, avec une **`List<Number>`** ;
  - `Algos.<Number>copy(all, squares)`, avec une **`List<Object>`**, puis `all.add("texte")` ;
  - `describe(all)` et `repeat("ab", 3)`.
- **Questions (PECS) :**
  - pourquoi `sum` ne peut-elle pas faire `values.add(…)` ?
  - pourquoi `fillSquares` ne peut-elle lire que des `Object` ?

### ☐ Étape 4 — Cache LRU, interface générique, effacement

```
LRU : [a] [a, b] [a, b, c] [b, c, a] [c, a, d] [a, d, b] [d, b, e] [b, e, a] [e, a, c] [e, c, a] -> 2 succes, 8 echecs
transformer : ********* ; effacement : true
```
- **`class Cache<K, V> extends LinkedHashMap<K, V>`** :
  - le constructeur appelle `super(16, 0.75f, true)` : l'ordre devient celui des **accès** ;
  - `removeEldestEntry` rend `size() > capacity` ;
  - `V load(K key, Function<? super K, ? extends V> loader)` compte succès et échecs ;
  - `stats()`.
  - Pour chaque accès de `Data.ACCESSES`, appelle `load(key, String::length)` et affiche `keySet()`.
- **`@FunctionalInterface interface Transformer<A, B>`** :
  - `B transform(A)` ;
  - `default <C> Transformer<A, C> then(Transformer<? super B, ? extends C> next)`.
  - `Transformer<String, Integer> length = String::length`, puis `length.then(n -> "*".repeat(n))`, appliqué à `"generique"`.
- **L'effacement :** `new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass()`.
- **Expériences :**
  - `new T()` ou `new T[3]` dans `Heap` ;
  - deux méthodes `void f(List<String>)` et `void f(List<Integer>)` dans la même classe ;
  - `List<Number> n = new ArrayList<Integer>();`.

---

## Checklist (vérifiée par `Check`)

- `record Pair<A, B>`, `public static <X> Pair<X, X> twin(`, `Pair.<Integer>twin(` ;
- `record Range<T extends Comparable<T>>` ;
- `class Heap<T>`, `Comparator<? super T>` ;
- `class Cache<K, V> extends LinkedHashMap<K, V>`, `removeEldestEntry(` ;
- `interface Transformer<A, B>`, `default <C> Transformer<A, C> then(` ;
- `<T extends Comparable<? super T>> T max(Collection<? extends T>`, `<K, V extends Comparable<? super V>>` ;
- `Collection<? extends Number>`, `List<? super Integer>`, `Collection<?>` ;
- `static <T> void copy(List<? super T> dst, List<? extends T> src)`, `Algos.<Number>copy(`.

---

## Sortie attendue complète

```
paires : Pair[first=age, second=30] Pair[first=30, second=age] Pair[first=x, second=x] Pair[first=7, second=7] Integer
intervalles : Range[low=13, high=19] true false true false
tas : 1 3 7 11 19 25 30 42 | set map deque lambda generique collection
tri fusion : [map, set, deque, lambda, generique, collection] [1, 3, 7, 11, 19, 25, 30, 42] ; recherche 25 -> 5, 20 -> -6
bornes : max 42 set, argMax collection, somme 138.0 4.0
jokers : [1, 4, 9, 16] [1, 4, 9, 16, texte] | 5 elements : Integer Integer Integer Integer String | [ab, ab, ab]
LRU : [a] [a, b] [a, b, c] [b, c, a] [c, a, d] [a, d, b] [d, b, e] [b, e, a] [e, a, c] [e, c, a] -> 2 succes, 8 echecs
transformer : ********* ; effacement : true
```
