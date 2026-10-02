package ch3_makingdecisions.projects.p05_robot.solution;

/**
 * SOLUTION du projet 5 (capstone) - une conception possible.
 * Un robot sur une grille, pilote par des commandes "direction pas" lues dans args.
 */
public class Robot {

    static int width;
    static int height;
    static int x;
    static int y;
    static int battery;
    static int travelled;

    // Les obstacles ne sont pas stockes (pas de tableau au chapitre 3) : une FORMULE dit si une case est bloquee.
    static boolean obstacle(int cx, int cy) {
        return (cx * 3 + cy * 5) % 11 == 0 && !(cx == 0 && cy == 0);
    }

    static int dx(String dir) {
        return switch (dir) {
            case "E" -> 1;
            case "O" -> -1;
            default -> 0;
        };
    }

    static int dy(String dir) {
        return switch (dir) {
            case "N" -> 1;
            case "S" -> -1;
            default -> 0;
        };
    }

    static void map() {
        for (int row = height - 1; row >= 0; row--) {
            String line = row + " ";
            for (int col = 0; col < width; col++) {
                char cell;
                if (col == x && row == y) {
                    cell = 'R';
                } else if (col == 0 && row == 0) {
                    cell = 'S';
                } else if (obstacle(col, row)) {
                    cell = '#';
                } else {
                    cell = '.';
                }
                line = line + cell;
            }
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        width = Integer.parseInt(args[0]);
        height = Integer.parseInt(args[1]);
        battery = Integer.parseInt(args[2]);
        int order = 0;

        // Deux etiquettes : continue commands = commande suivante ; break commands = arret total.
        commands:
        for (int i = 3; i < args.length; i += 2) {
            String dir = args[i];
            int steps = Integer.parseInt(args[i + 1]);
            order++;
            if (dx(dir) == 0 && dy(dir) == 0) {
                System.out.println(order + ". " + dir + steps + " : direction inconnue, ignoree");
                continue;
            }
            int done = 0;
            while (done < steps) {
                if (battery == 0) {
                    System.out.println(order + ". " + dir + steps + " : batterie vide en (" + x + "," + y + ") apres " + done + " pas");
                    break commands;
                }
                int nx = x + dx(dir);
                int ny = y + dy(dir);
                if (nx < 0 || ny < 0 || nx >= width || ny >= height) {
                    System.out.println(order + ". " + dir + steps + " : mur atteint en (" + x + "," + y + ") apres " + done + " pas");
                    continue commands;
                }
                if (obstacle(nx, ny)) {
                    System.out.println(order + ". " + dir + steps + " : obstacle en (" + nx + "," + ny + "), arret en (" + x + "," + y + ") apres " + done + " pas");
                    continue commands;
                }
                x = nx;
                y = ny;
                battery--;
                travelled++;
                done++;
            }
            System.out.println(order + ". " + dir + steps + " : arrive en (" + x + "," + y + "), batterie " + battery);
        }
        int manhattan = x + y;
        System.out.println("BILAN : " + travelled + " pas parcourus, position (" + x + "," + y + "), distance au depart " + manhattan
                + ", batterie " + battery);
        map();
    }
}
