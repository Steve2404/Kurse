package ch4_coreapis.projects.p03_life.solution;

import ch4_coreapis.projects.p03_life.Data;

import java.util.Arrays;

/**
 * SOLUTION du projet 3 - une conception possible.
 */
public class Life {

    static boolean[][] parse(String[] rows) {
        // Tableau 2D : d'abord le nombre de lignes, puis chaque ligne a sa longueur (elles sont toutes egales ici).
        boolean[][] grid = new boolean[rows.length][rows[0].length()];
        for (int r = 0; r < rows.length; r++) {
            for (int c = 0; c < rows[r].length(); c++) {
                grid[r][c] = rows[r].charAt(c) == '#';
            }
        }
        return grid;
    }

    static int neighbours(boolean[][] grid, int r, int c) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nr = r + dr;
                int nc = c + dc;
                // On saute la cellule elle-meme et tout ce qui sort de la grille (les bords sont morts).
                if ((dr == 0 && dc == 0) || nr < 0 || nc < 0 || nr >= grid.length || nc >= grid[nr].length) {
                    continue;
                }
                if (grid[nr][nc]) {
                    count++;
                }
            }
        }
        return count;
    }

    // Piege : calculer DANS la meme grille fausserait les voisins des cellules suivantes -> nouvelle grille.
    static boolean[][] next(boolean[][] grid) {
        boolean[][] result = new boolean[grid.length][grid[0].length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                int n = neighbours(grid, r, c);
                result[r][c] = grid[r][c] ? n == 2 || n == 3 : n == 3;
            }
        }
        return result;
    }

    static int alive(boolean[][] grid) {
        int count = 0;
        for (boolean[] row : grid) {
            for (boolean cell : row) {
                count += cell ? 1 : 0;
            }
        }
        return count;
    }

    static void print(String title, boolean[][] grid) {
        System.out.println(title + " (vivantes : " + alive(grid) + ")");
        for (boolean[] row : grid) {
            String line = "";
            for (boolean cell : row) {
                line += cell ? '#' : '.';
            }
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        boolean[][] world = parse(Data.WORLD);
        print("generation 0", world);
        for (int g = 1; g <= Data.GENERATIONS; g++) {
            world = next(world);
            print("generation " + g, world);
        }

        boolean[][] blinker = parse(Data.BLINKER);
        boolean[][] once = next(blinker);
        boolean[][] twice = next(once);
        // equals sur un tableau compare les REFERENCES ; Arrays.equals compare un niveau ; deepEquals descend dans les sous-tableaux.
        System.out.println("clignotant : 1 etape identique " + Arrays.deepEquals(blinker, once) + ", 2 etapes identique "
                + Arrays.deepEquals(blinker, twice) + ", Arrays.equals(lignes) " + Arrays.equals(blinker, twice)
                + ", equals " + blinker.equals(twice));
        System.out.println("ligne 2 apres 1 etape : " + Arrays.toString(once[2]) + ", dimensions " + once.length + "x" + once[0].length);
    }
}
