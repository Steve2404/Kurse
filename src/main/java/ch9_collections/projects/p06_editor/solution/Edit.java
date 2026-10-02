package ch9_collections.projects.p06_editor.solution;

/**
 * SOLUTION - une modification reversible : son type, la ligne, le texte avant et apres.
 */
public record Edit(String type, int index, String before, String after) {
}
