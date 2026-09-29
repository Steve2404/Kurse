package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;
import ch10_streams.drills.Library.Loan;
import ch10_streams.drills.Library.Member;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * DRILL 12 - KATA MELANGE : 20 questions du chef de la bibliotheque (projet bibliotheque)
 * =======================================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi, AVEC UNE DIFFERENCE : ici,
 * AUCUNE methode n'est indiquee. C'est a toi de choisir l'outil
 * (Optional ? Stream ? quel Collector ? quel Comparator ?), exactement
 * comme dans un vrai projet ou a l'examen. Les drills 01 a 11 t'ont
 * donne les outils ; celui-ci verifie que tu sais QUAND les sortir.
 *
 * Fais-le en dernier, puis refais-le regulierement : c'est le meilleur
 * test "est-ce que je m'en souviens vraiment ?".
 *
 * Aides disponibles : Library.book(isbn) et Library.member(id).
 *
 *
 * -- Les 20 questions --
 *
 * TODO 1  : titlesOfMember(name)      les titres empruntes par ce membre, ordre des emprunts.
 *           Hugo -> [1984, Dune, La Ferme des animaux].
 * TODO 2  : membersWhoNeverBorrowed() noms des membres sans aucun emprunt -> [Tom].
 * TODO 3  : mostBorrowedTitle()       le titre le plus emprunte (boite) -> Dune.
 * TODO 4  : memberWithMostLateDays()  nom du membre au plus grand TOTAL de retard -> Ines.
 * TODO 5  : lateLoansDescription()    "nom:titre:retard" des emprunts en retard,
 *           du plus gros retard au plus petit.
 * TODO 6  : borrowedValueByMember()   TreeMap nom -> somme des prix des livres empruntes.
 * TODO 7  : genresBorrowedByMember()  TreeMap nom -> TreeSet des genres empruntes.
 * TODO 8  : neverBorrowedTitles()     titres jamais empruntes -> [Les Robots].
 * TODO 9  : loansPerMonthLine()       "M1:2, M2:2, M3:4".
 * TODO 10 : adultEmails()             emails utilisables (non null, non blancs, strip)
 *           des membres majeurs, tries -> [ines@biblio.org].
 * TODO 11 : averageAgeOfBorrowers()   age moyen des membres ayant emprunte au moins
 *           une fois (chacun compte UNE fois) -> OptionalDouble.
 * TODO 12 : firstLateTitleInMonth(m)  titre du 1er emprunt en retard de ce mois (boite).
 * TODO 13 : borrowedAuthors()         auteurs des livres empruntes, sans doublon, tries.
 * TODO 14 : topBorrowers(n)           noms des n plus gros emprunteurs (nombre d'emprunts
 *           decroissant, puis nom) -> n = 2 : [Hugo, Lea].
 * TODO 15 : allLatecomersAreAdults()  tous les membres ayant un retard sont-ils majeurs ?
 * TODO 16 : loanCountByGenre()        TreeMap genre -> nombre d'emprunts.
 * TODO 17 : membersByHasEmail()       partition (email utilisable ?) -> noms.
 * TODO 18 : cheapestNeverBorrowed()   titre du livre jamais emprunte le moins cher (boite).
 * TODO 19 : maxDelayByMonth()         TreeMap mois -> plus gros retard du mois.
 * TODO 20 : summaryLine()             "8 emprunts, 19 jours de retard" EN UN SEUL collect.
 *
 * Pas de carte memoire ici : si tu bloques, retourne voir la carte du
 * drill concerne (01-02 Optional, 03-07 Stream, 08-11 Collectors).
 */
public class Drill12_MixedKata {

    public static List<String> titlesOfMember(String name) {
        throw new UnsupportedOperationException("TODO 1 : implementer titlesOfMember()");
    }

    public static List<String> membersWhoNeverBorrowed() {
        throw new UnsupportedOperationException("TODO 2 : implementer membersWhoNeverBorrowed()");
    }

    public static Optional<String> mostBorrowedTitle() {
        throw new UnsupportedOperationException("TODO 3 : implementer mostBorrowedTitle()");
    }

    public static Optional<String> memberWithMostLateDays() {
        throw new UnsupportedOperationException("TODO 4 : implementer memberWithMostLateDays()");
    }

    public static List<String> lateLoansDescription() {
        throw new UnsupportedOperationException("TODO 5 : implementer lateLoansDescription()");
    }

    public static TreeMap<String, Double> borrowedValueByMember() {
        throw new UnsupportedOperationException("TODO 6 : implementer borrowedValueByMember()");
    }

    public static TreeMap<String, TreeSet<String>> genresBorrowedByMember() {
        throw new UnsupportedOperationException("TODO 7 : implementer genresBorrowedByMember()");
    }

    public static List<String> neverBorrowedTitles() {
        throw new UnsupportedOperationException("TODO 8 : implementer neverBorrowedTitles()");
    }

    public static String loansPerMonthLine() {
        throw new UnsupportedOperationException("TODO 9 : implementer loansPerMonthLine()");
    }

    public static List<String> adultEmails() {
        throw new UnsupportedOperationException("TODO 10 : implementer adultEmails()");
    }

    public static OptionalDouble averageAgeOfBorrowers() {
        throw new UnsupportedOperationException("TODO 11 : implementer averageAgeOfBorrowers()");
    }

    public static Optional<String> firstLateTitleInMonth(int month) {
        throw new UnsupportedOperationException("TODO 12 : implementer firstLateTitleInMonth()");
    }

    public static List<String> borrowedAuthors() {
        throw new UnsupportedOperationException("TODO 13 : implementer borrowedAuthors()");
    }

    public static List<String> topBorrowers(int n) {
        throw new UnsupportedOperationException("TODO 14 : implementer topBorrowers()");
    }

    public static boolean allLatecomersAreAdults() {
        throw new UnsupportedOperationException("TODO 15 : implementer allLatecomersAreAdults()");
    }

    public static TreeMap<String, Long> loanCountByGenre() {
        throw new UnsupportedOperationException("TODO 16 : implementer loanCountByGenre()");
    }

    public static Map<Boolean, List<String>> membersByHasEmail() {
        throw new UnsupportedOperationException("TODO 17 : implementer membersByHasEmail()");
    }

    public static Optional<String> cheapestNeverBorrowed() {
        throw new UnsupportedOperationException("TODO 18 : implementer cheapestNeverBorrowed()");
    }

    public static TreeMap<Integer, Integer> maxDelayByMonth() {
        throw new UnsupportedOperationException("TODO 19 : implementer maxDelayByMonth()");
    }

    public static String summaryLine() {
        throw new UnsupportedOperationException("TODO 20 : implementer summaryLine()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  titlesOfMember(Hugo) == [1984, Dune, La Ferme des animaux]",
                titlesOfMember("Hugo").equals(List.of("1984", "Dune", "La Ferme des animaux")));
        ExerciseChecker.check("2  membersWhoNeverBorrowed == [Tom]", membersWhoNeverBorrowed().equals(List.of("Tom")));
        ExerciseChecker.check("3  mostBorrowedTitle == Dune", mostBorrowedTitle().equals(Optional.of("Dune")));
        ExerciseChecker.check("4  memberWithMostLateDays == Ines", memberWithMostLateDays().equals(Optional.of("Ines")));
        ExerciseChecker.check("5  lateLoansDescription", lateLoansDescription().equals(List.of(
                "Ines:Fondation:10", "Hugo:La Ferme des animaux:5", "Hugo:1984:3", "Ines:Neuromancien:1")));
        ExerciseChecker.check("6  borrowedValueByMember == {Hugo=23.5, Ines=17.0, Lea=25.5}",
                borrowedValueByMember().toString().equals("{Hugo=23.5, Ines=17.0, Lea=25.5}"));
        ExerciseChecker.check("7  genresBorrowedByMember",
                genresBorrowedByMember().toString().equals("{Hugo=[Dystopie, SF], Ines=[SF], Lea=[Conte, Fantasy, SF]}"));
        ExerciseChecker.check("8  neverBorrowedTitles == [Les Robots]", neverBorrowedTitles().equals(List.of("Les Robots")));
        ExerciseChecker.check("9  loansPerMonthLine == M1:2, M2:2, M3:4", loansPerMonthLine().equals("M1:2, M2:2, M3:4"));
        ExerciseChecker.check("10 adultEmails == [ines@biblio.org]", adultEmails().equals(List.of("ines@biblio.org")));
        ExerciseChecker.check("11 averageAgeOfBorrowers == (17 + 34 + 52) / 3",
                averageAgeOfBorrowers().equals(OptionalDouble.of((17 + 34 + 52) / 3.0)));
        ExerciseChecker.check("12 firstLateTitleInMonth(3) == Neuromancien, (2) == Fondation, (4) vide",
                firstLateTitleInMonth(3).equals(Optional.of("Neuromancien"))
                        && firstLateTitleInMonth(2).equals(Optional.of("Fondation")) && firstLateTitleInMonth(4).isEmpty());
        ExerciseChecker.check("13 borrowedAuthors", borrowedAuthors().equals(
                List.of("Asimov", "Gibson", "Herbert", "Orwell", "Saint-Exupery", "Tolkien")));
        ExerciseChecker.check("14 topBorrowers(2) == [Hugo, Lea], topBorrowers(3) == [Hugo, Lea, Ines]",
                topBorrowers(2).equals(List.of("Hugo", "Lea")) && topBorrowers(3).equals(List.of("Hugo", "Lea", "Ines")));
        ExerciseChecker.check("15 allLatecomersAreAdults == true", allLatecomersAreAdults());
        ExerciseChecker.check("16 loanCountByGenre == {Conte=1, Dystopie=2, Fantasy=1, SF=4}",
                loanCountByGenre().toString().equals("{Conte=1, Dystopie=2, Fantasy=1, SF=4}"));
        ExerciseChecker.check("17 membersByHasEmail == {false=[Hugo, Tom], true=[Lea, Ines]}",
                membersByHasEmail().toString().equals("{false=[Hugo, Tom], true=[Lea, Ines]}"));
        ExerciseChecker.check("18 cheapestNeverBorrowed == Les Robots", cheapestNeverBorrowed().equals(Optional.of("Les Robots")));
        ExerciseChecker.check("19 maxDelayByMonth == {1=3, 2=10, 3=5}", maxDelayByMonth().toString().equals("{1=3, 2=10, 3=5}"));
        ExerciseChecker.check("20 summaryLine == 8 emprunts, 19 jours de retard", summaryLine().equals("8 emprunts, 19 jours de retard"));

        ExerciseChecker.summary();
    }
}
