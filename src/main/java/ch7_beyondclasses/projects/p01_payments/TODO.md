# Projet 1 — Les moyens de paiement (interfaces)

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 7) :**
- **déclarer une interface** :
  - les champs sont implicitement `public static final` ;
  - les méthodes sans corps sont implicitement `public abstract` ;
- les **méthodes `default`**, **`static`**, **`private`** et **`private static`** d'une interface ;
- une **interface qui en étend plusieurs** (`extends A, B`) ;
- une classe qui **implémente plusieurs interfaces** ;
- le **conflit en losange** entre deux `default` de même signature, résolu avec `X.super.m()` ;
- **redéfinir une `default`**, et appeler l'originale avec `PaymentMethod.super.pay(...)` ;
- le type de la référence décide de ce qu'on peut appeler : `instanceof` avec pattern.

Côté algorithmes :
- **clé de Luhn** (cartes bancaires) ;
- **IBAN modulo 97**, calculé chiffre par chiffre, car le nombre dépasse un `long` ;
- une clé de contrôle maison pour les bons d'achat.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch7_beyondclasses.projects.p01_payments` :
- les interfaces `PaymentMethod`, `Refundable`, `Traceable`, `SecurePayment` ;
- les classes `CreditCard`, `BankTransfer`, `Voucher` ;
- **`PaymentsApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 7.
- Pas de lambda ni de `::` (chapitre 8).
- Pas de collection ni de générique déclaré (chapitre 9).
- Pas de `try/catch` (chapitre 11).
- Les montants sont en centimes (`long`).

---

## Tableau de bord

### ☐ Étape 1 — Les interfaces

- **`PaymentMethod`** :
  - `long MAX_CENTS = 500_000;` (implicitement `public static final`) ;
  - les méthodes abstraites `String label()`, `boolean isValid()`, `long fee(long amount)` ;
  - **`default String pay(long amount)`** :
    - si invalide → `label : REFUSE (invalide)` ;
    - si le montant dépasse `MAX_CENTS` → `label : REFUSE (plafond 5000.00)` ;
    - sinon → `label : OK 450.00 + frais 7.00`.
    
    Ce texte est construit par **`private String line(String result)`**.
  - **`static String money(long cents)`** écrit `450.00` ;
  - **`private static int value(char c)`** : un chiffre vaut lui-même, une lettre A–Z vaut 10–35 ;
  - **`static boolean ibanValid(String)`** :
    1. déplace les 4 premiers caractères à la fin ;
    2. calcule le reste modulo 97 caractère par caractère : `rest = (rest * 10 + v) % 97` pour un chiffre, `(rest * 100 + v) % 97` pour une lettre ;
    3. l'IBAN est valide si le reste vaut 1.
  - **`static boolean luhnValid(String)`** : depuis la droite, double un chiffre sur deux (retire 9 au-delà de 9). Valide si la somme est un multiple de 10 ;
  - **`static int luhnCheckDigit(String partial)`** : le chiffre de 0 à 9 qui rend le numéro valide.
- **`Refundable`** : `long refund(long amount)` et `default String policy()`, qui rend `remboursable 30 jours`.
- **`Traceable`** : `default String policy()`, qui rend `trace anti-fraude`.
- **`SecurePayment extends PaymentMethod, Refundable`** :
  - `long THRESHOLD_3DS = 30_000;` ;
  - `default boolean needs3ds(long amount)` (montant > seuil) ;
  - elle **redéfinit** `policy()` : `"3-D Secure, " + Refundable.super.policy()`.

### ☐ Étape 2 — Les classes

- **`CreditCard implements SecurePayment, Traceable`** :
  - `label()` = `carte <titulaire> ****<4 derniers>` ;
  - `isValid()` utilise Luhn ;
  - `fee` = `Math.round(amount * 0.015) + 25` ;
  - `refund` rembourse tout ;
  - **le losange :** `SecurePayment` et `Traceable` apportent toutes deux `policy()`. Tu **dois** la redéfinir : `SecurePayment.super.policy() + " + " + Traceable.super.policy()`.
- **`BankTransfer implements PaymentMethod, Refundable`** :
  - `label()` = `virement <titulaire> <pays>` ;
  - `isValid()` utilise l'IBAN ;
  - les frais valent 0 si le pays est dans `"FR DE ES IT BE NL"`, sinon 500 ;
  - `refund` rend 90 %. `policy()` est hérité tel quel.
- **`Voucher implements PaymentMethod`** :
  - `static char checkLetter(String body)` = `'A' + (somme des codes des caractères hors tirets) % 26` ;
  - le bon est valide si sa dernière lettre est la bonne clé ;
  - les frais valent 0 ;
  - **`pay` est redéfini** :
    - si le bon est valide et que le montant dépasse le solde → `bon <code> : REFUSE (solde …)` ;
    - sinon, `PaymentMethod.super.pay(amount)`, on débite si valide, puis on ajoute `, reste …`.

### ☐ Étape 3 — Le rapport

```
plafond 5000.00, seuil 3-D Secure 300.00
carte Alice ****1486 : OK 450.00 + frais 7.00
  3-D Secure, remboursable 30 jours + trace anti-fraude, rembourse 450.00
  3-D Secure exige : true
carte Bob ****1487 : REFUSE (invalide)
  3-D Secure exige : false
...
paiements acceptes 3/7, frais totaux 12.00
bon GIFT-2026-G : OK 12.00 + frais 0.00, reste 18.00 | bon GIFT-2026-G : REFUSE (solde 18.00)
cle de Luhn de 453957876362148 : 6 ; cle de bon GIFT-2026- : G
```
- `static PaymentMethod create(String line)` utilise un `switch` sur `CARD`, `IBAN` et `VOUCHER`. Le tableau est un **`PaymentMethod[]`**.
- **Pour chaque moyen**, avec le montant de même indice dans `Data.AMOUNTS` :
  1. affiche `pay(amount)` ;
  2. s'il est valide et sous le plafond, cumule les frais et compte-le ;
  3. **seulement s'il est valide** et `instanceof Refundable r`, affiche deux espaces, `r.policy()`, puis `, rembourse …` ;
  4. s'il est `instanceof SecurePayment s`, affiche `  3-D Secure exige : …`.
- Ensuite la ligne des totaux.
- **Le bon d'achat :** `new Voucher("GIFT-2026-" + Voucher.checkLetter("GIFT-2026-"), 3000)`. Affiche `pay(1200)`, ` | `, puis `pay(2000)`.
- **La dernière ligne :** `luhnCheckDigit("453957876362148")` et `checkLetter("GIFT-2026-")`.
- **Expériences :**
  - dans `CreditCard`, retire la redéfinition de `policy()` : quelle erreur ?
  - écris `new CreditCard(…).money(5)` : pourquoi une méthode `static` d'interface ne s'appelle-t-elle pas via un objet ?
  - écris `String label();` puis implémente-la sans `public` : quelle erreur ?
  - dans `CreditCard`, appelle `Refundable.super.policy()` : pourquoi est-ce refusé ? (`Refundable` n'est pas une super-interface **directe** de `CreditCard`.)

---

## Checklist (vérifiée par `Check`)

- `Data.METHODS` et `Data.AMOUNTS` ;
- `interface PaymentMethod`, `interface SecurePayment extends PaymentMethod, Refundable`, `implements SecurePayment, Traceable` ;
- `default String pay(`, `private String line(`, `static String money(`, `private static int value(` ;
- les trois `X.super.policy()` et `PaymentMethod.super.pay(` ;
- `long MAX_CENTS` et `instanceof Refundable r`.

---

## Sortie attendue complète

```
plafond 5000.00, seuil 3-D Secure 300.00
carte Alice ****1486 : OK 450.00 + frais 7.00
  3-D Secure, remboursable 30 jours + trace anti-fraude, rembourse 450.00
  3-D Secure exige : true
carte Bob ****1487 : REFUSE (invalide)
  3-D Secure exige : false
virement Chloe FR : OK 1200.00 + frais 0.00
  remboursable 30 jours, rembourse 1080.00
virement Dan GB : OK 800.00 + frais 5.00
  remboursable 30 jours, rembourse 720.00
virement Eve DE : REFUSE (invalide)
bon ABCD-1234-X : REFUSE (invalide), reste 50.00
carte Fanny ****1486 : REFUSE (plafond 5000.00)
  3-D Secure, remboursable 30 jours + trace anti-fraude, rembourse 6000.00
  3-D Secure exige : true
paiements acceptes 3/7, frais totaux 12.00
bon GIFT-2026-G : OK 12.00 + frais 0.00, reste 18.00 | bon GIFT-2026-G : REFUSE (solde 18.00)
cle de Luhn de 453957876362148 : 6 ; cle de bon GIFT-2026- : G
```
