package ch11_exceptions.solutions;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Corrige de l'exercice 19.
 */
public class Solution19_BundleSearchOrderRules {

    public static List<String> candidates(String locale) {
        // Du plus precis (langue_PAYS) au moins precis (langue) ; la racine est ajoutee a part, tout a la fin.
        List<String> result = new ArrayList<>();
        result.add("messages_" + locale);
        int underscore = locale.indexOf('_');
        if (underscore >= 0) {
            result.add("messages_" + locale.substring(0, underscore));
        }
        return result;
    }

    public static List<String> searchOrder(String requested, String defaultLocale) {
        // Locale demandee, puis Locale par defaut, puis la racine : la racine ne passe jamais avant le defaut.
        LinkedHashSet<String> order = new LinkedHashSet<>(candidates(requested));
        order.addAll(candidates(defaultLocale));
        order.add("messages");
        return new ArrayList<>(order);
    }

    public static String chooseBundle(List<String> available, String requested, String defaultLocale) {
        // Le premier bundle EXISTANT dans l'ordre de recherche gagne.
        for (String name : searchOrder(requested, defaultLocale)) {
            if (available.contains(name)) {
                return name;
            }
        }
        return "MissingResourceException";
    }

    public static String lookup(String key, Map<String, Map<String, String>> bundles, String requested, String defaultLocale) {
        // Piege : la cle ne se cherche que dans les PARENTS du bundle choisi, jamais dans l'autre branche (ex. fr quand en_US a ete choisi).
        String chosen = chooseBundle(List.copyOf(bundles.keySet()), requested, defaultLocale);
        if (chosen.equals("MissingResourceException")) {
            return chosen;
        }
        List<String> chain = new ArrayList<>();
        if (!chosen.equals("messages")) {
            chain.addAll(candidates(chosen.substring("messages_".length())));
        }
        chain.add("messages");
        for (String name : chain) {
            Map<String, String> content = bundles.get(name);
            if (content != null && content.containsKey(key)) {
                return content.get(key);
            }
        }
        return "MissingResourceException";
    }
}
