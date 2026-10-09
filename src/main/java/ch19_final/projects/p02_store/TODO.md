# Projet 2 — Les données qui restent : le dépôt des tâches en base H2

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 19) :**
- l'**injection SQL**, la faille la plus connue du web, et sa seule vraie parade : `PreparedStatement` ;
- les **migrations versionnées** (l'idée de Flyway et Liquibase) : faire évoluer le schéma d'une base qui contient déjà des données ;
- le patron « **exécuter autour** » (*execute around*) : ouvrir, travailler, valider ou annuler, fermer, écrit **une seule fois** ;
- la **transaction** : tout ou rien ; et son piège avec H2 et MySQL (une instruction DDL valide toute seule) ;
- le **verrou optimiste** : un numéro de version contre les mises à jour perdues ;
- la **traduction des erreurs** à la frontière : `SQLException` ne sort pas du dépôt ;
- des **tests d'intégration** sur une vraie base, neuve pour chaque test.

**Ce qui est FOURNI :** `Data.java` contient `SEED` (les 7 tâches de départ de l'atelier : titre, colonne, points) et `LegacyTaskDao`, l'ancien accès aux tâches, qui accumule les erreurs classiques du code JDBC. Son `main` montre la pire. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch19_final.projects.p02_store` : `Task`, `NewTask`, `Migration`, `Migrations`, `TaskSchema`, `SqlWork`, `Transactions`, `StoreException`, `ConflictException`, `JdbcTaskRepository`, `StoreDemo`, et tes tests (par exemple `MigrationsTest` et `JdbcTaskRepositoryTest`).

**Règle du crescendo :** tout Java 17, JDBC et H2 (chapitre 15), JUnit et Mockito. Aucune méthode de plus de **15 lignes**. Dans `JdbcTaskRepository` : ni `createStatement`, ni `DriverManager`, ni `.now()` sans horloge, ni `setAutoCommit`. Nulle part : `printStackTrace`, ni `getMessage()` d'une `SQLException` (le texte change avec la langue du système ; on donne le **SQLState**, chapitre 15). Dans tes tests : ni `System.out`, ni `Thread.sleep`, ni base dans un fichier (`jdbc:h2:./…`).

---

## Tableau de bord

### ☐ Étape 1 — L'ancien code et l'injection SQL

**📖 La leçon : l'injection SQL.** Quand un programme **colle** une valeur reçue dans le texte d'une requête (`"… WHERE title LIKE '%" + text + "%'"`), la valeur peut **fermer** la chaîne SQL avec une apostrophe et écrire la suite de la requête à la place du programmeur. C'est l'**injection SQL**, en tête des failles du web depuis vingt ans. Échapper les apostrophes à la main ne suffit pas (il y a toujours un cas oublié). La seule parade sûre : le **`PreparedStatement`**, où la requête a des trous `?` et où chaque valeur part **à part**, comme une donnée, jamais comme du code (chapitre 15).

**👉 À toi :** lance `Data` (le `main` de `Data.java`). Puis lis `LegacyTaskDao` crayon en main.

**❓ Questions :**
- Explique chaque ligne de la sortie. Pour `x' OR 1=1 --` et `x'; DROP TABLE task; --`, écris la requête SQL **complète** que reçoit la base.
- Pourquoi la recherche de `50%` trouve-t-elle aussi « Remise 500 euros » ?
- Fais la liste de **toutes** les autres erreurs de `LegacyTaskDao` (au moins six). Pour chacune : quel problème réel provoque-t-elle ?

### ☐ Étape 2 — Les migrations : un schéma qui évolue

**📖 La leçon : on ne recrée jamais une base de production.** Ton application tourne depuis un an : la base contient les tâches de l'atelier. La nouvelle version a besoin d'une colonne de plus. Impossible de tout effacer et recréer : il faut **faire évoluer** la base. La méthode de tous les outils sérieux (Flyway, Liquibase) :
1. chaque évolution est une **migration** numérotée (1, 2, 3…), écrite une fois pour toutes ;
2. la base retient dans une table (`schema_version`) les migrations **déjà** appliquées ;
3. au démarrage, l'application applique **seulement les nouvelles**, dans l'ordre ;
4. une migration appliquée ne se **modifie jamais** (une autre base l'a peut-être déjà reçue) : on en ajoute une nouvelle.

**📖 La leçon : une base neuve pour chaque test.** Un test d'intégration parle à une **vraie** base. Pour qu'un test ne voie pas les données du précédent : une base H2 **en mémoire** avec un nom unique (`"jdbc:h2:mem:" + UUID.randomUUID()`). Une base en mémoire disparaît quand sa **dernière** connexion se ferme : un champ `keeper` ouvert dans `@BeforeEach` et fermé dans `@AfterEach` la garde en vie pendant le test, puis la libère.

**👉 À toi :**
- `public record Migration(int version, String description, List<String> statements)` : une version < 1 lance `IllegalArgumentException("version invalide : " + version)` ; la liste est copiée ;
- `public final class TaskSchema` avec `public static final List<Migration> MIGRATIONS`, les trois évolutions de l'application :
  1. `"taches"` : `CREATE TABLE task (id BIGINT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(200) NOT NULL, col VARCHAR(20) NOT NULL, points INT NOT NULL CHECK (points >= 0))` ;
  2. `"historique des deplacements"` : `CREATE TABLE task_event (id BIGINT AUTO_INCREMENT PRIMARY KEY, task_id BIGINT NOT NULL REFERENCES task(id), from_col VARCHAR(20) NOT NULL, to_col VARCHAR(20) NOT NULL, at TIMESTAMP NOT NULL, CHECK (from_col <> to_col))` ;
  3. `"verrou optimiste et index"` : deux instructions, `ALTER TABLE task ADD COLUMN version INT DEFAULT 0 NOT NULL` et `CREATE INDEX idx_task_col ON task(col)` ;
- `public final class Migrations` (constructeur privé), `public static List<Integer> apply(Transactions tx, List<Migration> migrations)` :
  - les versions doivent être **strictement croissantes** (des trous sont permis : 10, 20) : sinon `IllegalArgumentException("versions non croissantes : 1 apres 2")` ;
  - crée la table `schema_version (version INT PRIMARY KEY, description VARCHAR(200) NOT NULL)` si elle n'existe pas (`CREATE TABLE IF NOT EXISTS`) ;
  - **avant de rien changer**, vérifie qu'aucune migration déjà appliquée n'a changé de description : sinon `IllegalStateException("migration 1 modifiee apres application : \"taches\" devient \"les taches\"")` ;
  - applique chaque migration manquante dans **sa** transaction (ses instructions, puis sa ligne dans `schema_version`), et rend la liste des versions appliquées par cet appel (`[]` si la base est à jour) ;
  - une instruction fausse arrête tout : la `StoreException` remonte, les migrations précédentes restent appliquées.

Les étapes 2 et 3 se font ensemble : `Migrations` a besoin de `Transactions` (étape 3). Commence par l'étape 3 si tu préfères.

**🧪 Les tests (`MigrationsTest`) :** le premier lancement applique `[1, 2, 3]` (vérifie `schema_version` et les colonnes de `task` dans `information_schema.columns`) ; le second, `[]` ; d'abord `[1, 2]` puis `[3]` ; une tâche créée avant la migration 3 reçoit la version `0` ; une description modifiée est refusée **et rien ne change** ; les versions non croissantes (dans le désordre, en double) ; des trous ; une migration fausse (`CREAT TABLE`) : `StoreException`, la 1 reste, et la liste corrigée applique ensuite `[2, 3]`.

**🧪 Expérience :** dans un test jetable, **supprime** le `keeper` et lance `Migrations.apply` sur une base `jdbc:h2:mem:sanskeeper`. Que se passe-t-il, et pourquoi ?

**❓ Question :** pourquoi est-il dangereux de **modifier** une migration déjà appliquée, plutôt que d'en ajouter une nouvelle ?

### ☐ Étape 3 — Le domaine, les erreurs, et « exécuter autour »

**📖 La leçon : traduire les erreurs à la frontière.** `SQLException` est **vérifiée** (chapitre 11) et parle de JDBC : si elle sortait du dépôt, tout le programme dépendrait de JDBC. Le dépôt la **traduit** en une exception à lui, non vérifiée, qui garde l'original comme **cause** (`super(message, cause)`) et retient le **SQLState** : 5 caractères normalisés (`23505` doublon, `23513` contrainte `CHECK`, `22001` valeur trop longue, `42…` erreur de syntaxe ou objet inconnu).

**📖 La leçon : exécuter autour.** Chaque méthode JDBC répète le même cadre : ouvrir une connexion, désactiver l'auto-commit, travailler, valider (`commit`), **ou** annuler (`rollback`) si quelque chose échoue, toujours fermer. Recopié dans dix méthodes, il sera faux dans une. On l'écrit **une fois**, dans une méthode qui reçoit **le travail** sous forme de lambda : c'est le patron « exécuter autour » (le cousin de la méthode modèle du chapitre 18).

**👉 À toi :**
- `public record Task(long id, String title, String column, int points, int version)` et `public record NewTask(String title, String column, int points)` : les règles du **domaine**, vérifiées avant d'aller en base : titre `null` ou blanc → `IllegalArgumentException("titre vide")`, plus de 200 caractères → `"titre trop long : 201 caracteres (200 au plus)"`, points négatifs → `"points negatifs : -1"` ; le titre est rangé **sans** ses espaces autour (`strip`) ; `column` non `null`. `Task` a aussi `withTitle(String)` et `withColumn(String)`, qui rendent une copie ;
- `public class StoreException extends RuntimeException` : un constructeur `(String message)` (alors `sqlState()` rend `null`) et un constructeur `(SQLException cause)`, message `"erreur de base de donnees (SQLState " + cause.getSQLState() + ")"` ; `String sqlState()` ;
- `public class ConflictException extends StoreException`, constructeur `(long id, int staleVersion)`, message `"tache 3 : version 0 perimee"` ;
- `@FunctionalInterface public interface SqlWork<T>` : `T run(Connection connection) throws SQLException` ;
- `public final class Transactions`, constructeur `Transactions(DataSource dataSource)` :
  - `<T> T read(SqlWork<T> work)` : une connexion (auto-commit), fermée quoi qu'il arrive ;
  - `<T> T write(SqlWork<T> work)` : auto-commit désactivé ; `commit` si tout va bien ; si le travail lance **n'importe quelle** exception (`SQLException` ou `RuntimeException`), `rollback` puis on la relance ;
  - une `SQLException` devient une `StoreException`.

**🧪 Les tests :** les règles du domaine (un `@ParameterizedTest`) ; un `tx.write` qui modifie une ligne puis lance `IllegalStateException("plantage au milieu")` : l'exception arrive **telle quelle**, et la ligne n'a pas changé.

**❓ Question :** pourquoi `write` attrape-t-il aussi les `RuntimeException`, alors que `SqlWork` ne déclare que `SQLException` ?

### ☐ Étape 4 — Créer, relire, paginer

**📖 La leçon : la pagination.** Personne n'affiche 10 000 tâches : on les demande **par pages**. `ORDER BY id LIMIT ? OFFSET ?` : la page `p` de taille `n` saute `p × n` lignes. Sans `ORDER BY`, l'ordre d'une base n'est **garanti nulle part** : deux pages pourraient se chevaucher. Et une taille de page se **limite** (ici 100) : sinon un client demande `size=1000000` et le serveur s'écroule.

**👉 À toi :** `public final class JdbcTaskRepository`, constructeur `(Transactions tx, Clock clock)` :
- `Task create(NewTask task)` : `INSERT` avec `Statement.RETURN_GENERATED_KEYS`, et rend la tâche avec son identifiant et la version `0` ;
- `Optional<Task> find(long id)` ;
- `List<Task> page(String column, int page, int size)` : triée par `id` ; `column` à `null` veut dire « toutes les colonnes » (une seule requête suffit : `WHERE ? IS NULL OR col = ?`) ; une page négative → `IllegalArgumentException("page negative : -1")`, une taille hors de 1 à 100 → `"taille de page invalide : 101 (1 a 100)"` ;
- `Map<String, Integer> countByColumn()` : le nombre de tâches par colonne, colonnes **triées** (`GROUP BY`, une `TreeMap`).

Une erreur de la base (par exemple une colonne de 25 caractères pour `VARCHAR(20)`) donne une `StoreException` de SQLState `22001`.

**🧪 Les tests (`JdbcTaskRepositoryTest`) :** une base neuve et migrée dans `@BeforeEach`, une horloge fixe ; la création (identifiants 1 et 2, titre sans espaces autour), `find` d'une tâche absente ; un titre plein de pièges (`'`, `"`, `;`, `--`, `%`, `_`, `!`, `é`) relu **à l'identique** ; les 7 tâches de `SEED` en pages de 3 (pages 0, 1, 2 et 3), en pages d'une colonne, les tailles refusées ; `countByColumn` ; la `StoreException` `22001` (et sa cause, une `SQLException`).

**❓ Question :** pour afficher la 5 000e page de 20 tâches, `OFFSET 99980` oblige la base à parcourir 99 980 lignes pour les jeter. Comment ferais-tu une pagination qui ne ralentit pas au fil des pages ?

### ☐ Étape 5 — Chercher sans injection

**📖 La leçon : `LIKE` a ses propres jokers.** Le `PreparedStatement` empêche l'injection, mais dans un `LIKE`, `%` (n'importe quel texte) et `_` (n'importe quel caractère) **restent des jokers** : chercher `50%` trouverait « 500 euros ». On les **échappe** avec un caractère choisi, déclaré dans la requête : `LIKE ? ESCAPE '!'`, et l'on écrit `!%`, `!_` et `!!` (le caractère d'échappement lui-même).

**👉 À toi :**
- `static String escapeLike(String text)` : `50%` → `50!%`, `a_b!c` → `a!_b!!c` ;
- `List<Task> search(String text)` : les tâches dont le titre **contient** `text`, sans tenir compte des majuscules (`LOWER(title) LIKE ? ESCAPE '!'`), triées par `id`. La chaîne vide trouve tout.

**🧪 Les tests :** un tableau de recherches (avec `quoteCharacter = '"'` dans `@CsvSource`, puisque les données contiennent des apostrophes) : `remise`, `REMISE 50`, `50%`, `_`, `a_r`, `%`, `!`, la chaîne vide, `x' OR 1=1 --`, `'` ; `x'; DROP TABLE task; --` ne trouve rien **et** la table est toujours là ; `escapeLike` ; `a_b` ne trouve pas `axb`, et `t!` trouve `Urgent!`.

**❓ Question :** `a_r` sans échappement : quelle tâche de `SEED` trouverait-il, et pourquoi ?

### ☐ Étape 6 — Le verrou optimiste

**📖 La leçon : la mise à jour perdue.** Ada et Bob ouvrent la même tâche (version 0). Ada change le titre et enregistre. Bob, qui a encore l'ancienne version sous les yeux, change la colonne et enregistre **toute** la tâche : le titre d'Ada est **écrasé**, sans que personne ne le sache. Le **verrou optimiste** l'empêche sans rien bloquer :
- chaque ligne a un numéro de `version` ;
- on écrit `UPDATE … SET …, version = version + 1 WHERE id = ? AND version = ?` avec la version **lue** ;
- si quelqu'un est passé avant, la version a changé : la requête ne touche **aucune** ligne (`executeUpdate()` rend 0), et l'on prévient l'utilisateur, qui relit et recommence.

« Optimiste » : on parie que les conflits sont rares, et on ne verrouille rien pendant que l'utilisateur réfléchit.

**👉 À toi :** `Task update(Task task)` : enregistre le titre, la colonne et les points ; rend la tâche avec la version **suivante**. Aucune ligne touchée : si la tâche existe, `ConflictException` ; sinon `NoSuchElementException("tache 42 introuvable")`.

**🧪 Les tests :** deux mises à jour de suite (versions 1 puis 2, relues par `find`) ; Ada et Bob : Bob reçoit `tache 1 : version 0 perimee` (et `sqlState()` vaut `null`), le titre d'Ada est intact ; une tâche inconnue ; `ConflictException` est bien une `StoreException`, elle-même une `RuntimeException`.

**❓ Question :** que devrait faire une **application** (pas le dépôt) quand elle reçoit une `ConflictException` ?

### ☐ Étape 7 — La transaction : tout ou rien

**📖 La leçon : plusieurs écritures, une seule décision.** Déplacer une tâche, c'est **deux** écritures : changer sa colonne, et noter le déplacement dans `task_event`. Si la seconde échoue après la première, la base serait **incohérente** (une tâche déplacée sans trace). Dans une transaction, les deux sont validées ensemble, ou annulées ensemble.

**👉 À toi :**
- `Task move(long id, String toColumn, int expectedVersion)` dans **un seul** `tx.write` : relit la tâche (`NoSuchElementException` si elle n'existe pas), la met à jour avec le verrou optimiste, puis insère l'événement (colonne de départ, colonne d'arrivée, heure lue avec `LocalDateTime.now(clock)`, rangée avec `Timestamp.valueOf`). Déplacer vers la **même** colonne : la base refuse l'événement (`CHECK`, SQLState `23513`), **après** la mise à jour… que la transaction doit annuler ;
- `List<String> history(long id)` : `"2026-10-09T10:15 A faire -> En cours"`, du plus ancien au plus récent (`rs.getTimestamp(1).toLocalDateTime()`) ;
- `boolean delete(long id)` : efface l'historique **puis** la tâche (la clé étrangère interdit l'inverse), dans une transaction ; `false` si elle n'existait pas.

**🧪 Les tests :** deux déplacements et leur historique ; une autre horloge (`2026-12-24T18:30:45Z` donne `2026-12-24T18:30:45`) ; le déplacement vers la même colonne : `23513`, et **ni** la tâche **ni** l'historique n'ont changé ; un déplacement avec une version périmée ; une tâche inconnue ; l'effacement.

**🧪 Expérience :** dans un test jetable, fais un `tx.write` qui insère une tâche, **puis** exécute `CREATE TABLE tmp (id INT)`, puis lance une exception. La tâche est-elle annulée ? Recommence sans le `CREATE TABLE`.

**❓ Question :** dans `history`, `LocalDateTime.toString()` affiche `2026-10-09T10:15` pour 10 h 15 min 0 s, mais `2026-12-24T18:30:45` quand il y a des secondes. Est-ce un bon format pour un fichier qu'un autre programme relira ?

### ☐ Étape 8 — La démonstration, et les mutants

**👉 À toi :** `public final class StoreDemo` avec `main` : une base dans un **fichier** (`jdbc:h2:./build/ch19/atelier`, avec un `JdbcDataSource`), les migrations (affiche `migrations appliquees : …`), les tâches de `SEED` si la base est vide, le déplacement de la première tâche « A faire » vers « En cours », puis `par colonne : …`, la recherche de `50%`, celle de l'injection, et l'historique de la tâche déplacée.

**🧪 Expérience :** lance `StoreDemo` **deux fois**. Compare les deux sorties. Puis supprime le dossier `build/ch19` et relance.

**👉 Puis :** lance `Check`. Les 21 mutants touchent les migrations, la transaction, l'échappement, la pagination, le verrou optimiste, l'historique et l'effacement.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Task(`, `record NewTask(`, `record Migration(`, `final class Migrations`, `final class TaskSchema`, `interface SqlWork`, `final class Transactions`, `class StoreException extends RuntimeException`, `class ConflictException extends StoreException`, `final class JdbcTaskRepository`, `final class StoreDemo`, `DataSource`, `PreparedStatement`, `setAutoCommit(false)`, `commit()`, `rollback()`, `RETURN_GENERATED_KEYS`, `getSQLState()`, `ESCAPE`, `LIMIT ? OFFSET ?`, `version = version + 1`, `Timestamp`, `LocalDateTime.now(clock)`, `CREATE TABLE IF NOT EXISTS schema_version`, `Data.SEED` ; jamais `getMessage()`.
- **La conception :** aucune méthode de plus de **15 lignes** ; `JdbcTaskRepository.java` ne contient ni `createStatement`, ni `DriverManager`, ni `.now()`, ni `setAutoCommit` ; ni `JdbcTaskRepository.java` ni `Transactions.java` ne contiennent `printStackTrace`.
- **Tes tests :** au moins **40** tests, `JdbcDataSource`, `@BeforeEach`, `@AfterEach`, `UUID`, `Clock.fixed(`, `@ParameterizedTest`, `assertThrows(`, `sqlState()`, `DROP TABLE` ; ni `System.out`, ni `Thread.sleep`, ni `jdbc:h2:./`.
- **Les 21 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch19_final.projects.p02_store ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 43 tests, 43 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 21/21 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
