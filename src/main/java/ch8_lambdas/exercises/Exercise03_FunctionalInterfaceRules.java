package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 3 - Qu'est-ce qu'une interface fonctionnelle ? La regle ecrite par toi, comparee a 15 verdicts de javac (niveau : difficile)
 * =====================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une interface fonctionnelle a EXACTEMENT UNE methode abstraite (on
 * dit SAM : Single Abstract Method). Pour compter :
 *   - on compte ses methodes abstraites ET celles heritees ;
 *   - deux methodes heritees de MEME signature ne comptent qu'une fois ;
 *   - un default qui "remplit" une methode abstraite heritee l'enleve du compte ;
 *   - les methodes PUBLIQUES d'Object redeclarees (equals(Object), hashCode(),
 *     toString()) ne comptent PAS : toute classe les a deja. clone() n'est
 *     pas public dans Object : elle COMPTE. equals(V) (autre parametre) COMPTE.
 * default, static et private ne comptent jamais.
 *
 * -- Verdicts reels de javac 17 --
 *
 *   @FunctionalInterface avec 2 abstraites, ou 0       -> error: Unexpected @FunctionalInterface annotation
 *   test(T) + equals(Object)                         -> compile
 *   run() + toString() + hashCode()                  -> compile
 *   run() + clone()                                  -> error: Unexpected @FunctionalInterface annotation
 *   run() + equals(V other)                          -> error: Unexpected @FunctionalInterface annotation
 *   P extends T<Double> sans rien ajouter            -> compile ; ... + une abstraite a lui -> error
 *   AB extends A, B (les deux ont void run())        -> compile (une seule signature)
 *   B extends A (a(), b()) avec default b()          -> compile (il reste a())
 *   f() + default + static + private                 -> compile
 *   @FunctionalInterface sur une classe (meme abstraite) -> error: Unexpected @FunctionalInterface annotation
 *   Two t = () -> {} (Two a 2 abstraites)            -> error: incompatible types: Two is not a functional interface
 *   K k = () -> {} (K classe abstraite)              -> error: incompatible types: K is not a functional interface
 *
 *
 * ==================================================================
 * TODO 1 : abstractCount(own, inherited, filledByDefault)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * own : les methodes abstraites ecrites dans l'interface ; inherited :
 * celles recues des parents ; filledByDefault : celles qu'un default de
 * l'interface remplit. Chaque methode est une signature texte, par
 * exemple "run()", "equals(Object)", "apply(Double)".
 *
 * -- Essayons a la main --
 *
 *   (["test(T)", "equals(Object)"], [], [])     -> 1
 *   ([], ["run()", "run()"], [])                -> 1 (meme signature)
 *   ([], ["a()", "b()"], ["b()"])               -> 1
 *   (["run()", "clone()"], [], [])              -> 2
 *
 * -- Le plan --
 *
 *   1. Mettre own et inherited dans un Set (les doublons disparaissent).
 *   2. Enlever filledByDefault et les 3 methodes publiques d'Object : "equals(Object)", "hashCode()", "toString()".
 *   3. Rendre la taille.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : annotationError(kind, abstractCount)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. kind different de "interface" -> "Unexpected @FunctionalInterface annotation".
 *   2. abstractCount != 1 -> meme message.
 *   3. "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : lambdaTargetError(typeName, kind, abstractCount)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. kind "interface" et abstractCount == 1 -> "OK".
 *   2. Sinon "incompatible types: " + typeName + " is not a functional interface".
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
 *   - new HashSet<>(own) puis addAll(inherited), removeAll(filledByDefault), removeAll(List.of(...)).
 */
public class Exercise03_FunctionalInterfaceRules {

    public static int abstractCount(List<String> own, List<String> inherited, List<String> filledByDefault) {
        throw new UnsupportedOperationException("TODO 1 : implementer abstractCount()");
    }

    public static String annotationError(String kind, int abstractCount) {
        throw new UnsupportedOperationException("TODO 2 : implementer annotationError()");
    }

    public static String lambdaTargetError(String typeName, String kind, int abstractCount) {
        throw new UnsupportedOperationException("TODO 3 : implementer lambdaTargetError()");
    }

    public static void main(String[] args) {
        String unexpected = "Unexpected @FunctionalInterface annotation";
        // Verdicts REELS de javac 17 : {propres, heritees, remplies par default, verdict de @FunctionalInterface}.
        Object[][] javac = {
                {List.of("a(String)", "b(String)"), List.of(), List.of(), unexpected},
                {List.of(), List.of(), List.of(), unexpected},
                {List.of("test(T)", "equals(Object)"), List.of(), List.of(), "OK"},
                {List.of("run()", "toString()", "hashCode()"), List.of(), List.of(), "OK"},
                {List.of("run()", "clone()"), List.of(), List.of(), unexpected},
                {List.of("run()", "equals(V)"), List.of(), List.of(), unexpected},
                {List.of(), List.of("apply(Double)"), List.of(), "OK"},
                {List.of("extra()"), List.of("apply(Double)"), List.of(), unexpected},
                {List.of(), List.of("run()", "run()"), List.of(), "OK"},
                {List.of(), List.of("a()", "b()"), List.of("b()"), "OK"},
                {List.of("f()"), List.of(), List.of(), "OK"}};
        int agree = 0;
        for (Object[] v : javac) {
            @SuppressWarnings("unchecked")
            int count = abstractCount((List<String>) v[0], (List<String>) v[1], (List<String>) v[2]);
            if (annotationError("interface", count).equals(v[3])) {
                agree++;
            }
        }
        ExerciseChecker.check("abstractCount + annotationError == javac sur 11 interfaces (" + agree + " d'accord)", agree == 11);
        ExerciseChecker.check("annotationError sur une classe (meme abstraite) == javac",
                annotationError("class", 1).equals(unexpected) && annotationError("abstract class", 1).equals(unexpected));
        ExerciseChecker.check("lambdaTargetError == javac sur 2 cas (+ 1 correct)",
                lambdaTargetError("Two", "interface", 2).equals("incompatible types: Two is not a functional interface")
                        && lambdaTargetError("K", "abstract class", 1).equals("incompatible types: K is not a functional interface")
                        && lambdaTargetError("Runnable", "interface", 1).equals("OK"));

        ExerciseChecker.summary();
    }
}
