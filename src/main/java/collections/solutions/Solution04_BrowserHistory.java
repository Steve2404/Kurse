package collections.solutions;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Corrige de l'exercice 4.
 */
public class Solution04_BrowserHistory {

    static class BrowserHistory {
        private final Deque<String> backStack = new ArrayDeque<>();
        private final Deque<String> forwardStack = new ArrayDeque<>();
        private String currentPage;

        BrowserHistory(String homepage) {
            this.currentPage = homepage;
        }

        void visit(String url) {
            backStack.push(currentPage);
            forwardStack.clear();
            currentPage = url;
        }

        String back() {
            if (backStack.isEmpty()) {
                return currentPage;
            }
            forwardStack.push(currentPage);
            currentPage = backStack.pop();
            return currentPage;
        }

        String forward() {
            if (forwardStack.isEmpty()) {
                return currentPage;
            }
            backStack.push(currentPage);
            currentPage = forwardStack.pop();
            return currentPage;
        }

        String current() {
            return currentPage;
        }
    }
}