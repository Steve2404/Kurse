package ch19_final.projects.p08_atelier.solution;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Le service avec la vraie base H2 : les regles du Board ET le depot du projet 2, ensemble. */
class BoardServiceTest {

    private Connection keeper;
    private BoardService service;

    @BeforeEach
    void freshDatabase() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:" + UUID.randomUUID());
        keeper = dataSource.getConnection();
        Transactions tx = new Transactions(dataSource);
        Migrations.apply(tx, TaskSchema.MIGRATIONS);
        service = new BoardService(new JdbcTaskRepository(tx, new ManualClock(Instant.parse("2026-10-09T10:15:00Z"))), new Board(2));
    }

    @AfterEach
    void dropDatabase() throws SQLException {
        keeper.close();
    }

    private Task todo(String title) {
        return service.create(new NewTask(title, "A faire", 1));
    }

    @Test
    void boardListsEveryColumnInOrder() {
        assertEquals("{A faire=0, En cours=0, Fini=0}", service.board().toString());
        Task t = todo("Pneu");
        todo("Selle");
        service.move(t.id(), "En cours", 0);
        assertEquals("{A faire=1, En cours=1, Fini=0}", service.board().toString());
    }

    @Test
    void createChecksTheBoardFirst() {
        assertThrows(IllegalArgumentException.class, () -> service.create(new NewTask("Pneu", "Fini", 1)));
        assertEquals(Map.of("A faire", 0, "En cours", 0, "Fini", 0), service.board());
    }

    @Test
    void moveFollowsTheRulesAndKeepsHistory() {
        Task t = todo("Pneu");
        Task moving = service.move(t.id(), "En cours", 0);
        assertEquals(1, moving.version());
        service.move(t.id(), "Fini", 1);
        assertEquals(List.of("2026-10-09T10:15 A faire -> En cours", "2026-10-09T10:15 En cours -> Fini"), service.history(t.id()));
        assertThrows(IllegalArgumentException.class, () -> service.move(t.id(), "A faire", 2));
    }

    @Test
    void wipLimitIsEnforcedWithTheDatabaseCounts() {
        Task a = todo("a");
        Task b = todo("b");
        Task c = todo("c");
        service.move(a.id(), "En cours", 0);
        service.move(b.id(), "En cours", 0);
        assertEquals(409, assertThrows(ApiException.class, () -> service.move(c.id(), "En cours", 0)).status());
        service.move(a.id(), "Fini", 1);
        assertEquals("En cours", service.move(c.id(), "En cours", 0).column());
    }

    @Test
    void columnChangesOnlyThroughMove() {
        Task t = todo("Pneu");
        assertEquals("la colonne se change avec /move, pas avec PUT", assertThrows(IllegalArgumentException.class,
                () -> service.update(new Task(t.id(), "Pneu", "Fini", 1, 0))).getMessage());
        Task renamed = service.update(new Task(t.id(), "Pneu arriere", "A faire", 3, 0));
        assertEquals(new Task(t.id(), "Pneu arriere", "A faire", 3, 1), renamed);
    }

    @Test
    void unknownTasks() {
        assertEquals("tache 9 introuvable", assertThrows(NoSuchElementException.class, () -> service.get(9)).getMessage());
        assertThrows(NoSuchElementException.class, () -> service.history(9));
        assertThrows(NoSuchElementException.class, () -> service.move(9, "En cours", 0));
        assertThrows(NoSuchElementException.class, () -> service.delete(9));
        Task t = todo("Pneu");
        service.delete(t.id());
        assertThrows(NoSuchElementException.class, () -> service.get(t.id()));
    }

    @Test
    void concurrentMovesNeverExceedTheLimit() throws Exception {
        List<Task> six = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            six.add(todo("t" + i));
        }
        ExecutorService pool = Executors.newFixedThreadPool(6);
        List<CompletableFuture<Boolean>> moves = new ArrayList<>();
        try {
            CountDownLatch gate = new CountDownLatch(1);
            for (Task t : six) {
                moves.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        gate.await();
                        service.move(t.id(), "En cours", 0);
                        return true;
                    } catch (ApiException e) {
                        return false;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }, pool));
            }
            gate.countDown();
            CompletableFuture.allOf(moves.toArray(new CompletableFuture[0])).join();
        } finally {
            pool.shutdownNow();
        }
        assertEquals(2, moves.stream().filter(CompletableFuture::join).count());
        assertEquals(2, service.board().get("En cours"));
    }
}
