package ch4_coreapis.projects.p06_hotel.solution;

import ch4_coreapis.projects.p06_hotel.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

/**
 * SOLUTION du projet 6 (capstone) - une conception possible.
 * Tableaux "paralleles" : l'indice i decrit la meme reservation dans chaque tableau.
 */
public class Hotel {

    static String[] codes;
    static String[] guests;
    static int[] rooms;
    static LocalDate[] arrivals;
    static LocalDate[] departures;

    static String[] roomNumbers;
    static String[] roomTypes;
    static int[] roomCents;

    static String money(long cents) {
        return cents / 100 + "." + (cents % 100 < 10 ? "0" : "") + cents % 100;
    }

    // "  lea MARTIN " -> "Lea Martin" : strip, decoupage, majuscule initiale.
    static String normalize(String raw) {
        StringBuilder sb = new StringBuilder();
        for (String part : raw.strip().split(" +")) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    static int roomIndex(String number) {
        for (int i = 0; i < roomNumbers.length; i++) {
            if (roomNumbers[i].equals(number)) {
                return i;
            }
        }
        return -1;
    }

    // Prix : nuit par nuit ; vendredi et samedi majores. Calcul en centimes, arrondi par Math.round.
    static long price(int booking) {
        int base = roomCents[rooms[booking]];
        long total = 0;
        for (LocalDate night = arrivals[booking]; night.isBefore(departures[booking]); night = night.plusDays(1)) {
            DayOfWeek day = night.getDayOfWeek();
            boolean weekend = day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY;
            total += weekend ? Math.round(base * (100 + Data.WEEKEND_PERCENT) / 100.0) : base;
        }
        return total;
    }

    // Deux sejours [a1, d1) et [a2, d2) se chevauchent si chacun commence avant la fin de l'autre.
    static boolean overlap(int i, int j) {
        return rooms[i] == rooms[j] && arrivals[i].isBefore(departures[j]) && arrivals[j].isBefore(departures[i]);
    }

    public static void main(String[] args) {
        roomNumbers = new String[Data.ROOMS.length];
        roomTypes = new String[Data.ROOMS.length];
        roomCents = new int[Data.ROOMS.length];
        for (int i = 0; i < Data.ROOMS.length; i++) {
            String[] p = Data.ROOMS[i].split(";");
            roomNumbers[i] = p[0];
            roomTypes[i] = p[1];
            // "120.50" -> 12050 centimes, sans passer par un double (pas d'erreur d'arrondi).
            roomCents[i] = Integer.parseInt(p[2].replace(".", ""));
        }

        int n = Data.BOOKINGS.length;
        codes = new String[n];
        guests = new String[n];
        rooms = new int[n];
        arrivals = new LocalDate[n];
        departures = new LocalDate[n];
        for (int i = 0; i < n; i++) {
            String[] p = Data.BOOKINGS[i].split(";");
            codes[i] = p[0];
            guests[i] = normalize(p[1]);
            rooms[i] = roomIndex(p[2]);
            arrivals[i] = LocalDate.parse(p[3]);
            departures[i] = LocalDate.parse(p[4]);
        }

        System.out.println("=== FACTURES ===");
        long revenue = 0;
        for (int i = 0; i < n; i++) {
            long nights = ChronoUnit.DAYS.between(arrivals[i], departures[i]);
            long total = price(i);
            revenue += total;
            String reference = guests[i].substring(0, 3).toUpperCase() + "-" + arrivals[i].getDayOfYear() + "-" + roomNumbers[rooms[i]];
            System.out.println(String.format("%-3s %-13s %-6s %s -> %s %2d nuit(s) %9s  ref %s", codes[i], guests[i], roomTypes[rooms[i]],
                    arrivals[i], departures[i], nights, money(total), reference));
        }
        System.out.println("CHIFFRE D'AFFAIRES : " + money(revenue));

        System.out.println("=== CONFLITS ===");
        int conflicts = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (overlap(i, j)) {
                    LocalDate from = arrivals[i].isAfter(arrivals[j]) ? arrivals[i] : arrivals[j];
                    LocalDate to = departures[i].isBefore(departures[j]) ? departures[i] : departures[j];
                    System.out.println(codes[i] + " et " + codes[j] + " : chambre " + roomNumbers[rooms[i]] + ", " + ChronoUnit.DAYS.between(from, to)
                            + " nuit(s) en commun a partir du " + from);
                    conflicts++;
                }
            }
        }
        System.out.println(conflicts + " conflit(s)");

        System.out.println("=== PLANNING (nombre de reservations par nuit) ===");
        LocalDate start = LocalDate.parse(Data.PLANNING_START);
        int[][] planning = new int[roomNumbers.length][Data.PLANNING_DAYS];
        for (int i = 0; i < n; i++) {
            for (int d = 0; d < Data.PLANNING_DAYS; d++) {
                LocalDate night = start.plusDays(d);
                if (!night.isBefore(arrivals[i]) && night.isBefore(departures[i])) {
                    planning[rooms[i]][d]++;
                }
            }
        }
        StringBuilder header = new StringBuilder("     ");
        for (int d = 0; d < Data.PLANNING_DAYS; d++) {
            header.append(String.format("%3d", start.plusDays(d).getDayOfMonth()));
        }
        System.out.println(header);
        for (int r = 0; r < planning.length; r++) {
            StringBuilder line = new StringBuilder(roomNumbers[r]).append("  ");
            for (int count : planning[r]) {
                line.append("  ").append(count == 0 ? '.' : count == 1 ? '#' : '!');
            }
            System.out.println(line);
        }

        String[] sortedGuests = Arrays.copyOf(guests, n);
        Arrays.sort(sortedGuests);
        System.out.println("CLIENTS : " + String.join(", ", sortedGuests));
    }
}
