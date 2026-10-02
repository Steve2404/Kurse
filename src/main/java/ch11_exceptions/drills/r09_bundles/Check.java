package ch11_exceptions.drills.r09_bundles;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 9 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall09, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [en_CA] Hello Canada / color / taille racine",
            "D02 : [en] Hello en / color / taille racine",
            "D03 : [fr] Bonjour / colour / taille racine",
            "D04 : [color, hello, only, size] true seulement ici | MissingResourceException nope",
            "D05 : en_CA en-CA true en Canada Japanisch fr_FR",
            "D06 : Französisch (Frankreich) | fr_FR de_DE fr_FR",
            "D07 : [fr] Bonjour / colour / taille racine | String");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Locale.setDefault(Locale.FRANCE)", "ResourceBundle.getBundle(", ".getLocale()", ".keySet()",
            ".containsKey(", "catch (MissingResourceException", ".getKey()", "new Locale(\"en\", \"CA\")",
            "new Locale.Builder()", "Locale.forLanguageTag(", ".toLanguageTag()", ".getDisplayCountry(",
            ".getDisplayLanguage(", "Locale.Category.DISPLAY", "Locale.Category.FORMAT", ".getObject(",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall09", args, EXPECTED, API);
    }
}
