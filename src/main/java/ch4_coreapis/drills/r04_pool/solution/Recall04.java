package ch4_coreapis.drills.r04_pool.solution;

/**
 * SOLUTION du drill de rappel 4 - pool de chaines et egalite.
 */
public class Recall04 {

    static final String CONSTANT = "Hello";

    public static void main(String[] args) {
        String x = "Hello World";
        String y = "Hello World";
        System.out.println("D01 : " + (x == y) + " " + x.equals(y));
        String z = new String("Hello World");
        System.out.println("D02 : " + (x == z) + " " + x.equals(z) + " " + (x == z.intern()));
        String literal = "Hello" + " World";      // constante de compilation
        System.out.println("D03 : " + (x == literal));
        String part = "Hello";
        String runtime = part + " World";          // calcule a l'execution
        System.out.println("D04 : " + (x == runtime) + " " + (x == runtime.intern()));
        String fromConstant = CONSTANT + " World"; // final static : constante de compilation
        System.out.println("D05 : " + (x == fromConstant));
        String trimmed = " Hello World".trim();
        String unchanged = "Hello World".trim();   // rien a retirer : trim rend le MEME objet
        System.out.println("D06 : " + (x == trimmed) + " " + (x == unchanged));
        StringBuilder sb1 = new StringBuilder("ab");
        StringBuilder sb2 = new StringBuilder("ab");
        System.out.println("D07 : " + sb1.equals(sb2) + " " + sb1.toString().equals(sb2.toString()) + " " + (sb1.toString() == sb2.toString()));
        String concatenated = "a".concat("b");
        System.out.println("D08 : " + ("ab" == concatenated) + " " + "ab".equals(concatenated) + " " + ("ab" == concatenated.intern()));
    }
}
