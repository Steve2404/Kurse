# Projet 8 — La supervision d'une flotte de serveurs (`long`/`double`, interfaces primitives, pièges)

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**API visée :** tout ce que les projets 1 à 7 n'ont pas encore fait pratiquer.

- **`LongStream` et `OptionalLong` :** `LongStream.iterate`, `asLongStream`, `getAsLong`.
- **`DoubleStream` :** `DoubleSummaryStatistics`, `summarizingDouble`, `averagingDouble`, `summingDouble`.
- **`findAny`, `Stream.empty()`, `IntStream.generate`, `toArray(IntFunction)`.**
- **Les interfaces fonctionnelles primitives**, que tu **déclares toi-même** avec leur type : `IntFunction`, `ToIntFunction`, `ToLongFunction`, `ToDoubleFunction`, `LongFunction`, `IntUnaryOperator`, `IntBinaryOperator`, `LongUnaryOperator`, `DoubleToIntFunction`, `IntToLongFunction`, `LongPredicate`, `DoublePredicate`, `IntSupplier`, `BooleanSupplier`, `ObjIntConsumer`.
- **Les pièges du chapitre,** prouvés par des expériences (étape 7).

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch10_streams.projects.p08_telemetry`. La classe du `main` s'appelle **`Telemetry`**.

**Règle du projet :** chaque fonction primitive est déclarée **avec son type écrit en toutes lettres**, par exemple `IntUnaryOperator penalty = n -> n * 15;`, et non pas en lambda anonyme glissée dans un appel. `Check` cherche ces types dans ton code. Le but : savoir **reconnaître et nommer** le type exact qu'attend chaque méthode, ce que l'examen demande sans cesse.

**C'est le projet-bilan du chapitre 10**, centré sur les streams de nombres et les interfaces fonctionnelles pour primitifs. Quand tu bloques, relis la leçon d'origine :

| Tu dois… | Leçon à relire |
|---|---|
| les interfaces pour primitifs (`ToLongFunction`, `IntBinaryOperator`, `LongPredicate`…) | chapitre 8, projet 4, étape 1 |
| passer d'un stream de nombres à un autre | projet 3, étapes 1, 4 et 7 |
| `summaryStatistics`, `OptionalLong`, `OptionalDouble` | projet 3, étape 3 |
| `averagingDouble`, `summingDouble`, `collectingAndThen` | projet 5, étapes 3 et 8 |
| `Stream.empty()` | projet 2, étape 1 |
| `partitioningBy` | projet 5, étape 2 |
| composer des `IntUnaryOperator` avec `andThen` | chapitre 8, projet 1, étape 3 |
| `toArray` avec un `IntFunction<String[]>` | chapitre 8, projet 5, étape 3 (`String[]::new`) |

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch10-p08 -sourcepath src/main/java src/main/java/ch10_streams/projects/p08_telemetry/Telemetry.java
java "-Duser.language=fr" -cp build/ch10-p08 ch10_streams.projects.p08_telemetry.Telemetry
```

---

## Le problème

Quatre serveurs envoient chaque minute leur CPU, leur mémoire et les octets sortis (`Data.SAMPLES`). La flotte est surveillée par des règles d'alerte (`Data.RULES`). Une région est en maintenance, et ses alertes sont masquées. Une sonde est tombée en panne.

Ton programme produit le rapport de supervision.

**Affichage :**
- les heures s'affichent avec `LocalTime.ofSecondOfDay(epoch % 86_400)` (chapitre 4) ; son `toString()` donne `14:13:20` ;
- les décimales s'affichent avec `Math.round(x * 10) / 10.0`. Pas de `Locale` : c'est le chapitre 11.

---

## Tableau de bord

### ☐ Étape 1 — Le modèle et les mesures

- **Un échantillon :** serveur, région, instant, cpu (`double`), mémoire (`int`), octets (`long`).
  - **Question :** pourquoi les octets **doivent-ils** être un `long` ? Regarde la plus grande valeur.
- **Une règle d'alerte :** un nom, un seuil, et **la mesure qu'elle observe**.
  - La mesure est une fonction échantillon → nombre. Quel type primitif évite de boxer chaque valeur en `Double` ?
  - Construis une table nom de mesure → fonction, pour transformer `"CPU;90"` en règle.
- **Le test de seuil** (alerte si la valeur est **strictement** supérieure) est un `DoublePredicate`.

### ☐ Étape 2 — Vue d'ensemble

```
SERVEURS : [web1, web2, db1, cache1]
PREMIERS : web1@35.5 web2@30.0 db1@70.0 cache1@12.0
UNIQUE db1 > 90% (findAny) : 93.5 a 14:17:20
REGIONS DISTINCTES : 3
```
- **SERVEURS.** Les serveurs distincts **dans l'ordre d'apparition**, mis dans un `String[]` avec `toArray(...)`. Le paramètre est une `IntFunction<String[]>` : déclare-la avec son type.
  - **Question :** que rendrait `toArray()` **sans** argument ? Pourquoi ne peut-on pas le caster en `String[]` ?
- **PREMIERS.** Le premier échantillon de chaque serveur (instant `Data.WINDOW_START`), dans l'ordre.
- **UNIQUE.** Utilise `findAny()`.
  - **Question :** `findAny` peut rendre **n'importe quel** élément qui passe le filtre. Pourquoi le résultat est-il quand même prévisible ici ?
  - Pourquoi l'examen dit-il qu'on ne peut pas prédire `findAny`, alors qu'en pratique, sur un stream séquentiel, il rend souvent le premier ?
- **REGIONS DISTINCTES.** Le nombre de régions différentes.

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
  - Le max se calcule avec `reduce` et un `IntBinaryOperator` déclaré, sans `max()`. Quelle identité choisir pour un max ?
  - Le cumul en **octets** : passe d'`IntStream` (Mo) à `LongStream` **avant** de multiplier par 1 048 576, avec un `LongUnaryOperator`.
  - **Question :** que donnerait la multiplication faite en `int` ? Calcule l'ordre de grandeur.
- **CALENDRIER.**
  - Construis les instants attendus **de deux façons** :
    - `IntStream.rangeClosed` des numéros de minute + un `IntToLongFunction` ;
    - `LongStream.iterate` à 3 arguments.
  - Compare les deux tableaux avec `Arrays.equals` (chapitre 4).
- **TROUS.**
  - Pour chaque serveur, ce sont les instants attendus qu'aucun de ses échantillons ne couvre. Le test « manquant » est un `LongPredicate`.
  - Pour formater l'heure, `mapToObj` attend une `LongFunction<String>` : déclare-la une fois et réutilise-la partout.
  - Un serveur sans trou n'affiche rien.

### ☐ Étape 4 — Les `double` et `Stream.empty()`

```
CPU : 22 mesures, min 11.0, max 95.0, moy 51.2
CPU MOYEN PAR REGION : {ASIA=14.1, EU=48.5, US=81.3}
CPU CUMULE PAR SERVEUR : {cache1=56.5, db1=488.0, web1=364.5, web2=218.0}
REGION AFRICA : 0 echantillon, cpu moyen absent
```
- **CPU :** un seul collecteur (`summarizingDouble`).
- **CPU MOYEN PAR REGION :** `averagingDouble`, arrondi à 1 décimale directement dans la `Map`. Quel collecteur applique une fonction au résultat d'un autre ?
- **CPU CUMULE PAR SERVEUR :** `summingDouble`, dans une `Map` triée.
  - **Question :** au projet 6, on interdisait les `double` pour des sommes. Pourquoi ces sommes-ci sont-elles exactes ? Indice : tous les CPU sont des multiples de 0.5, donc exactement représentables en binaire.
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
  - Le seuil s'affiche sans décimale : un cast `(long)` (chapitre 2) suffit.
- **SANTE.**
  - Le score part de 100, perd `Data.PENALTY_PER_ALERT` par alerte (visible **ou** masquée) et ne descend pas sous `Data.MIN_HEALTH`.
  - **Contrainte :** le calcul est **une** composition d'`IntUnaryOperator` : pénalité, puis score, puis plancher, avec `andThen` (chapitre 8).
  - **Question :** que donnerait `compose` à la place d'`andThen` ?

### ☐ Étape 6 — Profils et relances

```
PROFILS CPU (deciles) : web1=349964 web2=333433 db1=778897 cache1=1111
RELANCES SONDE cache1 : 1 2 4 8 16 s
```

**📖 La leçon : `collect` à 3 arguments sur un `IntStream`.** Comme au projet 4, étape 2, mais l'accumulateur reçoit un **`int`**. Son type est donc un `ObjIntConsumer` :

```java
ObjIntConsumer<StringBuilder> ajoute = (b, i) -> b.append(i).append('-');
StringBuilder sb = IntStream.of(4, 2).collect(StringBuilder::new, ajoute, StringBuilder::append);
// "4-2-"
```

**👉 À toi :**

- **PROFILS.**
  - Chaque CPU devient son décile (35.5 → 3). C'est un `DoubleStream` → `IntStream` via un `DoubleToIntFunction`.
  - Les chiffres sont ensuite collés avec `IntStream.collect(fournisseur, accumulateur, combiner)`.
  - L'accumulateur d'un `IntStream.collect` n'est pas un `BiConsumer`. Quel est son type exact ? Déclare-le.
- **RELANCES.**
  - `IntStream.generate` avec un `IntSupplier` qui double à chaque appel.
  - **Contrainte :** l'état (la prochaine valeur) vit **dans** le fournisseur.
  - **Question :** pourquoi une lambda ne peut-elle pas faire ça ? Quelle autre écriture de l'interface (chapitre 7) le permet ?

### ☐ Étape 7 — Les pièges du chapitre

```
SUPPLIER : 22 puis 3
```

**📖 La leçon : un `Supplier` de streams.** Un stream ne s'utilise qu'une fois (projet 2, étape 5). Pour en avoir un **neuf** à chaque fois, on range sa **recette** dans un `Supplier` :

```java
Supplier<Stream<String>> source = () -> Stream.of("a", "bb", "ccc");
source.get().count()                                 // 3
source.get().filter(s -> s.length() > 1).count()     // 2 : un nouveau stream
```

**Pour les expériences :** le programme s'arrêtera avec une exception. Lis son **nom** (le mot qui finit par `Exception`) et sa phrase, puis retire la ligne. Le chapitre 11 t'apprendra à les rattraper.

**👉 À toi :**

- **SUPPLIER.** Un stream ne se consomme qu'une fois. Le remède est un `Supplier<Stream<…>>` qui fabrique un flux **neuf** à chaque appel.
  - Compte tous les échantillons avec un premier flux.
  - Compte ensuite ceux à plus de 90 % de CPU, avec un **second** flux obtenu du même `Supplier`.

**Expériences à faire toi-même.** Elles ne font pas partie de la sortie attendue. Pour chacune, ajoute la ligne dans ton `main`, lance-le directement, lis le nom de l'exception dans la console, puis **retire la ligne** avant de relancer `Check` :
1. Consomme **deux fois** le même stream (deux `count()`). Quelle exception ? À quelle ligne ?
2. Ajoute un élément à une liste obtenue par `stream.toList()`, puis à une liste obtenue par `collect(Collectors.toList())`. Laquelle refuse, et avec quelle exception ?
3. Fais un `collect(Collectors.toUnmodifiableMap(Sample::server, s -> s))`. Pourquoi est-ce que ça échoue avec ces données ?
4. Fais un `Optional.of(null)`. Et un `Optional.ofNullable(null)` ?

Note tes réponses en commentaire. Ce sont des questions classiques de l'examen. La **gestion** des exceptions (`try/catch`) viendra au chapitre 11.

### ☐ Étape 8 — `main`

- Il imprime tout dans l'ordre de la sortie attendue.

---

## Checklist API (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `IntFunction<…>` + `toArray`, `findAny` | 2 | ☐ |
| `ToLongFunction<…>`, `OptionalLong`, `getAsLong` | 3 | ☐ |
| `IntBinaryOperator`, `LongUnaryOperator`, `asLongStream` | 3 | ☐ |
| `IntToLongFunction`, `LongStream.iterate` | 3 | ☐ |
| `LongPredicate`, `LongFunction<…>` | 3 | ☐ |
| `ToIntFunction<…>` | 3 | ☐ |
| `DoubleSummaryStatistics`, `summarizingDouble`, `averagingDouble`, `summingDouble` | 4 | ☐ |
| `Stream.empty()` | 4 | ☐ |
| `ToDoubleFunction<…>`, `DoublePredicate` | 1, 5 | ☐ |
| `BooleanSupplier`, `getAsBoolean` | 5 | ☐ |
| `IntUnaryOperator` + `andThen` | 5 | ☐ |
| `DoubleToIntFunction`, `ObjIntConsumer<…>` | 6 | ☐ |
| `IntSupplier`, `IntStream.generate` | 6 | ☐ |
| `Supplier<Stream<…>>` | 7 | ☐ |

---

## Sortie attendue complète

```
SERVEURS : [web1, web2, db1, cache1]
PREMIERS : web1@35.5 web2@30.0 db1@70.0 cache1@12.0
UNIQUE db1 > 90% (findAny) : 93.5 a 14:17:20
REGIONS DISTINCTES : 3
OCTETS : total 21670000000, pic 3900000000 (web1 a 14:15:20)
MEMOIRE : max 8150 Mo, cumul 118069657600 octets
CALENDRIER : 6 instants de 14:13:20 a 14:18:20 (identique a LongStream.iterate : true)
TROUS cache1 : 14:15:20, 14:16:20
DERNIER INSTANT : 14:18:20
CPU : 22 mesures, min 11.0, max 95.0, moy 51.2
CPU MOYEN PAR REGION : {ASIA=14.1, EU=48.5, US=81.3}
CPU CUMULE PAR SERVEUR : {cache1=56.5, db1=488.0, web1=364.5, web2=218.0}
REGION AFRICA : 0 echantillon, cpu moyen absent
ALERTE CPU>90 : 2 (web1) | masquees 1 (db1)
ALERTE MEMOIRE>8000 : 0 (-) | masquees 3 (db1)
ALERTE OCTETS>3000000000 : 2 (web1) | masquees 0 (-)
SANTE : cache1=100, db1=40, web1=40, web2=100
PROFILS CPU (deciles) : web1=349964 web2=333433 db1=778897 cache1=1111
RELANCES SONDE cache1 : 1 2 4 8 16 s
SUPPLIER : 22 puis 3
```
