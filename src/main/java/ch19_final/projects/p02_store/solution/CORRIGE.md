# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`MigrationsTest.java`](MigrationsTest.java) et [`JdbcTaskRepositoryTest.java`](JdbcTaskRepositoryTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17.0.18**, **H2 2.3.232** et **JUnit 5.11.4**, le 9 octobre 2026, lancées avec `-Duser.language=fr`.

---

## Étape 1 — L'ancien code et l'injection SQL

**La sortie de `Data` (vérifiée) :**

```
taches : 7
recherche 'Remise' : [Remise 50% sur les antivols, Remise 500 euros velo cargo]
recherche '50%' : [Remise 50% sur les antivols, Remise 500 euros velo cargo]
erreur : Erreur de syntaxe dans l'instruction SQL "SELECT title FROM task WHERE title LIKE '%'%[*]'"
Syntax error in SQL statement "SELECT title FROM task WHERE title LIKE '%'%[*]'"; SQL statement:
SELECT title FROM task WHERE title LIKE '%'%' [42000-232]
recherche ''' : []
recherche "x' OR 1=1 --" : [Changer la chaine, Regler les freins, Commander 12 chambres a air, Facture Dupont, Remise 50% sur les antivols, Remise 500 euros velo cargo, Graisser le pedalier]
recherche "x'; DROP TABLE task; --" : []
taches : impossible de compter (SQLState 42S04)
```

**Question — les requêtes reçues :**
- `SELECT title FROM task WHERE title LIKE '%x' OR 1=1 --%'` : l'apostrophe du texte **ferme** la chaîne, `OR 1=1` est vrai pour toutes les lignes, et `--` met en commentaire la fin (`%'`) qui aurait cassé la syntaxe. Toute la table sort. Sur une table d'utilisateurs, ce serait tous les comptes.
- `SELECT title FROM task WHERE title LIKE '%x'; DROP TABLE task; --%'` : **deux** instructions. H2 exécute la première (aucun résultat), puis la seconde : la table est **supprimée**, d'où `42S04` (table introuvable) au comptage suivant. Une ligne de recherche a détruit les données.

**Question — `50%` :** dans `LIKE`, `%` est un joker (« n'importe quel texte »). Le motif `%50%%` veut dire « contient 50 » : « Remise 500 euros » le contient. Le problème reste même avec un `PreparedStatement` : c'est l'étape 5.

**Question — les autres erreurs :**

| Erreur | Le problème réel |
|---|---|
| une `Connection` **statique**, ouverte une fois, jamais fermée | un seul fil à la fois peut s'en servir correctement ; une connexion coupée par le réseau ne revient jamais ; les tests se partagent tout |
| `Statement` et `ResultSet` jamais fermés | des ressources perdues côté base ; au bout de quelques milliers d'appels, plus de curseurs disponibles |
| les **nombres** aussi sont collés dans le SQL (`points`) | moins grave ici (un `int`), mais la base ne peut pas réutiliser le plan d'exécution : chaque requête est nouvelle |
| un titre avec une apostrophe (`L'atelier`) **casse** l'`INSERT` | erreur de syntaxe, alors que le titre est parfaitement normal |
| `catch` qui affiche (`printStackTrace`, `System.out`) et continue | l'appelant croit que l'ajout a marché ; `search` rend une liste vide qu'on ne peut pas distinguer de « rien trouvé » |
| `e.getMessage()` affiché | le texte change avec la langue du système (ici en français, en allemand ailleurs) et révèle la requête SQL à l'utilisateur |
| aucune transaction | plusieurs écritures liées ne peuvent pas être annulées ensemble |
| `CREATE TABLE` dans `open` | le schéma est créé par le code au démarrage, sans version : impossible de le faire évoluer (étape 2) |

---

## Étape 2 — Les migrations

Le code : [`Migration.java`](Migration.java), [`Migrations.java`](Migrations.java), [`TaskSchema.java`](TaskSchema.java).

```java
public static List<Integer> apply(Transactions tx, List<Migration> migrations) {
    checkOrder(migrations);
    tx.write(Migrations::createHistoryTable);
    Map<Integer, String> applied = tx.read(Migrations::applied);
    checkUnchanged(migrations, applied);
    List<Integer> done = new ArrayList<>();
    for (Migration m : migrations) {
        if (!applied.containsKey(m.version())) {
            tx.write(connection -> run(connection, m));
            done.add(m.version());
        }
    }
    return done;
}
```

**Expérience — sans keeper (vérifié) :** `apply` échoue tout de suite avec `erreur de base de donnees (SQLState 42S04)`. `createHistoryTable` crée `schema_version` sur sa connexion, puis la ferme : c'était la **seule** connexion, la base en mémoire **disparaît** avec elle. La lecture suivante ouvre une base neuve et vide, où `schema_version` n'existe pas (`42S04` : table introuvable ; la cause dit « this database is empty »). D'où le `keeper`, ouvert pendant tout le test.

**Question — modifier une migration appliquée :** la migration a déjà tourné sur d'autres bases (celle d'un collègue, la base de test, la production). Si on la modifie, les bases **neuves** recevront la nouvelle version, et les anciennes garderont l'ancienne : deux schémas différents pour le même numéro, et des bugs qui n'apparaissent que sur certaines machines. La description sert ici de **somme de contrôle** simplifiée (Flyway, lui, calcule une vraie somme de contrôle du texte SQL) : la modification est détectée **avant** de toucher à quoi que ce soit.

---

## Étape 3 — Le domaine, les erreurs, exécuter autour

Le code : [`Task.java`](Task.java), [`NewTask.java`](NewTask.java), [`StoreException.java`](StoreException.java), [`ConflictException.java`](ConflictException.java), [`SqlWork.java`](SqlWork.java), [`Transactions.java`](Transactions.java).

```java
public <T> T write(SqlWork<T> work) {
    try (Connection connection = dataSource.getConnection()) {
        connection.setAutoCommit(false);
        try {
            T result = work.run(connection);
            connection.commit();
            return result;
        } catch (SQLException | RuntimeException e) {
            connection.rollback();
            throw e;
        }
    } catch (SQLException e) {
        throw new StoreException(e);
    }
}
```

**Question — pourquoi aussi les `RuntimeException` :** le travail peut échouer **sans** erreur SQL : une `ConflictException` ou une `NoSuchElementException` lancée par le dépôt, une `IllegalArgumentException` du domaine, un `NullPointerException`… après avoir déjà écrit. Sans `rollback`, ce qui a été écrit reste en suspens. H2 annule à la fermeture de la connexion, mais la norme JDBC laisse ce comportement **au choix du pilote** : le pilote d'Oracle, lui, **valide** les écritures en suspens quand on ferme la connexion. Un `rollback` explicite rend le code juste avec toutes les bases.

---

## Étape 4 — Créer, relire, paginer

Le code : [`JdbcTaskRepository.java`](JdbcTaskRepository.java), méthodes `create`, `find`, `page`, `countByColumn`, et l'aide `list`.

**Question — la 5 000e page :** la pagination **par clé** (*keyset pagination*) : au lieu de dire « saute 99 980 lignes », le client donne le dernier `id` qu'il a vu, et l'on demande `WHERE id > ? ORDER BY id LIMIT 20`. Grâce à l'index de la clé primaire, la base va **directement** au bon endroit : la 5 000e page coûte autant que la première. En prime, une tâche ajoutée pendant la lecture ne décale pas les pages suivantes. Le prix : on ne peut plus sauter directement à la page 37.

---

## Étape 5 — Chercher sans injection

```java
static String escapeLike(String text) {
    StringBuilder sb = new StringBuilder();
    for (char ch : text.toCharArray()) {
        if (ch == '!' || ch == '%' || ch == '_') {
            sb.append('!');
        }
        sb.append(ch);
    }
    return sb.toString();
}
```

**Question — `a_r` sans échappement (vérifié) :** il trouve « Commander 12 chambres a air » : `_` remplace n'importe quel caractère, et **air** s'écrit a, i, r. Avec l'échappement, `a_r` cherche le texte exact « a_r », qui n'existe pas.

---

## Étape 6 — Le verrou optimiste

```java
private Task updateChecked(Connection c, Task task) throws SQLException {
    try (PreparedStatement ps = c.prepareStatement(
            "UPDATE task SET title = ?, col = ?, points = ?, version = version + 1 WHERE id = ? AND version = ?")) {
        // … les cinq paramètres …
        if (ps.executeUpdate() == 0) {
            throw find(c, task.id()).isPresent() ? new ConflictException(task.id(), task.version()) : notFound(task.id());
        }
    }
    return new Task(task.id(), task.title(), task.column(), task.points(), task.version() + 1);
}
```

Le mutant 13 retire `AND version = ?` : le test d'Ada et Bob échoue (vérifié), parce que l'écriture de Bob passe et remplace le titre d'Ada, sans que personne ne soit prévenu.

**Question — que fait l'application :** elle **ne réessaie pas** à l'aveugle (ce serait écraser Ada exactement comme avant). Elle relit la tâche, montre à l'utilisateur la version actuelle (« Ada a modifié le titre pendant que vous travailliez »), et le laisse décider : fusionner, ou recommencer sa modification. Sur une API web, c'est le code HTTP **409 Conflict** (projet 3).

---

## Étape 7 — La transaction

Le code : `move`, `history`, `delete`.

**Expérience — le DDL (vérifié) :** avec le `CREATE TABLE tmp` entre l'insertion et l'exception, la tâche **reste** (`{A faire=1}`) malgré le `rollback`. Sans le `CREATE TABLE`, elle est bien annulée. Avec H2 (comme avec MySQL et Oracle), une instruction de **définition** (`CREATE`, `ALTER`, `DROP`) valide d'abord la transaction en cours. PostgreSQL, lui, sait annuler un `CREATE TABLE`. Conséquence pour les migrations : avec H2, une migration de plusieurs instructions qui échoue au milieu laisse ses premières instructions appliquées. On écrit donc des migrations **petites**, une idée chacune.

**Question — le format de l'heure :** non. `LocalDateTime.toString()` a une **longueur variable** : les secondes disparaissent quand elles valent zéro, les fractions apparaissent quand elles existent (vérifié : `2026-10-09T10:15`, mais `2026-10-09T22:20:00.681896` dans la démo). Un programme qui découpe le texte à une position fixe se tromperait. Pour un format d'échange, on choisit un format **explicite** : `DateTimeFormatter.ISO_LOCAL_DATE_TIME` écrit toujours les secondes (`2026-10-09T10:15:00`, vérifié), ou `ofPattern("yyyy-MM-dd HH:mm:ss")`. Ici, l'historique est fait pour un humain : `toString` suffit, et les tests figent l'horloge.

---

## Étape 8 — La démonstration, et les mutants

Le code : [`StoreDemo.java`](StoreDemo.java).

**Expérience — deux lancements (vérifié, vers 22 h 20) :**

```
migrations appliquees : [1, 2, 3]
par colonne : {A faire=2, En cours=3, Fini=2}
recherche 50% : [Remise 50% sur les antivols]
recherche injection : []
historique de la tache 3 : [2026-10-09T22:20:00.681896 A faire -> En cours]
```

puis :

```
migrations appliquees : []
par colonne : {A faire=1, En cours=4, Fini=2}
recherche 50% : [Remise 50% sur les antivols]
recherche injection : []
historique de la tache 4 : [2026-10-09T22:20:01.644753 A faire -> En cours]
```

La base est dans un fichier : les données **restent** d'un lancement à l'autre, les migrations ne sont pas refaites, les tâches de départ ne sont pas ajoutées deux fois, et chaque lancement déplace une nouvelle tâche. Après la suppression de `build/ch19`, on retrouve la première sortie. L'injection ne trouve rien, et la table est toujours là.

**Les mutants :** avec les tests de référence, les **21 sont tués** (vérifié). Le tableau de l'indice 2 dit ce que change chacun.
