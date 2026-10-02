package ch11_exceptions.projects.p04_vm.solution;

import ch11_exceptions.projects.p04_vm.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION du projet 4 - la machine a exceptions, puis les regles de finally en Java.
 */
public class VmLab {

    // finally s'execute APRES l'evaluation du return du try : son propre return ECRASE l'autre.
    @SuppressWarnings("finally")
    static int finallyWins() {
        try {
            return 1;
        } finally {
            return 2;
        }
    }

    // La valeur de retour est deja calculee quand finally s'execute : modifier x n'y change rien.
    static int valueAlreadyComputed() {
        int x = 1;
        try {
            return x;
        } finally {
            x = 99;
        }
    }

    static String order(List<String> trace) {
        try {
            trace.add("try");
            throw new IllegalStateException("rate");
        } catch (IllegalStateException e) {
            trace.add("catch");
            return "retour du catch";
        } finally {
            trace.add("finally");
        }
    }

    // Une exception lancee DANS finally remplace celle du try : l'originale est PERDUE (pas supprimee).
    @SuppressWarnings("finally")
    static void masked(int zero) {
        try {
            System.out.println("jamais affiche " + 1 / zero);
        } finally {
            throw new IllegalStateException("lancee dans finally");
        }
    }

    public static void main(String[] args) {
        for (String program : Data.PROGRAMS) {
            String[] parts = program.split("\\|");
            Machine machine = new Machine(parts[1]);
            try {
                machine.run(Data.MAX_STEPS);
                System.out.println(parts[0] + " : " + machine.state());
            } catch (UncaughtVmException e) {
                System.out.println(parts[0] + " : " + e.getMessage() + " <- " + e.getCause().getClass().getSimpleName() + " " + e.getCause().getMessage()
                        + " ; " + machine.state());
            } catch (StepLimitException | IllegalArgumentException e) {
                System.out.println(parts[0] + " : arret " + e.getClass().getSimpleName() + " " + e.getMessage());
            }
        }

        List<String> trace = new ArrayList<>();
        String returned = order(trace);
        System.out.println("finally : finallyWins " + finallyWins() + ", valueAlreadyComputed " + valueAlreadyComputed() + ", " + returned + " " + trace);
        try {
            masked(0);
        } catch (RuntimeException e) {
            System.out.println("masquee : " + e.getClass().getSimpleName() + " (" + e.getMessage() + "), cause " + e.getCause() + ", supprimees "
                    + e.getSuppressed().length);
        }
    }
}
