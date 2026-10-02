package ch11_exceptions.projects.p07_shop.solution;

import ch11_exceptions.projects.p07_shop.Data;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * SOLUTION du projet 7 (capstone) - la boutique internationale : Locale, ResourceBundle, MessageFormat, Properties.
 */
public class Shop {

    // Le nom de base = paquet + nom des fichiers, SANS suffixe de locale ni ".properties".
    static final String BASE = Shop.class.getPackageName() + ".shop";

    static String visible(String text) {
        return text.replace(' ', '_').replace(' ', '_');
    }

    // Un motif MessageFormat lu dans le bundle, rempli avec la locale du client.
    static String message(ResourceBundle bundle, String key, Locale locale, Object... values) {
        return new MessageFormat(bundle.getString(key), locale).format(values);
    }

    // Une cle est "traduite" dans une locale si sa valeur differe de celle de la racine.
    static String coverage(String tag) {
        ResourceBundle.Control noFallback = ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES);
        ResourceBundle root = ResourceBundle.getBundle(BASE, Locale.ROOT, noFallback);
        ResourceBundle bundle = ResourceBundle.getBundle(BASE, Locale.forLanguageTag(tag), noFallback);
        long translated = root.keySet().stream().filter(k -> !bundle.getString(k).equals(root.getString(k))).count();
        double ratio = (double) translated / root.keySet().size();
        return tag + " " + translated + "/" + root.keySet().size() + " (" + NumberFormat.getPercentInstance(Locale.FRANCE).format(ratio) + ")";
    }

    public static void main(String[] args) {
        // La locale par defaut intervient dans la RECHERCHE des bundles : on la fixe (allemand).
        Locale.setDefault(Locale.GERMANY);

        Properties settings = new Properties();
        for (String line : Data.SETTINGS) {
            String[] p = line.split("=");
            settings.setProperty(p[0], p[1]);
        }
        settings.put("stock", 42);                                  // Properties est une Hashtable<Object, Object>...
        String shopName = settings.getProperty("shop.name");
        long freeShipping = Long.parseLong(settings.getProperty("shipping.free"));
        String promoPercent = settings.getProperty("promo.percent", "10");   // valeur par defaut si la cle manque
        System.out.println("reglages : " + settings.stringPropertyNames().stream().sorted().toList() + ", promo " + promoPercent + " %, stock "
                + settings.getProperty("stock") + " (pas une String !), get " + settings.get("stock"));

        Catalog catalog = new Catalog(Data.CATALOG);
        for (String customer : Data.CUSTOMERS) {
            String[] p = customer.split("\\|");
            Locale locale = Locale.forLanguageTag(p[0]);
            ResourceBundle bundle = ResourceBundle.getBundle(BASE, locale);
            StringBuilder out = new StringBuilder("[" + p[0] + " -> bundle " + (bundle.getLocale().toString().isEmpty() ? "racine" : bundle.getLocale()) + "] ");
            NumberFormat money = NumberFormat.getCurrencyInstance(locale);   // la monnaie du PAYS de la locale
            out.append(message(bundle, "welcome", locale, shopName, p[1]));
            try {
                long[] cart = catalog.total(p[2]);
                out.append(" | ").append(message(bundle, "cart", locale, cart[0], money.format(cart[1] / 100.0)));
                if (cart[1] < freeShipping) {
                    out.append(" | ").append(message(bundle, "shipping", locale, money.format(freeShipping / 100.0)));
                }
            } catch (UnknownProductException | NumberFormatException e) {
                out.append(" | ").append(message(bundle, "error", locale, e.getMessage()));
            }
            if (bundle.containsKey("promo")) {
                out.append(" | ").append(message(bundle, "promo", locale, promoPercent, settings.getProperty("promo.item")));
            }
            out.append(" | ").append(bundle.getString("bye"));
            System.out.println(visible(out.toString()));
        }

        ResourceBundle root = ResourceBundle.getBundle(BASE, Locale.ROOT);
        System.out.println("racine : " + root.keySet().stream().sorted().toList() + " ; legal = " + root.getString("legal"));
        try {
            root.getString("nope");
        } catch (MissingResourceException e) {
            System.out.println("cle absente : " + e.getClass().getSimpleName() + ", cle " + e.getKey());
        }
        try {
            ResourceBundle.getBundle(Shop.class.getPackageName() + ".missing", Locale.FRANCE);
        } catch (MissingResourceException e) {
            System.out.println("bundle absent : " + e.getClass().getSimpleName());
        }

        StringBuilder cover = new StringBuilder("traduction :");
        for (String tag : Data.COVERAGE) {
            cover.append(" | ").append(coverage(tag));
        }
        System.out.println(visible(cover.toString()));

        // Quatre facons d'obtenir la meme Locale.
        Locale a = new Locale("FR", "ca");                          // la casse est normalisee
        Locale b = Locale.CANADA_FRENCH;
        Locale c = new Locale.Builder().setLanguage("fr").setRegion("CA").build();
        Locale d = Locale.forLanguageTag("fr-CA");
        System.out.println("locales : " + a + " " + d.toLanguageTag() + " egales " + (a.equals(b) && b.equals(c) && c.equals(d)) + ", " + a.getDisplayName(Locale.FRENCH)
                + " / " + a.getDisplayName(Locale.GERMAN) + ", langue " + a.getLanguage() + ", pays " + a.getCountry());
        try {
            new Locale.Builder().setRegion("Canada");
        } catch (IllformedLocaleException e) {
            System.out.println("Builder : " + e.getClass().getSimpleName());
        }

        // Deux categories : FORMAT (nombres, dates, monnaies) et DISPLAY (noms affiches des locales).
        Locale.setDefault(Locale.Category.FORMAT, Locale.FRANCE);
        Locale.setDefault(Locale.Category.DISPLAY, Locale.US);
        System.out.println(visible("categories : " + NumberFormat.getCurrencyInstance().format(1234.5) + " ; " + Locale.GERMANY.getDisplayName() + " ; defaut "
                + Locale.getDefault() + ", FORMAT " + Locale.getDefault(Locale.Category.FORMAT) + ", DISPLAY " + Locale.getDefault(Locale.Category.DISPLAY)));
    }
}
