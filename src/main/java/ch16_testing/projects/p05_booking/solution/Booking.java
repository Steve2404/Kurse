package ch16_testing.projects.p05_booking.solution;

import java.time.LocalDateTime;

/** Une reservation de salle : de start (inclus) a end (exclu). */
public record Booking(int id, String room, String user, LocalDateTime start, LocalDateTime end) {

    // Pourquoi end EXCLU : une reunion de 10 h a 11 h et une autre de 11 h a 12 h ne se chevauchent pas.
    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return start.isBefore(otherEnd) && otherStart.isBefore(end);
    }
}
