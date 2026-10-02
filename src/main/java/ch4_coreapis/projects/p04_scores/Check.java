package ch4_coreapis.projects.p04_scores;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Scores, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "NOTES A : [72, 85, 58, 91, 64, 85, 47, 78] (8 copies)",
            "TRIEES : [47, 58, 64, 72, 78, 85, 85, 91], original intact : [72, 85, 58, 91, 64, 85, 47, 78]",
            "MIN 47, MAX 91, MOYENNE 72.5, MEDIANE 75.0, ECART-TYPE 14.22",
            "ARRONDIS de la moyenne : round 73, ceil 73.0, floor 72.0, round(-2.5) -2, round(2.5f) 3, abs(-7) 7",
            "RECHERCHE 78 : main 4 / Arrays 4 | 60 : -3 / -3 | 10 : -1 / -1",
            "FUSION A+B : [47, 50, 58, 64, 66, 70, 72, 78, 85, 85, 88, 91, 95], mediane 72.0",
            "ROTATION de 2 : [88, 95, 50, 66, 70]",
            "COMPARE : equals true, == false, compare(B, plus long) -1, compare(A trie, B) -1, mismatch(B, rotation) 0, mismatch(B, copie) -1",
            "NOMS TRIES : [Bob, Hugo, Zoe, adam, eva, ines, lea, tom] (majuscules avant minuscules)",
            "PODIUM : 1. Zoe 91 (+5, 3 bonus possibles) 2. Hugo/Bob 85 (+3, 2 bonus possibles) 3. Hugo/Bob 85 (+1, 1 bonus possibles)",
            "DEFAUTS : [0, 0, 0] [null, null] [null, null], fill [7, 7, 7], Math.pow(2, 10) 1024.0, min(-0.0, 0.0) -0.0");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Arrays.copyOf(", "Arrays.sort(", "Arrays.binarySearch(", "Arrays.equals(",
            "Arrays.compare(", "Arrays.mismatch(", "Arrays.fill(", "Arrays.toString(",
            "Math.min(", "Math.max(", "Math.pow(", "Math.sqrt(",
            "Math.round(", "Math.ceil(", "Math.floor(", "Math.abs(",
            "re:\\[\\]\\[\\]##tableau 2D", "re:\\[\\w+\\]\\[0\\]##tableau irregulier [i][0]", "re:-\\(\\w+ \\+ 1\\)##convention -(point d’insertion) - 1", "re:new int\\[\\w+\\.length \\+ \\w+\\.length\\]##fusion dans un nouveau tableau",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Scores", args, EXPECTED, API);
    }
}
