package ch11_exceptions.projects.p07_shop;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 7 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Shop, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "reglages : [promo.item, shipping.free, shop.name], promo 10 %, stock null (pas une String !), get 42",
            "[fr-FR -> bundle fr] Bienvenue chez Javashop, Marie ! | Votre panier : 5 article(s) pour 30,97_€. | Free shipping from 50,00_€ | Aujourd'hui : -10 % sur l'article stylo | Au revoir",
            "[fr-CA -> bundle fr_CA] Bienvenue chez Javashop, mon ami Louis ! | Votre panier : 2 article(s) pour 60,89_$_CA. | Aujourd'hui : -10 % sur l'article stylo | Au revoir",
            "[de-DE -> bundle de] Willkommen bei Javashop, Jonas! | Ihr Warenkorb: 3 Artikel fuer 64,48_€. | Auf Wiedersehen",
            "[it-IT -> bundle de] Willkommen bei Javashop, Giulia! | Ihr Warenkorb: 1 Artikel fuer 1,99_€. | Free shipping from 50,00_€ | Auf Wiedersehen",
            "[en -> bundle de] Willkommen bei Javashop, Sam! | Order rejected: produit inconnu : parapluie | Auf Wiedersehen",
            "[en-US -> bundle de] Willkommen bei Javashop, Ann! | Order rejected: For input string: \"x\" | Auf Wiedersehen",
            "racine : [bye, cart, error, legal, shipping, welcome] ; legal = Prices include VAT. Delivery within 3 days.",
            "cle absente : MissingResourceException, cle nope",
            "bundle absent : MissingResourceException",
            "traduction : | fr-CA 4/6 (67_%) | fr 4/6 (67_%) | de 3/6 (50_%) | en 0/6 (0_%)",
            "locales : fr_CA fr-CA egales true, français (Canada) / Französisch (Kanada), langue fr, pays CA",
            "Builder : IllformedLocaleException",
            "categories : 1_234,50_€ ; German (Germany) ; defaut de_DE, FORMAT fr_FR, DISPLAY en_US");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.SETTINGS", "Data.CATALOG", "Data.CUSTOMERS", "Data.COVERAGE",
            "new Properties()", ".setProperty(", ".getProperty(", ".stringPropertyNames()",
            "ResourceBundle.getBundle(", ".getLocale()", "new MessageFormat(", ".containsKey(",
            "catch (MissingResourceException", ".getKey()", "ResourceBundle.Control.getNoFallbackControl(", "Locale.ROOT",
            "new Locale.Builder()", "Locale.CANADA_FRENCH", "Locale.forLanguageTag(", ".toLanguageTag()",
            ".getDisplayName(", "catch (IllformedLocaleException", "Locale.Category.FORMAT", "Locale.Category.DISPLAY",
            "Locale.setDefault(Locale.GERMANY)", "catch (UnknownProductException | NumberFormatException", "NumberFormat.getPercentInstance(",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Shop", args, EXPECTED, API);
    }
}
