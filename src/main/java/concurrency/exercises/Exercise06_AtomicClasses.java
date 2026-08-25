package concurrency.exercises;

import concurrency.ExerciseChecker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * EXERCICE 6 - Les classes atomiques (niveau : difficile)
 * ====================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * AtomicInteger ressemble a un int, mais chacune de ses operations
 * (incrementAndGet(), accumulateAndGet()...) est GARANTIE atomique :
 * "lire-modifier-ranger" se fait comme un SEUL geste indivisible, que
 * DEUX threads ne peuvent jamais se marcher dessus au milieu - sans
 * jamais avoir besoin d'un synchronized ou d'un ReentrantLock explicite
 * (Exercise04/Exercise05).
 *
 * -- Piege classique de l'examen : atomic vs volatile --
 *
 * Un champ 'volatile int' garantit que TOUS les threads voient
 * IMMEDIATEMENT la derniere valeur ecrite (visibilite), mais ne rend
 * PAS "value++" atomique pour autant - les 3 sous-etapes cachees
 * (lire/modifier/ranger, voir Exercise04) restent tout aussi
 * separables avec volatile qu'avec un int normal. AtomicInteger
 * resout les DEUX problemes a la fois (visibilite ET atomicite),
 * volatile ne resout QUE la visibilite.
 *
 *
 * ==================================================================
 * TODO 1 : incrementAtomic(counter)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Appeler counter.incrementAndGet() (la version atomique de
 *      "value++").
 *
 *
 * ==================================================================
 * TODO 2 : updateMax(currentMax, candidate)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "si candidate est plus grand que currentMax, alors le remplacer"
 * ressemble a une seule etape, mais c'est encore un piege
 * lire-puis-agir : entre le moment ou un thread LIT currentMax et le
 * moment ou il le REMPLACE, un AUTRE thread a pu deja le changer entre
 * temps. accumulateAndGet(candidate, operateur) fait tout ca de facon
 * ATOMIQUE : lire, combiner avec l'operateur fourni (ici Math::max),
 * et ranger, sans jamais laisser un autre thread s'immiscer au milieu.
 *
 * -- Le plan --
 *
 *   1. Appeler currentMax.accumulateAndGet(candidate, Math::max).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule ligne, c'est le CHOIX de la bonne
 * methode atomique qui est le vrai coeur de l'exercice.
 *
 * Exemple a verifier : 8 threads appellent incrementAtomic() 10 000
 * fois chacun sur le MEME AtomicInteger -> resultat final EXACTEMENT
 * 80 000. 20 threads appellent updateMax() en parallele avec des
 * valeurs differentes -> le resultat final est EXACTEMENT le plus
 * grand candidat propose, quel que soit l'ordre reel d'execution.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - incrementAndGet() incremente PUIS renvoie la nouvelle valeur (il
 *     existe aussi getAndIncrement(), qui renvoie l'ANCIENNE valeur -
 *     peu importe ici lequel des deux, la valeur finale du compteur
 *     est la meme).
 *   - accumulateAndGet(valeur, BinaryOperator) applique l'operateur
 *     entre la valeur ACTUELLE et 'valeur', range le resultat, et le
 *     renvoie - le tout de facon atomique.
 */
public class Exercise06_AtomicClasses {

    public static void incrementAtomic(AtomicInteger counter) {
        throw new UnsupportedOperationException("TODO 1 : implementer incrementAtomic()");
    }

    public static void updateMax(AtomicInteger currentMax, int candidate) {
        throw new UnsupportedOperationException("TODO 2 : implementer updateMax()");
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        int threadCount = 8;
        int incrementsPerThread = 10_000;
        Thread[] threads = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            threads[t] = new Thread(() -> {
                for (int i = 0; i < incrementsPerThread; i++) {
                    incrementAtomic(counter);
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        ExerciseChecker.check("aucune incrementation perdue avec AtomicInteger",
                counter.get() == threadCount * incrementsPerThread);

        AtomicInteger currentMax = new AtomicInteger(Integer.MIN_VALUE);
        int candidateCount = 20;
        int trueMax = Integer.MIN_VALUE;
        Thread[] maxThreads = new Thread[candidateCount];
        for (int t = 0; t < candidateCount; t++) {
            int candidate = (t * 37) % 1000;
            trueMax = Math.max(trueMax, candidate);
            maxThreads[t] = new Thread(() -> updateMax(currentMax, candidate));
        }
        for (Thread thread : maxThreads) {
            thread.start();
        }
        for (Thread thread : maxThreads) {
            thread.join();
        }
        ExerciseChecker.check("le maximum final est correct malgre la concurrence", currentMax.get() == trueMax);

        ExerciseChecker.summary();
    }
}
