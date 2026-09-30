package ch6_classdesign.drills.solutions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.drills.exercises.Drill02_OverridingAndHiding.
 */
public class SolutionDrill02_OverridingAndHiding {

    static class Ticket {
        String tag = "ticket";
        final int id;

        Ticket(int id) {
            this.id = id;
        }

        static String kind() {
            return "ticket";
        }

        int price() {
            return 20;
        }

        Ticket copy() {
            return new Ticket(id);
        }

        @Override
        public String toString() {
            // Redefinir toString d'Object : println et la concatenation l'utilisent.
            return "Ticket#" + id;
        }

        @Override
        public boolean equals(Object other) {
            // Parametre Object (sinon surcharge) ; getClass() exige la meme classe exacte.
            if (other == null || other.getClass() != getClass()) {
                return false;
            }
            return id == ((Ticket) other).id;
        }

        @Override
        public int hashCode() {
            // Memes champs que equals.
            return Integer.hashCode(id);
        }
    }

    static class VipTicket extends Ticket {
        String tag = "vip";

        VipTicket(int id) {
            super(id);
        }

        static String kind() {
            return "vip";
        }

        @Override
        int price() {
            // On reutilise le calcul du parent.
            return super.price() * 2;
        }

        @Override
        VipTicket copy() {
            // Retour covariant : un sous-type du retour du parent est permis.
            return new VipTicket(id);
        }
    }

    public static String tagThroughParent() {
        // Un champ suit le type de la variable (Ticket).
        Ticket t = new VipTicket(1);
        return t.tag;
    }

    @SuppressWarnings("static")
    public static String kindThroughParent() {
        // Une methode static cachee suit aussi le type de la variable.
        Ticket t = new VipTicket(1);
        return t.kind();
    }

    public static int priceThroughParent() {
        // Une methode d'instance redefinie suit l'objet reel.
        Ticket t = new VipTicket(1);
        return t.price();
    }

    public static VipTicket asVipOrNull(Ticket t) {
        // instanceof avec variable : test et cast en une seule fois.
        return t instanceof VipTicket vip ? vip : null;
    }

    public static int distinctCount(List<Ticket> tickets) {
        // HashSet utilise hashCode puis equals : les doublons disparaissent.
        Set<Ticket> set = new HashSet<>(tickets);
        return set.size();
    }
}
