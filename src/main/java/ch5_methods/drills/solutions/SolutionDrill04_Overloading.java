package ch5_methods.drills.solutions;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.drills.exercises.Drill04_Overloading.
 */
public class SolutionDrill04_Overloading {

    public static String callExact() {
        // Le litteral 5 est un int : correspondance exacte.
        return show(5);
    }

    public static String callLongFromInt() {
        // Le cast change le type de l'argument, donc la surcharge choisie.
        int n = 5;
        return show((long) n);
    }

    public static String callInteger() {
        // Un Integer deja boxe correspond exactement a show(Integer).
        return show(Integer.valueOf(5));
    }

    public static String callObject() {
        // Le cast en Object : le type declare de l'argument est Object.
        return show((Object) 5);
    }

    public static String callVarargs() {
        // Deux arguments : seule la version varargs les accepte.
        return show(1, 2);
    }

    public static String callVarargsEmpty() {
        // Aucun argument : seule la version varargs accepte zero argument.
        return show();
    }

    public static String callDouble() {
        // 5.0 est un double.
        return show(5.0);
    }

    public static String callWithShort() {
        // short s'elargit en int (le plus petit elargissement disponible).
        short s = 5;
        return show(s);
    }

    public static String callWithChar() {
        // char s'elargit en int, bien avant tout boxing en Character.
        return show('a');
    }

    public static String callWithFloat() {
        // float s'elargit en double.
        return show(1.5f);
    }

    public static String callWithLongObject() {
        // Un Long n'est pas un Integer : son seul super-type disponible est Object.
        return show(Long.valueOf(5));
    }

    public static String callWithNullString() {
        // null seul serait ambigu (String, Integer, int[]) : le cast tranche.
        return show((String) null);
    }

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

    static String show(String x) {
        return "String";
    }

    static String show(Object x) {
        return "Object";
    }

    static String show(int... x) {
        return "int...";
    }
}
