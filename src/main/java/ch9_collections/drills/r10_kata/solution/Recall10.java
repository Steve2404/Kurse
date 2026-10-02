package ch9_collections.drills.r10_kata.solution;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * SOLUTION du drill de rappel 10 - kata mixte du chapitre 9.
 */
public class Recall10 {

    static <T extends Comparable<? super T>> List<T> topTwo(List<? extends T> values) {
        TreeSet<T> set = new TreeSet<>(values);
        List<T> out = new ArrayList<>();
        out.add(set.pollLast());
        out.add(set.pollLast());
        return out;
    }

    public static void main(String[] args) {
        List<Integer> nums = new ArrayList<>(List.of(4, 8, 15, 16, 23, 42));
        nums.remove(Integer.valueOf(15));
        nums.remove(0);
        nums.removeIf(n -> n > 40);
        System.out.println("D01 : " + nums);
        Map<Character, Integer> firstLetters = new TreeMap<>();
        for (String w : List.of("java", "map", "jdk", "list", "lambda")) {
            firstLetters.merge(w.charAt(0), 1, Integer::sum);
        }
        System.out.println("D02 : " + firstLetters);
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : "3 4 + 2 *".split(" ")) {
            switch (token) {
                case "+" -> stack.push(stack.pop() + stack.pop());
                case "*" -> stack.push(stack.pop() * stack.pop());
                default -> stack.push(Integer.parseInt(token));
            }
        }
        System.out.println("D03 : " + stack.pop() + " " + stack.isEmpty());
        List<String> names = new ArrayList<>(List.of("eve", "Bob", "alice", "Dan"));
        names.sort(Comparator.comparing(String::length).thenComparing(String.CASE_INSENSITIVE_ORDER));
        System.out.println("D04 : " + names + " " + topTwo(List.of(3, 9, 1, 7)) + " " + topTwo(names));
        TreeMap<Integer, String> grades = new TreeMap<>(Map.of(0, "F", 50, "D", 60, "C", 70, "B", 85, "A"));
        StringBuilder sb = new StringBuilder();
        for (int score : new int[] {42, 50, 68, 91}) {
            sb.append(grades.floorEntry(score).getValue());
        }
        System.out.println("D05 : " + sb + " " + grades.headMap(60).size() + " " + grades.ceilingKey(61));
    }
}
