package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise17_RobotCapstone.
 */
public class Solution17_RobotCapstone {

    public static final String[] MAP = {"S..#", ".#..", "...T"};

    public static int[] findCell(String[] grid, char target) {
        // break search quitte les 2 boucles des la premiere case trouvee.
        int[] found = null;
        search:
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length(); col++) {
                if (grid[row].charAt(col) == target) {
                    found = new int[]{row, col};
                    break search;
                }
            }
        }
        return found;
    }

    public static boolean isFree(String[] grid, int row, int col) {
        // Les bornes AVANT charAt : && court-circuite et evite StringIndexOutOfBoundsException.
        return row >= 0 && row < grid.length && col >= 0 && col < grid[row].length()
                && grid[row].charAt(col) != '#';
    }

    public static int[] step(String[] grid, int[] position, String direction) {
        // switch expression : une direction inconnue lance une exception au lieu de rendre une valeur.
        int dRow = switch (direction) {
            case "N" -> -1;
            case "S" -> 1;
            case "E", "W" -> 0;
            default -> throw new IllegalArgumentException("direction inconnue : " + direction);
        };
        int dCol = switch (direction) {
            case "E" -> 1;
            case "W" -> -1;
            default -> 0;
        };
        int row = position[0] + dRow;
        int col = position[1] + dCol;
        return isFree(grid, row, col) ? new int[]{row, col} : position;
    }

    public static String run(String[] grid, Object[] commands) {
        // Pattern matching pour trier les commandes, continue pour ignorer les autres,
        // break commands pour tout arreter sur le tresor, meme au milieu d'une repetition.
        int[] position = findCell(grid, 'S');
        String trace = label(position);
        String last = null;
        boolean found = false;
        commands:
        for (Object command : commands) {
            int repetitions;
            if (command instanceof String dir) {
                last = dir;
                repetitions = 1;
            } else if (command instanceof Integer n && last != null) {
                repetitions = n;
            } else {
                continue;
            }
            for (int i = 0; i < repetitions; i++) {
                position = step(grid, position, last);
                trace += ">" + label(position);
                if (grid[position[0]].charAt(position[1]) == 'T') {
                    found = true;
                    break commands;
                }
            }
        }
        return trace + (found ? ";trouve" : ";perdu");
    }

    private static String label(int[] position) {
        // Petite boite magique : la position en texte revient a chaque pas.
        return position[0] + "," + position[1];
    }

    public static int countWalls(String[] grid) {
        // for-each imbriques : les lignes, puis les caracteres de chaque ligne.
        int walls = 0;
        for (String line : grid) {
            for (char c : line.toCharArray()) {
                if (c == '#') {
                    walls++;
                }
            }
        }
        return walls;
    }
}
