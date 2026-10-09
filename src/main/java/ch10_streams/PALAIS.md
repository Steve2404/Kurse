# 🏠 Palais mental — chapitre 10 : la chambre 3, stations 7 à 12

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Les stations 1 à 6 de la chambre 3 gardent le chapitre 9.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 7. 🚪 L'armoire — `Optional`

- **Image :** dans l'armoire, une **boîte cadeau** qui est **peut-être vide**. `Optional.of(null)` : la boîte **explose** en usine (`NullPointerException`) ; pour un objet peut-être `null`, on prend `ofNullable`. Tu fais `get()` sur une boîte vide : un **diable** en sort (`NoSuchElementException`). `orElse(x)` prépare le cadeau de secours **toujours**, même si la boîte est pleine ; `orElseGet(…)` ne le prépare **que si** elle est vide.
- **À retenir :**
  - `Optional.of(v)` (`v` non `null`), `ofNullable(v)`, `empty()` ;
  - `get()` / `orElseThrow()` sur un vide → `NoSuchElementException` ;
  - `orElse(valeur)` évalue la valeur **toujours** ; `orElseGet(Supplier)` seulement si vide ; `orElseThrow(Supplier)` ;
  - `isPresent()`, `isEmpty()`, `ifPresent(Consumer)`, `ifPresentOrElse`, `map`, `filter`, `flatMap`.
- **Mon image :** …

### 8. 🪞 Le miroir — le pipeline et la paresse

- **Image :** dans le miroir, une **chaîne de montage** : une **source**, des **ouvriers** intermédiaires, et au bout le **patron** (l'opération terminale). Tant que le patron n'est pas là, les ouvriers **dorment** : rien ne se passe. Quand il arrive, chaque objet traverse **toute la chaîne, un par un**. Une fois la chaîne finie, elle est **démontée** : la relancer → `IllegalStateException`. Une source infinie (`generate`, `iterate`) ne s'arrête qu'avec un **patron pressé** (`limit`, `findFirst`, `anyMatch`).
- **À retenir :**
  - source → opérations intermédiaires (**paresseuses**) → **une** opération terminale ;
  - sans opération terminale, **rien** ne s'exécute ;
  - les éléments passent **un par un** dans tout le pipeline (pas étape par étape) ;
  - un stream ne se réutilise pas → `IllegalStateException` ;
  - `Stream.generate(s)`, `Stream.iterate(graine, f)` et `iterate(graine, condition, f)` ; un stream infini sans court-circuit ne finit jamais.
- **Mon image :** …

### 9. 🖥️ Le bureau — les opérations terminales

- **Image :** sur le bureau, le **patron** a une rangée de **tampons** : `count` (un `long`), `min` et `max` (une **boîte `Optional`**, avec un arbitre `Comparator`), `findFirst` et `findAny` (une boîte aussi), les trois juges **`allMatch`, `anyMatch`, `noneMatch`**, `forEach`, `collect`. Et le gros tampon **`reduce`** : avec une **valeur de départ**, il rend une valeur ; **sans** valeur de départ, il rend une **boîte `Optional`**.
- **À retenir :**
  - `count()` → `long` ; `min(c)` / `max(c)` / `findFirst()` / `findAny()` → `Optional` ;
  - `allMatch`, `anyMatch`, `noneMatch` → `boolean` (court-circuit ; sur un stream **vide**, `allMatch` et `noneMatch` donnent `true`) ;
  - `reduce(identite, accumulateur)` → `T` ; `reduce(accumulateur)` → `Optional<T>` ; `reduce(identite, accumulateur, combineur)` pour changer de type ;
  - `forEach` ne rend rien ; `collect` et `toList()` rendent une collection.
- **Mon image :** …

### 10. 🪑 La chaise — les opérations intermédiaires

- **Image :** autour de la chaise, des **ouvriers** : le **tamis** `filter`, le **videur** `distinct`, le **portier** `limit` qui ferme après N, le **sauteur** `skip`, le **peintre** `map`, le **déballeur** `flatMap` qui ouvre chaque carton et vide son contenu sur le tapis, le **rangeur** `sorted` (qui doit **tout voir** avant de laisser passer quelqu'un), et le **curieux** `peek` qui regarde sans toucher.
- **À retenir :**
  - `filter(Predicate)`, `map(Function)`, `flatMap(Function vers un Stream)`, `distinct()`, `limit(n)`, `skip(n)`, `peek(Consumer)` ;
  - `sorted()` (ordre naturel) et `sorted(Comparator)` : il attend **tous** les éléments (jamais sur un stream infini) ;
  - `limit` court-circuite : avec `peek`, on voit que les éléments au-delà ne passent pas.
- **Mon image :** …

### 11. 🪟 La fenêtre — les streams primitifs

- **Image :** par la fenêtre, un **escalier `IntStream.range(1, 5)`** : tu montes 1, 2, 3, 4, et la marche **5 n'existe pas**. L'escalier `rangeClosed(1, 5)` a **sa marche 5**. En bas, une **calculatrice** : `sum()` rend un nombre tout de suite, mais `average()` rend une **boîte `OptionalDouble`** (l'escalier peut être vide), `max()` une `OptionalInt`. Des **ascenseurs** passent d'un monde à l'autre : `mapToInt`, `mapToObj`, `boxed`.
- **À retenir :**
  - `IntStream.range(a, b)` exclut `b`, `rangeClosed(a, b)` l'inclut ;
  - `sum()` → `int` (`long`, `double`) ; `average()` → `OptionalDouble` ; `max()` / `min()` → `OptionalInt` ;
  - `summaryStatistics()` : min, max, moyenne, somme, nombre en un seul passage ;
  - `mapToInt`, `mapToObj`, `boxed`, `asDoubleStream` ; `getAsInt()` sur un `OptionalInt`.
- **Mon image :** …

### 12. 🟫 Le tapis — les `Collectors`

- **Image :** sur le tapis, des **paniers** qui ramassent la fin de la chaîne. `joining(", ")` **enfile des perles**. `groupingBy` **trie le linge** dans des paniers étiquetés (une `Map` de **listes**) ; avec `counting()`, chaque panier affiche un **`Long`**. `partitioningBy` n'a **que deux paniers**, `true` et `false`, **toujours présents même vides**. `toMap` crie **« doublon ! »** (`IllegalStateException`) si deux objets veulent la même étiquette, sauf si tu lui donnes une **règle de fusion**.
- **À retenir :**
  - `joining`, `toList`, `toSet`, `counting` (→ `Long`), `averagingInt` (→ `Double`), `summingInt`, `minBy`, `maxBy`, `mapping` ;
  - `groupingBy(f)` → `Map<K, List<T>>` ; `groupingBy(f, TreeMap::new, aval)` pour choisir la `Map` ;
  - `partitioningBy(p)` → `Map<Boolean, …>` avec **toujours** les deux clés ;
  - `toMap(k, v)` : clé en double → `IllegalStateException` ; `toMap(k, v, fusion)` ;
  - `Stream.toList()` rend une liste **non modifiable**.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 7 : quelle est la différence entre `orElse` et `orElseGet` ?
2. Station 8 : que se passe-t-il si on oublie l'opération terminale ?
3. Station 9 : que rend `reduce` sans valeur de départ ?
4. Station 10 : pourquoi `sorted()` sur un stream infini ne finit-il jamais ?
5. Station 11 : que contient `IntStream.range(1, 4)` ?
6. Station 12 : quelles clés a toujours le résultat de `partitioningBy` ?
