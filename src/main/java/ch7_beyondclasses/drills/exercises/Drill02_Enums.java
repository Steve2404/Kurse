package ch7_beyondclasses.drills.exercises;

import ch7_beyondclasses.ExerciseChecker;
import ch7_beyondclasses.drills.Catalog;

import java.util.EnumMap;
import java.util.Map;

/**
 * DRILL 02 - Enums : champs, constructeur, values, valueOf, ordinal, switch, EnumMap, methode par constante
 * ========================================================================================================
 *
 * Mode d'emploi : voir Drill01_Interfaces.
 *
 *   enum Size { S(10), M(15), L(20) } : la largeur en centimetres.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : Size(width)               [constructeur d'enum, toujours private] affecte width.
 * TODO 2  : Size.bigger()             [methode abstract redefinie par constante] S -> M, M -> L, L -> L.
 *                                      (Dans le squelette, chaque constante a deja son corps.)
 * TODO 3  : count()                   [values()] nombre de constantes -> 3.
 * TODO 4  : parse(text)               [valueOf] "M" -> M.
 * TODO 5  : position(size)            [ordinal()] L -> 2.
 * TODO 6  : isSmallerThan(a, b)       [compareTo, pas <] S, L -> true.
 * TODO 7  : label(size)               [switch expression sans default, toutes les constantes] "petit", "moyen", "grand".
 * TODO 8  : countSizes()              [EnumMap] les formats du catalogue -> {S=1, M=1, L=2}.
 * TODO 9  : widest()                  [parcourir values()] la plus large -> L.
 * TODO 10 : fromWidth(width)          [recherche dans values()] 15 -> M ; 99 -> null.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Constructeur : toujours private (public/protected interdits) ; new Size() interdit
 *   Les constantes d'abord, puis ";" puis champs et methodes
 *   values() : tableau dans l'ordre ; valueOf("M") : par NOM ; ordinal() : position (0..)
 *   == pour comparer ; compareTo pour l'ordre (E.A < E.B ne compile pas)
 *   switch : case S (jamais case Size.S) ; switch expression : toutes les constantes ou default
 *   Methode abstract dans l'enum -> chaque constante la redefinit dans son corps { }
 * ---------------------------------------------------------------------
 */
public class Drill02_Enums {

    enum Size {
        S(10) {
            @Override
            Size bigger() {
                throw new UnsupportedOperationException("TODO 2 : implementer S.bigger()");
            }
        },
        M(15) {
            @Override
            Size bigger() {
                throw new UnsupportedOperationException("TODO 2 : implementer M.bigger()");
            }
        },
        L(20) {
            @Override
            Size bigger() {
                throw new UnsupportedOperationException("TODO 2 : implementer L.bigger()");
            }
        };

        private final int width;

        Size(int width) {
            throw new UnsupportedOperationException("TODO 1 : implementer Size(width)");
        }

        int width() {
            return width;
        }

        abstract Size bigger();
    }

    public static int count() {
        throw new UnsupportedOperationException("TODO 3 : implementer count()");
    }

    public static Size parse(String text) {
        throw new UnsupportedOperationException("TODO 4 : implementer parse()");
    }

    public static int position(Size size) {
        throw new UnsupportedOperationException("TODO 5 : implementer position()");
    }

    public static boolean isSmallerThan(Size a, Size b) {
        throw new UnsupportedOperationException("TODO 6 : implementer isSmallerThan()");
    }

    public static String label(Size size) {
        throw new UnsupportedOperationException("TODO 7 : implementer label()");
    }

    public static Map<Size, Integer> countSizes() {
        throw new UnsupportedOperationException("TODO 8 : implementer countSizes()");
    }

    public static Size widest() {
        throw new UnsupportedOperationException("TODO 9 : implementer widest()");
    }

    public static Size fromWidth(int width) {
        throw new UnsupportedOperationException("TODO 10 : implementer fromWidth()");
    }

    public static void main(String[] args) {
        Size first;
        try {
            first = Size.S;
        } catch (ExceptionInInitializerError e) {
            throw (RuntimeException) e.getCause();
        }
        ExerciseChecker.check("1  Size(width) : S = 10, L = 20", first.width() == 10 && Size.L.width() == 20);
        ExerciseChecker.check("2  bigger : S -> M, M -> L, L -> L", Size.S.bigger() == Size.M && Size.M.bigger() == Size.L && Size.L.bigger() == Size.L);
        ExerciseChecker.check("3  count() == 3", count() == 3);
        ExerciseChecker.check("4  parse(\"M\") == M", parse("M") == Size.M);
        ExerciseChecker.check("5  position(L) == 2", position(Size.L) == 2);
        ExerciseChecker.check("6  isSmallerThan(S, L) oui, (L, M) non", isSmallerThan(Size.S, Size.L) && !isSmallerThan(Size.L, Size.M));
        ExerciseChecker.check("7  label", label(Size.S).equals("petit") && label(Size.M).equals("moyen") && label(Size.L).equals("grand"));
        ExerciseChecker.check("8  countSizes == {S=1, M=1, L=2}", countSizes().toString().equals("{S=1, M=1, L=2}"));
        ExerciseChecker.check("9  widest() == L", widest() == Size.L);
        ExerciseChecker.check("10 fromWidth : 15 -> M, 99 -> null", fromWidth(15) == Size.M && fromWidth(99) == null);

        ExerciseChecker.summary();
    }
}
