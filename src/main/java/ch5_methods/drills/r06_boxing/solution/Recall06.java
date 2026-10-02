package ch5_methods.drills.r06_boxing.solution;

/**
 * SOLUTION du drill de rappel 6 - autoboxing et unboxing.
 */
public class Recall06 {

    static int twice(Integer n) {
        return n * 2;   // unboxing automatique
    }

    static Integer half(int n) {
        return n / 2;   // boxing automatique au retour
    }

    static boolean isNull(Integer n) {
        return n == null;
    }

    public static void main(String[] args) {
        Integer a = 127;
        Integer b = 127;
        Integer c = 1000;
        Integer d = 1000;
        System.out.println("D01 : " + (a == b) + " " + (c == d) + " " + c.equals(d) + " " + (c.intValue() == d));
        Integer boxed = 5;
        int unboxed = boxed;
        Double ratio = 2.5;
        Character letter = 'x';
        Boolean flag = true;
        System.out.println("D02 : " + (boxed + unboxed) + " " + ratio * 2 + " " + letter + " " + !flag);
        System.out.println("D03 : " + twice(21) + " " + half(9) + " " + isNull(null) + " " + isNull(0));
        Long big = 5L;
        System.out.println("D04 : " + big.equals(5) + " " + big.equals(5L) + " " + (big == 5) + " " + Integer.valueOf(5).equals(5));
        System.out.println("D05 : " + Integer.parseInt("12") + " " + Integer.valueOf("12") + " " + Double.parseDouble("1.5") + " " + Boolean.parseBoolean("TRUE")
                + " " + Boolean.parseBoolean("oui"));
        Integer counter = 10;
        counter++;            // unboxing, +1, boxing d'un NOUVEL objet
        Integer same = counter;
        counter += 5;
        System.out.println("D06 : " + counter + " " + same);
        Integer[] scores = {3, null, 7};
        int total = 0;
        int nulls = 0;
        for (Integer s : scores) {
            if (s == null) {
                nulls++;
            } else {
                total += s;
            }
        }
        System.out.println("D07 : " + total + " " + nulls);
        System.out.println("D08 : " + Integer.MAX_VALUE + " " + Integer.MIN_VALUE + " " + Integer.compare(3, 7) + " " + Character.getNumericValue('8')
                + " " + Integer.toBinaryString(10));
    }
}
