# Drill de rappel 11 — Les dates, la suite : `parse`, `atX`, epoch, unités, `until`, décalages

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 18 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall11`** dans le paquet `ch4_coreapis.drills.r11_moredates`.
- Pas de `now()`, pas de `DateTimeFormatter` (chapitre 11).

## Défis

`date` est `LocalDate.parse("2026-02-28")` et `time` est `LocalTime.parse("23:59:30")`.

- ☐ **D01.** Affiche :
  - `date` et `time` ;
  - `LocalDateTime.parse("2026-02-28T23:59")` ;
  - `Period.parse("P1Y2M")` ;
  - `Duration.parse("PT1H30M")` en minutes.
  → `D01 : 2026-02-28 23:59:30 2026-02-28T23:59 P1Y2M 90`
- ☐ **D02.** Les combinaisons :
  - `date.atTime(9, 30)` ;
  - `date.atStartOfDay()` ;
  - `time.atDate(date)` ;
  - `date.atStartOfDay(…)` avec la zone `Europe/Paris`.
  → `D02 : 2026-02-28T09:30 2026-02-28T00:00 2026-02-28T23:59:30 2026-02-28T00:00+01:00[Europe/Paris]`
- ☐ **D03.** L'epoch :
  - `date.toEpochDay()` ;
  - `LocalDate.ofEpochDay(0)` ;
  - `Instant.ofEpochMilli(1500)` ;
  - le `toEpochSecond()` du 01/01/1970 à 01:00 en `ZoneOffset.UTC`.
  → `D03 : 20512 1970-01-01 1970-01-01T00:00:01.500Z 3600`
- ☐ **D04.** Les constantes :
  - `LocalTime.MIDNIGHT`, `NOON` et `MAX` ;
  - `LocalTime.MIN.equals(LocalTime.MIDNIGHT)` ;
  - `ZoneOffset.UTC`.
  → `D04 : 00:00 12:00 23:59:59.999999999 true Z`
- ☐ **D05.** Les ajouts avec une unité :
  - `date.plus(2, ChronoUnit.WEEKS)` ;
  - `date.plus(1, ChronoUnit.DECADES)` ;
  - `time.plusSeconds(45)` ;
  - `time.plusNanos(1_000_000)` ;
  - `date.minusDays(60)`.
  → `D05 : 2026-03-14 2036-02-28 00:00:15 23:59:30.001 2025-12-30`
- ☐ **D06.** `until` et les conversions de `Duration` :
  - `date.until(noël 2026)` ;
  - la même chose en `ChronoUnit.DAYS` ;
  - `Duration.ofMinutes(1500)` convertie avec `toDays()`, `toHours()`, `toSeconds()`, `toHoursPart()` et `toMinutesPart()`.
  → `D06 : P9M27D 300 1 25 90000 1 0`
- ☐ **D07.** Le 01/07/2026 à 12:00, placé en `Asia/Kolkata` avec `atZone`. Affiche :
  - son décalage ;
  - ce décalage est-il égal à `ZoneOffset.of("+05:30")` ?
  - sa zone ;
  - son `toLocalDateTime()` ;
  - l'heure du même instant en UTC.
  → `D07 : +05:30 true Asia/Kolkata 2026-07-01T12:00 06:30`
- ☐ **D08.** Les `enum` de dates :
  - `date.getMonth().plus(11)` ;
  - `DayOfWeek.MONDAY.minus(1)` ;
  - `date.with(DayOfWeek.MONDAY)` ;
  - `Month.FEBRUARY.length(true)` ;
  - `date.lengthOfYear()` ;
  - `date.getDayOfWeek().getValue()` ;
  - `Month.of(12)`.
  → `D08 : JANUARY SUNDAY 2026-02-23 29 365 6 DECEMBER`

## Expériences (hors sortie attendue)

1. `LocalDate.parse("2026-2-28")` et `LocalDate.parse("28.02.2026")` : quelle exception ? (Le format ISO exige 2 chiffres.)
2. `date.plus(1, ChronoUnit.HOURS)` : ça compile ? Et à l'exécution ? (`UnsupportedTemporalTypeException`)
3. `date.until(christmas).getDays()` vaut 27, pas 300 : pourquoi ?
4. `Duration.ofMinutes(1500)` s'affiche-t-il `PT25H` ou `P1DT1H` ?
5. `LocalTime.of(23, 59).plusMinutes(2)` : la date change-t-elle ? (Un `LocalTime` n'a pas de date.)

## Sortie attendue complète

```
D01 : 2026-02-28 23:59:30 2026-02-28T23:59 P1Y2M 90
D02 : 2026-02-28T09:30 2026-02-28T00:00 2026-02-28T23:59:30 2026-02-28T00:00+01:00[Europe/Paris]
D03 : 20512 1970-01-01 1970-01-01T00:00:01.500Z 3600
D04 : 00:00 12:00 23:59:59.999999999 true Z
D05 : 2026-03-14 2036-02-28 00:00:15 23:59:30.001 2025-12-30
D06 : P9M27D 300 1 25 90000 1 0
D07 : +05:30 true Asia/Kolkata 2026-07-01T12:00 06:30
D08 : JANUARY SUNDAY 2026-02-23 29 365 6 DECEMBER
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**`parse` :**
- il lit le format ISO, le même que `toString` : `2026-02-28`, `23:59:30`, `2026-02-28T23:59`, `P1Y2M`, `PT1H30M` ;
- un autre format lève une `DateTimeParseException`.

**Les combinaisons :**
- `date.atTime(h, m)` et `time.atDate(date)` donnent un `LocalDateTime` ;
- `atStartOfDay()` donne minuit ; avec une zone, il donne un `ZonedDateTime` ;
- `ldt.atZone(zone)` donne un `ZonedDateTime`.

**L'epoch :**
- l'origine est le 01/01/1970 UTC ;
- `toEpochDay` compte des jours, `toEpochSecond` des secondes, `ofEpochMilli` des millisecondes.

**Les unités :**
- `plus(n, ChronoUnit.X)` accepte toutes les unités, mais une unité d'heure sur une `LocalDate` lève une exception ;
- les secondes et nanos ne s'affichent que si elles sont non nulles.

**`until` :**
- `until(autre)` rend une `Period` ;
- `until(autre, unité)` rend un `long`, comme `unité.between`.

**Les conversions de `Duration` :**
- `toX()` donne le **total** dans l'unité X (tronqué) ;
- `toXPart()` donne la **partie** X seule (25 h → `toHoursPart` vaut 1).

**Les `enum` :**
- `Month` et `DayOfWeek` tournent en boucle avec `plus` et `minus` ;
- `getValue()` va de 1 (lundi) à 7 (dimanche) ;
- `date.with(DayOfWeek.MONDAY)` donne le lundi de la même semaine.

**Les décalages :**
- `ZoneOffset` est un décalage fixe (`+05:30`, `Z`) ;
- `ZoneId` est une région, avec ses règles d'heure d'été.

</details>
