# Projet 5 — La facturation internationale (`NumberFormat`, `DecimalFormat`)

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 11) :**
- **`NumberFormat`** et ses fabriques : `getInstance`, `getNumberInstance`, `getCurrencyInstance`, `getPercentInstance`, `getIntegerInstance`, `getCompactNumberInstance` (styles `SHORT` et `LONG`) ;
- les réglages `setMinimumFractionDigits`, `setMaximumFractionDigits`, `setRoundingMode` ;
- l'**arrondi par défaut HALF_EVEN** (au pair) ;
- **`DecimalFormat`** : `0` contre `#`, le groupement `,`, le sous-motif négatif `;`, le texte littéral `'…'`, le `%` ;
- **`DecimalFormatSymbols`** ;
- **`parse`** : il lit le plus long **début** valide ; `ParseException` (vérifiée) et `getErrorOffset()` ;
- `Locale.setDefault`, `Locale.forLanguageTag`, `String.format(Locale, …)`.

Côté algorithmes :
- la **TVA par taux**, arrondie au centime sur la base cumulée ;
- le **partage au plus fort reste** (la somme tombe juste) ;
- le **tableau d'amortissement** d'un prêt (la dernière échéance solde le reste).

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch11_exceptions.projects.p05_invoice` :
- le record `InvoiceLine` ;
- `Invoice` et `Money` ;
- **`Billing`** (le `main`).

**À savoir :** plusieurs locales séparent les milliers par une espace **insécable** (U+00A0) ou **fine insécable** (U+202F), invisibles à l'écran. Écris `static String visible(String)`, qui les remplace toutes les deux par `_`, et passe-lui chaque ligne marquée « visible » ci-dessous.

**Règle du crescendo :** chapitres 1 à 11 (voir `PARCOURS.md`).

---

## Tableau de bord

### ☐ Étape 1 — La facture

```
facture : 4 lignes, total TTC en-US=$835.80 fr-FR=835,80_€ de-DE=835,80_€ de-CH=CHF_835.80 ja-JP=￥836
TVA : | 0_% ou 0,0_% de 450,00_€ = 0,00_€ | 6_% ou 5,5_% de 35,96_€ = 1,98_€ | 20_% ou 20,0_% de 289,88_€ = 57,98_€
```
- **Première ligne du `main` :** `Locale.setDefault(Locale.US)`. La machine peut être réglée en allemand.
- **`record InvoiceLine(int quantity, String label, long unitCents, int vatPerMille)`**, avec `parse` (sur `|`) et `long net()` = quantité × prix.
- **`Invoice`** (une liste de lignes) :
  - `net()` ;
  - `Map<Integer, Long> basesByRate()` : taux → base cumulée, dans une `TreeMap` ;
  - `static long vat(long base, int perMille)` = `Math.round(base * perMille / 1000.0)` ;
  - `totalVat()` = la somme des TVA **par taux** ;
  - `total()` et `size()`.
- **La ligne `facture`** (visible) : pour chaque balise de `Data.LOCALES`, ` <balise>=` suivi de `NumberFormat.getCurrencyInstance(Locale.forLanguageTag(balise)).format(total / 100.0)`.
  - **Question :** le même nombre devient $, €, CHF, ￥… Y a-t-il une conversion ?
- **La ligne `TVA`** (visible) : pour chaque taux, ` | ` suivi de quatre morceaux :
  - le taux (`perMille / 1000.0`) avec `getPercentInstance(Locale.FRANCE)` ;
  - ` ou `, puis le même avec `setMinimumFractionDigits(1)` ;
  - ` de `, puis la base en euros (`getCurrencyInstance(Locale.FRANCE)`) ;
  - ` = `, puis la TVA en euros.
  - **Question :** pourquoi 5,5 % s'affiche-t-il `6 %` ?

### ☐ Étape 2 — Arrondis et motifs

```
arrondis : 2 4 3 | 1 2.67 1,234.568 1.234,50
motif #,##0.00 : [1,234.50] [-7.25] [0.08]
motif '#'000 : [#1234] [-#007] [#000]
```
- **La ligne `arrondis`**, dans l'ordre :
  1. `getIntegerInstance(Locale.US)` sur 2.5, puis sur 3.5 ;
  2. le même avec `setRoundingMode(RoundingMode.HALF_UP)` sur 2.5 ;
  3. ` | `, puis `getNumberInstance(Locale.US)` avec `setMaximumFractionDigits(2)` sur 1.005, puis sur 2.675 ;
  4. `getInstance(Locale.US)` sur 1234.56789 ;
  5. `String.format(Locale.GERMANY, "%,.2f", 1234.5)`.
  - **Question :** pourquoi 1.005 donne-t-il `1`, et 2.675 donne-t-il `2.67` ?
- **Les motifs :** pour chaque motif de `Data.PATTERNS`, `new DecimalFormat(motif, DecimalFormatSymbols.getInstance(Locale.US))` appliqué à chaque valeur de `Data.SAMPLES`. Format de la ligne : `motif <motif> :` suivi de ` [<résultat>]` par valeur.

### ☐ Étape 3 — Compact et lecture

```
compact 1234 : 1K | 1.2K | 1 thousand | 1 Tausend
lu fr-FR nombre "1 234,56" -> 1
lu en-US monnaie "12.50" -> ParseException Unparseable number: "12.50" (position 0)
```
- **Le format compact** (visible), pour chaque nombre de `Data.BIG` : `compact <n> : ` suivi de quatre formats séparés par ` | ` :
  - `getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT)` ;
  - le même avec `setMaximumFractionDigits(1)` ;
  - le style `LONG` en `Locale.US` ;
  - le style `LONG` en `Locale.GERMANY`.
- **La lecture :** pour chaque `locale|genre|texte` de `Data.INPUTS`, prends `getCurrencyInstance` (genre `monnaie`) ou `getNumberInstance` (genre `nombre`) de la locale, puis `parse(texte)` :
  - succès : `lu <locale> <genre> "<texte>" -> <nombre>` ;
  - `catch (ParseException e)` : `-> ParseException <message> (position <getErrorOffset()>)`.
  - **Questions :**
    - Pourquoi `"1 234,56"` donne-t-il `1` en français ?
    - Et `"12abc"`, pourquoi `12` ?

### ☐ Étape 4 — Partage et prêt

```
partage de 835,80_€ en [2, 3, 4] : 185,73_€ 278,60_€ 371,47_€ (somme 835,80_€)
mois  1 : mensualite     849,67, interets   30,00, capital     819,67, reste   9_180,33
cout du credit : 196,06_€
```
- **`Money.split(long total, int[] weights)` (plus fort reste)** :
  1. chacun reçoit `total * w / somme` ;
  2. les centimes manquants vont, un par un, aux plus grands restes `total * w % somme` (à égalité, le premier).
  
  Affiche la ligne `partage` (visible) avec `Data.WEIGHTS`, les parts en euros, puis la somme.
- **`Money.schedule(long principal, int ratePerMille, int months)`** rend une liste de lignes `{mois, mensualité, intérêts, capital, reste}` :
  - `r = ratePerMille / 1000.0 / 12` ;
  - mensualité = `Math.round(P * r / (1 - Math.pow(1 + r, -n)))` ;
  - chaque mois, intérêts = `Math.round(reste * r)`, capital = mensualité − intérêts ;
  - le **dernier** mois paie `reste + intérêts`.
- **L'affichage du prêt** (`Data.LOAN`, `Data.LOAN_RATE`, `Data.MONTHS`) : les mois 1, 2 et le dernier, avec
  `String.format(Locale.FRANCE, "mois %2d : mensualite %,10.2f, interets %,7.2f, capital %,10.2f, reste %,10.2f", …)`, rendu visible.
  - Termine par `cout du credit : <somme des intérêts en euros>` (visible).

---

## Checklist (vérifiée par `Check`)

- les `Data.…` utilisées, `record InvoiceLine(` ;
- `Locale.setDefault(Locale.US)`, `Locale.forLanguageTag(` ;
- les cinq fabriques de `NumberFormat`, et `NumberFormat.getCompactNumberInstance(` avec `NumberFormat.Style.SHORT` et `LONG` ;
- `.setMinimumFractionDigits(`, `.setMaximumFractionDigits(`, `.setRoundingMode(RoundingMode.HALF_UP)` ;
- `String.format(Locale.GERMANY` et `String.format(Locale.FRANCE` ;
- `new DecimalFormat(`, `DecimalFormatSymbols.getInstance(Locale.US)` ;
- `.parse(`, `catch (ParseException`, `.getErrorOffset()` ;
- `Math.pow(`, `Math.round(`.

---

## Sortie attendue complète

```
facture : 4 lignes, total TTC en-US=$835.80 fr-FR=835,80_€ de-DE=835,80_€ de-CH=CHF_835.80 ja-JP=￥836
TVA : | 0_% ou 0,0_% de 450,00_€ = 0,00_€ | 6_% ou 5,5_% de 35,96_€ = 1,98_€ | 20_% ou 20,0_% de 289,88_€ = 57,98_€
arrondis : 2 4 3 | 1 2.67 1,234.568 1.234,50
motif #,##0.00 : [1,234.50] [-7.25] [0.08]
motif 0000.## : [1234.5] [-0007.25] [0000.08]
motif #.# : [1234.5] [-7.2] [0.1]
motif #,##0.00;(#,##0.00) : [1,234.50] [(7.25)] [0.08]
motif '#'000 : [#1234] [-#007] [#000]
motif 0.0% : [123450.0%] [-725.0%] [8.0%]
compact 999 : 999 | 999 | 999 | 999
compact 1234 : 1K | 1.2K | 1 thousand | 1 Tausend
compact 1250000 : 1M | 1.2M | 1 million | 1 Million
compact 7800000000 : 8B | 7.8B | 8 billion | 8 Milliarden
lu en-US nombre "1,234.56" -> 1234.56
lu de-DE nombre "1.234,56" -> 1234.56
lu fr-FR nombre "1 234,56" -> 1
lu en-US nombre "12abc" -> 12
lu en-US nombre "abc" -> ParseException Unparseable number: "abc" (position 0)
lu en-US monnaie "$12.50" -> 12.5
lu en-US monnaie "12.50" -> ParseException Unparseable number: "12.50" (position 0)
partage de 835,80_€ en [2, 3, 4] : 185,73_€ 278,60_€ 371,47_€ (somme 835,80_€)
mois  1 : mensualite     849,67, interets   30,00, capital     819,67, reste   9_180,33
mois  2 : mensualite     849,67, interets   27,54, capital     822,13, reste   8_358,20
mois 12 : mensualite     849,69, interets    2,54, capital     847,15, reste       0,00
cout du credit : 196,06_€
```
