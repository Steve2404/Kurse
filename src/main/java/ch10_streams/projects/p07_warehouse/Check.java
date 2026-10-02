package ch10_streams.projects.p07_warehouse;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 7 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Warehouse, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "REFUS O5 : client inconnu K9",
            "TRAITEMENT : O8, O2, O4, O1, O3, O6, O7, O9",
            "O8 Tom (GOLD) : COMPLETE 109.70",
            "O2 Lea (GOLD) : COMPLETE 338.30",
            "O4 Tom (GOLD) : PARTIELLE 349.00 | manque B2x2",
            "O1 Hugo (STANDARD) : PARTIELLE 139.60 | manque A1x1",
            "O3 Ines (STANDARD) : PARTIELLE 218.90 | manque A3x1",
            "O6 Zoe (STANDARD) : PARTIELLE 29.70 | manque C1x1",
            "O7 Hugo (STANDARD) : PARTIELLE 29.90 | manque B1x1 | inconnu Z9",
            "O9 Ines (STANDARD) : REFUSEE 0.00 | manque B2x1",
            "AVIS par courrier a Tom : O4 PARTIELLE",
            "AVIS par courrier a Hugo : O1 PARTIELLE",
            "AVIS ines@mail.fr : O3 PARTIELLE",
            "AVIS zoe@mail.fr : O6 PARTIELLE",
            "AVIS par courrier a Hugo : O7 PARTIELLE",
            "AVIS ines@mail.fr : O9 REFUSEE",
            "BONS : BL-001=O8 BL-002=O2 BL-003=O4 BL-004=O1 BL-005=O3 BL-006=O6 BL-007=O7",
            "STATUTS : {COMPLETE=[O2, O8], PARTIELLE=[O1, O3, O4, O6, O7], REFUSEE=[O9]}",
            "A RECOMMANDER : B2x3, A1x1, A3x1, B1x1, C1x1",
            "CA PAR REGION : {Est=458.70, Nord=557.20, Sud=199.20}",
            "CA GOLD : 797.00, STANDARD : 418.10",
            "UNITES PAR CATEGORIE : {Info=9, Maison=4, Sport=9}",
            "PANIERS : 7 expedies, min 29.70, max 349.00, total 1215.10",
            "PANIER MOYEN : 173.59, plus gros : O4 349.00",
            "TOP CLIENTS : Tom 458.70, Lea 338.30",
            "RUPTURES : A1, A3, B1, B2, C1",
            "STOCK RESTANT : A2=8, C2=12",
            "CONTROLE : 1493.10 = 1215.10 expedies + 278.00 en stock : oui");
            // EXPECTED-END

    static final List<String> API = List.of(
            "enum ", "Optional.ofNullable(", ".orElseGet(", ".flatMap(", ".sorted(", ".thenComparing",
            "groupingBy(", "EnumMap", "partitioningBy(", "mapping(", "summingInt(", "summingLong(",
            "collectingAndThen(", "teeing(", "maxBy(", "LongSummaryStatistics", ".mapToLong(",
            "IntStream.rangeClosed(", ".mapToObj(", ".reduce(", ".limit(", "joining(", "!.get()",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Warehouse", args, EXPECTED, API);
    }
}
