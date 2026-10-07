# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les villes

<details><summary>Indice 1</summary>

Dans le constructeur compact, on **réaffecte les paramètres** : `name = name.strip();`, puis `name = name.substring(0, 1).toUpperCase() + …`. Java affecte ensuite lui-même les champs avec ces valeurs.

</details>

<details><summary>Indice 2</summary>

Bornes : `lat = Math.max(-90, Math.min(90, lat));`. Le compteur `private static int created` est incrémenté dans le constructeur compact. Le constructeur `City(String)` passe par lui grâce à `this(name, 0, 0)`.

</details>

---

## Étape 2 — Les distances

<details><summary>Indice 1</summary>

Deux boucles `i`, `j` : `dist[i][j] = cities[i].distanceTo(cities[j]);`. La méthode `default` de l'interface est héritée par le record.

</details>

<details><summary>Indice 2</summary>

Haversine : convertis `lat()` et `other.lat()` en radians, ainsi que la différence des longitudes. Calcule `a`, puis `2 * EARTH_RADIUS_KM * Math.asin(Math.sqrt(a))`.

</details>

---

## Étape 3 — Trois tournées

<details><summary>Indice 1</summary>

- Plus proche voisin : un `boolean[] seen`. À chaque étape, la ville non vue la plus proche de la précédente.
- `Route.length` : `dist[order[i]][order[(i + 1) % n]]`, le `% n` fermant la boucle.

</details>

<details><summary>Indice 2</summary>

- 2-opt : un `while (improved)` autour des deux boucles. Inverser un tronçon, c'est échanger `order[l]` et `order[r]` en rapprochant l et r.
- Optimum : la récursion d'échanges du chapitre 5, en commençant à k = 1 pour garder Paris en tête. Remets `bestKm = Double.MAX_VALUE` avant.

</details>
