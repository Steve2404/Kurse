package ch10_streams.projects.p07_warehouse;

import java.util.List;

/**
 * Les donnees du projet 7 - CAPSTONE (DONNEES, ne pas modifier).
 */
public final class Data {

    /** sku;nom;categorie;stock;prix unitaire */
    public static final List<String> PRODUCTS = List.of(
            "A1;Clavier;Info;5;49.90",
            "A2;Souris;Info;10;19.90",
            "A3;Ecran;Info;2;189.00",
            "B1;Lampe;Maison;4;29.90",
            "B2;Tapis;Maison;0;39.00",
            "C1;Velo;Sport;1;349.00",
            "C2;Gourde;Sport;20;9.90");

    /** id;nom;region;niveau (GOLD ou STANDARD);email (peut manquer ou etre blanc) */
    public static final List<String> CUSTOMERS = List.of(
            "K1;Lea;Nord;GOLD;lea@mail.fr",
            "K2;Hugo;Sud;STANDARD;",
            "K3;Ines;Nord;STANDARD;ines@mail.fr",
            "K4;Tom;Est;GOLD;  ",
            "K5;Zoe;Sud;STANDARD;zoe@mail.fr");

    /** id;client;date;lignes "skuxquantite" separees par des virgules */
    public static final List<String> ORDERS = List.of(
            "O1;K2;2026-09-01;A1x3,A2x2",
            "O2;K1;2026-09-02;A1x2,A3x1,C2x5",
            "O3;K3;2026-09-01;A3x2,B1x1",
            "O4;K4;2026-09-03;C1x1,B2x2",
            "O5;K9;2026-09-03;A2x1",
            "O6;K5;2026-09-02;C1x1,C2x3",
            "O7;K2;2026-09-04;Z9x1,B1x2",
            "O8;K4;2026-09-01;A1x1,B1x2",
            "O9;K3;2026-09-05;B2x1");

    private Data() {
    }
}
