package ch5_methods.drills.exercises;

import ch5_methods.ExerciseChecker;
import ch5_methods.drills.Team;

import java.util.Arrays;

/**
 * DRILL 01 - Declarer des methodes et manier les varargs (equipe)
 * ===============================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en quelques lignes et
 * vise UNE forme precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Donnees : ch5_methods.drills.Team.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : total(values...)                 [varargs int, for-each] total(Team.SCORES) -> 43 ; total() -> 0.
 * TODO 2  : count(items...)                  [varargs Object, length] count() -> 0 ; count("a", null) -> 2.
 * TODO 3  : best(first, others...)           [parametre obligatoire + varargs] best(12, 7, 15, 9) -> 15.
 * TODO 4  : greet(greeting, names...)        [varargs apres un parametre] ("Bonjour", "Ada", "Linus") -> "Bonjour Ada, Linus".
 * TODO 5  : average(values...)               [varargs + double] average(Team.SCORES) -> 10.75 ; average() -> 0.0.
 * TODO 6  : last(names...)                   [varargs indexe] last("a", "b") -> "b" ; last() -> null.
 * TODO 7  : contains(target, names...)       [varargs + equals] contains("Grace", Team.NAMES) -> true.
 * TODO 8  : joinAll(separator, parts...)     [String.join avec un varargs] ("|", "a", "b") -> "a|b".
 * TODO 9  : minMax(values...)                [rendre un tableau] minMax(Team.SCORES) -> {7, 15}.
 * TODO 10 : countAsArray()                   [un String[] DEVIENT le sac] count((Object[]) Team.NAMES) -> 4.
 * TODO 11 : countAsOneObject()               [un tableau DANS le sac] count((Object) Team.NAMES) -> 1.
 * TODO 12 : label(prefix, number)            [declaration complete : static public, retour String] -> "P-7".
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   [modificateurs, ordre libre] [type de retour, juste avant le nom] nom(params) [throws ...]
 *   static public void m() : OK ; void public m() : <identifier> expected ; m() : return type required
 *   public private : illegal combination ; static static : repeated modifier
 *   Varargs : UN seul, EN DERNIER ; dedans c'est un tableau (vide si rien passe, jamais null)
 *   Un String[] passe a Object... devient le tableau ; un int[] devient UN objet
 *   Signature = nom + types des parametres (PAS le retour) ; m(int[]) et m(int...) : meme signature
 * ---------------------------------------------------------------------
 */
public class Drill01_DeclarationsAndVarargs {

    public static int total(int... values) {
        throw new UnsupportedOperationException("TODO 1 : implementer total()");
    }

    public static int count(Object... items) {
        throw new UnsupportedOperationException("TODO 2 : implementer count()");
    }

    public static int best(int first, int... others) {
        throw new UnsupportedOperationException("TODO 3 : implementer best()");
    }

    public static String greet(String greeting, String... names) {
        throw new UnsupportedOperationException("TODO 4 : implementer greet()");
    }

    public static double average(int... values) {
        throw new UnsupportedOperationException("TODO 5 : implementer average()");
    }

    public static String last(String... names) {
        throw new UnsupportedOperationException("TODO 6 : implementer last()");
    }

    public static boolean contains(String target, String... names) {
        throw new UnsupportedOperationException("TODO 7 : implementer contains()");
    }

    public static String joinAll(String separator, String... parts) {
        throw new UnsupportedOperationException("TODO 8 : implementer joinAll()");
    }

    public static int[] minMax(int... values) {
        throw new UnsupportedOperationException("TODO 9 : implementer minMax()");
    }

    public static int countAsArray() {
        throw new UnsupportedOperationException("TODO 10 : implementer countAsArray()");
    }

    public static int countAsOneObject() {
        throw new UnsupportedOperationException("TODO 11 : implementer countAsOneObject()");
    }

    public static String label(String prefix, int number) {
        throw new UnsupportedOperationException("TODO 12 : implementer label()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  total : 43 et 0", total(Team.SCORES) == 43 && total() == 0);
        ExerciseChecker.check("2  count : 0 et 2", count() == 0 && count("a", null) == 2);
        ExerciseChecker.check("3  best(12, 7, 15, 9) == 15", best(12, 7, 15, 9) == 15 && best(4) == 4);
        ExerciseChecker.check("4  greet", greet("Bonjour", "Ada", "Linus").equals("Bonjour Ada, Linus"));
        ExerciseChecker.check("5  average : 10.75 et 0.0", average(Team.SCORES) == 10.75 && average() == 0.0);
        ExerciseChecker.check("6  last : b et null", "b".equals(last("a", "b")) && last() == null);
        ExerciseChecker.check("7  contains : Grace oui, Bob non", contains("Grace", Team.NAMES) && !contains("Bob", Team.NAMES));
        ExerciseChecker.check("8  joinAll(\"|\", \"a\", \"b\") == \"a|b\"", joinAll("|", "a", "b").equals("a|b"));
        ExerciseChecker.check("9  minMax(SCORES) == {7, 15}", Arrays.equals(minMax(Team.SCORES), new int[] {7, 15}));
        ExerciseChecker.check("10 countAsArray() == 4", countAsArray() == 4);
        ExerciseChecker.check("11 countAsOneObject() == 1", countAsOneObject() == 1);
        ExerciseChecker.check("12 label(\"P\", 7) == \"P-7\"", label("P", 7).equals("P-7"));

        ExerciseChecker.summary();
    }
}
