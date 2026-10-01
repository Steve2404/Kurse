package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * EXERCICE 16 - La carte des classes java.io : octets ou caracteres, entree ou sortie, bas ou haut niveau (niveau : difficile)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * L'examen te montre un nom de classe java.io et demande : octets ou
 * caracteres ? lecture ou ecriture ? bas ou haut niveau ? Les NOMS
 * donnent la reponse :
 *
 *   ...InputStream / ...OutputStream   -> OCTETS     ...Reader / ...Writer -> CARACTERES
 *   Input / Reader                     -> ENTREE     Output / Writer       -> SORTIE
 *   File...                            -> BAS niveau (branche directement sur un fichier)
 *   les autres (Buffered, Object, Print, InputStreamReader...) -> HAUT niveau (enveloppent un autre flux)
 *
 * Deux pieges : InputStreamReader / OutputStreamWriter sont des flux de
 * CARACTERES (ils CONVERTISSENT des octets), et PrintStream (System.out)
 * est un flux d'OCTETS de SORTIE.
 *
 * main() demande la verite a la JVM par REFLEXION : la classe herite-t-elle
 * de Reader ? a-t-elle un constructeur qui prend un autre flux ?
 *
 *
 * ==================================================================
 * TODO 1 : kind(className)       -> "byte" ou "char"
 * TODO 2 : direction(className)  -> "input" ou "output"
 * TODO 3 : level(className)      -> "low" ou "high"
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   InputStreamReader -> char, input, high ; PrintStream -> byte, output, high ; FileWriter -> char, output, low
 *
 * -- Le plan --
 *
 *   1. kind : se termine par Reader ou Writer -> "char" ; sinon "byte".
 *   2. direction : contient "Input" ou se termine par "Reader" -> "input" ; sinon "output".
 *   3. level : commence par "File" -> "low" ; sinon "high".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : firstLineUtf8(file)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Construire A LA MAIN la chaine complete, de l'octet a la ligne :
 * FileInputStream (bas niveau, octets) -> InputStreamReader (octets ->
 * caracteres, en UTF-8) -> BufferedReader (lecture par lignes). Rendre
 * la 1re ligne. Fermer le BufferedReader ferme toute la chaine.
 *
 * -- Le plan --
 *
 *   1. try (BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file.toFile()), UTF_8)))
 *   2. return r.readLine();
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - className.endsWith("Reader"), className.startsWith("File"), className.contains("Input")
 *   - StandardCharsets.UTF_8
 */
public class Exercise16_IoClassRules {

    public static String kind(String className) {
        throw new UnsupportedOperationException("TODO 1 : implementer kind()");
    }

    public static String direction(String className) {
        throw new UnsupportedOperationException("TODO 2 : implementer direction()");
    }

    public static String level(String className) {
        throw new UnsupportedOperationException("TODO 3 : implementer level()");
    }

    public static String firstLineUtf8(Path file) throws IOException {
        throw new UnsupportedOperationException("TODO 4 : implementer firstLineUtf8()");
    }

    public static void main(String[] args) throws Exception {
        List<String> classes = List.of("FileInputStream", "FileOutputStream", "FileReader", "FileWriter",
                "BufferedInputStream", "BufferedOutputStream", "BufferedReader", "BufferedWriter",
                "ObjectInputStream", "ObjectOutputStream", "InputStreamReader", "OutputStreamWriter",
                "PrintStream", "PrintWriter");
        int kindOk = 0;
        int dirOk = 0;
        int levelOk = 0;
        for (String name : classes) {
            Class<?> c = Class.forName("java.io." + name);
            if (kind(name).equals(Reader.class.isAssignableFrom(c) || Writer.class.isAssignableFrom(c) ? "char" : "byte")) {
                kindOk++;
            }
            if (direction(name).equals(InputStream.class.isAssignableFrom(c) || Reader.class.isAssignableFrom(c) ? "input" : "output")) {
                dirOk++;
            }
            if (level(name).equals(wrapsAnotherStream(c) ? "high" : "low")) {
                levelOk++;
            }
        }
        ExerciseChecker.check("kind == JVM (heritage de Reader/Writer) sur 14 classes (" + kindOk + " d'accord)", kindOk == 14);
        ExerciseChecker.check("direction == JVM (InputStream/Reader) sur 14 classes (" + dirOk + " d'accord)", dirOk == 14);
        ExerciseChecker.check("level == JVM (constructeur qui prend un autre flux) sur 14 classes (" + levelOk + " d'accord)", levelOk == 14);

        Path file = Files.createTempFile("ex16", ".txt");
        try {
            Files.writeString(file, "Les étoiles de Hypérion\nligne 2\n", StandardCharsets.UTF_8);
            ExerciseChecker.check("firstLineUtf8 lit la 1re ligne avec ses accents", "Les étoiles de Hypérion".equals(firstLineUtf8(file)));
        } finally {
            Files.deleteIfExists(file);
        }

        ExerciseChecker.summary();
    }

    // Haut niveau = au moins un constructeur public qui prend un InputStream, OutputStream, Reader ou Writer.
    static boolean wrapsAnotherStream(Class<?> c) {
        for (Constructor<?> k : c.getConstructors()) {
            for (Class<?> p : k.getParameterTypes()) {
                if (p == InputStream.class || p == OutputStream.class || p == Reader.class || p == Writer.class) {
                    return true;
                }
            }
        }
        return false;
    }
}
