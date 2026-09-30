package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.Map;

/**
 * EXERCICE 10 - Les regles de redefinition et de "cachage" ecrites par toi, comparees a 26 verdicts reels de javac (niveau : difficile)
 * ====================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand un enfant ecrit une methode avec la MEME signature que son
 * parent, il PROMET de pouvoir remplacer le parent partout. Donc il ne
 * doit jamais promettre MOINS :
 *
 *   - acces : pareil ou PLUS ouvert (private < package < protected < public) ;
 *   - retour : le meme primitif, ou pour un objet le meme type ou un SOUS-type (covariance) ;
 *   - exceptions checked : aucune nouvelle, seulement les memes ou des sous-classes ;
 *   - final : on ne redefinit jamais une methode final ;
 *   - static : les deux static (c'est alors du "cachage") ou les deux d'instance ;
 *   - private : la methode du parent n'est pas heritee -> aucune regle (simple nouvelle methode).
 *
 * -- Verdicts reels de javac 17 (P parent, K enfant) --
 *
 *   Toutes les erreurs commencent par : error: m() in K cannot override m() in P
 *   suivie de la RAISON, que tu dois rendre :
 *     public -> protected           : attempting to assign weaker access privileges; was public
 *     package -> private            : attempting to assign weaker access privileges; was package
 *     throws IOException -> Exception : overridden method does not throw Exception
 *     rien -> throws IOException    : overridden method does not throw IOException
 *     D m() -> A m() (A parent de D) : return type A is not compatible with D
 *     int m() -> long m()           : return type long is not compatible with int
 *     final void m()                : overridden method is final
 *     static -> instance            : overridden method is static
 *     instance -> static            : overriding method is static
 *     static final -> static        : overridden method is static,final
 *   Compilent : acces elargi, FileNotFoundException a la place de IOException, RuntimeException ou Error
 *   ajoutee, exception supprimee, retour covariant (Number -> Integer, Object -> String), static -> static,
 *   parent private (meme avec un autre retour, static ou final).
 *   Autres messages : class K extends P (P final) -> error: cannot inherit from final P ;
 *   @Override sur une surcharge -> error: method does not override or implement a method from a supertype ;
 *   @Override sur une methode static -> error: static methods cannot be annotated with @Override.
 *
 *
 * ==================================================================
 * TODO 1 : reason(parent, child)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque methode est decrite par un record Method(access, isStatic,
 * isFinal, returnType, throwsType) ; throwsType vaut null si rien n'est
 * declare. On rend "OK" ou la raison exacte de javac.
 *
 * -- Le plan (dans cet ordre) --
 *
 *   1. parent.access est "private" -> "OK".
 *   2. parent final : les deux static -> "overridden method is static,final" ; sinon "overridden method is final".
 *   3. parent static et enfant non -> "overridden method is static" ; l'inverse -> "overriding method is static".
 *   4. rank(child.access) < rank(parent.access) -> "attempting to assign weaker access privileges; was " + parent.access.
 *   5. Retour : si les deux sont identiques, OK ; sinon, si l'un est primitif (ou void), ou si
 *      child.returnType n'est pas un sous-type de parent.returnType ->
 *      "return type " + child.returnType + " is not compatible with " + parent.returnType.
 *   6. Exception : si child.throwsType est checked et que parent.throwsType n'est pas lui-meme ou
 *      un de ses super-types -> "overridden method does not throw " + child.throwsType.
 *   7. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : rank(access), isSubtypeOf(type, parentType) (en remontant SUPER_TYPE) et isChecked(exception)
 * (checked sauf RuntimeException, Error et leurs sous-classes).
 *
 *
 * ==================================================================
 * TODO 2 : relation(parent, child)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand c'est valide, comment s'appelle ce qu'on a ecrit ?
 *   parent private                 -> "redeclaration" (une nouvelle methode)
 *   les deux static                -> "hiding" (cachage : choisi par le type de la VARIABLE)
 *   les deux d'instance            -> "overriding" (redefinition : choisi par l'OBJET reel)
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
 *   - SUPER_TYPE.get("Dog") -> "Animal" ; SUPER_TYPE.get("Object") -> null.
 *   - Un type est primitif s'il commence par une minuscule (int, long, void...).
 */
public class Exercise10_OverrideRules {

    public record Method(String access, boolean isStatic, boolean isFinal, String returnType, String throwsType) {
    }

    public static final Map<String, String> SUPER_TYPE = Map.ofEntries(
            Map.entry("String", "Object"), Map.entry("Number", "Object"), Map.entry("Integer", "Number"),
            Map.entry("Animal", "Object"), Map.entry("Dog", "Animal"), Map.entry("A", "Object"), Map.entry("D", "A"),
            Map.entry("Throwable", "Object"), Map.entry("Exception", "Throwable"), Map.entry("Error", "Throwable"),
            Map.entry("IOException", "Exception"), Map.entry("FileNotFoundException", "IOException"),
            Map.entry("RuntimeException", "Exception"), Map.entry("IllegalArgumentException", "RuntimeException"));

    public static final String[] ACCESS = {"private", "package", "protected", "public"};

    public static String reason(Method parent, Method child) {
        throw new UnsupportedOperationException("TODO 1 : implementer reason()");
    }

    public static String relation(Method parent, Method child) {
        throw new UnsupportedOperationException("TODO 2 : implementer relation()");
    }

    public static void main(String[] args) {
        // Verdicts REELS de javac 17 : {parent, enfant, raison ("OK" si ca compile)}.
        Object[][] javac = {
                {m("public", "void", null), m("protected", "void", null), "attempting to assign weaker access privileges; was public"},
                {m("package", "void", null), m("private", "void", null), "attempting to assign weaker access privileges; was package"},
                {m("protected", "void", null), m("package", "void", null), "attempting to assign weaker access privileges; was protected"},
                {m("protected", "void", null), m("public", "void", null), "OK"},
                {m("package", "void", "IOException"), m("package", "void", "Exception"), "overridden method does not throw Exception"},
                {m("package", "void", "IOException"), m("package", "void", "FileNotFoundException"), "OK"},
                {m("package", "void", null), m("package", "void", "IOException"), "overridden method does not throw IOException"},
                {m("package", "void", null), m("package", "void", "RuntimeException"), "OK"},
                {m("package", "void", null), m("package", "void", "Error"), "OK"},
                {m("package", "void", "IOException"), m("package", "void", null), "OK"},
                {m("package", "void", "Exception"), m("package", "void", "IOException"), "OK"},
                {m("package", "A", null), m("package", "D", null), "OK"},
                {m("package", "D", null), m("package", "A", null), "return type A is not compatible with D"},
                {m("package", "String", null), m("package", "int", null), "return type int is not compatible with String"},
                {m("package", "int", null), m("package", "long", null), "return type long is not compatible with int"},
                {m("package", "void", null), m("package", "int", null), "return type int is not compatible with void"},
                {m("package", "Number", null), m("package", "Integer", null), "OK"},
                {m("package", "Object", null), m("package", "String", null), "OK"},
                {m("package", "String", null), m("package", "Object", null), "return type Object is not compatible with String"},
                {new Method("package", false, true, "void", null), m("package", "void", null), "overridden method is final"},
                {new Method("package", true, false, "void", null), m("package", "void", null), "overridden method is static"},
                {m("package", "void", null), new Method("package", true, false, "void", null), "overriding method is static"},
                {new Method("package", true, false, "void", null), new Method("package", true, false, "void", null), "OK"},
                {new Method("package", true, true, "void", null), new Method("package", true, false, "void", null), "overridden method is static,final"},
                {new Method("public", true, false, "void", null), new Method("package", true, false, "void", null), "attempting to assign weaker access privileges; was public"},
                {m("private", "void", null), m("private", "int", null), "OK"}};
        int agree = 0;
        for (Object[] v : javac) {
            String mine = reason((Method) v[0], (Method) v[1]);
            if (mine.equals(v[2])) {
                agree++;
            } else {
                System.out.println("   desaccord : attendu \"" + v[2] + "\", obtenu \"" + mine + "\"");
            }
        }
        ExerciseChecker.check("reason() == javac sur " + javac.length + " cas (" + agree + " d'accord)", agree == javac.length);

        ExerciseChecker.check("relation : redeclaration, hiding, overriding",
                relation(m("private", "void", null), new Method("public", true, false, "String", null)).equals("redeclaration")
                        && relation(new Method("package", true, false, "void", null), new Method("package", true, false, "void", null)).equals("hiding")
                        && relation(m("package", "void", null), m("public", "void", null)).equals("overriding"));

        ExerciseChecker.summary();
    }

    // Deja ecrit : raccourci pour une methode d'instance, non final.
    static Method m(String access, String returnType, String throwsType) {
        return new Method(access, false, false, returnType, throwsType);
    }
}
