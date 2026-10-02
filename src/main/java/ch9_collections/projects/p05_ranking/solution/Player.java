package ch9_collections.projects.p05_ranking.solution;

/**
 * SOLUTION - un joueur, avec un ORDRE NATUREL (Comparable) : par nom.
 * Les noms etant uniques, compareTo est coherent avec equals (compareTo == 0 seulement pour des joueurs egaux).
 */
public record Player(String name, String team, int score, int age, Integer bonus) implements Comparable<Player> {

    public static Player parse(String line) {
        String[] p = line.split(" ");
        return new Player(p[0], p[1], Integer.parseInt(p[2]), Integer.parseInt(p[3]), p[4].equals("-") ? null : Integer.valueOf(p[4]));
    }

    @Override
    public int compareTo(Player other) {
        return name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
