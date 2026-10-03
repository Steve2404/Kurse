package ch15_jdbc.projects.p02_bank.solution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * La banque : chaque operation est une TRANSACTION (tout ou rien). L'auto-commit est donc coupe des la construction.
 */
final class Bank {

    private final Connection conn;

    Bank(Connection conn) throws SQLException {
        this.conn = conn;
        conn.setAutoCommit(false);
    }

    // Une ligne de plus ou de moins sur un compte. 0 ligne touchee = compte inconnu : ce n'est PAS une erreur SQL,
    // on la transforme donc nous-memes en SQLException (avec un SQLState choisi) pour la traiter comme les autres.
    private void add(String account, int delta) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE id = ?")) {
            ps.setInt(1, delta);
            ps.setString(2, account);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("compte inconnu " + account, "02000");
            }
        }
    }

    // Debit, credit, journal : trois ordres SQL, SANS commit. C'est l'appelant qui decide de valider ou d'annuler.
    private int move(String from, String to, int amount) throws SQLException {
        add(from, -amount);
        add(to, amount);
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO journal (from_id, to_id, amount) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, from);
            ps.setString(2, to);
            ps.setInt(3, amount);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    // Le CHECK (balance >= 0) leve 23513 ; notre propre exception porte 02000.
    private static String reason(SQLException e) {
        return switch (e.getSQLState()) {
            case "23513" -> "solde insuffisant (" + e.getSQLState() + ")";
            case "02000" -> e.getMessage() + " (" + e.getSQLState() + ")";
            default -> "erreur " + e.getSQLState();
        };
    }

    // Les regles metier sont verifiees AVANT toute requete : rien a annuler.
    String transfer(String from, String to, int amount) throws SQLException {
        if (amount <= 0 || from.equals(to)) {
            return "refuse : virement invalide";
        }
        try {
            int seq = move(from, to, amount);
            conn.commit();
            return "ok (journal #" + seq + ")";
        } catch (SQLException e) {
            conn.rollback();
            return "refuse : " + reason(e) + ", rien n'a change";
        }
    }

    // Plusieurs virements, UNE seule transaction : un seul echec annule aussi ceux qui avaient reussi.
    String payroll(String from, String[] lines) throws SQLException {
        int done = 0;
        try {
            for (String line : lines) {
                String[] parts = line.split(":");
                move(from, parts[0], Integer.parseInt(parts[1]));
                done++;
            }
            conn.commit();
            return "ok, " + done + " virements";
        } catch (SQLException e) {
            conn.rollback();
            return "annulee entierement : " + reason(e) + " au virement " + (done + 1) + ", " + done + " deja faits annules";
        }
    }

    int balance(String account) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT balance FROM accounts WHERE id = ?")) {
            ps.setString(1, account);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("balance") : -1;
            }
        }
    }
}
