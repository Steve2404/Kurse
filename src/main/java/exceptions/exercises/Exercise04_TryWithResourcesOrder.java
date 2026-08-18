package exceptions.exercises;

import exceptions.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 4 - try-with-resources : fermeture dans l'ordre INVERSE de la declaration (niveau : moyen)
 * ==================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CheckedVsUnchecked.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que tu empiles 3 poupees russes les unes DANS les autres, en
 * les ouvrant dans l'ordre A, puis B, puis C. Pour tout refermer
 * proprement a la fin, tu dois forcement refermer C EN PREMIER (la
 * derniere ouverte), puis B, puis A EN DERNIER - jamais l'inverse. Un
 * try-with-resources fonctionne pareil : les ressources declarees sont
 * fermees automatiquement, TOUJOURS dans l'ordre INVERSE de leur
 * declaration.
 *
 *
 * ==================================================================
 * TODO : useResourcesInOrder(trace)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Ouvrir un try-with-resources qui declare 3 TrackedResource
 *      (deja fournie plus bas, pas besoin d'y toucher) : "A", "B",
 *      "C", DANS CET ORDRE, chacune recevant 'trace'.
 *   2. A l'INTERIEUR du corps du try, ajouter "use" a trace (pour
 *      marquer le moment ou le travail "utile" se produit, avant
 *      toute fermeture).
 *
 * -- Essayons a la main --
 *
 * Une fois le bloc termine, la trace doit contenir, DANS CET ORDRE :
 *   ["use", "close:C", "close:B", "close:A"]
 *
 * Remarque : meme si A a ete OUVERTE en premier, elle est FERMEE en
 * DERNIER - exactement comme la poupee russe la plus exterieure.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : un seul try-with-resources suffit, TrackedResource fait deja
 * tout le travail de tracage pour toi.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - try (TrackedResource a = new TrackedResource("A", trace);
 *          TrackedResource b = new TrackedResource("B", trace);
 *          TrackedResource c = new TrackedResource("C", trace)) {
 *         trace.add("use");
 *     }
 *   - Le point-virgule separe les ressources declarees ; Java se
 *     charge tout seul d'appeler close() sur chacune, dans l'ordre
 *     inverse, meme si une exception survient au milieu.
 */
public class Exercise04_TryWithResourcesOrder {

    static class TrackedResource implements AutoCloseable {
        private final String name;
        private final List<String> trace;

        TrackedResource(String name, List<String> trace) {
            this.name = name;
            this.trace = trace;
        }

        @Override
        public void close() {
            trace.add("close:" + name);
        }
    }

    public static void useResourcesInOrder(List<String> trace) {
        throw new UnsupportedOperationException("TODO : implementer useResourcesInOrder()");
    }

    public static void main(String[] args) {
        List<String> trace = new ArrayList<>();
        useResourcesInOrder(trace);

        ExerciseChecker.check("A, B, C fermees dans l'ordre INVERSE de leur declaration",
                trace.equals(List.of("use", "close:C", "close:B", "close:A")));

        ExerciseChecker.summary();
    }
}
