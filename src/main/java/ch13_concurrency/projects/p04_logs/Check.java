package ch13_concurrency.projects.p04_logs;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON LogPipeline, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "lignes traitees 6000 / 6000, file vide true",
            "visites {/=1244, /compte=1207, /panier=1208, /produit=1182, /recherche=1159} ; duree moyenne (ms) /=516 /compte=506 /panier=510 /produit=522 /recherche=520",
            "page la plus vue /=1244 ; statuts {200=5353, 404=632, 500=15} ; erreurs 500 pour [ana, bob, chloe, dan, eve, fred, gina]",
            "requetes lentes 369, les 3 plus lentes [1009 ms bob /, 1009 ms bob /produit, 1009 ms chloe /compte]",
            "file de 1 : offer true puis false, poll 1 puis null, remainingCapacity 1",
            "ArrayList modifiee en boucle : ConcurrentModificationException ; CopyOnWriteArrayList : 3 tours, taille finale 6 [a, b, c, a!, b!, c!]",
            "ConcurrentHashMap.put(null) NullPointerException, HashMap.put(null) 1 ; x=100 y=10 ; synchronizedList [1, 2, 3] ; newKeySet true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.LINES", "Data.PRODUCERS", "Data.CONSUMERS", "Data.CAPACITY",
            "Data.POISON", "Data.line(", "BlockingQueue<String>", "new LinkedBlockingQueue<>(",
            ".take()", ".put(", "new ConcurrentHashMap<>()", ".merge(",
            "new ConcurrentSkipListMap<>()", "new ConcurrentSkipListSet<>()", "new ConcurrentLinkedQueue<>()", ".offer(",
            ".poll(10, TimeUnit.MILLISECONDS)", ".remainingCapacity()", "catch (ConcurrentModificationException", "new CopyOnWriteArrayList<>(",
            "catch (NullPointerException", ".putIfAbsent(", ".computeIfAbsent(", ".compute(",
            "Collections.synchronizedList(", "synchronized (synced)", "ConcurrentHashMap.newKeySet()",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "LogPipeline", args, EXPECTED, API);
    }
}
