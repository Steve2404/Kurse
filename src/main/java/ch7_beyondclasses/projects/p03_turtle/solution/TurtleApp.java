package ch7_beyondclasses.projects.p03_turtle.solution;

import ch7_beyondclasses.projects.p03_turtle.Data;

/**
 * SOLUTION du projet 3 - la tortue.
 */
public class TurtleApp {

    private static String[] tokens;
    private static int pos;

    // Analyse recursive : un bloc se termine a "]" ou a la fin du texte.
    static Command[] parseBlock() {
        Command[] out = new Command[4];
        int n = 0;
        while (pos < tokens.length && !tokens[pos].equals("]")) {
            String word = tokens[pos++];
            Command c = switch (word) {
                case "MOVE" -> new Move(Integer.parseInt(tokens[pos++]));
                case "RIGHT" -> Turn.RIGHT;
                case "LEFT" -> Turn.LEFT;
                case "PENUP" -> new PenUp();
                case "PENDOWN" -> new PenDown();
                case "SQUARE" -> new Square(Integer.parseInt(tokens[pos++]));
                case "STAIRS" -> new Stairs(Integer.parseInt(tokens[pos++]));
                default -> {                       // REPEAT n [ ... ]
                    int times = Integer.parseInt(tokens[pos++]);
                    pos++;                         // "["
                    Command[] body = parseBlock();
                    pos++;                         // "]"
                    yield new Repeat(times, body);
                }
            };
            if (n == out.length) {
                out = java.util.Arrays.copyOf(out, n * 2);
            }
            out[n++] = c;
        }
        return java.util.Arrays.copyOf(out, n);
    }

    static Command[] parse(String program) {
        tokens = program.split(" ");
        pos = 0;
        return parseBlock();
    }

    public static void main(String[] args) {
        Command[] program = parse(Data.PROGRAM);
        int size = 0;
        int depth = 0;
        StringBuilder kinds = new StringBuilder();
        for (Command c : program) {
            size += c.size();
            if (c instanceof Repeat r) {
                depth = Math.max(depth, r.depth());
            }
            kinds.append(c instanceof Macro m ? m.name() : c.getClass().getSimpleName()).append(' ');
        }
        System.out.println("programme : " + program.length + " commandes " + kinds.toString().strip());
        System.out.println("deplie : " + size + " commandes elementaires, imbrication " + depth);
        System.out.println("records : " + new Move(3) + " " + new Move(-2) + " " + Turn.RIGHT + " " + new PenUp() + " egal " + new Move(3).equals(new Move(3))
                + " ; Repeat.equals compare les tableaux par reference : " + new Repeat(1, new Command[0]).equals(new Repeat(1, new Command[0])));
        Turtle t = new Turtle(Data.ROWS, Data.COLS, 1, 1);
        for (Command c : program) {
            t.run(c);
        }
        for (String line : t.picture()) {
            System.out.println(line);
        }
        System.out.println(t.report());
    }
}
