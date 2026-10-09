package ch19_final.projects.p02_store.solution;

import ch19_final.projects.p02_store.Data;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcTaskRepositoryTest {

    private static final Clock AT_10_15 = Clock.fixed(Instant.parse("2026-10-09T10:15:00Z"), ZoneOffset.UTC);

    private Connection keeper;
    private Transactions tx;
    private JdbcTaskRepository tasks;

    @BeforeEach
    void freshDatabase() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:" + UUID.randomUUID());
        keeper = dataSource.getConnection();
        tx = new Transactions(dataSource);
        Migrations.apply(tx, TaskSchema.MIGRATIONS);
        tasks = new JdbcTaskRepository(tx, AT_10_15);
    }

    @AfterEach
    void dropDatabase() throws SQLException {
        keeper.close();
    }

    private void seed() {
        for (String[] row : Data.SEED) {
            tasks.create(new NewTask(row[0], row[1], Integer.parseInt(row[2])));
        }
    }

    private static List<String> titles(List<Task> list) {
        return list.stream().map(Task::title).toList();
    }

    // ------------------------------------------------------------------ creer et relire

    @Test
    void createGivesIdsAndVersionZero() {
        Task first = tasks.create(new NewTask("Pneu", "A faire", 2));
        Task second = tasks.create(new NewTask("  Selle  ", "Fini", 0));
        assertEquals(new Task(1, "Pneu", "A faire", 2, 0), first);
        assertEquals(new Task(2, "Selle", "Fini", 0, 0), second);
        assertEquals(Optional.of(second), tasks.find(2));
        assertEquals(Optional.empty(), tasks.find(3));
    }

    @Test
    void textIsStoredExactly() {
        String title = "L'atelier \"Velo\" ; DROP TABLE task; -- 100% _ ! é";
        Task t = tasks.create(new NewTask(title, "A faire", 1));
        assertEquals(title, tasks.find(t.id()).orElseThrow().title());
        assertEquals(Map.of("A faire", 1), tasks.countByColumn());
    }

    @Test
    void countByColumnIsSorted() {
        seed();
        assertEquals("{A faire=3, En cours=2, Fini=2}", tasks.countByColumn().toString());
    }

    // ------------------------------------------------------------------ les regles du domaine

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "''    | 1  | titre vide",
            "'   ' | 1  | titre vide",
            "Pneu  | -1 | points negatifs : -1"})
    void domainRulesComeBeforeTheDatabase(String title, int points, String message) {
        assertEquals(message, assertThrows(IllegalArgumentException.class,
                () -> new NewTask(title, "A faire", points)).getMessage());
        assertEquals(Map.of(), tasks.countByColumn());
    }

    @Test
    void titleLengthIsLimitedTo200() {
        assertEquals(200, new NewTask("x".repeat(200), "A faire", 1).title().length());
        assertEquals("titre trop long : 201 caracteres (200 au plus)",
                assertThrows(IllegalArgumentException.class, () -> new NewTask("x".repeat(201), "A faire", 1)).getMessage());
        assertEquals("titre vide", assertThrows(IllegalArgumentException.class,
                () -> new NewTask(null, "A faire", 1)).getMessage());
    }

    @Test
    void databaseRulesBecomeStoreExceptionsWithSqlState() {
        StoreException e = assertThrows(StoreException.class,
                () -> tasks.create(new NewTask("Pneu", "Une colonne bien trop longue", 1)));
        assertEquals("22001", e.sqlState());
        assertEquals("erreur de base de donnees (SQLState 22001)", e.getMessage());
        assertTrue(e.getCause() instanceof SQLException);
    }

    // ------------------------------------------------------------------ pages et recherche

    @Test
    void pagesAreSortedById() {
        seed();
        assertEquals(List.of("Changer la chaine", "Regler les freins", "Commander 12 chambres a air"),
                titles(tasks.page(null, 0, 3)));
        assertEquals(List.of("Facture Dupont", "Remise 50% sur les antivols", "Remise 500 euros velo cargo"),
                titles(tasks.page(null, 1, 3)));
        assertEquals(List.of("Graisser le pedalier"), titles(tasks.page(null, 2, 3)));
        assertEquals(List.of(), tasks.page(null, 3, 3));
    }

    @Test
    void pagesOfOneColumn() {
        seed();
        assertEquals(List.of("Commander 12 chambres a air", "Facture Dupont"), titles(tasks.page("A faire", 0, 2)));
        assertEquals(List.of("Remise 50% sur les antivols"), titles(tasks.page("A faire", 1, 2)));
        assertEquals(List.of(), tasks.page("Archive", 0, 10));
    }

    @Test
    void pageSizeIsChecked() {
        seed();
        assertEquals(7, tasks.page(null, 0, 100).size());
        assertEquals("taille de page invalide : 101 (1 a 100)",
                assertThrows(IllegalArgumentException.class, () -> tasks.page(null, 0, 101)).getMessage());
        assertEquals("taille de page invalide : 0 (1 a 100)",
                assertThrows(IllegalArgumentException.class, () -> tasks.page(null, 0, 0)).getMessage());
        assertEquals("page negative : -1",
                assertThrows(IllegalArgumentException.class, () -> tasks.page(null, -1, 10)).getMessage());
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', quoteCharacter = '"', value = {
            "remise        | Remise 50% sur les antivols,Remise 500 euros velo cargo",
            "REMISE 50     | Remise 50% sur les antivols,Remise 500 euros velo cargo",
            "50%           | Remise 50% sur les antivols",
            "_             | ",
            "a_r           | ",
            "%             | Remise 50% sur les antivols",
            "!             | ",
            "\"\"          | Changer la chaine,Regler les freins,Commander 12 chambres a air,Facture Dupont,Remise 50% sur les antivols,Remise 500 euros velo cargo,Graisser le pedalier",
            "x' OR 1=1 --  | ",
            "'             | "})
    void searchFindsTextLiterally(String text, String expected) {
        seed();
        List<String> wanted = expected == null ? List.of() : List.of(expected.split(","));
        assertEquals(wanted, titles(tasks.search(text)));
    }

    @Test
    void injectionDoesNotDropTheTable() {
        seed();
        assertEquals(List.of(), tasks.search("x'; DROP TABLE task; --"));
        assertEquals(7, tasks.page(null, 0, 100).size());
    }

    @Test
    void likeEscapes() {
        assertEquals("50!%", JdbcTaskRepository.escapeLike("50%"));
        assertEquals("a!_b!!c", JdbcTaskRepository.escapeLike("a_b!c"));
        assertEquals("abc", JdbcTaskRepository.escapeLike("abc"));
    }

    @Test
    void underscoreAndBangAreLiteral() {
        tasks.create(new NewTask("axb", "A faire", 1));
        tasks.create(new NewTask("a_b", "A faire", 1));
        tasks.create(new NewTask("Urgent!", "A faire", 1));
        assertEquals(List.of("a_b"), titles(tasks.search("a_b")));
        assertEquals(List.of("Urgent!"), titles(tasks.search("t!")));
    }

    // ------------------------------------------------------------------ le verrou optimiste

    @Test
    void updateIncrementsTheVersion() {
        Task t = tasks.create(new NewTask("Pneu", "A faire", 2));
        Task renamed = tasks.update(t.withTitle("Pneu avant"));
        assertEquals(new Task(1, "Pneu avant", "A faire", 2, 1), renamed);
        assertEquals(Optional.of(renamed), tasks.find(1));
        Task again = tasks.update(renamed.withColumn("Fini"));
        assertEquals(2, again.version());
        assertEquals(Optional.of(again), tasks.find(1));
    }

    @Test
    void secondWriterWithTheSameVersionLoses() {
        Task t = tasks.create(new NewTask("Pneu", "A faire", 2));
        Task ada = tasks.find(1).orElseThrow();
        Task bob = tasks.find(1).orElseThrow();
        tasks.update(ada.withTitle("Pneu (Ada)"));
        ConflictException e = assertThrows(ConflictException.class, () -> tasks.update(bob.withTitle("Pneu (Bob)")));
        assertEquals("tache 1 : version 0 perimee", e.getMessage());
        assertEquals(null, e.sqlState());
        assertEquals("Pneu (Ada)", tasks.find(t.id()).orElseThrow().title());
        assertEquals(1, tasks.find(t.id()).orElseThrow().version());
    }

    @Test
    void conflictIsAStoreException() {
        assertTrue(StoreException.class.isAssignableFrom(ConflictException.class));
        assertTrue(RuntimeException.class.isAssignableFrom(StoreException.class));
    }

    @Test
    void updateOfUnknownTaskSaysNotFound() {
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> tasks.update(new Task(42, "Fantome", "A faire", 1, 0)));
        assertEquals("tache 42 introuvable", e.getMessage());
    }

    // ------------------------------------------------------------------ la transaction : tout ou rien

    @Test
    void moveUpdatesAndWritesHistory() {
        Task t = tasks.create(new NewTask("Pneu", "A faire", 2));
        Task moved = tasks.move(t.id(), "En cours", 0);
        assertEquals(new Task(1, "Pneu", "En cours", 2, 1), moved);
        tasks.move(t.id(), "Fini", 1);
        assertEquals(List.of("2026-10-09T10:15 A faire -> En cours", "2026-10-09T10:15 En cours -> Fini"),
                tasks.history(t.id()));
        assertEquals(Optional.of(new Task(1, "Pneu", "Fini", 2, 2)), tasks.find(1));
    }

    @Test
    void historyUsesTheClock() {
        Task t = tasks.create(new NewTask("Pneu", "A faire", 2));
        new JdbcTaskRepository(tx, Clock.fixed(Instant.parse("2026-12-24T18:30:45Z"), ZoneOffset.UTC)).move(t.id(), "Fini", 0);
        assertEquals(List.of("2026-12-24T18:30:45 A faire -> Fini"), tasks.history(t.id()));
        assertEquals(List.of(), tasks.history(99));
    }

    @Test
    void failedMoveChangesNothing() {
        Task t = tasks.create(new NewTask("Pneu", "A faire", 2));
        // La base refuse un deplacement vers la meme colonne (CHECK from_col <> to_col)... apres la mise a jour.
        StoreException e = assertThrows(StoreException.class, () -> tasks.move(t.id(), "A faire", 0));
        assertEquals("23513", e.sqlState());
        assertEquals(Optional.of(t), tasks.find(t.id()));
        assertEquals(List.of(), tasks.history(t.id()));
    }

    @Test
    void staleMoveChangesNothing() {
        Task t = tasks.create(new NewTask("Pneu", "A faire", 2));
        tasks.move(t.id(), "En cours", 0);
        assertThrows(ConflictException.class, () -> tasks.move(t.id(), "Fini", 0));
        assertEquals("En cours", tasks.find(t.id()).orElseThrow().column());
        assertEquals(1, tasks.history(t.id()).size());
        assertEquals("tache 7 introuvable",
                assertThrows(NoSuchElementException.class, () -> tasks.move(7, "Fini", 0)).getMessage());
    }

    @Test
    void writeRollsBackOnAnyException() {
        Task t = tasks.create(new NewTask("Pneu", "A faire", 2));
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> tx.write(c -> {
            c.createStatement().execute("UPDATE task SET title = 'Perdu'");
            throw new IllegalStateException("plantage au milieu");
        }));
        assertEquals("plantage au milieu", e.getMessage());
        assertEquals("Pneu", tasks.find(t.id()).orElseThrow().title());
    }

    @Test
    void deleteRemovesTaskAndHistory() {
        Task t = tasks.create(new NewTask("Pneu", "A faire", 2));
        tasks.move(t.id(), "En cours", 0);
        assertTrue(tasks.delete(t.id()));
        assertEquals(Optional.empty(), tasks.find(t.id()));
        assertEquals(List.of(), tasks.history(t.id()));
        assertFalse(tasks.delete(t.id()));
    }
}
