# Projet 5 — Les classements (`Comparable`, `Comparator`, collections navigables)

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

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

---

## Tableau de bord

### ☐ Étape 1 — Ordre naturel et comparateurs

```
ordre naturel : [Adam, Bob, Emma, Hugo, Ines, Lea, Noah, Zoe]
classement (score desc, age, nom) : [Hugo, Ines, Bob, Zoe, Lea, Noah, Adam, Emma]
par equipe : [Hugo, Noah, Zoe, Ines, Lea, Emma, Bob, Adam] ; par bonus (null a la fin) : [Zoe, Lea, Noah, Ines, Bob, Adam, Emma, Hugo]
rangs : Hugo=1/1 Ines=1/1 Bob=1/1 Zoe=4/2 Lea=4/2 Noah=4/2 Adam=7/3 Emma=8/4
```
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
- **Le piège :** `new TreeSet<>(Comparator.comparingInt(Player::score))`, puis `addAll(players)`.
  - **Question :** pourquoi n'en reste-t-il que 4 ? Lesquels sont gardés ?
- **`NavigableMap<Integer, List<String>> byScore = new TreeMap<>()`**, remplie avec `computeIfAbsent`. Appelle chaque méthode de navigation **dans l'ordre affiché**.
- **`NavigableSet<Integer> ages`** : la même chose, puis affiche l'ensemble **après** `pollFirst()`.
  - L'ensemble est affiché en début de ligne, donc avant `pollFirst` : la concaténation s'évalue de gauche à droite.

### ☐ Étape 3 — Intervalles et médiane

```
intervalles tries [[1,3], [2,6], [5,7], [8,10], [9,12], [15,18], [17,20]] -> fusion [[1,7], [8,12], [15,20]] ; planning max [[1,3], [5,7], [8,10], [15,18]]
medianes : 5.0 10.0 5.0 4.0 5.0 6.0 7.0 7.5 8.0 7.5
```
- **`record Interval(int start, int end) implements Comparable<Interval>`** : le début, puis la fin ; `toString()` = `[a,b]`.
- **La fusion :** trie (`Collections.sort`). Si le dernier intervalle fusionné chevauche (`end >= start`), retire-le, puis ajoute l'union.
- **Le planning :** trie une copie **par fin** (`comparingInt(Interval::end)`), et garde chaque intervalle qui commence **après** la fin du dernier gardé.
  - **Question :** pourquoi trier par fin, et non par début ?
- **La médiane glissante :** `low`, un tas **max** (`new PriorityQueue<>(Collections.reverseOrder())`), et `high`, un tas min.
  1. Range x dans `low` s'il est ≤ `low.peek()` (ou si `low` est vide), sinon dans `high` ;
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
