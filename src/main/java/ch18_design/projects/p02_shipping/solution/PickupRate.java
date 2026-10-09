package ch18_design.projects.p02_shipping.solution;

import java.util.Set;

/** Le point relais : 3,90 fixe, en France et en Belgique, jusqu'a 20 kg inclus. */
public final class PickupRate implements ShippingRate {

    private static final Set<String> COUNTRIES = Set.of("FR", "BE");

    @Override
    public String code() {
        return "PICKUP";
    }

    @Override
    public boolean accepts(Parcel parcel) {
        return COUNTRIES.contains(parcel.country()) && parcel.grams() <= 20_000;
    }

    @Override
    public long basePrice(Parcel parcel) {
        return 390;
    }
}
