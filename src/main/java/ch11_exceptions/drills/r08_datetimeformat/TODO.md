# Drill de rappel 8 — `DateTimeFormatter`

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall08`** dans le paquet `ch11_exceptions.drills.r08_datetimeformat`.
- Ajoute `static String v(String)`, qui remplace U+00A0 et U+202F par `_`. Passe-lui la ligne D04.
- Utilise `LocalDateTime t = LocalDateTime.of(2026, 7, 4, 15, 5, 9)`.

## Défis

- ☐ **D01.** Trois motifs :
  - `"yyyy-MM-dd HH:mm:ss"` ;
  - `"d/M/yy h:m a"` (en `Locale.US`) ;
  - `"MMM MMMM E EEEE"` (en `Locale.US`).
  → `D01 : 2026-07-04 15:05:09 | 4/7/26 3:5 PM | Jul July Sat Saturday`
- ☐ **D02.** `french = ofPattern("EEEE d MMMM", Locale.FRANCE)`. Affiche `t.format(french)`, `french.format(t)`, puis `french.withLocale(Locale.GERMANY).format(t)`.
  → `D02 : samedi 4 juillet | samedi 4 juillet | Samstag 4 Juli`
- ☐ **D03.** Le motif `"'Le' dd 'a' HH'h'mm"`, puis le motif `"hh 'o''clock'"`.
  → `D03 : Le 04 a 15h05 | 03 o'clock`
- ☐ **D04.** Les styles localisés :
  - `ofLocalizedDate(FormatStyle.SHORT)` en `Locale.US` ;
  - `ofLocalizedDate(FormatStyle.LONG)` en `Locale.FRANCE` ;
  - `ofLocalizedDateTime(FormatStyle.MEDIUM)` en `Locale.US` ;
  - `ofLocalizedTime(FormatStyle.SHORT)` en `Locale.GERMANY`.
  → `D04 : 7/4/26 | 4 juillet 2026 | Jul 4, 2026, 3:05:09 PM | 15:05`
- ☐ **D05.** `z = t.atZone(ZoneId.of("Europe/Paris"))`. Affiche :
  - `z` avec `"HH:mm z VV xxx"` (en `Locale.US`) ;
  - `ISO_LOCAL_DATE.format(t)` ;
  - `ISO_LOCAL_TIME.format(t)`.
  → `D05 : 15:05 CEST Europe/Paris +02:00 | 2026-07-04 | 15:05:09`
- ☐ **D06.** Trois cas :
  - `LocalTime.of(8, 0)` formaté avec `"yyyy"` → `catch (DateTimeException e)` : nom simple ;
  - `LocalDate.parse("04/07/2026", ofPattern("dd-MM-yyyy"))` → `catch (DateTimeParseException e)` : `index <getErrorIndex()>` ;
  - la même date avec `"dd/MM/yyyy"`.
  → `D06 : UnsupportedTemporalTypeException | index 2 | 2026-07-04`
- ☐ **D07.** `ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM)` en `Locale.US` sur `t` ; puis `ofLocalizedDateTime(FormatStyle.LONG)` en `Locale.US` sur `z`. La ligne passe par `v`.
  → `D07 : 7/4/26, 3:05:09 PM | July 4, 2026 at 3:05:09 PM CEST`

## Expériences (hors sortie attendue)

1. `ofPattern("mm")` contre `ofPattern("MM")` : que donne chacun pour `t` ?
2. `ofLocalizedDateTime(FormatStyle.LONG)` sur `t` (un `LocalDateTime`), puis sur `z` : que se passe-t-il ?
3. `ofPattern("yyyy-MM-dd'T")` : quelle exception, et quand (à la création ou au formatage) ?
4. `t.format(DateTimeFormatter.ISO_LOCAL_DATE)` ne contient pas l'heure : pourquoi est-ce permis ?

## Sortie attendue complète

```
D01 : 2026-07-04 15:05:09 | 4/7/26 3:5 PM | Jul July Sat Saturday
D02 : samedi 4 juillet | samedi 4 juillet | Samstag 4 Juli
D03 : Le 04 a 15h05 | 03 o'clock
D04 : 7/4/26 | 4 juillet 2026 | Jul 4, 2026, 3:05:09 PM | 15:05
D05 : 15:05 CEST Europe/Paris +02:00 | 2026-07-04 | 15:05:09
D06 : UnsupportedTemporalTypeException | index 2 | 2026-07-04
D07 : 7/4/26, 3:05:09 PM | July 4, 2026 at 3:05:09 PM CEST
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Lettre | Sens | Exemple |
|---|---|---|
| `y` / `u` | année | `yy` 26, `yyyy` 2026 |
| `M` | mois | `M` 7, `MM` 07, `MMM` Jul, `MMMM` July |
| `d` | jour du mois | `d` 4, `dd` 04 |
| `E` | jour de la semaine | `E` Sat, `EEEE` Saturday |
| `H` / `h` | heure 0-23 / 1-12 | `HH` 15, `hh` 03 |
| `m` / `s` | minutes / secondes | `mm` 05 |
| `a` | AM / PM | PM |
| `z` / `VV` / `xxx` | nom du fuseau / identifiant / décalage | CEST / Europe/Paris / +02:00 |
| `'…'` | texte littéral (`''` pour une apostrophe) | |

- **Les deux sens marchent :** `t.format(f)` et `f.format(t)`.
- **Champ absent du type** (heure sur une `LocalDate`) : `UnsupportedTemporalTypeException`.
- **`FormatStyle.LONG` et `FULL`** pour une heure exigent un fuseau (`ZonedDateTime`), sinon `DateTimeException`.
- **`ofLocalizedDateTime(styleDate, styleHeure)`** combine deux styles.
- **Lecture ratée :** `DateTimeParseException` (non vérifiée), avec `getErrorIndex()`.

</details>
