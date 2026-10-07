# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — RLE et Adler-32

<details><summary>Indice 1</summary>

`read()` rend l'octet sous forme d'un `int` entre 0 et 255, ou -1 à la fin du fichier : boucle `while ((b = in.read()) != -1)`.

</details>

<details><summary>Indice 2</summary>

Pour la compression, garde l'octet courant et son compte. Écris le couple quand l'octet change, quand le compte atteint 255, et à la fin.

</details>

---

## Étape 2 — Texte, ajout, encodages

<details><summary>Indice 1</summary>

`new FileWriter(fichier, true)` ouvre en **ajout** : le texte s'écrit à la fin, sans effacer.

</details>

<details><summary>Indice 2</summary>

`OutputStreamWriter(flux, StandardCharsets.UTF_8)` choisit l'encodage à l'écriture, `InputStreamReader(flux, …)` à la lecture.

</details>

---

## Étape 3 — `mark`/`reset`, données typées

<details><summary>Indice 1</summary>

`mark(100)` pose un marque-page ; `reset()` y revient. `skip(n)` saute n caractères et rend le nombre réellement sauté.

</details>

<details><summary>Indice 2</summary>

Un `DataInputStream` doit relire **dans le même ordre** et avec les **mêmes types** que l'écriture.

</details>
