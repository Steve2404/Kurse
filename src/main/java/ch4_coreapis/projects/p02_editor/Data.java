package ch4_coreapis.projects.p02_editor;

/**
 * Les donnees du projet 2 (DONNEES, ne pas modifier).
 * Chaque commande tient sur une ligne : NOM puis ses parametres separes par une espace ;
 * le dernier parametre (le texte) peut lui-meme contenir des espaces.
 */
public final class Data {

    public static final String[] COMMANDS = {
        "APPEND Bonjour monde",
        "INSERT 8 le grand ",
        "FIND monde",
        "REPLACE 11 16 GRAND",
        "DELETE 0 8",
        "UNDO",
        "DELCHAR 7",
        "CUT 0 8",
        "PASTE 99 !",
        "PASTE 0",
        "REVERSE",
        "REVERSE",
        "UNDO",
        "UNDO",
        "UNDO",
        "SHOUT",
        "UNDO",
        "UNDO",
        "UNDO",
        "UNDO",
        "UNDO",
        "UNDO",
        "LENGTH"
    };

    /** Taille maximale de l'historique d'annulation. */
    public static final int HISTORY = 5;

    private Data() {
    }
}
