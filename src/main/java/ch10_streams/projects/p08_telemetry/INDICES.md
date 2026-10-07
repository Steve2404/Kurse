# Projet 8 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Le modèle et les mesures

<details><summary>Indice 1</summary>

`record Sample(server, region, long epoch, double cpu, int memMb, long bytes)`. Une mesure qui rend un `double` sans boxing est une `ToDoubleFunction<Sample>`.

</details>

<details><summary>Indice 2</summary>

`Map.of("CPU", Sample::cpu, "MEMOIRE", Sample::memMb, "OCTETS", Sample::bytes)` : `int` et `long` s'élargissent en `double`, donc les trois références conviennent.

</details>

---

## Étape 2 — Vue d'ensemble

<details><summary>Indice 1</summary>

`IntFunction<String[]> NEW_ARRAY = String[]::new;`, puis `.distinct().toArray(NEW_ARRAY)`.

</details>

<details><summary>Indice 2</summary>

`filter(s -> s.server().equals("db1") && s.cpu() > 90).findAny()`. Compte combien d'échantillons passent ce filtre.

</details>

---

## Étape 3 — Les `long`

<details><summary>Indice 1</summary>

`mapToLong(BYTES).max()` rend un `OptionalLong`. Pour la mémoire : `mapToInt(MEMORY).asLongStream().map(MB_TO_BYTES).sum()`.

</details>

<details><summary>Indice 2</summary>

- Le max par `reduce` : l'identité est `Integer.MIN_VALUE`, car tout nombre est ≥ elle.
- Le calendrier : `IntStream.rangeClosed(0, 5).mapToLong(i -> start + i * 60)` et `LongStream.iterate(start, t -> t <= end, t -> t + 60)`.

</details>

---

## Étape 4 — Les `double` et `Stream.empty()`

<details><summary>Indice 1</summary>

`collectingAndThen(averagingDouble(Sample::cpu), Telemetry::fmt)` arrondit directement dans la `Map`.

</details>

<details><summary>Indice 2</summary>

`region(name)` : si aucun échantillon n'a cette région, `return Stream.empty();`. Puis `mapToDouble(Sample::cpu).average()` donne un `OptionalDouble` vide.

</details>

---

## Étape 5 — Les alertes et la santé

<details><summary>Indice 1</summary>

`partitioningBy(s -> maintenanceOn.getAsBoolean() && s.region().equals("US"), mapping(Sample::server, toList()))`.

</details>

<details><summary>Indice 2</summary>

`penalty.andThen(p -> 100 - p).andThen(h -> Math.max(h, MIN))`. Le nombre d'alertes par serveur se cumule avec `merge(serveur, 1, Integer::sum)`.

</details>

---

## Étape 6 — Profils et relances

<details><summary>Indice 1</summary>

`mapToDouble(Sample::cpu).mapToInt(c -> (int) (c / 10))`, puis `collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)`. L'accumulateur est un `ObjIntConsumer<StringBuilder>`.

</details>

<details><summary>Indice 2</summary>

Une **classe anonyme** `new IntSupplier() { private int next = 1; public int getAsInt() { … } }` : un champ peut être modifié.

</details>

---

## Étape 7 — Les pièges du chapitre

<details><summary>Indice 1</summary>

`Supplier<Stream<Sample>> fresh = samples::stream;`. Chaque `fresh.get()` crée un nouveau flux.

</details>

<details><summary>Indice 2</summary>

Pour les expériences, lis le **nom de l'exception** (la 1re ligne de la trace) et la ligne de ton code qu'elle désigne.

</details>

---

## Étape 8 — `main`

<details><summary>Indice 1</summary>

Une méthode par bloc : `overview`, `longs`, `doubles`, `alerts`, `profiles` et `reuse`.

</details>

<details><summary>Indice 2</summary>

Le `main` les appelle dans l'ordre de la sortie attendue.

</details>
