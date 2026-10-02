package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - l'interpreteur : il dessine sur une grille de caracteres.
 */
public class Turtle {

    private final char[][] canvas;
    private int row;
    private int col;
    private Direction dir = Direction.EAST;
    private boolean penDown = true;
    private int distance;
    private int turns;

    public Turtle(int rows, int cols, int row, int col) {
        canvas = new char[rows][cols];
        for (char[] line : canvas) {
            java.util.Arrays.fill(line, ' ');
        }
        this.row = row;
        this.col = col;
        mark();
    }

    private void mark() {
        if (penDown && row >= 0 && row < canvas.length && col >= 0 && col < canvas[0].length) {
            canvas[row][col] = '#';
        }
    }

    // La hierarchie etant scellee, cette chaine de instanceof couvre TOUS les cas possibles.
    public void run(Command c) {
        if (c instanceof Move m) {
            for (int i = 0; i < m.steps(); i++) {
                row += dir.dr;
                col += dir.dc;
                distance++;
                mark();
            }
        } else if (c instanceof Turn t) {
            dir = dir.turn(t.quarters());
            turns++;
        } else if (c instanceof Repeat r) {
            for (int i = 0; i < r.times(); i++) {
                for (Command inner : r.body()) {
                    run(inner);
                }
            }
        } else if (c instanceof PenUp) {
            penDown = false;
        } else if (c instanceof PenDown) {
            penDown = true;
            mark();
        } else if (c instanceof Macro macro) {
            for (Command inner : macro.expand()) {
                run(inner);
            }
        }
    }

    public String report() {
        return "position (" + row + "," + col + ") cap " + dir + ", distance " + distance + ", virages " + turns;
    }

    public String[] picture() {
        int last = canvas.length - 1;
        while (last > 0 && new String(canvas[last]).isBlank()) {
            last--;
        }
        String[] out = new String[last + 1];
        for (int r = 0; r <= last; r++) {
            out[r] = "|" + new String(canvas[r]).stripTrailing();
        }
        return out;
    }
}
