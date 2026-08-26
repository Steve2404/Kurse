package io.exercises;

import io.ExerciseChecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * EXERCICE 4 - Explorer un dossier avec l'API Stream de NIO.2 : list, walk, find (niveau : difficile)
 * ===================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un dossier avec des sous-dossiers a l'interieur (une arborescence).
 *
 *   - Files.list(dossier) : ne regarde QUE le NIVEAU DIRECT du
 *     dossier (comme ouvrir un tiroir et regarder ce qu'il y a
 *     dedans, SANS ouvrir les boites qui sont a l'interieur).
 *   - Files.walk(dossier) : descend dans TOUS les sous-dossiers,
 *     recursivement, et renvoie TOUT (le dossier de depart INCLUS)
 *     - comme visiter chaque piece de la maison, y compris les
 *     placards des placards.
 *   - Files.find(dossier, profondeurMax, filtre) : pareil que walk(),
 *     mais ne garde QUE ce qui correspond a un filtre donne (un
 *     BiPredicate<Path, BasicFileAttributes>).
 *
 * IMPORTANT : ces 3 methodes renvoient un Stream<Path> qui est
 * BRANCHE SUR UNE VRAIE RESSOURCE du systeme de fichiers (comme un
 * descripteur de fichier ouvert) - il FAUT toujours les utiliser dans
 * un try-with-resources, sinon la ressource fuit.
 *
 *
 * ==================================================================
 * TODO 1 : listImmediateChildren(dir)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. try (Stream<Path> s = Files.list(dir)) { renvoyer
 *      s.collect(Collectors.toList()) DANS le try (le stream doit
 *      etre entierement consomme AVANT que le try-with-resources ne
 *      referme la ressource). }
 *
 *
 * ==================================================================
 * TODO 2 : countAllEntries(root)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. try (Stream<Path> s = Files.walk(root)) { renvoyer s.count(). }
 *
 *
 * ==================================================================
 * TODO 3 : findTextFiles(root)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. try (Stream<Path> s = Files.find(root, Integer.MAX_VALUE,
 *      (path, attrs) -> attrs.isRegularFile() &&
 *      path.toString().endsWith(".txt"))) { renvoyer
 *      s.collect(Collectors.toList()). }
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient dans un seul try-with-resources.
 *
 * Exemple a verifier : sur une arborescence root/a.txt,
 * root/sub1/b.txt, root/sub1/sub2/c.txt : listImmediateChildren(root)
 * trouve 2 entrees (a.txt et sub1, PAS b.txt ni c.txt, trop
 * profonds). countAllEntries(root) trouve 6 entrees (root LUI-MEME +
 * a.txt + sub1 + b.txt + sub2 + c.txt). findTextFiles(root) trouve
 * EXACTEMENT les 3 fichiers .txt, ni plus ni moins (jamais les
 * dossiers).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - L'ORDRE renvoye par list()/walk()/find() n'est PAS garanti par
 *     le contrat de l'API (ca depend du systeme de fichiers reel) -
 *     c'est pour ca que le test compare des ENSEMBLES TRIES, jamais
 *     un ordre brut.
 */
public class Exercise04_NioStreamListWalkFind {

    public static List<Path> listImmediateChildren(Path dir) throws IOException {
        throw new UnsupportedOperationException("TODO 1 : implementer listImmediateChildren()");
    }

    public static long countAllEntries(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 2 : implementer countAllEntries()");
    }

    public static List<Path> findTextFiles(Path root) throws IOException {
        throw new UnsupportedOperationException("TODO 3 : implementer findTextFiles()");
    }

    public static void main(String[] args) throws IOException {
        Path root = Files.createTempDirectory("io-ex04-");
        try {
            Path aTxt = root.resolve("a.txt");
            Files.createFile(aTxt);
            Path sub1 = root.resolve("sub1");
            Files.createDirectory(sub1);
            Path bTxt = sub1.resolve("b.txt");
            Files.createFile(bTxt);
            Path sub2 = sub1.resolve("sub2");
            Files.createDirectory(sub2);
            Path cTxt = sub2.resolve("c.txt");
            Files.createFile(cTxt);

            List<Path> children = listImmediateChildren(root).stream().sorted().collect(Collectors.toList());
            ExerciseChecker.check("listImmediateChildren() trouve exactement 2 entrees (a.txt, sub1)",
                    children.equals(List.of(aTxt, sub1).stream().sorted().collect(Collectors.toList())));

            ExerciseChecker.check("countAllEntries() trouve les 6 entrees (root inclus)",
                    countAllEntries(root) == 6);

            List<Path> textFiles = findTextFiles(root).stream().sorted().collect(Collectors.toList());
            List<Path> expectedTextFiles = List.of(aTxt, bTxt, cTxt).stream().sorted().collect(Collectors.toList());
            ExerciseChecker.check("findTextFiles() trouve EXACTEMENT les 3 fichiers .txt",
                    textFiles.equals(expectedTextFiles));
        } finally {
            try (Stream<Path> cleanup = Files.walk(root)) {
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
