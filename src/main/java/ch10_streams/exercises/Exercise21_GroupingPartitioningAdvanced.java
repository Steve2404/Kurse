package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * EXERCICE 21 - groupingBy / partitioningBy en profondeur : collecteurs en aval, groupements imbriques, filtering vs filter (niveau : difficile)
 * ===========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Les donnees --
 *
 *   Employee(name, department, city, salary)
 *   Alice  IT      Lyon   5200.0
 *   Bob    IT      Paris  6100.0
 *   Chloe  RH      Lyon   3800.0
 *   Dan    IT      Lyon   4800.0
 *   Emma   RH      Paris  4500.0
 *   Farid  Ventes  Nice   3100.0
 *   Gina   Ventes  Nice   3300.0
 *
 * -- La forme generale a maitriser --
 *
 *   groupingBy(classifieur)                          -> Map<K, List<T>> (HashMap)
 *   groupingBy(classifieur, aval)                    -> Map<K, resultat de l'aval>
 *   groupingBy(classifieur, fabriqueDeMap, aval)     -> la Map de TON choix
 *   partitioningBy(predicat [, aval])                -> Map<Boolean, ...> avec
 *                                                       TOUJOURS les 2 cles
 *
 * "aval" (downstream) = un AUTRE Collector applique a chaque groupe :
 * counting(), mapping(f, aval2), filtering(p, aval2), maxBy(cmp),
 * averagingDouble(f), summingDouble(f), joining(), toSet(),
 * toCollection(fabrique), collectingAndThen(aval2, finisseur), et...
 * un autre groupingBy ou partitioningBy (groupement imbrique) !
 *
 * Dans ce fichier, les Map de resultat sont des TreeMap pour que leur
 * ordre (et leur toString) soit toujours le meme.
 *
 *
 * ==================================================================
 * TODO 1 : namesByDept(employees)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * groupingBy(Employee::department) tout seul ferait des tas
 * d'EMPLOYES. On ne veut que leurs NOMS dans chaque tas : mapping(f,
 * aval) transforme chaque element du tas AVANT de le ranger.
 *
 * -- Essayons a la main --
 *
 *   {IT=[Alice, Bob, Dan], RH=[Chloe, Emma], Ventes=[Farid, Gina]}
 *
 * -- Le plan --
 *
 *   1. Grouper par departement, dans une TreeMap.
 *   2. En aval : transformer en nom, collecter en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : countByDeptAndCity(employees)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Des tiroirs dans des tiroirs : d'abord par departement, puis DANS
 * chaque departement, par ville, et dans chaque petit tiroir on compte.
 * L'aval du premier groupingBy est... un deuxieme groupingBy.
 *
 * -- Essayons a la main --
 *
 *   IT : Lyon (Alice, Dan) = 2, Paris (Bob) = 1
 *   RH : Lyon (Chloe) = 1, Paris (Emma) = 1
 *   Ventes : Nice (Farid, Gina) = 2
 *   -> {IT={Lyon=2, Paris=1}, RH={Lyon=1, Paris=1}, Ventes={Nice=2}}
 *
 * -- Le plan --
 *
 *   1. Grouper par departement (TreeMap).
 *   2. En aval : grouper par ville (TreeMap), en aval : compter.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. Remarque le type : Map<String, Map<String, Long>> (counting rend
 * un Long, pas un Integer).
 *
 *
 * ==================================================================
 * TODO 3 : topEarnerByDept(employees)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * maxBy(cmp) en aval rend un Optional<Employee> par groupe (le Collector
 * ne sait pas qu'un groupe n'est jamais vide). On veut juste le NOM :
 * collectingAndThen(maxBy(...), opt -> ...) ouvre la boite juste apres
 * la collecte de chaque groupe.
 *
 * -- Essayons a la main --
 *
 *   IT : Bob (6100) ; RH : Emma (4500) ; Ventes : Gina (3300)
 *   -> {IT=Bob, RH=Emma, Ventes=Gina}
 *
 * -- Le plan --
 *
 *   1. Grouper par departement (TreeMap).
 *   2. En aval : max par salaire, PUIS transformer la boite en nom.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : averageSalaryByCity(employees)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On change juste le classifieur (la ville) et l'aval (moyenne).
 *
 * -- Essayons a la main --
 *
 *   Lyon  : (5200 + 3800 + 4800) / 3 = 4600.0
 *   Nice  : (3100 + 3300) / 2 = 3200.0
 *   Paris : (6100 + 4500) / 2 = 5300.0
 *   -> {Lyon=4600.0, Nice=3200.0, Paris=5300.0}
 *
 * -- Le plan --
 *
 *   1. Grouper par ville (TreeMap), en aval : moyenne des salaires.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : citiesByDept(employees)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour chaque departement, les villes ou il est present, SANS doublon
 * et TRIEES. toSet() donnerait un HashSet (pas d'ordre) :
 * toCollection(TreeSet::new) laisse choisir la collection.
 *
 * -- Essayons a la main --
 *
 *   {IT=[Lyon, Paris], RH=[Lyon, Paris], Ventes=[Nice]}
 *
 * -- Le plan --
 *
 *   1. Grouper par departement (TreeMap).
 *   2. En aval : transformer en ville, collecter dans un TreeSet.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : highEarnersByDept(employees, threshold)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut, pour CHAQUE departement, les noms de ceux qui gagnent au
 * moins threshold. La difference cle :
 *   - filter(...) AVANT groupingBy : les employes recales disparaissent
 *     avant le rangement -> un departement sans "gros salaire" N'A MEME
 *     PAS de tiroir.
 *   - filtering(...) EN AVAL (Java 9) : chaque departement a son tiroir,
 *     le tri se fait DEDANS -> tiroir present, mais vide.
 * Ici on veut TOUS les departements, donc filtering.
 *
 * -- Essayons a la main --
 *
 *   threshold = 5000
 *   IT : Alice (5200), Bob (6100) ; Dan (4800) recale
 *   RH : personne ; Ventes : personne
 *   -> {IT=[Alice, Bob], RH=[], Ventes=[]}
 *   (avec filter avant : {IT=[Alice, Bob]} seulement - faux ici)
 *
 * -- Le plan --
 *
 *   1. Grouper par departement (TreeMap).
 *   2. En aval : garder salaire >= threshold, puis transformer en nom,
 *      puis collecter en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : filtering(p, mapping(f, toList())) - des avals dans des avals.
 *
 *
 * ==================================================================
 * TODO 7 : payrollSplit(employees, threshold)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * partitioningBy range dans exactement 2 tiroirs (true / false) - et
 * les 2 tiroirs existent TOUJOURS, meme vides. (groupingBy avec un
 * classifieur booleen, lui, ne creerait que les tiroirs utilises.)
 *
 * -- Essayons a la main --
 *
 *   threshold 4500  -> true : Alice, Bob, Dan, Emma = 4 ; false : 3
 *                   -> {false=3, true=4}
 *   threshold 10000 -> {false=7, true=0}   (le tiroir true existe, a 0)
 *
 * -- Le plan --
 *
 *   1. Partitionner selon salaire >= threshold, en aval : compter.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 8 : salaryBandsByDept(employees, threshold)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On combine tout : par departement, on partitionne les employes en
 * "sous le seuil" / "au-dessus ou egal", et dans chaque partie on colle
 * les noms separes par ",".
 *
 * -- Essayons a la main --
 *
 *   threshold = 4500
 *   IT     : false -> "" ; true -> "Alice,Bob,Dan"
 *   RH     : false -> "Chloe" ; true -> "Emma"
 *   Ventes : false -> "Farid,Gina" ; true -> ""
 *   -> {IT={false=, true=Alice,Bob,Dan}, RH={false=Chloe, true=Emma},
 *       Ventes={false=Farid,Gina, true=}}
 *
 * -- Le plan --
 *
 *   1. Grouper par departement (TreeMap).
 *   2. En aval : partitionner par salaire >= threshold.
 *   3. En aval du partitionnement : noms joints par ",".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non, mais construis-le couche par couche en verifiant le type a
 * chaque etape : Map<String, Map<Boolean, String>>.
 *
 *
 * ==================================================================
 * TODO 9 : departmentTotalsSortedDesc(employees)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une Map n'est pas triee par VALEUR. Pour classer les departements par
 * masse salariale decroissante : on groupe (somme par departement),
 * PUIS on refait un stream sur les entrees de la Map (entrySet), qu'on
 * trie par valeur decroissante, et on formate "dept=total".
 *
 * -- Essayons a la main --
 *
 *   IT : 5200 + 6100 + 4800 = 16100.0
 *   RH : 3800 + 4500 = 8300.0
 *   Ventes : 3100 + 3300 = 6400.0
 *   -> [IT=16100.0, RH=8300.0, Ventes=6400.0]
 *
 * -- Le plan --
 *
 *   1. Grouper par departement, en aval : somme des salaires.
 *   2. Stream des entrees, trie par valeur decroissante.
 *   3. Formater chaque entree en cle + "=" + valeur, rassembler.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. (Mais c'est un pipeline en 2 temps : un collect, puis un
 * nouveau stream sur son resultat.)
 *
 *
 * Exemple a verifier : voir les "-> ..." de chaque TODO ci-dessus
 * (main() compare les toString() des TreeMap).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - groupingBy(Employee::department, TreeMap::new, mapping(Employee::name, toList()))
 *   - groupingBy(dept, TreeMap::new, groupingBy(Employee::city, TreeMap::new, counting()))
 *   - collectingAndThen(maxBy(Comparator.comparingDouble(Employee::salary)),
 *       opt -> opt.map(Employee::name).orElse("?"))
 *   - averagingDouble(Employee::salary)
 *   - mapping(Employee::city, toCollection(TreeSet::new))
 *   - filtering(e -> e.salary() >= threshold, mapping(Employee::name, toList()))
 *   - partitioningBy(e -> e.salary() >= threshold, counting())
 *   - partitioningBy(p, mapping(Employee::name, joining(",")))
 *   - map.entrySet().stream()
 *       .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
 *       .map(e -> e.getKey() + "=" + e.getValue())
 *   - (import static java.util.stream.Collectors.* raccourcit l'ecriture,
 *     sinon prefixe chaque appel par Collectors.)
 */
public class Exercise21_GroupingPartitioningAdvanced {

    public record Employee(String name, String department, String city, double salary) {
    }

    public static TreeMap<String, List<String>> namesByDept(List<Employee> employees) {
        throw new UnsupportedOperationException("TODO 1 : implementer namesByDept()");
    }

    public static TreeMap<String, TreeMap<String, Long>> countByDeptAndCity(List<Employee> employees) {
        throw new UnsupportedOperationException("TODO 2 : implementer countByDeptAndCity()");
    }

    public static TreeMap<String, String> topEarnerByDept(List<Employee> employees) {
        throw new UnsupportedOperationException("TODO 3 : implementer topEarnerByDept()");
    }

    public static TreeMap<String, Double> averageSalaryByCity(List<Employee> employees) {
        throw new UnsupportedOperationException("TODO 4 : implementer averageSalaryByCity()");
    }

    public static TreeMap<String, TreeSet<String>> citiesByDept(List<Employee> employees) {
        throw new UnsupportedOperationException("TODO 5 : implementer citiesByDept()");
    }

    public static TreeMap<String, List<String>> highEarnersByDept(List<Employee> employees, double threshold) {
        throw new UnsupportedOperationException("TODO 6 : implementer highEarnersByDept()");
    }

    public static Map<Boolean, Long> payrollSplit(List<Employee> employees, double threshold) {
        throw new UnsupportedOperationException("TODO 7 : implementer payrollSplit()");
    }

    public static TreeMap<String, Map<Boolean, String>> salaryBandsByDept(List<Employee> employees, double threshold) {
        throw new UnsupportedOperationException("TODO 8 : implementer salaryBandsByDept()");
    }

    public static List<String> departmentTotalsSortedDesc(List<Employee> employees) {
        throw new UnsupportedOperationException("TODO 9 : implementer departmentTotalsSortedDesc()");
    }

    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee("Alice", "IT", "Lyon", 5200.0),
                new Employee("Bob", "IT", "Paris", 6100.0),
                new Employee("Chloe", "RH", "Lyon", 3800.0),
                new Employee("Dan", "IT", "Lyon", 4800.0),
                new Employee("Emma", "RH", "Paris", 4500.0),
                new Employee("Farid", "Ventes", "Nice", 3100.0),
                new Employee("Gina", "Ventes", "Nice", 3300.0));

        ExerciseChecker.check("namesByDept",
                namesByDept(employees).toString().equals("{IT=[Alice, Bob, Dan], RH=[Chloe, Emma], Ventes=[Farid, Gina]}"));

        ExerciseChecker.check("countByDeptAndCity",
                countByDeptAndCity(employees).toString().equals("{IT={Lyon=2, Paris=1}, RH={Lyon=1, Paris=1}, Ventes={Nice=2}}"));

        ExerciseChecker.check("topEarnerByDept",
                topEarnerByDept(employees).toString().equals("{IT=Bob, RH=Emma, Ventes=Gina}"));

        ExerciseChecker.check("averageSalaryByCity",
                averageSalaryByCity(employees).toString().equals("{Lyon=4600.0, Nice=3200.0, Paris=5300.0}"));

        ExerciseChecker.check("citiesByDept",
                citiesByDept(employees).toString().equals("{IT=[Lyon, Paris], RH=[Lyon, Paris], Ventes=[Nice]}"));

        ExerciseChecker.check("highEarnersByDept(5000) garde les tiroirs vides",
                highEarnersByDept(employees, 5000).toString().equals("{IT=[Alice, Bob], RH=[], Ventes=[]}"));

        ExerciseChecker.check("payrollSplit(4500) == {false=3, true=4}",
                payrollSplit(employees, 4500).equals(Map.of(false, 3L, true, 4L)));
        ExerciseChecker.check("payrollSplit(10000) == {false=7, true=0} (les 2 cles existent)",
                payrollSplit(employees, 10000).equals(Map.of(false, 7L, true, 0L)));

        ExerciseChecker.check("salaryBandsByDept(4500)",
                salaryBandsByDept(employees, 4500).toString().equals(
                        "{IT={false=, true=Alice,Bob,Dan}, RH={false=Chloe, true=Emma}, Ventes={false=Farid,Gina, true=}}"));

        ExerciseChecker.check("departmentTotalsSortedDesc == [IT=16100.0, RH=8300.0, Ventes=6400.0]",
                departmentTotalsSortedDesc(employees).equals(List.of("IT=16100.0", "RH=8300.0", "Ventes=6400.0")));

        ExerciseChecker.summary();
    }
}
