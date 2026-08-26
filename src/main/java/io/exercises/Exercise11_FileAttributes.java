package io.exercises;

import io.ExerciseChecker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * EXERCICE 11 - Lire et modifier les attributs d'un fichier avec NIO.2 (niveau : difficile)
 * ==========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un fichier a plein de METADONNEES (des informations SUR lui, pas
 * SON contenu) : sa taille, s'il est un dossier ou pas, ses dates de
 * creation/modification... Files.readAttributes(path,
 * BasicFileAttributes.class) recupere TOUTES ces infos en UN SEUL
 * appel (plutot que de faire un aller-retour separe vers le systeme
 * d'exploitation pour chaque petite question).
 *
 * Pour LIRE, un TYPE d'attributs (BasicFileAttributes.class) suffit.
 * Pour MODIFIER (par exemple, changer la date de derniere
 * modification), il faut passer par une VUE (BasicFileAttributeView),
 * qui elle propose des methodes comme setTimes(...).
 *
 *
 * ==================================================================
 * TODO 1 : readSize(file)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer Files.readAttributes(file, BasicFileAttributes.class).size().
 *
 *
 * ==================================================================
 * TODO 2 : isDirectoryViaAttributes(path)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer Files.readAttributes(path, BasicFileAttributes.class).isDirectory().
 *
 *
 * ==================================================================
 * TODO 3 : updateLastModified(file, newTime)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * setTimes(dateModif, dateAcces, dateCreation) prend 3 arguments : si
 * tu ne veux changer QUE la date de derniere modification, passe null
 * pour les 2 autres (Java les laissera simplement INCHANGEES).
 *
 * -- Le plan --
 *
 *   1. Recuperer la vue : Files.getFileAttributeView(file, BasicFileAttributeView.class).
 *   2. Appeler view.setTimes(newTime, null, null).
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en 1-2 lignes.
 *
 * Exemple a verifier : un fichier contenant "hello" (5 caracteres) a
 * bien une taille de 5 octets. isDirectoryViaAttributes() distingue
 * correctement un fichier d'un dossier. Apres updateLastModified()
 * vers une date precise (1er janvier 2020), relire les attributs du
 * MEME fichier montre bien cette date exacte comme derniere
 * modification.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - FileTime.from(Instant) construit un FileTime a partir d'un
 *     instant precis, independant de tout fuseau horaire.
 *   - BasicFileAttributeView fonctionne sur TOUS les systemes de
 *     fichiers (contrairement a DosFileAttributeView ou
 *     PosixFileAttributeView, specifiques a un OS) - c'est pour ca
 *     qu'on l'utilise ici, pour un exercice qui doit marcher partout.
 */
public class Exercise11_FileAttributes {

    public static long readSize(Path file) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer readSize()");
    }

    public static boolean isDirectoryViaAttributes(Path path) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer isDirectoryViaAttributes()");
    }

    public static void updateLastModified(Path file, FileTime newTime) throws IOException {
        throw new UnsupportedOperationException("TODO 3 : implementer updateLastModified()");
    }

    public static void main(String[] args) throws IOException {
        Path tempDir = Files.createTempDirectory("io-ex11-");
        try {
            Path file = tempDir.resolve("a.txt");
            Files.writeString(file, "hello", StandardCharsets.UTF_8);

            ExerciseChecker.check("readSize('hello') == 5", readSize(file) == 5);
            ExerciseChecker.check("isDirectoryViaAttributes(fichier) == false", !isDirectoryViaAttributes(file));
            ExerciseChecker.check("isDirectoryViaAttributes(dossier) == true", isDirectoryViaAttributes(tempDir));

            FileTime newTime = FileTime.from(Instant.parse("2020-01-01T00:00:00Z"));
            updateLastModified(file, newTime);
            FileTime readBack = Files.readAttributes(file, BasicFileAttributes.class).lastModifiedTime();
            ExerciseChecker.check("la date de derniere modification a bien ete mise a jour",
                    readBack.equals(newTime));
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
