package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

/**
 * EXERCICE 5 - Nettoyer et mettre en forme : strip, isBlank, split, repeat, format, lines (niveau : difficile)
 * ============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_StringImmutabilityAndConcatenation.java.
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   " x ".trim().length()  -> 3   trim() n'enleve que les caracteres <= ' '
 *   " x ".strip().length() -> 1   strip() enleve TOUS les espaces Unicode
 *   "   ".isEmpty() -> false     "   ".isBlank() -> true     "".isBlank() -> true
 *   "a,b,,c,,".split(",")        -> [a, b, , c]          les vides de la FIN disparaissent
 *   "a,b,,c,,".split(",", -1)    -> [a, b, , c, , ]      limite -1 : on les garde
 *   "ab".repeat(3) -> "ababab"   "ab".repeat(0) -> ""
 *   String.format("%-5s|%5s|%05d", "ab", "ab", 42) -> "ab   |   ab|00042"
 *   "a\nb\n\nc".lines().count()  -> 4
 *
 *
 * ==================================================================
 * TODO 1 : normalizeSpaces(text)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quelqu'un a tape avec des espaces partout. On veut un seul espace
 * entre les mots, et aucun au debut ni a la fin.
 *
 * -- Essayons a la main --
 *
 *   "  le   chat \t dort " -> "le chat dort"
 *   "   "                  -> ""
 *
 * -- Le plan --
 *
 *   1. clean = text.strip() ; si clean est vide, rendre "".
 *   2. Couper clean sur "\\s+" (un ou plusieurs blancs) et recoller avec String.join(" ", ...).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : tableRow(name, qty)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une ligne de tableau bien alignee : le nom colle a GAUCHE sur 6
 * cases, une barre, la quantite collee a DROITE sur 4 cases.
 *
 * -- Essayons a la main --
 *
 *   ("pomme", 3)  -> "pomme |   3"
 *   ("kiwi", 120) -> "kiwi  | 120"
 *
 * -- Le plan --
 *
 *   1. Rendre String.format("%-6s|%4d", name, qty).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : frame(text)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On dessine un cadre autour d'un mot. La ligne du haut et celle du
 * bas ont autant de tirets que le mot + 2 (un espace de chaque cote).
 *
 * -- Essayons a la main --
 *
 *   "Java" -> "+------+\n| Java |\n+------+"
 *
 * -- Le plan --
 *
 *   1. border = "+" + "-".repeat(text.length() + 2) + "+".
 *   2. Rendre border + "\n| " + text + " |\n" + border.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non (border est reutilise, mais une variable suffit).
 *
 *
 * ==================================================================
 * TODO 4 : csvFields(line)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une ligne CSV "a,,b," a QUATRE champs (le 2e et le 4e sont vides).
 * split(",") en oublierait un : les vides de la fin disparaissent.
 *
 * -- Essayons a la main --
 *
 *   "a,,b," -> [a, , b, ]  (4 champs)       "x" -> [x]
 *
 * -- Le plan --
 *
 *   1. Rendre line.split(",", -1).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : countNonBlankLines(text)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On compte les lignes qui contiennent vraiment quelque chose (une
 * ligne faite seulement d'espaces ne compte pas).
 *
 * -- Essayons a la main --
 *
 *   "un\n  \ndeux\n\ntrois" -> 3
 *
 * -- Le plan --
 *
 *   1. Pour chaque ligne de text.lines() (ou text.split("\n")) : si !ligne.isBlank(), compter.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : kind(s)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On range un texte dans une des 4 boites : "null", "vide" (aucun
 * caractere), "blanc" (que des espaces, meme Unicode), "texte". L'ORDRE
 * des tests compte : un texte vide est aussi blanc !
 *
 * -- Essayons a la main --
 *
 *   null -> "null" ; "" -> "vide" ; " \t" -> "blanc" ; " " -> "blanc" ; " a " -> "texte"
 *
 * -- Le plan --
 *
 *   1. null d'abord, puis isEmpty(), puis isBlank(), sinon "texte".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - String.join(" ", tableau) colle les elements avec un separateur.
 *   - %-6s : texte colle a gauche sur 6 cases ; %4d : entier colle a droite sur 4 cases.
 *   - text.lines() rend un Stream<String> (chapitre 10) ; une boucle sur split("\n") marche aussi.
 */
public class Exercise05_StringCleaningAndFormatting {

    public static String normalizeSpaces(String text) {
        throw new UnsupportedOperationException("TODO 1 : implementer normalizeSpaces()");
    }

    public static String tableRow(String name, int qty) {
        throw new UnsupportedOperationException("TODO 2 : implementer tableRow()");
    }

    public static String frame(String text) {
        throw new UnsupportedOperationException("TODO 3 : implementer frame()");
    }

    public static String[] csvFields(String line) {
        throw new UnsupportedOperationException("TODO 4 : implementer csvFields()");
    }

    public static long countNonBlankLines(String text) {
        throw new UnsupportedOperationException("TODO 5 : implementer countNonBlankLines()");
    }

    public static String kind(String s) {
        throw new UnsupportedOperationException("TODO 6 : implementer kind()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("normalizeSpaces : \"  le   chat \\t dort \" -> \"le chat dort\", \"   \" -> \"\"",
                normalizeSpaces("  le   chat \t dort ").equals("le chat dort") && normalizeSpaces("   ").isEmpty());
        ExerciseChecker.check("tableRow : \"pomme |   3\" et \"kiwi  | 120\"",
                tableRow("pomme", 3).equals("pomme |   3") && tableRow("kiwi", 120).equals("kiwi  | 120"));
        ExerciseChecker.check("frame(\"Java\")", frame("Java").equals("+------+\n| Java |\n+------+"));
        String[] fields = csvFields("a,,b,");
        ExerciseChecker.check("csvFields(\"a,,b,\") : 4 champs, le 2e et le 4e vides",
                fields.length == 4 && fields[1].isEmpty() && fields[3].isEmpty() && csvFields("x").length == 1);
        ExerciseChecker.check("countNonBlankLines(\"un\\n  \\ndeux\\n\\ntrois\") == 3", countNonBlankLines("un\n  \ndeux\n\ntrois") == 3);
        ExerciseChecker.check("kind : null, vide, blanc (x2), texte",
                kind(null).equals("null") && kind("").equals("vide") && kind(" \t").equals("blanc")
                        && kind(" ").equals("blanc") && kind(" a ").equals("texte"));

        ExerciseChecker.summary();
    }
}
