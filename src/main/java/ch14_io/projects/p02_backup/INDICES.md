# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les outils sur les arbres

<details><summary>Indice 1</summary>

`Files.walk(racine)` donne la racine, puis tout ce qu'elle contient, dossiers compris : filtre avec `Files.isRegularFile`.

</details>

<details><summary>Indice 2</summary>

Dans `copyTree`, le chemin cible est `to.resolve(from.relativize(p))` : le même chemin relatif, sous le nouveau dossier.

</details>

---

## Étape 2 — Sauvegarder, modifier, comparer

<details><summary>Indice 1</summary>

`Files.mismatch(a, b)` rend -1 si les deux fichiers ont le même contenu, sinon la position du premier octet différent.

</details>

<details><summary>Indice 2</summary>

Pour l'incrément, crée d'abord le dossier parent de chaque fichier copié (`createDirectories(cible.getParent())`).

</details>

---

## Étape 3 — Ce qui échoue, et les parcours

<details><summary>Indice 1</summary>

Chaque erreur est une sous-classe de `IOException` : attrape-la et affiche `getClass().getSimpleName()`.

</details>

<details><summary>Indice 2</summary>

`Files.list` ne descend que d'**un** niveau ; `Files.find(racine, profondeur, test)` filtre avec un test qui reçoit le chemin et ses attributs.

</details>
