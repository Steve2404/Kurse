package exceptions.solutions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise14_BatchImportCapstone.
 */
public class Solution14_BatchImportCapstone {

    public static class RecordProcessor implements AutoCloseable {
        private final List<String> trace;
        private int processedCount;
        private int failedCount;

        public RecordProcessor(List<String> trace) {
            this.trace = trace;
        }

        public void recordSuccess() {
            processedCount++;
        }

        public void recordFailure() {
            failedCount++;
        }

        @Override
        public void close() {
            trace.add("processed:" + processedCount + ";failed:" + failedCount);
        }
    }

    public static List<Integer> processAll(List<String> rawRecords, Locale locale,
                                            List<String> trace, List<String> errorMessages) {
        try (RecordProcessor processor = new RecordProcessor(trace)) {
            ResourceBundle bundle = ResourceBundle.getBundle("exceptions.messages", locale);
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
