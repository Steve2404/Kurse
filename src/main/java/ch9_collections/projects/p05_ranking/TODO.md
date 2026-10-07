# Projet 5 — Les classements (`Comparable`, `Comparator`, collections navigables)

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 9) :**
- **`Comparable<T>`** : l'ordre naturel (`compareTo`), cohérent avec `equals` ;
- **`Comparator`** :
  - `comparingInt`, `comparing(clé, comparateur)` ;
  - `thenComparing`, `reversed`, `naturalOrder`, `reverseOrder`, `nullsLast` ;
- `Collections.sort` et `List.sort` ;
- le **piège du `TreeSet`** avec un comparateur : deux éléments dont `compare == 0` sont considérés **égaux**, et le second est refusé ;
- **`NavigableMap`** (`TreeMap`) : `floorKey`, `ceilingKey`, `lowerKey`, `higherKey`, `firstEntry`, `lastKey`, `headMap`, `tailMap`, `subMap`, `descendingMap` ;
- **`NavigableSet`** (`TreeSet`) : `first`, `last`, `floor`, `ceiling`, `headSet`, `tailSet(…, false)`, `pollFirst` ;
- **`PriorityQueue`** en tas max : `new PriorityQueue<>(Collections.reverseOrder())`.

Côté algorithmes :
- les **rangs** « compétition » et « dense » ;
- la **fusion d'intervalles** ;
- la **planification gloutonne** (tri par fin) ;
- la **médiane glissante** avec deux tas.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch9_collections.projects.p05_ranking` :
- les records `Player` et `Interval` (tous deux `Comparable`) ;
- **`Ranking`** (le `main`).

**Règle du crescendo :** chapitres 1 à 9. Pas de stream.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch9-p05 -sourcepath src/main/java src/main/java/ch9_collections/projects/p05_ranking/Ranking.java
java "-Duser.language=fr" -cp build/ch9-p05 ch9_collections.projects.p05_ranking.Ranking
```

---

## Tableau de bord

### ☐ Étape 1 — Ordre naturel et comparateurs

```
ordre naturel : [Adam, Bob, Emma, Hugo, Ines, Lea, Noah, Zoe]
classement (score desc, age, nom) : [Hugo, Ines, Bob, Zoe, Lea, Noah, Adam, Emma]
par equipe : [Hugo, Noah, Zoe, Ines, Lea, Emma, Bob, Adam] ; par bonus (null a la fin) : [Zoe, Lea, Noah, Ines, Bob, Adam, Emma, Hugo]
rangs : Hugo=1/1 Ines=1/1 Bob=1/1 Zoe=4/2 Lea=4/2 Noah=4/2 Adam=7/3 Emma=8/4
```

**📖 La leçon : `Comparable`, l'ordre naturel d'une classe.** Une classe qui réalise `Comparable<Elle-même>` dit comment **ses** objets se comparent, avec `compareTo`. `Collections.sort(liste)` et `TreeSet` utilisent alors cet ordre sans qu'on leur donne de comparateur :

```java
record Note(String eleve, int valeur) implements Comparable<Note> {
    @Override
    public int compareTo(Note autre) { return Integer.compare(valeur, autre.valeur); }
}
Collections.sort(notes);       // de la plus petite à la plus grande valeur
```

**`Comparable`** = **un** ordre, écrit dans la classe. **`Comparator`** = autant d'ordres qu'on veut, écrits à côté (projet 1, étape 2).

**📖 La leçon : les `null` dans un tri.** `Comparator.nullsLast(…)` place les `null` à la fin, et trie les autres avec le comparateur donné :

```java
List<String> v = new ArrayList<>(Arrays.asList("b", null, "a"));
v.sort(Comparator.nullsLast(Comparator.naturalOrder()));   // [a, b, null]
```

**👉 À toi :**

- **`record Player(String name, String team, int score, int age, Integer bonus) implements Comparable<Player>`** :
  - `parse` (un bonus `-` devient `null`) ;
  - `compareTo` compare les **noms** ;
  - `toString()` rend le nom.
- **L'ordre naturel :** une copie triée par `Collections.sort`.
- **Le classement :** `board = comparingInt(Player::score).reversed().thenComparing(Player::age).thenComparing(Comparator.naturalOrder())`, puis `players.sort(board)`. La liste `players` reste ainsi triée pour la suite.
- **Les autres ordres :**
  - `byTeam` : `comparing(Player::team)`, puis `.thenComparing(Player::score, Comparator.reverseOrder())`, puis `.thenComparing(Player::name)` ;
  - `byBonus` : `comparing(Player::bonus, Comparator.nullsLast(Comparator.reverseOrder()))`, puis `.thenComparing(Player::name)`.
- **Les rangs**, dans l'ordre du classement :
  - le rang « compétition » vaut i + 1 à chaque nouveau score ;
  - le rang « dense » augmente de 1 à chaque nouveau score.

### ☐ Étape 2 — Le piège et la navigation

```
TreeSet par score : [Emma, Adam, Zoe, Hugo] (4 sur 8)
navigation : floorKey(1300) 1200, ceilingKey(1300) 1500, lowerKey(1200) 980, higherKey(1500) null, firstEntry 870=[Emma], lastKey 1500
vues : headMap(1200) {870=[Emma], 980=[Adam]}, tailMap(1200) [1200, 1500], subMap(900, 1300) [980, 1200], descending [1500, 1200, 980, 870]
ages : [25, 29, 31, 37, 42], first 25, last 42, floor(30) 29, ceiling(30) 31, headSet(31) [25, 29], tailSet(31, false) [37, 42], pollFirst 25 -> [29, 31, 37, 42]
```

**📖 La leçon : naviguer dans une `TreeMap` ou un `TreeSet`.** Comme leurs éléments sont triés, ils savent répondre à « le plus proche de… » :

```java
TreeMap<Integer, String> paliers = new TreeMap<>(Map.of(0, "bronze", 100, "argent", 500, "or"));
paliers.floorKey(250)          // 100  : la plus grande clé <= 250
paliers.ceilingKey(250)        // 500  : la plus petite clé >= 250
paliers.lowerKey(100)          // 0    : strictement plus petite
paliers.higherKey(500)         // null : aucune
paliers.headMap(500)           // {0=bronze, 100=argent} : les clés < 500
paliers.tailMap(100).keySet()  // [100, 500] : les clés >= 100
paliers.descendingMap().keySet()   // [500, 100, 0]

TreeSet<Integer> t = new TreeSet<>(List.of(10, 20, 30, 40));
t.floor(25)                    // 20
t.ceiling(25)                  // 30
t.headSet(30)                  // [10, 20]
t.subSet(15, 35)               // [20, 30]
t.pollFirst()                  // 10, et le retire
```

Ces méthodes existent grâce aux interfaces `NavigableMap` et `NavigableSet`.

**📖 Rappel :** un `TreeSet` construit avec un `Comparator` range ses éléments selon **ce** comparateur. L'étape te fait découvrir une conséquence surprenante.

**👉 À toi :**

- **Le piège :** `new TreeSet<>(Comparator.comparingInt(Player::score))`, puis `addAll(players)`.
  - **Question :** pourquoi n'en reste-t-il que 4 ? Lesquels sont gardés ?
- **`NavigableMap<Integer, List<String>> byScore = new TreeMap<>()`** : score → noms, remplie avec `computeIfAbsent` en parcourant `players` (dans l'ordre du classement). Appelle chaque méthode de navigation **dans l'ordre affiché**.
  - Sur la ligne `vues`, `headMap(1200)` est affichée entière ; pour `tailMap`, `subMap` et `descendingMap`, n'affiche que `.keySet()`.
- **`NavigableSet<Integer> ages`** (un `TreeSet` des âges des joueurs) : appelle les méthodes dans l'ordre affiché, puis affiche l'ensemble **après** `pollFirst()`.
  - L'ensemble est affiché en début de ligne, donc avant `pollFirst` : la concaténation s'évalue de gauche à droite.

### ☐ Étape 3 — Intervalles et médiane

```
intervalles tries [[1,3], [2,6], [5,7], [8,10], [9,12], [15,18], [17,20]] -> fusion [[1,7], [8,12], [15,20]] ; planning max [[1,3], [5,7], [8,10], [15,18]]
medianes : 5.0 10.0 5.0 4.0 5.0 6.0 7.0 7.5 8.0 7.5
```

**📖 La leçon : un tas « max ».** Par défaut, une `PriorityQueue` sort le **plus petit**. Avec `Collections.reverseOrder()`, elle sort le **plus grand** :

```java
PriorityQueue<Integer> tasMax = new PriorityQueue<>(Collections.reverseOrder());
tasMax.addAll(List.of(5, 1, 4, 2));
tasMax.poll()      // 5
```

**👉 À toi :**

- **`record Interval(int start, int end) implements Comparable<Interval>`** : le début, puis la fin ; `toString()` = `[a,b]`. La liste est remplie depuis `Data.INTERVALS`.
- **La fusion :** trie (`Collections.sort`). Si le dernier intervalle fusionné chevauche (`end >= start`), retire-le, puis ajoute l'union.
- **Le planning :** trie une copie **par fin** (`comparingInt(Interval::end)`), et garde chaque intervalle qui commence **après** la fin du dernier gardé.
  - **Question :** pourquoi trier par fin, et non par début ?
- **La médiane glissante**, sur chaque valeur de `Data.STREAM` : `low`, un tas **max** (`new PriorityQueue<>(Collections.reverseOrder())`), et `high`, un tas min.
  1. Range x dans `low` s'il est ≤ `low.peek()` (ou si `low` est vide), sinon dans `high` (`offer`) ;
  2. rééquilibre pour que `low` ait autant d'éléments que `high`, ou un de plus ;
  3. la médiane vaut `low.peek()`, ou la moyenne des deux sommets.

---

## Checklist (vérifiée par `Check`)

- `Data.PLAYERS` et `Data.INTERVALS` ;
- `implements Comparable<Player>`, `public int compareTo(Player`, `implements Comparable<Interval>` ;
- `Collections.sort(`, `comparingInt(`, `reversed()`, `thenComparing(`, `naturalOrder()`, `nullsLast(` ;
- `NavigableMap<Integer, List<String>>` et ses méthodes `floorKey`, `ceilingKey`, `lowerKey`, `higherKey`, `firstEntry`, `headMap`, `tailMap`, `subMap`, `descendingMap` ;
- `NavigableSet<Integer>` et ses méthodes `floor`, `ceiling`, `headSet`, `tailSet`, `pollFirst` ;
- `new PriorityQueue<>(Collections.reverseOrder())`.

---

## Sortie attendue complète

```
ordre naturel : [Adam, Bob, Emma, Hugo, Ines, Lea, Noah, Zoe]
classement (score desc, age, nom) : [Hugo, Ines, Bob, Zoe, Lea, Noah, Adam, Emma]
par equipe : [Hugo, Noah, Zoe, Ines, Lea, Emma, Bob, Adam] ; par bonus (null a la fin) : [Zoe, Lea, Noah, Ines, Bob, Adam, Emma, Hugo]
rangs : Hugo=1/1 Ines=1/1 Bob=1/1 Zoe=4/2 Lea=4/2 Noah=4/2 Adam=7/3 Emma=8/4
TreeSet par score : [Emma, Adam, Zoe, Hugo] (4 sur 8)
navigation : floorKey(1300) 1200, ceilingKey(1300) 1500, lowerKey(1200) 980, higherKey(1500) null, firstEntry 870=[Emma], lastKey 1500
vues : headMap(1200) {870=[Emma], 980=[Adam]}, tailMap(1200) [1200, 1500], subMap(900, 1300) [980, 1200], descending [1500, 1200, 980, 870]
ages : [25, 29, 31, 37, 42], first 25, last 42, floor(30) 29, ceiling(30) 31, headSet(31) [25, 29], tailSet(31, false) [37, 42], pollFirst 25 -> [29, 31, 37, 42]
intervalles tries [[1,3], [2,6], [5,7], [8,10], [9,12], [15,18], [17,20]] -> fusion [[1,7], [8,12], [15,20]] ; planning max [[1,3], [5,7], [8,10], [15,18]]
medianes : 5.0 10.0 5.0 4.0 5.0 6.0 7.0 7.5 8.0 7.5
```
