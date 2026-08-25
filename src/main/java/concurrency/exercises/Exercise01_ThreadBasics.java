package concurrency.exercises;

import concurrency.ExerciseChecker;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * EXERCICE 1 - Creer, attendre et interrompre un Thread (niveau : moyen/difficile)
 * ===============================================================================================
 *
 * -- Rappel du decoupage en "boites magiques" --
 *
 * Une methode, c'est une boite magique : tu la nourris d'ingredients
 * (parametres), et elle rend un resultat, sans que tu aies besoin de
 * savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
 * demande-toi : est-ce qu'elle se raconte seule ? revient-elle
 * plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
 * moins une question, elle merite sa propre boite.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Thread, c'est un "petit robot ouvrier" qui travaille EN MEME
 * TEMPS que toi (le thread principal), sans t'attendre. Runnable
 * decrit LE TRAVAIL a faire ("quoi"), le Thread est l'OUVRIER qui va
 * l'executer ("qui"). start() reveille l'ouvrier et le lance TOUT DE
 * SUITE, sans attendre qu'il finisse. join() dit "attends ici que CET
 * ouvrier ait fini son travail avant de continuer" - sans join(), tu
 * ne peux JAMAIS etre sur que le travail est deja termine.
 *
 *
 * ==================================================================
 * TODO 1 : startAndJoin(task)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Fabriquer un nouveau Thread a partir du Runnable recu.
 *   2. Le demarrer (start()).
 *   3. ATTENDRE qu'il ait fini (join()) avant de rendre la main a
 *      l'appelant.
 *
 * Exemple a verifier : un drapeau (AtomicBoolean) commence a false.
 * Apres startAndJoin(() -> drapeau.set(true)), le drapeau DOIT deja
 * etre a true - la preuve que join() a bien attendu la fin reelle du
 * travail, et pas juste le demarrage.
 *
 *
 * ==================================================================
 * TODO 2 : buildInterruptibleWorker(interruptedFlag)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un ouvrier qui fait une PAUSE de 50 millisecondes en boucle,
 * indefiniment, en attendant qu'on lui dise d'arreter. interrupt() est
 * la facon POLIE de lui dire "arrete-toi quand tu peux" - ca ne le
 * coupe pas en plein geste, mais ca reveille sa pause en cours (sous
 * la forme d'une InterruptedException) pour qu'IL DECIDE lui-meme
 * quoi faire (ici : lever un drapeau et s'arreter proprement).
 *
 * -- Le plan --
 *
 *   1. Renvoyer un NOUVEAU Thread (pas encore demarre), dont le
 *      travail est une boucle infinie qui :
 *      a. essaie de dormir 50ms (Thread.sleep(50)) ;
 *      b. si cette attente est interrompue (InterruptedException
 *         attrapee) : mettre interruptedFlag a true, et ARRETER la
 *         boucle (return) - ne JAMAIS ignorer une interruption en
 *         boucle indefiniment.
 *
 * Exemple a verifier : on demarre ce thread, on le laisse le temps
 * d'entrer dans sa boucle de sommeil, on l'interrompt (interrupt()),
 * puis on l'attend (join()). Apres ca : interruptedFlag doit etre a
 * true, ET le thread ne doit plus etre vivant (isAlive() == false).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun est deja sa propre methode.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - new Thread(runnable).start() puis .join() (join() peut lancer
 *     InterruptedException, a laisser remonter ou declarer).
 *   - Thread.sleep(50) lance CHECKED InterruptedException si le
 *     thread est interrompu PENDANT qu'il dort - c'est le mecanisme
 *     normal de reveil d'une interruption.
 *   - thread.interrupt() ne "tue" jamais un thread de force - elle se
 *     contente de signaler la demande, le code du thread doit lui-
 *     meme choisir comment reagir (ici : s'arreter).
 */
public class Exercise01_ThreadBasics {

    public static void startAndJoin(Runnable task) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 1 : implementer startAndJoin()");
    }

    public static Thread buildInterruptibleWorker(AtomicBoolean interruptedFlag) {
        throw new UnsupportedOperationException("TODO 2 : implementer buildInterruptibleWorker()");
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicBoolean ran = new AtomicBoolean(false);
        startAndJoin(() -> ran.set(true));
        ExerciseChecker.check("startAndJoin() attend VRAIMENT la fin du travail avant de rendre la main",
                ran.get());

        AtomicBoolean interruptedFlag = new AtomicBoolean(false);
        Thread worker = buildInterruptibleWorker(interruptedFlag);
        worker.start();
        Thread.sleep(100);
        worker.interrupt();
        worker.join();
        ExerciseChecker.check("le worker a detecte l'interruption et leve son drapeau", interruptedFlag.get());
        ExerciseChecker.check("le worker s'est arrete proprement (plus vivant)", !worker.isAlive());

        ExerciseChecker.summary();
    }
}
