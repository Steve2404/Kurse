package ch13_concurrency.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * EXERCICE 12 - Capstone : producteur/consommateur avec BlockingQueue (niveau : capstone, style entretien)
 * ===========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un tapis roulant de longueur LIMITEE entre une usine
 * (producteur) et un entrepot (consommateur). Si le tapis est PLEIN,
 * l'usine doit PATIENTER avant de poser une nouvelle piece (elle ne
 * la jette pas, elle attend qu'une place se libere). Si le tapis est
 * VIDE, l'entrepot doit PATIENTER avant de pouvoir en retirer une
 * (il ne "regarde pas dans le vide", il attend qu'une piece arrive).
 * BlockingQueue fait EXACTEMENT ca tout seul : put() bloque si plein,
 * take() bloque si vide - jamais besoin d'ecrire soi-meme cette
 * logique d'attente.
 *
 * Ce capstone reutilise Thread (Exercise01) pour fabriquer les 2
 * ouvriers (producteur et consommateur), qui communiquent SEULEMENT
 * via la queue partagee - jamais directement l'un avec l'autre.
 *
 *
 * ==================================================================
 * TODO 1 : buildProducer(queue, itemCount)
 * ==================================================================
 *
 * -- Le plan --
 *
 * Renvoyer un Runnable qui :
 *
 *   1. Pose (put()) les entiers de 0 a itemCount-1, DANS L'ORDRE, un
 *      par un, dans 'queue' (put() attend TOUT SEUL si la queue est
 *      pleine - rien a gerer manuellement).
 *   2. Une fois TOUS les entiers poses, pose EN PLUS la sentinelle
 *      POISON_PILL (deja definie plus bas) - le signal convenu pour
 *      dire au consommateur "il n'y aura plus rien apres ca, tu peux
 *      t'arreter".
 *
 *
 * ==================================================================
 * TODO 2 : buildConsumer(queue, consumed)
 * ==================================================================
 *
 * -- Le plan --
 *
 * Renvoyer un Runnable qui :
 *
 *   1. Boucle indefiniment : retire (take()) un element de 'queue'
 *      (take() attend TOUT SEUL si la queue est vide).
 *   2. Si cet element EST la sentinelle POISON_PILL : arreter la
 *      boucle (return depuis le lambda, ou break).
 *   3. Sinon : l'ajouter a la liste 'consumed'.
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun est deja sa propre boite (un Runnable dedie).
 *
 * Exemple a verifier : runProducerConsumer(1000, 10) (une queue TRES
 * petite face a 1000 elements, pour forcer BEAUCOUP d'attentes des 2
 * cotes) renvoie une liste consumed EXACTEMENT egale a [0, 1, 2, ...,
 * 999], dans cet ordre precis - un seul producteur et un seul
 * consommateur sur une queue FIFO preservent toujours l'ordre.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - queue.put(valeur) et queue.take() lancent tous les 2
 *     InterruptedException (checked) - a attraper dans le Runnable
 *     (qui ne peut pas declarer "throws"), typiquement en appelant
 *     Thread.currentThread().interrupt() pour ne pas avaler
 *     silencieusement l'interruption.
 *   - POISON_PILL doit etre une valeur qui ne pourra JAMAIS etre
 *     confondue avec une vraie donnee produite (ici, Integer.MIN_VALUE,
 *     hors de la plage 0..itemCount-1 utilisee).
 */
public class Exercise12_ProducerConsumerCapstone {

    static final int POISON_PILL = Integer.MIN_VALUE;

    public static Runnable buildProducer(BlockingQueue<Integer> queue, int itemCount) {
        throw new UnsupportedOperationException("TODO 1 : implementer buildProducer()");
    }

    public static Runnable buildConsumer(BlockingQueue<Integer> queue, List<Integer> consumed) {
        throw new UnsupportedOperationException("TODO 2 : implementer buildConsumer()");
    }

    public static List<Integer> runProducerConsumer(int itemCount, int queueCapacity) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(queueCapacity);
        List<Integer> consumed = new ArrayList<>();

        Thread producer = new Thread(buildProducer(queue, itemCount));
        Thread consumer = new Thread(buildConsumer(queue, consumed));

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        return consumed;
    }

    public static void main(String[] args) throws InterruptedException {
        List<Integer> expected = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            expected.add(i);
        }

        List<Integer> consumed = runProducerConsumer(1000, 10);
        ExerciseChecker.check("les 1000 elements sont recus EXACTEMENT dans l'ordre de production",
                consumed.equals(expected));

        ExerciseChecker.summary();
    }
}
