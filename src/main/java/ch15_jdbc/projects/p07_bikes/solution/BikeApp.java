package ch15_jdbc.projects.p07_bikes.solution;

import ch15_jdbc.projects.p07_bikes.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION du projet 7 (capstone) - un service de velos en libre-service : tout le chapitre 15 dans une application.
 */
public class BikeApp {

    static List<String> lines(Connection conn, String sql) throws SQLException {
        List<String> result = new ArrayList<>();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            int n = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                List<String> cells = new ArrayList<>();
                for (int i = 1; i <= n; i++) {
                    cells.add(String.valueOf(rs.getObject(i)));
                }
                result.add(String.join(" ", cells));
            }
        }
        return result;
    }

    static Map<String, int[]> occupancy(Connection conn) throws SQLException {
        Map<String, int[]> result = new TreeMap<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT s.id, s.capacity, COUNT(b.id) AS bikes FROM stations s LEFT JOIN bikes b ON b.station_id = s.id GROUP BY s.id, s.capacity")) {
            while (rs.next()) {
                result.put(rs.getString("id"), new int[]{rs.getInt("bikes"), rs.getInt("capacity")});
            }
        }
        return result;
    }

    static String show(Map<String, int[]> occupancy) {
        StringBuilder sb = new StringBuilder();
        occupancy.forEach((s, v) -> sb.append(sb.length() == 0 ? "" : ", ").append(s).append(' ').append(v[0]).append('/').append(v[1]));
        return sb.toString();
    }

    static void setup(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            for (String ddl : Data.SCHEMA) {
                st.executeUpdate(ddl);
            }
            st.execute("CREATE ALIAS TARIF FOR \"" + Tariff.class.getName() + ".cost\"");
        }
        try (PreparedStatement stations = conn.prepareStatement("INSERT INTO stations VALUES (?, ?, ?)");
             PreparedStatement bikes = conn.prepareStatement("INSERT INTO bikes VALUES (?, ?, ?, 'OK')")) {
            for (String line : Data.STATIONS) {
                String[] f = line.split("\\|");
                stations.setString(1, f[0]);
                stations.setString(2, f[1]);
                stations.setInt(3, Integer.parseInt(f[2]));
                stations.addBatch();
            }
            for (String line : Data.BIKES) {
                String[] f = line.split("\\|");
                bikes.setString(1, f[0]);
                bikes.setString(2, f[1]);
                bikes.setInt(3, Integer.parseInt(f[2]));
                bikes.addBatch();
            }
            // Les stations d'abord : les velos les referencent (cle etrangere).
            System.out.println("installation : " + stations.executeBatch().length + " stations, " + bikes.executeBatch().length + " velos");
        }
    }

    static String run(BikeService service, String command) throws SQLException {
        String[] w = command.split(" ");
        return switch (w[0]) {
            case "membre" -> service.register(w[1], Integer.parseInt(w[2]));
            case "louer" -> service.rent(w[1], w[2]);
            case "rendre" -> service.giveBack(w[1], w[2], Integer.parseInt(w[3]), Integer.parseInt(w[4]));
            case "recharger" -> service.topUp(w[1], Integer.parseInt(w[2]));
            case "maintenance" -> service.maintenance();
            default -> "commande inconnue";
        };
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(Data.URL)) {
            setup(conn);
            System.out.println("tarifs : " + lines(conn, "SELECT TARIF(20), TARIF(30), TARIF(31), TARIF(45), TARIF(46), TARIF(130)"));
            System.out.println("stations : " + show(occupancy(conn)));

            BikeService service = new BikeService(conn);
            for (String command : Data.SCRIPT) {
                System.out.println("> " + command + " : " + run(service, command));
            }

            System.out.println("membres : " + lines(conn, "SELECT m.name, m.credit, COALESCE(d.amount, 0) FROM members m LEFT JOIN debts d ON d.member_id = m.id ORDER BY m.name"));
            lines(conn, "SELECT r.id, m.name, r.bike_id, r.from_station || '>' || COALESCE(r.to_station, '?'), r.minutes, r.cost FROM rides r JOIN members m ON m.id = r.member_id ORDER BY r.id")
                    .forEach(l -> System.out.println("  trajet " + l));
            System.out.println("recette : " + lines(conn, "SELECT SUM(cost), COUNT(cost), COUNT(*) FROM rides") + " ; en revision : "
                    + lines(conn, "SELECT id FROM bikes WHERE state = 'REVISION' ORDER BY id"));

            // Le reequilibrage : le plan en Java, puis UN lot d'UPDATE, puis un commit.
            Map<String, int[]> before = occupancy(conn);
            List<String[]> moves = Rebalancer.plan(before);
            List<String> done = new ArrayList<>();
            try (PreparedStatement pick = conn.prepareStatement("SELECT id FROM bikes WHERE station_id = ? ORDER BY km, id LIMIT 1 OFFSET ?");
                 PreparedStatement move = conn.prepareStatement("UPDATE bikes SET station_id = ? WHERE id = ?")) {
                Map<String, Integer> taken = new TreeMap<>();
                for (String[] m : moves) {
                    // OFFSET : on saute les velos deja choisis dans cette station (le lot n'est pas encore envoye).
                    int offset = taken.merge(m[0], 1, Integer::sum) - 1;
                    pick.setString(1, m[0]);
                    pick.setInt(2, offset);
                    try (ResultSet rs = pick.executeQuery()) {
                        rs.next();
                        move.setString(1, m[1]);
                        move.setString(2, rs.getString(1));
                        move.addBatch();
                        done.add(rs.getString(1) + " " + m[0] + ">" + m[1]);
                    }
                }
                System.out.println("reequilibrage : " + show(before) + " ; mouvements " + done + " ; lot " + Arrays.toString(move.executeBatch()));
            }
            conn.commit();
            System.out.println("apres : " + show(occupancy(conn)));
        }
    }
}
