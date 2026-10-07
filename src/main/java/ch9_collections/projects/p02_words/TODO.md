# Projet 2 — Les mots (`Map` et `Set`)

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

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

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch9-p02 -sourcepath src/main/java src/main/java/ch9_collections/projects/p02_words/Words.java
java "-Duser.language=fr" -cp build/ch9-p02 ch9_collections.projects.p02_words.Words
```

---

## Tableau de bord

### ☐ Étape 1 — Compter et indexer

```
mots distincts 20, java 2, python null, getOrDefault(python) 0, containsKey(set) true, containsValue(4) false
ordre d'apparition : java aime collections liste map set associe cle ...
```

**📖 La leçon : `Map`, un dictionnaire clé → valeur.** Une `Map` associe chaque **clé** (unique) à une **valeur**, comme un dictionnaire associe un mot à sa définition :

```java
Map<String, Integer> prix = new HashMap<>();
prix.put("pomme", 3);
prix.put("kiwi", 2);
prix.put("pomme", 4);                  // même clé : la valeur est remplacée
prix.get("pomme")                      // 4
prix.get("mangue")                     // null : clé absente
prix.getOrDefault("mangue", 0)         // 0
prix.containsKey("kiwi")               // true
prix.size()                            // 2
```

**Les trois `Map` principales** se distinguent par l'**ordre** de leurs clés :
- `HashMap` : aucun ordre garanti (la plus rapide) ;
- `LinkedHashMap` : l'ordre d'**insertion** ;
- `TreeMap` : l'ordre **trié** des clés.

**📖 La leçon : compter et regrouper en une ligne.**

```java
Map<String, Integer> compte = new TreeMap<>();
for (String f : List.of("kiwi", "pomme", "kiwi")) {
    compte.merge(f, 1, Integer::sum);       // absent : met 1 ; présent : ancienne + 1
}
// {kiwi=2, pomme=1}

Map<Character, List<String>> parLettre = new TreeMap<>();
for (String f : List.of("kiwi", "pomme", "poire")) {
    parLettre.computeIfAbsent(f.charAt(0), k -> new ArrayList<>()).add(f);   // crée la liste si besoin, puis ajoute
}
// {k=[kiwi], p=[pomme, poire]}

ordre.putIfAbsent("z", 9);                  // ne met 9 que si "z" n'a pas encore de valeur
```

**📖 La leçon : `Set`, un ensemble sans doublons.** Un `Set` refuse les doublons : `add` rend `false` si l'élément y est déjà. Mêmes trois variantes que pour `Map` : `HashSet` (sans ordre), `LinkedHashSet` (ordre d'insertion), `TreeSet` (trié).

```java
Set<String> vus = new HashSet<>();
vus.add("kiwi")       // true
vus.add("kiwi")       // false : déjà présent
```

**👉 À toi :**

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

**📖 La leçon : parcourir une `Map`.** `keySet()` donne les clés, `values()` les valeurs, et `entrySet()` les couples, de type `Map.Entry`, avec `getKey()` et `getValue()` :

```java
for (Map.Entry<String, Integer> e : tri.entrySet()) {
    System.out.print(e.getKey() + "=" + e.getValue() + ";");     // kiwi=2;pomme=4;
}
```

**📖 La leçon : intersection, union, différence.** Sur une **copie**, pour ne pas abîmer l'original :

```java
Set<String> a = new TreeSet<>(List.of("kiwi", "pomme", "fraise"));
Set<String> b = new TreeSet<>(List.of("pomme", "mangue"));
Set<String> inter = new TreeSet<>(a); inter.retainAll(b);    // [pomme]
Set<String> union = new TreeSet<>(a); union.addAll(b);       // [fraise, kiwi, mangue, pomme]
Set<String> diff  = new TreeSet<>(a); diff.removeAll(b);     // [fraise, kiwi]
```

**📖 La leçon : `PriorityQueue`, une file où le plus petit sort d'abord.** Quel que soit l'ordre d'ajout, `poll()` rend toujours le **plus petit** élément, selon l'ordre naturel ou le comparateur donné à la création :

```java
PriorityQueue<Integer> tas = new PriorityQueue<>(List.of(5, 1, 4, 2));
tas.poll()      // 1
tas.poll()      // 2
```

**👉 À toi :**

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

**📖 La leçon : modifier une `Map` à travers ses vues.** `keySet()`, `values()` et `entrySet()` ne sont pas des copies : ce sont des **fenêtres** sur la `Map`. Retirer un élément d'une vue (par exemple avec `removeIf`) le retire de la `Map`.

**📖 Rappel :** `merge`, `computeIfAbsent` et `putIfAbsent` (étape 1). Les autres méthodes de l'étape (`compute`, `computeIfPresent`, `replaceAll`, `remove(clé, valeur)`) suivent la même idée : une lambda reçoit la clé et l'ancienne valeur, et rend la nouvelle. Si la lambda rend `null`, la clé est supprimée.

**👉 À toi :**

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
