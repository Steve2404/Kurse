package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 16 - Modeliser des commandes avec des records : validation, copie defensive, "withers", fabrique (niveau : avance)
 * =========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une boutique enregistre des commandes. Chaque ligne de commande est
 * un record OrderLine(product, qty, unitCents) ; une commande est un
 * record Order(id, lines). Les records sont immuables... sauf si un
 * composant est une LISTE que l'appelant garde et modifie ! Le
 * constructeur compact sert donc a VALIDER, NETTOYER et COPIER.
 *
 *
 * ==================================================================
 * TODO 1 : OrderLine (constructeur compact)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   new OrderLine("  pomme ", 3, 50) -> product "pomme" (nettoye), qty 3
 *   new OrderLine("", 1, 50)         -> IllegalArgumentException("produit obligatoire")
 *   new OrderLine("pomme", 0, 50)    -> IllegalArgumentException("quantite > 0")
 *
 * -- Le plan --
 *
 *   1. product null ou blank -> exception ; qty <= 0 -> exception.
 *   2. product = product.strip() (on modifie le PARAMETRE ; javac l'affecte au champ ensuite).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : OrderLine.totalCents()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Rendre qty * unitCents (qty() et unitCents() existent, ou directement les champs).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : Order (constructeur compact)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On recopie la liste recue (List.copyOf) : si l'appelant la modifie
 * plus tard, la commande ne change pas, et lines() rend une liste non
 * modifiable.
 *
 * -- Le plan --
 *
 *   1. id blank -> IllegalArgumentException("id obligatoire").
 *   2. lines = List.copyOf(lines).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : Order.of(id, lines...)    TODO 5 : Order.totalCents()    TODO 6 : Order.withLine(line)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. of : fabrique static avec varargs -> new Order(id, List.of(lines)).
 *   2. totalCents : somme des totalCents() des lignes.
 *   3. withLine : copie modifiable des lignes, ajout, puis new Order(id, copie) : l'ancienne commande ne change pas.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : totalCents de OrderLine (TODO 2).
 *
 *
 * ==================================================================
 * TODO 7 : Order.quantityOf(product)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   commande [pomme x3, poire x2, pomme x1] -> quantityOf("pomme") == 4
 *
 * -- Le plan --
 *
 *   1. Somme des qty des lignes dont product est egal au nom demande.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Dans un constructeur compact, on n'ecrit JAMAIS this.x = ... (erreur de compilation).
 *   - Deux records avec les memes composants sont equals, et toString() est genere : OrderLine[product=pomme, qty=3, unitCents=50].
 */
public class Exercise16_RecordModeling {

    public record OrderLine(String product, int qty, long unitCents) {
        public OrderLine {
            throw new UnsupportedOperationException("TODO 1 : implementer le constructeur compact de OrderLine");
        }

        public long totalCents() {
            throw new UnsupportedOperationException("TODO 2 : implementer totalCents()");
        }
    }

    public record Order(String id, List<OrderLine> lines) {
        public Order {
            throw new UnsupportedOperationException("TODO 3 : implementer le constructeur compact de Order");
        }

        public static Order of(String id, OrderLine... lines) {
            throw new UnsupportedOperationException("TODO 4 : implementer of()");
        }

        public long totalCents() {
            throw new UnsupportedOperationException("TODO 5 : implementer Order.totalCents()");
        }

        public Order withLine(OrderLine line) {
            throw new UnsupportedOperationException("TODO 6 : implementer withLine()");
        }

        public int quantityOf(String product) {
            throw new UnsupportedOperationException("TODO 7 : implementer quantityOf()");
        }
    }

    public static void main(String[] args) {
        OrderLine apples = new OrderLine("  pomme ", 3, 50);
        ExerciseChecker.check("OrderLine : produit nettoye, toString genere",
                apples.product().equals("pomme") && apples.toString().equals("OrderLine[product=pomme, qty=3, unitCents=50]"));
        ExerciseChecker.check("OrderLine refuse produit vide et quantite 0",
                "produit obligatoire".equals(errorOf(() -> new OrderLine("", 1, 50)))
                        && "quantite > 0".equals(errorOf(() -> new OrderLine("pomme", 0, 50))));
        ExerciseChecker.check("totalCents de la ligne == 150", apples.totalCents() == 150);

        List<OrderLine> source = new ArrayList<>(List.of(apples));
        Order order = new Order("A-1", source);
        source.add(new OrderLine("fraude", 99, 1));
        ExerciseChecker.check("Order copie la liste : la modifier dehors ne change rien", order.lines().size() == 1);
        ExerciseChecker.check("Order refuse un id vide", "id obligatoire".equals(errorOf(() -> new Order(" ", List.of()))));

        Order full = Order.of("A-2", apples, new OrderLine("poire", 2, 80), new OrderLine("pomme", 1, 50));
        ExerciseChecker.check("of + totalCents == 150 + 160 + 50 = 360", full.totalCents() == 360);
        Order bigger = full.withLine(new OrderLine("kiwi", 1, 40));
        ExerciseChecker.check("withLine : nouvelle commande (4 lignes), l'ancienne en garde 3",
                bigger.lines().size() == 4 && full.lines().size() == 3 && bigger.totalCents() == 400);
        ExerciseChecker.check("quantityOf(\"pomme\") == 4", full.quantityOf("pomme") == 4);
        ExerciseChecker.check("records egaux : memes composants", Order.of("A-3", apples).equals(Order.of("A-3", new OrderLine("pomme", 3, 50))));

        ExerciseChecker.summary();
    }

    // Deja ecrit : le message de l'IllegalArgumentException lancee, ou null.
    private static String errorOf(Runnable action) {
        try {
            action.run();
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
