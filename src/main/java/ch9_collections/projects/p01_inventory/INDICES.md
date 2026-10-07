# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Charger, et le piège `remove`

<details><summary>Indice 1</summary>

`List` a **deux** `remove` : `remove(int index)` et `remove(Object o)`. Avec un `int` littéral, `javac` choisit la version **sans** conversion, donc `remove(int)`.

</details>

<details><summary>Indice 2</summary>

`Collections.frequency(liste, valeur)` compte les égaux. `indexOf` et `lastIndexOf` rendent −1 si l'élément est absent.

</details>

---

## Étape 2 — Modifier en masse, trier

<details><summary>Indice 1</summary>

`removeIf(prédicat)` retire tout ce qui vérifie le prédicat. `replaceAll(opérateur)` remplace chaque élément par le résultat de l'opérateur. Comme les records sont immuables, on rend une **copie** avec `withPrice`.

</details>

<details><summary>Indice 2</summary>

`Comparator.comparing(Item::category)` trie par catégorie. `.thenComparing(Item::price, Comparator.reverseOrder())` départage par prix, du plus grand au plus petit.

</details>

---

## Étape 3 — Parcourir en modifiant

<details><summary>Indice 1</summary>

`ListIterator<Item> it = items.listIterator(); while (it.hasNext()) { Item i = it.next(); if (…) it.set(i.withStock(…)); }`. `set` remplace le **dernier** élément rendu par `next()`.

</details>

<details><summary>Indice 2</summary>

ABC : compare `cumulated * 100 <= total * 70` en entiers, pour éviter les `double`. Les deux seuils s'écrivent avec un ternaire imbriqué.

</details>

---

## Étape 4 — `LinkedList`, `Collections`, listes figées

<details><summary>Indice 1</summary>

`LinkedList` a des méthodes de **deque** : `addFirst`, `addLast` et `removeFirst`. `Collections.rotate(liste, 1)` décale tout d'un cran vers la droite, et le dernier passe en tête.

</details>

<details><summary>Indice 2</summary>

`Collections.shuffle(liste, new Random(seed))` : avec une graine fixe, le mélange est toujours le même. `Collections.binarySearch` suit la même convention que `Arrays.binarySearch` (chapitre 4).

</details>
