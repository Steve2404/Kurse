package ch6_classdesign.projects.p04_immutable;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON ImmutableLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "money : 19.99 EUR + 5.01 EUR = 25.00 EUR ; prix inchange 19.99 EUR ; 15 % = 3.75 EUR ; EUR + USD = null",
            "partage 3 : [33.34 EUR, 33.33 EUR, 33.33 EUR] ; 50/30/20 de 0.07 : [0.04 EUR, 0.02 EUR, 0.01 EUR]",
            "egalite : true false true false",
            "fractions : -3/4 1/2 3/2 H(10)=7381/2520 true",
            "copies defensives : [[1, 2], [3, 4]] intacte ; transposee [[1, 3], [2, 4]] ; carre [[7, 10], [15, 22]] ; egal a lui-meme reconstruit true",
            "fibonacci par puissance : F(10)=55 F(50)=12586269025 F(90)=2880067194370816120",
            "determinants : 49 30 0 5 ; det(A^3) = det(A)^3 : 117649");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.M3", "Data.WEIGHTS", "final class Money", "final class Fraction",
            "final class Matrix", "private Money(", "private Fraction(", ".clone()",
            "3xpublic boolean equals(Object", "3xpublic int hashCode()", "Arrays.deepEquals(", "Arrays.deepHashCode(",
            "!re:void set\\w*\\(##setter (un objet immuable n en a pas)",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "ImmutableLab", args, EXPECTED, API);
    }
}
