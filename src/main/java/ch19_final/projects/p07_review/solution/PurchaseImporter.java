package ch19_final.projects.p07_review.solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * La lecture d'un fichier d'achats ("C1;19.99;2026-10-01" ou "C1;19.99;2026-10-01;DOUBLE").
 * Deux corrections par rapport au collegue :
 *   - le flux est TOUJOURS ferme (try-with-resources), meme si une ligne est fausse ;
 *   - une ligne fausse arrete tout avec un message qui dit laquelle et pourquoi : avaler l'erreur et rendre
 *     "ce qui a ete lu" ferait croire a un import reussi, avec des achats en moins.
 */
public final class PurchaseImporter {

    private PurchaseImporter() {
    }

    public static List<Purchase> read(Reader in) throws IOException {
        try (BufferedReader reader = new BufferedReader(in)) {
            List<Purchase> result = new ArrayList<>();
            String line;
            int number = 0;
            while ((line = reader.readLine()) != null) {
                number++;
                if (!line.isBlank()) {
                    result.add(parse(line, number));
                }
            }
            return result;
        }
    }

    private static Purchase parse(String line, int number) {
        String[] f = line.split(";", -1);
        if (f.length < 3 || f.length > 4) {
            throw new IllegalArgumentException("ligne " + number + " : 3 ou 4 champs attendus");
        }
        String promo = f.length == 4 && !f[3].isEmpty() ? f[3] : null;
        return new Purchase(f[0], cents(f[1], number), date(f[2], number), promo);
    }

    // "19.99" -> 1999, exactement : BigDecimal lit le texte tel quel ; "19.999" (trois decimales) est refuse.
    private static long cents(String text, int number) {
        try {
            long cents = new BigDecimal(text).movePointRight(2).longValueExact();
            if (cents < 0) {
                throw new ArithmeticException();
            }
            return cents;
        } catch (NumberFormatException | ArithmeticException e) {
            throw new IllegalArgumentException("ligne " + number + " : montant invalide : " + text);
        }
    }

    private static LocalDate date(String text, int number) {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("ligne " + number + " : date invalide : " + text);
        }
    }
}
