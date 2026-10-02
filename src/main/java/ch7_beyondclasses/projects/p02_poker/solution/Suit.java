package ch7_beyondclasses.projects.p02_poker.solution;

/**
 * SOLUTION - les couleurs : un enum avec champs, constructeur (implicitement private) et methodes.
 */
public enum Suit implements Symbolic {
    CLUBS('C', false), DIAMONDS('D', true), HEARTS('H', true), SPADES('S', false);   // le ; est obligatoire avant les membres

    private final char symbol;
    private final boolean red;

    Suit(char symbol, boolean red) {
        this.symbol = symbol;
        this.red = red;
    }

    @Override
    public char symbol() {
        return symbol;
    }

    public boolean isRed() {
        return red;
    }

    // values() rend un NOUVEAU tableau des constantes, dans l'ordre de declaration.
    public static Suit of(char c) {
        for (Suit s : values()) {
            if (s.symbol == c) {
                return s;
            }
        }
        return null;
    }
}
