package ch19_final.drills.r02_jdbc.solution;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/** Executer autour : ouvrir, travailler, valider ou annuler, fermer. */
public final class Tx {

    private final DataSource dataSource;

    public Tx(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T read(SqlWork<T> work) {
        try (Connection c = dataSource.getConnection()) {
            return work.run(c);
        } catch (SQLException e) {
            throw new DataException(e);
        }
    }

    public <T> T write(SqlWork<T> work) {
        try (Connection c = dataSource.getConnection()) {
            c.setAutoCommit(false);
            try {
                T result = work.run(c);
                c.commit();
                return result;
            } catch (SQLException | RuntimeException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataException(e);
        }
    }
}
