package ch7_beyondclasses.projects.p03_turtle;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON TurtleApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "programme : 14 commandes SQUARE PenUp Move PenDown Repeat PenUp Turn Move Turn Move Turn Turn PenDown STAIRS",
            "deplie : 57 commandes elementaires, imbrication 2",
            "records : Move[steps=3] Move[steps=0] Turn[quarters=1] PenUp[] egal true ; Repeat.equals compare les tableaux par reference : false",
            "|",
            "| ##### ###",
            "| #   #   ###",
            "| #   #     ###",
            "| #   #       ####",
            "| #####          ###",
            "|                  ###",
            "|                    ##",
            "|",
            "| ###",
            "|   ###",
            "|     ###",
            "|       #",
            "position (12,7) cap EAST, distance 73, virages 26");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.PROGRAM", "Data.ROWS", "sealed interface Command permits", "non-sealed abstract class Macro",
            "2xextends Macro", "record Move(", "record Turn(", "record Repeat(",
            "record PenUp()", "record PenDown()", "public Move {", "enum Direction",
            "instanceof Repeat r", "instanceof Macro", "yield ",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "TurtleApp", args, EXPECTED, API);
    }
}
