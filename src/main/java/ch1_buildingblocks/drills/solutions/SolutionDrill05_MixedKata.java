package ch1_buildingblocks.drills.solutions;

import ch1_buildingblocks.drills.Shop;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.drills.exercises.Drill05_MixedKata.
 */
public class SolutionDrill05_MixedKata {

    public static int argumentCount() {
        // args est un tableau ordinaire : length est un champ, pas une methode.
        return Shop.ARGS.length;
    }

    public static String firstArticleToken() {
        // Les options commencent par "--" ; le premier autre argument est un article.
        for (String arg : Shop.ARGS) {
            if (!arg.startsWith("--")) {
                return arg;
            }
        }
        return null;
    }

    public static String clientOrDefault() {
        // Valeur par defaut si l'option manque : Java ne verifie rien dans args.
        for (String arg : Shop.ARGS) {
            if (arg.startsWith("--client=")) {
                return arg.substring("--client=".length());
            }
        }
        return "anonyme";
    }

    public static boolean isVipCustomer() {
        // Un interrupteur n'a pas de valeur : on teste sa presence exacte.
        for (String arg : Shop.ARGS) {
            if ("--vip".equals(arg)) {
                return true;
            }
        }
        return false;
    }

    public static int validQuantityCount() {
        // parseInt refuse "abc", "" et les nombres trop grands pour un int ; strip gere " 12 ".
        int count = 0;
        for (String raw : Shop.RAW_QUANTITIES) {
            if (parseOrNull(raw) != null) {
                count++;
            }
        }
        return count;
    }

    private static Integer parseOrNull(String raw) {
        // Petite boite magique reutilisee par les TODO 5 et 6 : null = "pas un int valide".
        try {
            return Integer.parseInt(raw.strip());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static int positiveQuantitySum() {
        // "+7" est accepte par parseInt ; "-2" est valide mais pas positif : 3 + 12 + 7.
        int sum = 0;
        for (String raw : Shop.RAW_QUANTITIES) {
            Integer quantity = parseOrNull(raw);
            if (quantity != null && quantity > 0) {
                sum += quantity;
            }
        }
        return sum;
    }

    public static int totalCents() {
        // Chaque article "nom:quantite:prix" : split puis deux conversions texte -> int.
        int total = 0;
        for (String arg : Shop.ARGS) {
            if (!arg.startsWith("--")) {
                String[] parts = arg.split(":");
                total += Integer.parseInt(parts[1]) * Integer.parseInt(parts[2]);
            }
        }
        return total;
    }

    public static String formatEuros(int cents) {
        // Division entiere et reste ; 5 centimes -> "0.05" grace au "0" ajoute.
        int rest = cents % 100;
        return cents / 100 + "." + (rest < 10 ? "0" : "") + rest;
    }

    public static int codeDigitSum() {
        // Un char chiffre n'est pas sa valeur : getNumericValue('7') == 7 (et pas 55).
        int sum = 0;
        for (char c : Shop.PRODUCT_CODE.toCharArray()) {
            if (Character.isDigit(c)) {
                sum += Character.getNumericValue(c);
            }
        }
        return sum;
    }

    public static int codeLetterCount() {
        // Le tiret n'est ni une lettre ni un chiffre.
        int count = 0;
        for (char c : Shop.PRODUCT_CODE.toCharArray()) {
            if (Character.isLetter(c)) {
                count++;
            }
        }
        return count;
    }

    public static int flagsValue() {
        // parseInt avec une base : "1010" en base 2 vaut 10.
        return Integer.parseInt(Shop.BINARY_FLAGS, 2);
    }

    public static boolean looksLikeName(String word) {
        // Regles de caracteres d'un identifiant : pas de chiffre au debut, $ et _ permis.
        if (word.isEmpty() || !Character.isJavaIdentifierStart(word.charAt(0))) {
            return false;
        }
        for (int i = 1; i < word.length(); i++) {
            if (!Character.isJavaIdentifierPart(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String header(String client) {
        // Bloc de texte pour la ligne fixe (avec son \n final), puis concatenation.
        return """
                == TICKET ==
                """ + "Client : " + client + "\n";
    }
}
