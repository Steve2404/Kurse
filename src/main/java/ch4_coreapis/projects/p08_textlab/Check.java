package ch4_coreapis.projects.p08_textlab;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 8 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON TextLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "anagrammes : Listen=oui triangle=oui Dormitory=oui apple=oui abc=non",
            "rle : aaabccddddde -> 3a1b2c5d1e -> aaabccddddde aller-retour true gain 2",
            "cesar : Khoor, Zruog! | Hello, World! | rot13 deux fois Hello, World!",
            "vigenere : LXFOPVEFRNHR | ATTACKATDAWN",
            "addition : 111111111011111111100 (21 chiffres, long max 9223372036854775807)",
            "factorielle 25 : 15511210043330985984000000 (6 zeros a la fin)",
            "prefixe commun : inter",
            "rotations : erbottlewat=true(3) cdab=true(2) acbd=false a=false",
            "occurrences de ana : 4 avec chevauchement, 2 sans",
            "plus long palindrome : geeksskeeg (10) verifie true",
            "|Java     strings     are|",
            "|immutable    so    every|",
            "|method   returns  a  new|",
            "|object  while  a builder|",
            "|changes itself          |",
            "sans casse : Apple apple banana Banana cherry date | naturel : Apple Banana apple banana cherry date",
            "romains : 4=IV 9=IX 14=XIV 1994=MCMXCIV 2026=MMXXVI 3999=MMMCMXCIX",
            "relus : XLII=42 MCMXC=1990 CDXLIV=444",
            "bases : 10=1010b/Ah 255=11111111b/FFh 2026=11111101010b/7EAh relu 2026 255");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.ANAGRAMS", "Data.PARAGRAPH", "new int[26]", ".charAt(",
            "- 'a'", "- '0'", ".setCharAt(", ".reverse()",
            ".insert(0, ", ".compareToIgnoreCase(", ".startsWith(", ".concat(",
            ".contains(", "String.join(", "Arrays.copyOfRange(", ".toCharArray()",
            "Integer.toBinaryString(", "Integer.toHexString(", "Integer.parseInt(", "String.valueOf(",
            ".repeat(", "re:indexOf\\(\\w+, \\w+ \\+ 1\\)##indexOf(texte, depart + 1) (chevauchement)", "re:\\(char\\) \\(##cast (char) sur un calcul",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "TextLab", args, EXPECTED, API);
    }
}
