# Drill de rappel 7 — L'entretien technique (test final du chapitre)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md). À faire après le capstone p11.

**Chrono cible :** 45 min, puis 25 min.

**C'est un entretien :** sept problèmes classiques des entretiens d'embauche. Cette fois, **personne ne te dit** quel algorithme utiliser : reconnaître la famille (dichotomie, fenêtre, hachage, pile, parcours, programmation dynamique, glouton…) est la moitié du travail. Avant de coder chaque défi, écris en commentaire : la famille, l'idée en une phrase, et la complexité visée.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée **`public final class Recall07`** dans le paquet `ch17_algorithms.drills.r07_interview`, avec les méthodes **`public static`** ci-dessous, **exactement** avec ces signatures.
- Tu n'écris pas de tests : les tests de référence (dans `solution/`) vérifient ton code, **vitesse comprise**.

**Les notions de ce drill ont été apprises dans :** tout le chapitre 17 (projets 1 à 10). Un défi ne ressemble à rien de connu ? Relis la carte « Reconnaître la famille » en bas, **après** avoir tenté 5 minutes.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ. Pour chaque défi, d'abord le commentaire (famille, idée, complexité), puis le code.
2. **Bloqué plus de 5 minutes sur un défi ?** `// D03 : ✗`, une valeur bidon, et défi suivant.
3. **Lance `Check.java`** à la fin.
4. **Ensuite seulement**, la carte mémoire : compare tes familles aux siennes.
5. **Note** la date, ton temps et tes ✗ dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `int maxProfit(int[] prices)` : `prices[i]` est le prix d'une action le jour `i`. On achète un jour et on revend un jour **plus tard** (une seule fois). Le plus grand bénéfice possible (0 si on ne peut que perdre). Un million de jours en moins de 2 secondes.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** `long maxSubarray(int[] a)` : la plus grande somme d'un morceau **non vide** de cases consécutives (le tableau n'est pas vide ; il peut n'avoir que des négatifs).
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `int searchRotated(int[] a, int target)` : un tableau trié de valeurs **distinctes** a été « tourné » (par exemple `{4, 5, 6, 7, 0, 1, 2}`). L'indice de `target`, ou −1, en **O(log n)**.
  → `d03 : 6 executions, 6 reussies`
- ☐ **D04.** `int islands(char[][] grid)` : une carte de `'1'` (terre) et de `'0'` (mer). Le nombre d'îles : des terres reliées horizontalement ou verticalement. Une carte de 1000 × 1000 en moins de 3 secondes (attention à la récursion).
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `boolean wordBreak(String s, List<String> dictionary)` : peut-on découper `s` entièrement en mots du dictionnaire (chaque mot réutilisable) ? Un piège de 501 lettres en moins de 2 secondes.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `long[] productExceptSelf(int[] a)` : pour chaque case, le produit de **toutes les autres**, en O(n) et **sans division** (il peut y avoir des zéros).
  → `d06 : 1 executions, 1 reussies`
- ☐ **D07.** `long networkDelay(int n, int[][] times, int source)` : un réseau de `n` machines ; `times[i] = {de, vers, millisecondes}` (orienté). Un message part de `source` : combien de temps pour qu'il atteigne **toutes** les machines ? −1 si une machine n'est jamais atteinte.
  → `d07 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 1 executions, 1 reussies
d03 : 6 executions, 6 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
d07 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

**Reconnaître la famille : les signaux**

| Signal dans l'énoncé | Famille | Projet |
|---|---|---|
| « trié », « O(log n) », « la plus petite valeur qui marche » | dichotomie (sur le tableau ou sur la réponse) | p01 |
| « morceau consécutif », « sous-chaîne », « fenêtre » | deux pointeurs, fenêtre glissante | p03 |
| « déjà vu ? », « paire », « regrouper », « en O(n) » sans tri | hachage | p04 |
| « parenthèses », « prochain plus grand », « annuler » | pile, pile monotone | p05 |
| « toutes les combinaisons », « placer sans conflit » | retour arrière | p06 |
| « les k plus… », « le plus prioritaire », « médiane » | tas | p08 |
| « relié », « réseau », « grille », « le moins d'étapes », « prérequis » | graphe : BFS, DFS, Dijkstra, tri topologique | p09 |
| « de combien de façons », « le minimum de… », un choix qui dépend des choix plus petits | programmation dynamique | p10 |
| « un seul passage », « le meilleur jusqu'ici » | glouton (à justifier !) | p03, p11 |

**Les sept défis :**
- **D01** : glouton en un passage : retenir le prix **le plus bas vu jusqu'ici**, et le meilleur `prix - plus bas`.
- **D02** : Kadane (une programmation dynamique à une variable) : `finiIci = max(a[i], finiIci + a[i])` ; somme en `long`.
- **D03** : dichotomie : à chaque milieu, **une moitié est triée** (`a[lo] <= a[mid]` : la gauche) ; si la cible est dans ses bornes, on y va, sinon dans l'autre.
- **D04** : un graphe caché : chaque case est un sommet ; un parcours (BFS avec une file, ou DFS avec une pile **explicite** : une récursion de 1 000 000 de niveaux déborde) par île non encore vue.
- **D05** : programmation dynamique : `ok[i]` = « le début `s[0, i[` se découpe » ; `ok[i]` vrai s'il existe `j < i` avec `ok[j]` et `s[j, i[` dans le dictionnaire (un `HashSet`). La récursion naïve est exponentielle sur `aaaa…ab`.
- **D06** : préfixes et suffixes : un passage de gauche à droite range le produit de tout ce qui est à gauche, un passage de droite à gauche le multiplie par le produit de tout ce qui est à droite.
- **D07** : Dijkstra depuis la source, puis le **plus grand** des plus courts temps ; un sommet à `Long.MAX_VALUE` → −1.

</details>
