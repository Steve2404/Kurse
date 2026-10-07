# Drill de rappel 6 — `NumberFormat`

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall06`** dans le paquet `ch11_exceptions.drills.r06_numberformat`. Le `main` déclare `throws ParseException`.
- Ajoute `static String v(String)`, qui remplace U+00A0 et U+202F par `_`. Passe-lui les lignes D01 à D03.
- Utilise `double x = 1234567.891`.

**Les notions de ce drill ont été apprises dans :** projet 5 (étapes 1 et 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r06_numberformat` → **New** → **Java Class** → `Recall06`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall06`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall06`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Les espaces insécables** : écris-les `'\u00A0'` et `'\u202F'` (projet 5, étape 1).
8. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
9. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
