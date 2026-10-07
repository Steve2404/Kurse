# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Tracer l'ordre de chargement

<details><summary>Indice 1</summary>

Un bloc static s'écrit `static { … }`, au niveau de la classe. Ordre dans `Network` : `NAMES = load();`, `SIZE;` sans valeur, bloc 1 (`SIZE = NAMES.length;`), `INF` et `DIST`, bloc 2 (Floyd-Warshall).

</details>

<details><summary>Indice 2</summary>

Floyd-Warshall, ce sont trois boucles imbriquées, **k à l'extérieur** : `if (DIST[i][k] + DIST[k][j] < DIST[i][j])`, alors mise à jour et `routes++`. `split("[- ]")` coupe sur un tiret **ou** une espace : `"Gare-Centre 2"` donne `[Gare, Centre, 2]`.

</details>

---

## Étape 2 — Des objets avec un registre partagé

<details><summary>Indice 1</summary>

Le bloc d'instance s'écrit `{ … }` au niveau de la classe, sans `static`. Il contient `id = created++;` et la trace. `create` fait `new Station()` (le bloc s'exécute là), remplit les champs, puis `ALL[s.id] = s;`.

</details>

<details><summary>Indice 2</summary>

`snapshot` parcourt `ALL` de 0 à `created - 1` et assemble `nom=velos/capacite`, séparés par des espaces.

</details>

---

## Étape 3 — Les trajets

<details><summary>Indice 1</summary>

`nearest` : parcourt les stations, ignore `from`, garde celles qui conviennent (`bikes > 0` ou `bikes < capacity`), et retient la plus proche avec un `<` strict. À égalité, la première reste.

</details>

<details><summary>Indice 2</summary>

`ride` : 1) si `from` est vide, `start = nearest(from, true)`, et la marche compte `km(from, start)`. 2) `start.bikes--`. 3) Si `to` est plein, `end = nearest(to, false)`, et la marche compte `km(end, to)`. 4) `end.bikes++`. 5) Le vélo compte `km(start, end)`.

</details>

---

## Étape 4 — Le camion et le `static` via `null`

<details><summary>Indice 1</summary>

Un `while (true)` contient une double boucle (a, b) qui cherche la paire (surplus en a, manque en b) de plus petite distance. S'il n'y en a pas, `break`.

</details>

<details><summary>Indice 2</summary>

Quantité déplacée : `Math.min(sa.bikes - sa.capacity / 2, sb.capacity / 2 - sb.bikes)`. Pour la dernière ligne : `Station nothing = null; nothing.count()` et `Station.get(4).describe()`.

</details>
