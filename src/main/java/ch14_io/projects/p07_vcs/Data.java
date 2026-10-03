package ch14_io.projects.p07_vcs;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier) : le scenario joue sur le depot.
 * Dans "write", le texte apres le nom du fichier est son contenu, "/" separant les lignes.
 */
public final class Data {

    public static final String SANDBOX = "build/ch14/p07_vcs";

    public static final String[] SCRIPT = {
            "write recette.txt farine/oeufs/lait/sucre",
            "write courses.txt pain/beurre",
            "commit premiere version",
            "write recette.txt farine/oeufs/lait entier/sucre/vanille",
            "delete courses.txt",
            "write notes.txt cuisson 20 min",
            "status",
            "commit ajout vanille",
            "write recette.txt farine/lait entier/sucre/vanille/sel",
            "status",
            "commit sans oeufs",
            "log",
            "diff 1 3 recette.txt",
            "checkout 1",
            "status",
            "cat recette.txt",
            "checkout 3"};

    private Data() {
    }
}
