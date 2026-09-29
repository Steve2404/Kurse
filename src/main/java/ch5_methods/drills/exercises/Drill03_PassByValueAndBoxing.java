package ch5_methods.drills.exercises;

import ch5_methods.ExerciseChecker;
import ch5_methods.drills.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DRILL 03 - Passage par valeur, autoboxing et unboxing
 * =====================================================
 *
 * Mode d'emploi : voir Drill01_DeclarationsAndVarargs.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : doubled(x)                 [rendre la nouvelle valeur d'un primitif] 7 -> 14.
 * TODO 2  : doubleAll(values)          [modifier le tableau recu] {1, 2} -> {2, 4} chez l'appelant.
 * TODO 3  : appendBang(sb)             [modifier l'objet recu] "Go" -> "Go!".
 * TODO 4  : reassignBroken(sb)         [reassigner le parametre : invisible] ecris sb = new StringBuilder("X");
 * TODO 5  : swapCells(values, i, j)    [echanger des cases] {1, 2, 3}, 0, 2 -> {3, 2, 1}.
 * TODO 6  : sumBonuses()               [deballer en evitant null] Team.BONUS -> 8.
 * TODO 7  : bonusOrZero(index)         [Integer null -> valeur par defaut] index 1 -> 0 ; index 2 -> 5.
 * TODO 8  : bigEqualsByValue()         [equals entre Integer] Integer a = 1000, b = 1000 -> a.equals(b) -> true.
 * TODO 9  : bigEqualsByIdentity()      [== entre Integer hors cache] meme a et b -> a == b -> false.
 * TODO 10 : removeSeven()              [remove(Object)] liste [12, 7, 15] -> [12, 15].
 * TODO 11 : removeFirst()              [remove(int index)] liste [12, 7, 15] -> [7, 15].
 * TODO 12 : longEqualsInt()            [Long.equals avec un int] Long.valueOf(7).equals(7) -> false.
 * TODO 13 : unboxNull()                [deballer null] Integer n = null ; int x = n ; -> "NullPointerException".
 * TODO 14 : parsed(text)               [Integer.parseInt] "12" -> 12.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Java passe TOUJOURS une copie : du nombre (primitif) ou de l'ADRESSE (objet)
 *   Modifier l'objet recu : visible dehors ; reassigner le parametre : invisible
 *   Integer a = 127, b = 127 : a == b vrai (cache -128..127) ; 1000 : faux -> equals
 *   Deballer null -> NullPointerException (aussi dans un ternaire de type int)
 *   List<Integer>.remove(1) enleve l'INDEX 1 ; remove(Integer.valueOf(1)) enleve la VALEUR 1
 *   Long.valueOf(7).equals(7) faux (Integer) ; equals(7L) vrai
 *   long l = Integer.valueOf(5) compile ; Long x = 5 ne compile pas
 * ---------------------------------------------------------------------
 */
public class Drill03_PassByValueAndBoxing {

    public static int doubled(int x) {
        throw new UnsupportedOperationException("TODO 1 : implementer doubled()");
    }

    public static void doubleAll(int[] values) {
        throw new UnsupportedOperationException("TODO 2 : implementer doubleAll()");
    }

    public static void appendBang(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 3 : implementer appendBang()");
    }

    public static void reassignBroken(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 4 : implementer reassignBroken()");
    }

    public static void swapCells(int[] values, int i, int j) {
        throw new UnsupportedOperationException("TODO 5 : implementer swapCells()");
    }

    public static int sumBonuses() {
        throw new UnsupportedOperationException("TODO 6 : implementer sumBonuses()");
    }

    public static int bonusOrZero(int index) {
        throw new UnsupportedOperationException("TODO 7 : implementer bonusOrZero()");
    }

    public static boolean bigEqualsByValue() {
        throw new UnsupportedOperationException("TODO 8 : implementer bigEqualsByValue()");
    }

    public static boolean bigEqualsByIdentity() {
        throw new UnsupportedOperationException("TODO 9 : implementer bigEqualsByIdentity()");
    }

    public static List<Integer> removeSeven() {
        throw new UnsupportedOperationException("TODO 10 : implementer removeSeven()");
    }

    public static List<Integer> removeFirst() {
        throw new UnsupportedOperationException("TODO 11 : implementer removeFirst()");
    }

    public static boolean longEqualsInt() {
        throw new UnsupportedOperationException("TODO 12 : implementer longEqualsInt()");
    }

    public static String unboxNull() {
        throw new UnsupportedOperationException("TODO 13 : implementer unboxNull()");
    }

    public static int parsed(String text) {
        throw new UnsupportedOperationException("TODO 14 : implementer parsed()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  doubled(7) == 14", doubled(7) == 14);
        int[] values = {1, 2};
        doubleAll(values);
        ExerciseChecker.check("2  doubleAll : {2, 4}", Arrays.equals(values, new int[] {2, 4}));
        StringBuilder sb = new StringBuilder("Go");
        appendBang(sb);
        ExerciseChecker.check("3  appendBang : Go!", sb.toString().equals("Go!"));
        reassignBroken(sb);
        ExerciseChecker.check("4  reassignBroken : toujours Go!", sb.toString().equals("Go!"));
        int[] three = {1, 2, 3};
        swapCells(three, 0, 2);
        ExerciseChecker.check("5  swapCells : {3, 2, 1}", Arrays.equals(three, new int[] {3, 2, 1}));
        ExerciseChecker.check("6  sumBonuses() == 8", sumBonuses() == 8);
        ExerciseChecker.check("7  bonusOrZero : 0 et 5", bonusOrZero(1) == 0 && bonusOrZero(2) == 5);
        ExerciseChecker.check("8  bigEqualsByValue() == true", bigEqualsByValue());
        ExerciseChecker.check("9  bigEqualsByIdentity() == false", !bigEqualsByIdentity());
        ExerciseChecker.check("10 removeSeven() == [12, 15]", removeSeven().equals(List.of(12, 15)));
        ExerciseChecker.check("11 removeFirst() == [7, 15]", removeFirst().equals(List.of(7, 15)));
        ExerciseChecker.check("12 longEqualsInt() == false", !longEqualsInt());
        ExerciseChecker.check("13 unboxNull() == \"NullPointerException\"", "NullPointerException".equals(unboxNull()));
        ExerciseChecker.check("14 parsed(\"12\") == 12", parsed("12") == 12);

        ExerciseChecker.summary();
    }
}
