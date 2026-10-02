package ch4_coreapis.drills.r05_arrays.solution;

import java.util.Arrays;

/**
 * SOLUTION du drill de rappel 5 - tableaux.
 */
public class Recall05 {

    public static void main(String[] args) {
        int[] a = new int[3];
        int b[] = {4, 5, 6};
        int[] c = new int[] {7, 8};
        System.out.println("D01 : " + Arrays.toString(a) + " " + Arrays.toString(b) + " " + c.length);
        String[] names = new String[2];
        boolean[] flags = new boolean[2];
        double[] values = new double[1];
        char[] letters = new char[1];
        int code = letters[0];
        System.out.println("D02 : " + Arrays.toString(names) + " " + Arrays.toString(flags) + " " + Arrays.toString(values) + " " + code);
        int[][] grid = new int[2][3];
        grid[1][2] = 9;
        int[] rows[] = {{1}, {2, 3}, {4, 5, 6}};
        System.out.println("D03 : " + grid.length + " " + grid[0].length + " " + Arrays.deepToString(grid) + " " + rows[2].length + " " + rows[1][1]);
        int[][] jagged = new int[3][];
        jagged[0] = new int[1];
        jagged[2] = new int[] {1, 2};
        System.out.println("D04 : " + Arrays.toString(jagged[2]) + " " + (jagged[1] == null) + " " + jagged.length);
        int[] original = {1, 2, 3};
        int[] alias = original;
        int[] copy = original.clone();
        alias[0] = 99;
        System.out.println("D05 : " + Arrays.toString(original) + " " + Arrays.toString(copy) + " " + (original == alias) + " " + original.equals(copy));
        int[] numbers = {1, 2, 3};
        for (int n : numbers) {
            n = n * 10;      // modifie la COPIE locale, pas le tableau
        }
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] *= 2;
        }
        System.out.println("D06 : " + Arrays.toString(numbers));
        Object[] objects = new String[] {"x", "y"};
        int total = 0;
        for (int[] row : rows) {
            for (int v : row) {
                total += v;
            }
        }
        System.out.println("D07 : " + objects.length + " " + objects[1] + " " + total);
        char[] word = {'j', 'a', 'v', 'a'};
        System.out.println("D08 : " + new String(word) + " " + String.valueOf(word, 1, 2) + " " + Arrays.toString("a-b".toCharArray()));
    }
}
