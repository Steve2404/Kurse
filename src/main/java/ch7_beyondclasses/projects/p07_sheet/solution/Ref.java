package ch7_beyondclasses.projects.p07_sheet.solution;

/**
 * SOLUTION - une reference de cellule : "B3" = colonne 1, ligne 2 (a partir de 0).
 */
public record Ref(int col, int row) {

    public static Ref parse(String text) {
        return new Ref(text.charAt(0) - 'A', Integer.parseInt(text.substring(1)) - 1);
    }

    @Override
    public String toString() {
        return "" + (char) ('A' + col) + (row + 1);
    }
}
