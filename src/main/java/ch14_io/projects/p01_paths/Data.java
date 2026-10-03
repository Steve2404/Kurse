package ch14_io.projects.p01_paths;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier). Tous les chemins sont RELATIFS a la racine du site.
 */
public final class Data {

    /** Les pages du site ; certaines sont ecrites avec des "." et des ".." inutiles. */
    public static final String[] PAGES = {"index.html", "docs/guide/intro.html", "docs/guide/./install.html", "docs/api/index.html",
            "blog/2026/../2025/bilan.html", "blog/2026/janvier.html", "docs/api/io/files.html"};

    /** Les liens "page source -> page cible" a ecrire en chemin RELATIF. */
    public static final String[] LINKS = {"docs/guide/intro.html -> docs/api/index.html", "docs/api/io/files.html -> index.html",
            "blog/2026/janvier.html -> blog/2025/bilan.html", "index.html -> docs/guide/install.html", "docs/api/index.html -> docs/api/io/paths.html"};

    /** Les commandes du mini-shell : "cd <chemin>" ; on ne peut pas remonter au-dessus de la racine. */
    public static final String[] COMMANDS = {"cd docs", "cd guide", "cd ../api/./io", "cd ../../..", "cd ..", "cd blog/2026/../2025", "cd /tmp", "cd docs//api"};

    private Data() {
    }
}
