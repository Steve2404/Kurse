# Projet 4 — Le classement d'un championnat (sans `Collectors` !)

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**API visée :** la réduction sous toutes ses formes :
- les **trois** `reduce` : `reduce(identité, accumulateur)`, `reduce(accumulateur)` → `Optional`, et `reduce(identité, accumulateur, combiner)` ;
- le `collect` à **trois** arguments (`supplier`, `accumulator`, `combiner`) ;
- un `Collector` **écrit par toi** avec `Collector.of` ;
- les quatre morceaux d'un `Collector` (`supplier`, `accumulator`, `combiner`, `finisher`), que tu appelleras **toi-même** pour vérifier qu'une réduction est juste.

**Contrainte du projet :** la classe `Collectors` est **interdite** (`Check` la refuse). Tout passe par `reduce`, `collect` ou ton propre `Collector`. `Stream.toList()` reste autorisé.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch10_streams.projects.p04_league`. La classe du `main` s'appelle **`League`**.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch10-p04 -sourcepath src/main/java src/main/java/ch10_streams/projects/p04_league/League.java
java "-Duser.language=fr" -cp build/ch10-p04 ch10_streams.projects.p04_league.League
```

**Ce projet t'apprend les réductions** : comment un stream « résume » tous ses éléments en un seul résultat.

---

## Le problème

Quatre équipes jouent un championnat aller-retour : 12 matchs, sur 6 journées (`Data.MATCHES`). Ton programme imprime les résultats, des statistiques, le classement officiel avec ses départages et les séries sans défaite.

Il **prouve** ensuite que chaque réduction reste juste quand on **coupe la saison en deux morceaux**, qu'on réduit chaque morceau séparément, puis qu'on les **fusionne avec le combiner**. C'est le cœur du projet : une réduction peut donner le bon résultat d'une traite et un faux résultat par morceaux, si l'identité n'est pas neutre ou si le combiner est mal écrit. Au chapitre 13, les streams parallèles feront exactement ce découpage automatiquement. Ici, tu le fais à la main, et tu comprendras donc pourquoi.

---

## Tableau de bord

### ☐ Étape 1 — Le modèle

**📖 La leçon : réduire, c'est combiner deux par deux.** `reduce` combine les éléments **deux par deux** jusqu'à n'en garder qu'un, comme on additionne une colonne de chiffres :

```java
List.of(3, 5, 2).stream().reduce(0, Integer::sum)     // 0 + 3 + 5 + 2 = 10
```

Le `0` est l'**identité** : la valeur de départ, qui ne change rien quand on la combine (0 pour l'addition, 1 pour la multiplication).

**👉 À toi :**

- **Un match :** journée, deux équipes, deux scores. Il sait donner son nombre de buts, son écart et s'il concerne une équipe.
- **Le match vu par une équipe.** Pour le classement, un match doit pouvoir se transformer en « bilan d'une équipe pour ce match » : joué, gagné, nul, perdu, buts pour, buts contre.
- **Le bilan d'une équipe** est un record qui sait **s'additionner** à un autre bilan. Il a un bilan **zéro**.
  - **Question clé :** pour être l'identité d'un `reduce`, quelles deux propriétés doivent avoir ton « zéro » et ton « plus » ? Écris-les en commentaire au-dessus du record. On les appelle un *monoïde*.
  - Il calcule aussi ses points (`Data.POINTS_WIN`, `Data.POINTS_DRAW`) et sa différence de buts.

### ☐ Étape 2 — `RESULTATS` : `collect` à 3 arguments

```
RESULTATS : Lions 3-1 Tigres | Ours 0-0 Aigles | ...
```

**📖 La leçon : `collect` à 3 arguments, remplir un conteneur.** Au lieu de fabriquer un nouvel objet à chaque étape (comme `reduce`), `collect` **remplit** un conteneur modifiable :
1. le **fournisseur** crée un conteneur vide ;
2. l'**accumulateur** ajoute un élément au conteneur ;
3. le **combiner** fusionne deux conteneurs (utile quand le travail est découpé en morceaux).

```java
StringBuilder sb = Stream.of("a", "b", "c")
        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);   // abc
```

**👉 À toi :**

- Construis la ligne avec `stream.collect(StringBuilder::new, accumulateur, combiner)`. Le séparateur ` | ` n'apparaît qu'**entre** deux matchs.
- **Piège du combiner :** si ton combiner est `StringBuilder::append`, que devient la ligne à la jonction de deux morceaux ? Écris le combiner correct.
  - **Conception :** donne un **nom** à l'accumulateur et au combiner (des constantes typées `BiConsumer<…>`, chapitre 8). Tu en auras besoin à l'étape 9 pour fusionner deux morceaux à la main.
- **Question :** pourquoi `collect` (réduction **mutable**) convient-il ici mieux que `reduce("", (a, m) -> a + ...)` ?

### ☐ Étape 3 — `BUTS` : `reduce` à 3 arguments

```
BUTS : 35 en 12 matchs, moyenne 2.92
```

**📖 La leçon : `reduce` à 3 arguments.** Quand les éléments et le résultat n'ont **pas le même type**, il faut 3 morceaux : l'identité, l'accumulateur (résultat + élément → résultat) et le combiner (résultat + résultat → résultat) :

```java
Stream.of("pain", "sel").reduce(0, (total, mot) -> total + mot.length(), Integer::sum)   // 7
```

**👉 À toi :**

- Le total se calcule avec **un** `reduce`, directement sur le `Stream<Match>`, sans `map` ni `mapToInt` avant.
- Les éléments sont des matchs et le résultat est un `int`. Pourquoi la forme à 2 arguments ne compile-t-elle pas ? À quoi sert le troisième argument, et quand est-il appelé ?
- **Piège :** remplace l'identité `0` par `10`. Que donne le passage unique ? Et deux morceaux réduits séparément puis additionnés ? Explique pourquoi les deux résultats diffèrent.
- Comme pour l'étape 2, donne un nom au combiner (`BinaryOperator<Integer>`).

### ☐ Étape 4 — `PLUS LARGE VICTOIRE` : `reduce` sans identité

```
PLUS LARGE VICTOIRE : J3 Lions 5-0 Ours (ecart 5)
```

**📖 La leçon : `reduce` sans identité.** `reduce(operateur)` n'a pas de valeur de départ. Il rend donc un `Optional`, vide si le stream l'est :

```java
List.of(3, 5, 2).stream().reduce(Integer::max)     // Optional[5]
Stream.<Integer>empty().reduce(Integer::max)       // Optional.empty
```

**👉 À toi :**

- Les matchs nuls ne comptent pas.
- **Contrainte :** un `reduce(accumulateur)`. Il rend un `Optional`. Pourquoi Java ne peut-il pas rendre directement un `Match` ?
- À égalité d'écart, garde le match **le plus ancien**. Ton opérateur doit rester **associatif**, sinon le résultat dépendrait de la façon de découper la saison. Vérifie-le sur trois matchs à égalité.
- S'il n'y a aucune victoire, affiche `PLUS LARGE VICTOIRE : aucune`.

### ☐ Étape 5 — `CLASSEMENT` : ton propre `Collector`

```
CLASSEMENT
1. Lions 11 pts (G3 N2 P1) 14:6 +8
2. Aigles 11 pts (G3 N2 P1) 9:7 +2
3. Tigres 9 pts (G3 N0 P3) 10:8 +2
4. Ours 2 pts (G0 N2 P4) 2:14 -12
```

**📖 La leçon : écrire ton propre `Collector`.** `Collector.of` assemble les quatre morceaux d'une collecte : fournisseur, accumulateur, combiner, et un **finisher** qui transforme le conteneur en résultat final.

```java
Collector<String, StringBuilder, String> colle = Collector.of(
        StringBuilder::new,          // fournisseur
        StringBuilder::append,       // accumulateur
        StringBuilder::append,       // combiner
        StringBuilder::toString);    // finisher
Stream.of("x", "y", "z").collect(colle)     // "xyz"
```

**👉 À toi :**

Écris un `Collector<Match, ?, List<…>>` avec `Collector.of`, en quatre parties :
- **le conteneur mutable** : une table équipe → bilan ;
- **l'accumulateur** : un match met à jour **deux** équipes. Quelle méthode de `Map` ajoute ou cumule en un seul appel ?
- **le combiner** : il fusionne deux tables partielles ;
- **le finisher** : il transforme la table en liste triée.

Les départages, dans cet ordre :
1. les points ;
2. la différence de buts ;
3. les buts marqués ;
4. le nom.

Calcule à la main pourquoi Lions passe devant Aigles.

**Piège OCP :** `comparingInt(points).thenComparingInt(diff).reversed()` ne donne pas l'ordre voulu pour le nom. Pourquoi ?

### ☐ Étape 6 — `BILAN Lions` : `reduce` à 2 arguments

```
BILAN Lions (reduce) : 6 matchs, 11 pts, identique au classement : oui
```

**📖 Rappel :** `reduce(identite, operateur)` (étape 1), sur un stream qu'on a d'abord transformé avec `map`.

**👉 À toi :**

- Calcule le bilan des Lions sans ton `Collector`, avec `map` vers « bilan pour Lions » puis `reduce(zéro, plus)`.
- **Vérification croisée :** ce bilan doit être **égal** (`equals`) à la ligne Lions du classement. Pourquoi un record rend-il cette comparaison gratuite ?

### ☐ Étape 7 — `SERIES SANS DEFAITE` : l'algorithme fusionnable

```
SERIES SANS DEFAITE : Aigles 5, Lions 3, Ours 1, Tigres 1
```
- Pour chaque équipe, cherche la plus longue suite de matchs consécutifs **sans défaite**, dans l'ordre chronologique.
- Trie par série décroissante, puis par nom.
- **La vraie difficulté :** ce calcul doit être un `reduce` à 3 arguments **correct même par morceaux**.
  - Un compteur « série en cours » ne marche pas. Si le stream est coupé en deux au milieu d'une série, chaque moitié ne voit qu'un bout.
  - Il faut résumer un **morceau** de saison avec 4 nombres : sa longueur, sa série au **début**, sa série à la **fin** et sa meilleure série **interne**.
  - **À trouver :**
    - comment fusionner deux morceaux voisins (gauche + droite) en un seul résumé, sans revoir les matchs ? Indice : la série qui traverse la frontière vaut fin(gauche) + début(droite) ;
    - que valent début et fin quand **tout** un morceau est sans défaite ?
    - quel est le résumé neutre (l'identité) ?
- **À la main :** Aigles fait N N V V V D. Découpe en `[N N V]` et `[V V D]`, résume chaque morceau, puis fusionne. Tu dois trouver 5.
- La liste finale se construit **sans** `Collectors.joining`. Quel `reduce` concatène des chaînes ?

### ☐ Étape 8 — `CONTROLE`

```
CONTROLE : 33 points distribues = 9 victoires x 3 + 3 nuls x 2 : oui
```
- **Le total des points du classement** : un `reduce` à 3 arguments sur les lignes du classement.
- **Le nombre de victoires** : un `reduce` sur les matchs.
- **Le contrôle :** un nul rapporte 1 point à **chacune** des deux équipes.

### ☐ Étape 9 — `COMBINER` : la preuve, à la main

```
COMBINER (5 + 7 matchs) : resultats oui, buts oui, classement oui, series oui
```
- Coupe la saison en deux morceaux inégaux à l'indice `Data.SPLIT_AT` (`subList`, chapitre 9) : 5 matchs à gauche et 7 à droite.
- Pour les étapes 2, 3, 5 et 7 :
  - réduis **chaque morceau séparément** ;
  - fusionne les deux résultats **avec le combiner de la réduction** ;
  - compare au résultat obtenu en un seul passage.
- **Pour ton `Collector` (étape 5) :** n'appelle pas `collect`. Appelle toi-même `supplier()`, `accumulator()`, `combiner()` puis `finisher()`, exactement ce qu'une implémentation de `collect` fait en interne.
  - **Question :** dans quel ordre, et combien de fois chacun ?
- Si une seule de ces réponses vaut `non`, ta réduction est fausse, même si la sortie d'un seul passage est juste.
- **Pour finir, casse volontairement chaque réduction une fois :**
  - mets une identité non neutre ;
  - mets un combiner qui ignore la gauche ;
  - mets un combiner sans séparateur.
  
  Observe à chaque fois. C'est exactement ce que l'examen OCP teste.

### ☐ Étape 10 — `main`

- Il charge les données et imprime le rapport dans l'ordre de la sortie attendue.

---

## Checklist API (vérifiée par `Check`)

| Méthode | Étape | ☐ |
|---|---|---|
| `collect(supplier, accumulator, combiner)` + `StringBuilder::new` | 2 | ☐ |
| `reduce(identité, acc, combiner)` | 3, 7, 8 | ☐ |
| `reduce(acc)` → `Optional` | 4, 7 | ☐ |
| `reduce(identité, acc)` | 6 | ☐ |
| `Collector.of` + type `Collector<…>` | 5 | ☐ |
| `supplier()`, `combiner()` d'un `Collector` | 9 | ☐ |
| au moins 5 `reduce` au total | — | ☐ |
| ~~`Collectors.*`~~ | **interdit** | — |

---

## Sortie attendue complète

```
RESULTATS : Lions 3-1 Tigres | Ours 0-0 Aigles | Aigles 2-2 Lions | Tigres 2-0 Ours | Lions 5-0 Ours | Tigres 1-2 Aigles | Tigres 2-1 Lions | Aigles 3-1 Ours | Ours 1-1 Lions | Aigles 2-1 Tigres | Lions 2-0 Aigles | Ours 0-3 Tigres
BUTS : 35 en 12 matchs, moyenne 2.92
PLUS LARGE VICTOIRE : J3 Lions 5-0 Ours (ecart 5)
CLASSEMENT
1. Lions 11 pts (G3 N2 P1) 14:6 +8
2. Aigles 11 pts (G3 N2 P1) 9:7 +2
3. Tigres 9 pts (G3 N0 P3) 10:8 +2
4. Ours 2 pts (G0 N2 P4) 2:14 -12
BILAN Lions (reduce) : 6 matchs, 11 pts, identique au classement : oui
SERIES SANS DEFAITE : Aigles 5, Lions 3, Ours 1, Tigres 1
CONTROLE : 33 points distribues = 9 victoires x 3 + 3 nuls x 2 : oui
COMBINER (5 + 7 matchs) : resultats oui, buts oui, classement oui, series oui
```
