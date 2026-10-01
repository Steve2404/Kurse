package ch14_io.drills.exercises;

import ch14_io.ExerciseChecker;

import java.nio.file.Path;

import static ch14_io.drills.Workspace.slash;

/**
 * DRILL 01 - L'API de Path (sans toucher au disque)
 * =================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en UNE ligne et vise
 * UNE methode precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Les chemins de ce drill sont RELATIFS et ne touchent jamais le disque.
 * main() compare avec des "/" (Workspace.slash) : ca marche aussi sous Windows.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : build()            [Path.of(premier, autres...)] "docs", "notes", "todo.txt" -> docs/notes/todo.txt.
 * TODO 2  : fileName(p)        [getFileName] docs/notes/todo.txt -> todo.txt.
 * TODO 3  : parent(p)          [getParent] docs/notes/todo.txt -> docs/notes ; un seul nom -> null.
 * TODO 4  : count(p)           [getNameCount] docs/notes/todo.txt -> 3.
 * TODO 5  : second(p)          [getName(1)] docs/notes/todo.txt -> notes.
 * TODO 6  : firstTwo(p)        [subpath(0, 2)] docs/notes/todo.txt -> docs/notes.
 * TODO 7  : sibling(p)         [resolveSibling] docs/readme.txt -> docs/index.txt.
 * TODO 8  : intoOld()          [resolve] logs + old/app-1.log -> logs/old/app-1.log.
 * TODO 9  : fromNotesToLog()   [relativize] de docs/notes vers logs/app.log -> ../../logs/app.log.
 * TODO 10 : clean()            [normalize] docs/./notes/../readme.txt -> docs/readme.txt.
 * TODO 11 : absoluteOrNot()    [isAbsolute + toAbsolutePath] Path.of("x") : "avant apres" -> "false true".
 * TODO 12 : ends()             [endsWith] docs/notes/todo.txt finit par "notes/todo.txt" ? par "todo" ? -> "true false".
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Path.of("a", "b") == Paths.get("a", "b") ; file.toPath() / path.toFile()
 *   getFileName() getParent() (null s'il n'y en a pas) getRoot() getNameCount() getName(i) subpath(debut, finExclue)
 *   resolve(autre) : colle (autre absolu -> autre gagne) ; resolveSibling(nom) : remplace le dernier nom
 *   relativize(autre) : le chemin de this vers autre (meme type : relatifs ou absolus)
 *   normalize() : enleve . et .. (sans le disque) ; toRealPath() : verifie sur le disque, suit les liens
 *   isAbsolute() toAbsolutePath() ; startsWith / endsWith comparent des NOMS entiers
 *   Path est immuable : chaque methode rend un NOUVEAU Path
 * ---------------------------------------------------------------------
 */
public class Drill01_PathApi {

    public static Path build() {
        throw new UnsupportedOperationException("TODO 1 : implementer build()");
    }

    public static Path fileName(Path p) {
        throw new UnsupportedOperationException("TODO 2 : implementer fileName()");
    }

    public static Path parent(Path p) {
        throw new UnsupportedOperationException("TODO 3 : implementer parent()");
    }

    public static int count(Path p) {
        throw new UnsupportedOperationException("TODO 4 : implementer count()");
    }

    public static Path second(Path p) {
        throw new UnsupportedOperationException("TODO 5 : implementer second()");
    }

    public static Path firstTwo(Path p) {
        throw new UnsupportedOperationException("TODO 6 : implementer firstTwo()");
    }

    public static Path sibling(Path p) {
        throw new UnsupportedOperationException("TODO 7 : implementer sibling()");
    }

    public static Path intoOld() {
        throw new UnsupportedOperationException("TODO 8 : implementer intoOld()");
    }

    public static Path fromNotesToLog() {
        throw new UnsupportedOperationException("TODO 9 : implementer fromNotesToLog()");
    }

    public static Path clean() {
        throw new UnsupportedOperationException("TODO 10 : implementer clean()");
    }

    public static String absoluteOrNot() {
        throw new UnsupportedOperationException("TODO 11 : implementer absoluteOrNot()");
    }

    public static String ends() {
        throw new UnsupportedOperationException("TODO 12 : implementer ends()");
    }

    public static void main(String[] args) {
        Path todo = Path.of("docs/notes/todo.txt");
        ExerciseChecker.check("1  build == docs/notes/todo.txt", "docs/notes/todo.txt".equals(slash(build())));
        ExerciseChecker.check("2  fileName == todo.txt", "todo.txt".equals(slash(fileName(todo))));
        ExerciseChecker.check("3  parent == docs/notes ; parent(a) == null", "docs/notes".equals(slash(parent(todo))) && parent(Path.of("a")) == null);
        ExerciseChecker.check("4  count == 3", count(todo) == 3);
        ExerciseChecker.check("5  second == notes", "notes".equals(slash(second(todo))));
        ExerciseChecker.check("6  firstTwo == docs/notes", "docs/notes".equals(slash(firstTwo(todo))));
        ExerciseChecker.check("7  sibling == docs/index.txt", "docs/index.txt".equals(slash(sibling(Path.of("docs/readme.txt")))));
        ExerciseChecker.check("8  intoOld == logs/old/app-1.log", "logs/old/app-1.log".equals(slash(intoOld())));
        ExerciseChecker.check("9  fromNotesToLog == ../../logs/app.log", "../../logs/app.log".equals(slash(fromNotesToLog())));
        ExerciseChecker.check("10 clean == docs/readme.txt", "docs/readme.txt".equals(slash(clean())));
        ExerciseChecker.check("11 absoluteOrNot == false true", "false true".equals(absoluteOrNot()));
        ExerciseChecker.check("12 ends == true false", "true false".equals(ends()));

        ExerciseChecker.summary();
    }
}
