package ch19_final.projects.p08_atelier.solution;

import java.util.List;

/** Une evolution du schema : un numero, une description, et ses instructions SQL (une par element). */
public record Migration(int version, String description, List<String> statements) {

    public Migration {
        if (version < 1) {
            throw new IllegalArgumentException("version invalide : " + version);
        }
        statements = List.copyOf(statements);
    }
}
