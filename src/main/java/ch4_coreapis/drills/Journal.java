package ch4_coreapis.drills;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Les donnees du "journal de bord" partagees par TOUS les drills du chapitre 4.
 * ============================================================================
 *
 * Toujours les memes donnees : ton cerveau se concentre sur les METHODES
 * de l'API. Lis ce fichier une fois et garde-le ouvert a cote.
 *
 *   LINE    : "2024-03-10 07:42 WARN disque presque plein"  (date 0-9, heure 11-15, niveau 17-20)
 *   TITLE   : "  Journal de Bord  "                          (2 espaces de chaque cote)
 *   WORDS   : {"delta", "Alpha", "charlie", "bravo", "Echo"}
 *   SCORES  : {42, 7, 19, 88, 7, 63}                          (somme 226)
 *   SORTED  : {3, 8, 15, 23, 42, 57}                          (deja trie, pour binarySearch)
 *   START   : 2024-01-31 (un mercredi, annee bissextile)
 *   OPENING : 09:15        CLOSING : 17:45
 *   MEETING : 2024-03-10T01:30 (la nuit du passage a l'heure d'ete a New York)
 *   NEW_YORK: America/New_York        PARIS : Europe/Paris
 */
public final class Journal {

    public static final String LINE = "2024-03-10 07:42 WARN disque presque plein";

    public static final String TITLE = "  Journal de Bord  ";

    public static final String[] WORDS = {"delta", "Alpha", "charlie", "bravo", "Echo"};

    public static final int[] SCORES = {42, 7, 19, 88, 7, 63};

    public static final int[] SORTED = {3, 8, 15, 23, 42, 57};

    public static final LocalDate START = LocalDate.of(2024, 1, 31);

    public static final LocalTime OPENING = LocalTime.of(9, 15);

    public static final LocalTime CLOSING = LocalTime.of(17, 45);

    public static final LocalDateTime MEETING = LocalDateTime.of(2024, 3, 10, 1, 30);

    public static final ZoneId NEW_YORK = ZoneId.of("America/New_York");

    public static final ZoneId PARIS = ZoneId.of("Europe/Paris");

    private Journal() {
    }
}
