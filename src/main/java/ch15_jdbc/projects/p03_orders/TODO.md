# Projet 3 — Les commandes de la boutique (points de sauvegarde)

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 15) :**
- **`Savepoint`** : un point de retour **à l'intérieur** d'une transaction :
  - `conn.setSavepoint("nom")` ou `conn.setSavepoint()` (anonyme) ;
  - `conn.rollback(sp)` : annule ce qui a été fait **après** `sp`, et garde ce qui précède ;
  - `conn.releaseSavepoint(sp)` : on n'en a plus besoin (y revenir ensuite lève une `SQLException`) ;
  - `getSavepointName()` (point nommé) et `getSavepointId()` (point anonyme) : l'autre lève une `SQLException` ;
- **la différence** entre `rollback()` (toute la transaction) et `rollback(sp)` (une partie) ;
- **plusieurs `PreparedStatement`** ouverts dans un même try-with-resources ;
- **un `PreparedStatement` est compilé dès sa création** : la table doit déjà exister.

Côté algorithmes :
- deux politiques de commande : **TOUT** (tout ou rien) et **PARTIEL** (on livre ce qui est possible) ;
- une erreur au **2e** ordre d'une ligne, alors que le 1er a réussi ;
- un **contrôle d'intégrité** par jointure : stock restant + quantités livrées = stock initial.

**Ce que TU crées :** dans `ch15_jdbc.projects.p03_orders` : `Shop` et **`ShopApp`** (le `main`, qui déclare `throws SQLException`).

**Règle du crescendo :** chapitres 1 à 15.

**Tes outils pour ce projet :** la flèche verte, ou le terminal avec le pilote H2 (projet 1, en-tête) :

```
javac -d build/ch15-p03 -sourcepath src/main/java src/main/java/ch15_jdbc/projects/p03_orders/ShopApp.java
java -cp "build/ch15-p03;$env:USERPROFILE\.m2\repository\com\h2database\h2\2.3.232\h2-2.3.232.jar" ch15_jdbc.projects.p03_orders.ShopApp
```

---

## Tableau de bord

### ☐ Étape 1 — Le magasin

```
stock initial : {P1=5, P2=10, P3=2, P4=20}
```

**📖 Rappel :** se connecter, `Statement`, `PreparedStatement` (projet 1). Un `PreparedStatement` est vérifié par la base **dès sa création**.

**👉 À toi :**

- **Une connexion** vers `Data.URL`, pour tout le `main`.
- **Le schéma :** `Data.SCHEMA`, avec un `Statement`, dans un try-with-resources **à lui**.
- **Les produits :** **ensuite seulement**, un `PreparedStatement` `INSERT INTO products VALUES (?, ?, ?, ?)`, pour chaque ligne de `Data.PRODUCTS` (`sku|nom|stock|prix`). Remplis en même temps une `TreeMap` `initial` (sku → stock).
- **`static Map<String, Integer> column(Connection conn, String column)`** (une `TreeMap`) : `"SELECT sku, " + column + " FROM products"`, lu par index. Elle sert pour le stock et pour les prix.
- **Question :** que se passe-t-il si tu crées le `PreparedStatement` dans le **même** try que le `Statement`, avant d'exécuter le schéma ?

### ☐ Étape 2 — Passer les commandes

```
#1 Ana PARTIEL -> PARTIELLE, livre [P1:2, P4:5], refuse [P3 rupture], total 105
#2 Ben TOUT -> COMPLETE, livre [P1:2, P2:3], refuse [], total 125
#3 Cleo TOUT -> ANNULEE (rupture sur P1)
...
```

**📖 La leçon : `Savepoint`, revenir en arrière d'un seul pas.** Dans une transaction, un **point de sauvegarde** marque un endroit. `rollback(sp)` défait seulement ce qui a été fait **depuis** ce point ; ce qui précède reste en attente du `commit()` :

```java
conn.setAutoCommit(false);
st.executeUpdate("UPDATE bocaux SET grammes = grammes - 100 WHERE nom = 'riz'");
Savepoint sp = conn.setSavepoint("avant-sucre");
st.executeUpdate("UPDATE bocaux SET grammes = grammes - 50 WHERE nom = 'sucre'");
conn.rollback(sp);      // défait seulement le sucre
conn.commit();          // le riz est validé : riz 400, sucre inchangé
```

`conn.releaseSavepoint(sp)` supprime un point dont on n'a plus besoin.

**👉 À toi :**

- **`final class Shop`**, construit avec la `Connection` : son constructeur coupe l'auto-commit.
- **`private static String reason(SQLException e)`**, un `switch` sur le SQLState :

  | SQLState | Texte | Cause |
  |---|---|---|
  | `"23513"` | `rupture` | le `CHECK (stock >= 0)` |
  | `"23505"` | `doublon` | la clé primaire `(order_id, sku)` |
  | `"02000"` | `inconnu` | ta propre exception |
  | autre | `erreur <etat>` | |

- **`private int ship(int orderId, String sku, int qty)`**, avec **trois** `PreparedStatement` dans le même try-with-resources :
  1. `UPDATE products SET stock = stock - ? WHERE sku = ?`. Si 0 ligne, lève `new SQLException("produit inconnu " + sku, "02000")` ;
  2. `INSERT INTO order_lines (order_id, sku, qty) VALUES (?, ?, ?)` ;
  3. `SELECT price FROM products WHERE sku = ?`.
  
  Elle rend `qty * prix`.
- **`String place(String customer, String policy, String[] lines)`** :
  1. **Crée la commande :** `INSERT INTO orders (customer, status, total) VALUES (?, 'EN COURS', 0)`, avec `RETURN_GENERATED_KEYS`. Garde l'`orderId`. Le début de chaque réponse vaut `#<id> <client> <politique> -> `.
  2. **Pour chaque ligne `sku:qte`**, un `Savepoint sp = conn.setSavepoint("ligne-" + sku)`, puis `ship` :
     - **si elle réussit :** `releaseSavepoint(sp)`, et la ligne entre dans la liste `livre` ;
     - **sur une `SQLException`, en politique `TOUT` :** `rollback()` (toute la transaction), et rend `ANNULEE (<reason> sur <sku>)` ;
     - **sur une `SQLException`, en politique `PARTIEL` :** `rollback(sp)`, et `<sku> <reason>` entre dans la liste `refuse`.
  3. **Rien de livré :** `rollback()`, et rend `ANNULEE (rien a livrer : <refuse>)`.
  4. **Sinon :** le statut vaut `COMPLETE` (rien de refusé) ou `PARTIELLE`. Fais `UPDATE orders SET status = ?, total = ? WHERE id = ?`, puis `commit()`. Rend `<statut>, livre <livre>, refuse <refuse>, total <total>`.
- **Le `main`** découpe chaque ligne de `Data.ORDERS` (`client|politique|lignes`, les lignes séparées par `,`) et affiche la réponse de `place`.
- **Questions :**
  - pour `Dan`, la ligne `P2:1` est un doublon. Le stock de P2 a **déjà baissé** quand l'insertion échoue : qui le remet ?
  - pourquoi la commande de Cleo **n'existe plus** en base, et pourquoi Dan reçoit-il le numéro 4, pas 3 ?

### ☐ Étape 3 — Le bilan et le contrôle

```
stock final : {P1=1, P2=3, P3=1, P4=13}
commandes en base : [1 Ana PARTIELLE 105, 2 Ben COMPLETE 125, 4 Dan PARTIELLE 60, 5 Eve COMPLETE 190]
controle : stock + livre = stock initial true
```

**📖 La leçon : `LEFT JOIN` et `COALESCE`.** `A LEFT JOIN B ON …` garde **toutes** les lignes de A, même celles qui n'ont pas de correspondance dans B (leurs colonnes de B valent alors `NULL`). `COALESCE(x, 0)` vaut `x`, ou 0 si `x` est `NULL`.

**👉 À toi :**

- **Le stock :** `column(conn, "stock")`.
- **Les commandes :** `SELECT id, customer, status, total FROM orders ORDER BY id`, chaque ligne affichée `id client statut total`.
- **Le contrôle :** une seule requête, `SELECT p.sku, p.stock + COALESCE(SUM(l.qty), 0) AS origin FROM products p LEFT JOIN order_lines l ON l.sku = p.sku GROUP BY p.sku, p.stock`. Range-la dans une `TreeMap` (sku → `origin`), puis compare-la à `initial` avec `equals`.
- **Question :** pourquoi un `LEFT JOIN`, et pourquoi `COALESCE` ?

### ☐ Étape 4 — L'API des `Savepoint` (`static void savepoints(Connection)`)

```
promos : {P1=40, P2=10, P3=130, P4=5} ; nom promo-ecran, id anonyme >= 0 true
  getSavepointId d'un point nomme : SQLException
rollback(promo-ecran) : {P1=40, P2=15, P3=180, P4=5}
  rollback vers un point libere : 90063
apres commit : {P1=40, P2=15, P3=180, P4=6}
```

**📖 La leçon : nommé ou anonyme.** `conn.setSavepoint("nom")` crée un point **nommé** (`getSavepointName()`), `conn.setSavepoint()` un point **anonyme** (`getSavepointId()`).

**👉 À toi :**

Avec un `Statement st`, dans cet ordre :
1. `screen = conn.setSavepoint("promo-ecran")`, puis `UPDATE products SET price = price - 50 WHERE sku = 'P3'` ;
2. `mouse = conn.setSavepoint()` (anonyme), puis `UPDATE products SET price = price - 5 WHERE sku = 'P2'` ;
3. affiche les prix (`column(conn, "price")`), `screen.getSavepointName()` et `mouse.getSavepointId() >= 0` ;
4. `screen.getSavepointId()`, attrapée : affiche `  getSavepointId d'un point nomme : SQLException` ;
5. `conn.rollback(screen)`, puis les prix ;
6. `UPDATE products SET price = price + 1 WHERE sku = 'P4'`, puis `cable = conn.setSavepoint("hausse-cable")`, `conn.releaseSavepoint(cable)`, et `conn.rollback(cable)` attrapée (affiche le SQLState) ;
7. `conn.commit()`, puis les prix.

- **Question :** l'étape 5 a annulé **deux** promotions. Pourquoi la 2e, qui était **après** un autre `Savepoint` ?

### Expériences (hors sortie attendue)

1. Dans `place`, oublie le `rollback(sp)` du cas `PARTIEL` : que devient le stock de P2 pour Dan ?
2. Appelle `conn.setSavepoint()` alors que l'auto-commit est actif. Que dit la Javadoc ?
3. Remplace `rollback(sp)` par `rollback()` en politique `PARTIEL` : qu'arrive-t-il aux lignes déjà livrées ? Et à la ligne `orders` ?

---

## Checklist (vérifiée par `Check`)

- `Data.URL`, `Data.SCHEMA`, `Data.PRODUCTS`, `Data.ORDERS` ;
- `Savepoint`, `.setSavepoint("ligne-"`, `.setSavepoint()`, `.rollback(sp)`, `.rollback()`, `.releaseSavepoint(`, `.getSavepointName()`, `.getSavepointId()` ;
- `.setAutoCommit(false)`, `.commit()`, `new SQLException(`, `case "23505"`, `case "23513"`, `Statement.RETURN_GENERATED_KEYS` ;
- `LEFT JOIN`, `COALESCE`, `GROUP BY`, `.prepareStatement(` (au moins 5 fois).

---

## Sortie attendue complète

```
stock initial : {P1=5, P2=10, P3=2, P4=20}
#1 Ana PARTIEL -> PARTIELLE, livre [P1:2, P4:5], refuse [P3 rupture], total 105
#2 Ben TOUT -> COMPLETE, livre [P1:2, P2:3], refuse [], total 125
#3 Cleo TOUT -> ANNULEE (rupture sur P1)
#4 Dan PARTIEL -> PARTIELLE, livre [P2:4], refuse [P9 inconnu, P2 doublon], total 60
#5 Eve PARTIEL -> COMPLETE, livre [P3:1, P4:2], refuse [], total 190
#6 Fay PARTIEL -> ANNULEE (rien a livrer : [P3 rupture])
stock final : {P1=1, P2=3, P3=1, P4=13}
commandes en base : [1 Ana PARTIELLE 105, 2 Ben COMPLETE 125, 4 Dan PARTIELLE 60, 5 Eve COMPLETE 190]
controle : stock + livre = stock initial true
promos : {P1=40, P2=10, P3=130, P4=5} ; nom promo-ecran, id anonyme >= 0 true
  getSavepointId d'un point nomme : SQLException
rollback(promo-ecran) : {P1=40, P2=15, P3=180, P4=5}
  rollback vers un point libere : 90063
apres commit : {P1=40, P2=15, P3=180, P4=6}
```
