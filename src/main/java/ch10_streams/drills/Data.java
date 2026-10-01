package ch10_streams.drills;

import java.util.List;

/**
 * Les donnees communes a tous les drills de rappel (DONNEES, ne pas modifier).
 */
public final class Data {

    /** titre;auteur;genre;annee;pages;prix */
    public static final List<String> BOOKS = List.of(
            "Dune;Herbert;SF;1965;412;9.90",
            "Fondation;Asimov;SF;1951;255;8.50",
            "Hyperion;Simmons;SF;1989;482;10.90",
            "Le Hobbit;Tolkien;Fantasy;1937;310;7.90",
            "Le Silmarillion;Tolkien;Fantasy;1977;365;12.00",
            "Neuromancien;Gibson;SF;1984;271;8.90",
            "Germinal;Zola;Classique;1885;592;6.50",
            "L'Assommoir;Zola;Classique;1877;512;6.50");

    public static final List<String> WORDS = List.of(
            "stream", "lambda", "optional", "java", "stream", "collector", "map", "java", "filter");

    public static final int[] NUMBERS = {5, 3, 8, 1, 9, 2, 8, 7};

    private Data() {
    }
}
