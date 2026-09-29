package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise16_LabelsAndJumps.
 */
public class Solution16_LabelsAndJumps {

    public static int sumUntilStop(String[] commands) {
        // Dans un switch, "break;" ne sort que du switch : il faut "break loop;" pour sortir de la boucle.
        // continue, lui, ne concerne jamais un switch : il passe au tour de boucle suivant.
        int total = 0;
        loop:
        for (String command : commands) {
            switch (command) {
                case "stop":
                    break loop;
                case "skip":
                    continue;
                default:
                    total += Integer.parseInt(command);
            }
        }
        return total;
    }

    public static int rowsWithoutNegatives(int[][] grid) {
        // continue outer abandonne toute la ligne ; un continue simple passerait seulement a la case suivante.
        int count = 0;
        outer:
        for (int[] row : grid) {
            for (int value : row) {
                if (value < 0) {
                    continue outer;
                }
            }
            count++;
        }
        return count;
    }

    public static int unchangedWithoutMatch(int x) {
        // Aucun case ne correspond et pas de default : le switch ne fait rien, sans erreur.
        int before = 5;
        switch (x) {
            case 1:
                before = 100;
                break;
        }
        return before;
    }

    public static String firstPairSummingTo(int[] values, int target) {
        // break search quitte les DEUX boucles des la premiere paire trouvee.
        String result = "aucune";
        search:
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if (values[i] + values[j] == target) {
                    result = i + "," + j;
                    break search;
                }
            }
        }
        return result;
    }

    public static String checkSign(int x) {
        // Un bloc etiquete accepte break (sortie anticipee), jamais continue (pas une boucle).
        String result = "negatif";
        check:
        {
            if (x < 0) {
                break check;
            }
            result = "zero";
            if (x == 0) {
                break check;
            }
            result = "positif";
        }
        return result;
    }
}
