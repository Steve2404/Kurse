package ch11_exceptions.drills.solutions;

import ch11_exceptions.drills.Ledger;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Corrige du drill 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.drills.exercises.Drill06_MixedKata.
 */
public class SolutionDrill06_MixedKata {

    public static List<Integer> validNumbers(List<String> raw) {
        // Un try/catch par entree : une erreur ne coupe pas la boucle.
        List<Integer> result = new ArrayList<>();
        for (String r : raw) {
            try {
                result.add(Integer.parseInt(r));
            } catch (NumberFormatException e) {
                // entree ignoree
            }
        }
        return result;
    }

    public static String firstErrorMessage(List<String> raw) {
        // Le message de NumberFormatException cite le texte fautif entre guillemets.
        for (String r : raw) {
            try {
                Integer.parseInt(r);
            } catch (NumberFormatException e) {
                return e.getMessage();
            }
        }
        return "aucune";
    }

    public static String total(Locale locale) {
        // On reutilise validNumbers ; la devise vient de la Locale (US : $ devant ; Allemagne : euro derriere).
        int sum = 0;
        for (int n : validNumbers(Ledger.RAW)) {
            sum += n;
        }
        return NumberFormat.getCurrencyInstance(locale).format(sum);
    }

    public static String stamp() {
        // HH = 0-23 ; mm = minutes (MM serait le mois).
        return Ledger.WHEN.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static String errorLine(Locale locale, String raw) {
        // Gabarit localise (absent de fr : repli sur la racine), rempli par MessageFormat.
        return MessageFormat.format(ResourceBundle.getBundle(Ledger.BUNDLE, locale).getString("invalidRecord"), raw);
    }

    public static List<String> report(Locale locale) {
        // Meme parcours que validNumbers, mais on garde les ratees ("" en fait partie).
        List<String> lines = new ArrayList<>();
        for (String r : Ledger.RAW) {
            try {
                Integer.parseInt(r);
            } catch (NumberFormatException e) {
                lines.add(errorLine(locale, r));
            }
        }
        return lines;
    }

    @SuppressWarnings("try") // a et b ne servent qu'a tracer l'ordre de fermeture
    public static List<String> closingLog() {
        // Une lambda suffit comme ressource ; AutoCloseable.close() declare Exception : le catch doit la couvrir.
        List<String> log = new ArrayList<>();
        try (AutoCloseable a = () -> log.add("close A"); AutoCloseable b = () -> log.add("close B")) {
            throw new IllegalStateException("boom");
        } catch (Exception e) {
            log.add("caught " + e.getMessage());
        }
        return log;
    }

    public static String describe(Throwable t) {
        // On suit getCause() jusqu'au bout.
        List<String> parts = new ArrayList<>();
        for (Throwable c = t; c != null; c = c.getCause()) {
            parts.add(c.getClass().getSimpleName() + "(" + c.getMessage() + ")");
        }
        return String.join(" <- ", parts);
    }

    public static List<String> checkedNames(List<Class<? extends Throwable>> types) {
        // Checked = ni RuntimeException ni Error dans les ancetres ; Exception elle-meme est checked.
        List<String> names = new ArrayList<>();
        for (Class<? extends Throwable> type : types) {
            if (!RuntimeException.class.isAssignableFrom(type) && !Error.class.isAssignableFrom(type)) {
                names.add(type.getSimpleName());
            }
        }
        return names;
    }

    public static int safeDivide(int a, int b) {
        // On traduit l'erreur technique en erreur d'argument, sans perdre l'originale (cause).
        try {
            return a / b;
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("b == 0", e);
        }
    }
}
