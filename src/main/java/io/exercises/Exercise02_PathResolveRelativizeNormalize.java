package io.exercises;

import io.ExerciseChecker;

import java.nio.file.Path;

/**
 * EXERCICE 2 - Combiner et resoudre des Path : resolve, relativize, normalize (niveau : difficile)
 * ================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Path est IMMUABLE : aucune de ses methodes ne le modifie jamais,
 * elles renvoient TOUTES un NOUVEAU Path.
 *
 *   - resolve(autre) : "a partir d'ICI, va ENSUITE la" - colle 'autre'
 *     A LA SUITE du chemin actuel (comme suivre un panneau
 *     "continuez tout droit, puis...").
 *   - relativize(autre) : la question INVERSE - "pour aller d'ICI
 *     jusqu'a LA, quel chemin RELATIF dois-je suivre ?"
 *   - normalize() : nettoie un chemin qui contient des ".." (remonter
 *     d'un cran) ou des "." (rester sur place), en les appliquant
 *     VRAIMENT, sans jamais toucher au disque.
 *
 *
 * ==================================================================
 * TODO 1 : combine(base, other)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * base = "a/b", other = "c/d.txt". combine(base, other) -> "a/b/c/d.txt"
 * (other est colle A LA SUITE de base).
 *
 * -- Le plan --
 *
 *   1. Renvoyer base.resolve(other).
 *
 *
 * ==================================================================
 * TODO 2 : relativeFromTo(from, to)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * from = "a/b", to = "a/b/c/d.txt". relativeFromTo(from, to) ->
 * "c/d.txt" (le chemin RELATIF pour aller de 'from' jusqu'a 'to').
 *
 * -- Le plan --
 *
 *   1. Renvoyer from.relativize(to).
 *
 *
 * ==================================================================
 * TODO 3 : normalizePath(path)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * path = "a/b/../c/./d.txt". normalizePath(path) -> "a/c/d.txt" (le
 * ".." efface le "b" juste avant lui, le "." disparait simplement).
 *
 * -- Le plan --
 *
 *   1. Renvoyer path.normalize().
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule ligne, c'est le CHOIX de la bonne
 * methode (et la comprehension de sa direction : "coller a la suite"
 * vs "chemin pour y aller" vs "nettoyer") qui est le vrai coeur de cet
 * exercice.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Aucune de ces 3 methodes ne verifie si les fichiers/dossiers
 *     EXISTENT vraiment - tout se passe en manipulation de TEXTE de
 *     chemin, jamais sur le disque.
 *   - relativize() suppose que 'from' et 'to' sont TOUS LES DEUX
 *     relatifs, ou TOUS LES DEUX absolus - melanger les 2 lance une
 *     IllegalArgumentException.
 */
public class Exercise02_PathResolveRelativizeNormalize {

    public static Path combine(Path base, Path other) {
        throw new UnsupportedOperationException("TODO 1 : implementer combine()");
    }

    public static Path relativeFromTo(Path from, Path to) {
        throw new UnsupportedOperationException("TODO 2 : implementer relativeFromTo()");
    }

    public static Path normalizePath(Path path) {
        throw new UnsupportedOperationException("TODO 3 : implementer normalizePath()");
    }

    public static void main(String[] args) {
        Path base = Path.of("a", "b");
        Path other = Path.of("c", "d.txt");
        ExerciseChecker.check("combine('a/b', 'c/d.txt') == 'a/b/c/d.txt'",
                combine(base, other).equals(Path.of("a", "b", "c", "d.txt")));

        Path from = Path.of("a", "b");
        Path to = Path.of("a", "b", "c", "d.txt");
        ExerciseChecker.check("relativeFromTo('a/b', 'a/b/c/d.txt') == 'c/d.txt'",
                relativeFromTo(from, to).equals(Path.of("c", "d.txt")));

        Path messy = Path.of("a", "b", "..", "c", ".", "d.txt");
        ExerciseChecker.check("normalizePath('a/b/../c/./d.txt') == 'a/c/d.txt'",
                normalizePath(messy).equals(Path.of("a", "c", "d.txt")));

        ExerciseChecker.summary();
    }
}
