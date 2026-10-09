package ch19_final.projects.p02_store.solution;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MigrationsTest {

    private JdbcDataSource dataSource;
    private Connection keeper;
    private Transactions tx;

    // Une base neuve en memoire pour chaque test. Elle vit tant qu'une connexion est ouverte : keeper la garde.
    @BeforeEach
    void freshDatabase() throws SQLException {
        dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:" + UUID.randomUUID());
        keeper = dataSource.getConnection();
        tx = new Transactions(dataSource);
    }

    @AfterEach
    void dropDatabase() throws SQLException {
        keeper.close();
    }

    private List<String> query(String sql) throws SQLException {
        List<String> rows = new ArrayList<>();
        try (PreparedStatement ps = keeper.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rows.add(rs.getString(1));
            }
        }
        return rows;
    }

    @Test
    void firstRunAppliesEverythingInOrder() throws SQLException {
        assertEquals(List.of(1, 2, 3), Migrations.apply(tx, TaskSchema.MIGRATIONS));
        assertEquals(List.of("1 taches", "2 historique des deplacements", "3 verrou optimiste et index"),
                query("SELECT version || ' ' || description FROM schema_version ORDER BY version"));
        assertEquals(List.of("ID", "TITLE", "COL", "POINTS", "VERSION"),
                query("SELECT column_name FROM information_schema.columns WHERE table_name = 'TASK' ORDER BY ordinal_position"));
    }

    @Test
    void secondRunAppliesNothing() {
        Migrations.apply(tx, TaskSchema.MIGRATIONS);
        assertEquals(List.of(), Migrations.apply(tx, TaskSchema.MIGRATIONS));
        assertEquals(List.of(), Migrations.apply(tx, TaskSchema.MIGRATIONS));
    }

    @Test
    void onlyNewMigrationsAreApplied() throws SQLException {
        assertEquals(List.of(1, 2), Migrations.apply(tx, TaskSchema.MIGRATIONS.subList(0, 2)));
        // L'application a grandi : la migration 3 arrive avec la version suivante.
        assertEquals(List.of(3), Migrations.apply(tx, TaskSchema.MIGRATIONS));
        assertEquals(List.of("1", "2", "3"), query("SELECT version FROM schema_version ORDER BY version"));
    }

    @Test
    void existingRowsGetTheDefaultVersion() throws SQLException {
        Migrations.apply(tx, TaskSchema.MIGRATIONS.subList(0, 1));
        keeper.createStatement().execute("INSERT INTO task (title, col, points) VALUES ('Pneu', 'A faire', 2)");
        Migrations.apply(tx, TaskSchema.MIGRATIONS);
        assertEquals(List.of("0"), query("SELECT version FROM task"));
    }

    @Test
    void modifiedMigrationIsRefusedBeforeAnyChange() throws SQLException {
        Migrations.apply(tx, TaskSchema.MIGRATIONS.subList(0, 1));
        List<Migration> edited = List.of(
                new Migration(1, "les taches", TaskSchema.MIGRATIONS.get(0).statements()),
                TaskSchema.MIGRATIONS.get(1));
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> Migrations.apply(tx, edited));
        assertEquals("migration 1 modifiee apres application : \"taches\" devient \"les taches\"", e.getMessage());
        assertEquals(List.of("1"), query("SELECT version FROM schema_version"));
        assertEquals(List.of(), query("SELECT table_name FROM information_schema.tables WHERE table_name = 'TASK_EVENT'"));
    }

    @Test
    void versionsMustIncrease() {
        List<Migration> swapped = List.of(TaskSchema.MIGRATIONS.get(1), TaskSchema.MIGRATIONS.get(0));
        assertEquals("versions non croissantes : 1 apres 2",
                assertThrows(IllegalArgumentException.class, () -> Migrations.apply(tx, swapped)).getMessage());
        List<Migration> twice = List.of(TaskSchema.MIGRATIONS.get(0), new Migration(1, "encore", List.of("SELECT 1")));
        assertEquals("versions non croissantes : 1 apres 1",
                assertThrows(IllegalArgumentException.class, () -> Migrations.apply(tx, twice)).getMessage());
        assertEquals("version invalide : 0",
                assertThrows(IllegalArgumentException.class, () -> new Migration(0, "zero", List.of())).getMessage());
    }

    @Test
    void gapsInVersionsAreAllowed() {
        List<Migration> withGap = List.of(new Migration(10, "a", List.of("CREATE TABLE a (id INT)")),
                new Migration(20, "b", List.of("CREATE TABLE b (id INT)")));
        assertEquals(List.of(10, 20), Migrations.apply(tx, withGap));
    }

    @Test
    void brokenMigrationStopsAndKeepsThePreviousOnes() throws SQLException {
        List<Migration> broken = List.of(TaskSchema.MIGRATIONS.get(0),
                new Migration(2, "faute de frappe", List.of("CREAT TABLE oops (id INT)")),
                TaskSchema.MIGRATIONS.get(2));
        StoreException e = assertThrows(StoreException.class, () -> Migrations.apply(tx, broken));
        assertTrue(e.sqlState().startsWith("42"), e.sqlState());
        assertEquals("erreur de base de donnees (SQLState " + e.sqlState() + ")", e.getMessage());
        assertEquals(List.of("1"), query("SELECT version FROM schema_version"));
        // Corrigee, la migration 2 passe, puis la 3.
        assertEquals(List.of(2, 3), Migrations.apply(tx, TaskSchema.MIGRATIONS));
    }

    @Test
    void migrationListIsCopied() {
        List<String> statements = new ArrayList<>(List.of("CREATE TABLE a (id INT)"));
        Migration m = new Migration(1, "a", statements);
        statements.add("DROP TABLE a");
        assertEquals(1, m.statements().size());
    }
}
