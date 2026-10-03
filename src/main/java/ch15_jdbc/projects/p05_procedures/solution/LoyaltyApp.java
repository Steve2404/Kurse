package ch15_jdbc.projects.p05_procedures.solution;

import ch15_jdbc.projects.p05_procedures.Data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION du projet 5 - le programme de fidelite : CallableStatement, parametres IN, OUT, IN OUT, procedure qui rend un ResultSet.
 */
public class LoyaltyApp {

    static Map<String, String> customers(Connection conn) throws SQLException {
        Map<String, String> result = new TreeMap<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT name, tier, points FROM customers")) {
            while (rs.next()) {
                result.put(rs.getString("name"), rs.getString("tier") + "/" + rs.getInt("points"));
            }
        }
        return result;
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(Data.URL)) {
            try (Statement st = conn.createStatement()) {
                for (String ddl : Data.SCHEMA) {
                    st.executeUpdate(ddl);
                }
                // Le nom COMPLET de la classe (paquet compris) : getName() le donne, sans risque de faute de frappe.
                String cls = StoredProcs.class.getName();
                String[][] aliases = {{"LUHN", "luhn"}, {"POINTS", "points"}, {"NEXT_TIER", "nextTier"}, {"CREDIT_PURCHASES", "creditPurchases"},
                        {"TOP_CUSTOMERS", "topCustomers"}, {"PROMOTE", "promote"}};
                for (String[] alias : aliases) {
                    st.execute("CREATE ALIAS " + alias[0] + " FOR \"" + cls + "." + alias[1] + "\"");
                }
                System.out.println("alias : " + aliases.length + " procedures enregistrees");
            }
            try (PreparedStatement c = conn.prepareStatement("INSERT INTO customers VALUES (?, ?, ?, 0)");
                 PreparedStatement p = conn.prepareStatement("INSERT INTO purchases (customer_id, amount, card) VALUES (?, ?, ?)")) {
                for (String line : Data.CUSTOMERS) {
                    String[] f = line.split("\\|");
                    c.setInt(1, Integer.parseInt(f[0]));
                    c.setString(2, f[1]);
                    c.setString(3, f[2]);
                    c.executeUpdate();
                }
                for (String line : Data.PURCHASES) {
                    String[] f = line.split("\\|");
                    p.setInt(1, Integer.parseInt(f[0]));
                    p.setInt(2, Integer.parseInt(f[1]));
                    p.setString(3, f[2]);
                    p.executeUpdate();
                }
            }

            // Une fonction s'utilise aussi DANS le SQL, comme une fonction native.
            List<String> invalid = new ArrayList<>();
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT card FROM purchases WHERE NOT LUHN(card) ORDER BY id")) {
                while (rs.next()) {
                    invalid.add(rs.getString(1));
                }
            }
            System.out.println("cartes refusees par LUHN en SQL : " + invalid);

            // OUT : le resultat de la fonction est le parametre 1, qu'il faut DECLARER avant execute().
            try (CallableStatement cs = conn.prepareCall("{? = call POINTS(?, ?)}")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setInt(2, 120);
                cs.setString(3, "GOLD");
                cs.execute();
                int gold = cs.getInt(1);
                cs.setString(3, "BRONZE");
                cs.execute();
                System.out.println("OUT : POINTS(120, GOLD) = " + gold + ", POINTS(120, BRONZE) = " + cs.getInt(1));
            }
            try (CallableStatement cs = conn.prepareCall("{? = call LUHN(?)}")) {
                cs.registerOutParameter(1, Types.BOOLEAN);
                cs.setString(2, "7992 7398 713");
                cs.execute();
                System.out.println("OUT : LUHN(7992 7398 713) = " + cs.getBoolean(1));
            }

            // IN OUT : la meme case recoit une valeur (set) et en rend une autre (register + get).
            try (CallableStatement cs = conn.prepareCall("{call NEXT_TIER(?)}")) {
                List<String> chain = new ArrayList<>(List.of("BRONZE"));
                for (int i = 0; i < 3; i++) {
                    cs.setString(1, chain.get(chain.size() - 1));
                    cs.registerOutParameter(1, Types.VARCHAR);
                    cs.execute();
                    chain.add(cs.getString(1));
                }
                System.out.println("IN OUT : " + String.join(" -> ", chain));
            }

            try (CallableStatement cs = conn.prepareCall("{? = call CREDIT_PURCHASES()}")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.execute();
                System.out.println("CREDIT_PURCHASES : " + cs.getInt(1) + " achats credites ; " + customers(conn));
            }

            // Une procedure qui rend un ResultSet : executeQuery, et le type de curseur peut etre precise.
            try (CallableStatement cs = conn.prepareCall("{call TOP_CUSTOMERS(?)}", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
                cs.setInt(1, 3);
                List<String> top = new ArrayList<>();
                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        top.add(rs.getString("name") + "=" + rs.getInt("points"));
                    }
                }
                System.out.println("TOP_CUSTOMERS(3) : " + top);
            }

            try (CallableStatement cs = conn.prepareCall("{? = call PROMOTE(?)}")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setInt(2, 30);
                cs.execute();
                System.out.println("PROMOTE(30) : " + cs.getInt(1) + " promus ; " + customers(conn));
            }

            try {
                conn.prepareCall("{call BONUS(?)}");
            } catch (SQLException e) {
                System.out.println("procedure inconnue : " + e.getSQLState());
            }
            try (CallableStatement cs = conn.prepareCall("{? = call POINTS(?, ?)}")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setInt(2, 50);
                cs.execute();
            } catch (SQLException e) {
                System.out.println("parametre oublie : " + e.getSQLState());
            }
        }
    }
}
