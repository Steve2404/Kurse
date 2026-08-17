package lambdas.exercises;

import lambdas.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * EXERCICE 3 - Composer des Predicate : filtrage multi-criteres et evaluation paresseuse (niveau : difficile)
 * ==================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 *
 * ==================================================================
 * TODO 1 et 2 : worksInDepartment(dept), salaryAtLeast(min)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que tu dois trier une pile de fiches d'employes, et que la
 * directrice te donne ses criteres au fur et a mesure, jamais
 * d'avance. Plutot que d'ecrire un immense "si... et si... et si..."
 * fige, tu prefererais avoir des petites REGLES REUTILISABLES toutes
 * pretes ("travaille au service X", "gagne au moins Y"), que tu
 * pourrais assembler a la demande, dans n'importe quelle combinaison.
 *
 * C'est ca, une USINE a Predicate : une methode qui NE TESTE RIEN
 * elle-meme, mais qui FABRIQUE et RENVOIE un Predicate<Employee> tout
 * pret a etre teste plus tard (ou combine avec d'autres).
 *
 * -- Le plan --
 *
 *   1. worksInDepartment(dept) renvoie un Predicate qui, teste sur un
 *      Employee, regarde si son department est EGAL a dept.
 *   2. salaryAtLeast(min) renvoie un Predicate qui regarde si le
 *      salary de l'Employee est >= min.
 *
 *
 * ==================================================================
 * TODO 3 : filterEmployees(employees, criteria)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Preparer une liste resultat vide.
 *   2. Pour chaque employe, s'il passe le test du Predicate recu,
 *      l'ajouter au resultat.
 *
 *
 * ==================================================================
 * TODO 4 : CountingPredicate<T> - comprendre le court-circuit
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un juge de concours doit verifier DEUX epreuves pour qualifier un
 * candidat : "a-t-il reussi l'epreuve A ?" ET "a-t-il reussi l'epreuve
 * B ?". Si l'epreuve A est deja RATEE, le juge n'a AUCUNE raison de
 * se deplacer jusqu'au terrain de l'epreuve B : le candidat est
 * disqualifie de toute facon, peu importe B. C'est ca, le "court-
 * circuit" de and() : des que le premier test dit FAUX, le second
 * n'est JAMAIS execute.
 *
 * CountingPredicate<T> est un espion : chaque fois que son test() est
 * REELLEMENT appele (pas juste "prevu"), il incremente un compteur.
 * En regardant ce compteur APRES coup, on peut PROUVER, avec des
 * chiffres, que le court-circuit a vraiment eu lieu - pas juste le
 * croire sur parole.
 *
 * -- Essayons a la main --
 *
 * alwaysFalse = un Predicate qui repond toujours FAUX.
 * spy = un CountingPredicate qui repond toujours VRAI, et compte ses
 * appels.
 *
 * Test 1 : alwaysFalse.and(spy).test(unEmploye)
 *   -> and() regarde d'abord alwaysFalse -> FAUX -> s'arrete la, ne
 *   regarde JAMAIS spy. Compteur de spy apres ce test : 0.
 *
 * Test 2 : spy.and(alwaysFalse).test(unEmploye)
 *   -> and() regarde d'abord spy (VRAI, et le compteur passe a 1),
 *   PUIS regarde alwaysFalse (FAUX) -> resultat final FAUX, mais
 *   cette fois spy A ETE appele. Compteur de spy apres ce test : 1.
 *
 * Le resultat final (FAUX) est identique dans les deux tests, mais le
 * nombre d'appels a spy est different : 0 contre 1. C'est la preuve
 * concrete du court-circuit, et de l'importance de l'ORDRE dans
 * and()/or().
 *
 * -- Le plan pour CountingPredicate<T> --
 *
 *   1. Implementer Predicate<T> (pas juste stocker un lambda a
 *      l'interieur : c'est TOI qui dois etre le Predicate).
 *   2. Garder un compteur interne, demarrant a 0.
 *   3. A chaque appel de test(value) : incrementer le compteur, PUIS
 *      renvoyer le resultat voulu (ici, toujours VRAI pour cet
 *      exercice).
 *   4. Fournir une methode getCallCount() pour lire le compteur
 *      depuis l'exterieur.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * CountingPredicate merite sa PROPRE CLASSE (pas juste un lambda),
 * car elle a besoin d'un ETAT qui survit entre les appels (le
 * compteur) - un lambda tout seul ne peut pas se souvenir de son
 * propre historique d'appels comme ca.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - static Predicate<Employee> worksInDepartment(String dept) {
 *         return employee -> employee.department.equals(dept);
 *     }
 *   - CountingPredicate implements Predicate<T> { int count = 0;
 *         public boolean test(T value) { count++; return true; } }
 *   - Pour combiner plusieurs criteres avec un ordre precis
 *     ("IT ET salaire eleve, OU manager"), extraire les etapes
 *     intermediaires dans des variables plutot que tout enchainer sur
 *     une seule ligne, pour bien controler le groupement :
 *       Predicate<Employee> itAndHighPay = worksInDepartment("IT").and(salaryAtLeast(50000));
 *       Predicate<Employee> rule = itAndHighPay.or(isManager);
 */
public class Exercise03_PredicateComposition {

    static final class Employee {
        final String name;
        final String department;
        final double salary;
        final boolean manager;

        Employee(String name, String department, double salary, boolean manager) {
            this.name = name;
            this.department = department;
            this.salary = salary;
            this.manager = manager;
        }
    }

    static final class CountingPredicate<T> implements Predicate<T> {
        private int count = 0;

        @Override
        public boolean test(T value) {
            throw new UnsupportedOperationException("TODO 4 : implementer test()");
        }

        int getCallCount() {
            return count;
        }
    }

    public static Predicate<Employee> worksInDepartment(String dept) {
        throw new UnsupportedOperationException("TODO 1 : implementer worksInDepartment()");
    }

    public static Predicate<Employee> salaryAtLeast(double min) {
        throw new UnsupportedOperationException("TODO 2 : implementer salaryAtLeast()");
    }

    public static List<Employee> filterEmployees(List<Employee> employees, Predicate<Employee> criteria) {
        throw new UnsupportedOperationException("TODO 3 : implementer filterEmployees()");
    }

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", "IT", 60000, false),
                new Employee("Bob", "IT", 40000, false),
                new Employee("Chloe", "RH", 55000, true),
                new Employee("David", "Vente", 45000, false));

        Predicate<Employee> itAndHighPay = worksInDepartment("IT").and(salaryAtLeast(50000));
        Predicate<Employee> isManager = e -> e.manager;
        Predicate<Employee> rule = itAndHighPay.or(isManager);

        List<Employee> matched = filterEmployees(employees, rule);
        ExerciseChecker.check("regle (IT et salaire>=50000) ou manager -> Alice et Chloe",
                matched.size() == 2 && matched.get(0).name.equals("Alice") && matched.get(1).name.equals("Chloe"));

        Predicate<Employee> alwaysFalse = e -> false;
        CountingPredicate<Employee> spy = new CountingPredicate<>();

        alwaysFalse.and(spy).test(employees.get(0));
        ExerciseChecker.check("court-circuit : alwaysFalse.and(spy) n'appelle JAMAIS spy",
                spy.getCallCount() == 0);

        spy.and(alwaysFalse).test(employees.get(0));
        ExerciseChecker.check("sans court-circuit : spy.and(alwaysFalse) appelle spy une fois",
                spy.getCallCount() == 1);

        ExerciseChecker.summary();
    }
}
