package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

/**
 * EXERCICE 19 - Les streams systeme (System.in/out/err) et la classe Console (niveau : moyen/difficile)
 * =====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Java fournit 3 "tuyaux" DEJA BRANCHES des le demarrage de tout
 * programme : System.in (ENTREE : ce que l'utilisateur tape),
 * System.out (SORTIE normale), System.err (SORTIE reservee aux
 * erreurs, separee expres, meme si elle s'affiche souvent au meme
 * endroit dans un terminal). System.in est un InputStream BAS niveau
 * (des octets bruts) - pour lire du TEXTE ligne par ligne
 * confortablement, on l'enveloppe dans un InputStreamReader (convertit
 * octets -> caracteres) PUIS un BufferedReader (Exercise14), exactement
 * comme pour un fichier.
 *
 * La classe Console offre des methodes PLUS RICHES (readPassword() qui
 * n'affiche RIEN a l'ecran pendant la saisie, format()...), MAIS
 * System.console() renvoie null des qu'AUCUN vrai terminal
 * interactif n'est attache au programme (execution via un IDE sans
 * emulation de terminal, via un script automatise, avec une entree
 * redirigee...) - un code qui utilise Console DOIT toujours verifier
 * ce cas avant de l'utiliser.
 *
 *
 * ==================================================================
 * TODO 1 : readFirstLine()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Envelopper System.in dans un InputStreamReader, PUIS dans un
 *      BufferedReader (PAS de try-with-resources ici : fermer ce
 *      reader fermerait DEFINITIVEMENT System.in pour tout le reste
 *      du programme - un cas special ou on NE ferme PAS le stream).
 *   2. Renvoyer reader.readLine() (la toute premiere ligne
 *      disponible).
 *
 *
 * ==================================================================
 * TODO 2 : isRunningWithoutConsole()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer (System.console() == null).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en 1-2 lignes.
 *
 *
 * ==================================================================
 * TODO 3 : readSecret(console, fallback)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour un mot de passe, Console.readPassword() est le bon outil : elle
 * N'AFFICHE PAS ce qu'on tape (readLine() l'afficherait), et rend un
 * char[] (qu'on peut effacer) plutot qu'un String (qui reste en memoire
 * sans qu'on puisse le forcer a disparaitre). Mais System.console() vaut
 * null sans vrai terminal (IDE, tests, redirection) : il faut alors un
 * chemin de REPLI qui lit une ligne dans fallback.
 *
 * -- Le plan --
 *
 *   1. console != null -> console.readPassword("Mot de passe : ").
 *   2. Sinon -> la ligne lue dans fallback, en char[] (null si plus rien a lire).
 *
 * main() teste le chemin de repli (console == null), le seul possible ici.
 *
 *
 * ==================================================================
 * TODO 4 : wipe(secret)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Remplacer chaque caractere du tableau par '*' (Arrays.fill).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. C'est justement POUR pouvoir faire ca qu'on prefere un char[] a un String.
 *
 * Exemple a verifier : en substituant TEMPORAIREMENT System.in avec
 * un faux flux (System.setIn(...), une technique standard pour
 * TESTER du code qui lit l'entree utilisateur), readFirstLine() doit
 * lire EXACTEMENT le texte injecte. Et puisque ce programme s'execute
 * ici sans terminal interactif reel, isRunningWithoutConsole() doit
 * renvoyer true.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - new BufferedReader(new InputStreamReader(System.in))
 *   - System.setIn(unAutreInputStream) remplace GLOBALEMENT
 *     System.in pour tout le programme - a restaurer avec
 *     System.setIn(ancienStream) une fois le test termine, sinon la
 *     substitution "fuit" vers le reste du programme.
 */
public class Exercise19_SystemStreamsAndConsole {

    public static String readFirstLine() throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer readFirstLine()");
    }

    public static boolean isRunningWithoutConsole() {
        throw new UnsupportedOperationException("TODO 2 : implementer isRunningWithoutConsole()");
    }

    public static char[] readSecret(Console console, BufferedReader fallback) throws IOException {
        throw new UnsupportedOperationException("TODO 3 : implementer readSecret()");
    }

    public static void wipe(char[] secret) {
        throw new UnsupportedOperationException("TODO 4 : implementer wipe()");
    }

    public static void main(String[] args) throws IOException {
        InputStream originalIn = System.in;
        try {
            String injected = "Bonjour depuis un faux stdin";
            System.setIn(new ByteArrayInputStream((injected + "\n").getBytes(StandardCharsets.UTF_8)));

            String line = readFirstLine();
            ExerciseChecker.check("readFirstLine() lit bien le texte injecte via System.setIn()",
                    injected.equals(line));
        } finally {
            System.setIn(originalIn);
        }

        ExerciseChecker.check("isRunningWithoutConsole() == true (pas de vrai terminal ici)",
                isRunningWithoutConsole());

        BufferedReader fallback = new BufferedReader(new StringReader("s3cret\n"));
        char[] secret = readSecret(null, fallback);
        ExerciseChecker.check("readSecret(null, ...) prend le chemin de repli -> s3cret ; puis null quand il n'y a plus rien",
                secret != null && new String(secret).equals("s3cret") && readSecret(null, fallback) == null);
        wipe(secret);
        ExerciseChecker.check("wipe efface le secret : ******", new String(secret).equals("******"));

        ExerciseChecker.summary();
    }
}
