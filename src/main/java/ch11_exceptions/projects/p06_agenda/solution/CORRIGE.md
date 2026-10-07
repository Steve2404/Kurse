# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Event`, `UnreadableDateException`, `DateReader`, `Planner` et `Agenda`.
>
> Les valeurs et les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`java` 17.0.18).

---

## Étape 1 — Lire des dates de toutes les formes

**Le code :** [`DateReader.java`](DateReader.java) et la 1re boucle du `main` d'[`Agenda.java`](Agenda.java).

**`Locale.ENGLISH` pour les motifs :** `"March"` et `"PM"` sont des mots anglais. Sans locale explicite, le format utiliserait la locale par défaut, et un ordinateur français ne lirait pas `March`.

**Les supprimées pour garder l'historique :** `DateTimeParseException` est non vérifiée, mais on l'attrape pour essayer le format suivant. Chaque échec est attaché à l'exception finale (`addSuppressed`), comme au projet 3. On sait ainsi combien de formats ont été essayés, et pourquoi le dernier a échoué.

**Question — le 30 février :** les deux formats n'ont pas le même **style de résolution** (vérifié) :
- `DateTimeFormatter.ofPattern(…)` est en **SMART** par défaut : un jour du mois entre 29 et 31 qui n'existe pas est **ramené** au dernier jour valide. `2026-02-30 10:00` devient `2026-02-28T10:00` ;
- `ISO_LOCAL_DATE_TIME` est en **STRICT** : le 30 février est refusé, avec `Invalid date 'FEBRUARY 30'`. Ici, il refuse même avant de regarder le jour : le texte a une **espace** au lieu du `T`, d'où `could not be parsed at index 10`.

Même effet SMART pour `31/04/2026 08:00`, qui devient `2026-04-30T08:00` : avril n'a que 30 jours.

**Question — pourquoi le 32 est refusé partout ?** Même en SMART, un jour du mois doit être entre **1 et 31**. 32 n'est valide pour **aucun** mois : `Invalid value for DayOfMonth (valid values 1 - 28/31): 32`. SMART ne corrige que les jours possibles dans un autre mois.

---

## Étape 2 — L'agenda en français, récurrences, jours ouvrés

**Le code :** le tri et l'affichage de l'agenda, puis [`Planner.java`](Planner.java).

**Les motifs localisés :** `EEEE d MMMM` avec `Locale.FRANCE` donne `samedi 28 février`. Les noms des jours et des mois viennent de la locale, pas du motif.

**Le 2e mardi :** pour chaque mois, le 1er mardi, puis + 1 semaine. En mars 2026, le 1er mardi est le 3, d'où le **10**.

**Les jours ouvrés :** à partir du 1er avril 2026, on saute les week-ends et le lundi de Pâques (6 avril, dans `HOLIDAYS`). Le 10e jour ouvré tombe le **jeudi 16 avril**.

---

## Étape 3 — Fuseaux et styles localisés

**Le code :** les boucles « réunion » et « styles ».

**Question — pourquoi l'écart Paris – New York passe de 5 h à 6 h ?** Les deux pays ne changent **pas d'heure le même jour**. Vérifié :
- le **27 mars 2026**, Paris est encore à **+01:00** (heure d'hiver), et New York déjà à **−04:00** (heure d'été depuis le 8 mars). L'écart est de **5 h** ;
- une semaine plus tard, le **3 avril**, Paris est passé à **+02:00** (changement le 29 mars). L'écart est de **6 h**.

`withZoneSameInstant` tient compte automatiquement des règles de chaque fuseau. Tokyo n'a pas d'heure d'été : l'heure de la réunion y passe de `Sat 00:00` à `Fri 23:00`.

**Les styles :**
- en `en-US`, on obtient `Saturday, March 14, 2026` (FULL), puis `March 14, 2026` (LONG), puis `Mar 14, 2026` (MEDIUM), puis `3/14/26` (SHORT), et l'heure `9:30 AM` ;
- chaque locale a ses propres conventions, et `ofLocalizedDate` les applique sans motif écrit à la main.

---

## Étape 4 — Constantes ISO, apostrophe et erreurs

**Le code :** la fin du `main`.

**L'apostrophe :** `'o''clock'` est un texte littéral (`o…clock`) qui contient une apostrophe écrite `''`. On obtient `09 o'clock AM`.

**Les trois erreurs :**
1. **`HH:mm` sur une `LocalDate`** : une date n'a **pas** d'heure. `UnsupportedTemporalTypeException: Unsupported field: HourOfDay`.
2. **`FULL` sur une `LocalDateTime`** : le style FULL d'une date-heure inclut le **fuseau** (« heure normale d'Europe centrale »…), et une `LocalDateTime` n'en a pas. `DateTimeException: Unable to extract ZoneId from temporal …`.
3. **`LocalDate.parse("14.03.2026")`** : le format par défaut est ISO (`uuuu-MM-dd`). L'erreur est à l'**index 0** : `14.` ne peut pas être le début d'une année sur 4 chiffres suivie d'un tiret.

**Question — pourquoi le 1er `catch (DateTimeException e)` attrape une `UnsupportedTemporalTypeException` ?** Parce qu'elle en est une **fille** : `UnsupportedTemporalTypeException` hérite de `DateTimeException`. Un `catch` attrape aussi toutes les sous-classes du type déclaré.
