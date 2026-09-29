package ch3_makingdecisions.exercises;

import ch3_makingdecisions.ExerciseChecker;

/**
 * EXERCICE 17 - CAPSTONE : un robot sur une grille (tout le chapitre 3 en un exercice) (niveau : capstone)
 * =====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_IfElseBasics.java.
 *
 * -- Le contexte --
 *
 * Un robot se deplace sur une carte :
 *
 *   ligne 0 : "S..#"      S = depart, . = libre, # = mur, T = tresor
 *   ligne 1 : ".#.."
 *   ligne 2 : "...T"
 *
 * Positions ecrites "ligne,colonne". Le depart S est en 0,0, le
 * tresor T en 2,3. Le robot recoit une liste de commandes de TYPES
 * DIFFERENTS (Object[]) :
 *   - un String "N", "S", "E" ou "W" : un pas dans cette direction ;
 *   - un Integer n : refaire n fois la DERNIERE direction (s'il n'y en
 *     a pas encore, ignorer) ;
 *   - tout le reste (null, 3.5...) : ignore.
 * Un pas vers un mur ou hors de la carte laisse le robot sur place.
 * Des qu'il atteint le tresor, TOUT s'arrete (meme au milieu d'une
 * repetition) : c'est un break etiquete sur 2 boucles.
 *
 * Tu reutilises : switch expression (Ex07-09), pattern matching (Ex02-04),
 * boucles (Ex10-12), break etiquete (Ex14, Ex16).
 *
 *
 * ==================================================================
 * TODO 1 : findCell(grid, target)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ('S') -> {0, 0} ; ('T') -> {2, 3} ; ('X') -> null
 *
 * -- Le plan --
 *
 *   1. found = null ; search: for ligne { for colonne { si le caractere == target : found = {l, c} ; break search; } }
 *   2. Rendre found.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique pour le TODO 4.
 *
 *
 * ==================================================================
 * TODO 2 : isFree(grid, row, col)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Faux si row ou col sort de la carte ; faux si la case est '#' ; sinon vrai.
 *
 * -- Essayons a la main --
 *
 *   (0, 1) -> true ; (0, 3) -> false (mur) ; (3, 0) -> false (hors carte) ; (0, -1) -> false
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est une boite magique pour le TODO 3.
 *
 *
 * ==================================================================
 * TODO 3 : step(grid, position, direction)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On calcule la case visee (N : ligne - 1, S : ligne + 1, E : colonne
 * + 1, W : colonne - 1). Si elle est libre, on y va ; sinon on reste.
 * Une direction inconnue est une erreur de programmation :
 * IllegalArgumentException("direction inconnue : X").
 *
 * -- Essayons a la main --
 *
 *   ({0, 0}, "E") -> {0, 1} ; ({0, 0}, "N") -> {0, 0} (hors carte) ;
 *   ({0, 2}, "E") -> {0, 2} (mur) ; ({0, 0}, "X") -> exception
 *
 * -- Le plan --
 *
 *   1. dRow = switch (direction) { "N" -> -1 ; "S" -> 1 ; "E", "W" -> 0 ; default -> throw ... }
 *   2. dCol de la meme facon.
 *   3. Si isFree(nouvelle case) : rendre la nouvelle position, sinon la meme.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isFree (TODO 2).
 *
 *
 * ==================================================================
 * TODO 4 : run(grid, commands)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On note la position de depart, puis la position apres CHAQUE pas
 * tente (meme bloque). A la fin : ";trouve" si le tresor est atteint,
 * sinon ";perdu".
 *
 * -- Essayons a la main --
 *
 *   commandes {"E", "E", "S", 2, null, 3.5, "E", "N"}
 *   depart 0,0 ; E -> 0,1 ; E -> 0,2 ; S -> 1,2 ; 2 x S -> 2,2 puis 2,2 (hors carte) ;
 *   null et 3.5 ignores ; E -> 2,3 = tresor -> STOP ("N" n'est jamais joue)
 *   -> "0,0>0,1>0,2>1,2>2,2>2,2>2,3;trouve"
 *
 *   commandes {3, "W", 2} : 3 ignore (pas encore de direction) ; W -> 0,0 (bloque) ;
 *   2 x W -> 0,0, 0,0 -> "0,0>0,0>0,0>0,0;perdu"
 *
 * -- Le plan --
 *
 *   1. position = findCell('S') ; trace = "l,c" ; last = null ; found = false.
 *   2. commands: pour chaque commande :
 *        String dir -> repetitions = 1 ; last = dir
 *        Integer n et last != null -> repetitions = n
 *        sinon -> continue (commande ignoree)
 *      pour chaque repetition : position = step(...) ; trace += ">l,c" ;
 *        si la case est 'T' : found = true ; break commands;
 *   3. Rendre trace + (found ? ";trouve" : ";perdu").
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : TODO 1 et 3. "position en texte" (l + "," + c) revient souvent :
 * une petite methode privee est bienvenue.
 *
 *
 * ==================================================================
 * TODO 5 : countWalls(grid)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Deux for-each imbriques (lignes, puis caracteres), compter les '#'. -> 2
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les "Essayons a la main".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - grid[row].charAt(col) ; grid.length ; grid[row].length()
 *   - if (command instanceof String dir) { ... } else if (command instanceof Integer n && last != null) { ... } else continue;
 *   - for (char c : line.toCharArray())
 */
public class Exercise17_RobotCapstone {

    public static final String[] MAP = {"S..#", ".#..", "...T"};

    public static int[] findCell(String[] grid, char target) {
        throw new UnsupportedOperationException("TODO 1 : implementer findCell()");
    }

    public static boolean isFree(String[] grid, int row, int col) {
        throw new UnsupportedOperationException("TODO 2 : implementer isFree()");
    }

    public static int[] step(String[] grid, int[] position, String direction) {
        throw new UnsupportedOperationException("TODO 3 : implementer step()");
    }

    public static String run(String[] grid, Object[] commands) {
        throw new UnsupportedOperationException("TODO 4 : implementer run()");
    }

    public static int countWalls(String[] grid) {
        throw new UnsupportedOperationException("TODO 5 : implementer countWalls()");
    }

    public static void main(String[] args) {
        int[] start = findCell(MAP, 'S');
        int[] treasure = findCell(MAP, 'T');
        ExerciseChecker.check("1 findCell : S en 0,0, T en 2,3, X absent",
                start[0] == 0 && start[1] == 0 && treasure[0] == 2 && treasure[1] == 3 && findCell(MAP, 'X') == null);
        ExerciseChecker.check("2 isFree : (0,1) oui ; mur, hors carte et colonne -1 non",
                isFree(MAP, 0, 1) && !isFree(MAP, 0, 3) && !isFree(MAP, 3, 0) && !isFree(MAP, 0, -1));
        int[] moved = step(MAP, new int[]{0, 0}, "E");
        int[] blocked = step(MAP, new int[]{0, 2}, "E");
        int[] outside = step(MAP, new int[]{0, 0}, "N");
        ExerciseChecker.check("3 step : E avance, mur et bord bloquent",
                moved[0] == 0 && moved[1] == 1 && blocked[0] == 0 && blocked[1] == 2 && outside[0] == 0 && outside[1] == 0);
        String message = null;
        try {
            step(MAP, new int[]{0, 0}, "X");
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }
        ExerciseChecker.check("3 step direction X -> IllegalArgumentException(\"direction inconnue : X\")",
                "direction inconnue : X".equals(message));
        ExerciseChecker.check("4 run jusqu'au tresor",
                run(MAP, new Object[]{"E", "E", "S", 2, null, 3.5, "E", "N"}).equals("0,0>0,1>0,2>1,2>2,2>2,2>2,3;trouve"));
        ExerciseChecker.check("4 run bloque", run(MAP, new Object[]{3, "W", 2}).equals("0,0>0,0>0,0>0,0;perdu"));
        ExerciseChecker.check("5 countWalls == 2", countWalls(MAP) == 2);

        ExerciseChecker.summary();
    }
}
