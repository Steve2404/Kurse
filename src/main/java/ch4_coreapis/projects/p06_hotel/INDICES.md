# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Lire les données

<details><summary>Indice 1</summary>

Un tableau par champ : `codes`, `guests`, `rooms` (l'**indice** de la chambre, pas son numéro), `arrivals` et `departures` (des `LocalDate`, avec `LocalDate.parse`). Pour les chambres : `roomNumbers`, `roomTypes`, `roomCents`.

</details>

<details><summary>Indice 2</summary>

Centimes sans `double` : retire le point (`"120.50".replace(".", "")` donne `"12050"`), puis `Integer.parseInt`. Ça marche parce que chaque prix a exactement 2 décimales.

</details>

---

## Étape 2 — Les factures

<details><summary>Indice 1</summary>

Nom normalisé : `strip`, `split(" +")`, puis pour chaque morceau une majuscule initiale et le reste en minuscules, assemblés avec un `StringBuilder`. Prix : une boucle de nuit en nuit, `for (night = arrivée; night.isBefore(départ); night = night.plusDays(1))`.

</details>

<details><summary>Indice 2</summary>

Majoration d'une nuit de week-end : `Math.round(base * (100 + 20) / 100.0)`. Affichage : `cents / 100 + "." + (cents % 100 < 10 ? "0" : "") + cents % 100`. Format de ligne : `"%-3s %-13s %-6s %s -> %s %2d nuit(s) %9s  ref %s"`.

</details>

---

## Étape 3 — Les conflits : l'algorithme

<details><summary>Indice 1</summary>

Conflit si **même chambre** et `arrivals[i].isBefore(departures[j]) && arrivals[j].isBefore(departures[i])`. Double boucle `for i`, puis `for (int j = i + 1; …)`.

</details>

<details><summary>Indice 2</summary>

Début commun : la **plus tardive** des deux arrivées (`isAfter`). Fin commune : le **plus précoce** des deux départs. Nuits communes : `ChronoUnit.DAYS.between(début, fin)`.

</details>

---

## Étape 4 — Le planning en tableau 2D

<details><summary>Indice 1</summary>

`int[][] planning = new int[nombreDeChambres][10];`. Pour chaque réservation et chaque jour d, la nuit `start.plusDays(d)` est occupée si elle est dans le séjour : `planning[rooms[i]][d]++`.

</details>

<details><summary>Indice 2</summary>

« arrivée ≤ nuit » s'écrit `!night.isBefore(arrivée)`. En-tête : `String.format("%3d", jour)` pour chaque jour. Cellule : `count == 0 ? '.' : count == 1 ? '#' : '!'`.

</details>

---

## Étape 5 — Les clients triés

<details><summary>Indice 1</summary>

`Arrays.copyOf(guests, n)`, puis `Arrays.sort`, puis `String.join(", ", copie)`.

</details>

<details><summary>Indice 2</summary>

Trie bien les noms **normalisés** : sinon `"  INES petit"` passerait avant `"hugo durand"` à cause des espaces et des majuscules.

</details>
