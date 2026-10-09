package ch19_final.drills.r02_jdbc.solution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/** La banque de l'atelier : des PreparedStatement partout, et le virement dans UNE transaction. */
public final class Bank {

    private final Tx tx;

    public Bank(Tx tx) {
        this.tx = tx;
    }

    public long open(String owner, long cents) {
        return tx.write(c -> {
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO account (owner, cents) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, owner);
                ps.setLong(2, cents);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    return keys.getLong(1);
                }
            }
        });
    }

    public long balance(long id) {
        return tx.read(c -> balance(c, id));
    }

    /** Le debit et le credit ensemble, ou rien : un solde insuffisant ou un compte inconnu annule tout. */
    public void transfer(long from, long to, long cents) {
        tx.write(c -> {
            long available = balance(c, from);
            if (available < cents) {
                throw new IllegalArgumentException("solde insuffisant : " + available + " < " + cents);
            }
            add(c, from, -cents);
            if (add(c, to, cents) == 0) {
                throw new NoSuchElementException("compte inconnu : " + to);
            }
            return null;
        });
    }

    /** Les titulaires dont le nom contient text (sans tenir compte de la casse), % et _ pris tels quels, tries. */
    public List<String> search(String text) {
        String like = likePattern(text);
        return tx.read(c -> {
            List<String> owners = new ArrayList<>();
            try (PreparedStatement ps = c.prepareStatement("SELECT owner FROM account WHERE LOWER(owner) LIKE ? ESCAPE '!' ORDER BY owner")) {
                ps.setString(1, like);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        owners.add(rs.getString(1));
                    }
                }
            }
            return owners;
        });
    }

    /** Le verrou optimiste : faux si la version a change depuis la lecture (rien n'est modifie). */
    public boolean rename(long id, String owner, int expectedVersion) {
        return tx.write(c -> {
            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE account SET owner = ?, version = version + 1 WHERE id = ? AND version = ?")) {
                ps.setString(1, owner);
                ps.setLong(2, id);
                ps.setInt(3, expectedVersion);
                return ps.executeUpdate() == 1;
            }
        });
    }

    // %texte%, avec % _ et ! precedes de ! (le caractere d'echappement declare dans la requete).
    private static String likePattern(String text) {
        StringBuilder pattern = new StringBuilder("%");
        for (char ch : text.toLowerCase().toCharArray()) {
            if (ch == '!' || ch == '%' || ch == '_') {
                pattern.append('!');
            }
            pattern.append(ch);
        }
        return pattern.append('%').toString();
    }

    private static long balance(Connection c, long id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT cents FROM account WHERE id = ?")) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new NoSuchElementException("compte inconnu : " + id);
                }
                return rs.getLong(1);
            }
        }
    }

    private static int add(Connection c, long id, long cents) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("UPDATE account SET cents = cents + ? WHERE id = ?")) {
            ps.setLong(1, cents);
            ps.setLong(2, id);
            return ps.executeUpdate();
        }
    }
}
