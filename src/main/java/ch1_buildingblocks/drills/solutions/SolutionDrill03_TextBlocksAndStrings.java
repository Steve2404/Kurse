package ch1_buildingblocks.drills.solutions;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.drills.exercises.Drill03_TextBlocksAndStrings.
 */
public class SolutionDrill03_TextBlocksAndStrings {

    public static String twoLines() {
        // Le """ final seul sur sa ligne produit le \n apres "b".
        return """
                a
                b
                """;
    }

    public static String noFinalBreak() {
        // """ colle a "b" : pas de saut de ligne final.
        return """
                a
                b""";
    }

    public static String keepTrailingSpace() {
        // Les espaces de fin de ligne sont supprimes ; \s en garde un.
        return """
                a\s
                b
                """;
    }

    public static String joinedLines() {
        // \ en fin de ligne supprime le saut de ligne : "a" et "b" se collent.
        return """
                a\
                b
                """;
    }

    public static String indented() {
        // La marge commune est retiree ; "b" garde ses 2 espaces supplementaires.
        return """
                a
                  b
                """;
    }

    public static String withQuotes() {
        // Dans un bloc de texte, un " n'a pas besoin d'echappement.
        return """
                il dit "oui"
                """;
    }

    public static String withTripleQuotes() {
        // Trois " a la suite fermeraient le bloc : il faut en echapper un.
        return """
                x \""" y
                """;
    }

    public static String numbersThenText() {
        // Gauche a droite : 1 + 2 = 3 (addition), puis 3 + "x" = "3x" (concatenation).
        return 1 + 2 + "x";
    }

    public static String textThenNumbers() {
        // Des le premier String, chaque + devient une concatenation : "x1" puis "x12".
        return "x" + 1 + 2;
    }

    public static String charPlusInt() {
        // 'a' + 1 est une addition d'entiers (97 + 1) AVANT la concatenation avec "".
        return 'a' + 1 + "";
    }

    public static int lengthOfBlock() {
        // "Ticket\nOK\n" : 6 + 1 + 2 + 1 = 10 caracteres (chaque \n compte pour 1).
        String block = """
                Ticket
                OK
                """;
        return block.length();
    }
}
