package ch19_final.projects.p02_store;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Migrations.java", "if (!applied.containsKey(m.version())) {", "if (true) {"),
            new Mutant("Migrations.java", "if (known != null && !known.equals(m.description())) {", "if (false) {"),
            new Mutant("Migrations.java", "if (migrations.get(i).version() <= previous) {", "if (migrations.get(i).version() < previous) {"),
            new Mutant("Migrations.java", "                done.add(m.version());\n", ""),
            new Mutant("Transactions.java", "            connection.setAutoCommit(false);\n", ""),
            new Mutant("Transactions.java", "                connection.commit();\n", ""),
            new Mutant("JdbcTaskRepository.java", "escapeLike(text.toLowerCase())", "text.toLowerCase()"),
            new Mutant("JdbcTaskRepository.java", "if (ch == '!' || ch == '%' || ch == '_') {", "if (ch == '%' || ch == '_') {"),
            new Mutant("JdbcTaskRepository.java", "WHERE LOWER(title) LIKE ?", "WHERE title LIKE ?"),
            new Mutant("JdbcTaskRepository.java", "page * size));", "page));"),
            new Mutant("JdbcTaskRepository.java", "size > MAX_PAGE_SIZE", "size > MAX_PAGE_SIZE + 1"),
            new Mutant("JdbcTaskRepository.java", "WHERE ? IS NULL OR col = ?", "WHERE ? IS NULL OR col <> ?"),
            new Mutant("JdbcTaskRepository.java", "WHERE id = ? AND version = ?", "WHERE id = ?"),
            new Mutant("JdbcTaskRepository.java", "task.version() + 1);", "task.version());"),
            new Mutant("JdbcTaskRepository.java", "throw find(c, task.id()).isPresent() ? new ConflictException(task.id(), task.version()) : notFound(task.id());",
                    "throw new ConflictException(task.id(), task.version());"),
            new Mutant("JdbcTaskRepository.java", "ps.setString(2, current.column());\n                ps.setString(3, toColumn);",
                    "ps.setString(2, toColumn);\n                ps.setString(3, current.column());"),
            new Mutant("JdbcTaskRepository.java", "task_event WHERE task_id = ? ORDER BY id", "task_event WHERE task_id = ? ORDER BY id DESC"),
            new Mutant("JdbcTaskRepository.java", "                events.executeUpdate();\n", ""),
            new Mutant("Task.java", "        return title.strip();", "        return title;"),
            new Mutant("Task.java", "if (title.length() > 200) {", "if (title.length() > 201) {"),
            new Mutant("StoreException.java", "this.sqlState = cause.getSQLState();", "this.sqlState = cause.getSQLState().substring(0, 2);"));

    static final List<String> API_CODE = List.of(
            "record Task(", "record NewTask(", "record Migration(", "final class Migrations", "final class TaskSchema",
            "interface SqlWork", "final class Transactions", "class StoreException extends RuntimeException",
            "class ConflictException extends StoreException", "final class JdbcTaskRepository", "final class StoreDemo",
            "DataSource", "PreparedStatement", "setAutoCommit(false)", "commit()", "rollback()", "RETURN_GENERATED_KEYS",
            "getSQLState()", "ESCAPE", "LIMIT ? OFFSET ?", "version = version + 1", "Timestamp", "LocalDateTime.now(clock)",
            "CREATE TABLE IF NOT EXISTS schema_version", "Data.SEED", "!getMessage()",
            "max:method=15",
            "in:JdbcTaskRepository.java!createStatement##le depot n'utilise que des PreparedStatement",
            "in:JdbcTaskRepository.java!DriverManager##le depot recoit ses connexions (DataSource), il ne les fabrique pas",
            "in:JdbcTaskRepository.java!.now()##l'heure vient de l'horloge injectee",
            "in:JdbcTaskRepository.java!setAutoCommit##les transactions sont l'affaire de Transactions",
            "in:JdbcTaskRepository.java!printStackTrace##une erreur se traduit, elle ne s'affiche pas",
            "in:Transactions.java!printStackTrace##une erreur se traduit, elle ne s'affiche pas");

    static final List<String> API_TESTS = List.of(
            "JdbcDataSource", "@BeforeEach", "@AfterEach", "UUID", "Clock.fixed(", "@ParameterizedTest", "assertThrows(",
            "sqlState()", "DROP TABLE", "!System.out", "!Thread.sleep", "!jdbc:h2:./");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 40, MUTANTS, API_CODE, API_TESTS);
    }
}
