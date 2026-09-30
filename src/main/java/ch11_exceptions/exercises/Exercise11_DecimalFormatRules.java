package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * EXERCICE 11 - Refaire DecimalFormat a la main : # contre 0, groupement, arrondi HALF_EVEN (niveau : difficile)
 * ==============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un motif DecimalFormat est un gabarit :
 *
 *   0   un chiffre OBLIGATOIRE (on complete avec des zeros)
 *   #   un chiffre FACULTATIF (rien s'il ne sert a rien)
 *   ,   separateur de milliers : la TAILLE DU DERNIER groupe compte ("#,##,###" groupe par 3)
 *   .   la virgule decimale
 *
 * Tu vas reecrire DecimalFormat (symboles US) et main() comparera ton
 * resultat au vrai DecimalFormat sur 11 motifs x 13 nombres.
 *
 * Trois pieges verifies en direct :
 *   - arrondi HALF_EVEN ("au pair") sur la VRAIE valeur binaire du double :
 *     0.125 -> 0.12 (egalite exacte : on va au pair) ; 0.135 -> 0.14 ;
 *     2.675 -> 2.67 (le double vaut en fait 2.67499999...) ;
 *   - un motif SANS AUCUN '0' mais avec un '.' ("#.##") recoit quand meme
 *     un chiffre entier obligatoire : 0.5 -> "0.5". Mais "#.00" -> ".50" !
 *   - un nombre negatif garde son signe meme arrondi a zero : -0.001 avec "0.00" -> "-0.00".
 *
 *
 * ==================================================================
 * TODO 1 : describe(pattern)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Rendre {minEntiers, tailleDuGroupe (0 si pas de ','), minDecimales, maxDecimales}.
 *
 * -- Essayons a la main --
 *
 *   "#,##0.00"  -> {1, 3, 2, 2}
 *   "000.#"     -> {3, 0, 0, 1}
 *   "#.##"      -> {1, 0, 0, 2}   (aucun '0' dans tout le motif, mais un '.' : 1 entier obligatoire)
 *   "#.00"      -> {0, 0, 2, 2}
 *   "#,##,###"  -> {0, 3, 0, 0}
 *
 * -- Le plan --
 *
 *   1. Couper au '.' : partie entiere, partie decimale (vide s'il n'y a pas de '.').
 *   2. minEntiers = nombre de '0' a gauche ; groupe = nombre de caracteres apres la derniere ',' a gauche.
 *   3. minDecimales = nombre de '0' a droite ; maxDecimales = longueur de la partie droite.
 *   4. Motif sans aucun '0' mais avec un '.' -> minEntiers = 1.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui (Q2) : "compter un caractere dans un texte" sert deux fois.
 *
 *
 * ==================================================================
 * TODO 2 : roundHalfEven(value, scale)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   (0.125, 2) -> "0.12" ; (0.135, 2) -> "0.14" ; (2.675, 2) -> "2.67" ; (2.5, 0) -> "2" ; (3.5, 0) -> "4"
 *
 * -- Le plan --
 *
 *   1. new BigDecimal(value) : la valeur EXACTE du double (pas BigDecimal.valueOf, qui passe par le texte "2.675").
 *   2. setScale(scale, RoundingMode.HALF_EVEN), puis toPlainString().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : format(value, pattern)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. d = describe(pattern) ; texte = roundHalfEven(|value|, maxDecimales).
 *   2. Couper texte en chiffres entiers et chiffres decimaux.
 *   3. Entiers : enlever les zeros de tete, puis completer a gauche jusqu'a minEntiers ; grouper.
 *   4. Decimales : enlever les zeros de queue tant qu'on reste >= minDecimales.
 *   5. Coller : signe "-" si value < 0, entiers, puis "." + decimales si elles existent.
 *   6. Si RIEN n'a ete ecrit (ni entier, ni decimale) : "0".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : describe (TODO 1), roundHalfEven (TODO 2), et une petite boite
 * "grouper des chiffres par paquets de n depuis la droite".
 *
 * Exemple a verifier : voir les tests de main() (143 comparaisons avec DecimalFormat).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - pattern.indexOf('.'), pattern.lastIndexOf(',')
 *   - new BigDecimal(value).setScale(scale, RoundingMode.HALF_EVEN).toPlainString()
 *   - StringBuilder.insert(0, ...) pour construire les groupes depuis la droite.
 */
public class Exercise11_DecimalFormatRules {

    public static int[] describe(String pattern) {
        throw new UnsupportedOperationException("TODO 1 : implementer describe()");
    }

    public static String roundHalfEven(double value, int scale) {
        throw new UnsupportedOperationException("TODO 2 : implementer roundHalfEven()");
    }

    public static String format(double value, String pattern) {
        throw new UnsupportedOperationException("TODO 3 : implementer format()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("describe : #,##0.00 / 000.# / #.## / #.00 / #,##,###",
                java.util.Arrays.equals(describe("#,##0.00"), new int[]{1, 3, 2, 2})
                        && java.util.Arrays.equals(describe("000.#"), new int[]{3, 0, 0, 1})
                        && java.util.Arrays.equals(describe("#.##"), new int[]{1, 0, 0, 2})
                        && java.util.Arrays.equals(describe("#.00"), new int[]{0, 0, 2, 2})
                        && java.util.Arrays.equals(describe("#,##,###"), new int[]{0, 3, 0, 0}));
        ExerciseChecker.check("roundHalfEven : 0.125 -> 0.12, 0.135 -> 0.14, 2.675 -> 2.67, 2.5 -> 2, 3.5 -> 4",
                roundHalfEven(0.125, 2).equals("0.12") && roundHalfEven(0.135, 2).equals("0.14")
                        && roundHalfEven(2.675, 2).equals("2.67") && roundHalfEven(2.5, 0).equals("2")
                        && roundHalfEven(3.5, 0).equals("4"));

        String[] patterns = {"#,##0.00", "000.#", "#.##", "0.0", "#,###", "00", "#,##0.###", "0.00#", "#", "#.00", "0,000.0"};
        double[] values = {1234.5, 0.125, 0.135, 2.675, -42.0, 1234567.891, 0.5, 7, 0.0, -0.001, 1.005, 999.995, 0.05};
        int agree = 0;
        int total = 0;
        String firstMiss = "";
        for (String p : patterns) {
            DecimalFormat real = new DecimalFormat(p, DecimalFormatSymbols.getInstance(Locale.US));
            for (double v : values) {
                total++;
                String expected = real.format(v);
                String mine = format(v, p);
                if (mine.equals(expected)) {
                    agree++;
                } else if (firstMiss.isEmpty()) {
                    firstMiss = " ; 1er ecart : format(" + v + ", \"" + p + "\") = " + mine + " au lieu de " + expected;
                }
            }
        }
        ExerciseChecker.check("format == DecimalFormat sur " + total + " cas (" + agree + " d'accord)" + firstMiss, agree == total);

        ExerciseChecker.summary();
    }
}
