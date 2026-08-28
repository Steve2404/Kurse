package ch9_collections;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Demonstration de l'interface List : ArrayList vs LinkedList.
 *
 * - ArrayList : tableau redimensionnable, acces rapide par index (O(1)),
 *   insertion/suppression au milieu plus couteuse (O(n)).
 * - LinkedList : liste doublement chainee, insertion/suppression rapide
 *   en tete/queue (O(1)), acces par index couteux (O(n)).
 */
public class ListDemo {

    public static void run() {
        System.out.println("=== List : ArrayList ===");
        List<String> courses = new ArrayList<>();
        courses.add("Java");
        courses.add("Spring");
        courses.add("SQL");
        courses.add(1, "Kotlin"); // insertion a l'index 1
        System.out.println("ArrayList : " + courses);
        System.out.println("Element index 2 : " + courses.get(2));
        courses.remove("SQL");
        System.out.println("Apres suppression : " + courses);

        System.out.println("\n=== List : LinkedList (utilisee aussi comme Deque) ===");
        LinkedList<Integer> notes = new LinkedList<>();
        notes.add(12);
        notes.add(15);
        notes.addFirst(9);   // ajout en tete, rapide sur LinkedList
        notes.addLast(18);   // ajout en queue
        System.out.println("LinkedList : " + notes);
        System.out.println("Premier : " + notes.getFirst() + ", Dernier : " + notes.getLast());

        System.out.println("\n=== Parcours avec ListIterator (parcours bidirectionnel) ===");
        ListIterator<String> it = courses.listIterator();
        while (it.hasNext()) {
            int index = it.nextIndex();
            String value = it.next();
            System.out.println("index=" + index + " -> " + value);
        }
        while (it.hasPrevious()) {
            System.out.println("retour arriere -> " + it.previous());
        }
    }
}