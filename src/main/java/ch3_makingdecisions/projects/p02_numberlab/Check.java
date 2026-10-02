package ch3_makingdecisions.projects.p02_numberlab;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON NumberLab, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"60"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "PREMIERS <= 60 : 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59",
            "PARFAITS <= 10000 : 6 28 496 8128",
            "COLLATZ < 60 : depart 54, 112 etapes",
            "PALINDROMES 100..200 : 101 111 121 131 141 151 161 171 181 191",
            "ARMSTRONG 3 chiffres : 153 370 371 407",
            "PGCD(1071, 462) = 21 en 3 divisions, PPCM = 23562",
            "FACTEURS de 391 : 17 x 23",
            "FIZZBUZZ : 1 2 Fizz 4 Buzz Fizz 7 8 Fizz Buzz 11 Fizz 13 14 FizzBuzz");
            // EXPECTED-END

    static final List<String> API = List.of(
            "re:(?m)^\\s*\\w+:\\s*$##etiquette de boucle", "continue ", "break ", "do {",
            "} while (", "while (", "for (", "% 10",
            "/= 10", "re:= switch \\(##switch expression",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "NumberLab", args, ARGS, EXPECTED, API);
    }
}
