package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

/**
 * EXERCICE 4 - Qui gagne entre try, catch et finally ? Ta regle comparee a 16 executions reelles (niveau : difficile)
 * ===================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Voici la methode que la JVM execute vraiment (le "juge", en bas) :
 *
 *   try {
 *       trace "T" ; si tryThrows : lancer (catchMatches ? IllegalStateException : IllegalArgumentException)
 *       return "try";
 *   } catch (IllegalStateException e) {
 *       trace "C" ; si catchThrows : lancer ArithmeticException
 *       return "catch";
 *   } finally {
 *       trace "F" ; si finallyReturns : return "finally";
 *   }
 *
 * Trois regles de l'examen :
 *   - finally s'execute TOUJOURS (apres un return, apres une exception
 *     non attrapee...). Seul System.exit() l'en empeche.
 *   - Un return dans finally ECRASE tout : le return du try ou du catch,
 *     ET une exception en train de remonter (elle est perdue !).
 *   - Le return du try "prend une photo" de la valeur AVANT le finally :
 *     changer une variable int dans finally ne change pas ce qui est
 *     rendu ; mais modifier l'OBJET d'un StringBuilder rendu se voit.
 *
 *
 * ==================================================================
 * TODO 1 : predict(tryThrows, catchMatches, catchThrows, finallyReturns)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Rendre "trace -> issue", ou issue est "return X" ou "throws NomSimple".
 *
 * -- Essayons a la main --
 *
 *   (false, _, _, false) -> "TF -> return try"
 *   (true, true, false, false) -> "TCF -> return catch"
 *   (true, false, _, false) -> "TF -> throws IllegalArgumentException" (aucun catch ne correspond)
 *   (true, true, true, true) -> "TCF -> return finally" (l'ArithmeticException est avalee !)
 *
 * -- Le plan --
 *
 *   1. La trace : "T", puis "C" seulement si on entre dans le catch, puis "F".
 *   2. L'issue, dans cet ordre : finally qui rend -> "return finally" ;
 *      pas d'exception -> "return try" ; pas de catch -> "throws IllegalArgumentException" ;
 *      catch qui lance -> "throws ArithmeticException" ; sinon "return catch".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : returnedAfterFinally(kind)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 *   "int"            int x = 1;  try { return x; } finally { x = 2; }
 *   "StringBuilder"  sb = "a";   try { return sb; } finally { sb.append("b"); }
 *   "String"         s = "a";    try { return s; } finally { s = s + "b"; }
 *
 * Rendre, sous forme de texte, ce que la methode rend.
 *
 * -- Le plan --
 *
 *   1. Le return copie la VALEUR de la variable (pour un objet : la reference).
 *   2. Seul un objet modifie SUR PLACE (StringBuilder) montre le changement.
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
 *   - StringBuilder trace = new StringBuilder("T"); if (...) trace.append("C"); trace.append("F");
 *   - boolean enteredCatch = tryThrows && catchMatches;
 */
public class Exercise04_FinallyFlowRules {

    public static String predict(boolean tryThrows, boolean catchMatches, boolean catchThrows, boolean finallyReturns) {
        throw new UnsupportedOperationException("TODO 1 : implementer predict()");
    }

    public static String returnedAfterFinally(String kind) {
        throw new UnsupportedOperationException("TODO 2 : implementer returnedAfterFinally()");
    }

    public static void main(String[] args) {
        int agree = 0;
        for (int bits = 0; bits < 16; bits++) {
            boolean tryThrows = (bits & 1) != 0;
            boolean catchMatches = (bits & 2) != 0;
            boolean catchThrows = (bits & 4) != 0;
            boolean finallyReturns = (bits & 8) != 0;
            if (predict(tryThrows, catchMatches, catchThrows, finallyReturns)
                    .equals(jvm(tryThrows, catchMatches, catchThrows, finallyReturns))) {
                agree++;
            }
        }
        ExerciseChecker.check("predict == JVM sur les 16 combinaisons (" + agree + " d'accord)", agree == 16);

        ExerciseChecker.check("returnedAfterFinally : int -> 1, StringBuilder -> ab, String -> a",
                returnedAfterFinally("int").equals(String.valueOf(intCase()))
                        && returnedAfterFinally("StringBuilder").equals(builderCase().toString())
                        && returnedAfterFinally("String").equals(stringCase()));

        ExerciseChecker.summary();
    }

    // ---- Le juge : la JVM execute vraiment (ne pas modifier) ----

    static String jvm(boolean tryThrows, boolean catchMatches, boolean catchThrows, boolean finallyReturns) {
        StringBuilder trace = new StringBuilder();
        try {
            String result = flow(trace, tryThrows, catchMatches, catchThrows, finallyReturns);
            return trace + " -> return " + result;
        } catch (RuntimeException e) {
            return trace + " -> throws " + e.getClass().getSimpleName();
        }
    }

    @SuppressWarnings("finally")
    static String flow(StringBuilder trace, boolean tryThrows, boolean catchMatches, boolean catchThrows, boolean finallyReturns) {
        try {
            trace.append('T');
            if (tryThrows) {
                throw catchMatches ? new IllegalStateException() : new IllegalArgumentException();
            }
            return "try";
        } catch (IllegalStateException e) {
            trace.append('C');
            if (catchThrows) {
                throw new ArithmeticException();
            }
            return "catch";
        } finally {
            trace.append('F');
            if (finallyReturns) {
                return "finally";
            }
        }
    }

    static int intCase() {
        int x = 1;
        try {
            return x;
        } finally {
            x = 2;
        }
    }

    static StringBuilder builderCase() {
        StringBuilder sb = new StringBuilder("a");
        try {
            return sb;
        } finally {
            sb.append("b");
        }
    }

    static String stringCase() {
        String s = "a";
        try {
            return s;
        } finally {
            s = s + "b";
        }
    }
}
