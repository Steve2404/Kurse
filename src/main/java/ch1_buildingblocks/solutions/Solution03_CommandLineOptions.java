package ch1_buildingblocks.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise03_CommandLineOptions.
 */
public class Solution03_CommandLineOptions {

    public static boolean isOption(String arg) {
        // "--" tout seul n'a pas de nom : on exige au moins un caractere apres les tirets.
        return arg.startsWith("--") && arg.length() > 2;
    }

    public static String optionName(String arg) {
        // indexOf rend -1 quand il n'y a pas de "=" : c'est le cas de l'interrupteur (--vip).
        int equals = arg.indexOf('=');
        return equals < 0 ? arg.substring(2) : arg.substring(2, equals);
    }

    public static String optionValue(String arg) {
        // Un interrupteur present vaut "allume" ; substring(fin) donne "" pour "--note=".
        int equals = arg.indexOf('=');
        return equals < 0 ? "true" : arg.substring(equals + 1);
    }

    public static String findOption(String[] args, String name, String defaultValue) {
        // On ne s'arrete pas au premier trouve : la derniere occurrence doit gagner.
        String result = defaultValue;
        for (String arg : args) {
            if (isOption(arg) && optionName(arg).equals(name)) {
                result = optionValue(arg);
            }
        }
        return result;
    }

    public static int intOption(String[] args, String name, int defaultValue) {
        // null signale "absente" ; parseInt lance NumberFormatException sur un texte non numerique.
        String text = findOption(args, name, null);
        if (text == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static List<String> positionalArguments(String[] args) {
        // L'ordre de saisie est conserve : on parcourt le tableau du debut a la fin.
        List<String> result = new ArrayList<>();
        for (String arg : args) {
            if (!isOption(arg)) {
                result.add(arg);
            }
        }
        return result;
    }
}
