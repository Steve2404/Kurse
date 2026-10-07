# Projet 3 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — La hiérarchie

<details><summary>Indice 1</summary>

Chaque sous-classe a un constructeur qui commence par `super(id, name, base)`. Pour `Intern`, le calcul se fait **dans** l'appel : `super(id, name, Math.min(stipend, CAP))`.

</details>

<details><summary>Indice 2</summary>

`pay()` dans `Manager` et `Engineer` commence par `super.pay()`, le calcul du parent, puis ajoute sa part. `managerId` n'est pas `final`, puisqu'il est affecté après la construction. Un setter package-private suffit.

</details>

---

## Étape 2 — L'organigramme

<details><summary>Indice 1</summary>

`byId = new Employee[STAFF.length + 1]`. Pour chaque ligne, `split(" ")`, puis selon `p[1]` : `new Manager`, `new Engineer` (avec `p[5]`) ou `new Intern`. Range l'objet dans `byId[id]`.

</details>

<details><summary>Indice 2</summary>

- `children(id)` : un tableau temporaire, rempli des `i` dont `getManagerId() == id`, puis recopié à la bonne taille.
- `print(id, level)` : affiche la ligne de `id`, puis `print(enfant, level + 1)` pour chaque enfant.
- `cost(id)` : `pay()` + la somme des `cost(enfant)`.

</details>

---

## Étape 3 — Ancêtre commun et plus longue chaîne

<details><summary>Indice 1</summary>

`depth(id)` : 0 si `getManagerId() == 0`, sinon `1 + depth(manager)`.

</details>

<details><summary>Indice 2</summary>

- `lca` : deux `while` pour égaliser les profondeurs, puis `while (a != b)` pour monter les deux.
- La chaîne : `for (int i = deepest; i != 0; i = manager(i))`, et `chain.insert(0, …)` pour mettre le nom **devant**.

</details>

---

## Étape 4 — Masquer ou redéfinir, en direct

<details><summary>Indice 1</summary>

`if (byId[8] instanceof Manager hugo) { Employee asEmployee = hugo; … }`. Les deux variables désignent **le même objet**, avec deux types de référence différents.

</details>

<details><summary>Indice 2</summary>

Pour chaque ligne, demande-toi : ce membre est-il choisi à la **compilation** (d'après le type de la variable : champs, méthodes `static`, méthodes `private`), ou à l'**exécution** (d'après l'objet : méthodes d'instance redéfinies) ?

</details>
