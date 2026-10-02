package ch11_exceptions.drills.r06_numberformat.solution;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * SOLUTION du drill de rappel 6 - NumberFormat et ses fabriques.
 */
public class Recall06 {

    // Les espaces insecables des locales deviennent '_' pour etre visibles.
    static String v(String text) {
        return text.replace(' ', '_').replace(' ', '_');
    }

    public static void main(String[] args) throws ParseException {
        double x = 1234567.891;
        System.out.println(v("D01 : " + NumberFormat.getInstance(Locale.US).format(x) + " | " + NumberFormat.getNumberInstance(Locale.GERMANY).format(x) + " | "
                + NumberFormat.getInstance(Locale.FRANCE).format(x)));
        System.out.println(v("D02 : " + NumberFormat.getCurrencyInstance(Locale.US).format(x) + " | " + NumberFormat.getCurrencyInstance(Locale.GERMANY).format(x)
                + " | " + NumberFormat.getCurrencyInstance(Locale.JAPAN).format(x) + " | " + NumberFormat.getCurrencyInstance(Locale.UK).format(-3.5)));
        System.out.println(v("D03 : " + NumberFormat.getPercentInstance(Locale.US).format(0.125) + " | " + NumberFormat.getPercentInstance(Locale.FRANCE).format(0.5)
                + " | " + NumberFormat.getIntegerInstance(Locale.US).format(2.5) + " " + NumberFormat.getIntegerInstance(Locale.US).format(3.5)));
        NumberFormat custom = NumberFormat.getNumberInstance(Locale.US);
        custom.setMinimumFractionDigits(2);
        custom.setMaximumFractionDigits(2);
        custom.setGroupingUsed(false);
        NumberFormat up = NumberFormat.getIntegerInstance(Locale.US);
        up.setRoundingMode(RoundingMode.HALF_UP);
        System.out.println("D04 : " + custom.format(x) + " | " + custom.format(7) + " | " + up.format(2.5));
        NumberFormat compact = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        NumberFormat compactLong = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.LONG);
        System.out.println("D05 : " + compact.format(1_500) + " " + compact.format(2_600_000) + " " + compactLong.format(2_600_000) + " " + compact.format(999_999));
        NumberFormat reader = NumberFormat.getInstance(Locale.US);
        Number a = reader.parse("3.75kg");
        Number b = reader.parse("42");
        String c;
        try {
            c = "" + reader.parse("kg3");
        } catch (ParseException e) {
            c = "ParseException " + e.getErrorOffset();
        }
        System.out.println("D06 : " + a + " " + a.getClass().getSimpleName() + " | " + b + " " + b.getClass().getSimpleName() + " | " + c);
    }
}
