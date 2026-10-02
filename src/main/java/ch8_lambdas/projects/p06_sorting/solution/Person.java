package ch8_lambdas.projects.p06_sorting.solution;

/**
 * SOLUTION - une personne.
 */
public record Person(String name, int age, String city, int score) {

    public static Person parse(String line) {
        String[] p = line.split(" ");
        return new Person(p[0], Integer.parseInt(p[1]), p[2], Integer.parseInt(p[3]));
    }

    @Override
    public String toString() {
        return name;
    }
}
