package ch13_concurrency.projects.p05_life;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON ParallelLife, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "populations (generation 0 a 40) : [576, 603, 214, 132, 105, 116, 105, 98, 102, 101, 110, 110, 129, 114, 130, 130, 128, 132, 138, 133, 143, 136, 127, 131, 130, 130, 131, 137, 123, 104, 99, 96, 89, 92, 89, 95, 92, 90, 95, 95, 104]",
            "finale 104, empreinte 2050180327243196825, identique au sequentiel true",
            "barriere : parties 4, en attente 0, cassee false",
            "barriere cassee : l'autre thread [interrompu], main BrokenBarrierException, isBroken true puis apres reset false");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.SIZE", "Data.GENERATIONS", "Data.WORKERS", "Data.alive(",
            "new CyclicBarrier(Data.WORKERS, () ->", ".await()", ".getParties()", ".getNumberWaiting()",
            ".isBroken()", "catch (BrokenBarrierException", ".reset()", ".interrupt()",
            "Executors.newFixedThreadPool(Data.WORKERS)",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "ParallelLife", args, EXPECTED, API);
    }
}
