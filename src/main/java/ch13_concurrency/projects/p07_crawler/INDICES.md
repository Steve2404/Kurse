# Projet 7 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le robot

<details><summary>Indice 1</summary>

Le parcours avance **niveau par niveau**, comme un parcours en largeur (chapitre 9, projet 3) : toutes les pages d'un niveau sont téléchargées en parallèle par `invokeAll`, puis on construit le niveau suivant.

</details>

<details><summary>Indice 2</summary>

`visited.add(lien)` rend `true` seulement si le lien **n'y était pas** : c'est un test et un ajout en **une seule** opération atomique.

</details>

---

## Étape 2 — Le programme

<details><summary>Indice 1</summary>

L'index contient des ensembles concurrents : copie chacun dans un `TreeSet`, et l'index dans une `TreeMap`, pour un affichage trié.

</details>

<details><summary>Indice 2</summary>

« Découvertes » compte les pages ajoutées à `visited` ; « téléchargées » compte les pages réellement passées par `fetch`. Regarde la condition de la boucle des niveaux.

</details>

---

## Étape 3 — Le planificateur

<details><summary>Indice 1</summary>

`schedule(tâche, délai, unité)` lance une fois après le délai. `scheduleAtFixedRate` et `scheduleWithFixedDelay` relancent sans fin, jusqu'à `cancel`.

</details>

<details><summary>Indice 2</summary>

La `CountDownLatch` sert à attendre « au moins 5 battements » : `await()` rend la main quand le compte arrive à 0.

</details>
