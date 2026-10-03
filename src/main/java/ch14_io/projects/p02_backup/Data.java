package ch14_io.projects.p02_backup;

/**
 * Les donnees du projet 2 (DONNEES, ne pas modifier).
 */
public final class Data {

    /** Le bac a sable, relatif a la racine du depot ; il est entierement recree a chaque lancement. */
    public static final String SANDBOX = "build/ch14/p02_backup";

    /** Les fichiers de depart : "chemin relatif|contenu". */
    public static final String[] FILES = {"notes/todo.txt|acheter du pain\nappeler Lea", "notes/idees.txt|un jeu de cartes",
            "photos/2026/plage.jpg|JPEG-PLAGE-0001", "photos/2026/montagne.jpg|JPEG-MONTAGNE-0002", "budget.csv|mois;montant\njanvier;1200",
            "photos/2025/neige.jpg|JPEG-NEIGE-0003"};

    private Data() {
    }
}
