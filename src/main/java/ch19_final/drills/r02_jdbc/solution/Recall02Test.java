package ch19_final.drills.r02_jdbc.solution;

import ch19_final.drills.r02_jdbc.Data;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du drill 2 : une base H2 neuve pour chaque defi. */
class Recall02Test {

    private Connection keeper;
    private Tx tx;

    @BeforeEach
    void freshDatabase() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:" + UUID.randomUUID());
        keeper = ds.getConnection();
        tx = new Tx(ds);
    }

    @AfterEach
    void close() throws SQLException {
        keeper.close();
    }

    private long count() {
        return tx.read(c -> {
            var rs = c.createStatement().executeQuery("SELECT COUNT(*) FROM t");
            rs.next();
            return rs.getLong(1);
        });
    }

    @Test
    void d01() {
        tx.write(c -> c.createStatement().execute("CREATE TABLE t (id INT PRIMARY KEY)"));
        tx.write(c -> c.createStatement().executeUpdate("INSERT INTO t VALUES (1)"));
        assertEquals(1, count());
        IllegalStateException boom = assertThrows(IllegalStateException.class, () -> tx.write(c -> {
            c.createStatement().executeUpdate("INSERT INTO t VALUES (2)");
            throw new IllegalStateException("plantage");
        }));
        assertEquals("plantage", boom.getMessage());
        assertEquals(1, count());
        DataException duplicate = assertThrows(DataException.class, () -> tx.write(c -> {
            c.createStatement().executeUpdate("INSERT INTO t VALUES (3)");
            return c.createStatement().executeUpdate("INSERT INTO t VALUES (1)");
        }));
        assertEquals("23505", duplicate.sqlState());
        assertEquals("erreur SQL 23505", duplicate.getMessage());
        assertEquals(1, count());
    }

    @Test
    void d02() {
        assertEquals(List.of(1), Schema.migrate(tx, Data.SCRIPTS.subList(0, 1)));
        assertEquals(List.of(2), Schema.migrate(tx, Data.SCRIPTS));
        assertEquals(List.of(), Schema.migrate(tx, Data.SCRIPTS));
        long versions = tx.read(c -> {
            var rs = c.createStatement().executeQuery("SELECT COUNT(*) FROM schema_version");
            rs.next();
            return rs.getLong(1);
        });
        assertEquals(2, versions);
    }

    @Test
    void d03() {
        Schema.migrate(tx, Data.SCRIPTS);
        Bank bank = new Bank(tx);
        assertEquals(1, bank.open("Ada", 5000));
        assertEquals(2, bank.open("Bob", 0));
        assertEquals(5000, bank.balance(1));
        assertEquals("compte inconnu : 9", assertThrows(NoSuchElementException.class, () -> bank.balance(9)).getMessage());
        assertEquals("23513", assertThrows(DataException.class, () -> bank.open("Neg", -1)).sqlState());
    }

    @Test
    void d04() {
        Schema.migrate(tx, Data.SCRIPTS);
        Bank bank = new Bank(tx);
        long ada = bank.open("Ada", 5000);
        long bob = bank.open("Bob", 100);
        bank.transfer(ada, bob, 1200);
        assertEquals(3800, bank.balance(ada));
        assertEquals(1300, bank.balance(bob));
        assertEquals("solde insuffisant : 1300 < 5000",
                assertThrows(IllegalArgumentException.class, () -> bank.transfer(bob, ada, 5000)).getMessage());
        assertEquals("compte inconnu : 77", assertThrows(NoSuchElementException.class, () -> bank.transfer(ada, 77, 500)).getMessage());
        assertEquals(3800, bank.balance(ada));
        assertEquals(1300, bank.balance(bob));
    }

    @Test
    void d05() {
        Schema.migrate(tx, Data.SCRIPTS);
        Bank bank = new Bank(tx);
        for (String owner : List.of("Velo 50%", "Velo 500", "a_b", "axb", "Urgent!", "O'Neil")) {
            bank.open(owner, 0);
        }
        assertEquals(List.of("Velo 50%", "Velo 500"), bank.search("VELO"));
        assertEquals(List.of("Velo 50%"), bank.search("50%"));
        assertEquals(List.of("a_b"), bank.search("a_b"));
        assertEquals(List.of("Urgent!"), bank.search("t!"));
        assertEquals(List.of("O'Neil"), bank.search("o'n"));
        assertEquals(List.of(), bank.search("x' OR 1=1 --"));
        assertEquals(6, bank.search("").size());
    }

    @Test
    void d06() {
        Schema.migrate(tx, Data.SCRIPTS);
        Bank bank = new Bank(tx);
        long id = bank.open("Ada", 0);
        assertTrue(bank.rename(id, "Ada L.", 0));
        assertFalse(bank.rename(id, "Ada Lovelace", 0));
        assertTrue(bank.rename(id, "Ada Lovelace", 1));
        assertEquals(List.of("Ada Lovelace"), bank.search("ada"));
        assertFalse(bank.rename(99, "Personne", 0));
    }
}
