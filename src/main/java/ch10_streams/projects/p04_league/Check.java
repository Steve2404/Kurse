package ch10_streams.projects.p04_league;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON League, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "RESULTATS : Lions 3-1 Tigres | Ours 0-0 Aigles | Aigles 2-2 Lions | Tigres 2-0 Ours | Lions 5-0 Ours | Tigres 1-2 Aigles | Tigres 2-1 Lions | Aigles 3-1 Ours | Ours 1-1 Lions | Aigles 2-1 Tigres | Lions 2-0 Aigles | Ours 0-3 Tigres",
            "BUTS : 35 en 12 matchs, moyenne 2.92",
            "PLUS LARGE VICTOIRE : J3 Lions 5-0 Ours (ecart 5)",
            "CLASSEMENT",
            "1. Lions 11 pts (G3 N2 P1) 14:6 +8",
            "2. Aigles 11 pts (G3 N2 P1) 9:7 +2",
            "3. Tigres 9 pts (G3 N0 P3) 10:8 +2",
            "4. Ours 2 pts (G0 N2 P4) 2:14 -12",
            "BILAN Lions (reduce) : 6 matchs, 11 pts, identique au classement : oui",
            "SERIES SANS DEFAITE : Aigles 5, Lions 3, Ours 1, Tigres 1",
            "CONTROLE : 33 points distribues = 9 victoires x 3 + 3 nuls x 2 : oui",
            "PARALLELE : resultats oui, buts oui, classement oui, series oui");
            // EXPECTED-END

    static final List<String> API = List.of(
            "5x.reduce(", ".collect(", "StringBuilder::new", "Collector.of(", "Collector<",
            ".parallelStream()", "!Collectors.");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "League", args, EXPECTED, API);
    }
}
