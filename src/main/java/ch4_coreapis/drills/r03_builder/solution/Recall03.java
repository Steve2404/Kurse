package ch4_coreapis.drills.r03_builder.solution;

/**
 * SOLUTION du drill de rappel 3 - StringBuilder.
 */
public class Recall03 {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("ab").append(1).append('c').append(true).append(2.5);
        System.out.println("D01 : " + sb + " " + sb.length());
        StringBuilder a = new StringBuilder("animals");
        a.insert(7, "-").insert(0, "-").insert(4, "+");
        System.out.println("D02 : " + a);
        StringBuilder b = new StringBuilder("abcdef");
        b.delete(1, 3).deleteCharAt(0);
        System.out.println("D03 : " + b);
        // delete et replace acceptent une fin AU-DELA de la longueur (on s'arrete au bout).
        StringBuilder c = new StringBuilder("abcdef");
        c.delete(4, 100);
        StringBuilder d = new StringBuilder("pigeon dirty");
        d.replace(3, 6, "sty");
        StringBuilder e = new StringBuilder("12345");
        e.replace(2, 100, "X");
        System.out.println("D04 : " + c + " " + d + " " + e);
        StringBuilder f = new StringBuilder("stressed");
        String sub = f.substring(0, 3);
        f.reverse();
        System.out.println("D05 : " + f + " " + sub + " " + f.indexOf("s") + " " + f.charAt(1));
        StringBuilder one = new StringBuilder("x");
        StringBuilder two = one.append("y");
        two.append("z");
        System.out.println("D06 : " + one + " " + (one == two));
        StringBuilder empty = new StringBuilder(100);
        StringBuilder text = new StringBuilder("hello");
        text.setLength(2);
        System.out.println("D07 : " + empty.length() + " [" + text + "] " + new StringBuilder("ab").equals(new StringBuilder("ab")));
        String result = new StringBuilder("level").reverse().toString();
        System.out.println("D08 : " + result + " " + result.equals("level") + " " + "level".contentEquals(new StringBuilder("level")));
    }
}
