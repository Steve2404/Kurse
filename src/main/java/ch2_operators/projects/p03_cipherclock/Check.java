package ch2_operators.projects.p03_cipherclock;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON CipherClock, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"3", "-29", "23", "45", "50", "-1500"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "=== CESAR ===",
            "clair     : OCP JAVA",
            "chiffre 3 : RFS MDYD",
            "cle -29 = cle 23 ; -3 % 26 = -3, mod(-3, 26) = 23",
            "aller-retour : OCP JAVA (cle totale -26)",
            "'A' + 2 = 67, (char) ('A' + 2) = C, 'Z' - 'A' = 25",
            "=== HORLOGE ===",
            "depart 23:45 | +50 min -> 00:35 (+1 j) | -1500 min -> 22:45 (-1 j)",
            "-75 / 1440 = 0 (division tronquee), plancher = -1",
            "=== DEBORDEMENTS ===",
            "byte 120 += 10 -> -126, (byte) 200 = -56, (short) 40000 = -25536",
            "MAX_VALUE + 1 = -2147483648, en long : 2147483648",
            "(int) 9.99 = 9, (int) -9.99 = -9, (int) 3e10 = 2147483647",
            "7 / 2 = 3, 7 / 2.0 = 3.5, 7 % -3 = 1, -7 % 3 = -1",
            "0.1 + 0.2 = 0.30000000000000004, 0.1f + 0.2f = 0.3");
            // EXPECTED-END

    static final List<String> API = List.of(
            "(char)", "(byte)", "(short)", "(int)",
            "+=", "Integer.MAX_VALUE", "1L", "re:\\(\\w+ % \\w+ \\+ \\w+\\) % \\w+##modulo toujours positif (v % b + b) % b",
            "- 'A'", "+ 'A'", "0.1f", "2.0",
            "Data.C8",
            // Crescendo : notions des chapitres 3 a 15, interdites au chapitre 2.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!do {",
            "!->", "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)", "!re:instanceof \\w+ \\w+##instanceof avec variable : pattern matching (chapitre 3)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "CipherClock", args, ARGS, EXPECTED, API);
    }
}
