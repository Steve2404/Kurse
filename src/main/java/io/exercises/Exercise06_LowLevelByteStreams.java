package io.exercises;

import io.ExerciseChecker;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * EXERCICE 6 - Streams d'octets BAS NIVEAU : FileOutputStream/FileInputStream (niveau : moyen)
 * =============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un "I/O stream" (au sens du chapitre) n'est PAS un Stream de l'API
 * Streams (chapitre 10) - c'est un tuyau qui laisse passer des
 * donnees, OCTET PAR OCTET (byte) ou CARACTERE PAR CARACTERE (char),
 * dans UN SEUL sens (entree OU sortie, jamais les deux a la fois).
 *
 * FileOutputStream/FileInputStream sont BAS NIVEAU : ils parlent
 * DIRECTEMENT au fichier sur le disque, sans aucun intermediaire - le
 * nom se termine par "Stream" (pas "Reader"/"Writer") car ce sont des
 * streams d'OCTETS BRUTS, pas de texte.
 *
 *
 * ==================================================================
 * TODO 1 : writeBytes(file, data)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. try (FileOutputStream out = new FileOutputStream(file.toFile())) {
 *          out.write(data);
 *      }
 *
 *
 * ==================================================================
 * TODO 2 : readAllBytesLowLevel(file)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. try (FileInputStream in = new FileInputStream(file.toFile())) {
 *          renvoyer in.readAllBytes();
 *      }
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans un seul try-with-resources.
 *
 * Exemple a verifier : writeBytes() avec {1,2,3,4,5}, puis
 * readAllBytesLowLevel() sur le MEME fichier renvoie EXACTEMENT le
 * meme tableau d'octets.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - new FileOutputStream(file) EFFACE le contenu existant du
 *     fichier AVANT d'ecrire (sauf si on utilise le constructeur avec
 *     'append=true', pas necessaire ici).
 *   - in.readAllBytes() (depuis Java 9) lit TOUT le flux d'un coup,
 *     jusqu'a la toute fin du fichier - pratique pour de PETITS
 *     fichiers, a eviter sur d'enormes fichiers (tout part en
 *     memoire).
 */
public class Exercise06_LowLevelByteStreams {

    public static void writeBytes(Path file, byte[] data) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer writeBytes()");
    }

    public static byte[] readAllBytesLowLevel(Path file) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer readAllBytesLowLevel()");
    }

    public static void main(String[] args) throws IOException {
        Path tempDir = Files.createTempDirectory("io-ex06-");
        try {
            Path file = tempDir.resolve("data.bin");
            byte[] original = {1, 2, 3, 4, 5};

            writeBytes(file, original);
            byte[] readBack = readAllBytesLowLevel(file);

            ExerciseChecker.check("les octets relus sont EXACTEMENT ceux ecrits",
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
