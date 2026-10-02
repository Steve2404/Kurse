package ch9_collections.drills.r02_list.solution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * SOLUTION du drill de rappel 2 - l'interface List.
 */
public class Recall02 {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("b");
        list.add(0, "a");
        list.add("c");
        String old = list.set(1, "B");                     // set rend l'ANCIENNE valeur
        System.out.println("D01 : " + list + " " + old + " " + list.get(2) + " " + list.indexOf("c") + " " + list.indexOf("z"));
        List<Integer> nums = new ArrayList<>(List.of(10, 20, 30, 20));
        nums.remove(1);                                     // indice 1
        nums.remove(Integer.valueOf(20));                   // la valeur 20 (la premiere restante)
        System.out.println("D02 : " + nums + " " + nums.lastIndexOf(30));
        List<String> words = new ArrayList<>(List.of("pomme", "kiwi", "banane"));
        words.replaceAll(String::toUpperCase);
        words.sort(null);                                   // null : ordre naturel
        System.out.println("D03 : " + words);
        List<Integer> big = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Integer> view = big.subList(1, 4);             // [1, 4[ : une vue sur big
        view.set(0, 20);
        view.clear();
        System.out.println("D04 : " + big);
        ListIterator<String> it = new ArrayList<>(List.of("a", "b", "c")).listIterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            sb.append(it.nextIndex()).append(it.next());
        }
        while (it.hasPrevious()) {
            sb.append(it.previous());
        }
        System.out.println("D05 : " + sb);
        List<Integer> toClean = new ArrayList<>(List.of(1, 2, 3, 4));
        Iterator<Integer> i = toClean.iterator();
        while (i.hasNext()) {
            if (i.next() % 2 == 1) {
                i.remove();
            }
        }
        LinkedList<String> linked = new LinkedList<>(List.of("m"));
        linked.addFirst("d");
        linked.addLast("f");
        System.out.println("D06 : " + toClean + " " + linked + " " + linked.getFirst() + linked.getLast() + " " + linked.removeFirst() + " " + linked);
    }
}
