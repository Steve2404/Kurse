# Projet 1 — L'inventaire (l'API `List` et `Collections`)

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

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

**Ce que le chapitre 9 t'apprend :** les **collections**, des conteneurs qui **grandissent tout seuls**, contrairement aux tableaux :
- les listes ;
- les ensembles ;
- les tables d'association ;
- les files.

Puis à écrire tes **propres types génériques**. Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : des fruits.

**Les imports :** les collections sont dans `java.util` : écris `import java.util.*;` en haut de tes fichiers.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch9-p01 -sourcepath src/main/java src/main/java/ch9_collections/projects/p01_inventory/Inventory.java
java "-Duser.language=fr" -cp build/ch9-p01 ch9_collections.projects.p01_inventory.Inventory
```

---

## Tableau de bord

### ☐ Étape 1 — Charger, et le piège `remove`

```
charge : 10 articles, premier clavier, dernier horloge, vide false
ventes : [3, 12, 7, 3, 1], frequence de 3 : 2, indexOf(3) 0, lastIndexOf(3) 3, contains(12) true
```

**📖 La leçon : la liste, un tableau qui grandit.** Une `List` garde ses éléments **dans l'ordre**, numérotés à partir de 0, et accepte les doublons. On déclare la variable avec l'**interface** `List`, et on crée l'objet avec une **classe** concrète, `ArrayList`. Le type des éléments va entre chevrons ; à droite, le **diamant** `<>` le reprend tout seul :

```java
List<String> fruits = new ArrayList<>();
fruits.add("pomme");               // ajoute à la fin
fruits.add("kiwi");
fruits.add(0, "fraise");           // insère en position 0 : [fraise, pomme, kiwi]
fruits.size()                      // 3
fruits.get(1)                      // "pomme"
fruits.contains("kiwi")            // true
fruits.indexOf("kiwi")             // 2
fruits.set(2, "mangue");           // remplace la case 2 : [fraise, pomme, mangue]
fruits.remove("pomme");            // retire cette valeur : [fraise, mangue]
for (String f : fruits) { … }      // le for-each marche aussi
```

Une collection ne contient que des **objets**. Pour des nombres, on écrit `List<Integer>`, et Java emballe et déballe tout seul (chapitre 5, projet 4) :

```java
List<Integer> notes = new ArrayList<>();
notes.add(12);                     // 12 est emballé en Integer
int premiere = notes.get(0);       // déballé en int
```

`System.out.println(liste)` affiche directement `[fraise, mangue]` : pas besoin d'`Arrays.toString`.

**📖 La leçon : la classe `Collections`.** Comme `Arrays` pour les tableaux, `Collections` (avec un **s**) offre des outils `static` : `Collections.sort(l)`, `Collections.max(l)`, `Collections.frequency(l, x)` (combien de fois `x` apparaît)…

**👉 À toi :**

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

**📖 La leçon : modifier avec une lambda.** Les listes acceptent des lambdas (chapitre 8) :

```java
List<String> fruits = new ArrayList<>(List.of("pomme", "kiwi", "banane", "figue", "ananas"));
fruits.removeIf(f -> f.length() < 5);       // retire ceux qui passent le test : [pomme, banane, figue, ananas]
fruits.replaceAll(f -> f.toUpperCase());    // remplace chacun : [POMME, BANANE, FIGUE, ANANAS]
```

`new ArrayList<>(autreCollection)` fabrique une **copie** indépendante.

**📖 La leçon : trier avec un `Comparator`.** `liste.sort(comparateur)` trie sur place. `Comparator` fabrique les comparateurs à partir d'une méthode qui donne la **clé** de tri :

```java
fruits.sort(Comparator.naturalOrder());                       // ordre alphabétique
fruits.sort(Comparator.comparing(String::length)              // par longueur…
                      .thenComparing(Comparator.reverseOrder())); // …puis, à égalité, alphabétique inversé
fruits.sort(Comparator.comparingInt(String::length).reversed());  // par longueur décroissante

record Livre(String titre, int pages) { }
livres.sort(Comparator.comparingInt(Livre::pages));           // par nombre de pages
```

- `comparing(clé)` pour une clé objet, `comparingInt` / `comparingLong` pour une clé `int` / `long` ;
- `thenComparing(…)` départage les égalités ;
- `reversed()` inverse tout l'ordre ;
- `thenComparing(clé, Comparator.reverseOrder())` inverse **seulement** ce critère.

`Collections.max(liste, comparateur)` et `Collections.min(…)` rendent le plus grand et le plus petit selon ce comparateur.

**👉 À toi :**

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

**📖 La leçon : l'itérateur, pour retirer pendant le parcours.** Pour retirer des éléments **pendant** qu'on parcourt une liste, il faut passer par un `Iterator` et **sa** méthode `remove()` :

```java
Iterator<String> it = fruits.iterator();
while (it.hasNext()) {              // reste-t-il un élément ?
    String f = it.next();           // prends le suivant
    if (f.startsWith("c")) it.remove();   // retire celui qu'on vient de prendre
}
```

Un `ListIterator` permet en plus de **remplacer** l'élément courant avec `set` :

```java
ListIterator<String> li = fruits.listIterator();
while (li.hasNext()) {
    li.set(li.next().toUpperCase());
}
```

**👉 À toi :**

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

**📖 La leçon : `LinkedList`, ajouter aux deux bouts.** Une `LinkedList` est aussi une `List`, avec des méthodes pratiques aux extrémités :

```java
LinkedList<String> file = new LinkedList<>(List.of("b", "c"));
file.addFirst("a");             // [a, b, c]
file.addLast("d");              // [a, b, c, d]
file.removeFirst()              // "a" ; reste [b, c, d]
file.getLast()                  // "d"
```

**📖 La leçon : les listes toutes faites.** Il existe plusieurs façons rapides de créer une liste : `List.of(…)`, `Arrays.asList(…)`, `List.copyOf(…)`. Elles **n'ont pas toutes les mêmes droits** (ajouter ? modifier ? contenir `null` ?) : les expériences de l'étape te le font découvrir. Si tu as besoin d'une liste que tu modifieras librement, copie-la dans un `new ArrayList<>(…)`.

**👉 À toi :**

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
