# Drill de rappel 6 — Les tables de programmation dynamique

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p10.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée **`public final class Recall06`** dans le paquet `ch17_algorithms.drills.r06_dynamic`, avec les méthodes **`public static`** ci-dessous, **exactement** avec ces signatures.
- Tu n'écris pas de tests : les tests de référence (dans `solution/`) vérifient ton code. Pour D04 et D05, une table `int[][] dp` ; pour D06, un seul tableau parcouru à l'envers.

**Les notions de ce drill ont été apprises dans :** projet 10 (toutes les étapes). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : crée `Recall06`, écris les méthodes dans l'ordre (une valeur bidon pour un défi pas fini), `// D04 : ✗` après 3 minutes bloqué, lance `Check.java`, puis la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `long fibonacci(int n)` : F(0) = 0, F(1) = 1, en O(n) (`n >= 0`).
  → `d01 : 4 executions, 4 reussies`
- ☐ **D02.** `int minCoins(int[] coins, int amount)` : le moins de pièces, ou −1.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `long countWays(int[] coins, int amount)` : le nombre de combinaisons (l'ordre ne compte pas).
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `int lcsLength(String a, String b)` : la longueur de la plus longue sous-suite commune.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `int editDistance(String a, String b)` : la distance de Levenshtein.
  → `d05 : 3 executions, 3 reussies`
- ☐ **D06.** `int knapsack(int[] weights, int[] values, int capacity)` : le sac à dos, chaque objet au plus une fois.
  → `d06 : 1 executions, 1 reussies`
- ☐ **D07.** `int longestIncreasing(int[] a)` : la plus longue sous-suite **strictement** croissante, en O(n log n).
  → `d07 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 4 executions, 4 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 3 executions, 3 reussies
d06 : 1 executions, 1 reussies
d07 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

| Problème | Case | Départ | Relation | Piège |
|---|---|---|---|---|
| Fibonacci | 2 variables | `0, 1` | `next = prev + cur` | rendre `prev` après `n` tours |
| moins de pièces | `best[a]` | `best[0] = 0`, le reste « impossible » | `min(best[a - c] + 1)` | ne pas ajouter 1 à « impossible » |
| nombre de façons | `ways[a]` | `ways[0] = 1` | `ways[a] += ways[a - c]` | boucle des **pièces** à l'extérieur |
| LCS | `dp[i][j]` | ligne et colonne 0 à 0 | égales : `dp[i-1][j-1] + 1` ; sinon `max(haut, gauche)` | lettre `charAt(i - 1)` |
| Levenshtein | `dp[i][j]` | `dp[i][0] = i`, `dp[0][j] = j` | `min(remplacer, supprimer + 1, insérer + 1)` | remplacer coûte 0 si lettres égales |
| sac à dos 0/1 | `best[w]` | tout à 0 | `max(best[w], best[w - p] + v)` | `w` **de la fin vers le début** |
| sous-suite croissante | `tails[k]` | longueur 0 | dichotomie : première fin `>= v` remplacée | strictement : `tails[mid] < v` pour avancer |

</details>
