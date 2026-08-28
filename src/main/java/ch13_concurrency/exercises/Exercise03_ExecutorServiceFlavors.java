package ch13_concurrency.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 3 - newSingleThreadExecutor() et ScheduledExecutorService (niveau : difficile)
 * ======================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un ExecutorService "normal" (newFixedThreadPool(n) avec n > 1) a
 * PLUSIEURS ouvriers qui travaillent VRAIMENT en meme temps - si tu
 * leur donnes des taches de durees differentes, celle qui finit le
 * plus VITE peut tres bien etre RENDUE avant une autre commencee plus
 * tot. newSingleThreadExecutor() n'a QU'UN SEUL ouvrier : il traite
 * TOUJOURS les taches DANS L'ORDRE ou elles sont arrivees, une par
 * une, meme si certaines sont plus longues que d'autres.
 *
 *
 * ==================================================================
 * TODO 1 : runInSingleThreadOrder(taskCount, trace)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * taskCount = 5. La tache #0 dort le PLUS LONGTEMPS, la tache #4 le
 * MOINS longtemps (expres, pour "tenter" de les faire finir dans le
 * desordre). Avec un seul ouvrier, la tache #1 ne peut MEME PAS
 * commencer avant que la #0 soit terminee - le resultat final dans
 * 'trace' doit etre EXACTEMENT ["tache-0", "tache-1", ..., "tache-4"],
 * dans l'ordre de soumission, jamais autrement.
 *
 * -- Le plan --
 *
 *   1. Fabriquer un Executors.newSingleThreadExecutor().
 *   2. Soumettre 'taskCount' taches, numerotees de 0 a taskCount-1,
 *      chacune : dort (taskCount - index) * 15 millisecondes, PUIS
 *      ajoute "tache-" + index a trace.
 *   3. Arreter proprement l'executor (shutdown() PUIS
 *      awaitTermination(...) pour attendre la fin reelle de tout le
 *      monde avant de rendre la main).
 *
 *
 * ==================================================================
 * TODO 2 : countScheduledExecutions(targetCount, periodMillis)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * scheduleAtFixedRate(tache, delaiInitial, periode, unite) repete UNE
 * MEME tache encore et encore, a intervalles reguliers, jusqu'a ce
 * qu'on lui dise explicitement d'arreter (cancel()). Pour savoir
 * QUAND arreter proprement dans un test (plutot que de deviner un
 * temps d'attente au hasard), on utilise un CountDownLatch : un
 * compte a rebours partage qui se decremente a CHAQUE execution, et
 * qu'on peut ATTENDRE (await()) jusqu'a ce qu'il atteigne zero.
 *
 * -- Le plan --
 *
 *   1. Fabriquer un Executors.newScheduledThreadPool(1), un compteur
 *      atomique a 0, et un CountDownLatch(targetCount).
 *   2. scheduleAtFixedRate(...) : a CHAQUE execution, incrementer le
 *      compteur ET decrementer le latch (countDown()).
 *   3. Attendre le latch (await(5, TimeUnit.SECONDS)) - il se debloque
 *      des que targetCount executions ont eu lieu.
 *   4. Annuler la tache repetee (future.cancel(true)) et arreter
 *      l'executor (shutdown()).
 *   5. Renvoyer la valeur finale du compteur (elle peut etre
 *      LEGEREMENT superieure a targetCount, si une execution
 *      supplementaire a demarre juste avant le cancel() - c'est
 *      normal et attendu).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun est deja sa propre methode.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - executor.shutdown() arrete d'ACCEPTER de nouvelles taches, mais
 *     ne coupe PAS celles deja en cours - awaitTermination() attend
 *     leur fin reelle.
 *   - scheduleAtFixedRate(Runnable, delaiInitial, periode, unite) :
 *     le delai initial peut etre 0 (demarrer tout de suite).
 */
public class Exercise03_ExecutorServiceFlavors {

    public static void runInSingleThreadOrder(int taskCount, List<String> trace) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 1 : implementer runInSingleThreadOrder()");
    }

    public static int countScheduledExecutions(int targetCount, long periodMillis) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 2 : implementer countScheduledExecutions()");
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> trace = java.util.Collections.synchronizedList(new java.util.ArrayList<>());
        runInSingleThreadOrder(5, trace);
        ExerciseChecker.check("newSingleThreadExecutor traite TOUJOURS dans l'ordre de soumission",
                trace.equals(List.of("tache-0", "tache-1", "tache-2", "tache-3", "tache-4")));

        int count = countScheduledExecutions(5, 20);
        ExerciseChecker.check("scheduleAtFixedRate s'est bien execute au moins 5 fois", count >= 5);

        ExerciseChecker.summary();
    }
}
