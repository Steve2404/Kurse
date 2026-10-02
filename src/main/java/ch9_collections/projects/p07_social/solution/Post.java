package ch9_collections.projects.p07_social.solution;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * SOLUTION - une publication. Les tags sont copies dans un Set IMMUABLE (Set.copyOf) : le record reste immuable.
 */
public record Post(int id, String author, int time, int likes, Set<String> tags) {

    public Post {
        tags = Set.copyOf(tags);
    }

    public static Post parse(String line) {
        String[] p = line.split(" ");
        return new Post(Integer.parseInt(p[0]), p[1], Integer.parseInt(p[2]), Integer.parseInt(p[3]), new TreeSet<>(Arrays.asList(p[4].split(","))));
    }

    @Override
    public String toString() {
        return "#" + id + "(" + author + " " + time + "h)";
    }
}
