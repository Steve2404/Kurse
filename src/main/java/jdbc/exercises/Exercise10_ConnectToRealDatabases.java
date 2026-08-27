package jdbc.exercises;

import jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * EXERCICE 10 - Se connecter a de VRAIES bases Postgres et MySQL (niveau : difficile, necessite Docker)
 * =========================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * jdbc.exercises.Exercise01_JdbcUrlAndDriverManager.java.
 *
 * IMPORTANT - Avant de lancer cet exercice : demarre les 2 bases
 * Docker depuis jdbc-lab/ :
 *
 *     cd jdbc-lab
 *     docker compose up -d
 *
 * (voir jdbc-lab/DOCKER_EXPLIQUE.md si "Docker" ne te dit rien du
 * tout). Si tu ne les demarres pas, cet exercice te le signale
 * clairement (au lieu de planter avec une pile d'erreurs illisible),
 * et saute juste les verifications concernees.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Exactement le MEME DriverManager.getConnection() que pour H2
 * (Exercise01), mais avec une URL et des identifiants qui pointent
 * cette fois vers de VRAIS serveurs (dans des conteneurs Docker,
 * tournant sur des ports specifiques a ce projet - voir
 * docker-compose.yml). getMetaData() rend des informations sur LE
 * SERVEUR reellement contacte (son nom, sa version...) - une bonne
 * facon de PROUVER qu'on a VRAIMENT atteint le bon logiciel, et pas
 * autre chose.
 *
 *
 * ==================================================================
 * TODO : describeDatabase(url, user, password)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Ouvrir une Connection avec ces 3 informations (try-with-resources).
 *   2. Recuperer ses metadonnees (conn.getMetaData()).
 *   3. Renvoyer meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit - c'est main() (deja fourni plus
 * bas) qui gere le cas "le serveur ne repond pas" proprement, pour ne
 * jamais faire planter tout l'exercice a cause d'un Docker pas
 * demarre.
 *
 * Exemple a verifier : avec les conteneurs demarres,
 * describeDatabase() sur l'URL Postgres commence par "PostgreSQL",
 * et sur l'URL MySQL par "MySQL".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - getConnection() ET getMetaData() peuvent tous les deux lancer
 *     SQLException (checked) - a declarer sur la methode.
 */
public class Exercise10_ConnectToRealDatabases {

    public static String describeDatabase(String url, String user, String password) throws SQLException {
        throw new UnsupportedOperationException("TODO : implementer describeDatabase()");
    }

    private static void checkOrSkip(String label, String url, String user, String password, String expectedPrefix) {
        String description;
        try {
            description = describeDatabase(url, user, password);
        } catch (SQLException e) {
            System.out.println("[SAUTE] " + label + " indisponible - lance 'docker compose up -d' dans jdbc-lab/. "
                    + "Detail : " + e.getMessage());
            return;
        }
        ExerciseChecker.check(label + " : " + description, description.startsWith(expectedPrefix));
    }

    public static void main(String[] args) {
        checkOrSkip("Connexion reelle a Postgres (Docker)",
                "jdbc:postgresql://localhost:15432/kurse", "kurse", "kurse", "PostgreSQL");

        checkOrSkip("Connexion reelle a MySQL (Docker)",
                "jdbc:mysql://localhost:13306/kurse?allowPublicKeyRetrieval=true&useSSL=false", "kurse", "kurse",
                "MySQL");

        ExerciseChecker.summary();
    }
}
