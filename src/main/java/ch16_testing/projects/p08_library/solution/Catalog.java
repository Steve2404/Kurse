package ch16_testing.projects.p08_library.solution;

/** Le catalogue : combien d'exemplaires de chaque livre la mediatheque possede (0 = livre inconnu). */
public interface Catalog {

    int copies(String isbn);
}
