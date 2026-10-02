package ch10_streams.projects.p03_weather;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON WeatherStation, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "STATION LYON-BRON-07 : code de controle 51",
            "REJET 2026-07-19 : 23 releves au lieu de 24",
            "2026-07-14 : min 16 max 31 moy 22.8 | pic 10h-12h moy 30.7",
            "2026-07-14 : releves 0h/6h/12h/18h 18 22 31 21 | heures >= 30 [10, 11, 12, 13]",
            "2026-07-15 : min 18 max 35 moy 26.1 | pic 11h-13h moy 34.7",
            "2026-07-15 : releves 0h/6h/12h/18h 20 24 35 26 | heures >= 30 [8, 9, 10, 11, 12, 13, 14, 15, 16]",
            "2026-07-16 : min 19 max 37 moy 27.3 | pic 11h-13h moy 36.3",
            "2026-07-16 : releves 0h/6h/12h/18h 21 25 37 27 | heures >= 30 [8, 9, 10, 11, 12, 13, 14, 15, 16]",
            "2026-07-17 : min 15 max 31 moy 20.6 | pic 9h-11h moy 29.7",
            "2026-07-17 : releves 0h/6h/12h/18h 22 20 24 17 | heures >= 30 [10, 11]",
            "2026-07-18 : min 13 max 31 moy 21.6 | pic 11h-13h moy 30.7",
            "2026-07-18 : releves 0h/6h/12h/18h 15 19 31 22 | heures >= 30 [11, 12, 13, 14]",
            "MEDIANE : 22.0",
            "MOYENNE : 23.7 C / 74.6 F",
            "HISTO [10-14] 4 ##",
            "HISTO [15-19] 33 ################",
            "HISTO [20-24] 35 #################",
            "HISTO [25-29] 20 ##########",
            "HISTO [30-34] 21 ##########",
            "HISTO [35-39] 7 ###",
            "CANICULE : 2 jour(s) consecutifs (2026-07-15 -> 2026-07-16)",
            "MONTEE : 9 heure(s) de hausse continue le 2026-07-15 (3h -> 12h)",
            "ORAGE : chute de 7 degres le 2026-07-17 a 12h",
            "CLIMATISATION : 102900 Wh (102.9 kWh)",
            "DEGRES-JOURS : 5.38");
            // EXPECTED-END

    static final List<String> API = List.of(
            "IntStream.of(", "IntStream.range(", "IntStream.rangeClosed(", "IntStream.iterate(",
            ".chars()", ".mapToInt(", ".mapToLong(", ".mapToDouble(", ".mapToObj(", ".flatMapToInt(",
            ".boxed()", ".asDoubleStream()", ".toArray()", ".summaryStatistics()", "IntSummaryStatistics",
            ".average()", ".getAsDouble()", ".sum()", ".max()",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "WeatherStation", args, EXPECTED, API);
    }
}
