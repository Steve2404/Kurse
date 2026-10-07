# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le vieux code (sans `module-info`)

<details><summary>Indice 1</summary>

Le vieux code est rangé comme au chapitre 1 : un dossier par paquet, **sans** dossier de module au-dessus. `legacy/com/acme/text/Slugify.java` commence par `package com.acme.text;`.

</details>

<details><summary>Indice 2</summary>

`\\p{M}` (dans un texte Java) désigne, dans une expression régulière, les **marques** : les accents séparés de leur lettre par la forme NFD. Les retirer laisse la lettre seule.

</details>

---

## Étape 2 — Le module nommé et le cycle

<details><summary>Indice 1</summary>

`blog.app` requiert les jars par leur **nom de module automatique** : `acme.text` (déduit du nom du fichier) et `com.acme.utils` (lu dans le manifeste).

</details>

<details><summary>Indice 2</summary>

`Main.class.getModule().getDescriptor().isAutomatic()` dit si un module est automatique. Le module sans nom s'obtient avec `ClassLoader.getSystemClassLoader().getUnnamedModule()`.

</details>

---

## Étape 3 — Le script `build.sh`

<details><summary>Indice 1</summary>

`-C "$OUT/legacy" com/acme/text` : entre dans `$OUT/legacy`, puis ajoute **seulement** le dossier `com/acme/text`. C'est ainsi que chaque jar ne contient qu'un paquet.

</details>

<details><summary>Indice 2</summary>

Pour `jdeps`, le module path s'écrit en entier : `--module-path`. `-p` y veut dire autre chose.

</details>
