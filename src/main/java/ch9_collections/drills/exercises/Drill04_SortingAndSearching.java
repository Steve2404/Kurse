package ch9_collections.drills.exercises;

import ch9_collections.ExerciseChecker;

import java.util.List;

/**
 * DRILL 04 - Trier et chercher : Collections.sort, Comparator, binarySearch
 * =========================================================================
 *
 * Mode d'emploi : voir Drill01_ListAndSet. Donnees : Pantry.FRUITS,
 * Pantry.NUMBERS et Pantry.items() (records Item(name, price) :
 * kiwi 3, pomme 2, banane 1, cerise 6, abricot 4).
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : natural()          [Collections.sort] une copie de FRUITS triee.
 * TODO 2  : reverse()          [Comparator.reverseOrder()] une copie de FRUITS triee a l'envers.
 * TODO 3  : byPrice()          [Comparator.comparingInt] les noms des items, du moins cher au plus cher.
 * TODO 4  : byPriceDesc()      [reversed()] les noms, du plus cher au moins cher.
 * TODO 5  : byLengthThenName() [thenComparing] les noms des items par longueur, puis alphabet.
 * TODO 6  : nullsFirst()       [Comparator.nullsFirst] trier [kiwi, null, abricot] -> [null, abricot, kiwi].
 * TODO 7  : indexOf8()         [Collections.binarySearch] dans NUMBERS TRIES [1, 2, 3, 5, 8, 9] -> 4.
 * TODO 8  : searchMissing4()   [binarySearch sur un absent] chercher 4 -> -(point d'insertion) - 1 = -4.
 * TODO 9  : mostExpensive()    [Collections.max avec Comparator] le nom de l'item le plus cher -> cerise.
 * TODO 10 : firstAlphabetical() [Collections.min] le premier fruit dans l'ordre alphabetique -> abricot.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Collections.sort(list) / list.sort(null)        ordre naturel (Comparable)
 *   list.sort(Comparator.reverseOrder())            ordre naturel inverse
 *   Comparator.comparing(Item::name) / comparingInt(Item::price)
 *   .reversed() retourne TOUTE la chaine a sa gauche ; .thenComparing(...) departage les egalites
 *   Comparator.nullsFirst(Comparator.naturalOrder())   / nullsLast
 *   Collections.binarySearch(listeTriee, cle)       index, ou -(point d'insertion) - 1 ; liste non triee = resultat indefini
 *   Collections.max(coll, comparator) / Collections.min(coll)
 *   Comparable : int compareTo(T o) (dans la classe) ; Comparator : int compare(T a, T b) (a cote)
 * ---------------------------------------------------------------------
 */
public class Drill04_SortingAndSearching {

    public static List<String> natural() {
        throw new UnsupportedOperationException("TODO 1 : implementer natural()");
    }

    public static List<String> reverse() {
        throw new UnsupportedOperationException("TODO 2 : implementer reverse()");
    }

    public static List<String> byPrice() {
        throw new UnsupportedOperationException("TODO 3 : implementer byPrice()");
    }

    public static List<String> byPriceDesc() {
        throw new UnsupportedOperationException("TODO 4 : implementer byPriceDesc()");
    }

    public static List<String> byLengthThenName() {
        throw new UnsupportedOperationException("TODO 5 : implementer byLengthThenName()");
    }

    public static List<String> nullsFirst() {
        throw new UnsupportedOperationException("TODO 6 : implementer nullsFirst()");
    }

    public static int indexOf8() {
        throw new UnsupportedOperationException("TODO 7 : implementer indexOf8()");
    }

    public static int searchMissing4() {
        throw new UnsupportedOperationException("TODO 8 : implementer searchMissing4()");
    }

    public static String mostExpensive() {
        throw new UnsupportedOperationException("TODO 9 : implementer mostExpensive()");
    }

    public static String firstAlphabetical() {
        throw new UnsupportedOperationException("TODO 10 : implementer firstAlphabetical()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  natural", natural().equals(List.of("abricot", "banane", "cerise", "kiwi", "kiwi", "pomme", "pomme")));
        ExerciseChecker.check("2  reverse", reverse().equals(List.of("pomme", "pomme", "kiwi", "kiwi", "cerise", "banane", "abricot")));
        ExerciseChecker.check("3  byPrice", byPrice().equals(List.of("banane", "pomme", "kiwi", "abricot", "cerise")));
        ExerciseChecker.check("4  byPriceDesc", byPriceDesc().equals(List.of("cerise", "abricot", "kiwi", "pomme", "banane")));
        ExerciseChecker.check("5  byLengthThenName", byLengthThenName().equals(List.of("kiwi", "pomme", "banane", "cerise", "abricot")));
        ExerciseChecker.check("6  nullsFirst", nullsFirst().toString().equals("[null, abricot, kiwi]"));
        ExerciseChecker.check("7  indexOf8 == 4", indexOf8() == 4);
        ExerciseChecker.check("8  searchMissing4 == -4", searchMissing4() == -4);
        ExerciseChecker.check("9  mostExpensive == cerise", mostExpensive().equals("cerise"));
        ExerciseChecker.check("10 firstAlphabetical == abricot", firstAlphabetical().equals("abricot"));

        ExerciseChecker.summary();
    }
}
