package ch2_operators.projects.p01_parking.solution;

/**
 * SOLUTION du projet 1 - une conception possible.
 * Aucun if : chaque choix est un ternaire, chaque regle un operateur.
 */
public class Parking {

    static final int FREE_MINUTES = 15;
    static final int DAILY_CAP = 2000;
    static final int TRUCK_CAP = 3500;

    static int issued;

    static String euros(int cents) {
        return cents / 100 + "." + cents / 10 % 10 + cents % 10;
    }

    // Ternaires imbriques : ils se lisent de gauche a droite, comme une chaine de "sinon si".
    static String label(int type) {
        return type == 1 ? "MOTO" : type == 2 ? "VOITURE" : "CAMION";
    }

    static int hourlyRate(int type) {
        return type == 1 ? 150 : type == 2 ? 250 : 400;
    }

    static String ticket(int type, int minutes, boolean night, boolean subscriber, int visit) {
        // Les 15 premieres minutes sont gratuites ; jamais de duree negative.
        int billable = minutes > FREE_MINUTES ? minutes - FREE_MINUTES : 0;
        // Arrondi a l'heure SUPERIEURE en division entiere : (n + 59) / 60.
        int hours = (billable + 59) / 60;
        int fee = hours * hourlyRate(type);
        // += avec un ternaire : la majoration de nuit vaut la moitie du tarif, ou rien.
        fee += night ? fee / 2 : 0;
        int cap = type == 3 ? TRUCK_CAP : DAILY_CAP;
        fee = fee > cap ? cap : fee;
        fee -= subscriber ? fee / 5 : 0;
        // % 10 == 0 : une visite sur dix est offerte.
        boolean free = visit % 10 == 0;
        int due = free ? 0 : fee;
        String reason = hours == 0 ? "gratuit (moins de 15 min)" : free ? "offert (10e visite)" : euros(due);
        // Pre-increment : ++issued incremente PUIS rend la nouvelle valeur (1 pour le 1er ticket) ; issued++ rendrait 0.
        return "#" + ++issued + " " + label(type) + " | " + minutes + " min | " + (night ? "nuit" : "jour")
                + (!subscriber ? "" : " | abonne") + " | " + hours + " h | " + reason;
    }

    public static void main(String[] args) {
        System.out.println(ticket(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]), Integer.parseInt(args[4])));
        System.out.println(ticket(1, 10, false, false, 1));
        System.out.println(ticket(3, 600, false, true, 4));
        System.out.println(ticket(2, 61, true, true, 10));
        System.out.println(ticket(1, 16, false, false, 7));
        System.out.println(ticket(3, 1440, true, false, 20));
        System.out.println("Tickets emis : " + issued + ", prochain numero : " + (issued + 1));
    }
}
