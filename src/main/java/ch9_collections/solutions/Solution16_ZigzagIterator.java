package ch9_collections.solutions;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans collections.exercises.Exercise16_ZigzagIterator.
 */
public class Solution16_ZigzagIterator {

    static final class ZigzagIterable<T> implements Iterable<T> {
        private final List<List<T>> lists;

        ZigzagIterable(List<List<T>> lists) {
            this.lists = lists;
        }

        @Override
        public Iterator<T> iterator() {
            // Chaque appel rend un NOUVEL iterateur avec son propre etat : on peut parcourir plusieurs fois (for-each).
            return new Iterator<T>() {
                private final int[] indexInEachList = new int[lists.size()];
                private int currentList = 0;

                @Override
                public boolean hasNext() {
                    // Reste-t-il un element dans au moins une liste ? hasNext ne doit rien consommer.
                    for (int i = 0; i < lists.size(); i++) {
                        if (indexInEachList[i] < lists.get(i).size()) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public T next() {
                    // Contrat d'Iterator : NoSuchElementException quand il n'y a plus rien ; on saute les listes epuisees puis on avance d'un cran.
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    while (indexInEachList[currentList] >= lists.get(currentList).size()) {
                        currentList = (currentList + 1) % lists.size();
                    }
                    T value = lists.get(currentList).get(indexInEachList[currentList]);
                    indexInEachList[currentList]++;
                    currentList = (currentList + 1) % lists.size();
                    return value;
                }
            };
        }
    }
}
