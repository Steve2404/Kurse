package ch7_beyondclasses.projects.p07_sheet.solution;

import ch7_beyondclasses.projects.p07_sheet.Data;

/**
 * SOLUTION du projet 7 (capstone) - le tableur.
 */
public class SheetApp {

    static String pad(String s) {
        return s.length() >= 9 ? s : s + " ".repeat(9 - s.length());
    }

    static void print(Sheet sheet) {
        StringBuilder header = new StringBuilder("   ");
        for (int c = 0; c < Sheet.COLS; c++) {
            header.append(pad("" + (char) ('A' + c)));
        }
        System.out.println(header.toString().stripTrailing());
        StringBuilder[] lines = new StringBuilder[Sheet.ROWS];
        for (int r = 0; r < Sheet.ROWS; r++) {
            lines[r] = new StringBuilder((r + 1) + "  ");
        }
        // Classe ANONYME : elle remplit les lignes (lines est effectively final).
        sheet.visit(new Sheet.CellVisitor() {
            @Override
            public void visit(Ref ref, Content content, String shown) {
                lines[ref.row()].append(pad(shown));
            }
        });
        for (StringBuilder line : lines) {
            System.out.println(line.toString().stripTrailing());
        }
    }

    public static void main(String[] args) {
        Sheet sheet = new Sheet();
        int numbers = 0;
        int texts = 0;
        int formulas = 0;
        for (String cell : Data.CELLS) {
            sheet.set(cell);
        }
        Sheet.Parser parser = new Sheet.Parser("B2/A1+B1");     // classe static imbriquee : pas besoin d'une feuille
        Expr tree = parser.expr();
        System.out.println("arbre de B3 : " + tree);
        for (String cell : Data.CELLS) {
            String raw = cell.substring(cell.indexOf('=') + 1);
            if (raw.startsWith("'")) {
                texts++;
            } else if (Character.isDigit(raw.charAt(0))) {
                numbers++;
            } else {
                formulas++;
            }
        }
        System.out.println("cellules : " + numbers + " nombres, " + texts + " texte, " + formulas + " formules ; operateurs " + Op.values().length + " ; "
                + Op.of('*') + ".apply(6, 7) = " + Op.TIMES.apply(6, 7));
        sheet.recalculate();
        System.out.println("ordre de calcul : " + sheet.formulaOrder());
        print(sheet);
        sheet.set(Data.CHANGE);
        sheet.recalculate();
        System.out.println("apres " + Data.CHANGE + " :");
        print(sheet);
    }
}
