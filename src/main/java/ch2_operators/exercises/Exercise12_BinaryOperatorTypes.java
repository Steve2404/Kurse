package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 12 - Chaque operateur binaire exige des types COMPATIBLES : ecris le controleur de javac (niveau : difficile)
 * ====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_PreAndPostIncrementDecrement.java.
 *
 * -- Le contexte --
 *
 * Le resume du chapitre insiste : "il est important de remarquer
 * quand un operateur et ses operandes ne correspondent pas". Tu as
 * ecrit le controleur des operateurs UNAIRES (Exercise02) ; voici
 * celui des operateurs a DEUX operandes. Types possibles : boolean,
 * byte, short, char, int, long, float, double, String, et "null"
 * (le litteral null).
 *
 * Verdicts REELS de javac 17 ("var r = gauche OP droite;") :
 *
 *   int + int -> int          boolean + int -> ERREUR : bad operand types for binary operator '+'
 *   String + boolean -> String   char + char -> int   byte + byte -> int
 *   byte + short -> int       short + long -> long     long + float -> float
 *   int + double -> double    String + null -> String  char + String -> String
 *   int == boolean -> ERREUR : incomparable types: int and boolean
 *   String == int  -> ERREUR : bad operand types for binary operator '=='
 *   String == null -> boolean   int == double -> boolean   char == int -> boolean
 *   boolean && boolean -> boolean   int && int -> ERREUR
 *   int & int -> int    boolean & boolean -> boolean    int & boolean -> ERREUR
 *   boolean ^ boolean -> boolean    int ^ long -> long
 *   int << long -> int  (le type vient de l'operande de GAUCHE seulement)
 *   byte << int -> int  long >> int -> long
 *   int < double -> boolean   boolean < boolean -> ERREUR
 *   char * String -> ERREUR : bad operand types for binary operator '*'
 *
 * Les regles, operateur par operateur :
 *
 *   +          : si l'un des deux est String -> String (tout se concatene,
 *                meme boolean et null). Sinon deux nombres -> promotion. Sinon ERREUR.
 *   - * / %    : deux nombres -> promotion. Sinon ERREUR.
 *   < <= > >=  : deux nombres -> boolean. Sinon ERREUR.
 *   == !=      : deux nombres -> boolean ; deux boolean -> boolean ;
 *                deux references (String ou null) -> boolean. Sinon ERREUR.
 *   && ||      : deux boolean -> boolean. Sinon ERREUR.
 *   & | ^      : deux boolean -> boolean ; deux entiers -> promotion. Sinon ERREUR.
 *   << >> >>>  : deux entiers -> promotion unaire de l'operande de GAUCHE. Sinon ERREUR.
 *
 * (promotion = regle de l'Exercise07 ; promotion unaire = regle de
 * l'Exercise02 : byte/short/char -> int.)
 *
 *
 * ==================================================================
 * TODO 1 : isNumeric(type), isIntegral(type), isReference(type)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Trois familles pour trier les operandes : les nombres (char compris),
 * les entiers (sans float ni double), et les references (ici String et
 * null).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Ce SONT des boites magiques, utilisees par le TODO 3.
 *
 *
 * ==================================================================
 * TODO 2 : promote(left, right)  et  promoteOne(type)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   promote    : double > float > long > int (comme l'Exercise07).
 *   promoteOne : byte, short, char -> "int" ; sinon le type (comme l'Exercise02).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Ce SONT des boites magiques pour le TODO 3.
 *
 *
 * ==================================================================
 * TODO 3 : resultType(operator, left, right)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On applique le tableau des regles ci-dessus, operateur par
 * operateur. Chaque ligne du tableau est une petite condition.
 *
 * -- Essayons a la main --
 *
 *   ("+", "String", "boolean") : un String -> "String"
 *   ("==", "int", "boolean")   : ni 2 nombres, ni 2 boolean, ni 2 references -> "ERREUR"
 *   ("<<", "int", "long")      : 2 entiers -> promoteOne("int") -> "int"
 *
 * -- Le plan --
 *
 *   1. Un switch sur l'operateur, en regroupant ceux qui partagent une regle.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : TODO 1 et 2.
 *
 *
 * Exemple a verifier : les 29 verdicts de javac ci-dessus.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - switch (operator) { case "-": case "*": case "/": case "%": ... }
 *   - left.equals("String") || right.equals("String")
 */
public class Exercise12_BinaryOperatorTypes {

    public static boolean isNumeric(String type) {
        throw new UnsupportedOperationException("TODO 1 : implementer isNumeric()");
    }

    public static boolean isIntegral(String type) {
        throw new UnsupportedOperationException("TODO 1 : implementer isIntegral()");
    }

    public static boolean isReference(String type) {
        throw new UnsupportedOperationException("TODO 1 : implementer isReference()");
    }

    public static String promote(String left, String right) {
        throw new UnsupportedOperationException("TODO 2 : implementer promote()");
    }

    public static String promoteOne(String type) {
        throw new UnsupportedOperationException("TODO 2 : implementer promoteOne()");
    }

    public static String resultType(String operator, String left, String right) {
        throw new UnsupportedOperationException("TODO 3 : implementer resultType()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 familles : char numerique, double non entier, null reference",
                isNumeric("char") && !isIntegral("double") && isReference("null") && isReference("String")
                        && !isReference("int") && !isNumeric("boolean"));
        ExerciseChecker.check("2 promote(short, long) == long, promoteOne(byte) == int",
                promote("short", "long").equals("long") && promoteOne("byte").equals("int"));

        String[][] javacVerdicts = {
                {"+", "int", "int", "int"}, {"+", "boolean", "int", "ERREUR"}, {"+", "String", "boolean", "String"},
                {"+", "char", "char", "int"}, {"+", "byte", "byte", "int"}, {"+", "byte", "short", "int"},
                {"+", "short", "long", "long"}, {"+", "long", "float", "float"}, {"+", "int", "double", "double"},
                {"+", "String", "null", "String"}, {"+", "char", "String", "String"},
                {"==", "int", "boolean", "ERREUR"}, {"==", "String", "int", "ERREUR"}, {"==", "String", "null", "boolean"},
                {"==", "int", "double", "boolean"}, {"==", "char", "int", "boolean"},
                {"&&", "boolean", "boolean", "boolean"}, {"&&", "int", "int", "ERREUR"},
                {"&", "int", "int", "int"}, {"&", "boolean", "boolean", "boolean"}, {"&", "int", "boolean", "ERREUR"},
                {"^", "boolean", "boolean", "boolean"}, {"^", "int", "long", "long"},
                {"<<", "int", "long", "int"}, {"<<", "byte", "int", "int"}, {">>", "long", "int", "long"},
                {"<", "int", "double", "boolean"}, {"<", "boolean", "boolean", "ERREUR"}, {"*", "char", "String", "ERREUR"}};
        int ok = 0;
        for (String[] v : javacVerdicts) {
            if (resultType(v[0], v[1], v[2]).equals(v[3])) {
                ok++;
            } else {
                System.out.println("   ecart : " + v[1] + " " + v[0] + " " + v[2] + " -> attendu " + v[3]);
            }
        }
        ExerciseChecker.check("3 resultType reproduit les 29 verdicts de javac (" + ok + "/29)", ok == 29);

        ExerciseChecker.summary();
    }
}
