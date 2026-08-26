package io.solutions;

import java.nio.file.Path;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans io.exercises.Exercise02_PathResolveRelativizeNormalize.
 */
public class Solution02_PathResolveRelativizeNormalize {

    public static Path combine(Path base, Path other) {
        return base.resolve(other);
    }

    public static Path relativeFromTo(Path from, Path to) {
        return from.relativize(to);
    }

    public static Path normalizePath(Path path) {
        return path.normalize();
    }
}
