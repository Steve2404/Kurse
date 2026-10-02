package ch3_makingdecisions.drills.r05_for.solution;

/**
 * SOLUTION du drill de rappel 5 - for et for-each.
 */
public class Recall05 {

    public static void main(String[] args) {
        String up = "";
        for (int i = 0; i < 5; i++) {
            up = up + i;
        }
        String down = "";
        for (int i = 10; i > 0; i -= 3) {
            down = down + i + " ";
        }
        System.out.println("D01 : " + up + " | " + down);
        // Plusieurs variables du MEME type dans l'initialisation, plusieurs expressions dans la mise a jour.
        String pairs = "";
        for (int i = 0, j = 6; i < j; i++, j--) {
            pairs = pairs + i + j + " ";
        }
        System.out.println("D02 : " + pairs);
        int k = 0;
        for (; k < 3; ) {
            k++;
        }
        System.out.println("D03 : " + k);
        String all = "";
        int count = 0;
        for (String arg : args) {
            all = all + "[" + arg + "]";
            count++;
        }
        System.out.println("D04 : " + count + " " + all);
        int total = 0;
        for (String arg : args) {
            total += Integer.parseInt(arg);
        }
        System.out.println("D05 : " + total);
        String reversed = "";
        for (int i = args.length - 1; i >= 0; i--) {
            reversed = reversed + args[i] + " ";
        }
        System.out.println("D06 : " + reversed);
        long factorial = 1;
        for (int i = 2; i <= 20; i++) {
            factorial *= i;
        }
        System.out.println("D07 : " + factorial);
        int loops = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = i; j < 3; j++) {
                loops++;
            }
        }
        System.out.println("D08 : " + loops);
    }
}
