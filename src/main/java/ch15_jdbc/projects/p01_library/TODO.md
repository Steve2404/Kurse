# Projet 1 — Le catalogue de la bibliothèque (`Connection`, `Statement`, `PreparedStatement`, `ResultSet`)

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 15) :**
- **l'URL JDBC** : `jdbc:` + le fournisseur + le reste (propre au fournisseur) ;
- **`DriverManager.getConnection(url, user, password)`**, une `Connection` à fermer (try-with-resources) ;
- **les trois façons d'exécuter :**

  | Méthode | Pour | Rend |
  |---|---|---|
  | `executeQuery` | un `SELECT` | un `ResultSet` |
  | `executeUpdate` | `INSERT`, `UPDATE`, `DELETE`, DDL | un `int` (lignes touchées, 0 pour un DDL) |
  | `execute` | n'importe quoi | un `boolean` : `true` si le résultat est un `ResultSet` |

- **`PreparedStatement`** : des `?` numérotés **à partir de 1**, `setString`/`setInt`/`setNull` ;
- **`ResultSet`** :
  - le curseur démarre **avant** la 1re ligne : `next()` d'abord ;
  - les colonnes se lisent par **nom** ou par **index** (à partir de 1) ;
  - `getInt` rend 0 pour un `NULL` : `wasNull()` fait la différence ;
- **`SQLException`** et son `getSQLState()` ;
- **pourquoi `PreparedStatement`** plutôt que `Statement` : l'injection SQL.

Côté algorithmes : le découpage d'une URL, une pagination (`LIMIT ? OFFSET ?`) jusqu'à la première page vide, un regroupement fait **par SQL**.

**Ce que TU crées :** dans `ch15_jdbc.projects.p01_library` : `Book`, `Catalog` et **`LibraryApp`** (le `main`, qui déclare `throws SQLException`).

**Règle du crescendo :** chapitres 1 à 15.

---

## Tableau de bord

### ☐ Étape 1 — Se connecter et créer la table

```
connexion : jdbc | h2 | mem:p01_library ; autoCommit true ; valide true
schema : execute false, executeUpdate 0
```
- **`static String urlParts(String url)`** dans `LibraryApp` : découpe l'URL avec `split(":", 3)` (au plus 3 morceaux), puis les joint avec `" | "`.
- **La connexion :** `DriverManager.getConnection(Data.URL, Data.USER, Data.PASSWORD)` dans un try-with-resources, qui englobe **tout** le reste du `main`. Affiche `urlParts(Data.URL)`, `getAutoCommit()` et `isValid(1)`.
- **Le schéma**, avec un `Statement kept = conn.createStatement()` (garde la variable `kept` : elle **sert après** le try, à l'étape 5) :
  - `kept.execute(Data.SCHEMA)` (affiche le `boolean`) ;
  - puis `kept.executeUpdate("CREATE INDEX idx_author ON books(author)")` (affiche l'`int`).
- **Question :** pourquoi `execute` rend-il `false` ici ?

### ☐ Étape 2 — Insérer et relire

```
insertion : 10 livres, 10 lignes, 2 sans pages
Camus : [L'Etranger (1942), La Peste (1947), Le Premier Homme (1994)]
find 978-05 : Book[isbn=978-05, title=La Peste, author=Camus, pubYear=1947, pages=336]
...
```
- **`record Book(String isbn, String title, String author, int pubYear, Integer pages)`** :
  - `pages` est un `Integer` : `null` quand c'est inconnu ;
  - `static Book parse(String line)` : `split("\\|", -1)` (le `-1` garde le dernier champ, même vide) ; un champ `pages` vide donne `null` ;
  - `String shortName()` : `titre (annee)`.
- **`final class Catalog`**, construit avec la `Connection` (il ne la ferme pas). Chaque `PreparedStatement` et chaque `ResultSet` va dans son try-with-resources.
  - `int add(Book)` : `INSERT INTO books (isbn, title, author, pub_year, pages) VALUES (?, ?, ?, ?, ?)`. Pour des `pages` nulles, `setNull(5, Types.INTEGER)`, sinon `setInt`. Rend le résultat de `executeUpdate()`.
  - `private static Book read(ResultSet rs)` : lis `getInt("pages")`, puis `wasNull()` pour décider entre la valeur et `null`. Lis le **titre par son index** (`getString(2)`), et le reste par nom : `isbn`, `author`, `pub_year`.
  - `List<Book> byAuthor(String)` : `SELECT * FROM books WHERE author = ? ORDER BY pub_year`, avec une boucle `while (rs.next())`.
  - `Optional<Book> find(String isbn)` : `WHERE isbn = ?`, avec un `if (rs.next())`.
- **Le `main`** insère chaque ligne de `Data.BOOKS`. Affiche :
  - le nombre de livres ;
  - la **somme** des `executeUpdate` ;
  - combien n'ont pas de pages.
  
  Puis affiche les `shortName()` de `byAuthor("Camus")`, et trois `find` :
  - `find("978-05").orElseThrow()` en entier ;
  - seulement les `pages()` de `978-06` ;
  - `find("999")` tel quel.
- **Question :** que rendrait `getInt("pages")` pour `978-06`, sans `wasNull()` ?

### ☐ Étape 3 — Paginer, regrouper, modifier

```
recherche "le" page 1 : [Le Dernier Jour d'un condamne, Le Petit Prince, Le Premier Homme]
...
recherche "le" : arret a la page 3 (vide)
par siecle : {1800=4, 1900=6}
Hugo +10 pages : 3 lignes ; supprimes avant 1830 : 1 ; inconnu : 0 ; restent 9
```
- **`List<String> search(String word, int page, int size)`** :
  - la requête : `SELECT title FROM books WHERE LOWER(title) LIKE ? ORDER BY title LIMIT ? OFFSET ?` ;
  - les paramètres : `"%" + word.toLowerCase() + "%"`, `size`, puis `(page - 1) * size`.
  
  Dans le `main`, demande les pages 1, 2, 3… de `search("le", page, 3)` **jusqu'à la première vide**. Affiche chaque page non vide, puis le numéro de la page vide.
- **`Map<Integer, Integer> countByCentury()`** (une `TreeMap`) : avec un `Statement`, `SELECT pub_year / 100 * 100 AS century, COUNT(*) AS n FROM books GROUP BY pub_year / 100 * 100`. Lis les colonnes par leurs **alias** : `century` et `n`.
- **Les modifications :**
  - `int addPages(String author, int extra)` : `UPDATE books SET pages = pages + ? WHERE author = ?` ;
  - `int deleteBefore(int year)` : `DELETE FROM books WHERE pub_year < ?` ;
  - `int count()` : `SELECT COUNT(*) FROM books`, lu avec `getInt(1)`.
  
  Affiche, dans cet ordre : `addPages("Hugo", 10)`, `deleteBefore(1830)`, `addPages("Zola", 10)`, puis `count()`.
- **Question :** pourquoi `UPDATE … SET pages = pages + 10` laisse-t-il `NULL` un livre sans pages ?

### ☐ Étape 4 — Les pièges (`static void pitfalls(Connection)`)

```
pieges :
  getString avant next : 02000
  colonne 0 : 90008
  ...
```
- Un seul `Statement st` (try-with-resources) pour toute la méthode. Chaque erreur est attrapée **séparément** (`catch (SQLException e)`), et tu affiches `e.getSQLState()`, avec deux espaces devant :
  1. `rs = st.executeQuery("SELECT title FROM books ORDER BY isbn")`, puis `rs.getString(1)` **avant** `next()` ;
  2. après `rs.next()`, `rs.getString(0)` ;
  3. `rs.getString("nope")` ;
  4. `st.executeUpdate("SELECT * FROM books")` ;
  5. `st.executeQuery("DELETE FROM books WHERE pub_year > 3000")` ;
  6. `st.executeQuery("SELECT * FROM livres")`.
- **Ensuite :**
  - `first = st.executeQuery("SELECT isbn FROM books")`, puis une 2e requête sur le **même** `st` (`SELECT title FROM books`). Affiche `first.isClosed()` ;
  - puis `first.next()` (attrapée).
- **Question :** les codes `02000`, `42S22` et `42S02` sont standards ; les codes `9xxxx` sont propres à H2. Lesquels changeraient avec PostgreSQL ?

### ☐ Étape 5 — L'injection SQL, et la fermeture

```
injection :
  Statement avec x' OR '1'='1 : 9 livres
  PreparedStatement avec x' OR '1'='1 : 0 livre
  Statement avec L'Etranger : SQLState 42000
  PreparedStatement avec L'Etranger : 1 livre
apres le try : Statement inutilisable, SQLState 90007
```
- Dans `Catalog` :
  - `int countByTitleUnsafe(String title)` **colle** le titre dans le SQL : `"SELECT COUNT(*) FROM books WHERE title = '" + title + "'"`, avec un `Statement` ;
  - `int countByTitle(String title)` fait la même chose avec `WHERE title = ?`.
- Appelle les deux avec `Data.INJECTION`, puis avec `Data.APOSTROPHE` (la version `Statement` lève une exception : affiche son état).
- **Après** le try de la connexion : `kept.executeQuery("SELECT 1")`, attrapée.
- **Questions :**
  - écris la requête **complète** que reçoit la base avec `Data.INJECTION` : pourquoi compte-t-elle tout ?
  - qui ferme quoi, quand un try-with-resources ferme la `Connection` ?

### Expériences (hors sortie attendue)

1. Remplace `Data.URL` par `"jdbc:h2:men:p01"` (faute de frappe), puis par `"jdbc:hh2:mem:p01"`. Quelles erreurs obtiens-tu ? (Indice : `No suitable driver`, SQLState `08001`.)
2. Ouvre une 2e connexion vers la même URL **après** la fermeture de la première : la table existe-t-elle encore ? Pourquoi ?
3. Écris `ps.setString(6, …)` sur une requête qui n'a que 5 `?`.

---

## Checklist (vérifiée par `Check`)

- `Data.URL`, `Data.USER`, `Data.PASSWORD`, `Data.SCHEMA`, `Data.BOOKS`, `Data.INJECTION`, `Data.APOSTROPHE` ;
- `DriverManager.getConnection(`, `try (Connection`, `.getAutoCommit()`, `.isValid(`, `.split(":", 3)`, `record Book(`, `Integer pages` ;
- `.createStatement()`, `.execute(`, `.executeUpdate(`, `.executeQuery(`, `.prepareStatement(`, `try (PreparedStatement`, `try (ResultSet` ;
- `.setString(`, `.setInt(`, `.setNull(`, `Types.INTEGER`, `rs.next()`, `.getString(`, `.getInt(`, `.wasNull()`, `LIMIT ? OFFSET ?` ;
- `catch (SQLException`, `.getSQLState()`, `.isClosed()`, `Optional<Book>`.

---

## Sortie attendue complète

```
connexion : jdbc | h2 | mem:p01_library ; autoCommit true ; valide true
schema : execute false, executeUpdate 0
insertion : 10 livres, 10 lignes, 2 sans pages
Camus : [L'Etranger (1942), La Peste (1947), Le Premier Homme (1994)]
find 978-05 : Book[isbn=978-05, title=La Peste, author=Camus, pubYear=1947, pages=336]
find 978-06 : pages null
find 999 : Optional.empty
recherche "le" page 1 : [Le Dernier Jour d'un condamne, Le Petit Prince, Le Premier Homme]
recherche "le" page 2 : [Le Rouge et le Noir, Les Miserables]
recherche "le" : arret a la page 3 (vide)
par siecle : {1800=4, 1900=6}
Hugo +10 pages : 3 lignes ; supprimes avant 1830 : 1 ; inconnu : 0 ; restent 9
pieges :
  getString avant next : 02000
  colonne 0 : 90008
  colonne inconnue : 42S22
  executeUpdate d'un SELECT : 90001
  executeQuery d'un DELETE : 90002
  table inconnue : 42S02
  1er ResultSet ferme par la 2e requete : true
  next sur un ResultSet ferme : 90007
injection :
  Statement avec x' OR '1'='1 : 9 livres
  PreparedStatement avec x' OR '1'='1 : 0 livre
  Statement avec L'Etranger : SQLState 42000
  PreparedStatement avec L'Etranger : 1 livre
apres le try : Statement inutilisable, SQLState 90007
```
