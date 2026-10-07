# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Les classes

<details><summary>Indice 1</summary>

Une classe sérialisable réalise `Serializable` (une interface sans méthode). `transient` exclut un champ de la sauvegarde.

</details>

<details><summary>Indice 2</summary>

`Entity` n'est **pas** sérialisable : à la relecture, Java appellera son constructeur sans argument. Fais-lui compter ses appels.

</details>

---

## Étape 2 — Sauver, relire

<details><summary>Indice 1</summary>

`writeObject` et `readObject`, dans le **même ordre**. `readObject` rend un `Object` : il faut un cast.

</details>

<details><summary>Indice 2</summary>

Lire plus loin que la fin lance une `EOFException` : attrape-la.

</details>

---

## Étape 3 — La pile d'annulation

<details><summary>Indice 1</summary>

Une copie profonde par sérialisation : écrire l'objet dans un `ByteArrayOutputStream`, puis le relire depuis un `ByteArrayInputStream` construit avec `toByteArray()`.

</details>

<details><summary>Indice 2</summary>

Avant chaque action (sauf `undo`), empile une copie profonde de l'état ; `undo` remplace l'état par la copie dépilée.

</details>
