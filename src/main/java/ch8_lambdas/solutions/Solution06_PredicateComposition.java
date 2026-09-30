package ch8_lambdas.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise06_PredicateComposition.
 */
public class Solution06_PredicateComposition {

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
            // On compte les appels : and() et or() court-circuitent, le 2e predicat n'est pas toujours appele.
            count++;
            return true;
        }

        int getCallCount() {
            return count;
        }
    }

    public static Predicate<Employee> worksInDepartment(String dept) {
        // Fabrique de Predicate : dept est capture (effectivement final).
        return employee -> employee.department.equals(dept);
    }

    public static Predicate<Employee> salaryAtLeast(double min) {
        // Meme principe, avec un seuil capture.
        return employee -> employee.salary >= min;
    }

    public static List<Employee> filterEmployees(List<Employee> employees, Predicate<Employee> criteria) {
        // Le critere est une donnee : n'importe quelle composition de Predicate convient.
        List<Employee> result = new ArrayList<>();
        for (Employee employee : employees) {
            if (criteria.test(employee)) {
                result.add(employee);
            }
        }
        return result;
    }
}
