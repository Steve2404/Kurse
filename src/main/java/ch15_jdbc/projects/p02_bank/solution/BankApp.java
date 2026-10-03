package ch15_jdbc.projects.p02_bank.solution;

import ch15_jdbc.projects.p02_bank.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION du projet 2 - les virements d'une banque : transactions, commit, rollback, auto-commit, visibilite.
 */
public class BankApp {

    // La 1re colonne de la 1re ligne, en texte : pratique pour observer la base depuis une autre connexion.
    static String first(Connection c, String sql) throws SQLException {
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getString(1) : null;
        }
    }

    static Map<String, Integer> balances(Connection c) throws SQLException {
        Map<String, Integer> result = new TreeMap<>();
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT id, balance FROM accounts")) {
            while (rs.next()) {
                result.put(rs.getString("id"), rs.getInt("balance"));
            }
        }
        return result;
    }

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(Data.URL);
             Connection observer = DriverManager.getConnection(Data.URL)) {
            // Tant que l'auto-commit est actif (par defaut), chaque ordre est valide tout seul.
            try (Statement st = conn.createStatement()) {
                for (String ddl : Data.SCHEMA) {
                    st.executeUpdate(ddl);
                }
            }
            Map<String, Integer> initial = new TreeMap<>();
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO accounts VALUES (?, ?, ?)")) {
                for (String line : Data.ACCOUNTS) {
                    String[] f = line.split("\\|");
                    ps.setString(1, f[0]);
                    ps.setString(2, f[1]);
                    ps.setInt(3, Integer.parseInt(f[2]));
                    ps.executeUpdate();
                    initial.put(f[0], Integer.parseInt(f[2]));
                }
            }
            System.out.println("comptes : " + initial + " ; autoCommit " + conn.getAutoCommit());

            Bank bank = new Bank(conn);
            System.out.println("banque ouverte : autoCommit " + conn.getAutoCommit());
            for (String t : Data.TRANSFERS) {
                String[] p = t.split("[>:]");
                System.out.println("  " + t + " -> " + bank.transfer(p[0], p[1], Integer.parseInt(p[2])));
            }
            for (String[] payroll : Data.PAYROLLS) {
                System.out.println("paie de D4 " + List.of(payroll) + " -> " + bank.payroll("D4", payroll));
            }

            // Le rejeu : on repart des soldes initiaux et on applique le journal. Il doit retrouver la table.
            Map<String, Integer> replay = new TreeMap<>(initial);
            List<Integer> seqs = new ArrayList<>();
            try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery("SELECT seq, from_id, to_id, amount FROM journal ORDER BY seq")) {
                while (rs.next()) {
                    seqs.add(rs.getInt("seq"));
                    replay.merge(rs.getString("from_id"), -rs.getInt("amount"), Integer::sum);
                    replay.merge(rs.getString("to_id"), rs.getInt("amount"), Integer::sum);
                }
            }
            Map<String, Integer> actual = balances(conn);
            int total = actual.values().stream().mapToInt(Integer::intValue).sum();
            int initialTotal = initial.values().stream().mapToInt(Integer::intValue).sum();
            System.out.println("soldes : " + actual + " ; total " + total + " ; conserve " + (total == initialTotal));
            System.out.println("journal : " + seqs + " ; le rejeu retrouve les soldes " + replay.equals(actual));

            // Isolation : tant que conn n'a pas valide, l'observateur (une AUTRE connexion) voit l'ancienne valeur.
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("UPDATE accounts SET balance = balance + 1000 WHERE id = 'A1'");
                System.out.println("pendant la transaction : moi " + bank.balance("A1") + ", observateur " + first(observer, "SELECT balance FROM accounts WHERE id = 'A1'"));
                conn.rollback();
                System.out.println("apres rollback : moi " + bank.balance("A1"));
                // Repasser en auto-commit VALIDE la transaction en cours.
                st.executeUpdate("UPDATE accounts SET owner = 'Cleopatre' WHERE id = 'C3'");
                System.out.println("avant setAutoCommit(true) : observateur voit " + first(observer, "SELECT owner FROM accounts WHERE id = 'C3'"));
                conn.setAutoCommit(true);
                System.out.println("apres setAutoCommit(true) : observateur voit " + first(observer, "SELECT owner FROM accounts WHERE id = 'C3'"));
            }
        }
    }
}
