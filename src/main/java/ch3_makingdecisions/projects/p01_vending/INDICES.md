# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — La boucle de commandes

<details><summary>Indice 1</summary>

`for (int i = 0; i < args.length; i++)` puis `switch (args[i])`. Pour une commande qui a un paramètre, l'appel lit `args[++i]` : l'indice avance d'une case, et la boucle ne repassera pas sur ce paramètre.

</details>

<details><summary>Indice 2</summary>

Un `switch` en flèche (`case "PIECE" -> …;`) évite les `break`. Les crédits et les stocks sont des champs `static`, pour que toutes les méthodes les partagent.

</details>

---

## Étape 2 — Les pièces

<details><summary>Indice 1</summary>

`case 10, 20, 50, 100, 200 -> { … }` : plusieurs valeurs séparées par des virgules dans un seul `case`.

</details>

<details><summary>Indice 2</summary>

Une pièce acceptée affiche son montant **en euros** (`euros(coin)`) ; une pièce refusée affiche la valeur **brute** (`30`). Reprends la méthode `euros` du chapitre 1.

</details>

---

## Étape 3 — Le catalogue : des `switch` expressions

<details><summary>Indice 1</summary>

`return switch (code) { case "A1" -> 120; case "B2" -> 150; case "C3" -> { … yield …; } default -> -1; };`. Le `-1` signale un produit inconnu.

</details>

<details><summary>Indice 2</summary>

Dans le `switch` classique, chaque `case "A1":` se termine par `stockA1--; break;`. Le `default:` peut retirer un C3 : on n'y arrive qu'avec un code déjà validé.

</details>

---

## Étape 4 — L'achat

<details><summary>Indice 1</summary>

Calcule d'abord `int price = price(code);`. Puis enchaîne : `if (price < 0)`, `else if (stock(code) == 0)`, `else if (credit < price)`, `else` (on sert).

</details>

<details><summary>Indice 2</summary>

Servir, c'est trois actions dans l'ordre : retirer le prix du crédit, retirer le produit du stock, puis afficher. Le montant manquant vaut `price - credit`.

</details>

---

## Étape 5 — Le rendu de monnaie : l'algorithme glouton

<details><summary>Indice 1</summary>

Deux variables locales : la pièce courante (au départ 200) et le texte des pièces rendues. Condition du `while` : il reste du crédit **et** la pièce courante n'est pas 0.

</details>

<details><summary>Indice 2</summary>

Dans la boucle : si `credit >= coin`, rends cette pièce (et ne change pas de pièce, elle peut resservir). Sinon, `coin = switch (coin) { case 200 -> 100; … default -> 0; };`.

</details>
