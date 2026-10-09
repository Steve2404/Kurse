package ch19_final.projects.p05_resilience.solution;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * Une horloge qu'on avance a la main. Clock.fixed est arretee pour toujours ; ici, le test (ou la demo)
 * fait passer le temps quand il veut, de la duree qu'il veut : 5 minutes de panne se testent en 1 ms.
 * synchronized : plusieurs fils peuvent la lire pendant qu'un autre l'avance.
 */
public final class ManualClock extends Clock {

    private Instant now;

    public ManualClock(Instant start) {
        this.now = start;
    }

    public synchronized void advance(Duration duration) {
        now = now.plus(duration);
    }

    @Override
    public synchronized Instant instant() {
        return now;
    }

    @Override
    public ZoneId getZone() {
        return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        throw new UnsupportedOperationException("une seule zone : UTC");
    }
}
