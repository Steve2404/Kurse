package ch11_exceptions.drills.r07_decimalformat.solution;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

/**
 * SOLUTION du drill de rappel 7 - les motifs DecimalFormat.
 */
public class Recall07 {

    static final DecimalFormatSymbols US = DecimalFormatSymbols.getInstance(Locale.US);

    static String f(String pattern, double value) {
        return new DecimalFormat(pattern, US).format(value);
    }

    public static void main(String[] args) throws ParseException {
        // 0 : chiffre OBLIGATOIRE (complete par des zeros) ; # : chiffre FACULTATIF (rien si absent).
        System.out.println("D01 : " + f("###.##", 3.5) + " | " + f("000.00", 3.5) + " | " + f("#.##", 0.456) + " | " + f("0.##", 0.456));
        System.out.println("D02 : " + f("#,###", 1234567) + " | " + f("#,##0.0", 1234567.89) + " | " + f("#,##", 1234567));
        System.out.println("D03 : " + f("0.0", 0.25) + " " + f("0.0", 0.35) + " " + f("0", 2.5) + " " + f("0", -2.5));
        System.out.println("D04 : " + f("#,##0.00;(#,##0.00)", -1234.5) + " | " + f("0.0%", 0.0789) + " | " + f("'#'0", 7) + " | " + f("0 'pts'", 12));
        DecimalFormat german = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.GERMANY));
        System.out.println("D05 : " + german.format(1234.5) + " | " + german.toPattern() + " | " + new DecimalFormat("#,##0.00", US).parse("1,234.50xyz"));
        DecimalFormat money = new DecimalFormat("$#,##0.00", US);
        money.setMinimumIntegerDigits(3);
        System.out.println("D06 : " + money.format(5.1) + " | " + money.parse("$1,000.25") + " | " + money.parse("$0042"));
    }
}
