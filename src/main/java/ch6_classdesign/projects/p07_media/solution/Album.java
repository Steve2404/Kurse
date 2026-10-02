package ch6_classdesign.projects.p07_media.solution;

/**
 * SOLUTION - un album.
 */
public class Album extends AudioMedia {

    public Album(String title, int year, String[] tags, String artist, int[] tracks) {
        super(title, year, tags, artist, tracks);
    }

    @Override
    public String kind() {
        return "album";
    }
}
