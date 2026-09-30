package ch11_exceptions.solutions;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Corrige de l'exercice 11.
 */
public class Solution11_DecimalFormatRules {

    public static int[] describe(String pattern) {
        // '0' = chiffre obligatoire, '#' = facultatif ; seule la taille du DERNIER groupe compte pour la ','.
        int dot = pattern.indexOf('.');
        String left = dot < 0 ? pattern : pattern.substring(0, dot);
        String right = dot < 0 ? "" : pattern.substring(dot + 1);
        int minInt = count(left, '0');
        int comma = left.lastIndexOf(',');
        int grouping = comma < 0 ? 0 : left.length() - comma - 1;
        // Regle de DecimalFormat : "#.##" (aucun 0 du tout) recoit un chiffre entier obligatoire.
        if (dot >= 0 && count(pattern, '0') == 0) {
            minInt = 1;
        }
        return new int[]{minInt, grouping, count(right, '0'), right.length()};
    }

    public static String roundHalfEven(double value, int scale) {
        // new BigDecimal(double) garde la valeur binaire exacte : 2.675 vaut 2.67499..., donc 2.67 comme DecimalFormat.
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN).toPlainString();
    }

    public static String format(double value, String pattern) {
        // On arrondit la valeur absolue, puis on habille : zeros obligatoires, groupes, decimales facultatives, signe.
        int[] d = describe(pattern);
        String plain = roundHalfEven(Math.abs(value), d[3]);
        int dot = plain.indexOf('.');
        String intDigits = (dot < 0 ? plain : plain.substring(0, dot)).replaceFirst("^0+", "");
        StringBuilder frac = new StringBuilder(dot < 0 ? "" : plain.substring(dot + 1));
        while (intDigits.length() < d[0]) {
            intDigits = "0" + intDigits;
        }
        while (frac.length() > d[2] && frac.charAt(frac.length() - 1) == '0') {
            frac.setLength(frac.length() - 1);
        }
        String result = group(intDigits, d[1]) + (frac.length() > 0 ? "." + frac : "");
        if (result.isEmpty()) {
            result = "0";
        }
        // Le signe suit la valeur d'origine : -0.001 arrondi a zero s'affiche quand meme "-0.00".
        return (value < 0 ? "-" : "") + result;
    }

    private static String group(String digits, int size) {
        // Des paquets de size chiffres en partant de la droite ; size 0 = pas de groupement.
        if (size == 0) {
            return digits;
        }
        StringBuilder sb = new StringBuilder();
        for (int end = digits.length(); end > 0; end -= size) {
            sb.insert(0, digits.substring(Math.max(0, end - size), end));
            if (end - size > 0) {
                sb.insert(0, ',');
            }
        }
        return sb.toString();
    }

    private static int count(String text, char c) {
        // Boite magique : combien de fois c apparait dans text.
        int n = 0;
        for (char x : text.toCharArray()) {
            if (x == c) {
                n++;
            }
        }
        return n;
    }
}
