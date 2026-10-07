# Projet 7 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`Tariff.java`](Tariff.java), [`BikeService.java`](BikeService.java), [`Rebalancer.java`](Rebalancer.java) et [`BikeApp.java`](BikeApp.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **H2 2.3.232**, sur la solution et sur des copies modifiées.

---

## Étape 1 — L'installation et le tarif

**Question — pourquoi les stations avant les vélos ?** Chaque vélo **référence** une station (clé étrangère). Si les vélos partaient d'abord, la base refuserait des vélos qui pointent vers des stations qui n'existent pas encore.

---

## Étape 2 — Le service : une commande = une transaction

**Le motif :** chaque commande passe par `transaction(() -> { … })`, qui fait `commit()` si tout va bien et `rollback()` sur une `SQLException`. Une erreur **métier** (`45000`) est une `SQLException` comme une autre : elle annule aussi toute la commande.

---

## Étape 3 — Rendre, payer, rembourser

**Question — pour Ben, quelle ligne garantit que le vélo est rendu ?** Le `Savepoint payment = conn.setSavepoint("paiement")`, posé **après** les `UPDATE` du vélo et de la location. `rollback(payment)` ne défait que ce qui suit : le débit. Le retour du vélo, fait avant, reste acquis, et il est validé au `commit()` de `transaction`.

**Question — pourquoi `rendre Ana S2 20 3` ne change-t-il rien ?** La place libre est vérifiée **avant** toute modification. La station est pleine : `refuse(...)` lève l'erreur métier, et `transaction` fait `rollback()`. Aucun `UPDATE` n'avait encore eu lieu, et rien n'est validé.

**Expérience 1 — `rendre Ben S1 50 1` juste après `louer Ben B3`** (vérifié) : `ok, B3 a S1, 50 min, 100 cts`. 50 minutes coûtent 100 centimes, et Ben a 100 de crédit : il paie tout, **sans** dette. La suite du scénario change : `rendre Ben S2 130 30` devient `aucune location en cours`.

**Expérience 2 — sans `rollback(payment)`** (vérifié) : la sortie **ne change pas**. Quand un ordre échoue (ici l'`UPDATE` qui rendrait le crédit négatif), la base l'annule **seul** : le crédit n'a pas bougé. Un `Savepoint` devient indispensable quand **plusieurs** ordres doivent être défaits ensemble.

**Expérience 3 — `int intOf` au lieu de `Integer intOf`** (vérifié) : le programme s'arrête à **`louer Zoe B2`** :

```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "java.lang.Integer.intValue()"
```

`Zoe` n'existe pas : `intOf` veut rendre `null`, qui ne peut pas devenir un `int` (le déballage plante, chapitre 5). Et comme une `NullPointerException` n'est **pas** une `SQLException`, elle échappe au `catch` de `transaction`, et arrête tout le programme.

---

## Étape 4 — Le bilan et le rééquilibrage

**Question — pourquoi l'`OFFSET` ?** Les déplacements sont mis **en lot**, et le lot n'est envoyé qu'à la fin. Tant qu'il n'est pas envoyé, la base ne sait pas qu'un vélo a été choisi : sans `OFFSET`, `pick` rendrait **le même** vélo (le moins usé) à chaque mouvement d'une même station. L'`OFFSET` saute ceux déjà choisis.

**Question — qu'une 2e connexion ne voie jamais un rééquilibrage à moitié fait ?** C'est **déjà** le cas : la connexion a l'auto-commit coupé (par le constructeur de `BikeService`), et tous les déplacements sont validés par **un seul** `commit()` à la fin. Une autre connexion voit l'état d'avant, puis l'état d'après, jamais un état intermédiaire. Avec l'auto-commit actif, chaque `UPDATE` du lot serait validé séparément, et visible tout de suite.
