package lambdas.solutions;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise08_ConsumerOperators.
 */
public class Solution08_ConsumerOperators {

    public static Consumer<String> buildNotifier(List<String> emailLog, List<String> smsLog) {
        Consumer<String> toEmail = emailLog::add;
        Consumer<String> toSms = smsLog::add;
        return toEmail.andThen(toSms);
    }

    public static BiConsumer<Map<String, Integer>, String> buildStockUpdater() {
        BiConsumer<Map<String, Integer>, String> decrement = (stock, item) -> stock.merge(item, -1, Integer::sum);
        BiConsumer<Map<String, Integer>, String> removeIfEmpty = (stock, item) -> {
            if (stock.get(item) <= 0) {
                stock.remove(item);
            }
        };
        return decrement.andThen(removeIfEmpty);
    }

    public static UnaryOperator<Double> combineDiscounts(List<UnaryOperator<Double>> discounts) {
        return price -> {
            double result = price;
            for (UnaryOperator<Double> discount : discounts) {
                result = discount.apply(result);
            }
            return result;
        };
    }

    public static double bestOffer(List<Double> offers, BinaryOperator<Double> betterOf) {
        double best = offers.get(0);
        for (int i = 1; i < offers.size(); i++) {
            best = betterOf.apply(best, offers.get(i));
        }
        return best;
    }
}