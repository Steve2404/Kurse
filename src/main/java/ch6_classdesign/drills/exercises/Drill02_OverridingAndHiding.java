package ch6_classdesign.drills.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DRILL 02 - Redefinir, cacher, equals/hashCode/toString, retour covariant, cast
 * =============================================================================
 *
 * Mode d'emploi : voir Drill01_InheritanceAndConstructors.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : Ticket.toString()         [redefinir toString] "Ticket#7".
 * TODO 2  : Ticket.equals(other)      [equals(Object), pas equals(Ticket)] meme classe et meme id.
 * TODO 3  : Ticket.hashCode()         [coherent avec equals] Integer.hashCode(id).
 * TODO 4  : VipTicket.price()         [super.methode()] le double du prix du parent -> 40.
 * TODO 5  : VipTicket.copy()          [retour covariant] rend un VipTicket (pas un Ticket) de meme id.
 * TODO 6  : tagThroughParent()        [champ cache] Ticket t = new VipTicket(1) ; t.tag -> "ticket".
 * TODO 7  : kindThroughParent()       [static cachee] t.kind() -> "ticket".
 * TODO 8  : priceThroughParent()      [redefinition] t.price() -> 40 (l'objet reel decide).
 * TODO 9  : asVipOrNull(t)            [instanceof avec variable] le VipTicket, ou null.
 * TODO 10 : distinctCount(tickets)    [HashSet + equals/hashCode] [1, 1, 2] -> 2.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Redefinition : meme signature ; acces >= ; retour identique ou sous-type ; pas de nouvelle checked
 *   @Override fait verifier tout ca par le compilateur
 *   Methode d'instance : choisie par l'OBJET ; champ et methode static : choisis par le TYPE de la variable
 *   equals(Object o) ! equals(Ticket t) serait une SURCHARGE, ignoree par HashSet
 *   a.equals(b) vrai -> a.hashCode() == b.hashCode()
 *   (Enfant) ref : compile si Enfant est un sous-type ; ClassCastException si l'objet n'en est pas un
 * ---------------------------------------------------------------------
 */
public class Drill02_OverridingAndHiding {

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
            throw new UnsupportedOperationException("TODO 1 : implementer toString()");
        }

        @Override
        public boolean equals(Object other) {
            throw new UnsupportedOperationException("TODO 2 : implementer equals()");
        }

        @Override
        public int hashCode() {
            throw new UnsupportedOperationException("TODO 3 : implementer hashCode()");
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
            throw new UnsupportedOperationException("TODO 4 : implementer VipTicket.price()");
        }

        @Override
        VipTicket copy() {
            throw new UnsupportedOperationException("TODO 5 : implementer VipTicket.copy()");
        }
    }

    public static String tagThroughParent() {
        throw new UnsupportedOperationException("TODO 6 : implementer tagThroughParent()");
    }

    public static String kindThroughParent() {
        throw new UnsupportedOperationException("TODO 7 : implementer kindThroughParent()");
    }

    public static int priceThroughParent() {
        throw new UnsupportedOperationException("TODO 8 : implementer priceThroughParent()");
    }

    public static VipTicket asVipOrNull(Ticket t) {
        throw new UnsupportedOperationException("TODO 9 : implementer asVipOrNull()");
    }

    public static int distinctCount(List<Ticket> tickets) {
        throw new UnsupportedOperationException("TODO 10 : implementer distinctCount()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  toString : Ticket#7", new Ticket(7).toString().equals("Ticket#7"));
        ExerciseChecker.check("2  equals : meme classe et meme id",
                new Ticket(1).equals(new Ticket(1)) && !new Ticket(1).equals(new Ticket(2)) && !new Ticket(1).equals(new VipTicket(1))
                        && !new Ticket(1).equals(null));
        ExerciseChecker.check("3  hashCode coherent", new Ticket(5).hashCode() == new Ticket(5).hashCode());
        ExerciseChecker.check("4  VipTicket.price() == 40", new VipTicket(1).price() == 40);
        VipTicket copy = new VipTicket(3).copy();
        ExerciseChecker.check("5  copy() rend un VipTicket de meme id", copy.id == 3);
        ExerciseChecker.check("6  tagThroughParent() == ticket", tagThroughParent().equals("ticket"));
        ExerciseChecker.check("7  kindThroughParent() == ticket", kindThroughParent().equals("ticket"));
        ExerciseChecker.check("8  priceThroughParent() == 40", priceThroughParent() == 40);
        ExerciseChecker.check("9  asVipOrNull", asVipOrNull(new VipTicket(2)) != null && asVipOrNull(new Ticket(2)) == null);
        ExerciseChecker.check("10 distinctCount([1, 1, 2]) == 2", distinctCount(List.of(new Ticket(1), new Ticket(1), new Ticket(2))) == 2);

        ExerciseChecker.summary();
    }
}
