# 🏠 Palais mental — chapitre 15 : la terrasse, stations 7 à 12

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Les stations 1 à 6 de la terrasse gardent le chapitre 14. C'est la **dernière** pièce : ta grande balade du dimanche finit ici.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 7. 🧱 Le garde-corps — la connexion

- **Image :** le garde-corps est un **pont** vers la base de données. Pour le traverser, tu montres une **adresse en trois morceaux séparés par des deux-points** : `jdbc:h2:mem:zoo` (le protocole `jdbc`, le **pilote**, puis le nom de la base). Le **gardien `DriverManager`** te donne un **laissez-passer `Connection`**. Quand tu repars, tout se referme **dans l'ordre inverse** : `ResultSet`, puis `Statement`, puis `Connection`, grâce au `try` avec ressources.
- **À retenir :**
  - URL : `jdbc:sous-protocole:nom` (exemple : `jdbc:h2:mem:zoo`, `jdbc:postgresql://localhost:5432/zoo`) ;
  - `DriverManager.getConnection(url)` ou `(url, utilisateur, motDePasse)` → `Connection` ;
  - fermer la `Connection` ferme ses `Statement`, et fermer un `Statement` ferme son `ResultSet` ;
  - toujours `try (Connection c = …; PreparedStatement ps = …; ResultSet rs = …)`.
- **Mon image :** …

### 8. 💡 La lampe extérieure — `PreparedStatement`

- **Image :** la lampe a des **douilles numérotées à partir de 1** : `?`, `?`, `?`. Tu visses une ampoule dans chaque douille : `setString(1, …)`, `setInt(2, …)`. Trois interrupteurs : **`executeQuery`** allume une **liste de résultats** (`ResultSet`), **`executeUpdate`** te dit **combien de lignes** ont changé (un `int`), **`execute`** te répond **vrai** s'il y a une liste, **faux** sinon.
- **À retenir :**
  - `c.prepareStatement("SELECT … WHERE id = ?")` ; les paramètres commencent à **1** ;
  - `setString`, `setInt`, `setDouble`, `setObject`, `setNull` ; un paramètre oublié → `SQLException` ;
  - `executeQuery()` → `ResultSet` (pour un `SELECT`) ; `executeUpdate()` → `int` (lignes modifiées) ; `execute()` → `boolean` (`true` s'il y a un `ResultSet`) ;
  - `PreparedStatement` protège de l'**injection SQL** : ne jamais coller une saisie dans le texte SQL.
- **Mon image :** …

### 9. 🚿 Le tuyau d'arrosage — `ResultSet`

- **Image :** le tuyau est enroulé, et le **curseur** est posé **avant la première spire**. Pour arroser, tu dois **tirer** une première fois : `next()`. Chaque `next()` avance d'une ligne et répond **vrai**, jusqu'au bout où il répond **faux**. Les colonnes se lisent par **nom** (`getString("nom")`) ou par **numéro, à partir de 1** (`getInt(1)`). Lire sans avoir tiré, ou une colonne qui n'existe pas : `SQLException`.
- **À retenir :**
  - le curseur commence **avant** la première ligne : toujours `next()` d'abord ;
  - `while (rs.next()) { … }` ; `if (rs.next())` pour une seule ligne ;
  - colonnes par nom ou par indice **à partir de 1** ; `getString`, `getInt`, `getDouble`, `getObject`, `getBoolean` ;
  - mauvais nom ou indice, ou lecture sans ligne courante → `SQLException`.
- **Mon image :** …

### 10. 🪑 Le transat — `CallableStatement`

- **Image :** tu t'allonges sur le transat et tu **appelles un serveur** de procédures : `{call ma_procedure(?, ?)}`, entre **accolades**. Certaines places sont pour **envoyer** (`IN`), d'autres pour **recevoir** (`OUT`, qu'il faut **réserver d'avance** avec `registerOutParameter`), d'autres pour les deux (`INOUT`).
- **À retenir :**
  - `c.prepareCall("{call proc(?, ?)}")` → `CallableStatement` ;
  - paramètre `IN` : `setXxx` ; `OUT` : `registerOutParameter(i, Types.INTEGER)` **avant** l'exécution, puis `getInt(i)` ; `INOUT` : les deux ;
  - une procédure peut rendre un `ResultSet` (`executeQuery`).
- **Mon image :** …

### 11. 🟫 Le sol de la terrasse — les transactions

- **Image :** chaque **carreau** du sol est une modification. Par défaut, le **ciment prend tout de suite** (`autoCommit` à `true`). Tu coupes le ciment : `setAutoCommit(false)`. Tu poses plusieurs carreaux, puis tu choisis : **`commit()`**, le ciment prend, ou **`rollback()`**, tous les carreaux **sautent**. Tu peux planter un **drapeau `Savepoint`** au milieu et revenir **jusqu'au drapeau** seulement. Remettre `setAutoCommit(true)` **valide** les carreaux en attente.
- **À retenir :**
  - par défaut `autoCommit` = `true` : chaque instruction est validée seule ;
  - `setAutoCommit(false)`, puis `commit()` ou `rollback()` ;
  - `Savepoint sp = c.setSavepoint("nom");` puis `rollback(sp)` ;
  - `setAutoCommit(true)` pendant une transaction **valide** ce qui est en attente ; fermer sans `commit` : résultat dépendant du pilote (ne jamais compter dessus).
- **Mon image :** …

### 12. 🌅 La vue sur le ciel — `SQLException` et les bonnes habitudes

- **Image :** au loin, le ciel affiche les **codes d'orage** : chaque `SQLException` porte un **code standard en cinq caractères** (`getSQLState()`, par exemple `42S02` : table introuvable) et un **code du fabricant** (`getErrorCode()`). Le texte du message (`getMessage()`) change d'une base à l'autre : on se fie au **code**. Un nuage en forme de **seringue** te rappelle l'**injection SQL**.
- **À retenir :**
  - `SQLException` est **vérifiée** ; `getSQLState()` (standard), `getErrorCode()` (propre au fabricant), `getMessage()` ;
  - les interfaces (`Connection`, `Statement`, `ResultSet`…) sont dans `java.sql`, leurs implémentations dans le **pilote** ;
  - `Statement` simple pour du SQL fixe, `PreparedStatement` dès qu'il y a une valeur, `CallableStatement` pour une procédure ;
  - toujours fermer les ressources, du plus récent au plus ancien.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 7 : quels sont les trois morceaux d'une URL JDBC ?
2. Station 8 : que rend `executeUpdate()` ?
3. Station 9 : où est le curseur juste après `executeQuery()` ?
4. Station 10 : que faut-il faire avant d'exécuter une procédure avec un paramètre `OUT` ?
5. Station 11 : que fait `rollback(sp)` ?
6. Station 12 : pourquoi lire `getSQLState()` plutôt que `getMessage()` ?
