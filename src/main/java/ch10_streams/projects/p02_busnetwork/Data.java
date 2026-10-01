package ch10_streams.projects.p02_busnetwork;

import java.util.List;

/**
 * Les donnees du projet 2 (DONNEES, ne pas modifier).
 */
public final class Data {

    /**
     * id;premier depart;dernier depart;frequence en minutes;arret:minutes depuis l'arret precedent,...
     * Les horaires sont ceux du PREMIER arret ; le bus met ensuite les minutes indiquees entre deux arrets.
     */
    public static final List<String> LINES = List.of(
            "L1;06:00;21:00;20;Gare:0,Centre:4,Musee:6,Port:5",
            "L2;06:10;20:30;30;Universite:0,Centre:7,Hopital:5,Stade:8",
            "L3;07:00;19:00;45;Port:0,Plage:9,Phare:12",
            "L4;05:30;23:30;15;Stade:0,Hopital:6,Gare:10");

    /** Arrets sans acces pour fauteuil roulant. */
    public static final List<String> NOT_ACCESSIBLE = List.of("Musee", "Phare");

    /** Temps minimum (minutes) pour changer de bus a un arret. */
    public static final int TRANSFER_MINUTES = 2;

    public static final List<String> COMMANDS = List.of(
            "ARRETS",
            "PAGE 2 3",
            "PAGE 4 3",
            "LIGNES",
            "CIRCUIT L1 L3",
            "PROCHAIN L1 Musee 07:52",
            "PROCHAIN L3 Phare 18:30",
            "PROCHAIN L3 Phare 19:30",
            "PROCHAIN L2 Plage 08:00",
            "PROCHAIN L9 Gare 08:00",
            "HORAIRES L4 Gare 22:00 23:00",
            "DIRECT Gare Port 07:00",
            "DIRECT Centre Stade 08:00",
            "DIRECT Stade Centre 08:00",
            "CORRESPONDANCE Universite Port 07:00",
            "CORRESPONDANCE Hopital Port 08:00",
            "CORRESPONDANCE Stade Plage 09:00",
            "ACCESSIBLE L1",
            "ACCESSIBLE L2",
            "TICKETS 3",
            "TICKETS 2",
            "RESEAU",
            "RETARD L1");

    private Data() {
    }
}
