package ch16_testing.projects.p05_booking.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Les tests de reference du projet 5 : trois doublures ecrites a la main, et une horloge figee. */
class BookingServiceTest {

    // ---- les doublures (test doubles)

    /** Un FAUX (fake) : un vrai depot qui marche, mais en memoire. */
    static class InMemoryRepository implements BookingRepository {
        final Map<Integer, Booking> bookings = new TreeMap<>();
        int lastId = 0;

        @Override
        public int nextId() {
            return ++lastId;
        }

        @Override
        public void save(Booking booking) {
            bookings.put(booking.id(), booking);
        }

        @Override
        public Optional<Booking> find(int id) {
            return Optional.ofNullable(bookings.get(id));
        }

        @Override
        public List<Booking> forRoom(String room) {
            return bookings.values().stream().filter(b -> b.room().equals(room)).toList();
        }

        @Override
        public void delete(int id) {
            bookings.remove(id);
        }
    }

    /** Un ESPION (spy) : il ne fait rien, mais note chaque appel pour qu'on le verifie apres. */
    static final class RecordingNotifier implements Notifier {
        final List<String> sent = new ArrayList<>();

        @Override
        public void send(String to, String message) {
            sent.add(to + " <- " + message);
        }
    }

    /** Un BOUCHON (stub) qui echoue : pour tester ce qui se passe quand l'enregistrement casse. */
    static final class BrokenRepository extends InMemoryRepository {
        @Override
        public void save(Booking booking) {
            throw new IllegalStateException("disque plein");
        }
    }

    // Lundi 2 mars 2026, 9 h 00, en UTC : "maintenant", pour TOUS les tests, tous les jours, sur toutes les machines.
    static final Clock NINE_AM = Clock.fixed(Instant.parse("2026-03-02T09:00:00Z"), ZoneOffset.UTC);
    static final LocalDateTime TEN_AM = LocalDateTime.of(2026, 3, 2, 10, 0);

    private InMemoryRepository repo;
    private RecordingNotifier notifier;
    private BookingService service;

    @BeforeEach
    void setUp() {
        repo = new InMemoryRepository();
        notifier = new RecordingNotifier();
        service = new BookingService(repo, notifier, NINE_AM);
    }

    @Nested
    class WhenBooking {

        @Test
        void aValidBookingIsSavedAndConfirmed() {
            Booking b = service.book("Atlas", "ana", TEN_AM, 60);
            assertAll(
                    () -> assertEquals(new Booking(1, "Atlas", "ana", TEN_AM, TEN_AM.plusHours(1)), b),
                    () -> assertEquals(Optional.of(b), repo.find(1)),
                    () -> assertEquals(List.of("ana <- Reservation 1 : Atlas le 2026-03-02T10:00"), notifier.sent));
        }

        @Test
        void idsComeFromTheRepository() {
            service.book("Atlas", "ana", TEN_AM, 15);
            Booking second = service.book("Atlas", "bob", TEN_AM.plusHours(1), 15);
            assertEquals(2, second.id());
        }

        @ParameterizedTest(name = "duree {0} refusee")
        @ValueSource(ints = {0, 10, 14, 25, 255})
        void invalidDurationsAreRejected(int minutes) {
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.book("Atlas", "ana", TEN_AM, minutes));
            assertEquals("duree invalide : " + minutes, e.getMessage());
        }

        @ParameterizedTest(name = "duree {0} acceptee")
        @ValueSource(ints = {15, 45, 240})
        void durationsFromFifteenToTwoHundredFortyByQuarters(int minutes) {
            assertEquals(TEN_AM.plusMinutes(minutes), service.book("Atlas", "ana", TEN_AM, minutes).end());
        }

        @Test
        void startMustBeStrictlyInTheFuture() {
            LocalDateTime now = LocalDateTime.of(2026, 3, 2, 9, 0);
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> service.book("Atlas", "ana", now, 30));
            assertEquals("debut dans le passe : 2026-03-02T09:00", e.getMessage());
            assertEquals(1, service.book("Atlas", "ana", now.plusMinutes(1), 30).id());
        }

        @Test
        void atMostThirtyDaysAhead() {
            LocalDateTime limit = LocalDateTime.of(2026, 4, 1, 9, 0);   // maintenant + 30 jours
            assertEquals(1, service.book("Atlas", "ana", limit, 30).id());
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                    () -> service.book("Atlas", "ana", limit.plusMinutes(1), 30));
            assertEquals("trop tot pour reserver : 2026-04-01T09:01", e.getMessage());
        }

        @Test
        void overlappingBookingIsRejectedButBackToBackIsFine() {
            service.book("Atlas", "ana", TEN_AM, 60);
            IllegalStateException e = assertThrows(IllegalStateException.class,
                    () -> service.book("Atlas", "bob", TEN_AM.plusMinutes(45), 30));
            assertAll(
                    () -> assertEquals("salle occupee : Atlas", e.getMessage()),
                    () -> assertEquals(2, service.book("Atlas", "bob", TEN_AM.plusHours(1), 30).id()),
                    () -> assertEquals(3, service.book("Atlas", "eve", TEN_AM.minusMinutes(30), 30).id()),
                    () -> assertEquals(4, service.book("Orion", "bob", TEN_AM, 60).id()));
        }

        @Test
        void nothingIsSentWhenSavingFails() {
            BookingService broken = new BookingService(new BrokenRepository(), notifier, NINE_AM);
            assertThrows(IllegalStateException.class, () -> broken.book("Atlas", "ana", TEN_AM, 30));
            assertEquals(List.of(), notifier.sent);
        }
    }

    @Nested
    class WhenCancelling {

        @BeforeEach
        void bookOne() {
            service.book("Atlas", "ana", LocalDateTime.of(2026, 3, 2, 11, 0), 60);
            notifier.sent.clear();
        }

        @Test
        void ownerCanCancelTwoHoursBefore() {
            service.cancel(1, "ana");
            assertAll(
                    () -> assertEquals(Optional.empty(), repo.find(1)),
                    () -> assertEquals(List.of("ana <- Annulation 1"), notifier.sent));
        }

        @Test
        void tooLateLessThanTwoHoursBefore() {
            service.book("Orion", "ana", LocalDateTime.of(2026, 3, 2, 10, 59), 15);   // 1 h 59 avant le debut
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.cancel(2, "ana"));
            assertAll(
                    () -> assertEquals("trop tard pour annuler : 2", e.getMessage()),
                    () -> assertEquals(2, repo.find(2).orElseThrow().id()));
        }

        @Test
        void onlyTheOwnerCanCancel() {
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.cancel(1, "bob"));
            assertAll(
                    () -> assertEquals("pas ta reservation : 1", e.getMessage()),
                    () -> assertEquals(List.of(), notifier.sent));
        }

        @Test
        void unknownBookingCannotBeCancelled() {
            NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> service.cancel(99, "ana"));
            assertEquals("reservation inconnue : 99", e.getMessage());
        }
    }

    @Test
    void todayListsOnlyTodaysBookingsOfTheRoomInOrder() {
        service.book("Atlas", "ana", LocalDateTime.of(2026, 3, 2, 16, 0), 30);
        service.book("Atlas", "bob", LocalDateTime.of(2026, 3, 3, 8, 0), 30);
        service.book("Orion", "eve", LocalDateTime.of(2026, 3, 2, 12, 0), 30);
        service.book("Atlas", "eve", TEN_AM, 30);
        assertEquals(List.of("eve", "ana"), service.todayFor("Atlas").stream().map(Booking::user).toList());
    }
}
