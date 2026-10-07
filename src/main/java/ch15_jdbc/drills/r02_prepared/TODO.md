# Drill de rappel 2 — `PreparedStatement`

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p02.

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall02`** dans le paquet `ch15_jdbc.drills.r02_prepared`. Le `main` déclare `throws SQLException`.
- Un seul try-with-resources pour `conn = DriverManager.getConnection("jdbc:h2:mem:r02")` et `st = conn.createStatement()`. Puis `st.executeUpdate("CREATE TABLE films (id INT PRIMARY KEY, title VARCHAR(30) NOT NULL, rating DOUBLE, seen BOOLEAN NOT NULL, released DATE)")`.
- Chaque `PreparedStatement` et chaque `ResultSet` va dans son try-with-resources (sauf au défi D07).

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 2, 3 et 5). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r02_prepared` → **New** → **Java Class** → `Recall02`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall02`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall02`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Un **seul** `PreparedStatement` `INSERT INTO films (id, title, rating, seen) VALUES (?, ?, ?, ?)`, réutilisé pour `{{"1", "Alien", "8.5"}, {"2", "Brazil", ""}, {"3", "Casino", "8.2"}}` :
  - `setInt`, `setString` ;
  - `setDouble`, ou `setNull(3, Types.DOUBLE)` si la note est vide ;
  - `setBoolean(4, id == "1")` (avec `equals`).
  
  Affiche la somme des `executeUpdate()`.
  → `D01 : 3`
- ☐ **D02.**
  - `UPDATE films SET released = ? WHERE id = ?`, avec `setObject(1, LocalDate.of(1979, 5, 25))` et l'id 1 ;
  - puis `SELECT released, seen FROM films WHERE id = ?`. Affiche :
    - `getObject("released", LocalDate.class).getYear()` ;
    - `getBoolean("seen")` ;
    - `getDate(1).getClass().getName()`.
  
  → `D02 : 1979 true java.sql.Date`
- ☐ **D03-D04.** Avec `SELECT title FROM films WHERE id = ? AND seen = ?`, ajoute à une liste, dans l'ordre :
  1. le SQLState de `setInt(3, 1)` ;
  2. après `setInt(1, 1)` seul, le SQLState de `executeQuery()` ;
  3. après `setBoolean(2, true)`, le titre trouvé (ou `rien`) ;
  4. après `clearParameters()`, le SQLState de `executeQuery()`.
  
  → `D03-D04 : [90008, 90012, Alien, 90012]`
- ☐ **D05.** `DELETE FROM films WHERE rating < ?`, avec 8.3. Exécute-le **deux fois**, et affiche les deux résultats.
  → `D05 : 1 puis 0`
- ☐ **D06.**
  - `prepareStatement("SELECT COUNT(*) FROM films WHERE title LIKE '%?%'")`, puis `setString(1, "li")`, attrapée ;
  - puis `SELECT title FROM films WHERE title LIKE ?`, avec `"%li%"`. Affiche le 1er titre.
  
  → `D06 : ? entre apostrophes 90008`
  → `D06 : LIKE ? avec "%li%" Alien`
- ☐ **D07.** Sur `SELECT title FROM films WHERE id = ?` (le `PreparedStatement` dans un try, mais **pas** ses `ResultSet`) :
  - `first = executeQuery()` avec l'id 1 ;
  - puis `second = executeQuery()` avec l'id 2.
  
  Affiche `first.isClosed()`, puis le titre de `second`.
  → `D07 : 1er ferme true, 2e Brazil`
- ☐ **D08.** `input = "' OR 1=1 --"`. Compte les films avec `"SELECT COUNT(*) FROM films WHERE title = '" + input + "'"` sur `st`, puis avec `WHERE title = ?` en `PreparedStatement`.
  → `D08 : Statement 2, PreparedStatement 0`
- ☐ **D09.** `ps = conn.prepareStatement("SELECT title FROM films")`. Ajoute à une liste le SQLState de chacun de ces appels (chacun attrapé) :
  1. `ps.executeQuery("SELECT * FROM films")` ;
  2. `ps.addBatch("DELETE FROM films")`.
  
  → `D09 : [90130, 90130]`
- ☐ **D10.** `st.executeUpdate("CREATE TABLE shows (id INT PRIMARY KEY, starts TIMESTAMP NOT NULL, doors TIME NOT NULL)")`. Insère, avec un `PreparedStatement` :
  - l'id 1 ;
  - `setTimestamp(2, Timestamp.valueOf(LocalDateTime.of(2026, 3, 1, 20, 30)))` ;
  - `setTime(3, Time.valueOf(LocalTime.of(19, 45)))`.
  
  Puis `SELECT starts, doors FROM shows`, et affiche, séparés par ` | ` :
  - `getTimestamp(1)` ;
  - `getTime("doors")` ;
  - `starts = getObject("starts", LocalDateTime.class)` ;
  - `getTime(2).toLocalTime().isBefore(starts.toLocalTime())`.
  
  → `D10 : 2026-03-01 20:30:00.0 | 19:45:00 | 2026-03-01T20:30 | true`

## Expériences (hors sortie attendue)

1. D05 : pourquoi Brazil (note `NULL`) n'est-il pas supprimé par `rating < 8.3` ?
2. D08 : écris la requête **exacte** reçue par la base. Que fait `--` ?
3. Remplace `setNull(3, Types.DOUBLE)` par `setObject(3, null)` : cela marche-t-il avec H2 ? La Javadoc le promet-elle partout ?

## Sortie attendue complète

```
D01 : 3
D02 : 1979 true java.sql.Date
D03-D04 : [90008, 90012, Alien, 90012]
D05 : 1 puis 0
D06 : ? entre apostrophes 90008
D06 : LIKE ? avec "%li%" Alien
D07 : 1er ferme true, 2e Brazil
D08 : Statement 2, PreparedStatement 0
D09 : [90130, 90130]
D10 : 2026-03-01 20:30:00.0 | 19:45:00 | 2026-03-01T20:30 | true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **`conn.prepareStatement(sql)`** : le SQL est envoyé **une fois**, puis exécuté autant de fois qu'on veut, avec d'autres valeurs. C'est plus rapide, et **sûr**.
- **Les `?`** :
  - numérotés **à partir de 1**, dans l'ordre du texte ;
  - un `?` remplace une **valeur**, jamais un nom de table ou de colonne, ni un mot-clé ;
  - `'%?%'` est un texte, pas un paramètre : écris `LIKE ?` et passe `"%li%"`.
- **Les setters :** `setString`, `setInt`, `setLong`, `setDouble`, `setBoolean`, `setBigDecimal`, `setDate` (un `java.sql.Date`), `setObject` (tout, dont `LocalDate`), `setNull(i, Types.XXX)`.
- **Les erreurs** (des `SQLException`) :
  - un index hors limites : H2 dit `90008` ;
  - un `?` sans valeur au moment d'exécuter : H2 dit `90012`.
- **Entre deux exécutions :**
  - les valeurs **restent** posées ;
  - `clearParameters()` les efface toutes ;
  - une nouvelle exécution **ferme** le `ResultSet` précédent du même `PreparedStatement`.
- **Le piège des méthodes héritées :** `PreparedStatement` hérite de `executeQuery(String)`, `executeUpdate(String)`, `execute(String)` et `addBatch(String)` de `Statement`. Les appeler sur un `PreparedStatement` **compile**, mais lève une `SQLException` à l'exécution. Sur un `PreparedStatement`, on appelle toujours la version **sans** argument.
- **Les dates :**

  | Type SQL | Getter / setter | Classe `java.sql` | Pont vers `java.time` |
  |---|---|---|---|
  | `DATE` | `getDate` / `setDate` | `Date` | `toLocalDate()`, `Date.valueOf(LocalDate)` |
  | `TIME` | `getTime` / `setTime` | `Time` | `toLocalTime()`, `Time.valueOf(LocalTime)` |
  | `TIMESTAMP` | `getTimestamp` / `setTimestamp` | `Timestamp` | `toLocalDateTime()`, `Timestamp.valueOf(LocalDateTime)` |

  - `getDate` rend un `java.sql.Date` (une sous-classe de `java.util.Date`) ;
  - `getObject(col, LocalDate.class)` rend un `LocalDate` (JDBC 4.2) ;
  - `java.sql.Date.valueOf(LocalDate)` et `date.toLocalDate()` font le pont.
- **`NULL` en SQL :** `x < 8.3`, `x = NULL` et `x <> 1` sont **inconnus** quand `x` vaut `NULL`. Pour le tester : `IS NULL` / `IS NOT NULL`.
- **L'injection :** coller une saisie dans du SQL laisse l'utilisateur **écrire du SQL**. Le `?` transporte une valeur : l'apostrophe reste une lettre.

</details>
