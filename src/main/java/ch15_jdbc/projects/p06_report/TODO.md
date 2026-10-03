# Projet 6 — Le générateur de rapports et la copie de base (métadonnées)

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 15) :**
- **`ResultSetMetaData`** (`rs.getMetaData()`) : la description d'un résultat **inconnu à l'avance** :
  - `getColumnCount()` ;
  - `getColumnLabel(i)` (l'alias) contre `getColumnName(i)` (la colonne) ;
  - `getColumnType(i)` (une constante de `java.sql.Types`), `getColumnTypeName(i)`, `getColumnClassName(i)` ;
- **`getObject(i)`** : lire une valeur sans connaître son type ;
- **`DatabaseMetaData`** (`conn.getMetaData()`) : la description de la **base** :
  - le produit, le pilote, l'utilisateur, ce qu'elle sait faire (`supportsBatchUpdates`, `supportsSavepoints`) ;
  - les tables (`getTables`), les colonnes (`getColumns`), la clé primaire (`getPrimaryKeys`), les clés étrangères (`getImportedKeys`).
  
  Chacune de ces méthodes rend un **`ResultSet`**, à lire avec des noms de colonnes **normalisés** (`TABLE_NAME`, `COLUMN_NAME`…).

Côté algorithmes :
- un moteur de tableau **générique** en deux passages (les largeurs, puis la mise en forme), avec les nombres alignés à droite ;
- un **tri topologique** (algorithme de Kahn) des tables selon leurs clés étrangères ;
- un **dump** (le schéma et les données, en SQL), rechargé dans une 2e base, puis comparé table par table.

**Ce que TU crées :** dans `ch15_jdbc.projects.p06_report` : `TablePrinter`, `Schema` et **`ReportApp`** (le `main`, qui déclare `throws SQLException`).

**Règle du crescendo :** chapitres 1 à 15.

---

## Tableau de bord

### ☐ Étape 1 — La base, et ce qu'elle dit d'elle-même

```
base H2, pilote H2 JDBC Driver, utilisateur SA, lots true, points de sauvegarde true
```
- **Deux connexions**, dans le même try-with-resources :
  - `conn = DriverManager.getConnection(Data.URL, Data.USER, Data.PASSWORD)` ;
  - `copy = DriverManager.getConnection(Data.COPY_URL)` (vide pour l'instant).
- **Les données :** `Data.SCHEMA`, puis `Data.INSERTS`, avec `executeUpdate` sur un `Statement` de `conn`.
- **Avec `DatabaseMetaData meta = conn.getMetaData()`**, affiche :
  - `getDatabaseProductName()`, `getDriverName()`, `getUserName()` ;
  - `supportsBatchUpdates()`, `supportsSavepoints()`.

### ☐ Étape 2 — Le moteur de tableau (`TablePrinter`)

```
titre          | auteur | prix  | parution
---------------+--------+-------+-----------
Les Miserables | Hugo   | 12.90 | 1862-04-03
Le Proces      | Kafka  |  7.20 | -
...
(4 lignes)
```
- **`final class TablePrinter`**, avec `static List<String> render(ResultSet rs)` :
  1. Avec `ResultSetMetaData`, pour chaque colonne `i` (de 1 à `getColumnCount()`) :
     - l'en-tête : `getColumnLabel(i).toLowerCase()` ;
     - l'alignement : **à droite** si `getColumnType(i)` est dans un `Set` de `Types.INTEGER`, `SMALLINT`, `BIGINT`, `DECIMAL`, `NUMERIC`, `DOUBLE`, `REAL` ;
     - la largeur de départ : celle de l'en-tête.
  2. **1er passage :** lis toutes les lignes. Chaque cellule vaut `getObject(i)`, ou `-` pour `null`. Agrandis les largeurs au besoin.
  3. **2e passage :** produis les lignes du tableau :
     - l'en-tête (toujours à gauche) ;
     - le séparateur : des `-` sur la largeur de chaque colonne, joints par `-+-` ;
     - les lignes de données ;
     - `(<n> lignes)`.
     
     Les cellules sont complétées par des espaces, jointes par `" | "`, et chaque ligne passe par `stripTrailing()`.
- **`static List<String> query(Connection conn, String sql)`** dans `ReportApp` : exécute la requête et rend `TablePrinter.render(rs)`. Affiche chaque ligne des deux rapports de `Data.REPORTS`.
- **Puis**, `SELECT title AS titre, price * 2 AS double_prix FROM books` : pour chaque colonne, affiche `colonne <i> : label <getColumnLabel>, nom <getColumnName>, type <getColumnTypeName>, classe Java <getColumnClassName>`.
- **Questions :**
  - pourquoi deux passages ?
  - que vaut `getColumnName` pour une colonne **calculée** ?

### ☐ Étape 3 — Explorer le schéma (`Schema`)

```
AUTHORS : cle ID, [ID INTEGER NOT NULL, NAME CHARACTER VARYING(30) NOT NULL, COUNTRY CHARACTER VARYING(20)], references {}
BOOKS : cle ID, [...], references {AUTHOR_ID=AUTHORS, PUBLISHER_ID=PUBLISHERS}
...
ordre de creation : [AUTHORS, PUBLISHERS, BOOKS, REVIEWS, TAGS]
ordre de suppression : [TAGS, REVIEWS, BOOKS, PUBLISHERS, AUTHORS]
```
- **`final class Schema`**, construit avec la `Connection` (il garde aussi son `DatabaseMetaData`). Toutes les recherches utilisent le catalogue `null` et le schéma `"PUBLIC"` :
  - `List<String> tables()` : `getTables(null, "PUBLIC", "%", new String[]{"TABLE"})`, colonne `TABLE_NAME`, triée ;
  - `List<String> columns(String table)` : `getColumns(null, "PUBLIC", table, "%")`. Chaque colonne s'écrit `COLUMN_NAME TYPE_NAME`, plus :
    - `(<COLUMN_SIZE>)` si `DATA_TYPE` vaut `Types.VARCHAR` ;
    - `(<COLUMN_SIZE>,<DECIMAL_DIGITS>)` pour `Types.DECIMAL` ou `Types.NUMERIC` ;
    - ` NOT NULL` si `NULLABLE` vaut `DatabaseMetaData.columnNoNulls` ;
  - `String primaryKey(String table)` : `getPrimaryKeys`, colonne `COLUMN_NAME` (une seule colonne ici), ou `null` ;
  - `Map<String, String> foreignKeys(String table)` (une `TreeMap`) : `getImportedKeys`, `FKCOLUMN_NAME` → `PKTABLE_NAME`.
- **`List<String> creationOrder()`**, le tri topologique de Kahn :
  - pour chaque table, l'ensemble des tables qu'elle **référence** (sans elle-même) ;
  - un `TreeSet` `ready` démarre avec les tables qui ne dépendent de rien ;
  - tant qu'il n'est pas vide : `pollFirst()`, ajoute la table à l'ordre, et retire-la des dépendances des autres. Une table dont les dépendances deviennent vides entre dans `ready` ;
  - s'il reste des tables à la fin, lève `IllegalStateException("cycle de cles etrangeres")`.
- **Le `main`** affiche, pour chaque table, `<table> : cle <pk>, <colonnes>, references <fks>`. Puis l'ordre de création, et l'ordre de suppression (une copie inversée avec `Collections.reverse`).
- **Question :** pourquoi les noms sont-ils en MAJUSCULES alors que `Data.SCHEMA` les écrit en minuscules ?

### ☐ Étape 4 — Le dump, la copie, la vérification

```
dump : 21 ordres
  CREATE TABLE AUTHORS (ID INTEGER NOT NULL, NAME CHARACTER VARYING(30) NOT NULL, COUNTRY CHARACTER VARYING(20), PRIMARY KEY (ID))
  INSERT INTO AUTHORS VALUES (3, 'Kafka', NULL)
  ...
copie : 5/5 tables identiques
vider AUTHORS en premier : 23503
vider dans l'ordre de suppression : 16 lignes supprimees, il en reste 0
```
- **Dans `Schema`** :
  - `private static String literal(Object value, int sqlType)` : `NULL` pour `null` ; `DATE '<valeur>'` si le type vaut `Types.DATE` ; tel quel pour un `Number` ; sinon, entre apostrophes, chaque `'` intérieure **doublée** ;
  - `List<String> dump()`, pour chaque table dans l'ordre de création :
    - `CREATE TABLE <t> (<colonnes>, PRIMARY KEY (<pk>), FOREIGN KEY (<col>) REFERENCES <table>…)`, les morceaux joints par `", "` ;
    - puis, pour chaque ligne de `SELECT * FROM <t> ORDER BY 1`, `INSERT INTO <t> VALUES (<litteraux>)`, avec les types tirés du `ResultSetMetaData`.
- **Le `main`** affiche le nombre d'ordres, puis, avec deux espaces devant, **seulement** les ordres qui commencent par `CREATE`, ou qui contiennent `NULL` ou `''`.
- **La copie :** exécute chaque ordre du dump sur `copy`. Puis, pour chaque table, compare `query(conn, "SELECT * FROM " + t + " ORDER BY 1")` à la même requête sur `copy` (`equals` sur les listes). Affiche `copie : <identiques>/<total> tables identiques`.
- **Vider la copie :**
  - `DELETE FROM` la **1re** table de l'ordre de création, attrapée : affiche son SQLState ;
  - puis `DELETE FROM` chaque table dans l'ordre de **suppression**, en additionnant les lignes ;
  - avec `static int count(Connection conn, String table)` (`SELECT COUNT(*)`), additionne ce qui reste.
- **Questions :**
  - pourquoi la 1re suppression échoue-t-elle ?
  - pourquoi `TAGS` arrive-t-il en dernier à la création, alors qu'il ne dépend de rien ?

### Expériences (hors sortie attendue)

1. Ajoute à `Data.SCHEMA` une table qui se référence elle-même (`manager_id REFERENCES employees(id)`) : le tri topologique tient-il ? (Indice : le `remove(t)` de la table elle-même.)
2. Passe `render` sur un `ResultSet` de `meta.getTables(…)` : quelles colonnes une base décrit-elle pour chaque table ?
3. Lance `meta.getColumns(null, "PUBLIC", "books", "%")`, en minuscules : que trouves-tu ?

---

## Checklist (vérifiée par `Check`)

- `Data.URL`, `Data.COPY_URL`, `Data.USER`, `Data.PASSWORD`, `Data.SCHEMA`, `Data.INSERTS`, `Data.REPORTS` ;
- `ResultSetMetaData`, `.getMetaData()`, `.getColumnCount()`, `.getColumnLabel(`, `.getColumnName(`, `.getColumnType(`, `.getColumnTypeName(`, `.getColumnClassName(`, `.getObject(` ;
- `DatabaseMetaData`, `.getDatabaseProductName()`, `.getDriverName()`, `.getUserName()`, `.supportsBatchUpdates()`, `.supportsSavepoints()` ;
- `.getTables(`, `.getColumns(`, `.getPrimaryKeys(`, `.getImportedKeys(`, `"TABLE_NAME"`, `"COLUMN_SIZE"`, `"DECIMAL_DIGITS"`, `DatabaseMetaData.columnNoNulls`, `"FKCOLUMN_NAME"`, `"PKTABLE_NAME"` ;
- `Types.VARCHAR`, `Types.DECIMAL`, `Types.DATE`, `.pollFirst()`, `IllegalStateException`, `Collections.reverse(`, `.stripTrailing()`.

---

## Sortie attendue complète

```
base H2, pilote H2 JDBC Driver, utilisateur SA, lots true, points de sauvegarde true
titre          | auteur | prix  | parution
---------------+--------+-------+-----------
Les Miserables | Hugo   | 12.90 | 1862-04-03
La Peste       | Camus  |  9.50 | 1947-06-10
Le Proces      | Kafka  |  7.20 | -
L'Etranger     | Camus  |  6.80 | 1942-05-19
(4 lignes)
pays   | avis | moyenne
-------+------+--------
-      |    1 |     4.0
France |    4 |     4.3
(2 lignes)
colonne 1 : label TITRE, nom TITLE, type CHARACTER VARYING, classe Java java.lang.String
colonne 2 : label DOUBLE_PRIX, nom DOUBLE_PRIX, type NUMERIC, classe Java java.math.BigDecimal
AUTHORS : cle ID, [ID INTEGER NOT NULL, NAME CHARACTER VARYING(30) NOT NULL, COUNTRY CHARACTER VARYING(20)], references {}
BOOKS : cle ID, [ID INTEGER NOT NULL, TITLE CHARACTER VARYING(40) NOT NULL, AUTHOR_ID INTEGER NOT NULL, PUBLISHER_ID INTEGER, PRICE DECIMAL(6,2) NOT NULL, PUBLISHED DATE], references {AUTHOR_ID=AUTHORS, PUBLISHER_ID=PUBLISHERS}
PUBLISHERS : cle ID, [ID INTEGER NOT NULL, NAME CHARACTER VARYING(30) NOT NULL], references {}
REVIEWS : cle ID, [ID INTEGER NOT NULL, BOOK_ID INTEGER NOT NULL, STARS INTEGER NOT NULL, NOTE CHARACTER VARYING(40)], references {BOOK_ID=BOOKS}
TAGS : cle ID, [ID INTEGER NOT NULL, LABEL CHARACTER VARYING(15) NOT NULL], references {}
ordre de creation : [AUTHORS, PUBLISHERS, BOOKS, REVIEWS, TAGS]
ordre de suppression : [TAGS, REVIEWS, BOOKS, PUBLISHERS, AUTHORS]
dump : 21 ordres
  CREATE TABLE AUTHORS (ID INTEGER NOT NULL, NAME CHARACTER VARYING(30) NOT NULL, COUNTRY CHARACTER VARYING(20), PRIMARY KEY (ID))
  INSERT INTO AUTHORS VALUES (3, 'Kafka', NULL)
  CREATE TABLE PUBLISHERS (ID INTEGER NOT NULL, NAME CHARACTER VARYING(30) NOT NULL, PRIMARY KEY (ID))
  CREATE TABLE BOOKS (ID INTEGER NOT NULL, TITLE CHARACTER VARYING(40) NOT NULL, AUTHOR_ID INTEGER NOT NULL, PUBLISHER_ID INTEGER, PRICE DECIMAL(6,2) NOT NULL, PUBLISHED DATE, PRIMARY KEY (ID), FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS, FOREIGN KEY (PUBLISHER_ID) REFERENCES PUBLISHERS)
  INSERT INTO BOOKS VALUES (3, 'Le Proces', 3, NULL, 7.20, NULL)
  INSERT INTO BOOKS VALUES (4, 'L''Etranger', 1, 2, 6.80, DATE '1942-05-19')
  CREATE TABLE REVIEWS (ID INTEGER NOT NULL, BOOK_ID INTEGER NOT NULL, STARS INTEGER NOT NULL, NOTE CHARACTER VARYING(40), PRIMARY KEY (ID), FOREIGN KEY (BOOK_ID) REFERENCES BOOKS)
  INSERT INTO REVIEWS VALUES (2, 1, 4, NULL)
  INSERT INTO REVIEWS VALUES (4, 4, 3, 'l''absurde')
  INSERT INTO REVIEWS VALUES (5, 3, 4, NULL)
  CREATE TABLE TAGS (ID INTEGER NOT NULL, LABEL CHARACTER VARYING(15) NOT NULL, PRIMARY KEY (ID))
copie : 5/5 tables identiques
vider AUTHORS en premier : 23503
vider dans l'ordre de suppression : 16 lignes supprimees, il en reste 0
```
