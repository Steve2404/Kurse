package ch11_exceptions.projects.p05_invoice.solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * SOLUTION - les algorithmes au centime pres : partage au plus fort reste et tableau d'amortissement.
 */
public final class Money {

    private Money() {
    }

    // Plus fort reste : chacun recoit la partie entiere de sa part ; les centimes restants vont,
    // un par un, aux plus grands restes (a egalite : le premier). La somme tombe JUSTE.
    public static long[] split(long total, int[] weights) {
        int sum = IntStream.of(weights).sum();
        long[] shares = new long[weights.length];
        long[] remainders = new long[weights.length];
        long given = 0;
        for (int i = 0; i < weights.length; i++) {
            shares[i] = total * weights[i] / sum;
            remainders[i] = total * weights[i] % sum;
            given += shares[i];
        }
        List<Integer> order = new ArrayList<>(IntStream.range(0, weights.length).boxed().toList());
        order.sort(Comparator.comparingLong((Integer i) -> remainders[i]).reversed().thenComparing(i -> i));
        for (int k = 0; k < total - given; k++) {
            shares[order.get(k)]++;
        }
        return shares;
    }

    // Mensualite constante : P * r / (1 - (1 + r)^-n), arrondie au centime ; la DERNIERE echeance solde le reste.
    // Chaque ligne : {mois, mensualite, interets, capital rembourse, capital restant}.
    public static List<long[]> schedule(long principal, int ratePerMille, int months) {
        double r = ratePerMille / 1000.0 / 12;
        long payment = Math.round(principal * r / (1 - Math.pow(1 + r, -months)));
        List<long[]> rows = new ArrayList<>();
        long balance = principal;
        for (int m = 1; m <= months; m++) {
            long interest = Math.round(balance * r);
            long pay = m == months ? balance + interest : payment;
            long capital = pay - interest;
            balance -= capital;
            rows.add(new long[] {m, pay, interest, capital, balance});
        }
        return rows;
    }
}
