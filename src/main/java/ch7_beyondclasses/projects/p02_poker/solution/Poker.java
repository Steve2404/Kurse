package ch7_beyondclasses.projects.p02_poker.solution;

import ch7_beyondclasses.projects.p02_poker.Data;

/**
 * SOLUTION du projet 2 - le poker.
 */
public class Poker {

    // Generateur congruentiel lineaire : deterministe, donc la sortie est reproductible.
    private static long seed = Data.SEED;

    static int nextInt(int bound) {
        seed = (seed * 6364136223846793005L + 1442695040888963407L) & Long.MAX_VALUE;
        return (int) ((seed >>> 17) % bound);
    }

    // Meilleure main de 5 parmi 7 : les C(7,5) = 21 combinaisons, en choisissant les 2 cartes a EXCLURE.
    static Hand best(Card[] seven) {
        Hand best = null;
        for (int a = 0; a < seven.length; a++) {
            for (int b = a + 1; b < seven.length; b++) {
                Card[] five = new Card[5];
                int k = 0;
                for (int i = 0; i < seven.length; i++) {
                    if (i != a && i != b) {
                        five[k++] = seven[i];
                    }
                }
                Hand h = new Hand(five);
                if (best == null || h.compareTo(best) > 0) {
                    best = h;
                }
            }
        }
        return best;
    }

    public static void main(String[] args) {
        StringBuilder suits = new StringBuilder("couleurs :");
        for (Suit s : Suit.values()) {
            suits.append(' ').append(s.describe()).append(s.isRed() ? "(rouge)" : "(noire)");
        }
        System.out.println(suits);
        System.out.println("rangs : " + Rank.values().length + ", " + Rank.valueOf("QUEEN").ordinal() + " " + Rank.ACE.compareTo(Rank.KING) + " "
                + Rank.TWO.compareTo(Rank.ACE) + " " + Rank.of('T').name() + " " + Category.values()[5].label() + " " + (Suit.of('H') == Suit.HEARTS));

        for (String text : Data.HANDS) {
            System.out.println(new Hand(Card.parseAll(text)));
        }
        Hand wheel = new Hand(Card.parseAll("AC 2D 3H 4S 5C"));
        Hand six = new Hand(Card.parseAll("2C 3D 4H 5S 6C"));
        Hand pairKings = new Hand(Card.parseAll("KC KD 2H 3S 4C"));
        Hand pairKingsAce = new Hand(Card.parseAll("KH KS 2D 3C AC"));
        System.out.println("departage : roue < suite au 6 " + (wheel.compareTo(six) < 0) + ", paire de rois kicker As > kicker 4 " + (pairKingsAce.compareTo(pairKings) > 0)
                + ", record egal " + new Card(Rank.ACE, Suit.SPADES).equals(Card.parse("AS")));

        // Fisher-Yates : pour i de la fin vers le debut, echanger avec une position au hasard dans [0, i].
        Card[] deck = new Card[52];
        int n = 0;
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                deck[n++] = new Card(r, s);
            }
        }
        for (int i = deck.length - 1; i > 0; i--) {
            int j = nextInt(i + 1);
            Card t = deck[i];
            deck[i] = deck[j];
            deck[j] = t;
        }
        Hand winner = null;
        int winnerIndex = -1;
        for (int p = 0; p < Data.PLAYERS; p++) {
            Card[] five = new Card[5];
            for (int c = 0; c < 5; c++) {
                five[c] = deck[c * Data.PLAYERS + p];   // distribution une carte a la fois, en tournant
            }
            Hand h = new Hand(five);
            System.out.println("joueur " + (p + 1) + " : " + h);
            if (winner == null || h.compareTo(winner) > 0) {
                winner = h;
                winnerIndex = p + 1;
            }
        }
        System.out.println("gagnant : joueur " + winnerIndex + " avec " + winner.category().label());

        Card[] board = Card.parseAll(Data.BOARD);
        StringBuilder holdem = new StringBuilder("hold'em sur " + Data.BOARD + " :");
        int best = -1;
        Hand top = null;
        for (int p = 0; p < Data.HOLES.length; p++) {
            Card[] seven = new Card[7];
            System.arraycopy(board, 0, seven, 0, 5);
            System.arraycopy(Card.parseAll(Data.HOLES[p]), 0, seven, 5, 2);
            Hand h = best(seven);
            holdem.append(" [").append(Data.HOLES[p]).append(": ").append(h.category().label()).append(']');
            if (top == null || h.compareTo(top) > 0) {
                top = h;
                best = p + 1;
            }
        }
        System.out.println(holdem);
        System.out.println("gagnant hold'em : joueur " + best + " (" + top + ")");
    }
}
