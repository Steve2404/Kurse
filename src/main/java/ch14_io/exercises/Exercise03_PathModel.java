package ch14_io.exercises;

import ch14_io.ExerciseChecker;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 3 - Refaire Path a la main : noms, normalize, resolve, relativize, subpath, startsWith (niveau : difficile)
 * ====================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_FileAndPathBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Path n'est qu'une LISTE DE NOMS (les morceaux entre les "/") : il
 * ne regarde JAMAIS le disque pour ces operations. Tu vas reecrire ces
 * operations sur des chemins RELATIFS ecrits avec "/" ; main() les
 * compare au vrai java.nio.file.Path (les "\" de Windows sont convertis
 * en "/").
 *
 * Les pieges verifies en direct :
 *   - Path.of("") a UN nom : le nom vide (getNameCount() == 1) ;
 *   - resolve NE normalise PAS : "a/b".resolve("../c") -> "a/b/../c" ;
 *   - normalize garde les ".." qu'on ne peut pas remonter : "a/../../b" -> "../b" ;
 *   - startsWith compare des NOMS entiers : "abc/d" ne commence PAS par "ab" ;
 *   - subpath(debut, fin) : fin EXCLUE, et une plage vide ou trop longue -> IllegalArgumentException.
 *
 *
 * ==================================================================
 * TODO 1 : names(path)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "a/b/c" -> [a, b, c] ; "../x" -> [.., x] ; "" -> [""] (un nom vide)
 *
 * -- Le plan --
 *
 *   1. Couper sur "/" (split garde bien [""] pour "").
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non, mais names est la boite magique de tous les autres TODO.
 *
 *
 * ==================================================================
 * TODO 2 : normalize(path)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "a/./b/../c" -> "a/c" ; "a/../../b" -> "../b" ; "a/b/../.." -> "" ; "." -> ""
 *
 * -- Le plan --
 *
 *   1. Une pile de noms. Pour chaque nom : "." -> rien ; ".." -> depiler si le sommet est
 *      un vrai nom (pas ".."), sinon empiler ".." ; vide -> rien ; sinon empiler.
 *   2. Recoller avec "/".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : names.
 *
 *
 * ==================================================================
 * TODO 3 : resolve(base, other)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ("a", "b/c") -> "a/b/c" ; ("", "x") -> "x" ; ("a/b", "") -> "a/b" ; ("a/b", "../c") -> "a/b/../c"
 *
 * -- Le plan --
 *
 *   1. other vide -> base ; base vide -> other ; sinon base + "/" + other (SANS normaliser).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : relativize(from, to)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le chemin qui mene de from a to (deux chemins relatifs deja normalises).
 *
 * -- Essayons a la main --
 *
 *   ("a/b", "a/c") -> "../c" ; ("a", "a/b/c") -> "b/c" ; ("a/b/c", "a") -> "../.." ; ("a/b", "a/b") -> ""
 *
 * -- Le plan --
 *
 *   1. Compter les noms communs au debut (k). Un chemin vide n'a aucun nom utile.
 *   2. Un ".." pour chaque nom de from apres k, puis les noms de to apres k.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : names (en ignorant le nom vide de "").
 *
 *
 * ==================================================================
 * TODO 5 : subpath(path, begin, end)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ("a/b/c/d", 1, 3) -> "b/c" ; ("a/b", 0, 3) -> "IllegalArgumentException" ; ("a/b", 1, 1) -> "IllegalArgumentException"
 *
 * -- Le plan --
 *
 *   1. begin < 0, end > nombre de noms, ou begin >= end -> "IllegalArgumentException".
 *   2. Sinon les noms [begin, end[ recolles.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : names.
 *
 *
 * ==================================================================
 * TODO 6 : startsWith(path, prefix)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ("a/b/c", "a/b") -> true ; ("abc/d", "ab") -> false ; ("a/b", "a/b/c") -> false
 *
 * -- Le plan --
 *
 *   1. prefix a plus de noms -> false.
 *   2. Comparer les noms un par un.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : names.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - List.of(path.split("/", -1)) ; String.join("/", liste)
 *   - Deque<String> pile = new ArrayDeque<>() ; pile.peekLast() ; pile.pollLast()
 */
public class Exercise03_PathModel {

    public static List<String> names(String path) {
        throw new UnsupportedOperationException("TODO 1 : implementer names()");
    }

    public static String normalize(String path) {
        throw new UnsupportedOperationException("TODO 2 : implementer normalize()");
    }

    public static String resolve(String base, String other) {
        throw new UnsupportedOperationException("TODO 3 : implementer resolve()");
    }

    public static String relativize(String from, String to) {
        throw new UnsupportedOperationException("TODO 4 : implementer relativize()");
    }

    public static String subpath(String path, int begin, int end) {
        throw new UnsupportedOperationException("TODO 5 : implementer subpath()");
    }

    public static boolean startsWith(String path, String prefix) {
        throw new UnsupportedOperationException("TODO 6 : implementer startsWith()");
    }

    public static void main(String[] args) {
        List<String> samples = List.of("a/b/c", "a/./b/../c", "../x/y", "a/../../b", ".", "a/b/../../..", "docs/./readme.txt", "");
        int namesOk = 0;
        int normOk = 0;
        String miss = "";
        for (String s : samples) {
            Path p = Path.of(s);
            List<String> real = new ArrayList<>();
            for (Path name : p) {
                real.add(name.toString());
            }
            if (s.isEmpty()) {
                real = List.of("");
            }
            if (names(s).equals(real) && names(s).size() == p.getNameCount()) {
                namesOk++;
            }
            String expected = slash(p.normalize());
            if (normalize(s).equals(expected)) {
                normOk++;
            } else if (miss.isEmpty()) {
                miss = " ; 1er ecart : normalize(\"" + s + "\") = " + normalize(s) + " au lieu de " + expected;
            }
        }
        ExerciseChecker.check("names == Path (getName / getNameCount) sur 8 chemins (" + namesOk + " d'accord)", namesOk == 8);
        ExerciseChecker.check("normalize == Path.normalize sur 8 chemins (" + normOk + " d'accord)" + miss, normOk == 8);

        String[][] resolves = {{"a", "b/c"}, {"", "x"}, {"a/b", ""}, {"a/b", "../c"}};
        int resOk = 0;
        for (String[] r : resolves) {
            if (resolve(r[0], r[1]).equals(slash(Path.of(r[0]).resolve(r[1])))) {
                resOk++;
            }
        }
        ExerciseChecker.check("resolve == Path.resolve sur 4 cas (" + resOk + " d'accord)", resOk == 4);

        String[][] relatives = {{"a/b", "a/c"}, {"a", "a/b/c"}, {"a/b/c", "a"}, {"x", "y"}, {"a/b", "a/b"}, {"", "a/b"}};
        int relOk = 0;
        for (String[] r : relatives) {
            if (relativize(r[0], r[1]).equals(slash(Path.of(r[0]).relativize(Path.of(r[1]))))) {
                relOk++;
            }
        }
        ExerciseChecker.check("relativize == Path.relativize sur 6 cas (" + relOk + " d'accord)", relOk == 6);

        Object[][] subs = {{"a/b/c/d", 1, 3}, {"a/b", 0, 3}, {"a/b", 1, 1}, {"a/b/c", 0, 1}, {"a/b/c", 2, 3}};
        int subOk = 0;
        for (Object[] r : subs) {
            String real;
            try {
                real = slash(Path.of((String) r[0]).subpath((Integer) r[1], (Integer) r[2]));
            } catch (IllegalArgumentException e) {
                real = "IllegalArgumentException";
            }
            if (subpath((String) r[0], (Integer) r[1], (Integer) r[2]).equals(real)) {
                subOk++;
            }
        }
        ExerciseChecker.check("subpath == Path.subpath sur 5 cas (" + subOk + " d'accord)", subOk == 5);

        String[][] starts = {{"a/b/c", "a/b"}, {"abc/d", "ab"}, {"a/b", "a/b/c"}, {"a/b", "a/b"}, {"a/b", "b"}};
        int stOk = 0;
        for (String[] r : starts) {
            if (startsWith(r[0], r[1]) == Path.of(r[0]).startsWith(Path.of(r[1]))) {
                stOk++;
            }
        }
        ExerciseChecker.check("startsWith == Path.startsWith sur 5 cas (" + stOk + " d'accord)", stOk == 5);

        ExerciseChecker.summary();
    }

    // Le chemin reel, ecrit avec des "/" quel que soit le systeme.
    static String slash(Path p) {
        return p.toString().replace('\\', '/');
    }
}
