# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les trois modules

<details><summary>Indice 1</summary>

Le dossier d'un module porte **le nom du module** (`src/library.service/`), et son `module-info.java` est **à la racine** de ce dossier. Les paquets sont rangés **en dessous** : `src/library.service/library/service/Catalog.java`.

</details>

<details><summary>Indice 2</summary>

Un export qualifié s'écrit `exports paquet to module;`. Pour la question : regarde le **type rendu** par `byAuthor`, et dans quel module il est déclaré. Qui l'utilise ?

</details>

---

## Étape 2 — Le programme

<details><summary>Indice 1</summary>

`Main.class.getModule()` donne le module de `Main`. Fais de même avec `Catalog.class` et `Book.class` : tu as les trois objets `Module` à comparer.

</details>

<details><summary>Indice 2</summary>

`isExported(paquet, module)` teste l'export **vers** un module. `isExported(paquet)` (un seul argument) teste l'export **à tous**. `canRead(autre)` teste la lecture.

</details>

---

## Étape 3 — L'intrus

<details><summary>Indice 1</summary>

L'intrus est un module **à part**, dans `intruder/` et non dans `src/`. Il a son propre `module-info.java` et son paquet `library.intruder`.

</details>

<details><summary>Indice 2</summary>

Il **doit** échouer : n'essaie pas de le réparer. Son échec prouve que l'export qualifié ne s'ouvre qu'à `library.app`.

</details>

---

## Étape 4 — Le script `build.sh`

<details><summary>Indice 1</summary>

Lance chaque commande **seule** dans Git Bash, sans filtre, et lis tout ce qu'elle affiche. Ajoute ensuite les filtres `sed`, `sort` et `head`.

</details>

<details><summary>Indice 2</summary>

L'intrus se compile **contre** les modules déjà compilés : `-p "$OUT/mods"`. Sans `|| true`, `set -e` arrêterait le script sur cet échec voulu.

</details>
