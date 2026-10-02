# Drill de rappel 7 — Les motifs `DecimalFormat`

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall07`** dans le paquet `ch11_exceptions.drills.r07_decimalformat`. Le `main` déclare `throws ParseException`.
- Prépare `static final DecimalFormatSymbols US = DecimalFormatSymbols.getInstance(Locale.US)`, et `static String f(String pattern, double value)` qui rend `new DecimalFormat(pattern, US).format(value)`.

## Défis

- ☐ **D01.** `f("###.##", 3.5)` ; `f("000.00", 3.5)` ; `f("#.##", 0.456)` ; `f("0.##", 0.456)`.
  → `D01 : 3.5 | 003.50 | 0.46 | 0.46`
- ☐ **D02.** `f("#,###", 1234567)` ; `f("#,##0.0", 1234567.89)` ; `f("#,##", 1234567)`.
  → `D02 : 1,234,567 | 1,234,567.9 | 1,23,45,67`
- ☐ **D03.** `f("0.0", 0.25)`, `f("0.0", 0.35)`, `f("0", 2.5)` et `f("0", -2.5)`, séparés par un espace.
  → `D03 : 0.2 0.3 2 -2`
- ☐ **D04.** `f("#,##0.00;(#,##0.00)", -1234.5)` ; `f("0.0%", 0.0789)` ; `f("'#'0", 7)` ; `f("0 'pts'", 12)`.
  → `D04 : (1,234.50) | 7.9% | #7 | 12 pts`
- ☐ **D05.** `german = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.GERMANY))`. Affiche :
  - `german.format(1234.5)` ;
  - `german.toPattern()` ;
  - `new DecimalFormat("#,##0.00", US).parse("1,234.50xyz")`.
  → `D05 : 1.234,50 | #,##0.00 | 1234.5`
- ☐ **D06.** `money = new DecimalFormat("$#,##0.00", US)`, puis `setMinimumIntegerDigits(3)`. Affiche `format(5.1)`, `parse("$1,000.25")`, puis `parse("$0042")`.
  → `D06 : $005.10 | 1000.25 | 42`

## Expériences (hors sortie attendue)

1. Pourquoi 0.35 donne-t-il `0.3`, et non `0.4` ? (Indice : écris `new java.math.BigDecimal(0.35)`.)
2. Pourquoi `"#,##"` groupe-t-il les chiffres par deux ?
3. `new DecimalFormat("#.#.#")` : que se passe-t-il ?
4. Le motif reste `#,##0.00` en allemand : qui choisit `.` ou `,` à l'affichage ?

## Sortie attendue complète

```
D01 : 3.5 | 003.50 | 0.46 | 0.46
D02 : 1,234,567 | 1,234,567.9 | 1,23,45,67
D03 : 0.2 0.3 2 -2
D04 : (1,234.50) | 7.9% | #7 | 12 pts
D05 : 1.234,50 | #,##0.00 | 1234.5
D06 : $005.10 | 1000.25 | 42
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Symbole | Sens |
|---|---|
| `0` | chiffre **obligatoire** (complété par des zéros) |
| `#` | chiffre **facultatif** (rien s'il est absent) |
| `.` | séparateur décimal (affiché selon les symboles de la locale) |
| `,` | groupement (sa taille = le nombre de chiffres après la dernière `,`) |
| `;` | sépare le motif positif du motif **négatif** |
| `%` | ×100, puis le signe `%` |
| `'texte'` | texte littéral (`''` pour une apostrophe) |

- **L'arrondi** est HALF_EVEN, appliqué sur la vraie valeur binaire du `double`.
- **`DecimalFormatSymbols`** fixe les caractères ; sans eux, c'est la locale par défaut qui décide.
- **`parse`** suit le même motif, et lit le plus long début valide.

</details>
