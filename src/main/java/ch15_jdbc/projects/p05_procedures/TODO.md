# Projet 5 — Le programme de fidélité (procédures stockées, `CallableStatement`)

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 15) :**
- **`CallableStatement`**, créé par `conn.prepareCall(…)`, avec la syntaxe d'échappement JDBC :
  - `{call NOM(?, ?)}` pour une procédure ;
  - `{? = call NOM(?)}` quand elle rend une valeur ;
- **les trois sortes de paramètres :**

  | Sorte | Avant `execute()` | Après |
  |---|---|---|
  | **IN** | `setXxx(i, valeur)` | — |
  | **OUT** | `registerOutParameter(i, Types.XXX)` | `getXxx(i)` |
  | **IN OUT** | `setXxx(i, v)` **et** `registerOutParameter(i, …)` | `getXxx(i)` |

- **une procédure qui rend un `ResultSet`** : `executeQuery()` sur le `CallableStatement` ;
- `prepareCall(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)` : on peut préciser le curseur, comme pour `prepareStatement` ;
- **les procédures avec H2** : ce sont des méthodes Java `public static`, enregistrées par `CREATE ALIAS NOM FOR "paquet.Classe.methode"`. Un 1er paramètre `Connection` est fourni par H2 (l'appelant ne le passe pas).

Côté algorithmes :
- l'**algorithme de Luhn** (la clé de contrôle des cartes bancaires) ;
- un barème de points ;
- une montée de niveau.

Ces fonctions servent à la fois **depuis Java** et **dans le SQL**.

**Ce que TU crées :** dans `ch15_jdbc.projects.p05_procedures` : **`public final class StoredProcs`** (les procédures) et **`LoyaltyApp`** (le `main`, qui déclare `throws SQLException`).

**Règle du crescendo :** chapitres 1 à 15.

---

## Tableau de bord

### ☐ Étape 1 — Écrire et enregistrer les procédures

```
alias : 6 procedures enregistrees
cartes refusees par LUHN en SQL : [4539 1488 0343 6468, 1234 5678 9012 3456]
```
- **Dans `public final class StoredProcs`** (H2 la charge par son nom : elle **doit** être `public`), un constructeur privé et ces méthodes `public static` :
  - `boolean luhn(String card)` :
    - enlève les espaces ;
    - parcours les chiffres **de droite à gauche** (rang `i` = 0, 1, 2…) ;
    - aux rangs impairs, double le chiffre, et retire 9 s'il dépasse 9 ;
    - la carte est valide si la somme est un multiple de 10 ;
  - `int points(int amount, String tier)` : `amount / 10 * facteur`, plus 5 si `amount >= 100`. Le facteur vient d'un `switch` : `GOLD` → 3, `SILVER` → 2, sinon 1 ;
  - `String nextTier(String tier)` : `BRONZE` → `SILVER`, sinon `GOLD` ;
  - les trois autres, à l'étape 3.
- **Dans le `main`**, une connexion pour tout le programme :
  - exécute `Data.SCHEMA` ;
  - puis, avec `String cls = StoredProcs.class.getName()`, six `CREATE ALIAS` sur le modèle `"CREATE ALIAS LUHN FOR \"" + cls + ".luhn\""` :

    | Alias | Méthode |
    |---|---|
    | `LUHN` | `luhn` |
    | `POINTS` | `points` |
    | `NEXT_TIER` | `nextTier` |
    | `CREDIT_PURCHASES` | `creditPurchases` |
    | `TOP_CUSTOMERS` | `topCustomers` |
    | `PROMOTE` | `promote` |

    Un tableau `String[][]` de paires et une boucle évitent six copies ;
  - affiche le nombre d'alias.
- **Les données :** deux `PreparedStatement` dans le même try :
  - `INSERT INTO customers VALUES (?, ?, ?, 0)` pour `Data.CUSTOMERS` (`id|nom|niveau`) ;
  - `INSERT INTO purchases (customer_id, amount, card) VALUES (?, ?, ?)` pour `Data.PURCHASES` (`id client|montant|carte`).
- **Dans le SQL :** `SELECT card FROM purchases WHERE NOT LUHN(card) ORDER BY id`.
- **Question :** pourquoi `getName()`, et pas le nom écrit à la main ? (Pense à la solution, rangée dans un autre paquet.)

### ☐ Étape 2 — OUT et IN OUT

```
OUT : POINTS(120, GOLD) = 41, POINTS(120, BRONZE) = 17
OUT : LUHN(7992 7398 713) = true
IN OUT : BRONZE -> SILVER -> GOLD -> GOLD
```
- **`{? = call POINTS(?, ?)}`** : le `?` de gauche est le paramètre **1**. Déclare-le avec `registerOutParameter(1, Types.INTEGER)`, puis `setInt(2, 120)`, `setString(3, "GOLD")`, `execute()` et `getInt(1)`. **Réutilise** le même `CallableStatement` : change seulement le paramètre 3 (`"BRONZE"`), puis `execute()` de nouveau.
- **`{? = call LUHN(?)}`** : `Types.BOOLEAN`, la carte `"7992 7398 713"`, puis `getBoolean(1)`.
- **`{call NEXT_TIER(?)}`** en IN OUT : une liste qui commence par `"BRONZE"`. Trois fois de suite :
  - `setString(1, <dernier element>)` ;
  - `registerOutParameter(1, Types.VARCHAR)` ;
  - `execute()` ;
  - ajoute `getString(1)` à la liste.
  
  Affiche la liste, jointe par `" -> "`.
- **Question :** que se passe-t-il si tu appelles `getInt(1)` sans `registerOutParameter` ?

### ☐ Étape 3 — Les procédures qui travaillent sur la base

```
CREDIT_PURCHASES : 6 achats credites ; {Ana=GOLD/53, Ben=SILVER/45, Cleo=BRONZE/36, Dan=BRONZE/6}
TOP_CUSTOMERS(3) : [Ana=53, Ben=45, Cleo=36]
PROMOTE(30) : 2 promus ; {Ana=GOLD/53, Ben=GOLD/45, Cleo=SILVER/36, Dan=BRONZE/6}
```
- **Dans `StoredProcs`**, chacune reçoit la `Connection` en **1er** paramètre :
  - `int creditPurchases(Connection conn)` :
    - lis `SELECT p.customer_id, p.amount, p.card, c.tier FROM purchases p JOIN customers c ON c.id = p.customer_id ORDER BY p.id` ;
    - pour chaque achat dont la carte passe `luhn`, empile (`addBatch`) un `UPDATE customers SET points = points + ? WHERE id = ?`, avec `points(montant, niveau)` ;
    - envoie le lot à la fin, et rends le nombre d'achats crédités ;
  - `ResultSet topCustomers(Connection conn, int n)` : `SELECT name, points FROM customers ORDER BY points DESC, name LIMIT ?`. Rends `executeQuery()`, **sans fermer** le `PreparedStatement` ;
  - `int promote(Connection conn, int threshold)` : `UPDATE customers SET tier = NEXT_TIER(tier) WHERE points >= ? AND tier <> 'GOLD'` (ta fonction, appelée **dans** le SQL). Rends le nombre de lignes.
- **`static Map<String, String> customers(Connection)`** (une `TreeMap`) : `SELECT name, tier, points FROM customers`, chaque valeur au format `niveau/points`.
- **Les appels :**
  - `{? = call CREDIT_PURCHASES()}` : OUT seulement, aucun IN. Affiche le résultat et `customers(conn)` ;
  - `prepareCall("{call TOP_CUSTOMERS(?)}", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)` : `setInt(1, 3)`, puis `executeQuery()`. Chaque ligne au format `nom=points` ;
  - `{? = call PROMOTE(?)}`, avec 30. Affiche le résultat et `customers(conn)`.
- **Questions :**
  - pourquoi `topCustomers` ne doit-elle **pas** fermer son `PreparedStatement` ?
  - H2 l'appelle aussi **pendant** `prepareCall` (pour connaître les colonnes). Pourquoi une telle procédure ne doit-elle rien modifier ?

### ☐ Étape 4 — Les erreurs

```
procedure inconnue : 90022
parametre oublie : 90012
```
- `conn.prepareCall("{call BONUS(?)}")`, attrapée.
- `{? = call POINTS(?, ?)}`, avec seulement `registerOutParameter(1, …)` et `setInt(2, 50)`, puis `execute()`, attrapée.

### Expériences (hors sortie attendue)

1. Rends `StoredProcs` non `public` : quel message donne `CREATE ALIAS` ?
2. Appelle `{call LUHN(?)}` (sans `? =`) avec `executeQuery()` : que contient le `ResultSet` ?
3. Avec PostgreSQL ou MySQL, une procédure s'écrit en SQL (`CREATE PROCEDURE … LANGUAGE plpgsql`), mais le code **JDBC** reste le même. Lequel de tes fichiers changerait ?

---

## Checklist (vérifiée par `Check`)

- `Data.URL`, `Data.SCHEMA`, `Data.CUSTOMERS`, `Data.PURCHASES`, `public final class StoredProcs`, `public static boolean luhn(` ;
- `StoredProcs.class.getName()`, `CREATE ALIAS`, `CallableStatement`, `.prepareCall(`, `{? = call`, `{call` ;
- `.registerOutParameter(`, `Types.INTEGER`, `Types.BOOLEAN`, `Types.VARCHAR`, `.getBoolean(1)`, `ResultSet.TYPE_FORWARD_ONLY`, `ResultSet.CONCUR_READ_ONLY` ;
- `Connection conn, int`, `.addBatch()`, `.executeBatch()`, `NEXT_TIER(tier)`, `WHERE NOT LUHN(card)`.

---

## Sortie attendue complète

```
alias : 6 procedures enregistrees
cartes refusees par LUHN en SQL : [4539 1488 0343 6468, 1234 5678 9012 3456]
OUT : POINTS(120, GOLD) = 41, POINTS(120, BRONZE) = 17
OUT : LUHN(7992 7398 713) = true
IN OUT : BRONZE -> SILVER -> GOLD -> GOLD
CREDIT_PURCHASES : 6 achats credites ; {Ana=GOLD/53, Ben=SILVER/45, Cleo=BRONZE/36, Dan=BRONZE/6}
TOP_CUSTOMERS(3) : [Ana=53, Ben=45, Cleo=36]
PROMOTE(30) : 2 promus ; {Ana=GOLD/53, Ben=GOLD/45, Cleo=SILVER/36, Dan=BRONZE/6}
procedure inconnue : 90022
parametre oublie : 90012
```
