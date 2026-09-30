package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 9 - try-with-resources : ce que javac accepte, ta regle comparee a 18 verdicts (niveau : difficile)
 * ============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * try (R r = new R()) { ... } ferme r tout seul a la fin, comme si on
 * avait ecrit un finally { r.close(); } invisible. D'ou les regles :
 *
 *   - R doit etre AutoCloseable (Closeable en est un sous-type) ;
 *   - la ressource est une VARIABLE : declaree dans les parentheses, ou
 *     deja existante mais final / effectivement finale (Java 9) ;
 *   - elle est implicitement final dans le bloc et n'existe plus apres ;
 *   - le close() invisible peut lancer ce que close() declare : il faut
 *     l'attraper ou le declarer, comme n'importe quel appel.
 *
 * -- Verdicts reels de javac 17 --
 *
 *   try (String s = "x") { }                   -> error: incompatible types: try-with-resources not applicable to variable type
 *   try (new R()) { }                          -> error: the try-with-resources resource must either be a variable declaration
 *                                                 or an expression denoting a reference to a final or effectively final variable
 *   R r = new R(); r = new R(); try (r) { }    -> error: variable r used as a try-with-resources resource neither final nor effectively final
 *   R r = new R(); try (r) { }  /  try (var r = new R()) { }  /  try (champFinal) { }  -> compile
 *   close() throws Exception, rien autour      -> error: unreported exception Exception; must be caught or declared to be thrown
 *                                                 (exception thrown from implicit call to close() on resource variable 'r')
 *   try (R r = new R()) { r = null; }          -> error: auto-closeable resource r may not be assigned
 *   try (R r = new R()) { } finally { r.close(); } -> error: cannot find symbol
 *   try { }                                    -> error: 'try' without 'catch', 'finally' or resource declarations
 *   try (R r = new R()) { }  (seul)            -> compile ; try (R a = ...; R b = ...;) { } -> compile (le ; final est permis)
 *
 *
 * ==================================================================
 * TODO 1 : resourceError(type, form, effectivelyFinal)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * type : "AutoCloseable", "Closeable" ou "String". form : "declaration"
 * (R r = ...), "var" (var r = ...), "existingVariable" (try (r)),
 * "field" (try (this.f), champ final) ou "newExpression" (try (new R())).
 *
 * -- Le plan --
 *
 *   1. form "newExpression" -> le message "must either be a variable declaration...".
 *   2. type "String" -> "incompatible types: try-with-resources not applicable to variable type".
 *   3. form "existingVariable" et pas effectivelyFinal -> "variable r used as ... neither final nor effectively final".
 *   4. Sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : closeError(closeThrows, caught, declared)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * closeThrows : ce que declare close() ("" = rien, "Exception",
 * "IOException"). Un catch ou un throws d'un ANCETRE suffit.
 *
 * -- Essayons a la main --
 *
 *   ("Exception", [], [])            -> unreported exception Exception; must be caught or declared to be thrown
 *   ("IOException", [], [IOException]) -> OK
 *   ("", [], [])                      -> OK
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isA (donnee), et "un ancetre dans la liste ?" deux fois.
 *
 *
 * ==================================================================
 * TODO 3 : bodyError(assignsResource, usesResourceAfter)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. assignsResource -> "auto-closeable resource r may not be assigned".
 *   2. usesResourceAfter (dans catch/finally ou apres) -> "cannot find symbol".
 *   3. Sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : tryShapeError(hasResources, hasCatchOrFinally)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Ni ressource, ni catch, ni finally -> "'try' without 'catch', 'finally' or resource declarations".
 *   2. Sinon "OK".
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
 *   - closeThrows.isEmpty() -> rien a verifier.
 */
public class Exercise09_TryWithResourcesRules {

    // "a est-il b ou un de ses descendants ?" pour le petit arbre IOException -> Exception -> Throwable.
    public static boolean isA(String a, String b) {
        List<String> chain = switch (a) {
            case "IOException" -> List.of("IOException", "Exception", "Throwable");
            case "Exception" -> List.of("Exception", "Throwable");
            default -> List.of(a);
        };
        return chain.contains(b);
    }

    public static String resourceError(String type, String form, boolean effectivelyFinal) {
        throw new UnsupportedOperationException("TODO 1 : implementer resourceError()");
    }

    public static String closeError(String closeThrows, List<String> caught, List<String> declared) {
        throw new UnsupportedOperationException("TODO 2 : implementer closeError()");
    }

    public static String bodyError(boolean assignsResource, boolean usesResourceAfter) {
        throw new UnsupportedOperationException("TODO 3 : implementer bodyError()");
    }

    public static String tryShapeError(boolean hasResources, boolean hasCatchOrFinally) {
        throw new UnsupportedOperationException("TODO 4 : implementer tryShapeError()");
    }

    public static void main(String[] args) {
        String notApplicable = "incompatible types: try-with-resources not applicable to variable type";
        String mustBeVariable = "the try-with-resources resource must either be a variable declaration or an expression "
                + "denoting a reference to a final or effectively final variable";
        String notFinal = "variable r used as a try-with-resources resource neither final nor effectively final";
        // Verdicts REELS de javac 17.
        Object[][] resources = {
                {"String", "declaration", true, notApplicable},
                {"AutoCloseable", "newExpression", true, mustBeVariable},
                {"AutoCloseable", "existingVariable", false, notFinal},
                {"AutoCloseable", "existingVariable", true, "OK"},
                {"AutoCloseable", "field", true, "OK"},
                {"AutoCloseable", "var", true, "OK"},
                {"Closeable", "declaration", true, "OK"}};
        int agree = 0;
        for (Object[] c : resources) {
            if (resourceError((String) c[0], (String) c[1], (Boolean) c[2]).equals(c[3])) {
                agree++;
            }
        }
        ExerciseChecker.check("resourceError == javac sur 7 cas (" + agree + " d'accord)", agree == 7);

        String unreported = "unreported exception %s; must be caught or declared to be thrown";
        Object[][] closes = {
                {"Exception", List.of(), List.of(), String.format(unreported, "Exception")},
                {"", List.of(), List.of(), "OK"},
                {"IOException", List.of(), List.of(), String.format(unreported, "IOException")},
                {"IOException", List.of(), List.of("IOException"), "OK"},
                {"IOException", List.of("Exception"), List.of(), "OK"}};
        agree = 0;
        for (Object[] c : closes) {
            @SuppressWarnings("unchecked")
            String mine = closeError((String) c[0], (List<String>) c[1], (List<String>) c[2]);
            if (mine.equals(c[3])) {
                agree++;
            }
        }
        ExerciseChecker.check("closeError == javac sur 5 cas (" + agree + " d'accord)", agree == 5);

        ExerciseChecker.check("bodyError == javac sur 3 cas",
                bodyError(true, false).equals("auto-closeable resource r may not be assigned")
                        && bodyError(false, true).equals("cannot find symbol") && bodyError(false, false).equals("OK"));
        ExerciseChecker.check("tryShapeError == javac sur 3 cas",
                tryShapeError(false, false).equals("'try' without 'catch', 'finally' or resource declarations")
                        && tryShapeError(true, false).equals("OK") && tryShapeError(false, true).equals("OK"));

        ExerciseChecker.summary();
    }
}
