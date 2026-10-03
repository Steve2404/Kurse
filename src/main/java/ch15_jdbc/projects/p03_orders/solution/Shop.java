package ch15_jdbc.projects.p03_orders.solution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * La boutique : une commande = une transaction ; une ligne de commande = un point de sauvegarde (Savepoint).
 */
final class Shop {

    private final Connection conn;

    Shop(Connection conn) throws SQLException {
        this.conn = conn;
        conn.setAutoCommit(false);
    }

    private static String reason(SQLException e) {
        return switch (e.getSQLState()) {
            case "23513" -> "rupture";
            case "23505" -> "doublon";
            case "02000" -> "inconnu";
            default -> "erreur " + e.getSQLState();
        };
    }

    // Deux ordres par ligne : le stock baisse, PUIS la ligne est inseree. Si l'insertion echoue (doublon),
    // la baisse du stock a deja eu lieu : seul le retour au Savepoint de la ligne l'efface.
    private int ship(int orderId, String sku, int qty) throws SQLException {
        try (PreparedStatement stock = conn.prepareStatement("UPDATE products SET stock = stock - ? WHERE sku = ?");
             PreparedStatement line = conn.prepareStatement("INSERT INTO order_lines (order_id, sku, qty) VALUES (?, ?, ?)");
             PreparedStatement price = conn.prepareStatement("SELECT price FROM products WHERE sku = ?")) {
            stock.setInt(1, qty);
            stock.setString(2, sku);
            if (stock.executeUpdate() == 0) {
                throw new SQLException("produit inconnu " + sku, "02000");
            }
            line.setInt(1, orderId);
            line.setString(2, sku);
            line.setInt(3, qty);
            line.executeUpdate();
            price.setString(1, sku);
            try (ResultSet rs = price.executeQuery()) {
                rs.next();
                return qty * rs.getInt(1);
            }
        }
    }

    String place(String customer, String policy, String[] lines) throws SQLException {
        int orderId;
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO orders (customer, status, total) VALUES (?, 'EN COURS', 0)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, customer);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                orderId = keys.getInt(1);
            }
        }
        String head = "#" + orderId + " " + customer + " " + policy + " -> ";
        List<String> shipped = new ArrayList<>();
        List<String> refused = new ArrayList<>();
        int total = 0;
        for (String l : lines) {
            String[] p = l.split(":");
            Savepoint sp = conn.setSavepoint("ligne-" + p[0]);
            try {
                total += ship(orderId, p[0], Integer.parseInt(p[1]));
                conn.releaseSavepoint(sp);
                shipped.add(l);
            } catch (SQLException e) {
                if (policy.equals("TOUT")) {
                    // Tout ou rien : on annule TOUTE la transaction, la commande elle-meme comprise.
                    conn.rollback();
                    return head + "ANNULEE (" + reason(e) + " sur " + p[0] + ")";
                }
                // On n'annule que cette ligne : les lignes deja livrees restent.
                conn.rollback(sp);
                refused.add(p[0] + " " + reason(e));
            }
        }
        if (shipped.isEmpty()) {
            conn.rollback();
            return head + "ANNULEE (rien a livrer : " + refused + ")";
        }
        String status = refused.isEmpty() ? "COMPLETE" : "PARTIELLE";
        try (PreparedStatement ps = conn.prepareStatement("UPDATE orders SET status = ?, total = ? WHERE id = ?")) {
            ps.setString(1, status);
            ps.setInt(2, total);
            ps.setInt(3, orderId);
            ps.executeUpdate();
        }
        conn.commit();
        return head + status + ", livre " + shipped + ", refuse " + refused + ", total " + total;
    }
}
