package collections.exercises;

import collections.ExerciseChecker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * EXERCICE 6 - Comparable vs Comparator, tri multi-criteres (niveau : difficile)
 * =================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 *
 * ==================================================================
 * TODO 1 : Employee implements Comparable<Employee>
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque eleve d'une ecole recoit, a son inscription, un numero
 * d'eleve unique, ecrit sur son cartable, colle a lui pour toujours.
 * Si la maitresse dit juste "rangez-vous", sans autre precision, tout
 * le monde sait se ranger par numero d'eleve croissant, parce que ce
 * numero est toujours accroche a chaque eleve - c'est son ordre "par
 * defaut", celui qu'il porte sur lui en permanence.
 *
 * C'est ca, Comparable : une classe qui declare "voici MON ordre
 * naturel, celui que je porte toujours sur moi". Une classe ne peut
 * avoir qu'UN SEUL ordre naturel (l'eleve n'a qu'un seul numero).
 *
 * -- Le plan --
 *
 *   1. Faire porter a Employee un ordre naturel base sur son id.
 *   2. Ce n'est rien d'autre que : "compare mon numero au numero de
 *      l'autre".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : c'est une seule comparaison, une seule ligne. Elle va
 * directement dans la methode que Comparable te demande d'ecrire
 * (compareTo).
 *
 *
 * ==================================================================
 * TODO 2 : byDepartmentThenSalaryDescThenName
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Maintenant, imagine que la DIRECTRICE (pas l'eleve lui-meme) veuille
 * une feuille de classement toute differente, juste pour une reunion :
 * "d'abord par classe (A avant B avant C...), puis a l'interieur
 * d'une meme classe, du plus grand salaire au plus petit (pour les
 * profs), et si deux profs ont EXACTEMENT le meme salaire, on les
 * departage par leur prenom, dans l'ordre alphabetique."
 *
 * Cette regle-la n'appartient pas a l'eleve (il ne la porte pas sur
 * lui) : c'est une feuille de regles EXTERNE, ecrite par la
 * directrice, qu'on peut changer sans toucher a l'eleve. C'est ca,
 * un Comparator : une regle de tri externe et remplacable, alors que
 * Comparable est l'ordre que l'objet porte toujours sur lui.
 *
 * -- Essayons a la main --
 *
 * Prends 4 fiches d'employes sur une table. Range-les d'abord en
 * petits tas par departement (tas "IT", tas "RH", ...). Range les tas
 * eux-memes par ordre alphabetique de departement. PUIS, a l'interieur
 * de chaque tas, range les fiches par salaire, du plus grand au plus
 * petit. Si deux salaires sont identiques dans le meme tas, range-les
 * par prenom.
 *
 * -- Ce qu'on remarque --
 *
 * Il y a 3 regles, appliquees dans un ORDRE bien precis : d'abord la
 * regle principale (departement), puis, SEULEMENT en cas d'egalite,
 * la regle suivante (salaire), puis, SEULEMENT si encore egalite, la
 * derniere regle (nom). C'est une chaine de regles de secours, pas
 * 3 regles independantes.
 *
 * -- Le plan --
 *
 *   1. Trier par departement (ordre alphabetique).
 *   2. En cas d'egalite de departement, departager par salaire, du
 *      plus GRAND au plus petit.
 *   3. En cas d'egalite de departement ET de salaire, departager par
 *      prenom (ordre alphabetique).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Q1 : chaque regle se raconte-t-elle toute seule ? "comparer par
 * departement" -> oui. "comparer par salaire, a l'envers" -> oui.
 * "comparer par nom" -> oui. Ce sont 3 petites boites qui existent
 * DEJA toutes faites dans le JDK (tu n'as pas besoin de les ecrire toi
 * meme a la main avec des if/else) : le JDK te laisse juste les
 * enchainer dans le bon ordre, comme des maillons d'une chaine.
 *
 *
 * ==================================================================
 * TODO 3 : ByIdDescendingComparator (une CLASSE, pas une lambda)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Meme regle "par numero d'eleve", mais a l'ENVERS (du plus grand au
 * plus petit), et cette fois on veut l'ecrire comme un vrai petit
 * objet independant, pas juste une ligne rapide - pour bien sentir la
 * difference entre "l'ordre que je porte sur moi" (Comparable, ecrit
 * DANS Employee) et "une regle ecrite a part, qu'on peut fabriquer
 * autant de fois qu'on veut, avec des variantes differentes"
 * (Comparator, ecrit EN DEHORS de Employee).
 *
 * -- Le plan --
 *
 *   1. Ecrire une petite classe a part qui sait comparer deux
 *      Employee par id, a l'envers.
 *
 * Exemple a verifier : Bob(id=1), David(id=2), Alice(id=3),
 * Carla(id=4) -> ordre naturel (Comparable) = Bob, David, Alice,
 * Carla. Tri multi-criteres = David(IT,5000), Alice(IT,4200),
 * Carla(IT,4200), Bob(RH,3900). Tri par id decroissant = Carla,
 * Alice, David, Bob.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 * Indice TODO 1 :
 *   - Integer.compare(this.id, other.id) plutot que (this.id -
 *     other.id) (soustraction qui peut deborder sur des valeurs
 *     extremes, un piege classique de l'examen OCP).
 *
 * Indice TODO 2 :
 *   - Comparator.comparing(Employee::getDepartment)
 *       .thenComparing(Comparator.comparing(Employee::getSalary).reversed())
 *       .thenComparing(Employee::getName)
 *
 * Indice TODO 3 :
 *   - public int compare(Employee a, Employee b) { return
 *     Integer.compare(b.getId(), a.getId()); } (a et b inverses,
 *     pour obtenir l'ordre decroissant).
 */
public class Exercise06_ComparableComparator {

    static class Employee /* TODO 1 : implements Comparable<Employee> */ {
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

        // TODO 1 : @Override public int compareTo(Employee other) { ... }

        @Override
        public String toString() {
            return name + "(" + department + "," + (int) salary + ")";
        }
    }

    public static final Comparator<Employee> byDepartmentThenSalaryDescThenName = null; // TODO 2

    static class ByIdDescendingComparator /* TODO 3 : implements Comparator<Employee> */ {
        // TODO 3 : @Override public int compare(Employee a, Employee b) { ... }
    }

    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>(List.of(
                new Employee(3, "Alice", "IT", 4200),
                new Employee(1, "Bob", "RH", 3900),
                new Employee(4, "Carla", "IT", 4200),
                new Employee(2, "David", "IT", 5000)
        ));

        // TODO 1 : decommenter une fois Comparable implemente
        // List<Employee> byNaturalOrder = new ArrayList<>(employees);
        // java.util.Collections.sort(byNaturalOrder);
        // ExerciseChecker.check("Ordre naturel = tri par id croissant",
        //         byNaturalOrder.toString().equals("[Bob(RH,3900), David(IT,5000), Alice(IT,4200), Carla(IT,4200)]"));

        // TODO 2 : decommenter une fois byDepartmentThenSalaryDescThenName implemente
        // List<Employee> multiSort = new ArrayList<>(employees);
        // multiSort.sort(byDepartmentThenSalaryDescThenName);
        // ExerciseChecker.check("Tri departement puis salaire desc puis nom : IT/5000/David, IT/4200/Alice, IT/4200/Carla, RH/3900/Bob",
        //         multiSort.toString().equals("[David(IT,5000), Alice(IT,4200), Carla(IT,4200), Bob(RH,3900)]"));

        // TODO 3 : decommenter une fois ByIdDescendingComparator implemente
        // List<Employee> byIdDesc = new ArrayList<>(employees);
        // byIdDesc.sort(new ByIdDescendingComparator());
        // ExerciseChecker.check("Tri par id decroissant : 4,3,2,1",
        //         byIdDesc.toString().equals("[Carla(IT,4200), Alice(IT,4200), David(IT,5000), Bob(RH,3900)]"));

        ExerciseChecker.summary();
    }
}