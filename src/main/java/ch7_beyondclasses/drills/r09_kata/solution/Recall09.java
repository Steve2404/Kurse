package ch7_beyondclasses.drills.r09_kata.solution;

/**
 * SOLUTION du drill de rappel 9 - kata mixte du chapitre 7.
 */
public class Recall09 {

    public static void main(String[] args) {
        Item book = new Item("livre", 1200, Tax.REDUCED);
        Item phone = new Item("telephone", 50000, Tax.NORMAL);
        System.out.println("D01 : " + book + " " + book.total() + " " + phone.total());
        System.out.println("D02 : " + Tax.valueOf("ZERO").apply(1000) + " " + Tax.NORMAL.ordinal() + " " + Tax.values().length + " " + Priced.currency());
        Discount promo = new Discount() {
            @Override
            public long apply(long price) {
                return price - price / 10;
            }
        };
        System.out.println("D03 : " + promo.apply(phone.total()) + " " + Discount.NONE.apply(500) + " " + new Cart().label());
        Payment pay = new Card("1234");
        String shown = pay instanceof Card c ? "carte " + c.last4() : "autre";
        System.out.println("D04 : " + shown + " " + new Cash(500).describe() + " " + Payment.class.isSealed());
        Cart cart = new Cart();
        Cart.Line line = cart.new Line(book, 3);
        System.out.println("D05 : " + line.subtotal() + " " + new Cart.Totals(2, 100).avg() + " " + book.equals(new Item("livre", 1200, Tax.REDUCED)));
    }
}

interface Priced {
    long price();

    default long total() {
        return price() + tax();
    }

    long tax();

    static String currency() {
        return "EUR";
    }
}

enum Tax {
    ZERO(0), REDUCED(55), NORMAL(200);

    private final int perMille;

    Tax(int perMille) {
        this.perMille = perMille;
    }

    long apply(long price) {
        return price * perMille / 1000;
    }
}

record Item(String name, long price, Tax rate) implements Priced {
    Item {
        name = name.toUpperCase();
    }

    @Override
    public long tax() {
        return rate.apply(price);
    }
}

interface Discount {
    Discount NONE = new Discount() {
        @Override
        public long apply(long price) {
            return price;
        }
    };

    long apply(long price);
}

sealed interface Payment permits Card, Cash {
    default String describe() {
        return getClass().getSimpleName();
    }
}

record Card(String last4) implements Payment {
}

record Cash(long amount) implements Payment {
    @Override
    public String describe() {
        return "especes " + amount;
    }
}

class Cart {
    private int lines;

    class Line {
        private final Item item;
        private final int qty;

        Line(Item item, int qty) {
            this.item = item;
            this.qty = qty;
            lines++;   // l'interne modifie un champ prive de son Cart
        }

        long subtotal() {
            return item.total() * qty;
        }
    }

    record Totals(int count, long sum) {
        long avg() {
            return sum / count;
        }
    }

    String label() {
        return "panier(" + lines + ")";
    }
}
