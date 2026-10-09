package ch18_design.drills.r06_states.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Les tests de reference du drill 6. */
class Recall06Test {

    private static Turnstile in(String state) {
        Turnstile gate = new Turnstile();
        switch (state) {
            case "deverrouille" -> gate.coin();
            case "en panne" -> gate.breakDown();
            default -> { }
        }
        return gate;
    }

    private static Consumer<Turnstile> action(String name) {
        return switch (name) {
            case "piece" -> Turnstile::coin;
            case "pousser" -> Turnstile::push;
            case "panne" -> Turnstile::breakDown;
            default -> Turnstile::repair;
        };
    }

    // Le tableau complet : 3 etats x 4 actions.
    @ParameterizedTest
    @CsvSource({"verrouille, piece, deverrouille", "verrouille, pousser, refus", "verrouille, panne, en panne", "verrouille, reparer, refus",
            "deverrouille, piece, refus", "deverrouille, pousser, verrouille", "deverrouille, panne, en panne", "deverrouille, reparer, refus",
            "en panne, piece, refus", "en panne, pousser, refus", "en panne, panne, refus", "en panne, reparer, verrouille"})
    void d01(String from, String actionName, String expected) {
        Turnstile gate = in(from);
        if (expected.equals("refus")) {
            assertEquals(actionName + " refuse (" + from + ")",
                    assertThrows(IllegalStateException.class, () -> action(actionName).accept(gate)).getMessage());
            assertEquals(from, gate.status());
        } else {
            action(actionName).accept(gate);
            assertEquals(expected, gate.status());
        }
    }

    @Test
    void d02() {
        Turnstile gate = new Turnstile();
        gate.coin();
        gate.push();
        gate.coin();
        assertThrows(IllegalStateException.class, gate::coin);
        gate.push();
        assertEquals(2, gate.coins());
        assertEquals(2, gate.passages());
    }

    @Test
    void d03() {
        Turnstile gate = new Turnstile();
        gate.coin();
        gate.breakDown();
        gate.repair();
        assertEquals(List.of("verrouille -> deverrouille", "deverrouille -> en panne", "en panne -> verrouille"), gate.events());
        assertThrows(UnsupportedOperationException.class, () -> gate.events().clear());
    }

    @Test
    void d04() {
        Turnstile gate = new Turnstile();
        gate.coin();
        gate.push();
        assertEquals("""
                Journal du tourniquet
                - verrouille -> deverrouille
                - deverrouille -> verrouille
                1 piece(s), 1 passage(s)
                """, new PlainReport().render(gate));
        assertEquals("""
                evenement
                verrouille -> deverrouille
                deverrouille -> verrouille
                """, new CsvReport().render(gate));
    }
}
