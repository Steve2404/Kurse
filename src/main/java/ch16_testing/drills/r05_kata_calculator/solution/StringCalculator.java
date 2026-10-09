package ch16_testing.drills.r05_kata_calculator.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** Le kata de la calculatrice de chaines (Roy Osherove), construit par TDD. */
public final class StringCalculator {

    private StringCalculator() {
    }

    // Piege : un separateur personnalise comme "." ou "|" est un caractere SPECIAL dans une expression reguliere ;
    // Pattern.quote le rend litteral.
    public static int add(String numbers) {
        Objects.requireNonNull(numbers, "chaine absente");
        if (numbers.isEmpty()) {
            return 0;
        }
        String separators = "[,\n]";
        String body = numbers;
        if (numbers.startsWith("//")) {
            separators = Pattern.quote(numbers.substring(2, 3)) + "|\n";
            body = numbers.substring(4);
        }
        List<Integer> negatives = new ArrayList<>();
        int sum = 0;
        for (String part : body.split(separators)) {
            int n = Integer.parseInt(part.strip());
            if (n < 0) {
                negatives.add(n);
            } else if (n <= 1000) {
                sum += n;
            }
        }
        // Pourquoi TOUS les negatifs : l'utilisateur corrige tout en une fois.
        if (!negatives.isEmpty()) {
            throw new IllegalArgumentException("negatifs interdits : "
                    + negatives.stream().map(String::valueOf).collect(Collectors.joining(", ")));
        }
        return sum;
    }
}
