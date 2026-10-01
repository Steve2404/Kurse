# Projet 6 — Le lecteur parallèle d'un journal de caisse (ton propre `Spliterator`)

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**API visée :** l'interface `Spliterator` :
- `tryAdvance`, `forEachRemaining`, `trySplit`, `estimateSize`, `getExactSizeIfKnown`, `characteristics` et `hasCharacteristics` ;
- les constantes `ORDERED`, `SIZED`, `SUBSIZED`, etc. ;
- `StreamSupport.stream`, qui transforme **ton** spliterator en `Stream`, séquentiel ou parallèle.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch10_streams.projects.p06_spliterator`. Cela inclut **une classe qui implémente `Spliterator<…>`**. La classe du `main` s'appelle **`CashJournal`**.

---

## Le problème

La caisse d'une librairie écrit un journal texte (`Data.LOG`). Une **transaction** s'étale sur plusieurs lignes : un en-tête `TX`, puis des lignes indentées d'achat (`+`) ou de retour (`-`). Des commentaires (`#`) traînent au milieu, et une ligne est illisible.

Une `List<String>` sait faire un stream de **lignes**. Toi, tu veux un stream de **transactions**. Aucun opérateur standard ne regroupe « un en-tête et ses lignes suivantes ». Il faut donc écrire la **source** elle-même : un `Spliterator<Transaction>`.

De plus, le journal réel fait des millions de lignes. Ton spliterator doit donc savoir **se couper en deux** pour un traitement parallèle, sans jamais couper une transaction au milieu.

---

## Tableau de bord

### ☐ Étape 1 — Le modèle

- **Un article :** une quantité (négative pour un retour), un nom et un prix unitaire.
- **Une transaction :** un id, un client, ses articles et ses lignes illisibles.
- **Décision imposée : les montants en centimes, dans un `long`.**
  - **Question :** en parallèle, une somme de `double` peut donner un résultat différent selon le découpage. Pourquoi ? Indice : `(a + b) + c` n'est pas toujours égal à `a + (b + c)` en virgule flottante.
  - Comment transformer `"12.50"` en `1250` sans `double` ?

### ☐ Étape 2 — `tryAdvance` : lire UNE transaction

Ton spliterator travaille sur une **plage** `[début, fin)` d'indices de la liste de lignes.

- `tryAdvance(action)` :
  - saute ce qui n'est pas un en-tête (commentaires, lignes orphelines) ;
  - lit l'en-tête, puis toutes les lignes suivantes **jusqu'au prochain en-tête ou la fin de la plage** ;
  - construit **une** transaction et la donne à `action` ;
  - rend `true`. S'il ne reste plus d'en-tête, il rend `false` sans appeler `action`.
- Une ligne indentée qui n'a pas la forme `± qte x article a prix` va dans les lignes illisibles. Elle ne fait pas planter le programme.
- **Question :** pourquoi le contrat dit-il « **au plus** un élément » par appel ? Que casserait un `tryAdvance` qui en traiterait deux ?

```
TX 1001 lea : 3 article(s), 34.90
TX 1003 ines : vide
TX 1008 ines : annulee (0 article)
ANOMALIE TX 1005 : ligne illisible "  + 2 x"
```
- **Les trois cas d'une transaction :**
  - `vide` : aucune ligne d'article ;
  - `annulee` : des articles, mais la quantité totale vaut 0 ;
  - sinon : le nombre d'articles et le total.
- Les anomalies s'affichent **après** toutes les transactions.

### ☐ Étape 3 — `estimateSize` et `characteristics`

- `estimateSize()` : tu ne sais pas combien de **transactions** il reste, seulement combien de **lignes**. Ce majorant est autorisé. Relis la Javadoc pour savoir à quoi il sert.
- `characteristics()` : choisis les bonnes constantes, avec l'opérateur `|`.
  - `ORDERED` : oui. L'ordre du journal compte.
  - `SIZED` : **non**. Pourquoi serait-ce un mensonge, et qu'est-ce qui casserait ? Pense à `count()` et `toArray()`.
  - `NONNULL`, `IMMUTABLE` : justifie ton choix.

### ☐ Étape 4 — En faire un `Stream` et compter

```
COMPTE : 8 transactions (sequentiel) / 8 (parallele)
CA TOTAL : 119.25 (parallele identique : oui)
MEILLEUR CLIENT : hugo (58.90)
```
- `StreamSupport.stream(tonSpliterator, parallèle?)` donne un `Stream<Transaction>` ordinaire. Tous les opérateurs des projets précédents marchent dessus.
- **À tester toi-même :** avec un `trySplit` qui rend toujours `null`, le parallèle est-il faux ? Lent ?

### ☐ Étape 5 — `trySplit` : l'algorithme de découpe

```
DECOUPAGE : [1001] [1002] [1003 1004] [1005] [1006] [1007] [1008]
```
- `trySplit()` coupe **ta** plage en deux. Il **rend** la première moitié (le préfixe) dans un **nouveau** spliterator, et **garde** la seconde.
  - **Question :** pourquoi le préfixe et pas le suffixe, puisque tu es `ORDERED` ?
- **Les règles :**
  - moins de `Data.MIN_SPLIT_LINES` lignes dans la plage : on ne coupe pas (`null`) ;
  - sinon, on vise le milieu des lignes. Si ce n'est pas un en-tête, on **avance** la coupe jusqu'au prochain en-tête ;
  - si on atteint la fin de la plage, on ne coupe pas (`null`).
- **DECOUPAGE :** coupe **récursivement** le spliterator du journal entier tant que `trySplit` accepte. Affiche ensuite les ids de chaque morceau final, de gauche à droite.
  - Pour lister un morceau, vide-le avec `forEachRemaining`.
- **À la main :**
  - les 22 lignes vont des indices 0 à 21. La première coupe vise 11, qui est `TX 1005`. Continue ;
  - pourquoi `[1003 1004]` reste-t-il un seul morceau, alors qu'il fait 5 lignes ?

### ☐ Étape 6 — `tryAdvance` puis `forEachRemaining`

```
PREMIERE (tryAdvance) : 1001, RESTE (forEachRemaining) : 7, ENCORE : false
```
- Sur un **même** spliterator neuf :
  - `tryAdvance` lit la première transaction ;
  - `forEachRemaining` compte les autres ;
  - un dernier `tryAdvance` rend `false`.
- **Question :** un spliterator, comme un stream, est à usage unique. Comment le vois-tu ici ?

### ☐ Étape 7 — Répartir des lots avec le spliterator d'une `List`

```
LOTS : [J01, J02] [J03, J04, J05] [J06, J07] [J08, J09, J10]
```
- Répartis `Data.JOBS` en lots d'au plus `Data.MAX_BATCH` commandes. Coupe **récursivement** le spliterator de la liste avec `trySplit`, tant que `estimateSize()` dépasse la limite.
- Ne calcule **aucun** indice toi-même : c'est la liste qui décide où elle coupe.
- **À la main :** pourquoi 10 éléments donnent-ils `2-3-2-3` et pas `3-3-3-1` ?

### ☐ Étape 8 — Lire les caractéristiques

```
CARACTERISTIQUES ArrayList : ORDERED SIZED SUBSIZED (taille exacte 10)
...
CARACTERISTIQUES journal : ORDERED NONNULL IMMUTABLE (taille exacte -1)
```
Pour chacune des 6 sources de la sortie attendue, affiche ses caractéristiques avec `hasCharacteristics`, puis `getExactSizeIfKnown()`.

- **L'ordre des mots :** les noms s'affichent dans l'ordre **croissant de la valeur numérique** des constantes. Consulte leurs valeurs dans la Javadoc, ou affiche-les.
- **Les 6 sources :**
  - une `ArrayList` ;
  - un `HashSet` ;
  - un `TreeSet` ;
  - un `Stream.iterate` infini, via `.spliterator()` ;
  - `JOBS.stream().sorted(Comparator.reverseOrder())` ;
  - ton journal.
- **Questions :**
  - pourquoi le `HashSet` n'est-il pas `ORDERED` ?
  - pourquoi le flux trié en ordre **inverse** n'est-il pas `SORTED`, alors que le `TreeSet` l'est ? Indice : relis la définition de `SORTED`, et pense à `getComparator()`.
  - que signifie `-1` ?

### ☐ Étape 9 — `main`

- Il imprime tout dans l'ordre de la sortie attendue.

---

## Checklist API (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `implements Spliterator<…>` | 2 | ☐ |
| `tryAdvance` | 2, 6 | ☐ |
| `estimateSize`, `characteristics` | 3 | ☐ |
| `Spliterator.ORDERED`, `SIZED`, `SUBSIZED` | 3, 8 | ☐ |
| `StreamSupport.stream` | 4 | ☐ |
| `trySplit` | 5, 7 | ☐ |
| `forEachRemaining` | 5, 6, 7 | ☐ |
| `.spliterator()` d'une collection / d'un stream | 7, 8 | ☐ |
| `hasCharacteristics`, `getExactSizeIfKnown` | 8 | ☐ |

---

## Sortie attendue complète

```
TX 1001 lea : 3 article(s), 34.90
TX 1002 hugo : 3 article(s), 24.00
TX 1003 ines : vide
TX 1004 tom : 4 article(s), 19.25
TX 1005 lea : 1 article(s), 1.20
TX 1006 hugo : 3 article(s), 34.90
TX 1007 zoe : 10 article(s), 5.00
TX 1008 ines : annulee (0 article)
ANOMALIE TX 1005 : ligne illisible "  + 2 x"
COMPTE : 8 transactions (sequentiel) / 8 (parallele)
CA TOTAL : 119.25 (parallele identique : oui)
MEILLEUR CLIENT : hugo (58.90)
DECOUPAGE : [1001] [1002] [1003 1004] [1005] [1006] [1007] [1008]
PREMIERE (tryAdvance) : 1001, RESTE (forEachRemaining) : 7, ENCORE : false
LOTS : [J01, J02] [J03, J04, J05] [J06, J07] [J08, J09, J10]
CARACTERISTIQUES ArrayList : ORDERED SIZED SUBSIZED (taille exacte 10)
CARACTERISTIQUES HashSet : DISTINCT SIZED (taille exacte 10)
CARACTERISTIQUES TreeSet : DISTINCT SORTED ORDERED SIZED (taille exacte 10)
CARACTERISTIQUES Stream.iterate : ORDERED IMMUTABLE (taille exacte -1)
CARACTERISTIQUES sorted() : ORDERED SIZED SUBSIZED (taille exacte 10)
CARACTERISTIQUES journal : ORDERED NONNULL IMMUTABLE (taille exacte -1)
```
