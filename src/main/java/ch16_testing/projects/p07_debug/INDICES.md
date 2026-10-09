# Projet 7 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Reproduire

<details><summary>Indice 1</summary>

Mets les deux sorties côte à côte, ligne par ligne, et coche les lignes identiques. Il y en a **deux** : la 1re et la dernière.

</details>

<details><summary>Indice 2</summary>

Pour copier `LegacyInventory` : sélectionne la classe entière dans `Data.java`, **Ctrl+C**, puis colle-la dans ta nouvelle classe `Inventory` et renomme-la. Retire `static` de sa déclaration (c'est une classe de premier niveau maintenant).

</details>

---

## Étape 2 — Le premier point d'arrêt (BUG 1)

<details><summary>Indice 1</summary>

Dans le panneau *Variables*, un objet affiche un petit numéro entre accolades, par exemple `{String@812}`. Compare le numéro de `item.sku` et celui de `sku` : ce numéro identifie **l'objet**.

</details>

<details><summary>Indice 2</summary>

Pour comparer le **contenu** de deux chaînes : `equals` (chapitre 4). Remplace `==` par `.equals(…)` dans la boucle de `receive`.

</details>

---

## Étape 3 — Le point d'arrêt conditionnel (BUG 2)

<details><summary>Indice 1</summary>

Avec `item.qty` égal à 200 et `qty` égal à 200, que vaut `item.qty > qty` ? Que **devrait**-il valoir pour une expédition de tout le stock ?

</details>

<details><summary>Indice 2</summary>

Avant ta correction du BUG 1, il y avait **deux** articles `PEN` (120 et 80) : regarde lequel `ship` examine.

</details>

---

## Étape 4 — Évaluer une expression (BUG 3)

<details><summary>Indice 1</summary>

Java calcule `item.qty * item.priceCents` en `int` (les deux opérandes sont des `int`), **puis** ajoute le résultat au `long`. Le débordement a déjà eu lieu.

</details>

<details><summary>Indice 2</summary>

Convertis **un des deux opérandes** avant la multiplication : `(long) item.qty * item.priceCents`. Le cast s'applique à `item.qty` seul (il est prioritaire sur `*`, chapitre 2).

</details>

---

## Étape 5 — Surveiller des variables (BUG 4)

<details><summary>Indice 1</summary>

`sum / n` entre entiers tronque (chapitre 2). Pour arrondir au plus proche : `(sum + n / 2) / n`.

</details>

<details><summary>Indice 2</summary>

Avant le BUG 1, l'article `PEN` était compté **deux fois** dans la moyenne : regarde `n` (7 au lieu de 6).

</details>

---

## Étape 6 — Le cache des `Integer` (BUG 5)

<details><summary>Indice 1</summary>

`qty` est un `Integer`, pas un `int`. `==` entre deux `Integer` ne les déballe pas : il compare les objets.

</details>

<details><summary>Indice 2</summary>

`quantity(…)` rend un `int` : comparer deux `int` avec `==` compare les valeurs. Et un code inconnu donne 0 au lieu d'une `NullPointerException`.

</details>

---

## Étape 7 — Un bug en cache un autre (BUG 6)

<details><summary>Indice 1</summary>

Au premier tour, `i` vaut 1 : l'article d'indice 0 n'est jamais regardé.

</details>

<details><summary>Indice 2</summary>

Avec `LegacyInventory`, il y avait un **deuxième** article `PEN` (80, à l'indice 3) : c'est lui qui faisait apparaître `PEN` dans la liste.

</details>

---

## Étape 8 — Valider les entrées

<details><summary>Indice 1</summary>

Mets les deux `Integer.parseInt` et le contrôle du nombre de morceaux dans un `try`, et transforme la `NumberFormatException` en `IllegalArgumentException` dans le `catch`.

</details>

<details><summary>Indice 2</summary>

Tous les contrôles **avant** la boucle qui cherche l'article : ainsi, une ligne refusée ne touche jamais au stock.

</details>

---

## Étape 9 — Les mutants

<details><summary>Indice : ce que change chaque mutant</summary>

1. BUG 1 : `==` au lieu de `equals` dans `receive`.
2. BUG 2 : `>` au lieu de `>=` dans `ship`.
3. BUG 3 : la multiplication déborde (plus de cast `(long)`).
4. BUG 4 : la moyenne est tronquée.
5. BUG 5 : les quantités sont comparées comme des `Integer` avec `==`.
6. BUG 6 : la boucle de `lowStock` commence à 1.
7. Une quantité reçue de 0 est acceptée.
8. Une ligne avec plus de 3 morceaux est acceptée.
9. Expédier 0 est accepté.
10. La moyenne d'un inventaire vide vaut −1 au lieu de 0.

</details>
