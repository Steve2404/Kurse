package lambdas.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise03_PredicateComposition.
 */
public class Solution03_PredicateComposition {

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
            count++;
            return true;
        }

        int getCallCount() {
            return count;
        }
    }

    public static Predicate<Employee> worksInDepartment(String dept) {
        return employee -> employee.department.equals(dept);
    }

    public static Predicate<Employee> salaryAtLeast(double min) {
        return employee -> employee.salary >= min;
    }

    public static List<Employee> filterEmployees(List<Employee> employees, Predicate<Employee> criteria) {
        List<Employee> result = new ArrayList<>();
        for (Employee employee : employees) {
            if (criteria.test(employee)) {
                result.add(employee);
            }
        }
        return result;
    }
}
