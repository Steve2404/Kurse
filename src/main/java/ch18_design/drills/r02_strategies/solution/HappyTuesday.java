package ch18_design.drills.r02_strategies.solution;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/** Le mardi, 10 % du total, arrondi au centime le plus proche. */
public final class HappyTuesday implements Promotion {

    @Override
    public long discount(List<Long> prices, LocalDate today) {
        long total = prices.stream().mapToLong(Long::longValue).sum();
        return today.getDayOfWeek() == DayOfWeek.TUESDAY ? (total * 10 + 50) / 100 : 0;
    }
}
