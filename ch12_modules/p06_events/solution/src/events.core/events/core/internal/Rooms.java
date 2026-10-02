package events.core.internal;

import events.model.Event;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * SOLUTION - allocation de salles par balayage : evenements tries par debut ; un tas des salles OCCUPEES
 * (par heure de fin) et un tas des salles LIBRES (plus petit numero d'abord). Exporte seulement a events.app.
 */
public final class Rooms {

    private Rooms() {
    }

    record Busy(int end, int room) {
    }

    public static Map<String, Integer> allocate(List<Event> events) {
        List<Event> sorted = events.stream().sorted(Comparator.comparingInt(Event::start).thenComparing(Event::id)).toList();
        PriorityQueue<Busy> busy = new PriorityQueue<>(Comparator.comparingInt(Busy::end).thenComparingInt(Busy::room));
        PriorityQueue<Integer> free = new PriorityQueue<>();
        Map<String, Integer> rooms = new TreeMap<>();
        int opened = 0;
        for (Event e : sorted) {
            while (!busy.isEmpty() && busy.peek().end() <= e.start()) {
                free.add(busy.poll().room());                  // la salle se libere avant le debut
            }
            int room = free.isEmpty() ? ++opened : free.poll();
            rooms.put(e.id(), room);
            busy.add(new Busy(e.end(), room));
        }
        return rooms;
    }
}
