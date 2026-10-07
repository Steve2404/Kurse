# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `core/Money.java`, `core/Account.java`, `core/Ledger.java`, `premium/PremiumAccount.java` et `app/BankApp.java`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur une copie réduite du projet où les paquets s'appellent `core`, `premium` et `app`.

---

## Étape 1 — `Money` et les fabriques

**Le code de l'étape :**

```java
// core/Money.java
public static String format(long cents) {
    String sign = cents < 0 ? "-" : "";
    long abs = Math.abs(cents);
    long rest = abs % 100;
    return sign + abs / 100 + "." + (rest < 10 ? "0" : "") + rest;
}

// core/Account.java
private static int nextId = 1;
private static int opened;
private int id;
private String owner;
private long balance;
…
protected long overdraft;
protected String kind = "courant";

public static Account open(String owner, long cents) {
    Account a = new Account();
    a.init(owner, cents);
    return a;
}

protected void init(String owner, long cents) {
    id = nextId++;
    opened++;
    this.owner = owner;
    balance = cents;
    record("ouverture " + Money.format(cents));
}

// premium/PremiumAccount.java
public static PremiumAccount openPremium(String owner, long cents, long overdraft) {
    PremiumAccount p = new PremiumAccount();
    p.init(owner, cents);
    p.overdraft = overdraft;
    p.kind = "premium";
    return p;
}
```

**Pourquoi le signe à part dans `format` ?** Pour −10050 : `-10050 / 100` = −100 et `-10050 % 100` = −50. On obtiendrait `-100.-50`. On calcule donc sur la valeur absolue, et on remet le signe devant.

**L'import static :** `import static …core.Money.format;` importe **une méthode**, pas une classe. Dans `BankApp`, on écrit alors `format(…)` au lieu de `Money.format(…)`.

**Expérience clé — `Account a = p; a.init(…);` dans `PremiumAccount` :**

```
error: init() has protected access in Account
error: overdraft has protected access in Account
```

(vérifié pour la méthode **et** pour le champ.) Depuis un **autre paquet**, une sous-classe n'accède à un membre `protected` qu'à travers une référence de **son propre type** (ou d'un sous-type) : `p.init(…)` passe, `a.init(…)` non. L'idée : `PremiumAccount` peut toucher à la partie `Account` **de ses propres objets**, pas à celle de n'importe quel `Account`.

**Questions :**
- **Pourquoi `nextId` est-il `static` ?** Il doit être **partagé** par tous les comptes : c'est le compteur de la banque, pas celui d'un compte. Sans `static`, chaque nouvel objet aurait son propre `nextId`, qui vaudrait 1. Vérifié : deux `open()` successifs donnent tous les deux l'id **1**.
- **Pourquoi `private` ?** Pour que personne d'autre ne puisse le modifier. Un `Account.nextId = 1;` écrit ailleurs créerait des numéros en double. Seule la fabrique fait avancer le compteur : c'est l'**encapsulation**.

---

## Étape 2 — Le grand livre : la seule porte

**Le code :** les méthodes `deposit`, `withdraw`, `withdrawStreak`, `applyInterest` et `record` d'[`Account.java`](core/Account.java), et la classe [`Ledger.java`](core/Ledger.java).

**Les quatre niveaux d'accès de ce projet :**

| Modificateur | Visible depuis | Exemple |
|---|---|---|
| `private` | la classe seule | `balance`, `record()` |
| (rien) | le **paquet** | `deposit()`, appelé par `Ledger` (même paquet `core`) |
| `protected` | le paquet **et** les sous-classes, même ailleurs | `init()`, `overdraft` |
| `public` | partout | `getBalance()`, `statement()` |

**Expérience — `a.deposit(100)` depuis `BankApp` :**

```
error: deposit(long) is not public in Account; cannot be accessed from outside package
```

`BankApp` est dans le paquet `app`. Pour faire bouger l'argent, il **doit** passer par `Ledger.execute`, la seule porte, qui vérifie tout et compte les opérations.

**La trace des retraits :**
- Bob (premium, découvert 200.00) retire 600.00 sur 500.00 : −100.00 ≥ −200.00, donc **OK**.
- Puis il retire 150.00 : −250.00 < −200.00, donc **REFUS**.
- Chloé retire 3 fois 10.00 sans dépôt entre-temps : l'alerte apparaît au **3e** retrait.

**Le virement « tout ou rien » :** `VIREMENT 1 9 100` est refusé **avant** tout débit, car le compte 9 n'existe pas. Si l'on débitait Alice avant de chercher le compte 9, l'argent disparaîtrait.

**`record` et le tableau qui double :** l'historique commence à 2 cases. Quand il est plein, `Arrays.copyOf(history, history.length * 2)` crée un **nouveau** tableau deux fois plus grand, et l'ancien est abandonné. C'est le principe d'`ArrayList` (chapitre 9).

**Question — le tableau des comptes est `final`, peut-on y ajouter des comptes ?** **Oui.** `final` sur une **référence** interdit de la **réaffecter** (`accounts = new Account[10];` ne compilerait pas). Il n'empêche pas de modifier l'**objet** désigné : `accounts[count++] = a;` remplit une case du même tableau.

---

## Étape 3 — Intérêts, relevés, classement

**Le code :** le `case "INTERETS"` et la méthode `ranking()` de `Ledger`, puis la méthode `statement()` d'`Account`.

**Les intérêts, à la main :**

| Compte | Solde (centimes) | Taux | Calcul | Intérêt |
|---|---|---|---|---|
| 1 Alice (courant) | 120000 | 10 | 120000 × 10 / 10000 = 120 | 1.20 |
| 2 Bob (négatif) | −10000 | 150 | −10000 × 150 / 10000 = −150 | −1.50 |
| 3 Chloé (courant) | 97050 | 10 | 97050 × 10 / 10000 = 97.05, arrondi à 97 | 0.97 |
| 4 Dan (premium) | 224950 | 25 | 224950 × 25 / 10000 = 562.375, arrondi à 562 | 5.62 |

**`/ 10_000.0` :** le `.0` fait la division en `double`, puis `Math.round` arrondit au plus proche. Avec `/ 10_000` (deux `long`), on aurait une troncature.

**La dernière ligne :** `Account.opened()`, `Ledger.operations()` et `Ledger.MAX_ACCOUNTS` sont appelés par le **nom de la classe**. Ce sont des membres `static` : ils appartiennent à la classe, pas à un objet. Il y a eu 14 opérations, une par ligne de `Data.OPERATIONS`, refus compris.
