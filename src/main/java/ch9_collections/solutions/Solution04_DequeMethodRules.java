package ch9_collections.solutions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Corrige de l'exercice 4.
 */
public class Solution04_DequeMethodRules {

    public static String onEmpty(String method) {
        // Les "polis" (poll, peek) rendent null ; tous les autres, y compris pop et element, lancent une exception.
        return method.startsWith("poll") || method.startsWith("peek") ? "null" : "NoSuchElementException";
    }

    public static String end(String method) {
        // Le suffixe First/Last gagne ; sans suffixe, seule l'ajout "de file" (add, offer) va a la fin, push/pop/poll/peek... au debut.
        if (method.endsWith("Last")) {
            return "last";
        }
        if (method.endsWith("First")) {
            return "first";
        }
        return method.equals("add") || method.equals("offer") ? "last" : "first";
    }

    public static List<String> simulate(List<String> ops) {
        // end() est reutilise pour chaque ordre : c'est la boite magique qui evite 20 cas ecrits a la main.
        List<String> list = new ArrayList<>();
        for (String op : ops) {
            String[] parts = op.split(" ");
            boolean atFirst = end(parts[0]).equals("first");
            if (parts.length == 2) {
                if (atFirst) {
                    list.add(0, parts[1]);
                } else {
                    list.add(parts[1]);
                }
            } else if (!list.isEmpty()) {
                list.remove(atFirst ? 0 : list.size() - 1);
            }
        }
        return list;
    }

    public static boolean isPalindrome(String word) {
        // Deux bouts retires en meme temps ; equals car pollFirst rend un Character (== comparerait des references).
        Deque<Character> deque = new ArrayDeque<>();
        for (char c : word.toCharArray()) {
            deque.offerLast(c);
        }
        while (deque.size() > 1) {
            if (!deque.pollFirst().equals(deque.pollLast())) {
                return false;
            }
        }
        return true;
    }
}
