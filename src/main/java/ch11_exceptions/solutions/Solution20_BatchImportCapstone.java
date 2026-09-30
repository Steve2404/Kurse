package ch11_exceptions.solutions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Corrige de l'exercice 20. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise20_BatchImportCapstone.
 */
public class Solution20_BatchImportCapstone {

    public static class RecordProcessor implements AutoCloseable {
        private final List<String> trace;
        private int processedCount;
        private int failedCount;

        public RecordProcessor(List<String> trace) {
            this.trace = trace;
        }

        public void recordSuccess() {
            // Le processeur ne fait que compter ; le resume n'est ecrit qu'une fois, dans close().
            processedCount++;
        }

        public void recordFailure() {
            // Un echec compte, mais n'arrete pas le lot.
            failedCount++;
        }

        @Override
        public void close() {
            // Appele automatiquement par le try-with-resources, meme si une exception remonte.
            trace.add("processed:" + processedCount + ";failed:" + failedCount);
        }
    }

    public static List<Integer> processAll(List<String> rawRecords, Locale locale,
                                            List<String> trace, List<String> errorMessages) {
        // try/catch PAR enregistrement pour continuer apres une erreur ; le bundle fournit le message localise (repli sur la racine).
        try (RecordProcessor processor = new RecordProcessor(trace)) {
            ResourceBundle bundle = ResourceBundle.getBundle("ch11_exceptions.messages", locale);
            List<Integer> results = new ArrayList<>();
            for (String raw : rawRecords) {
                try {
                    results.add(Integer.parseInt(raw));
                    processor.recordSuccess();
                } catch (NumberFormatException e) {
                    errorMessages.add(MessageFormat.format(bundle.getString("invalidRecord"), raw));
                    processor.recordFailure();
                }
            }
            return results;
        }
    }
}
