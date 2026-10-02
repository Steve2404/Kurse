package ch7_beyondclasses.projects.p04_routes.solution;

/**
 * SOLUTION - une ville. Un record qui implemente une interface : lat() et lon() sont ses accesseurs generes.
 */
public record City(String name, double lat, double lon) implements Located {

    // Un record peut avoir des champs STATIC (pas de champ d'instance en plus des composants).
    private static int created;

    // Constructeur COMPACT : on corrige les parametres, puis Java affecte les champs tout seul.
    public City {
        name = name.strip();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        lat = Math.max(-90, Math.min(90, lat));
        lon = Math.max(-180, Math.min(180, lon));
        created++;
    }

    // Un constructeur supplementaire DOIT deleguer au canonique avec this(...).
    public City(String name) {
        this(name, 0, 0);
    }

    public static City parse(String line) {
        String[] p = line.split("\\|");
        return new City(p[0], Double.parseDouble(p[1]), Double.parseDouble(p[2]));
    }

    public static int created() {
        return created;
    }

    // Une methode d'instance ordinaire dans un record.
    public String initials() {
        return name.substring(0, 3).toUpperCase();
    }
}
