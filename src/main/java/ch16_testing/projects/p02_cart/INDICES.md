# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le panier, et un panier neuf pour chaque test

<details><summary>Indice 1</summary>

Une `Map` du code article vers « quantité et prix unitaire » (un petit `record` privé fait très bien l'affaire). Laquelle des `Map` du chapitre 9 garde ses clés **triées** ?

</details>

<details><summary>Indice 2</summary>

`lines()` parcourt la map triée et construit chaque texte : `sku + " x " + quantite + " = " + quantite * prix`.

</details>

---

## Étape 2 — Vérifier plusieurs choses à la fois

<details><summary>Indice 1</summary>

`assertAll(() -> …, () -> …, () -> …)` : chaque vérification est une lambda **sans argument**, séparée des autres par une virgule.

</details>

<details><summary>Indice 2</summary>

Le sous-total des trois articles : 2 × 120 + 3 × 50 + 1 × 99. Fais le calcul sur papier, puis écris le nombre dans le test.

</details>

---

## Étape 3 — Ranger les tests par situation

<details><summary>Indice 1</summary>

Une classe `@Nested` est une classe interne **non `static`**, dans `CartTest`, avec ses propres `@Test`. Son `@BeforeEach` s'exécute après celui de `CartTest` : le panier est déjà créé.

</details>

<details><summary>Indice 2</summary>

`@DisplayName` se place sur la classe extérieure, sur chaque classe `@Nested` et sur des méthodes `@Test`, juste au-dessus ou en dessous de `@Test`.

</details>

---

## Étape 4 — Refuser, sans rien abîmer

<details><summary>Indice 1</summary>

Calcule d'abord la **nouvelle** quantité dans une variable locale, contrôle-la, et seulement ensuite fais le `put` dans la map.

</details>

<details><summary>Indice 2</summary>

Pour le test de l'invariant : `assertThrows(…)` d'abord, puis `assertEquals(99, cart.quantity("APPLE"))` sur l'état d'après.

</details>

---

## Étape 5 — Les codes promo et les frais de port

<details><summary>Indice 1</summary>

Ne stocke pas la remise : stocke seulement le **code**. `discountCents()` et `shippingCents()` recalculent tout à partir du panier, à chaque appel.

</details>

<details><summary>Indice 2</summary>

Pour tester 2999 puis 3000 dans un même test : ajoute un article à 2999, vérifie, retire-le, ajoute un article (de même code) à 3000, vérifie.

</details>

---

## Étape 6 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quel seuil, quel message, quel ordre, quel état après un refus n'est pas vérifié par tes tests ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Le maximum devient 98 (`>= 99` au lieu de `> 99`).
2. Un autre prix pour le même article n'est plus refusé.
3. La livraison gratuite commence au-dessus de 3000, pas à 3000.
4. La remise commence au-dessus de 5000, pas à 5000.
5. La remise est tronquée au lieu d'être arrondie.
6. Les lignes restent dans l'ordre d'ajout au lieu d'être triées.
7. `lines()` rend une liste modifiable.
8. Retirer exactement la quantité présente laisse une ligne à 0.
9. Un panier vide paie la livraison.
10. On peut appliquer un 2e code.
11. Le code `LIVRAISON` n'offre plus le port.
12. Les quantités d'un même article ne s'additionnent plus.
13. Un article gratuit (prix 0) est refusé.
14. La livraison coûte 500 au lieu de 490.

</details>
