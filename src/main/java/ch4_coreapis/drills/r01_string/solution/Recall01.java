package ch4_coreapis.drills.r01_string.solution;

/**
 * SOLUTION du drill de rappel 1 - methodes de base de String.
 */
public class Recall01 {

    public static void main(String[] args) {
        String s = "animals";
        System.out.println("D01 : " + s.length() + " " + s.charAt(0) + " " + s.charAt(6) + " " + s.charAt(s.length() - 1));
        // 4 formes de indexOf : un char, un char a partir de, un texte, un texte a partir de.
        System.out.println("D02 : " + s.indexOf('a') + " " + s.indexOf('a', 4) + " " + s.indexOf("al") + " " + s.indexOf("al", 5) + " " + s.lastIndexOf('a'));
        // substring(debut) / substring(debut, fin EXCLUE) ; substring(3, 3) est vide.
        System.out.println("D03 : " + s.substring(3) + " " + s.substring(3, 4) + " [" + s.substring(3, 3) + "] " + s.substring(0, s.length()));
        String mixed = "Java Rocks";
        System.out.println("D04 : " + mixed.toUpperCase() + " " + mixed.toLowerCase() + " " + mixed);
        System.out.println("D05 : " + "abc".equals("ABC") + " " + "abc".equalsIgnoreCase("ABC") + " " + mixed.startsWith("Ja") + " "
                + mixed.startsWith("va", 2) + " " + mixed.endsWith("ks") + " " + mixed.contains("a R"));
        System.out.println("D06 : " + "banana".replace('a', 'o') + " " + "banana".replace("an", "AN") + " " + "ab".concat("cd") + " " + "-".repeat(3));
        System.out.println("D07 : [" + "  pad  ".strip() + "] [" + "  pad  ".stripLeading() + "] [" + "  pad  ".stripTrailing() + "] [" + "  pad  ".trim()
                + "] " + "".isEmpty() + " " + " ".isEmpty() + " " + " ".isBlank());
        System.out.println("D08 : " + String.join("/", "a", "b", "c") + " " + "x,y,,z".split(",").length + " " + "AbC".compareTo("Abd"));
    }
}
