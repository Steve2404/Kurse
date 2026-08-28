package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.io.File;
import java.nio.file.Path;

/**
 * EXERCICE 1 - File (I/O) et Path (NIO.2) : creation et conversion (niveau : moyen)
 * ================================================================================================
 *
 * -- Rappel du decoupage en "boites magiques" --
 *
 * Une methode, c'est une boite magique : tu la nourris d'ingredients
 * (parametres), et elle rend un resultat, sans que tu aies besoin de
 * savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
 * demande-toi : est-ce qu'elle se raconte seule ? revient-elle
 * plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
 * moins une question, elle merite sa propre boite.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * File (l'ancienne API, depuis Java 1.0) et Path (la nouvelle API
 * NIO.2, depuis Java 7) representent TOUS LES DEUX "l'adresse" d'un
 * fichier ou dossier - un peu comme une adresse postale ECRITE sur une
 * enveloppe : ca ne garantit PAS que la maison existe vraiment, c'est
 * juste le TEXTE de l'adresse. Path.of(...) est la "boite" moderne
 * pour fabriquer cette adresse ; toFile()/toPath() permettent de
 * passer d'une representation a l'autre, car du VIEUX code peut encore
 * attendre un File alors que ton code moderne manipule des Path.
 *
 *
 * ==================================================================
 * TODO 1 : buildPath(first, more)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer Path.of(first, more) (more est un varargs : 0, 1 ou
 *      plusieurs segments supplementaires).
 *
 *
 * ==================================================================
 * TODO 2 : toFile(path)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer path.toFile().
 *
 *
 * ==================================================================
 * TODO 3 : toPath(file)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer file.toPath().
 *
 *
 * ==================================================================
 * TODO 4 : fileNameOf(path)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * getFileName() renvoie SEULEMENT le DERNIER morceau du chemin (le nom
 * du fichier ou du dossier final), jamais tout le chemin complet.
 *
 * -- Le plan --
 *
 *   1. Renvoyer path.getFileName().toString().
 *
 * -- Ces 4 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule ligne, ce sont des methodes toutes
 * faites du JDK.
 *
 * Exemple a verifier : buildPath("a", "b", "c.txt") assemble les 3
 * segments en un seul Path. toFile() puis toPath() sur le resultat
 * (un "aller-retour" complet File <-> Path) redonne un Path EGAL a
 * l'original. fileNameOf(...) sur ce meme Path renvoie "c.txt" (juste
 * le dernier morceau).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Path.of(String, String...) accepte un nombre variable de
 *     segments, assembles automatiquement avec le bon separateur pour
 *     le systeme d'exploitation courant (/ sous Linux/macOS, \ sous
 *     Windows) - jamais besoin de l'ecrire soi-meme.
 *   - path.toFile() et file.toPath() ne touchent JAMAIS le disque -
 *     ce sont de simples conversions de representation, meme si le
 *     fichier n'existe pas reellement.
 */
public class Exercise01_FileAndPathBasics {

    public static Path buildPath(String first, String... more) {
        throw new UnsupportedOperationException("TODO 1 : implementer buildPath()");
    }

    public static File toFile(Path path) {
        throw new UnsupportedOperationException("TODO 2 : implementer toFile()");
    }

    public static Path toPath(File file) {
        throw new UnsupportedOperationException("TODO 3 : implementer toPath()");
    }

    public static String fileNameOf(Path path) {
        throw new UnsupportedOperationException("TODO 4 : implementer fileNameOf()");
    }

    public static void main(String[] args) {
        Path path = buildPath("a", "b", "c.txt");
        Path expected = Path.of("a", "b", "c.txt");
        ExerciseChecker.check("buildPath assemble les segments comme Path.of()", path.equals(expected));

        File file = toFile(path);
        ExerciseChecker.check("toFile() garde le meme texte de chemin", file.getPath().equals(path.toString()));

        Path roundTrip = toPath(file);
        ExerciseChecker.check("File.toPath() puis retour == Path d'origine (aller-retour complet)",
                roundTrip.equals(path));

        ExerciseChecker.check("fileNameOf() renvoie SEULEMENT le dernier segment",
                fileNameOf(path).equals("c.txt"));

        ExerciseChecker.summary();
    }
}
