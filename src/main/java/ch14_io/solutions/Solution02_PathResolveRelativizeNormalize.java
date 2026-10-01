package ch14_io.solutions;

import java.nio.file.Path;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch14_io.exercises.Exercise02_PathResolveRelativizeNormalize.
 */
public class Solution02_PathResolveRelativizeNormalize {

    public static Path combine(Path base, Path other) {
        // resolve colle other a base (sauf si other est absolu : il gagne) ; aucune normalisation.
        return base.resolve(other);
    }

    public static Path relativeFromTo(Path from, Path to) {
        // relativize : le chemin pour aller de from a to ; les deux doivent etre du meme type (relatifs ou absolus).
        return from.relativize(to);
    }

    public static Path normalizePath(Path path) {
        // normalize enleve . et .. sans regarder le disque (contrairement a toRealPath).
        return path.normalize();
    }
}
