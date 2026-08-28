package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * EXERCICE 9 - Identifier les URL JDBC valides ou invalides, tous fournisseurs confondus (niveau : difficile)
 * ==============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * jdbc.exercises.Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Debut de la 2e moitie du chapitre : les fournisseurs "reels" --
 *
 * H2 (exercices 1 a 8) est parfait pour s'entrainer sans rien
 * installer, mais JDBC existe justement pour qu'on puisse ECRIRE LE
 * MEME CODE JAVA quel que soit le VRAI fournisseur en dessous. Ce
 * projet ajoute donc les VRAIS pilotes PostgreSQL et MySQL au
 * classpath (voir pom.xml) - cet exercice-ci ne se connecte encore a
 * RIEN de reel (pas besoin de Docker), il verifie juste que Java sait
 * RECONNAITRE le FORMAT de chaque URL.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque pilote JDBC (Driver) enregistre au demarrage se demande, pour
 * CHAQUE URL qu'on lui presente : "est-ce que CETTE adresse me
 * concerne, moi ?". DriverManager.getDriver(url) pose cette question a
 * TOUS les pilotes enregistres, et renvoie LE PREMIER qui repond "oui,
 * c'est pour moi" - ou lance une SQLException si AUCUN ne repond
 * "oui". Le gros avantage pour cet exercice : ca ne tente JAMAIS de
 * VRAIMENT se connecter, ca verifie juste le FORMAT - meme un serveur
 * qui n'existe pas du tout peut donner une URL "reconnue".
 *
 *
 * ==================================================================
 * TODO 1 : buildPostgresUrl(host, port, database)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer "jdbc:postgresql://" + host + ":" + port + "/" + database.
 *
 *
 * ==================================================================
 * TODO 2 : buildMysqlUrl(host, port, database)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Renvoyer "jdbc:mysql://" + host + ":" + port + "/" + database.
 *
 *
 * ==================================================================
 * TODO 3 : hasRegisteredDriver(url)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Essayer DriverManager.getDriver(url) dans un try.
 *   2. Si ca reussit (aucune exception) : renvoyer true.
 *   3. Si SQLException est attrapee ("aucun pilote ne reconnait cette
 *      URL") : renvoyer false.
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en quelques lignes.
 *
 * Exemple a verifier : buildPostgresUrl("localhost", 5432, "mydb") ==
 * "jdbc:postgresql://localhost:5432/mydb" - un format RECONNU (meme
 * si "localhost:5432" ne repond a rien du tout). "h2:mem:test" (SANS
 * le prefixe "jdbc:") - format NON reconnu. "jdbc:unknownvendor://host/db"
 * - format NON reconnu non plus (aucun pilote pour "unknownvendor").
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Une URL JDBC a TOUJOURS 3 morceaux separes par ':' (rappel de
 *     l'Exercise01) : "jdbc", le nom du fournisseur, puis le reste
 *     specifique a ce fournisseur (souvent "//hote:port/base" pour
 *     les bases "client-serveur" comme Postgres/MySQL, mais "mem:nom"
 *     pour H2 en memoire - CHAQUE fournisseur choisit librement la
 *     forme de son 3e morceau).
 */
public class Exercise09_JdbcUrlAcrossVendors {

    public static String buildPostgresUrl(String host, int port, String database) {
        throw new UnsupportedOperationException("TODO 1 : implementer buildPostgresUrl()");
    }

    public static String buildMysqlUrl(String host, int port, String database) {
        throw new UnsupportedOperationException("TODO 2 : implementer buildMysqlUrl()");
    }

    public static boolean hasRegisteredDriver(String url) {
        throw new UnsupportedOperationException("TODO 3 : implementer hasRegisteredDriver()");
    }

    public static void main(String[] args) throws SQLException {
        String postgresUrl = buildPostgresUrl("localhost", 5432, "mydb");
        ExerciseChecker.check("buildPostgresUrl() assemble le bon format",
                postgresUrl.equals("jdbc:postgresql://localhost:5432/mydb"));

        String mysqlUrl = buildMysqlUrl("localhost", 3306, "mydb");
        ExerciseChecker.check("buildMysqlUrl() assemble le bon format",
                mysqlUrl.equals("jdbc:mysql://localhost:3306/mydb"));

        ExerciseChecker.check("l'URL Postgres bien formee est RECONNUE (meme sans serveur reel)",
                hasRegisteredDriver(postgresUrl));
        ExerciseChecker.check("l'URL MySQL bien formee est RECONNUE (meme sans serveur reel)",
                hasRegisteredDriver(mysqlUrl));
        ExerciseChecker.check("une URL H2 bien formee est RECONNUE",
                hasRegisteredDriver("jdbc:h2:mem:test"));

        ExerciseChecker.check("une URL SANS le prefixe 'jdbc:' n'est PAS reconnue",
                !hasRegisteredDriver("h2:mem:test"));
        ExerciseChecker.check("une URL avec un fournisseur INCONNU n'est PAS reconnue",
                !hasRegisteredDriver("jdbc:unknownvendor://localhost/db"));

        ExerciseChecker.summary();
    }
}
