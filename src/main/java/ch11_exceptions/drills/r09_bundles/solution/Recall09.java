package ch11_exceptions.drills.r09_bundles.solution;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * SOLUTION du drill de rappel 9 - Locale et ResourceBundle (ordre de recherche).
 */
public class Recall09 {

    static final String BASE = Recall09.class.getPackageName() + ".labels";

    static String show(ResourceBundle b) {
        return "[" + b.getLocale() + "] " + b.getString("hello") + " / " + b.getString("color") + " / " + b.getString("size");
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.FRANCE);
        // Recherche : demandee (en_CA) -> sa langue (en) -> ... ; les cles absentes remontent vers les PARENTS (en, puis racine).
        System.out.println("D01 : " + show(ResourceBundle.getBundle(BASE, new Locale("en", "CA"))));
        System.out.println("D02 : " + show(ResourceBundle.getBundle(BASE, Locale.US)));
        // Aucun bundle de : on passe a la locale PAR DEFAUT (fr_FR -> fr). Le parent de fr est la racine, jamais "en".
        System.out.println("D03 : " + show(ResourceBundle.getBundle(BASE, Locale.GERMANY)));
        ResourceBundle canada = ResourceBundle.getBundle(BASE, Locale.CANADA);
        String missing;
        try {
            missing = canada.getString("nope");
        } catch (MissingResourceException e) {
            missing = e.getClass().getSimpleName() + " " + e.getKey();
        }
        System.out.println("D04 : " + canada.keySet().stream().sorted().toList() + " " + canada.containsKey("only") + " " + canada.getString("only") + " | " + missing);
        Locale a = new Locale("en", "CA");
        Locale b = new Locale.Builder().setRegion("CA").setLanguage("en").build();
        Locale c = Locale.forLanguageTag("en-CA");
        System.out.println("D05 : " + a + " " + c.toLanguageTag() + " " + (a.equals(Locale.CANADA) && a.equals(b) && b.equals(c)) + " " + new Locale("EN") + " "
                + a.getDisplayCountry(Locale.FRENCH) + " " + Locale.JAPAN.getDisplayLanguage(Locale.GERMAN) + " " + Locale.getDefault());
        Locale.setDefault(Locale.Category.DISPLAY, Locale.GERMANY);
        System.out.println("D06 : " + Locale.FRANCE.getDisplayName() + " | " + Locale.getDefault() + " " + Locale.getDefault(Locale.Category.DISPLAY) + " "
                + Locale.getDefault(Locale.Category.FORMAT));
    }
}
