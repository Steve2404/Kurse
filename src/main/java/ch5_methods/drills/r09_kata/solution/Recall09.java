package ch5_methods.drills.r09_kata.solution;

import java.util.Arrays;

import static java.lang.Math.abs;

/**
 * SOLUTION du drill de rappel 9 - kata mixte du chapitre 5.
 */
public class Recall09 {

    private static int calls;
    static final String TAG;

    static {
        TAG = "kata";
    }

    static int sum(int... v) {
        calls++;
        int s = 0;
        for (int x : v) {
            s += x;
        }
        return s;
    }

    static String kind(long x) {
        return "long";
    }

    static String kind(Integer x) {
        return "Integer";
    }

    static String kind(Object x) {
        return "Object";
    }

    static void reset(int[] a, int v) {
        a = new int[] {v};
    }

    static void set(int[] a, int v) {
        a[0] = v;
    }

    static int gcd(int a, int b) {
        calls++;
        return b == 0 ? abs(a) : gcd(b, a % b);
    }

    static int[] minMax(int first, int... rest) {
        int min = first;
        int max = first;
        for (int x : rest) {
            min = Math.min(min, x);
            max = Math.max(max, x);
        }
        return new int[] {min, max};
    }

    public static void main(String[] args) {
        System.out.println("D01 : " + sum() + " " + sum(1, 2) + " " + sum(new int[] {3, 4, 5}) + " " + calls);
        System.out.println("D02 : " + kind(1) + " " + kind(Integer.valueOf(1)) + " " + kind(1.0) + " " + kind('c'));
        int[] a = {1};
        reset(a, 5);
        String before = Arrays.toString(a);
        set(a, 5);
        System.out.println("D03 : " + before + " " + Arrays.toString(a));
        calls = 0;
        System.out.println("D04 : " + gcd(-84, 36) + " " + calls);
        System.out.println("D05 : " + Arrays.toString(minMax(4, 9, -2, 7)) + " " + Arrays.toString(minMax(3)));
        Integer x = 128;
        Integer y = 128;
        System.out.println("D06 : " + (x == y) + " " + x.equals(y) + " " + (x <= y));
        Recall09 none = null;
        System.out.println("D07 : " + TAG + " " + none.TAG.length());
    }
}
