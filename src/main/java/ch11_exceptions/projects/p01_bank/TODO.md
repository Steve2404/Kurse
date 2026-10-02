# Projet 1 — Le guichet de banque (lancer, attraper, déclarer)

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 11) :**
- **la hiérarchie :** `Throwable` → `Exception` (**vérifiées**) / `RuntimeException` (**non vérifiées**) / `Error` ;
- **écrire ses exceptions :** une vérifiée (`extends Exception`), une non vérifiée (`extends RuntimeException`), une sous-hiérarchie, une exception qui **transporte une donnée**, les constructeurs `(message)` et `(message, cause)` ;
- **`throw`** contre **`throws`** ;
- **redéfinir une méthode qui déclare une exception :** l'implémentation peut déclarer **plus précis**, ou rien — jamais plus large ;
- **plusieurs `catch` :** le plus précis d'abord ; le **multi-catch** `A | B` (types sans lien d'héritage) ;
- **`finally`** ;
- le **chaînage** (`getCause()`) et la **traduction** d'une exception en une autre ;
- les exceptions du JDK : `NumberFormatException`, `ArrayIndexOutOfBoundsException`, `ArithmeticException`, `IllegalArgumentException`, `IllegalStateException` ;
- une **exception vérifiée dans une lambda**.

Côté algorithme : un **virement atomique** avec **compensation** (si le crédit échoue après le débit, on rembourse).

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch11_exceptions.projects.p01_bank` :
- les exceptions `BankException`, `InsufficientFundsException`, `FrozenAccountException`, `TransferException` et `UnknownAccountException` ;
- l'interface `Account`, et les classes `StandardAccount`, `FrozenAccount` et `Bank` ;
- **`Teller`** (le `main`).

**Règle du crescendo :** chapitres 1 à 11. Les streams et `Optional` sont permis. Pas de threads, d'entrées/sorties de fichiers ni de JDBC. Pas de `System.exit` ni de `printStackTrace` : `Check` ne peut pas les vérifier.

---

## Tableau de bord

### ☐ Étape 1 — Les exceptions et les comptes

- **`class BankException extends Exception`** : deux constructeurs, `(String message)` et `(String message, Throwable cause)`, qui appellent `super(…)`.
- **`InsufficientFundsException extends BankException`** :
  - construite avec `(String accountId, long missing)` ;
  - message `solde insuffisant sur <id>` ;
  - garde `missing` et l'expose par `long missing()`.
- **`FrozenAccountException extends BankException`** : construite avec `(String accountId)`, message `compte gele : <id>`.
- **`TransferException extends BankException`** : construite avec `(String message, Throwable cause)`.
- **`UnknownAccountException extends RuntimeException`** : construite avec `(String accountId)`, message `compte inconnu : <id>`.
- **`interface Account`** :
  - `String id()`, `long balance()` ;
  - `void deposit(long amount)` ;
  - `void withdraw(long amount) throws BankException`.
- **`StandardAccount implements Account`** :
  - `deposit` lève `IllegalArgumentException("montant invalide : " + amount)` si `amount <= 0` ;
  - `withdraw` **déclare `throws InsufficientFundsException`** (plus précis que l'interface). Elle lève la même `IllegalArgumentException` si `amount <= 0`, puis une `InsufficientFundsException(id, amount - balance)` si le solde ne suffit pas.
- **`FrozenAccount implements Account`** :
  - `deposit` lève `IllegalStateException("compte gele : " + id)` ;
  - `withdraw` déclare et lève `FrozenAccountException`.
- **Question :** pourquoi `deposit` ne peut-il pas lever une `FrozenAccountException` ?

### ☐ Étape 2 — La banque

- **`Bank`** : une `LinkedHashMap<String, Account>`.
  - `static String money(long cents)` rend `525.00` (deux décimales).
  - `void open(String line)` : `id titulaire solde`. Un 4e mot `gele` crée un `FrozenAccount`, sinon c'est un `StandardAccount`.
  - `Account find(String id)` lève `UnknownAccountException` si l'identifiant est inconnu. **Aucun `throws`** : elle est non vérifiée.
  - `Collection<Account> accounts()`.
- **`void transfer(String from, String to, long amount) throws TransferException`** :
  1. `find` les deux comptes ;
  2. `source.withdraw(amount)` ; en cas de `BankException` → `throw new TransferException("virement " + from + "->" + to + " refuse", e)` ;
  3. `target.deposit(amount)` ; en cas d'`IllegalStateException` → **rembourse** (`source.deposit(amount)`), puis `throw new TransferException("virement … annule", e)`.
- **`String share(String id, int parts)`** rend `N parts de X (reste Y)`. Le partage utilise `/` et `%` sur des `long` : 0 part lève une `ArithmeticException`.

### ☐ Étape 3 — Le guichet

```
DEPOT A1 2500 -> ok A1 = 525.00
RETRAIT B2 20000 -> REFUS solde insuffisant sur B2, manque 80.00
...
VIREMENT B2 C3 5000 -> ECHEC virement B2->C3 annule <- IllegalStateException: compte gele : C3
PARTAGE A1 0 -> ERREUR java.lang.ArithmeticException: / by zero
RETRAIT -> MAL FORMEE ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1
traitees 13, erreurs {ArithmeticException=1, ArrayIndexOutOfBoundsException=1, ...}
```
- **`static String execute(Bank bank, String operation) throws BankException`** découpe sur l'espace :
  - `DEPOT id montant` et `RETRAIT id montant` : `find(p[1])`, puis `Long.parseLong(p[2])` (dans cet ordre), et rendent `ok <id> = <solde>` ;
  - `VIREMENT a b montant` : rend `ok <a> = <solde>, <b> = <solde>` ;
  - `PARTAGE id n` : rend `ok ` + `share(...)` ;
  - sinon : `IllegalArgumentException("operation inconnue : " + p[0])`.
- **`main`** :
  1. ouvre les comptes de `Data.ACCOUNTS` ;
  2. pour chaque opération de `Data.OPERATIONS`, appelle `execute` dans un `try` suivi de **sept `catch`, dans cet ordre** :

     | `catch` | Résultat affiché |
     |---|---|
     | `InsufficientFundsException` | `REFUS <message>, manque <money(missing)>` |
     | `TransferException` | `ECHEC <message> <- <nom simple de la cause>: <message de la cause>` |
     | `BankException` | `REFUS <message>` |
     | `NumberFormatException \| ArrayIndexOutOfBoundsException` | `MAL FORMEE <nom simple>: <message>` |
     | `IllegalArgumentException` | `INVALIDE <message>` |
     | `UnknownAccountException` | `INCONNU <message>` |
     | `RuntimeException` | `ERREUR ` + l'exception elle-même (son `toString()`) |

  3. dans un **`finally`**, incrémente un compteur `static int processed` ;
  4. affiche `<opération> -> <résultat>` ;
  5. compte chaque échec par **nom simple** de classe, dans une `TreeMap`.
  
  La dernière ligne de cette étape est `traitees N, erreurs {…}`.
- **Questions :**
  - Que dit le compilateur si tu mets le `catch (BankException e)` **avant** celui d'`InsufficientFundsException` ?
  - Et `catch (IllegalArgumentException e)` avant le multi-catch ?
- **Expérience :** écris `catch (NumberFormatException | IllegalArgumentException e)`. Quelle erreur ?

### ☐ Étape 4 — Les frais (exception vérifiée dans une lambda)

```
frais : impayes [C3 compte gele : C3, D4 manque 2.00] ; soldes : A1=415.00 B2=210.00 C3=300.00 D4=8.00
```
- `bank.accounts().forEach(a -> { … })` prélève `Data.FEE` avec `withdraw`. La lambda **doit** attraper les exceptions vérifiées :
  - une `InsufficientFundsException` ajoute `<id> manque <money(missing)>` à la liste des impayés ;
  - une autre `BankException` ajoute `<id> <message>`.
- Puis affiche les soldes, sous la forme ` id=solde`.
- **Question :** pourquoi la lambda ne peut-elle pas simplement laisser sortir l'exception (un `Consumer` n'a pas de `throws`) ?
- **Expériences :**
  - Dans `StandardAccount`, déclare `withdraw(long) throws Exception` : quelle erreur ?
  - Déclare `deposit(long) throws BankException` : quelle erreur ?

---

## Checklist (vérifiée par `Check`)

- `Data.ACCOUNTS`, `Data.OPERATIONS`, `Data.FEE` ;
- `class BankException extends Exception`, `extends BankException`, `extends RuntimeException`, `super(message, cause)` ;
- les `throws BankException`, `throws InsufficientFundsException`, `throws FrozenAccountException`, `throws TransferException` ;
- `throw new IllegalArgumentException(` et `throw new IllegalStateException(` ;
- les sept `catch` de l'étape 3, et `catch (IllegalStateException` ;
- `finally`, `.getCause()`, `.getMessage()`, `.missing()`.

---

## Sortie attendue complète

```
DEPOT A1 2500 -> ok A1 = 525.00
RETRAIT B2 20000 -> REFUS solde insuffisant sur B2, manque 80.00
VIREMENT A1 B2 10000 -> ok A1 = 425.00, B2 = 220.00
RETRAIT Z9 100 -> INCONNU compte inconnu : Z9
DEPOT A1 -5 -> INVALIDE montant invalide : -5
DEPOT A1 12x -> MAL FORMEE NumberFormatException: For input string: "12x"
RETRAIT C3 100 -> REFUS compte gele : C3
VIREMENT B2 C3 5000 -> ECHEC virement B2->C3 annule <- IllegalStateException: compte gele : C3
VIREMENT D4 A1 999999 -> ECHEC virement D4->A1 refuse <- InsufficientFundsException: solde insuffisant sur D4
PARTAGE A1 0 -> ERREUR java.lang.ArithmeticException: / by zero
PARTAGE A1 3 -> ok 3 parts de 141.66 (reste 0.02)
RETRAIT -> MAL FORMEE ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1
BONUS A1 100 -> INVALIDE operation inconnue : BONUS
traitees 13, erreurs {ArithmeticException=1, ArrayIndexOutOfBoundsException=1, FrozenAccountException=1, IllegalArgumentException=2, InsufficientFundsException=1, NumberFormatException=1, TransferException=2, UnknownAccountException=1}
frais : impayes [C3 compte gele : C3, D4 manque 2.00] ; soldes : A1=415.00 B2=210.00 C3=300.00 D4=8.00
```
