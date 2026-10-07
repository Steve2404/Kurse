# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Le modèle

<details><summary>Indice 1</summary>

`Stats(played, won, drawn, lost, goalsFor, goalsAgainst)`, avec `ZERO = new Stats(0, 0, 0, 0, 0, 0)` et `plus(o)` qui additionne champ par champ.

</details>

<details><summary>Indice 2</summary>

`statsFor(team)` : ramène le match à « mes buts / leurs buts », puis rend un `Stats` d'**un** match (1 joué, et 1 dans la bonne case parmi gagné, nul, perdu).

</details>

---

## Étape 2 — `RESULTATS` : `collect` à 3 arguments

<details><summary>Indice 1</summary>

L'accumulateur ajoute `" | "` **si le builder n'est pas vide**, puis le match.

</details>

<details><summary>Indice 2</summary>

Le combiner fait la même chose à l'échelle de deux morceaux : si les deux ne sont pas vides, il ajoute `" | "`, puis `left.append(right)`.

</details>

---

## Étape 3 — `BUTS` : `reduce` à 3 arguments

<details><summary>Indice 1</summary>

`reduce(0, (sum, m) -> sum + m.goals(), Integer::sum)` : l'identité est un `int`, l'accumulateur combine un `int` et un `Match`, et le combiner combine deux `int`.

</details>

<details><summary>Indice 2</summary>

Pour le piège, calcule à la main : 10 + tous les buts, contre (10 + buts de gauche) + (10 + buts de droite).

</details>

---

## Étape 4 — `PLUS LARGE VICTOIRE` : `reduce` sans identité

<details><summary>Indice 1</summary>

`filter(m -> m.margin() > 0).reduce((a, b) -> b.margin() > a.margin() ? b : a)`.

</details>

<details><summary>Indice 2</summary>

Le `>` **strict** garde `a` (le plus ancien) à égalité. Vérifie : avec x, y et z à égalité, `(x op y) op z` et `x op (y op z)` rendent-ils tous deux x ?

</details>

---

## Étape 5 — `CLASSEMENT` : ton propre `Collector`

<details><summary>Indice 1</summary>

`Collector.of(HashMap::new, (table, m) -> { table.merge(home, statsFor(home), Stats::plus); … }, (l, r) -> { r.forEach((t, s) -> l.merge(t, s, Stats::plus)); return l; }, finisher)`.

</details>

<details><summary>Indice 2</summary>

Le comparateur : chaque critère décroissant est **inversé séparément** (`comparingInt(…).reversed()`), puis on chaîne avec `thenComparing(...)`. Le nom vient en dernier, croissant.

</details>

---

## Étape 6 — `BILAN Lions` : `reduce` à 2 arguments

<details><summary>Indice 1</summary>

`matches.stream().filter(m -> m.involves("Lions")).map(m -> m.statsFor("Lions")).reduce(Stats.ZERO, Stats::plus)`.

</details>

<details><summary>Indice 2</summary>

La comparaison : `table.stream().anyMatch(r -> r.stats().equals(lions))`.

</details>

---

## Étape 7 — `SERIES SANS DEFAITE` : l'algorithme fusionnable

<details><summary>Indice 1</summary>

`record Segment(length, prefix, suffix, best)`. Un match seul sans défaite donne `(1, 1, 1, 1)`, et avec défaite `(1, 0, 0, 0)`. L'identité est `(0, 0, 0, 0)`.

</details>

<details><summary>Indice 2</summary>

- Fusion gauche + droite : `prefix` = `gauche.prefix == gauche.length ? gauche.length + droite.prefix : gauche.prefix`, et symétrique pour `suffix`.
- `best` = le max de `gauche.best`, `droite.best` et `gauche.suffix + droite.prefix`.

</details>

---

## Étape 8 — `CONTROLE`

<details><summary>Indice 1</summary>

`table.stream().reduce(0, (sum, r) -> sum + r.stats().points(), Integer::sum)`.

</details>

<details><summary>Indice 2</summary>

Points distribués = victoires × 3 + nuls × 2 (1 point pour **chaque** équipe).

</details>

---

## Étape 9 — `COMBINER` : la preuve, à la main

<details><summary>Indice 1</summary>

`left = matches.subList(0, 5)` et `right = matches.subList(5, size)`. Pour chaque réduction : réduis `left` et `right` séparément, puis applique le combiner nommé.

</details>

<details><summary>Indice 2</summary>

Pour le `Collector` : `A c = collector.supplier().get();`, puis `accumulator().accept(c, m)` pour chaque match du morceau. Fais-le pour les deux morceaux, puis `finisher().apply(combiner().apply(cg, cd))`.

</details>

---

## Étape 10 — `main`

<details><summary>Indice 1</summary>

Une méthode `report()` qui imprime tout dans l'ordre.

</details>

<details><summary>Indice 2</summary>

`new League(Data.MATCHES).report();`.

</details>
