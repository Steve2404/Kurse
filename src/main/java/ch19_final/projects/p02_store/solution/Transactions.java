package ch19_final.projects.p02_store.solution;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * "Execute autour" (execute around) : ouvrir, faire le travail, valider ou annuler, fermer. Ecrit UNE fois ici,
 * au lieu d'etre recopie (et oublie une fois sur dix) dans chaque methode du depot.
 */
public final class Transactions {

    private final DataSource dataSource;

    public Transactions(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** Une lecture : une connexion en auto-commit, fermee a la fin quoi qu'il arrive. */
    public <T> T read(SqlWork<T> work) {
        try (Connection connection = dataSource.getConnection()) {
            return work.run(connection);
        } catch (SQLException e) {
            throw new StoreException(e);
        }
    }

    /** Une ecriture : tout ou rien. Une exception, quelle qu'elle soit, annule tout ce que le travail a fait. */
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
}
