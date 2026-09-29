package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 10 - Un mini editeur de texte avec un curseur, construit sur StringBuilder (niveau : avance)
 * =====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un editeur de texte, c'est un texte et un curseur (une position
 * entre deux lettres, de 0 a la longueur). On lui donne des commandes :
 *
 *   "type abc"  : insere "abc" au curseur ; le curseur avance de 3
 *   "left n"    : recule le curseur de n (sans passer sous 0)
 *   "right n"   : avance le curseur de n (sans depasser la longueur)
 *   "back n"    : efface n lettres AVANT le curseur (comme la touche Retour arriere)
 *   "del n"     : efface n lettres APRES le curseur (comme la touche Suppr)
 *   "home"      : curseur au debut ; "end" : curseur a la fin
 *   "upper"     : tout le texte en majuscules (le curseur ne bouge pas)
 *   autre chose : IllegalArgumentException("commande inconnue : " + nom)
 *
 * On affiche le texte avec un '|' a la place du curseur.
 *
 *
 * ==================================================================
 * TODO 1 : render(text, cursor)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut voir le curseur SANS abimer le texte : on travaille sur une
 * COPIE (new StringBuilder(text)), jamais sur text lui-meme.
 *
 * -- Essayons a la main --
 *
 *   ("abc", 1) -> "a|bc"     ("abc", 3) -> "abc|"
 *
 * -- Le plan --
 *
 *   1. Rendre new StringBuilder(text).insert(cursor, '|').toString().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : clamp(value, min, max)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   (5, 0, 3) -> 3     (-2, 0, 3) -> 0     (2, 0, 3) -> 2
 *
 * -- Le plan --
 *
 *   1. Rendre Math.max(min, Math.min(max, value)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * C'est elle, la petite boite : left, right, back et del s'en servent.
 *
 *
 * ==================================================================
 * TODO 3 : apply(text, cursor, command)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une seule commande. Elle MODIFIE text (le StringBuilder) et rend la
 * nouvelle position du curseur.
 *
 * -- Essayons a la main --
 *
 *   texte "abcdef", curseur 4, "back 3" : on efface de max(0, 4 - 3) = 1 a 4 -> "aef", curseur 1
 *   texte "aef",    curseur 1, "del 1"  : on efface de 1 a min(3, 1 + 1) = 2 -> "af",  curseur 1
 *   texte "xy",     curseur 2, "left 10": curseur clamp(2 - 10, 0, 2) = 0
 *
 * -- Le plan --
 *
 *   1. space = command.indexOf(' ') ; name = la partie avant (ou toute la commande) ;
 *      arg = la partie apres (le texte pour "type", un nombre pour les autres).
 *   2. switch sur name :
 *        type  -> text.insert(cursor, arg) ; rendre cursor + arg.length()
 *        left  -> clamp(cursor - n, 0, longueur)       right -> clamp(cursor + n, 0, longueur)
 *        back  -> start = clamp(cursor - n, 0, cursor) ; text.delete(start, cursor) ; rendre start
 *        del   -> text.delete(cursor, clamp(cursor + n, 0, longueur)) ; rendre cursor
 *        home  -> 0       end -> longueur
 *        upper -> chaque lettre : setCharAt(i, Character.toUpperCase(...)) ; rendre cursor
 *        default -> throw new IllegalArgumentException("commande inconnue : " + name)
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : clamp (TODO 2). Integer.parseInt(arg) transforme "3" en 3.
 *
 *
 * ==================================================================
 * TODO 4 : run(commands...)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. text = new StringBuilder() ; cursor = 0.
 *   2. Pour chaque commande : cursor = apply(text, cursor, commande).
 *   3. Rendre render(text, cursor).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Il assemble apply et render.
 *
 * Exemple a verifier :
 *
 *   run("type Bonjour", "home", "type Oh ", "end", "type !")  -> "Oh Bonjour!|"
 *   run("type abcdef", "left 2", "back 3", "del 1")          -> "a|f"
 *   run("type xy", "left 10", "type >")                        -> ">|xy"
 *   run("back 5")                                              -> "|"
 *   run("type java", "left 2", "upper")                        -> "JA|VA"
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - delete(debut, fin) : fin EXCLUE ; delete(3, 3) ne fait rien (pas d'exception).
 *   - insert(position, texte) : position doit etre entre 0 et length(), sinon exception.
 */
public class Exercise10_TextEditor {

    public static String render(StringBuilder text, int cursor) {
        throw new UnsupportedOperationException("TODO 1 : implementer render()");
    }

    public static int clamp(int value, int min, int max) {
        throw new UnsupportedOperationException("TODO 2 : implementer clamp()");
    }

    public static int apply(StringBuilder text, int cursor, String command) {
        throw new UnsupportedOperationException("TODO 3 : implementer apply()");
    }

    public static String run(String... commands) {
        throw new UnsupportedOperationException("TODO 4 : implementer run()");
    }

    public static void main(String[] args) {
        StringBuilder abc = new StringBuilder("abc");
        ExerciseChecker.check("render : a|bc et abc| (le texte n'est PAS modifie)",
                render(abc, 1).equals("a|bc") && render(abc, 3).equals("abc|") && abc.toString().equals("abc"));
        ExerciseChecker.check("clamp : 3, 0, 2", clamp(5, 0, 3) == 3 && clamp(-2, 0, 3) == 0 && clamp(2, 0, 3) == 2);

        StringBuilder text = new StringBuilder("abcdef");
        int cursor = apply(text, 4, "back 3");
        ExerciseChecker.check("apply back 3 : \"aef\", curseur 1", text.toString().equals("aef") && cursor == 1);

        ExerciseChecker.check("run : Oh Bonjour!|", run("type Bonjour", "home", "type Oh ", "end", "type !").equals("Oh Bonjour!|"));
        ExerciseChecker.check("run : a|f", run("type abcdef", "left 2", "back 3", "del 1").equals("a|f"));
        ExerciseChecker.check("run : >|xy et | (bornes respectees)", run("type xy", "left 10", "type >").equals(">|xy") && run("back 5").equals("|"));
        ExerciseChecker.check("run : JA|VA", run("type java", "left 2", "upper").equals("JA|VA"));

        String message = null;
        try {
            run("jump 3");
        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        }
        ExerciseChecker.check("commande inconnue -> IllegalArgumentException", "commande inconnue : jump".equals(message));

        ExerciseChecker.summary();
    }
}
