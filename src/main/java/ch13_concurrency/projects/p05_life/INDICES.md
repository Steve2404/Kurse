# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les règles

<details><summary>Indice 1</summary>

L'indice `(r + dr + n) % n` fait « le tour » de la grille : la ligne au-dessus de la 1re est la dernière. Le `+ n` évite un reste négatif (chapitre 2, projet 3).

</details>

<details><summary>Indice 2</summary>

`step` ne lit **que** `current`, et n'écrit **que** dans `next`, sur ses lignes `[fromRow, toRow)`. C'est ce qui permet à plusieurs ouvriers de travailler en même temps sans se gêner.

</details>

---

## Étape 2 — Les générations en parallèle

<details><summary>Indice 1</summary>

L'action de la barrière (le 2e argument du constructeur) s'exécute **une fois**, quand le dernier ouvrier arrive, et **avant** que quiconque reparte. C'est le bon moment pour échanger `current` et `next`.

</details>

<details><summary>Indice 2</summary>

Les deux grilles sont des champs `static` : les ouvriers relisent `current` à **chaque** génération, après l'échange.

</details>

---

## Étape 3 — La barrière cassée

<details><summary>Indice 1</summary>

Pour être sûr que l'autre thread attend déjà, boucle tant que `barrier.getNumberWaiting()` ne vaut pas 1, avec un petit `Thread.sleep(1)`.

</details>

<details><summary>Indice 2</summary>

Quand un thread qui attend est interrompu, la barrière est **cassée** : tous ceux qui l'attendent, ou qui l'attendront, reçoivent une `BrokenBarrierException`, jusqu'au `reset()`.

</details>
