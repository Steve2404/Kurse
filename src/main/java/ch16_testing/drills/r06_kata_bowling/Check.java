package ch16_testing.drills.r06_kata_bowling;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6, le kata final (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON BowlingGame et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("BowlingGame.java", "score += 10 + rolls.get(i + 1) + rolls.get(i + 2);", "score += 10 + rolls.get(i + 1);"),
            new Mutant("BowlingGame.java", "score += 10 + rolls.get(i + 2);", "score += 10;"),
            new Mutant("BowlingGame.java", "if (pinsStanding == 0 || ballInFrame == 2) {", "if (ballInFrame == 2) {"),
            new Mutant("BowlingGame.java", "tenthBalls = 3;", "tenthBalls = 2;"),
            new Mutant("BowlingGame.java", "pins > pinsStanding", "pins > 10"),
            new Mutant("BowlingGame.java", "if (frame > 10) {", "if (frame > 11) {"),
            new Mutant("BowlingGame.java", "if (frame <= 10) {", "if (frame < 10) {"),
            new Mutant("BowlingGame.java", "pins < 0 ||", "pins < -1 ||"),
            new Mutant("BowlingGame.java", "tenthBalls = 3;\n                pinsStanding = 10;", "tenthBalls = 3;"),
            new Mutant("BowlingGame.java", "                i += 1;", "                i += 2;"));

    static final List<String> API_CODE = List.of(
            "final class BowlingGame", "void roll(int pins)", "int score()", "IllegalStateException(", "IllegalArgumentException(");

    static final List<String> API_TESTS = List.of(
            "@BeforeEach", "@Test", "@ParameterizedTest", "assertEquals(", "assertThrows(", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 12, MUTANTS, API_CODE, API_TESTS);
    }
}
