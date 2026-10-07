# Projet 6 — L'agenda international (`DateTimeFormatter`)

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 11) :**
- **`DateTimeFormatter`** :
  - `ofPattern(motif)` et `ofPattern(motif, locale)` ;
  - les constantes `ISO_LOCAL_DATE_TIME`, `ISO_LOCAL_DATE`, `ISO_LOCAL_TIME` ;
  - `ofLocalizedDate`, `ofLocalizedTime` et `ofLocalizedDateTime`, avec `FormatStyle` (`FULL`, `LONG`, `MEDIUM`, `SHORT`) ;
  - `withLocale` et `localizedBy` ;
- les lettres de motif `u y M d E H h m s a z xxx` ;
- **l'échappement** : `'texte'`, et `''` pour une apostrophe ;
- `date.format(f)` comme `f.format(date)` ;
- **les erreurs :**
  - `DateTimeParseException` (`getErrorIndex`, `getParsedString`) ;
  - `UnsupportedTemporalTypeException` (champ absent du type) ;
  - `DateTimeException` (un style `FULL` sans fuseau) ;
- **le résolveur SMART** des motifs : le « 30 février » devient le 28 ; `ISO_…` est STRICT ;
- une chaîne de **repli**, où chaque échec devient une exception **supprimée** de l'erreur finale.

Côté algorithmes :
- le N-ième jour de semaine du mois ;
- l'ajout de **jours ouvrés** (sans week-ends ni fériés) ;
- le **même instant** dans plusieurs fuseaux, avec un décalage qui change selon l'heure d'été.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch11_exceptions.projects.p06_agenda` :
- le record `Event` et `UnreadableDateException` ;
- `DateReader` et `Planner` ;
- **`Agenda`** (le `main`).

**Règle du crescendo :** chapitres 1 à 11. Pas de `now()` : les dates viennent de `Data`.

---

## Tableau de bord

### ☐ Étape 1 — Lire des dates de toutes les formes

```
lu "March 27, 2026 4:00 PM" -> 2026-03-27T16:00
lu "2026-02-30 10:00" -> 2026-02-28T10:00
date illisible : 2026-04-32 10:00 (4 essais) ; dernier : Text '2026-04-32 10:00' could not be parsed: Invalid value for DayOfMonth (valid values 1 - 28/31): 32
```
- **Premier appel du `main` :** `Locale.setDefault(Locale.US)`.
- **`record Event(LocalDateTime when, String title)`**.
- **`UnreadableDateException extends Exception`** : construite avec `(String text)`, message `date illisible : <texte>`.
- **`DateReader(String[] patterns)`** garde une liste de formats :
  - d'abord `DateTimeFormatter.ISO_LOCAL_DATE_TIME` ;
  - puis `DateTimeFormatter.ofPattern(motif, Locale.ENGLISH)` pour chaque motif.
- **`LocalDateTime read(String text) throws UnreadableDateException`** :
  - essaie chaque format avec `LocalDateTime.parse(text, format)`, et rend le premier succès ;
  - chaque `DateTimeParseException` est gardée ;
  - à la fin : une `UnreadableDateException`, avec chaque échec ajouté par `addSuppressed`, puis lancée.
- **Dans `main`**, pour chaque `texte|titre` de `Data.RAW` (avec `Data.PATTERNS`) :
  - succès : `lu "<texte>" -> <LocalDateTime>` (son `toString()`), et l'événement est gardé ;
  - échec : `<message> (<nb de supprimées> essais) ; dernier : <message de la dernière supprimée>`.
- **Questions :**
  - Pourquoi le 30 février est-il accepté par `uuuu-MM-dd`, et refusé par `ISO_LOCAL_DATE_TIME` ?
  - Pourquoi le 32 est-il refusé partout ?

### ☐ Étape 2 — L'agenda en français, récurrences, jours ouvrés

```
  samedi 28 février 2026 a 10h00 : budget
recurrence 2e TUESDAY : 10.03.2026, 14.04.2026, 12.05.2026, 09.06.2026
echeance 2026-04-01 + 10 jours ouvres = jeu. 16/04, sautes [sam. 04/04, dim. 05/04, lun. 06/04, sam. 11/04, dim. 12/04]
```
- **L'agenda :** trie les événements par date. Affiche chacun `  <date> : <titre>` (deux espaces devant), avec `ofPattern("EEEE d MMMM uuuu 'a' HH'h'mm", Locale.FRANCE)`.
- **`Planner.nthWeekday(int n, DayOfWeek day, YearMonth first, int count)`** : pour chaque mois, le 1er jour voulu du mois, puis `plusWeeks(n - 1)`.
  - Appel : `Data.NTH`, `DayOfWeek.valueOf(Data.WEEKDAY)`, `YearMonth.parse(Data.FIRST_MONTH)` et `Data.OCCURRENCES`.
  - Affichage avec `ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMANY)`, joint par `, `.
- **`Planner.addBusinessDays(LocalDate start, int days, Set<LocalDate> holidays, List<LocalDate> skipped)`** : avance jour par jour. Un jour ouvré décompte, un autre jour va dans `skipped`.
  - Appel : `Data.START`, `Data.BUSINESS_DAYS` et `Data.HOLIDAYS` (lus par `LocalDate.parse`).
  - Affichage avec `ofPattern("EEE dd/MM", Locale.FRANCE)`.

### ☐ Étape 3 — Fuseaux et styles localisés

```
reunion : | Europe/Paris Fri 16:00 CET (+01:00) | America/New_York Fri 11:00 EDT (-04:00) | Asia/Tokyo Sat 00:00 JST (+09:00)
en-US : | Saturday, March 14, 2026 | March 14, 2026 | Mar 14, 2026 | 3/14/26 | 9:30 AM
```
- **La réunion :** pour la semaine 0, puis la semaine 1 :
  - `LocalDateTime.parse(Data.MEETING).plusWeeks(semaine).atZone(ZoneId.of(Data.ZONES[0]))` ;
  - pour chaque zone de `Data.ZONES` : ` | <zone> ` suivi de `withZoneSameInstant(ZoneId.of(zone))`, formaté avec `ofPattern("EEE HH:mm z (xxx)", Locale.US)`.
  - **Question :** pourquoi l'écart Paris – New York passe-t-il de 5 h à 6 h ?
- **Les styles localisés** (ligne rendue visible avec `visible`, comme au projet 5) : pour chaque balise de `Data.LOCALES`, et la date `Data.SAMPLE` :
  - ` | ` suivi de `ofLocalizedDate(style).withLocale(locale)` pour chaque style de `FormatStyle.values()` ;
  - puis ` | ` suivi de `ofLocalizedTime(FormatStyle.SHORT).localizedBy(locale)`.

### ☐ Étape 4 — Constantes ISO, apostrophe et erreurs

```
ISO : 2026-03-14 09:30:00 ; apostrophe : 09 o'clock AM, Mar 14
date sans heure : UnsupportedTemporalTypeException Unsupported field: HourOfDay
FULL sans zone : DateTimeException Unable to extract ZoneId from temporal 2026-03-14T09:30
parse ISO : Text '14.03.2026' could not be parsed at index 0 (index 0, texte 14.03.2026)
```
- **La ligne `ISO`** :
  - `sample.format(DateTimeFormatter.ISO_LOCAL_DATE)` ;
  - `DateTimeFormatter.ISO_LOCAL_TIME.format(sample)` ;
  - puis le motif `"hh 'o''clock' a, MMM dd"` en `Locale.US`.
- **Trois erreurs**, chacune dans son `try` :
  1. formater `LocalDate.parse("2026-03-14")` avec `ofPattern("HH:mm")` → `catch (DateTimeException e)` : nom simple et message ;
  2. formater `sample` avec `ofLocalizedDateTime(FormatStyle.FULL)` → idem ;
  3. `LocalDate.parse("14.03.2026")` → `catch (DateTimeParseException e)` : message, `getErrorIndex()` et `getParsedString()`.
- **Question :** pourquoi le 1er `catch` attrape-t-il une `UnsupportedTemporalTypeException` ?

---

## Checklist (vérifiée par `Check`)

- les `Data.…` utilisées, `record Event(` ;
- `DateTimeFormatter.ISO_LOCAL_DATE_TIME`, `Locale.ENGLISH`, `catch (DateTimeParseException`, `addSuppressed`, `.getSuppressed()`, `LocalDateTime.parse(` ;
- `Locale.FRANCE`, `ofLocalizedDate(FormatStyle.MEDIUM)`, `.withLocale(`, `FormatStyle.values()`, `ofLocalizedTime(FormatStyle.SHORT)`, `.localizedBy(` ;
- `.withZoneSameInstant(`, `ZoneId.of(`, `DateTimeFormatter.ISO_LOCAL_DATE`, `DateTimeFormatter.ISO_LOCAL_TIME` ;
- `ofLocalizedDateTime(FormatStyle.FULL)`, `catch (DateTimeException`, `.getErrorIndex()`, `.getParsedString()` ;
- `YearMonth`, `.plusWeeks(`.

---

## Sortie attendue complète

```
lu "2026-03-14T09:30" -> 2026-03-14T09:30
lu "20/03/2026 14:00" -> 2026-03-20T14:00
lu "March 27, 2026 4:00 PM" -> 2026-03-27T16:00
lu "2026-02-30 10:00" -> 2026-02-28T10:00
lu "31/04/2026 08:00" -> 2026-04-30T08:00
date illisible : 2026-04-32 10:00 (4 essais) ; dernier : Text '2026-04-32 10:00' could not be parsed: Invalid value for DayOfMonth (valid values 1 - 28/31): 32
date illisible : demain matin (4 essais) ; dernier : Text 'demain matin' could not be parsed at index 0
  samedi 28 février 2026 a 10h00 : budget
  samedi 14 mars 2026 a 09h30 : lancement
  vendredi 20 mars 2026 a 14h00 : revue
  vendredi 27 mars 2026 a 16h00 : demo client
  jeudi 30 avril 2026 a 08h00 : audit
recurrence 2e TUESDAY : 10.03.2026, 14.04.2026, 12.05.2026, 09.06.2026
echeance 2026-04-01 + 10 jours ouvres = jeu. 16/04, sautes [sam. 04/04, dim. 05/04, lun. 06/04, sam. 11/04, dim. 12/04]
reunion : | Europe/Paris Fri 16:00 CET (+01:00) | America/New_York Fri 11:00 EDT (-04:00) | Asia/Tokyo Sat 00:00 JST (+09:00)
reunion : | Europe/Paris Fri 16:00 CEST (+02:00) | America/New_York Fri 10:00 EDT (-04:00) | Asia/Tokyo Fri 23:00 JST (+09:00)
en-US : | Saturday, March 14, 2026 | March 14, 2026 | Mar 14, 2026 | 3/14/26 | 9:30 AM
fr-FR : | samedi 14 mars 2026 | 14 mars 2026 | 14 mars 2026 | 14/03/2026 | 09:30
de-DE : | Samstag, 14. März 2026 | 14. März 2026 | 14.03.2026 | 14.03.26 | 09:30
ISO : 2026-03-14 09:30:00 ; apostrophe : 09 o'clock AM, Mar 14
date sans heure : UnsupportedTemporalTypeException Unsupported field: HourOfDay
FULL sans zone : DateTimeException Unable to extract ZoneId from temporal 2026-03-14T09:30
parse ISO : Text '14.03.2026' could not be parsed at index 0 (index 0, texte 14.03.2026)
```
