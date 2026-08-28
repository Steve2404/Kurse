package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

/**
 * EXERCICE 12 - Envoyer PLUSIEURS ordres SQL en UN SEUL voyage : les batch updates (niveau : difficile)
 * ==========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * jdbc.exercises.Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine un livreur qui doit deposer 3 colis dans 3 boites aux
 * lettres de la MEME rue. Il pourrait faire 3 ALLERS-RETOURS separes
 * depuis son entrepot (un par colis) - ca marche, mais c'est lent et
 * fatiguant : a CHAQUE aller-retour, il y a le temps du trajet en
 * plus du temps de depose. OU BIEN il peut charger les 3 colis DANS
 * LE MEME camion et ne faire QU'UN SEUL trajet, en les deposant les
 * uns apres les autres une fois arrive sur place. C'est exactement ce
 * que fait un "batch" JDBC : au lieu d'appeler executeUpdate() (un
 * aller-retour reseau vers la base) UNE FOIS PAR LIGNE, on empile
 * plusieurs commandes avec addBatch() (on charge le camion), PUIS on
 * declenche UN SEUL executeBatch() (un seul trajet) qui execute
 * TOUTES les commandes empilees d'un coup cote serveur.
 *
 * IMPORTANT - Pour verifier sur Postgres/MySQL, demarre d'abord les
 * conteneurs (voir Exercise10). H2, lui, ne demande jamais rien.
 *
 *
 * ==================================================================
 * TODO : insertNamesInBatch(conn, names)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * Avec names = ["Steve", "Alice", "Bob"], on veut inserer 3 lignes :
 * (1,"Steve"), (2,"Alice"), (3,"Bob") - en un SEUL executeBatch(), pas
 * 3 executeUpdate() separes.
 *
 * -- Le plan --
 *
 *   1. Avec un Statement simple : DROP TABLE IF EXISTS batch_demo
 *      (pour repartir de zero), PUIS CREATE TABLE batch_demo (id INT
 *      PRIMARY KEY, name VARCHAR(50)).
 *   2. Preparer UNE SEULE FOIS : "INSERT INTO batch_demo (id, name)
 *      VALUES (?, ?)".
 *   3. Pour chaque nom de la liste (avec son index i, en partant de
 *      0) : setInt(1, i + 1), setString(2, nom), PUIS addBatch() (au
 *      lieu d'executeUpdate() !) - ca EMPILE la commande sans encore
 *      rien envoyer a la base.
 *   4. Une fois la boucle finie : appeler executeBatch() UNE SEULE
 *      FOIS - ca envoie et execute TOUTES les commandes empilees d'un
 *      coup, et renvoie un int[] : le nombre de lignes touchees PAR
 *      CHAQUE commande, dans l'ORDRE ou elles ont ete empilees.
 *   5. Renvoyer ce int[].
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit.
 *
 * Exemple a verifier : insertNamesInBatch(conn, ["Steve","Alice","Bob"])
 * rend un tableau de longueur 3, ou CHAQUE case vaut 1 (1 ligne
 * touchee par chaque INSERT empile) - et ce, sur H2, Postgres ET
 * MySQL, SANS AUCUNE modification du code.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - addBatch() (sans argument, sur le PreparedStatement DEJA
 *     rempli avec setXxx()) empile la commande courante.
 *   - executeBatch() renvoie int[], PAS un simple int - une case par
 *     commande empilee, dans l'ordre d'empilement.
 */
public class Exercise12_BatchUpdates {

    public static int[] insertNamesInBatch(Connection conn, List<String> names) throws SQLException {
        throw new UnsupportedOperationException("TODO : implementer insertNamesInBatch()");
    }

    private static void checkOrSkip(String label, String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            int[] results = insertNamesInBatch(conn, List.of("Steve", "Alice", "Bob"));
            boolean ok = results.length == 3 && Arrays.stream(results).allMatch(n -> n == 1);
            ExerciseChecker.check(label + " -> " + Arrays.toString(results), ok);
        } catch (SQLException e) {
            System.out.println("[SAUTE] " + label + " indisponible - lance 'docker compose up -d' dans jdbc-lab/. "
                    + "Detail : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        checkOrSkip("H2 (toujours disponible)", "jdbc:h2:mem:exercice12", "sa", "");
        checkOrSkip("Postgres (Docker)", "jdbc:postgresql://localhost:15432/kurse", "kurse", "kurse");
        checkOrSkip("MySQL (Docker)",
                "jdbc:mysql://localhost:13306/kurse?allowPublicKeyRetrieval=true&useSSL=false", "kurse", "kurse");

        ExerciseChecker.summary();
    }
}
