package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 9 - Un distributeur de boissons : une machine a etats pilotee par des switch (niveau : difficile)
 * ========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_IfElseBasics.java.
 *
 * -- Le contexte --
 *
 * Le distributeur est toujours dans UN etat, et chaque evenement
 * (piece, achat, remboursement...) le fait passer dans un autre :
 *
 *   IDLE    (attend)       : "coin" -> READY ; tout le reste -> IDLE
 *   READY   (a de l'argent): "coin" -> READY ; "refund" -> IDLE ;
 *                            "buy" -> SERVING ; tout le reste -> READY
 *   SERVING (sert)         : "done" -> IDLE ; tout le reste -> SERVING
 *
 * C'est l'usage ideal du switch expression : un switch sur l'ETAT,
 * et dans chaque case, un switch sur l'EVENEMENT. Comme State est un
 * enum et que ses 3 valeurs sont listees, le switch exterieur n'a pas
 * besoin de default ; les switch interieurs portent sur des String,
 * donc ils en ont besoin.
 *
 *
 * ==================================================================
 * TODO 1 : next(state, event)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On regarde d'abord dans quelle piece on est (l'etat), puis quelle
 * porte on pousse (l'evenement) : ca dit dans quelle piece on arrive.
 *
 * -- Essayons a la main --
 *
 *   (IDLE, "coin") -> READY ; (IDLE, "buy") -> IDLE ; (READY, "buy") -> SERVING ;
 *   (READY, "refund") -> IDLE ; (SERVING, "coin") -> SERVING ; (SERVING, "done") -> IDLE
 *
 * -- Le plan --
 *
 *   1. return switch (state) { case IDLE -> switch (event) {...}; case READY -> ...; case SERVING -> ...; };
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est la boite magique principale.
 *
 *
 * ==================================================================
 * TODO 2 : screen(state)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   IDLE -> "Inserez une piece" ; READY -> "Choisissez" ; SERVING -> "Servez-vous"
 *
 * -- Le plan --
 *
 *   1. Un switch expression sur l'enum, sans default.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : run(events)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On part de IDLE et on joue les evenements un par un, en notant
 * chaque etat traverse.
 *
 * -- Essayons a la main --
 *
 *   ["coin", "coin", "buy", "done", "refund"]
 *   IDLE -> READY -> READY -> SERVING -> IDLE -> IDLE
 *   -> "IDLE>READY>READY>SERVING>IDLE>IDLE"
 *
 * -- Le plan --
 *
 *   1. state = IDLE ; trace = "IDLE".
 *   2. Pour chaque evenement : state = next(state, event) ; trace += ">" + state.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : next (TODO 1).
 *
 *
 * ==================================================================
 * TODO 4 : coinsBeforeFirstPurchase(events)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Combien de pieces ont ete mises avant le premier achat reussi ? On
 * rejoue les evenements, on compte les "coin", et on ARRETE la boucle
 * des que la machine passe en SERVING. Un switch statement (sur le
 * nouvel etat) plus un break... attention : un break dans un switch ne
 * sort que du switch ! Ici, un simple if suffit pour le break de boucle.
 *
 * -- Essayons a la main --
 *
 *   ["buy", "coin", "coin", "buy", "coin"] -> "buy" ignore (IDLE),
 *   2 pieces, "buy" -> SERVING -> stop -> 2
 *   ["coin", "refund"] -> jamais de SERVING -> 1
 *
 * -- Le plan --
 *
 *   1. state = IDLE ; coins = 0.
 *   2. Pour chaque evenement : si "coin", coins++ ; state = next(...) ;
 *      si state == SERVING, sortir de la boucle.
 *   3. Rendre coins.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : next.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - case IDLE -> switch (event) { case "coin" -> State.READY; default -> State.IDLE; };
 *   - un switch expression peut etre imbrique dans un autre (il a son propre ;)
 */
public class Exercise09_VendingMachine {

    public enum State { IDLE, READY, SERVING }

    public static State next(State state, String event) {
        throw new UnsupportedOperationException("TODO 1 : implementer next()");
    }

    public static String screen(State state) {
        throw new UnsupportedOperationException("TODO 2 : implementer screen()");
    }

    public static String run(String[] events) {
        throw new UnsupportedOperationException("TODO 3 : implementer run()");
    }

    public static int coinsBeforeFirstPurchase(String[] events) {
        throw new UnsupportedOperationException("TODO 4 : implementer coinsBeforeFirstPurchase()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 transitions depuis IDLE", next(State.IDLE, "coin") == State.READY && next(State.IDLE, "buy") == State.IDLE);
        ExerciseChecker.check("1 transitions depuis READY", next(State.READY, "buy") == State.SERVING
                && next(State.READY, "refund") == State.IDLE && next(State.READY, "coin") == State.READY
                && next(State.READY, "xyz") == State.READY);
        ExerciseChecker.check("1 transitions depuis SERVING", next(State.SERVING, "coin") == State.SERVING
                && next(State.SERVING, "done") == State.IDLE);
        ExerciseChecker.check("2 screen", screen(State.IDLE).equals("Inserez une piece") && screen(State.READY).equals("Choisissez")
                && screen(State.SERVING).equals("Servez-vous"));
        ExerciseChecker.check("3 run == IDLE>READY>READY>SERVING>IDLE>IDLE",
                run(new String[]{"coin", "coin", "buy", "done", "refund"}).equals("IDLE>READY>READY>SERVING>IDLE>IDLE"));
        ExerciseChecker.check("3 run([]) == IDLE", run(new String[0]).equals("IDLE"));
        ExerciseChecker.check("4 coinsBeforeFirstPurchase : 2 puis 1",
                coinsBeforeFirstPurchase(new String[]{"buy", "coin", "coin", "buy", "coin"}) == 2
                        && coinsBeforeFirstPurchase(new String[]{"coin", "refund"}) == 1);

        ExerciseChecker.summary();
    }
}
