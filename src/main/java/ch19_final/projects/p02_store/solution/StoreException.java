package ch19_final.projects.p02_store.solution;

import java.sql.SQLException;

/**
 * Une erreur de la base, traduite a la frontiere : le reste du programme ne voit jamais SQLException
 * (verifiee, et liee a JDBC). Le message donne le SQLState (5 caracteres normalises : 23505 = doublon,
 * 23513 = contrainte CHECK...) et pas getMessage(), qui change avec la langue du systeme (chapitre 15).
 */
public class StoreException extends RuntimeException {

    private final String sqlState;

    public StoreException(String message) {
        super(message);
        this.sqlState = null;
    }

    public StoreException(SQLException cause) {
        super("erreur de base de donnees (SQLState " + cause.getSQLState() + ")", cause);
        this.sqlState = cause.getSQLState();
    }

    /** Le SQLState de l'erreur d'origine, ou null si l'erreur ne vient pas de la base. */
    public String sqlState() {
        return sqlState;
    }
}
