# Projet 6 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le modèle et le chemin heureux

<details><summary>Indice 1</summary>

`withStatus` construit un nouveau `Order` avec les mêmes composants, sauf le statut : `return new Order(id, customer, email, totalCents, newStatus);`.

</details>

<details><summary>Indice 2</summary>

`orders.find(orderId).orElseThrow(…)` donne la commande. Puis `gateway.charge(order.customer(), order.totalCents())`.

</details>

---

## Étape 2 — Ton premier simulacre

<details><summary>Indice 1</summary>

Le test prépare **deux** réponses avant d'agir : `when(orders.find("C-1")).thenReturn(Optional.of(commande))` et `when(gateway.charge("ana", 4250)).thenReturn(new ChargeResult(true, "T-77", null))`.

</details>

<details><summary>Indice 2</summary>

Pour vérifier l'enregistrement : `verify(orders).save(commande.withStatus(OrderStatus.PAID))`. Deux records sont égaux quand tous leurs composants le sont.

</details>

---

## Étape 3 — Vérifier l'ordre des appels

<details><summary>Indice 1</summary>

`InOrder ordre = inOrder(gateway, orders, mailer);` puis trois `ordre.verify(…)`, dans l'ordre attendu.

</details>

<details><summary>Indice 2</summary>

Avec `InOrder`, tu peux remplacer tes `verify` simples : chaque `ordre.verify(…)` vérifie **à la fois** l'appel et sa place.

</details>

---

## Étape 4 — Les refus, et l'`ArgumentCaptor`

<details><summary>Indice 1</summary>

`ArgumentCaptor<Order> enregistree = ArgumentCaptor.forClass(Order.class);` puis `verify(orders).save(enregistree.capture());` puis `enregistree.getValue().status()`.

</details>

<details><summary>Indice 2</summary>

Les contrôles « déjà payée » et « montant invalide » se placent **avant** l'appel à la banque. Le test le prouve avec `verifyNoInteractions(gateway)`.

</details>

---

## Étape 5 — Les pannes

<details><summary>Indice 1</summary>

Une boucle de 2 tentatives, avec un `try` autour de `gateway.charge` : `return` du résultat dans le `try`, rien dans le `catch` (on recommence). Après la boucle : les deux essais ont échoué.

</details>

<details><summary>Indice 2</summary>

Une petite méthode privée qui rend le `ChargeResult`, ou `null` après deux délais dépassés, garde `pay` lisible.

</details>

---

## Étape 6 — Le traitement par lot

<details><summary>Indice 1</summary>

`retryFailed` réutilise `pay` : il suffit de regarder ce qu'elle rend pour chaque commande.

</details>

<details><summary>Indice 2</summary>

Dans le test, il faut préparer `findByStatus`, **et** `find` pour chacune des trois commandes (car `pay` les relit), **et** trois réponses de la banque, une par client, avec `eq("ana")`, `eq("bob")`, `eq("cid")`.

</details>

---

## Étape 7 — Les mutants

<details><summary>Indice 1</summary>

Pour chaque survivant : quel appel (ou quelle absence d'appel), quel argument, quel ordre, quel nombre d'essais n'est vérifié par aucun test ?

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Une commande déjà payée repasse par la banque.
2. Un montant de 0 est accepté.
3. Pas de nouvelle tentative après un délai dépassé.
4. Jusqu'à 3 tentatives au lieu de 2.
5. Le paiement accepté enregistre la commande sans changer son statut.
6. Le reçu est envoyé **avant** l'enregistrement.
7. Le reçu affiche le montant divisé par 100.
8. Un refus n'enregistre pas le statut `FAILED`.
9. Le courriel de refus ne contient plus la raison.
10. Après deux délais dépassés, aucun courriel n'est envoyé.
11. `retryFailed` compte aussi les commandes refusées.
12. La banque reçoit l'adresse courriel au lieu du client.
13. `pay` rend `"OK"` au lieu du numéro de transaction.

</details>
