# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Modifier et annuler

<details><summary>Indice 1</summary>

Un `Edit` mémorise **tout** ce qu'il faut pour défaire. Pour `SET` et `DELETE`, l'ancienne ligne (`before`) se lit **avant** de modifier : `lines.get(i)`.

</details>

<details><summary>Indice 2</summary>

- `apply` : un `switch (e.type())`. `case "ADD", "INSERT" -> forward ? lines.add(i, after) : lines.remove(i)`, et ainsi de suite.
- `UNDO` : `Edit e = undo.poll(); if (e == null) return "rien a annuler"; apply(e, false); redo.push(e);`.

</details>

---

## Étape 2 — Compléter

<details><summary>Indice 1</summary>

Tous les mots qui commencent par `ma` sont ≥ `ma` et < tout mot qui commencerait par `ma` suivi d'un caractère plus grand que n'importe quelle lettre.

</details>

<details><summary>Indice 2</summary>

`subSet(debut, true, fin, false)` : début inclus, fin exclue. `ceiling(p)` rend le plus petit élément ≥ p.

</details>

---

## Étape 3 — Vérifier et corriger

<details><summary>Indice 1</summary>

`brackets` : `"([{".indexOf(c)` et `")]}".indexOf(c)`. Un ouvrant et un fermant correspondent s'ils ont le **même indice** dans leurs chaînes.

</details>

<details><summary>Indice 2</summary>

Les variantes :
- supprimer : `w.substring(0, i) + w.substring(i + 1)` ;
- échanger : `… + w.charAt(i + 1) + w.charAt(i) + …` ;
- remplacer et insérer : une double boucle sur la position et sur `c` de `'a'` à `'z'`.

</details>
