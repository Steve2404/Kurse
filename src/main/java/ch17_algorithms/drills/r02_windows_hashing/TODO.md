# Drill de rappel 2 — Deux pointeurs, fenêtres, hachage

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md). À faire après les projets p03 et p04.

**Chrono cible :** 20 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée **`public final class Recall02`** dans le paquet `ch17_algorithms.drills.r02_windows_hashing`, avec les méthodes **`public static`** ci-dessous, **exactement** avec ces signatures.
- Tu n'écris pas de tests : les tests de référence (dans `solution/`) vérifient ton code.

**Les notions de ce drill ont été apprises dans :** projet 3 (D01 à D04, D07) et projet 4 (D05, D06). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : crée `Recall02`, écris les méthodes dans l'ordre (une valeur bidon pour un défi pas fini, pour que tout compile), `// D03 : ✗` après 3 minutes bloqué, lance `Check.java`, puis la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `int[] pairWithSum(int[] sorted, int target)` : deux pointeurs aux deux bouts d'un tableau trié ; les indices `{i, j}` ou un tableau vide ; la somme en `long`.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** `long maxSumOfK(int[] a, int k)` : la plus grande somme de `k` cases consécutives (`1 <= k <= a.length`), en O(n).
  → `d02 : 4 executions, 4 reussies`
- ☐ **D03.** `int longestUniqueRun(String s)` : le plus long morceau sans caractère répété (la gauche ne recule jamais).
  → `d03 : 4 executions, 4 reussies`
- ☐ **D04.** `int shortestAtLeast(int[] positive, int target)` : la plus courte fenêtre de somme `>= target`, ou 0.
  → `d04 : 3 executions, 3 reussies`
- ☐ **D05.** `int[] twoSum(int[] a, int target)` : tableau non trié ; la première paire complétée `{i, j}` (premier indice gardé pour un doublon), ou un tableau vide ; O(n).
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `int longestConsecutive(int[] a)` : la plus longue suite d'entiers consécutifs présents, en O(n).
  → `d06 : 1 executions, 1 reussies`
- ☐ **D07.** `int[][] mergeIntervals(int[][] intervals)` : chaque intervalle est `{debut, fin}` ; fusionne ceux qui se chevauchent ou se touchent, triés par début, **sans modifier** le tableau reçu.
  → `d07 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 4 executions, 4 reussies
d03 : 4 executions, 4 reussies
d04 : 3 executions, 3 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
d07 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **Deux pointeurs aux bouts** (tableau trié) : trop petit → `i++` ; trop grand → `j--` ; `while (i < j)`.
- **Fenêtre fixe** : somme des `k` premiers, puis `window += a[i] - a[i - k]`.
- **Fenêtre variable** : la droite avance à chaque tour ; la gauche avance **tant que** la fenêtre est invalide (`while`, pas `if`) ; `left = Math.max(left, precedent + 1)` pour ne jamais reculer.
- **« L'ai-je déjà vu ? »** : `Integer i = seen.get(target - a[j]);` puis `seen.putIfAbsent(a[j], j)` (après la recherche).
- **Suite consécutive** : un `HashSet` ; ne compter qu'à partir d'un début (`v - 1` absent).
- **Intervalles** : copier, trier par début (`Arrays.sort(copie, Comparator.comparingInt(x -> x[0]))`), puis prolonger le dernier tant que `debut <= fin du dernier`. Pour ne pas modifier l'entrée, range des **copies** (`new int[]{debut, fin}`).

</details>
