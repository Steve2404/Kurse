package ch19_final.drills.r02_jdbc.solution;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SqlWork<T> {

    T run(Connection connection) throws SQLException;
}
