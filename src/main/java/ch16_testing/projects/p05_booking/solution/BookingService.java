package ch16_testing.projects.p05_booking.solution;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Les reservations de salles de reunion.
 * Pourquoi tout recevoir par le constructeur (injection de dependances) : les tests donnent un faux depot,
 * un espion de notifications et une horloge FIXE ; le service ne cree rien lui-meme.
 */
public final class BookingService {

    private final BookingRepository repo;
    private final Notifier notifier;
    private final Clock clock;

    public BookingService(BookingRepository repo, Notifier notifier, Clock clock) {
        this.repo = repo;
        this.notifier = notifier;
        this.clock = clock;
    }

    // Piege : "maintenant" vient TOUJOURS de l'horloge recue ; LocalDateTime.now() sans argument rendrait les tests faux demain.
    public Booking book(String room, String user, LocalDateTime start, int minutes) {
        if (minutes < 15 || minutes > 240 || minutes % 15 != 0) {
            throw new IllegalArgumentException("duree invalide : " + minutes);
        }
        LocalDateTime now = LocalDateTime.now(clock);
        if (!start.isAfter(now)) {
            throw new IllegalArgumentException("debut dans le passe : " + start);
        }
        if (start.isAfter(now.plusDays(30))) {
            throw new IllegalArgumentException("trop tot pour reserver : " + start);
        }
        LocalDateTime end = start.plusMinutes(minutes);
        if (repo.forRoom(room).stream().anyMatch(b -> b.overlaps(start, end))) {
            throw new IllegalStateException("salle occupee : " + room);
        }
        Booking booking = new Booking(repo.nextId(), room, user, start, end);
        // Pourquoi save AVANT send : si l'enregistrement echoue, personne ne doit recevoir une confirmation.
        repo.save(booking);
        notifier.send(user, "Reservation " + booking.id() + " : " + room + " le " + start);
        return booking;
    }

    // Piege : exactement 2 h avant le debut, on peut encore annuler ; 1 minute de moins, non.
    public void cancel(int id, String user) {
        Booking booking = repo.find(id).orElseThrow(() -> new NoSuchElementException("reservation inconnue : " + id));
        if (!booking.user().equals(user)) {
            throw new IllegalStateException("pas ta reservation : " + id);
        }
        if (LocalDateTime.now(clock).plusHours(2).isAfter(booking.start())) {
            throw new IllegalStateException("trop tard pour annuler : " + id);
        }
        repo.delete(id);
        notifier.send(user, "Annulation " + id);
    }

    public List<Booking> todayFor(String room) {
        LocalDate today = LocalDate.now(clock);
        return repo.forRoom(room).stream()
                .filter(b -> b.start().toLocalDate().equals(today))
                .sorted(Comparator.comparing(Booking::start))
                .toList();
    }
}
