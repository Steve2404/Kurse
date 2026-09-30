package ch7_beyondclasses.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise16_RecordModeling.
 */
public class Solution16_RecordModeling {

    public record OrderLine(String product, int qty, long unitCents) {
        public OrderLine {
            // Compact : on valide puis on transforme le PARAMETRE ; javac affecte les champs a la fin.
            if (product == null || product.isBlank()) {
                throw new IllegalArgumentException("produit obligatoire");
            }
            if (qty <= 0) {
                throw new IllegalArgumentException("quantite > 0");
            }
            product = product.strip();
        }

        public long totalCents() {
            // Un record peut avoir ses propres methodes, en lecture seule.
            return qty * unitCents;
        }
    }

    public record Order(String id, List<OrderLine> lines) {
        public Order {
            // Copie defensive : un record contenant une liste modifiable ne serait pas vraiment immuable.
            if (id == null || id.isBlank()) {
                throw new IllegalArgumentException("id obligatoire");
            }
            lines = List.copyOf(lines);
        }

        public static Order of(String id, OrderLine... lines) {
            // Fabrique static avec varargs : plus agreable a appeler que le constructeur.
            return new Order(id, List.of(lines));
        }

        public long totalCents() {
            long total = 0;
            for (OrderLine line : lines) {
                total += line.totalCents();
            }
            return total;
        }

        public Order withLine(OrderLine line) {
            // "Wither" : on rend une NOUVELLE commande, l'ancienne reste identique.
            List<OrderLine> copy = new ArrayList<>(lines);
            copy.add(line);
            return new Order(id, copy);
        }

        public int quantityOf(String product) {
            int total = 0;
            for (OrderLine line : lines) {
                if (line.product().equals(product)) {
                    total += line.qty();
                }
            }
            return total;
        }
    }
}
