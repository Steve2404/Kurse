package ch14_io.solutions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise03_PathModel.
 */
public class Solution03_PathModel {

    public static List<String> names(String path) {
        // split avec -1 garde les morceaux vides : "" donne [""], exactement comme Path.of("") (1 nom vide).
        return List.of(path.split("/", -1));
    }

    public static String normalize(String path) {
        // Une pile : ".." annule le nom precedent, sauf s'il n'y en a pas (ou si c'est deja un "..") : on le garde.
        Deque<String> stack = new ArrayDeque<>();
        for (String name : names(path)) {
            if (name.equals(".") || name.isEmpty()) {
                continue;
            }
            if (name.equals("..") && !stack.isEmpty() && !stack.peekLast().equals("..")) {
                stack.pollLast();
            } else {
                stack.addLast(name);
            }
        }
        return String.join("/", stack);
    }

    public static String resolve(String base, String other) {
        // resolve colle simplement les noms, SANS normaliser (les ".." restent).
        if (other.isEmpty()) {
            return base;
        }
        if (base.isEmpty()) {
            return other;
        }
        return base + "/" + other;
    }

    public static String relativize(String from, String to) {
        // Remonter (..) jusqu'a l'ancetre commun, puis redescendre vers to.
        List<String> a = useful(from);
        List<String> b = useful(to);
        int k = 0;
        while (k < a.size() && k < b.size() && a.get(k).equals(b.get(k))) {
            k++;
        }
        List<String> result = new ArrayList<>();
        for (int i = k; i < a.size(); i++) {
            result.add("..");
        }
        result.addAll(b.subList(k, b.size()));
        return String.join("/", result);
    }

    public static String subpath(String path, int begin, int end) {
        // fin EXCLUE ; une plage vide, negative ou trop longue est refusee par Path (IllegalArgumentException).
        List<String> n = names(path);
        if (begin < 0 || end > n.size() || begin >= end) {
            return "IllegalArgumentException";
        }
        return String.join("/", n.subList(begin, end));
    }

    public static boolean startsWith(String path, String prefix) {
        // On compare des NOMS entiers, pas des caracteres : "abc/d" ne commence pas par "ab".
        List<String> p = names(path);
        List<String> q = names(prefix);
        return q.size() <= p.size() && p.subList(0, q.size()).equals(q);
    }

    private static List<String> useful(String path) {
        // Le chemin vide n'a qu'un nom vide, qui ne compte pas pour relativize.
        return path.isEmpty() ? List.of() : names(path);
    }
}
