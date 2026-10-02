package ch9_collections.drills.r09_wildcards.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION du drill de rappel 9 - les jokers : ?, ? extends, ? super.
 */
public class Recall09 {

    // ? : n'importe quelle liste ; on ne lit que des Object.
    static int count(List<?> list) {
        return list.size();
    }

    static String types(List<?> list) {
        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            sb.append(o.getClass().getSimpleName().charAt(0));
        }
        return sb.toString();
    }

    // ? extends Number : on LIT des Number (producteur).
    static double total(List<? extends Number> list) {
        double t = 0;
        for (Number n : list) {
            t += n.doubleValue();
        }
        return t;
    }

    // ? super Integer : on AJOUTE des Integer (consommateur).
    static void addNumbers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
    }

    static <T extends Comparable<? super T>> T min(List<? extends T> list) {
        T best = list.get(0);
        for (T t : list) {
            if (t.compareTo(best) < 0) {
                best = t;
            }
        }
        return best;
    }

    public static void main(String[] args) {
        List<Integer> ints = List.of(1, 2, 3);
        List<Double> doubles = List.of(1.5, 2.5);
        List<String> strings = List.of("a", "b");
        System.out.println("D01 : " + count(ints) + " " + count(strings) + " " + types(List.of(1, "x", 2.0)));
        System.out.println("D02 : " + total(ints) + " " + total(doubles));
        List<Number> numbers = new ArrayList<>();
        List<Object> objects = new ArrayList<>(List.of("debut"));
        addNumbers(numbers);
        addNumbers(objects);
        System.out.println("D03 : " + numbers + " " + objects);
        System.out.println("D04 : " + min(List.of(5, 3, 8)) + " " + min(List.of("pomme", "kiwi")));
        // List<Integer> n'est PAS une List<Number> ; mais c'est une List<? extends Number>.
        List<? extends Number> readOnly = ints;
        Number firstNumber = readOnly.get(0);
        List<? super Integer> writeOnly = numbers;
        writeOnly.add(99);
        Object got = writeOnly.get(0);
        System.out.println("D05 : " + firstNumber + " " + numbers + " " + got);
    }
}
