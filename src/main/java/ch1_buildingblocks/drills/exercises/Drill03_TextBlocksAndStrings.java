package ch1_buildingblocks.drills.exercises;

import ch1_buildingblocks.ExerciseChecker;

/**
 * DRILL 03 - Blocs de texte et bases des String : sauts de ligne, marges, echappements, concatenation
 * ===================================================================================================
 *
 * Mode d'emploi : voir Drill01_WrapperApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 * Pour les TODO 1 a 7, ecris un BLOC DE TEXTE (""") qui produit
 * exactement le texte demande (les \n du resultat sont les vrais
 * sauts de ligne).
 *
 *
 * -- Les TODO (outil vise entre crochets) --
 *
 * TODO 1  : twoLines()          [""" final seul sur sa ligne] -> "a\nb\n".
 * TODO 2  : noFinalBreak()      [""" colle au texte] -> "a\nb".
 * TODO 3  : keepTrailingSpace() [\s] -> "a \nb\n" (l'espace apres a est conserve).
 * TODO 4  : joinedLines()       [\ en fin de ligne] -> "ab\n" (2 lignes du code, 1 ligne de texte).
 * TODO 5  : indented()          [marge commune retiree] -> "a\n  b\n".
 * TODO 6  : withQuotes()        [guillemets sans echappement] -> "il dit \"oui\"\n".
 * TODO 7  : withTripleQuotes()  [\""" dans un bloc] -> "x \"\"\" y\n".
 * TODO 8  : numbersThenText()   [+ lu de gauche a droite] 1 + 2 + "x" -> "3x".
 * TODO 9  : textThenNumbers()   [+ lu de gauche a droite] "x" + 1 + 2 -> "x12".
 * TODO 10 : charPlusInt()       [char + int = int] 'a' + 1 + "" -> "98".
 * TODO 11 : lengthOfBlock()     [length()] longueur du texte "Ticket\nOK\n" -> 10.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Ouverture : """ puis RETOUR A LA LIGNE obligatoire (rien d'autre sur la ligne)
 *   Fin       : """ seul sur sa ligne -> \n final ; """ colle au texte -> pas de \n final
 *   Marge     : l'indentation commune a toutes les lignes (et au """ final) est retiree
 *   Espaces de fin de ligne : supprimes, sauf avec \s
 *   \ en fin de ligne : pas de saut de ligne (la ligne suivante est collee)
 *   " et "" : permis tels quels ; """ : il faut ecrire \"""
 *   + : evalue de gauche a droite ; tant qu'aucun String n'est apparu, c'est une addition
 * ---------------------------------------------------------------------
 */
public class Drill03_TextBlocksAndStrings {

    public static String twoLines() {
        throw new UnsupportedOperationException("TODO 1 : implementer twoLines()");
    }

    public static String noFinalBreak() {
        throw new UnsupportedOperationException("TODO 2 : implementer noFinalBreak()");
    }

    public static String keepTrailingSpace() {
        throw new UnsupportedOperationException("TODO 3 : implementer keepTrailingSpace()");
    }

    public static String joinedLines() {
        throw new UnsupportedOperationException("TODO 4 : implementer joinedLines()");
    }

    public static String indented() {
        throw new UnsupportedOperationException("TODO 5 : implementer indented()");
    }

    public static String withQuotes() {
        throw new UnsupportedOperationException("TODO 6 : implementer withQuotes()");
    }

    public static String withTripleQuotes() {
        throw new UnsupportedOperationException("TODO 7 : implementer withTripleQuotes()");
    }

    public static String numbersThenText() {
        throw new UnsupportedOperationException("TODO 8 : implementer numbersThenText()");
    }

    public static String textThenNumbers() {
        throw new UnsupportedOperationException("TODO 9 : implementer textThenNumbers()");
    }

    public static String charPlusInt() {
        throw new UnsupportedOperationException("TODO 10 : implementer charPlusInt()");
    }

    public static int lengthOfBlock() {
        throw new UnsupportedOperationException("TODO 11 : implementer lengthOfBlock()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  twoLines() == \"a\\nb\\n\"", twoLines().equals("a\nb\n"));
        ExerciseChecker.check("2  noFinalBreak() == \"a\\nb\"", noFinalBreak().equals("a\nb"));
        ExerciseChecker.check("3  keepTrailingSpace() == \"a \\nb\\n\"", keepTrailingSpace().equals("a \nb\n"));
        ExerciseChecker.check("4  joinedLines() == \"ab\\n\"", joinedLines().equals("ab\n"));
        ExerciseChecker.check("5  indented() == \"a\\n  b\\n\"", indented().equals("a\n  b\n"));
        ExerciseChecker.check("6  withQuotes() == il dit \"oui\" + \\n", withQuotes().equals("il dit \"oui\"\n"));
        ExerciseChecker.check("7  withTripleQuotes() == x \"\"\" y + \\n", withTripleQuotes().equals("x \"\"\" y\n"));
        ExerciseChecker.check("8  numbersThenText() == \"3x\"", numbersThenText().equals("3x"));
        ExerciseChecker.check("9  textThenNumbers() == \"x12\"", textThenNumbers().equals("x12"));
        ExerciseChecker.check("10 charPlusInt() == \"98\"", charPlusInt().equals("98"));
        ExerciseChecker.check("11 lengthOfBlock() == 10", lengthOfBlock() == 10);

        ExerciseChecker.summary();
    }
}
