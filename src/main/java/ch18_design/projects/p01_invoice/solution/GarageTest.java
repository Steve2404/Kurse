package ch18_design.projects.p01_invoice.solution;

import ch18_design.projects.p01_invoice.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Le FILET DE SECURITE du refactoring : tant que ces tests passent, la nouvelle facture est
 * caractere pour caractere celle du legacy. On les ecrit AVANT de toucher au code.
 */
class GarageTest {

    // Le temoin : le legacy et le nouveau code doivent produire exactement le meme texte.
    private static void sameAsLegacy(String customer, boolean loyal, List<String> lines) {
        assertEquals(Data.LegacyGarage.invoice(customer, loyal, lines), Garage.invoice(customer, loyal, lines),
                () -> "lignes : " + lines + ", fidele : " + loyal);
    }

    @Test
    @DisplayName("la facture d'exemple, recopiee telle que le legacy l'imprime")
    void sampleInvoice() {
        assertEquals("""
                FACTURE - Client : Dupont
                  Plaquettes de frein x2 : 90,00
                  Disque x2 : 125,00
                  Montage freins (1 h 45) : 126,00
                  Diagnostic (0 h 15) : 18,00
                Pieces : 215,00
                Main-d'oeuvre : 144,00
                Remise fidelite : -21,50
                Sous-total HT : 337,50
                TVA 20 % : 67,50
                Total TTC : 405,00
                """, Garage.invoice("Dupont", true, Data.SAMPLE));
    }

    // Le MAITRE ETALON (golden master) : des centaines de factures au hasard, mais reproductibles
    // (la graine est le numero de la repetition), comparees au legacy.
    @RepeatedTest(300)
    void goldenMaster(RepetitionInfo info) {
        Random random = new Random(info.getCurrentRepetition());
        List<String> lines = new ArrayList<>();
        int count = random.nextInt(7);
        for (int i = 0; i < count; i++) {
            lines.add(random.nextBoolean()
                    ? "PART;Piece " + i + ";" + (1 + random.nextInt(5)) + ";" + random.nextInt(15_000)
                    : "LABOR;Travail " + i + ";" + (1 + random.nextInt(200)));
        }
        sameAsLegacy("Client " + info.getCurrentRepetition(), random.nextBoolean(), lines);
    }

    // Le hasard tombe rarement PILE sur un seuil : les limites se testent a la main.
    @Test
    void thresholdsAndRounding() {
        sameAsLegacy("A", true, List.of("PART;Pile;1;20000"));
        sameAsLegacy("B", true, List.of("PART;Pile;1;19999"));
        sameAsLegacy("C", false, List.of("PART;Pile;1;20000"));
        sameAsLegacy("D", false, List.of("LABOR;Long;240"));
        sameAsLegacy("E", false, List.of("LABOR;Long;226"));
        sameAsLegacy("F", true, List.of("PART;Vis;1;5", "LABOR;Court;1"));
        sameAsLegacy("G", true, List.of());
    }

    @ParameterizedTest
    @ValueSource(strings = {"PART;X;0;100", "PART;X;1;-1", "PART;X;1", "PART;X;1;2;3", "LABOR;X;0", "LABOR;X",
            "TAXI;X;1", "", "part;X;1;100"})
    void sameRefusalsAsLegacy(String line) {
        IllegalArgumentException legacy = assertThrows(IllegalArgumentException.class,
                () -> Data.LegacyGarage.invoice("Z", true, List.of(line)));
        IllegalArgumentException mine = assertThrows(IllegalArgumentException.class,
                () -> Garage.invoice("Z", true, List.of(line)));
        assertEquals(legacy.getMessage(), mine.getMessage());
    }

    // Un comportement discutable (une NumberFormatException brute), mais c'est celui du legacy : on le garde.
    @Test
    void unreadableNumberStillEscapesAsNumberFormatException() {
        assertThrows(NumberFormatException.class, () -> Data.LegacyGarage.invoice("Z", true, List.of("PART;X;deux;100")));
        assertThrows(NumberFormatException.class, () -> Garage.invoice("Z", true, List.of("PART;X;deux;100")));
    }

    @Test
    @DisplayName("etape 7 : un forfait ne compte pas dans la remise fidelite, mais paie la TVA")
    void feeLine() {
        assertEquals("""
                FACTURE - Client : Ba
                  Pneu x4 : 200,00
                  Recyclage (forfait) : 3,50
                Pieces : 200,00
                Main-d'oeuvre : 0,00
                Forfaits : 3,50
                Remise fidelite : -20,00
                Sous-total HT : 183,50
                TVA 20 % : 36,70
                Total TTC : 220,20
                """, Garage.invoice("Ba", true, List.of("PART;Pneu;4;5000", "FEE;Recyclage;350")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"FEE;X;-1", "FEE;X", "FEE;X;1;2"})
    void invalidFees(String line) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Garage.invoice("Z", true, List.of(line)));
        assertEquals("ligne invalide : " + line, e.getMessage());
    }
}
