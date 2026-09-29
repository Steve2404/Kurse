package ch10_streams.solutions;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 21. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise21_GroupingPartitioningAdvanced.
 */
public class Solution21_GroupingPartitioningAdvanced {

    public record Employee(String name, String department, String city, double salary) {
    }

    public static TreeMap<String, List<String>> namesByDept(List<Employee> employees) {
        // mapping en aval : on range le NOM de chaque employe, pas l'employe entier.
        return employees.stream().collect(Collectors.groupingBy(
                Employee::department, TreeMap::new,
                Collectors.mapping(Employee::name, Collectors.toList())));
    }

    public static TreeMap<String, TreeMap<String, Long>> countByDeptAndCity(List<Employee> employees) {
        // L'aval du 1er groupingBy est un 2e groupingBy : des tiroirs dans des tiroirs.
        return employees.stream().collect(Collectors.groupingBy(
                Employee::department, TreeMap::new,
                Collectors.groupingBy(Employee::city, TreeMap::new, Collectors.counting())));
    }

    public static TreeMap<String, String> topEarnerByDept(List<Employee> employees) {
        // maxBy rend un Optional par groupe ; collectingAndThen l'ouvre juste apres la collecte.
        return employees.stream().collect(Collectors.groupingBy(
                Employee::department, TreeMap::new,
                Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparingDouble(Employee::salary)),
                        opt -> opt.map(Employee::name).orElse("?"))));
    }

    public static TreeMap<String, Double> averageSalaryByCity(List<Employee> employees) {
        // Meme structure, autre classifieur (la ville) et autre aval (la moyenne).
        return employees.stream().collect(Collectors.groupingBy(
                Employee::city, TreeMap::new, Collectors.averagingDouble(Employee::salary)));
    }

    public static TreeMap<String, TreeSet<String>> citiesByDept(List<Employee> employees) {
        // toCollection(TreeSet::new) : sans doublon ET trie (toSet ne garantit aucun ordre).
        return employees.stream().collect(Collectors.groupingBy(
                Employee::department, TreeMap::new,
                Collectors.mapping(Employee::city, Collectors.toCollection(TreeSet::new))));
    }

    public static TreeMap<String, List<String>> highEarnersByDept(List<Employee> employees, double threshold) {
        // filtering EN AVAL : chaque departement garde sa cle, meme avec une liste vide.
        // Un filter AVANT groupingBy ferait disparaitre les departements sans gros salaire.
        return employees.stream().collect(Collectors.groupingBy(
                Employee::department, TreeMap::new,
                Collectors.filtering(e -> e.salary() >= threshold,
                        Collectors.mapping(Employee::name, Collectors.toList()))));
    }

    public static Map<Boolean, Long> payrollSplit(List<Employee> employees, double threshold) {
        // partitioningBy garde toujours les 2 cles, meme a 0.
        return employees.stream().collect(Collectors.partitioningBy(
                e -> e.salary() >= threshold, Collectors.counting()));
    }

    public static TreeMap<String, Map<Boolean, String>> salaryBandsByDept(List<Employee> employees, double threshold) {
        // groupingBy -> partitioningBy -> mapping -> joining : des avals dans des avals.
        return employees.stream().collect(Collectors.groupingBy(
                Employee::department, TreeMap::new,
                Collectors.partitioningBy(e -> e.salary() >= threshold,
                        Collectors.mapping(Employee::name, Collectors.joining(",")))));
    }

    public static List<String> departmentTotalsSortedDesc(List<Employee> employees) {
        // Une Map ne se trie pas par VALEUR : on refait un stream sur entrySet() et on trie.
        // Le type explicite <String, Double> aide l'inference avant reversed().
        Map<String, Double> totals = employees.stream().collect(Collectors.groupingBy(
                Employee::department, Collectors.summingDouble(Employee::salary)));
        return totals.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(e -> e.getKey() + "=" + e.getValue())
                .toList();
    }
}
