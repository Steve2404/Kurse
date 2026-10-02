package ch5_methods.drills.r07_overload.solution;

/**
 * SOLUTION du drill de rappel 7 - la resolution des surcharges.
 */
public class Recall07 {

    static String f(int x) {
        return "int";
    }

    static String f(long x) {
        return "long";
    }

    static String f(Integer x) {
        return "Integer";
    }

    static String f(int... x) {
        return "int...";
    }

    static String g(double x) {
        return "double";
    }

    static String g(Object x) {
        return "Object";
    }

    static String h(Long x) {
        return "Long";
    }

    static String h(Number x) {
        return "Number";
    }

    static String k(String x) {
        return "String";
    }

    static String k(Object x) {
        return "Object";
    }

    static String m(int[] x) {
        return "int[]";
    }

    static String m(Object x) {
        return "Object";
    }

    static String p(short x) {
        return "short";
    }

    static String p(char x) {
        return "char";
    }

    static String p(float x) {
        return "float";
    }

    public static void main(String[] args) {
        byte b = 1;
        System.out.println("D01 : " + f(1) + " " + f(b) + " " + f('c') + " " + f(1L) + " " + f(Integer.valueOf(1)) + " " + f() + " " + f(1, 2));
        System.out.println("D02 : " + g(1) + " " + g(1.5f) + " " + g('a') + " " + g("s") + " " + g(true));
        System.out.println("D03 : " + h(1L) + " " + h(1) + " " + h(Long.valueOf(1)) + " " + h(1.0));
        Object o = "texte";
        System.out.println("D04 : " + k("a") + " " + k(o) + " " + k(null) + " " + k((Object) null));
        System.out.println("D05 : " + m(new int[2]) + " " + m(null) + " " + m(new int[2][2]) + " " + m(new Integer[1]));
        short s = 3;
        System.out.println("D06 : " + p(s) + " " + p('x') + " " + p(3) + " " + p(b) + " " + p(3L));
    }
}
