# Projet 1 — L'inventaire (l'API `List` et `Collections`)

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 9) :**
- **`List`** et **`ArrayList`** : `add`, `get`, `set`, `size`, `isEmpty`, `indexOf`, `lastIndexOf`, `contains` ;
- le **piège** `remove(int)` contre `remove(Object)` sur une `List<Integer>` ;
- **`removeIf`**, **`replaceAll`**, **`sort`** avec un **`Comparator`** composé (`comparing`, `thenComparing`, `reverseOrder`, `comparingInt`, `comparingLong`, `reversed`) ;
- **`subList`** : une **vue** ;
- **`Iterator.remove`** et **`ListIterator.set`** pendant un parcours ;
- **`LinkedList`** : `addFirst`, `addLast`, `removeFirst` ;
- **`Collections`** : `sort`, `reverse`, `swap`, `rotate`, `shuffle(Random)`, `nCopies`, `frequency`, `max`, `min`, `binarySearch` ;
- **les listes de taille fixe ou immuables** : `Arrays.asList`, `List.of`, `List.copyOf` ;
- `toArray(new Item[0])`, `equals` entre listes.

Côté algorithme : l'**analyse ABC** d'un stock (tri par valeur, puis classement par part cumulée).

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch9_collections.projects.p01_inventory` :
- le record **`Item`** ;
- **`Inventory`** (le `main`).

**Règle du crescendo :** chapitres 1 à 9. **Pas de stream** (`stream()`, `Collectors`) ni d'`Optional` : ils sont au chapitre 10. Pas de `try/catch`.

---

## Tableau de bord

### ☐ Étape 1 — Charger, et le piège `remove`

```
charge : 10 articles, premier clavier, dernier horloge, vide false
ventes : [3, 12, 7, 3, 1], frequence de 3 : 2, indexOf(3) 0, lastIndexOf(3) 3, contains(12) true
```
- **`record Item(String sku, String name, String category, int stock, long price)`** :
  - `parse` ;
  - `value()` = stock × prix ;
  - `withStock(int)` et `withPrice(long)` rendent une copie ;
  - `toString()` rend le nom.
- **Le chargement :** `List<Item> items = new ArrayList<>();`. L'interface est à gauche, le diamant `<>` à droite.
- **Les ventes :** une `List<Integer>` remplie depuis `Data.SALES` (autoboxing). Puis :
  - `sales.remove(1)` retire l'élément à l'**indice** 1 ;
  - `sales.remove(Integer.valueOf(3))` retire la **valeur** 3.
- Ensuite : `Collections.frequency(sales, 3)`, `indexOf(3)`, `lastIndexOf(3)` et `contains(12)`.
- **Question :** que ferait `sales.remove(3)` ?

### ☐ Étape 2 — Modifier en masse, trier

```
ruptures retirees [souris, poele], reste 8 ; cuisine +10 % : tasse=9.79 bouilloire=32.89
par categorie puis prix decroissant : [bouilloire, tasse, ecran, clavier, cable, vase, lampe, horloge]
subList(0, 3) : [bouilloire, tasse, ecran], max par valeur clavier, min par stock vase
```
- **Les ruptures :**
  1. copie la liste (`new ArrayList<>(items)`) et garde seulement les ruptures (`removeIf(i -> i.stock() > 0)`) ;
  2. retire-les de `items` avec `removeIf`.
- **La hausse :** `replaceAll` augmente de 10 % (`prix + prix / 10`) les articles `cuisine`. Affiche chaque article de cuisine sous la forme `nom=prix`.
- **Le tri :** `items.sort(Comparator.comparing(Item::category).thenComparing(Item::price, Comparator.reverseOrder()))`.
- **Ligne 3 :**
  - `subList(0, 3)` ;
  - `Collections.max(items, Comparator.comparingLong(Item::value))` ;
  - `Collections.min(items, Comparator.comparingInt(Item::stock))`.
- **Question :** que devient `items` si on fait `top3.clear()` ?

### ☐ Étape 3 — Parcourir en modifiant

```
reassort de 3 articles ; sans les noms en c : [bouilloire, tasse, ecran, vase, lampe, horloge]
ABC : ecran=A tasse=A vase=B horloge=B bouilloire=C lampe=C (total 1916.70)
```
- **Le réassort :** un `ListIterator<Item>`. Pour chaque article sous `Data.TARGET_STOCK`, `it.set(i.withStock(TARGET_STOCK))` ; compte-les.
- **Le retrait :** un `Iterator<Item>` qui retire (`remover.remove()`) les noms qui commencent par `c`.
  - **Expérience :** fais plutôt `items.remove(x)` dans un for-each. Quelle exception ?
- **L'analyse ABC :**
  1. copie la liste et trie-la par `value()` décroissante (`comparingLong(...).reversed()`) ;
  2. fais le total ;
  3. cumule article par article : **A** si le cumul × 100 ≤ total × 70, sinon **B** si ≤ total × 90, sinon **C** ;
  4. le total s'affiche en euros, avec `money(long)`.

### ☐ Étape 4 — `LinkedList`, `Collections`, listes figées

```
file : servi urgent, restant [cmd4, cmd1, cmd2, cmd3], inverse+swap [cmd3, cmd4, cmd2, cmd1], melange [a, c, d, b, e], nCopies [x, x, x]
trie [1, 3, 3, 7, 12], binarySearch(7) 3, binarySearch(5) -4
asList [X, y, z], List.of [a, b], toArray 6, egalite true, copyOf [X, y, z]
```
- **La file :**
  1. `LinkedList<String> orders` à partir de `List.of("cmd1", "cmd2", "cmd3")` ;
  2. `addFirst("urgent")`, `addLast("cmd4")`, puis `removeFirst()` (le servi) ;
  3. une copie : `Collections.reverse`, puis `Collections.swap(copy, 0, 1)` ;
  4. `Collections.rotate(orders, 1)` ;
  5. `Collections.shuffle` de `a b c d e` avec `new Random(Data.SEED)` ;
  6. `Collections.nCopies(3, "x")`.
- **La recherche :** trie une copie des ventes (`Collections.sort`), puis `Collections.binarySearch` de 7 et de 5.
- **Les listes figées :**
  - `fixed = Arrays.asList("x", "y", "z")`, puis `fixed.set(0, "X")` (permis) ;
  - `List.of("a", "b")` ;
  - `items.toArray(new Item[0]).length` ;
  - `List.of(1, 2).equals(Arrays.asList(1, 2))` ;
  - `List.copyOf(fixed)`.
- **Expériences :**
  - `fixed.add("w")` ;
  - `List.of("a").set(0, "b")` ;
  - `List.of("a", null)`.
  
  Lesquelles compilent ? Que se passe-t-il à l'exécution ?

---

## Checklist (vérifiée par `Check`)

- `Data.ITEMS` et `Data.SALES` ;
- `List<Item> items = new ArrayList<>()`, `Integer.valueOf(` ;
- `removeIf`, `replaceAll`, `Comparator.comparing`, `thenComparing`, `reverseOrder`, `subList` ;
- `Collections.max`, `min`, `frequency`, `reverse`, `swap`, `rotate`, `shuffle`, `nCopies`, `binarySearch`, `sort` ;
- `ListIterator<Item>` avec `set`, `Iterator<Item>` avec `remove()` ;
- `Arrays.asList`, `List.of`, `List.copyOf`, `toArray(new Item[0])` ;
- `LinkedList<String>`, `addFirst`, `removeFirst`.

---

## Sortie attendue complète

```
charge : 10 articles, premier clavier, dernier horloge, vide false
ventes : [3, 12, 7, 3, 1], frequence de 3 : 2, indexOf(3) 0, lastIndexOf(3) 3, contains(12) true
ruptures retirees [souris, poele], reste 8 ; cuisine +10 % : tasse=9.79 bouilloire=32.89
par categorie puis prix decroissant : [bouilloire, tasse, ecran, clavier, cable, vase, lampe, horloge]
subList(0, 3) : [bouilloire, tasse, ecran], max par valeur clavier, min par stock vase
reassort de 3 articles ; sans les noms en c : [bouilloire, tasse, ecran, vase, lampe, horloge]
ABC : ecran=A tasse=A vase=B horloge=B bouilloire=C lampe=C (total 1916.70)
file : servi urgent, restant [cmd4, cmd1, cmd2, cmd3], inverse+swap [cmd3, cmd4, cmd2, cmd1], melange [a, c, d, b, e], nCopies [x, x, x]
trie [1, 3, 3, 7, 12], binarySearch(7) 3, binarySearch(5) -4
asList [X, y, z], List.of [a, b], toArray 6, egalite true, copyOf [X, y, z]
```
