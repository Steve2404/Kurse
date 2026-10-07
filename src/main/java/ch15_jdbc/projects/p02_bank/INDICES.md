# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Ouvrir la banque

<details><summary>Indice 1</summary>

Un seul `PreparedStatement`, réutilisé : change seulement les `set…`, puis `executeUpdate()` à chaque ligne.

</details>

<details><summary>Indice 2</summary>

Les comptes sont insérés **avant** que la banque coupe l'auto-commit : chaque `INSERT` a été validé tout de suite.

</details>

---

## Étape 2 — Les virements

<details><summary>Indice 1</summary>

Avec `setAutoCommit(false)`, rien n'est définitif avant `commit()`. En cas d'erreur, `rollback()` défait **tout** depuis le dernier `commit`.

</details>

<details><summary>Indice 2</summary>

`RETURN_GENERATED_KEYS` en 2e argument de `prepareStatement`, puis `getGeneratedKeys()` après `executeUpdate()`.

</details>

---

## Étape 3 — Les paies groupées et le rejeu

<details><summary>Indice 1</summary>

Une paie = plusieurs `move`, **un seul** `commit` à la fin : tout passe, ou rien.

</details>

<details><summary>Indice 2</summary>

Le rejeu applique le journal aux soldes initiaux, avec `merge` (chapitre 9).

</details>

---

## Étape 4 — Ce que voit une autre connexion

<details><summary>Indice 1</summary>

Une autre connexion ne voit que ce qui est **validé** (`commit`).

</details>

<details><summary>Indice 2</summary>

`setAutoCommit(true)` valide la transaction en cours.

</details>
