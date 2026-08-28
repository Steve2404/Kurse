package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.util.function.LongSupplier;

/**
 * EXERCICE 11 - Anti-rebond (throttle) d'une action, avec une horloge injectee (niveau : difficile)
 * ===========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un bouton d'ascenseur : si plusieurs personnes appuient
 * dessus PLUSIEURS FOIS de suite en quelques secondes, l'ascenseur ne
 * doit pas repartir a chaque appui - une seule fois suffit, et il faut
 * attendre un petit temps de repos ("cooldown") avant qu'un NOUVEL
 * appui redeclenche vraiment quelque chose. Les appuis trop
 * rapprochés sont juste ignores.
 *
 * -- Pourquoi une horloge INJECTEE, et pas System.currentTimeMillis() --
 *
 * Un test ne doit JAMAIS avoir besoin de vraiment attendre (Thread.sleep)
 * pour verifier un comportement lie au temps - ce serait lent et
 * fragile. A la place, on donne a notre fonction une "horloge"
 * (LongSupplier : une recette qui rend l'heure actuelle, en millisecondes,
 * SANS ingredient) que le test peut totalement controler - une horloge
 * FICTIVE qu'on avance a la main, exactement quand on le veut.
 *
 *
 * ==================================================================
 * TODO : buildThrottledAction(action, cooldownMillis, clock)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * cooldown = 100. Horloge fictive a 0 au depart.
 *
 *   horloge=0   : throttled.run() -> aucun appel precedent -> EXECUTE.
 *   horloge=50  : throttled.run() -> seulement 50 depuis le dernier
 *                 appel EXECUTE (0), et 50 < 100 -> IGNORE.
 *   horloge=150 : throttled.run() -> 150 depuis le dernier appel
 *                 EXECUTE (0), et 150 >= 100 -> EXECUTE.
 *   horloge=200 : throttled.run() -> seulement 50 depuis le dernier
 *                 appel EXECUTE (150) -> IGNORE.
 *
 * -- Le plan --
 *
 *   1. Fabriquer DEUX "boites aux lettres" : un booleen "a-t-on deja
 *      execute au moins une fois ?" (au debut : non), et l'heure du
 *      DERNIER appel reellement EXECUTE.
 *   2. Renvoyer un nouveau Runnable qui, a CHAQUE run() :
 *      a. lit l'heure actuelle via clock.getAsLong() ;
 *      b. si on n'a JAMAIS encore execute, OU SI (heure actuelle -
 *         derniere heure executee) >= cooldownMillis : execute
 *         VRAIMENT action.run(), et MET A JOUR les deux boites
 *         (marquer "execute au moins une fois" + l'heure actuelle) ;
 *      c. sinon, ne fait RIEN DU TOUT (l'appel est silencieusement
 *         ignore).
 *
 * -- Piege a eviter --
 *
 * On pourrait etre tente d'initialiser "la derniere heure executee" a
 * Long.MIN_VALUE pour que le premier appel passe toujours (une
 * soustraction avec une horloge normale donnerait un tres grand
 * nombre, forcement >= cooldownMillis). MAIS "heure actuelle -
 * Long.MIN_VALUE" DEBORDE (overflow) en arithmetique long : le
 * resultat repasse par surprise a une valeur TRES NEGATIVE, et le tout
 * premier appel serait alors ignore au lieu d'etre execute ! D'ou le
 * booleen separe a l'etape 1, plus simple et surtout plus sur.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : c'est un seul Runnable, avec une seule "boite aux lettres"
 * (comme le tableau a une case de l'Exercise07/10) pour se souvenir de
 * la derniere execution entre deux appels.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - boolean[] hasRun = {false};
 *     long[] lastRun = {0L};
 *   - return () -> {
 *         long now = clock.getAsLong();
 *         if (!hasRun[0] || now - lastRun[0] >= cooldownMillis) {
 *             action.run();
 *             lastRun[0] = now;
 *             hasRun[0] = true;
 *         }
 *     };
 *   - LongSupplier est la version "primitive" de Supplier<Long> (voir
 *     Exercise06) : sa methode s'appelle getAsLong(), pas get().
 */
public class Exercise11_ThrottledAction {

    public static Runnable buildThrottledAction(Runnable action, long cooldownMillis, LongSupplier clock) {
        throw new UnsupportedOperationException("TODO : implementer buildThrottledAction()");
    }

    public static void main(String[] args) {
        long[] currentTime = {0L};
        LongSupplier fakeClock = () -> currentTime[0];

        int[] runCount = {0};
        Runnable action = () -> runCount[0]++;

        Runnable throttled = buildThrottledAction(action, 100, fakeClock);

        throttled.run();
        ExerciseChecker.check("1er appel (t=0) : toujours execute", runCount[0] == 1);

        currentTime[0] = 50;
        throttled.run();
        ExerciseChecker.check("t=50, trop tot depuis t=0 (< 100) : ignore", runCount[0] == 1);

        currentTime[0] = 150;
        throttled.run();
        ExerciseChecker.check("t=150, 150ms depuis t=0 (>= 100) : execute", runCount[0] == 2);

        currentTime[0] = 200;
        throttled.run();
        ExerciseChecker.check("t=200, seulement 50ms depuis t=150 : ignore", runCount[0] == 2);

        currentTime[0] = 260;
        throttled.run();
        ExerciseChecker.check("t=260, 110ms depuis t=150 (>= 100) : execute", runCount[0] == 3);

        ExerciseChecker.summary();
    }
}
