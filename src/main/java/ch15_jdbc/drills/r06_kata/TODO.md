# Drill de rappel 6 (test final) — Le carnet de notes

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p07.

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`public class Recall06`** dans le paquet `ch15_jdbc.drills.r06_kata`. Le `main` déclare `throws SQLException`.
- Dans `Recall06` :
  - `record Tracked(String name, List<String> log) implements AutoCloseable`, dont `close()` ajoute `name` à `log` ;
  - `public static int words(String text)` : 0 pour un texte blanc, sinon le nombre de morceaux de `text.trim().split("\\s+")` ;
  - `static int count(Connection)` : `SELECT COUNT(*) FROM notes`.
- Après D01, une connexion vers `jdbc:h2:mem:r06` pour tout le reste. Avec un `Statement` :
  - `CREATE TABLE notes (id INT AUTO_INCREMENT PRIMARY KEY, topic VARCHAR(10) NOT NULL, body VARCHAR(60) NOT NULL, stars INT)` ;
  - l'alias `WORDS` vers `words` (avec `Recall06.class.getName()`).

**Les notions de ce drill ont été apprises dans :** projets 1 à 7 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r06_kata` → **New** → **Java Class** → `Recall06`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall06`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall06`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Un try-with-resources ouvre trois `Tracked` : `c` (`connexion`), `s` (`statement`) et `r` (`resultset`), sur le même `log`. Dans le corps : `log.add("ouvert " + c.name() + ">" + s.name() + ">" + r.name())`. Affiche `log` après le try.
  → `D01 : [ouvert connexion>statement>resultset, resultset, statement, connexion]`
- ☐ **D02.** `conn.setAutoCommit(false)`. Puis un lot `INSERT INTO notes (topic, body, stars) VALUES (?, ?, ?)`, avec les clés générées, pour ces notes (une étoile vide donne `setNull(3, Types.INTEGER)`) :
  ```
  {{"java", "les streams sont paresseux", "5"}, {"sql", "un index accelere la lecture", ""},
   {"java", "var est local", "3"}, {"sql", "commit valide", "4"}, {"java", "record est final", "4"}}
  ```
  `executeBatch()`, lis les clés, puis `commit()`.
  → `D02 : [1, 2, 3, 4, 5]`
- ☐ **D03.** `SELECT id FROM notes ORDER BY id LIMIT ? OFFSET ?` : la page 2, de taille 2. Lis `getString("id")`.
  → `D03 : [3, 4]`
- ☐ **D04.** `SELECT topic, COUNT(*), COUNT(stars), SUM(stars) FROM notes GROUP BY topic ORDER BY topic`. Chaque ligne au format `topic n/notees/somme` (la somme par `getObject(4)`).
  → `D04 : [java 3/3/12, sql 2/1/4]`
- ☐ **D05.** Dans le même try-with-resources :
  - `{? = call WORDS(?)}` avec `"  un  deux trois "` ;
  - `SELECT SUM(WORDS(body)) FROM notes`.
  
  → `D05 : 3 17`
- ☐ **D06.** Dans cet ordre :
  1. `DELETE FROM notes WHERE topic = 'sql'` ;
  2. `sp = conn.setSavepoint("sql-supprime")` ;
  3. `DELETE FROM notes WHERE topic = 'java'` (garde le nombre) ;
  4. `rollback(sp)`, puis `commit()`.
  
  Affiche le nombre gardé, puis `count(conn)`.
  → `D06 : 3 supprimees puis restaurees, il reste 3`
- ☐ **D07.** `SELECT * FROM notes`. Affiche :
  - `getMetaData().getColumnCount()` ;
  - `getMetaData().getColumnName(4)` ;
  - `conn.getMetaData().supportsSavepoints()`.
  
  → `D07 : 4 colonnes, STARS, savepoints true`
- ☐ **D08.** `SELECT stars FROM notes WHERE id = 1`, puis `next()`. `getInt("rating")`, attrapée. Dans le `catch`, lis `getInt("stars")`, puis `wasNull()`.
  → `D08 : 42S22, stars 5 wasNull false`

## Expériences (hors sortie attendue)

1. D01 : lève une exception dans le corps du try. Les trois `close()` ont-ils lieu ? Dans quel ordre ?
2. D06 : remplace `rollback(sp)` par `rollback()`. Que reste-t-il ?
3. D02 : oublie le `commit()`, puis ouvre une 2e connexion : combien de notes voit-elle ?

## Sortie attendue complète

```
D01 : [ouvert connexion>statement>resultset, resultset, statement, connexion]
D02 : [1, 2, 3, 4, 5]
D03 : [3, 4]
D04 : [java 3/3/12, sql 2/1/4]
D05 : 3 17
D06 : 3 supprimees puis restaurees, il reste 3
D07 : 4 colonnes, STARS, savepoints true
D08 : 42S22, stars 5 wasNull false
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Le squelette à savoir écrire les yeux fermés :**
  ```java
  try (Connection conn = DriverManager.getConnection(url, user, password);
       PreparedStatement ps = conn.prepareStatement("SELECT … WHERE x = ?")) {
      ps.setInt(1, 42);
      try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
              … rs.getString("col") …
          }
      }
  }
  ```
- **La fermeture :**
  - try-with-resources ferme dans l'ordre **inverse** : `ResultSet`, puis `Statement`, puis `Connection` ;
  - fermer un `Statement` ferme son `ResultSet` ;
  - fermer la `Connection` ferme tout.
- **Les agrégats SQL et `NULL` :**
  - `COUNT(*)` compte les lignes ;
  - `COUNT(col)`, `SUM`, `AVG` ignorent les `NULL` ;
  - `SUM` d'un ensemble vide vaut `NULL`.
- **Le plan de révision du chapitre :**
  - r01 : les trois `execute`, `ResultSet` ;
  - r02 : les `?` ;
  - r03 : `CallableStatement` ;
  - r04 : les transactions ;
  - r05 : lots, clés, métadonnées.
  
  Chaque ✗ de ce drill renvoie à l'un d'eux.

</details>
