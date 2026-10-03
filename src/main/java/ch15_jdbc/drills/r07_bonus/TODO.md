# Drill de rappel 7 (BONUS) — Fournisseurs, curseurs défilants, isolation

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md). À faire après le drill r06.

**Chrono cible :** 15 min, puis 8 min.

**Particularité :** ce drill va **au-delà** de l'examen, pour le travail réel :
- les pilotes de plusieurs bases ;
- les curseurs qui défilent et qui modifient ;
- les niveaux d'isolation.

Sa dernière partie se fait **à la main**, avec Docker (hors `Check`).

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall07`** dans le paquet `ch15_jdbc.drills.r07_bonus`. Le `main` déclare `throws SQLException`.
- `static final String[] VENDOR_URLS = {"jdbc:h2:mem:r07", "jdbc:postgresql://localhost:15432/kurse", "jdbc:mysql://localhost:13306/kurse"}`.

## Défis

- ☐ **D01.** Pour chaque URL, `DriverManager.getDriver(url)` (sans connexion) : note `getClass().getName()` et `acceptsURL(url)`. Puis `getDriver("jdbc:oracle:thin:@localhost:1521:kurse")`, attrapée.
  → `D01 : [org.h2.Driver true, org.postgresql.Driver true, com.mysql.cj.jdbc.Driver true]`
  → `D01 : oracle 08001`
- ☐ **D02.** Les noms de classe de `DriverManager.drivers()`, triés.
  → `D02 : [com.mysql.cj.jdbc.Driver, org.h2.Driver, org.postgresql.Driver]`
- ☐ **D03.** Des `Properties` avec `user` = `sa` et `password` vide. Puis `DriverManager.getConnection(VENDOR_URLS[0], props)` et un `Statement st`, dans le même try-with-resources, pour la suite :
  - `CREATE TABLE scores (player VARCHAR(10) PRIMARY KEY, points INT NOT NULL)` ;
  - `INSERT INTO scores VALUES ('Ana', 40), ('Ben', 25), ('Cleo', 60), ('Dan', 10)`.
  
  Affiche `getMetaData().getUserName()`.
  → `D03 : SA`
- ☐ **D04.** `createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)`, puis `SELECT player FROM scores ORDER BY points DESC`. Note dans une liste, dans cet ordre :
  1. après `last()` : le joueur, puis `getRow()` ;
  2. après `absolute(2)` ;
  3. après `relative(-1)` ;
  4. après `absolute(-2)` ;
  5. après `afterLast()` puis `previous()` ;
  6. après `beforeFirst()` : `isBeforeFirst()`.
  
  → `D04 : [last Dan row 4, absolute(2) Ana, relative(-1) Cleo, absolute(-2) Ben, previous Dan, beforeFirst true]`
- ☐ **D05.** `createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE)`, puis `SELECT player, points FROM scores ORDER BY player`. Dans cet ordre :
  1. `next()`, `updateInt("points", 45)`, `updateRow()` ;
  2. `moveToInsertRow()`, `updateString("player", "Eve")`, `updateInt("points", 33)`, `insertRow()`, `moveToCurrentRow()` ;
  3. `last()`, puis `deleteRow()`.
  
  Relis ensuite toute la table avec `st`, triée par joueur.
  → `D05 : [Ana=45, Ben=25, Cleo=60, Eve=33]`
- ☐ **D06.** Avec les constantes de `Connection`, affiche :
  - si `getTransactionIsolation()` vaut `TRANSACTION_READ_COMMITTED` ;
  - `getMetaData().supportsTransactionIsolationLevel(TRANSACTION_SERIALIZABLE)` ;
  - après `setTransactionIsolation(TRANSACTION_SERIALIZABLE)`, si c'est le niveau actif.
  
  → `D06 : READ_COMMITTED par defaut true, SERIALIZABLE supporte true, actif true`
- ☐ **D07.** `st.setMaxRows(2)`, `st.setQueryTimeout(5)`, puis compte les lignes lues par `SELECT * FROM scores`.
  → `D07 : maxRows 2, lignes lues 2, timeout 5 s`
- ☐ **D08.** `conn.getWarnings() == null` et `st.getWarnings() == null`.
  → `D08 : warnings true true`
- ☐ **Pour la partie à la main :** après le try, si `args[0]` vaut `"docker"`, appelle `crud(url)` pour les deux URL Docker. `static void crud(String url)` :
  1. se connecte avec `kurse` / `kurse` ;
  2. `DROP TABLE IF EXISTS r07_scores`, la crée comme `scores`, puis insère `Ana` (40) avec un `PreparedStatement` ;
  3. affiche le produit et la ligne ;
  4. provoque un doublon, et affiche `getSQLState()` et `getErrorCode()`.
  
  Elle attrape toute `SQLException` et affiche alors `<url> injoignable (<etat>)`.

## À faire à la main, avec Docker (hors `Check`)

1. Depuis `ch15_jdbc-lab/` : `docker compose up -d` (le guide : `ch15_jdbc-lab/DOCKER_EXPLIQUE.md`).
2. Lance `Recall07` avec l'argument `docker` (dans IntelliJ : *Modify Run Configuration… > Program arguments*).
3. Compare le **SQLState** du doublon (le même partout : `23505`, ou `23000` chez MySQL) et le **code d'erreur** (propre à chaque base).
4. Arrête une boîte (`docker stop kurse-jdbc-mysql`), relance : quelle erreur ? Puis `docker compose down`.

## Expériences (hors sortie attendue)

1. D04 : appelle `absolute(2)` sur un curseur `TYPE_FORWARD_ONLY`. Que se passe-t-il ?
2. D05 : pourquoi `deleteRow()` a-t-il supprimé Dan, et pas Eve, qui vient pourtant d'être insérée ?
3. D07 : `setMaxRows(0)` : que veut dire 0 ?

## Sortie attendue complète

```
D01 : [org.h2.Driver true, org.postgresql.Driver true, com.mysql.cj.jdbc.Driver true]
D01 : oracle 08001
D02 : [com.mysql.cj.jdbc.Driver, org.h2.Driver, org.postgresql.Driver]
D03 : SA
D04 : [last Dan row 4, absolute(2) Ana, relative(-1) Cleo, absolute(-2) Ben, previous Dan, beforeFirst true]
D05 : [Ana=45, Ben=25, Cleo=60, Eve=33]
D06 : READ_COMMITTED par defaut true, SERIALIZABLE supporte true, actif true
D07 : maxRows 2, lignes lues 2, timeout 5 s
D08 : warnings true true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Les pilotes :**
  - `DriverManager.getDriver(url)` trouve le pilote qui accepte l'URL, sinon `SQLException` (`08001`) ;
  - `DriverManager.drivers()` les liste ;
  - depuis JDBC 4, les pilotes du classpath s'enregistrent seuls (`META-INF/services/java.sql.Driver`) ;
  - `getConnection(url, Properties)` : `user`, `password`, et des options propres au fournisseur.
- **Le même code, plusieurs bases :** seules l'URL et quelques détails de SQL changent :
  - `AUTO_INCREMENT` contre `GENERATED ALWAYS AS IDENTITY` ;
  - `LIMIT` ;
  - les procédures stockées.
- **Les curseurs** : `createStatement(type, concurrence)`, de même pour `prepareStatement` et `prepareCall` :
  - `TYPE_FORWARD_ONLY` (par défaut) : seulement `next()` ;
  - `TYPE_SCROLL_INSENSITIVE` : `first`, `last`, `absolute(n)` (négatif = depuis la fin), `relative(n)`, `previous`, `beforeFirst`, `afterLast`, `getRow`. Il ne voit **pas** les changements faits par d'autres ;
  - `CONCUR_UPDATABLE` : `updateXxx` puis `updateRow()`, `moveToInsertRow()` + `insertRow()`, `deleteRow()`.
- **L'isolation** (`Connection.TRANSACTION_…`) :

  | Niveau | Lectures sales | Non répétables | Fantômes |
  |---|---|---|---|
  | `READ_UNCOMMITTED` | possibles | possibles | possibles |
  | `READ_COMMITTED` | — | possibles | possibles |
  | `REPEATABLE_READ` | — | — | possibles |
  | `SERIALIZABLE` | — | — | — |

- **Le `Statement`** :
  - `setMaxRows(n)` (0 = sans limite) ;
  - `setQueryTimeout(secondes)` : au-delà, `SQLTimeoutException` ;
  - `setFetchSize(n)` : un conseil au pilote.
- **`SQLWarning`** : un avertissement n'est **pas** lancé. On le lit avec `getWarnings()`, puis `getNextWarning()`.

</details>
