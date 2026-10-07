# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le nettoyage (pur Java)

<details><summary>Indice 1</summary>

`putIfAbsent` rend l'**ancienne** valeur, ou `null` si la clé était nouvelle : c'est ainsi que tu repères un doublon.

</details>

<details><summary>Indice 2</summary>

`split(";")` puis `length` donne le nombre de champs.

</details>

---

## Étape 2 — L'import par lots

<details><summary>Indice 1</summary>

`addBatch()` met l'ordre de côté ; `executeBatch()` envoie tout le lot d'un coup et rend un `int[]` (une case par ordre).

</details>

<details><summary>Indice 2</summary>

`BatchUpdateException` est une `SQLException` : `getUpdateCounts()` dit quelles cases ont échoué (`Statement.EXECUTE_FAILED`).

</details>

---

## Étape 3 — Le fichier qui gêne

<details><summary>Indice 1</summary>

Le mode strict annule tout avec `rollback()` dès le 1er lot en échec ; le mode tolérant garde les lignes qui ont réussi.

</details>

<details><summary>Indice 2</summary>

Les numéros de clé consommés par une transaction annulée ne sont **pas** rendus.

</details>

---

## Étape 4 — Le lot de `Statement`, et la clé par son nom

<details><summary>Indice 1</summary>

Un `Statement` aussi a `addBatch(sql)` ; `clearBatch()` vide le lot sans l'envoyer.

</details>

<details><summary>Indice 2</summary>

`prepareStatement(sql, new String[]{"ID"})` demande la clé générée par le **nom** de sa colonne.

</details>
