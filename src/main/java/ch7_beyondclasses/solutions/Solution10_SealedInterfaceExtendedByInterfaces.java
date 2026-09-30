package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise10_SealedInterfaceExtendedByInterfaces.
 */
public class Solution10_SealedInterfaceExtendedByInterfaces {

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
        // Les sous-interfaces permises sont connues : instanceof avec variable pour chaque branche.
        if (cmd instanceof ReadCommand r) {
            return r.describe();
        } else if (cmd instanceof InsertCommand i) {
            return "INSERT " + i.payload();
        } else if (cmd instanceof DeleteCommand d) {
            return "DELETE " + d.id();
        }
        return "";
    }
}
