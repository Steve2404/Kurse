# Projet 7 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 6. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le dépôt

<details><summary>Indice 1</summary>

Un objet est stocké sous son **empreinte** : deux contenus identiques ont la même empreinte, et un seul objet suffit.

</details>

<details><summary>Indice 2</summary>

`status` compare deux `Map` chemin → empreinte : présent d'un seul côté, ou empreintes différentes.

</details>

---

## Étape 2 — Le scénario

<details><summary>Indice 1</summary>

`split(" ", 3)` garde le message d'un commit entier, même s'il contient des espaces.

</details>

<details><summary>Indice 2</summary>

Pour le `log`, remonte les parents avec `load(id).parent()` jusqu'à 0.

</details>
