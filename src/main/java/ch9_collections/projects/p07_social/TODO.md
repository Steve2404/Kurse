# Projet 7 (CAPSTONE) — Le réseau social

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées :** tout le chapitre 9 :
- un graphe d'amitiés **`Map<String, Set<String>>`** ;
- une classe **générique bornée** `UnionFind<T extends Comparable<T>>`, adossée à une `Map<T, T>` ;
- des **records** :
  - `Post`, avec un `Set` copié en **immuable** (`Set.copyOf`) ;
  - `Cursor`, imbriqué ;
- `merge`, `computeIfAbsent`, `retainAll`, `removeAll` ;
- **`Queue`** (`ArrayDeque`) et **`PriorityQueue`**, avec des comparateurs sur des records et sur `Map.Entry` ;
- `Map.Entry.<String, Integer>comparingByValue().reversed()` : un **argument de type explicite** ;
- `subList`.

Côté algorithmes :
- les **suggestions d'amis** (amis d'amis, classés par nombre d'amis communs) ;
- les **degrés de séparation** (parcours en largeur) ;
- le **fil d'actualité** par **fusion de k listes triées** avec un tas, en O(n log k) ;
- les **tags tendance** (top-k) ;
- les **communautés** (Union-Find avec compression de chemin).

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch9_collections.projects.p07_social` :
- `Post`, `UnionFind` ;
- **`Social`** (le `main`, avec le record `Cursor` imbriqué).

**Règle du crescendo :** chapitres 1 à 9. Pas de stream.

---

## Tableau de bord

### ☐ Étape 1 — Le graphe et les communautés

```
ana : [bob, chloe, gina] ; 10 membres ; communautes [[ana, bob, chloe, dan, eve, fred, gina], [hugo, ines, jade]] (8 unions)
```
- **`class UnionFind<T extends Comparable<T>>`** :
  - une `Map<T, T> parent` ;
  - `add` (`putIfAbsent(x, x)`) ;
  - `find`, récursif avec **compression de chemin** ;
  - `union`, qui rend `false` si a et b sont déjà dans le même groupe, et compte les vraies unions ;
  - `groups()` : regroupe par racine dans des `TreeSet`, puis rend les groupes triés par leur plus petit élément (via une `TreeMap`).
- **Dans `main`**, pour chaque amitié de `Data.FRIENDS` :
  - `computeIfAbsent(a, k -> new TreeSet<>()).add(b)`, et le symétrique, dans une `TreeMap` ;
  - ajoute les deux à l'Union-Find, puis `union`.
- Affiche les amis de `Data.ME`, le nombre de membres, les communautés et le nombre d'unions.

### ☐ Étape 2 — Suggestions et séparation

```
suggestions : [eve=2, dan=1, fred=1] ; amis communs bob/chloe [ana]
degres : {ana=0, bob=1, chloe=1, dan=2, eve=2, fred=2, gina=1} ; injoignables [hugo, ines, jade]
```
- **Les suggestions :** pour chaque ami f de `ME`, et chaque ami ff de f, qui n'est ni `ME` ni déjà son ami : `mutual.merge(ff, 1, Integer::sum)`.
  - Copie l'`entrySet()` dans une liste, triée par `Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey())`.
- **Les amis communs** de bob et chloe : une copie, puis `retainAll`.
- **Les degrés :** un parcours en largeur depuis `ME`, avec une `Queue<String>` (`ArrayDeque`, `add` / `remove`) et une `TreeMap` des degrés.
- **Les injoignables :** une copie de toutes les clés, puis `removeAll` de celles atteintes.

### ☐ Étape 3 — Le fil d'actualité

```
fil de ana : [#11(bob 18h), #6(chloe 15h), #4(bob 14h), #8(ana 12h), #2(chloe 10h)]
```
- **`record Post(int id, String author, int time, int likes, Set<String> tags)`** :
  - le constructeur compact fait `tags = Set.copyOf(tags)` ;
  - `parse`, où les tags sont découpés sur `,` dans un `TreeSet` ;
  - `toString()` = `#id(auteur heureh)`.
- Range les publications par auteur (`computeIfAbsent`). Trie chaque liste par heure **décroissante** (`newestFirst`).
- **La fusion des k listes** de `ME` et de ses amis :
  1. un `record Cursor(List<Post> posts, int index)`, avec `current()` ;
  2. une `PriorityQueue<Cursor>` triée par `Comparator.comparing(Cursor::current, newestFirst)`, qui reçoit un curseur par liste non vide ;
  3. `poll` le plus récent, ajoute-le au fil, puis remets le curseur avancé d'un cran s'il en reste.
  - On s'arrête à `Data.FEED` publications.
  - **Question :** pourquoi ne pas tout concaténer et trier ? Compare les complexités.

### ☐ Étape 4 — Tendances

```
likes par tag : {collections=21, cuisine=80, java=41, map=5, set=3, sport=67, voyage=105} ; tendances [voyage=105, cuisine=80, sport=67]
plus connectes : [ana, bob, chloe] ; copyOf d'un set immuable = meme objet true [collections, java]
```
- **Les likes par tag :** `TreeMap` et `merge(tag, likes, Integer::sum)`.
- **Les tendances :** un tas min de taille `Data.TRENDING` (`PriorityQueue<Map.Entry<String, Integer>>` avec `comparingByValue()`). Copie-le, puis trie par valeur décroissante.
- **Les plus connectés :**
  - trie les membres par nombre d'amis décroissant, puis par nom : `Comparator.comparing((String m) -> friends.get(m).size()).reversed().thenComparing(Comparator.naturalOrder())` ;
  - affiche `subList(0, 3)`.
- **L'immuabilité :** avec `tags = all.get(0).tags()`, affiche `Set.copyOf(tags) == tags` (copier un `Set` déjà immuable ne crée pas de copie), puis les tags triés.
- **Expérience :** `all.get(0).tags().add("x")` : que se passe-t-il ?

---

## Checklist (vérifiée par `Check`)

- `Data.FRIENDS` et `Data.POSTS` ;
- `class UnionFind<T extends Comparable<T>>`, `Map<T, T> parent` ;
- `record Cursor(`, `Set.copyOf(` ;
- `Map<String, Set<String>>`, `merge(` ;
- `Map.Entry.<String, Integer>comparingByValue().reversed()` ;
- `retainAll(`, `removeAll(` ;
- `Queue<String> queue = new ArrayDeque<>()`, `PriorityQueue<Cursor>`, `PriorityQueue<Map.Entry<String, Integer>>` ;
- `subList(`.

---

## Sortie attendue complète

```
ana : [bob, chloe, gina] ; 10 membres ; communautes [[ana, bob, chloe, dan, eve, fred, gina], [hugo, ines, jade]] (8 unions)
suggestions : [eve=2, dan=1, fred=1] ; amis communs bob/chloe [ana]
degres : {ana=0, bob=1, chloe=1, dan=2, eve=2, fred=2, gina=1} ; injoignables [hugo, ines, jade]
fil de ana : [#11(bob 18h), #6(chloe 15h), #4(bob 14h), #8(ana 12h), #2(chloe 10h)]
likes par tag : {collections=21, cuisine=80, java=41, map=5, set=3, sport=67, voyage=105} ; tendances [voyage=105, cuisine=80, sport=67]
plus connectes : [ana, bob, chloe] ; copyOf d'un set immuable = meme objet true [collections, java]
```
