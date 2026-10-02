package ch11_exceptions.projects.p05_invoice.solution;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION - la facture : la TVA est calculee PAR TAUX, sur la somme des bases (puis arrondie au centime).
 */
public class Invoice {

    private final List<InvoiceLine> lines;

    public Invoice(List<InvoiceLine> lines) {
        this.lines = List.copyOf(lines);
    }

    public long net() {
        return lines.stream().mapToLong(InvoiceLine::net).sum();
    }

    // taux (pour mille) -> base HT cumulee
    public Map<Integer, Long> basesByRate() {
        Map<Integer, Long> bases = new TreeMap<>();
        lines.forEach(l -> bases.merge(l.vatPerMille(), l.net(), Long::sum));
        return bases;
    }

    public static long vat(long base, int perMille) {
        return Math.round(base * perMille / 1000.0);
    }

    public long totalVat() {
        return basesByRate().entrySet().stream().mapToLong(e -> vat(e.getValue(), e.getKey())).sum();
    }

    public long total() {
        return net() + totalVat();
    }

    public int size() {
        return lines.size();
    }
}
