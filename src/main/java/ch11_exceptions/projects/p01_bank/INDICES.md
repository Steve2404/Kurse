# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les exceptions et les comptes

<details><summary>Indice 1</summary>

Une exception vérifiée hérite d'`Exception`, une non vérifiée de `RuntimeException`. Chaque constructeur appelle `super(message)` ou `super(message, cause)`.

</details>

<details><summary>Indice 2</summary>

Une méthode qui redéfinit peut déclarer **moins** d'exceptions vérifiées, ou des exceptions **plus précises**, jamais de nouvelles ni de plus larges. `withdraw(...) throws InsufficientFundsException` est donc permis, puisqu'elle est une sous-classe de `BankException`.

</details>

---

## Étape 2 — La banque

<details><summary>Indice 1</summary>

`find` : `Account a = accounts.get(id); if (a == null) throw new UnknownAccountException(id); return a;`. Aucun `throws` n'est nécessaire.

</details>

<details><summary>Indice 2</summary>

`transfer` : deux blocs `try` séparés. Le premier enveloppe le retrait (`catch (BankException e)`), le second le dépôt (`catch (IllegalStateException e)`), qui rembourse avant de lever. Chaque `throw` passe `e` comme **cause**.

</details>

---

## Étape 3 — Le guichet

<details><summary>Indice 1</summary>

Les `catch` vont du **plus précis** au **plus général**. Un `catch` d'une classe mère placé avant celui d'une classe fille rendrait ce dernier inatteignable.

</details>

<details><summary>Indice 2</summary>

Mémorise l'exception attrapée dans une variable (`Exception failure`) pour compter les erreurs **après** le `try`. Le `finally` ne fait que `processed++`.

</details>

---

## Étape 4 — Les frais (exception vérifiée dans une lambda)

<details><summary>Indice 1</summary>

Le `try`/`catch` s'écrit **dans** le corps de la lambda : `a -> { try { a.withdraw(FEE); } catch (…) { … } }`.

</details>

<details><summary>Indice 2</summary>

La liste des impayés est déclarée avant la lambda : elle est effectively final, et la lambda la **modifie** par `add`.

</details>
