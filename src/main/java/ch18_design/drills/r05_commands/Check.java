package ch18_design.drills.r05_commands;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON code ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 1 executions, 1 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 1 executions, 1 reussies");
            // EXPECTED-END

    static final List<String> API = List.of(
            "implements Action", "Deque<Action>", "List.copyOf(", "!instanceof", "!switch",
            "in:Undo.java!Add##l'historique ne connait aucune action concrete",
            "in:Undo.java!Reset##l'historique ne connait aucune action concrete");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
