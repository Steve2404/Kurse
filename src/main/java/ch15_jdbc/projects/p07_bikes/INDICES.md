# Projet 7 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 6. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — L'installation et le tarif

<details><summary>Indice 1</summary>

Une tranche entamée : `(a + b - 1) / b`, en division entière (chapitre 2).

</details>

<details><summary>Indice 2</summary>

Les vélos référencent les stations : les stations doivent exister **avant**.

</details>

---

## Étape 2 — Le service : une commande = une transaction

<details><summary>Indice 1</summary>

`transaction(work)` : `work.run()`, puis `commit()` ; sur une `SQLException`, `rollback()`. Chaque commande lui passe une lambda.

</details>

<details><summary>Indice 2</summary>

Une erreur **métier** est une `SQLException` que tu crées toi-même, avec ton propre SQLState (`45000`).

</details>

---

## Étape 3 — Rendre, payer, rembourser

<details><summary>Indice 1</summary>

Le `Savepoint` du paiement permet d'annuler **seulement** le débit, en gardant le retour du vélo.

</details>

<details><summary>Indice 2</summary>

Toutes les vérifications (location ouverte, place libre) se font **avant** la 1re modification.

</details>

---

## Étape 4 — Le bilan et le rééquilibrage

<details><summary>Indice 1</summary>

Une station qui a 2 vélos en trop apparaît **deux fois** dans `givers`.

</details>

<details><summary>Indice 2</summary>

Le lot n'est envoyé qu'à la fin : la base ne sait pas encore quels vélos ont été choisis, d'où l'`OFFSET`.

</details>
