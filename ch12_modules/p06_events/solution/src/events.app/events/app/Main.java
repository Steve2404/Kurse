package events.app;

import events.audit.Auditor;
import events.core.Dispatcher;
import events.core.internal.Rooms;
import events.model.Attendee;
import events.model.Event;

import java.util.List;

/**
 * SOLUTION du projet 6 (capstone) - la plateforme d'evenements.
 */
public class Main {

    static final List<Event> EVENTS = List.of(new Event("E1", 540, 600, "Paris"), new Event("E2", 570, 660, "Lyon"), new Event("E3", 600, 690, "Paris"),
            new Event("E4", 615, 645, "Lille"), new Event("E5", 660, 720, "Marseille"), new Event("E6", 690, 750, "Lyon"));

    static final List<Attendee> ATTENDEES = List.of(new Attendee("Ana", "email", "Bordeaux"), new Attendee("Bob", "sms", "Nice"),
            new Attendee("Chloe", "email", "Lyon"), new Attendee("Dan", "fax", "Paris"));

    public static void main(String[] args) {
        System.out.println("salles : " + Rooms.allocate(EVENTS));
        Dispatcher dispatcher = new Dispatcher();
        System.out.println("canaux : " + dispatcher.channels());
        for (Attendee a : ATTENDEES) {
            System.out.println("  " + dispatcher.dispatch(a, EVENTS));
        }
        System.out.println("audit : " + Auditor.inspect(dispatcher.counters()));
        System.out.println("lectures : events.app lit events.model " + Main.class.getModule().canRead(Event.class.getModule()) + ", Distance dans "
                + dispatcher.getClass().getModule().getDescriptor().requires().stream().filter(r -> r.name().startsWith("geo")).map(r -> r.name()).toList());
    }
}
