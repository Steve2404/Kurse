package ch7_beyondclasses.solutions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Corrige de l'exercice 24. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise24_ShopCapstone.
 */
public class Solution24_ShopCapstone {

    public enum Category {
        BOOK(5), FOOD(10), TECH(20);

        private final int vatPercent;

        Category(int vatPercent) {
            this.vatPercent = vatPercent;
        }

        public long withVat(long cents) {
            // Une methode d'instance d'enum lit le champ de SA constante.
            return cents + cents * vatPercent / 100;
        }
    }

    public sealed interface Product permits Book, Food, Gadget {
        long cents();

        Category category();
    }

    public record Book(String title, long cents) implements Product {
        @Override
        public Category category() {
            // Chaque record fournit la methode abstraite de l'interface.
            return Category.BOOK;
        }
    }

    public record Food(String name, long cents, boolean fresh) implements Product {
        @Override
        public Category category() {
            return Category.FOOD;
        }
    }

    public record Gadget(String name, long cents) implements Product {
        @Override
        public Category category() {
            return Category.TECH;
        }
    }

    public interface Discount {
        long apply(long cents);

        static Discount percent(int p) {
            // Fabrique static : une lambda implemente l'unique methode abstraite.
            return cents -> cents - cents * p / 100;
        }

        default Discount then(Discount next) {
            // this d'abord, puis next sur le resultat.
            return cents -> next.apply(this.apply(cents));
        }

        static Discount none() {
            return cents -> cents;
        }
    }

    public static class Cart {
        private final List<Product> products = new ArrayList<>();

        public Cart add(Product product) {
            products.add(product);
            return this;
        }

        public static String describe(Product product) {
            // sealed : ces 3 cas sont les seuls possibles.
            if (product instanceof Book b) {
                return "livre " + b.title();
            }
            if (product instanceof Food f) {
                return (f.fresh() ? "frais : " : "sec : ") + f.name();
            }
            if (product instanceof Gadget g) {
                return "gadget " + g.name();
            }
            throw new IllegalStateException("produit inconnu : " + product);
        }

        public long total(Discount discount) {
            // Chaque produit avec la TVA de sa categorie, puis la remise sur le total.
            long sum = 0;
            for (Product p : products) {
                sum += p.category().withVat(p.cents());
            }
            return discount.apply(sum);
        }

        public Map<Category, Integer> countByCategory() {
            // EnumMap : ordre de declaration des constantes.
            Map<Category, Integer> counts = new EnumMap<>(Category.class);
            for (Product p : products) {
                counts.merge(p.category(), 1, Integer::sum);
            }
            return counts;
        }

        public List<Product> cheapestFirst() {
            // Une copie triee : le panier garde son ordre d'ajout.
            List<Product> copy = new ArrayList<>(products);
            copy.sort(Comparator.comparingLong(Product::cents));
            return copy;
        }
    }
}
