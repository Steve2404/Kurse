# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — `Money` et les fabriques

<details><summary>Indice 1</summary>

`format` : le signe à part (`cents < 0 ? "-" : ""`), puis travaille sur `Math.abs(cents)`. On a les euros avec `/ 100` et les centimes avec `% 100`, complétés d'un `"0"` s'ils sont sous 10.

</details>

<details><summary>Indice 2</summary>

`open` : `Account a = new Account(); a.init(owner, cents); return a;`. Dans `init`, `id = nextId++; opened++;`, et l'historique reçoit `"ouverture " + format(cents)`. `openPremium` fait la même chose avec `new PremiumAccount()`.

</details>

---

## Étape 2 — Le grand livre : la seule porte

<details><summary>Indice 1</summary>

`withdraw` : si `balance - cents < -overdraft`, rends `false` sans rien toucher. Sinon, débite, incrémente la série, note dans l'historique et rends `true`.

</details>

<details><summary>Indice 2</summary>

- `execute` : `command.split(" ")`, puis un `switch (p[0])`, avec un `case` par commande. Un compteur `operations++` en tête de méthode.
- `find(id)` : une méthode `private` qui parcourt les comptes et rend `null` si l'id est inconnu.
- Virement : trouve **les deux** comptes, refuse si l'un est `null`, puis `withdraw` et seulement ensuite `deposit`.

</details>

---

## Étape 3 — Intérêts, relevés, classement

<details><summary>Indice 1</summary>

Taux : `b < 0 ? 150 : estPremium ? 25 : 10`. Intérêt : `Math.round(b * rate / 10_000.0)`. Ajoute-le avec `applyInterest`, qui l'inscrit aussi dans l'historique.

</details>

<details><summary>Indice 2</summary>

- `statement` : `"releve " + id + " " + owner + " (" + kind + ", solde " + … + ") :"`, puis chaque ligne d'historique, séparée par `" | "`.
- `ranking` : `System.arraycopy` des `count` comptes dans un nouveau tableau, puis un tri par sélection sur `getBalance()`, en ordre décroissant.

</details>
