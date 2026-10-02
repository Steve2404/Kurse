package ch1_buildingblocks.drills.r04_textblocks.solution;

/**
 * SOLUTION du drill de rappel 4 - text blocks.
 */
public class Recall04 {

    public static void main(String[] args) {
        // Le contenu commence a la LIGNE SUIVANT les """ ouvrants.
        String d01 = """
                bonjour""";
        System.out.println("D01 : [" + d01 + "]");

        // """ fermants sur leur propre ligne : le texte se termine par un saut de ligne.
        String d02 = """
                a
                b
                """;
        System.out.print("D02 : [" + d02 + "]\n");

        // Les """ fermants 2 colonnes a gauche : 2 espaces conserves devant chaque ligne.
        String d03 = """
                  x
                  y
                """;
        System.out.print("D03 :\n" + d03);

        // \ en fin de ligne : pas de saut de ligne.
        String d04 = """
                un \
                deux""";
        System.out.println("D04 : [" + d04 + "]");

        // \s : un espace conserve, meme en fin de ligne (les espaces de fin ordinaires sont supprimes).
        String d05 = """
                fin\s""";
        System.out.println("D05 : [" + d05 + "]");

        // Guillemets libres ; trois guillemets d'affilee doivent etre echappes.
        String d06 = """
                "cite" et \"""triple\""" fin""";
        System.out.println("D06 : " + d06);

        // Les echappements classiques marchent : \t, \n.
        String d07 = """
                col1\tcol2\nligne2""";
        System.out.println("D07 :\n" + d07);

        // Une ligne vide au milieu est conservee.
        String d08 = """
                haut

                bas""";
        System.out.println("D08 :\n" + d08);
    }
}
