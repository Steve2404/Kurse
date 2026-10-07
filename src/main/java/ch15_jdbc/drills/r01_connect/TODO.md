# Drill de rappel 1 — Connexion, `Statement`, `ResultSet`

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p01.

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall01`** dans le paquet `ch15_jdbc.drills.r01_connect`. Le `main` déclare `throws SQLException`.
- Déclare `Connection kept`, `Statement st` et `ResultSet rs` **avant** le try-with-resources de la connexion : ils servent après, au défi D08.
- La connexion : `DriverManager.getConnection("jdbc:h2:mem:r01", "sa", "")`. Garde-la dans `kept`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1, 2 et 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_connect` → **New** → **Java Class** → `Recall01`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall01`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Affiche `conn.getMetaData().getURL()`, `getAutoCommit()` et `isClosed()`.
  → `D01 : jdbc:h2:mem:r01 autoCommit true closed false`
- ☐ **D02.** `st = conn.createStatement()`. Affiche :
  - le résultat de `st.execute("CREATE TABLE pets (id INT PRIMARY KEY, name VARCHAR(20) NOT NULL, age INT)")` ;
  - celui de `st.executeUpdate("INSERT INTO pets VALUES (1, 'Rex', 7), (2, 'Tom', 3), (3, 'Kiki', NULL)")`.
  
  → `D02 : false 3`
- ☐ **D03.** `rs = st.executeQuery("SELECT name, age FROM pets ORDER BY id")`. Pour chaque ligne, lis `getInt("age")`, puis **aussitôt** `wasNull()`. Ajoute à une liste `nom age`, avec `?` à la place d'un âge `NULL` (le nom se lit par son index : `getString(1)`).
  → `D03 : [Rex 7, Tom 3, Kiki ?]`
- ☐ **D04.** `rs = st.executeQuery("SELECT * FROM pets WHERE id = 3")`, puis `next()`. Affiche :
  - `getObject("name").getClass().getSimpleName()` ;
  - `getObject(1).getClass().getSimpleName()` ;
  - `getObject("age")`.
  
  → `D04 : String Integer null`
- ☐ **D05.** Dans cet ordre :
  1. `st.execute("SELECT COUNT(*) FROM pets")` (garde le `boolean`), puis `st.getUpdateCount()` ;
  2. `st.getResultSet()`, `next()`, `getInt(1)` ;
  3. `st.execute("UPDATE pets SET age = age + 1 WHERE age IS NOT NULL")`, puis `st.getUpdateCount()`.
  
  → `D05 : true -1 3 / false 2`
- ☐ **D06.** Quatre erreurs, chacune dans son `try` ; ajoute `getSQLState()` à une liste :
  1. `rs = st.executeQuery("SELECT name FROM pets")`, puis `rs.getString(1)` **avant** `next()` ;
  2. après `rs.next()`, `rs.getString(2)` ;
  3. `st.executeQuery("SELECT * FROM cats")` ;
  4. `st.executeUpdate("INSERT INTO pets VALUES (1, 'Rex', 1)")`.
  
  → `D06 : [02000, 90008, 42S02, 23505]`
- ☐ **D07.** `DriverManager.getConnection("jdbc:nope:r01")`, attrapée.
  → `D07 : 08001`
- ☐ **D08.** **Après** le try : affiche `kept.isClosed()` et `rs.isClosed()`. Puis `st.executeQuery("SELECT 1")`, attrapée.
  → `D08 : connexion fermee true, ResultSet ferme true`
  → `D08 : Statement apres fermeture 90007`

## Expériences (hors sortie attendue)

1. Dans D03, appelle `getString(1)` **avant** `wasNull()` : que devient Kiki ? Pourquoi ?
2. Sans `ORDER BY`, l'ordre des lignes est-il garanti ?
3. Ouvre une 2e connexion vers `jdbc:h2:mem:r01` **après** le try : la table `pets` existe-t-elle ?

## Sortie attendue complète

```
D01 : jdbc:h2:mem:r01 autoCommit true closed false
D02 : false 3
D03 : [Rex 7, Tom 3, Kiki ?]
D04 : String Integer null
D05 : true -1 3 / false 2
D06 : [02000, 90008, 42S02, 23505]
D07 : 08001
D08 : connexion fermee true, ResultSet ferme true
D08 : Statement apres fermeture 90007
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **L'URL JDBC :** `jdbc:<fournisseur>:<reste>`. Exemples :
  - `jdbc:h2:mem:base` ;
  - `jdbc:postgresql://hote:5432/base` ;
  - `jdbc:mysql://hote:3306/base`.
  
  Aucun pilote ne l'accepte : `SQLException` « No suitable driver », SQLState `08001`.
- **`DriverManager.getConnection(url)`**, `(url, user, password)` ou `(url, Properties)`. Depuis JDBC 4, **pas besoin** de `Class.forName` : les pilotes du classpath s'enregistrent seuls.
- **Les interfaces clés** (`java.sql`) : `Driver`, `Connection`, `Statement`, `PreparedStatement`, `CallableStatement`, `ResultSet`. Le fournisseur les **implémente**.
- **Les trois `execute` :**

  | Méthode | Pour | Rend | Mauvais usage |
  |---|---|---|---|
  | `executeQuery` | `SELECT` | `ResultSet` | `SQLException` |
  | `executeUpdate` | DML, DDL | `int` (lignes, 0 pour un DDL) | `SQLException` |
  | `execute` | tout | `boolean` (`true` = ResultSet) | — |

  Après `execute`, lis `getResultSet()` ou `getUpdateCount()` (-1 si ce n'est pas un nombre de lignes).
- **`ResultSet` :**
  - le curseur démarre **avant** la 1re ligne : `while (rs.next())`, ou `if (rs.next())` pour une seule ligne ;
  - les colonnes se lisent par index **à partir de 1**, ou par nom (sans tenir compte de la casse) ;
  - `getInt` d'un `NULL` rend 0 : `wasNull()` regarde la **dernière** colonne lue ;
  - `getObject` rend `null` pour un `NULL`.
- **Fermer :**
  - `Connection` → ses `Statement` → leurs `ResultSet` ;
  - une nouvelle requête sur un `Statement` ferme son `ResultSet` précédent ;
  - try-with-resources ferme dans l'ordre **inverse** de l'ouverture.
- **Les SQLState vus ici :**

  | SQLState | Sens | Standard ? |
  |---|---|---|
  | `02000` | pas de ligne | oui |
  | `08001` | connexion impossible | oui |
  | `23505` | doublon de clé | oui |
  | `42S02` | table inconnue | oui |
  | `90008`, `90007` | | non : propres à H2 |

</details>
