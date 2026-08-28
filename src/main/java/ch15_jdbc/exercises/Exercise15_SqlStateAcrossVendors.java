package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * EXERCICE 15 (CAPSTONE) - SQLException, getSQLState() et le seul point VRAIMENT commun entre 3 fournisseurs (niveau : difficile)
 * ======================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * jdbc.exercises.Exercise01_JdbcUrlAndDriverManager.java.
 *
 * IMPORTANT - Pour verifier sur Postgres/MySQL, demarre d'abord les
 * conteneurs (voir Exercise10). H2, lui, ne demande jamais rien.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Ce dernier exercice ferme la boucle ouverte par l'Exercise09 : SI
 * on essaie d'inserer un DEUXIEME eleve avec le MEME numero qu'un
 * eleve deja inscrit (meme cle primaire), CHAQUE base de donnees
 * refuse - mais chacune le dit A SA FACON : H2 ecrit "Eindeutiger
 * Index... verletzt", Postgres ecrit "duplicate key value violates
 * unique constraint", MySQL ecrit "Duplicate entry... for key" - 3
 * PHRASES totalement differentes, meme dans des LANGUES parfois
 * differentes ! Lire e.getMessage() pour DECIDER quoi faire dans le
 * code serait donc totalement casse d'un fournisseur a l'autre.
 * MAIS : la norme SQL prevoit un CODE, le "SQLState", un texte a 5
 * caracteres OU LES 2 PREMIERS forment une "famille" standardisee,
 * identique quel que soit le fournisseur - "23" veut TOUJOURS dire
 * "violation d'une contrainte d'integrite" (une regle sur les
 * donnees, comme "cette valeur doit etre unique", n'a pas ete
 * respectee). C'est CA, le seul morceau VRAIMENT portable pour
 * reconnaitre CE genre d'erreur en code, peu importe le fournisseur.
 *
 *
 * ==================================================================
 * TODO : sqlStateFamilyOfDuplicateKey(conn)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Sur H2, un doublon de cle primaire donne le SQLState complet
 * "23505". Sur MySQL, "23000". Les 5 caracteres complets DIFFERENT -
 * mais les 2 PREMIERS ("23") sont identiques dans les 2 cas.
 *
 * -- Le plan --
 *
 *   1. Avec un Statement simple : DROP TABLE IF EXISTS exc_demo
 *      (pour repartir de zero), PUIS CREATE TABLE exc_demo (id INT
 *      PRIMARY KEY), PUIS INSERT INTO exc_demo (id) VALUES (1) (ce
 *      premier insert reussit normalement).
 *   2. Dans un NOUVEAU bloc try : refaire EXACTEMENT le meme INSERT
 *      INTO exc_demo (id) VALUES (1) - le MEME id 1, deja pris.
 *   3. Ca DOIT lancer une SQLException (le id 1 existe deja) : dans
 *      son bloc catch (SQLException e), lire e.getSQLState() (un
 *      texte a 5 caracteres), et en garder seulement les 2 PREMIERS
 *      (substring).
 *   4. Renvoyer ces 2 premiers caracteres.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit - le try/catch EST le coeur meme du
 * plan, pas un detail a extraire a part.
 *
 * Exemple a verifier : sur les 3 fournisseurs (ceux disponibles),
 * sqlStateFamilyOfDuplicateKey(conn) rend TOUJOURS "23" - le SEUL
 * point ou les 3 fournisseurs, pourtant si differents partout
 * ailleurs dans ce chapitre, se retrouvent VRAIMENT d'accord.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - e.getSQLState() peut renvoyer null pour CERTAINES exceptions
 *     JDBC (rare, mais pas ici) - toujours garde en tete que ce n'est
 *     pas garanti a 100% par TOUS les pilotes pour TOUTES les
 *     erreurs.
 *   - e.getErrorCode() existe aussi (un entier), mais lui n'est PAS
 *     standardise du tout : chaque fournisseur choisit ses propres
 *     nombres (H2 utilise carrement le SQLState comme code, MySQL
 *     utilise 1062, Postgres n'en fournit meme pas) - contrairement
 *     a la FAMILLE du SQLState, il n'est d'aucune aide ici.
 */
public class Exercise15_SqlStateAcrossVendors {

    public static String sqlStateFamilyOfDuplicateKey(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO : implementer sqlStateFamilyOfDuplicateKey()");
    }

    private static void checkOrSkip(String label, String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String family = sqlStateFamilyOfDuplicateKey(conn);
            ExerciseChecker.check(label + " -> famille SQLState = \"" + family + "\"", "23".equals(family));
        } catch (SQLException e) {
            System.out.println("[SAUTE] " + label + " indisponible - lance 'docker compose up -d' dans jdbc-lab/. "
                    + "Detail : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        checkOrSkip("H2 (toujours disponible)", "jdbc:h2:mem:exercice15", "sa", "");
        checkOrSkip("Postgres (Docker)", "jdbc:postgresql://localhost:15432/kurse", "kurse", "kurse");
        checkOrSkip("MySQL (Docker)",
                "jdbc:mysql://localhost:13306/kurse?allowPublicKeyRetrieval=true&useSSL=false", "kurse", "kurse");

        ExerciseChecker.summary();
    }
}
