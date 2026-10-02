package ch11_exceptions.projects.p06_agenda.solution;

import ch11_exceptions.projects.p06_agenda.Data;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * SOLUTION du projet 6 - l'agenda international : DateTimeFormatter dans tous ses etats.
 */
public class Agenda {

    static String visible(String text) {
        return text.replace(' ', '_').replace(' ', '_');
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        DateReader reader = new DateReader(Data.PATTERNS);
        List<Event> events = new ArrayList<>();
        for (String raw : Data.RAW) {
            String[] p = raw.split("\\|");
            try {
                LocalDateTime when = reader.read(p[0]);
                events.add(new Event(when, p[1]));
                System.out.println("lu \"" + p[0] + "\" -> " + when);    // toString d'un LocalDateTime : format ISO
            } catch (UnreadableDateException e) {
                System.out.println(e.getMessage() + " (" + e.getSuppressed().length + " essais) ; dernier : " + e.getSuppressed()[e.getSuppressed().length - 1].getMessage());
            }
        }

        // Echappement : le texte entre apostrophes est recopie tel quel ; '' donne une apostrophe.
        DateTimeFormatter french = DateTimeFormatter.ofPattern("EEEE d MMMM uuuu 'a' HH'h'mm", Locale.FRANCE);
        events.sort(Comparator.comparing(Event::when));
        for (Event e : events) {
            System.out.println("  " + e.when().format(french) + " : " + e.title());
        }

        DateTimeFormatter medium = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.GERMANY);
        List<LocalDate> meetings = Planner.nthWeekday(Data.NTH, DayOfWeek.valueOf(Data.WEEKDAY), YearMonth.parse(Data.FIRST_MONTH), Data.OCCURRENCES);
        System.out.println("recurrence " + Data.NTH + "e " + Data.WEEKDAY + " : " + meetings.stream().map(medium::format).collect(Collectors.joining(", ")));

        Set<LocalDate> holidays = Arrays.stream(Data.HOLIDAYS).map(LocalDate::parse).collect(Collectors.toSet());
        List<LocalDate> skipped = new ArrayList<>();
        LocalDate due = Planner.addBusinessDays(LocalDate.parse(Data.START), Data.BUSINESS_DAYS, holidays, skipped);
        DateTimeFormatter shortDay = DateTimeFormatter.ofPattern("EEE dd/MM", Locale.FRANCE);
        System.out.println("echeance " + Data.START + " + " + Data.BUSINESS_DAYS + " jours ouvres = " + due.format(shortDay) + ", sautes "
                + skipped.stream().map(shortDay::format).toList());

        // Meme instant, autres fuseaux : withZoneSameInstant. Le decalage change avec l'heure d'ete (pas aux memes dates !).
        DateTimeFormatter zoned = DateTimeFormatter.ofPattern("EEE HH:mm z (xxx)", Locale.US);
        for (int week = 0; week < 2; week++) {
            ZonedDateTime paris = LocalDateTime.parse(Data.MEETING).plusWeeks(week).atZone(ZoneId.of(Data.ZONES[0]));
            StringBuilder line = new StringBuilder("reunion :");
            for (String zone : Data.ZONES) {
                line.append(" | ").append(zone).append(' ').append(paris.withZoneSameInstant(ZoneId.of(zone)).format(zoned));
            }
            System.out.println(line);
        }

        LocalDateTime sample = LocalDateTime.parse(Data.SAMPLE);
        for (String tag : Data.LOCALES) {
            Locale locale = Locale.forLanguageTag(tag);
            StringBuilder line = new StringBuilder(tag + " :");
            for (FormatStyle style : FormatStyle.values()) {
                line.append(" | ").append(DateTimeFormatter.ofLocalizedDate(style).withLocale(locale).format(sample));
            }
            line.append(" | ").append(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).localizedBy(locale).format(sample));
            System.out.println(visible(line.toString()));
        }

        System.out.println("ISO : " + sample.format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + DateTimeFormatter.ISO_LOCAL_TIME.format(sample) + " ; apostrophe : "
                + sample.format(DateTimeFormatter.ofPattern("hh 'o''clock' a, MMM dd", Locale.US)));

        // Les erreurs : champ absent du type, zone absente pour un style LONG/FULL, texte illisible.
        try {
            LocalDate.parse("2026-03-14").format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeException e) {
            System.out.println("date sans heure : " + e.getClass().getSimpleName() + " " + e.getMessage());
        }
        try {
            sample.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL));
        } catch (DateTimeException e) {
            System.out.println("FULL sans zone : " + e.getClass().getSimpleName() + " " + e.getMessage());
        }
        try {
            LocalDate.parse("14.03.2026");
        } catch (DateTimeParseException e) {
            System.out.println("parse ISO : " + e.getMessage() + " (index " + e.getErrorIndex() + ", texte " + e.getParsedString() + ")");
        }
    }
}
