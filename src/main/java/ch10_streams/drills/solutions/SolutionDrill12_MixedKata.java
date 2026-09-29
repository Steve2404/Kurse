package ch10_streams.drills.solutions;

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
 * Corrige du drill 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill12_MixedKata.
 */
public class SolutionDrill12_MixedKata {

    public static List<String> titlesOfMember(String name) {
        // Jointure par recherche : emprunt -> membre (filtre sur le nom) -> livre -> titre.
        return Library.LOANS.stream()
                .filter(l -> Library.member(l.memberId()).name().equals(name))
                .map(l -> Library.book(l.isbn()).title())
                .toList();
    }

    public static List<String> membersWhoNeverBorrowed() {
        // On calcule d'abord l'ensemble des emprunteurs (un Set : contains est rapide).
        Set<String> borrowers = Library.LOANS.stream().map(Loan::memberId).collect(Collectors.toSet());
        return Library.MEMBERS.stream().filter(m -> !borrowers.contains(m.id())).map(Member::name).toList();
    }

    public static Optional<String> mostBorrowedTitle() {
        // Compter par isbn, puis prendre l'entree de plus grande valeur.
        return Library.LOANS.stream()
                .collect(Collectors.groupingBy(Loan::isbn, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> Library.book(e.getKey()).title());
    }

    public static Optional<String> memberWithMostLateDays() {
        // Meme motif : somme par membre, puis max sur les entrees.
        return Library.LOANS.stream()
                .collect(Collectors.groupingBy(Loan::memberId, Collectors.summingInt(Loan::daysLate)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> Library.member(e.getKey()).name());
    }

    public static List<String> lateLoansDescription() {
        // filter, tri decroissant, puis formatage avec deux recherches.
        return Library.LOANS.stream()
                .filter(l -> l.daysLate() > 0)
                .sorted(Comparator.comparingInt(Loan::daysLate).reversed())
                .map(l -> Library.member(l.memberId()).name() + ":" + Library.book(l.isbn()).title() + ":" + l.daysLate())
                .toList();
    }

    public static TreeMap<String, Double> borrowedValueByMember() {
        // La cle et la valeur sont toutes deux obtenues par recherche.
        return Library.LOANS.stream().collect(Collectors.groupingBy(
                l -> Library.member(l.memberId()).name(), TreeMap::new,
                Collectors.summingDouble(l -> Library.book(l.isbn()).price())));
    }

    public static TreeMap<String, TreeSet<String>> genresBorrowedByMember() {
        // mapping vers le genre dans un TreeSet : trie et sans doublon.
        return Library.LOANS.stream().collect(Collectors.groupingBy(
                l -> Library.member(l.memberId()).name(), TreeMap::new,
                Collectors.mapping(l -> Library.book(l.isbn()).genre(), Collectors.toCollection(TreeSet::new))));
    }

    public static List<String> neverBorrowedTitles() {
        // Le contraire de membersWhoNeverBorrowed, cote livres.
        Set<String> borrowed = Library.LOANS.stream().map(Loan::isbn).collect(Collectors.toSet());
        return Library.BOOKS.stream().filter(b -> !borrowed.contains(b.isbn())).map(Book::title).toList();
    }

    public static String loansPerMonthLine() {
        // 2 temps : on compte par mois (TreeMap), puis on formate les entrees avec joining.
        return Library.LOANS.stream()
                .collect(Collectors.groupingBy(Loan::month, TreeMap::new, Collectors.counting()))
                .entrySet().stream()
                .map(e -> "M" + e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(", "));
    }

    public static List<String> adultEmails() {
        // Une Optional par membre, puis Optional::stream retire les emails inutilisables.
        return Library.MEMBERS.stream()
                .filter(m -> m.age() >= 18)
                .map(m -> Optional.ofNullable(m.email()).map(String::strip).filter(e -> !e.isEmpty()))
                .flatMap(Optional::stream)
                .sorted()
                .toList();
    }

    public static OptionalDouble averageAgeOfBorrowers() {
        // distinct sur les id : un membre qui emprunte 3 fois ne compte qu'une fois.
        return Library.LOANS.stream()
                .map(Loan::memberId)
                .distinct()
                .mapToInt(id -> Library.member(id).age())
                .average();
    }

    public static Optional<String> firstLateTitleInMonth(int month) {
        // findFirst rend une boite, transformee en titre seulement si elle est pleine.
        return Library.LOANS.stream()
                .filter(l -> l.month() == month && l.daysLate() > 0)
                .findFirst()
                .map(l -> Library.book(l.isbn()).title());
    }

    public static List<String> borrowedAuthors() {
        // distinct PUIS sorted : sans doublon et trie.
        return Library.LOANS.stream().map(l -> Library.book(l.isbn()).author()).distinct().sorted().toList();
    }

    public static List<String> topBorrowers(int n) {
        // Tri par nombre decroissant, puis par nom pour departager, puis limit(n).
        return Library.LOANS.stream()
                .collect(Collectors.groupingBy(l -> Library.member(l.memberId()).name(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    public static boolean allLatecomersAreAdults() {
        // allMatch sur les seuls emprunts en retard.
        return Library.LOANS.stream()
                .filter(l -> l.daysLate() > 0)
                .allMatch(l -> Library.member(l.memberId()).age() >= 18);
    }

    public static TreeMap<String, Long> loanCountByGenre() {
        // Classifieur calcule par recherche : le genre du livre emprunte.
        return Library.LOANS.stream().collect(Collectors.groupingBy(
                l -> Library.book(l.isbn()).genre(), TreeMap::new, Collectors.counting()));
    }

    public static Map<Boolean, List<String>> membersByHasEmail() {
        // partitioningBy : les deux listes existent toujours.
        return Library.MEMBERS.stream().collect(Collectors.partitioningBy(
                m -> m.email() != null && !m.email().isBlank(),
                Collectors.mapping(Member::name, Collectors.toList())));
    }

    public static Optional<String> cheapestNeverBorrowed() {
        // min(Comparator) sur les livres jamais empruntes.
        Set<String> borrowed = Library.LOANS.stream().map(Loan::isbn).collect(Collectors.toSet());
        return Library.BOOKS.stream()
                .filter(b -> !borrowed.contains(b.isbn()))
                .min(Comparator.comparingDouble(Book::price))
                .map(Book::title);
    }

    public static TreeMap<Integer, Integer> maxDelayByMonth() {
        // maxBy en aval rend un Optional : collectingAndThen l'ouvre.
        return Library.LOANS.stream().collect(Collectors.groupingBy(
                Loan::month, TreeMap::new,
                Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparingInt(Loan::daysLate)),
                        o -> o.map(Loan::daysLate).orElse(0))));
    }

    public static String summaryLine() {
        // teeing : compter ET additionner en un seul collect.
        return Library.LOANS.stream().collect(Collectors.teeing(
                Collectors.counting(),
                Collectors.summingInt(Loan::daysLate),
                (n, late) -> n + " emprunts, " + late + " jours de retard"));
    }
}
