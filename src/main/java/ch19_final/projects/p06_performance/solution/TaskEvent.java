package ch19_final.projects.p06_performance.solution;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Une ligne du journal, lue UNE fois. Le legacy redecoupait chaque ligne (split) des milliers de fois.
 * Le motif est compile une seule fois (static final) : String.matches le recompilait a chaque ligne.
 * Les bizarreries du legacy sont gardees telles quelles : 2026-13-45 est une date "valide", le titre peut
 * contenir des ';'. On ne corrige rien pendant une optimisation (chapitre 18).
 */
public record TaskEvent(String day, String who, String column, int points) {

    private static final Pattern LINE = Pattern.compile("\\d{4}-\\d{2}-\\d{2};[a-z]+;[^;]+;\\d{1,4};.+");

    public static Optional<TaskEvent> parse(String line) {
        if (!LINE.matcher(line).matches()) {
            return Optional.empty();
        }
        int a = line.indexOf(';');
        int b = line.indexOf(';', a + 1);
        int c = line.indexOf(';', b + 1);
        int d = line.indexOf(';', c + 1);
        return Optional.of(new TaskEvent(line.substring(0, a), line.substring(a + 1, b), line.substring(b + 1, c),
                Integer.parseInt(line, c + 1, d, 10)));
    }
}
