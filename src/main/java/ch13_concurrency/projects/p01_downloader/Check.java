package ch13_concurrency.projects.p01_downloader;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Downloader, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "decoupage : 4 morceaux de 250000 octets",
            "morceau 0 [0,250000) somme 417545, serie max 18, tete 2, queue 1, par dl-0 (Runnable)",
            "morceau 1 [250000,500000) somme 416243, serie max 16, tete 1, queue 1, par dl-1 (Thread)",
            "morceau 2 [500000,750000) somme 416714, serie max 17, tete 1, queue 2, par dl-2 (Runnable)",
            "morceau 3 [750000,1000000) somme 416273, serie max 16, tete 1, queue 1, par dl-3 (Thread)",
            "total : somme 1666775, plus longue serie 18 ; identique au calcul sequentiel true",
            "run() direct execute par main ; start() execute par dl-run",
            "etats : avant start NEW ; dormeur TIMED_WAITING, attente WAITING, bloque BLOCKED ; apres join TERMINATED",
            "journal : [bloque : verrou obtenu, dormeur : InterruptedException, drapeau apres catch false, boucle : arretee, drapeau true]",
            "daemon true, setDaemon apres start IllegalThreadStateException, start deux fois IllegalThreadStateException, priorite par defaut 5 (min 1, max 10)");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.SIZE", "Data.CHUNKS", "Data.at(", "implements Runnable",
            "extends Thread", "record ChunkStats(", ".merge(", "new Thread(",
            ".start()", ".join()", "Thread.currentThread().getName()", ".run()",
            "Thread.sleep(", ".getState()", "Thread.State.TIMED_WAITING", "Thread.State.WAITING",
            "Thread.State.BLOCKED", "synchronized (", ".interrupt()", ".isInterrupted()",
            "catch (InterruptedException", ".setDaemon(true)", ".isDaemon()", "catch (IllegalThreadStateException",
            ".getPriority()", "Thread.MAX_PRIORITY", "Collections.synchronizedList(",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Downloader", args, EXPECTED, API);
    }
}
