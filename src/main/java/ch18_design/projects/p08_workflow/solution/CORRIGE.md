# Projet 8 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`WorkflowTest.java`](WorkflowTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** et **JUnit 5.11.4**.

---

## Étape 1 — Comparer le legacy au tableau

La sortie de `Data` (vérifiée) :

```
payee, expediee, puis annulee : CANCELLED, rembourse 3400
payee deux fois : PAID, paiements 2
remboursee sans avoir ete payee : REFUNDED, rembourse 3400
```

**Question — les cases non respectées :**
1. **expédiée + annuler** : le tableau refuse (le colis roule), le legacy annule **et** rembourse 34,00 alors que le client recevra quand même le colis ;
2. **payée + payer** : le tableau refuse, le legacy encaisse une **deuxième** fois ;
3. **nouvelle + rembourser** : le tableau refuse, le legacy rembourse 34,00 qui n'ont **jamais** été payés. Et il ne vérifie aucun délai.

**Question — les refus :** le legacy ne refuse jamais : une action interdite ne fait **rien**, en silence (aucune exception, aucun `boolean`). L'appelant ne peut pas savoir que son action a été ignorée, sauf à relire l'état après chaque appel.

**Question — un état de plus :** **toutes** les méthodes doivent être relues (les cinq), puisque chacune teste l'état avec ses propres `if`, et la plupart modifiées (`ship` doit partir du nouvel état, `cancel` doit décider s'il rembourse…). Les règles d'**un** état sont éparpillées dans **cinq** méthodes.

---

## Étape 2 — Les tests d'abord

Le test : `transitionTable` dans [`WorkflowTest.java`](WorkflowTest.java), 30 cas.

**Question — le chemin normal :** parce qu'il n'existe **pas** d'autre moyen sûr : une commande `livree` doit avoir une **date de livraison** (sinon le remboursement plante), une commande `payee` doit pouvoir être remboursée de son montant… Un raccourci (un constructeur qui accepte l'état voulu) fabriquerait des commandes **impossibles** dans la vraie vie, et les tests vérifieraient des situations qui n'arrivent jamais. Le chemin normal teste aussi, au passage, les transitions qui y mènent.

---

## Étape 3 — Le patron État

Le code : [`OrderState.java`](OrderState.java), [`NewState.java`](NewState.java), [`PaidState.java`](PaidState.java), [`ShippedState.java`](ShippedState.java), [`DeliveredState.java`](DeliveredState.java), [`ClosedState.java`](ClosedState.java), [`Order.java`](Order.java), [`Transition.java`](Transition.java). Les tests : `aRefusalChangesNothing`, `refunds`, `refundUntilTheFourteenthDayIncluded`, `refundOnTheFifteenthDayIsRefused`, `theDelayStartsAtDelivery`, `transitionsAreRecordedAndProtected`.

**Question — l'état « en préparation » :** on **ajoute** `PreparingState` (qui accepte `ship` vers `ShippedState`, et `cancel` avec remboursement), et l'on change **une** ligne : `PaidState.ship` rend `new PreparingState()` au lieu de `new ShippedState()`. `Order` ne change **pas** : il délègue sans savoir quels états existent. Comparé au legacy (cinq méthodes à reprendre), le changement est **local** : les règles d'un état sont toutes dans **sa** classe.

**Question — les méthodes du paquet :** ce sont des **portes de service** pour les états, pas des actions métier. Si `recordRefund` était publique, n'importe quel code pourrait rembourser une commande **sans passer par la machine à états**, donc sans vérifier le tableau : on contournerait tout ce qu'on vient de construire. La visibilité du paquet (chapitre 5) les réserve aux classes d'état, rangées dans le même paquet.

---

## Étape 4 — La méthode modèle

Le code : [`ReceiptExporter.java`](ReceiptExporter.java), [`TextReceipt.java`](TextReceipt.java), [`CsvReceipt.java`](CsvReceipt.java). Le reçu texte d'une commande payée puis annulée (vérifié) :

```
Commande CMD-1
  nouvelle -> payee
  payee -> annulee
Etat final : annulee, rembourse : 3400 centimes
```

**Question — `final` :** pour **garantir le squelette**. Sans `final`, une sous-classe pourrait redéfinir `export` entier, oublier le pied ou changer l'ordre, et l'on aurait de nouveau des exports différents avec du code recopié : exactement ce que la méthode modèle voulait éviter. Avec `final`, une sous-classe ne peut changer **que** les étapes prévues, et le compilateur le vérifie. C'est le **principe d'Hollywood** : « ne nous appelez pas, nous vous appellerons » : la classe mère appelle les étapes de la sous-classe, jamais l'inverse.

---

## Étape 5 — Les mutants

Les 17 mutants sont tués par les 38 tests de référence (vérifié). Le mutant 10 est instructif : il remplace le refus par défaut de `refund` par `return this`, c'est-à-dire par le **comportement du legacy** (ignorer en silence). Seul un test qui attend une **exception** le voit : un test qui vérifierait seulement « l'état n'a pas changé » passerait aussi avec le mutant.
