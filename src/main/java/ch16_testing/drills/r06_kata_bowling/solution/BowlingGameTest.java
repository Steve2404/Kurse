package ch16_testing.drills.r06_kata_bowling.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Les tests du kata, dans l'ordre ou ils ont ete ecrits. */
class BowlingGameTest {

    private BowlingGame game;

    @BeforeEach
    void newGame() {
        game = new BowlingGame();
    }

    private void rollMany(int times, int pins) {
        for (int i = 0; i < times; i++) {
            game.roll(pins);
        }
    }

    @Test
    void gutterGame() {
        rollMany(20, 0);
        assertEquals(0, game.score());
    }

    @Test
    void allOnes() {
        rollMany(20, 1);
        assertEquals(20, game.score());
    }

    @Test
    void oneSpare() {
        game.roll(5);
        game.roll(5);
        game.roll(3);
        rollMany(17, 0);
        assertEquals(16, game.score());
    }

    @Test
    void oneStrike() {
        game.roll(10);
        game.roll(3);
        game.roll(4);
        rollMany(16, 0);
        assertEquals(24, game.score());
    }

    @Test
    void perfectGame() {
        rollMany(12, 10);
        assertEquals(300, game.score());
    }

    @Test
    void allSpares() {
        rollMany(21, 5);
        assertEquals(150, game.score());
    }

    @Test
    void spareInTheTenthFrameGivesOneBonusBall() {
        rollMany(18, 0);
        game.roll(7);
        game.roll(3);
        game.roll(4);
        assertEquals(14, game.score());
    }

    @Test
    void openTenthFrameEndsTheGame() {
        rollMany(18, 0);
        game.roll(7);
        game.roll(2);
        assertEquals(9, game.score());
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> game.roll(1));
        assertEquals("partie terminee", e.getMessage());
    }

    @Test
    void strikeInTheTenthThenTwoBonusBallsThatCannotExceedTen() {
        rollMany(18, 0);
        game.roll(10);
        game.roll(7);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> game.roll(4));
        assertEquals("quilles invalides : 4", e.getMessage());
        game.roll(3);
        assertEquals(20, game.score());
    }

    @ParameterizedTest(name = "{0} quilles refusees")
    @ValueSource(ints = {-1, 11})
    void impossibleRolls(int pins) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> game.roll(pins));
        assertEquals("quilles invalides : " + pins, e.getMessage());
    }

    @Test
    void aFrameCannotKnockMoreThanTenPins() {
        game.roll(5);
        assertThrows(IllegalArgumentException.class, () -> game.roll(6));
    }

    @Test
    void scoreOfAnUnfinishedGameIsRefused() {
        rollMany(19, 1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> game.score());
        assertEquals("partie incomplete", e.getMessage());
    }
}
