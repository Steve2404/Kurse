package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 7 - Promotion et casts : ecris les regles que Java applique en silence (niveau : difficile)
 * ===================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_PreAndPostIncrementDecrement.java.
 *
 * -- Le contexte --
 *
 * Les exercices 04 a 06 ont montre les effets : byte + byte donne un
 * int, (byte) 130 donne -126, (int) -3.9 donne -3, b += 5 cache un
 * cast. Ici, tu reecris ces regles toi-meme, et main() les compare a
 * ce que fait VRAIMENT Java, sur des dizaines de valeurs.
 *
 *
 * ==================================================================
 * TODO 1 : binaryPromotion(left, right)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand deux nombres de types differents se rencontrent (+, -, *, /,
 * %), le plus "grand" gagne, et personne ne reste plus petit qu'un
 * int. L'echelle : int < long < float < double (oui, float est plus
 * "grand" que long : il va beaucoup plus loin, meme s'il est moins
 * precis).
 *
 * -- Essayons a la main (verdicts reels de javac 17) --
 *
 *   byte + byte -> int     byte + short -> int    char + char -> int
 *   short + long -> long   long + float -> float  int + double -> double
 *
 * -- Le plan --
 *
 *   1. Si l'un est double -> "double".
 *   2. Sinon si l'un est float -> "float".
 *   3. Sinon si l'un est long -> "long".
 *   4. Sinon -> "int".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : wrapToByte(value)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un byte, c'est un compteur a 256 positions (de -128 a 127) : quand
 * on depasse 127, il refait le tour par -128, comme une horloge.
 * (byte) garde seulement les 8 derniers bits. Tu dois retrouver ce
 * resultat SANS le cast (byte), avec des calculs :
 *
 *   (value + 128) ramene l'intervalle a 0..255,
 *   % 256 fait le tour de l'horloge (attention : en Java, % d'un
 *   negatif est negatif : -7 % 3 == -1 ; d'ou le "+ 256" puis "% 256"),
 *   - 128 revient a -128..127.
 *
 * Attention au debordement : value + 128 deborde si value vaut
 * Integer.MAX_VALUE. Calcule en long (128L).
 *
 * -- Essayons a la main (valeurs reelles) --
 *
 *   130 -> -126 ; -129 -> 127 ; 256 -> 0 ; 200 -> -56 ; 127 -> 127
 *
 * -- Le plan --
 *
 *   1. r = ((value + 128L) % 256 + 256) % 256 - 128, en long.
 *   2. Rendre r converti en int (r est entre -128 et 127).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : truncateTowardZero(value)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * (int) ne fait jamais d'arrondi : il COUPE ce qui depasse la virgule,
 * en allant vers zero. 3.9 -> 3, mais -3.9 -> -3 (et pas -4). Math.floor
 * descend toujours (-3.9 -> -4.0), Math.ceil monte toujours (-3.9 -> -3.0).
 * Couper vers zero, c'est donc floor pour les positifs et ceil pour
 * les negatifs.
 *
 * -- Le plan --
 *
 *   1. value >= 0 ? Math.floor(value) : Math.ceil(value).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : safeMultiply(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * 1000000 * 3000 en int deborde EN SILENCE et donne -1294967296. Pour
 * obtenir 3000000000, il faut que la multiplication se fasse deja en
 * long. PIEGE : (long) (a * b) ne sert a rien, le debordement a deja
 * eu lieu DANS la parenthese. Il faut convertir un operande AVANT.
 *
 * -- Le plan --
 *
 *   1. Rendre (long) a * b  (le cast s'applique a a, puis la promotion
 *      fait le reste).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : les 7 promotions du tableau ; wrapToByte ==
 * (byte) pour 1000 valeurs et les extremes ; truncateTowardZero ==
 * (int) sur des positifs et negatifs ; safeMultiply(1000000, 3000) ==
 * 3000000000.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - left.equals("double") || right.equals("double")
 *   - long r = ((value + 128L) % 256 + 256) % 256 - 128; return (int) r;
 *   - Math.floor(x), Math.ceil(x) rendent des double
 */
public class Exercise07_PromotionAndCastRules {

    public static String binaryPromotion(String left, String right) {
        throw new UnsupportedOperationException("TODO 1 : implementer binaryPromotion()");
    }

    public static int wrapToByte(int value) {
        throw new UnsupportedOperationException("TODO 2 : implementer wrapToByte()");
    }

    public static double truncateTowardZero(double value) {
        throw new UnsupportedOperationException("TODO 3 : implementer truncateTowardZero()");
    }

    public static long safeMultiply(int a, int b) {
        throw new UnsupportedOperationException("TODO 4 : implementer safeMultiply()");
    }

    public static void main(String[] args) {
        String[][] promotions = {{"byte", "byte", "int"}, {"byte", "short", "int"}, {"char", "char", "int"},
                {"short", "long", "long"}, {"long", "float", "float"}, {"int", "double", "double"}, {"char", "int", "int"}};
        boolean promotionsOk = true;
        for (String[] p : promotions) {
            promotionsOk &= binaryPromotion(p[0], p[1]).equals(p[2]);
        }
        ExerciseChecker.check("1 binaryPromotion reproduit les 7 verdicts de javac", promotionsOk);

        boolean wrapOk = wrapToByte(130) == -126 && wrapToByte(-129) == 127 && wrapToByte(256) == 0 && wrapToByte(200) == -56;
        for (int v = -500; v <= 500; v++) {
            wrapOk &= wrapToByte(v) == (byte) v;
        }
        wrapOk &= wrapToByte(Integer.MAX_VALUE) == (byte) Integer.MAX_VALUE
                && wrapToByte(Integer.MIN_VALUE) == (byte) Integer.MIN_VALUE;
        ExerciseChecker.check("2 wrapToByte == (byte) pour -500..500, MIN et MAX", wrapOk);

        boolean truncOk = true;
        for (double d : new double[]{3.9, -3.9, 0.5, -0.5, 7.0, -7.0, 123.456, -123.456}) {
            truncOk &= truncateTowardZero(d) == (int) d;
        }
        ExerciseChecker.check("3 truncateTowardZero == (int) (3.9 -> 3, -3.9 -> -3)", truncOk);

        ExerciseChecker.check("4 safeMultiply(1000000, 3000) == 3000000000 (et pas -1294967296)",
                safeMultiply(1000000, 3000) == 3_000_000_000L);

        ExerciseChecker.summary();
    }
}
