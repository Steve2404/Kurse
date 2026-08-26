package io.solutions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise10_SystemStreamsAndConsole.
 */
public class Solution10_SystemStreamsAndConsole {

    public static String readFirstLine() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static boolean isRunningWithoutConsole() {
        return System.console() == null;
    }
}
