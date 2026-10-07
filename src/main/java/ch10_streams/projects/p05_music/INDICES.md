# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Le modèle

<details><summary>Indice 1</summary>

Dans une expression régulière, `|` signifie « ou ». Pour couper sur un vrai `|`, il faut l'échapper : `"\\|"` dans le code Java.

</details>

<details><summary>Indice 2</summary>

`record Play(user, artist, title, genre, seconds, List<String> tags)`, avec `valid()` qui rend `seconds >= Data.VALID_SECONDS`.

</details>

---

## Étape 2 — Compter et partitionner

<details><summary>Indice 1</summary>

`groupingBy(clé, TreeMap::new, counting())`, et `partitioningBy(Play::valid, counting())`.

</details>

<details><summary>Indice 2</summary>

`partitioningBy(Play::valid, mapping(Play::user, toCollection(TreeSet::new)))`, puis `get(false)`.

</details>

---

## Étape 3 — Le meilleur artiste de chaque genre

<details><summary>Indice 1</summary>

`collectingAndThen(groupingBy(Play::artist, counting()), counts -> …)` : la fonction finale reçoit la sous-table artiste → nombre.

</details>

<details><summary>Indice 2</summary>

Avec `max`, « premier dans l'alphabet » signifie que l'ordre des noms doit être **inversé** : le plus petit nom doit être « le plus grand » pour `max`. `comparingByValue().thenComparing(comparingByKey().reversed())`.

</details>

---

## Étape 4 — Filtrer dans ou avant le groupement ?

<details><summary>Indice 1</summary>

`filtering(prédicat, collecteurAval)` filtre **à l'intérieur** de chaque groupe. Le groupe existe donc toujours, même si rien n'y passe.

</details>

<details><summary>Indice 2</summary>

`groupingBy(Play::user, TreeMap::new, filtering(Play::valid, summingInt(Play::seconds)))`.

</details>

---

## Étape 5 — Aplatir dans un groupe

<details><summary>Indice 1</summary>

`flatMapping(p -> p.tags().stream(), …)` : comme `flatMap`, mais en aval d'un `groupingBy`.

</details>

<details><summary>Indice 2</summary>

En aval de `flatMapping` : `toCollection(TreeSet::new)`.

</details>

---

## Étape 6 — Extrêmes et réductions par groupe

<details><summary>Indice 1</summary>

`collectingAndThen(maxBy(comparingInt(Play::seconds)), o -> o.map(Play::title).orElseThrow())`.

</details>

<details><summary>Indice 2</summary>

`reducing(0, Play::seconds, Integer::sum)` : identité, transformation de chaque élément, puis opérateur.

</details>

---

## Étape 7 — `toMap` et ses pièges

<details><summary>Indice 1</summary>

`toMap(clé, valeur, fusion, TreeMap::new)` : la fusion `Integer::sum` cumule les écoutes du même titre.

</details>

<details><summary>Indice 2</summary>

`joining(", ", "[", "]")` : le séparateur, puis le préfixe, puis le suffixe.

</details>

---

## Étape 8 — Statistiques et `teeing`

<details><summary>Indice 1</summary>

`summarizingInt(Play::seconds)` donne un `IntSummaryStatistics`. `teeing(minBy(…), maxBy(…), (min, max) -> …)` donne deux résultats, puis la fusion.

</details>

<details><summary>Indice 2</summary>

Range le résultat du `teeing` dans une variable `String`, puis affiche-la. Le type cible aide l'inférence.

</details>

---

## Étape 9 — Les explorateurs

<details><summary>Indice 1</summary>

Le nombre de genres existants : `plays.stream().map(Play::genre).distinct().count()`.

</details>

<details><summary>Indice 2</summary>

`groupingBy(Play::user, TreeMap::new, mapping(Play::genre, toSet()))` sur les écoutes valides, puis `filter(e -> e.getValue().size() == genres)`.

</details>

---

## Étape 10 — Les recommandations : l'algorithme

<details><summary>Indice 1</summary>

Jaccard : une copie de A avec `retainAll(B)` (l'intersection), et une autre avec `addAll(B)` (l'union). Le résultat est `(double) inter.size() / union.size()`.

</details>

<details><summary>Indice 2</summary>

- Le voisin : `max(comparingDouble(similarity).thenComparing(user, reverseOrder()))`.
- Les nouveautés : `filter(a -> !mine.contains(a)).sorted().collect(collectingAndThen(joining(", "), s -> s.isEmpty() ? "rien de nouveau" : s))`.

</details>

---

## Étape 11 — `main`

<details><summary>Indice 1</summary>

Une méthode `report()` qui affiche tout dans l'ordre.

</details>

<details><summary>Indice 2</summary>

`new MusicStats(Data.PLAYS).report();`.

</details>
