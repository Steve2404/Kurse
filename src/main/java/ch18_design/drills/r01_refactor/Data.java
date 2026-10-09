package ch18_design.drills.r01_refactor;

/** FOURNI (ne pas modifier) : le prix d'une place au cinema, ecrit d'un seul bloc. C'est la specification. */
public final class Data {

    private Data() {
    }

    /** Le prix en centimes. day : "LUN", "MAR", "MER", "JEU", "VEN", "SAM" ou "DIM". */
    public static int legacyTicket(int age, boolean student, String day, int hour) {
        int p;
        if (age < 0 || hour < 0 || hour > 23) {
            throw new IllegalArgumentException("entree invalide");
        }
        if (age < 4) {
            p = 0;
        } else {
            if (age < 14) {
                p = 600;
            } else if (age >= 65) {
                p = 750;
            } else {
                if (student) {
                    p = 800;
                } else {
                    p = 1100;
                }
            }
            if (day.equals("MER") && age < 14) {
                p = p - 100;
            }
            if (hour < 12) {
                p = p - 200;
            }
            if (day.equals("SAM") || day.equals("DIM")) {
                p = p + 150;
            }
        }
        return p;
    }
}
