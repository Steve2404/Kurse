package ch13_concurrency.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * EXERCICE 10 - Deadlock, livelock, famine, race condition : les eviter en code (niveau : difficile)
 * ==================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Les 4 problemes classiques :
 *
 *   DEADLOCK        des threads s'attendent EN CERCLE, chacun tient ce que l'autre veut : tout le monde dort pour toujours.
 *   LIVELOCK        des threads restent ACTIFS mais s'annulent sans fin (deux personnes polies dans un couloir).
 *   STARVATION      un thread n'obtient JAMAIS la ressource, toujours double par d'autres (aucun cercle).
 *   RACE CONDITION  le resultat depend de l'ordre imprevisible des threads (value++ n'est pas atomique).
 *
 * Le virement classique qui bloque : le thread 1 verrouille A puis B,
 * le thread 2 verrouille B puis A, en meme temps -> deadlock. Le remede
 * de l'examen : TOUJOURS verrouiller dans le MEME ordre (par exemple
 * le plus petit id d'abord), quel que soit le sens du virement.
 *
 * SECURITE : main() lance les virements sur des threads DAEMON avec un
 * delai maximal. Si ta version bloque, le test echoue et le programme
 * se termine quand meme (un thread daemon n'empeche pas la JVM de
 * s'arreter). Ne lance JAMAIS de deadlock volontaire ailleurs.
 *
 *
 * ==================================================================
 * TODO 1 : transfer(from, to, amount)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Deplacer amount de from vers to, en tenant les DEUX verrous
 * (synchronized sur les deux comptes), sans jamais pouvoir faire de
 * deadlock quand un autre thread fait le virement inverse.
 *
 * -- Essayons a la main --
 *
 *   A(id 1)=20 000, B(id 2)=20 000 ; 10 000 virements A->B de 1 et 9 000 B->A de 1, en parallele
 *   -> se termine, avec A = 19 000 et B = 21 000
 *
 * -- Le plan --
 *
 *   1. first = le compte de plus petit id, second = l'autre.
 *   2. synchronized (first) { synchronized (second) { from.balance -= amount; to.balance += amount; } }
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : tryTransfer(from, to, amount, millis)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Autre remede : ne pas attendre pour toujours. Prendre le lock de
 * from, puis celui de to, chacun avec tryLock(millis) ; si l'un des
 * deux n'est pas obtenu a temps, tout relacher et rendre false (rien
 * n'a bouge). Sinon faire le virement et rendre true.
 *
 * -- Le plan --
 *
 *   1. if (from.lock.tryLock(millis, MILLISECONDS)) { try { ... meme chose pour to ... } finally { from.lock.unlock(); } }
 *   2. unlock() TOUJOURS dans un finally, et seulement si le tryLock a reussi.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : safeCount(threads, perThread)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * threads threads incrementent perThread fois un compteur partage.
 * Rendre le total : il doit etre EXACT a chaque execution (8 x 50 000
 * -> 400 000), donc sans race condition.
 *
 * -- Le plan --
 *
 *   1. Un AtomicInteger ; chaque thread fait incrementAndGet() en boucle.
 *   2. start() de tous les threads, puis join() de tous.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : diagnose(waitInCircle, busyWithoutProgress, oneNeverServed, resultDependsOnTiming)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. waitInCircle -> "DEADLOCK" ; busyWithoutProgress -> "LIVELOCK" ;
 *      oneNeverServed -> "STARVATION" ; resultDependsOnTiming -> "RACE_CONDITION" ; sinon "OK".
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
 *   - Account first = from.id < to.id ? from : to;
 *   - from.lock.tryLock(millis, TimeUnit.MILLISECONDS) lance InterruptedException (checked).
 *   - AtomicInteger counter = new AtomicInteger(); Thread[] all = new Thread[threads];
 */
public class Exercise10_DeadlockFreeTransfers {

    public static class Account {
        public final int id;
        public int balance;
        public final ReentrantLock lock = new ReentrantLock();

        public Account(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }
    }

    public static void transfer(Account from, Account to, int amount) {
        throw new UnsupportedOperationException("TODO 1 : implementer transfer()");
    }

    public static boolean tryTransfer(Account from, Account to, int amount, long millis) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 2 : implementer tryTransfer()");
    }

    public static int safeCount(int threads, int perThread) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 3 : implementer safeCount()");
    }

    public static String diagnose(boolean waitInCircle, boolean busyWithoutProgress, boolean oneNeverServed, boolean resultDependsOnTiming) {
        throw new UnsupportedOperationException("TODO 4 : implementer diagnose()");
    }

    public static void main(String[] args) throws Exception {
        Account a = new Account(1, 20_000);
        Account b = new Account(2, 20_000);
        ExecutorService daemons = Executors.newFixedThreadPool(2, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
        Future<?> aToB = daemons.submit(() -> {
            for (int i = 0; i < 10_000; i++) {
                transfer(a, b, 1);
            }
        });
        Future<?> bToA = daemons.submit(() -> {
            for (int i = 0; i < 9_000; i++) {
                transfer(b, a, 1);
            }
        });
        daemons.shutdown();
        boolean finished = daemons.awaitTermination(10, TimeUnit.SECONDS);
        if (finished) {
            aToB.get();   // fait remonter une exception lancee dans les virements (ExecutionException)
            bToA.get();
        } else {
            daemons.shutdownNow();
        }
        ExerciseChecker.check("transfer : 19 000 virements croises se terminent (pas de deadlock) -> A = 19 000, B = 21 000",
                finished && a.balance == 19_000 && b.balance == 21_000);

        Account c = new Account(3, 100);
        Account d = new Account(4, 100);
        CountDownLatch held = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        Thread holder = new Thread(() -> {
            d.lock.lock();
            try {
                held.countDown();
                release.await();
            } catch (InterruptedException e) {
                // fin
            } finally {
                d.lock.unlock();
            }
        });
        holder.setDaemon(true);
        holder.start();
        held.await();
        boolean refused = !tryTransfer(c, d, 10, 50);
        boolean nothingMoved = c.balance == 100 && d.balance == 100 && !c.lock.isLocked();
        release.countDown();
        holder.join();
        boolean accepted = tryTransfer(c, d, 10, 50);
        ExerciseChecker.check("tryTransfer : refuse (false) quand to est verrouille ailleurs, sans rien bouger ni garder de verrou",
                refused && nothingMoved);
        ExerciseChecker.check("tryTransfer : accepte (true) une fois le verrou libre -> 90 / 110",
                accepted && c.balance == 90 && d.balance == 110 && !c.lock.isLocked() && !d.lock.isLocked());

        boolean exact = true;
        for (int run = 0; run < 3; run++) {
            exact &= safeCount(8, 50_000) == 400_000;
        }
        ExerciseChecker.check("safeCount(8, 50 000) == 400 000, trois fois de suite", exact);

        ExerciseChecker.check("diagnose : les 4 problemes + OK",
                diagnose(true, false, false, false).equals("DEADLOCK")
                        && diagnose(false, true, false, false).equals("LIVELOCK")
                        && diagnose(false, false, true, false).equals("STARVATION")
                        && diagnose(false, false, false, true).equals("RACE_CONDITION")
                        && diagnose(false, false, false, false).equals("OK"));

        ExerciseChecker.summary();
    }
}
