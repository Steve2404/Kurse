package events.core;

import com.geo.Distance;
import events.api.Notifier;
import events.core.state.Counters;
import events.model.Attendee;
import events.model.Event;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * SOLUTION - envoie a chaque participant l'evenement le plus PROCHE de sa ville, par son canal prefere.
 */
public class Dispatcher {

    private final Map<String, Notifier> notifiers;
    private final Counters counters = new Counters();

    public Dispatcher() {
        notifiers = ServiceLoader.load(Notifier.class).stream().map(ServiceLoader.Provider::get)
                .collect(Collectors.toMap(Notifier::channel, Function.identity(), (a, b) -> a, TreeMap::new));
    }

    public List<String> channels() {
        return List.copyOf(notifiers.keySet());
    }

    public Counters counters() {
        return counters;
    }

    public String dispatch(Attendee attendee, List<Event> events) {
        Event nearest = events.stream().min(Comparator.comparingLong((Event e) -> Distance.km(attendee.city(), e.city())).thenComparing(Event::id)).orElseThrow();
        Notifier n = notifiers.get(attendee.channel());
        if (n == null) {
            counters.missing();
            return attendee.name() + " : pas de canal " + attendee.channel();
        }
        counters.sent();
        return n.send(attendee, nearest) + " (" + Distance.km(attendee.city(), nearest.city()) + " km)";
    }
}
