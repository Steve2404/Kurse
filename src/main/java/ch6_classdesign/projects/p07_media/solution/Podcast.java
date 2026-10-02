package ch6_classdesign.projects.p07_media.solution;

/**
 * SOLUTION - un podcast : comme un album, mais on compte les episodes.
 */
public class Podcast extends AudioMedia {

    public Podcast(String title, int year, String[] tags, String host, int[] episodes) {
        super(title, year, tags, host, episodes);
    }

    @Override
    public String kind() {
        return "podcast";
    }

    @Override
    public String toString() {
        return super.toString() + ", " + tracks.length + " episodes";
    }
}
