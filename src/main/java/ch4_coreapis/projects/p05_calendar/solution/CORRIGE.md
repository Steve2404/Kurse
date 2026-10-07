# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Calendar.java`](Calendar.java).
>
> Les valeurs et les exceptions ci-dessous ont été obtenues en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Dates et immutabilité

**Le code de l'étape :**

```java
LocalDate jan31 = LocalDate.of(2024, Month.JANUARY, 31);
jan31.plusDays(1);
System.out.println(jan31 + " +1 mois = " + jan31.plusMonths(1) + " (bissextile " + jan31.isLeapYear() + "), 2023 : "
        + LocalDate.of(2023, 1, 31).plusMonths(1) + ", ignore : " + jan31);
LocalDate exam = LocalDate.of(2026, 10, 2);
System.out.println(exam + " est un " + exam.getDayOfWeek() + ", jour " + exam.getDayOfYear() + " de l'annee, +3 semaines "
        + exam.plusWeeks(3) + ", fin du mois " + exam.withDayOfMonth(exam.lengthOfMonth()));
```

**Le 31 janvier + 1 mois :** le 31 février n'existe pas. `plusMonths` **ajuste au dernier jour valide** : le 29 en 2024 (bissextile), le 28 en 2023. Pas d'exception. En revanche, `LocalDate.of(2023, 2, 29)` **lève** une exception, car on demande une date précise :

```
java.time.DateTimeException: Invalid date 'February 29' as '2023' is not a leap year
```

**Question — pourquoi la ligne « ignore » ?** Les classes de `java.time` sont **immuables**, comme `String`. `plusDays(1)` rend une **nouvelle** date et laisse `jan31` intact. Le résultat, non rangé, est perdu. Vérifié : `jan31` vaut toujours `2024-01-31`.

---

## Étape 2 — `Period`

**Le code de l'étape :**

```java
LocalDate birth = LocalDate.of(1995, 7, 14);
Period age = Period.between(birth, exam);
System.out.println("age : " + age + " = " + age.getYears() + " ans " + age.getMonths() + " mois " + age.getDays() + " jours, en jours "
        + ChronoUnit.DAYS.between(birth, exam));
System.out.println("Period.of(1, 2, 3) " + Period.of(1, 2, 3) + ", ofYears(1).ofWeeks(2) " + Period.ofYears(1).ofWeeks(2)
        + ", ofMonths(14) " + Period.ofMonths(14) + ", normalise " + Period.ofMonths(14).normalized() + ", exam + P1M " + exam.plus(Period.ofMonths(1)));
```

**Le piège `ofYears(1).ofWeeks(2)` :** `ofWeeks` est **statique**. Java l'autorise via une expression, mais l'objet `P1Y` est ignoré, et le résultat est `Period.ofWeeks(2)` = `P14D`. Il n'existe pas de `P2W` : les semaines sont converties en jours. Vérifié : `javac` sans option **ne dit rien**. Seul `javac -Xlint:static` affiche :

```
warning: [static] static method should be qualified by type name, Period, instead of by an expression
```

**Question — pourquoi `ofMonths(14)` n'est pas normalisé automatiquement ?** Un `Period` garde **exactement** les trois nombres qu'on lui donne (années, mois, jours). Il ne les recalcule jamais tout seul. `normalized()` convertit les mois en années (12 mois = 1 an), car cette règle est fixe. Il ne touche **jamais** aux jours, car un mois n'a pas un nombre fixe de jours. Vérifié : `ofMonths(14).normalized()` donne `P1Y2M`, mais `ofDays(45).normalized()` reste `P45D`.

---

## Étape 3 — `Duration` et heures

**Le code de l'étape :**

```java
LocalTime late = LocalTime.of(23, 30);
Duration meeting = Duration.ofMinutes(90);
System.out.println(late + " + 2 h = " + late.plusHours(2) + " (passe minuit), reunion " + meeting + ", " + meeting.toMinutes() + " min, "
        + Duration.ofSeconds(3725) + ", entre 08:15 et 17:40 : " + Duration.between(LocalTime.of(8, 15), LocalTime.of(17, 40)));
LocalDateTime start = LocalDateTime.of(2026, 10, 2, 14, 47, 33);
System.out.println("debut " + start + ", tronque a l'heure " + start.truncatedTo(ChronoUnit.HOURS) + ", minutes jusqu'a 18:00 "
        + ChronoUnit.MINUTES.between(start, start.withHour(18).withMinute(0).withSecond(0)));
```

**Lire `PT1H2M5S` :** `P` pour période, `T` pour « partie horaire », puis 1 heure, 2 minutes, 5 secondes : 3725 s = 3600 + 120 + 5. Un `Period` s'écrit sans `T` : `P1Y2M3D`.

**Les minutes jusqu'à 18:00 :** de 14:47:33 à 18:00:00, il y a 3 h 12 min 27 s. `ChronoUnit.MINUTES.between` compte les minutes **complètes** : **192**. Les 27 secondes de reste sont ignorées.

**Question — `Duration.between` sur deux `LocalDate` :** une **exception** à l'exécution. Une `LocalDate` n'a pas d'heure, donc pas de secondes à compter :

```
java.time.temporal.UnsupportedTemporalTypeException: Unsupported unit: Seconds
```

Ça compile, car les deux paramètres sont des `Temporal`. L'erreur n'apparaît qu'à l'exécution.

---

## Étape 4 — Fuseaux et changement d'heure

**Le code de l'étape :**

```java
ZonedDateTime departure = ZonedDateTime.of(LocalDateTime.of(2026, 3, 29, 10, 0), PARIS);
ZonedDateTime arrival = departure.plus(Duration.ofMinutes(510)).withZoneSameInstant(NEW_YORK);
ZonedDateTime gap = ZonedDateTime.of(LocalDateTime.of(2026, 3, 29, 2, 30), PARIS);
ZonedDateTime overlap = ZonedDateTime.of(LocalDateTime.of(2026, 10, 25, 2, 30), PARIS);
… overlap.withLaterOffsetAtOverlap()
ZonedDateTime night = ZonedDateTime.of(LocalDateTime.of(2026, 3, 29, 1, 0), PARIS);
Duration.between(night, night.withHour(4))
ChronoUnit.HOURS.between(night.toLocalDateTime(), night.withHour(4).toLocalDateTime())
```

**Le vol :** 10:00 à Paris le 29 mars = 08:00 UTC (Paris est passé en heure d'été, +02:00, à 02:00 ce matin-là). + 8 h 30 = 16:30 UTC. New York est déjà en heure d'été (−04:00), donc **12:30**. `withZoneSameInstant` garde le **même instant** et change seulement l'affichage. `withZoneSameLocal` garderait 18:30 et changerait l'instant.

**L'heure qui n'existe pas :** à 02:00, les horloges passent à 03:00. Pour 02:30, Java **avance de la durée du trou** (1 h) : 03:30+02:00. Pas d'exception.

**L'heure double :** le 25 octobre, de 02:00 à 03:00, l'heure se répète (+02:00, puis +01:00). Par défaut, Java garde le **premier** passage, le décalage d'**été** +02:00. `withLaterOffsetAtOverlap()` donne le second, +01:00.

**De 01:00 à 04:00 le 29 mars :**
- **Réellement** (entre deux `ZonedDateTime`) : **2 h**, puisque l'heure de 02:00 à 03:00 n'a pas existé.
- **Sur l'horloge** (entre deux `LocalDateTime`, sans fuseau) : **3 h**. Une `LocalDateTime` ne connaît pas les changements d'heure.

---

## Étape 5 — `Instant` et petits algorithmes

**Le code de l'étape :**

```java
static LocalDate nextFriday13(LocalDate from) {
    LocalDate d = from.withDayOfMonth(13);
    if (!d.isAfter(from)) {
        d = d.plusMonths(1);
    }
    while (d.getDayOfWeek() != DayOfWeek.FRIDAY) {
        d = d.plusMonths(1);
    }
    return d;
}

static int workingDays(LocalDate from, LocalDate toExclusive) {
    int count = 0;
    for (LocalDate d = from; d.isBefore(toExclusive); d = d.plusDays(1)) {
        DayOfWeek day = d.getDayOfWeek();
        if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
            count++;
        }
    }
    return count;
}

static void month(int year, Month month) {
    LocalDate first = LocalDate.of(year, month, 1);
    System.out.println(month + " " + year + " (" + first.lengthOfMonth() + " jours)");
    System.out.println(" Lu Ma Me Je Ve Sa Di");
    String line = "   ".repeat(first.getDayOfWeek().getValue() - 1);
    for (LocalDate d = first; d.getMonth() == month; d = d.plusDays(1)) {
        line += (d.getDayOfMonth() < 10 ? "  " : " ") + d.getDayOfMonth();
        if (d.getDayOfWeek() == DayOfWeek.SUNDAY) {
            System.out.println(line);
            line = "";
        }
    }
    if (!line.isEmpty()) {
        System.out.println(line);
    }
}
```

**`Instant` :** `Instant.parse("2026-10-02T08:00:00Z")` est un instant UTC. `atZone(PARIS)` l'affiche à Paris : **10:00**, car Paris est en heure d'été (+02:00) le 2 octobre.

**Le piège du vendredi 13 :** le 13 février 2026 **est** un vendredi (vérifié). Sans le `!d.isAfter(from)`, on rendrait la date de départ elle-même. Avec, on cherche à partir de mars, et le 13 mars 2026 est aussi un vendredi. C'est le cas chaque fois que février a 28 jours, puisque 28 jours font 4 semaines exactes.

**Le calendrier :** le 1er février 2026 est un dimanche, donc `getValue()` = 7 et 6 cases vides de 3 espaces. Le 1 est seul sur la 1re ligne, et la ligne s'imprime aussitôt (dimanche).
