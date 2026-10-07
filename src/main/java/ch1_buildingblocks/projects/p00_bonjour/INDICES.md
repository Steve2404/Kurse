# Projet 0 — Indices, étape par étape

> **Comment s'en servir :** ce projet est guidé, donc tu ne devrais pas bloquer longtemps. Si quelque chose ne marche pas après **10 minutes**, ouvre l'**indice 1** de l'étape, puis l'**indice 2** s'il ne suffit pas.

---

## Étape 2 — Créer la classe `Bonjour`

<details><summary>Indice 1</summary>

Pas de **Java Class** dans le menu **New** ? Tu as sans doute fait le clic droit sur un fichier au lieu du **dossier** `p00_bonjour`. Le dossier a une icône de dossier, pas de tasse de café.

</details>

<details><summary>Indice 2</summary>

Le fichier a été créé au mauvais endroit ? Supprime-le (clic droit → **Delete**), puis recommence le clic droit **sur le dossier** `p00_bonjour`.

</details>

---

## Étape 3 — `main` et la flèche verte

<details><summary>Indice 1</summary>

Pas de flèche verte ? Compare ta ligne `main` **lettre par lettre** avec celle du `TODO.md` : `public static void main(String[] args)`. Un `S` minuscule à `String`, un mot oublié, et la flèche n'apparaît pas.

</details>

<details><summary>Indice 2</summary>

Du rouge dans le code ? Pose la souris sur le soulignement rouge : IntelliJ explique le problème. Le plus souvent, c'est un `;`, un guillemet `"` ou une accolade `}` oublié.

</details>

---

## Étape 5 — Les arguments

<details><summary>Indice 1</summary>

Pas de champ **Program arguments** dans **Edit Configurations…** ? Vérifie qu'à gauche, c'est bien **Bonjour** qui est sélectionné, sous **Application**. Si le champ est caché, clique sur **Modify options** et coche **Program arguments**.

</details>

<details><summary>Indice 2</summary>

`Check` affiche `Bonjour, Marie!` au lieu de `Bonjour, Marie !` ? L'espace **avant** le `!` doit être écrit **dans les guillemets** : `" !"`.

</details>

---

## Étape 6 — Le terminal

<details><summary>Indice 1</summary>

`javac` ou `java` : « n'est pas reconnu comme nom de commande » ? Le JDK n'est pas dans le chemin du terminal. Ferme et rouvre IntelliJ ; si ça ne suffit pas, demande-moi de l'aide.

</details>

<details><summary>Indice 2</summary>

`file not found` avec `javac` ? Tape `pwd` : tu dois être dans le dossier `Kurse`. Sinon, tape `cd` suivi du chemin du dossier `Kurse`. Vérifie aussi le chemin du fichier, en le complétant avec **Tab**.

</details>

---

## Étape 7 — Les erreurs

<details><summary>Indice 1</summary>

Plusieurs erreurs d'un coup ? Lis seulement **la première**, regarde où pointe le chapeau `^`, et compare avec le code d'origine.

</details>

<details><summary>Indice 2</summary>

Tu ne retrouves plus le code d'origine ? Recopie le code complet de l'étape 5 du `TODO.md`.

</details>
