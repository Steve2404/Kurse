# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code complet est dans [`PaymentService.java`](PaymentService.java) et les petits types à côté ; les tests de référence dans [`PaymentServiceTest.java`](PaymentServiceTest.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), **JUnit 5.11.4** et **Mockito 5.14.2**, sur la solution et sur de petits programmes d'essai.

---

## Étape 1 — Le modèle et le chemin heureux

**Le code :** les sept petits fichiers (`Order`, `OrderStatus`, `ChargeResult`, `GatewayTimeoutException`, `PaymentGateway`, `OrderRepository`, `Mailer`) et le début de `pay`.

---

## Étape 2 — Ton premier simulacre

**Le code :** les champs `@Mock`, `@InjectMocks`, et le test `approvedPaymentSavesPaidOrderThenSendsAReceipt`.

**Les expériences** (vérifiées) :
1. Sans rien préparer, `pay("C-1")` lance `NoSuchElementException` (« commande inconnue : C-1 ») : le simulacre de `find`, non préparé, rend **`Optional.empty()`**. Les deux assertions sont **vertes** : `orders.find("Z")` rend `Optional.empty()`, `findByStatus` rendrait une liste vide, et `gateway.charge` rend **`null`** (un objet quelconque). Un simulacre rend toujours « vide », jamais une exception.
2. `UnnecessaryStubbingException` (`Unnecessary stubbings detected.`). Avec `MockitoExtension`, Mockito est **strict** : une réponse préparée mais jamais utilisée fait échouer le test. Ce n'est pas du zèle : un stub inutile signale souvent un test qui ne vérifie pas ce qu'il croit vérifier.

---

## Étape 3 — Vérifier l'ordre des appels

**Le code :** le `InOrder` du test `approvedPaymentSavesPaidOrderThenSendsAReceipt`.

**Les expériences** (vérifiées) :
1. `ArgumentsAreDifferent` : `Argument(s) are different! Wanted:` suivi de l'appel attendu, puis `Actual invocations have different arguments at position [2]:` suivi de l'appel réel. Mockito dit **quel** argument diffère (le 3e, numéroté à partir de 0).
2. `VerificationInOrderFailure` : `Verification in order failure`, puis `Wanted but not invoked: orders.save(<any>);` suivi de `Wanted anywhere AFTER following interaction:` et du courriel.

**Question — dépôt puis courriel :** le courriel **promet** au client que sa commande est payée. Si on l'envoyait avant l'enregistrement et que l'enregistrement échouait (base en panne), le client aurait un reçu pour une commande que la boutique croit impayée : elle la lui refacturerait. On n'annonce que ce qui est déjà vrai.

---

## Étape 4 — Les refus, et l'`ArgumentCaptor`

**Le code :** les contrôles et le refus dans `pay` ; les tests `declinedPaymentMarksTheOrderFailedAndTellsWhy`, `alreadyPaidOrderNeverReachesTheBank`, `zeroAmountIsRefusedBeforeTheBank`, `oneCentIsEnough` et `unknownOrder`.

**L'expérience** (vérifiée) : `InvalidUseOfMatchersException`, avec `2 matchers expected, 1 recorded:`. Mockito explique lui-même la règle : soit tous les arguments sont des matchers, soit aucun ; il faut écrire `charge(anyString(), eq(4250L))`.

---

## Étape 5 — Les pannes

**Le code :** `chargeWithOneRetry` et le cas « deux délais » de `pay` ; les tests `oneTimeoutIsRetriedOnce` et `twoTimeoutsGiveUpWithoutAThirdTry`.

**Question — pourquoi `times(2)` ?** Le résultat `"INDISPONIBLE"` serait le même avec **3** essais, ou 10. Seul le nombre d'appels à la banque distingue « une seule nouvelle tentative » de « on insiste ». Pour la banque, ce n'est pas un détail : chaque essai peut coûter, ou aggraver sa panne. C'est le mutant 4. (Le mutant 3, qui ne retente jamais, est plus facile : le test « un délai » le voit déjà par son résultat, `INDISPONIBLE` au lieu de `T-78`.)

---

## Étape 6 — Le traitement par lot

**Le code :** `retryFailed` et le test `retryFailedCountsOnlyTheOrdersFinallyPaid`.

---

## Étape 7 — Les mutants

Tous les mutants sont tués par les tests de référence (vérifié).

---

## Expériences de fin de projet

1. Avec le courriel envoyé avant l'enregistrement (vérifié), le test du paiement accepté échoue avec `VerificationInOrderFailure` : sans `InOrder`, il serait resté vert.
2. Sans `@ExtendWith(MockitoExtension.class)` (vérifié), personne ne crée les simulacres : les champs `@Mock` valent **`null`**, et `@InjectMocks` n'est pas traité. **Tous** les tests échouent avec une `NullPointerException` : `Cannot invoke "….OrderRepository.find(String)" because "this.orders" is null`. (Dans le rapport de `Check`, le paquet s'appelle `checkrun.r1` : c'est le paquet temporaire où `Check` recopie ton code.)
