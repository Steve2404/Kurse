# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — La base, et ce qu'elle dit d'elle-même

<details><summary>Indice 1</summary>

`conn.getMetaData()` rend un `DatabaseMetaData` : la base décrit son nom, son pilote, ses capacités.

</details>

<details><summary>Indice 2</summary>

Deux connexions dans le même try-with-resources : séparées par `;`.

</details>

---

## Étape 2 — Le moteur de tableau (`TablePrinter`)

<details><summary>Indice 1</summary>

`rs.getMetaData()` donne le nombre de colonnes, leurs noms et leurs types, **avant** de lire les lignes.

</details>

<details><summary>Indice 2</summary>

Garde les lignes dans une `List<String[]>` au 1er passage ; au 2e, complète chaque cellule avec des espaces jusqu'à la largeur.

</details>

---

## Étape 3 — Explorer le schéma (`Schema`)

<details><summary>Indice 1</summary>

`getTables`, `getColumns`, `getPrimaryKeys` et `getImportedKeys` rendent tous des `ResultSet` : lis-les comme une requête, par nom de colonne.

</details>

<details><summary>Indice 2</summary>

Le tri topologique de Kahn : chapitre 9, projet 3, étape 1.

</details>

---

## Étape 4 — Le dump, la copie, la vérification

<details><summary>Indice 1</summary>

Dans un littéral SQL, une apostrophe s'écrit `''` (deux apostrophes) : `replace("'", "''")`.

</details>

<details><summary>Indice 2</summary>

Vider les tables dans l'ordre de **suppression** : les tables qui référencent d'abord, celles qui sont référencées ensuite.

</details>
