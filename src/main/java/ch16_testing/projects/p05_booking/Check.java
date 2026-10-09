package ch16_testing.projects.p05_booking;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON BookingService et TES tests, ou avec l'argument "solution".
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("BookingService.java", "minutes < 15 ||", "minutes < 0 ||"),
            new Mutant("BookingService.java", "minutes > 240", "minutes > 255"),
            new Mutant("BookingService.java", " || minutes % 15 != 0", ""),
            new Mutant("BookingService.java", "if (!start.isAfter(now)) {", "if (start.isBefore(now)) {"),
            new Mutant("BookingService.java", "now.plusDays(30)", "now.plusDays(31)"),
            new Mutant("Booking.java", "return start.isBefore(otherEnd) && otherStart.isBefore(end);",
                    "return !start.isAfter(otherEnd) && !otherStart.isAfter(end);"),
            new Mutant("BookingService.java", "repo.forRoom(room).stream().anyMatch(", "repo.forRoom(\"\").stream().anyMatch("),
            new Mutant("BookingService.java", "repo.save(booking);\n        notifier.send(user, \"Reservation \"",
                    "notifier.send(user, \"Reservation \" + booking.id() + \" : \" + room + \" le \" + start);\n        repo.save(booking);\n        if (false) notifier.send(user, \"Reservation \""),
            new Mutant("BookingService.java", "if (!booking.user().equals(user)) {", "if (false) {"),
            new Mutant("BookingService.java", "plusHours(2).isAfter(booking.start())", "plusHours(1).isAfter(booking.start())"),
            new Mutant("BookingService.java", "notifier.send(user, \"Annulation \" + id);", ""),
            new Mutant("BookingService.java", ".filter(b -> b.start().toLocalDate().equals(today))", ""),
            new Mutant("BookingService.java", ".sorted(Comparator.comparing(Booking::start))", ""),
            new Mutant("BookingService.java", "new Booking(repo.nextId(), room,", "new Booking(1, room,"),
            new Mutant("BookingService.java", "repo.delete(id);", ""));

    static final List<String> API_CODE = List.of(
            "record Booking(int id, String room, String user, LocalDateTime start, LocalDateTime end)",
            "interface BookingRepository", "interface Notifier", "final class BookingService",
            "BookingService(BookingRepository repo, Notifier notifier, Clock clock)",
            "LocalDateTime.now(clock)", "LocalDate.now(clock)", "orElseThrow(",
            // "Maintenant" vient de l'horloge recue, jamais de la vraie.
            "!LocalDateTime.now()", "!LocalDate.now()", "!Instant.now()", "!System.currentTimeMillis");

    static final List<String> API_TESTS = List.of(
            "Clock.fixed(", "Instant.parse(", "ZoneOffset.UTC", "implements BookingRepository", "implements Notifier",
            "re:static (final )?class \\w+ (extends \\w+ )?implements##une doublure : classe static imbriquee qui implements", "@BeforeEach", "assertThrows(", "assertAll(",
            // Ici, les doublures s'ecrivent a la main : Mockito viendra au projet 6.
            "!org.mockito", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 18, MUTANTS, API_CODE, API_TESTS);
    }
}
