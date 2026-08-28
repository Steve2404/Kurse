package ch13_concurrency.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * EXERCICE 7 - CyclicBarrier : un point de rendez-vous (niveau : difficile)
 * =======================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine plusieurs randonneurs qui font CHACUN une etape de montee
 * (phase 1) a leur propre rythme, avec la regle stricte : "PERSONNE ne
 * commence la descente (phase 2) tant que TOUT LE MONDE n'est pas
 * arrive au sommet". CyclicBarrier(n) EST exactement cette regle : un
 * point de rendez-vous qui bloque chaque thread arrivant a
 * barrier.await(), jusqu'a ce que EXACTEMENT n threads y soient tous
 * arrives - alors, et seulement alors, TOUS repartent EN MEME TEMPS.
 * ("Cyclic" car la meme barriere peut resservir pour un 2e rendez-vous
 * plus tard, ce qu'on n'utilise pas ici.)
 *
 *
 * ==================================================================
 * TODO : buildBarrierWorker(barrier, index, trace)
 * ==================================================================
 *
 * -- Le plan --
 *
 * Renvoyer un Runnable qui, execute :
 *
 *   1. Ajoute "phase1-" + index a trace (la phase de montee).
 *   2. Attend au rendez-vous (barrier.await()).
 *   3. Ajoute "phase2-" + index a trace (la phase de descente,
 *      seulement possible une fois TOUT LE MONDE arrive).
 *
 * Exemple a verifier : avec 6 randonneurs, une fois tous les threads
 * termines, trace doit contenir 6 entrees "phase1-*" ET 6 entrees
 * "phase2-*" - et surtout, TOUTES les "phase1-*" doivent apparaitre
 * AVANT n'importe quelle "phase2-*" dans l'ordre d'ecriture (l'ordre
 * EXACT entre randonneurs a l'interieur d'une meme phase n'est PAS
 * garanti, seule la separation stricte entre les 2 phases l'est).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : le Runnable tient en 3 lignes.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - barrier.await() peut lancer InterruptedException et
 *     BrokenBarrierException (checked toutes les 2) - a laisser
 *     remonter, ou attraper et re-lancer en RuntimeException si le
 *     Runnable ne peut pas declarer "throws".
 *   - new CyclicBarrier(n) : n est le nombre EXACT de threads attendus
 *     a chaque rendez-vous - ni plus, ni moins.
 */
public class Exercise07_CyclicBarrierRendezvous {

    public static Runnable buildBarrierWorker(CyclicBarrier barrier, int index, List<String> trace) {
        throw new UnsupportedOperationException("TODO : implementer buildBarrierWorker()");
    }

    public static void main(String[] args) throws InterruptedException {
        int hikerCount = 6;
        CyclicBarrier barrier = new CyclicBarrier(hikerCount);
        List<String> trace = Collections.synchronizedList(new ArrayList<>());

        Thread[] threads = new Thread[hikerCount];
        for (int i = 0; i < hikerCount; i++) {
            threads[i] = new Thread(buildBarrierWorker(barrier, i, trace));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        ExerciseChecker.check("6 randonneurs ont fait la phase 1 et la phase 2", trace.size() == 2 * hikerCount);

        int lastPhase1Position = -1;
        int firstPhase2Position = Integer.MAX_VALUE;
        for (int i = 0; i < trace.size(); i++) {
            if (trace.get(i).startsWith("phase1")) {
                lastPhase1Position = Math.max(lastPhase1Position, i);
            } else {
                firstPhase2Position = Math.min(firstPhase2Position, i);
            }
        }
        ExerciseChecker.check("AUCUNE phase2 ne commence avant que TOUTES les phase1 soient finies",
                lastPhase1Position < firstPhase2Position);

        ExerciseChecker.summary();
    }
}
