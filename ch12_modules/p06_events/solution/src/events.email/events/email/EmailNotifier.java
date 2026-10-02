package events.email;

import events.api.Notifier;
import events.model.Attendee;
import events.model.Event;

/**
 * SOLUTION - le canal email (classe publique, constructeur public sans argument implicite).
 */
public class EmailNotifier implements Notifier {

    @Override
    public String channel() {
        return "email";
    }

    @Override
    public String send(Attendee attendee, Event event) {
        return "email a " + attendee.name() + " : " + event.id() + " a " + event.city();
    }
}
