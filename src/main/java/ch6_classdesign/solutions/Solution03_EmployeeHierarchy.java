package ch6_classdesign.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise03_EmployeeHierarchy.
 */
public class Solution03_EmployeeHierarchy {

    static class Employee {
        protected final String name;
        protected final int baseSalary;

        Employee(String name, int baseSalary) {
            this.name = name;
            this.baseSalary = baseSalary;
        }

        int monthlyPay() {
            return baseSalary;
        }

        String title() {
            return "Employe";
        }

        @Override
        public String toString() {
            // title() et monthlyPay() sont resolues sur l'objet REEL : la version de l'enfant s'execute.
            return title() + " " + name + " (" + monthlyPay() + ")";
        }

        @Override
        public boolean equals(Object other) {
            // getClass() exige la MEME classe exacte : un Manager n'est jamais egal a un Employee.
            if (other == null || other.getClass() != getClass()) {
                return false;
            }
            return name.equals(((Employee) other).name);
        }

        @Override
        public int hashCode() {
            // Deux objets egaux (meme nom) doivent avoir le meme hashCode.
            return name.hashCode();
        }
    }

    static class Manager extends Employee {
        private final int bonus;

        Manager(String name, int baseSalary, int bonus) {
            super(name, baseSalary);
            this.bonus = bonus;
        }

        @Override
        int monthlyPay() {
            // super. appelle la version du parent ; sans lui, la methode s'appellerait elle-meme.
            return super.monthlyPay() + bonus;
        }

        @Override
        String title() {
            // Redefinition simple : meme signature, nouveau comportement.
            return "Manager";
        }
    }

    static class Intern extends Employee {
        Intern(String name) {
            super(name, 800);
        }

        @Override
        String title() {
            return "Stagiaire";
        }
    }

    public static int totalPayroll(List<Employee> staff) {
        // Polymorphisme : chaque element repond avec SA version de monthlyPay().
        int total = 0;
        for (Employee e : staff) {
            total += e.monthlyPay();
        }
        return total;
    }

    public static int countByTitle(List<Employee> staff, String title) {
        // title() est redefinie : pas besoin d'instanceof.
        int count = 0;
        for (Employee e : staff) {
            if (e.title().equals(title)) {
                count++;
            }
        }
        return count;
    }
}
