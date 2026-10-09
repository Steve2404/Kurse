package ch19_final.projects.p02_store.solution;

import ch19_final.projects.p02_store.Data;
import org.h2.jdbcx.JdbcDataSource;

import java.time.Clock;
import java.util.List;

/**
 * La demonstration, sur une base dans un FICHIER (build/ch19/atelier.mv.db) : lance-la deux fois.
 * La 1re fois, les 3 migrations s'appliquent et les taches de depart sont ajoutees ; la 2e, rien a migrer.
 */
public final class StoreDemo {

    private StoreDemo() {
    }

    public static void main(String[] args) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:./build/ch19/atelier");
        Transactions tx = new Transactions(dataSource);
        System.out.println("migrations appliquees : " + Migrations.apply(tx, TaskSchema.MIGRATIONS));
        JdbcTaskRepository tasks = new JdbcTaskRepository(tx, Clock.systemDefaultZone());
        seedIfEmpty(tasks);
        Task first = tasks.page("A faire", 0, 1).get(0);
        tasks.move(first.id(), "En cours", first.version());
        System.out.println("par colonne : " + tasks.countByColumn());
        System.out.println("recherche 50% : " + titles(tasks.search("50%")));
        System.out.println("recherche injection : " + titles(tasks.search("x'; DROP TABLE task; --")));
        System.out.println("historique de la tache " + first.id() + " : " + tasks.history(first.id()));
    }

    private static void seedIfEmpty(JdbcTaskRepository tasks) {
        if (tasks.countByColumn().isEmpty()) {
            for (String[] row : Data.SEED) {
                tasks.create(new NewTask(row[0], row[1], Integer.parseInt(row[2])));
            }
        }
    }

    private static List<String> titles(List<Task> list) {
        return list.stream().map(Task::title).toList();
    }
}
