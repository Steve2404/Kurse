# Projet 5 — Le planificateur de calendrier

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

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

---

## Tableau de bord

### ☐ Étape 1 — Dates et immutabilité

```
2024-01-31 +1 mois = 2024-02-29 (bissextile true), 2023 : 2023-02-28, ignore : 2024-01-31
2026-10-02 est un FRIDAY, jour 275 de l'annee, +3 semaines 2026-10-23, fin du mois 2026-10-31
```
- **Que donne le 31 janvier + 1 mois ?** Java ajuste au dernier jour **valide**.
- **`jan31.plusDays(1);` seul :** la ligne « ignore » montre que l'objet n'a pas changé. Pourquoi ?
- **La fin du mois :** avec `withDayOfMonth` et `lengthOfMonth`.

### ☐ Étape 2 — `Period`

```
age : P31Y2M18D = 31 ans 2 mois 18 jours, en jours 11403
Period.of(1, 2, 3) P1Y2M3D, ofYears(1).ofWeeks(2) P14D, ofMonths(14) P14M, normalise P1Y2M, exam + P1M 2026-11-02
```
- **L'âge :** `Period.between`, puis `ChronoUnit.DAYS.between` pour le total en jours.
- **Le piège :** `Period.ofYears(1).ofWeeks(2)` vaut `P14D`. `ofWeeks` est une méthode **statique** : appelée « sur » un objet, elle l'ignore. (`javac` te prévient d'ailleurs avec un avertissement `[static]`.)
- **Question :** pourquoi `Period.ofMonths(14)` n'est-il **pas** normalisé automatiquement en `P1Y2M` ?

### ☐ Étape 3 — `Duration` et heures

```
23:30 + 2 h = 01:30 (passe minuit), reunion PT1H30M, 90 min, PT1H2M5S, entre 08:15 et 17:40 : PT9H25M
debut 2026-10-02T14:47:33, tronque a l'heure 2026-10-02T14:00, minutes jusqu'a 18:00 192
```
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
