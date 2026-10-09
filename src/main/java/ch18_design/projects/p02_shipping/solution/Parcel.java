package ch18_design.projects.p02_shipping.solution;

/** Un colis. Il se valide lui-meme : un colis invalide n'existe pas (memes messages que le legacy). */
public record Parcel(int grams, String country, boolean fragile) {

    public Parcel {
        if (grams < 1) {
            throw new IllegalArgumentException("poids invalide : " + grams);
        }
        if (country == null || !country.matches("[A-Z]{2}")) {
            throw new IllegalArgumentException("pays invalide : " + country);
        }
    }

    public boolean domestic() {
        return country.equals("FR");
    }

    // Le kilo commence se paie entier : 1 g coute un kilo, 1000 g aussi, 1001 g en coutent deux.
    public int startedKilos() {
        return (grams + 999) / 1000;
    }
}
