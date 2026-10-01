package ch14_io.solutions;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Corrige de l'exercice 19. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise19_SystemStreamsAndConsole.
 */
public class Solution19_SystemStreamsAndConsole {

    public static String readFirstLine() throws IOException {
        // PAS de try-with-resources : fermer ce reader fermerait System.in pour tout le programme.
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    public static boolean isRunningWithoutConsole() {
        // System.console() vaut null sans vrai terminal (IDE, redirection, tests).
        return System.console() == null;
    }

    public static char[] readSecret(Console console, BufferedReader fallback) throws IOException {
        // readPassword n'affiche pas la saisie et rend un char[] ; sans terminal, console vaut null : chemin de repli.
        if (console != null) {
            return console.readPassword("Mot de passe : ");
        }
        String line = fallback.readLine();
        return line == null ? null : line.toCharArray();
    }

    public static void wipe(char[] secret) {
        // Un char[] s'efface sur place ; un String est immuable et peut rester en memoire.
        Arrays.fill(secret, '*');
    }
}
