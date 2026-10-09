package ch18_design.projects.p02_shipping.solution;

/** L'express : 12,90 plus 2,50 par kilo commence ; 15,00 de plus hors de France. */
public final class ExpressRate implements ShippingRate {

    @Override
    public String code() {
        return "EXPRESS";
    }

    @Override
    public boolean accepts(Parcel parcel) {
        return parcel.grams() <= 30_000;
    }

    @Override
    public long basePrice(Parcel parcel) {
        long price = 1290 + parcel.startedKilos() * 250L;
        return parcel.domestic() ? price : price + 1500;
    }
}
