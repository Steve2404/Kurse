# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le visiteur

<details><summary>Indice 1</summary>

`SimpleFileVisitor` a une méthode par moment du parcours : avant un dossier, pour chaque fichier, après un dossier. Redéfinis seulement celles dont tu as besoin.

</details>

<details><summary>Indice 2</summary>

Pour ajouter la taille à chaque dossier ancêtre, remonte avec `getParent()` depuis le chemin relatif du fichier, jusqu'à `null`.

</details>

---

## Étape 2 — Lire et modifier les attributs

<details><summary>Indice 1</summary>

`Files.readAttributes(f, BasicFileAttributes.class)` lit tous les attributs de base d'un coup.

</details>

<details><summary>Indice 2</summary>

Pour trier par date décroissante, compare les `FileTime` (ils sont `Comparable`), puis inverse.

</details>

---

## Étape 3 — Les doublons

<details><summary>Indice 1</summary>

Deux fichiers de tailles différentes ne peuvent pas être identiques : on ne compare les contenus qu'à l'intérieur d'un même groupe de taille.

</details>

<details><summary>Indice 2</summary>

La profondeur d'un chemin vaut `racine.relativize(p).getNameCount()`, sauf pour la racine elle-même (0).

</details>
