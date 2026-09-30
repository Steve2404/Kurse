package ch7_beyondclasses.solutions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Corrige de l'exercice 22. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise22_StackWithIterator.
 */
public class Solution22_StackWithIterator {

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
            // Classe interne : elle lit "top" de la pile qui l'a creee.
            private Node current = top;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public String next() {
                // Le contrat d'Iterator : NoSuchElementException au-dela de la fin.
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                String value = current.value;
                current = current.next;
                return value;
            }
        }

        private Node top;
        private int size;

        public void push(String value) {
            // Le nouveau maillon pointe vers l'ancien dessus.
            top = new Node(value, top);
            size++;
        }

        public String pop() {
            // Enlever le dessus : le suivant devient le nouveau dessus.
            String value = peek();
            top = top.next;
            size--;
            return value;
        }

        public String peek() {
            // Lire sans enlever ; une pile vide est une erreur d'usage.
            if (top == null) {
                throw new IllegalStateException("pile vide");
            }
            return top.value;
        }

        public int size() {
            return size;
        }

        @Override
        public Iterator<String> iterator() {
            return new StackIterator();
        }

        public List<String> sortedCopy() {
            // Une copie triee par une classe anonyme : utilisee une seule fois, pas besoin de nom.
            List<String> list = new ArrayList<>();
            for (String s : this) {
                list.add(s);
            }
            list.sort(new Comparator<String>() {
                @Override
                public int compare(String a, String b) {
                    int byLength = Integer.compare(a.length(), b.length());
                    return byLength != 0 ? byLength : a.compareTo(b);
                }
            });
            return list;
        }

        public int countLongerThan(int n) {
            // Classe locale : visible seulement ici ; elle lit n (effectivement final).
            class Counter {
                int count;

                void accept(String s) {
                    if (s.length() > n) {
                        count++;
                    }
                }
            }
            Counter counter = new Counter();
            for (String s : this) {
                counter.accept(s);
            }
            return counter.count;
        }
    }
}
