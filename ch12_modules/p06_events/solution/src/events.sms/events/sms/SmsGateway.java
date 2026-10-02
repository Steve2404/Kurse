package events.sms;

import events.api.Notifier;
import events.model.Attendee;
import events.model.Event;

/**
 * SOLUTION - fabrique de fournisseur : public static provider().
 */
public final class SmsGateway {

    private SmsGateway() {
    }

    public static Notifier provider() {
        return new Notifier() {
            @Override
            public String channel() {
                return "sms";
            }

            @Override
            public String send(Attendee attendee, Event event) {
                return "sms a " + attendee.name() + " : " + event.id() + " " + event.start() / 60 + "h";
            }
        };
    }
}
