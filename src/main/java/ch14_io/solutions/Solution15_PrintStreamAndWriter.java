package ch14_io.solutions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise15_PrintStreamAndWriter.
 */
public class Solution15_PrintStreamAndWriter {

    public static String formatReport(String name, double score) {
        // PrintStream (octets, comme System.out) ne lance jamais IOException ; Locale.US fixe le point decimal.
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        printStream.printf(Locale.US, "Joueur : %s, Score : %.2f%n", name, score);
        return buffer.toString(StandardCharsets.UTF_8);
    }

    public static String formatSummary(int itemCount) {
        // PrintWriter (caracteres) ; flush sinon le texte peut rester dans le tampon ; %n = fin de ligne du systeme.
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.printf(Locale.US, "Total : %d items%n", itemCount);
        printWriter.flush();
        return stringWriter.toString();
    }
}
