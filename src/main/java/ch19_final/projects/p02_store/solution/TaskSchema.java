package ch19_final.projects.p02_store.solution;

import java.util.List;

/** Le schema des taches, en trois evolutions : comme une vraie application qui a grandi. */
public final class TaskSchema {

    private TaskSchema() {
    }

    public static final List<Migration> MIGRATIONS = List.of(
            new Migration(1, "taches", List.of(
                    "CREATE TABLE task (id BIGINT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(200) NOT NULL, "
                            + "col VARCHAR(20) NOT NULL, points INT NOT NULL CHECK (points >= 0))")),
            new Migration(2, "historique des deplacements", List.of(
                    "CREATE TABLE task_event (id BIGINT AUTO_INCREMENT PRIMARY KEY, task_id BIGINT NOT NULL REFERENCES task(id), "
                            + "from_col VARCHAR(20) NOT NULL, to_col VARCHAR(20) NOT NULL, at TIMESTAMP NOT NULL, "
                            + "CHECK (from_col <> to_col))")),
            new Migration(3, "verrou optimiste et index", List.of(
                    "ALTER TABLE task ADD COLUMN version INT DEFAULT 0 NOT NULL",
                    "CREATE INDEX idx_task_col ON task(col)")));
}
