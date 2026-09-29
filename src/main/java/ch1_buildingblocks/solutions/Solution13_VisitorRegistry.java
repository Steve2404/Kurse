package ch1_buildingblocks.solutions;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise13_VisitorRegistry.
 */
public class Solution13_VisitorRegistry {

    static class Visitor {
        static int created = 0;
        final int id;
        String name;

        Visitor(String name) {
            // created est PARTAGE (static) : chaque nouveau visiteur le fait avancer.
            // this.name = le champ ; "name" seul = le parametre, qui masque le champ.
            created++;
            id = created;
            this.name = name;
        }

        void rename(String name) {
            // Sans "this.", on ecrirait le parametre dans lui-meme : le champ ne changerait pas.
            this.name = name;
        }

        static void resetCounter() {
            // Methode static : pas de "this", seuls les champs static sont accessibles.
            created = 0;
        }
    }

    public static String badgeLine(Visitor... visitors) {
        // line est locale : elle repart de "" a chaque appel ; v n'existe que dans la boucle.
        String line = "";
        for (Visitor v : visitors) {
            if (!line.isEmpty()) {
                line += " ";
            }
            line += v.id + ":" + v.name;
        }
        return line;
    }
}
