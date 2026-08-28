package ch11_exceptions.exercises;

import ch11_exceptions.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * EXERCICE 2 - Multi-catch, et finally qui s'execute TOUJOURS (niveau : moyen/difficile)
 * ====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un multi-catch (catch (TypeA | TypeB e)) permet de traiter PLUSIEURS
 * types d'exception AVEC LE MEME code, sans dupliquer le bloc catch -
 * a condition qu'aucun des deux types ne soit un sous-type de l'autre
 * (sinon, Java te force a choisir un ordre precis avec des blocs
 * separes, voir Exercise03).
 *
 * Le bloc finally, lui, s'execute TOUJOURS - meme si le try se termine
 * par un return, meme si une exception NON attrapee (par aucun catch)
 * remonte plus loin. C'est le seul endroit garanti de s'executer quoi
 * qu'il arrive (sauf arret brutal de la JVM).
 *
 *
 * ==================================================================
 * TODO : withFinallyTrace(action, trace)
 * ==================================================================
 *
 * -- Le plan --
 *
 * Construire :
 *
 *   try {
 *       int result = action.get();
 *       trace.add("try");
 *       return result;
 *   } catch (ArithmeticException | NullPointerException e) {
 *       trace.add("catch:" + e.getClass().getSimpleName());
 *       return -1;
 *   } finally {
 *       trace.add("finally");
 *   }
 *
 * -- Essayons a la main --
 *
 * action qui renvoie 5 sans exception -> trace = ["try", "finally"],
 * resultat = 5.
 *
 * action qui lance une ArithmeticException (ex: 1/0) -> attrapee par
 * le multi-catch -> trace = ["catch:ArithmeticException", "finally"],
 * resultat = -1.
 *
 * action qui lance une IllegalStateException (PAS dans la liste du
 * multi-catch) -> ni try ni catch ne peuvent la retenir, elle remonte
 * hors de la methode - MAIS finally s'execute quand meme AVANT
 * qu'elle ne remonte : trace contiendra quand meme "finally", meme
 * si withFinallyTrace() elle-meme ne renvoie jamais normalement dans
 * ce cas.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : un seul bloc try/catch/finally suffit.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Une IllegalStateException n'est ni une ArithmeticException ni
 *     une NullPointerException : le multi-catch (ArithmeticException |
 *     NullPointerException e) ne la retient PAS, elle continue son
 *     chemin normalement (comme s'il n'y avait aucun catch pour elle).
 */
public class Exercise02_MultiCatchAndFinally {

    public static Integer withFinallyTrace(Supplier<Integer> action, List<String> trace) {
        throw new UnsupportedOperationException("TODO : implementer withFinallyTrace()");
    }

    public static void main(String[] args) {
        List<String> trace1 = new ArrayList<>();
        Integer result1 = withFinallyTrace(() -> 5, trace1);
        ExerciseChecker.check("action OK : resultat == 5, trace == [try, finally]",
                result1 == 5 && trace1.equals(List.of("try", "finally")));

        List<String> trace2 = new ArrayList<>();
        Integer result2 = withFinallyTrace(() -> {
            throw new ArithmeticException("/ by zero");
        }, trace2);
        ExerciseChecker.check("ArithmeticException attrapee par le multi-catch : resultat == -1",
                result2 == -1 && trace2.equals(List.of("catch:ArithmeticException", "finally")));

        List<String> trace3 = new ArrayList<>();
        Integer result3 = withFinallyTrace(() -> {
            throw new NullPointerException("valeur nulle");
        }, trace3);
        ExerciseChecker.check("NullPointerException attrapee par le meme multi-catch : resultat == -1",
                result3 == -1 && trace3.equals(List.of("catch:NullPointerException", "finally")));

        List<String> trace4 = new ArrayList<>();
        boolean propagated = false;
        try {
            withFinallyTrace(() -> {
                throw new IllegalStateException("hors du multi-catch");
            }, trace4);
        } catch (IllegalStateException e) {
            propagated = true;
        }
        ExerciseChecker.check("IllegalStateException NON couverte par le multi-catch : elle remonte quand meme",
                propagated);
        ExerciseChecker.check("... mais finally s'est quand meme execute avant qu'elle ne remonte",
                trace4.equals(List.of("finally")));

        ExerciseChecker.summary();
    }
}
