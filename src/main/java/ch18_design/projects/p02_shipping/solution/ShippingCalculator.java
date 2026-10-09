package ch18_design.projects.p02_shipping.solution;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * FERME a la modification, OUVERT a l'extension : ce calculateur ne nomme aucun transporteur ni
 * aucune surcharge. On lui DONNE ses strategies a la construction ; il ne change plus jamais.
 */
public final class ShippingCalculator {

    private final Map<String, ShippingRate> rates = new LinkedHashMap<>();
    private final List<Surcharge> surcharges;

    public ShippingCalculator(List<ShippingRate> rates, List<Surcharge> surcharges) {
        for (ShippingRate rate : rates) {
            // Deux strategies sous le meme code : l'une ecraserait l'autre en silence. On refuse tout de suite.
            if (this.rates.putIfAbsent(rate.code(), rate) != null) {
                throw new IllegalArgumentException("transporteur en double : " + rate.code());
            }
        }
        this.surcharges = List.copyOf(surcharges);
    }

    public long price(String carrier, Parcel parcel) {
        ShippingRate rate = rates.get(carrier);
        if (rate == null) {
            throw new IllegalArgumentException("transporteur inconnu : " + carrier);
        }
        if (!rate.accepts(parcel)) {
            throw new IllegalArgumentException("colis refuse par " + carrier);
        }
        return finalPrice(rate, parcel);
    }

    // Chaque surcharge se calcule sur le prix de BASE (pas sur le prix deja surcharge).
    private long finalPrice(ShippingRate rate, Parcel parcel) {
        long base = rate.basePrice(parcel);
        return base + surcharges.stream().mapToLong(s -> s.amount(parcel, base)).sum();
    }

    // Plus d'exceptions pour sauter un transporteur (le legacy le faisait) : on DEMANDE accepts().
    public List<Quote> quotes(Parcel parcel) {
        return rates.values().stream()
                .filter(rate -> rate.accepts(parcel))
                .map(rate -> new Quote(rate.code(), finalPrice(rate, parcel)))
                .sorted(Comparator.comparingLong(Quote::priceCents).thenComparing(Quote::carrier))
                .toList();
    }

    public Optional<Quote> cheapest(Parcel parcel) {
        return quotes(parcel).stream().findFirst();
    }
}
