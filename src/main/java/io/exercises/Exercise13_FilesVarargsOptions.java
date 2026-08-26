package io.exercises;

import io.ExerciseChecker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * EXERCICE 13 - Les options varargs de Files : StandardCopyOption et StandardOpenOption (niveau : difficile)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Beaucoup de methodes de Files acceptent, EN PLUS de leurs
 * parametres habituels, une LISTE D'OPTIONS (des valeurs d'enum, en
 * varargs - 0, 1 ou plusieurs, separees par une virgule) qui
 * PRECISENT le comportement exact voulu. Sans option, Files.copy() et
 * Files.write() ont un comportement "prudent" par defaut (refuser
 * d'ecraser quelque chose qui existe deja) - les options permettent
 * d'assouplir ce comportement, EXPLICITEMENT, quand c'est vraiment ce
 * qu'on veut.
 *
 *
 * ==================================================================
 * TODO 1 : copyReplaceExisting(source, target)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Files.copy(source, target) SANS option lance
 * FileAlreadyExistsException si 'target' existe deja - une protection
 * pour eviter d'ecraser un fichier important PAR ACCIDENT.
 * StandardCopyOption.REPLACE_EXISTING dit explicitement "oui, je
 * VEUX ecraser s'il existe deja".
 *
 * -- Le plan --
 *
 *   1. Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING).
 *
 *
 * ==================================================================
 * TODO 2 : appendLine(file, line)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Files.writeString(file, texte) SANS option EFFACE tout le contenu
 * existant avant d'ecrire (comme un FileWriter classique, Exercise08).
 * StandardOpenOption.APPEND dit "ajoute A LA SUITE du contenu deja
 * present, ne l'efface jamais". StandardOpenOption.CREATE dit en plus
 * "fabrique le fichier tout neuf s'il n'existe pas encore" (sinon,
 * APPEND seul echouerait sur un fichier absent).
 *
 * -- Le plan --
 *
 *   1. Files.writeString(file, line + System.lineSeparator(),
 *      StandardOpenOption.CREATE, StandardOpenOption.APPEND).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne, c'est le CHOIX de la bonne
 * combinaison d'options qui est le vrai coeur de l'exercice.
 *
 * Exemple a verifier : copier SANS option vers un fichier qui existe
 * deja lance bien FileAlreadyExistsException. La MEME copie, mais
 * AVEC REPLACE_EXISTING, reussit et remplace le contenu. Deux appels
 * successifs a appendLine() sur le meme fichier ACCUMULENT les 2
 * lignes, sans jamais effacer la premiere.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Ces options sont des CopyOption / OpenOption (des interfaces),
 *     dont StandardCopyOption / StandardOpenOption sont les
 *     implementations les plus courantes (des enums).
 */
public class Exercise13_FilesVarargsOptions {

    public static void copyReplaceExisting(Path source, Path target) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer copyReplaceExisting()");
    }

    public static void appendLine(Path file, String line) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer appendLine()");
    }

    public static void main(String[] args) throws IOException {
        Path tempDir = Files.createTempDirectory("io-ex13-");
        try {
            Path source = tempDir.resolve("source.txt");
            Path target = tempDir.resolve("target.txt");
            Files.writeString(source, "nouveau contenu", StandardCharsets.UTF_8);
            Files.writeString(target, "ancien contenu", StandardCharsets.UTF_8);

            boolean threwWithoutOption = false;
            try {
                Files.copy(source, target);
            } catch (FileAlreadyExistsException e) {
                threwWithoutOption = true;
            }
            ExerciseChecker.check("Files.copy() SANS option refuse d'ecraser un fichier existant",
                    threwWithoutOption);

            copyReplaceExisting(source, target);
            ExerciseChecker.check("copyReplaceExisting() ecrase bien le contenu existant",
                    Files.readString(target, StandardCharsets.UTF_8).equals("nouveau contenu"));

            Path appendFile = tempDir.resolve("append.txt");
            appendLine(appendFile, "premiere ligne");
            appendLine(appendFile, "deuxieme ligne");
            String expected = "premiere ligne" + System.lineSeparator() + "deuxieme ligne" + System.lineSeparator();
            ExerciseChecker.check("appendLine() accumule les lignes SANS jamais effacer les precedentes",
                    Files.readString(appendFile, StandardCharsets.UTF_8).equals(expected));
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
