package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise09_VendingMachine.
 */
public class Solution09_VendingMachine {

    public enum State { IDLE, READY, SERVING }

    public static State next(State state, String event) {
        // Switch exterieur sur l'enum (exhaustif, sans default) ; switch interieurs sur des String
        // (default obligatoire : un String a une infinite de valeurs possibles).
        return switch (state) {
            case IDLE -> switch (event) {
                case "coin" -> State.READY;
                default -> State.IDLE;
            };
            case READY -> switch (event) {
                case "coin" -> State.READY;
                case "refund" -> State.IDLE;
                case "buy" -> State.SERVING;
                default -> State.READY;
            };
            case SERVING -> switch (event) {
                case "done" -> State.IDLE;
                default -> State.SERVING;
            };
        };
    }

    public static String screen(State state) {
        // Enum complet : aucun default necessaire.
        return switch (state) {
            case IDLE -> "Inserez une piece";
            case READY -> "Choisissez";
            case SERVING -> "Servez-vous";
        };
    }

    public static String run(String[] events) {
        // La trace commence par l'etat initial, puis chaque etat traverse.
        State state = State.IDLE;
        String trace = "IDLE";
        for (String event : events) {
            state = next(state, event);
            trace += ">" + state;
        }
        return trace;
    }

    public static int coinsBeforeFirstPurchase(String[] events) {
        // Un simple if + break sort de la BOUCLE (un break dans un switch ne sortirait que du switch).
        State state = State.IDLE;
        int coins = 0;
        for (String event : events) {
            if (event.equals("coin")) {
                coins++;
            }
            state = next(state, event);
            if (state == State.SERVING) {
                break;
            }
        }
        return coins;
    }
}
