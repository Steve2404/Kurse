package ch9_collections.solutions;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Corrige de l'exercice 5.
 */
public class Solution05_BrowserHistory {

    static class BrowserHistory {
        private final Deque<String> backStack = new ArrayDeque<>();
        private final Deque<String> forwardStack = new ArrayDeque<>();
        private String currentPage;

        BrowserHistory(String homepage) {
            this.currentPage = homepage;
        }

        void visit(String url) {
            // Une nouvelle visite rend l'avenir caduc : on vide la pile forward, comme un vrai navigateur.
            backStack.push(currentPage);
            forwardStack.clear();
            currentPage = url;
        }

        String back() {
            // push/pop travaillent au meme bout (le haut de la pile) : la page quittee part dans forward.
            if (backStack.isEmpty()) {
                return currentPage;
            }
            forwardStack.push(currentPage);
            currentPage = backStack.pop();
            return currentPage;
        }

        String forward() {
            // Symetrique de back() ; pile vide -> on reste sur place au lieu de lancer une exception.
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