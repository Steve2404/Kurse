package ch18_design.drills.r02_strategies.solution;

import java.time.LocalDate;
import java.util.List;

/** Une promotion : la remise (en centimes) sur ces prix, ce jour-la. Une seule methode : une lambda suffit. */
@FunctionalInterface
public interface Promotion {

    long discount(List<Long> prices, LocalDate today);
}
