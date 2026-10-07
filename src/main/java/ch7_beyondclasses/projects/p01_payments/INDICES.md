# Projet 1 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les interfaces

<details><summary>Indice 1</summary>

Dans une interface, une méthode sans corps est `public abstract`, et un champ est `public static final`, sans rien écrire. Une méthode `default` a un corps. Une méthode `private` (Java 9+) aussi, mais elle n'est visible que dans l'interface.

</details>

<details><summary>Indice 2</summary>

- IBAN : `moved = iban.substring(4) + iban.substring(0, 4)`, puis une boucle avec `rest`.
- Luhn : `d = digits.charAt(length - 1 - i) - '0'`. Si `i % 2 == 1`, double et retire 9 au-delà de 9.
- `luhnCheckDigit` : essaie d de 0 à 9 avec `luhnValid(partial + d)`.
- `Refundable.super.policy()` appelle la version `default` de `Refundable`.

</details>

---

## Étape 2 — Les classes

<details><summary>Indice 1</summary>

Toute méthode qui implémente une méthode d'interface doit être **`public`**. `CreditCard.isValid()` : `PaymentMethod.luhnValid(number)`. Une méthode static d'interface s'appelle **par le nom de l'interface**.

</details>

<details><summary>Indice 2</summary>

- `Voucher.pay` : d'abord le cas « solde insuffisant ». Puis `String result = PaymentMethod.super.pay(amount);`, puis le débit si le bon est valide, puis `result + ", reste " + …`.
- `checkLetter` : somme des `charAt(i)` (des `char`, donc des nombres) hors `'-'`, puis `(char) ('A' + sum % 26)`.

</details>

---

## Étape 3 — Le rapport

<details><summary>Indice 1</summary>

Le tableau est `PaymentMethod[]` : on ne peut appeler que les méthodes de `PaymentMethod`. Pour `policy()` et `refund()`, il faut `if (m.isValid() && m instanceof Refundable r)`, puis utiliser `r`.

</details>

<details><summary>Indice 2</summary>

Les frais ne se cumulent que si le moyen est valide **et** si le montant ne dépasse pas `MAX_CENTS`. C'est le même test que dans `pay`, réécrit dans le `main`.

</details>
