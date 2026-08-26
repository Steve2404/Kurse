package io.exercises;

import io.ExerciseChecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * EXERCICE 8 - Streams de CARACTERES : FileWriter/FileReader + Buffered (niveau : moyen)
 * =======================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * FileOutputStream/FileInputStream (Exercise06) manipulent des OCTETS
 * BRUTS - parfait pour des images, des fichiers binaires... mais pas
 * ideal pour du TEXTE (il faudrait gerer soi-meme l'encodage des
 * caracteres). FileWriter/FileReader font le TRAVAIL INVERSE : ils
 * manipulent directement des CARACTERES (deja convertis depuis/vers
 * les octets du fichier, selon un encodage), et leur nom se termine
 * en "Writer"/"Reader" (jamais "Stream") - c'est LA regle du chapitre
 * pour reconnaitre un stream de caracteres.
 *
 * Comme pour les octets, on enveloppe le stream BAS niveau
 * (FileWriter/FileReader) dans un stream HAUT niveau
 * (BufferedWriter/BufferedReader) pour la performance ET pour des
 * methodes pratiques supplementaires (newLine(), readLine()...).
 *
 *
 * ==================================================================
 * TODO 1 : writeLines(file, lines)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()))) {
 *   2.     Pour chaque ligne de 'lines' : writer.write(ligne), PUIS
 *          writer.newLine() (ajoute le bon separateur de ligne pour
 *          le systeme d'exploitation courant).
 *      }
 *
 *
 * ==================================================================
 * TODO 2 : readLines(file)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
 *   2.     Preparer une liste vide.
 *   3.     Boucler : tant que reader.readLine() ne renvoie pas null,
 *          ajouter la ligne lue a la liste (readLine() renvoie null
 *          UNIQUEMENT a la toute fin du fichier - jamais d'exception
 *          pour "plus rien a lire").
 *   4.     Renvoyer la liste.
 *      }
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans un seul try-with-resources.
 *
 * Exemple a verifier : writeLines() avec ["Bonjour", "le", "monde"],
 * puis readLines() sur le MEME fichier renvoie EXACTEMENT la meme
 * liste, dans le meme ordre.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - writer.newLine() est PREFERABLE a ecrire "\n" a la main : elle
 *     utilise le bon separateur pour CHAQUE systeme d'exploitation
 *     (\n sous Linux/macOS, \r\n sous Windows), la ou "\n" en dur
 *     serait fige sur un seul.
 */
public class Exercise08_CharacterStreams {

    public static void writeLines(Path file, List<String> lines) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer writeLines()");
    }

    public static List<String> readLines(Path file) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer readLines()");
    }

    public static void main(String[] args) throws IOException {
        Path tempDir = Files.createTempDirectory("io-ex08-");
        try {
            Path file = tempDir.resolve("texte.txt");
            List<String> original = List.of("Bonjour", "le", "monde");

            writeLines(file, original);
            List<String> readBack = readLines(file);

            ExerciseChecker.check("les lignes relues sont EXACTEMENT celles ecrites, dans le meme ordre",
                    readBack.equals(original));
        } finally {
            try (Stream<Path> cleanup = Files.walk(tempDir)) {
                cleanup.sorted(Comparator.reverseOrder()).forEach(p -> {
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
