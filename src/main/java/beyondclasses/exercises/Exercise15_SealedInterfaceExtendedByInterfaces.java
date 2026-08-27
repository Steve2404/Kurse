package beyondclasses.exercises;

import beyondclasses.ExerciseChecker;

/**
 * EXERCICE 15 - Sealed interfaces : limiter aussi quelles INTERFACES ont le droit d'en etendre une autre (niveau : difficile)
 * ===================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * beyondclasses.exercises.Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Dans l'Exercise06/07, "permits" limitait quelles CLASSES (et
 * records) avaient le droit d'implementer un sealed interface. Mais
 * "permits" peut AUSSI limiter quelles autres INTERFACES ont le droit
 * d'ETENDRE ("extends") un sealed interface - les 2 usages peuvent
 * meme se MELANGER dans la MEME liste. Ici, Command (sealed) autorise
 * exactement 2 "familles" : ReadCommand (une INTERFACE, pas une
 * classe, qui la relaie plus loin) et WriteCommand (une AUTRE
 * interface, elle-meme sealed, qui referme sa PROPRE petite liste
 * fermee de 2 records). ReadCommand, elle, choisit non-sealed :
 * "a partir de moi, n'importe QUI peut a nouveau m'implementer
 * librement" - SelectCommand en profite, sans rien devoir a Command.
 *
 *
 * ==================================================================
 * TODO : describeCommand(cmd)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * new SelectCommand().describe() -> "SELECT". new
 * InsertCommand("Ada") -> une insertion. new DeleteCommand("42") ->
 * une suppression. describeCommand() doit savoir traiter les 3.
 *
 * -- Le plan --
 *
 *   1. Si cmd instanceof ReadCommand r : renvoyer r.describe() (peu
 *      importe QUELLE implementation concrete de ReadCommand c'est -
 *      ReadCommand etant non-sealed, il pourrait y en avoir
 *      d'autres que SelectCommand).
 *   2. Sinon si cmd instanceof InsertCommand i : renvoyer "INSERT " + i.payload().
 *   3. Sinon si cmd instanceof DeleteCommand d : renvoyer "DELETE " + d.id().
 *   4. Renvoyer "" par defaut (jamais vraiment atteint).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit.
 *
 * Exemple a verifier : describeCommand(new SelectCommand()) ==
 * "SELECT". describeCommand(new InsertCommand("Ada")) ==
 * "INSERT Ada". describeCommand(new DeleteCommand("42")) ==
 * "DELETE 42".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "sealed interface WriteCommand extends Command permits
 *     InsertCommand, DeleteCommand" : WriteCommand est A LA FOIS un
 *     sous-type PERMIS de Command (il figure dans le permits de
 *     Command), ET lui-meme sealed avec SON PROPRE permits pour SES
 *     propres sous-types - une chaine de listes fermees, chacune
 *     avec ses propres regles.
 */
public class Exercise15_SealedInterfaceExtendedByInterfaces {

    sealed interface Command permits ReadCommand, WriteCommand {
    }

    non-sealed interface ReadCommand extends Command {
        String describe();
    }

    sealed interface WriteCommand extends Command permits InsertCommand, DeleteCommand {
    }

    record InsertCommand(String payload) implements WriteCommand {
    }

    record DeleteCommand(String id) implements WriteCommand {
    }

    static class SelectCommand implements ReadCommand {
        @Override
        public String describe() {
            return "SELECT";
        }
    }

    public static String describeCommand(Command cmd) {
        throw new UnsupportedOperationException("TODO : implementer describeCommand()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("ReadCommand (interface non-sealed) implementee librement par SelectCommand",
                describeCommand(new SelectCommand()).equals("SELECT"));
        ExerciseChecker.check("WriteCommand (interface sealed) -> InsertCommand",
                describeCommand(new InsertCommand("Ada")).equals("INSERT Ada"));
        ExerciseChecker.check("WriteCommand (interface sealed) -> DeleteCommand",
                describeCommand(new DeleteCommand("42")).equals("DELETE 42"));

        ExerciseChecker.summary();
    }
}
