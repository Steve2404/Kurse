package ch11_exceptions.drills.r06_numberformat;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 1,234,567.891 | 1.234.567,891 | 1_234_567,891",
            "D02 : $1,234,567.89 | 1.234.567,89_€ | ￥1,234,568 | -£3.50",
            "D03 : 12% | 50_% | 2 4",
            "D04 : 1234567.89 | 7.00 | 3",
            "D05 : 2K 3M 3 million 1000K",
            "D06 : 3.75 Double | 42 Long | ParseException 0");
            // EXPECTED-END

    static final List<String> API = List.of(
            "NumberFormat.getInstance(", "NumberFormat.getNumberInstance(", "NumberFormat.getCurrencyInstance(", "NumberFormat.getPercentInstance(",
            "NumberFormat.getIntegerInstance(", ".setMinimumFractionDigits(", ".setMaximumFractionDigits(", ".setGroupingUsed(false)",
            ".setRoundingMode(RoundingMode.HALF_UP)", "NumberFormat.getCompactNumberInstance(", "NumberFormat.Style.LONG", ".parse(",
            "catch (ParseException", ".getErrorOffset()", "throws ParseException",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
