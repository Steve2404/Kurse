package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise05_SwitchStatementBasics.
 */
public class Solution05_SwitchStatementBasics {

    public static String monthName(int monthNumber) {
        // Chaque case sort par return : pas de break necessaire, pas de fall-through possible.
        switch (monthNumber) {
            case 1:
                return "Janvier";
            case 2:
                return "Fevrier";
            case 3:
                return "Mars";
            case 4:
                return "Avril";
            case 5:
                return "Mai";
            case 6:
                return "Juin";
            case 7:
                return "Juillet";
            case 8:
                return "Aout";
            case 9:
                return "Septembre";
            case 10:
                return "Octobre";
            case 11:
                return "Novembre";
            case 12:
                return "Decembre";
            default:
                return "Inconnu";
        }
    }

    public static String seasonForMonth(int monthNumber) {
        // Plusieurs case empiles partagent le meme code (fall-through voulu), puis break pour s'arreter.
        // result est initialisee sur tous les chemins grace au default.
        String result;
        switch (monthNumber) {
            case 12:
            case 1:
            case 2:
                result = "Hiver";
                break;
            case 3:
            case 4:
            case 5:
                result = "Printemps";
                break;
            case 6:
            case 7:
            case 8:
                result = "Ete";
                break;
            case 9:
            case 10:
            case 11:
                result = "Automne";
                break;
            default:
                result = "Inconnu";
        }
        return result;
    }
}
