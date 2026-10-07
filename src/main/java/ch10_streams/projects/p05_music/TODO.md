# Projet 5 — Les statistiques et recommandations d'une plateforme musicale

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**API visée :** toute la classe `Collectors` :
- `groupingBy` (1, 2 et 3 arguments) ;
- `partitioningBy` ;
- les collecteurs **en aval** : `counting`, `summingInt`, `averagingInt`, `summarizingInt`, `mapping`, `filtering`, `flatMapping`, `maxBy`, `minBy`, `reducing`, `collectingAndThen`, `toSet`, `toCollection` ;
- `toMap` avec fusion et fabrique ;
- `teeing` ;
- `joining`.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch10_streams.projects.p05_music`. La classe du `main` s'appelle **`MusicStats`**.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch10-p05 -sourcepath src/main/java src/main/java/ch10_streams/projects/p05_music/MusicStats.java
java "-Duser.language=fr" -cp build/ch10-p05 ch10_streams.projects.p05_music.MusicStats
```

**Ce projet t'apprend les `Collectors`** : des collectes toutes faites, à passer à `collect(…)`. Les exemples utilisent un menu :

```java
record Plat(String nom, String type, int prix) { }
List<Plat> menu = List.of(new Plat("soupe", "entree", 6), new Plat("salade", "entree", 8),
        new Plat("steak", "plat", 18), new Plat("tarte", "dessert", 7), new Plat("glace", "dessert", 5));
```

**Conseil :** range chaque résultat dans une **variable typée** avant de l'afficher. Le type écrit à gauche aide Java (et toi) à comprendre ce que la collecte fabrique.

---

## Le problème

Une plateforme de streaming enregistre chaque écoute : utilisateur, artiste, titre, genre, secondes écoutées et ambiances (`Data.PLAYS`). Une écoute de moins de `Data.VALID_SECONDS` secondes est **zappée**. Elle compte comme écoute, mais pas pour l'artiste.

Ton programme imprime le tableau de bord de l'équipe produit, puis **recommande des artistes** à chaque utilisateur à partir des goûts de son voisin le plus proche.

**Règle :** les `Map` affichées doivent être **triées** par clé. Leur `toString()` (`{a=1, b=2}`) fait partie de la sortie attendue. Un `HashMap` ne garantit aucun ordre : à toi de choisir la bonne fabrique.

**Astuce de lisibilité :** `import static java.util.stream.Collectors.*;` (ou un import par méthode).

---

## Tableau de bord

### ☐ Étape 1 — Le modèle

- Une écoute vient d'une ligne de `Data.PLAYS`. Les ambiances `epique|live` deviennent une **liste**.
  - **Piège :** `split("|")` ne fait pas ce que tu crois. Pourquoi ? (`split` prend une expression régulière.)
- Une écoute sait dire si elle est **validée**.

### ☐ Étape 2 — Compter et partitionner

```
ECOUTES PAR GENRE : {Electro=5, Jazz=6, Pop=6, Rock=7}
VALIDEES : 19, ZAPPEES : 5
ONT ZAPPE : [hugo, lea, tom, zoe]
```

**📖 La leçon : regrouper.** `groupingBy(clé)` range les éléments dans une `Map` : clé → liste des éléments qui ont cette clé. Avec 3 arguments, on choisit aussi la sorte de `Map`, et ce qu'on fait de chaque groupe (un collecteur **en aval**) :

```java
Map<String, List<Plat>> parType = menu.stream().collect(Collectors.groupingBy(Plat::type));
Map<String, Long> compte = menu.stream()
        .collect(Collectors.groupingBy(Plat::type, TreeMap::new, Collectors.counting()));
// {dessert=2, entree=2, plat=1}
```

**📖 La leçon : partitionner.** `partitioningBy(test)` coupe en **deux** groupes, `true` et `false` :

```java
Map<Boolean, Long> petits = menu.stream()
        .collect(Collectors.partitioningBy(p -> p.prix() < 7, Collectors.counting()));
// {false=3, true=2}
```

**📖 La leçon : transformer dans un groupe, choisir la collection.**

```java
Map<String, List<String>> noms = menu.stream()
        .collect(Collectors.groupingBy(Plat::type, TreeMap::new,
                Collectors.mapping(Plat::nom, Collectors.toList())));
// {dessert=[tarte, glace], entree=[soupe, salade], plat=[steak]}
TreeSet<String> types = menu.stream().map(Plat::type).collect(Collectors.toCollection(TreeSet::new));
```

**👉 À toi :**

- **ECOUTES PAR GENRE :** `groupingBy` à 3 arguments. Quel est le rôle de chacun ?
- **VALIDEES / ZAPPEES :** une **partition** avec un collecteur en aval.
  - **Question :** que contient la `Map` de `partitioningBy` si **aucune** écoute n'est zappée ? Et celle de `groupingBy(Play::valid)` ?
- **ONT ZAPPE :** une partition dont chaque côté est un **ensemble trié** de noms. Il faut deux collecteurs en aval imbriqués.

### ☐ Étape 3 — Le meilleur artiste de chaque genre

```
TOP ARTISTE PAR GENRE : {Electro=Daft (3), Jazz=Miles (3), Pop=Adele (2), Rock=Queen (4)}
```

**📖 La leçon : retoucher le résultat d'un collecteur.** `collectingAndThen(collecteur, fonction)` applique une fonction **au résultat** :

```java
String phrase = menu.stream().collect(Collectors.collectingAndThen(Collectors.counting(), n -> n + " plats"));
// "5 plats"
Optional<Plat> cher = menu.stream().collect(Collectors.maxBy(Comparator.comparingInt(Plat::prix)));
```

Un `groupingBy` peut en contenir un autre : on obtient une `Map` de `Map`.

**👉 À toi :**

- Ne compte que les écoutes **validées**.
- C'est un groupement **à deux niveaux** : genre → (artiste → nombre). Chaque sous-table est ensuite **réduite** à son meilleur artiste, dans le **même** `collect`.
  - Quel collecteur applique une fonction finale au résultat d'un autre collecteur ?
- À égalité, garde l'artiste **le premier dans l'alphabet**.
  - Pop : Adele 2 et Dua 2. Les écoutes de Dua par lea et d'Adele par tom sont zappées : vérifie-le à la main.
  - **Piège :** avec `max`, « premier dans l'alphabet » se traduit comment sur le comparateur de noms ?

### ☐ Étape 4 — Filtrer **dans** ou **avant** le groupement ?

```
TEMPS VALIDE PAR UTILISATEUR : {hugo=1360, ines=1567, lea=2009, tom=950, zoe=0}
```

**📖 La leçon : filtrer dans chaque groupe.** `filtering(test, collecteurEnAval)` ne garde, **dans chaque groupe**, que les éléments qui passent le test :

```java
Map<String, Long> chers = menu.stream()
        .collect(Collectors.groupingBy(Plat::type, TreeMap::new,
                Collectors.filtering(p -> p.prix() > 6, Collectors.counting())));
// {dessert=1, entree=1, plat=1}
```

**👉 À toi :**

- C'est la somme des secondes des écoutes validées, par utilisateur.
- **Le piège central de cette étape :** zoe n'a **que** des écoutes zappées, et elle doit apparaître avec `0`.
  - Avec `filter(...)` **avant** `groupingBy`, zoe disparaît.
  - Quel collecteur en aval (Java 9) filtre **dans** chaque groupe ?
  - Essaie les deux versions.

### ☐ Étape 5 — Aplatir dans un groupe

```
AMBIANCES PAR GENRE : {Electro=[energie, fete, nuit], Jazz=[calme, nuit, voix], ...}
```

**📖 La leçon : aplatir dans un groupe.** `flatMapping(f, collecteurEnAval)` : `f` transforme chaque élément en un **stream**, et tous ces streams sont mis bout à bout dans le groupe :

```java
Set<String> lettres = Stream.of(List.of("a", "b"), List.of("b", "c"))
        .collect(Collectors.flatMapping(List::stream, Collectors.toCollection(TreeSet::new)));
// [a, b, c]
```

**👉 À toi :**

- Chaque écoute apporte **plusieurs** ambiances. Le groupe genre doit contenir l'ensemble trié de **toutes** ses ambiances.
- `mapping` donnerait un ensemble de listes. Quel collecteur (Java 9) aplatit ?

### ☐ Étape 6 — Extrêmes et réductions par groupe

```
ECOUTE LA PLUS LONGUE : {hugo=Around, ines=So What, lea=So What, tom=One More Time, zoe=Uprising}
SECONDES PAR GENRE : {Electro=1537, Jazz=1816, Pop=1006, Rock=1591}
```

**📖 La leçon : réduire dans un collecteur.** `reducing(identite, transformation, operateur)` fait un `reduce` (projet 4) à l'intérieur d'un collecteur :

```java
int total = menu.stream().collect(Collectors.reducing(0, Plat::prix, Integer::sum));   // 44
```

**👉 À toi :**

- **ECOUTE LA PLUS LONGUE :**
  - `maxBy` en aval rend un `Optional<Play>`. Or on veut le **titre**, sans `Optional` dans la `Map`. Transforme le résultat dans le même collecteur.
  - **Question :** un groupe de `groupingBy` peut-il être vide ? Alors pourquoi le type reste-t-il un `Optional` ?
- **SECONDES PAR GENRE :**
  - **Contrainte :** sans `summingInt`. Utilise `reducing` à 3 arguments : identité, transformation, opérateur.

### ☐ Étape 7 — `toMap` et ses pièges

```
TOP 3 TITRES : [So What (1124 s), Bohemian (908 s), One More Time (640 s)]
```

**📖 La leçon : `toMap` et `joining`.**

```java
Map<String, Integer> prixParNom = menu.stream()
        .collect(Collectors.toMap(Plat::nom, Plat::prix, Integer::sum, TreeMap::new));
//                            clé       valeur      si deux fois la même clé   sorte de Map
String noms = menu.stream().map(Plat::nom).collect(Collectors.joining(", "));             // soupe, salade, …
String liste = menu.stream().map(Plat::nom).collect(Collectors.joining(", ", "[", "]"));  // [soupe, salade, …]
```

**👉 À toi :**

- Commence par une table titre → secondes **cumulées**, construite avec `toMap`.
  - « Hello » est écouté 3 fois. Que se passe-t-il **sans** fonction de fusion ? Essaie et lis l'exception.
  - Utilise la version à 4 arguments pour obtenir une `TreeMap`.
- Ensuite, garde les 3 premiers par secondes décroissantes (à égalité, par titre) et formate avec `joining` à 3 arguments (séparateur, préfixe, suffixe).

### ☐ Étape 8 — Statistiques et `teeing`

```
STATS : 24 ecoutes, min 8 s, max 562 s, moyenne 247.9 s
EXTREMES : Hello par tom / So What par lea
MOYENNE VALIDEE : 309.8 s sur 19 ecoutes
```

**📖 La leçon : deux collecteurs en un passage.** `teeing(c1, c2, fusion)` envoie chaque élément aux **deux** collecteurs, puis fusionne leurs résultats :

```java
String ratio = menu.stream().collect(Collectors.teeing(
        Collectors.counting(),                   // 5
        Collectors.summingInt(Plat::prix),       // 44
        (n, s) -> s + "/" + n));                 // "44/5"
double moyenne = menu.stream().collect(Collectors.averagingInt(Plat::prix));   // 8.8
```

`summarizingInt` donne les statistiques complètes, comme `summaryStatistics` (projet 3, étape 3).

**👉 À toi :**

- **STATS :** un seul collecteur donne les quatre nombres.
- **EXTREMES :** `teeing(minBy, maxBy, fusion)`, en un seul passage.
  - lea et ines ont toutes deux 562 s sur « So What ». Pourquoi est-ce lea qui sort ? Vérifie dans la Javadoc ce que `maxBy` garde à égalité.
- **MOYENNE VALIDEE :** `teeing(counting, averagingInt, fusion)`.
- **Piège de compilation :** écris directement `System.out.println(stream.collect(teeing(...)))`. Pourquoi est-ce que ça ne compile pas, alors que la fusion rend une `String` ?
  - Indice : `println` a une surcharge `println(char[])`, et le type `R` de `teeing` est inféré.
  - Quelle est la correction la plus simple ?

### ☐ Étape 9 — Les explorateurs

```
EXPLORATEURS (tous les genres) : [lea]
```

**📖 Rappel :** `groupingBy` avec `mapping` et `toSet` (étape 2).

**👉 À toi :**

- Ce sont les utilisateurs dont les écoutes **validées** couvrent **tous** les genres existants.
- Il faut un groupement utilisateur → ensemble de genres (`toSet`), puis une comparaison au nombre total de genres.

### ☐ Étape 10 — Les recommandations : l'algorithme

```
RECO hugo : voisin tom (50%) -> Dua
RECO ines : voisin lea (67%) -> Daft, Muse
RECO lea : voisin ines (67%) -> rien de nouveau
RECO tom : voisin hugo (50%) -> Queen
RECO zoe : aucune ecoute validee
```

**📖 Rappel :** intersection et union d'ensembles avec `retainAll` et `addAll` sur des **copies** (chapitre 9, projet 2, étape 2).

**👉 À toi :**

1. **Les goûts.** Pour chaque utilisateur, prends l'ensemble trié des artistes de ses écoutes **validées**. C'est un `groupingBy` + `mapping` + `toCollection`.
2. **La similarité de Jaccard** entre deux ensembles A et B : |A ∩ B| / |A ∪ B|.
   - Calcule à la main lea / ines : {Adele, Miles, Nina, Queen} sur 6 artistes, soit 0.666…, affiché `67%` (`Math.round`, chapitre 4).
3. **Le voisin.** C'est l'**autre** utilisateur le plus similaire.
   - La similarité doit être strictement supérieure à 0.
   - À égalité, prends le premier dans l'alphabet.
   - S'il n'y en a pas, affiche `aucun voisin`.
4. **La recommandation.** Ce sont les artistes du voisin que l'utilisateur **n'a pas**, triés.
   - S'il n'y en a aucun, affiche `rien de nouveau`. Fais-le avec `collectingAndThen(joining(...), …)`, sans `if`.
5. **zoe** n'a aucune écoute validée : elle n'est pas dans la table des goûts. Le parcours des utilisateurs doit donc partir de **toutes** les écoutes.

**Question :** quelle est la complexité de ton algorithme en nombre d'utilisateurs ? Que faudrait-il changer pour un million d'utilisateurs ?

### ☐ Étape 11 — `main`

- Il charge les données et imprime le tableau de bord dans l'ordre de la sortie attendue.

---

## Checklist API (vérifiée par `Check`)

| Collecteur | Étape | ☐ |
|---|---|---|
| `groupingBy` + `TreeMap::new` | 2 à 10 | ☐ |
| `partitioningBy` | 2 | ☐ |
| `counting` | 2, 3, 8 | ☐ |
| `mapping`, `toCollection` | 2, 10 | ☐ |
| `collectingAndThen` | 3, 6, 10 | ☐ |
| `filtering` | 4 | ☐ |
| `summingInt` | 4 | ☐ |
| `flatMapping` | 5 | ☐ |
| `maxBy`, `minBy` | 6, 8 | ☐ |
| `reducing` | 6 | ☐ |
| `toMap` (4 arguments) | 7 | ☐ |
| `joining` | 7, 9, 10 | ☐ |
| `summarizingInt` | 8 | ☐ |
| `teeing`, `averagingInt` | 8 | ☐ |
| `toSet` | 9 | ☐ |

---

## Sortie attendue complète

```
ECOUTES PAR GENRE : {Electro=5, Jazz=6, Pop=6, Rock=7}
VALIDEES : 19, ZAPPEES : 5
ONT ZAPPE : [hugo, lea, tom, zoe]
TOP ARTISTE PAR GENRE : {Electro=Daft (3), Jazz=Miles (3), Pop=Adele (2), Rock=Queen (4)}
TEMPS VALIDE PAR UTILISATEUR : {hugo=1360, ines=1567, lea=2009, tom=950, zoe=0}
AMBIANCES PAR GENRE : {Electro=[energie, fete, nuit], Jazz=[calme, nuit, voix], Pop=[calme, energie, fete, voix], Rock=[energie, epique, live]}
ECOUTE LA PLUS LONGUE : {hugo=Around, ines=So What, lea=So What, tom=One More Time, zoe=Uprising}
SECONDES PAR GENRE : {Electro=1537, Jazz=1816, Pop=1006, Rock=1591}
TOP 3 TITRES : [So What (1124 s), Bohemian (908 s), One More Time (640 s)]
STATS : 24 ecoutes, min 8 s, max 562 s, moyenne 247.9 s
EXTREMES : Hello par tom / So What par lea
MOYENNE VALIDEE : 309.8 s sur 19 ecoutes
EXPLORATEURS (tous les genres) : [lea]
RECO hugo : voisin tom (50%) -> Dua
RECO ines : voisin lea (67%) -> Daft, Muse
RECO lea : voisin ines (67%) -> rien de nouveau
RECO tom : voisin hugo (50%) -> Queen
RECO zoe : aucune ecoute validee
```
