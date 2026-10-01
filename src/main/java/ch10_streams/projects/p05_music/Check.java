package ch10_streams.projects.p05_music;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON MusicStats, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "ECOUTES PAR GENRE : {Electro=5, Jazz=6, Pop=6, Rock=7}",
            "VALIDEES : 19, ZAPPEES : 5",
            "ONT ZAPPE : [hugo, lea, tom, zoe]",
            "TOP ARTISTE PAR GENRE : {Electro=Daft (3), Jazz=Miles (3), Pop=Adele (2), Rock=Queen (4)}",
            "TEMPS VALIDE PAR UTILISATEUR : {hugo=1360, ines=1567, lea=2009, tom=950, zoe=0}",
            "AMBIANCES PAR GENRE : {Electro=[energie, fete, nuit], Jazz=[calme, nuit, voix], Pop=[calme, energie, fete, voix], Rock=[energie, epique, live]}",
            "ECOUTE LA PLUS LONGUE : {hugo=Around, ines=So What, lea=So What, tom=One More Time, zoe=Uprising}",
            "SECONDES PAR GENRE : {Electro=1537, Jazz=1816, Pop=1006, Rock=1591}",
            "TOP 3 TITRES : [So What (1124 s), Bohemian (908 s), One More Time (640 s)]",
            "STATS : 24 ecoutes, min 8 s, max 562 s, moyenne 247.9 s",
            "EXTREMES : Hello par tom / So What par lea",
            "MOYENNE VALIDEE : 309.8 s sur 19 ecoutes",
            "EXPLORATEURS (tous les genres) : [lea]",
            "RECO hugo : voisin tom (0.50) -> Dua",
            "RECO ines : voisin lea (0.67) -> Daft, Muse",
            "RECO lea : voisin ines (0.67) -> rien de nouveau",
            "RECO tom : voisin hugo (0.50) -> Queen",
            "RECO zoe : aucune ecoute validee");
            // EXPECTED-END

    static final List<String> API = List.of(
            "groupingBy(", "TreeMap::new", "partitioningBy(", "counting()", "mapping(", "filtering(",
            "flatMapping(", "collectingAndThen(", "toCollection(", "toSet()", "maxBy(", "minBy(",
            "summingInt(", "averagingInt(", "summarizingInt(", "reducing(", "toMap(", "teeing(",
            "joining(");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "MusicStats", args, EXPECTED, API);
    }
}
