# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Décomposer, comparer

<details><summary>Indice 1</summary>

Les noms d'un chemin sont numérotés à partir de 0, **sans** la racine : `getName(0)` est `docs`. `subpath(1, 3)` prend les noms 1 et 2.

</details>

<details><summary>Indice 2</summary>

`startsWith("doc")` compare des **noms entiers**, pas des lettres : `doc` n'est pas le nom `docs`.

</details>

---

## Étape 2 — Normaliser, relativiser

<details><summary>Indice 1</summary>

`normalize()` retire les `.` et résout les `..` **sans** regarder le disque.

</details>

<details><summary>Indice 2</summary>

`relativize` part du **dossier** de la source (`getParent()`), pas de la page elle-même : un lien se lit depuis le dossier qui contient la page.

</details>

---

## Étape 3 — Le mini-shell et l'arborescence

<details><summary>Indice 1</summary>

Après `normalize()`, un chemin qui sort de la racine commence par `..` : `next.startsWith("..")` le détecte.

</details>

<details><summary>Indice 2</summary>

Pour l'arborescence, garde dans un `Set` les dossiers déjà affichés. Pour une page de profondeur n, ses dossiers sont `subpath(0, 1)`, …, `subpath(0, n - 1)`.

</details>

---

## Étape 4 — `resolve`, `File`, erreurs

<details><summary>Indice 1</summary>

`resolve` avec un chemin **absolu** ignore le chemin de départ et rend l'argument.

</details>

<details><summary>Indice 2</summary>

Les trois erreurs sont des exceptions non vérifiées : attrape-les, et affiche `getClass().getSimpleName()`.

</details>
