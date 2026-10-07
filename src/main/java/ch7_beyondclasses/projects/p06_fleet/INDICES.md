# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les capacités

<details><summary>Indice 1</summary>

`speedOn` : `return switch (mode) { case ROAD -> this instanceof Drivable ? ((Drivable) this).roadSpeed() : 0; … };`. Un `switch` sur un enum s'écrit avec les noms **courts** (`ROAD`, pas `Mode.ROAD`). Il couvre toutes les constantes, donc pas besoin de `default`.

</details>

<details><summary>Indice 2</summary>

`Amphibian extends Car implements Sailable` hérite de `Drivable` par `Car`. Son `horn()` : `"pouet-" + super.horn()`. `super` désigne ici `Car`, qui a hérité de la version de `Drivable`.

</details>

---

## Étape 2 — Le plus rapide trajet

<details><summary>Indice 1</summary>

Dijkstra en O(n²) : `n` tours. À chaque tour, le lieu `u` non traité de plus petit `time`. Si aucun n'est atteignable, `break`. Marque `u` comme traité, puis relâche ses liaisons.

</details>

<details><summary>Indice 2</summary>

- Une liaison `A B MODE km` touche `u` si `A` ou `B` vaut `u` : l'autre extrémité est `a == u ? b : b == u ? a : -1`.
- La vitesse vient de `v.speedOn(Mode.valueOf(p[2]))` : si elle vaut 0, on saute la liaison.
- Le chemin : `for (int at = prev[to]; at >= 0; at = prev[at]) path.insert(0, PLACES[at] + " > ");`.

</details>

---

## Étape 3 — Un objet, plusieurs vues

<details><summary>Indice 1</summary>

Upcast (vers un parent ou une interface) : implicite, aucun risque. Downcast (vers un enfant) : un cast explicite, sûr seulement si l'objet **est** de ce type.

</details>

<details><summary>Indice 2</summary>

Les tableaux d'interfaces : `if (v instanceof Flyable fl) flyers[f++] = fl;`. Un même objet (l'avion) peut aller dans **les deux** tableaux.

</details>
