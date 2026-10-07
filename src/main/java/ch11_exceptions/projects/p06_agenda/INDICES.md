# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Lire des dates de toutes les formes

<details><summary>Indice 1</summary>

`read` : une boucle sur les formats. `try { return LocalDateTime.parse(text, format); } catch (DateTimeParseException e) { failures.add(e); }`. Le premier succès sort de la méthode.

</details>

<details><summary>Indice 2</summary>

Après la boucle : `UnreadableDateException e = new UnreadableDateException(text); failures.forEach(e::addSuppressed); throw e;`. Le dernier essai est `e.getSuppressed()[length - 1]`.

</details>

---

## Étape 2 — L'agenda en français, récurrences, jours ouvrés

<details><summary>Indice 1</summary>

Dans un motif, le texte entre apostrophes est recopié tel quel : `'a'`, et `'h'` entre `HH` et `mm`. `EEEE` donne le jour complet, `MMMM` le mois complet, dans la langue de la locale.

</details>

<details><summary>Indice 2</summary>

- `nthWeekday` : `first.plusMonths(i).atDay(1)`, puis avance d'un jour tant que ce n'est pas le bon jour de la semaine, puis `plusWeeks(n - 1)`.
- `addBusinessDays` : `plusDays(1)` en boucle, et ne décompte que les jours ouvrés.

</details>

---

## Étape 3 — Fuseaux et styles localisés

<details><summary>Indice 1</summary>

`atZone(ZoneId.of("Europe/Paris"))` donne l'instant à Paris. `withZoneSameInstant(ZoneId.of(…))` montre le **même instant** ailleurs.

</details>

<details><summary>Indice 2</summary>

`FormatStyle.values()` donne FULL, LONG, MEDIUM, SHORT. `ofLocalizedDate(style).withLocale(locale)` pour la date. `ofLocalizedTime(FormatStyle.SHORT).localizedBy(locale)` pour l'heure.

</details>

---

## Étape 4 — Constantes ISO, apostrophe et erreurs

<details><summary>Indice 1</summary>

Une apostrophe littérale s'écrit `''` (deux apostrophes) dans un motif.

</details>

<details><summary>Indice 2</summary>

`DateTimeParseException` a `getErrorIndex()` (la position de l'erreur) et `getParsedString()` (le texte lu).

</details>
