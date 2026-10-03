# Drill de rappel 3 — `CallableStatement`

> Première fois ? Lis d'abord le mode d'emploi [`ch15_jdbc/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p05.

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`public class Recall03`** dans le paquet `ch15_jdbc.drills.r03_callable`. Le `main` déclare `throws SQLException`.
- **Quatre méthodes `public static`** dans `Recall03` (H2 les appellera) :
  - `int square(int x)` ;
  - `String shout(String s)` : en majuscules, suivi de `!` ;
  - `ResultSet adults(Connection conn, int minAge)` : `SELECT name FROM people WHERE age >= ? ORDER BY name`, rendu **sans fermer** le `PreparedStatement` ;
  - `int birthday(Connection conn, String name)` : `UPDATE people SET age = age + 1 WHERE name = ?`, rend le nombre de lignes.
- La connexion : `jdbc:h2:mem:r03`. Avec un `Statement` :
  - `CREATE TABLE people (name VARCHAR(10) PRIMARY KEY, age INT NOT NULL)` ;
  - `INSERT INTO people VALUES ('Lea', 17), ('Max', 30), ('Ines', 18)` ;
  - puis quatre alias `SQUARE`, `SHOUT`, `ADULTS`, `BIRTHDAY`, sur le modèle `"CREATE ALIAS SQUARE FOR \"" + Recall03.class.getName() + ".square\""`.

## Défis

- ☐ **D01.** Sur le même `Statement` : `SELECT name, SQUARE(age) FROM people ORDER BY name`, chaque ligne au format `nom=carre`.
  → `D01 : [Ines=324, Lea=289, Max=900]`
- ☐ **D02.** `{? = call SQUARE(?)}` : déclare la sortie, passe 12, `execute()`, lis. Puis passe 5 sur le **même** objet, et `execute()` de nouveau.
  → `D02 : 144 25`
- ☐ **D03.** `{call SHOUT(?)}` en IN OUT :
  1. `setString(1, "salut")`, `registerOutParameter(1, Types.VARCHAR)`, `execute()` : garde le résultat ;
  2. repasse ce résultat en entrée, puis `execute()` de nouveau.
  
  → `D03 : SALUT! SALUT!!`
- ☐ **D04.** `{call ADULTS(?)}` avec 18, puis `executeQuery()`. Lis la colonne `name`.
  → `D04 : [Ines, Max]`
- ☐ **D05.** `{? = call BIRTHDAY(?)}` avec `"Lea"`. Puis `SELECT age FROM people WHERE name = 'Lea'`.
  → `D05 : 1 ligne, Lea a 18 ans`
- ☐ **D06.** `prepareCall("{call ADULTS(?)}", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)`, avec 0. Sur le `ResultSet` obtenu, affiche :
  - `getType() == ResultSet.TYPE_FORWARD_ONLY` ;
  - `getConcurrency() == ResultSet.CONCUR_READ_ONLY`.
  
  → `D06 : true true`
- ☐ **D07.** Deux erreurs ; ajoute leur SQLState à une liste :
  1. `conn.prepareCall("{call NOPE()}")` ;
  2. `{? = call SQUARE(?)}` avec la sortie déclarée, mais **sans** le paramètre 2, puis `execute()`.
  
  → `D07 : [90022, 90012]`
- ☐ **D08.** **Après** le try de la connexion, affiche :
  - `PreparedStatement.class.isAssignableFrom(CallableStatement.class)` ;
  - `Statement.class.isAssignableFrom(PreparedStatement.class)`.
  
  → `D08 : true true`

## Expériences (hors sortie attendue)

1. Rends `square` non `public` : à quel moment l'erreur arrive-t-elle ?
2. Dans D02, appelle `cs.getInt(1)` **avant** `execute()`.
3. Écris `{call SQUARE(?)}` (sans `? =`) avec `executeQuery()` : que contient la 1re colonne ?

## Sortie attendue complète

```
D01 : [Ines=324, Lea=289, Max=900]
D02 : 144 25
D03 : SALUT! SALUT!!
D04 : [Ines, Max]
D05 : 1 ligne, Lea a 18 ans
D06 : true true
D07 : [90022, 90012]
D08 : true true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **`conn.prepareCall(sql)`** rend un **`CallableStatement`**, qui hérite de `PreparedStatement`, qui hérite de `Statement`.
- **La syntaxe d'échappement JDBC** (le pilote la traduit pour sa base) :
  - `{call proc(?, ?)}` ;
  - `{? = call fonction(?)}`.
- **Les paramètres :**

  | Sorte | Avant `execute()` | Après |
  |---|---|---|
  | IN | `setXxx(i, v)` | — |
  | OUT | `registerOutParameter(i, Types.XXX)` | `getXxx(i)` |
  | IN OUT | les deux, sur le **même** index | `getXxx(i)` |

  Les index comptent **tous** les `?`, y compris celui de gauche dans `{? = call …}`. On peut aussi nommer : `cs.setInt("nom", v)`, si la base le permet.
- **Exécuter :**
  - `execute()` ;
  - `executeQuery()` si la procédure rend un `ResultSet` ;
  - `executeUpdate()` si elle ne rend qu'un nombre de lignes.
- **`prepareCall(sql, type, concurrence)`** :
  - `ResultSet.TYPE_FORWARD_ONLY` / `TYPE_SCROLL_INSENSITIVE` / `TYPE_SCROLL_SENSITIVE` ;
  - `ResultSet.CONCUR_READ_ONLY` / `CONCUR_UPDATABLE`.
  
  Le même trio existe pour `createStatement` et `prepareStatement`.
- **H2 :**
  - `CREATE ALIAS NOM FOR "paquet.Classe.methode"` : la classe et la méthode doivent être `public static` ;
  - un 1er paramètre `Connection` est injecté par H2 ;
  - une fonction qui rend un `ResultSet` est aussi appelée à la **préparation** : elle ne doit rien modifier.
- **Ailleurs :** PostgreSQL et MySQL écrivent la procédure en SQL (`CREATE PROCEDURE …`). Le code JDBC, lui, ne change **pas**.

</details>
