package ch19_final.drills.r06_review.solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/** La revue express, corrigee : chaque methode repare un defaut classique de Data.Before. */
public final class Fixes {

    private Fixes() {
    }

    /** L'argent exact : "19.99" -> 1999 centimes, additionnes dans un long. */
    public static long totalCents(List<String> amounts) {
        long total = 0;
        for (String a : amounts) {
            total += new BigDecimal(a).movePointRight(2).longValueExact();
        }
        return total;
    }

    /** equals, le litteral a gauche : juste pour un texte lu ou tape, et sans NullPointerException. */
    public static boolean isPromo(String code) {
        return "DOUBLE".equals(code);
    }

    /** "A partir de" : >=. */
    public static String tier(int points) {
        return points >= 1000 ? "GOLD" : points >= 300 ? "SILVER" : "BRONZE";
    }

    /** Valable un an : jusqu'a la veille du meme jour, l'annee suivante (les annees bissextiles comprises). */
    public static LocalDate lastValidDay(LocalDate earned) {
        return earned.plusYears(1).minusDays(1);
    }

    /** Les doublons retires, l'ordre de premiere apparition garde, en O(n) : LinkedHashSet. */
    public static List<String> distinctInOrder(List<String> items) {
        return new ArrayList<>(new LinkedHashSet<>(items));
    }

    /** Un StringBuilder (ou String.join) : la concatenation dans une boucle recopie tout a chaque tour. */
    public static String join(List<String> parts) {
        return String.join(", ", parts);
    }

    /** Le flux est toujours ferme, et une erreur d'entree-sortie remonte (pas de -1 qui ressemble a un resultat). */
    public static int countLines(Reader in) throws IOException {
        try (BufferedReader reader = new BufferedReader(in)) {
            int n = 0;
            while (reader.readLine() != null) {
                n++;
            }
            return n;
        }
    }
}
