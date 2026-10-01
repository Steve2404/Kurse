package ch10_streams.drills.r01_optional;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 1 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : Optional[Dune] Optional.empty",
            "D02 : true NullPointerException",
            "D03 : Germinal | inconnu",
            "D04 : orElse appelle 1 fois, orElseGet 0 fois",
            "D05 : Le Messie de Dune | aucune suite",
            "D06 : Optional[Le Hobbit]",
            "D07 : trouve 1965 absent",
            "D08 : NoSuchElementException IllegalArgumentException",
            "D09 : [Le Messie de Dune, Le Seigneur des Anneaux]",
            "D10 : 592 1989 8.89 -1",
            "D11 : true Optional.empty",
            "D12 : Optional.empty Optional[Hyperion]",
            "D13 : Asimov true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Optional.of(", "Optional.ofNullable(", "Optional.empty()", ".isEmpty()", ".isPresent()",
            ".orElse(", ".orElseGet(", ".orElseThrow()", ".orElseThrow(", ".map(",
            ".flatMap(", ".filter(", ".or(", ".ifPresent(", ".ifPresentOrElse(",
            "Optional::stream", "OptionalInt", "OptionalLong", "OptionalDouble", ".getAsInt()",
            ".getAsLong()", ".getAsDouble()", "!.get()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
