package ch1_buildingblocks.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise15_InitializationOrderTrace.
 */
public class Solution15_InitializationOrderTrace {

    static class Ticket {
        static final List<String> TRACE = new ArrayList<>();
        static int nextNumber = record("champ static", 1);

        static {
            record("bloc static");
        }

        String status = record("champ d'instance", "ouvert");

        {
            record("bloc d'instance");
        }

        final int number;

        Ticket() {
            // Le corps du constructeur passe APRES les champs et blocs d'instance ;
            // nextNumber++ rend l'ancienne valeur puis augmente le compteur partage.
            record("constructeur");
            number = nextNumber++;
        }

        String describe() {
            // status a ete pose par l'initialiseur de champ, avant meme le constructeur.
            return "Ticket #" + number + " (" + status + ")";
        }

        static <T> T record(String step, T value) {
            TRACE.add(step);
            return value;
        }

        static void record(String step) {
            TRACE.add(step);
        }
    }
}
