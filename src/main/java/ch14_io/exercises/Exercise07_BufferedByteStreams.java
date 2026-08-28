package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * EXERCICE 7 - Envelopper un stream BAS niveau dans un stream HAUT niveau : Buffered (niveau : moyen)
 * ====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Ecrire ou lire OCTET PAR OCTET, directement sur le disque a chaque
 * fois, est LENT (chaque petit acces disque a un cout). L'astuce
 * classique du chapitre : envelopper un stream BAS NIVEAU (qui parle
 * directement au disque, Exercise06) dans un stream HAUT NIVEAU comme
 * BufferedOutputStream/BufferedInputStream, qui accumule les donnees
 * dans un GROS PAQUET en memoire, et ne parle au disque que rarement,
 * par GROS paquets - beaucoup plus rapide, sans jamais changer le
 * resultat final.
 *
 *
 * ==================================================================
 * TODO 1 : writeBytesBuffered(file, data)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Fabriquer un FileOutputStream sur 'file' (le stream BAS
 *      niveau), et l'envelopper IMMEDIATEMENT dans un
 *      BufferedOutputStream (le stream HAUT niveau) - le tout dans un
 *      SEUL try-with-resources (fermer le buffered stream ferme
 *      automatiquement le stream bas niveau qu'il enveloppe).
 *   2. Ecrire 'data' via le BufferedOutputStream.
 *
 *
 * ==================================================================
 * TODO 2 : readAllBytesBuffered(file)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Fabriquer un FileInputStream sur 'file', enveloppe dans un
 *      BufferedInputStream, dans un try-with-resources.
 *   2. Renvoyer TOUT le contenu lu (readAllBytes() marche aussi bien
 *      a travers un BufferedInputStream qu'a travers un
 *      FileInputStream direct).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans un seul try-with-resources, avec un stream
 * imbrique dans l'autre.
 *
 * Exemple a verifier : ecrire/relire un tableau d'octets a travers les
 * versions "buffered" donne EXACTEMENT le meme resultat qu'a travers
 * les versions bas niveau de l'Exercise06 - la mise en tampon change
 * la PERFORMANCE, jamais le RESULTAT.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - new BufferedOutputStream(new FileOutputStream(file.toFile()))
 *   - new BufferedInputStream(new FileInputStream(file.toFile()))
 *   - Fermer (close(), automatique en try-with-resources) le stream
 *     HAUT niveau ferme EN CASCADE le stream BAS niveau qu'il
 *     enveloppe - jamais besoin de fermer les 2 separement.
 */
public class Exercise07_BufferedByteStreams {

    public static void writeBytesBuffered(Path file, byte[] data) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer writeBytesBuffered()");
    }

    public static byte[] readAllBytesBuffered(Path file) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer readAllBytesBuffered()");
    }

    public static void main(String[] args) throws IOException {
        Path tempDir = Files.createTempDirectory("io-ex07-");
        try {
            Path file = tempDir.resolve("data.bin");
            byte[] original = new byte[10_000];
            for (int i = 0; i < original.length; i++) {
                original[i] = (byte) (i % 128);
            }

            writeBytesBuffered(file, original);
            byte[] readBack = readAllBytesBuffered(file);

            ExerciseChecker.check("les octets relus via Buffered sont EXACTEMENT ceux ecrits",
                    Arrays.equals(original, readBack));
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
