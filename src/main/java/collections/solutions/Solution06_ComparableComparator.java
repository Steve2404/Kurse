package collections.solutions;

import java.util.Comparator;

/**
 * Corrige de l'exercice 6.
 */
public class Solution06_ComparableComparator {

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
            return Integer.compare(this.id, other.id);
        }

        @Override
        public String toString() {
            return name + "(" + department + "," + (int) salary + ")";
        }
    }

    public static final Comparator<Employee> byDepartmentThenSalaryDescThenName =
            Comparator.comparing(Employee::getDepartment)
                    .thenComparing(Comparator.comparing(Employee::getSalary).reversed())
                    .thenComparing(Employee::getName);

    static class ByIdDescendingComparator implements Comparator<Employee> {
        @Override
        public int compare(Employee a, Employee b) {
            return Integer.compare(b.getId(), a.getId());
        }
    }
}