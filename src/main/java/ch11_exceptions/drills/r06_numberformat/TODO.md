# Drill de rappel 6 — `NumberFormat`

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall06`** dans le paquet `ch11_exceptions.drills.r06_numberformat`. Le `main` déclare `throws ParseException`.
- Ajoute `static String v(String)`, qui remplace U+00A0 et U+202F par `_`. Passe-lui les lignes D01 à D03.
- Utilise `double x = 1234567.891`.

## Défis

- ☐ **D01.** `x` avec `getInstance(Locale.US)`, `getNumberInstance(Locale.GERMANY)` et `getInstance(Locale.FRANCE)`.
  → `D01 : 1,234,567.891 | 1.234.567,891 | 1_234_567,891`
- ☐ **D02.** `getCurrencyInstance` : `x` en `Locale.US`, `GERMANY` et `JAPAN`, puis `-3.5` en `Locale.UK`.
  → `D02 : $1,234,567.89 | 1.234.567,89_€ | ￥1,234,568 | -£3.50`
- ☐ **D03.** `getPercentInstance(Locale.US)` sur 0.125 ; `getPercentInstance(Locale.FRANCE)` sur 0.5 ; puis `getIntegerInstance(Locale.US)` sur 2.5 et sur 3.5.
  → `D03 : 12% | 50_% | 2 4`
- ☐ **D04.** `custom = getNumberInstance(Locale.US)`, avec :
  - `setMinimumFractionDigits(2)` et `setMaximumFractionDigits(2)` ;
  - `setGroupingUsed(false)`.
  
  Applique-le à `x`, puis à `7`. Ensuite, `getIntegerInstance(Locale.US)` avec `setRoundingMode(RoundingMode.HALF_UP)`, sur 2.5.
  → `D04 : 1234567.89 | 7.00 | 3`
- ☐ **D05.** `getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT)` sur 1 500, 2 600 000, puis le style `LONG` sur 2 600 000, puis `SHORT` sur 999 999.
  → `D05 : 2K 3M 3 million 1000K`
- ☐ **D06.** `reader = getInstance(Locale.US)` :
  - `parse("3.75kg")` et `parse("42")` : affiche chaque valeur et le nom simple de sa classe ;
  - `parse("kg3")` dans un `try` : `ParseException <getErrorOffset()>`.
  → `D06 : 3.75 Double | 42 Long | ParseException 0`

## Expériences (hors sortie attendue)

1. Pourquoi 0.125 donne-t-il `12%`, et 1 500 donne-t-il `2K` ?
2. `999 999` donne `1000K` sur ce JDK 17 : quel est le piège ?
3. `NumberFormat.getCurrencyInstance(Locale.ENGLISH)` (une langue sans pays) : quelle monnaie ?
4. `new NumberFormat()` : pourquoi est-ce impossible ?

## Sortie attendue complète

```
D01 : 1,234,567.891 | 1.234.567,891 | 1_234_567,891
D02 : $1,234,567.89 | 1.234.567,89_€ | ￥1,234,568 | -£3.50
D03 : 12% | 50_% | 2 4
D04 : 1234567.89 | 7.00 | 3
D05 : 2K 3M 3 million 1000K
D06 : 3.75 Double | 42 Long | ParseException 0
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Fabrique (toutes avec ou sans `Locale`) | Pour |
|---|---|
| `getInstance()` / `getNumberInstance()` | un nombre (3 décimales maximum) |
| `getCurrencyInstance()` | la monnaie du **pays** de la locale (sans conversion !) |
| `getPercentInstance()` | ×100, avec `%` |
| `getIntegerInstance()` | arrondi à l'entier |
| `getCompactNumberInstance(locale, Style.SHORT ou LONG)` | 2K, 3 million… |

- **L'arrondi par défaut** est **`HALF_EVEN`** (au pair) : 2.5 → 2, 3.5 → 4. On le change avec `setRoundingMode`.
- **`parse(String)`** :
  - il lit le plus long **début** valide, et ignore la suite ;
  - il rend un `Long` si le nombre est entier, sinon un `Double` ;
  - il lève `ParseException` (**vérifiée**) si rien n'est lisible.
- `NumberFormat` est **abstraite** : on passe toujours par une fabrique.

</details>
