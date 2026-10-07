# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`MusicStats.java`](MusicStats.java).
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Le modèle

**Le code :** le record `Play`.

**Piège — `split("|")` :** `split` prend une **expression régulière**, où `|` signifie « ou ». `"|"` est donc « rien ou rien » : `split` coupe **entre chaque caractère**. Vérifié : `"epique|live".split("|")` donne `[e, p, i, q, u, e, |, l, i, v, e]`. Avec le `|` échappé (`"\\|"` dans le code), on obtient `[epique, live]`.

---

## Étape 2 — Compter et partitionner

**Le code :** le début de `report`.

**`groupingBy` à 3 arguments :** la **clé**, la **fabrique** de la `Map` (`TreeMap::new`, pour un affichage trié) et le collecteur **en aval** (`counting()`), appliqué à chaque groupe.

**Question — une partition vide :** `partitioningBy` a **toujours** les deux clés, `true` et `false`. `groupingBy` ne crée que les clés **rencontrées**. Vérifié sur `1, 2` avec le test « > 5 » :
- `partitioningBy` donne `{false=[1, 2], true=[]}` ;
- `groupingBy` donne `{false=[1, 2]}`, sans clé `true`.

**Deux collecteurs imbriqués :** `partitioningBy(valid, mapping(user, toCollection(TreeSet::new)))`. Chaque côté devient l'ensemble trié des noms.

---

## Étape 3 — Le meilleur artiste de chaque genre

**Le code :** `topArtistPerGenre`.

**`collectingAndThen(collecteur, fonction)`** applique une fonction **finale** au résultat du collecteur. Ici, la sous-table artiste → nombre devient le texte du meilleur artiste, dans le **même** `collect`.

**Pop, à la main :** parmi les écoutes **validées**, Adele en a 2 (lea et ines ; celle de tom dure 8 s, elle est zappée), et Dua aussi (tom deux fois ; celle de lea dure 12 s). C'est une égalité à 2, et Adele passe en premier dans l'alphabet.

**Le piège :** `max` garde le **plus grand**. Pour que le premier nom de l'alphabet gagne, il doit être le plus grand pour le comparateur, d'où `comparingByKey().reversed()` en départage.

---

## Étape 4 — Filtrer dans ou avant le groupement ?

**Le code :** la ligne `TEMPS VALIDE PAR UTILISATEUR`.

**Le piège central, vérifié** sur lea (100 s) et zoe (10 s) :
- avec `filter` **avant** `groupingBy`, on obtient `{lea=100}` : les écoutes de zoe sont retirées **avant** le groupement, et sa clé n'existe jamais ;
- avec `filtering` **en aval**, on obtient `{lea=100, zoe=0}` : le groupe zoe est créé, puis filtré à l'intérieur, et il reste vide (une somme de 0).

---

## Étape 5 — Aplatir dans un groupe

**Le code :** la ligne `AMBIANCES PAR GENRE`.

**`mapping` contre `flatMapping`** (Java 9) :
- `mapping(p -> p.tags(), toSet())` donnerait un **ensemble de listes** ;
- `flatMapping(p -> p.tags().stream(), …)` met toutes les ambiances au **même niveau**, comme `flatMap` dans un stream.

---

## Étape 6 — Extrêmes et réductions par groupe

**Le code :** les lignes `ECOUTE LA PLUS LONGUE` et `SECONDES PAR GENRE`.

**Question — un groupe peut-il être vide ?** Non : `groupingBy` ne crée un groupe que pour une clé **rencontrée**, donc avec au moins un élément. Mais `maxBy` est un collecteur **général**, utilisable sur n'importe quel flux, y compris vide. Son type de retour est donc toujours `Optional`. `collectingAndThen(…, o -> o.map(Play::title).orElseThrow())` le déballe, avec un `orElseThrow()` qui ne se déclenche jamais ici.

**`reducing(0, Play::seconds, Integer::sum)`** : c'est la version « collecteur » d'un `reduce` à 3 arguments, l'équivalent de `summingInt`.

---

## Étape 7 — `toMap` et ses pièges

**Le code :** la ligne `TOP 3 TITRES`.

**Sans fonction de fusion**, une clé en double lève une exception (vérifiée) :

```
java.lang.IllegalStateException: Duplicate key Hello (attempted merging values 1 and 1)
```

« Hello » est écouté 3 fois. La version à 4 arguments donne la fusion (`Integer::sum`) **et** la fabrique de la `Map` (`TreeMap::new`).

---

## Étape 8 — Statistiques et `teeing`

**Le code :** `STATS`, `EXTREMES` et `MOYENNE VALIDEE`.

**`teeing(c1, c2, fusion)`** (Java 12) envoie chaque élément à **deux** collecteurs, en un seul passage, puis fusionne leurs résultats.

**Pourquoi lea et pas ines ?** `maxBy` garde le **premier** des éléments maximaux (comme `BinaryOperator.maxBy`). Vérifié : sur `lea, ines`, tous deux à 562, `maxBy` rend `lea`. Elle apparaît **avant** ines dans les données.

**Piège de compilation — `println(stream.collect(teeing(…)))`** (vérifié) :

```
error: reference to println is ambiguous
error: incompatible types: inference variable R has incompatible bounds
```

Le type `R` de `teeing` est **inféré à partir de la cible**. Or `println` a plusieurs surcharges (`String`, `char[]`, `Object`…), et `javac` ne peut pas choisir la cible et inférer R en même temps. **Correction :** ranger le résultat dans une variable `String`. La cible est alors connue, et R vaut `String`.

---

## Étape 9 — Les explorateurs

**Le code :** `explorers`.

Il y a 4 genres en tout. Seule lea a des écoutes **validées** dans les 4 : `[lea]`.

---

## Étape 10 — Les recommandations : l'algorithme

**Le code :** `jaccard` et `recommendations`.

**lea et ines, à la main :**
- lea (validées) : Adele, Daft, Miles, Muse, Nina, Queen. ines : Adele, Miles, Nina, Queen.
- Intersection : 4 artistes. Union : 6. 4/6 = 0.666…, arrondi à **67 %**.
- Tout ce qu'a ines, lea l'a déjà : `rien de nouveau`.

**zoe :** ses deux écoutes sont zappées, et elle n'est **pas** dans la table des goûts (`get` rend `null`). C'est pour ça que la boucle part des utilisateurs de **toutes** les écoutes.

**Question — la complexité :** chaque utilisateur est comparé à **tous** les autres. Avec n utilisateurs, cela fait O(n²) calculs de Jaccard. Pour un million d'utilisateurs, ce serait 10¹² comparaisons, impossible. Il faudrait ne comparer qu'aux utilisateurs qui partagent **au moins un artiste** (un index inverse artiste → utilisateurs), ou utiliser des techniques d'approximation comme MinHash et LSH.
