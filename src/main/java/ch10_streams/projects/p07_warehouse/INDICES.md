# Projet 7 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Le modèle

<details><summary>Indice 1</summary>

Des records : `Product`, `Customer`, `Line`, `Order`, `Allocation(product, served, missing)` et `Result(order, customer, allocations, unknownSkus)`. Deux `enum` : `Tier { GOLD, STANDARD }` et `Status { COMPLETE, PARTIELLE, REFUSEE }`.

</details>

<details><summary>Indice 2</summary>

`split(";", -1)` : une limite **négative** garde les chaînes vides de la fin.

</details>

---

## Étape 2 — Refus et file de priorité

<details><summary>Indice 1</summary>

Les refus : `orders.stream().filter(o -> customer(o.customerId()).isEmpty())`.

</details>

<details><summary>Indice 2</summary>

La file : `flatMap(o -> customer(o.customerId()).map(c -> new Result(o, c, List.of(), List.of())).stream())`, puis `sorted(PRIORITY)`. `PRIORITY` compare d'abord `tier()` : l'ordre de l'enum met GOLD en premier.

</details>

---

## Étape 3 — L'allocation : l'algorithme

<details><summary>Indice 1</summary>

Une boucle `for` sur la file, puis une boucle sur les lignes de chaque commande. `served = Math.min(demandé, stock.get(sku))`, puis `stock.merge(sku, -served, Integer::sum)`.

</details>

<details><summary>Indice 2</summary>

Le statut : `!anyServed ? REFUSEE : anyMissing ? PARTIELLE : COMPLETE`, où `anyMissing` inclut les articles inconnus.

</details>

---

## Étape 4 — Avis et bons de livraison

<details><summary>Indice 1</summary>

`email().map(e -> "AVIS " + e).orElseGet(() -> "AVIS par courrier a " + nom)`.

</details>

<details><summary>Indice 2</summary>

`IntStream.rangeClosed(1, shipped.size()).mapToObj(i -> String.format("BL-%03d=%s", i, shipped.get(i - 1)…))`.

</details>

---

## Étape 5 — Les regroupements

<details><summary>Indice 1</summary>

La fabrique d'`EnumMap` : `() -> new EnumMap<>(Status.class)`. `EnumMap::new` ne compile pas, car il faut passer la classe de l'enum.

</details>

<details><summary>Indice 2</summary>

`collectingAndThen(summingLong(Result::cents), Warehouse::money)` formate le total de chaque région.

</details>

---

## Étape 6 — Statistiques

<details><summary>Indice 1</summary>

`shipped.stream().mapToLong(Result::cents).summaryStatistics()` donne un `LongSummaryStatistics`.

</details>

<details><summary>Indice 2</summary>

`teeing(averagingLong(Result::cents), maxBy(comparingLong(Result::cents)), (avg, max) -> …)`, rangé dans une variable `String`.

</details>

---

## Étape 7 — Fin de journée et contrôle comptable

<details><summary>Indice 1</summary>

Une `TreeMap` pour le stock : ses clés sont triées par sku.

</details>

<details><summary>Indice 2</summary>

`shipped.stream().map(Result::cents).reduce(0L, Long::sum)`. Attention au `0L` : l'identité doit être un `Long`.

</details>

---

## Étape 8 — `main`

<details><summary>Indice 1</summary>

Une méthode `run()` fait tout, dans l'ordre de la sortie attendue.

</details>

<details><summary>Indice 2</summary>

`new Warehouse().run();`.

</details>
