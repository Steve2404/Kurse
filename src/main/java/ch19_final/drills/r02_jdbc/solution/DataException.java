package ch19_final.drills.r02_jdbc.solution;

import java.sql.SQLException;

/** Une SQLException traduite : non verifiee, avec le SQLState (jamais getMessage, qui depend de la langue). */
public class DataException extends RuntimeException {

    private final String sqlState;

    public DataException(SQLException cause) {
        super("erreur SQL " + cause.getSQLState(), cause);
        this.sqlState = cause.getSQLState();
    }

    public String sqlState() {
        return sqlState;
    }
}
