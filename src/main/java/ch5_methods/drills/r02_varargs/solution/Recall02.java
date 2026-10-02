package ch5_methods.drills.r02_varargs.solution;

/**
 * SOLUTION du drill de rappel 2 - varargs.
 */
public class Recall02 {

    static int count(int... v) {
        return v == null ? -1 : v.length;
    }

    // Le varargs est TOUJOURS le dernier parametre, et il n'y en a qu'un.
    static String label(String prefix, int... v) {
        StringBuilder sb = new StringBuilder(prefix);
        for (int x : v) {
            sb.append(x);
        }
        return sb.toString();
    }

    static int first(int... v) {
        return v.length == 0 ? 0 : v[0];
    }

    static int last(int... v) {
        return v[v.length - 1];
    }

    static String kinds(Object... items) {
        if (items == null) {
            return "null";
        }
        return String.valueOf(items.length);
    }

    static int rows(int[]... grid) {
        return grid.length;
    }

    static void change(int... v) {
        v[0] = 99;   // un vrai tableau : si l'appelant a passe SON tableau, il voit le changement
    }

    static String joined(char sep, String... words) {
        return String.join(String.valueOf(sep), words);
    }

    public static void main(String[] args) {
        int[] data = {4, 5, 6};
        System.out.println("D01 : " + count() + " " + count(7) + " " + count(1, 2, 3) + " " + count(data) + " " + count((int[]) null));
        System.out.println("D02 : " + label("x") + " " + label("x", 1) + " " + label("y", 1, 2, 3));
        System.out.println("D03 : " + first() + " " + first(8, 9) + " " + last(data) + " " + last(5));
        System.out.println("D04 : " + kinds() + " " + kinds("a", 1) + " " + kinds((Object) null) + " " + kinds((Object[]) null) + " "
                + kinds((Object) new String[] {"a", "b"}) + " " + kinds((Object[]) new String[] {"a", "b"}));
        System.out.println("D05 : " + rows() + " " + rows(new int[] {1}, new int[] {2, 3}) + " " + rows(new int[][] {{1}, {2}, {3}}));
        change(data);
        int[] other = {1, 2};
        change(other[0], other[1]);
        System.out.println("D06 : " + data[0] + " " + other[0]);
        System.out.println("D07 : " + joined('-', "a", "b", "c") + " [" + joined('-') + "]");
        System.out.println("D08 : " + count(new int[0]) + " " + count(new int[] {}) + " " + label("z", new int[] {7, 8}));
    }
}
