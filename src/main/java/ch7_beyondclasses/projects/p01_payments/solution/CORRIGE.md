# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : les interfaces `PaymentMethod`, `Refundable`, `Traceable` et `SecurePayment`, les classes `CreditCard`, `BankTransfer` et `Voucher`, et `PaymentsApp`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur des types de test réduits (`P`, `R`, `S`, `T`, `C`).

---

## Étape 1 — Les interfaces

**Le code :** [`PaymentMethod.java`](PaymentMethod.java), [`Refundable.java`](Refundable.java), [`Traceable.java`](Traceable.java) et [`SecurePayment.java`](SecurePayment.java).

**Les 5 sortes de membres d'une interface (Java 17) :**

| Membre | Modificateurs implicites | Exemple |
|---|---|---|
| champ | `public static final` | `MAX_CENTS` |
| méthode sans corps | `public abstract` | `label()` |
| `default` | `public` | `pay(…)`, `policy()` |
| `static` | `public` (sauf si `private`) | `money(…)`, `ibanValid(…)` |
| `private` / `private static` | — | `line(…)`, `value(…)` |

**Pourquoi l'IBAN chiffre par chiffre ?** Le nombre obtenu en remplaçant les lettres a une trentaine de chiffres : il ne tient pas dans un `long`. Mais (a × 10 + b) mod 97 = ((a mod 97) × 10 + b) mod 97. On peut donc garder seulement le **reste** à chaque étape. Une lettre vaut 2 chiffres (10 à 35), d'où `rest * 100`.

**Luhn :** depuis la droite, chaque 2e chiffre est doublé. Retirer 9 revient à additionner les deux chiffres du double (16 donne 1 + 6 = 7 = 16 − 9).

**`SecurePayment` redéfinit un `default`** de `Refundable`. Une sous-interface peut le faire. `Refundable.super.policy()` appelle la version du parent : la syntaxe `Interface.super.méthode()` ne marche que vers une super-interface **directe**.

---

## Étape 2 — Les classes

**Le code :** [`CreditCard.java`](CreditCard.java), [`BankTransfer.java`](BankTransfer.java) et [`Voucher.java`](Voucher.java).

**Le losange de `CreditCard` :** `SecurePayment.policy()` et `Traceable.policy()` sont deux `default` **sans lien** entre elles. Java ne choisit pas au hasard : la classe **doit** redéfinir `policy()`. Elle peut réutiliser les deux versions avec `X.super.policy()`.

**`BankTransfer` n'a pas ce problème :** il hérite de `policy()` par une **seule** interface (`Refundable`).

**`Voucher` redéfinit un `default`** (`pay`) et réutilise l'original avec `PaymentMethod.super.pay(amount)`. Le texte « OK … » reste construit au même endroit.

**`Voucher` a un état modifiable** (`balance`). Une interface ne peut pas avoir de champ d'instance : tout état vit dans les classes.

---

## Étape 3 — Le rapport

**Le code :** [`PaymentsApp.java`](PaymentsApp.java).

**Le type de la référence limite les appels :** `m` est un `PaymentMethod`, qui ne connaît ni `policy()` ni `refund()`. `m instanceof Refundable r` teste l'objet **et** fournit une variable du bon type. C'est le même objet, vu à travers une autre interface.

**Les résultats :**
- **Bob** : son numéro diffère d'Alice au dernier chiffre, donc Luhn échoue : `REFUSE (invalide)`. Il n'a pas de ligne « rembourse » (pas valide), mais garde la ligne 3-D Secure, puisque c'est un `SecurePayment`.
- **Fanny** : sa carte est valide, mais 6000.00 dépasse le plafond, d'où `REFUSE (plafond 5000.00)`.
- **3 paiements acceptés sur 7.**

**Le bon d'achat :** `pay(1200)` passe (solde 30.00, reste 18.00). `pay(2000)` est refusé, car 20.00 > 18.00. L'objet a **gardé son état** entre les deux appels.

**Expériences :**

| Expérience | Erreur de `javac` |
|---|---|
| retirer `policy()` de `CreditCard` | `types S and T are incompatible;` `class C inherits unrelated defaults for policy() from types S and T` |
| `new CreditCard(…).money(5)` | `cannot find symbol` `symbol: method money(int)` |
| implémenter `label()` sans `public` | `label() in C cannot implement label() in P` `attempting to assign weaker access privileges; was public` |
| `Refundable.super.policy()` dans `CreditCard` | `not an enclosing class: R` |

**Pourquoi `money` ne s'appelle pas via un objet ?** Une méthode `static` d'interface **n'est pas héritée** par les classes qui l'implémentent, contrairement aux méthodes static d'une classe. Elle appartient à l'interface seule, et on l'appelle `PaymentMethod.money(5)`. `javac` ne la trouve même pas sur `CreditCard`.

**`Refundable.super.policy()` refusé dans `CreditCard` :** `CreditCard` implémente `SecurePayment` et `Traceable`. `Refundable` n'est qu'une super-interface **indirecte** (via `SecurePayment`). `X.super` n'est permis que pour une super-interface **directe**. Le message de `javac` est trompeur (`not an enclosing class`), car il interprète `Refundable.super` comme la syntaxe des classes internes.
