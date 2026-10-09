package ch16_testing.projects.p05_booking.solution;

import java.util.List;
import java.util.Optional;

/**
 * Ou sont rangees les reservations. En production, ce serait une base de donnees (chapitre 15) ;
 * dans les tests, une simple Map en memoire. Le service ne sait pas laquelle il recoit.
 */
public interface BookingRepository {

    int nextId();

    void save(Booking booking);

    Optional<Booking> find(int id);

    List<Booking> forRoom(String room);

    void delete(int id);
}
