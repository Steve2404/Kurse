# Drills du chapitre 10 — mode d'emploi et plan de révision

Les **exercices** (`ch10_streams/exercises`, 01 → 25) t'apprennent les notions.
Les **drills** (`ch10_streams/drills/exercises`, 01 → 13) te les font répéter
jusqu'à ce que les méthodes sortent toutes seules. Tous les drills utilisent le
même petit projet : la bibliothèque de `drills/Library.java` (8 livres,
4 membres, 8 emprunts). Lis ce fichier une fois et garde-le ouvert à côté.

| Drill | API couverte | TODO | À faire après les exercices |
|---|---|---|---|
| 01 `OptionalApi` | toutes les méthodes de `Optional<T>` | 19 | 01 – 03 |
| 02 `OptionalPrimitiveApi` | `OptionalInt` / `OptionalLong` / `OptionalDouble` | 14 | 01 – 03 |
| 03 `StreamCreation` | `of`, `ofNullable`, `iterate`, `generate`, `concat`, `builder`, `Arrays.stream`, `chars`… | 18 | 04 – 06 |
| 04 `StreamIntermediateOps` | `filter`, `map`, `flatMap`, `distinct`, `sorted`, `peek`, `limit`, `skip`, `takeWhile`, `dropWhile`, `mapToXxx`… | 17 | 07 – 11 |
| 05 `ComparatorApi` | `comparing*`, `thenComparing*`, `reversed`, `nullsFirst/Last`, `Map.Entry.comparingBy*` | 15 | 09 – 10 |
| 06 `StreamTerminalOps` | `count`, `min/max`, `find*`, `*Match`, `forEach*`, `toArray`, `toList`, `iterator` | 17 | 12 |
| 07 `PrimitiveStreamApi` | `IntStream` / `LongStream` / `DoubleStream`, statistiques, conversions | 17 | 13 – 15 |
| 08 `ReduceAndCollectApi` | les 3 `reduce`, `collect` à 3 arguments, `Collectors.reducing` | 13 | 16 – 17 |
| 09 `CollectorsBasicsApi` | `toList/Set/Collection`, `toUnmodifiable*`, `joining`, `counting`, `summing*`, `averaging*`, `minBy/maxBy`, `summarizing*` | 21 | 18 |
| 10 `CollectorsToMapApi` | toutes les formes de `toMap` / `toUnmodifiableMap` | 12 | 18 |
| 11 `GroupingPartitioningTeeingApi` | `groupingBy`, `partitioningBy`, `mapping`, `filtering`, `flatMapping`, `collectingAndThen`, `teeing` | 19 | 19 – 23 |
| 12 `MixedKata` | **tout, sans indice** : 20 questions métier | 20 | 25 |
| 13 `AdvancedPipelineConcepts` | stream lié à sa source, exceptions vérifiées dans les lambdas, `onClose`, derniers outils primitifs | 15 | 24 |

Les corrigés sont dans `drills/solutions/SolutionDrillNN_*.java` (et ceux des exercices dans
`solutions/`). Ils sont **commentés** : chaque méthode explique pourquoi on a choisi cet outil
et quel piège il évite. Lis-les **après** avoir réussi, jamais avant.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.**

- L'**exercice** sert à **comprendre** (histoire, calcul à la main, plan). On commence toujours par lui.
- Le **drill** sert à **mémoriser**. On le fait **le lendemain** des exercices du même thème,
  quand il faut déjà un petit effort pour se souvenir. C'est cet effort qui fixe la mémoire.

Ne fais **pas** les 25 exercices puis les 13 drills : au moment d'arriver aux drills
sur Optional, tu aurais déjà oublié la moitié.

### Le parcours, étape par étape

| Étape | Thème | Jour 1 : comprendre (exercices) | Jour 2 : mémoriser (drills) |
|---|---|---|---|
| 1 | Optional | Exercise01 → 02 → 03 | Drill01, Drill02 |
| 2 | Pipeline, paresse, sources | Exercise04 → 05 → 06 | Drill03 |
| 3 | Opérations intermédiaires, tri, terminales | Exercise07 → 08 → 09 → 10 → 11 → 12 | Drill04, Drill05, Drill06 |
| 4 | Streams primitifs | Exercise13 → 14 → 15 | Drill07 |
| 5 | reduce / collect | Exercise16 → 17 | Drill08 |
| 6 | Collectors | Exercise18 → 19 → 20 → 21 → 22 → 23 | Drill09, Drill10, Drill11 |
| 7 | Spliterator et concepts avancés | Exercise24 | Drill13 |
| 8 | Synthèse | Exercise25 (capstone) | Drill12 (kata mélangé) |

Les étapes 3 et 6 sont longues : étale-les sur 2 ou 3 jours si besoin.

**Une séance type (environ 1 h) :**
1. **D'abord les révisions dues** (10 – 20 min) : les drills déjà réussis dont la date
   J+1 / J+3 / J+7… tombe aujourd'hui (voir le tableau de suivi en bas).
2. **Ensuite, la nouveauté** : les exercices ou le drill de l'étape en cours.
3. **Pour finir, 2 minutes de « carte vierge »** : sur une feuille, écris de mémoire les
   méthodes vues aujourd'hui.

Compte environ 3 semaines pour les 8 étapes. Les révisions espacées continuent ensuite,
de moins en moins souvent, jusqu'à l'examen.

---

## Comment faire un drill

1. Lance un chronomètre.
2. Remplis les TODO **sans regarder la « CARTE MÉMOIRE »** en bas du fichier.
3. Bloqué plus d'une minute ? Regarde la carte, **cache-la, puis réécris la ligne
   de mémoire**. Mets une croix à côté de ce TODO : c'est un point faible.
4. Lance `main()` jusqu'à obtenir 100 %.
5. Note ton temps, ton score au premier lancement et tes TODO « croix » dans le
   tableau de suivi ci-dessous.
6. Seulement ensuite, compare avec le corrigé : il y a souvent une écriture plus
   courte.

## Pourquoi tu oublies, et comment ne plus oublier

On oublie ce qu'on a seulement **relu**. On retient ce qu'on a dû **retrouver de
mémoire**, plusieurs fois, en espaçant les séances. D'où trois règles.

**1. Rappel actif.** Refaire un drill depuis une page blanche vaut dix relectures
du corrigé. L'effort pour retrouver une méthode, c'est justement ce qui la fixe.

**2. Répétition espacée.** Refais chaque drill selon ce calendrier, en comptant
à partir du jour où tu l'as réussi pour la première fois :

| Séance | Quand |
|---|---|
| 1 | Jour J (première réussite) |
| 2 | J + 1 |
| 3 | J + 3 |
| 4 | J + 7 |
| 5 | J + 14 |
| 6 | J + 30 |
| ensuite | tous les 2 mois, ou avant l'examen |

Un drill refait à 100 % du premier coup et en moins de 10 minutes peut passer à
l'étape suivante. Sinon, refais-le le lendemain, puis reprends le calendrier.

**3. Mélange.** Après les drills « une API à la fois » (01 – 11 et 13), le drill 12
mélange tout. C'est lui qui t'apprend à **choisir** la bonne méthode. Refais-le
chaque semaine pendant la révision de l'examen.

**Petits plus qui marchent :**
- **Carte vierge.** Sur une feuille, sans rien regarder, écris toutes les méthodes
  de `Optional` (ou de `Collectors`…) avec leur type de retour. Compare ensuite
  avec la carte mémoire du drill. Deux minutes, très efficace.
- **À voix haute.** Explique pourquoi `orElse` exécute toujours son argument, ou
  pourquoi `partitioningBy` a toujours deux clés. Si tu bloques en expliquant,
  c'est que ce n'est pas encore acquis.
- **Dans ton propre code.** Chaque fois que tu écris une boucle `for` dans un autre
  chapitre, demande-toi si un stream ferait mieux. Réécris-la pour t'entraîner.

## Remettre un drill à zéro pour le refaire

Les drills doivent d'abord être commités « vierges » (tous les TODO avec leur
`throw`). Ensuite, pour repartir de zéro sur un drill :

```
git restore src/main/java/ch10_streams/drills/exercises/Drill01_OptionalApi.java
```

Pour tout remettre à zéro :

```
git restore src/main/java/ch10_streams/drills/exercises/
```

> ⚠️ `git restore` **efface ta version** du fichier. C'est voulu pour un drill : le
> but est de réécrire, pas de garder. Si tu veux conserver une tentative, copie-la
> avant ailleurs, hors de `src/`.

## Tableau de suivi

Format d'une case : `date – temps – score au 1er lancement` (ex. `29/09 – 14 min – 16/19`).
Liste aussi les TODO « croix » : ce sont eux qu'il faut surveiller.

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Optional | | | | | | | |
| 02 Optional primitifs | | | | | | | |
| 03 Création | | | | | | | |
| 04 Intermédiaires | | | | | | | |
| 05 Comparator | | | | | | | |
| 06 Terminales | | | | | | | |
| 07 Primitifs | | | | | | | |
| 08 Reduce / collect | | | | | | | |
| 09 Collectors simples | | | | | | | |
| 10 toMap | | | | | | | |
| 11 Grouping / teeing | | | | | | | |
| 12 Kata mélangé | | | | | | | |
| 13 Concepts avancés | | | | | | | |
