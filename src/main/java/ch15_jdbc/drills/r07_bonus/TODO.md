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

**Les notions de ce drill ont été apprises dans :** projets 1 à 7 ; ce bonus ajoute aussi les pilotes PostgreSQL et MySQL, fournis par Maven : lance-le avec la flèche verte. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r07_bonus` → **New** → **Java Class** → `Recall07`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall07`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall07`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

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
- ☐ **D09.** **Après** le try de la connexion : JDBC vu comme un **module** (le lien avec le chapitre 12).
  - `Module sql = Connection.class.getModule()`, puis son `ModuleDescriptor` ;
  - affiche son nom, puis `descriptor.uses()` ;
  - puis les `source()` de `exports()`, triés dans un `TreeSet` ;
  - puis les noms des `requires()` qui portent `ModuleDescriptor.Requires.Modifier.TRANSITIVE`, triés dans un `TreeSet`.
  
  Sur une 2e ligne, affiche `getModule().isNamed()` pour la classe du pilote H2 (`DriverManager.getDriver(VENDOR_URLS[0]).getClass()`), puis pour `Recall07.class`.
  → `D09 : java.sql uses [java.sql.Driver] exports [java.sql, javax.sql] requires transitive [java.logging, java.transaction.xa, java.xml]`
  → `D09 : pilote H2 module nomme false, Recall07 module nomme false`
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
2. D09 : relance avec le pilote H2 sur le **module path** (`java --module-path …h2-2.3.232.jar --add-modules com.h2database …`). Que devient `isNamed()` ? Quel est le nom du module H2 ?
3. D05 : pourquoi `deleteRow()` a-t-il supprimé Dan, et pas Eve, qui vient pourtant d'être insérée ?
4. D07 : `setMaxRows(0)` : que veut dire 0 ?

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
D09 : java.sql uses [java.sql.Driver] exports [java.sql, javax.sql] requires transitive [java.logging, java.transaction.xa, java.xml]
D09 : pilote H2 module nomme false, Recall07 module nomme false
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
- **JDBC et les modules :**
  - l'API vit dans le module **`java.sql`** (paquets `java.sql` et `javax.sql`). Une application modulaire écrit `requires java.sql;` ;
  - `java.sql` déclare `uses java.sql.Driver` : `DriverManager` trouve les pilotes par **`ServiceLoader`** ;
  - un pilote modulaire déclare `provides java.sql.Driver with …` ; sur le classpath, il tombe dans le module **sans nom** ;
  - `requires transitive java.logging, java.transaction.xa, java.xml` : qui lit `java.sql` lit aussi ces trois modules.
- **`SQLWarning`** : un avertissement n'est **pas** lancé. On le lit avec `getWarnings()`, puis `getNextWarning()`.

</details>
