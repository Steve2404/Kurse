# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Le journal et les classes

<details><summary>Indice 1</summary>

`log(String)` : `step++`, puis `(step < 10 ? "0" : "") + step + " " + what`. `log(String, int)` appelle `log(what + " = " + v)`, puis `return v;`.

</details>

<details><summary>Indice 2</summary>

Respecte **exactement** l'ordre des membres de l'énoncé dans chaque fichier : c'est cet ordre qui produit le journal. Le constructeur `Car(String)` commence par `this(name, 5);`, et `ElectricCar()` par `this("Anonyme", 100);`.

</details>

---

## Étape 2 — Ce qui charge une classe, ou pas

<details><summary>Indice 1</summary>

Écris les deux lignes du `main` et observe : que s'affiche-t-il entre la ligne `TracerApp` et `main : KIND` ? Et avant `main : Car.count` ?

</details>

<details><summary>Indice 2</summary>

Une constante de compilation, c'est un champ `static final` de type primitif ou `String`, initialisé par une **expression constante**. `javac` recopie sa **valeur** dans le code qui la lit.

</details>

---

## Étape 3 — Construire, trois fois

<details><summary>Indice 1</summary>

L'ordre d'une construction : d'abord le constructeur du parent (en entier, initialiseurs compris), **puis** les initialiseurs de la classe, **puis** le reste du corps de son constructeur.

</details>

<details><summary>Indice 2</summary>

Au moment de la ligne 13, en quelle étape de la construction se trouve l'objet ? Les initialiseurs d'`ElectricCar` (`battery = … 50`) ont-ils déjà tourné ? Quelle est la valeur par défaut d'un `int` ?

</details>
