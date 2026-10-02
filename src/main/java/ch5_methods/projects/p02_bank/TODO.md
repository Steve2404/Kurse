# Projet 2 — La banque en paquets

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 5) :**
- les **quatre niveaux d'accès** : `private`, *package-private* (aucun mot-clé), `protected`, `public` ;
- l'**encapsulation** : des champs `private` et des méthodes publiques ;
- les **membres `static`** : compteurs partagés, constantes, fabriques ;
- `final` sur une référence ;
- l'**import static** ;
- les varargs.

**Ce qui est donné :** `Data.java` (les comptes et les opérations) et `Check.java`.

**Ce que TU crées :** une application en **quatre sous-paquets** de `ch5_methods.projects.p02_bank` :

| Paquet | Classes | Rôle |
|---|---|---|
| `core` | `Money`, `Account`, `Ledger` | le cœur : l'argent ne bouge que par `Ledger` |
| `premium` | `PremiumAccount` | un compte avec découvert, dans un **autre** paquet |
| `app` | `BankApp` | le `main` (`app.BankApp`) : il ne voit que le `public` |

**Règle du crescendo :** chapitres 1 à 5.
- Pas de constructeur écrit par toi : les comptes se créent par des **fabriques `static`**.
- **Exception unique :** `PremiumAccount extends Account` est permis, **seulement** pour étudier `protected`. L'héritage lui-même est au chapitre 6 : pas de redéfinition (`@Override`), pas de `super`.
- Pas de collection ni de `try/catch`.
- Les montants sont en **centimes** (`long`).

---

## Tableau de bord

### ☐ Étape 1 — `Money` et les fabriques

```
ouvert 1 Alice courant 1200.00
ouvert 2 Bob premium 500.00
ouvert 3 Chloe courant 0.00
ouvert 4 Dan premium 3000.00
```
- **`core.Money`** : `public static String format(long cents)`. Elle gère le signe et met toujours 2 chiffres après le point (`-100.50`, `0.00`).
  - `BankApp` l'importe en **static** : `import static …core.Money.format;`, puis appelle simplement `format(…)`.
- **`core.Account`** :
  - champs **`private`** : numéro, titulaire, solde, historique (`String[]`), série de retraits ;
  - **`private static int nextId`** (le prochain numéro) et **`opened`** (le compteur partagé) ;
  - **`protected long overdraft`** et **`protected String kind = "courant"`** ;
  - une fabrique **`public static Account open(String owner, long cents)`** : elle fait `new Account()` (le constructeur par défaut, que Java fournit) puis appelle **`protected void init(…)`** ;
  - des getters publics.
- **`premium.PremiumAccount extends Account`**, avec `public static PremiumAccount openPremium(String owner, long cents, long overdraft)`.
  - Elle appelle `p.init(…)`, puis remplit `p.overdraft` et `p.kind`.
  - **Expérience clé :** écris `Account a = p; a.init(…);` dans `PremiumAccount`. Que dit `javac` ?
  - **Retiens :** depuis un autre paquet, un membre `protected` n'est accessible qu'à travers une référence du type de la **sous-classe**.
- **Questions :**
  - pourquoi `nextId` est-il `static` ? Que se passerait-il s'il ne l'était pas ?
  - pourquoi `private` et pas `public` ?

### ☐ Étape 2 — Le grand livre : la seule porte

```
OK depot 250.00 sur 3 -> 250.00
REFUS RETRAIT 1 150000 (decouvert autorise 0.00)
OK retrait 600.00 sur 2 -> -100.00
OK virement 750.50 de 4 vers 3
REFUS RETRAIT 2 15000 (decouvert autorise 200.00)
REFUS VIREMENT 1 9 100 (compte inconnu)
OK retrait 10.00 sur 3 -> 990.50
OK retrait 10.00 sur 3 -> 980.50
OK retrait 10.00 sur 3 -> 970.50 ALERTE 3 retraits de suite
REFUS DEPOT 1 -500 (compte inconnu ou montant <= 0)
```
- **Dans `Account`, sans modificateur (package-private)** : `void deposit(long)`, `boolean withdraw(long)`, `int withdrawStreak()`, `void applyInterest(long)`.
  - Seul `Ledger`, **du même paquet**, peut les appeler.
  - **Expérience :** appelle `a.deposit(100)` depuis `BankApp`. Lis l'erreur.
- **Le retrait :** refusé si le solde passerait sous `-overdraft`.
  - Un dépôt remet la série de retraits à 0.
  - L'alerte apparaît dès 3 retraits de suite.
- **L'historique :** une méthode `private void record(String)`. Le tableau double de taille quand il est plein (`Arrays.copyOf`).
- **`core.Ledger`** :
  - `public static final int MAX_ACCOUNTS = 8` ;
  - un **`private final Account[]`** ;
  - un compteur `private static int operations` ;
  - `public void add(Account... newAccounts)` ;
  - `public String execute(String command)`, avec un `switch` sur le premier mot.
- **Un virement est « tout ou rien »** : vérifie les deux comptes avant de toucher à l'argent.
- **Question :** le tableau des comptes est `final`. Peut-on quand même y ajouter des comptes ? Pourquoi ?

### ☐ Étape 3 — Intérêts, relevés, classement

```
OK interets : 1:1.20 2:-1.50 3:0.97 4:5.62
OK depot 500.00 sur 2 -> 398.50
releve 2 Bob (premium, solde 398.50) : ouverture 500.00 | retrait 600.00 | interets -1.50 | depot 500.00
releve 3 Chloe (courant, solde 971.47) : ouverture 0.00 | depot 250.00 | depot 750.50 | retrait 10.00 | retrait 10.00 | retrait 10.00 | interets 0.97
classement : 1.Dan=2255.12 2.Alice=1201.20 3.Chloe=971.47 4.Bob=398.50
total 4826.29, comptes ouverts 4, operations 14, max 8
```
- **Les intérêts**, en dix-millièmes :
  - un solde négatif paie −1,50 % (150) ;
  - un premium positif reçoit +0,25 % (25) ;
  - un courant positif reçoit +0,10 % (10).
  - La formule : `Math.round(solde * taux / 10_000.0)`.
- **Le relevé** : une méthode publique `statement()` de `Account`, qui lit l'historique privé.
- **Le classement** : `public Account[] ranking()` trie **une copie** par sélection, solde décroissant.
- **La dernière ligne** appelle les membres `static` par le **nom de la classe** : `Account.opened()`, `Ledger.operations()`, `Ledger.MAX_ACCOUNTS`.

---

## Checklist (vérifiée par `Check`)

- `Data.ACCOUNTS` et `Data.OPERATIONS` ;
- les paquets `core`, `premium` et `app` ;
- `private static int`, `private long`, `protected` ;
- `extends Account` ;
- `import static` ;
- une méthode package-private ;
- une fabrique `static … open…(` ;
- `Account...` ;
- `Arrays.copyOf` et `final`.

---

## Sortie attendue complète

```
ouvert 1 Alice courant 1200.00
ouvert 2 Bob premium 500.00
ouvert 3 Chloe courant 0.00
ouvert 4 Dan premium 3000.00
OK depot 250.00 sur 3 -> 250.00
REFUS RETRAIT 1 150000 (decouvert autorise 0.00)
OK retrait 600.00 sur 2 -> -100.00
OK virement 750.50 de 4 vers 3
REFUS RETRAIT 2 15000 (decouvert autorise 200.00)
REFUS VIREMENT 1 9 100 (compte inconnu)
OK retrait 10.00 sur 3 -> 990.50
OK retrait 10.00 sur 3 -> 980.50
OK retrait 10.00 sur 3 -> 970.50 ALERTE 3 retraits de suite
REFUS DEPOT 1 -500 (compte inconnu ou montant <= 0)
OK interets : 1:1.20 2:-1.50 3:0.97 4:5.62
OK depot 500.00 sur 2 -> 398.50
releve 2 Bob (premium, solde 398.50) : ouverture 500.00 | retrait 600.00 | interets -1.50 | depot 500.00
releve 3 Chloe (courant, solde 971.47) : ouverture 0.00 | depot 250.00 | depot 750.50 | retrait 10.00 | retrait 10.00 | retrait 10.00 | interets 0.97
classement : 1.Dan=2255.12 2.Alice=1201.20 3.Chloe=971.47 4.Bob=398.50
total 4826.29, comptes ouverts 4, operations 14, max 8
```
