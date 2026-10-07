# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : les 5 exceptions, `Account`, `StandardAccount`, `FrozenAccount`, `Bank` et `Teller`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur des classes de test réduites (`BE` pour `BankException`, `IE` pour `InsufficientFundsException`).

---

## Étape 1 — Les exceptions et les comptes

**Le code :** les classes d'exceptions, [`Account.java`](Account.java), [`StandardAccount.java`](StandardAccount.java) et [`FrozenAccount.java`](FrozenAccount.java).

**Vérifiée ou non vérifiée :**

| Exception | Parent | Doit être déclarée (`throws`) ? |
|---|---|---|
| `BankException` et ses filles | `Exception` | **oui** : vérifiée |
| `UnknownAccountException` | `RuntimeException` | non |
| `IllegalArgumentException`, `IllegalStateException` | `RuntimeException` | non |

**Le constructeur `(message, cause)`** permet le **chaînage** : l'exception d'origine reste accessible par `getCause()`. Le guichet s'en sert pour afficher la cause d'un virement échoué.

**Question — pourquoi `deposit` ne peut pas lever une `FrozenAccountException` ?** `Account.deposit` ne déclare **aucune** exception vérifiée. Une méthode qui l'implémente ne peut pas en ajouter : un appelant qui manipule un `Account` ne s'y attendrait pas. Vérifié :

```
error: deposit(long) in F cannot implement deposit(long) in A
  overridden method does not throw BE
```

D'où l'`IllegalStateException`, non vérifiée, pour le dépôt sur un compte gelé.

---

## Étape 2 — La banque

**Le code :** [`Bank.java`](Bank.java).

**`find` sans `throws`** : `UnknownAccountException` est non vérifiée. Les appelants ne sont pas obligés de l'attraper, et le guichet l'attrape quand même pour l'afficher.

**Le virement atomique :** si le dépôt échoue **après** le retrait, l'argent aurait disparu. Le second `catch` rembourse la source (une **compensation**) avant de lever. Le solde de B2 est donc intact après `VIREMENT B2 C3 5000`.

**Envelopper la cause :** `throw new TransferException("…", e)` transforme une exception technique en exception **métier**, sans perdre l'information d'origine.

**`share` avec 0 part :** `balance / 0` sur des `long` lève une `ArithmeticException` (`/ by zero`). Sur des `double`, on obtiendrait `Infinity`, sans exception.

---

## Étape 3 — Le guichet

**Le code :** `execute` et `main` de [`Teller.java`](Teller.java).

**Les sept `catch`, du plus précis au plus général :** `InsufficientFundsException` avant `TransferException` avant `BankException` (toutes trois filles de `BankException`), puis le multi-catch, puis `IllegalArgumentException`, `UnknownAccountException`, et enfin `RuntimeException` comme filet.

**Quelques résultats expliqués :**
- **`DEPOT A1 12x`** : `Long.parseLong("12x")` lève une `NumberFormatException`, et on obtient `MAL FORMEE`.
- **`DEPOT A1 -5`** : l'`IllegalArgumentException` de `deposit` donne `INVALIDE`.
- **`RETRAIT`** seul : `p[1]` n'existe pas, d'où `ArrayIndexOutOfBoundsException` et `MAL FORMEE`.
- **`PARTAGE A1 0`** : l'`ArithmeticException` n'a pas de `catch` dédié et tombe dans le filet `RuntimeException`. Son `toString()` donne `java.lang.ArithmeticException: / by zero`.

**Le `finally`** s'exécute **dans tous les cas**, exception ou non. Le compteur atteint donc 13.

**Questions :**
- **`catch (BankException e)` avant `InsufficientFundsException`** :

  ```
  error: exception IE has already been caught
  ```

  Le `catch` de la mère attrape déjà toutes ses filles, et celui de la fille serait inatteignable. `javac` le refuse.
- **`catch (IllegalArgumentException e)` avant le multi-catch** : même erreur, pour `NumberFormatException` (vérifié : `exception NumberFormatException has already been caught`). `NumberFormatException` est une **fille** d'`IllegalArgumentException`.

**Expérience — `catch (NumberFormatException | IllegalArgumentException e)` :**

```
error: Alternatives in a multi-catch statement cannot be related by subclassing
  Alternative NumberFormatException is a subclass of alternative IllegalArgumentException
```

Dans un multi-catch, les types doivent être **sans lien** d'héritage : la mère suffirait.

---

## Étape 4 — Les frais (exception vérifiée dans une lambda)

**Le code :** la fin du `main`.

**Question — pourquoi la lambda doit attraper elle-même ?** `forEach` attend un `Consumer`, dont la méthode `accept` **ne déclare aucune** exception vérifiée. Une lambda qui laisserait sortir une `BankException` ne respecterait pas cette signature. Vérifié :

```
error: unreported exception BE; must be caught or declared to be thrown
```

**Les impayés :**
- C3 est gelé : `withdraw` lève une `FrozenAccountException`, attrapée par le `catch (BankException e)`.
- D4 n'a que 8,00 € : il manque 2,00 €.

**Expériences :**
- **`withdraw(long) throws Exception`** dans `StandardAccount` :

  ```
  error: withdraw(long) in S cannot implement withdraw(long) in A
    overridden method does not throw Exception
  ```

  `Exception` est **plus large** que `BankException`.
- **`deposit(long) throws BankException`** : même règle qu'à l'étape 1, avec le message `overridden method does not throw …`. L'interface ne déclare rien pour `deposit`.
