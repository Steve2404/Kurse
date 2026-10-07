# Drill de rappel 5 — Lots, clés générées, métadonnées, exceptions

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p06.

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall05`** dans le paquet `ch15_jdbc.drills.r05_batch_meta`. Le `main` déclare `throws SQLException`.
- Un seul try-with-resources pour `conn` (`jdbc:h2:mem:r05`) et `st = conn.createStatement()`. Puis `st.executeUpdate("CREATE TABLE tasks (id INT AUTO_INCREMENT PRIMARY KEY, label VARCHAR(20) NOT NULL UNIQUE, done BOOLEAN NOT NULL)")`.

**Les notions de ce drill ont été apprises dans :** projet 4 (étapes 2 et 4) et projet 6 (étapes 1 à 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r05_batch_meta` → **New** → **Java Class** → `Recall05`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall05`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall05`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `INSERT INTO tasks (label, done) VALUES (?, ?)`, préparé avec `Statement.RETURN_GENERATED_KEYS`. Fais un `addBatch()` pour `laver`, `ranger` et `cuisiner` (tous `false`), puis `executeBatch()`. Affiche le tableau, puis les clés de `getGeneratedKeys()`.
  → `D01 : [1, 1, 1] cles [1, 2, 3]`
- ☐ **D02.** Sur `st`, trois `addBatch` :
  1. `UPDATE tasks SET done = TRUE WHERE id <= 2` ;
  2. `DELETE FROM tasks WHERE label = 'absent'` ;
  3. `INSERT INTO tasks (label, done) VALUES ('dormir', FALSE)`.
  
  Puis `executeBatch()`.
  → `D02 : [2, 0, 1]`
- ☐ **D03.** `INSERT INTO tasks (label, done) VALUES (?, FALSE)`, en lot, pour `lire`, `laver` et `courir`. Attrape la `BatchUpdateException`. Affiche `getUpdateCounts()`, puis la position de `Statement.EXECUTE_FAILED` (avec `Arrays.stream(…).boxed().toList().indexOf(…)`).
  → `D03 : [1, -3, 1], refusee en position 1`
- ☐ **D04.** `st.addBatch("DELETE FROM tasks")`, `st.clearBatch()`, puis la longueur de `st.executeBatch()`.
  → `D04 : 0 ordre envoye`
- ☐ **D05.** `prepareStatement("INSERT INTO tasks (label, done) VALUES ('nager', TRUE)", new String[]{"ID"})`, `executeUpdate()`, puis la clé.
  → `D05 : cle 8`
- ☐ **D06.** `SELECT label AS tache, done, id * 10 AS rang FROM tasks`. Avec `ResultSetMetaData`, affiche `getColumnCount()`, puis, pour chaque colonne, `label/nom/type`.
  → `D06 : 3 [TACHE/LABEL/CHARACTER VARYING, DONE/DONE/BOOLEAN, RANG/RANG/INTEGER]`
- ☐ **D07.** Avec `DatabaseMetaData db = conn.getMetaData()` :
  - les `COLUMN_NAME` de `db.getColumns(null, "PUBLIC", "TASKS", "%")` ;
  - le nombre de lignes de `db.getTables(null, "PUBLIC", "%", new String[]{"TABLE"})`.
  
  Affiche `getDatabaseProductName()`, ce nombre, puis les colonnes.
  → `D07 : H2 1 table [ID, LABEL, DONE]`
- ☐ **D08.** Deux erreurs, chacune attrapée en `SQLException e` :
  1. `INSERT INTO tasks (label, done) VALUES ('nager', TRUE)` : affiche `e instanceof SQLIntegrityConstraintViolationException`, `getSQLState()`, `getErrorCode()` ;
  2. `st.executeQuery("SELEC 1")` : affiche `e instanceof SQLSyntaxErrorException`, `getSQLState()`, `getNextException() == null`.
  
  → `D08 : true 23505 23505`
  → `D08 : true 42001 true`

## Expériences (hors sortie attendue)

1. D05 : pourquoi la clé vaut-elle 8, et pas 6 ?
2. Mets un `SELECT` dans le lot de D02 : que se passe-t-il ?
3. Dans D07, cherche la table `"tasks"` en minuscules : que rend `getColumns` ?

## Sortie attendue complète

```
D01 : [1, 1, 1] cles [1, 2, 3]
D02 : [2, 0, 1]
D03 : [1, -3, 1], refusee en position 1
D04 : 0 ordre envoye
D05 : cle 8
D06 : 3 [TACHE/LABEL/CHARACTER VARYING, DONE/DONE/BOOLEAN, RANG/RANG/INTEGER]
D07 : H2 1 table [ID, LABEL, DONE]
D08 : true 23505 23505
D08 : true 42001 true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Les lots :**
  - `PreparedStatement` : les `set…`, puis `addBatch()`, autant de fois que nécessaire, puis `executeBatch()` ;
  - `Statement` : `addBatch(String sql)` (des ordres différents, jamais un `SELECT`) ;
  - `executeBatch()` rend un `int[]`, une case par ordre (`Statement.SUCCESS_NO_INFO` = -2 si le nombre est inconnu) ;
  - `clearBatch()` vide la pile ;
  - avec l'auto-commit coupé, on choisit soi-même le `commit()` ou le `rollback()` du lot.
- **`BatchUpdateException`** (une `SQLException`) :
  - `getUpdateCounts()` ;
  - `Statement.EXECUTE_FAILED` (-3) marque un ordre refusé ;
  - un pilote peut aussi **s'arrêter** à la 1re erreur : le tableau est alors plus court.
- **Les clés générées :**
  - `prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)` ;
  - ou `prepareStatement(sql, new String[]{"ID"})` ;
  - ou `prepareStatement(sql, new int[]{1})` ;
  - puis `getGeneratedKeys()`, un `ResultSet`.
  
  Sur un `Statement` : `executeUpdate(sql, Statement.RETURN_GENERATED_KEYS)`.
- **`ResultSetMetaData`** (`rs.getMetaData()`) : `getColumnCount()`, `getColumnLabel(i)` (l'alias), `getColumnName(i)`, `getColumnType(i)` (`java.sql.Types`), `getColumnTypeName(i)`, `getColumnClassName(i)`, `isNullable(i)`.
- **`DatabaseMetaData`** (`conn.getMetaData()`) :
  - `getDatabaseProductName/Version`, `getDriverName`, `getURL`, `getUserName` ;
  - `supportsBatchUpdates`, `supportsSavepoints`, `supportsTransactionIsolationLevel` ;
  - `getTables`, `getColumns`, `getPrimaryKeys`, `getImportedKeys`. Elles rendent des `ResultSet` aux colonnes normalisées (`TABLE_NAME`, `COLUMN_NAME`…).
- **`SQLException`** :
  - `getMessage()`, `getSQLState()` (standard), `getErrorCode()` (propre au fournisseur), `getNextException()` (une chaîne d'erreurs) ;
  - sous-classes : `SQLIntegrityConstraintViolationException`, `SQLSyntaxErrorException`, `SQLTimeoutException`, `SQLTransientException`… ;
  - c'est une exception **vérifiée** : `throws` ou `catch`.

</details>
