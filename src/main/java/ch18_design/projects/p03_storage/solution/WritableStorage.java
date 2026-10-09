package ch18_design.projects.p03_storage.solution;

/**
 * Un stockage qu'on peut aussi modifier. Il EST un ReadableStorage (il respecte tout son contrat), plus :
 * - apres write(k, v), read(k) rend v ; une valeur null est refusee (IllegalArgumentException) ;
 * - delete(k) rend true si la cle existait, false sinon ; ensuite read(k) est vide.
 * L'archive n'implemente PAS cette interface : elle n'a pas a mentir sur ce qu'elle sait faire.
 */
public interface WritableStorage extends ReadableStorage {

    void write(String key, String value);

    boolean delete(String key);
}
