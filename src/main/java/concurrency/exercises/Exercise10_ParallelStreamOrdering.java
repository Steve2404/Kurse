package concurrency.exercises;

import concurrency.ExerciseChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EXERCICE 10 - Un stream parallele traite les elements dans le desordre (niveau : difficile)
 * ==========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * .parallelStream() decoupe le travail entre PLUSIEURS threads, qui
 * traitent chacun un morceau, PAS FORCEMENT dans l'ordre d'origine.
 * Mais ca ne veut PAS dire que "tout devient imprevisible" : certaines
 * operations RECOLLENT les morceaux dans le bon ordre a la fin
 * (collect(), toArray(), les reductions comme sum()), tandis que
 * D'AUTRES (forEach(), specifiquement) ne le garantissent PAS DU TOUT.
 *
 *
 * ==================================================================
 * TODO 1 : sumWithParallelStream(values)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. values.parallelStream().mapToLong(Integer::longValue).sum().
 *
 * -- Ce qu'on remarque --
 *
 * Une addition est associative (peu importe l'ordre dans lequel on
 * additionne des nombres, le total est le meme) : sum() reste
 * TOUJOURS correct en parallele, meme si l'ADDITION reelle se fait
 * dans un ordre different a chaque execution.
 *
 *
 * ==================================================================
 * TODO 2 : collectOrderedWithParallel(values)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. values.parallelStream().map(v -> v * 2).collect(Collectors.toList()).
 *
 * -- Ce qu'on remarque --
 *
 * MEME si le travail se fait en parallele, dans le desordre en
 * coulisses, collect(Collectors.toList()) RECONSTRUIT le resultat dans
 * l'ordre D'ORIGINE de la source (une List est une source "ordonnee").
 * Le resultat est donc EXACTEMENT le meme qu'en sequentiel.
 *
 *
 * ==================================================================
 * TODO 3 : traceUnorderedForEach(values, trace)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * forEach() est l'exception notable : le chapitre insiste dessus, son
 * comportement en parallele n'a JAMAIS garanti l'ordre, MEME sur une
 * source ordonnee comme une List - chaque thread ajoute ses resultats
 * a 'trace' des qu'il a fini SON morceau, sans se soucier de l'ordre
 * d'origine.
 *
 * -- Le plan --
 *
 *   1. values.parallelStream().forEach(v -> trace.add(v)).
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne, c'est le CHOIX de l'operation
 * terminale (et sa garantie d'ordre ou non) qui est le vrai coeur de
 * l'exercice.
 *
 * Exemple a verifier : sur 100 000 entiers, sumWithParallelStream() et
 * collectOrderedWithParallel() donnent TOUJOURS exactement le meme
 * resultat qu'en sequentiel. traceUnorderedForEach() contient bien LES
 * MEMES elements (le meme "multi-ensemble"), mais tres probablement
 * PAS dans le meme ordre que la liste d'origine.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - 'trace' doit etre une liste THREAD-SAFE (par exemple
 *     Collections.synchronizedList(new ArrayList<>())) - plusieurs
 *     threads y ajoutent en meme temps, une ArrayList normale
 *     risquerait de se corrompre.
 */
public class Exercise10_ParallelStreamOrdering {

    public static long sumWithParallelStream(List<Integer> values) {
        throw new UnsupportedOperationException("TODO 1 : implementer sumWithParallelStream()");
    }

    public static List<Integer> collectOrderedWithParallel(List<Integer> values) {
        throw new UnsupportedOperationException("TODO 2 : implementer collectOrderedWithParallel()");
    }

    public static void traceUnorderedForEach(List<Integer> values, List<Integer> trace) {
        throw new UnsupportedOperationException("TODO 3 : implementer traceUnorderedForEach()");
    }

    public static void main(String[] args) {
        List<Integer> values = new ArrayList<>();
        for (int i = 1; i <= 100_000; i++) {
            values.add(i);
        }

        long expectedSum = values.stream().mapToLong(Integer::longValue).sum();
        ExerciseChecker.check("sumWithParallelStream == somme sequentielle", sumWithParallelStream(values) == expectedSum);

        List<Integer> expectedOrdered = values.stream().map(v -> v * 2).collect(Collectors.toList());
        ExerciseChecker.check("collectOrderedWithParallel garde l'ordre d'origine",
                collectOrderedWithParallel(values).equals(expectedOrdered));

        List<Integer> trace = Collections.synchronizedList(new ArrayList<>());
        traceUnorderedForEach(values, trace);
        List<Integer> sortedTrace = new ArrayList<>(trace);
        Collections.sort(sortedTrace);
        ExerciseChecker.check("traceUnorderedForEach contient EXACTEMENT les memes elements (une fois retries)",
                sortedTrace.equals(values));
        System.out.println("(pour info, ordre respecte par forEach cette fois-ci : " + trace.equals(values) + " -"
                + " en general false, mais ce n'est PAS garanti dans un sens ou dans l'autre)");

        ExerciseChecker.summary();
    }
}
