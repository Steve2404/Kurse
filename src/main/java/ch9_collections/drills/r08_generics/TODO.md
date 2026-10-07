# Drill de rappel 8 — Déclarer des génériques

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

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
  - **`class RawContainer implements Container`** (type **brut**) : `content()` rend `"brut"`.
  - **`class Trio<T> implements Iterable<T>`**, construite avec trois `T` gardés dans une `List.of`, dont `iterator()` rend l'itérateur de cette liste.
- Dans `Box<T>`, ajoute aussi `<T> T echo(T other)`, qui rend `other`. Ce `<T>` **masque** celui de la classe.
- Dans `Recall08`, trois méthodes `static` génériques :
  - `<T> T first(List<T>)` ;
  - `<T extends Comparable<T>> T larger(T a, T b)`, qui rend a si a ≥ b ;
  - `<K, V> String entry(K, V)`, qui rend `"k=v"` ;
  - `<T extends Number & Comparable<T>> double spread(T a, T b)`, qui rend le plus grand moins le plus petit (en `doubleValue()`).

**Les notions de ce drill ont été apprises dans :** projet 4 (étapes 1, 2 et 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r08_generics` → **New** → **Java Class** → `Recall08`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall08`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall08`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
- ☐ **D07.** `spread(3, 10)`, puis `spread(2.5, 1.0)`.
  → `D07 : 7.0 1.5`
- ☐ **D08.** `var guess = new ArrayList<>()`, puis `add(1)` et `add("x")`. Affiche `guess`, `s.echo(5)`, puis `s.echo("ok").length()`.
  → `D08 : [1, x] 5 2`
- ☐ **D09.** Les types bruts :
  1. `List rawList = new ArrayList()`, puis `rawList.add(7)` ;
  2. `List<String> typed = rawList` ;
  3. `Object polluted = typed.get(0)` ;
  4. `Container rawContainer = new RawContainer()`.

  Affiche le nom simple de la classe de `polluted`, `rawContainer.content()`, puis `rawContainer.describe()`.
  → `D09 : Integer brut contient String`
- ☐ **D10.** Une boucle for-each sur `new Trio<>("a", "b", "c")` concatène les éléments.
  → `D10 : abc`

## Expériences (hors sortie attendue)

Pour chaque ligne, dis si elle **compile** :

1. `larger(new Object(), new Object())` ;
2. dans `Box` : `T t = new T();` et `T[] arr = new T[2];` ;
3. `static T cached;` dans `Box<T>` ;
4. `Box<int> b;` ;
5. `if (s instanceof Box<String>) { }`, puis la même ligne avec `Box<?>` ;
6. deux surcharges `void f(Box<String>)` et `void f(Box<Integer>)` ;
7. `String bad = typed.get(0);` après D09 : compile-t-il ? Que se passe-t-il à l'exécution, et à quelle ligne ?
8. `spread("a", "b")` ;
9. `<T extends Comparable<T> & Number>` (l'interface avant la classe).

## Sortie attendue complète

```
D01 : java 43 4
D02 : a 7 pomme 9 age=30
D03 : 2.5 contient Double
D04 : Holder[key=k, value=1] Holder[key=1, value=k]
D05 : true ArrayList
D06 : 2 c
D07 : 7.0 1.5
D08 : [1, x] 5 2
D09 : Integer brut contient String
D10 : abc
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Les déclarations :**
  - `class Box<T>`, `interface Container<T>`, `record Holder<K, V>` ;
  - méthode générique : `<T>` **avant** le type de retour (`static <T> T first(…)`) ;
  - appel explicite : `Recall08.<Integer>first(…)`.
- **Implémenter une interface générique :** on fixe le type (`implements Container<Double>`), ou on le garde (`class Box2<T> implements Container<T>`).
- **Les bornes :** `<T extends Comparable<T>>`. Plusieurs bornes : `<T extends Number & Comparable<T>>` (la classe d'abord, une seule classe).
- **Implémenter une interface générique, 3 façons :** fixer le type (`implements Container<Double>`), garder le paramètre (`class Box2<T> implements Container<T>`), ou le type **brut** (`implements Container` : T devient `Object`).
- **`var` + diamant :** `var l = new ArrayList<>()` est une `ArrayList<Object>`.
- **Masquage :** `<T> T echo(T)` dans `Box<T>` déclare un **nouveau** T, sans lien avec celui de la classe.
- **Types bruts :** ils compilent avec un avertissement *unchecked*. La « pollution » explose plus tard, à la ligne qui caste (`ClassCastException`).
- **`Iterable<T>`** : `Iterator<T> iterator()` suffit pour une boucle for-each.
- **L'effacement de type :** à l'exécution, `T` devient sa borne (`Object` par défaut). D'où les interdits :
  - `new T()` et `new T[]` ;
  - un champ `static T` ;
  - `instanceof Box<String>` (mais `instanceof Box<?>` est permis) ;
  - deux surcharges qui ne diffèrent que par le paramètre de type ;
  - les types primitifs comme arguments de type.

</details>
