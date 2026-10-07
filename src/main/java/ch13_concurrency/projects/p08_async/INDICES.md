# Projet 8 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 7. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les devis asynchrones

<details><summary>Indice 1</summary>

`CompletableFuture.supplyAsync(() -> …, executor)` lance le calcul sur le pool et rend tout de suite une future. `thenCombine(autre, (a, b) -> …)` attend les deux résultats.

</details>

<details><summary>Indice 2</summary>

`toChf` rend **déjà** une `CompletableFuture`. Pour l'enchaîner sans obtenir une future dans une future, utilise `thenCompose`.

</details>

---

## Étape 2 — Erreurs, délais, enchaînements

<details><summary>Indice 1</summary>

`join()` enveloppe l'erreur dans une `CompletionException` (non vérifiée) ; `get()` l'enveloppe dans une `ExecutionException` (vérifiée). Dans les deux cas, la vraie est dans `getCause()`.

</details>

<details><summary>Indice 2</summary>

Une `new CompletableFuture<>()` ne se termine jamais toute seule : `completeOnTimeout` lui donne une valeur au bout du délai, `orTimeout` la termine en erreur.

</details>

---

## Étape 3 — `ThreadLocal`, `Semaphore`, `CountDownLatch`

<details><summary>Indice 1</summary>

Un `ThreadLocal` donne à **chaque thread** sa propre valeur : celle de `main` n'est pas touchée par les tâches du pool.

</details>

<details><summary>Indice 2</summary>

`acquire()` prend un permis (et attend s'il n'y en a plus) ; `release()` le rend, toujours dans un `finally`.

</details>

---

## Étape 4 — Fork/Join

<details><summary>Indice 1</summary>

Le `Summary` de Fork/Join est le même que celui de Kadane par morceaux : `merge` ressemble à la fusion des séries du projet 1.

</details>

<details><summary>Indice 2</summary>

`left.fork()` envoie la moitié gauche à un autre thread ; le thread courant calcule la droite lui-même avec `compute()`, puis attend la gauche avec `join()`.

</details>
