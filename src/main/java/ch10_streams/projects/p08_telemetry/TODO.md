# Projet 8 — La supervision d'une flotte de serveurs (parallèle, `long`/`double`, interfaces primitives)

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**API visée :** tout ce que les projets 1 à 7 n'ont pas encore fait pratiquer.

- **Le parallélisme :** `parallel`, `sequential`, `isParallel`, `unordered`, `findAny`, `forEachOrdered`, `groupingByConcurrent`.
- **`LongStream` et `OptionalLong` :** `LongStream.iterate`, `asLongStream`, `getAsLong`.
- **`DoubleStream` :** `DoubleSummaryStatistics`, `summarizingDouble`, `averagingDouble`, `summingDouble`.
- **Les interfaces fonctionnelles primitives**, que tu **déclares toi-même** avec leur type : `IntFunction`, `ToIntFunction`, `ToLongFunction`, `ToDoubleFunction`, `LongFunction`, `IntUnaryOperator`, `IntBinaryOperator`, `LongUnaryOperator`, `DoubleToIntFunction`, `IntToLongFunction`, `LongPredicate`, `DoublePredicate`, `IntSupplier`, `BooleanSupplier`, `ObjIntConsumer`.
- **Les pièges du chapitre :**
  - le stream réutilisé ;
  - `Stream.empty()` ;
  - `Collectors.toList()` face à `Stream.toList()` et `toUnmodifiableList()` ;
  - `toUnmodifiableMap` avec des clés en double.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch10_streams.projects.p08_telemetry`. La classe du `main` s'appelle **`Telemetry`**.

**Règle du projet :** chaque fonction primitive est déclarée **avec son type écrit en toutes lettres**, par exemple `IntUnaryOperator penalty = n -> n * 15;`, et non pas en lambda anonyme glissée dans un appel. `Check` cherche ces types dans ton code. Le but : savoir **reconnaître et nommer** le type exact qu'attend chaque méthode, ce que l'examen demande sans cesse.

---

## Le problème

Quatre serveurs envoient chaque minute leur CPU, leur mémoire et les octets sortis (`Data.SAMPLES`). La flotte est surveillée par des règles d'alerte (`Data.RULES`). Une région est en maintenance, et ses alertes sont masquées. Une sonde est tombée en panne.

Ton programme produit le rapport de supervision. Une partie du traitement est **parallèle** : il doit rester **juste et déterministe**.

Les heures s'affichent avec `LocalTime.ofSecondOfDay(epoch % 86_400)`. Son `toString()` donne `14:13:20`.

---

## Tableau de bord

### ☐ Étape 1 — Le modèle et les mesures

- **Un échantillon :** serveur, région, instant, cpu (`double`), mémoire (`int`), octets (`long`).
  - **Question :** pourquoi les octets **doivent-ils** être un `long` ? Regarde la plus grande valeur.
- **Une règle d'alerte :** un nom, un seuil, et **la mesure qu'elle observe**.
  - La mesure est une fonction échantillon → nombre. Quel type primitif évite de boxer chaque valeur en `Double` ?
  - Construis une table nom de mesure → fonction, pour transformer `"CPU;90"` en règle.
- **Le test de seuil** (alerte si la valeur est **strictement** supérieure) est un `DoublePredicate`.

### ☐ Étape 2 — Le pipeline parallèle

```
PIPELINE : parallel puis sequential -> false, sequential puis parallel -> true
SERVEURS : [web1, web2, db1, cache1]
PREMIERS (forEachOrdered) : web1@35.5 web2@30.0 db1@70.0 cache1@12.0
UNIQUE db1 > 90% (findAny) : 93.5 a 14:17:20
REGIONS DISTINCTES (unordered) : 3
```
- **PIPELINE.** Écris `stream().parallel().filter(...).sequential()` et l'inverse, puis demande `isParallel()`.
  - **Question OCP :** `parallel()` et `sequential()` s'appliquent-ils à **une étape** ou à **tout le pipeline** ? Quel appel gagne ?
- **SERVEURS.**
  - Les serveurs distincts **dans l'ordre d'apparition**, depuis un `parallelStream()`.
  - Mets-les dans un `String[]` avec `toArray(...)`. Le paramètre est une `IntFunction<String[]>` : déclare-la avec son type.
  - **Question :** pourquoi `distinct()` garde-t-il l'ordre, même en parallèle ?
- **PREMIERS.**
  - Le premier échantillon de chaque serveur, affiché **dans l'ordre**, depuis un flux parallèle.
  - **Fais l'essai :** avec `forEach`, lance plusieurs fois. Que vois-tu ?
- **UNIQUE.**
  - `findAny()` sur un flux parallèle. **Pourquoi est-il déterministe ici,** alors qu'il ne l'est pas en général ?
  - Quand préférer `findAny` à `findFirst` ?
- **REGIONS DISTINCTES.** Un comptage n'a pas besoin d'ordre.
  - Où placer `unordered()` ?
  - **Question :** qu'est-ce que ça change pour `distinct()` en parallèle ?

### ☐ Étape 3 — Les `long`

```
OCTETS : total 21670000000, pic 3900000000 (web1 a 14:15:20)
MEMOIRE : max 8150 Mo, cumul 118069657600 octets
CALENDRIER : 6 instants de 14:13:20 a 14:18:20 (identique a LongStream.iterate : true)
TROUS cache1 : 14:15:20, 14:16:20
DERNIER INSTANT : 14:18:20
```
- **OCTETS.**
  - L'extracteur d'octets est un `ToLongFunction`.
  - Le pic est un `OptionalLong`, lu avec `getAsLong()`.
  - Retrouve ensuite l'échantillon du pic.
- **MEMOIRE.**
  - Le max se calcule avec `reduce` et un `IntBinaryOperator` déclaré, sans `max()`.
  - Le cumul en **octets** : passe d'`IntStream` (Mo) à `LongStream` **avant** de multiplier par 1 048 576, avec un `LongUnaryOperator`.
  - **Question :** que donnerait la multiplication faite en `int` ? Calcule l'ordre de grandeur.
- **CALENDRIER.**
  - Construis les instants attendus **de deux façons** :
    - `IntStream.rangeClosed` des numéros de minute + un `IntToLongFunction` ;
    - `LongStream.iterate` à 3 arguments.
  - Compare les deux tableaux.
- **TROUS.**
  - Pour chaque serveur, ce sont les instants attendus qu'aucun de ses échantillons ne couvre. Le test « manquant » est un `LongPredicate`.
  - Pour formater l'heure, `mapToObj` attend une `LongFunction<String>` : déclare-la une fois et réutilise-la partout.
  - Un serveur sans trou n'affiche rien.

### ☐ Étape 4 — Les `double`, `groupingByConcurrent` et `Stream.empty()`

```
CPU : 22 mesures, min 11.0, max 95.0, moy 51.2
CPU MOYEN PAR REGION : {ASIA=14.1, EU=48.5, US=81.3}
CPU CUMULE PAR SERVEUR (concurrent) : {cache1=56.5, db1=488.0, web1=364.5, web2=218.0}
REGION AFRICA : 0 echantillon, cpu moyen absent
```
- **CPU :** un seul collecteur (`summarizingDouble`).
- **CPU MOYEN PAR REGION :** `averagingDouble`, formaté à 1 décimale directement dans la `Map`.
- **CPU CUMULE PAR SERVEUR :**
  - `groupingByConcurrent` + `summingDouble`, sur un flux **parallèle non ordonné** ;
  - le résultat est une `ConcurrentMap`, dont l'affichage n'est pas trié. Comment l'afficher trié ?
  - **Questions :**
    - quelle est la différence de fonctionnement avec `groupingBy` en parallèle ? Pense à une seule map partagée, ou à des maps fusionnées.
    - pourquoi ces sommes de `double` sont-elles exactes et stables ici, alors que le projet 6 interdisait les `double` ? Indice : tous les CPU sont des multiples de 0.5.
- **REGION AFRICA :**
  - une méthode « échantillons d'une région » rend `Stream.empty()` pour une région inconnue, jamais `null` ;
  - sa moyenne est un `OptionalDouble` **vide**. Affiche `absent`, pas `0.0`.

### ☐ Étape 5 — Les alertes et la santé

```
ALERTE CPU>90 : 2 (web1) | masquees 1 (db1)
ALERTE MEMOIRE>8000 : 0 (-) | masquees 3 (db1)
ALERTE OCTETS>3000000000 : 2 (web1) | masquees 0 (-)
SANTE : cache1=100, db1=40, web1=40, web2=100
```
- **ALERTE.**
  - Pour chaque règle, sépare les déclenchements visibles de ceux masqués par la maintenance (`Data.MAINTENANCE_REGION`), avec une **partition**.
  - « La maintenance est-elle active ? » est une condition **sans argument**, évaluée **au moment du test**. C'est un `BooleanSupplier`.
- **SANTE.**
  - Le score part de 100, perd `Data.PENALTY_PER_ALERT` par alerte (visible **ou** masquée) et ne descend pas sous `Data.MIN_HEALTH`.
  - **Contrainte :** le calcul est **une** composition d'`IntUnaryOperator` : pénalité, puis score, puis plancher, avec `andThen`.
  - **Question :** que donnerait `compose` à la place d'`andThen` ?

### ☐ Étape 6 — Profils et relances

```
PROFILS CPU (deciles) : web1=349964 web2=333433 db1=778897 cache1=1111
RELANCES SONDE cache1 : 1 2 4 8 16 s
```
- **PROFILS.**
  - Chaque CPU devient son décile (35.5 → 3). C'est un `DoubleStream` → `IntStream` via un `DoubleToIntFunction`.
  - Les chiffres sont ensuite collés avec `IntStream.collect(fournisseur, accumulateur, combiner)`.
  - L'accumulateur d'un `IntStream.collect` n'est pas un `BiConsumer`. Quel est son type exact ? Déclare-le.
- **RELANCES.**
  - `IntStream.generate` avec un `IntSupplier` qui double à chaque appel.
  - **Contrainte :** l'état (la prochaine valeur) vit **dans** le fournisseur.
  - **Question :** pourquoi une lambda ne peut-elle pas faire ça ? Quelle autre écriture de l'interface le permet ?

### ☐ Étape 7 — Les pièges, prouvés par l'exécution

```
REUTILISATION : IllegalStateException
SUPPLIER : 22 puis 22
MODIFIABLE : Collectors.toList() oui, Stream.toList() non, toUnmodifiableList() non
toUnmodifiableMap avec cles en double : IllegalStateException
```
- **REUTILISATION.** Consomme un stream deux fois, attrape l'exception et affiche son nom simple.
- **SUPPLIER.** Le remède : un `Supplier<Stream<…>>` qui fabrique un flux neuf à chaque appel.
- **MODIFIABLE.** Essaie d'ajouter un élément à chacune des trois listes.
  - **Question :** pourquoi `Collectors.toList()` est-elle modifiable ? Que garantit vraiment sa Javadoc ?
- **toUnmodifiableMap.** Avec des clés en double, il échoue.
  - **Question :** et avec une valeur `null` ? Lis la Javadoc et teste.

### ☐ Étape 8 — `main`

- Il imprime tout dans l'ordre de la sortie attendue.

---

## Checklist API (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `parallel`, `sequential`, `isParallel` | 2 | ☐ |
| `forEachOrdered`, `findAny`, `unordered` | 2 | ☐ |
| `IntFunction<…>` | 2 | ☐ |
| `ToLongFunction<…>`, `OptionalLong`, `getAsLong` | 3 | ☐ |
| `IntBinaryOperator`, `LongUnaryOperator`, `asLongStream` | 3 | ☐ |
| `IntToLongFunction`, `LongStream.iterate` | 3 | ☐ |
| `LongPredicate`, `LongFunction<…>` | 3 | ☐ |
| `DoubleSummaryStatistics`, `summarizingDouble`, `averagingDouble` | 4 | ☐ |
| `groupingByConcurrent`, `ConcurrentMap<…>`, `summingDouble` | 4 | ☐ |
| `Stream.empty()` | 4 | ☐ |
| `ToDoubleFunction<…>`, `DoublePredicate` | 1, 5 | ☐ |
| `BooleanSupplier`, `getAsBoolean` | 5 | ☐ |
| `IntUnaryOperator` + `andThen` | 5 | ☐ |
| `ToIntFunction<…>` | 3 | ☐ |
| `DoubleToIntFunction`, `ObjIntConsumer<…>` | 6 | ☐ |
| `IntSupplier`, `IntStream.generate` | 6 | ☐ |
| `IllegalStateException`, `Supplier<Stream<…>>` | 7 | ☐ |
| `Collectors.toList()`, `toUnmodifiableList`, `toUnmodifiableMap` | 7 | ☐ |

---

## Sortie attendue complète

```
PIPELINE : parallel puis sequential -> false, sequential puis parallel -> true
SERVEURS : [web1, web2, db1, cache1]
PREMIERS (forEachOrdered) : web1@35.5 web2@30.0 db1@70.0 cache1@12.0
UNIQUE db1 > 90% (findAny) : 93.5 a 14:17:20
REGIONS DISTINCTES (unordered) : 3
OCTETS : total 21670000000, pic 3900000000 (web1 a 14:15:20)
MEMOIRE : max 8150 Mo, cumul 118069657600 octets
CALENDRIER : 6 instants de 14:13:20 a 14:18:20 (identique a LongStream.iterate : true)
TROUS cache1 : 14:15:20, 14:16:20
DERNIER INSTANT : 14:18:20
CPU : 22 mesures, min 11.0, max 95.0, moy 51.2
CPU MOYEN PAR REGION : {ASIA=14.1, EU=48.5, US=81.3}
CPU CUMULE PAR SERVEUR (concurrent) : {cache1=56.5, db1=488.0, web1=364.5, web2=218.0}
REGION AFRICA : 0 echantillon, cpu moyen absent
ALERTE CPU>90 : 2 (web1) | masquees 1 (db1)
ALERTE MEMOIRE>8000 : 0 (-) | masquees 3 (db1)
ALERTE OCTETS>3000000000 : 2 (web1) | masquees 0 (-)
SANTE : cache1=100, db1=40, web1=40, web2=100
PROFILS CPU (deciles) : web1=349964 web2=333433 db1=778897 cache1=1111
RELANCES SONDE cache1 : 1 2 4 8 16 s
REUTILISATION : IllegalStateException
SUPPLIER : 22 puis 22
MODIFIABLE : Collectors.toList() oui, Stream.toList() non, toUnmodifiableList() non
toUnmodifiableMap avec cles en double : IllegalStateException
```
