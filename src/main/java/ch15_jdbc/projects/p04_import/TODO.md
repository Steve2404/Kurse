# Projet 4 — L'import des fichiers clients (lots, clés générées)

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 15) :**
- **les lots (batch)** :
  - `addBatch()` empile un jeu de paramètres ;
  - `executeBatch()` envoie tout d'un coup et rend un `int[]` (une case par ordre) ;
  - `clearBatch()` vide la pile sans rien envoyer ;
  - un `Statement` empile du SQL : `addBatch(String)`, des ordres différents, mais jamais un `SELECT` ;
- **`BatchUpdateException`** (une `SQLException`) : `getUpdateCounts()` dit quelles lignes ont échoué (`Statement.EXECUTE_FAILED`, qui vaut -3) ;
- **les clés générées** :
  - `prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)`, ou `prepareStatement(sql, new String[]{"ID"})` (le nom de la colonne) ;
  - puis `getGeneratedKeys()`, un `ResultSet` ;
- **lot et transaction :** l'auto-commit coupé, un seul `commit()` par fichier, ou un `rollback()` de tout le fichier.

Côté algorithmes :
- un **nettoyage** : validation, normalisation (espaces, casse), dédoublonnage qui garde la 1re occurrence ;
- un découpage en **paquets** de `Data.CHUNK` ;
- deux politiques d'import : **strict** (un rejet annule tout) et **tolérant** (on garde ce qui passe).

**Ce que TU crées :** dans `ch15_jdbc.projects.p04_import` : `Customer`, `Importer` et **`ImportApp`** (le `main`, qui déclare `throws SQLException`).

**Règle du crescendo :** chapitres 1 à 15.

---

## Tableau de bord

### ☐ Étape 1 — Le nettoyage (pur Java)

```
nettoyage : 13 lignes, invalides [ligne 4 : email sans @, ligne 9 : nom vide, ligne 11 : 4 champs], doublons [ANA@MAIL.FR]
```
- **`record Customer(String email, String name, String city)`**.
- **`final class Importer`**, construit avec `(Connection conn, int chunk)` : son constructeur coupe l'auto-commit. Il garde deux listes, rendues par `problems()` et `duplicates()`.
- **`private Customer parse(int number, String line)`** (numéro à partir de 1) : `split(";")`, puis, dans cet ordre :
  1. pas 3 champs : note `ligne <n> : <nb> champs`, rend `null` ;
  2. l'email (`trim().toLowerCase()`) sans `@` : note `ligne <n> : email sans @` ;
  3. le nom (`trim()`) vide : note `ligne <n> : nom vide` ;
  4. sinon, rend le `Customer` (la ville aussi passe par `trim()`).
- **`List<Customer> clean(String[] lines)`** : vide les deux listes, puis remplit une `LinkedHashMap` (email → client) avec `putIfAbsent`. Si la clé existait déjà, note dans `duplicates` le **1er champ brut** de la ligne (`split(";")[0].trim()`, sans le passer en minuscules). Rend les valeurs, dans l'ordre du fichier.
- **Question :** pourquoi une `LinkedHashMap`, et pas une `HashMap` ?

### ☐ Étape 2 — L'import par lots

```
clients.csv : 9 inseres, lots [4, 4, 1], cles [1, 2, 3, 4, 5, 6, 7, 8, 9], rejets []
```
- **Le schéma :** `Data.SCHEMA`, avant de créer l'`Importer`.
- **Dans `Importer`**, trois listes remises à zéro à chaque import : `batchSizes`, `keys` et `rejected`.
- **`private void collectKeys(PreparedStatement ps)`** : ajoute à `keys` chaque `getInt(1)` de `ps.getGeneratedKeys()`.
- **`private boolean flush(PreparedStatement ps, List<Customer> pending, boolean strict)`** envoie le lot. Elle rend `true` si l'import continue :
  - **`pending` vide :** rend `true` ;
  - **sinon :** `executeBatch()`, ajoute la **longueur** du tableau à `batchSizes`, puis `collectKeys` ;
  - **sur une `BatchUpdateException e` :**
    - pour chaque case `i` de `e.getUpdateCounts()` qui vaut `Statement.EXECUTE_FAILED`, ajoute à `rejected` `<email de pending.get(i)> (<e.getSQLState()>)` ;
    - ajoute la longueur à `batchSizes` ;
    - en mode strict, `rollback()` et rend `false` ;
    - sinon, `collectKeys` et rend `true` ;
  - **dans tous les cas** (`finally`) : `pending.clear()`.
- **`String importFile(String file, String[] lines, boolean strict)`** :
  1. `clean(lines)` ;
  2. un seul `PreparedStatement` `INSERT INTO customers (email, name, city) VALUES (?, ?, ?)`, avec `RETURN_GENERATED_KEYS`. Pour chaque client : les trois `set…`, `addBatch()`, ajout à `pending`. Dès que `pending` atteint `chunk`, appelle `flush` ; après la boucle, `flush` le reste. Si un `flush` rend `false`, rend `<file> : ANNULE (strict), rejets <rejected>` ;
  3. sinon, `inserted` = nombre de clients - nombre de rejets. Ajoute une ligne au journal (`INSERT INTO import_log (file, inserted, rejected) VALUES (?, ?, ?)`), puis `commit()`, et rend `<file> : <inserted> inseres, lots <batchSizes>, cles <keys>, rejets <rejected>`.
- **Le `main`** importe `Data.CLIENTS` (nom `clients.csv`, non strict) **avant** d'afficher la ligne `nettoyage`, puis le compte rendu.
- **Question :** combien d'allers-retours vers la base pour 9 clients, avec des lots de 4 ? Et sans lot ?

### ☐ Étape 3 — Le fichier qui gêne

```
delta.csv : ANNULE (strict), rejets [ben@mail.fr (23505), gus@mail.fr (23505)]
  clients apres l'import strict : [clients=9]
delta.csv : 3 inseres, lots [4, 1], cles [14, 16, 18], rejets [ben@mail.fr (23505), gus@mail.fr (23505)]
```
- **`static List<String> rows(Connection conn, String sql)`** : chaque ligne vaut `getString(1) + "=" + getString(2)`.
- Importe `Data.DELTA` (`delta.csv`) en **strict**, puis affiche `  clients apres l'import strict : ` suivi de `rows(conn, "SELECT 'clients', COUNT(*) FROM customers")`.
- Puis importe **le même** fichier en mode **tolérant**.
- **Questions :**
  - H2 a exécuté les autres lignes du lot après l'échec. La norme JDBC l'**oblige**-t-elle ? (Non : un pilote peut aussi s'arrêter à la 1re erreur, et le tableau est alors plus court.)
  - pourquoi les clés sautent-elles de 9 à 14, puis de 2 en 2 ?

### ☐ Étape 4 — Le lot de `Statement`, et la clé par son nom

```
lot mixte : [12, 3, 1]
lot vide apres clearBatch : 0 ordre
zoe : cle 19
par ville : [LYON=6, PARIS=3, LILLE=1]
journal : [clients.csv=9/0, delta.csv=3/2, nettoyage=0/0]
```
- **Un `Statement`**, avec trois `addBatch` :
  1. `UPDATE customers SET city = UPPER(city)` ;
  2. `DELETE FROM customers WHERE city = 'NICE'` ;
  3. `INSERT INTO import_log (file, inserted, rejected) VALUES ('nettoyage', 0, 0)`.
  
  Affiche `Arrays.toString(st.executeBatch())`. Puis `addBatch("DELETE FROM customers")`, `clearBatch()`, et affiche la longueur de `executeBatch()`. Ensuite, `commit()`.
- **Zoé :** `prepareStatement("INSERT INTO customers (email, name, city) VALUES (?, ?, ?)", new String[]{"ID"})`, avec `zoe@mail.fr`, `Zoe`, `LYON`. Affiche la clé, puis `commit()`.
- **Les deux bilans, avec `rows`** :
  - `SELECT city, COUNT(*) AS n FROM customers GROUP BY city ORDER BY n DESC, city` ;
  - `SELECT file, inserted || '/' || rejected FROM import_log ORDER BY id`.
- **Question :** pourquoi un `SELECT` est-il interdit dans un lot ?

### Expériences (hors sortie attendue)

1. Passe `Data.CHUNK` à 1, puis à 100 : que deviennent `lots` et les clés de `delta.csv` ?
2. Oublie le `commit()` final de `importFile` : que voit-on des clients à la fermeture ? (Cela dépend du fournisseur.)
3. Appelle `executeBatch()` sans aucun `addBatch()` : que rend-il ?

---

## Checklist (vérifiée par `Check`)

- `Data.URL`, `Data.SCHEMA`, `Data.CHUNK`, `Data.CLIENTS`, `Data.DELTA`, `record Customer(` ;
- `.addBatch()`, `.addBatch("`, `.executeBatch()`, `.clearBatch()`, `catch (BatchUpdateException`, `.getUpdateCounts()`, `Statement.EXECUTE_FAILED` ;
- `Statement.RETURN_GENERATED_KEYS`, `new String[]{"ID"}`, `.getGeneratedKeys()`, `.setAutoCommit(false)`, `.commit()`, `.rollback()` ;
- `LinkedHashMap`, `.putIfAbsent(`, `.trim()`, `.toLowerCase()`, `finally`, `Arrays.toString(`.

---

## Sortie attendue complète

```
nettoyage : 13 lignes, invalides [ligne 4 : email sans @, ligne 9 : nom vide, ligne 11 : 4 champs], doublons [ANA@MAIL.FR]
clients.csv : 9 inseres, lots [4, 4, 1], cles [1, 2, 3, 4, 5, 6, 7, 8, 9], rejets []
delta.csv : ANNULE (strict), rejets [ben@mail.fr (23505), gus@mail.fr (23505)]
  clients apres l'import strict : [clients=9]
delta.csv : 3 inseres, lots [4, 1], cles [14, 16, 18], rejets [ben@mail.fr (23505), gus@mail.fr (23505)]
lot mixte : [12, 3, 1]
lot vide apres clearBatch : 0 ordre
zoe : cle 19
par ville : [LYON=6, PARIS=3, LILLE=1]
journal : [clients.csv=9/0, delta.csv=3/2, nettoyage=0/0]
```
