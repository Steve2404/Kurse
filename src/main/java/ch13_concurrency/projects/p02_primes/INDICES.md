# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le crible segmenté

<details><summary>Indice 1</summary>

Un `boolean[]` de taille `to - from` : la case `n - from` représente le nombre `n`. Le premier multiple de p ≥ `from` vaut `((from + p - 1) / p) * p`.

</details>

<details><summary>Indice 2</summary>

Commence à rayer à `max(p * p, premier multiple)`. Attention au débordement : `p * p` peut dépasser un `int` sur de grands nombres ; calcule en `long` si besoin.

</details>

---

## Étape 2 — Soumettre et combiner

<details><summary>Indice 1</summary>

`pool.submit(tache)` rend tout de suite un `Future`. `future.get()` attend le résultat. En lisant les `Future` **dans l'ordre de la liste**, la fusion reste dans l'ordre, quel que soit le thread le plus rapide.

</details>

<details><summary>Indice 2</summary>

`invokeAll` attend **toutes** les tâches et rend la liste des `Future` (déjà terminés). `invokeAny` rend le résultat de **la première** tâche qui réussit, et annule les autres.

</details>

---

## Étape 3 — Délai, annulation, échec

<details><summary>Indice 1</summary>

`get(50, TimeUnit.MILLISECONDS)` lance une `TimeoutException` si le résultat n'est pas prêt : la tâche continue de tourner, c'est `cancel(true)` qui l'interrompt.

</details>

<details><summary>Indice 2</summary>

L'exception d'une tâche n'arrive pas directement : `get()` l'**enveloppe** dans une `ExecutionException`. La vraie est dans `getCause()`.

</details>

---

## Étape 4 — Arrêter

<details><summary>Indice 1</summary>

`shutdown()` refuse les nouvelles tâches mais laisse finir celles en cours. `awaitTermination` attend cette fin, au plus le délai donné.

</details>

<details><summary>Indice 2</summary>

`shutdownNow()` interrompt la tâche en cours **et** rend la liste des tâches qui attendaient encore dans la file : sa taille est le nombre de tâches jamais lancées.

</details>
