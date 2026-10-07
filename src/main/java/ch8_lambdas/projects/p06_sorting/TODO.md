# Projet 6 — Trier avec des fonctions

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 8) :**
- une **interface fonctionnelle à toi**, `Order`, qui redéclare `toString()` : une méthode publique d'`Object` ne compte pas ;
- des **combinateurs** `default` qui rendent de nouvelles lambdas (`reversed`, `then`) ;
- des **fabriques `static`** qui reçoivent des fonctions (`by(ToIntFunction<Person>)`, `byText(Function<Person, String>)`) ;
- des références **`Person::name`**, **`Person::age`** ;
- une lambda qui **modifie un champ** (le compteur de comparaisons), et une lambda qui **capture une variable locale** (`target`).

Tu reconstruis à la main ce que `Comparator` offrira au chapitre 9 (`comparing`, `thenComparing`, `reversed`).

Côté algorithmes :
- le **tri fusion** (stable) et le **tri par insertion**, paramétrés par un `Order` ;
- la **sélection partielle** des k meilleurs ;
- la **dichotomie** par clé (premier indice, ou `-(insertion) - 1`) ;
- le comptage des comparaisons.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch8_lambdas.projects.p06_sorting` :
- `Person` (record), `Order`, `Sorter` ;
- **`SortingApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 8. **Pas de `Comparator` ni d'`Arrays.sort`** : tu écris l'ordre et les tris toi-même.

---

## Tableau de bord

### ☐ Étape 1 — `Person` et `Order`

- **`record Person(String name, int age, String city, int score)`**, avec `parse(String)` et `toString()` qui rend le nom.
- **`@FunctionalInterface interface Order`** :
  - `int compare(Person a, Person b);` ;
  - `@Override String toString();` (toujours fonctionnelle : pourquoi ?) ;
  - `default Order reversed()` → `(a, b) -> compare(b, a)` ;
  - `default Order then(Order next)` : à égalité, `next` départage ;
  - `static Order by(ToIntFunction<Person> key)`, avec `Integer.compare` ;
  - `static Order byText(Function<Person, String> key)`, avec `compareTo`.

### ☐ Étape 2 — Le trieur

- **`Sorter`** :
  - un **champ** `comparisons`, et `comparisons()` qui le rend **et le remet à zéro** ;
  - `private Order counting(Order order)` rend une lambda qui incrémente le champ avant de comparer ;
  - `Person[] mergeSort(Person[] input, Order order)` trie une **copie**. La fusion prend à gauche si `compare <= 0` (stabilité) ;
  - `insertionSort` ;
  - `top(input, k, order)` : k passes de sélection (le meilleur restant passe en position i) ; rend les k premiers ;
  - `static int search(Person[] sorted, int target, ToIntFunction<Person> key)` : la borne inférieure, puis `lo` si la clé vaut `target`, sinon `-(lo + 1)`.

### ☐ Étape 3 — Les tris

```
par nom : Adam Bob Emma Hugo Ines Lea Lina Noah Theo Zoe (25 comparaisons)
par age puis nom : Hugo Zoe Emma Lina Ines Lea Theo Bob Adam Noah (24)
score decroissant puis age : Emma Hugo Adam Lina Lea Bob Theo Ines Noah Zoe (25)
stabilite : par ville apres par nom Adam Lea Noah Emma Theo Zoe Bob Hugo Ines Lina = ville puis nom Adam Lea Noah Emma Theo Zoe Bob Hugo Ines Lina
insertion true : 24 comparaisons contre 25 pour la fusion
```
- **Les ordres :**
  - `byName = Order.byText(Person::name)` ;
  - `byAge = Order.by(Person::age)` ;
  - `byCity = Order.byText(city)`, avec `Function<Person, String> city = Person::city` ;
  - `byScoreDesc = Order.by(score).reversed()`, avec `ToIntFunction<Person> score = Person::score`.
- **Les trois premières lignes** sont des tris fusion. Chacune se termine par `comparisons()`, sous la forme `(N comparaisons)` pour la première et `(N)` ensuite.
- **La stabilité :**
  1. trie par nom (`byNameFirst`), puis remets le compteur à zéro ;
  2. trie `byNameFirst` par ville ;
  3. compare avec `byCity.then(byName)` sur l'original : les deux doivent être identiques.
- **L'insertion :**
  1. remets le compteur à zéro, puis fais le tri par insertion par nom et note ses comparaisons ;
  2. fais un tri fusion par nom ;
  3. affiche si l'insertion donne le même ordre que `byNameFirst`, ses comparaisons, puis celles de la fusion.

### ☐ Étape 4 — Sélection, dichotomie, capture

```
top 3 : Emma Adam Hugo (24 comparaisons)
dichotomie par age : 29->2(Emma) 42->8(Adam) 30->-5
plus proches de 30 ans : Emma Ines Lea Lina ; ordre inverse du nom : Zoe Theo Noah
```
- **Le top :** `top(people, Data.TOP, byScoreDesc.then(byName))`.
- **La dichotomie :** dans le tableau trié par `byAge.then(byName)`, cherche chaque âge de `Data.AGES` avec `Sorter.search(…, Person::age)`. Affiche `age->indice`, et le nom si l'indice est positif.
- **La capture :**
  - `int target = 30;`, puis `Order closeTo30 = (a, b) -> Integer.compare(|a.age - target|, |b.age - target|)` ;
  - `top(people, 4, closeTo30.then(byName))` ;
  - enfin les 13 premiers caractères de la liste triée par `byName.reversed()`.
- **Expériences :**
  - ajoute `target++` après la lambda : quelle erreur ?
  - dans `counting`, remplace le champ par une variable locale incrémentée : quelle erreur ?
  - la fusion avec `< 0` au lieu de `<= 0` : la stabilité tient-elle encore ?

---

## Checklist (vérifiée par `Check`)

- `Data.PEOPLE` et `Data.AGES` ;
- `@FunctionalInterface` et `interface Order` ;
- `default Order reversed()` et `default Order then(` ;
- `static Order by(ToIntFunction<Person>` et `static Order byText(Function<Person, String>` ;
- `Person::name`, `Person::age` ;
- `comparisons++` et `int target = 30`.

---

## Sortie attendue complète

```
par nom : Adam Bob Emma Hugo Ines Lea Lina Noah Theo Zoe (25 comparaisons)
par age puis nom : Hugo Zoe Emma Lina Ines Lea Theo Bob Adam Noah (24)
score decroissant puis age : Emma Hugo Adam Lina Lea Bob Theo Ines Noah Zoe (25)
stabilite : par ville apres par nom Adam Lea Noah Emma Theo Zoe Bob Hugo Ines Lina = ville puis nom Adam Lea Noah Emma Theo Zoe Bob Hugo Ines Lina
insertion true : 24 comparaisons contre 25 pour la fusion
top 3 : Emma Adam Hugo (24 comparaisons)
dichotomie par age : 29->2(Emma) 42->8(Adam) 30->-5
plus proches de 30 ans : Emma Ines Lea Lina ; ordre inverse du nom : Zoe Theo Noah
```
