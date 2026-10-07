# Projet 4 — Le laboratoire des génériques

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

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

**Ce projet t'apprend à écrire tes propres types génériques**, comme `List<T>`.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch9-p04 -sourcepath src/main/java src/main/java/ch9_collections/projects/p04_generics/GenericsLab.java
java "-Duser.language=fr" -cp build/ch9-p04 ch9_collections.projects.p04_generics.GenericsLab
```

---

## Tableau de bord

### ☐ Étape 1 — `Pair` et `Range`

```
paires : Pair[first=age, second=30] Pair[first=30, second=age] Pair[first=x, second=x] Pair[first=7, second=7] Integer
intervalles : Range[low=13, high=19] true false true false
```

**📖 La leçon : une classe générique.** Une lettre entre chevrons après le nom de la classe (souvent `T`, pour « type ») est un **paramètre de type** : un trou, rempli par celui qui utilise la classe :

```java
class Boite<T> {
    private T contenu;
    void ranger(T x) { contenu = x; }
    T sortir() { return contenu; }
}
Boite<String> b = new Boite<>();
b.ranger("kiwi");
String s = b.sortir();          // pas de cast : Java sait que c'est un String

record Duo<A, B>(A gauche, B droite) { }       // plusieurs paramètres de type
new Duo<>("age", 8)                            // Duo[gauche=age, droite=8]
```

**📖 La leçon : une méthode générique.** Une méthode peut avoir **ses propres** paramètres de type, déclarés **avant** le type rendu :

```java
static <T> List<T> trois(T x) { return List.of(x, x, x); }
trois("x")                     // [x, x, x] : Java devine T = String
Atelier.<Integer>trois(7)      // [7, 7, 7] : T donné explicitement
```

**📖 La leçon : borner un paramètre de type.** `T extends Comparable<T>` veut dire « n'importe quel type, **pourvu qu'il sache se comparer** ». On peut alors appeler `compareTo` sur un `T` :

```java
static <T extends Comparable<T>> T plusGrand(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}
plusGrand("kiwi", "pomme")     // "pomme"
plusGrand(3, 9)                // 9
```

**👉 À toi :**

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

**📖 Rappel :** un `Comparator` passé en paramètre (projet 1, étape 2). Le tas et la fusion : chapitre 8, projet 3, et chapitre 5, projet 6. `numbers.forEach(tas::push)` appelle `push` pour chaque élément (chapitre 8 : référence de méthode).

**👉 À toi :**

- **`class Heap<T>`** :
  - `private final List<T> items` et `private final Comparator<? super T> order` ;
  - `push`, `pop`, `size`, `isEmpty`.
  - **Les données :** `numbers`, une `ArrayList<Integer>` remplie depuis `Data.NUMBERS`, et `words = Arrays.asList(Data.WORDS)`.
  - Un `Heap<Integer>` avec `Comparator.naturalOrder()`, et un `Heap<String>` avec `comparingInt(String::length).thenComparing(Comparator.reverseOrder())`.
  - Remplis-les avec `numbers.forEach(minHeap::push)` et `words.forEach(…)`, puis vide-les avec `pop` : les valeurs sont séparées par une espace, et les deux tas par ` | `.
- **Dans `final class Algos`**, avec un constructeur privé :
  - `public static <T> List<T> mergeSort(List<T> list, Comparator<? super T> cmp)`, récursif et stable (`<= 0`) ;
  - `public static <T> int binarySearch(List<? extends T> sorted, T key, Comparator<? super T> cmp)`, qui rend l'indice, ou `-(lo + 1)` si la clé est absente (la même convention que `Collections.binarySearch`).
  - Trie `words` par `comparingInt(String::length)`, puis `numbers` par `naturalOrder()`. Cherche 25, puis 20, dans les nombres triés.

### ☐ Étape 3 — Bornes et jokers

```
bornes : max 42 set, argMax collection, somme 138.0 4.0
jokers : [1, 4, 9, 16] [1, 4, 9, 16, texte] | 5 elements : Integer Integer Integer Integer String | [ab, ab, ab]
```

**📖 La leçon : les jokers `?`.** Une `List<Integer>` n'est **pas** une `List<Number>`, même si un `Integer` est un `Number`. Pour écrire une méthode qui accepte plusieurs sortes de listes, on utilise le joker `?` :
- `List<? extends Number>` : une liste de `Number` **ou d'un sous-type** (`Integer`, `Double`…). On peut y **lire** des `Number` ;
- `List<? super Integer>` : une liste d'`Integer` **ou d'un super-type** (`Number`, `Object`). On peut y **ajouter** des `Integer` ;
- `List<?>` : une liste de n'importe quoi.

```java
static double total(List<? extends Number> nombres) {
    double s = 0;
    for (Number n : nombres) s += n.doubleValue();
    return s;
}
total(List.of(1, 2.5))        // 3.5 : une liste qui mélange Integer et Double

static void remplir(List<? super Integer> cible) {
    cible.add(1);
    cible.add(2);
}
List<Number> nums = new ArrayList<>();
remplir(nums);                // [1, 2]
```

**Le moyen mnémotechnique PECS** : *Producer Extends, Consumer Super*. Une collection qui te **fournit** des valeurs : `extends`. Une collection qui **reçoit** tes valeurs : `super`.

**👉 À toi :**

- **Dans `Algos`** :
  - `<T extends Comparable<? super T>> T max(Collection<? extends T> values)` ;
  - `<K, V extends Comparable<? super V>> K argMax(Map<K, V> map)` ;
  - `double sum(Collection<? extends Number>)` ;
  - `void fillSquares(List<? super Integer> target, int n)` ;
  - `<T> void copy(List<? super T> dst, List<? extends T> src)` ;
  - `String describe(Collection<?> c)`, qui rend `N elements :` suivi du nom simple de la classe de chaque élément (précédé d'une espace) ;
  - `<T> List<T> repeat(T value, int times)`.
- **Les appels :**
  - `max(numbers)` et `max(words)` ;
  - `argMax` d'une `TreeMap` mot → longueur, remplie depuis `words` ;
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

**📖 La leçon : hériter d'une collection.** Une classe peut **étendre** une collection générique (`class Cache<K, V> extends LinkedHashMap<K, V>`) et redéfinir une de ses méthodes `protected` pour changer son comportement.

**📖 La leçon : l'effacement.** Les types entre chevrons n'existent que pour `javac`, qui les vérifie puis les **efface**. À l'exécution, une `ArrayList<String>` et une `ArrayList<Integer>` sont de la même classe. Les expériences de l'étape te montrent ce que cela interdit.

**👉 À toi :**

- **`class Cache<K, V> extends LinkedHashMap<K, V>`** :
  - le constructeur appelle `super(16, 0.75f, true)` : l'ordre devient celui des **accès** ;
  - `removeEldestEntry` rend `size() > capacity` ;
  - `V load(K key, Function<? super K, ? extends V> loader)` :
    - si `containsKey(key)` : un succès, et `return get(key)`. C'est `get` qui remet la clé en dernier (`containsKey` ne compte pas comme un accès) ;
    - sinon : un échec, `loader.apply(key)`, puis `put` ;
  - `stats()` rend `N succes, M echecs`.
  - Crée `new Cache<>(Data.CAPACITY)`. Pour chaque accès de `Data.ACCESSES`, appelle `load(key, String::length)`, et ajoute `keySet()` à la trace (séparés par une espace). Termine par ` -> ` et `stats()`.
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
