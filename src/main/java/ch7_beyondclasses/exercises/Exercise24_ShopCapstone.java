package ch7_beyondclasses.exercises;

import ch7_beyondclasses.ExerciseChecker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * EXERCICE 24 (CAPSTONE) - Une boutique : enum avec champs, interface sealed + records, interface a methodes default/static, classe imbriquee (niveau : capstone)
 * ======================================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InterfaceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une petite boutique vend trois sortes de produits, et SEULEMENT
 * trois : des livres, de la nourriture, des gadgets (interface sealed
 * Product, trois records). Chaque produit a une Category (un enum avec
 * son taux de TVA). Des remises (interface Discount) se fabriquent et
 * se combinent. Un panier (classe imbriquee static Cart) calcule le
 * total, compte par categorie (EnumMap) et trie les produits.
 *
 *   Category : BOOK 5 %, FOOD 10 %, TECH 20 %
 *   Book(title, cents) -> BOOK ; Food(name, cents, fresh) -> FOOD ; Gadget(name, cents) -> TECH
 *
 *
 * ==================================================================
 * TODO 1 : Book.category()    TODO 2 : Food.category()    TODO 3 : Gadget.category()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Chaque record rend SA categorie.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : Category.withVat(cents)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   TECH.withVat(1000) -> 1000 + 20 % = 1200 ; FOOD.withVat(333) -> 333 + 33 = 366 (division entiere)
 *
 * -- Le plan --
 *
 *   1. Rendre cents + cents * vatPercent / 100.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : Discount.percent(p)    TODO 6 : Discount.then(next)    TODO 7 : Discount.none()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une remise transforme un prix en un autre (sa seule methode abstract :
 * apply). percent(10) enleve 10 %. a.then(b) applique a, PUIS b sur le
 * resultat. none() ne change rien.
 *
 * -- Essayons a la main --
 *
 *   percent(10).apply(1000) -> 900 ; percent(10).then(percent(50)).apply(1000) -> 450 ; none().apply(7) -> 7
 *
 * -- Le plan --
 *
 *   1. percent : cents -> cents - cents * p / 100.
 *   2. then (default) : cents -> next.apply(this.apply(cents)).
 *   3. none (static) : cents -> cents.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 8 : Cart.describe(product)    [pattern matching sur le sealed]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   Book("Java", 3000)            -> "livre Java"
 *   Food("pain", 250, true)       -> "frais : pain"   (fresh faux -> "sec : pain")
 *   Gadget("cle USB", 1500)       -> "gadget cle USB"
 *
 * -- Le plan --
 *
 *   1. instanceof Book b / Food f / Gadget g ; un dernier throw (jamais atteint grace a sealed).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 9 : Cart.total(discount)    TODO 10 : Cart.countByCategory()    TODO 11 : Cart.cheapestFirst()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   panier [Book 3000, Food 250, Gadget 1500]
 *   total(none())         -> 3150 + 275 + 1800 = 5225   (chaque prix avec SA TVA)
 *   total(percent(10))    -> 5225 - 522 = 4703          (la remise sur le total TTC)
 *   countByCategory()     -> {BOOK=1, FOOD=1, TECH=1}
 *   cheapestFirst()       -> [Food pain, Gadget cle USB, Book Java]  (par prix hors taxe croissant)
 *
 * -- Le plan --
 *
 *   1. total : somme de p.category().withVat(p.cents()) ; puis discount.apply(somme).
 *   2. countByCategory : new EnumMap<>(Category.class) et merge(..., 1, Integer::sum).
 *   3. cheapestFirst : copie de la liste, triee par Comparator.comparingLong(Product::cents).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : withVat (TODO 4) et apply des remises (TODO 5 a 7).
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Un record qui implemente une interface doit fournir ses methodes abstraites (ici category()).
 *   - Product::cents fonctionne car chaque record a un accesseur cents() et l'interface le declare.
 */
public class Exercise24_ShopCapstone {

    public enum Category {
        BOOK(5), FOOD(10), TECH(20);

        private final int vatPercent;

        Category(int vatPercent) {
            this.vatPercent = vatPercent;
        }

        public long withVat(long cents) {
            throw new UnsupportedOperationException("TODO 4 : implementer withVat()");
        }
    }

    public sealed interface Product permits Book, Food, Gadget {
        long cents();

        Category category();
    }

    public record Book(String title, long cents) implements Product {
        @Override
        public Category category() {
            throw new UnsupportedOperationException("TODO 1 : implementer Book.category()");
        }
    }

    public record Food(String name, long cents, boolean fresh) implements Product {
        @Override
        public Category category() {
            throw new UnsupportedOperationException("TODO 2 : implementer Food.category()");
        }
    }

    public record Gadget(String name, long cents) implements Product {
        @Override
        public Category category() {
            throw new UnsupportedOperationException("TODO 3 : implementer Gadget.category()");
        }
    }

    public interface Discount {
        long apply(long cents);

        static Discount percent(int p) {
            throw new UnsupportedOperationException("TODO 5 : implementer percent()");
        }

        default Discount then(Discount next) {
            throw new UnsupportedOperationException("TODO 6 : implementer then()");
        }

        static Discount none() {
            throw new UnsupportedOperationException("TODO 7 : implementer none()");
        }
    }

    public static class Cart {
        private final List<Product> products = new ArrayList<>();

        public Cart add(Product product) {
            products.add(product);
            return this;
        }

        public static String describe(Product product) {
            throw new UnsupportedOperationException("TODO 8 : implementer describe()");
        }

        public long total(Discount discount) {
            throw new UnsupportedOperationException("TODO 9 : implementer total()");
        }

        public Map<Category, Integer> countByCategory() {
            throw new UnsupportedOperationException("TODO 10 : implementer countByCategory()");
        }

        public List<Product> cheapestFirst() {
            throw new UnsupportedOperationException("TODO 11 : implementer cheapestFirst()");
        }
    }

    public static void main(String[] args) {
        Product book = new Book("Java", 3000);
        Product bread = new Food("pain", 250, true);
        Product key = new Gadget("cle USB", 1500);
        ExerciseChecker.check("category : BOOK, FOOD, TECH",
                book.category() == Category.BOOK && bread.category() == Category.FOOD && key.category() == Category.TECH);
        ExerciseChecker.check("withVat : 1200 et 366", Category.TECH.withVat(1000) == 1200 && Category.FOOD.withVat(333) == 366);
        ExerciseChecker.check("Discount : percent, then, none",
                Discount.percent(10).apply(1000) == 900 && Discount.percent(10).then(Discount.percent(50)).apply(1000) == 450
                        && Discount.none().apply(7) == 7);
        ExerciseChecker.check("describe : livre, frais, sec, gadget",
                Cart.describe(book).equals("livre Java") && Cart.describe(bread).equals("frais : pain")
                        && Cart.describe(new Food("riz", 100, false)).equals("sec : riz") && Cart.describe(key).equals("gadget cle USB"));
        Cart cart = new Cart().add(book).add(bread).add(key);
        ExerciseChecker.check("total : 5225 sans remise, 4703 avec 10 %",
                cart.total(Discount.none()) == 5225 && cart.total(Discount.percent(10)) == 4703);
        ExerciseChecker.check("countByCategory == {BOOK=1, FOOD=1, TECH=1}", cart.countByCategory().toString().equals("{BOOK=1, FOOD=1, TECH=1}"));
        ExerciseChecker.check("cheapestFirst : pain, cle USB, Java", cart.cheapestFirst().equals(List.of(bread, key, book)));

        ExerciseChecker.summary();
    }
}
