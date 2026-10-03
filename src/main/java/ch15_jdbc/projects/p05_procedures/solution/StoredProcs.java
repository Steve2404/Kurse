package ch15_jdbc.projects.p05_procedures.solution;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Les "procedures stockees" : avec H2, ce sont des methodes Java public static, enregistrees par CREATE ALIAS.
 * La classe doit etre public (H2 la charge par son nom). Un 1er parametre Connection est fourni par H2 lui-meme.
 */
public final class StoredProcs {

    private StoredProcs() {
    }

    // L'algorithme de Luhn : de droite a gauche, on double un chiffre sur deux (moins 9 au-dela de 9) ; la somme doit finir par 0.
    public static boolean luhn(String card) {
        String digits = card.replace(" ", "");
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            int d = digits.charAt(digits.length() - 1 - i) - '0';
            if (i % 2 == 1) {
                d *= 2;
                if (d > 9) {
                    d -= 9;
                }
            }
            sum += d;
        }
        return sum % 10 == 0;
    }

    public static int points(int amount, String tier) {
        int factor = switch (tier) {
            case "GOLD" -> 3;
            case "SILVER" -> 2;
            default -> 1;
        };
        return amount / 10 * factor + (amount >= 100 ? 5 : 0);
    }

    // Utilisee en parametre IN OUT : la meme case entre, puis ressort.
    public static String nextTier(String tier) {
        return switch (tier) {
            case "BRONZE" -> "SILVER";
            default -> "GOLD";
        };
    }

    // Une procedure qui MODIFIE la base, par la connexion que H2 lui passe : chaque achat a carte valide credite des points.
    public static int creditPurchases(Connection conn) throws SQLException {
        int credited = 0;
        try (PreparedStatement read = conn.prepareStatement("SELECT p.customer_id, p.amount, p.card, c.tier FROM purchases p JOIN customers c ON c.id = p.customer_id ORDER BY p.id");
             PreparedStatement update = conn.prepareStatement("UPDATE customers SET points = points + ? WHERE id = ?");
             ResultSet rs = read.executeQuery()) {
            while (rs.next()) {
                if (luhn(rs.getString("card"))) {
                    update.setInt(1, points(rs.getInt("amount"), rs.getString("tier")));
                    update.setInt(2, rs.getInt("customer_id"));
                    update.addBatch();
                    credited++;
                }
            }
            update.executeBatch();
        }
        return credited;
    }

    // Une procedure qui rend un ResultSet. H2 l'appelle aussi a la preparation (pour connaitre les colonnes) :
    // elle ne doit donc rien modifier. On ne ferme pas le PreparedStatement : cela fermerait le ResultSet rendu.
    public static ResultSet topCustomers(Connection conn, int n) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT name, points FROM customers ORDER BY points DESC, name LIMIT ?");
        ps.setInt(1, n);
        return ps.executeQuery();
    }

    public static int promote(Connection conn, int threshold) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE customers SET tier = NEXT_TIER(tier) WHERE points >= ? AND tier <> 'GOLD'")) {
            ps.setInt(1, threshold);
            return ps.executeUpdate();
        }
    }
}
