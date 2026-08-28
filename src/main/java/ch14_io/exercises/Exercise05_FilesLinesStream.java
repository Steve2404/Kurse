package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * EXERCICE 5 - Lire un fichier texte comme un Stream avec Files.lines() (niveau : moyen/difficile)
 * =================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Files.lines(path) transforme un fichier texte en Stream<String> : UNE
 * ligne = UN element du stream. Comme les autres methodes NIO.2 vues
 * a l'Exercise04, ce Stream reste BRANCHE sur un fichier reellement
 * ouvert - toujours un try-with-resources, jamais d'exception. Le
 * gros avantage : le fichier n'est PAS charge entierement en memoire
 * d'un coup (contrairement a Files.readAllLines()), les lignes sont
 * lues PARESSEUSEMENT, une a une, au fil du traitement du stream -
 * utile pour de tres gros fichiers.
 *
 *
 * ==================================================================
 * TODO : sumOfLineLengths(file)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Fichier contenant 3 lignes : "ab" (2), "cde" (3), "f" (1). Total :
 * 2 + 3 + 1 = 6.
 *
 * -- Le plan --
 *
 *   1. try (Stream<String> lines = Files.lines(file)) { renvoyer
 *      lines.mapToInt(String::length).sum() DANS le try. }
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule chaine d'appels dans un try-with-resources.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Files.lines(path) utilise UTF-8 par defaut (il existe une
 *     surcharge acceptant un Charset explicite si besoin).
 *   - String::length compte les CARACTERES de la ligne, JAMAIS le
 *     caractere de fin de ligne lui-meme (\n ou \r\n) - Files.lines()
 *     le retire toujours avant de fabriquer chaque element du stream.
 */
public class Exercise05_FilesLinesStream {

    public static int sumOfLineLengths(Path file) throws IOException {
        throw new UnsupportedOperationException("TODO : implementer sumOfLineLengths()");
    }

    public static void main(String[] args) throws IOException {
        Path tempDir = Files.createTempDirectory("io-ex05-");
        try {
            Path file = tempDir.resolve("lignes.txt");
            Files.write(file, List.of("ab", "cde", "f"), StandardCharsets.UTF_8);

            ExerciseChecker.check("sumOfLineLengths('ab','cde','f') == 6", sumOfLineLengths(file) == 6);
        } finally {
            try (Stream<Path> cleanup = Files.walk(tempDir)) {
                cleanup.sorted(java.util.Comparator.reverseOrder()).forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException ignored) {
                    }
                });
            }
        }

        ExerciseChecker.summary();
    }
}
