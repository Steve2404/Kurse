package ch19_final.projects.p06_performance.solution;

import ch19_final.projects.p06_performance.Data;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FastReportTest {

    // ------------------------------------------------------------------ le maitre etalon

    @RepeatedTest(150)
    void sameAsLegacyOnRealisticJournals(RepetitionInfo info) {
        List<String> lines = Data.generate(info.getCurrentRepetition() % 120, info.getCurrentRepetition());
        assertEquals(Data.LegacyReport.report(lines), FastReport.report(lines));
    }

    /** Peu de personnes, peu de jours, de petits points : beaucoup d'egalites, le terrain des bugs de tri. */
    @RepeatedTest(150)
    void sameAsLegacyWithManyTies(RepetitionInfo info) {
        SplittableRandom random = new SplittableRandom(1000 + info.getCurrentRepetition());
        List<String> lines = new ArrayList<>();
        int n = random.nextInt(40);
        for (int i = 0; i < n; i++) {
            lines.add(random.nextInt(10) == 0 && !lines.isEmpty() ? lines.get(random.nextInt(lines.size()))
                    : "2026-10-0" + (1 + random.nextInt(3)) + ";" + "abc".charAt(random.nextInt(3)) + ";"
                    + "XYZ".charAt(random.nextInt(3)) + ";" + random.nextInt(3) + ";t" + random.nextInt(4));
        }
        assertEquals(Data.LegacyReport.report(lines), FastReport.report(lines));
    }

    // ------------------------------------------------------------------ des cas choisis

    @Test
    void emptyJournal() {
        assertEquals("lignes : 0 (invalides : 0, doublons : 0)\npersonnes :\ncolonnes :\njour le plus charge : aucun\n",
                FastReport.report(List.of()));
    }

    @Test
    void fullReport() {
        List<String> lines = List.of(
                "2026-10-02;bob;Fini;3;Pneu",
                "2026-10-01;ada;En cours;2;Selle",
                "2026-10-02;ada;Fini;1;Cadre",
                "2026-10-02;bob;Fini;3;Pneu",
                "pas une ligne",
                "2026-10-01;cid;A faire;3;Facture; client Dupont");
        assertEquals("""
                lignes : 6 (invalides : 1, doublons : 1)
                personnes :
                  ada : 3 points, 2 taches
                  bob : 3 points, 1 taches
                  cid : 3 points, 1 taches
                colonnes :
                  A faire : 1
                  En cours : 1
                  Fini : 2
                jour le plus charge : 2026-10-01 (2 evenements)
                """, FastReport.report(lines));
    }

    @Test
    void busiestDayTieGoesToTheEarliest() {
        List<String> lines = List.of("2026-10-09;ada;Fini;1;a", "2026-10-03;ada;Fini;1;b", "2026-10-09;bob;Fini;1;c",
                "2026-10-03;bob;Fini;1;d", "2026-10-05;bob;Fini;1;e");
        assertTrue(FastReport.report(lines).endsWith("jour le plus charge : 2026-10-03 (2 evenements)\n"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "2026-10-03;ada;Fini", "2026-1-03;bob;Fini;2;Pneu", "2026-10-03;Bob;Fini;2;Pneu",
            "2026-10-03;bob;Fini;deux;Pneu", "2026-10-03;bob;Fini;2;", "2026-10-03;bob;;2;Pneu",
            "2026-10-03;bob;Fini;12345;Pneu", "2026-10-03;bob;Fini;-2;Pneu", "2026-10-03;bo-b;Fini;2;Pneu"})
    void invalidLines(String line) {
        assertEquals(Optional.empty(), TaskEvent.parse(line));
        assertTrue(FastReport.report(List.of(line)).startsWith("lignes : 1 (invalides : 1, doublons : 0)"));
    }

    @Test
    void legacyQuirksAreKept() {
        assertEquals(Optional.of(new TaskEvent("2026-13-45", "ada", "Fini", 12)), TaskEvent.parse("2026-13-45;ada;Fini;0012;x"));
        assertEquals(Optional.of(new TaskEvent("2026-10-03", "bob", "En cours", 9999)),
                TaskEvent.parse("2026-10-03;bob;En cours;9999;Facture; client; Dupont"));
    }

    @Test
    void duplicatedInvalidLinesCountAsInvalid() {
        assertTrue(FastReport.report(List.of("x", "x", "2026-10-03;ada;Fini;1;a", "2026-10-03;ada;Fini;1;a"))
                .startsWith("lignes : 4 (invalides : 2, doublons : 1)"));
    }

    // ------------------------------------------------------------------ la vitesse

    @Test
    void twoHundredThousandLinesUnderTwoSeconds() {
        List<String> lines = Data.generate(200_000, 7);
        String report = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> FastReport.report(lines));
        assertTrue(report.startsWith("lignes : 200000 (invalides : "));
    }

    // ------------------------------------------------------------------ le banc de mesure

    @Test
    void benchRunsWarmupsThenMeasuredRuns() {
        AtomicInteger calls = new AtomicInteger();
        Bench.Result result = Bench.measure(calls::incrementAndGet, 7, 5);
        assertEquals(12, calls.get());
        assertEquals(5, result.nanos().length);
        assertTrue(result.min() <= result.median() && result.median() <= result.max());
        for (int i = 1; i < result.nanos().length; i++) {
            assertTrue(result.nanos()[i - 1] <= result.nanos()[i]);
        }
    }

    @Test
    void benchMedianIsTheMiddle() {
        Bench.Result r = new Bench.Result(new long[]{1_000_000, 2_000_000, 9_000_000});
        assertEquals(2_000_000, r.median());
        assertEquals("mediane 2 ms (min 1, max 9)", r.toString());
        assertEquals(3_000_000, new Bench.Result(new long[]{1_000_000, 2_000_000, 3_000_000, 4_000_000}).median());
    }

    @Test
    void benchArgumentsAreChecked() {
        assertThrows(IllegalArgumentException.class, () -> Bench.measure(() -> 1, -1, 3));
        assertThrows(IllegalArgumentException.class, () -> Bench.measure(() -> 1, 0, 0));
        assertEquals(1, Bench.measure(() -> 1, 0, 1).nanos().length);
    }
}
