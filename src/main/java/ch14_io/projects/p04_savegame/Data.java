package ch14_io.projects.p04_savegame;

/**
 * Les donnees du projet 4 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final String SANDBOX = "build/ch14/p04_savegame";

    /** Les actions de la partie : "loot nom puissance", "level", "drop nom", "undo". */
    public static final String[] ACTIONS = {"loot epee 7", "level", "loot bouclier 4", "undo", "loot arc 5", "level", "drop epee", "undo", "undo", "level"};

    private Data() {
    }
}
