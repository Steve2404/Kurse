package ch5_methods.projects.p04_tournament.solution;

import ch5_methods.projects.p04_tournament.Data;

import java.util.Arrays;

/**
 * SOLUTION du projet 4 - passage par valeur, autoboxing, et un tournoi.
 */
public class Tournament {

    // ------------------------------------------------------------ 1. passage par valeur
    // Java copie TOUJOURS l'argument : la valeur d'un primitif, ou la valeur d'une REFERENCE.
    static void swap(int a, int b) {
        int t = a;
        a = b;
        b = t;
    }

    static void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    static void touch(StringBuilder x, StringBuilder y) {
        x.append('!');               // modifie l'OBJET partage : visible dehors
        y = new StringBuilder("??");  // change seulement la copie locale de la reference
        y.append("perdu");
    }

    static void reassign(int[] arr) {
        arr = new int[] {9, 9, 9};    // l'appelant garde son ancien tableau
        arr[0] = 0;
    }

    static void mutate(int[] arr) {
        arr[0] *= 10;
    }

    static void tryChange(String s, Integer n) {
        s += "!";                      // un String est immuable : += cree un nouvel objet local
        n++;                           // Integer aussi : n++ = unboxing, +1, boxing d'un NOUVEL Integer local
    }

    static int increment(int n) {
        return n + 1;                  // la bonne facon : RETOURNER la nouvelle valeur
    }

    // ------------------------------------------------------------ 2. permutations par echanges
    private static int permCount;

    // Retour arriere : on fixe la position k par echange, on recurse, puis on DEFAIT l'echange.
    static void permute(char[] c, int k, StringBuilder out) {
        if (k == c.length) {
            out.append(' ').append(c);
            permCount++;
            return;
        }
        for (int i = k; i < c.length; i++) {
            swap(c, k, i);
            permute(c, k + 1, out);
            swap(c, k, i);
        }
    }

    static void swap(char[] c, int i, int j) {
        char t = c[i];
        c[i] = c[j];
        c[j] = t;
    }

    // Permutation suivante dans l'ordre lexicographique (en place). false s'il n'y en a plus.
    static boolean next(char[] c) {
        int i = c.length - 2;
        while (i >= 0 && c[i] >= c[i + 1]) {
            i--;
        }
        if (i < 0) {
            return false;
        }
        int j = c.length - 1;
        while (c[j] <= c[i]) {
            j--;
        }
        swap(c, i, j);
        for (int l = i + 1, r = c.length - 1; l < r; l++, r--) {
            swap(c, l, r);
        }
        return true;
    }

    // Le rang (a partir de 1) sans tout enumerer : on compte les lettres plus petites restantes, fois (n-1)!.
    static int rank(String word) {
        int r = 0;
        for (int i = 0; i < word.length(); i++) {
            int smaller = 0;
            for (int j = i + 1; j < word.length(); j++) {
                if (word.charAt(j) < word.charAt(i)) {
                    smaller++;
                }
            }
            r += smaller * factorial(word.length() - 1 - i);
        }
        return r + 1;
    }

    static int factorial(int n) {
        return n <= 1 ? 1 : n * factorial(n - 1);
    }

    // ------------------------------------------------------------ 3. tournoi
    // void + tableaux recus en parametre : la methode remplit les tableaux de l'appelant.
    static void record(int[] points, int[] diff, int home, int away, int hg, int ag) {
        diff[home] += hg - ag;
        diff[away] += ag - hg;
        if (hg > ag) {
            points[home] += 3;
        } else if (hg < ag) {
            points[away] += 3;
        } else {
            points[home]++;
            points[away]++;
        }
    }

    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        swap(a, b);
        int[] pair = {1, 2};
        swap(pair, 0, 1);
        System.out.println("echange : primitifs a=" + a + " b=" + b + ", tableau " + Arrays.toString(pair));
        StringBuilder sb1 = new StringBuilder("Lions");
        StringBuilder sb2 = new StringBuilder("Ours");
        touch(sb1, sb2);
        int[] numbers = {1, 2, 3};
        reassign(numbers);
        String before = Arrays.toString(numbers);
        mutate(numbers);
        System.out.println("references : " + sb1 + " " + sb2 + ", reassigne " + before + ", modifie " + Arrays.toString(numbers));
        String s = "Lions";
        Integer n = 5;
        tryChange(s, n);
        int counter = 5;
        increment(counter);
        int returned = increment(counter);
        System.out.println("immuables : " + s + " " + n + ", resultat ignore " + counter + ", resultat garde " + returned);

        // Autoboxing : Integer.valueOf garde en cache les valeurs de -128 a 127.
        Integer small1 = 127;
        Integer small2 = 127;
        Integer big1 = 128;
        Integer big2 = 128;
        Long five = 5L;
        System.out.println("cache Integer : 127 " + (small1 == small2) + ", 128 " + (big1 == big2) + ", equals " + big1.equals(big2)
                + ", Long.equals(5) " + five.equals(5) + ", 5L == 5 " + (five == 5) + ", compare " + (big1 < 200));
        Integer[] boxed = {3, null, 4};
        int total = 0;
        for (Integer x : boxed) {
            if (x != null) {           // sans ce test : NullPointerException a l'unboxing
                total += x;
            }
        }
        Character letter = 'A';
        char next = (char) (letter + 1);
        System.out.println("unboxing : total " + total + ", " + letter + next + ", Integer.valueOf(\"42\") + 1 = " + (Integer.valueOf("42") + 1)
                + ", Double " + Double.valueOf(5));

        char[] word = Data.WORD.toCharArray();
        StringBuilder perms = new StringBuilder();
        permute(Data.WORD.substring(0, 3).toCharArray(), 0, perms);
        System.out.println("permutations par echanges :" + perms + " (" + permCount + ")");
        StringBuilder lexical = new StringBuilder();
        int position = 0;
        int found = 0;
        do {
            position++;
            lexical.append(position <= 6 ? " " + new String(word) : "");
            if (new String(word).equals(Data.TARGET)) {
                found = position;
            }
        } while (next(word));
        System.out.println("ordre lexicographique :" + lexical + " ... " + new String(word) + " (" + position + " au total)");
        System.out.println("rang de " + Data.TARGET + " : par enumeration " + found + ", par calcul " + rank(Data.TARGET) + ", le tableau est reste sur "
                + new String(word));

        // Calendrier « toutes rondes » (methode du cercle) : l'equipe 0 reste fixe, les autres tournent.
        String[] teams = Data.TEAMS;
        int t = teams.length;
        int[] ring = new int[t];
        for (int i = 0; i < t; i++) {
            ring[i] = i;
        }
        int[] points = new int[t];
        int[] diff = new int[t];
        for (int round = 1; round < t; round++) {
            StringBuilder line = new StringBuilder("J" + round + " :");
            for (int m = 0; m < t / 2; m++) {
                int home = ring[m];
                int away = ring[t - 1 - m];
                int hg = (teams[home].length() + round) % 4;
                int ag = (home * 2 + away + round) % 3;
                record(points, diff, home, away, hg, ag);
                line.append(' ').append(teams[home]).append(' ').append(hg).append('-').append(ag).append(' ').append(teams[away]).append(m < t / 2 - 1 ? "," : "");
            }
            System.out.println(line);
            // Rotation de ring[1..t-1] d'un cran vers la droite.
            int last = ring[t - 1];
            System.arraycopy(ring, 1, ring, 2, t - 2);
            ring[1] = last;
        }
        // Classement : tri par insertion sur des INDICES (points, puis difference, puis nom).
        int[] order = new int[t];
        for (int i = 0; i < t; i++) {
            order[i] = i;
        }
        for (int i = 1; i < t; i++) {
            int key = order[i];
            int j = i - 1;
            while (j >= 0 && before(key, order[j], points, diff, teams)) {
                order[j + 1] = order[j];
                j--;
            }
            order[j + 1] = key;
        }
        StringBuilder table = new StringBuilder("classement :");
        for (int i = 0; i < t; i++) {
            int k = order[i];
            table.append(' ').append(i + 1).append('.').append(teams[k]).append(' ').append(points[k]).append("pts(").append(diff[k] > 0 ? "+" : "")
                    .append(diff[k]).append(')');
        }
        System.out.println(table);
    }

    static boolean before(int x, int y, int[] points, int[] diff, String[] teams) {
        if (points[x] != points[y]) {
            return points[x] > points[y];
        }
        if (diff[x] != diff[y]) {
            return diff[x] > diff[y];
        }
        return teams[x].compareTo(teams[y]) < 0;
    }
}
