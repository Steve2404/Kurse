package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 2 - Chaque operateur unaire exige SON type : ecris toi-meme la regle du compilateur (niveau : difficile)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_PreAndPostIncrementDecrement.java.
 *
 * -- Les regles --
 *
 *   !        : seulement un boolean -> boolean. Jamais un nombre
 *              (Java ne transforme jamais 0 en false).
 *   ~        : seulement un ENTIER (byte, short, char, int, long).
 *              byte/short/char sont d'abord promus en int -> int ;
 *              long -> long. Formule : ~x == -(x + 1).
 *   - et +   : un NOMBRE ; byte/short/char promus en int ; long,
 *              float, double gardent leur type. Jamais un String.
 *   ++ et -- : un NOMBRE, et le type NE CHANGE PAS : un byte reste
 *              un byte, un char reste un char (cast cache, comme +=).
 *
 * Verdicts REELS de javac 17 (type de "var r = <expression>;") :
 *
 *   !z boolean      !i ERREUR : bad operand type int for unary operator '!'
 *   ~i int   ~b int   ~c int   ~l long
 *   ~z ERREUR : bad operand type boolean for unary operator '~'
 *   ~d ERREUR : bad operand type double for unary operator '~'
 *   -str ERREUR : bad operand type String for unary operator '-'
 *   -c int   -b int   -d double   +s int
 *   b++ byte   c++ char   --l long   ++d double
 *
 * (z boolean, b byte, s short, c char, i int, l long, d double, str String)
 *
 *
 * ==================================================================
 * TODO 1 : isNumeric(type)  et  TODO 2 : isIntegral(type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Deux familles : les NOMBRES (byte, short, char, int, long, float,
 * double - oui, char est un nombre pour Java : 'A' vaut 65) et, parmi
 * eux, les ENTIERS (tous sauf float et double). boolean et String ne
 * sont dans aucune des deux.
 *
 * -- Essayons a la main --
 *
 *   isNumeric("char") true ; isNumeric("boolean") false
 *   isIntegral("long") true ; isIntegral("double") false
 *
 * -- Le plan --
 *
 *   Comparer le nom du type a la liste de sa famille.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Ce SONT des boites magiques, utilisees par le TODO 4.
 *
 *
 * ==================================================================
 * TODO 3 : unaryPromotion(type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour calculer, Java ne travaille jamais avec des "petits" entiers :
 * byte, short et char montent d'abord au rang int. Les autres types
 * restent tels quels.
 *
 * -- Essayons a la main --
 *
 *   "byte" -> "int" ; "char" -> "int" ; "long" -> "long" ; "double" -> "double"
 *
 * -- Le plan --
 *
 *   1. byte, short ou char -> "int" ; sinon le type lui-meme.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique pour le TODO 4.
 *
 *
 * ==================================================================
 * TODO 4 : unaryResultType(operator, type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le compilateur est un controleur : pour chaque operateur, il verifie
 * que l'operande a le bon type, puis annonce le type du resultat, ou
 * refuse ("ERREUR").
 *
 * -- Le plan --
 *
 *   "!"        : boolean -> "boolean", sinon "ERREUR".
 *   "~"        : entier -> unaryPromotion(type), sinon "ERREUR".
 *   "-" / "+"  : nombre -> unaryPromotion(type), sinon "ERREUR".
 *   "++"/"--"  : nombre -> le type LUI-MEME, sinon "ERREUR".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : TODO 1, 2 et 3.
 *
 *
 * ==================================================================
 * TODO 5 : complementByFormula(x)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * ~ retourne chaque bit. En Java, les negatifs sont codes en
 * "complement a deux", ce qui donne une formule simple : ~x == -(x+1).
 * Calcule-la SANS utiliser ~ ; main() compare avec le vrai ~x.
 *
 * -- Essayons a la main --
 *
 *   5 -> -6 ; -1 -> 0 ; 0 -> -1
 *
 * -- Le plan --
 *
 *   1. Rendre -(x + 1).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : les 17 verdicts javac du tableau, et ~x pour
 * plusieurs valeurs.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - type.equals("byte") || type.equals("short") || ...
 *   - switch (operator) { case "!": ... case "-": case "+": ... default: return "ERREUR"; }
 */
public class Exercise02_UnaryOperatorTypes {

    public static boolean isNumeric(String type) {
        throw new UnsupportedOperationException("TODO 1 : implementer isNumeric()");
    }

    public static boolean isIntegral(String type) {
        throw new UnsupportedOperationException("TODO 2 : implementer isIntegral()");
    }

    public static String unaryPromotion(String type) {
        throw new UnsupportedOperationException("TODO 3 : implementer unaryPromotion()");
    }

    public static String unaryResultType(String operator, String type) {
        throw new UnsupportedOperationException("TODO 4 : implementer unaryResultType()");
    }

    public static int complementByFormula(int x) {
        throw new UnsupportedOperationException("TODO 5 : implementer complementByFormula()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 isNumeric : char oui, boolean et String non",
                isNumeric("char") && !isNumeric("boolean") && !isNumeric("String"));
        ExerciseChecker.check("2 isIntegral : long oui, double non", isIntegral("long") && !isIntegral("double"));
        ExerciseChecker.check("3 unaryPromotion : byte et char -> int, long -> long, double -> double",
                unaryPromotion("byte").equals("int") && unaryPromotion("char").equals("int")
                        && unaryPromotion("long").equals("long") && unaryPromotion("double").equals("double"));

        String[][] javacVerdicts = {
                {"!", "boolean", "boolean"}, {"!", "int", "ERREUR"},
                {"~", "int", "int"}, {"~", "byte", "int"}, {"~", "char", "int"}, {"~", "long", "long"},
                {"~", "boolean", "ERREUR"}, {"~", "double", "ERREUR"},
                {"-", "String", "ERREUR"}, {"-", "char", "int"}, {"-", "byte", "int"}, {"-", "double", "double"},
                {"+", "short", "int"},
                {"++", "byte", "byte"}, {"++", "char", "char"}, {"--", "long", "long"}, {"++", "double", "double"}};
        int ok = 0;
        for (String[] v : javacVerdicts) {
            if (unaryResultType(v[0], v[1]).equals(v[2])) {
                ok++;
            } else {
                System.out.println("   ecart : " + v[0] + " " + v[1] + " -> attendu " + v[2]);
            }
        }
        ExerciseChecker.check("4 unaryResultType reproduit les 17 verdicts de javac (" + ok + "/17)", ok == 17);

        boolean formulaOk = true;
        for (int x : new int[]{5, -1, 0, 42, Integer.MAX_VALUE, Integer.MIN_VALUE}) {
            formulaOk &= complementByFormula(x) == ~x;
        }
        ExerciseChecker.check("5 complementByFormula(x) == ~x pour 6 valeurs (dont MIN et MAX)", formulaOk);

        ExerciseChecker.summary();
    }
}
