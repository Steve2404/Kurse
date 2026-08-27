package jdbc.exercises;

import jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 14 - Decouvrir la structure d'une table SANS la connaitre a l'avance : ResultSetMetaData (niveau : difficile)
 * =============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * jdbc.exercises.Exercise01_JdbcUrlAndDriverManager.java.
 *
 * IMPORTANT - Pour verifier sur Postgres/MySQL, demarre d'abord les
 * conteneurs (voir Exercise10). H2, lui, ne demande jamais rien.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Jusqu'ici, TOUS les exercices connaissaient DEJA les colonnes a
 * l'avance (rs.getString("name"), par exemple - il fallait SAVOIR que
 * "name" existait). Mais imagine un programme "generique" qui doit
 * afficher N'IMPORTE QUELLE table, meme une qu'il n'a JAMAIS vue
 * avant (un outil d'administration, par exemple) : il ne PEUT PAS
 * connaitre les noms de colonnes a l'avance ! rs.getMetaData() rend
 * un objet special (ResultSetMetaData) qui repond a la question "sans
 * meme lire une seule ligne de donnees, dis-moi juste COMBIEN de
 * colonnes il y a, et COMMENT elles s'appellent."
 *
 *
 * ==================================================================
 * TODO : describeColumns(conn)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Sur une table meta_demo(id INT PRIMARY KEY, name VARCHAR(50)),
 * describeColumns() doit rendre ["ID", "NAME"] (les noms de colonnes,
 * TOUJOURS mis en MAJUSCULES par notre code - car selon le
 * fournisseur, les noms reviennent parfois en minuscules -Postgres,
 * MySQL- et parfois en MAJUSCULES -H2 par defaut- : uniformiser
 * evite que le TEST se casse juste a cause de cette difference de
 * casse, qui n'a rien a voir avec le VRAI sujet de l'exercice).
 *
 * -- Le plan --
 *
 *   1. Avec un Statement simple : DROP TABLE IF EXISTS meta_demo
 *      (pour repartir de zero), PUIS CREATE TABLE meta_demo (id INT
 *      PRIMARY KEY, name VARCHAR(50)).
 *   2. Executer "SELECT * FROM meta_demo" (executeQuery) - MEME SI la
 *      table est VIDE, ca ne change RIEN a sa STRUCTURE.
 *   3. Demander rs.getMetaData() : ca rend le ResultSetMetaData.
 *   4. Demander meta.getColumnCount() : le nombre de colonnes.
 *   5. Pour chaque position i, de 1 A getColumnCount() INCLUS
 *      (ATTENTION : ca commence a 1, PAS a 0, comme les ResultSet
 *      eux-memes) : ajouter meta.getColumnName(i).toUpperCase() a une
 *      liste.
 *   6. Renvoyer cette liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit.
 *
 * Exemple a verifier : sur les 3 fournisseurs (ceux disponibles),
 * describeColumns(conn) rend EXACTEMENT ["ID", "NAME"], dans cet
 * ordre - MEME SI, en coulisses, H2 rend deja "ID"/"NAME" alors que
 * Postgres et MySQL rendent "id"/"name" (le .toUpperCase() gomme
 * cette difference).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Les index de colonnes JDBC (getColumnName(i), getString(i)...)
 *     commencent TOUJOURS a 1, jamais a 0 - piege classique.
 *   - meta.getColumnTypeName(i) existe aussi (le TYPE SQL de la
 *     colonne), mais volontairement NON utilise ici : contrairement
 *     au NOM, le type revient sous des libelles DIFFERENTS selon le
 *     fournisseur (INTEGER sur H2, int4 sur Postgres, INT sur MySQL,
 *     pour la MEME colonne "INT" declaree) - un exemple de plus que
 *     JDBC unifie l'ACCES, mais pas forcement le VOCABULAIRE en
 *     dessous.
 */
public class Exercise14_ResultSetMetaDataAcrossVendors {

    public static List<String> describeColumns(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO : implementer describeColumns()");
    }

    private static void checkOrSkip(String label, String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            List<String> columns = describeColumns(conn);
            ExerciseChecker.check(label + " -> " + columns, columns.equals(List.of("ID", "NAME")));
        } catch (SQLException e) {
            System.out.println("[SAUTE] " + label + " indisponible - lance 'docker compose up -d' dans jdbc-lab/. "
                    + "Detail : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        checkOrSkip("H2 (toujours disponible)", "jdbc:h2:mem:exercice14", "sa", "");
        checkOrSkip("Postgres (Docker)", "jdbc:postgresql://localhost:15432/kurse", "kurse", "kurse");
        checkOrSkip("MySQL (Docker)",
                "jdbc:mysql://localhost:13306/kurse?allowPublicKeyRetrieval=true&useSSL=false", "kurse", "kurse");

        ExerciseChecker.summary();
    }
}
