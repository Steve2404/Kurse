# Drill de rappel 2 — JDBC solide : transactions, migrations, verrou optimiste

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p02.

**Chrono cible :** 35 min, puis 20 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch19_final.drills.r02_jdbc`.
- `Data.SCRIPTS` (fourni) contient les deux scripts du schéma de la banque de l'atelier : le script `i` est la version `i + 1`. La table `account` a `id` (auto-incrémenté), `owner`, `cents` (jamais négatif, contrainte `CHECK`) et, après la version 2, `version`.
- Crée les types ci-dessous, **exactement** avec ces noms et ces signatures. Jamais `getMessage()` d'une `SQLException`, ni `DriverManager` ; `Bank` n'utilise que des `PreparedStatement`. Aucune méthode de plus de 18 lignes.
- Tu n'écris pas de tests : les tests de référence vérifient ton code (avec une base H2 neuve pour chaque défi).

**Les notions de ce drill ont été apprises dans :** projet 2 (étapes 2 à 7).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : les défis dans l'ordre, `// D04 : ✗` après 3 minutes bloqué, `Check.java`, puis la carte mémoire, puis ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `@FunctionalInterface public interface SqlWork<T>` (`T run(Connection connection) throws SQLException`) ; `public class DataException extends RuntimeException`, construite avec une `SQLException` : message `"erreur SQL 23505"` (le SQLState), et `String sqlState()` ; `public final class Tx`, construite avec un `DataSource` : `<T> T read(SqlWork<T> work)` et `<T> T write(SqlWork<T> work)`. `write` valide si tout va bien, annule si le travail lance **n'importe quelle** exception, et la relance telle quelle. Une `SQLException` devient une `DataException`.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** `public final class Schema`, `public static List<Integer> migrate(Tx tx, List<String> scripts)` : crée `schema_version (version INT PRIMARY KEY)` si elle n'existe pas, applique chaque script manquant (le script **et** sa ligne dans `schema_version`, dans la même transaction), et rend les versions appliquées par cet appel.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `public final class Bank`, construite avec un `Tx` : `long open(String owner, long cents)` rend l'identifiant **généré** ; `long balance(long id)` (compte inconnu → `NoSuchElementException("compte inconnu : 9")`). Un solde négatif est refusé par la base : `DataException` de SQLState `23513`.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `void transfer(long from, long to, long cents)`, dans **une** transaction : solde insuffisant → `IllegalArgumentException("solde insuffisant : 1300 < 5000")` ; compte d'arrivée inconnu → `NoSuchElementException("compte inconnu : 77")`, **et le débit est annulé**.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `List<String> search(String text)` : les titulaires dont le nom **contient** `text`, sans tenir compte de la casse, triés ; `%` et `_` sont cherchés **tels quels** (échappés avec `!`, et `!` aussi) ; une injection ne trouve rien.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `boolean rename(long id, String owner, int expectedVersion)` : le verrou optimiste. Vrai si la version était encore celle-là (et elle augmente de 1) ; faux sinon, ou si le compte n'existe pas, et rien ne change.
  → `d06 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **Exécuter autour** : `try (Connection c = ds.getConnection()) { c.setAutoCommit(false); try { T r = work.run(c); c.commit(); return r; } catch (SQLException | RuntimeException e) { c.rollback(); throw e; } } catch (SQLException e) { throw new DataException(e); }`.
- **Les migrations** : lire les versions faites dans un `Set<Integer>`, puis pour chaque script manquant un `tx.write` qui exécute le script **et** insère sa version.
- **La clé générée** : `prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)`, puis `getGeneratedKeys()`, `next()`, `getLong(1)`.
- **Le virement** : une seule connexion pour tout (des méthodes privées qui reçoivent `Connection c`) ; lire le solde, débiter, créditer ; `executeUpdate()` rend 0 pour un compte inconnu : lancer, et le `rollback` défait le débit.
- **`LIKE`** : `LOWER(owner) LIKE ? ESCAPE '!' ORDER BY owner`, avec `"%" + texte échappé + "%"`.
- **Le verrou optimiste** : `UPDATE … SET owner = ?, version = version + 1 WHERE id = ? AND version = ?`, puis `executeUpdate() == 1`.

</details>
