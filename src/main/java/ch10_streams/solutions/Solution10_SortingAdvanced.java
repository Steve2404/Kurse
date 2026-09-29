package ch10_streams.solutions;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise10_SortingAdvanced.
 */
public class Solution10_SortingAdvanced {

    public record Employee(String name, String department, double salary, int seniority) {
    }

    public static List<String> sortByDeptThenSalaryDesc(List<Employee> employees) {
        // reversed() n'est applique QU'au comparateur de salaire : sur toute la chaine, il
        // inverserait aussi l'ordre des departements.
        Comparator<Employee> cmp = Comparator.comparing(Employee::department)
                .thenComparing(Comparator.comparingDouble(Employee::salary).reversed())
                .thenComparing(Employee::name);
        return employees.stream().sorted(cmp).map(Employee::name).toList();
    }

    public static List<String> topNBySalary(List<Employee> employees, int n) {
        // sorted PUIS limit : dans l'autre ordre, on prendrait n elements au hasard avant de trier.
        Comparator<Employee> cmp = Comparator.comparingDouble(Employee::salary).reversed()
                .thenComparing(Employee::name);
        return employees.stream().sorted(cmp).limit(n).map(Employee::name).toList();
    }

    public static List<String> sortWithNullsLast(List<String> words) {
        // nullsLast emballe l'ordre naturel : sans lui, comparer null lancerait une NPE.
        // toList() (Java 16) accepte les null, contrairement a List.of.
        return words.stream().sorted(Comparator.nullsLast(Comparator.naturalOrder())).toList();
    }

    public static List<String> caseInsensitiveThenNatural(List<String> words) {
        // CASE_INSENSITIVE_ORDER rend "Banane" et "banane" egaux : l'ordre naturel les departage.
        return words.stream()
                .sorted(String.CASE_INSENSITIVE_ORDER.thenComparing(Comparator.naturalOrder()))
                .toList();
    }

    public static Optional<String> mostSeniorIn(List<Employee> employees, String department) {
        // max(Comparator) fait un seul passage (pas besoin de trier) et rend une boite :
        // le departement peut etre vide.
        return employees.stream()
                .filter(e -> e.department().equals(department))
                .max(Comparator.comparingInt(Employee::seniority))
                .map(Employee::name);
    }

    public static List<String> byLengthDescThenAlpha(List<String> words) {
        // comparing(cle, comparateurDeCle) : une autre facon d'inverser UN SEUL critere.
        return words.stream()
                .sorted(Comparator.comparing(String::length, Comparator.<Integer>reverseOrder())
                        .thenComparing(Comparator.naturalOrder()))
                .toList();
    }
}
