package ch8_lambdas.drills.r01_syntax.solution;

/**
 * SOLUTION du drill de rappel 1 - la syntaxe des lambdas.
 */
public class Recall01 {

    static int combine(int[] values, int start, Op op) {
        int acc = start;
        for (int v : values) {
            acc = op.apply(acc, v);
        }
        return acc;
    }

    // Une methode qui RENVOIE une lambda ; prefix est capture (effectively final).
    static Tester startsWith(String prefix) {
        return s -> s.startsWith(prefix);
    }

    public static void main(String[] args) {
        Op add = (a, b) -> a + b;                        // types deduits, expression
        Op mul = (int a, int b) -> a * b;                // types explicites
        Op max = (var a, var b) -> {                     // var, bloc avec return
            return a > b ? a : b;
        };
        Op first = (final int a, final int b) -> a;      // modificateur final : seulement avec un type (ou var)
        System.out.println("D01 : " + add.apply(3, 4) + " " + mul.apply(3, 4) + " " + max.apply(3, 4) + " " + first.apply(3, 4));
        Tester empty = s -> s.isEmpty();                  // un seul parametre sans type : parentheses facultatives
        Tester longer = (s) -> s.length() > 3;
        Tester notEmpty = (String s) -> {
            boolean r = !s.isEmpty();
            return r;
        };
        System.out.println("D02 : " + empty.test("") + " " + longer.test("abc") + " " + notEmpty.test("x"));
        Maker hello = () -> "bonjour";                   // aucun parametre : () obligatoires
        Maker block = () -> {
            return "bloc";
        };
        System.out.println("D03 : " + hello.make() + " " + block.make());
        int[] values = {2, 5, 3};
        System.out.println("D04 : " + combine(values, 0, add) + " " + combine(values, 1, (a, b) -> a * b) + " " + combine(values, Integer.MIN_VALUE, max));
        Tester jv = startsWith("ja");
        System.out.println("D05 : " + jv.test("java") + " " + jv.test("kotlin") + " " + startsWith("").test("x"));
        StringBuilder sb = new StringBuilder();
        Runnable r = () -> sb.append("run");             // void : une expression dont la valeur est ignoree
        r.run();
        r.run();
        System.out.println("D06 : " + sb);
    }
}

interface Op {
    int apply(int a, int b);
}

interface Tester {
    boolean test(String s);
}

interface Maker {
    String make();
}
