package ch7_beyondclasses.drills;

/**
 * Les donnees du "catalogue de romans" partagees par les drills du chapitre 7.
 * ===========================================================================
 *
 * Toujours les memes livres : ton cerveau se concentre sur les types
 * (interfaces, enums, sealed, records, classes imbriquees).
 *
 *   TITLES : {"Dune", "Fondation", "Hyperion", "Solaris"}
 *   YEARS  : {1965, 1951, 1989, 1961}
 *   PAGES  : {412, 255, 482, 204}        (somme 1353)
 *   SIZES  : {"L", "M", "L", "S"}        (format du livre, voir l'enum Size du Drill02)
 */
public final class Catalog {

    public static final String[] TITLES = {"Dune", "Fondation", "Hyperion", "Solaris"};

    public static final int[] YEARS = {1965, 1951, 1989, 1961};

    public static final int[] PAGES = {412, 255, 482, 204};

    public static final String[] SIZES = {"L", "M", "L", "S"};

    private Catalog() {
    }
}
