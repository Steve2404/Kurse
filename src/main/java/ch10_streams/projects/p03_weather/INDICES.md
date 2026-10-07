# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Lire et valider les journées

<details><summary>Indice 1</summary>

`Arrays.stream(texte.split(",")).mapToInt(Integer::parseInt).toArray()`.

</details>

<details><summary>Indice 2</summary>

Le record expose `IntStream temps() { return IntStream.of(values); }` : un flux en lecture seule, qui ne donne aucun accès au tableau lui-même.

</details>

---

## Étape 2 — Le code de contrôle de la station

<details><summary>Indice 1</summary>

`String.chars()` rend un `IntStream` des codes des caractères.

</details>

<details><summary>Indice 2</summary>

`code.chars().filter(Character::isLetterOrDigit).sum() % 97`.

</details>

---

## Étape 3 — Le bilan de chaque journée (2 lignes par jour)

<details><summary>Indice 1</summary>

`IntSummaryStatistics s = d.temps().summaryStatistics();`, puis `getMin()`, `getMax()` et `getAverage()`. Le pic : `IntStream.rangeClosed(0, 21).boxed().max(Comparator.comparingInt(h -> somme de h, h+1, h+2))`.

</details>

<details><summary>Indice 2</summary>

- Les relevés : `IntStream.iterate(0, h -> h < 24, h -> h + 6)`.
- Les heures chaudes : `IntStream.range(0, 24).filter(h -> d.at(h) >= 30).boxed().toList()`.

</details>

---

## Étape 4 — Médiane et moyennes de toute la période

<details><summary>Indice 1</summary>

`days.stream().flatMapToInt(Day::temps)`, dans une méthode `all()` qui rend un flux **neuf** à chaque appel.

</details>

<details><summary>Indice 2</summary>

- Médiane paire : `all().sorted().skip(n / 2 - 1).limit(2).average()`.
- Fahrenheit : `all().asDoubleStream().map(c -> c * 9 / 5 + 32)`.

</details>

---

## Étape 5 — L'histogramme

<details><summary>Indice 1</summary>

La classe d'une température t est `t / 5` (division entière). Les classes vont de `min / 5` à `max / 5`.

</details>

<details><summary>Indice 2</summary>

`IntStream.rangeClosed(min / 5, max / 5).mapToObj(b -> …)`, où chaque `b` compte `all().filter(t -> t / 5 == b).count()`.

</details>

---

## Étape 6 — Les trois détections : l'algorithme

<details><summary>Indice 1</summary>

Pour CANICULE et MONTÉE, une boucle avec deux variables : le début de la série en cours, et la meilleure série. On ferme une série dès que la condition échoue, et aussi **à la fin** (d'où `i <= size` ou `h == 24`).

</details>

<details><summary>Indice 2</summary>

ORAGE : `days.stream().flatMap(d -> IntStream.range(1, 24).filter(h -> d.at(h - 1) - d.at(h) > 5).mapToObj(h -> "ORAGE : …"))`.

</details>

---

## Étape 7 — L'énergie

<details><summary>Indice 1</summary>

`all().filter(t -> t > 24).mapToLong(t -> (t - 24) * WH)`. `WH` est un `long`, donc le produit aussi.

</details>

<details><summary>Indice 2</summary>

`days.stream().mapToDouble(d -> Math.max(0, d.temps().average().orElse(0) - 24)).sum()`.

</details>

---

## Étape 8 — `main`

<details><summary>Indice 1</summary>

Une méthode `report()` imprime tout, dans l'ordre de la sortie attendue.

</details>

<details><summary>Indice 2</summary>

`new WeatherStation(Data.DAYS).report();`.

</details>
