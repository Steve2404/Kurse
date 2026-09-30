package ch11_exceptions.drills.solutions;

import ch11_exceptions.drills.Ledger;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.drills.exercises.Drill05_LocaleAndBundles.
 */
public class SolutionDrill05_LocaleAndBundles {

    public static String fromConstant() {
        // toString colle langue et pays avec un souligne.
        return Locale.CANADA_FRENCH.toString();
    }

    public static String fromConstructor() {
        // Le constructeur normalise : langue en minuscules, pays en majuscules.
        return new Locale("FR", "ca").toString();
    }

    public static String fromBuilder() {
        // Le Builder valide chaque partie ; setRegion = le pays.
        return new Locale.Builder().setLanguage("de").setRegion("CH").build().toString();
    }

    public static String fromTag() {
        // Une etiquette IETF utilise un TIRET ; toString rend un souligne.
        return Locale.forLanguageTag("pt-BR").toString();
    }

    public static String toTag() {
        // Le sens inverse : tiret.
        return Locale.CANADA_FRENCH.toLanguageTag();
    }

    public static String countryName() {
        // Le nom est donne dans la langue de la Locale passee en argument.
        return Locale.CANADA_FRENCH.getDisplayCountry(Locale.US);
    }

    public static String greeting(Locale locale) {
        // getBundle choisit le fichier le plus precis qui existe (fr_CA, sinon fr, sinon...).
        return ResourceBundle.getBundle(Ledger.BUNDLE, locale).getString("greeting");
    }

    public static String farewellFrench() {
        // Absente de messages_fr : la recherche remonte au parent, la racine.
        return ResourceBundle.getBundle(Ledger.BUNDLE, Locale.FRENCH).getString("farewell");
    }

    public static Set<String> keysFrench() {
        // Piege : keySet() inclut les cles heritees des parents, pas seulement celles de messages_fr.
        return new TreeSet<>(ResourceBundle.getBundle(Ledger.BUNDLE, Locale.FRENCH).keySet());
    }

    public static String withFormatCategory() {
        // La categorie FORMAT pilote les nombres et dates ; on restaure dans finally pour ne rien casser ailleurs.
        Locale before = Locale.getDefault(Locale.Category.FORMAT);
        try {
            Locale.setDefault(Locale.Category.FORMAT, Locale.GERMANY);
            return NumberFormat.getInstance().format(1234.5);
        } finally {
            Locale.setDefault(Locale.Category.FORMAT, before);
        }
    }

    public static String invalid(Locale locale, String raw) {
        // Le bundle fournit le gabarit, MessageFormat le remplit.
        return MessageFormat.format(ResourceBundle.getBundle(Ledger.BUNDLE, locale).getString("invalidRecord"), raw);
    }
}
