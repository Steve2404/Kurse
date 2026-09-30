package ch8_lambdas.solutions;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise09_ConsumerOperators.
 */
public class Solution09_ConsumerOperators {

    public static Consumer<String> buildNotifier(List<String> emailLog, List<String> smsLog) {
        // andThen enchaine deux Consumer : les deux effets se produisent, dans l'ordre.
        Consumer<String> toEmail = emailLog::add;
        Consumer<String> toSms = smsLog::add;
        return toEmail.andThen(toSms);
    }

    public static BiConsumer<Map<String, Integer>, String> buildStockUpdater() {
        // BiConsumer : deux entrees, aucun resultat, un effet sur la Map.
        BiConsumer<Map<String, Integer>, String> decrement = (stock, item) -> stock.merge(item, -1, Integer::sum);
        BiConsumer<Map<String, Integer>, String> removeIfEmpty = (stock, item) -> {
            if (stock.get(item) <= 0) {
                stock.remove(item);
            }
        };
        return decrement.andThen(removeIfEmpty);
    }

    public static UnaryOperator<Double> combineDiscounts(List<UnaryOperator<Double>> discounts) {
        // On part de l'identite et on enchaine chaque remise.
        return price -> {
            double result = price;
            for (UnaryOperator<Double> discount : discounts) {
                result = discount.apply(result);
            }
            return result;
        };
    }

    public static double bestOffer(List<Double> offers, BinaryOperator<Double> betterOf) {
        // BinaryOperator : deux valeurs du meme type -> une valeur de ce type.
        double best = offers.get(0);
        for (int i = 1; i < offers.size(); i++) {
            best = betterOf.apply(best, offers.get(i));
        }
        return best;
    }
}