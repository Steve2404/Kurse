# Projet 1 — Le guichet de banque (lancer, attraper, déclarer)

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

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

**Ce que le chapitre 11 t'apprend :**
- les **exceptions** : comment un programme signale un problème, et comment le rattraper au lieu de s'arrêter ;
- la **localisation** : afficher nombres, monnaies, dates et textes selon le pays de l'utilisateur.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : la cuisine.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch11-p01 -sourcepath src/main/java src/main/java/ch11_exceptions/projects/p01_bank/Teller.java
java "-Duser.language=fr" -cp build/ch11-p01 ch11_exceptions.projects.p01_bank.Teller
```

---

## Tableau de bord

### ☐ Étape 1 — Les exceptions et les comptes

**📖 La leçon : une exception, un problème qui remonte.** Depuis le chapitre 1, tu as vu des programmes s'arrêter avec `Exception in thread "main" …`. Une exception est un **objet** qui décrit un problème. Elle est **lancée** (`throw`) là où le problème arrive. Ensuite, elle **remonte** de méthode en méthode, jusqu'à ce que quelqu'un la **rattrape** (`catch`). Si personne ne la rattrape, le programme s'arrête.

```java
try {
    System.out.println("avant");
    int n = Integer.parseInt("douze");          // lance une NumberFormatException
    System.out.println("jamais affiche");       // sauté
} catch (NumberFormatException e) {             // rattrape ce type d'exception
    System.out.println("rattrape : " + e.getMessage());   // For input string: "douze"
} finally {
    System.out.println("finally, toujours");    // s'exécute dans TOUS les cas
}
System.out.println("le programme continue");
```

**📖 La leçon : la famille des exceptions.**

```
Throwable
├── Error                  (problèmes graves de la machine : on ne les rattrape pas)
└── Exception              (VÉRIFIÉES : le compilateur t'oblige à t'en occuper)
    └── RuntimeException   (NON vérifiées : bugs, valeurs interdites…)
```

- Une exception **vérifiée** doit être soit rattrapée par un `catch`, soit **annoncée** par `throws` dans la signature de la méthode. Sinon `javac` refuse : `error: unreported exception …; must be caught or declared to be thrown`.
- Une exception **non vérifiée** (`RuntimeException` et ses filles : `IllegalArgumentException`, `ArithmeticException`…) peut être lancée sans rien annoncer.

**📖 La leçon : écrire ta propre exception.** Une classe qui étend `Exception` (vérifiée) ou `RuntimeException` (non vérifiée). Son constructeur passe le message à `super(…)` :

```java
class FourFroidException extends Exception {
    private final int manque;
    FourFroidException(int manque) {
        super("four trop froid, il manque " + manque + " degres");
        this.manque = manque;
    }
    int manque() { return manque; }
}

static void cuire(int temperature) throws FourFroidException {     // throws : je peux la lancer
    if (temperature < 180) throw new FourFroidException(180 - temperature);   // throw : je la lance
    System.out.println("cuisson a " + temperature);
}
```

`throws` (avec un **s**) s'écrit dans la **signature**. `throw` (sans **s**) **lance** une exception.

**📖 Rappel :** une méthode qui en redéfinit une autre (chapitre 6) peut annoncer **moins** d'exceptions vérifiées, ou des exceptions **plus précises**. L'expérience de l'étape 4 te montre l'inverse.

**👉 À toi :**

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

**📖 La leçon : lancer une exception non vérifiée.** Pour refuser une valeur interdite, on lance une exception toute faite, sans `throws` :

```java
if (portions <= 0) throw new IllegalArgumentException("portions invalides : " + portions);
```

Un **2e constructeur** `(String message, Throwable cause)` permet de garder la trace de l'exception d'origine (le projet 2 te l'apprend en détail).

**👉 À toi :**

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

**📖 La leçon : plusieurs `catch`.** Un `try` peut être suivi de plusieurs `catch`. Java essaie **le premier**, puis le suivant… et s'arrête au premier qui correspond. L'ordre compte donc : place les exceptions **les plus précises d'abord**. Les questions de l'étape te montrent ce qui arrive sinon.

```java
try {
    …
} catch (ClassCastException e) {              // la plus précise
    System.out.println("cast : " + e.getClass().getSimpleName());
} catch (RuntimeException e) {                // plus générale : rattrape le reste
    System.out.println("autre");
}
```

**Le multi-catch** traite plusieurs types de la même façon, séparés par `|` :

```java
} catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
    System.out.println("multi : " + e.getClass().getSimpleName());
}
```

`e.getMessage()` donne le message. `e.toString()` donne le nom complet de la classe, puis le message.

**👉 À toi :**

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

**📖 Rappel :** une lambda est l'écriture courte d'une méthode d'interface fonctionnelle (chapitre 8). Si cette méthode n'annonce aucun `throws`, la lambda non plus.

**👉 À toi :**

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
