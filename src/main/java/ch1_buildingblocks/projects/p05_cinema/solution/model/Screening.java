package ch1_buildingblocks.projects.p05_cinema.solution.model;

/**
 * Une seance. Son initialisation est journalisee : champs et bloc dans l'ordre du texte, puis constructeur.
 */
public class Screening {

    static int step;

    int room = log("Seance : champ salle", 7);

    {
        // capacity est declare plus bas : via this, on lit sa valeur par defaut.
        note("Seance : bloc d'initialisation (capacite = " + this.capacity + ")");
    }

    int capacity = log("Seance : champ capacite", 120);
    String film;
    String version;

    public Screening(String film, String version) {
        this.film = film;
        this.version = version;
        note("Seance : constructeur (film = " + this.film + ")");
    }

    static void note(String message) {
        step = step + 1;
        System.out.println("[" + step + "] " + message);
    }

    static int log(String field, int value) {
        note(field + " = " + value);
        return value;
    }

    public String title() {
        return film + " (" + version + "), salle " + room;
    }

    public int seatsLeft(int sold) {
        return capacity - sold;
    }
}
