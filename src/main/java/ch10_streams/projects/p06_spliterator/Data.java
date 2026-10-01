package ch10_streams.projects.p06_spliterator;

import java.util.List;

/**
 * Les donnees du projet 6 (DONNEES, ne pas modifier).
 */
public final class Data {

    /**
     * Le journal de caisse d'une librairie, ligne par ligne, comme lu dans un fichier.
     *   "TX <id> <client>"                 ouvre une transaction ;
     *   "  + <qte> x <article> a <prix>"   ligne d'achat (indentee de 2 espaces) ;
     *   "  - <qte> x <article> a <prix>"   ligne de retour (rembourse) ;
     *   "# ..."                            commentaire, a ignorer.
     * Une transaction peut n'avoir aucune ligne. Une ligne indentee peut etre illisible.
     */
    public static final List<String> LOG = List.of(
            "TX 1001 lea",
            "  + 2 x Dune a 12.50",
            "  + 1 x Fondation a 9.90",
            "TX 1002 hugo",
            "  + 3 x Hobbit a 8.00",
            "# reprise apres coupure reseau",
            "TX 1003 ines",
            "TX 1004 tom",
            "  + 1 x Dune a 12.50",
            "  + 4 x Carnet a 2.25",
            "  - 1 x Carnet a 2.25",
            "TX 1005 lea",
            "  + 2 x",
            "  + 1 x Stylo a 1.20",
            "TX 1006 hugo",
            "  + 1 x Fondation a 9.90",
            "  + 2 x Dune a 12.50",
            "TX 1007 zoe",
            "  + 10 x Marque-page a 0.50",
            "TX 1008 ines",
            "  + 1 x Hobbit a 8.00",
            "  - 1 x Hobbit a 8.00");

    /** En dessous de ce nombre de lignes, ton Spliterator refuse de se couper en deux. */
    public static final int MIN_SPLIT_LINES = 4;

    /** Les commandes a repartir entre les preparateurs. */
    public static final List<String> JOBS = List.of("J01", "J02", "J03", "J04", "J05", "J06", "J07", "J08", "J09", "J10");

    /** Taille maximale d'un lot de preparation. */
    public static final int MAX_BATCH = 3;

    private Data() {
    }
}
