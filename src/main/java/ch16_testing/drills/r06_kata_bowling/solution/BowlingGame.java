package ch16_testing.drills.r06_kata_bowling.solution;

import java.util.ArrayList;
import java.util.List;

/** Le kata du bowling (Robert C. Martin), avec la validation des lancers en plus. */
public final class BowlingGame {

    private final List<Integer> rolls = new ArrayList<>();
    private int frame = 1;
    private int ballInFrame = 0;
    private int pinsStanding = 10;
    private int tenthBalls = 2;

    // Pourquoi suivre les quilles DEBOUT : un 2e lancer ne peut pas abattre plus que ce qui reste.
    // Piege : au 10e carreau, un strike ou une reserve redresse les 10 quilles et donne une boule de plus.
    public void roll(int pins) {
        if (frame > 10) {
            throw new IllegalStateException("partie terminee");
        }
        if (pins < 0 || pins > pinsStanding) {
            throw new IllegalArgumentException("quilles invalides : " + pins);
        }
        rolls.add(pins);
        pinsStanding -= pins;
        ballInFrame++;
        if (frame < 10) {
            if (pinsStanding == 0 || ballInFrame == 2) {
                frame++;
                ballInFrame = 0;
                pinsStanding = 10;
            }
        } else {
            if (pinsStanding == 0 && ballInFrame < 3) {
                tenthBalls = 3;
                pinsStanding = 10;
            }
            if (ballInFrame == tenthBalls) {
                frame = 11;
            }
        }
    }

    public int score() {
        if (frame <= 10) {
            throw new IllegalStateException("partie incomplete");
        }
        int score = 0;
        int i = 0;
        for (int f = 0; f < 10; f++) {
            if (rolls.get(i) == 10) {
                score += 10 + rolls.get(i + 1) + rolls.get(i + 2);
                i += 1;
            } else if (rolls.get(i) + rolls.get(i + 1) == 10) {
                score += 10 + rolls.get(i + 2);
                i += 2;
            } else {
                score += rolls.get(i) + rolls.get(i + 1);
                i += 2;
            }
        }
        return score;
    }
}
