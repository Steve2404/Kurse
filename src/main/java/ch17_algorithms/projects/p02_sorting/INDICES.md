# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le tri par insertion

<details><summary>Indice 1</summary>

La boucle extérieure commence à **1** : un tableau d'une seule carte est déjà trié. La condition du `while` vérifie `j >= 0` **avant** de lire `a[j]`.

</details>

<details><summary>Indice 2</summary>

Pour le test paramétré : une méthode `static Stream<int[]> tricky()` qui rend les tableaux pièges, et, dans le test, `tricky().forEach(original -> { … })` qui clone, trie la copie avec `Arrays.sort`, trie l'autre copie avec le tri testé, et compare avec `assertArrayEquals`.

</details>

---

## Étape 2 — Le tri fusion

<details><summary>Indice 1</summary>

La récursion s'arrête quand le morceau a moins de 2 cases (`hi - lo < 2`). Sinon : `mid = lo + (hi - lo) / 2`, puis les deux appels récursifs sur `[lo, mid[` et `[mid, hi[`.

</details>

<details><summary>Indice 2</summary>

La fusion : trois indices `i` (moitié gauche), `j` (moitié droite), `k` (dans `tmp`). Tant que les deux moitiés ont des éléments, copie le plus petit. Puis copie **le reste** de la moitié gauche, puis **le reste** de la droite (une des deux est vide).

</details>

---

## Étape 3 — Le tri rapide

<details><summary>Indice 1</summary>

Une méthode privée `quickSort(int[] a, int lo, int hi)` avec `hi` **inclus** cette fois. Elle s'arrête si `lo >= hi`. Le pivot est une **valeur** (`int pivot = a[indice au hasard]`), pas un indice.

</details>

<details><summary>Indice 2</summary>

`lt = lo`, `i = lo`, `gt = hi`, et `while (i <= gt)`. Une petite méthode `swap(int[] a, int i, int j)` rend le code lisible.

</details>

---

## Étape 4 — Le tri par comptage

<details><summary>Indice 1</summary>

Un tableau `counts` de taille `max + 1` (les valeurs vont de 0 à `max` **inclus**). Une première boucle vérifie et compte ; une seconde réécrit `a`.

</details>

<details><summary>Indice 2</summary>

Pour ne pas modifier `a` en cas d'erreur, vérifie toutes les valeurs **pendant le comptage**, avant d'écrire quoi que ce soit dans `a`.

</details>

---

## Étape 5 — La stabilité

<details><summary>Indice 1</summary>

`list.subList(0, mid)` et `list.subList(mid, list.size())` (chapitre 9) donnent les deux moitiés sans copier. Les appels récursifs rendent des listes **nouvelles** : tu peux les lire librement.

</details>

<details><summary>Indice 2</summary>

Une liste de 0 ou 1 élément est déjà triée, mais rends quand même une **nouvelle** liste : `new ArrayList<>(list)`. Pour la fin de la fusion : `result.addAll(left.subList(i, left.size()))`.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quel tableau piège, quel cas d'égalité, quelle valeur limite (0, `max`) n'est pas testé ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Le tri par insertion commence à 2 : le 2e élément n'est jamais rangé.
2. Le tri fusion oublie de recopier le reste de la moitié gauche.
3. Le tri fusion trie la moitié droite à partir de `mid + 1` (il saute un élément).
4. Le tri rapide envoie les éléments égaux au pivot vers la droite (partition en deux) : récursion sans fin sur les doublons.
5. Le tri rapide oublie l'élément juste après le tas des égaux.
6. Le tri par comptage oublie la valeur `max`.
7. Une valeur négative n'est plus refusée (le tri plante au lieu de refuser).
8. Le tri générique n'est plus stable (`<` au lieu de `<=`).
9. Le tri générique oublie le reste de la moitié droite.
10. Le tri générique perd un élément de la moitié gauche.

</details>
