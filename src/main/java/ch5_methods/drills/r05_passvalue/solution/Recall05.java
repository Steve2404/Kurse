package ch5_methods.drills.r05_passvalue.solution;

import java.util.Arrays;

/**
 * SOLUTION du drill de rappel 5 - le passage par valeur.
 */
public class Recall05 {

    static void bump(int n) {
        n++;
    }

    static int bumped(int n) {
        return n + 1;
    }

    static void bump(int[] a) {
        a[0]++;
    }

    static void replace(int[] a) {
        a = new int[] {100};
        a[0]++;
    }

    static void grow(StringBuilder sb) {
        sb.append("+");
    }

    static void swapRefs(StringBuilder x, StringBuilder y) {
        StringBuilder t = x;
        x = y;
        y = t;
    }

    static void shout(String s) {
        s = s.toUpperCase();
    }

    static String shouted(String s) {
        return s.toUpperCase();
    }

    static int[] doubled(int[] a) {
        int[] copy = a.clone();
        for (int i = 0; i < copy.length; i++) {
            copy[i] *= 2;
        }
        return copy;
    }

    static void fill(int[][] grid, int v) {
        for (int[] row : grid) {
            Arrays.fill(row, v);   // row est une copie de la REFERENCE de la ligne : la vraie ligne change
        }
    }

    public static void main(String[] args) {
        int n = 1;
        bump(n);
        int m = bumped(n);
        System.out.println("D01 : " + n + " " + m);
        int[] a = {1};
        bump(a);
        replace(a);
        System.out.println("D02 : " + a[0]);
        StringBuilder x = new StringBuilder("x");
        StringBuilder y = new StringBuilder("y");
        grow(x);
        swapRefs(x, y);
        System.out.println("D03 : " + x + " " + y);
        String s = "hey";
        shout(s);
        String t = shouted(s);
        System.out.println("D04 : " + s + " " + t);
        int[] src = {1, 2, 3};
        int[] d = doubled(src);
        System.out.println("D05 : " + Arrays.toString(src) + " " + Arrays.toString(d));
        int[][] grid = new int[2][2];
        fill(grid, 7);
        System.out.println("D06 : " + Arrays.deepToString(grid));
        int[] alias = src;
        alias[2] = 30;
        bump(alias);
        System.out.println("D07 : " + Arrays.toString(src));
        StringBuilder chain = new StringBuilder("a");
        grow(chain);
        grow(chain);
        StringBuilder other = chain;
        other = new StringBuilder("z");
        System.out.println("D08 : " + chain + " " + other);
    }
}
