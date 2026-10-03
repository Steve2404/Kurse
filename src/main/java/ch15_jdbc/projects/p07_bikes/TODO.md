# Projet 7 (capstone) — Les vélos en libre-service

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md).

**Notions visées :** **tout le chapitre 15**, dans une seule application :
- connexion et `PreparedStatement` ;
- lots et clés générées ;
- une fonction stockée appelée par `CallableStatement` ;
- des transactions, avec un `Savepoint` ;
- `getObject` et `ResultSetMetaData` ;
- des erreurs de la base, et des erreurs **métier** levées comme des `SQLException`.

Côté algorithmes :
- un tarif par **tranche entamée** (division arrondie vers le haut) ;
- un paiement qui bascule en **dette** quand le crédit ne suffit pas, puis son remboursement ;
- un **interpréteur** de commandes ;
- un **rééquilibrage** des stations, planifié en Java puis envoyé en un seul lot.

**Ce que TU crées :** dans `ch15_jdbc.projects.p07_bikes` : **`public final class Tariff`**, `BikeService`, `Rebalancer` et **`BikeApp`** (le `main`, qui déclare `throws SQLException`).

**Règle du crescendo :** chapitres 1 à 15.

---

## Tableau de bord

### ☐ Étape 1 — L'installation et le tarif

```
installation : 3 stations, 6 velos
tarifs : [0 0 50 50 100 350]
stations : S1 2/3, S2 1/2, S3 3/4
```
- **`public final class Tariff`**, avec `public static int cost(int minutes)` :
  - les minutes payantes valent `max(0, minutes - Data.FREE)` ;
  - le prix vaut le nombre de tranches **entamées** de `Data.STEP` minutes, fois `Data.STEP_PRICE` ;
  - une tranche entamée : `(a + b - 1) / b`.
- **`static void setup(Connection)`** dans `BikeApp` :
  1. exécute `Data.SCHEMA` ;
  2. puis `CREATE ALIAS TARIF FOR "<Tariff.class.getName()>.cost"` ;
  3. deux `PreparedStatement` dans le même try :
     - `INSERT INTO stations VALUES (?, ?, ?)` ;
     - `INSERT INTO bikes VALUES (?, ?, ?, 'OK')`.
     
     Avec `addBatch()` pour chaque ligne de `Data.STATIONS` et de `Data.BIKES`.
  4. affiche la **longueur** des deux `executeBatch()`, les stations **d'abord**.
- **`static List<String> lines(Connection conn, String sql)`**, un affichage générique :
  - chaque ligne vaut ses colonnes `String.valueOf(rs.getObject(i))`, jointes par une espace ;
  - le nombre de colonnes vient de `rs.getMetaData().getColumnCount()`.
  
  Affiche `lines(conn, "SELECT TARIF(20), TARIF(30), TARIF(31), TARIF(45), TARIF(46), TARIF(130)")`.
- **`static Map<String, int[]> occupancy(Connection)`** (une `TreeMap`) : station → `{velos, capacite}`. La requête : `SELECT s.id, s.capacity, COUNT(b.id) AS bikes FROM stations s LEFT JOIN bikes b ON b.station_id = s.id GROUP BY s.id, s.capacity`.
- **`static String show(Map<String, int[]>)`** : `S1 2/3, S2 1/2, …`.
- **Question :** pourquoi envoyer le lot des stations **avant** celui des vélos ?

### ☐ Étape 2 — Le service : une commande = une transaction

```
> membre Ana 300 : ok, membre #1
> membre Ana 50 : refuse : deja inscrit
> louer Ben B1 : refuse : velo B1 indisponible
> louer Zoe B2 : refuse : membre inconnu Zoe
```
- **`final class BikeService`**, construit avec la `Connection` : son constructeur coupe l'auto-commit.
- **Les outils privés :**
  - `refuse(String message)` rend `new SQLException(message, "45000")` (une erreur **métier**) ;
  - `reason(SQLException e)`, un `switch` sur le SQLState : `"45000"` → `getMessage()`, `"23505"` → `deja inscrit`, sinon `erreur <etat>` ;
  - une interface fonctionnelle privée `Work`, avec `String run() throws SQLException` (un `Supplier` ne peut pas lever d'exception vérifiée) ;
  - **`String transaction(Work work)`** : `run()`, puis `commit()`, et rend `ok, <resultat>`. Sur une `SQLException` : `rollback()`, et rend `refuse : <reason>` ;
  - `Integer intOf(String sql, Object... params)` : la 1re colonne de la 1re ligne, ou `null` ;
  - `int update(String sql, Object... params)` : rend `executeUpdate()`.
  
    Ces deux outils passent leurs paramètres avec `setObject(i + 1, params[i])` ;
  - `int memberId(String name)` : `SELECT id FROM members WHERE name = ?`, ou `refuse("membre inconnu " + name)`.
- **Les commandes** passent toutes par `transaction(() -> { … })` :
  - **`register(name, credit)`** : `INSERT INTO members (name, credit) VALUES (?, ?)` avec `RETURN_GENERATED_KEYS`. Rend `membre #<cle>` ;
  - **`rent(name, bike)`** :
    1. un membre avec une location ouverte (`SELECT COUNT(*) FROM rides WHERE member_id = ? AND to_station IS NULL`) est refusé : `location deja en cours` ;
    2. `SELECT station_id FROM bikes WHERE id = ? AND state = 'OK'` : aucune ligne, ou `null`, donne `velo <id> indisponible` ;
    3. `UPDATE bikes SET station_id = NULL WHERE id = ?` ;
    4. `INSERT INTO rides (member_id, bike_id, from_station) VALUES (?, ?, ?)`, avec la clé générée.
    
    Rend `location #<cle> depuis <station>`.
- **`static String run(BikeService, String command)`** dans `BikeApp` : `split(" ")`, puis un `switch` sur le 1er mot (`membre`, `louer`, `rendre`, `recharger`, `maintenance`, sinon `commande inconnue`). Le `main` affiche `> <commande> : <resultat>` pour chaque ligne de `Data.SCRIPT`.

### ☐ Étape 3 — Rendre, payer, rembourser

```
> rendre Ana S2 45 12 : ok, B1 a S2, 45 min, 50 cts
> rendre Ben S2 130 30 : ok, B3 a S2, 130 min, 350 cts, dont 250 en dette
> rendre Ana S2 20 3 : refuse : station S2 pleine
> recharger Ben 400 : ok, credit 150, dette remboursee 250
> maintenance : ok, 2 velos en revision
```
- **`giveBack(name, station, minutes, km)`** :
  1. la location ouverte (`SELECT id, bike_id FROM rides WHERE member_id = ? AND to_station IS NULL`), sinon `aucune location en cours` ;
  2. les places libres : `SELECT capacity - (SELECT COUNT(*) FROM bikes WHERE station_id = s.id) FROM stations s WHERE id = ?`. Si c'est `null` ou `<= 0` : `station <id> pleine` ;
  3. le prix : `{? = call TARIF(?)}` (OUT `Types.INTEGER`) ;
  4. `UPDATE bikes SET station_id = ?, km = km + ? WHERE id = ?`, puis `UPDATE rides SET to_station = ?, minutes = ?, cost = ? WHERE id = ?` ;
  5. **le paiement :** `Savepoint payment = conn.setSavepoint("paiement")`, puis `UPDATE members SET credit = credit - ? WHERE id = ?`. Rend `<velo> a <station>, <minutes> min, <prix> cts`.
     
     Sur une `SQLException` de SQLState `23513` (toute autre est relancée) :
     - `rollback(payment)` ;
     - lis le crédit, puis remets-le à 0 ;
     - la dette vaut prix - crédit : `UPDATE debts SET amount = amount + ? WHERE member_id = ?`, et, si 0 ligne, `INSERT INTO debts (member_id, amount) VALUES (?, ?)` ;
     - rend le même texte, suivi de `, dont <dette> en dette`.
- **`topUp(name, amount)`** :
  - ajoute `amount` au crédit ;
  - s'il y a une dette, rembourse `min(dette, credit)` : baisse le crédit et la dette, puis `DELETE FROM debts WHERE member_id = ? AND amount = 0` ;
  - rend `credit <credit>, dette remboursee <rembourse>`.
- **`maintenance()`** : `UPDATE bikes SET state = 'REVISION', station_id = NULL WHERE km > ? AND station_id IS NOT NULL`, avec `Data.SERVICE_KM`. Rend `<n> velos en revision`.
- **Questions :**
  - pour Ben, le vélo est rendu **même si** le paiement échoue. Quelle ligne du code le garantit ?
  - pourquoi `rendre Ana S2 20 3` ne change-t-il **rien**, même pas le kilométrage ?

### ☐ Étape 4 — Le bilan et le rééquilibrage

```
membres : [Ana 250 0, Ben 150 0]
  trajet 1 Ana B1 S1>S2 45 50
...
recette : [400 4 4] ; en revision : [B3, B5]
reequilibrage : S1 3/3, S2 1/2, S3 0/4 ; mouvements [B4 S1>S3, B6 S1>S3] ; lot [1, 1]
apres : S1 1/3, S2 1/2, S3 2/4
```
- **Le bilan, avec `lines`** :
  - `membres : ` suivi de `SELECT m.name, m.credit, COALESCE(d.amount, 0) FROM members m LEFT JOIN debts d ON d.member_id = m.id ORDER BY m.name` ;
  - chaque ligne de `SELECT r.id, m.name, r.bike_id, r.from_station || '>' || COALESCE(r.to_station, '?'), r.minutes, r.cost FROM rides r JOIN members m ON m.id = r.member_id ORDER BY r.id`, précédée de `  trajet ` ;
  - `recette : ` suivi de `SELECT SUM(cost), COUNT(cost), COUNT(*) FROM rides`, puis ` ; en revision : ` suivi de `SELECT id FROM bikes WHERE state = 'REVISION' ORDER BY id`.
- **`final class Rebalancer`** (pur Java) :
  - `static int target(int capacity)` : `capacity / 2` ;
  - `static List<String[]> plan(Map<String, int[]> occupancy)` : une liste `givers` où chaque station apparaît **une fois par vélo en trop** (présents - cible), et une liste `takers`, une fois par vélo **manquant**. Le mouvement `i` va de `givers.get(i)` à `takers.get(i)`, tant que les deux listes en ont.
- **L'application du plan**, avec deux `PreparedStatement` :
  - `pick` : `SELECT id FROM bikes WHERE station_id = ? ORDER BY km, id LIMIT 1 OFFSET ?` (le vélo le moins usé). L'`OFFSET` vaut le nombre de vélos **déjà choisis** dans cette station : compte-les avec `merge(station, 1, Integer::sum) - 1` ;
  - `move` : `UPDATE bikes SET station_id = ? WHERE id = ?`, avec `addBatch()`.
  
  Note chaque mouvement `<velo> <de>><vers>`. Affiche l'occupation d'avant, les mouvements et `Arrays.toString(move.executeBatch())`. Puis `commit()`, et l'occupation d'après.
- **Questions :**
  - pourquoi faut-il l'`OFFSET` ? Que choisirait `pick` sans lui, puisque le lot n'est pas encore envoyé ?
  - que faudrait-il changer pour qu'une **2e** connexion ne voie jamais un rééquilibrage à moitié fait ?

### Expériences (hors sortie attendue)

1. Ajoute `"rendre Ben S1 50 1"` juste après `"louer Ben B3"` : Ben paie-t-il ? (Indice : 50 minutes.)
2. Supprime `conn.rollback(payment)` : la sortie change-t-elle ? Pourquoi ? (Un ordre qui échoue est annulé **seul** ; un `Savepoint` sert surtout quand **plusieurs** ordres doivent être défaits ensemble.)
3. Remplace `Integer intOf` par `int intOf` : quelle commande plante, et pourquoi ?

---

## Checklist (vérifiée par `Check`)

- `Data.URL`, `Data.SCHEMA`, `Data.STATIONS`, `Data.BIKES`, `Data.FREE`, `Data.STEP`, `Data.STEP_PRICE`, `Data.SERVICE_KM`, `Data.SCRIPT` ;
- `public final class Tariff`, `Tariff.class.getName()`, `CREATE ALIAS TARIF`, `{? = call TARIF(?)}`, `.registerOutParameter(`, `CallableStatement` ;
- `.addBatch()`, `.executeBatch()`, `Statement.RETURN_GENERATED_KEYS`, `.getGeneratedKeys()`, `.setObject(`, `Object... params` ;
- `.setAutoCommit(false)`, `.commit()`, `.rollback()`, `.setSavepoint("paiement")`, `.rollback(payment)`, `new SQLException(`, `"45000"`, `"23513"` ;
- `String run() throws SQLException`, `.getMetaData().getColumnCount()`, `.getObject(`, `LEFT JOIN`, `LIMIT 1 OFFSET ?`, `Integer::sum`.

---

## Sortie attendue complète

```
installation : 3 stations, 6 velos
tarifs : [0 0 50 50 100 350]
stations : S1 2/3, S2 1/2, S3 3/4
> membre Ana 300 : ok, membre #1
> membre Ben 100 : ok, membre #2
> membre Ana 50 : refuse : deja inscrit
> louer Ana B1 : ok, location #1 depuis S1
> louer Ben B1 : refuse : velo B1 indisponible
> louer Ben B3 : ok, location #2 depuis S2
> louer Ana B2 : refuse : location deja en cours
> louer Zoe B2 : refuse : membre inconnu Zoe
> rendre Ana S2 45 12 : ok, B1 a S2, 45 min, 50 cts
> rendre Ben S2 130 30 : ok, B3 a S2, 130 min, 350 cts, dont 250 en dette
> louer Ana B4 : ok, location #3 depuis S3
> rendre Ana S2 20 3 : refuse : station S2 pleine
> rendre Ana S1 20 3 : ok, B4 a S1, 20 min, 0 cts
> recharger Ben 400 : ok, credit 150, dette remboursee 250
> maintenance : ok, 2 velos en revision
> louer Ben B5 : refuse : velo B5 indisponible
> louer Ana B6 : ok, location #4 depuis S3
> rendre Ana S1 10 2 : ok, B6 a S1, 10 min, 0 cts
> rendre Ben S1 5 1 : refuse : aucune location en cours
membres : [Ana 250 0, Ben 150 0]
  trajet 1 Ana B1 S1>S2 45 50
  trajet 2 Ben B3 S2>S2 130 350
  trajet 3 Ana B4 S3>S1 20 0
  trajet 4 Ana B6 S3>S1 10 0
recette : [400 4 4] ; en revision : [B3, B5]
reequilibrage : S1 3/3, S2 1/2, S3 0/4 ; mouvements [B4 S1>S3, B6 S1>S3] ; lot [1, 1]
apres : S1 1/3, S2 1/2, S3 2/4
```
