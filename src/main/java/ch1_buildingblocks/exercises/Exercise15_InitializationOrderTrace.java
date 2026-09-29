package ch1_buildingblocks.exercises;

import ch1_buildingblocks.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 15 - L'ordre d'initialisation complet : static une seule fois, puis instance a chaque objet (niveau : difficile)
 * =======================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_MainMethodArgs.java.
 *
 * -- Le contexte --
 *
 * L'Exercise14 a montre qu'a la creation d'un objet, les champs et
 * blocs d'instance passent AVANT le corps du constructeur. Il manque
 * la partie "static" : la CLASSE elle-meme s'initialise, UNE SEULE
 * FOIS, la premiere fois qu'on s'en sert. L'ordre complet :
 *
 *   premiere utilisation de la classe (une seule fois) :
 *     1. champs static et blocs static, dans l'ordre du fichier
 *   puis a CHAQUE new :
 *     2. champs d'instance et blocs d'instance, dans l'ordre du fichier
 *     3. corps du constructeur
 *
 * La classe Ticket ci-dessous note chaque etape dans TRACE (tout est
 * deja ecrit, sauf le constructeur et describe). Tu completes, et le
 * main() verifie la trace EXACTE pour 2 tickets.
 *
 * Detail important : TRACE est declare EN PREMIER. Les champs static
 * s'initialisent de haut en bas : s'il etait declare apres
 * nextNumber, il serait encore null quand nextNumber essaierait
 * d'ecrire dedans (NullPointerException au chargement de la classe).
 *
 *
 * ==================================================================
 * TODO 1 : Ticket() - le constructeur
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Au guichet, le distributeur de numeros (nextNumber, partage par tous
 * les tickets) a ete installe une seule fois, le matin. Quand un ticket
 * est imprime, son papier et son tampon "ouvert" sont deja poses
 * (champ et bloc d'instance) ; le constructeur, en DERNIER, note son
 * passage et prend le numero suivant.
 *
 * -- Essayons a la main --
 *
 *   1er new Ticket() : trace + [champ static, bloc static, champ d'instance,
 *                               bloc d'instance, constructeur] ; number = 1
 *   2e new Ticket()  : trace + [champ d'instance, bloc d'instance,
 *                               constructeur] ; number = 2
 *
 * -- Le plan --
 *
 *   1. Noter "constructeur" dans la trace (avec record).
 *   2. number = la valeur actuelle de nextNumber, puis augmenter
 *      nextNumber de 1.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : record existe deja.
 *
 *
 * ==================================================================
 * TODO 2 : describe()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On imprime le ticket : son numero et son statut (pose par le champ
 * d'instance avant meme le constructeur).
 *
 * -- Essayons a la main --
 *
 *   1er ticket -> "Ticket #1 (ouvert)"
 *
 * -- Le plan --
 *
 *   1. Rendre "Ticket #" + number + " (" + status + ")".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : trace de 8 etapes pour 2 tickets (voir TODO 1),
 * numeros 1 et 2, describe du 1er.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - record("constructeur");
 *   - number = nextNumber++;   (lit PUIS augmente : post-incrementation)
 */
public class Exercise15_InitializationOrderTrace {

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
            throw new UnsupportedOperationException("TODO 1 : implementer le constructeur Ticket()");
        }

        String describe() {
            throw new UnsupportedOperationException("TODO 2 : implementer describe()");
        }

        static <T> T record(String step, T value) {
            TRACE.add(step);
            return value;
        }

        static void record(String step) {
            TRACE.add(step);
        }
    }

    public static void main(String[] args) {
        Ticket first = new Ticket();
        Ticket second = new Ticket();

        ExerciseChecker.check("1 trace exacte pour 2 tickets (static une seule fois, constructeur en dernier)",
                Ticket.TRACE.equals(List.of("champ static", "bloc static",
                        "champ d'instance", "bloc d'instance", "constructeur",
                        "champ d'instance", "bloc d'instance", "constructeur")));
        ExerciseChecker.check("1 numeros 1 et 2", first.number == 1 && second.number == 2);
        ExerciseChecker.check("2 describe() == \"Ticket #1 (ouvert)\"", first.describe().equals("Ticket #1 (ouvert)"));

        ExerciseChecker.summary();
    }
}
