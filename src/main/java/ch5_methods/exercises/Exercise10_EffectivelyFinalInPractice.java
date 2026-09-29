package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

import java.util.List;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;

/**
 * EXERCICE 10 - "Effectivement final" : la regle ecrite par toi, puis des lambdas qui capturent (niveau : difficile)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MethodDeclarationRules.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une lambda "prend une photo" des variables locales qu'elle utilise.
 * Java n'accepte la photo que si la variable ne change JAMAIS apres
 * avoir recu sa valeur (final, ou "effectivement final" : on pourrait
 * ecrire final devant sans casser le code).
 *
 * -- Verdicts reels de javac 17 (une lambda "() -> x" a la fin de la methode) --
 *
 *   int x = 5;                   -> compile
 *   int x = 5;  x = 10;          -> error: local variables referenced from a lambda expression must be final or effectively final
 *   int x = 5;  x++;             -> meme erreur
 *   int x = 5;  x += 1;          -> meme erreur
 *   int x = 5;  x = 5;           -> meme erreur (meme valeur, mais c'est une reaffectation)
 *   int x;      x = 3;           -> compile (UNE seule affectation, apres la declaration)
 *   int x;      x = 3; x = 4;    -> meme erreur
 *   int x = 5;  int y = x + 1;   -> compile (lire n'est pas modifier)
 *   modifier x APRES la lambda   -> meme erreur (l'ordre ne sauve pas)
 *   parametre reassigne (p = 2)  -> meme erreur ; parametre jamais reassigne -> compile
 *   () -> x++ dans la lambda     -> meme erreur
 *   for (int i = 0; i < 3; i++) { () -> i }  -> meme erreur ; for (int i : tableau) { () -> i } -> compile
 *   final int x = 1; x = 2;      -> error: cannot assign a value to final variable x
 *
 *
 * ==================================================================
 * TODO 1 : isEffectivelyFinal(declaredWithValue, statements...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Tu lis les instructions qui suivent la declaration de x et tu comptes
 * les ECRITURES dans x. Une ecriture : "x = ...", "x++", "x--", "++x",
 * "--x", "x += ...", "x -= ..." (et les autres operateurs composes).
 * Tout le reste (par exemple "int y = x + 1;") n'est qu'une lecture.
 *
 *   declare AVEC valeur ("int x = 5;")  : effectivement final si 0 ecriture.
 *   declare SANS valeur ("int x;")      : effectivement final si EXACTEMENT 1
 *                                         ecriture, et que c'est un simple "x = ...".
 *
 * -- Le plan --
 *
 *   1. Petite boite writeKind(statement) : "simple" si l'instruction commence par "x =" (mais pas "x =="),
 *      "compound" pour x++, x--, ++x, --x ou "x" suivi de "+=", "-=", "*=", "/=" ; sinon "none".
 *   2. Compter les simples et les composees ; appliquer la regle.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : writeKind(statement).
 *
 *
 * ==================================================================
 * TODO 2 : multiplier(factor)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   multiplier(3).applyAsInt(7) -> 21   (factor est un parametre jamais reassigne : capturable)
 *
 * -- Le plan --
 *
 *   1. Rendre x -> x * factor.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : suppliers(n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut n lambdas qui rendent 0, 1, ..., n - 1. Dans un for classique,
 * i change a chaque tour : la lambda ne peut pas le capturer. L'astuce :
 * une NOUVELLE variable par tour, "int copy = i;", jamais modifiee.
 *
 * -- Le plan --
 *
 *   1. result = new ArrayList<>() ; for (int i = 0; i < n; i++) { int copy = i ; result.add(() -> copy) ; }
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : countWithLambda(times)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut compter dans une lambda, mais "count++" est interdit. Astuce :
 * un tableau d'une case. La VARIABLE tableau ne change pas (effectivement
 * final) ; c'est son CONTENU qui change, et ca, c'est permis.
 *
 * -- Le plan --
 *
 *   1. int[] box = {0} ; Runnable r = () -> box[0]++ ; lancer r.run() times fois ; rendre box[0].
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : labeler(prefix)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   labeler("n").apply(7) -> "n7"
 *
 * -- Le plan --
 *
 *   1. Rendre value -> prefix + value.
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
 *   - statement.strip().startsWith("x =") && !statement.strip().startsWith("x ==")
 *   - IntSupplier : () -> int ; IntUnaryOperator : int -> int.
 */
public class Exercise10_EffectivelyFinalInPractice {

    public static boolean isEffectivelyFinal(boolean declaredWithValue, String... statements) {
        throw new UnsupportedOperationException("TODO 1 : implementer isEffectivelyFinal()");
    }

    public static IntUnaryOperator multiplier(int factor) {
        throw new UnsupportedOperationException("TODO 2 : implementer multiplier()");
    }

    public static List<IntSupplier> suppliers(int n) {
        throw new UnsupportedOperationException("TODO 3 : implementer suppliers()");
    }

    public static int countWithLambda(int times) {
        throw new UnsupportedOperationException("TODO 4 : implementer countWithLambda()");
    }

    public static Function<Integer, String> labeler(String prefix) {
        throw new UnsupportedOperationException("TODO 5 : implementer labeler()");
    }

    public static void main(String[] args) {
        // Verdicts REELS de javac 17 (true = la lambda "() -> x" compile).
        Object[][] javac = {
                {true, new String[] {}, true},
                {true, new String[] {"x = 10;"}, false},
                {true, new String[] {"x++;"}, false},
                {true, new String[] {"x += 1;"}, false},
                {true, new String[] {"x = 5;"}, false},
                {false, new String[] {"x = 3;"}, true},
                {false, new String[] {"x = 3;", "x = 4;"}, false},
                {true, new String[] {"int y = x + 1;"}, true}};
        int agree = 0;
        for (Object[] v : javac) {
            if (isEffectivelyFinal((Boolean) v[0], (String[]) v[1]) == (Boolean) v[2]) {
                agree++;
            }
        }
        ExerciseChecker.check("isEffectivelyFinal() == javac sur 8 cas (" + agree + " d'accord)", agree == 8);

        ExerciseChecker.check("multiplier(3).applyAsInt(7) == 21", multiplier(3).applyAsInt(7) == 21);
        List<IntSupplier> list = suppliers(3);
        ExerciseChecker.check("suppliers(3) rendent 0, 1, 2",
                list.size() == 3 && list.get(0).getAsInt() == 0 && list.get(1).getAsInt() == 1 && list.get(2).getAsInt() == 2);
        ExerciseChecker.check("countWithLambda(5) == 5", countWithLambda(5) == 5);
        ExerciseChecker.check("labeler(\"n\").apply(7) == \"n7\"", labeler("n").apply(7).equals("n7"));

        ExerciseChecker.summary();
    }
}
