package ch18_design.projects.p02_shipping.solution;

/** La poste : des paliers jusqu'a 5 kg, puis 1,20 par kilo commence ; le double hors de France. */
public final class PostRate implements ShippingRate {

    static final int MAX_GRAMS = 30_000;

    @Override
    public String code() {
        return "POST";
    }

    @Override
    public boolean accepts(Parcel parcel) {
        return parcel.grams() <= MAX_GRAMS;
    }

    @Override
    public long basePrice(Parcel parcel) {
        long price = domesticPrice(parcel.grams());
        return parcel.domestic() ? price : price * 2;
    }

    private static long domesticPrice(int grams) {
        if (grams <= 500) {
            return 495;
        }
        if (grams <= 2000) {
            return 750;
        }
        if (grams <= 5000) {
            return 1150;
        }
        // Au-dela de 5 kg : chaque kilo COMMENCE en plus coute 1,20.
        return 1150 + (grams - 5000 + 999) / 1000 * 120;
    }
}
