package ch7_beyondclasses.projects.p02_poker.solution;

/**
 * SOLUTION - une carte : un record = des champs private final, un constructeur canonique,
 * des accesseurs rank() et suit(), et equals/hashCode/toString generes.
 */
public record Card(Rank rank, Suit suit) {

    // Methode static dans un record : une fabrique.
    public static Card parse(String text) {
        return new Card(Rank.of(text.charAt(0)), Suit.of(text.charAt(1)));
    }

    public static Card[] parseAll(String text) {
        String[] parts = text.split(" ");
        Card[] cards = new Card[parts.length];
        for (int i = 0; i < parts.length; i++) {
            cards[i] = parse(parts[i]);
        }
        return cards;
    }

    // On redefinit toString : "AS" plutot que "Card[rank=ACE, suit=SPADES]".
    @Override
    public String toString() {
        return "" + rank.symbol() + suit.symbol();
    }
}
