package ch4_coreapis.drills.r13_kata.solution;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * SOLUTION du drill de rappel 13 - kata mixte chronometre.
 */
public class Recall13 {

    public static void main(String[] args) {
        String sentence = "  le Java est un Langage  ";
        String[] words = sentence.strip().split(" ");
        StringBuilder initials = new StringBuilder();
        for (String w : words) {
            initials.append(w.toUpperCase().charAt(0));
        }
        System.out.println("D01 : " + words.length + " " + initials + " " + initials.reverse());
        String word = "Kayak";
        String reversed = new StringBuilder(word).reverse().toString();
        System.out.println("D02 : " + reversed + " " + word.equalsIgnoreCase(reversed) + " " + word.equals(reversed));
        int[] values = {5, 3, 9, 1, 7};
        int[] sorted = Arrays.copyOf(values, values.length);
        Arrays.sort(sorted);
        System.out.println("D03 : " + Arrays.toString(sorted) + " " + Arrays.binarySearch(sorted, 7) + " " + Arrays.binarySearch(sorted, 4) + " " + values[0]);
        int[][] table = new int[3][3];
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                table[r][c] = (r + 1) * (c + 1);
            }
        }
        System.out.println("D04 : " + Arrays.deepToString(table) + " " + table[2][1]);
        System.out.println("D05 : " + Math.round(2.5) + " " + Math.round(-2.5) + " " + Math.ceil(-0.5) + " " + Math.pow(3, 2) + " " + Math.max(1, 2L));
        LocalDate start = LocalDate.of(2026, 1, 31);
        System.out.println("D06 : " + start.plusMonths(1) + " " + start.plusMonths(1).plusMonths(1) + " " + start.plusMonths(2) + " "
                + Period.between(start, LocalDate.of(2026, 3, 1)));
        System.out.println("D07 : " + ChronoUnit.DAYS.between(start, LocalDate.of(2026, 12, 25)) + " " + LocalDate.of(2026, 12, 25).getDayOfWeek());
        String a = "Java";
        String b = "Ja" + "va";
        String c = new String("Java");
        System.out.println("D08 : " + (a == b) + " " + (a == c) + " " + a.equals(c) + " " + "%s-%03d".formatted(a, 7));
    }
}
