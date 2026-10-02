package ch3_makingdecisions.drills.r02_switchstmt.solution;

/**
 * SOLUTION du drill de rappel 2 - le switch instruction.
 */
public class Recall02 {

    static final int FREEZE = 0;

    static int fallThrough(int start) {
        int count = 0;
        switch (start) {
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

    static String day(int d) {
        String result = "";
        switch (d) {
            default:
                result = "inconnu";
                break;
            case 6:
            case 7:
                result = "week-end";
                break;
            case 1, 2, 3, 4, 5:
                result = "semaine";
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("D01 : " + fallThrough(1) + " " + fallThrough(2) + " " + fallThrough(3) + " " + fallThrough(4) + " " + fallThrough(9));
        System.out.println("D02 : " + day(6) + " " + day(3) + " " + day(0));
        String cmd = "stop";
        switch (cmd) {
            case "go" -> System.out.println("D03 : demarre");
            case "stop" -> System.out.println("D03 : arret");
            default -> System.out.println("D03 : ?");
        }
        char grade = 'B';
        String msg = "";
        switch (grade) {
            case 'A':
                msg = msg + "excellent ";
            case 'B':
                msg = msg + "bien ";
            case 'C':
                msg = msg + "passable";
                break;
            default:
                msg = "echec";
        }
        System.out.println("D04 : " + msg);
        int temp = 0;
        switch (temp) {
            case FREEZE -> System.out.println("D05 : gel (case sur une constante final)");
            default -> System.out.println("D05 : pas de gel");
        }
        Integer boxed = 2;
        switch (boxed) {
            case 1 -> System.out.println("D06 : un");
            case 2 -> System.out.println("D06 : deux (switch sur un Integer)");
            default -> System.out.println("D06 : autre");
        }
        var level = "medium";
        int points = 0;
        switch (level) {
            case "easy":
                points += 1;
                break;
            case "medium":
                points += 5;
            case "hard":
                points += 10;
        }
        System.out.println("D07 : " + points);
        byte small = 3;
        switch (small) {
            case 1, 3, 5 -> System.out.println("D08 : impair");
            default -> System.out.println("D08 : pair");
        }
    }
}
