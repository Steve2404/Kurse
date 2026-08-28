package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * EXERCICE 12 - Capstone : copier une arborescence complete (niveau : capstone, style entretien)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Ce capstone recycle presque TOUT ce chapitre en une seule tache
 * realiste : copier un dossier ENTIER (avec tous ses sous-dossiers et
 * fichiers) vers un nouvel emplacement, en recreant EXACTEMENT la
 * meme structure.
 *
 *   - Files.walk(source) (Exercise04) visite CHAQUE entree de
 *     l'arborescence, dossier de depart INCLUS, TOUJOURS un parent
 *     AVANT ses propres enfants (important : ca garantit qu'on peut
 *     creer un sous-dossier de destination AVANT d'y copier un
 *     fichier qu'il doit contenir).
 *   - source.relativize(entree) (Exercise02) calcule "le morceau de
 *     chemin a rajouter" pour chaque entree, RELATIF au dossier de
 *     depart - exactement ce qu'il faut recoller sur le dossier de
 *     destination.
 *   - target.resolve(relatif) (Exercise02) recolle ce morceau sur le
 *     dossier de destination, pour obtenir l'adresse EXACTE ou copier
 *     chaque chose.
 *   - Files.createDirectories/Files.copy (Exercise03) font le travail
 *     final, selon que l'entree du moment est un dossier ou un
 *     fichier.
 *
 *
 * ==================================================================
 * TODO : copyDirectoryTree(source, target)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. try (Stream<Path> stream = Files.walk(source)) { recuperer
 *      TOUTES les entrees dans une List<Path> (les collecter DANS le
 *      try, avant que la ressource ne soit refermee). }
 *   2. Pour CHAQUE entree de cette liste, DANS L'ORDRE :
 *      a. calculer son chemin relatif par rapport a 'source' ;
 *      b. calculer le chemin de destination correspondant (coller ce
 *         relatif sur 'target') ;
 *      c. si l'entree est un dossier (Files.isDirectory()) :
 *         Files.createDirectories(destination) (cree aussi les
 *         parents manquants d'un coup, y compris 'target' lui-meme
 *         s'il n'existe pas encore) ;
 *      d. sinon (un fichier) : Files.copy(entree, destination).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit, chaque etape reutilise un outil deja
 * vu dans ce chapitre.
 *
 * Exemple a verifier : source/a.txt, source/sub/b.txt,
 * source/sub/sub2/c.txt (3 fichiers, 2 dossiers, + source lui-meme =
 * 6 entrees). Apres copyDirectoryTree(source, target), 'target'
 * contient EXACTEMENT la meme arborescence (meme nombre d'entrees,
 * memes noms relatifs, meme contenu pour chaque fichier) - meme si
 * 'target' n'existait pas du tout au depart.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Collectors.toList() a l'interieur du try-with-resources permet
 *     de "vider" le Stream dans une vraie List AVANT de refermer la
 *     ressource - indispensable ici puisqu'on reutilise ensuite le
 *     resultat DANS une boucle for classique, apres coup.
 *   - Files.createDirectories() (avec un 's', pluriel) cree TOUS les
 *     dossiers manquants d'un coup sur le chemin, contrairement a
 *     Files.createDirectory() (Exercise03) qui exige que le PARENT
 *     existe deja.
 */
public class Exercise12_DirectoryTreeCopyCapstone {

    public static void copyDirectoryTree(Path source, Path target) throws IOException {
        throw new UnsupportedOperationException("TODO : implementer copyDirectoryTree()");
    }

    public static void main(String[] args) throws IOException {
        Path source = Files.createTempDirectory("io-ex12-src-");
        Path target = Files.createTempDirectory("io-ex12-dst-");
        Files.delete(target);

        try {
            Files.writeString(source.resolve("a.txt"), "A", StandardCharsets.UTF_8);
            Path sub = source.resolve("sub");
            Files.createDirectory(sub);
            Files.writeString(sub.resolve("b.txt"), "B", StandardCharsets.UTF_8);
            Path sub2 = sub.resolve("sub2");
            Files.createDirectory(sub2);
            Files.writeString(sub2.resolve("c.txt"), "C", StandardCharsets.UTF_8);

            copyDirectoryTree(source, target);

            ExerciseChecker.check("a.txt copie avec le bon contenu",
                    Files.readString(target.resolve("a.txt")).equals("A"));
            ExerciseChecker.check("sub/b.txt copie avec le bon contenu",
                    Files.readString(target.resolve("sub").resolve("b.txt")).equals("B"));
            ExerciseChecker.check("sub/sub2/c.txt copie avec le bon contenu",
                    Files.readString(target.resolve("sub").resolve("sub2").resolve("c.txt")).equals("C"));

            long sourceCount;
            long targetCount;
            try (Stream<Path> s = Files.walk(source)) {
                sourceCount = s.count();
            }
            try (Stream<Path> s = Files.walk(target)) {
                targetCount = s.count();
            }
            ExerciseChecker.check("la copie a EXACTEMENT le meme nombre d'entrees que l'original",
                    sourceCount == targetCount);
        } finally {
            for (Path root : List.of(source, target)) {
                if (Files.exists(root)) {
                    try (Stream<Path> cleanup = Files.walk(root)) {
                        cleanup.sorted(Comparator.reverseOrder()).forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException ignored) {
                            }
                        });
                    }
                }
            }
        }

        ExerciseChecker.summary();
    }
}
