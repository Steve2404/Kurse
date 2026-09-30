package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * EXERCICE 3 - Une hierarchie d'employes : redefinir, appeler super, toString, equals et hashCode (niveau : avance)
 * ===============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une entreprise a des Employee. Un Manager EST un Employee qui touche
 * en plus une prime. Un Intern EST un Employee au salaire fixe. Chaque
 * enfant redefinit ce qui change (le titre, la paie) et reutilise le
 * reste. Magie du polymorphisme : toString(), ecrit UNE fois dans
 * Employee, appelle title() et monthlyPay()... et c'est la version de
 * l'OBJET REEL (Manager, Intern) qui s'execute.
 *
 * Et deux objets sont "egaux" quand on le decide : ici, meme classe
 * exacte et meme nom. Qui redefinit equals doit redefinir hashCode
 * (sinon un HashSet garde des doublons).
 *
 *
 * ==================================================================
 * TODO 1 : Employee.toString()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   new Employee("Linus", 3000)      -> "Employe Linus (3000)"
 *   new Manager("Ada", 5000, 500)    -> "Manager Ada (5500)"   (la methode de Manager est appelee !)
 *   new Intern("Tim")                -> "Stagiaire Tim (800)"
 *
 * -- Le plan --
 *
 *   1. Rendre title() + " " + name + " (" + monthlyPay() + ")".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : title() et monthlyPay() SONT les boites, redefinies par les enfants.
 *
 *
 * ==================================================================
 * TODO 2 : Manager.monthlyPay()    et    TODO 3 : Manager.title()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La paie d'un manager, c'est la paie d'un employe PLUS la prime. On ne
 * recopie pas le calcul du parent : on l'appelle avec super.
 *
 * -- Le plan --
 *
 *   1. monthlyPay : rendre super.monthlyPay() + bonus.
 *   2. title : rendre "Manager".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. (Sans "super.", monthlyPay() s'appellerait elle-meme a l'infini : StackOverflowError.)
 *
 *
 * ==================================================================
 * TODO 4 : Employee.equals(other)    et    TODO 5 : Employee.hashCode()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * equals recoit un Object (n'importe quoi, meme null). On verifie que
 * c'est la MEME classe exacte (un Manager n'est pas egal a un Employee
 * du meme nom), puis on compare les noms. hashCode doit donner le meme
 * nombre pour deux objets egaux : on prend celui du nom.
 *
 * -- Le plan --
 *
 *   1. equals : si other est null ou other.getClass() != getClass() -> false ;
 *      sinon caster en Employee et comparer name avec equals.
 *   2. hashCode : rendre name.hashCode().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : totalPayroll(staff)    et    TODO 7 : countByTitle(staff, title)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une liste de List<Employee> contient des Manager, des Intern... On
 * appelle monthlyPay() et title() sans se demander qui est qui : chaque
 * objet repond avec SA version.
 *
 * -- Essayons a la main --
 *
 *   [Manager Ada 5500, Employe Linus 3000, Stagiaire Tim 800] -> total 9300 ; "Manager" -> 1
 *
 * -- Le plan --
 *
 *   1. Deux boucles for-each.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - @Override au-dessus d'une methode redefinie : le compilateur verifie que c'est bien une redefinition.
 *   - equals(Object other), et non equals(Employee other) : sinon c'est une SURCHARGE, et HashSet ne la voit pas.
 */
public class Exercise03_EmployeeHierarchy {

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
            throw new UnsupportedOperationException("TODO 1 : implementer toString()");
        }

        @Override
        public boolean equals(Object other) {
            throw new UnsupportedOperationException("TODO 4 : implementer equals()");
        }

        @Override
        public int hashCode() {
            throw new UnsupportedOperationException("TODO 5 : implementer hashCode()");
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
            throw new UnsupportedOperationException("TODO 2 : implementer monthlyPay()");
        }

        @Override
        String title() {
            throw new UnsupportedOperationException("TODO 3 : implementer title()");
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
        throw new UnsupportedOperationException("TODO 6 : implementer totalPayroll()");
    }

    public static int countByTitle(List<Employee> staff, String title) {
        throw new UnsupportedOperationException("TODO 7 : implementer countByTitle()");
    }

    public static void main(String[] args) {
        Employee linus = new Employee("Linus", 3000);
        Employee ada = new Manager("Ada", 5000, 500);
        Employee tim = new Intern("Tim");
        ExerciseChecker.check("toString : Employe Linus (3000)", linus.toString().equals("Employe Linus (3000)"));
        ExerciseChecker.check("Manager : paie 5500 via super, titre Manager", ada.monthlyPay() == 5500 && ada.toString().equals("Manager Ada (5500)"));
        ExerciseChecker.check("Intern : Stagiaire Tim (800)", tim.toString().equals("Stagiaire Tim (800)"));

        ExerciseChecker.check("equals : meme classe et meme nom", new Employee("Linus", 1).equals(new Employee("Linus", 2))
                && !new Employee("Ada", 5000).equals(ada) && !linus.equals(null) && !linus.equals("Linus"));
        Set<Employee> set = new HashSet<>(List.of(linus, new Employee("Linus", 9999), ada, tim));
        ExerciseChecker.check("hashCode coherent : le HashSet garde 3 employes", set.size() == 3);

        List<Employee> staff = List.of(ada, linus, tim);
        ExerciseChecker.check("totalPayroll == 9300", totalPayroll(staff) == 9300);
        ExerciseChecker.check("countByTitle : 1 Manager, 1 Stagiaire", countByTitle(staff, "Manager") == 1 && countByTitle(staff, "Stagiaire") == 1);

        ExerciseChecker.summary();
    }
}
