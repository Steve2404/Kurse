package ch19_final.projects.p07_review.solution;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Un achat. Le montant est en CENTIMES, dans un long : un double ne sait pas ecrire 0,10 exactement
 * (0,1 + 0,2 = 0,30000000000000004). promoCode peut etre null.
 */
public record Purchase(String customerId, long cents, LocalDate date, String promoCode) {

    public Purchase {
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(date, "date");
        if (cents < 0) {
            throw new IllegalArgumentException("montant negatif : " + cents);
        }
    }
}
