package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

import java.util.Arrays;

/**
 * EXERCICE 1 - Les regles de declaration d'une methode, ecrites par toi et comparees a javac (niveau : difficile)
 * ==============================================================================================================
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
 * Une declaration de methode suit une recette dans cet ordre :
 *
 *   [modificateurs, dans N'IMPORTE QUEL ordre entre eux] [type de retour] [nom] ( [parametres] ) [throws ...]
 *
 * Ici, c'est TOI qui joues le compilateur : tu lis l'en-tete d'une
 * methode (un String) et tu dis si javac l'accepte, et sinon pourquoi.
 * main() compare tes reponses aux VRAIS verdicts de javac 17.
 *
 * -- Verdicts reels de javac 17 (en anglais), utilises dans main() --
 *
 *   public static void m()               -> compile
 *   static public void m()               -> compile (l'ordre des modificateurs est libre)
 *   final static public int m()          -> compile
 *   public private void m()              -> error: illegal combination of modifiers: public and private
 *   private protected void m()           -> error: illegal combination of modifiers: private and protected
 *   static static void m()               -> error: repeated modifier
 *   m(), public m(), static m()          -> error: invalid method declaration; return type required
 *   protected static final void m()      -> compile
 *   void public m()                      -> error: <identifier> expected (un modificateur APRES le type)
 *   public void static m()               -> error: <identifier> expected
 *   void m(int... a, String s)           -> error: varargs parameter must be the last parameter
 *   void m(int... a, int... b)           -> error: varargs parameter must be the last parameter
 *   void m(int a, int a)                 -> error: variable a is already defined in method m
 *   void m() throws IOException, SQLException -> compile
 *
 *   int m(int x) et long m(int x) dans la meme classe   -> error: method m(int) is already defined in class C
 *   void m(int[] a) et void m(int... a)                 -> error: cannot declare both m(int...) and m(int[]) in C
 *   int add(int a, int b) et double add(double a, double b) -> compile (vraie surcharge)
 *
 *   Noms : M, _m, $m -> compile ; 2m -> error: <identifier> expected ;
 *          _ -> error: as of release 9, '_' is a keyword, and may not be used as an identifier
 *
 *
 * ==================================================================
 * TODO 1 : paramTypes(header)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut juste les TYPES des parametres, dans l'ordre. Un varargs
 * "int... a" est en realite un tableau : on l'ecrit "int[]".
 *
 * -- Essayons a la main --
 *
 *   "void m(int a, String... rest)" -> ["int", "String[]"]
 *   "void m()"                      -> []
 *
 * -- Le plan --
 *
 *   1. inside = ce qui est entre '(' et ')' ; s'il est vide (strip) -> tableau vide.
 *   2. Pour chaque morceau de inside.split(",") : strip, prendre le 1er mot (le type),
 *      remplacer "..." par "[]".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non, mais elle sert aux TODO 2 et 3.
 *
 *
 * ==================================================================
 * TODO 2 : check(header)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On passe l'en-tete au crible, une regle apres l'autre, et on rend le
 * PREMIER probleme trouve (un code), ou "OK".
 *
 * -- Essayons a la main --
 *
 *   "static public void m()"     -> mots avant '(' : static, public, void, m -> "OK"
 *   "void public m()"            -> apres le type "void", on trouve encore "public" -> "MODIFIER_AFTER_TYPE"
 *   "m()"                        -> un seul mot, pas de type -> "RETURN_TYPE_REQUIRED"
 *   "void m(int... a, String s)" -> varargs pas en dernier -> "VARARGS_NOT_LAST"
 *
 * -- Le plan (dans cet ordre) --
 *
 *   1. words = la partie avant '(' coupee sur les blancs ; le dernier mot est le nom.
 *   2. MODIFIER_AFTER_TYPE : un modificateur apparait apres un mot qui n'en est pas un
 *      (parmi les mots avant le nom).
 *   3. RETURN_TYPE_REQUIRED : il n'y a qu'un mot, ou le mot juste avant le nom est un modificateur.
 *   4. REPEATED_MODIFIER : un meme modificateur deux fois.
 *   5. ILLEGAL_COMBINATION : plus d'un modificateur d'acces (public, protected, private).
 *   6. VARARGS_NOT_LAST : un parametre avec "..." qui n'est pas le dernier.
 *   7. DUPLICATE_PARAMETER : deux parametres avec le meme nom (2e mot de chaque parametre).
 *   8. Sinon "OK".
 *   Modificateurs connus : public, protected, private, static, final.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isModifier(word) et isAccess(word) (une ligne chacune), et les
 * parametres decoupes (proche du TODO 1).
 *
 *
 * ==================================================================
 * TODO 3 : sameSignature(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La SIGNATURE d'une methode, c'est son NOM et les TYPES de ses
 * parametres, dans l'ordre. Ni le type de retour, ni les noms des
 * parametres, ni les modificateurs n'en font partie. Deux methodes de
 * meme signature dans une classe : javac refuse.
 *
 * -- Essayons a la main --
 *
 *   "int m(int x)" / "long m(int y)"                      -> true  (le retour ne compte pas)
 *   "int add(int a, int b)" / "double add(double a, double b)" -> false (vraie surcharge)
 *   "void m(int[] a)" / "void m(int... a)"                -> true  (int... est un int[])
 *   "void m(int a, String b)" / "void m(String b, int a)" -> false (l'ordre compte)
 *
 * -- Le plan --
 *
 *   1. Meme nom (le mot juste avant '(') ET Arrays.equals(paramTypes(a), paramTypes(b)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : paramTypes (TODO 1) et nameOf(header).
 *
 *
 * ==================================================================
 * TODO 4 : isValidName(name)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un nom de methode commence par une lettre, '_' ou '$', puis lettres,
 * chiffres, '_' ou '$'. "_" tout seul est un mot reserve depuis Java 9,
 * comme "class" ou "void".
 *
 * -- Essayons a la main --
 *
 *   "M", "_m", "$m", "total2" -> true ;  "2m", "_", "class", "", "a-b" -> false
 *
 * -- Le plan --
 *
 *   1. Vide, "_" ou mot reserve (liste KEYWORDS deja fournie) -> false.
 *   2. Character.isJavaIdentifierStart(1er caractere) et isJavaIdentifierPart(les autres).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - header.substring(0, header.indexOf('(')).strip().split("\\s+")
 *   - Pour REPEATED_MODIFIER : comparer chaque mot aux mots suivants (deux boucles).
 */
public class Exercise01_MethodDeclarationRules {

    public static final String[] KEYWORDS = {"class", "void", "int", "static", "public", "private", "protected",
            "final", "return", "new", "if", "for", "while", "switch", "var"};

    public static String[] paramTypes(String header) {
        throw new UnsupportedOperationException("TODO 1 : implementer paramTypes()");
    }

    public static String check(String header) {
        throw new UnsupportedOperationException("TODO 2 : implementer check()");
    }

    public static boolean sameSignature(String a, String b) {
        throw new UnsupportedOperationException("TODO 3 : implementer sameSignature()");
    }

    public static boolean isValidName(String name) {
        throw new UnsupportedOperationException("TODO 4 : implementer isValidName()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("paramTypes : [int, String[]] et []",
                Arrays.equals(paramTypes("void m(int a, String... rest)"), new String[] {"int", "String[]"})
                        && paramTypes("void m()").length == 0);

        String[][] javacVerdicts = {
                {"public static void m()", "OK"},
                {"static public void m()", "OK"},
                {"final static public int m()", "OK"},
                {"private final void m()", "OK"},
                {"void m(String... s)", "OK"},
                {"void m(int a, String... rest)", "OK"},
                {"void m() throws java.io.IOException, java.sql.SQLException", "OK"},
                {"public private void m()", "ILLEGAL_COMBINATION"},
                {"public protected int m()", "ILLEGAL_COMBINATION"},
                {"private protected void m()", "ILLEGAL_COMBINATION"},
                {"static static void m()", "REPEATED_MODIFIER"},
                {"m()", "RETURN_TYPE_REQUIRED"},
                {"public m()", "RETURN_TYPE_REQUIRED"},
                {"static m()", "RETURN_TYPE_REQUIRED"},
                {"protected static final void m()", "OK"},
                {"void public m()", "MODIFIER_AFTER_TYPE"},
                {"public void static m()", "MODIFIER_AFTER_TYPE"},
                {"void m(int... a, String s)", "VARARGS_NOT_LAST"},
                {"void m(int... a, int... b)", "VARARGS_NOT_LAST"},
                {"void m(int a, int a)", "DUPLICATE_PARAMETER"}};
        int agree = 0;
        for (String[] v : javacVerdicts) {
            if (check(v[0]).equals(v[1])) {
                agree++;
            } else {
                System.out.println("   desaccord : " + v[0] + " -> attendu " + v[1] + ", obtenu " + check(v[0]));
            }
        }
        ExerciseChecker.check("check() == verdict de javac sur " + javacVerdicts.length + " en-tetes (" + agree + " d'accord)",
                agree == javacVerdicts.length);

        ExerciseChecker.check("sameSignature : 4 cas",
                sameSignature("int m(int x)", "long m(int y)") && !sameSignature("int add(int a, int b)", "double add(double a, double b)")
                        && sameSignature("void m(int[] a)", "void m(int... a)") && !sameSignature("void m(int a, String b)", "void m(String b, int a)"));

        ExerciseChecker.check("isValidName : 4 bons, 5 mauvais",
                isValidName("M") && isValidName("_m") && isValidName("$m") && isValidName("total2")
                        && !isValidName("2m") && !isValidName("_") && !isValidName("class") && !isValidName("") && !isValidName("a-b"));

        ExerciseChecker.summary();
    }
}
