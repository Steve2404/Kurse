package ch7_beyondclasses.projects.p07_sheet.solution;

/**
 * SOLUTION - la feuille de calcul.
 */
public class Sheet {

    public static final int COLS = 4;
    public static final int ROWS = 3;

    // Interface imbriquee : ce qu'on fait de chaque cellule (utilisee avec une classe anonyme).
    public interface CellVisitor {
        void visit(Ref ref, Content content, String shown);
    }

    // Classe imbriquee STATIC : l'analyseur n'a besoin d'aucune feuille.
    static class Parser {
        private final String text;
        private int pos;

        Parser(String text) {
            this.text = text;
        }

        Expr expr() {
            Expr e = term();
            while (pos < text.length() && (text.charAt(pos) == '+' || text.charAt(pos) == '-')) {
                Op op = Op.of(text.charAt(pos++));
                e = new Expr.Binary(op, e, term());
            }
            return e;
        }

        Expr term() {
            Expr e = factor();
            while (pos < text.length() && (text.charAt(pos) == '*' || text.charAt(pos) == '/')) {
                Op op = Op.of(text.charAt(pos++));
                e = new Expr.Binary(op, e, factor());
            }
            return e;
        }

        Expr factor() {
            char c = text.charAt(pos);
            if (c == '(') {
                pos++;
                Expr e = expr();
                pos++;
                return e;
            }
            if (text.startsWith("SUM(", pos)) {
                int colon = text.indexOf(':', pos);
                int close = text.indexOf(')', pos);
                Expr e = new Expr.Sum(Ref.parse(text.substring(pos + 4, colon)), Ref.parse(text.substring(colon + 1, close)));
                pos = close + 1;
                return e;
            }
            int start = pos;
            if (Character.isLetter(c)) {
                pos++;
                while (pos < text.length() && Character.isDigit(text.charAt(pos))) {
                    pos++;
                }
                return new Expr.Cell(Ref.parse(text.substring(start, pos)));
            }
            while (pos < text.length() && (Character.isDigit(text.charAt(pos)) || text.charAt(pos) == '.')) {
                pos++;
            }
            return new Expr.Num(Double.parseDouble(text.substring(start, pos)));
        }
    }

    private final Content[][] cells = new Content[COLS][ROWS];
    private final double[][] values = new double[COLS][ROWS];
    private final boolean[][] inCycle = new boolean[COLS][ROWS];
    private Ref[] order = new Ref[0];

    public void set(String assignment) {
        int eq = assignment.indexOf('=');
        Ref ref = Ref.parse(assignment.substring(0, eq));
        String raw = assignment.substring(eq + 1);
        Content content;
        if (raw.startsWith("'")) {
            content = new Content.Text(raw.substring(1));
        } else if (Character.isDigit(raw.charAt(0))) {
            content = new Content.Number(Double.parseDouble(raw));
        } else {
            content = new Content.Formula(raw, new Parser(raw).expr());
        }
        cells[ref.col()][ref.row()] = content;
    }

    static boolean inside(Ref r) {
        return r.col() >= 0 && r.col() < COLS && r.row() >= 0 && r.row() < ROWS;
    }

    // Classe INTERNE : elle lit directement cells et values de LA feuille qui l'a creee (Sheet.this).
    class Evaluator {
        double eval(Expr e) {
            if (e instanceof Expr.Num n) {
                return n.value();
            } else if (e instanceof Expr.Cell c) {
                return inside(c.ref()) ? Sheet.this.values[c.ref().col()][c.ref().row()] : 0;   // hors feuille : 0
            } else if (e instanceof Expr.Binary b) {
                return b.op().apply(eval(b.left()), eval(b.right()));
            } else if (e instanceof Expr.Sum s) {
                double total = 0;
                for (int col = s.from().col(); col <= s.to().col(); col++) {
                    for (int row = s.from().row(); row <= s.to().row(); row++) {
                        total += values[col][row];
                    }
                }
                return total;
            }
            return 0;
        }
    }

    // Tri topologique de Kahn : une formule est calculee apres toutes les cellules dont elle depend.
    // Les cellules jamais atteintes (degre entrant jamais nul) forment ou dependent d'un cycle.
    public void recalculate() {
        // Classe LOCALE : un petit graphe, visible seulement dans cette methode.
        class Graph {
            final int n = COLS * ROWS;
            final boolean[][] edge = new boolean[n][n];   // edge[a][b] : b depend de a
            final int[] inDegree = new int[n];

            int id(Ref r) {
                return r.col() * ROWS + r.row();
            }

            void add(Ref from, Ref to) {
                if (inside(from) && !edge[id(from)][id(to)]) {
                    edge[id(from)][id(to)] = true;
                    inDegree[id(to)]++;
                }
            }
        }
        Graph g = new Graph();
        Ref[] deps = new Ref[g.n];
        for (int col = 0; col < COLS; col++) {
            for (int row = 0; row < ROWS; row++) {
                if (cells[col][row] instanceof Content.Formula f) {
                    int k = Expr.references(f.expr(), deps, 0);
                    for (int i = 0; i < k; i++) {
                        g.add(deps[i], new Ref(col, row));
                    }
                }
            }
        }
        int[] queue = new int[g.n];
        int head = 0;
        int tail = 0;
        for (int i = 0; i < g.n; i++) {
            if (g.inDegree[i] == 0) {
                queue[tail++] = i;
            }
        }
        Ref[] sorted = new Ref[g.n];
        int count = 0;
        Evaluator ev = new Evaluator();          // equivalent a this.new Evaluator()
        while (head < tail) {
            int u = queue[head++];
            Ref r = new Ref(u / ROWS, u % ROWS);
            sorted[count++] = r;
            Content c = cells[r.col()][r.row()];
            inCycle[r.col()][r.row()] = false;
            if (c instanceof Content.Number num) {
                values[r.col()][r.row()] = num.value();
            } else if (c instanceof Content.Formula f) {
                values[r.col()][r.row()] = ev.eval(f.expr());
            } else {
                values[r.col()][r.row()] = 0;
            }
            for (int v = 0; v < g.n; v++) {
                if (g.edge[u][v] && --g.inDegree[v] == 0) {
                    queue[tail++] = v;
                }
            }
        }
        for (int i = 0; i < g.n; i++) {
            if (g.inDegree[i] > 0) {
                inCycle[i / ROWS][i % ROWS] = true;
            }
        }
        order = java.util.Arrays.copyOf(sorted, count);
    }

    public String formulaOrder() {
        StringBuilder sb = new StringBuilder();
        for (Ref r : order) {
            if (cells[r.col()][r.row()] instanceof Content.Formula) {
                sb.append(r).append(' ');
            }
        }
        return sb.toString().strip();
    }

    public String shown(Ref r) {
        Content c = cells[r.col()][r.row()];
        if (inCycle[r.col()][r.row()]) {
            return "#CYCLE";
        }
        if (c instanceof Content.Text t) {
            return t.text();
        }
        if (c == null) {
            return "";
        }
        double v = values[r.col()][r.row()];
        if (Double.isNaN(v)) {
            return "#DIV0";
        }
        return v == Math.rint(v) ? String.valueOf((long) v) : String.valueOf(Math.round(v * 100) / 100.0);
    }

    public void visit(CellVisitor visitor) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Ref r = new Ref(col, row);
                visitor.visit(r, cells[col][row], shown(r));
            }
        }
    }
}
