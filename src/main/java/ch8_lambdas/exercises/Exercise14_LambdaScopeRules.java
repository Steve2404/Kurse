package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.util.List;
import java.util.function.Supplier;

/**
 * EXERCICE 14 - Les variables dans une lambda : noms, captures, this (regles comparees a 11 verdicts de javac) (niveau : difficile)
 * ===============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une lambda n'est PAS une nouvelle portee de noms : ses parametres et
 * ses variables vivent dans la meme piece que les variables locales de
 * la methode. On ne peut donc pas reutiliser un nom local DEJA declare.
 * Par contre, un CHAMP peut etre cache sans probleme, et un champ peut
 * etre modifie (seules les variables LOCALES doivent etre effectivement
 * final). Enfin, "this" dans une lambda designe l'objet englobant (pas
 * la lambda), contrairement a une classe anonyme.
 *
 * -- Verdicts reels de javac 17 --
 *
 *   int x = 1; Predicate<Integer> p = x -> x > 0;      -> error: variable x is already defined in method m()
 *   int x = 1; Runnable r = () -> { int x = 2; };      -> error: variable x is already defined in method m()
 *   void m(String s) { ... s -> s.isEmpty() ... }      -> error: variable s is already defined in method m(String)
 *   champ x ; lambda x -> x > 0                        -> compile (le parametre cache le champ)
 *   lambda x -> ... puis PLUS BAS int x = 1;           -> compile (le nom n'existait pas encore)
 *   champ count ; () -> count++                        -> compile (champ d'instance ou static)
 *   local count ; () -> count++                        -> error: local variables referenced from a lambda expression must be final or effectively final
 *   local base ; n -> n + base ; puis base = 2         -> meme erreur
 *   n -> { n = n + 1; return n; }                      -> compile (le parametre est a la lambda)
 *   (final int n) -> { n = 1; ... }                    -> error: final parameter n may not be assigned
 *   champ static : () -> this.name dans un contexte static -> error: non-static variable this cannot be referenced from a static context
 *   int[] c = {0}; () -> c[0]++                        -> compile (on modifie le CONTENU, pas la variable)
 *
 *
 * ==================================================================
 * TODO 1 : nameError(lambdaName, localsDeclaredBefore, methodParams, methodSignature)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. lambdaName dans localsDeclaredBefore ou methodParams ->
 *      "variable " + lambdaName + " is already defined in method " + methodSignature.
 *   2. Sinon "OK" (un champ du meme nom, ou une locale declaree APRES, ne genent pas).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : modifyError(target)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * target : ce que la lambda modifie (ou capture puis on modifie) :
 * "field", "static field", "array element", "own parameter",
 * "final own parameter", "local".
 *
 * -- Le plan --
 *
 *   1. "local" -> "local variables referenced from a lambda expression must be final or effectively final".
 *   2. "final own parameter" -> "final parameter n may not be assigned".
 *   3. Sinon "OK".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : lambdaThis()    et    TODO 4 : anonymousThis()    [methodes d'instance]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les deux rendent un Supplier<Object> qui rend "this". Dans la lambda,
 * this est l'objet Exercise14 lui-meme ; dans la classe anonyme, this
 * est l'objet anonyme.
 *
 * -- Le plan --
 *
 *   1. lambdaThis : rendre () -> this.
 *   2. anonymousThis : rendre new Supplier<Object>() { public Object get() { return this; } }.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : clicks()    [modifier un CHAMP depuis une lambda]
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Runnable click = () -> clickCount++ ; lancer 3 fois ; rendre clickCount.
 *      (clickCount est un champ : pas besoin d'etre effectivement final.)
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
 *   - list.contains(name).
 */
public class Exercise14_LambdaScopeRules {

    private int clickCount;

    public static String nameError(String lambdaName, List<String> localsDeclaredBefore, List<String> methodParams,
                                   String methodSignature) {
        throw new UnsupportedOperationException("TODO 1 : implementer nameError()");
    }

    public static String modifyError(String target) {
        throw new UnsupportedOperationException("TODO 2 : implementer modifyError()");
    }

    public Supplier<Object> lambdaThis() {
        throw new UnsupportedOperationException("TODO 3 : implementer lambdaThis()");
    }

    public Supplier<Object> anonymousThis() {
        throw new UnsupportedOperationException("TODO 4 : implementer anonymousThis()");
    }

    public int clicks() {
        throw new UnsupportedOperationException("TODO 5 : implementer clicks()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("nameError() == javac sur 5 cas",
                nameError("x", List.of("x"), List.of(), "m()").equals("variable x is already defined in method m()")
                        && nameError("s", List.of(), List.of("s"), "m(String)").equals("variable s is already defined in method m(String)")
                        && nameError("x", List.of(), List.of(), "m()").equals("OK")
                        && nameError("x", List.of("y"), List.of(), "m()").equals("OK")
                        && nameError("n", List.of(), List.of("s"), "m(String)").equals("OK"));
        String notFinal = "local variables referenced from a lambda expression must be final or effectively final";
        ExerciseChecker.check("modifyError() == javac sur 6 cas",
                modifyError("local").equals(notFinal) && modifyError("final own parameter").equals("final parameter n may not be assigned")
                        && modifyError("field").equals("OK") && modifyError("static field").equals("OK")
                        && modifyError("array element").equals("OK") && modifyError("own parameter").equals("OK"));
        Exercise14_LambdaScopeRules outer = new Exercise14_LambdaScopeRules();
        ExerciseChecker.check("lambdaThis : this == l'objet englobant", outer.lambdaThis().get() == outer);
        Object anonymous = outer.anonymousThis().get();
        ExerciseChecker.check("anonymousThis : this == l'objet anonyme (pas l'englobant)",
                anonymous != outer && anonymous instanceof Supplier);
        ExerciseChecker.check("clicks() == 3 (un champ modifie dans une lambda)", outer.clicks() == 3);

        ExerciseChecker.summary();
    }
}
