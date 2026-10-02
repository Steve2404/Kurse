package ch11_exceptions.projects.p05_invoice;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Billing, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "facture : 4 lignes, total TTC en-US=$835.80 fr-FR=835,80_€ de-DE=835,80_€ de-CH=CHF_835.80 ja-JP=￥836",
            "TVA : | 0_% ou 0,0_% de 450,00_€ = 0,00_€ | 6_% ou 5,5_% de 35,96_€ = 1,98_€ | 20_% ou 20,0_% de 289,88_€ = 57,98_€",
            "arrondis : 2 4 3 | 1 2.67 1,234.568 1.234,50",
            "motif #,##0.00 : [1,234.50] [-7.25] [0.08]",
            "motif 0000.## : [1234.5] [-0007.25] [0000.08]",
            "motif #.# : [1234.5] [-7.2] [0.1]",
            "motif #,##0.00;(#,##0.00) : [1,234.50] [(7.25)] [0.08]",
            "motif '#'000 : [#1234] [-#007] [#000]",
            "motif 0.0% : [123450.0%] [-725.0%] [8.0%]",
            "compact 999 : 999 | 999 | 999 | 999",
            "compact 1234 : 1K | 1.2K | 1 thousand | 1 Tausend",
            "compact 1250000 : 1M | 1.2M | 1 million | 1 Million",
            "compact 7800000000 : 8B | 7.8B | 8 billion | 8 Milliarden",
            "lu en-US nombre \"1,234.56\" -> 1234.56",
            "lu de-DE nombre \"1.234,56\" -> 1234.56",
            "lu fr-FR nombre \"1 234,56\" -> 1",
            "lu en-US nombre \"12abc\" -> 12",
            "lu en-US nombre \"abc\" -> ParseException Unparseable number: \"abc\" (position 0)",
            "lu en-US monnaie \"$12.50\" -> 12.5",
            "lu en-US monnaie \"12.50\" -> ParseException Unparseable number: \"12.50\" (position 0)",
            "partage de 835,80_€ en [2, 3, 4] : 185,73_€ 278,60_€ 371,47_€ (somme 835,80_€)",
            "mois  1 : mensualite     849,67, interets   30,00, capital     819,67, reste   9_180,33",
            "mois  2 : mensualite     849,67, interets   27,54, capital     822,13, reste   8_358,20",
            "mois 12 : mensualite     849,69, interets    2,54, capital     847,15, reste       0,00",
            "cout du credit : 196,06_€");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.LINES", "Data.LOCALES", "Data.PATTERNS", "Data.SAMPLES",
            "Data.BIG", "Data.INPUTS", "Data.WEIGHTS", "Data.LOAN",
            "record InvoiceLine(", "Locale.setDefault(Locale.US)", "Locale.forLanguageTag(", "NumberFormat.getCurrencyInstance(",
            "NumberFormat.getPercentInstance(", ".setMinimumFractionDigits(", "NumberFormat.getIntegerInstance(", ".setRoundingMode(RoundingMode.HALF_UP)",
            ".setMaximumFractionDigits(", "NumberFormat.getNumberInstance(", "NumberFormat.getInstance(", "String.format(Locale.GERMANY",
            "String.format(Locale.FRANCE", "new DecimalFormat(", "DecimalFormatSymbols.getInstance(Locale.US)", "NumberFormat.getCompactNumberInstance(",
            "NumberFormat.Style.SHORT", "NumberFormat.Style.LONG", ".parse(", "catch (ParseException",
            ".getErrorOffset()", "Math.pow(", "Math.round(",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Billing", args, EXPECTED, API);
    }
}
