package ch8_lambdas.projects.p07_orders.solution;

import ch8_lambdas.projects.p07_orders.Data;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.function.LongUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * SOLUTION - le moteur : toutes les regles metier sont des fonctions, assemblees a partir des donnees.
 */
public class Engine {

    private final String[] skus;
    private final long[] prices;
    private final int[] weights;
    private final Consumer<String> notify;

    public Engine(String[] catalog, Consumer<String> notify) {
        skus = new String[catalog.length];
        prices = new long[catalog.length];
        weights = new int[catalog.length];
        for (int i = 0; i < catalog.length; i++) {
            String[] p = catalog[i].split(" ");
            skus[i] = p[0];
            prices[i] = Long.parseLong(p[1]);
            weights[i] = Integer.parseInt(p[2]);
        }
        this.notify = notify;
    }

    int index(String sku) {
        for (int i = 0; i < skus.length; i++) {
            if (skus[i].equals(sku)) {
                return i;
            }
        }
        return -1;
    }

    // Une regle texte devient une Validation (il dit si la commande est VALIDE).
    Validation rule(String text) {
        String[] p = text.split(" ");
        return switch (p[0]) {
            case "not-empty" -> o -> o.lineCount() > 0;
            case "known-skus" -> o -> {
                for (int i = 0; i < o.lineCount(); i++) {
                    if (index(o.sku(i)) < 0) {
                        return false;
                    }
                }
                return true;
            };
            case "max-qty" -> {
                int max = Integer.parseInt(p[1]);
                ToIntFunction<Purchase> total = o -> {
                    int q = 0;
                    for (int i = 0; i < o.lineCount(); i++) {
                        q += o.quantity(i);
                    }
                    return q;
                };
                yield o -> total.applyAsInt(o) <= max;
            }
            default -> o -> true;
        };
    }

    // CURRYING : une fonction qui recoit un pourcentage et RENVOIE la fonction de remise correspondante.
    static final Function<Integer, LongUnaryOperator> PERCENT_OFF = percent -> amount -> amount - Math.round(amount * percent / 100.0);

    static LongUnaryOperator tierDiscount(String tier) {
        return switch (tier) {
            case "gold" -> PERCENT_OFF.apply(15);
            case "silver" -> PERCENT_OFF.apply(10);
            case "bronze" -> PERCENT_OFF.apply(5);
            default -> LongUnaryOperator.identity();
        };
    }

    // Le meilleur prix avec AU PLUS deux codes, essayes dans les DEUX ordres (la composition n'est pas commutative).
    static String bestPromos(long amount, Promo[] promos, long[] result) {
        long best = amount;
        String label = "aucun";
        for (int i = 0; i < promos.length; i++) {
            long single = promos[i].apply(amount);
            if (single < best) {
                best = single;
                label = promos[i].code();
            }
            for (int j = 0; j < promos.length; j++) {
                if (i != j && single < amount) {                                 // la paire n'a de sens que si le 1er code agit
                    LongUnaryOperator first = promos[i]::apply;              // reference sur un objet precis
                    LongUnaryOperator both = first.andThen(promos[j]::apply);
                    long pair = both.applyAsLong(amount);
                    if (pair < single && pair < best) {                       // ... et si le 2e agit aussi
                        best = pair;
                        label = promos[i].code() + " puis " + promos[j].code();
                    }
                }
            }
        }
        result[0] = best;
        return label;
    }

    public String process(Purchase o, Validation[] rules, String[] ruleNames, Promo[] promos) {
        for (int r = 0; r < rules.length; r++) {
            if (!rules[r].test(o)) {
                notify.accept(o.id() + " rejetee (" + ruleNames[r] + ")");
                return o.id() + " " + o.customer() + " : REJETEE (" + ruleNames[r] + ")";
            }
        }
        BiFunction<String, Integer, Long> lineTotal = (sku, qty) -> prices[index(sku)] * qty;
        ToLongFunction<Purchase> subtotal = p -> {
            long s = 0;
            for (int i = 0; i < p.lineCount(); i++) {
                s += lineTotal.apply(p.sku(i), p.quantity(i));
            }
            return s;
        };
        long gross = subtotal.applyAsLong(o);
        long afterTier = tierDiscount(o.tier()).applyAsLong(gross);
        long[] promo = new long[1];
        String codes = bestPromos(afterTier, promos, promo);
        // Livraison PARESSEUSE : le calcul du poids n'a lieu que si la livraison n'est pas offerte.
        LongSupplier shipping = () -> {
            int grams = 0;
            for (int i = 0; i < o.lineCount(); i++) {
                grams += weights[index(o.sku(i))] * o.quantity(i);
            }
            return 490 + 100L * ((grams + 999) / 1000);
        };
        long ship = promo[0] >= 5000 ? 0 : shipping.getAsLong();
        long total = promo[0] + ship;
        notify.accept(o.id() + " acceptee " + money(total));
        return o.id() + " " + o.customer() + " : " + money(gross) + " -" + o.tier() + "-> " + money(afterTier) + " -[" + codes + "]-> " + money(promo[0])
                + " + port " + money(ship) + " = " + money(total);
    }

    static String money(long cents) {
        return cents / 100 + "." + (cents % 100 < 10 ? "0" : "") + cents % 100;
    }

    public static Validation[] rules(Engine e, String[] texts) {
        Validation[] r = new Validation[texts.length];
        for (int i = 0; i < texts.length; i++) {
            r[i] = e.rule(texts[i]);
        }
        return r;
    }

    public static void main(String[] args) {
        StringBuilder audit = new StringBuilder();
        int[] accepted = {0};
        Consumer<String> log = audit::append;                                // reference sur un objet precis
        Consumer<String> separator = s -> audit.append(" ; ");
        Consumer<String> counter = s -> {
            if (s.contains("acceptee")) {
                accepted[0]++;
            }
        };
        Engine engine = new Engine(Data.CATALOG, log.andThen(separator).andThen(counter));
        Validation[] rules = rules(engine, Data.RULES);
        Promo[] promos = new Promo[Data.PROMOS.length];
        for (int i = 0; i < promos.length; i++) {
            promos[i] = Promo.parse(Data.PROMOS[i]);
        }
        for (String line : Data.PURCHASES) {
            System.out.println(engine.process(Purchase.parse(line), rules, Data.RULES, promos));
        }
        System.out.println("acceptees " + accepted[0] + "/" + Data.PURCHASES.length + " | journal : " + audit.toString().strip());
        LongUnaryOperator gold = tierDiscount("gold");
        LongUnaryOperator ten = PERCENT_OFF.apply(10);
        LongUnaryOperator minus500 = amount -> amount - 500;
        System.out.println("currying : or(10000) " + gold.applyAsLong(10000) + ", -10% puis -5.00 " + ten.andThen(minus500).applyAsLong(10000) + ", -5.00 puis -10% "
                + ten.compose(minus500).applyAsLong(10000) + ", identite " + tierDiscount("none").applyAsLong(10000));
    }
}
