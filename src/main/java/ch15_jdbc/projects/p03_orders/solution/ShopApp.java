package ch15_jdbc.projects.p03_orders.solution;

import ch15_jdbc.projects.p03_orders.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION du projet 3 - les commandes d'une boutique : points de sauvegarde (Savepoint), annulation partielle ou totale.
 */
public class ShopApp {

    static Map<String, Integer> column(Connection conn, String column) throws SQLException {
        Map<String, Integer> values = new TreeMap<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT sku, " + column + " FROM products")) {
            while (rs.next()) {
                values.put(rs.getString(1), rs.getInt(2));
            }
        }
        return values;
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(Data.URL)) {
            Map<String, Integer> initial = new TreeMap<>();
            try (Statement st = conn.createStatement()) {
                for (String ddl : Data.SCHEMA) {
                    st.executeUpdate(ddl);
                }
            }
            // Le PreparedStatement est compile des sa creation : la table doit donc deja exister.
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO products VALUES (?, ?, ?, ?)")) {
                for (String line : Data.PRODUCTS) {
                    String[] f = line.split("\\|");
                    ps.setString(1, f[0]);
                    ps.setString(2, f[1]);
                    ps.setInt(3, Integer.parseInt(f[2]));
                    ps.setInt(4, Integer.parseInt(f[3]));
                    ps.executeUpdate();
                    initial.put(f[0], Integer.parseInt(f[2]));
                }
            }
            System.out.println("stock initial : " + initial);

            Shop shop = new Shop(conn);
            for (String order : Data.ORDERS) {
                String[] f = order.split("\\|");
                System.out.println(shop.place(f[0], f[1], f[2].split(",")));
            }
            System.out.println("stock final : " + column(conn, "stock"));

            List<String> orders = new ArrayList<>();
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT id, customer, status, total FROM orders ORDER BY id")) {
                while (rs.next()) {
                    orders.add(rs.getInt("id") + " " + rs.getString("customer") + " " + rs.getString("status") + " " + rs.getInt("total"));
                }
            }
            System.out.println("commandes en base : " + orders);

            // Le controle : stock restant + quantites livrees = stock initial, pour chaque produit.
            Map<String, Integer> rebuilt = new TreeMap<>();
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT p.sku, p.stock + COALESCE(SUM(l.qty), 0) AS origin FROM products p LEFT JOIN order_lines l ON l.sku = p.sku GROUP BY p.sku, p.stock")) {
                while (rs.next()) {
                    rebuilt.put(rs.getString("sku"), rs.getInt("origin"));
                }
            }
            System.out.println("controle : stock + livre = stock initial " + rebuilt.equals(initial));

            savepoints(conn);
        }
    }

    // L'API des Savepoint, sur une promotion qu'on essaie puis qu'on annule.
    private static void savepoints(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            Savepoint screen = conn.setSavepoint("promo-ecran");
            st.executeUpdate("UPDATE products SET price = price - 50 WHERE sku = 'P3'");
            Savepoint mouse = conn.setSavepoint();
            st.executeUpdate("UPDATE products SET price = price - 5 WHERE sku = 'P2'");
            System.out.println("promos : " + column(conn, "price") + " ; nom " + screen.getSavepointName() + ", id anonyme >= 0 " + (mouse.getSavepointId() >= 0));
            // Un Savepoint nomme n'a pas d'id, un anonyme n'a pas de nom.
            try {
                screen.getSavepointId();
            } catch (SQLException e) {
                System.out.println("  getSavepointId d'un point nomme : SQLException");
            }
            // Revenir a un point ANTERIEUR efface aussi tout ce qui a ete fait apres lui.
            conn.rollback(screen);
            System.out.println("rollback(promo-ecran) : " + column(conn, "price"));
            st.executeUpdate("UPDATE products SET price = price + 1 WHERE sku = 'P4'");
            Savepoint cable = conn.setSavepoint("hausse-cable");
            conn.releaseSavepoint(cable);
            try {
                conn.rollback(cable);
            } catch (SQLException e) {
                System.out.println("  rollback vers un point libere : " + e.getSQLState());
            }
            conn.commit();
            System.out.println("apres commit : " + column(conn, "price"));
        }
    }
}
