# Projet 4 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Le modèle (paquet `model`)

<details><summary>Indice 1</summary>

Crée le dossier `model` dans `p04_bookshop`, et mets en 1re ligne de chaque fichier `package ch1_buildingblocks.projects.p04_bookshop.model;`. Le nom du paquet doit correspondre **exactement** au chemin du dossier.

</details>

<details><summary>Indice 2</summary>

Pour que `export.Book` puisse lire le titre, l'auteur et l'année, donne au livre trois petites méthodes `public` qui rendent ces valeurs. Les champs peuvent rester sans modificateur. Le constructeur et les méthodes appelés depuis un autre paquet doivent être `public`, pas seulement la classe.

</details>

---

## Étape 2 — L'export (paquet `export`) : le conflit de noms

<details><summary>Indice 1</summary>

Un nom **pleinement qualifié**, c'est le nom du paquet + `.` + le nom de la classe. Il s'écrit partout où un type est attendu : type d'un champ, d'un paramètre, après `new`. Aucun import n'est alors nécessaire.

</details>

<details><summary>Indice 2</summary>

Pour produire `"Dune";"Herbert, Frank";1965`, chaque guillemet affiché s'écrit `\"` dans le code. Le nom de tri vient de l'auteur du livre source.

</details>

---

## Étape 3 — Le lanceur (paquet `app`)

<details><summary>Indice 1</summary>

Avec `import ….model.*;`, `Book` seul désigne `model.Book`. Pour l'autre `Book`, écris le nom complet à gauche **et** après `new`.

</details>

<details><summary>Indice 2</summary>

Pour les expériences 1 et 2, pense à ce que fait `javac` quand **deux** imports joker fournissent le même nom simple. Il ne choisit pas au hasard. Un import **explicite**, lui, est prioritaire sur un joker. Pour l'expérience 3 : un joker n'importe que des **classes**, jamais des sous-paquets.

</details>

---

## Étape 4 — L'outil autonome (paquet `tools`)

<details><summary>Indice 1</summary>

Une classe normale avec `package …tools;` et un `main` qui lit `args[0]`. Le mode « fichier source unique » se lance avec `java chemin/Hello.java Lea`.

</details>

<details><summary>Indice 2</summary>

Pour la question : en mode fichier source unique, `java` compile **en mémoire** ce seul fichier. Les autres `.java` du projet ne sont pas compilés. Où pourraient alors se trouver les classes `model` ?

</details>

---

## Étape 5 — Le script `commandes.sh`

<details><summary>Indice 1</summary>

Commence par des variables pour ne pas recopier les longs chemins : le dossier source `src/main/java`, le chemin du projet, le nom complet de `Main`, le dossier de sortie `build/ch1-p04`. Commence aussi par `rm -rf` sur ce dossier : chaque lancement repart de zéro.

</details>

<details><summary>Indice 2</summary>

- `javac -d DOSSIER fichiers.java…` : donne les 4 fichiers de l'application (pas `Hello`).
- `java -cp DOSSIER nom.complet.de.Main args…`.
- `jar --create --file X.jar -C DOSSIER .` : `-C` change de dossier avant d'ajouter `.`.
- `jar --list --file X.jar | grep -c '\.class$'`, dans un `$( … )` pour l'insérer dans un `echo`.
- Le jar exécutable : la même commande `jar --create`, plus `--main-class nom.complet`.

</details>
