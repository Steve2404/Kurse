package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * EXERCICE 3 - Le couteau suisse Files : creer, copier, deplacer, supprimer (niveau : moyen/difficile)
 * ====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * La classe Files est un "couteau suisse" : plein de petites methodes
 * statiques, chacune faisant EXACTEMENT ce que son nom dit
 * (createFile, copy, move, delete...). Point commun important : la
 * PLUPART lancent une IOException CHECKED (le systeme de fichiers reel
 * peut echouer pour mille raisons - disque plein, permissions,
 * fichier deja verrouille par un autre programme...), donc PRESQUE
 * TOUJOURS a declarer avec "throws IOException" ou a attraper.
 *
 *
 * ==================================================================
 * TODO 1 : createEmptyFile(path)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Files.createFile(path) (lance IOException si le fichier existe
 *      DEJA, ou si le dossier parent n'existe pas).
 *
 *
 * ==================================================================
 * TODO 2 : copyFile(source, target)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Files.copy(source, target) (copie le CONTENU ; lance
 *      IOException si 'target' existe deja, sauf option
 *      REPLACE_EXISTING - pas necessaire ici).
 *
 *
 * ==================================================================
 * TODO 3 : moveFile(source, target)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Files.move(source, target) (contrairement a copy(), 'source'
 *      DISPARAIT apres un move() reussi - c'est un vrai deplacement,
 *      pas une duplication).
 *
 *
 * ==================================================================
 * TODO 4 : deleteIfPresent(path)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Files.delete(path) lance une exception si le fichier N'EXISTE DEJA
 * PLUS - genant si on veut juste "nettoyer, si jamais il existe encore
 * ce fichier, sinon tant pis". Files.deleteIfExists(path) est la
 * version "tolerante" : elle supprime SI le fichier existe, et
 * renvoie simplement false SANS lancer d'exception s'il n'existait
 * deja plus.
 *
 * -- Le plan --
 *
 *   1. Renvoyer Files.deleteIfExists(path).
 *
 * -- Ces 4 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne, c'est le CHOIX de la bonne methode
 * Files qui est le vrai coeur de l'exercice.
 *
 * Exemple a verifier : creer un fichier vide, ecrire dedans, le
 * copier (le contenu suit), deplacer la copie (l'original du
 * deplacement disparait), puis supprimer - une premiere fois avec
 * succes (true), une deuxieme fois sans effet (false, deja absent).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Files.createFile/copy/move/delete lancent TOUS IOException
 *     (checked) - sauf deleteIfExists, qui la lance UNIQUEMENT pour
 *     de vraies erreurs (pas pour "fichier deja absent").
 */
public class Exercise03_FilesHelperBasics {

    public static void createEmptyFile(Path path) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer createEmptyFile()");
    }

    public static void copyFile(Path source, Path target) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer copyFile()");
    }

    public static void moveFile(Path source, Path target) throws IOException {
        throw new UnsupportedOperationException("TODO 3 : implementer moveFile()");
    }

    public static boolean deleteIfPresent(Path path) throws IOException {
        throw new UnsupportedOperationException("TODO 4 : implementer deleteIfPresent()");
    }

    public static void main(String[] args) throws IOException {
        Path tempDir = Files.createTempDirectory("io-ex03-");
        try {
            Path fileA = tempDir.resolve("a.txt");
            createEmptyFile(fileA);
            ExerciseChecker.check("createEmptyFile() a bien cree le fichier", Files.exists(fileA));

            Files.writeString(fileA, "contenu original", StandardCharsets.UTF_8);

            Path fileB = tempDir.resolve("b.txt");
            copyFile(fileA, fileB);
            ExerciseChecker.check("copyFile() a cree la copie avec le MEME contenu",
                    Files.readString(fileB, StandardCharsets.UTF_8).equals("contenu original"));

            Path fileC = tempDir.resolve("c.txt");
            moveFile(fileB, fileC);
            ExerciseChecker.check("moveFile() : la source a disparu", !Files.exists(fileB));
            ExerciseChecker.check("moveFile() : la destination existe avec le contenu",
                    Files.readString(fileC, StandardCharsets.UTF_8).equals("contenu original"));

            ExerciseChecker.check("deleteIfPresent() sur un fichier existant renvoie true et le supprime",
                    deleteIfPresent(fileC) && !Files.exists(fileC));
            ExerciseChecker.check("deleteIfPresent() sur un fichier deja absent renvoie false",
                    !deleteIfPresent(fileC));

            Files.deleteIfExists(fileA);
        } finally {
            Files.deleteIfExists(tempDir);
        }

        ExerciseChecker.summary();
    }
}
