# Projet 3 — Le rapport d'une station météo

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**API visée :** les streams primitifs `IntStream`, `LongStream` et `DoubleStream`. On les crée (`of`, `range`, `rangeClosed`, `iterate`, `chars`), on passe de l'un à l'autre (`mapToInt`, `mapToLong`, `mapToDouble`, `mapToObj`, `flatMapToInt`, `boxed`, `asDoubleStream`), on les agrège (`sum`, `max`, `average`, `summaryStatistics`) et on lit leurs `OptionalInt` / `OptionalDouble`.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch10_streams.projects.p03_weather`. La classe du `main` s'appelle **`WeatherStation`**.

**Pour vérifier :** lance `Check.java`.

---

## Le problème

Une station météo envoie, chaque jour, 24 températures horaires (de 0h à 23h). Ton programme lit `Data.DAYS`, écarte les journées défectueuses et imprime un rapport complet.

On y trouve des statistiques par jour, une fenêtre glissante, une médiane, un histogramme, des détections de canicule, de hausse et d'orage, et des calculs d'énergie.

**Règle du projet :** les températures sont des `int`. Elles ne sont **jamais boxées** en `Integer`, sauf aux deux endroits où le rapport l'exige vraiment (étapes 3 et 4). À chaque `boxed()` que tu écris, demande-toi s'il est indispensable.

**Les nombres décimaux** s'affichent arrondis avec `Math.round` (chapitre 4) : `Math.round(x * 10) / 10.0` pour une décimale. `Double.toString` écrit toujours un point, quelle que soit la langue du système. Le formatage localisé (`Locale`, `NumberFormat`) est au programme du chapitre 11, pas ici.

---

## Tableau de bord

### ☐ Étape 1 — Lire et valider les journées

```
REJET 2026-07-19 : 23 releves au lieu de 24
```
- Transforme `"18,17,16,…"` en `int[]` sans passer par une `List<Integer>`. Quelle méthode fait passer un `Stream<String>` à un `IntStream` ?
- Une journée qui n'a pas exactement 24 relevés est rejetée. Elle apparaît dans le rapport et n'entre dans **aucun** calcul.
- **Conception :** tu veux peut-être mettre un `int[]` dans un record. Que comparent alors `equals` et `hashCode` du record ? Est-ce un problème ici ? Écris ta réponse en commentaire.
- **Accès aux valeurs :** comment donner les températures au reste du programme sans exposer le tableau, que n'importe qui pourrait modifier ? Penses-y avec un `IntStream`.

### ☐ Étape 2 — Le code de contrôle de la station

```
STATION LYON-BRON-07 : code de controle 51
```
- Le code est la somme des codes des caractères **lettres ou chiffres** de `Data.STATION`, modulo 97. Les tirets ne comptent pas.
- **Contrainte :** une seule chaîne, partie de la `String`, sans `char[]` ni boucle. Quelle méthode de `String` rend un `IntStream` ?
- **Question :** pourquoi un `IntStream`, et pas un `Stream<Character>` ?

### ☐ Étape 3 — Le bilan de chaque journée (2 lignes par jour)

```
2026-07-14 : min 16 max 31 moy 22.8 | pic 10h-12h moy 30.7
2026-07-14 : releves 0h/6h/12h/18h 18 22 31 21 | heures >= 30 [10, 11, 12, 13]
```
- **min, max, moy** viennent d'**un seul** parcours de la journée. Quel objet les calcule ensemble ?
- **Le pic** est la fenêtre de 3 heures consécutives dont la somme est la plus grande. C'est une fenêtre glissante :
  - chaque heure de départ possible (de 0 à 21) devient la somme de ses 3 heures ;
  - garde la meilleure ;
  - à égalité, prends **la plus tôt**. Le 14 juillet, 10h-12h et 11h-13h valent toutes les deux 92.
  - Quel comportement de `max(Comparator)` en cas d'égalité te donne ça gratuitement ? Vérifie-le dans la Javadoc de `BinaryOperator.maxBy`.
- **La moyenne du pic** : lis un `OptionalDouble` avec `getAsDouble()`. Pourquoi est-ce sûr ici ?
- **Les relevés 0h/6h/12h/18h** se calculent avec un `IntStream.iterate` à 3 arguments, sans tableau d'heures écrit à la main.
- **Les heures >= 30** s'affichent sous forme de `List<Integer>` (le format `[10, 11]` est celui de `List.toString`). C'est un endroit où `boxed()` est indispensable. Pourquoi ne peux-tu pas obtenir de `List` directement depuis un `IntStream` ?

### ☐ Étape 4 — Médiane et moyennes de toute la période

```
MEDIANE : 22.0
MOYENNE : 23.7 C / 74.6 F
```
- **Toutes les températures de toutes les journées valides** forment **un** `IntStream`. Quelle opération aplatit une liste de journées en un flux d'`int` sans boxing ?
- **La médiane :**
  - avec un nombre pair de valeurs, c'est la moyenne des deux valeurs du milieu ;
  - écris-la avec `sorted`, `skip`, `limit` et `average`, sans tableau ni `List` ;
  - gère aussi le cas impair, même si les données ne le testent pas.
- **Les Fahrenheit** se calculent avec `F = C × 9 / 5 + 32`.
  - **Piège :** sur un `IntStream`, `c * 9 / 5` est une division **entière**. Convertis d'abord le flux en `DoubleStream`. Quelle méthode le fait sans `mapToDouble` ?
- **Question :** un `IntStream` ne peut être consommé qu'une fois. Comment ton code obtient-il un flux neuf à chaque calcul ?

### ☐ Étape 5 — L'histogramme

```
HISTO [10-14] 4 ##
HISTO [15-19] 33 ################
...
```
- Les classes vont de 5 en 5, de la classe du minimum à celle du maximum (par exemple `[10-14]` puis `[15-19]`).
- La barre fait `nombre / 2` dièses (division entière).
- **Contrainte :** les bornes viennent de `summaryStatistics()`. Les classes sont produites par `IntStream.rangeClosed(...)`, et chaque classe devient une ligne avec `mapToObj`.
- **Vérification :** la somme des nombres doit faire 120. Pourquoi 120 et pas 144 ?

### ☐ Étape 6 — Les trois détections : l'algorithme

```
CANICULE : 2 jour(s) consecutifs (2026-07-15 -> 2026-07-16)
MONTEE : 9 heure(s) de hausse continue le 2026-07-15 (3h -> 12h)
ORAGE : chute de 7 degres le 2026-07-17 a 12h
```
- **CANICULE.** C'est la plus longue suite de journées consécutives dont le maximum atteint `Data.HEAT_WAVE_MAX`.
  - Le maximum d'une journée est un `OptionalInt`.
  - S'il n'y en a aucune, affiche `CANICULE : aucune`.
- **MONTÉE.** C'est la plus longue suite d'heures où la température **monte strictement** à chaque heure, toutes journées confondues.
  - La longueur compte les **hausses**, pas les heures.
  - À égalité, prends la plus tôt.
  - Calcule à la main le 15 et le 16 : les deux font 9. Lequel gagne, et pourquoi ?
- **ORAGE.** C'est chaque chute **strictement** supérieure à `Data.STORM_DROP` entre deux heures consécutives.
  - **Contrainte :** une seule chaîne, des journées jusqu'aux lignes de texte. Il faut un `IntStream.range` des heures **à l'intérieur** d'un `flatMap`. Quelle méthode transforme un `IntStream` en `Stream<String>` ?
  - S'il n'y a aucun orage, affiche `ORAGE : aucun`.
- **Question de conception :** pour CANICULE et MONTÉE, on garde un état (« la série en cours » et « la meilleure »). Un stream est-il fait pour ça ? Tu as le droit d'écrire une boucle. Justifie ce choix en commentaire.

### ☐ Étape 7 — L'énergie

```
CLIMATISATION : 102900 Wh (102.9 kWh)
DEGRES-JOURS : 5.38
```
- **CLIMATISATION** : chaque heure au-dessus de `Data.AC_THRESHOLD` coûte `(t − seuil) × Data.WH_PER_DEGREE_HOUR` Wh.
  - Le calcul se fait en `long`. Quelle méthode passe d'un `IntStream` à un `LongStream` ?
  - **Question :** sur un an de relevés et 1000 stations, pourquoi `int` serait-il dangereux ?
- **DEGRÉS-JOURS** : pour chaque journée, prends `max(0, moyenne − seuil)`, puis additionne.
  - Une valeur `double` par journée : quelle méthode passe d'un `Stream<journée>` à un `DoubleStream` ?

### ☐ Étape 8 — `main`

- Il charge les données et imprime le rapport dans l'ordre de la sortie attendue.

---

## Checklist API (vérifiée par `Check`)

| Méthode | Étape | ☐ |
|---|---|---|
| `IntStream.of` | 1 | ☐ |
| `mapToInt`, `toArray` | 1 | ☐ |
| `chars`, `sum` | 2 | ☐ |
| `summaryStatistics` / `IntSummaryStatistics` | 3, 5 | ☐ |
| `IntStream.range`, `IntStream.rangeClosed` | 3, 5, 6 | ☐ |
| `IntStream.iterate` (3 arguments) | 3 | ☐ |
| `average`, `getAsDouble` | 3, 4 | ☐ |
| `boxed` | 3 | ☐ |
| `flatMapToInt` | 4 | ☐ |
| `asDoubleStream` | 4 | ☐ |
| `mapToObj` | 3, 5, 6 | ☐ |
| `max()` (→ `OptionalInt`) | 6 | ☐ |
| `mapToLong` | 7 | ☐ |
| `mapToDouble` | 7 | ☐ |

---

## Sortie attendue complète

```
STATION LYON-BRON-07 : code de controle 51
REJET 2026-07-19 : 23 releves au lieu de 24
2026-07-14 : min 16 max 31 moy 22.8 | pic 10h-12h moy 30.7
2026-07-14 : releves 0h/6h/12h/18h 18 22 31 21 | heures >= 30 [10, 11, 12, 13]
2026-07-15 : min 18 max 35 moy 26.1 | pic 11h-13h moy 34.7
2026-07-15 : releves 0h/6h/12h/18h 20 24 35 26 | heures >= 30 [8, 9, 10, 11, 12, 13, 14, 15, 16]
2026-07-16 : min 19 max 37 moy 27.3 | pic 11h-13h moy 36.3
2026-07-16 : releves 0h/6h/12h/18h 21 25 37 27 | heures >= 30 [8, 9, 10, 11, 12, 13, 14, 15, 16]
2026-07-17 : min 15 max 31 moy 20.6 | pic 9h-11h moy 29.7
2026-07-17 : releves 0h/6h/12h/18h 22 20 24 17 | heures >= 30 [10, 11]
2026-07-18 : min 13 max 31 moy 21.6 | pic 11h-13h moy 30.7
2026-07-18 : releves 0h/6h/12h/18h 15 19 31 22 | heures >= 30 [11, 12, 13, 14]
MEDIANE : 22.0
MOYENNE : 23.7 C / 74.6 F
HISTO [10-14] 4 ##
HISTO [15-19] 33 ################
HISTO [20-24] 35 #################
HISTO [25-29] 20 ##########
HISTO [30-34] 21 ##########
HISTO [35-39] 7 ###
CANICULE : 2 jour(s) consecutifs (2026-07-15 -> 2026-07-16)
MONTEE : 9 heure(s) de hausse continue le 2026-07-15 (3h -> 12h)
ORAGE : chute de 7 degres le 2026-07-17 a 12h
CLIMATISATION : 102900 Wh (102.9 kWh)
DEGRES-JOURS : 5.38
```
