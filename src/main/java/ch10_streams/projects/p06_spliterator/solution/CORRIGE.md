# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`CashJournal.java`](CashJournal.java).
>
> Les valeurs et les exceptions ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18), sur la solution ou sur des copies modifiées.

---

## Étape 1 — Le modèle

**Le code :** les records `Item` et `Transaction`.

**Question — pourquoi des `double` additionnés par morceaux peuvent différer ?** L'addition flottante n'est **pas associative** : chaque opération arrondit. Vérifié : `(0.1 + 0.2) + 0.3` donne `0.6000000000000001`, et `0.1 + (0.2 + 0.3)` donne `0.6`. Un total calculé en un seul passage et un total recombiné à partir de morceaux peuvent donc différer. Les centimes en `long` sont **exacts**, quel que soit l'ordre.

---

## Étape 2 — `tryAdvance` : lire UNE transaction

**Le code :** `tryAdvance` et `parseItem` de `TransactionSpliterator`.

**Les trois cas :**
- **1003** n'a aucune ligne d'article : `vide`.
- **1008** a +1 et −1 Hobbit, pour une quantité totale de 0 : `annulee`.
- **1005** contient `  + 2 x`, qui n'a pas les 6 mots attendus : la ligne va dans les anomalies, sans planter.

**Question — pourquoi « au plus » un élément ?** `tryAdvance` est l'unité de travail du stream. Les opérations comme `findFirst`, `limit` ou `anyMatch` comptent sur lui pour s'arrêter **exactement** quand elles ont ce qu'il leur faut. Un `tryAdvance` qui pousserait deux transactions ferait traiter un élément de trop : un `limit(1)` en recevrait deux, et un `findFirst` aurait déjà déclenché l'action sur le suivant.

---

## Étape 3 — `estimateSize` et `characteristics`

**Le code :** `estimateSize` et `characteristics`.

**`estimateSize` est une estimation :** elle sert à décider s'il vaut la peine de **découper** (en parallèle). Un majorant convient, tant qu'on ne promet pas l'exactitude.

**Pourquoi `SIZED` serait un mensonge :** `SIZED` promet que `estimateSize()` est le nombre **exact** d'éléments. Or c'est un nombre de **lignes** (22), pas de transactions (8). Vérifié sur une copie qui déclare `SIZED` :
- `count()` rend **22**, sans même parcourir le flux ;
- `toArray()` lève `IllegalStateException: End size 8 is less than fixed size 22` : il a réservé 22 cases et n'en a rempli que 8.

**`NONNULL`** : `tryAdvance` ne fournit jamais `null`. **`IMMUTABLE`** : la liste de lignes ne change pas pendant le parcours.

---

## Étape 4 — En faire un `Stream`

**Le code :** `transactions`.

**`StreamSupport.stream(spliterator, false)`** enveloppe **ta** source dans un `Stream` ordinaire. Tout le reste du chapitre (groupements, collecteurs…) fonctionne dessus, sans rien savoir du format du journal.

---

## Étape 5 — `trySplit` : l'algorithme de découpe

**Le code :** `trySplit`, `range` et `leaves`.

**Question — pourquoi rendre le préfixe ?** Avec `ORDERED`, l'ordre de rencontre doit être préservé. La convention de `trySplit` : le spliterator **rendu** couvre les éléments qui viennent **avant** ceux qu'on garde. Un traitement parallèle peut ainsi recombiner les résultats dans l'ordre : gauche (le rendu), puis droite (le gardé).

**La découpe à la main** (22 lignes, indices 0 à 21) :

| Plage | Milieu | Coupe |
|---|---|---|
| [0, 22) | 11 = `TX 1005` | [0, 11) et [11, 22) |
| [0, 11) | 5 = `#` : on avance jusqu'à 6 = `TX 1003` | [0, 6) et [6, 11) |
| [0, 6) | 3 = `TX 1002` | [0, 3) = 1001 et [3, 6) = 1002 |
| [6, 11) | 8 = un article de 1004 : on avance, et on atteint 11 = la fin | **aucune coupe** |
| [11, 22) | 16 = un article de 1006 : on avance jusqu'à 17 = `TX 1007` | [11, 17) et [17, 22) |
| [11, 17) | 14 = `TX 1006` | 1005 et 1006 |
| [17, 22) | 19 = `TX 1008` | 1007 et 1008 |

**Pourquoi `[1003 1004]` reste un seul morceau :** sa plage [6, 11) a bien 5 lignes (≥ 4), mais à partir du milieu (8) il n'y a plus d'en-tête avant la fin. Couper là séparerait les articles de 1004 de leur en-tête.

**La copie dans `range()` :** `forEachRemaining` **consomme** le spliterator. Sans copie, le morceau serait vide après l'affichage, et la vérification COMPTE ne trouverait plus rien.

**À tester — couper une ligne trop loin :** vérifié sur une copie qui coupe **après** l'en-tête trouvé. La découpe devient `[1001] [1002] [1003] [1004] [1005] [1006] [] [1007] [1008]`. Le compte reste à 8, par chance, car chaque en-tête est encore lu une fois. Mais `CA TOTAL … identique : non` : des articles se retrouvent **séparés de leur en-tête**, dans le morceau suivant, où ils sont ignorés comme orphelins. Une coupe ne doit jamais tomber au milieu d'un élément.

---

## Étape 6 — `tryAdvance` puis `forEachRemaining`

**Le code :** le bloc `PREMIERE` du `main`.

**Question — l'usage unique, visible ici :** après `tryAdvance` (1 transaction) et `forEachRemaining` (les 7 autres), le spliterator est **épuisé**. Un nouveau `tryAdvance` rend `false`. Pour relire, il faut un **nouveau** spliterator. C'est pour ça que `transactions()` en crée un à chaque appel.

---

## Étape 7 — Répartir des lots avec le spliterator d'une `List`

**Le code :** `batches`.

**Pourquoi `2-3-2-3` et pas `3-3-3-1` ?** Le spliterator d'une `ArrayList` coupe **au milieu** :
- 10 éléments donnent 5 et 5 ;
- chaque 5 (> 3) donne 2 et 3.

On obtient 2, 3, 2, 3. Le découpage par le milieu donne des morceaux **équilibrés**, ce qui est l'objectif en parallèle. « Remplir des lots de 3 » serait un autre algorithme.

---

## Étape 8 — Lire les caractéristiques

**Le code :** `FLAGS` et `describe`.

**Les valeurs des constantes (vérifiées) :** ORDERED = 16, DISTINCT = 1, SORTED = 4, SIZED = 64, NONNULL = 256, IMMUTABLE = 1024, CONCURRENT = 4096, SUBSIZED = 16384. Triées par valeur : DISTINCT, SORTED, ORDERED, SIZED, NONNULL, IMMUTABLE, CONCURRENT, SUBSIZED.

**Questions :**
- **`HashSet` pas `ORDERED` :** ses éléments n'ont **aucun ordre défini**, celui du parcours dépend des codes de hachage. Il est `DISTINCT` (un `Set`) et `SIZED`.
- **Le flux trié en ordre inverse pas `SORTED` :** `SORTED` signifie que l'ordre suit l'ordre naturel, ou le comparateur rendu par `getComparator()`. Le pipeline d'un stream ne garde le drapeau `SORTED` que pour un tri par **ordre naturel**. Avec `sorted(Comparator.reverseOrder())`, il ne peut pas exposer le comparateur, et le drapeau disparaît. Le `TreeSet`, lui, est `SORTED`, et son `getComparator()` rend son comparateur (`null` pour l'ordre naturel).
- **`-1`** : `getExactSizeIfKnown()` rend −1 quand le spliterator n'est **pas `SIZED`** : la taille exacte est inconnue. C'est le cas du flux infini et du journal.
