package ch11_exceptions.drills.r05_builtin.solution;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * SOLUTION du drill de rappel 5 - les exceptions du JDK a reconnaitre (et leurs messages).
 */
public class Recall05 {

    // Lance l'action, et rend "NomSimple: message" de ce qui a ete lance.
    static String probe(Runnable action) {
        try {
            action.run();
            return "rien";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName() + ": " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        int[] three = new int[3];
        int zero = 0;
        System.out.println("D01 : " + probe(() -> System.out.print(10 / zero)) + " | " + probe(() -> three[3] = 1) + " | " + probe(() -> "abc".charAt(5)));
        System.out.println("D02 : " + probe(() -> Integer.parseInt("12.5")) + " | " + probe(() -> "ab".repeat(-1)) + " | " + probe(() -> new int[-1].clone()));
        Object text = "java";
        String nothing = null;
        System.out.println("D03 : " + probe(() -> System.out.print((Integer) text)).split(":")[0] + " | " + probe(() -> nothing.length()).split(":")[0]);
        System.out.println("D04 : " + probe(() -> List.of(1).add(2)) + " | " + probe(() -> Optional.empty().get()) + " | " + probe(() -> List.of().iterator().next()));
        Object[] strings = new String[1];
        System.out.println("D05 : " + probe(() -> strings[0] = 1) + " | " + probe(() -> LocalDate.of(2026, 2, 30)));
        System.out.println("D06 : " + probe(() -> {
            throw new IllegalStateException("etat invalide");
        }) + " | " + probe(() -> System.out.print("")));
    }
}
