# Projet 5 — Le planificateur de calendrier

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 4) :**
- `LocalDate`, `LocalTime` et `LocalDateTime` : création, `plus` / `minus` / `with`, **immutabilité** ;
- **`Period`**, dont le piège du chaînage statique ;
- **`Duration`** et **`ChronoUnit`** ;
- `truncatedTo` ;
- **`ZonedDateTime`** et **`ZoneId`** ;
- le **changement d'heure** : l'heure qui n'existe pas, l'heure qui existe deux fois ;
- **`Instant`** ;
- les `enum` du JDK `DayOfWeek` et `Month` (on s'en sert ; en créer reste au chapitre 7).

**Ce qui est donné :** `Check.java` seulement. Toutes les dates sont dans l'énoncé.

**Ce que TU crées :** tout le programme, dans le paquet `ch4_coreapis.projects.p05_calendar`. La classe du `main` s'appelle **`Calendar`**.

**Règle du crescendo :** chapitres 1 à 4. **Jamais `now()`**, sinon la sortie changerait chaque jour. Pas de `DateTimeFormatter` (chapitre 11) : on affiche avec `toString()`, au format ISO.

**Tes outils pour ce projet** (pas d'arguments, pas de `Data`) :

```
javac -d build/ch4-p05 src/main/java/ch4_coreapis/projects/p05_calendar/Calendar.java
java "-Duser.language=fr" -cp build/ch4-p05 ch4_coreapis.projects.p05_calendar.Calendar
```

**Les dates sont dans le paquet `java.time`** : écris `import java.time.*;` en haut du fichier. `ChronoUnit` est dans `java.time.temporal`, un **autre** paquet : il lui faut son propre import (chapitre 1, projet 4, étape 3 : le joker n'entre pas dans les sous-paquets).

---

## Tableau de bord

### ☐ Étape 1 — Dates et immutabilité

```
2024-01-31 +1 mois = 2024-02-29 (bissextile true), 2023 : 2023-02-28, ignore : 2024-01-31
2026-10-02 est un FRIDAY, jour 275 de l'annee, +3 semaines 2026-10-23, fin du mois 2026-10-31
```

**📖 La leçon : `LocalDate`, une date sans heure.**

```java
LocalDate noel = LocalDate.of(2026, 12, 25);   // année, mois, jour
noel.getDayOfWeek()        // FRIDAY
noel.getDayOfMonth()       // 25
noel.getMonthValue()       // 12
noel.getDayOfYear()        // 359
noel.plusDays(7)           // 2027-01-01
noel.minusMonths(1)        // 2026-11-25
noel.withDayOfMonth(1)     // 2026-12-01 : le même mois, le jour 1
noel.lengthOfMonth()       // 31
noel.isLeapYear()          // false
LocalDate.parse("2026-12-25")   // lit une date écrite AAAA-MM-JJ
```

**⚠️ Une date ne change jamais**, comme un `String` : `plusDays` rend une **nouvelle** date. `noel.plusDays(1);` tout seul ne sert à rien.

**Comparer :** `noel.isBefore(autre)`, `noel.isAfter(autre)`, `noel.equals(autre)`.

**👉 À toi :**

- **Que donne le 31 janvier + 1 mois ?** Java ajuste au dernier jour **valide**.
- **`jan31.plusDays(1);` seul :** la ligne « ignore » montre que l'objet n'a pas changé. Pourquoi ?
- **La fin du mois :** avec `withDayOfMonth` et `lengthOfMonth`.

### ☐ Étape 2 — `Period`

```
age : P31Y2M18D = 31 ans 2 mois 18 jours, en jours 11403
Period.of(1, 2, 3) P1Y2M3D, ofYears(1).ofWeeks(2) P14D, ofMonths(14) P14M, normalise P1Y2M, exam + P1M 2026-11-02
```

**📖 La leçon : `Period`, un écart en années, mois et jours.**

```java
LocalDate a = LocalDate.of(2020, 3, 10);
LocalDate b = LocalDate.of(2026, 5, 15);
Period p = Period.between(a, b);   // P6Y2M5D : 6 ans, 2 mois, 5 jours
p.getYears()                       // 6
Period.ofDays(10)                  // P10D
a.plus(Period.ofMonths(2))         // 2020-05-10
ChronoUnit.DAYS.between(a, b)      // l'écart total en JOURS
```

Le `toString` se lit : `P` (période), puis `Y` (années), `M` (mois), `D` (jours).

**👉 À toi :**

- **L'âge :** `Period.between`, puis `ChronoUnit.DAYS.between` pour le total en jours.
- **Le piège :** `Period.ofYears(1).ofWeeks(2)` vaut `P14D`. `ofWeeks` est une méthode **statique** : appelée « sur » un objet, elle l'ignore. (IntelliJ le souligne, et `javac -Xlint:static` affiche un avertissement `[static]` ; sans cette option, `javac` ne dit rien.)
- **Question :** pourquoi `Period.ofMonths(14)` n'est-il **pas** normalisé automatiquement en `P1Y2M` ?

### ☐ Étape 3 — `Duration` et heures

```
23:30 + 2 h = 01:30 (passe minuit), reunion PT1H30M, 90 min, PT1H2M5S, entre 08:15 et 17:40 : PT9H25M
debut 2026-10-02T14:47:33, tronque a l'heure 2026-10-02T14:00, minutes jusqu'a 18:00 192
```

**📖 La leçon : `LocalTime`, `LocalDateTime` et `Duration`.**

```java
LocalTime t = LocalTime.of(22, 45);
t.plusMinutes(30)                 // 23:15
t.plusHours(3)                    // 01:45 : l'heure tourne sur 24 h, sans changer de jour
Duration d = Duration.ofMinutes(135);
d                                 // PT2H15M : P, puis T (time), 2 heures, 15 minutes
d.toMinutes()                     // 135
Duration.between(LocalTime.of(9, 0), LocalTime.of(12, 20))   // PT3H20M
Duration.ofSeconds(4000)          // PT1H6M40S
LocalDateTime ldt = LocalDateTime.of(2026, 5, 1, 9, 15, 20); // une date ET une heure
ldt.truncatedTo(ChronoUnit.HOURS) // 2026-05-01T09:00 : coupe les minutes et les secondes
```

**👉 À toi :**

- **`LocalTime` :** il « tourne » sur 24 h, sans date.
- **`Duration` :** elle mesure du temps **exact** (heures, minutes, secondes).
  - Lis son `toString` : `PT1H30M`.
- **Question :** `Period` pour des dates, `Duration` pour des heures. Que ferait `Duration.between` sur deux `LocalDate` ?

### ☐ Étape 4 — Fuseaux et changement d'heure

```
depart 2026-03-29T10:00+02:00[Europe/Paris], arrivee 2026-03-29T12:30-04:00[America/New_York], meme instant true
02:30 le 29/03 (heure qui n'existe pas) -> 2026-03-29T03:30+02:00[Europe/Paris] ; 02:30 le 25/10 (heure double) -> ...+02:00 puis ...+01:00
de 01:00 a 04:00 le 29/03 : PT2H reelles, 3 h d'horloge
```

**📖 La leçon : les fuseaux horaires.** Une `ZonedDateTime` est une date-heure **dans un lieu** :

```java
ZonedDateTime paris = ZonedDateTime.of(LocalDateTime.of(2026, 7, 14, 12, 0), ZoneId.of("Europe/Paris"));
// 2026-07-14T12:00+02:00[Europe/Paris] : +02:00 est l'écart avec l'heure universelle
ZonedDateTime tokyo = paris.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
// 2026-07-14T19:00+09:00[Asia/Tokyo] : le MÊME instant, vu depuis Tokyo
```

Deux fois par an, la France change d'heure : certaines heures n'existent pas, d'autres existent deux fois. L'étape te fait observer ce que Java en fait.

**👉 À toi :**

- **Le vol :** il part de Paris le **29 mars 2026** à 10:00 et dure 8 h 30. Convertis l'arrivée en heure de New York avec `withZoneSameInstant`.
- **L'heure qui n'existe pas :** cette nuit-là, à 02:00, la France passe directement à 03:00. Que fait Java de 02:30 ?
- **L'heure double :** le 25 octobre, 02:30 existe deux fois. Quel décalage Java choisit-il par défaut ? Comment obtenir l'autre ?
- **De 01:00 à 04:00 le 29 mars :**
  - combien de temps s'écoule **réellement** (`Duration` entre deux `ZonedDateTime`) ?
  - et sur l'horloge (`ChronoUnit` entre deux `LocalDateTime`) ?

### ☐ Étape 5 — `Instant` et petits algorithmes

```
epoch 1970-01-01T00:00:00Z, lancement 2026-10-02T08:00:00Z, a Paris 10:00, jours depuis l'epoch 20728
prochain vendredi 13 apres 2026-10-02 : 2026-11-13, apres le 13/02/2026 : 2026-03-13
jours ouvres en octobre 2026 : 22
FEBRUARY 2026 (28 jours)
 Lu Ma Me Je Ve Sa Di
                    1
  2  3  4  5  6  7  8
```

**📖 La leçon : `Instant`, un point sur la ligne du temps.** Il est toujours exprimé en heure universelle, avec un `Z` à la fin :

```java
Instant.EPOCH                  // 1970-01-01T00:00:00Z : le « point zéro » des ordinateurs
Instant.ofEpochSecond(86400)   // 1970-01-02T00:00:00Z : un jour (86 400 secondes) plus tard
paris.toInstant()              // 2026-07-14T10:00:00Z : midi à Paris en été = 10 h universelle
```

**📖 Rappel :** `noel.getDayOfWeek().getValue()` donne le numéro du jour : lundi = 1, … dimanche = 7. Pour aligner les jours du calendrier sur 3 caractères, `String.format("%3d", jour)` (projet 1, étape 6).

**👉 À toi :**

- **`Instant` :** un point sur la ligne du temps, en UTC (`Z`).
- **Le prochain vendredi 13 strictement après une date :** essaie le 13 de chaque mois.
  - **Piège :** si la date de départ **est** un vendredi 13, il faut chercher le suivant.
- **Les jours ouvrés :** les jours du lundi au vendredi, dans `[début, fin)`.
- **Le calendrier du mois :**
  - la 1re ligne est décalée selon le jour de la semaine du 1er (`getDayOfWeek().getValue()` : lundi = 1) ;
  - on passe à la ligne après chaque dimanche ;
  - chaque jour est aligné sur 3 caractères.

---

## Checklist (vérifiée par `Check`)

`LocalDate.of`, `LocalTime.of`, `LocalDateTime.of`, `ZonedDateTime.of`, `ZoneId.of`, `Instant.ofEpochSecond`, `Instant.parse`, `Period.between`, `Period.of`, `Period.ofYears(1).ofWeeks(…)`, `normalized`, `Duration.ofMinutes`, `Duration.between`, `ChronoUnit.DAYS.between`, `truncatedTo`, `plusMonths`, `withDayOfMonth`, `isLeapYear`, `getDayOfWeek`, `lengthOfMonth`, `withZoneSameInstant`, `toInstant`, `withLaterOffsetAtOverlap`, `DayOfWeek.`, `Month.` ☐

---

## Sortie attendue complète

```
=== DATES ===
2024-01-31 +1 mois = 2024-02-29 (bissextile true), 2023 : 2023-02-28, ignore : 2024-01-31
2026-10-02 est un FRIDAY, jour 275 de l'annee, +3 semaines 2026-10-23, fin du mois 2026-10-31
=== PERIOD ===
age : P31Y2M18D = 31 ans 2 mois 18 jours, en jours 11403
Period.of(1, 2, 3) P1Y2M3D, ofYears(1).ofWeeks(2) P14D, ofMonths(14) P14M, normalise P1Y2M, exam + P1M 2026-11-02
=== DURATION ET HEURES ===
23:30 + 2 h = 01:30 (passe minuit), reunion PT1H30M, 90 min, PT1H2M5S, entre 08:15 et 17:40 : PT9H25M
debut 2026-10-02T14:47:33, tronque a l'heure 2026-10-02T14:00, minutes jusqu'a 18:00 192
=== FUSEAUX ET CHANGEMENT D'HEURE ===
depart 2026-03-29T10:00+02:00[Europe/Paris], arrivee 2026-03-29T12:30-04:00[America/New_York], meme instant true
02:30 le 29/03 (heure qui n'existe pas) -> 2026-03-29T03:30+02:00[Europe/Paris] ; 02:30 le 25/10 (heure double) -> 2026-10-25T02:30+02:00[Europe/Paris] puis 2026-10-25T02:30+01:00[Europe/Paris]
de 01:00 a 04:00 le 29/03 : PT2H reelles, 3 h d'horloge
epoch 1970-01-01T00:00:00Z, lancement 2026-10-02T08:00:00Z, a Paris 10:00, jours depuis l'epoch 20728
=== ALGORITHMES ===
prochain vendredi 13 apres 2026-10-02 : 2026-11-13, apres le 13/02/2026 : 2026-03-13
jours ouvres en octobre 2026 : 22
FEBRUARY 2026 (28 jours)
 Lu Ma Me Je Ve Sa Di
                    1
  2  3  4  5  6  7  8
  9 10 11 12 13 14 15
 16 17 18 19 20 21 22
 23 24 25 26 27 28
```
