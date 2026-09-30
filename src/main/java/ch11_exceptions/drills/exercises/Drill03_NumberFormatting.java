package ch11_exceptions.drills.exercises;

import ch11_exceptions.ExerciseChecker;

/**
 * DRILL 03 - Formater des nombres : DecimalFormat, NumberFormat, MessageFormat
 * ============================================================================
 *
 * Mode d'emploi : voir Drill01_ExceptionBasics. Toujours une Locale
 * EXPLICITE (la Locale par defaut de la machine n'est pas fiable).
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : us(value)            [new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US))] 1234.5 -> 1,234.50.
 * TODO 2  : padded(value)        [0 = chiffre obligatoire] motif "000" : 7 -> 007.
 * TODO 3  : optional(value)      [# = chiffre facultatif] motif "#.##" : 2.5 -> 2.5.
 * TODO 4  : halfEven(value)      [arrondi HALF_EVEN] motif "0.00" : 0.125 -> 0.12.
 * TODO 5  : currencyUs(value)    [NumberFormat.getCurrencyInstance(Locale.US)] -1234.5 -> -$1,234.50.
 * TODO 6  : currencyFr(value)    [meme chose en Locale.FRANCE] 1234.5 -> 1 234,50 EUR (espaces speciaux, voir main).
 * TODO 7  : percentFr(ratio)     [getPercentInstance(Locale.FRANCE)] 0.256 -> 26 %.
 * TODO 8  : german(value)        [NumberFormat.getInstance(Locale.GERMANY)] 1234567.891 -> 1.234.567,891.
 * TODO 9  : compact(value)       [getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT)] 1200000 -> 1M.
 * TODO 10 : parsePrefix(text)    [NumberFormat.parse s'arrete au 1er caractere invalide] "12abc" -> 12 (Locale.US).
 * TODO 11 : inbox(name, count)   [MessageFormat.format] "{0} a {1} messages" -> Ana a 3 messages.
 * TODO 12 : apostrophe()         [deux apostrophes dans MessageFormat] le gabarit "It''s {0}" avec "ok" -> It's ok.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   DecimalFormat : 0 obligatoire, # facultatif, ',' groupe, '.' decimale, 'texte' recopie ; arrondi HALF_EVEN
 *   NumberFormat.getInstance(loc) / getCurrencyInstance(loc) / getPercentInstance(loc) / getIntegerInstance(loc)
 *   NumberFormat.getCompactNumberInstance(loc, NumberFormat.Style.SHORT ou LONG)   (1M / 1 million)
 *   format(nombre) -> String ; parse(texte) -> Number, throws ParseException (CHECKED) ; lit le debut valide
 *   France : separateur de milliers U+202F, espace avant EUR et % U+00A0 (insecables)
 *   MessageFormat.format("{0} ... {1}", a, b) ; une apostrophe seule ouvre un texte litteral : ecrire ''
 * ---------------------------------------------------------------------
 */
public class Drill03_NumberFormatting {

    public static String us(double value) {
        throw new UnsupportedOperationException("TODO 1 : implementer us()");
    }

    public static String padded(int value) {
        throw new UnsupportedOperationException("TODO 2 : implementer padded()");
    }

    public static String optional(double value) {
        throw new UnsupportedOperationException("TODO 3 : implementer optional()");
    }

    public static String halfEven(double value) {
        throw new UnsupportedOperationException("TODO 4 : implementer halfEven()");
    }

    public static String currencyUs(double value) {
        throw new UnsupportedOperationException("TODO 5 : implementer currencyUs()");
    }

    public static String currencyFr(double value) {
        throw new UnsupportedOperationException("TODO 6 : implementer currencyFr()");
    }

    public static String percentFr(double ratio) {
        throw new UnsupportedOperationException("TODO 7 : implementer percentFr()");
    }

    public static String german(double value) {
        throw new UnsupportedOperationException("TODO 8 : implementer german()");
    }

    public static String compact(long value) {
        throw new UnsupportedOperationException("TODO 9 : implementer compact()");
    }

    public static int parsePrefix(String text) {
        throw new UnsupportedOperationException("TODO 10 : implementer parsePrefix()");
    }

    public static String inbox(String name, int count) {
        throw new UnsupportedOperationException("TODO 11 : implementer inbox()");
    }

    public static String apostrophe() {
        throw new UnsupportedOperationException("TODO 12 : implementer apostrophe()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  us(1234.5) == 1,234.50", us(1234.5).equals("1,234.50"));
        ExerciseChecker.check("2  padded(7) == 007", padded(7).equals("007"));
        ExerciseChecker.check("3  optional(2.5) == 2.5 ; optional(3) == 3", optional(2.5).equals("2.5") && optional(3).equals("3"));
        ExerciseChecker.check("4  halfEven(0.125) == 0.12 ; halfEven(0.135) == 0.14", halfEven(0.125).equals("0.12") && halfEven(0.135).equals("0.14"));
        ExerciseChecker.check("5  currencyUs(-1234.5) == -$1,234.50", currencyUs(-1234.5).equals("-$1,234.50"));
        ExerciseChecker.check("6  currencyFr(1234.5) == 1\\u202f234,50\\u00a0EUR", currencyFr(1234.5).equals("1 234,50 €"));
        ExerciseChecker.check("7  percentFr(0.256) == 26\\u00a0%", percentFr(0.256).equals("26 %"));
        ExerciseChecker.check("8  german(1234567.891) == 1.234.567,891", german(1234567.891).equals("1.234.567,891"));
        ExerciseChecker.check("9  compact(1200000) == 1M", compact(1_200_000).equals("1M"));
        ExerciseChecker.check("10 parsePrefix(12abc) == 12 ; parsePrefix(abc) == -1 (ParseException)",
                parsePrefix("12abc") == 12 && parsePrefix("abc") == -1);
        ExerciseChecker.check("11 inbox(Ana, 3) == Ana a 3 messages", inbox("Ana", 3).equals("Ana a 3 messages"));
        ExerciseChecker.check("12 apostrophe == It's ok", apostrophe().equals("It's ok"));

        ExerciseChecker.summary();
    }
}
