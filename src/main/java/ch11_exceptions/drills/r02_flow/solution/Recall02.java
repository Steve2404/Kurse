package ch11_exceptions.drills.r02_flow.solution;

import java.util.ArrayList;
import java.util.List;

/**
 * SOLUTION du drill de rappel 2 - le chemin d'execution dans try / catch / finally.
 */
public class Recall02 {

    static String run(boolean fail, List<String> trace) {
        try {
            trace.add("A");
            if (fail) {
                throw new IllegalStateException();
            }
            trace.add("B");
            return "try";
        } catch (IllegalStateException e) {
            trace.add("C");
            return "catch";
        } finally {
            trace.add("D");                                  // apres l'evaluation du return, avant le retour effectif
        }
    }

    @SuppressWarnings("finally")
    static String swallow() {
        try {
            throw new IllegalArgumentException("perdue");
        } finally {
            return "finally avale l'exception";             // un return dans finally ANNULE l'exception en cours
        }
    }

    static StringBuilder builder() {
        StringBuilder sb = new StringBuilder("x");
        try {
            return sb;
        } finally {
            sb.append("!");                                  // l'OBJET rendu est modifie (pas la reference)
        }
    }

    static String nested(List<String> trace) {
        try {
            try {
                trace.add("t1");
                throw new IllegalStateException("interne");
            } finally {
                trace.add("f1");
            }
        } catch (IllegalStateException e) {
            trace.add("c2:" + e.getMessage());
        } finally {
            trace.add("f2");
        }
        return String.join(" ", trace);
    }

    static String fromCatch(List<String> trace) {
        try {
            try {
                throw new IllegalStateException("premiere");
            } catch (IllegalStateException e) {
                trace.add("catch");
                throw new UnsupportedOperationException("seconde");
            } finally {
                trace.add("finally");
            }
        } catch (RuntimeException e) {
            trace.add(e.getMessage());
        }
        return String.join(" ", trace);
    }

    public static void main(String[] args) {
        List<String> ok = new ArrayList<>();
        String r1 = run(false, ok);
        List<String> ko = new ArrayList<>();
        String r2 = run(true, ko);
        System.out.println("D01 : " + r1 + " " + ok + " | " + r2 + " " + ko);
        System.out.println("D02 : " + swallow());
        System.out.println("D03 : " + builder());
        System.out.println("D04 : " + nested(new ArrayList<>()));
        System.out.println("D05 : " + fromCatch(new ArrayList<>()));
        int count = 0;
        for (int i = 0; i < 3; i++) {
            try {
                if (i == 1) {
                    continue;                                   // finally s'execute aussi avant continue / break
                }
                count += 10;
            } finally {
                count++;
            }
        }
        System.out.println("D06 : " + count);
    }
}
