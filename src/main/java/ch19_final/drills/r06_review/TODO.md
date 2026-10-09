# Drill de rappel 6 — La revue express

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md). À faire après les projets p06 et p07.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch19_final.drills.r06_review`.
- `Data.Before` (fourni) est le code d'un collègue : chaque méthode cache un défaut classique, de justesse **ou** de performance. Lis-la, trouve le défaut, puis **réécris** la méthode corrigée dans tes classes (ne copie pas : ta version ne doit rien contenir de `Data.`).
- Crée les types ci-dessous, **exactement** avec ces noms et ces signatures. Ni `double`, ni `== "`, ni `plusDays(`, ni `.contains(` ou `out = out +` dans `Fixes`, ni `value++` dans `SafeCounter`. Aucune méthode de plus de **12 lignes**.
- Tu n'écris pas de tests : les tests de référence vérifient ton code.

**Les notions de ce drill ont été apprises dans :** projet 6 (étape 5) et projet 7 (étapes 1 à 5).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : les défis dans l'ordre, `// D04 : ✗` après 3 minutes bloqué, `Check.java`, puis la carte mémoire, puis ton temps dans [`drills/README.md`](../README.md). Pour chaque défi, dis-toi d'abord **à voix haute** le défaut de `Data.Before` : c'est la moitié de l'exercice.

</details>

## Défis

- ☐ **D01.** `public final class Fixes` (constructeur privé) : `public static long totalCents(List<String> amounts)` (des montants en euros comme `"19.99"`, additionnés **exactement** en centimes ; trois décimales lancent `ArithmeticException`) et `public static boolean isPromo(String code)` (vrai pour le texte `DOUBLE`, qu'il soit tapé, lu ou `null`).
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** `public static String tier(int points)` (GOLD **à partir de** 1 000, SILVER à partir de 300, sinon BRONZE) et `public static LocalDate lastValidDay(LocalDate earned)` (des points gagnés valent un an : jusqu'à la **veille** du même jour l'année suivante ; gagnés le 15 janvier 2024, ils valent jusqu'au 14 janvier 2025).
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `public static List<String> distinctInOrder(List<String> items)` : les doublons retirés, dans l'ordre de première apparition, et **vite** : 200 000 éléments en moins de 2 secondes.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `public static String join(List<String> parts)` : les morceaux séparés par `", "`, et **vite** : 100 000 morceaux en moins de 2 secondes.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `public static int countLines(Reader in) throws IOException` : le nombre de lignes ; le flux est **toujours** fermé, et une erreur de lecture **remonte** (pas de `-1` qui ressemble à un résultat).
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `public final class SafeCounter` (`void increment()`, `long value()`), exact quand 8 fils l'augmentent ensemble ; et `public record Customer(String id, String email)`, dont le `toString` masque l'adresse : `Customer[C1, a***@example.org]` (sans `@`, ou `@` en premier : `***`).
  → `d06 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

| Défaut de `Data.Before` | Correction |
|---|---|
| `double` pour l'argent | `new BigDecimal(a).movePointRight(2).longValueExact()`, dans un `long` |
| `code == "DOUBLE"` | `"DOUBLE".equals(code)` |
| `>` au lieu de « à partir de » | `>=` |
| `plusDays(364)` | `plusYears(1).minusDays(1)` |
| `result.contains(item)` dans une boucle : O(n²) | `new ArrayList<>(new LinkedHashSet<>(items))` : O(n), ordre gardé |
| `out = out + …` dans une boucle : O(n²) | `String.join(", ", parts)` (ou un `StringBuilder`) |
| flux jamais fermé, `IOException` changée en `-1` | `try (BufferedReader r = new BufferedReader(in)) { … }` et `throws IOException` |
| `value++` sur un `int` partagé | `LongAdder` (ou `AtomicLong`) |
| (le défaut du projet 7) `equals` sans `hashCode`, l'adresse dans `toString` | un `record`, et un `toString` qui masque |

</details>
