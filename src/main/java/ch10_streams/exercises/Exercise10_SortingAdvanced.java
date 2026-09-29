package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * EXERCICE 10 - Trier comme un pro : Comparator enchaines, reversed(), nullsLast, CASE_INSENSITIVE_ORDER, min/max (niveau : difficile)
 * ===================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_OptionalBasics.java.
 *
 * -- Les donnees --
 *
 *   Employee(name, department, salary, seniority)
 *   Alice  IT     5000.0  3 ans
 *   Bob    IT     6000.0  5 ans
 *   Chloe  RH     4000.0  8 ans
 *   Dan    IT     5000.0  1 an
 *   Emma   RH     4500.0  2 ans
 *   Farid  Ventes 3000.0 10 ans
 *
 * Employee n'implemente PAS Comparable : sorted() SANS argument lance
 * une ClassCastException a l'execution (main() le montre). Il faut
 * donc toujours donner un Comparator.
 *
 *
 * ==================================================================
 * TODO 1 : sortByDeptThenSalaryDesc(employees)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour ranger la classe pour la photo : d'abord par groupe (departement,
 * A -> Z), puis DANS chaque groupe, les plus grands salaires devant, et
 * en cas d'egalite de salaire, par prenom (A -> Z). Chaque
 * thenComparing est un "et si c'est egal, alors regarde ca".
 *
 * LE piege classique : reversed() s'applique a TOUT le comparateur
 * construit jusque-la, pas seulement au dernier critere.
 *   comparing(dept).thenComparingDouble(salary).reversed()
 * inverse AUSSI l'ordre des departements (Ventes, RH, IT) ! Il faut
 * inverser SEULEMENT le critere salaire, dans son propre comparateur :
 *   thenComparing(Comparator.comparingDouble(salary).reversed())
 *
 * -- Essayons a la main --
 *
 *   IT     : Bob 6000, puis Alice 5000 et Dan 5000 (egalite -> Alice, Dan)
 *   RH     : Emma 4500, Chloe 4000
 *   Ventes : Farid
 *   -> [Bob, Alice, Dan, Emma, Chloe, Farid]
 *
 * -- Le plan --
 *
 *   1. Construire le comparateur : departement croissant, puis salaire
 *      decroissant, puis nom croissant.
 *   2. Trier avec, garder les noms, rassembler en liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Le comparateur peut etre range dans une variable locale bien nommee :
 * ca rend le pipeline lisible.
 *
 *
 * ==================================================================
 * TODO 2 : topNBySalary(employees, n)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le podium : les n meilleurs salaires, du plus grand au plus petit ;
 * a egalite, par nom. C'est sorted + limit (et PAS limit + sorted : on
 * prendrait les n premiers au hasard de la liste, puis on les
 * trierait).
 *
 * -- Essayons a la main --
 *
 *   salaires decroissants : Bob 6000, Alice 5000, Dan 5000, Emma 4500, ...
 *   n = 3 -> [Bob, Alice, Dan]
 *
 * -- Le plan --
 *
 *   1. Trier par salaire decroissant puis nom croissant.
 *   2. Garder les n premiers, puis leurs noms.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : sortWithNullsLast(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La liste contient un "trou" (null). L'ordre naturel appellerait
 * null.compareTo(...) -> NullPointerException. Comparator.nullsLast(c)
 * est un emballage qui dit "les trous vont a la fin, et pour le reste,
 * utilise c".
 *
 * Deuxieme piege : l'ordre NATUREL des String compare les codes des
 * caracteres, et TOUTES les majuscules (A=65...Z=90) passent AVANT
 * toutes les minuscules (a=97...). Donc "Banane" < "abricot" !
 *
 * -- Essayons a la main --
 *
 *   ["poire", null, "abricot", "Banane"]
 *   -> [Banane, abricot, poire, null]
 *
 * -- Le plan --
 *
 *   1. Trier avec "ordre naturel, nulls a la fin".
 *   2. Rassembler en liste (toList() accepte les null ; List.of non).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : caseInsensitiveThenNatural(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Maintenant on veut un ordre "comme dans un dictionnaire" : on ignore
 * les majuscules (String.CASE_INSENSITIVE_ORDER). Mais alors "Banane"
 * et "banane" sont EGAUX, et l'ordre entre eux n'est pas precise. Pour
 * que le resultat soit toujours le meme, on departage par l'ordre
 * naturel (majuscule d'abord).
 *
 * -- Essayons a la main --
 *
 *   ["poire", "Banane", "abricot", "banane"]
 *   sans casse : abricot < banane = Banane < poire
 *   departage  : "Banane" < "banane"
 *   -> [abricot, Banane, banane, poire]
 *
 * -- Le plan --
 *
 *   1. Comparateur : insensible a la casse, puis ordre naturel.
 *   2. Trier, rassembler.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : mostSeniorIn(employees, department)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut le doyen d'un departement. Pas besoin de TOUT trier pour
 * trouver le plus grand : max(comparateur) fait un seul passage. Il
 * rend un Optional, car un departement peut etre vide (ou inexistant).
 * Attention : sur un Stream<T>, max() et min() EXIGENT un Comparator
 * (contrairement a IntStream.max()).
 *
 * -- Essayons a la main --
 *
 *   IT : Alice 3, Bob 5, Dan 1 -> Bob
 *   RH : Chloe 8, Emma 2       -> Chloe
 *   Marketing : personne       -> Optional.empty
 *
 * -- Le plan --
 *
 *   1. Garder les employes du departement.
 *   2. Prendre le max par anciennete.
 *   3. Transformer la boite en boite de nom.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : byLengthDescThenAlpha(words)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Comparator.comparing a une version a DEUX arguments : "quelle
 * caracteristique regarder" ET "comment comparer cette
 * caracteristique". C'est une autre facon d'inverser UN SEUL critere :
 *   Comparator.comparing(String::length, Comparator.reverseOrder())
 *
 * -- Essayons a la main --
 *
 *   ["kiwi", "fig", "banana", "pear", "apple"]
 *   longueurs : kiwi 4, fig 3, banana 6, pear 4, apple 5
 *   -> [banana, apple, kiwi, pear, fig]   (kiwi/pear : alphabetique)
 *
 * -- Le plan --
 *
 *   1. Comparateur : longueur decroissante (version a 2 arguments),
 *      puis ordre naturel.
 *   2. Trier, rassembler.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier :
 *
 *   sortByDeptThenSalaryDesc == [Bob, Alice, Dan, Emma, Chloe, Farid]
 *   topNBySalary(3) == [Bob, Alice, Dan]
 *   sortWithNullsLast([poire, null, abricot, Banane]) == [Banane, abricot, poire, null]
 *   caseInsensitiveThenNatural([poire, Banane, abricot, banane]) == [abricot, Banane, banane, poire]
 *   mostSeniorIn(IT) == Optional[Bob] ; (RH) == Optional[Chloe] ; (Marketing) == vide
 *   byLengthDescThenAlpha([kiwi, fig, banana, pear, apple]) == [banana, apple, kiwi, pear, fig]
 *   (et main() montre que sorted() sans Comparator sur des Employee lance ClassCastException)
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Comparator.comparing(Employee::department)
 *         .thenComparing(Comparator.comparingDouble(Employee::salary).reversed())
 *         .thenComparing(Employee::name)
 *   - sorted(cmp).limit(n).map(Employee::name).toList()
 *   - Comparator.nullsLast(Comparator.naturalOrder())
 *     (parfois il faut aider l'inference : Comparator.<String>naturalOrder())
 *   - String.CASE_INSENSITIVE_ORDER.thenComparing(Comparator.naturalOrder())
 *   - max(Comparator.comparingInt(Employee::seniority)).map(Employee::name)
 *   - Comparator.comparing(String::length, Comparator.reverseOrder())
 */
public class Exercise10_SortingAdvanced {

    public record Employee(String name, String department, double salary, int seniority) {
    }

    public static List<String> sortByDeptThenSalaryDesc(List<Employee> employees) {
        throw new UnsupportedOperationException("TODO 1 : implementer sortByDeptThenSalaryDesc()");
    }

    public static List<String> topNBySalary(List<Employee> employees, int n) {
        throw new UnsupportedOperationException("TODO 2 : implementer topNBySalary()");
    }

    public static List<String> sortWithNullsLast(List<String> words) {
        throw new UnsupportedOperationException("TODO 3 : implementer sortWithNullsLast()");
    }

    public static List<String> caseInsensitiveThenNatural(List<String> words) {
        throw new UnsupportedOperationException("TODO 4 : implementer caseInsensitiveThenNatural()");
    }

    public static Optional<String> mostSeniorIn(List<Employee> employees, String department) {
        throw new UnsupportedOperationException("TODO 5 : implementer mostSeniorIn()");
    }

    public static List<String> byLengthDescThenAlpha(List<String> words) {
        throw new UnsupportedOperationException("TODO 6 : implementer byLengthDescThenAlpha()");
    }

    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee("Alice", "IT", 5000.0, 3),
                new Employee("Bob", "IT", 6000.0, 5),
                new Employee("Chloe", "RH", 4000.0, 8),
                new Employee("Dan", "IT", 5000.0, 1),
                new Employee("Emma", "RH", 4500.0, 2),
                new Employee("Farid", "Ventes", 3000.0, 10));

        boolean threw = false;
        try {
            employees.stream().sorted().toList();
        } catch (ClassCastException e) {
            threw = true;
        }
        ExerciseChecker.check("(demo) sorted() sans Comparator sur Employee lance ClassCastException", threw);

        ExerciseChecker.check("sortByDeptThenSalaryDesc == [Bob, Alice, Dan, Emma, Chloe, Farid]",
                sortByDeptThenSalaryDesc(employees).equals(List.of("Bob", "Alice", "Dan", "Emma", "Chloe", "Farid")));

        ExerciseChecker.check("topNBySalary(3) == [Bob, Alice, Dan]",
                topNBySalary(employees, 3).equals(List.of("Bob", "Alice", "Dan")));

        ExerciseChecker.check("sortWithNullsLast == [Banane, abricot, poire, null]",
                sortWithNullsLast(Arrays.asList("poire", null, "abricot", "Banane"))
                        .equals(Arrays.asList("Banane", "abricot", "poire", null)));

        ExerciseChecker.check("caseInsensitiveThenNatural == [abricot, Banane, banane, poire]",
                caseInsensitiveThenNatural(List.of("poire", "Banane", "abricot", "banane"))
                        .equals(List.of("abricot", "Banane", "banane", "poire")));

        ExerciseChecker.check("mostSeniorIn(IT) == Bob", mostSeniorIn(employees, "IT").equals(Optional.of("Bob")));
        ExerciseChecker.check("mostSeniorIn(RH) == Chloe", mostSeniorIn(employees, "RH").equals(Optional.of("Chloe")));
        ExerciseChecker.check("mostSeniorIn(Marketing) est vide", mostSeniorIn(employees, "Marketing").isEmpty());

        ExerciseChecker.check("byLengthDescThenAlpha == [banana, apple, kiwi, pear, fig]",
                byLengthDescThenAlpha(List.of("kiwi", "fig", "banana", "pear", "apple"))
                        .equals(List.of("banana", "apple", "kiwi", "pear", "fig")));

        ExerciseChecker.summary();
    }
}
