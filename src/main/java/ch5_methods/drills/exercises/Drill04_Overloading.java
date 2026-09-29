package ch5_methods.drills.exercises;

import ch5_methods.ExerciseChecker;

/**
 * DRILL 04 - Surcharge : fais appeler la version demandee
 * =======================================================
 *
 * Mode d'emploi : voir Drill01_DeclarationsAndVarargs.
 *
 * Plus bas, 7 surcharges de show() sont deja ecrites ; chacune rend le
 * type de SON parametre : show(int), show(long), show(double),
 * show(Integer), show(String), show(Object), show(int...).
 * Chaque TODO t'impose un argument ou une version : ecris l'appel (en
 * choisissant la valeur ou le cast) et RENDS ce que show renvoie.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : callExact()           [correspondance exacte] -> "int".
 * TODO 2  : callLongFromInt()     [cast] avec int n = 5, faire choisir show(long) -> "long".
 * TODO 3  : callInteger()         [boxing explicite] faire choisir show(Integer) -> "Integer".
 * TODO 4  : callObject()          [cast vers Object] avec 5, faire choisir show(Object) -> "Object".
 * TODO 5  : callVarargs()         [deux arguments] -> "int...".
 * TODO 6  : callVarargsEmpty()    [zero argument] -> "int...".
 * TODO 7  : callDouble()          [litteral double] -> "double".
 * TODO 8  : callWithShort()       [elargissement] show(short s = 5) -> "int" (le plus petit elargissement).
 * TODO 9  : callWithChar()        [char s'elargit en int] show('a') -> "int".
 * TODO 10 : callWithFloat()       [float s'elargit en double] show(1.5f) -> "double".
 * TODO 11 : callWithLongObject()  [un Long n'est pas un Integer] show(Long.valueOf(5)) -> "Object".
 * TODO 12 : callWithNullString()  [null ambigu : cast obligatoire] faire choisir show(String) avec null -> "String".
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Tour 1 : exact ou elargissement (byte < short < int < long < float < double ; char -> int)
 *   Tour 2 : boxing / unboxing (int -> Integer -> Number -> Object ; jamais int -> Long)
 *   Tour 3 : varargs
 *   Dans un tour : le plus precis gagne (plus petit primitif ; Integer avant Number avant Object)
 *   show(null) avec String, Integer, Object, int... : reference to show is ambiguous -> caster
 *   Le type de RETOUR ne choisit jamais la surcharge
 * ---------------------------------------------------------------------
 */
public class Drill04_Overloading {

    // Deja ecrit : les 7 surcharges.
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

    public static String callExact() {
        throw new UnsupportedOperationException("TODO 1 : implementer callExact()");
    }

    public static String callLongFromInt() {
        throw new UnsupportedOperationException("TODO 2 : implementer callLongFromInt()");
    }

    public static String callInteger() {
        throw new UnsupportedOperationException("TODO 3 : implementer callInteger()");
    }

    public static String callObject() {
        throw new UnsupportedOperationException("TODO 4 : implementer callObject()");
    }

    public static String callVarargs() {
        throw new UnsupportedOperationException("TODO 5 : implementer callVarargs()");
    }

    public static String callVarargsEmpty() {
        throw new UnsupportedOperationException("TODO 6 : implementer callVarargsEmpty()");
    }

    public static String callDouble() {
        throw new UnsupportedOperationException("TODO 7 : implementer callDouble()");
    }

    public static String callWithShort() {
        throw new UnsupportedOperationException("TODO 8 : implementer callWithShort()");
    }

    public static String callWithChar() {
        throw new UnsupportedOperationException("TODO 9 : implementer callWithChar()");
    }

    public static String callWithFloat() {
        throw new UnsupportedOperationException("TODO 10 : implementer callWithFloat()");
    }

    public static String callWithLongObject() {
        throw new UnsupportedOperationException("TODO 11 : implementer callWithLongObject()");
    }

    public static String callWithNullString() {
        throw new UnsupportedOperationException("TODO 12 : implementer callWithNullString()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  callExact() == int", "int".equals(callExact()));
        ExerciseChecker.check("2  callLongFromInt() == long", "long".equals(callLongFromInt()));
        ExerciseChecker.check("3  callInteger() == Integer", "Integer".equals(callInteger()));
        ExerciseChecker.check("4  callObject() == Object", "Object".equals(callObject()));
        ExerciseChecker.check("5  callVarargs() == int...", "int...".equals(callVarargs()));
        ExerciseChecker.check("6  callVarargsEmpty() == int...", "int...".equals(callVarargsEmpty()));
        ExerciseChecker.check("7  callDouble() == double", "double".equals(callDouble()));
        ExerciseChecker.check("8  callWithShort() == int", "int".equals(callWithShort()));
        ExerciseChecker.check("9  callWithChar() == int", "int".equals(callWithChar()));
        ExerciseChecker.check("10 callWithFloat() == double", "double".equals(callWithFloat()));
        ExerciseChecker.check("11 callWithLongObject() == Object", "Object".equals(callWithLongObject()));
        ExerciseChecker.check("12 callWithNullString() == String", "String".equals(callWithNullString()));

        ExerciseChecker.summary();
    }
}
