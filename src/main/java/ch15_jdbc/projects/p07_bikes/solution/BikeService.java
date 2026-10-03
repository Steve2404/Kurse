package ch15_jdbc.projects.p07_bikes.solution;

import ch15_jdbc.projects.p07_bikes.Data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Types;

/**
 * Le service de velos en libre-service. Chaque commande est UNE transaction : elle valide tout, ou n'annule rien
 * a moitie. Les refus metier sont des SQLException a nous (SQLState 45000), traitees comme celles de la base.
 */
final class BikeService {

    private static final String BUSINESS = "45000";

    private final Connection conn;

    BikeService(Connection conn) throws SQLException {
        this.conn = conn;
        conn.setAutoCommit(false);
    }

    private static SQLException refuse(String message) {
        return new SQLException(message, BUSINESS);
    }

    private static String reason(SQLException e) {
        return switch (e.getSQLState()) {
            case BUSINESS -> e.getMessage();
            case "23505" -> "deja inscrit";
            default -> "erreur " + e.getSQLState();
        };
    }

    // Toutes les commandes passent par ici : commit si tout va bien, rollback sinon.
    private interface Work {
        String run() throws SQLException;
    }

    private String transaction(Work work) throws SQLException {
        try {
            String result = work.run();
            conn.commit();
            return "ok, " + result;
        } catch (SQLException e) {
            conn.rollback();
            return "refuse : " + reason(e);
        }
    }

    // Un petit outil : la 1re colonne entiere de la 1re ligne, ou null s'il n'y a aucune ligne.
    private Integer intOf(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        }
    }

    private int update(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        }
    }

    private int memberId(String name) throws SQLException {
        Integer id = intOf("SELECT id FROM members WHERE name = ?", name);
        if (id == null) {
            throw refuse("membre inconnu " + name);
        }
        return id;
    }

    String register(String name, int credit) throws SQLException {
        return transaction(() -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO members (name, credit) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setInt(2, credit);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    return "membre #" + keys.getInt(1);
                }
            }
        });
    }

    String rent(String name, String bike) throws SQLException {
        return transaction(() -> {
            int member = memberId(name);
            if (intOf("SELECT COUNT(*) FROM rides WHERE member_id = ? AND to_station IS NULL", member) > 0) {
                throw refuse("location deja en cours");
            }
            String from;
            try (PreparedStatement ps = conn.prepareStatement("SELECT station_id FROM bikes WHERE id = ? AND state = 'OK'")) {
                ps.setString(1, bike);
                try (ResultSet rs = ps.executeQuery()) {
                    from = rs.next() ? rs.getString(1) : null;
                }
            }
            if (from == null) {
                throw refuse("velo " + bike + " indisponible");
            }
            update("UPDATE bikes SET station_id = NULL WHERE id = ?", bike);
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO rides (member_id, bike_id, from_station) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, member);
                ps.setString(2, bike);
                ps.setString(3, from);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    return "location #" + keys.getInt(1) + " depuis " + from;
                }
            }
        });
    }

    String giveBack(String name, String station, int minutes, int km) throws SQLException {
        return transaction(() -> {
            int member = memberId(name);
            int ride;
            String bike;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id, bike_id FROM rides WHERE member_id = ? AND to_station IS NULL")) {
                ps.setInt(1, member);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw refuse("aucune location en cours");
                    }
                    ride = rs.getInt("id");
                    bike = rs.getString("bike_id");
                }
            }
            Integer free = intOf("SELECT capacity - (SELECT COUNT(*) FROM bikes WHERE station_id = s.id) FROM stations s WHERE id = ?", station);
            if (free == null || free <= 0) {
                throw refuse("station " + station + " pleine");
            }
            // Le prix vient de la fonction stockee TARIF.
            int cost;
            try (CallableStatement cs = conn.prepareCall("{? = call TARIF(?)}")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setInt(2, minutes);
                cs.execute();
                cost = cs.getInt(1);
            }
            update("UPDATE bikes SET station_id = ?, km = km + ? WHERE id = ?", station, km, bike);
            update("UPDATE rides SET to_station = ?, minutes = ?, cost = ? WHERE id = ?", station, minutes, cost, ride);
            // Le paiement : si le credit ne suffit pas (CHECK), on revient au Savepoint, on prend tout le credit
            // et le reste devient une dette. Le retour du velo, lui, reste acquis.
            Savepoint payment = conn.setSavepoint("paiement");
            try {
                update("UPDATE members SET credit = credit - ? WHERE id = ?", cost, member);
                return bike + " a " + station + ", " + minutes + " min, " + cost + " cts";
            } catch (SQLException e) {
                if (!e.getSQLState().equals("23513")) {
                    throw e;
                }
                conn.rollback(payment);
                int credit = intOf("SELECT credit FROM members WHERE id = ?", member);
                update("UPDATE members SET credit = 0 WHERE id = ?", member);
                if (update("UPDATE debts SET amount = amount + ? WHERE member_id = ?", cost - credit, member) == 0) {
                    update("INSERT INTO debts (member_id, amount) VALUES (?, ?)", member, cost - credit);
                }
                return bike + " a " + station + ", " + minutes + " min, " + cost + " cts, dont " + (cost - credit) + " en dette";
            }
        });
    }

    String topUp(String name, int amount) throws SQLException {
        return transaction(() -> {
            int member = memberId(name);
            update("UPDATE members SET credit = credit + ? WHERE id = ?", amount, member);
            Integer debt = intOf("SELECT amount FROM debts WHERE member_id = ?", member);
            int paid = 0;
            if (debt != null) {
                paid = Math.min(debt, intOf("SELECT credit FROM members WHERE id = ?", member));
                update("UPDATE members SET credit = credit - ? WHERE id = ?", paid, member);
                update("UPDATE debts SET amount = amount - ? WHERE member_id = ?", paid, member);
                update("DELETE FROM debts WHERE member_id = ? AND amount = 0", member);
            }
            return "credit " + intOf("SELECT credit FROM members WHERE id = ?", member) + ", dette remboursee " + paid;
        });
    }

    // Seuls les velos EN STATION partent en revision (un velo loue n'est pas la).
    String maintenance() throws SQLException {
        return transaction(() -> update("UPDATE bikes SET state = 'REVISION', station_id = NULL WHERE km > ? AND station_id IS NOT NULL", Data.SERVICE_KM)
                + " velos en revision");
    }
}
