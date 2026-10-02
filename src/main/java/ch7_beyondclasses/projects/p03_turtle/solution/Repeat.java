package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - repeter un bloc : un record recursif (il contient d'autres Command).
 */
public record Repeat(int times, Command[] body) implements Command {

    public Repeat {
        body = body.clone();   // copie defensive : un record ne protege pas le CONTENU d'un tableau
    }

    @Override
    public Command[] body() {
        return body.clone();
    }

    @Override
    public int size() {
        int one = 0;
        for (Command c : body) {
            one += c.size();
        }
        return times * one;
    }

    public int depth() {
        int deepest = 0;
        for (Command c : body) {
            if (c instanceof Repeat r) {
                deepest = Math.max(deepest, r.depth());
            }
        }
        return 1 + deepest;
    }
}
