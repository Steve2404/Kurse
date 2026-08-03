package collections.solutions;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans collections.exercises.Exercise14_ZigzagIterator.
 */
public class Solution14_ZigzagIterator {

    static final class ZigzagIterable<T> implements Iterable<T> {
        private final List<List<T>> lists;

        ZigzagIterable(List<List<T>> lists) {
            this.lists = lists;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private final int[] indexInEachList = new int[lists.size()];
                private int currentList = 0;

                @Override
                public boolean hasNext() {
                    for (int i = 0; i < lists.size(); i++) {
                        if (indexInEachList[i] < lists.get(i).size()) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public T next() {
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
