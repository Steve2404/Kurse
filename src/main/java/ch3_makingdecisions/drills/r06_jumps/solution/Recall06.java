package ch3_makingdecisions.drills.r06_jumps.solution;

/**
 * SOLUTION du drill de rappel 6 - break, continue, etiquettes, return.
 */
public class Recall06 {

    static int firstMultiple(int of, int above) {
        for (int i = above + 1; ; i++) {
            if (i % of == 0) {
                return i;
            }
        }
    }

    public static void main(String[] args) {
        String s = "";
        for (int i = 0; i < 10; i++) {
            if (i == 6) {
                break;
            }
            if (i % 2 == 0) {
                continue;
            }
            s = s + i;
        }
        System.out.println("D01 : " + s);
        String pairs = "";
        outer:
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (j == 2) {
                    continue outer;
                }
                pairs = pairs + i + j + " ";
            }
        }
        System.out.println("D02 : " + pairs);
        String found = "";
        search:
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                if (i * j == 12) {
                    found = i + "x" + j;
                    break search;
                }
            }
        }
        System.out.println("D03 : " + found);
        int n = 0;
        while (true) {
            n++;
            if (n * n > 50) {
                break;
            }
        }
        System.out.println("D04 : " + n);
        System.out.println("D05 : " + firstMultiple(7, 30) + " " + firstMultiple(5, 5));
        int skipped = 0;
        int i = 0;
        do {
            i++;
            if (i % 3 != 0) {
                continue;   // dans un do/while, continue saute a la CONDITION
            }
            skipped++;
        } while (i < 10);
        System.out.println("D06 : " + skipped + " " + i);
        String trace = "";
        loop:
        for (int a = 0; a < 3; a++) {
            switch (a) {
                case 1:
                    trace = trace + "b";
                    break;          // sort du switch, pas de la boucle
                case 2:
                    trace = trace + "c";
                    break loop;     // sort de la boucle
                default:
                    trace = trace + "a";
            }
            trace = trace + ".";
        }
        System.out.println("D07 : " + trace);
        int total = 0;
        rows:
        for (int r = 0; r < 4; r++) {
            int c = 0;
            while (c < 4) {
                c++;
                if (c > r) {
                    continue rows;
                }
                total += c;
            }
        }
        System.out.println("D08 : " + total);
    }
}
