# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — `Pair` et `Range`

<details><summary>Indice 1</summary>

Une méthode static générique déclare ses **propres** paramètres de type, **avant** le type de retour : `public static <X> Pair<X, X> twin(X value)`.

</details>

<details><summary>Indice 2</summary>

`Range<T extends Comparable<T>>` : la borne permet d'appeler `low.compareTo(high)`. Dans le constructeur compact, on échange avec une variable `T t`.

</details>

---

## Étape 2 — Le tas et le tri génériques

<details><summary>Indice 1</summary>

C'est le même tas qu'au chapitre 8 (projet 3), avec une `List<T>` à la place du tableau : `items.get(i)`, `items.set(i, x)` et `items.remove(items.size() - 1)`. Un tableau `T[]` ne peut pas se créer.

</details>

<details><summary>Indice 2</summary>

`mergeSort` sur des listes : `list.subList(0, mid)` et `list.subList(mid, size)` pour les moitiés, puis une nouvelle `ArrayList` pour la fusion. `addAll(left.subList(i, left.size()))` vide le reste.

</details>

---

## Étape 3 — Bornes et jokers

<details><summary>Indice 1</summary>

**PECS** : *Producer Extends, Consumer Super*. Une collection qu'on **lit** s'écrit `? extends T`, une collection qu'on **remplit** s'écrit `? super T`.

</details>

<details><summary>Indice 2</summary>

`Algos.<Number>copy(all, squares)` fixe T = `Number`. La destination `List<Object>` est bien un `List<? super Number>`, et la source `List<Number>` un `List<? extends Number>`.

</details>

---

## Étape 4 — Cache LRU, interface générique, effacement

<details><summary>Indice 1</summary>

Le 3e argument `true` du constructeur de `LinkedHashMap` passe en ordre d'**accès** : chaque `get` déplace la clé à la fin. `removeEldestEntry` est appelée après chaque `put`, et rendre `true` retire la plus ancienne.

</details>

<details><summary>Indice 2</summary>

`then` : `default <C> Transformer<A, C> then(Transformer<? super B, ? extends C> next) { return a -> next.transform(transform(a)); }`.

</details>
