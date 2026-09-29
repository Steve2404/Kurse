package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 19 - Les regles de Math ecrites par toi : round, floor, ceil, max et leurs types, addExact (niveau : difficile)
 * ========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise11_ArraysBasics.java.
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   Math.round(2.5) -> 3     Math.round(-2.5) -> -2 (!)    Math.round(-2.6) -> -3    Math.round(-2.4) -> -2
 *   Math.round(2.5f) est un int ; Math.round(2.5) est un long
 *   Math.ceil(-1.5) -> -1.0  Math.floor(-1.5) -> -2.0   (toujours des double)
 *   (int) -3.99 -> -3        un cast COUPE vers zero ; floor descend TOUJOURS
 *   Math.max(3, 2.5) -> 3.0  (le int est promu en double)
 *   Math.abs(Integer.MIN_VALUE) -> -2147483648 (debordement silencieux !)
 *   Math.addExact(Integer.MAX_VALUE, 1) -> ArithmeticException: integer overflow
 *   Math.pow(2, 10) -> 1024.0 (double)   Math.sqrt(-1) -> NaN
 *
 * Ici, c'est TOI qui ecris la regle ; main() la compare au VRAI Math.
 *
 *
 * ==================================================================
 * TODO 1 : myRound(x)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Math.round n'arrondit pas "au plus proche en s'eloignant de zero".
 * Sa vraie regle : ajouter 0.5, puis descendre a l'entier en dessous
 * (floor). Pour -2.5 : -2.5 + 0.5 = -2.0 -> floor -> -2. Surprenant !
 *
 * -- Essayons a la main --
 *
 *   2.5 -> floor(3.0) = 3     -2.6 -> floor(-2.1) = -3     2.4999 -> floor(2.9999) = 2
 *
 * -- Le plan --
 *
 *   1. Rendre (long) Math.floor(x + 0.5).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : myFloor(x)    et    TODO 3 : myCeil(x)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * SANS Math.floor ni Math.ceil. Un cast (long) coupe vers zero : pour un
 * positif c'est deja floor, mais pour un negatif non entier, il faut
 * descendre d'un cran de plus. ceil, c'est l'inverse.
 *
 * -- Essayons a la main --
 *
 *   myFloor(-1.5) : cast -> -1 ; -1.5 < -1 -> -2.0      myFloor(1.8) -> 1.0     myFloor(-3.0) -> -3.0
 *   myCeil(-1.5)  : cast -> -1 ; -1.5 > -1 ? non -> -1.0 myCeil(1.2)  : cast 1 ; 1.2 > 1 -> 2.0
 *
 * -- Le plan --
 *
 *   1. t = (long) x.
 *   2. floor : si x < t, rendre t - 1, sinon t.   ceil : si x > t, rendre t + 1, sinon t.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : maxType(t1, t2)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Math.max existe en 4 versions : (int, int), (long, long),
 * (float, float), (double, double). Java choisit la plus petite qui
 * accepte les DEUX arguments, en promouvant le plus petit type. Le
 * resultat a ce type. Les types sont donnes en texte : "int", "long",
 * "float", "double". main() compare avec les VRAIS appels de Math.max.
 *
 * -- Essayons a la main --
 *
 *   ("int", "int") -> "int"     ("int", "long") -> "long"     ("long", "float") -> "float"
 *   ("float", "double") -> "double"
 *
 * -- Le plan --
 *
 *   1. Donner un rang a chaque type : int 0, long 1, float 2, double 3 (petite boite rank).
 *   2. Rendre le type du rang le plus grand.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : rank(type), avec un switch expression.
 *
 *
 * ==================================================================
 * TODO 5 : safeAdd(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * a + b deborde en silence (2147483647 + 1 = -2147483648). addExact,
 * lui, crie : il lance ArithmeticException. On rend le resultat en
 * texte, ou "debordement".
 *
 * -- Le plan --
 *
 *   1. try { return String.valueOf(Math.addExact(a, b)); }
 *      catch (ArithmeticException e) { return "debordement"; }
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : roundTo(x, decimals)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Arrondir a 2 decimales : on multiplie par 100, on arrondit, on
 * redivise par 100.0. Piege des double : 1.005 * 100 vaut
 * 100.49999999999999 (verifie) donc roundTo(1.005, 2) donne 1.0, pas 1.01 !
 *
 * -- Essayons a la main --
 *
 *   (3.14159, 2) -> 314.159 -> 314 -> 3.14     (2.5, 0) -> 3.0     (1.005, 2) -> 1.0
 *
 * -- Le plan --
 *
 *   1. factor = Math.pow(10, decimals).
 *   2. Rendre Math.round(x * factor) / factor.
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
 *   - Math.round(...) / factor : long / double -> double (pas de division entiere).
 *   - Math.round(x * factor) / 100 (int) serait une division ENTIERE : piege.
 */
public class Exercise19_MathPrecisionRules {

    public static long myRound(double x) {
        throw new UnsupportedOperationException("TODO 1 : implementer myRound()");
    }

    public static double myFloor(double x) {
        throw new UnsupportedOperationException("TODO 2 : implementer myFloor()");
    }

    public static double myCeil(double x) {
        throw new UnsupportedOperationException("TODO 3 : implementer myCeil()");
    }

    public static String maxType(String t1, String t2) {
        throw new UnsupportedOperationException("TODO 4 : implementer maxType()");
    }

    public static String safeAdd(int a, int b) {
        throw new UnsupportedOperationException("TODO 5 : implementer safeAdd()");
    }

    public static double roundTo(double x, int decimals) {
        throw new UnsupportedOperationException("TODO 6 : implementer roundTo()");
    }

    public static void main(String[] args) {
        double[] values = {2.5, -2.5, -2.6, -2.4, 2.4999, 0.5, -0.5, 7.0, -7.5, 123.456, -0.2};
        boolean roundOk = true;
        boolean floorOk = true;
        boolean ceilOk = true;
        for (double v : values) {
            roundOk &= myRound(v) == Math.round(v);
            floorOk &= myFloor(v) == Math.floor(v);
            ceilOk &= myCeil(v) == Math.ceil(v);
        }
        ExerciseChecker.check("myRound == Math.round sur 11 valeurs (dont -2.5 -> -2)", roundOk);
        ExerciseChecker.check("myFloor == Math.floor sur 11 valeurs", floorOk);
        ExerciseChecker.check("myCeil == Math.ceil sur 11 valeurs", ceilOk);

        Object[][] realCalls = {
                {"int", "int", Math.max(1, 2)}, {"int", "long", Math.max(1, 2L)}, {"long", "int", Math.max(1L, 2)},
                {"int", "float", Math.max(1, 2f)}, {"long", "float", Math.max(1L, 2f)}, {"float", "double", Math.max(1f, 2.0)},
                {"int", "double", Math.max(1, 2.0)}, {"long", "long", Math.max(1L, 2L)}};
        boolean typesOk = true;
        for (Object[] call : realCalls) {
            typesOk &= maxType((String) call[0], (String) call[1]).equals(primitiveName(call[2]));
        }
        ExerciseChecker.check("maxType == le type REEL de Math.max sur 8 combinaisons", typesOk);

        ExerciseChecker.check("safeAdd : 5 et \"debordement\"",
                safeAdd(2, 3).equals("5") && safeAdd(Integer.MAX_VALUE, 1).equals("debordement"));
        ExerciseChecker.check("roundTo : 3.14, 3.0 et le piege 1.005 -> 1.0",
                roundTo(3.14159, 2) == 3.14 && roundTo(2.5, 0) == 3.0 && roundTo(1.005, 2) == 1.0);

        ExerciseChecker.summary();
    }

    // Deja ecrit : Integer -> "int", Long -> "long"... (le type reel d'un resultat mis en boite).
    private static String primitiveName(Object boxed) {
        String name = boxed.getClass().getSimpleName();
        return name.equals("Integer") ? "int" : name.toLowerCase();
    }
}
