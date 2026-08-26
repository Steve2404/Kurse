package io.solutions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise14_PrintStreamAndConsoleQuiz.
 */
public class Solution14_PrintStreamAndConsoleQuiz {

    public static String formatReport(String name, double score) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        printStream.printf(Locale.US, "Joueur : %s, Score : %.2f%n", name, score);
        return buffer.toString(StandardCharsets.UTF_8);
    }

    public static String formatSummary(int itemCount) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.printf(Locale.US, "Total : %d items%n", itemCount);
        printWriter.flush();
        return stringWriter.toString();
    }
}
