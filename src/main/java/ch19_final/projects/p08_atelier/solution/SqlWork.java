package ch19_final.projects.p08_atelier.solution;

import java.sql.Connection;
import java.sql.SQLException;

/** Un travail sur une connexion ouverte. Il peut lancer SQLException : c'est Transactions qui la traduit. */
@FunctionalInterface
public interface SqlWork<T> {

    T run(Connection connection) throws SQLException;
}
