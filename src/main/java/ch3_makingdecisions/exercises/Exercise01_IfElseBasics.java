package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 1 - if/else : les bases, et 2 pieges d'INDENTATION TROMPEUSE (niveau : moyen)
 * ===============================================================================================
 *
 * -- Rappel du decoupage en "boites magiques" --
 *
 * Une methode, c'est une boite magique : tu la nourris d'ingredients
 * (parametres), et elle rend un resultat, sans que tu aies besoin de
 * savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
 * demande-toi : est-ce qu'elle se raconte seule ? revient-elle
 * plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
 * moins une question, elle merite sa propre boite.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Java NE REGARDE JAMAIS l'indentation de ton code (les espaces/
 * tabulations sont juste pour TES yeux a TOI, jamais pour le
 * compilateur). Sans accolades {}, un if (ou un else) ne "capture"
 * QUE la toute PROCHAINE instruction, JAMAIS un bloc entier de
 * plusieurs lignes - meme si elles sont indentees pour "faire croire"
 * le contraire. Et quand un else suit PLUSIEURS if imbriques sans
 * accolades, il se colle TOUJOURS au if le PLUS PROCHE (le plus
 * interne), jamais a un if plus loin - meme si l'indentation suggere
 * autre chose.
 *
 *
 * ==================================================================
 * TODO : classifyNumber(n)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * classifyNumber(-5) == "negatif". classifyNumber(0) == "zero".
 * classifyNumber(5) == "positif".
 *
 * -- Le plan --
 *
 *   1. Si n < 0 : renvoyer "negatif".
 *   2. Sinon, si n == 0 : renvoyer "zero".
 *   3. Sinon (n > 0) : renvoyer "positif".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit.
 *
 * Exemple a verifier : voir les 3 cas ci-dessus. Regardez AUSSI
 * demonstrateBraceTrap() et DANGLING_ELSE_RESULT plus bas (deja
 * ecrits, RIEN a completer) : ils prouvent en direct les 2 pieges
 * d'indentation decrits plus haut.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "if (n < 0) { ... } else if (n == 0) { ... } else { ... }" -
 *     TOUJOURS ecrire les accolades, meme pour une seule instruction
 *     : ca evite EXACTEMENT les 2 pieges de cet exercice.
 */
public class Exercise01_IfElseBasics {

    public static String classifyNumber(int n) {
        throw new UnsupportedOperationException("TODO : implementer classifyNumber()");
    }

    // -- Piege 1 : instruction "capturee" sans accolades --
    // Malgre l'indentation qui LAISSE CROIRE que les 2 lignes sont
    // "dans" le if, SEULE counter++ (la 1ere ligne) en fait vraiment
    // partie - la 2eme s'execute TOUJOURS, meme flag == false.
    private static int counter = 0;

    private static void demonstrateBraceTrap(boolean flag) {
        if (flag)
            counter++;
        counter++; // s'execute TOUJOURS, quel que soit flag (voir l'indentation du fichier source)
    }

    // -- Piege 2 : dangling else --
    // Le else ci-dessous se colle au if (y > 0) le PLUS PROCHE,
    // JAMAIS au if (x > 0) plus externe - meme si l'indentation du
    // fichier source suggere le contraire.
    private static String danglingElseDemo(int x, int y) {
        String result = "aucun";
        if (x > 0)
            if (y > 0)
                result = "les deux positifs";
            else
                result = "x positif seulement";
        return result;
    }

    public static void main(String[] args) {
        ExerciseChecker.check("classifyNumber(-5) == \"negatif\"", classifyNumber(-5).equals("negatif"));
        ExerciseChecker.check("classifyNumber(0) == \"zero\"", classifyNumber(0).equals("zero"));
        ExerciseChecker.check("classifyNumber(5) == \"positif\"", classifyNumber(5).equals("positif"));

        counter = 0;
        demonstrateBraceTrap(false);
        ExerciseChecker.check("Piege 1 : counter vaut 1 (PAS 0) meme avec flag=false - la 2e ligne n'etait PAS dans le if",
                counter == 1);

        ExerciseChecker.check("Piege 2 : x=-5,y=5 -> \"aucun\" (le if EXTERNE, lui, est bien respecte)",
                danglingElseDemo(-5, 5).equals("aucun"));
        ExerciseChecker.check("Piege 2 : x=5,y=-5 -> l'else s'est colle au if INTERNE (y > 0), pas a x > 0",
                danglingElseDemo(5, -5).equals("x positif seulement"));

        ExerciseChecker.summary();
    }
}
