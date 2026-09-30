package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * EXERCICE 4 - Les 20 methodes de Deque : quel bout, et que se passe-t-il quand c'est vide ? (niveau : difficile)
 * ===============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une Deque, c'est un tube ouvert des deux cotes : le DEBUT (first) et
 * la FIN (last). Chaque action existe en deux "caracteres" :
 *
 *                      poli (rend null/false)    grincheux (exception)
 *   ajouter            offer...                  add...
 *   retirer            poll...                   remove...
 *   regarder           peek...                   get... / element
 *
 * Et trois noms "de file" / "de pile" sans First/Last :
 *   add, offer                 -> ajoutent a la FIN (on fait la queue)
 *   remove, poll, element, peek -> travaillent au DEBUT (le premier de la queue)
 *   push, pop                  -> travaillent au DEBUT (le haut de la pile)
 *
 * Sur un tube vide, les polis rendent null ; les grincheux lancent
 * NoSuchElementException (pop aussi : c'est un removeFirst deguise).
 *
 *
 * ==================================================================
 * TODO 1 : onEmpty(method)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * method est une methode qui retire ou regarde (poll, pop, getLast,
 * peekFirst...). Rendre ce qu'elle fait sur une Deque vide : "null" ou
 * "NoSuchElementException".
 *
 * -- Essayons a la main --
 *
 *   "poll" -> null ; "pop" -> NoSuchElementException ; "element" -> NoSuchElementException ; "peekLast" -> null
 *
 * -- Le plan --
 *
 *   1. Si le nom commence par "poll" ou "peek" -> "null".
 *   2. Sinon -> "NoSuchElementException".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : end(method)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * De quel bout du tube travaille la methode : "first" ou "last" ?
 *
 * -- Essayons a la main --
 *
 *   "push" -> first ; "add" -> last ; "poll" -> first ; "offerFirst" -> first ; "peekLast" -> last
 *
 * -- Le plan --
 *
 *   1. Si le nom finit par "Last" -> "last" ; par "First" -> "first".
 *   2. Sinon : "add" et "offer" -> "last" ; tous les autres -> "first".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : simulate(ops)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On te donne des ordres ("push a", "offer b", "pollLast", "pop"...).
 * Joue-les SANS Deque, avec une simple ArrayList (index 0 = first), et
 * rends le contenu final. main() rejoue les memes ordres sur une vraie
 * ArrayDeque et compare.
 *
 * -- Essayons a la main --
 *
 *   [push a, push b, offer c]           -> [b, a, c]
 *   [add x, addFirst y, pollLast, push z] -> [z, y]
 *
 * -- Le plan --
 *
 *   1. Pour chaque ordre : couper en nom et (eventuelle) valeur.
 *   2. Avec une valeur : ajouter au bout donne par end(nom).
 *   3. Sans valeur : retirer au bout donne par end(nom) (si la liste n'est pas vide).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui (Q2 : "quel bout ?" sert a chaque ordre) : c'est end(), le TODO 2.
 *
 *
 * ==================================================================
 * TODO 4 : isPalindrome(word)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Mets toutes les lettres dans le tube, puis tire en meme temps une
 * lettre a chaque bout : tant qu'elles sont pareilles, continue.
 *
 * -- Essayons a la main --
 *
 *   "kayak" -> true ; "java" -> false ; "" -> true
 *
 * -- Le plan --
 *
 *   1. Remplir une ArrayDeque<Character> a la fin.
 *   2. Tant qu'il reste au moins 2 lettres : pollFirst et pollLast doivent etre egales.
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
 *   - name.startsWith("poll"), name.endsWith("Last").
 *   - String[] parts = op.split(" ") ; parts.length == 2 -> il y a une valeur.
 *   - list.add(0, v) / list.add(v) / list.remove(0) / list.remove(list.size() - 1).
 *   - Attention : pollFirst() rend un Character ; comparer avec equals, pas ==.
 */
public class Exercise04_DequeMethodRules {

    public static String onEmpty(String method) {
        throw new UnsupportedOperationException("TODO 1 : implementer onEmpty()");
    }

    public static String end(String method) {
        throw new UnsupportedOperationException("TODO 2 : implementer end()");
    }

    public static List<String> simulate(List<String> ops) {
        throw new UnsupportedOperationException("TODO 3 : implementer simulate()");
    }

    public static boolean isPalindrome(String word) {
        throw new UnsupportedOperationException("TODO 4 : implementer isPalindrome()");
    }

    public static void main(String[] args) {
        List<String> readers = List.of("poll", "pollFirst", "pollLast", "peek", "peekFirst", "peekLast",
                "remove", "removeFirst", "removeLast", "element", "getFirst", "getLast", "pop");
        int agree = 0;
        for (String m : readers) {
            if (onEmpty(m).equals(jvmOnEmpty(m))) {
                agree++;
            }
        }
        ExerciseChecker.check("onEmpty == JVM sur 13 methodes (" + agree + " d'accord)", agree == 13);

        List<String> all = new ArrayList<>(readers);
        all.addAll(List.of("add", "addFirst", "addLast", "offer", "offerFirst", "offerLast", "push"));
        agree = 0;
        for (String m : all) {
            if (end(m).equals(jvmEnd(m))) {
                agree++;
            }
        }
        ExerciseChecker.check("end == JVM sur 20 methodes (" + agree + " d'accord)", agree == 20);

        List<List<String>> scripts = List.of(
                List.of("push a", "push b", "offer c"),
                List.of("add x", "addFirst y", "pollLast", "push z"),
                List.of("offerLast 1", "offerFirst 2", "addLast 3", "pop", "removeLast", "push 4", "add 5"),
                List.of("push a", "poll", "poll", "offer b"));
        agree = 0;
        for (List<String> script : scripts) {
            if (simulate(script).equals(jvmSimulate(script))) {
                agree++;
            }
        }
        ExerciseChecker.check("simulate == vraie ArrayDeque sur 4 scenarios (" + agree + " d'accord)", agree == 4);

        ExerciseChecker.check("isPalindrome(kayak) / (java) / (\"\")",
                isPalindrome("kayak") && !isPalindrome("java") && isPalindrome(""));

        ExerciseChecker.summary();
    }

    // ---- Le juge : interroge une vraie ArrayDeque (ne pas modifier) ----

    static String jvmOnEmpty(String method) {
        Deque<String> d = new ArrayDeque<>();
        try {
            Object result = Deque.class.getMethod(method).invoke(d);
            return result == null ? "null" : "valeur";
        } catch (java.lang.reflect.InvocationTargetException e) {
            return e.getCause().getClass().getSimpleName();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    static String jvmEnd(String method) {
        Deque<String> d = new ArrayDeque<>(List.of("A", "Z"));
        try {
            if (hasParam(method)) {
                Deque.class.getMethod(method, Object.class).invoke(d, "new");
                return d.peekFirst().equals("new") ? "first" : "last";
            }
            Object result = Deque.class.getMethod(method).invoke(d);
            return "A".equals(result) ? "first" : "last";
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    static boolean hasParam(String method) {
        return method.startsWith("add") || method.startsWith("offer") || method.equals("push");
    }

    static List<String> jvmSimulate(List<String> ops) {
        Deque<String> d = new ArrayDeque<>();
        for (String op : ops) {
            String[] p = op.split(" ");
            try {
                if (p.length == 2) {
                    Deque.class.getMethod(p[0], Object.class).invoke(d, p[1]);
                } else if (!d.isEmpty()) {
                    Deque.class.getMethod(p[0]).invoke(d);
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException(e);
            }
        }
        return new ArrayList<>(d);
    }
}
