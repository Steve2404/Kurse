# Projet 5 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Créer et analyser le gros fichier

<details><summary>Indice 1</summary>

`Files.newBufferedWriter(chemin)` rend un `BufferedWriter` ; `newLine()` ajoute le saut de ligne du système.

</details>

<details><summary>Indice 2</summary>

Chaque parcours ouvre **son** `Files.lines(chemin)` dans un `try (Stream<String> lignes = …)`.

</details>

---

## Étape 2 — Le tri externe

<details><summary>Indice 1</summary>

Le tri externe : trier des petits paquets en mémoire, les écrire, puis les **fusionner** en ne gardant en mémoire qu'une ligne par paquet.

</details>

<details><summary>Indice 2</summary>

Le tas contient au plus une ligne par paquet : quand tu retires la plus petite, remets à sa place la ligne **suivante du même lecteur**.

</details>

---

## Étape 3 — Options d'ouverture

<details><summary>Indice 1</summary>

`StandardOpenOption.APPEND` ajoute à la fin ; sans option, le fichier est créé ou **remplacé**.

</details>

<details><summary>Indice 2</summary>

`CREATE_NEW` refuse un fichier qui existe déjà : attrape `FileAlreadyExistsException`.

</details>
