package ch13_concurrency.drills.r04_collections;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : {a=12, b=8, c=4}",
            "D02 : NullPointerException true 0 12 1",
            "D03 : ConcurrentModificationException ok[1, 3] [1, 3] [b, a]",
            "D04 : {10=a, 20=b, 30=c} 10 {10=a, 20=b} 20 [abricot, kiwi, pomme] pomme",
            "D05 : true false x y null",
            "D06 : [1, 2, 3, 4] 4 1 3 1");
            // EXPECTED-END

    static final List<String> API = List.of(
            "new ConcurrentHashMap<>()", ".merge(", "catch (NullPointerException", "Collections.synchronizedMap(",
            ".putIfAbsent(", "catch (ConcurrentModificationException", "new CopyOnWriteArrayList<>(", "new CopyOnWriteArraySet<>(",
            "new ConcurrentSkipListMap<>(", "new ConcurrentSkipListSet<>(", "new LinkedBlockingQueue<>(2)", ".offer(\"z\", 10, TimeUnit.MILLISECONDS)",
            ".take()", "new LinkedBlockingDeque<>()", ".offerFirst(", ".putLast(",
            ".takeFirst()", "new ConcurrentLinkedDeque<>(",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
