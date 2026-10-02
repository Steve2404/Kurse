package ch3_makingdecisions.projects.p03_drawing.solution;

/**
 * SOLUTION du projet 3 - une conception possible.
 * Les espaces de FIN de ligne ne comptent pas pour Check : seuls ceux de tete et du milieu comptent.
 */
public class Drawing {

    // String.repeat arrive au chapitre 4 : on construit la repetition avec une boucle.
    static String repeat(char c, int times) {
        String s = "";
        for (int i = 0; i < times; i++) {
            s = s + c;
        }
        return s;
    }

    static int digits(int n) {
        int count = 1;
        while (n >= 10) {
            n /= 10;
            count++;
        }
        return count;
    }

    // Alignement a droite sur une largeur fixe, sans String.format.
    static String padLeft(int value, int width) {
        return repeat(' ', width - digits(value)) + value;
    }

    static void pyramid(int n) {
        System.out.println("-- pyramide --");
        for (int row = 1; row <= n; row++) {
            System.out.println(repeat(' ', n - row) + repeat('*', 2 * row - 1));
        }
    }

    static void hollowDiamond(int n) {
        System.out.println("-- losange creux --");
        for (int row = -(n - 1); row <= n - 1; row++) {
            int half = n - 1 - (row < 0 ? -row : row);
            String line = repeat(' ', n - 1 - half) + "*";
            if (half > 0) {
                line = line + repeat(' ', 2 * half - 1) + "*";
            }
            System.out.println(line);
        }
    }

    static void checkerboard(int n) {
        System.out.println("-- damier --");
        for (int r = 0; r < n; r++) {
            String line = "";
            for (int c = 0; c < 2 * n; c++) {
                line = line + ((r + c) % 2 == 0 ? '#' : '.');
            }
            System.out.println(line);
        }
    }

    static void cross(int n) {
        System.out.println("-- croix --");
        for (int r = 0; r < n; r++) {
            String line = "";
            for (int c = 0; c < n; c++) {
                if (c != r && c != n - 1 - r) {
                    line = line + ' ';
                    continue;   // rien d'autre a faire pour cette case
                }
                line = line + (c == r && c == n - 1 - r ? '+' : c == r ? '\\' : '/');
            }
            System.out.println(line);
        }
    }

    static void table(int n) {
        System.out.println("-- table --");
        String header = "   |";
        for (int c = 1; c <= n; c++) {
            header = header + padLeft(c, 4);
        }
        System.out.println(header);
        System.out.println("---+" + repeat('-', 4 * n));
        for (int r = 1; r <= n; r++) {
            String line = padLeft(r, 2) + " |";
            for (int c = 1; c <= n; c++) {
                line = line + padLeft(r * c, 4);
            }
            System.out.println(line);
        }
    }

    // C(r, k+1) = C(r, k) * (r - k) / (k + 1) : chaque ligne se calcule sans tableau.
    static void pascal(int n) {
        System.out.println("-- pascal --");
        for (int r = 0; r < n; r++) {
            String line = repeat(' ', 2 * (n - 1 - r));
            long value = 1;
            for (int k = 0; k <= r; k++) {
                line = line + padLeft((int) value, 4);
                value = value * (r - k) / (k + 1);
            }
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        pyramid(n);
        hollowDiamond(n);
        checkerboard(n);
        cross(n);
        table(n);
        pascal(n + 1);
    }
}
