package ch4_coreapis.drills.r06_arraysutil.solution;

import java.util.Arrays;

/**
 * SOLUTION du drill de rappel 6 - la classe Arrays.
 */
public class Recall06 {

    public static void main(String[] args) {
        int[] numbers = {6, 9, 1, 8};
        Arrays.sort(numbers);
        System.out.println("D01 : " + Arrays.toString(numbers));
        String[] words = {"10", "9", "Zebre", "apple", "100", "Apple"};
        Arrays.sort(words);
        System.out.println("D02 : " + Arrays.toString(words));
        int[] sorted = {2, 4, 6, 8};
        System.out.println("D03 : " + Arrays.binarySearch(sorted, 6) + " " + Arrays.binarySearch(sorted, 1) + " " + Arrays.binarySearch(sorted, 5)
                + " " + Arrays.binarySearch(sorted, 9));
        System.out.println("D04 : " + Arrays.compare(new int[] {1, 2}, new int[] {1, 2}) + " " + Arrays.compare(new int[] {1, 2}, new int[] {1, 2, 3})
                + " " + Arrays.compare(new int[] {1, 3}, new int[] {1, 2, 3}) + " " + Arrays.compare(new String[] {"a"}, new String[] {"B"}));
        System.out.println("D05 : " + Arrays.mismatch(new int[] {1, 2}, new int[] {1, 2}) + " " + Arrays.mismatch(new int[] {1, 2}, new int[] {1, 3})
                + " " + Arrays.mismatch(new int[] {1, 2}, new int[] {1, 2, 3}));
        int[] first = {1, 2};
        int[] second = {1, 2};
        System.out.println("D06 : " + (first == second) + " " + first.equals(second) + " " + Arrays.equals(first, second));
        int[] filled = new int[4];
        Arrays.fill(filled, 7);
        Arrays.fill(filled, 1, 3, 0);
        System.out.println("D07 : " + Arrays.toString(filled) + " " + Arrays.toString(Arrays.copyOf(sorted, 2)) + " "
                + Arrays.toString(Arrays.copyOf(sorted, 6)) + " " + Arrays.toString(Arrays.copyOfRange(sorted, 1, 3)));
        int[] unsorted = {5, 1, 4};
        int found = Arrays.binarySearch(unsorted, 4);
        System.out.println("D08 : " + (found >= 0 ? "trouve par hasard" : "resultat imprevisible") + " " + Arrays.toString(unsorted));
    }
}
