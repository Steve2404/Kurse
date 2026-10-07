# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les modules

<details><summary>Indice 1</summary>

Le sac à dos : `best[i][b]` = la meilleure valeur avec les `i` premiers articles et un budget `b`. Soit on ne prend pas l'article `i` (`best[i - 1][b]`), soit on le prend s'il rentre (`best[i - 1][b - coût] + valeur`). On garde le maximum.

</details>

<details><summary>Indice 2</summary>

Pour retrouver le choix, pars de `b = budget` et remonte `i` de n à 1 : si `best[i][b] != best[i - 1][b]`, l'article `i` est pris, et `b` diminue de son coût. Ajoute les articles pris au **début** de la liste pour garder l'ordre d'origine.

</details>

---

## Étape 2 — Le programme

<details><summary>Indice 1</summary>

`ModuleLayer.boot().findModule(name)` rend un `Optional<Module>`. Sur le module : `getDescriptor()`, puis `requires()` (chaque `Requires` a un `name()`).

</details>

<details><summary>Indice 2</summary>

`mainClass()` et `version()` rendent des `Optional` : `map(…).orElse("aucune")` (chapitre 10). Le parcours en profondeur : marque le module vu, visite chacune de ses dépendances, puis ajoute le module à l'ordre.

</details>

---

## Étape 3 — Le script `build.sh`

<details><summary>Indice 1</summary>

`jlink` a besoin de **jars** (ou de modules compilés) et d'un module de départ (`--add-modules inv.app`). Il ajoute tout seul les modules du JDK nécessaires.

</details>

<details><summary>Indice 2</summary>

L'image est un dossier : son `java` se lance par son chemin, `"$OUT/image/bin/java"`. Le lanceur créé par `--launcher inventaire=inv.app` est `"$OUT/image/bin/inventaire"`.

</details>
