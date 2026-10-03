package ch14_io.projects.p07_vcs.solution;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * SOLUTION - un commit : son numero, son message, son parent (0 : aucun) et la photo du depot (chemin -> empreinte).
 * Un record serialisable ; TreeMap l'est aussi.
 */
public record Commit(int id, String message, int parent, TreeMap<String, String> snapshot) implements Serializable {
}
