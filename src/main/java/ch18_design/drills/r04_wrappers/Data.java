package ch18_design.drills.r04_wrappers;

import java.util.Map;
import java.util.NoSuchElementException;

/** FOURNI (ne pas modifier) : la bourse d'un fournisseur. Des prix en euros (double), des symboles en minuscules. */
public final class Data {

    private Data() {
    }

    public static final class OldExchange {
        private final Map<String, Double> euros;
        private int failuresLeft;
        private int calls;

        /** failures : le nombre de premiers appels qui echouent. */
        public OldExchange(Map<String, Double> euros, int failures) {
            this.euros = Map.copyOf(euros);
            this.failuresLeft = failures;
        }

        public double quote(String lowerCaseTicker) {
            calls++;
            if (failuresLeft > 0) {
                failuresLeft--;
                throw new IllegalStateException("bourse injoignable");
            }
            Double value = euros.get(lowerCaseTicker);
            if (value == null) {
                throw new NoSuchElementException("symbole inconnu : " + lowerCaseTicker);
            }
            return value;
        }

        public int calls() {
            return calls;
        }
    }
}
