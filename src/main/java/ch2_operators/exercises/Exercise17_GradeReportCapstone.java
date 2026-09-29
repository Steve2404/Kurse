package ch2_operators.exercises;

import ch2_operators.ExerciseChecker;

/**
 * EXERCICE 17 - CAPSTONE : le bulletin de notes (tous les operateurs du chapitre 2 en un exercice) (niveau : capstone)
 * ==================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir Exercise01_PreAndPostIncrementDecrement.java.
 *
 * -- Le contexte --
 *
 * Un professeur calcule les bulletins. Chaque piege du chapitre peut
 * fausser une note sans aucun message d'erreur :
 *   - division entiere : (15 + 16) / 2 == 15, pas 15.5 ;
 *   - debordement de byte : un byte 120 + 10 devient -126 ;
 *   - division par zero : 10 / 0 lance ArithmeticException ;
 *   - precedence : a + b / 2 n'est pas la moyenne de a et b.
 * Les options de l'eleve (absent, retard, dispense) sont rangees dans
 * un int, un bit par option.
 *
 * Bulletins attendus :
 *
 *   Lea  : notes [15, 16], retard        -> "Lea : 15.5 (B) [retard]"
 *   Hugo : notes [8, 9, 10], absent+retard -> "Hugo : 9.0 (AJ) [absent,retard]"
 *   Ines : aucune note, aucune option    -> "Ines : 0.0 (AJ) [-]"
 *
 *
 * ==================================================================
 * TODO 1 : average(scores)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Si on divise un int par un int, Java COUPE les decimales. Il faut
 * qu'au moins un des deux soit un double AVANT la division. Et la
 * moyenne de "aucune note" n'existe pas : on rend 0.0 (un ternaire).
 *
 * -- Essayons a la main --
 *
 *   [15, 16] -> 31 / 2.0 = 15.5 ; [8, 9, 10] -> 27 / 3.0 = 9.0 ; [] -> 0.0
 *
 * -- Le plan --
 *
 *   1. Additionner les notes (+=).
 *   2. Rendre 0.0 s'il n'y a pas de note, sinon somme / (double) nombre.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : mention(average)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Des ternaires enchaines, du seuil le plus haut au plus bas :
 * >= 16 "TB", >= 14 "B", >= 12 "AB", >= 10 "P", sinon "AJ".
 *
 * -- Le plan --
 *
 *   1. Une seule expression : avg >= 16 ? "TB" : avg >= 14 ? "B" : ...
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : addBonus(score, bonus)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On ajoute un bonus a une note stockee en byte, sans depasser 20.
 * PIEGE : score += bonus sur le byte deborderait AVANT qu'on puisse
 * plafonner (120 + 10 -> -126). Il faut calculer en int, plafonner,
 * PUIS revenir en byte.
 *
 * -- Essayons a la main --
 *
 *   (15, 3) -> 18 ; (18, 5) -> 20 ; (120, 10) -> 20 (pas -126)
 *
 * -- Le plan --
 *
 *   1. total = score + bonus (c'est deja un int : promotion).
 *   2. Rendre (byte) (total > 20 ? 20 : total).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : optionsLabel(options)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On lit les interrupteurs dans l'ordre ABSENT, LATE, EXEMPT, et on
 * ecrit leurs noms separes par des virgules. Aucun : "-".
 *
 * -- Essayons a la main --
 *
 *   LATE -> "retard" ; ABSENT | LATE -> "absent,retard" ; 0 -> "-"
 *
 * -- Le plan --
 *
 *   1. Pour chaque option presente ((options & OPTION) != 0), ajouter
 *      son nom, precede d'une virgule si le texte n'est pas vide.
 *   2. Texte vide -> "-".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : doubledProgress(before, after)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "L'eleve a-t-il au moins double sa note ?" : after / before >= 2.
 * Mais si before vaut 0, la division lance ArithmeticException. Le
 * && court-circuit est le garde du corps : si before != 0 est faux,
 * la division n'est MEME PAS calculee.
 *
 * -- Essayons a la main --
 *
 *   (5, 10) -> true ; (5, 9) -> false (9 / 5 == 1) ; (0, 10) -> false, sans exception
 *
 * -- Le plan --
 *
 *   1. Rendre before != 0 && after / before >= 2.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : reportLine(name, scores, options)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. name + " : " + moyenne + " (" + mention + ") [" + options + "]".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : TODO 1, 2 et 4.
 *
 *
 * Exemple a verifier : les 3 bulletins ci-dessus.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - sum / (double) scores.length   (et pas (double) (sum / scores.length))
 *   - String label = ""; if ((options & ABSENT) != 0) label += ...
 */
public class Exercise17_GradeReportCapstone {

    public static final int ABSENT = 1;
    public static final int LATE = 2;
    public static final int EXEMPT = 4;

    public static double average(int[] scores) {
        throw new UnsupportedOperationException("TODO 1 : implementer average()");
    }

    public static String mention(double average) {
        throw new UnsupportedOperationException("TODO 2 : implementer mention()");
    }

    public static byte addBonus(byte score, int bonus) {
        throw new UnsupportedOperationException("TODO 3 : implementer addBonus()");
    }

    public static String optionsLabel(int options) {
        throw new UnsupportedOperationException("TODO 4 : implementer optionsLabel()");
    }

    public static boolean doubledProgress(int before, int after) {
        throw new UnsupportedOperationException("TODO 5 : implementer doubledProgress()");
    }

    public static String reportLine(String name, int[] scores, int options) {
        throw new UnsupportedOperationException("TODO 6 : implementer reportLine()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1 average([15, 16]) == 15.5 (pas 15), ([8, 9, 10]) == 9.0, ([]) == 0.0",
                average(new int[]{15, 16}) == 15.5 && average(new int[]{8, 9, 10}) == 9.0 && average(new int[0]) == 0.0);
        ExerciseChecker.check("2 mention : 16 TB, 15.5 B, 12 AB, 10 P, 9.99 AJ",
                mention(16).equals("TB") && mention(15.5).equals("B") && mention(12).equals("AB")
                        && mention(10).equals("P") && mention(9.99).equals("AJ"));
        ExerciseChecker.check("3 addBonus : (15, 3) -> 18, (18, 5) -> 20, (120, 10) -> 20 (pas -126)",
                addBonus((byte) 15, 3) == 18 && addBonus((byte) 18, 5) == 20 && addBonus((byte) 120, 10) == 20);
        ExerciseChecker.check("4 optionsLabel : LATE -> retard, ABSENT|LATE -> absent,retard, tout -> absent,retard,dispense, 0 -> -",
                optionsLabel(LATE).equals("retard") && optionsLabel(ABSENT | LATE).equals("absent,retard")
                        && optionsLabel(ABSENT | LATE | EXEMPT).equals("absent,retard,dispense") && optionsLabel(0).equals("-"));
        ExerciseChecker.check("5 doubledProgress : (5, 10) oui, (5, 9) non, (0, 10) non sans exception",
                doubledProgress(5, 10) && !doubledProgress(5, 9) && !doubledProgress(0, 10));
        ExerciseChecker.check("6 bulletin de Lea", reportLine("Lea", new int[]{15, 16}, LATE).equals("Lea : 15.5 (B) [retard]"));
        ExerciseChecker.check("6 bulletin de Hugo",
                reportLine("Hugo", new int[]{8, 9, 10}, ABSENT | LATE).equals("Hugo : 9.0 (AJ) [absent,retard]"));
        ExerciseChecker.check("6 bulletin de Ines", reportLine("Ines", new int[0], 0).equals("Ines : 0.0 (AJ) [-]"));

        ExerciseChecker.summary();
    }
}
