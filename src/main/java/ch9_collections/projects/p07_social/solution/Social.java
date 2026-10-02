package ch9_collections.projects.p07_social.solution;

import ch9_collections.projects.p07_social.Data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * SOLUTION du projet 7 (capstone) - le reseau social.
 */
public class Social {

    // Un curseur dans la liste (triee) des publications d'un auteur : pour la fusion de k listes.
    record Cursor(List<Post> posts, int index) {
        Post current() {
            return posts.get(index);
        }
    }

    public static void main(String[] args) {
        Map<String, Set<String>> friends = new TreeMap<>();
        UnionFind<String> communities = new UnionFind<>();
        for (String f : Data.FRIENDS) {
            String[] p = f.split(" ");
            friends.computeIfAbsent(p[0], k -> new TreeSet<>()).add(p[1]);
            friends.computeIfAbsent(p[1], k -> new TreeSet<>()).add(p[0]);
            communities.add(p[0]);
            communities.add(p[1]);
            communities.union(p[0], p[1]);
        }
        String me = Data.ME;
        System.out.println(me + " : " + friends.get(me) + " ; " + friends.size() + " membres ; communautes " + communities.groups() + " (" + communities.unions()
                + " unions)");

        // Suggestions : amis d'amis, comptes avec merge, tries par nombre d'amis communs puis par nom.
        Map<String, Integer> mutual = new HashMap<>();
        for (String f : friends.get(me)) {
            for (String ff : friends.get(f)) {
                if (!ff.equals(me) && !friends.get(me).contains(ff)) {
                    mutual.merge(ff, 1, Integer::sum);
                }
            }
        }
        List<Map.Entry<String, Integer>> suggestions = new ArrayList<>(mutual.entrySet());
        suggestions.sort(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()));
        Set<String> common = new TreeSet<>(friends.get("bob"));
        common.retainAll(friends.get("chloe"));
        System.out.println("suggestions : " + suggestions + " ; amis communs bob/chloe " + common);

        // Degres de separation : parcours en largeur depuis me.
        Map<String, Integer> degree = new TreeMap<>();
        Queue<String> queue = new ArrayDeque<>();
        degree.put(me, 0);
        queue.add(me);
        while (!queue.isEmpty()) {
            String c = queue.remove();
            for (String n : friends.get(c)) {
                if (!degree.containsKey(n)) {
                    degree.put(n, degree.get(c) + 1);
                    queue.add(n);
                }
            }
        }
        Set<String> unreachable = new TreeSet<>(friends.keySet());
        unreachable.removeAll(degree.keySet());
        System.out.println("degres : " + degree + " ; injoignables " + unreachable);

        // Publications par auteur, chaque liste triee par heure DECROISSANTE.
        Map<String, List<Post>> byAuthor = new HashMap<>();
        List<Post> all = new ArrayList<>();
        for (String line : Data.POSTS) {
            Post p = Post.parse(line);
            all.add(p);
            byAuthor.computeIfAbsent(p.author(), k -> new ArrayList<>()).add(p);
        }
        Comparator<Post> newestFirst = Comparator.comparingInt(Post::time).reversed();
        byAuthor.values().forEach(list -> list.sort(newestFirst));

        // Le fil de me : fusion des k listes de ses amis (et des siennes) avec un tas de curseurs. O(n log k).
        PriorityQueue<Cursor> heap = new PriorityQueue<>(Comparator.comparing(Cursor::current, newestFirst));
        Set<String> sources = new TreeSet<>(friends.get(me));
        sources.add(me);
        for (String s : sources) {
            List<Post> posts = byAuthor.getOrDefault(s, List.of());
            if (!posts.isEmpty()) {
                heap.add(new Cursor(posts, 0));
            }
        }
        List<Post> feed = new ArrayList<>();
        while (!heap.isEmpty() && feed.size() < Data.FEED) {
            Cursor c = heap.poll();
            feed.add(c.current());
            if (c.index() + 1 < c.posts().size()) {
                heap.add(new Cursor(c.posts(), c.index() + 1));
            }
        }
        System.out.println("fil de " + me + " : " + feed);

        // Tags tendance : somme des likes par tag, puis les k meilleurs avec un tas MIN de taille k.
        Map<String, Integer> tagLikes = new TreeMap<>();
        for (Post p : all) {
            for (String t : p.tags()) {
                tagLikes.merge(t, p.likes(), Integer::sum);
            }
        }
        PriorityQueue<Map.Entry<String, Integer>> top = new PriorityQueue<>(Map.Entry.comparingByValue());
        for (Map.Entry<String, Integer> e : tagLikes.entrySet()) {
            top.offer(e);
            if (top.size() > Data.TRENDING) {
                top.poll();
            }
        }
        List<Map.Entry<String, Integer>> trending = new ArrayList<>(top);
        trending.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        System.out.println("likes par tag : " + tagLikes + " ; tendances " + trending);

        // Les plus connectes, et un Set immuable qui refuse toute modification.
        List<String> members = new ArrayList<>(friends.keySet());
        members.sort(Comparator.comparing((String m) -> friends.get(m).size()).reversed().thenComparing(Comparator.naturalOrder()));
        Set<String> tags = all.get(0).tags();
        // Set.copyOf d'un Set deja immuable rend LA MEME instance (pas de copie inutile).
        System.out.println("plus connectes : " + members.subList(0, 3) + " ; copyOf d'un set immuable = meme objet " + (Set.copyOf(tags) == tags)
                + " " + new TreeSet<>(tags));
    }
}
