# Projet 2 — Les mots (`Map` et `Set`)

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 9) :**
- **`Map`** :
  - `HashMap` (aucun ordre), `LinkedHashMap` (ordre d'insertion), `TreeMap` (ordre des clés) ;
  - les méthodes `merge`, `putIfAbsent`, `computeIfAbsent`, `compute`, `computeIfPresent`, `getOrDefault`, `containsKey`, `containsValue`, `replaceAll`, `remove(key, value)` ;
  - `entrySet` et `Map.Entry` ;
  - `Map.entry` et `Map.ofEntries` ;
  - les vues `keySet` et `values`, que l'on peut **modifier** ;
- **`Set`** : `HashSet`, `LinkedHashSet`, `TreeSet` ; `add` qui rend `false` pour un doublon ; `retainAll`, `addAll`, `removeAll` ;
- **`PriorityQueue`**, avec `Map.Entry.comparingByValue` et `comparingByKey`.

Côté algorithmes :
- **fréquences** de mots ;
- **index inversé** ;
- **requêtes booléennes** ;
- **top-k** avec un tas de taille k ;
- **groupes d'anagrammes**.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch9_collections.projects.p02_words`, la classe **`Words`**.

**Règle du crescendo :** chapitres 1 à 9. Pas de stream ni de `Collectors.groupingBy` : tu fais tout avec `merge` et `computeIfAbsent`.

---

## Tableau de bord

### ☐ Étape 1 — Compter et indexer

```
mots distincts 20, java 2, python null, getOrDefault(python) 0, containsKey(set) true, containsValue(4) false
ordre d'apparition : java aime collections liste map set associe cle ...
```
- **`static List<String> tokens(String text, Set<String> stop)`** : en minuscules, découpe sur `[^a-z]+`, et ignore les chaînes vides (`isEmpty()`) et les mots de `stop`.
  - `stop` est un `HashSet` construit depuis `Arrays.asList(Data.STOP)`.
- **Pour chaque document d (numéroté à partir de 1) et chaque mot w :**
  - `freq.merge(w, 1, Integer::sum)`, dans une `HashMap` ;
  - `firstSeen.putIfAbsent(w, d)`, dans une `LinkedHashMap` ;
  - `index.computeIfAbsent(w, k -> new TreeSet<>()).add(d)`, dans une `TreeMap<String, Set<Integer>>`.
- **Ligne 1 :** la taille, `get("java")`, `get("python")`, `getOrDefault("python", 0)`, `containsKey("set")` et `containsValue(4)`.
- **Ligne 2 :** les 8 premières clés de `firstSeen`, puis ` ...`.

### ☐ Étape 2 — Top-k, index, requêtes

```
top 5 : [set=3, collections=2, java=2, liste=2, map=2]
index : collections[1, 3] java[1, 3] liste[1, 3] map[1, 2] set[1, 2, 3]
requete java & collections -> [1, 3]
...
```
- **Le top-k :**
  - le comparateur `weakestFirst` = `Map.Entry.comparingByValue()`, puis `.thenComparing(Map.Entry.comparingByKey(Comparator.reverseOrder()))` ;
  - une `PriorityQueue` avec cet ordre : ajoute chaque entrée, et dès que la taille dépasse `Data.TOP`, `poll()` (le plus faible sort) ;
  - copie le tas dans une liste, triée par `weakestFirst.reversed()`.
  - **Question :** pourquoi un tas de taille k plutôt qu'un tri complet ? Quelle complexité ?
- **L'index :** les mots présents dans **plus d'un** document, sous la forme `mot[docs]`.
- **Les requêtes** `mot1 OP mot2` de `Data.QUERIES` (découpées sur l'espace), chacune affichée sous la forme `requete <texte> -> <résultat>` :
  1. une **copie** `new TreeSet<>(index.getOrDefault(mot1, Set.of()))` ;
  2. `&` → `retainAll`, `|` → `addAll`, `-` → `removeAll`.

### ☐ Étape 3 — Anagrammes, `merge` qui supprime, les trois `Set`

```
anagrammes : [[arme, mare, rame], [chien, chine, niche]]
merge/compute : {java=20, set=12}, remove(set, 999) false, entry k=1, ofEntries {a=1, b=2}
sets : hash 4 elements, linked [set, map, java, liste], tree [java, liste, map, set], add en double false
```
- **Les anagrammes :**
  - pour chaque mot distinct (`freq.keySet()`), une `TreeMap<String, List<String>>` dont la clé est le mot aux lettres triées (`toCharArray`, `Arrays.sort`, `new String(c)`) ;
  - puis `groups.values().removeIf(g -> g.size() < 2)` (on modifie la map à travers sa **vue**) ;
  - chaque groupe est trié avec `sort(null)`.
- **La ligne `merge/compute`**, sur `stock = new TreeMap<>(Map.of("java", 2, "map", 1))`, dans cet ordre :
  1. `merge("java", -1, …)` et `merge("map", -1, …)`, où le remappage rend **`null`** si le résultat vaut 0. La clé est alors **supprimée** ;
  2. `merge("set", 5, Integer::sum)` ;
  3. `compute("java", (k, v) -> v == null ? 1 : v * 10)` ;
  4. `computeIfPresent("set", (k, v) -> v + 1)` ;
  5. `replaceAll((k, v) -> v * 2)` ;
  6. `remove("set", 999)` : il ne retire que si la valeur correspond.
  
  Puis affiche `Map.entry("k", 1)` et une `TreeMap` de `Map.ofEntries(Map.entry("b", 2), Map.entry("a", 1))`.
- **Les sets**, sur `List.of("set", "map", "java", "liste", "map")` :
  - la taille du `HashSet` (son ordre n'est pas garanti : on ne l'affiche pas) ;
  - le `LinkedHashSet` et le `TreeSet` ;
  - `new HashSet<>(sample).add("map")`.
- **Expériences :**
  - `Map.of("a", 1, "a", 2)` ;
  - `new TreeMap<String, Integer>().put(null, 1)` ;
  - `new HashMap<String, Integer>().put(null, 1)`.
  
  Lesquelles échouent, et comment ?

---

## Checklist (vérifiée par `Check`)

- `Data.DOCS` et `Data.QUERIES` ;
- `HashMap`, `LinkedHashMap`, `TreeMap` ;
- `merge`, `putIfAbsent`, `computeIfAbsent`, `getOrDefault`, `containsKey`, `containsValue`, `compute`, `computeIfPresent` ;
- `PriorityQueue<Map.Entry<String, Integer>>`, `Map.Entry.comparingByValue()`, `comparingByKey(`, `entrySet()` ;
- `retainAll`, `addAll`, `removeAll`, `values().removeIf(` ;
- `Map.entry(`, `Map.ofEntries(` ;
- `LinkedHashSet`, `TreeSet`, `HashSet`.

---

## Sortie attendue complète

```
mots distincts 20, java 2, python null, getOrDefault(python) 0, containsKey(set) true, containsValue(4) false
ordre d'apparition : java aime collections liste map set associe cle ...
top 5 : [set=3, collections=2, java=2, liste=2, map=2]
index : collections[1, 3] java[1, 3] liste[1, 3] map[1, 2] set[1, 2, 3]
requete java & collections -> [1, 3]
requete map | streams -> [1, 2, 3]
requete set - map -> [3]
requete liste & chien -> []
anagrammes : [[arme, mare, rame], [chien, chine, niche]]
merge/compute : {java=20, set=12}, remove(set, 999) false, entry k=1, ofEntries {a=1, b=2}
sets : hash 4 elements, linked [set, map, java, liste], tree [java, liste, map, set], add en double false
```
