package ch4_coreapis.drills.r02_string2.solution;

/**
 * SOLUTION du drill de rappel 2 - concatenation, immutabilite, methodes recentes.
 */
public class Recall02 {

    public static void main(String[] args) {
        System.out.println("D01 : " + 1 + 2 + " " + (1 + 2) + " " + ('a' + 'b') + " " + ("" + 'a' + 'b') + " " + "x" + null);
        String s = "start";
        s += 1 + 2;
        s += 'c';
        s += true;
        System.out.println("D02 : " + s);
        String immutable = "abc";
        immutable.concat("def");
        immutable.toUpperCase();
        String chained = immutable.concat("def").toUpperCase().substring(2);
        System.out.println("D03 : " + immutable + " " + chained);
        // indent(n) ajoute n espaces (ou en retire si n < 0) et termine par \n ; on retire ce \n pour l'affichage.
        String indented = "a\n  b".indent(2);
        String unindented = "    x\n  y".indent(-2);
        System.out.println("D04 : [" + indented.replace("\n", "|") + "] [" + unindented.replace("\n", "|") + "]");
        System.out.println("D05 : [" + "  x\n    y".stripIndent().replace("\n", "|") + "] [" + "a\\tb\\\\c".translateEscapes() + "]");
        System.out.println("D06 : " + "%s=%d".formatted("n", 42) + " " + String.format("[%-4s][%4s][%.2s]", "ab", "ab", "abcdef") + " " + String.format("%05d", 42));
        System.out.println("D07 : " + "Abc".charAt(0) + 'b' + " " + ('A' + "bc") + " " + "abc".toUpperCase().equals("ABC"));
        String text = null;
        System.out.println("D08 : " + "valeur " + text + " " + ("" + null).length());
    }
}
