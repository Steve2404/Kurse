package ch13_concurrency.drills.r01_threads;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : t1",
            "D02 : travail fait par Thread-N TERMINATED",
            "D03 : main t3",
            "D04 : NEW TIMED_WAITING true TERMINATED false",
            "D05 : interrompu, drapeau false",
            "D06 : true false",
            "D07 : true 10 5 IllegalThreadStateException",
            "D08 : WAITING TERMINATED IllegalMonitorStateException");
            // EXPECTED-END

    static final List<String> API = List.of(
            "new Thread(", "extends Thread", ".start()", ".join()",
            ".run()", ".getState()", ".join(20)", ".isAlive()",
            ".interrupt()", "Thread.interrupted()", ".setDaemon(true)", ".setPriority(Thread.MAX_PRIORITY)",
            "Thread.NORM_PRIORITY", "catch (IllegalThreadStateException", ".wait()", ".notifyAll()",
            "catch (IllegalMonitorStateException",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
