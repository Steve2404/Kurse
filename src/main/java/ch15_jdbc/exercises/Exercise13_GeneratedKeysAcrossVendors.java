package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * EXERCICE 13 - Recuperer une cle auto-generee, MEME SI chaque fournisseur la fabrique differemment (niveau : difficile)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * jdbc.exercises.Exercise01_JdbcUrlAndDriverManager.java.
 *
 * IMPORTANT - Pour verifier sur Postgres/MySQL, demarre d'abord les
 * conteneurs (voir Exercise10). H2, lui, ne demande jamais rien.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Quand on inscrit un nouvel eleve dans un cahier de classe SANS lui
 * demander son numero (c'est le cahier qui le decide tout seul, "le
 * prochain numero libre"), chaque "cahier" (fournisseur de base de
 * donnees) a sa PROPRE facon d'ecrire cette regle : H2 et MySQL
 * disent "AUTO_INCREMENT" sur la colonne, Postgres dit "SERIAL". LE
 * CREATE TABLE n'est donc PAS identique partout (contrairement a
 * l'Exercise11 !) - main() te fournit d'ailleurs la bonne phrase SQL
 * pour chaque fournisseur, ce n'est pas ton TODO. MAIS, une fois la
 * table creee, la facon de DEMANDER A JAVA "c'est quel numero que tu
 * viens de me donner ?" est, elle, EXACTEMENT LA MEME peu importe le
 * fournisseur en dessous : Statement.RETURN_GENERATED_KEYS +
 * getGeneratedKeys(). C'est CA, le vrai point de cet exercice.
 *
 *
 * ==================================================================
 * TODO : insertAndReturnGeneratedId(conn, insertSql, name)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec insertSql = "INSERT INTO gen_demo (name) VALUES (?)" et name =
 * "Steve" (sur une table gen_demo TOUTE NEUVE), on s'attend a
 * recuperer l'id 1 (le tout premier numero distribue).
 *
 * -- Le plan --
 *
 *   1. Preparer insertSql, MAIS avec un 2e argument special a
 *      prepareStatement() : Statement.RETURN_GENERATED_KEYS (ca
 *      previent le pilote JDBC : "garde de cote la cle que tu vas
 *      generer, je la redemanderai juste apres").
 *   2. setString(1, name), PUIS executeUpdate() (l'insertion se fait
 *      normalement).
 *   3. Demander getGeneratedKeys() : ca rend un ResultSet special,
 *      A UNE SEULE LIGNE, dont la 1ere colonne contient le numero
 *      genere.
 *   4. rs.next() (avancer sur cette unique ligne), PUIS rs.getLong(1)
 *      (lire la cle generee par sa POSITION - elle n'a pas toujours
 *      le meme NOM de colonne selon le fournisseur, mais elle est
 *      TOUJOURS en position 1).
 *   5. Renvoyer cette valeur.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit.
 *
 * Exemple a verifier : sur les 3 fournisseurs (ceux disponibles),
 * apres avoir recree gen_demo (voir main()) et insere "Steve" en
 * PREMIER, insertAndReturnGeneratedId(...) rend 1 - LE MEME code Java
 * marche sur les 3, alors meme que le CREATE TABLE, lui, differe.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) :
 *     surcharge a 2 arguments, differente de prepareStatement(sql) tout court.
 *   - getGeneratedKeys() peut rendre un ResultSet VIDE si le pilote
 *     ne trouve rien - toujours verifier rs.next() avant de lire.
 */
public class Exercise13_GeneratedKeysAcrossVendors {

    public static long insertAndReturnGeneratedId(Connection conn, String insertSql, String name)
            throws SQLException {
        throw new UnsupportedOperationException("TODO : implementer insertAndReturnGeneratedId()");
    }

    private static void checkOrSkip(String label, String url, String user, String password, String createTableSql) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            try (Statement ddl = conn.createStatement()) {
                ddl.execute("DROP TABLE IF EXISTS gen_demo");
                ddl.execute(createTableSql);
            }
            long id = insertAndReturnGeneratedId(conn, "INSERT INTO gen_demo (name) VALUES (?)", "Steve");
            ExerciseChecker.check(label + " -> id genere = " + id, id == 1L);
        } catch (SQLException e) {
            System.out.println("[SAUTE] " + label + " indisponible - lance 'docker compose up -d' dans jdbc-lab/. "
                    + "Detail : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        checkOrSkip("H2 (toujours disponible)", "jdbc:h2:mem:exercice13", "sa", "",
                "CREATE TABLE gen_demo (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50))");
        checkOrSkip("Postgres (Docker)", "jdbc:postgresql://localhost:15432/kurse", "kurse", "kurse",
                "CREATE TABLE gen_demo (id SERIAL PRIMARY KEY, name VARCHAR(50))");
        checkOrSkip("MySQL (Docker)",
                "jdbc:mysql://localhost:13306/kurse?allowPublicKeyRetrieval=true&useSSL=false", "kurse", "kurse",
                "CREATE TABLE gen_demo (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(50))");

        ExerciseChecker.summary();
    }
}
