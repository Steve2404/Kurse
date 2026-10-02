package ch6_classdesign.projects.p07_media.solution;

/**
 * SOLUTION - un film.
 */
public class Movie extends Media {

    private final String director;
    private final int minutes;

    public Movie(String title, int year, String[] tags, String director, int minutes) {
        super(title, year, tags);
        this.director = director;
        this.minutes = minutes;
    }

    @Override
    public String kind() {
        return "film";
    }

    @Override
    public int minutes() {
        return minutes;
    }

    @Override
    public boolean matches(String query) {
        return super.matches(query) || director.equalsIgnoreCase(query);
    }

    @Override
    public String toString() {
        return super.toString() + " de " + director;
    }
}
