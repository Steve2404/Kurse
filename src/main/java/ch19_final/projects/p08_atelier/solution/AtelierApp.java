package ch19_final.projects.p08_atelier.solution;

import ch19_final.projects.p08_atelier.Data;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.Clock;
import java.util.function.Consumer;

/**
 * La RACINE DE COMPOSITION (chapitre 18) : le seul endroit qui fabrique et branche toutes les pieces.
 * Les tests appellent create avec une base en memoire et une horloge manuelle ; main, avec une base dans un
 * fichier et la vraie horloge. Le reste du code ne sait pas dans quel cas il est.
 */
public final class AtelierApp {

    /** Les reglages : la limite En cours, et le limiteur (rafale, puis requetes par seconde). */
    public record Config(int wipLimit, int burst, int perSecond) {
    }

    public static final Config DEFAULT = new Config(2, 20, 10);

    private AtelierApp() {
    }

    /** Migre la base, branche le depot, le metier, les routes et les filtres : MetricsFilter(RateLimitFilter(Router)). */
    public static RequestHandler create(DataSource dataSource, Clock clock, Config config, Consumer<Throwable> onInternalError) {
        Transactions tx = new Transactions(dataSource);
        Migrations.apply(tx, TaskSchema.MIGRATIONS);
        BoardService service = new BoardService(new JdbcTaskRepository(tx, clock), new Board(config.wipLimit()));
        ApiMetrics metrics = new ApiMetrics(new LatencyRecorder(1000));
        Router router = AtelierApi.routes(service, metrics, onInternalError);
        RateLimiter limiter = new RateLimiter(config.burst(), config.perSecond(), clock);
        return new MetricsFilter(new RateLimitFilter(router, limiter), metrics, clock);
    }

    /** Reprend les taches de l'ancien systeme si la base est vide (directement par le depot : elles ont deja leur colonne). */
    static int importLegacy(DataSource dataSource, Clock clock) {
        JdbcTaskRepository tasks = new JdbcTaskRepository(new Transactions(dataSource), clock);
        if (!tasks.countByColumn().isEmpty()) {
            return 0;
        }
        for (String[] row : Data.SEED) {
            tasks.create(new NewTask(row[0], row[1], Integer.parseInt(row[2])));
        }
        return Data.SEED.size();
    }

    public static void main(String[] args) throws IOException {
        JdbcDataSource dataSource = new JdbcDataSource();
        // DB_CLOSE_DELAY=-1 : sans lui, H2 referme la base des que sa derniere connexion se ferme, et chaque requete
        // la rouvrait depuis le disque (60 ms par requete, mesure avec /metrics). En entreprise : un pool de connexions.
        dataSource.setURL("jdbc:h2:./build/ch19/atelier-app;DB_CLOSE_DELAY=-1");
        Clock clock = Clock.systemDefaultZone();
        RequestHandler app = create(dataSource, clock, DEFAULT, e -> System.err.println("erreur interne : " + e));
        System.out.println("taches reprises de l'ancien systeme : " + importLegacy(dataSource, clock));
        try (WebServer server = WebServer.start(8080, app)) {
            System.out.println("l'atelier est ouvert : http://localhost:" + server.port() + "/board (Entree pour arreter)");
            System.in.read();
        }
    }
}
