package ch17_algorithms.projects.p05_stacks.solution;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * La pile monotone : une pile d'INDICES dont les valeurs restent triees.
 * Chaque indice entre et sort au plus une fois : O(n), malgre la boucle dans la boucle.
 */
public final class Monotonic {

    private Monotonic() {
    }

    // Pour chaque jour, combien de jours attendre une temperature plus CHAUDE (0 si jamais).
    public static int[] daysUntilWarmer(int[] temps) {
        int[] wait = new int[temps.length];
        Deque<Integer> waiting = new ArrayDeque<>();
        for (int day = 0; day < temps.length; day++) {
            // Piege : strictement plus chaud ; un jour aussi chaud ne "repond" pas.
            while (!waiting.isEmpty() && temps[waiting.peek()] < temps[day]) {
                int earlier = waiting.pop();
                wait[earlier] = day - earlier;
            }
            waiting.push(day);
        }
        return wait;
    }

    // Le plus grand rectangle sous un histogramme. Quand une barre plus BASSE arrive, chaque barre plus haute
    // de la pile ne peut plus s'etendre a droite : on calcule son rectangle (sa hauteur x sa largeur).
    public static long largestRectangle(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        long best = 0;
        for (int i = 0; i <= heights.length; i++) {
            // Pourquoi une barre fictive de hauteur 0 a la fin : elle vide la pile.
            int h = i == heights.length ? 0 : heights[i];
            while (!stack.isEmpty() && heights[stack.peek()] >= h) {
                int height = heights[stack.pop()];
                int left = stack.isEmpty() ? -1 : stack.peek();
                best = Math.max(best, (long) height * (i - left - 1));
            }
            stack.push(i);
        }
        return best;
    }
}
