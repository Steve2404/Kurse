# Drill de rappel 3 — Les tests paramétrés

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p03.

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire, **imports compris** (`org.junit.jupiter.params.ParameterizedTest`, `org.junit.jupiter.params.provider.*`).
- Crée la classe de test **`Recall03Test`** dans le paquet `ch16_testing.drills.r03_parameterized`.
- Un défi = une méthode `@ParameterizedTest` nommée `d01`, `d02`… Tous les cas doivent **passer**.
- **Le vrai défi :** prévoir le **nombre d'exécutions** de chaque source. Le rapport de `Check` le compte.

**Les notions de ce drill ont été apprises dans :** projet 3 (étapes 2 à 5) ; `@MethodSource` aussi au projet 8. `useHeadersInDisplayName`, `nullValues` et `@EnumSource` sont **nouveaux** : la carte mémoire les explique.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ.
2. **Crée la classe** : clic droit sur le dossier `r03_parameterized` → **New** → **Java Class** → `Recall03Test`.
3. **Écris une méthode par défi.** Lance la classe : dans la fenêtre Run, déplie chaque test pour voir ses cas.
4. **Bloqué plus de 3 minutes ?** `// D04 : ✗`, et défi suivant.
5. **Lance `Check.java`** : une ligne par méthode, avec le nombre de cas exécutés et réussis.
6. **Ensuite seulement**, la carte mémoire, puis les expériences.
7. **Note** date, temps et ✗ dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `@ValueSource(ints = …)` avec 2, 4, 6, 8 : chaque nombre est pair (`n % 2` vaut 0).
  → `d01 : 4 executions, 4 reussies`
- ☐ **D02.** `@ValueSource(strings = …)` avec `radar`, `kayak`, `elle` : chaque mot est égal à son inverse (`StringBuilder.reverse`).
  → `d02 : 3 executions, 3 reussies`
- ☐ **D03.** `@CsvSource({…})` (forme courte) : `1, 1` ; `2, 4` ; `3, 9`. Le carré du 1er nombre vaut le 2e.
  → `d03 : 3 executions, 3 reussies`
- ☐ **D04.** `@CsvSource` en `textBlock`, avec `useHeadersInDisplayName = true` et `name = "{arguments}"`. La 1re ligne est l'en-tête `mot, longueur` ; puis `java, 4` ; `junit, 5` ; `mockito, 7`. La longueur du mot vaut le nombre.
  → `d04 : 3 executions, 3 reussies`
- ☐ **D05.** `@CsvSource` : le texte `a,b,c` (avec ses virgules, **protégé** par des apostrophes) et 3 ; puis `x` et 1. `split(",")` du texte donne ce nombre de morceaux.
  → `d05 : 2 executions, 2 reussies`
- ☐ **D06.** `@CsvSource` avec `nullValues = "N/A"` : `N/A, true` ; `abc, false`. Le booléen dit si la chaîne reçue est `null`.
  → `d06 : 2 executions, 2 reussies`
- ☐ **D07.** `@NullAndEmptySource` **et** `@ValueSource(strings = {" ", "\t"})` sur la même méthode : chaque chaîne est `null` ou blanche (`isBlank`).
  → `d07 : 4 executions, 4 reussies`
- ☐ **D08.** `@EnumSource(DayOfWeek.class)` : le numéro de chaque jour (`getValue()`) est entre 1 et 7.
  → `d08 : 7 executions, 7 reussies`
- ☐ **D09.** `@EnumSource` limité à `SATURDAY` et `SUNDAY` (`names = {…}`) : leur numéro est au moins 6.
  → `d09 : 2 executions, 2 reussies`
- ☐ **D10.** La même chose avec `mode = EnumSource.Mode.EXCLUDE` : tous les **autres** jours ont un numéro d'au plus 5.
  → `d10 : 5 executions, 5 reussies`
- ☐ **D11.** `@MethodSource("sums")` et `name = "{index} : {0} + {1} = {2}"` ; la méthode `static Stream<Arguments> sums()` rend trois cas : `1 + 1 = 2`, `2 + 3 = 5`, `-4 + 4 = 0`.
  → `d11 : 3 executions, 3 reussies`
- ☐ **D12.** `@MethodSource("smallNumbers")` avec **un seul** paramètre `int` ; la méthode `static IntStream smallNumbers()` rend `IntStream.range(0, 5)`. Chaque nombre est entre 0 et 4.
  → `d12 : 5 executions, 5 reussies`

## Expériences (après le drill)

1. Dans D04, retire `useHeadersInDisplayName = true`. Que devient la ligne de `d04` dans le rapport, et pourquoi ?
2. Dans D05, retire les apostrophes autour de `a,b,c`. Que se passe-t-il ?

## Sortie attendue complète

```
d01 : 4 executions, 4 reussies
d02 : 3 executions, 3 reussies
d03 : 3 executions, 3 reussies
d04 : 3 executions, 3 reussies
d05 : 2 executions, 2 reussies
d06 : 2 executions, 2 reussies
d07 : 4 executions, 4 reussies
d08 : 7 executions, 7 reussies
d09 : 2 executions, 2 reussies
d10 : 5 executions, 5 reussies
d11 : 3 executions, 3 reussies
d12 : 5 executions, 5 reussies
```

<details><summary>Ouvrir la carte</summary>

| Source | Cas produits | Remarque |
|---|---|---|
| `@ValueSource(ints = {…})` | un par valeur | aussi `longs`, `doubles`, `strings`, `chars`, `booleans`, `classes`… |
| `@CsvSource({"a, 1", "b, 2"})` | un par chaîne | colonnes séparées par `,` ; `'…'` protège une virgule ; `''` = chaîne vide |
| `@CsvSource(textBlock = """…""")` | un par ligne | `#` en début de ligne = commentaire |
| `useHeadersInDisplayName = true` | la 1re ligne **n'est pas** un cas | elle donne les noms des colonnes |
| `nullValues = "N/A"` | — | ce texte devient `null` ; une colonne **vide** donne déjà `null` |
| `@NullSource` / `@EmptySource` / `@NullAndEmptySource` | 1 / 1 / 2 | pas pour un primitif ; se combinent avec une autre source |
| `@EnumSource(X.class)` | une par constante | `names = {…}` pour choisir ; `mode = EXCLUDE` pour retirer |
| `@MethodSource("nom")` | un par élément | méthode `static` ; `Stream<Arguments>` pour plusieurs colonnes, ou `Stream<T>`, `IntStream`, une `List`… pour une seule |

- `@ParameterizedTest` **remplace** `@Test`.
- `name = "…"` : `{0}`, `{1}`… les colonnes ; `{index}` le numéro du cas ; `{arguments}` toutes les valeurs.
- JUnit convertit le texte vers le type du paramètre (`"4"` → `int`, `"MONDAY"` → `DayOfWeek`…).

**Les expériences** (vérifiées avec JUnit 5.11.4) :
1. Sans `useHeadersInDisplayName`, l'en-tête devient un **cas** de plus : `d04 : 4 executions, 3 reussies`. Le cas `mot, longueur` échoue, car `"longueur"` ne se convertit pas en `int`.
2. Sans les apostrophes, la ligne `a,b,c, 3` a **quatre** colonnes : le 1er paramètre reçoit `a`, le 2e reçoit `b`, et la conversion échoue : `Error converting parameter at index 1: Failed to convert String "b" to type int`. Le rapport donne `d05 : 2 executions, 1 reussies` (la colonne en trop est ignorée, en JUnit 5.11).

</details>
