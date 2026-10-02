package events.api;

import events.model.Attendee;
import events.model.Event;

/**
 * SOLUTION - le service : un canal de notification.
 */
public interface Notifier {

    String channel();

    String send(Attendee attendee, Event event);
}
