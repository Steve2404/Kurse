package ch8_lambdas.projects.p06_sorting.solution;

import ch8_lambdas.projects.p06_sorting.Data;

import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * SOLUTION du projet 6 - trier avec des fonctions.
 */
public class SortingApp {

    static String names(Person[] people) {
        StringBuilder sb = new StringBuilder();
        for (Person p : people) {
            sb.append(p).append(' ');
        }
        return sb.toString().strip();
    }

    public static void main(String[] args) {
        Person[] people = new Person[Data.PEOPLE.length];
        for (int i = 0; i < people.length; i++) {
            people[i] = Person.parse(Data.PEOPLE[i]);
        }
        Sorter sorter = new Sorter();
        Order byName = Order.byText(Person::name);                       // reference : instance sur le parametre
        Order byAge = Order.by(Person::age);
        Function<Person, String> city = Person::city;
        Order byCity = Order.byText(city);
        ToIntFunction<Person> score = Person::score;
        Order byScoreDesc = Order.by(score).reversed();

        System.out.println("par nom : " + names(sorter.mergeSort(people, byName)) + " (" + sorter.comparisons() + " comparaisons)");
        System.out.println("par age puis nom : " + names(sorter.mergeSort(people, byAge.then(byName))) + " (" + sorter.comparisons() + ")");
        System.out.println("score decroissant puis age : " + names(sorter.mergeSort(people, byScoreDesc.then(byAge))) + " (" + sorter.comparisons() + ")");
        Person[] byNameFirst = sorter.mergeSort(people, byName);
        sorter.comparisons();
        System.out.println("stabilite : par ville apres par nom " + names(sorter.mergeSort(byNameFirst, byCity)) + " = ville puis nom "
                + names(sorter.mergeSort(people, byCity.then(byName))));
        sorter.comparisons();
        Person[] insertion = sorter.insertionSort(people, byName);
        int insertionCount = sorter.comparisons();
        sorter.mergeSort(people, byName);
        System.out.println("insertion " + names(insertion).equals(names(byNameFirst)) + " : " + insertionCount + " comparaisons contre " + sorter.comparisons()
                + " pour la fusion");

        Person[] best = sorter.top(people, Data.TOP, byScoreDesc.then(byName));
        System.out.println("top " + Data.TOP + " : " + names(best) + " (" + sorter.comparisons() + " comparaisons)");

        Person[] byAgeSorted = sorter.mergeSort(people, byAge.then(byName));
        StringBuilder found = new StringBuilder("dichotomie par age :");
        for (int age : Data.AGES) {
            int i = Sorter.search(byAgeSorted, age, Person::age);
            found.append(' ').append(age).append("->").append(i).append(i >= 0 ? "(" + byAgeSorted[i] + ")" : "");
        }
        System.out.println(found);

        // Une lambda locale capture des variables effectively final ; l'ordre "par proximite d'age" en depend.
        int target = 30;
        Order closeTo30 = (a, b) -> Integer.compare(Math.abs(a.age() - target), Math.abs(b.age() - target));
        System.out.println("plus proches de " + target + " ans : " + names(sorter.top(people, 4, closeTo30.then(byName))) + " ; ordre inverse du nom : "
                + names(sorter.mergeSort(people, byName.reversed())).substring(0, 13));
    }
}
