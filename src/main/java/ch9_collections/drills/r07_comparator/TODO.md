# Drill de rappel 7 — `Comparable` et `Comparator`

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall07`** dans le paquet `ch9_collections.drills.r07_comparator`.
- Dans le **même fichier**, crée le record package-private `Dog(String name, int age) implements Comparable<Dog>` :
  - `compareTo` compare les **noms** ;
  - `toString()` = nom + âge (`Rex5`).

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 2) et projet 5 (étape 1). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r07_comparator` → **New** → **Java Class** → `Recall07`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall07`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall07`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `dogs = new ArrayList<>(List.of(Rex 5, Ace 9, Max 5, Bob 2))`, puis `Collections.sort(dogs)`.
  → `D01 : [Ace9, Bob2, Max5, Rex5]`
- ☐ **D02.** `dogs.sort(Comparator.comparingInt(Dog::age))`. Le tri est **stable** : à âge égal, l'ordre précédent est gardé.
  → `D02 : [Bob2, Max5, Rex5, Ace9]`
- ☐ **D03.** Par âge décroissant, puis par nom.
  → `D03 : [Ace9, Max5, Rex5, Bob2]`
- ☐ **D04.** `comparing(Dog::name, Comparator.reverseOrder())`.
  → `D04 : [Rex5, Max5, Bob2, Ace9]`
- ☐ **D05.** Trois copies de `List.of("b", "A", "c", "B")`, triées par :
  - `Comparator.naturalOrder()` ;
  - `String.CASE_INSENSITIVE_ORDER` ;
  - `Collections.reverseOrder()`.
  → `D05 : [A, B, b, c] [A, b, B, c] [c, b, B, A]`
- ☐ **D06.** `withNulls = new ArrayList<>(Arrays.asList("b", null, "a"))`.
  1. Trie-la avec `Comparator.nullsFirst(Comparator.naturalOrder())` ;
  2. trie une copie avec `Comparator.nullsLast(Comparator.reverseOrder())`.
  
  Affiche les deux, puis `new Dog("Rex", 1).compareTo(new Dog("Ace", 1))`.
  → `D06 : [null, a, b] [b, a, null] 17`
- ☐ **D07.** `fruits = new ArrayList<>(List.of("kiwi", "fig", "banana", "plum"))`.
  1. `byWeight = Comparator.comparingDouble((String f) -> f.length() * 1.5).thenComparingInt(f -> f.charAt(0))` ;
  2. `Collections.sort(fruits, byWeight)`.

  Affiche `fruits`, `Collections.binarySearch(fruits, "plum", byWeight)`, puis `Collections.binarySearch(fruits, "date", byWeight)`.
  → `D07 : [fig, kiwi, plum, banana] 2 -2`

## Expériences (hors sortie attendue)

1. Pourquoi `CASE_INSENSITIVE_ORDER` garde-t-il `b` avant `B` ?
2. Que rendrait `Collections.sort` sur une liste de records qui n'implémentent **pas** `Comparable` ? Compilation, ou exécution ?
3. `Comparator.comparing(Dog::age)` compile-t-il ? Quelle différence avec `comparingInt` ?
4. `Collections.binarySearch(fruits, "plum")`, **sans** le comparateur : pourquoi le résultat n'est-il pas fiable ?
5. Pourquoi faut-il écrire `(String f) ->` dans `comparingDouble`, alors que `thenComparingInt(f -> …)` s'en passe ?

## Sortie attendue complète

```
D01 : [Ace9, Bob2, Max5, Rex5]
D02 : [Bob2, Max5, Rex5, Ace9]
D03 : [Ace9, Max5, Rex5, Bob2]
D04 : [Rex5, Max5, Bob2, Ace9]
D05 : [A, B, b, c] [A, b, B, c] [c, b, B, A]
D06 : [null, a, b] [b, a, null] 17
D07 : [fig, kiwi, plum, banana] 2 -2
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| `Comparable<T>` | `Comparator<T>` |
|---|---|
| `java.lang` | `java.util` |
| `int compareTo(T o)` dans la classe | `int compare(T a, T b)`, à part (souvent une lambda) |
| un seul ordre, « naturel » | autant d'ordres que l'on veut |

- **Signe du résultat :** négatif si a < b, zéro si égaux, positif si a > b.
- **`String.compareTo`** rend la différence entre les caractères : `'R' - 'A'` = 17.
- **Les fabriques :** `comparing`, `comparingInt`, `comparingLong`, `comparingDouble`, `naturalOrder`, `reverseOrder`, `nullsFirst`, `nullsLast`.
- **Les méthodes d'instance :** `reversed`, `thenComparing`, `thenComparingInt`, `thenComparingLong`, `thenComparingDouble`.
- **`Collections.binarySearch(liste, clé, comparateur)`** : la liste doit être triée avec **le même** ordre. Absent → `-(point d'insertion) - 1`.
- **`reversed()`** inverse **toute** la chaîne écrite avant lui.
- **Le tri** de `List.sort` et `Collections.sort` est **stable**.

</details>
