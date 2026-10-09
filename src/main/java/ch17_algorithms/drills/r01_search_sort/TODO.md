# Drill de rappel 1 — Dichotomie et tris, de mémoire

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md). À faire après les projets p01 et p02.

**Chrono cible :** 20 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`public final class Recall01`** dans le paquet `ch17_algorithms.drills.r01_search_sort`, avec les méthodes **`public static`** ci-dessous, **exactement** avec ces signatures.
- Ici, **tu n'écris pas de tests** : ce sont les tests de référence (dans `solution/`) qui vérifient ton code. Un défi = une méthode.
- Aucun tri ni aucune recherche tout faits (`Arrays.sort`, `Arrays.binarySearch`, `Collections.sort`, `.sort(`).

**Les notions de ce drill ont été apprises dans :** projet 1 (D01 à D03, D07) et projet 2 (D04 à D06). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ.
2. **Crée la classe** : clic droit sur le dossier `r01_search_sort` → **New** → **Java Class** → `Recall01`.
3. **Écris les méthodes** dans l'ordre des défis. Un défi pas fini ? Laisse une méthode qui rend une valeur bidon (`return 0;`), pour que tout compile.
4. **Bloqué plus de 3 minutes sur un défi ?** Écris `// D03 : ✗` et passe au suivant.
5. **Lance `Check.java`.** Il lance les tests de référence sur ton code et affiche une ligne par défi : `d01 : 6 executions, 6 reussies`. En dessous, les premiers échecs, avec ce qui était attendu.
6. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas, relis tes ✗.
7. **Note** la date, ton temps et tes ✗ dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `int binary(int[] a, int key)` : la dichotomie, avec le contrat d'`Arrays.binarySearch` (l'indice, ou `-(point d'insertion) - 1`).
  → `d01 : 6 executions, 6 reussies`
- ☐ **D02.** `int lowerBound(int[] a, int key)` : le premier indice dont la valeur est `>= key` (`a.length` s'il n'y en a pas).
  → `d02 : 4 executions, 4 reussies`
- ☐ **D03.** `int upperBound(int[] a, int key)` : le premier indice dont la valeur est `> key`.
  → `d03 : 4 executions, 4 reussies`
- ☐ **D04.** `void insertionSort(int[] a)` : le tri par insertion, sur place.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `void mergeSort(int[] a)` : le tri fusion, avec un seul tableau auxiliaire (500 000 nombres en moins de 3 secondes).
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `void quickSort(int[] a)` : le tri rapide, pivot au hasard, partition en trois (500 000 nombres qui ne valent que 0, 1 ou 2, en moins de 3 secondes).
  → `d06 : 1 executions, 1 reussies`
- ☐ **D07.** `int minCapacity(int[] weights, int days)` : la plus petite capacité d'un camion pour livrer les colis, dans l'ordre, en `days` jours (la dichotomie sur la réponse).
  → `d07 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 6 executions, 6 reussies
d02 : 4 executions, 4 reussies
d03 : 4 executions, 4 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
d07 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

**Les trois dichotomies :**

| | intervalle | boucle | trop petit | sinon | rend |
|---|---|---|---|---|---|
| `binary` | `[0, n-1]` | `lo <= hi` | `lo = mid + 1` | `>` : `hi = mid - 1` ; égal : `return mid` | `-(lo + 1)` |
| `lowerBound` | `[0, n[` | `lo < hi` | `a[mid] < key` : `lo = mid + 1` | `hi = mid` | `lo` |
| `upperBound` | `[0, n[` | `lo < hi` | `a[mid] <= key` : `lo = mid + 1` | `hi = mid` | `lo` |

Toujours `mid = lo + (hi - lo) / 2` (pas de débordement).

**La dichotomie sur la réponse :** l'intervalle des réponses possibles (`[plus gros colis, somme]`), une vérification gloutonne, et la forme `lowerBound` : `ok(mid)` → `hi = mid`, sinon `lo = mid + 1`.

**Les tris :**
- **insertion** : `for i de 1` ; `key = a[i]` ; tant que `a[j] > key`, décaler à droite ; `a[j + 1] = key`. O(n²), O(n) si presque trié.
- **fusion** : trier `[lo, mid[` et `[mid, hi[`, fusionner dans `tmp` (`<=` : stable), copier le reste des deux moitiés, `System.arraycopy`. O(n log n) toujours.
- **rapide** : pivot au hasard (`ThreadLocalRandom.current().nextInt(lo, hi + 1)`) ; `lt`, `i`, `gt` ; `<` : échange `lt`/`i`, avance les deux ; `>` : échange `i`/`gt`, recule `gt` ; sinon `i++` ; récursion sur `[lo, lt-1]` et `[gt+1, hi]`.

</details>
