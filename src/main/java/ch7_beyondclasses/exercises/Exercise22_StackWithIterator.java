package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * EXERCICE 22 - Une pile maison : noeud static imbrique, iterateur en classe interne, comparateur anonyme, classe locale (niveau : avance)
 * ====================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une pile d'assiettes : on pose sur le dessus (push), on reprend le
 * dessus (pop). On la construit avec des maillons chaines. Chaque sorte
 * de classe imbriquee a sa bonne place :
 *   - Node : classe imbriquee STATIC (un maillon n'a pas besoin de la pile) ;
 *   - StackIterator : classe INTERNE (elle lit le "top" de SA pile) ;
 *   - un Comparator ANONYME (utilise une seule fois) ;
 *   - une classe LOCALE dans une methode (un petit compteur).
 * StringStack implemente Iterable<String> : on peut faire for (String s : pile).
 *
 *
 * ==================================================================
 * TODO 1 : push(value)    TODO 2 : pop()    TODO 3 : peek()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   push("a"), push("b") -> dessus "b" ; pop() -> "b" puis "a" ; pop() sur pile vide -> IllegalStateException("pile vide")
 *
 * -- Le plan --
 *
 *   1. push : top = new Node(value, top) ; size++.
 *   2. pop : vide -> exception ; sinon garder top.value, top = top.next, size--, rendre la valeur.
 *   3. peek : vide -> exception ; sinon top.value (sans enlever).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : StackIterator.hasNext() et next()    [classe interne]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * L'iterateur garde un doigt sur un maillon (current), qui part du top
 * de SA pile (une classe interne voit les champs de l'objet englobant).
 * next() rend la valeur du maillon et avance ; au-dela de la fin :
 * NoSuchElementException.
 *
 * -- Le plan --
 *
 *   1. hasNext : current != null.
 *   2. next : si !hasNext() -> throw new NoSuchElementException() ; sinon valeur = current.value ; current = current.next.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : sortedCopy()    [comparateur anonyme]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une liste des elements triee par longueur (puis alphabetiquement),
 * sans toucher a la pile. Le Comparator est ecrit en classe anonyme.
 *
 * -- Essayons a la main --
 *
 *   pile (dessus en premier) ["kiwi", "fig", "banana", "apple"] -> ["fig", "kiwi", "apple", "banana"]
 *
 * -- Le plan --
 *
 *   1. list = les elements (via l'iterateur ou for-each sur this).
 *   2. list.sort(new Comparator<String>() { public int compare(String a, String b) { ... } }) :
 *      d'abord Integer.compare(a.length(), b.length()), si 0 alors a.compareTo(b).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : countLongerThan(n)    [classe locale]
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Dans la methode, declarer class Counter { int count; void accept(String s) { if (s.length() > n) count++; } }
 *      (n est un parametre effectivement final : la classe locale peut le lire).
 *   2. Creer un Counter, lui passer chaque element, rendre count.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est l'exercice : une classe locale EST une petite boite, visible seulement ici.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - for (String s : this) utilise iterator() : il faut donc le TODO 4 avant les TODO 5 et 6.
 */
public class Exercise22_StackWithIterator {

    public static class StringStack implements Iterable<String> {

        private static class Node {
            final String value;
            final Node next;

            Node(String value, Node next) {
                this.value = value;
                this.next = next;
            }
        }

        private class StackIterator implements Iterator<String> {
            private Node current = top;

            @Override
            public boolean hasNext() {
                throw new UnsupportedOperationException("TODO 4 : implementer hasNext()");
            }

            @Override
            public String next() {
                throw new UnsupportedOperationException("TODO 4 : implementer next()");
            }
        }

        private Node top;
        private int size;

        public void push(String value) {
            throw new UnsupportedOperationException("TODO 1 : implementer push()");
        }

        public String pop() {
            throw new UnsupportedOperationException("TODO 2 : implementer pop()");
        }

        public String peek() {
            throw new UnsupportedOperationException("TODO 3 : implementer peek()");
        }

        public int size() {
            return size;
        }

        @Override
        public Iterator<String> iterator() {
            return new StackIterator();
        }

        public List<String> sortedCopy() {
            throw new UnsupportedOperationException("TODO 5 : implementer sortedCopy()");
        }

        public int countLongerThan(int n) {
            throw new UnsupportedOperationException("TODO 6 : implementer countLongerThan()");
        }
    }

    public static void main(String[] args) {
        StringStack stack = new StringStack();
        stack.push("a");
        stack.push("b");
        ExerciseChecker.check("push/peek : dessus b, taille 2", stack.peek().equals("b") && stack.size() == 2);
        ExerciseChecker.check("pop : b puis a, taille 0", stack.pop().equals("b") && stack.pop().equals("a") && stack.size() == 0);
        String error = null;
        try {
            stack.pop();
        } catch (IllegalStateException e) {
            error = e.getMessage();
        }
        ExerciseChecker.check("pop sur pile vide -> pile vide", "pile vide".equals(error));

        StringStack fruits = new StringStack();
        for (String f : List.of("apple", "banana", "fig", "kiwi")) {
            fruits.push(f);
        }
        List<String> seen = new ArrayList<>();
        for (String f : fruits) {
            seen.add(f);
        }
        ExerciseChecker.check("for-each (classe interne) : du dessus vers le fond", seen.equals(List.of("kiwi", "fig", "banana", "apple")));
        Iterator<String> it = new StringStack().iterator();
        boolean thrown = false;
        try {
            it.next();
        } catch (NoSuchElementException e) {
            thrown = true;
        }
        ExerciseChecker.check("next() apres la fin -> NoSuchElementException", !it.hasNext() && thrown);
        ExerciseChecker.check("sortedCopy (comparateur anonyme)", fruits.sortedCopy().equals(List.of("fig", "kiwi", "apple", "banana")) && fruits.size() == 4);
        ExerciseChecker.check("countLongerThan(4) == 2 (classe locale)", fruits.countLongerThan(4) == 2);

        ExerciseChecker.summary();
    }
}
