package ch5_methods.projects.p05_overload.solution;

/**
 * SOLUTION - des familles de surcharges qui rendent le nom de la version choisie.
 * Ordre de resolution : 1. exact ou elargissement (sans boxing ni varargs), 2. avec boxing, 3. varargs.
 * Dans une phase, Java prend la version la PLUS SPECIFIQUE.
 */
public class Printer {

    // Famille show : int, long, double, Integer, Object, int...
    static String show(int x) {
        return "int";
    }

    static String show(long x) {
        return "long";
    }

    static String show(double x) {
        return "double";
    }

    static String show(Integer x) {
        return "Integer";
    }

    static String show(Object x) {
        return "Object";
    }

    static String show(int... x) {
        return "int...";
    }

    // Famille box : l'elargissement (int -> long) passe AVANT le boxing (int -> Integer).
    static String box(long x) {
        return "long";
    }

    static String box(Integer x) {
        return "Integer";
    }

    static String box(Object x) {
        return "Object";
    }

    // Famille pick : un int ne devient JAMAIS un Long (boxing puis elargissement interdit).
    static String pick(Long x) {
        return "Long";
    }

    static String pick(Object x) {
        return "Object";
    }

    // Famille text : null va a la version la plus specifique (String est un CharSequence, qui est un Object).
    static String text(String x) {
        return "String";
    }

    static String text(CharSequence x) {
        return "CharSequence";
    }

    static String text(Object x) {
        return "Object";
    }

    // Famille sum : les parametres fixes passent avant le varargs ; l'elargissement avant le boxing.
    static String sum(int a, int b) {
        return "int,int";
    }

    static String sum(int... v) {
        return "int...";
    }

    static String add(long a, long b) {
        return "long,long";
    }

    static String add(Integer a, Integer b) {
        return "Integer,Integer";
    }

    // Une surcharge static et une d'instance peuvent coexister (signatures differentes).
    String twice(String s) {
        return s + s;
    }

    static String twice(int n) {
        return String.valueOf(2 * n);
    }
}
