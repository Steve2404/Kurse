package ch9_collections.projects.p06_editor;

/**
 * Les donnees du projet 6 (DONNEES, ne pas modifier).
 * Commandes : "ADD texte", "INSERT i texte", "SET i texte", "DELETE i", "UNDO", "REDO", "OPEN fichier", "COMPLETE prefixe".
 * Les indices de ligne commencent a 0.
 */
public final class Data {

    public static final String[] COMMANDS = {
            "OPEN main.java", "ADD int x = (a + b;", "ADD list.add(map.get(k));", "INSERT 0 // debut", "SET 1 int x = (a + b);",
            "DELETE 0", "UNDO", "UNDO", "REDO", "OPEN util.java", "ADD if (x) { y[0] = 1; }", "OPEN main.java", "ADD retrun [x);",
            "COMPLETE ma", "COMPLETE li", "COMPLETE zz", "OPEN notes.txt", "UNDO", "REDO", "OPEN test.java"};

    /** Le dictionnaire des mots connus (autocompletion et orthographe). */
    public static final String[] DICTIONARY = {"map", "main", "math", "list", "listiterator", "long", "return", "retain", "int", "if", "add", "get",
            "set", "deque", "queue"};

    /** Les mots a corriger. */
    public static final String[] TYPOS = {"retrun", "mapp", "lsit", "deqeu", "set", "xyz"};

    /** Le nombre de fichiers recents gardes. */
    public static final int RECENT = 3;

    private Data() {
    }
}
