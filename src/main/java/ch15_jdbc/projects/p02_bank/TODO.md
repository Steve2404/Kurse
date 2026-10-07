# Projet 2 — Les virements de la banque (transactions)

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 15) :**
- **l'auto-commit :**
  - actif par défaut : chaque ordre est validé tout seul ;
  - `setAutoCommit(false)` ouvre une transaction, qui dure jusqu'à `commit()` ou `rollback()` ;
  - **repasser** à `setAutoCommit(true)` **valide** ce qui est en cours ;
- **le tout ou rien :** plusieurs ordres SQL, puis un seul `commit()` ; en cas d'échec, `rollback()` annule **tout** ;
- **l'isolation :** une **autre** connexion ne voit pas ce qui n'est pas validé ;
- **`SQLException`** :
  - celle de la base : une contrainte `CHECK` violée donne le SQLState `23513` ;
  - la tienne : `new SQLException(message, sqlState)`, pour traiter un « 0 ligne touchée » comme une erreur ;
- **`Statement.RETURN_GENERATED_KEYS`** et `getGeneratedKeys()` : le numéro créé par la base.

Côté algorithmes :
- des virements simples, puis des paies groupées **tout ou rien** ;
- un **rejeu** du journal : à partir des soldes initiaux, il doit retrouver exactement la table.

**Ce que TU crées :** dans `ch15_jdbc.projects.p02_bank` : `Bank` et **`BankApp`** (le `main`, qui déclare `throws SQLException`).

**Règle du crescendo :** chapitres 1 à 15.

**Tes outils pour ce projet :** la flèche verte, ou le terminal avec le pilote H2 (projet 1, en-tête) :

```
javac -d build/ch15-p02 -sourcepath src/main/java src/main/java/ch15_jdbc/projects/p02_bank/BankApp.java
java -cp "build/ch15-p02;$env:USERPROFILE\.m2\repository\com\h2database\h2\2.3.232\h2-2.3.232.jar" ch15_jdbc.projects.p02_bank.BankApp
```

---

## Tableau de bord

### ☐ Étape 1 — Ouvrir la banque

```
comptes : {A1=500, B2=200, C3=0, D4=1000} ; autoCommit true
banque ouverte : autoCommit false
```

**📖 La leçon : réutiliser un `PreparedStatement`.** On le prépare **une** fois, puis on change seulement ses paramètres avant chaque `executeUpdate()`. C'est plus rapide, et plus sûr.

**👉 À toi :**

- **Deux connexions** vers `Data.URL`, dans le **même** try-with-resources : `conn` (la banque) et `observer` (elle sert à l'étape 4).
- **Le schéma :** chaque ordre de `Data.SCHEMA`, avec `executeUpdate` sur un `Statement`.
- **Les comptes :** un **seul** `PreparedStatement` `INSERT INTO accounts VALUES (?, ?, ?)`, réutilisé pour chaque ligne de `Data.ACCOUNTS`. Remplis en même temps une `TreeMap` `initial` (id → solde). Affiche-la, avec `getAutoCommit()`.
- **`final class Bank`**, construit avec la `Connection`. Son constructeur appelle `conn.setAutoCommit(false)` : affiche de nouveau `getAutoCommit()`.
- **Question :** à ce moment, les comptes insérés sont-ils déjà validés ? Pourquoi ?

### ☐ Étape 2 — Les virements

```
  A1>B2:150 -> ok (journal #1)
  B2>C3:400 -> refuse : solde insuffisant (23513), rien n'a change
  C3>A1:0 -> refuse : virement invalide
  D4>Z9:100 -> refuse : compte inconnu Z9 (02000), rien n'a change
```

**📖 La leçon : la transaction, tout ou rien.** Par défaut, chaque ordre est validé tout seul (**auto-commit**). Pour qu'un virement (un débit **et** un crédit) soit tout ou rien, on coupe l'auto-commit, et on décide soi-même :

```java
conn.setAutoCommit(false);
try {
    st.executeUpdate("UPDATE bocaux SET grammes = grammes - 100 WHERE nom = 'riz'");
    st.executeUpdate("UPDATE …");      // une 2e modification
    conn.commit();                     // tout devient définitif
} catch (SQLException e) {
    conn.rollback();                   // tout est défait, comme si rien n'avait eu lieu
}
```

Une contrainte de la table (`CHECK (grammes >= 0)`) est vérifiée **par la base** : un `UPDATE` qui la violerait est refusé avec le SQLState `23513`.

**📖 La leçon : la clé générée.** Quand la base choisit elle-même la clé (`AUTO_INCREMENT`), on la récupère ainsi :

```java
try (PreparedStatement ps = conn.prepareStatement("INSERT INTO bocaux (nom, grammes) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
    ps.setString(1, "pates");
    ps.setInt(2, 1000);
    ps.executeUpdate();
    try (ResultSet keys = ps.getGeneratedKeys()) {
        keys.next();
        keys.getInt(1)                 // la clé que la base a choisie
    }
}
```

On peut aussi **créer** sa propre `SQLException`, avec son SQLState : `new SQLException("message", "02000")`.

**👉 À toi :**

- **Dans `Bank`**, les méthodes **privées** :
  - `void add(String account, int delta)` : `UPDATE accounts SET balance = balance + ? WHERE id = ?`. Si `executeUpdate()` rend 0, lève `new SQLException("compte inconnu " + account, "02000")` ;
  - `int move(String from, String to, int amount)` : `add(from, -amount)`, puis `add(to, amount)`, puis `INSERT INTO journal (from_id, to_id, amount) VALUES (?, ?, ?)`, préparé avec `Statement.RETURN_GENERATED_KEYS`. Rend `getInt(1)` de `getGeneratedKeys()`. **Aucun commit ici** ;
  - `static String reason(SQLException e)`, un `switch` sur `e.getSQLState()` :
  
    | SQLState | Texte |
    |---|---|
    | `"23513"` | `solde insuffisant (23513)` |
    | `"02000"` | `getMessage()` suivi de ` (02000)` |
    | autre | `erreur <etat>` |
- **`String transfer(String from, String to, int amount)`** :
  - un montant `<= 0`, ou `from.equals(to)` : rend `refuse : virement invalide` sans toucher la base ;
  - sinon, `move`, puis `commit()`, et rend `ok (journal #<seq>)` ;
  - sur une `SQLException` : `rollback()`, et rend `refuse : <reason>, rien n'a change`.
- **Le `main`** découpe chaque ligne de `Data.TRANSFERS` avec `split("[>:]")`. Affiche `  <ligne> -> <resultat>` (deux espaces devant).
- **Questions :**
  - pour `D4>Z9:100`, le débit de D4 **a réussi** avant l'erreur : qui l'annule ?
  - pour `B2>C3:400`, qui détecte le solde insuffisant : Java, ou la base ?

### ☐ Étape 3 — Les paies groupées et le rejeu

```
paie de D4 [A1:100, B2:100, C3:100] -> ok, 3 virements
paie de D4 [A1:200, B2:200, C3:500] -> annulee entierement : solde insuffisant (23513) au virement 3, 2 deja faits annules
soldes : {A1=450, B2=350, C3=100, D4=800} ; total 1700 ; conserve true
journal : [1, 2, 3, 4, 5, 6, 7] ; le rejeu retrouve les soldes true
```

**📖 Rappel :** une seule transaction pour plusieurs virements, avec **un** `commit()` à la fin (étape 2). `merge` dans une `TreeMap` (chapitre 9).

**👉 À toi :**

- **`String payroll(String from, String[] lines)`** : chaque ligne vaut `cible:montant`. Appelle `move` pour chacune, en comptant les réussites (`done`), puis **un seul** `commit()`. Elle rend :
  - `ok, <done> virements` ;
  - ou, sur une `SQLException` : `rollback()`, puis `annulee entierement : <reason> au virement <done + 1>, <done> deja faits annules`.
  
  Le `main` affiche `paie de D4 <List.of(paie)> -> <resultat>` pour chaque paie de `Data.PAYROLLS`.
- **`static Map<String, Integer> balances(Connection)`** (une `TreeMap`) : `SELECT id, balance FROM accounts`. Affiche :
  - les soldes ;
  - leur total ;
  - si ce total égale celui de `initial`.
- **Le rejeu :**
  - copie `initial` dans une nouvelle `TreeMap` ;
  - parcours `SELECT seq, from_id, to_id, amount FROM journal ORDER BY seq`. Garde chaque `seq` dans une liste, et applique `merge(from, -amount, Integer::sum)` et `merge(to, amount, Integer::sum)` ;
  - affiche la liste des `seq`, puis si le rejeu `equals` les soldes réels.
- **Question :** la 2e paie a inséré deux lignes de journal avant d'échouer. Pourquoi n'apparaissent-elles pas ? Quel `seq` aura le prochain virement réussi ? (Une séquence annulée n'est **pas** rendue.)

### ☐ Étape 4 — Ce que voit une autre connexion

```
pendant la transaction : moi 1450, observateur 450
apres rollback : moi 450
avant setAutoCommit(true) : observateur voit Cleo
apres setAutoCommit(true) : observateur voit Cleopatre
```

**📖 La leçon : deux connexions ne voient pas la même chose.** Une connexion voit ses **propres** changements non validés. Les autres connexions ne voient que ce qui a été **validé** (`commit`).

**👉 À toi :**

- **`static String first(Connection c, String sql)`** : la 1re colonne de la 1re ligne, avec `getString(1)` (ou `null` s'il n'y a pas de ligne).
- **`int balance(String account)`** dans `Bank` : `SELECT balance FROM accounts WHERE id = ?`.
- **Avec un `Statement` sur `conn`** :
  1. `UPDATE accounts SET balance = balance + 1000 WHERE id = 'A1'` (non validé). Affiche `bank.balance("A1")`, puis `first(observer, "SELECT balance FROM accounts WHERE id = 'A1'")` ;
  2. `conn.rollback()`, puis de nouveau `bank.balance("A1")` ;
  3. `UPDATE accounts SET owner = 'Cleopatre' WHERE id = 'C3'`. Affiche ce que voit l'observateur (`SELECT owner FROM accounts WHERE id = 'C3'`) ;
  4. `conn.setAutoCommit(true)`, puis de nouveau ce que voit l'observateur.
- **Questions :**
  - pourquoi l'observateur ne voit-il pas le `+1000` ?
  - sans le `setAutoCommit(true)`, que deviendrait le nouveau nom à la fermeture de `conn` ? (La réponse **dépend du fournisseur** : ne compte jamais dessus, et valide ou annule toujours toi-même.)

### Expériences (hors sortie attendue)

1. Supprime le `rollback()` du `catch` de `transfer` : que devient le débit de `D4>Z9:100` au `commit()` suivant ?
2. Appelle `conn.commit()` alors que l'auto-commit est actif. Que dit la Javadoc ? (H2 tolère, d'autres bases lèvent une `SQLException`.)
3. Remplace `CHECK (balance >= 0)` par une vérification en Java (lire le solde, puis le comparer). Avec deux connexions qui virent en même temps, que peut-il arriver ?

---

## Checklist (vérifiée par `Check`)

- `Data.URL`, `Data.SCHEMA`, `Data.ACCOUNTS`, `Data.TRANSFERS`, `Data.PAYROLLS` ;
- `DriverManager.getConnection(`, `.setAutoCommit(false)`, `.setAutoCommit(true)`, `.getAutoCommit()`, `.commit()`, `.rollback()` ;
- `new SQLException(`, `.getSQLState()`, `case "23513"`, `.getMessage()`, `Statement.RETURN_GENERATED_KEYS`, `.getGeneratedKeys()` ;
- `.prepareStatement(`, `.executeUpdate()`, `.executeQuery(`, `ORDER BY seq`, `.merge(`, `Integer::sum`, `TreeMap` ;
- au moins deux `DriverManager.getConnection(` (la banque et l'observateur).

---

## Sortie attendue complète

```
comptes : {A1=500, B2=200, C3=0, D4=1000} ; autoCommit true
banque ouverte : autoCommit false
  A1>B2:150 -> ok (journal #1)
  B2>C3:400 -> refuse : solde insuffisant (23513), rien n'a change
  C3>A1:0 -> refuse : virement invalide
  D4>Z9:100 -> refuse : compte inconnu Z9 (02000), rien n'a change
  D4>C3:250 -> ok (journal #2)
  Z9>A1:5 -> refuse : compte inconnu Z9 (02000), rien n'a change
  C3>B2:250 -> ok (journal #3)
  A1>A1:10 -> refuse : virement invalide
  B2>D4:350 -> ok (journal #4)
paie de D4 [A1:100, B2:100, C3:100] -> ok, 3 virements
paie de D4 [A1:200, B2:200, C3:500] -> annulee entierement : solde insuffisant (23513) au virement 3, 2 deja faits annules
soldes : {A1=450, B2=350, C3=100, D4=800} ; total 1700 ; conserve true
journal : [1, 2, 3, 4, 5, 6, 7] ; le rejeu retrouve les soldes true
pendant la transaction : moi 1450, observateur 450
apres rollback : moi 450
avant setAutoCommit(true) : observateur voit Cleo
apres setAutoCommit(true) : observateur voit Cleopatre
```
