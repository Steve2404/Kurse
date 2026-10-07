# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les libellés et les tarifs

<details><summary>Indice 1</summary>

Un ternaire imbriqué se lit comme une chaîne de « si… sinon si… sinon » : `condition1 ? valeur1 : condition2 ? valeur2 : valeur3`. La méthode tient en un seul `return`.

</details>

<details><summary>Indice 2</summary>

Les tarifs viennent de la règle 1 de l'énoncé, convertis en **centimes** : 1.50 € donne 150. Le type est un `int` (1, 2 ou 3) : le dernier « sinon » couvre le camion.

</details>

---

## Étape 2 — Le calcul d'un ticket

<details><summary>Indice 1</summary>

Une ligne par règle, dans l'ordre de l'énoncé : minutes facturables, heures, montant brut, nuit, plafond, abonné, fidélité. Chaque règle est soit un ternaire, soit une affectation composée (`+=`, `-=`) avec un ternaire à droite.

</details>

<details><summary>Indice 2</summary>

- **Nuit :** `fee += night ? fee / 2 : 0;`.
- **Abonné :** même forme avec `-=` et `fee / 5`.
- **Plafond :** deux constantes en centimes (règle 5). Un ternaire choisit la limite selon le type, un autre ramène `fee` à cette limite s'il la dépasse.
- **Fidélité :** la visite est offerte quand son numéro est un multiple de 10.

</details>

---

## Étape 3 — Le texte du ticket

<details><summary>Indice 1</summary>

Le numéro s'écrit `"#" + ++issued + …` : `issued` vaut 0 au départ, et le 1er ticket doit afficher 1. Les ternaires **au milieu** d'une concaténation doivent être entre parenthèses.

</details>

<details><summary>Indice 2</summary>

Le résultat final est un ternaire imbriqué : 0 h, puis visite offerte, puis montant. Pour l'abonné avec `!` : `(!subscriber ? "" : " | abonne")`.

</details>

---

## Étape 4 — Le `main`

<details><summary>Indice 1</summary>

Le 1er ticket vient de `args[0]` à `args[4]`, convertis avec `parseInt` et `parseBoolean`. Les 5 suivants sont des appels écrits en dur. Relis leurs valeurs dans la sortie attendue : type, minutes, nuit, abonné, numéro de visite.

</details>

<details><summary>Indice 2</summary>

Valeurs de visite qui reproduisent la sortie : 1, 4, 10, 7 et 20. Ce qui compte, c'est que la 3e et la 5e soient des multiples de 10.

</details>
