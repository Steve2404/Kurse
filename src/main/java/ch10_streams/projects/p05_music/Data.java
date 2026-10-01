package ch10_streams.projects.p05_music;

import java.util.List;

/**
 * Les donnees du projet 5 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** utilisateur;artiste;titre;genre;secondes ecoutees;ambiances separees par | */
    public static final List<String> PLAYS = List.of(
            "lea;Queen;Bohemian;Rock;354;epique|live",
            "lea;Muse;Uprising;Rock;305;energie",
            "lea;Adele;Hello;Pop;295;calme|voix",
            "lea;Miles;So What;Jazz;562;calme|nuit",
            "lea;Daft;One More Time;Electro;320;fete",
            "lea;Dua;Levitating;Pop;12;fete",
            "hugo;Queen;Bohemian;Rock;354;epique|live",
            "hugo;Queen;Radio Ga Ga;Rock;343;live",
            "hugo;Muse;Uprising;Rock;20;energie",
            "hugo;Daft;Around;Electro;429;fete|nuit",
            "hugo;Justice;Genesis;Electro;234;energie|nuit",
            "ines;Miles;So What;Jazz;562;calme|nuit",
            "ines;Nina;Feeling Good;Jazz;173;voix",
            "ines;Adele;Hello;Pop;295;calme|voix",
            "ines;Queen;Bohemian;Rock;200;epique",
            "ines;Miles;Blue in Green;Jazz;337;calme",
            "tom;Dua;Levitating;Pop;203;fete",
            "tom;Dua;Physical;Pop;193;energie|fete",
            "tom;Adele;Hello;Pop;8;voix",
            "tom;Justice;Genesis;Electro;234;energie",
            "tom;Daft;One More Time;Electro;320;fete",
            "zoe;Muse;Uprising;Rock;15;energie",
            "zoe;Nina;Feeling Good;Jazz;9;voix",
            "lea;Nina;Feeling Good;Jazz;173;voix");

    /** Une ecoute n'est "validee" (comptee pour l'artiste) qu'a partir de ce nombre de secondes. */
    public static final int VALID_SECONDS = 30;

    private Data() {
    }
}
