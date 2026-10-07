# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Dates et immutabilité

<details><summary>Indice 1</summary>

`LocalDate.of(2024, Month.JANUARY, 31)`, puis `plusMonths(1)`, `isLeapYear()`, `getDayOfWeek()`, `getDayOfYear()`, `plusWeeks(3)`. N'utilise **jamais** `now()` : la sortie doit être reproductible.

</details>

<details><summary>Indice 2</summary>

Fin du mois : `d.withDayOfMonth(d.lengthOfMonth())`. Pour la ligne « ignore », appelle `jan31.plusDays(1);` **sans** ranger le résultat, puis affiche `jan31`.

</details>

---

## Étape 2 — `Period`

<details><summary>Indice 1</summary>

`Period.between(naissance, examen)`, puis `getYears()`, `getMonths()` et `getDays()`. Le total en jours : `ChronoUnit.DAYS.between(naissance, examen)`.

</details>

<details><summary>Indice 2</summary>

`exam.plus(Period.ofMonths(1))` ajoute un mois. `normalized()` convertit les mois en années ; il ne touche jamais aux jours.

</details>

---

## Étape 3 — `Duration` et heures

<details><summary>Indice 1</summary>

`LocalTime.of(23, 30).plusHours(2)`, `Duration.ofMinutes(90)`, `toMinutes()`, `Duration.ofSeconds(3725)`, `Duration.between(heure1, heure2)`.

</details>

<details><summary>Indice 2</summary>

`truncatedTo(ChronoUnit.HOURS)` met les minutes et les secondes à 0. Pour 18:00 : `start.withHour(18).withMinute(0).withSecond(0)`, puis `ChronoUnit.MINUTES.between`.

</details>

---

## Étape 4 — Fuseaux et changement d'heure

<details><summary>Indice 1</summary>

`ZonedDateTime.of(LocalDateTime.of(…), ZoneId.of("Europe/Paris"))`. Arrivée : `departure.plus(Duration.ofMinutes(510)).withZoneSameInstant(ZoneId.of("America/New_York"))`.

</details>

<details><summary>Indice 2</summary>

`withLaterOffsetAtOverlap()` donne le 2e passage d'une heure double. Pour les « 3 h d'horloge », convertis les deux instants en `LocalDateTime` (`toLocalDateTime()`) avant `ChronoUnit.HOURS.between`.

</details>

---

## Étape 5 — `Instant` et petits algorithmes

<details><summary>Indice 1</summary>

Vendredi 13 : pars du 13 du mois de départ (`withDayOfMonth(13)`). S'il n'est pas **strictement après** la date de départ, passe au mois suivant. Puis avance d'un mois tant que ce 13 n'est pas un vendredi.

</details>

<details><summary>Indice 2</summary>

Calendrier : 1re ligne `"   ".repeat(jourDuPremier - 1)`, puis une boucle `for (LocalDate d = premier; d.getMonth() == mois; d = d.plusDays(1))`. Chaque jour est aligné sur 3 caractères, et on imprime la ligne après chaque `DayOfWeek.SUNDAY`. N'oublie pas la dernière ligne incomplète.

</details>
