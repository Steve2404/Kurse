# Drill de rappel 3 — Piles, tas, arbres

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md). À faire après les projets p05, p07 et p08.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée **`public final class Recall03`** dans le paquet `ch17_algorithms.drills.r03_stacks_heaps_trees`, avec les méthodes **`public static`** ci-dessous, **exactement** avec ces signatures. Pour D06 et D07, écris ta propre petite classe de nœud, **dans** `Recall03` (`private static final class Node`).
- Tu n'écris pas de tests : les tests de référence (dans `solution/`) vérifient ton code.
- Pas de `java.util.Stack`, pas de `Arrays.sort`, pas de `TreeMap` ni de `TreeSet`.

**Les notions de ce drill ont été apprises dans :** projet 5 (D01 à D03), projet 8 (D04, D05) et projet 7 (D06, D07). **D04 est nouveau :** c'est le tri par tas **sur place**, sans tableau en plus ; la carte mémoire l'explique. Fais-le une première fois avec la carte ouverte.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : crée `Recall03`, écris les méthodes dans l'ordre (une valeur bidon pour un défi pas fini), `// D04 : ✗` après 3 minutes bloqué, lance `Check.java`, puis la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `boolean balanced(String s)` : les parenthèses `()`, `[]`, `{}` sont bien équilibrées ; les autres caractères sont ignorés.
  → `d01 : 3 executions, 3 reussies`
- ☐ **D02.** `long evalRpn(String expr)` : la notation polonaise inverse (jetons séparés par des espaces, `+ - * /`, division entière) ; les expressions sont toujours valides.
  → `d02 : 4 executions, 4 reussies`
- ☐ **D03.** `int[] daysUntilWarmer(int[] temps)` : pour chaque jour, combien de jours attendre une température **strictement** plus chaude (0 si jamais), avec une pile monotone.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `void heapSort(int[] a)` : le tri par tas **sur place** (un million de nombres en moins de 3 secondes).
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `int[] topK(int[] a, int k)` : les `k` plus grands (ou tous, s'il y en a moins), du plus grand au plus petit, avec un tas de taille `k`.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `List<Integer> preOrder(int[] insertOrder)` : insère les clés, dans cet ordre, dans un arbre binaire de recherche (une clé déjà présente est ignorée), et rends son parcours **préfixe**.
  → `d06 : 1 executions, 1 reussies`
- ☐ **D07.** `List<Integer> levelOrder(int[] insertOrder)` : le même arbre, parcouru **par niveaux**.
  → `d07 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 3 executions, 3 reussies
d02 : 4 executions, 4 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
d07 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **Parenthèses** : une ouvrante → `push` ; une fermante → la pile ne doit pas être vide, et `pop()` doit rendre **son** ouvrante ; à la fin, pile vide.
- **RPN** : un nombre → `push` ; un opérateur → `right = pop()`, **puis** `left = pop()`, `push(left op right)`.
- **Pile monotone** : une pile d'**indices** ; tant que le dessus est plus froid que le jour actuel, il a trouvé sa réponse (`jour - dessus`) ; puis on empile le jour.
- **Tri par tas sur place** (nouveau) :
  1. **construire** un tas **max** dans le tableau : `siftDown` de chaque case, depuis `n / 2 - 1` jusqu'à 0 (les cases suivantes sont des feuilles) ;
  2. **vider** : pour `end` de `n - 1` à 1, échanger la racine (le maximum) avec `a[end]`, puis `siftDown(a, 0, end)` sur le tas raccourci.
  
  `siftDown(a, i, taille)` : le plus **grand** de `i`, `2i + 1`, `2i + 2` (dans la taille) ; si ce n'est pas `i`, échanger et continuer. O(n log n), sans mémoire en plus.
- **Top k** : un tas **min** (`PriorityQueue`) ; après chaque `add`, `poll` s'il dépasse `k` ; vider dans le tableau depuis la fin.
- **Arbre** : insertion par une boucle (à gauche si plus petit, à droite si plus grand, rien si égal) ; préfixe = nœud, gauche, droite (récursif) ; par niveaux = une **file** (`ArrayDeque`, `add` / `poll`).

</details>
