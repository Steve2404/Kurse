# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Comptes, compteur, banque

<details><summary>Indice 1</summary>

Le motif d'un verrou : `lock.lock(); try { … } finally { lock.unlock(); }`. Pour deux verrous, imbrique deux fois ce motif : le `finally` intérieur relâche le 2e, l'extérieur relâche le 1er.

</details>

<details><summary>Indice 2</summary>

`biggest.accumulateAndGet(amount, Math::max)` remplace la valeur par le maximum de l'ancienne et de `amount`, de façon atomique.

</details>

---

## Étape 2 — Les virements concurrents

<details><summary>Indice 1</summary>

Garde les `Future` dans une liste, puis fais `get()` sur chacun : c'est la façon d'attendre que les 20 000 tâches soient finies.

</details>

<details><summary>Indice 2</summary>

Le séquentiel applique les mêmes virements, dans l'ordre, sur un simple `long[]`. Le résultat final ne dépend pas de l'ordre : une addition est commutative.

</details>

---

## Étape 3 — L'interblocage évité

<details><summary>Indice 1</summary>

`barrier.await()` ne rend la main que quand les **deux** tâches l'ont appelé : à ce moment, chacune tient déjà son premier verrou.

</details>

<details><summary>Indice 2</summary>

`tryLock(délai, unité)` rend `false` au bout du délai au lieu d'attendre pour toujours. A attend peu (100 ms), abandonne et relâche le compte 0 ; B, qui attend plus longtemps, l'obtient alors.

</details>

---

## Étape 4 — Réentrance, `volatile`, atomiques

<details><summary>Indice 1</summary>

Un `ReentrantLock` peut être pris **plusieurs fois** par le même thread : il compte les prises (`getHoldCount()`), et il faut autant d'`unlock()`.

</details>

<details><summary>Indice 2</summary>

`compareAndSet(attendu, nouveau)` ne remplace que si la valeur **actuelle** est égale à `attendu`, et rend `true` dans ce cas.

</details>
