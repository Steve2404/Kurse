# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — L'outil de journalisation

<details><summary>Indice 1</summary>

Tout est `static` dans `BootLog` : le compteur et les trois méthodes. Ainsi, la classe `Server` peut les appeler par `BootLog.nomDeLaMethode(...)` sans créer d'objet `BootLog`.

</details>

<details><summary>Indice 2</summary>

Une méthode « journaliser puis rendre » reçoit un nom de champ et une valeur. Elle appelle la méthode « noter » avec `nom + " = " + valeur`, puis fait `return valeur;`. Dans `Server`, on l'utilise **à droite du `=`** d'une déclaration de champ.

</details>

---

## Étape 2 — L'ordre d'initialisation : la classe `Server`

<details><summary>Indice 1</summary>

Java exécute les initialiseurs de champs et les blocs `{ }` **de haut en bas, dans l'ordre du texte**, puis le corps du constructeur. Lis le journal attendu ligne par ligne : il te donne l'ordre exact dans lequel écrire les membres de `Server`. Le constructeur peut être placé n'importe où.

</details>

<details><summary>Indice 2</summary>

- **Ligne 2 :** le nom simple `name` est refusé avant sa déclaration, mais `this.name` est accepté.
- **Ligne 4 :** une méthode d'instance qui lit `late` et le journalise ; `early` est initialisé par cet appel.
- **Ligne 7 :** dans le constructeur, `maxUsers` seul désigne le paramètre ; le champ s'écrit `this.maxUsers`.

</details>

---

## Étape 3 — Deux objets, deux initialisations

<details><summary>Indice 1</summary>

`var` ne s'utilise que pour une variable **locale avec initialiseur** : `var first = new Server(50);`. Le type déduit est `Server`.

</details>

<details><summary>Indice 2</summary>

Pour la question : où vivent les blocs et les champs d'instance, et où vit le compteur `static` ? L'un existe **une fois par objet**, l'autre **une fois pour la classe**.

</details>

---

## Étape 4 — La portée et le masquage

<details><summary>Indice 1</summary>

Écris une méthode d'instance dans `Server`, appelée depuis `main` sur un des serveurs. Une variable locale `int port = 9090;` y masque le champ ; `this.port` reste accessible.

</details>

<details><summary>Indice 2</summary>

Un bloc, ce sont juste deux accolades `{ }` dans le corps de la méthode, sans mot-clé devant. `backup` vaut le `port` **local** + 1 : il est calculé, pas écrit en dur.

</details>

---

## Étape 5 — Le bilan et le ramasse-miettes

<details><summary>Indice 1</summary>

Le nombre de serveurs créés et le nombre d'étapes viennent des deux compteurs `static`. La 2e ligne lit `first.maxUsers` **après** `first = second;`.

</details>

<details><summary>Indice 2</summary>

Pour le ramasse-miettes, dessine les variables et les objets : une flèche par référence. Un objet devient éligible quand **plus aucune flèche** n'y mène. Compte les flèches vers chaque `Server` avant et après `first = second;`.

</details>
