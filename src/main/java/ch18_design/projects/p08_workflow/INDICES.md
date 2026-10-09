# Projet 8 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Comparer le legacy au tableau

<details><summary>Indice 1</summary>

Pour chaque ligne de la sortie, suis les `if` de la méthode appelée en dernier, avec l'état de départ : quelle branche est prise ?

</details>

<details><summary>Indice 2</summary>

Une action interdite dans le legacy ne fait… rien, en silence : aucune exception, aucun `boolean` rendu.

</details>

---

## Étape 2 — Les tests d'abord

<details><summary>Indice 1</summary>

`private static Consumer<Order> action(String name) { return switch (name) { case "payer" -> Order::pay; … default -> Order::refund; }; }`. Puis `action(actionName).accept(order)`.

</details>

<details><summary>Indice 2</summary>

Dans le test paramétré : `if (expected.equals("refus"))`, un `assertThrows` (avec le message, puis l'état inchangé) ; sinon, l'action puis l'état d'arrivée. Un nom lisible pour chaque cas : `@ParameterizedTest(name = "{0} + {1} -> {2}")`.

</details>

---

## Étape 3 — Le patron État

<details><summary>Indice 1</summary>

Une méthode `default` qui refuse : `default OrderState pay(Order order) { throw order.refused("payer"); }`. `refused` **rend** l'exception (avec l'état courant dans le message), et c'est la méthode `default` qui la lance.

</details>

<details><summary>Indice 2</summary>

Le délai : `LocalDate limit = order.deliveredOn().plusDays(14);` puis refuser si `order.today().isAfter(limit)`. `isAfter` est faux le jour même de la limite : le 14e jour est donc accepté.

</details>

---

## Étape 4 — La méthode modèle

<details><summary>Indice 1</summary>

`export` : `StringBuilder out = new StringBuilder(header(order));`, puis `order.transitions().forEach(t -> out.append(line(order, t)));`, puis `out.append(footer(order))`.

</details>

<details><summary>Indice 2</summary>

`TextReceipt.line` peut s'appuyer sur `Transition.toString()` : `"  " + transition + "\n"`.

</details>

---

## Étape 5 — Les mutants

<details><summary>Indice 1</summary>

Ton test paramétré sur les 30 cases tue à lui seul la plupart des mutants d'état. Les autres touchent les remboursements, les dates et les reçus.

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Payer une commande nouvelle la fait passer directement à `expediee`.
2. Annuler une commande nouvelle la marque `remboursee`.
3. Annuler une commande payée ne rembourse plus rien.
4. Expédier une commande payée la marque directement `livree`.
5. On peut annuler une commande expédiée.
6. La livraison ne note plus sa date.
7. Le remboursement le 14e jour pile est refusé.
8. Le remboursement après livraison ne rend que la moitié.
9. Une commande remboursée ne se dit plus fermée.
10. Rembourser n'est plus refusé par défaut : l'action est ignorée en silence (comme le legacy).
11. Les transitions notent le nouvel état des deux côtés.
12. `transitions()` rend la liste elle-même, modifiable.
13. Le message de refus ne dit plus l'état de la commande.
14. La date de livraison notée est celle de la veille.
15. Le pied du reçu n'est plus ajouté.
16. Une ligne du reçu texte ne montre plus que l'état d'arrivée.
17. L'en-tête du CSV inverse les colonnes.

</details>
