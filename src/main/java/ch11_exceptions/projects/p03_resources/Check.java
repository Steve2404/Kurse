package ch11_exceptions.projects.p03_resources;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Resources, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "db cache ok : ouvre db, ouvre cache, db <- debut, cache <- fin, ferme cache, ferme db, finally",
            "db cache fail : ouvre db, ouvre cache, db <- debut, ferme cache, ferme db, attrape IllegalStateException(travail rate), finally",
            "db ~cache fail : ouvre db, ouvre ~cache, db <- debut, ferme ~cache, ferme db, attrape IllegalStateException(travail rate) supprimees [fermeture ratee de ~cache], finally",
            "~db cache ok : ouvre ~db, ouvre cache, ~db <- debut, cache <- fin, ferme cache, ferme ~db, attrape ResourceException(fermeture ratee de ~db), finally",
            "db !cache ok : ouvre db, ferme db, attrape ResourceException(ouverture ratee de !cache), finally",
            "~db ~cache fail : ouvre ~db, ouvre ~cache, ~db <- debut, ferme ~cache, ferme ~db, attrape IllegalStateException(travail rate) supprimees [fermeture ratee de ~cache, fermeture ratee de ~db], finally",
            "ressource existante : ouvre partage, partage <- un, ferme partage, attrape IllegalStateException(canal ferme : partage)",
            "Closeable idempotent : fermetures demandees 2, effectives 1",
            "reessais [timeout, timeout, ok:42] -> 42",
            "reessais [timeout, refus, panne] -> abandon apres 3 essais <- panne (essai 3) ; RetryExhaustedException(abandon apres 3 essais) supprimees [timeout (essai 1), refus (essai 2)]",
            "disjoncteur : ok/CLOSED echec/CLOSED echec/CLOSED echec/OPEN refus/OPEN refus/OPEN ok/CLOSED echec/CLOSED ok/CLOSED ok/CLOSED echec/CLOSED echec/CLOSED echec/OPEN refus/OPEN refus/OPEN echec/OPEN refus/OPEN",
            "servis 4, echecs 8, refus 5, etat final OPEN");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.JOBS", "Data.FLAKY", "Data.DOWN", "Data.MAX_ATTEMPTS",
            "Data.CALLS", "Data.THRESHOLD", "Data.PAUSE", "implements AutoCloseable",
            "implements Closeable", "public void close() throws ResourceException", "try (Channel first = new Channel(", "try (shared)",
            "try (journal)", ".getSuppressed()", "addSuppressed", "catch (ResourceException | IllegalStateException",
            "finally", "enum State", "interface Attempt<T>", "throws ServiceException",
            "throw new CircuitOpenException(", "catch (CircuitOpenException", "catch (ServiceException", "catch (RetryExhaustedException",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Resources", args, EXPECTED, API);
    }
}
