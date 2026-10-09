package ch18_design.drills.r03_builder.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Les tests de reference du drill 3. */
class Recall03Test {

    static final LocalDate DAY = LocalDate.of(2026, 12, 20);

    private static String refusal(Trip.Builder builder) {
        return assertThrows(IllegalStateException.class, builder::build).getMessage();
    }

    @Test
    void d01() {
        assertSame(City.of(" par "), City.of("PAR"));
        assertEquals("LYS", City.of("lys").code());
    }

    @ParameterizedTest
    @ValueSource(strings = {"PA", "PARI", "P4R", ""})
    void d02(String raw) {
        assertThrows(IllegalArgumentException.class, () -> City.of(raw));
    }

    @Test
    void d03() {
        assertEquals("depart et arrivee obligatoires", refusal(Trip.builder().to("LYS").date(DAY)));
        assertEquals("depart et arrivee identiques", refusal(Trip.builder().from("PAR").to("par").date(DAY)));
        assertEquals("date obligatoire", refusal(Trip.builder().from("PAR").to("LYS")));
        assertEquals("passagers : de 1 a 9", refusal(Trip.builder().from("PAR").to("LYS").date(DAY).passengers(0)));
        assertEquals("passagers : de 1 a 9", refusal(Trip.builder().from("PAR").to("LYS").date(DAY).passengers(10)));
        assertEquals(9, Trip.builder().from("PAR").to("LYS").date(DAY).passengers(9).build().passengers());
    }

    @Test
    void d04() {
        Trip.Builder builder = Trip.builder().from("PAR").to("LYS").date(DAY).option("velo");
        Trip first = builder.build();
        builder.option("chien").passengers(3);
        assertEquals(List.of("velo"), first.options());
        assertEquals(1, first.passengers());
        assertThrows(UnsupportedOperationException.class, () -> first.options().add("x"));
    }

    @Test
    void d05() {
        Trip original = Trip.builder().from("PAR").to("LYS").date(DAY).passengers(2).option("velo").build();
        Trip back = original.toBuilder().from("LYS").to("PAR").date(DAY.plusDays(7)).build();
        assertEquals(City.of("PAR"), original.from());
        assertEquals(City.of("LYS"), back.from());
        assertEquals(DAY.plusDays(7), back.date());
        assertEquals(2, back.passengers());
        assertEquals(List.of("velo"), back.options());
    }

    @Test
    void d06() {
        Trip trip = Trip.oneWay("bod", "nce", DAY);
        assertEquals(City.of("BOD"), trip.from());
        assertEquals(City.of("NCE"), trip.to());
        assertEquals(1, trip.passengers());
        assertEquals(List.of(), trip.options());
    }
}
