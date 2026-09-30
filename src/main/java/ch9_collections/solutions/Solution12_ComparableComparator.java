package ch9_collections.solutions;

import java.util.Comparator;

/**
 * Corrige de l'exercice 12.
 */
public class Solution12_ComparableComparator {

    static class Employee implements Comparable<Employee> {
        private final int id;
        private final String name;
        private final String department;
        private final double salary;

        Employee(int id, String name, String department, double salary) {
            this.id = id;
            this.name = name;
            this.department = department;
            this.salary = salary;
        }

        int getId() {
            return id;
        }

        String getName() {
            return name;
        }

        String getDepartment() {
            return department;
        }

        double getSalary() {
            return salary;
        }

        @Override
        public int compareTo(Employee other) {
            // L'ordre naturel (par id) ; Integer.compare plutot que this.id - other.id, qui deborde sur des valeurs extremes.
            return Integer.compare(this.id, other.id);
        }

        @Override
        public String toString() {
            return name + "(" + department + "," + (int) salary + ")";
        }
    }

    public static Comparator<Employee> byDepartmentThenSalaryDescThenName() {
        // reversed() enveloppe seulement le comparateur du salaire : departement et nom restent croissants.
        return Comparator.comparing(Employee::getDepartment)
                .thenComparing(Comparator.comparing(Employee::getSalary).reversed())
                .thenComparing(Employee::getName);
    }

    static class ByIdDescendingComparator implements Comparator<Employee> {
        @Override
        public int compare(Employee a, Employee b) {
            // Inverser a et b donne l'ordre decroissant sans reversed().
            return Integer.compare(b.getId(), a.getId());
        }
    }
}