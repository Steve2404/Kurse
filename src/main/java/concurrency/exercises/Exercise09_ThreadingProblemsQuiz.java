package concurrency.exercises;

/**
 * EXERCICE 9 - Identifier les problemes classiques de concurrence (niveau : examen OCP)
 * ====================================================================================================
 *
 * Comme collections.exercises.Exercise10_WildcardsQuiz ou
 * exceptions.exercises.Exercise03_CatchOrderAndOverridingQuiz : lis
 * chaque scenario, essaie de repondre TOI-MEME (quel probleme ? sur
 * quels indices ?), PUIS regarde les "Reponses officielles" en bas.
 *
 * ATTENTION IMPORTANTE : contrairement aux autres quiz du projet, ce
 * fichier N'A PAS de main() qui lance tout automatiquement. Les blocs
 * A et C, s'ils etaient VRAIMENT executes, BLOQUERAIENT LA JVM POUR
 * TOUJOURS (deadlock / livelock) - ils restent donc COMMENTES ici a
 * vie ; ne les decommente que dans un fichier a part, jetable, en
 * sachant que tu devras tuer le processus a la main (Ctrl+C) pour t'en
 * sortir. Seul le Bloc D (race condition) est SANS DANGER a executer -
 * il se termine toujours, juste avec un resultat parfois FAUX.
 *
 * -- Les 4 problemes, en une phrase chacun (resume du chapitre) --
 *
 *   - DEADLOCK (interblocage) : 2 threads (ou plus) sont bloques POUR
 *     TOUJOURS, chacun attendant une ressource que l'autre tient deja.
 *   - STARVATION (famine) : UN thread precis n'obtient JAMAIS l'acces
 *     a une ressource partagee, sans cesse double par d'autres.
 *   - LIVELOCK (interblocage actif) : les threads restent ACTIFS
 *     (ils ne "dorment" pas comme en deadlock), mais s'annulent
 *     mutuellement pour toujours sans jamais progresser - une forme de
 *     starvation ou tout le monde est occupe pour rien.
 *   - RACE CONDITION : 2 threads s'executent EN MEME TEMPS sur une
 *     donnee partagee, et le resultat final depend de l'ordre EXACT
 *     (imprevisible) dans lequel ils s'entrelacent.
 *
 *
 * ------------------------------------------------------------------
 * Bloc A - Scenario (code JAMAIS a decommenter/executer)
 *
 * Histoire : 2 comptes bancaires. Un virement de compteA vers compteB
 * verrouille compteA D'ABORD, puis compteB. Un AUTRE virement, dans
 * l'autre sens (compteB vers compteA), verrouille compteB D'ABORD,
 * puis compteA. Les 2 virements se produisent EN MEME TEMPS.
 *
 * Quel probleme est-ce, et pourquoi ?
 * ------------------------------------------------------------------
 * // void transferAtoB(Object lockA, Object lockB) {
 * //     synchronized (lockA) {
 * //         synchronized (lockB) {
 * //             // ... transfert ...
 * //         }
 * //     }
 * // }
 * // void transferBtoA(Object lockA, Object lockB) {
 * //     synchronized (lockB) {
 * //         synchronized (lockA) {
 * //             // ... transfert ...
 * //         }
 * //     }
 * // }
 * // // Thread 1 : transferAtoB(compteA, compteB);
 * // // Thread 2 : transferBtoA(compteA, compteB);
 *
 *
 * ------------------------------------------------------------------
 * Bloc B - Scenario (aucun code, purement descriptif)
 *
 * Histoire : 10 threads "agressifs" et 1 thread "poli" appellent tous
 * en boucle synchronized(lockPartage) { ... } sur le MEME verrou. La
 * JVM ne garantit AUCUN ordre d'attribution du verrou entre threads en
 * attente. Au fil du temps, le thread "poli" se retrouve
 * systematiquement devance par un des 10 autres, encore et encore, et
 * n'execute quasiment jamais sa section critique.
 *
 * Quel probleme est-ce, et pourquoi ?
 * ------------------------------------------------------------------
 *
 *
 * ------------------------------------------------------------------
 * Bloc C - Scenario (code JAMAIS a decommenter/executer)
 *
 * Histoire : 2 personnes se croisent dans un couloir etroit. Chacune,
 * en POLITESSE, se decale du meme cote que l'autre vient de se
 * decaler, pour "la laisser passer" - et elles se decalent ainsi sans
 * fin, TOUJOURS du meme cote en meme temps, sans jamais reussir a se
 * croiser.
 *
 * Quel probleme est-ce, et pourquoi (en quoi est-ce DIFFERENT d'un
 * deadlock, puisque personne n'est jamais vraiment "bloque" a dormir) ?
 * ------------------------------------------------------------------
 * // void politeStep(java.util.concurrent.atomic.AtomicBoolean mySide, java.util.concurrent.atomic.AtomicBoolean otherSide) {
 * //     while (mySide.get() == otherSide.get()) {
 * //         mySide.set(!mySide.get()); // "apres vous, je me decale aussi"
 * //     }
 * // }
 * // // Thread 1 et Thread 2 appellent politeStep() en boucle infinie,
 * // // chacun avec (sonPropreCote, coteDeLAutre) inverses.
 *
 *
 * ------------------------------------------------------------------
 * Bloc D - SEUL bloc SANS DANGER a executer reellement (voir main())
 *
 * Histoire : 8 threads incrementent un MEME int (PAS un AtomicInteger,
 * PAS synchronized) 50 000 fois chacun. Le total THEORIQUE est
 * 400 000.
 *
 * Quel probleme est-ce, et pourquoi le resultat n'est-il PAS TOUJOURS
 * faux a chaque execution (essaie de lancer main() plusieurs fois de
 * suite) ?
 * ------------------------------------------------------------------
 *
 *
 * Reponses officielles (ne regarde qu'apres avoir repondu toi-meme) :
 *
 * Bloc A : DEADLOCK. Thread 1 tient compteA et attend compteB ; Thread
 *   2 tient compteB et attend compteA - un cycle d'attente PARFAIT,
 *   aucun des deux ne peut jamais avancer. Solution classique :
 *   toujours verrouiller les comptes dans le MEME ordre (par exemple,
 *   toujours le compte avec le plus petit identifiant en premier),
 *   quel que soit le sens du virement.
 *
 * Bloc B : STARVATION. Aucun cycle d'attente ici (contrairement au
 *   deadlock) - le thread poli PEUT techniquement obtenir le verrou, il
 *   n'y a juste JAMAIS de garantie d'equite (fairness) qui l'assure.
 *   Un ReentrantLock(true) (mode "fair", voir Exercise05) reduirait ce
 *   risque, contrairement a synchronized qui n'offre aucune garantie
 *   d'ordre.
 *
 * Bloc C : LIVELOCK. Les 2 threads restent ACTIFS EN PERMANENCE
 *   (contrairement au deadlock, ou ils dormiraient, bloques) - ils
 *   REAGISSENT sans cesse l'un a l'autre, mais cette reaction meme les
 *   empeche de jamais progresser. Une solution classique : introduire
 *   un delai ALEATOIRE avant de retenter, pour "desynchroniser" les 2
 *   threads.
 *
 * Bloc D : RACE CONDITION. "value++" cache 3 sous-etapes (lire,
 *   ajouter 1, ranger - rappel de l'Exercise04) : si 2 threads lisent
 *   la MEME valeur avant que l'un des deux n'ait fini de ranger la
 *   sienne, une incrementation est perdue. Le resultat n'est PAS
 *   TOUJOURS faux car le "mauvais entrelacement" depend du minutage
 *   REEL de l'OS et du JIT a cet instant precis - parfois les threads
 *   s'executent par pur hasard sans jamais se marcher dessus. C'est
 *   justement CA le danger d'une race condition : un bug qui ne se
 *   montre pas a chaque fois est un bug qu'on peut facilement rater en
 *   test, mais qui reapparaitra un jour en production.
 */
public class Exercise09_ThreadingProblemsQuiz {

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 8;
        int incrementsPerThread = 50_000;
        int[] value = {0};

        Thread[] threads = new Thread[threadCount];
        for (int t = 0; t < threadCount; t++) {
            threads[t] = new Thread(() -> {
                for (int i = 0; i < incrementsPerThread; i++) {
                    value[0]++;
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        int expected = threadCount * incrementsPerThread;
        System.out.println("Resultat obtenu : " + value[0] + " (attendu : " + expected + ")");
        if (value[0] == expected) {
            System.out.println("Pas de corruption visible CETTE FOIS - relance main() plusieurs fois,"
                    + " le hasard du minutage fera parfois apparaitre un resultat plus petit que " + expected + ".");
        } else {
            System.out.println("Race condition observee : des incrementations ont ete perdues.");
        }
    }
}
