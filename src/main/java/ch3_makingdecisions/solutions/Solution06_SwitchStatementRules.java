package ch3_makingdecisions.solutions;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.exercises.Exercise06_SwitchStatementRules.
 */
public class Solution06_SwitchStatementRules {

    public static boolean acceptsSwitch(String type) {
        // Les types d'un switch classique : entiers jusqu'a int (et leurs boites), char, String, enum.
        // Jamais long, float, double, boolean (ni leurs boites).
        switch (type) {
            case "byte", "short", "char", "int", "Byte", "Short", "Character", "Integer", "String", "enum":
                return true;
            default:
                return false;
        }
    }

    public static int fallThroughCount(int x) {
        // Sans break, l'execution coule dans les case suivants jusqu'au premier break.
        int count = 0;
        switch (x) {
            case 1:
                count++;
            case 2:
                count++;
            case 3:
                count++;
                break;
            case 4:
                count += 10;
        }
        return count;
    }

    public static String dayType(int day) {
        // final + litteral = constante de compilation : utilisable comme etiquette de case.
        final int SATURDAY = 6;
        final int SUNDAY = 7;
        switch (day) {
            case SATURDAY, SUNDAY:
                return "week-end";
            case 1, 2, 3, 4, 5:
                return "semaine";
            default:
                return "invalide";
        }
    }

    public static String commandLabel(String command) {
        // Un switch sur un String null lance NullPointerException : on filtre null avant.
        if (command == null) {
            return "aucune";
        }
        switch (command) {
            case "start":
                return "demarrage";
            case "stop":
                return "arret";
            case "quit", "exit":
                return "sortie";
            default:
                return "inconnue";
        }
    }

    public static String defaultInTheMiddle(int x) {
        // default n'est choisi que si aucun case ne correspond, mais on coule ensuite vers "case 2".
        String s = "";
        switch (x) {
            case 1:
                s += "1";
            default:
                s += "D";
            case 2:
                s += "2";
        }
        return s;
    }
}
