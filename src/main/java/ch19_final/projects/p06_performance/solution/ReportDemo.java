package ch19_final.projects.p06_performance.solution;

import ch19_final.projects.p06_performance.Data;

import java.util.List;

/**
 * La demonstration : les deux versions sur le meme journal de 1 000 lignes (le legacy ne peut pas faire plus
 * en un temps raisonnable), puis la version rapide seule sur 10 000, 100 000 et 1 000 000 de lignes.
 */
public final class ReportDemo {

    private ReportDemo() {
    }

    public static void main(String[] args) {
        List<String> small = Data.generate(1000, 42);
        System.out.println("memes rapports : " + FastReport.report(small).equals(Data.LegacyReport.report(small)));
        System.out.println("legacy, 1 000 lignes : " + Bench.measure(() -> Data.LegacyReport.report(small), 1, 3));
        System.out.println("rapide, 1 000 lignes : " + Bench.measure(() -> FastReport.report(small), 20, 21));
        for (int n : new int[]{10_000, 100_000, 1_000_000}) {
            List<String> lines = Data.generate(n, 42);
            System.out.println("rapide, " + n + " lignes : " + Bench.measure(() -> FastReport.report(lines), 3, 5));
        }
    }
}
