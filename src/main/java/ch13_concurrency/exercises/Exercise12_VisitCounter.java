package ch13_concurrency.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * EXERCICE 12 - Compteur de visites concurrent : merge, newKeySet, et la regle "modifier pendant qu'on itere" (niveau : avance)
 * =============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ThreadBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un site recoit 10 000 visites, traitees par 8 threads EN MEME TEMPS
 * (parallelForEach, deja ecrit plus bas, les repartit et attend la fin).
 * Avec une HashMap et un "get puis put", deux threads peuvent lire la
 * meme ancienne valeur : une visite est perdue (race condition). Avec
 * ConcurrentHashMap ET une operation atomique (merge, compute...), le
 * total est exact a chaque fois.
 *
 * Attention : ConcurrentHashMap ne suffit pas si on fait soi-meme
 * "v = get(k); put(k, v + 1)" : chaque appel est sur, mais pas les DEUX
 * ensemble. C'est merge qui rend le tout atomique.
 *
 *
 * ==================================================================
 * TODO 1 : countVisits(pages, threads)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   10 000 visites, page "p" + (i % 7) -> p0..p3 = 1429, p4..p6 = 1428, a chaque execution
 *
 * -- Le plan --
 *
 *   1. Une ConcurrentHashMap<String, Integer>.
 *   2. parallelForEach(pages, threads, page -> map.merge(page, 1, Integer::sum)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : parallelForEach (donnee).
 *
 *
 * ==================================================================
 * TODO 2 : uniqueVisitors(visitorIds, threads)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Un Set concurrent : ConcurrentHashMap.newKeySet().
 *   2. add() depuis tous les threads ; rendre la taille.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : parallelForEach.
 *
 *
 * ==================================================================
 * TODO 3 : bestScores(scores, threads)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * scores : des "utilisateur=points". Garder pour chaque utilisateur son
 * MEILLEUR score, en parallele, sans en perdre aucun.
 *
 * -- Le plan --
 *
 *   1. Couper "u3=870" en nom et points.
 *   2. merge(nom, points, Integer::max) dans une ConcurrentHashMap.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : parallelForEach.
 *
 *
 * ==================================================================
 * TODO 4 : modifyWhileIterating(kind)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * UN SEUL thread parcourt une collection de 3 elements avec un for-each
 * et AJOUTE un element a chaque tour. Selon la collection, ca leve
 * ConcurrentModificationException... ou pas. kind vaut "ArrayList",
 * "synchronizedList" (Collections.synchronizedList), "CopyOnWriteArrayList",
 * "HashMap" (ajout dans la map en parcourant keySet()), "ConcurrentHashMap"
 * (pareil). Rendre "ConcurrentModificationException" ou "OK" ; main()
 * le fait VRAIMENT et compare.
 *
 * -- Le plan --
 *
 *   1. Les collections "java.util.concurrent" (CopyOnWrite..., Concurrent...) -> "OK".
 *   2. Les autres, meme synchronisees -> "ConcurrentModificationException".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. Piege : synchronizedList protege chaque APPEL, pas l'iteration.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Map<String, Integer> visits = new ConcurrentHashMap<>();
 *   - Set<String> seen = ConcurrentHashMap.newKeySet();
 *   - String[] parts = s.split("="); Integer.parseInt(parts[1]);
 */
public class Exercise12_VisitCounter {

    // Donnee : repartit items entre threads threads, applique action, et attend la fin de tout.
    public static <T> void parallelForEach(List<T> items, int threads, Consumer<T> action) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (T item : items) {
            pool.submit(() -> action.accept(item));
        }
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
    }

    public static Map<String, Integer> countVisits(List<String> pages, int threads) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 1 : implementer countVisits()");
    }

    public static int uniqueVisitors(List<String> visitorIds, int threads) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 2 : implementer uniqueVisitors()");
    }

    public static Map<String, Integer> bestScores(List<String> scores, int threads) throws InterruptedException {
        throw new UnsupportedOperationException("TODO 3 : implementer bestScores()");
    }

    public static String modifyWhileIterating(String kind) {
        throw new UnsupportedOperationException("TODO 4 : implementer modifyWhileIterating()");
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> pages = new ArrayList<>();
        List<String> visitors = new ArrayList<>();
        List<String> scores = new ArrayList<>();
        Map<String, Integer> expectedBest = new HashMap<>();
        for (int i = 0; i < 10_000; i++) {
            pages.add("p" + (i % 7));
            visitors.add("u" + (i % 500));
            String user = "u" + (i % 50);
            int points = (i * 37) % 1000;
            scores.add(user + "=" + points);
            expectedBest.merge(user, points, Integer::max);
        }

        boolean exact = true;
        for (int run = 0; run < 3; run++) {
            Map<String, Integer> visits = countVisits(pages, 8);
            exact &= new TreeMap<>(visits).toString().equals("{p0=1429, p1=1429, p2=1429, p3=1429, p4=1428, p5=1428, p6=1428}");
        }
        ExerciseChecker.check("countVisits : p0..p3 = 1429, p4..p6 = 1428, trois fois de suite", exact);
        ExerciseChecker.check("uniqueVisitors == 500", uniqueVisitors(visitors, 8) == 500);
        ExerciseChecker.check("bestScores == le meilleur score de chacun des 50 utilisateurs",
                expectedBest.equals(bestScores(scores, 8)));

        int agree = 0;
        for (String kind : List.of("ArrayList", "synchronizedList", "CopyOnWriteArrayList", "HashMap", "ConcurrentHashMap")) {
            if (modifyWhileIterating(kind).equals(jvm(kind))) {
                agree++;
            }
        }
        ExerciseChecker.check("modifyWhileIterating == JVM sur 5 collections (" + agree + " d'accord)", agree == 5);

        ExerciseChecker.summary();
    }

    // ---- Le juge : modifie VRAIMENT pendant un for-each (ne pas modifier) ----

    static String jvm(String kind) {
        try {
            if (kind.endsWith("HashMap")) {
                Map<String, Integer> map = kind.equals("HashMap") ? new HashMap<>() : new ConcurrentHashMap<>();
                map.put("a", 1);
                map.put("b", 2);
                map.put("c", 3);
                int n = 0;
                for (String key : map.keySet()) {
                    if (n < 3) {
                        map.put(key + n++, 0);   // borne : l'iterateur de ConcurrentHashMap peut voir les nouvelles cles
                    }
                }
            } else {
                List<String> base = List.of("a", "b", "c");
                List<String> list = kind.equals("ArrayList") ? new ArrayList<>(base)
                        : kind.equals("synchronizedList") ? Collections.synchronizedList(new ArrayList<>(base))
                        : new CopyOnWriteArrayList<>(base);
                for (String s : list) {
                    list.add(s + "!");
                }
            }
            return "OK";
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }
}
