package jdbc.exercises;

import jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * EXERCICE 1 - L'URL JDBC et DriverManager (niveau : moyen)
 * ==========================================================================
 *
 * -- Rappel du decoupage en "boites magiques" --
 *
 * Une methode, c'est une boite magique : tu la nourris d'ingredients
 * (parametres), et elle rend un resultat, sans que tu aies besoin de
 * savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
 * demande-toi : est-ce qu'elle se raconte seule ? revient-elle
 * plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
 * moins une question, elle merite sa propre boite.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une URL JDBC, c'est comme une adresse postale a 3 niveaux : "jdbc"
 * (le pays : "on est dans le monde JDBC"), puis le NOM du fournisseur
 * de base de donnees ("h2", "postgresql", "mysql" : la ville), puis
 * TOUT LE RESTE, specifique a ce fournisseur (la rue precise : ou se
 * trouve le serveur, quel port, quel nom de base). Ces 3 morceaux sont
 * separes par des deux-points (:), mais ATTENTION : le 3e morceau
 * peut LUI-MEME contenir des deux-points (pour un port, par exemple
 * "localhost:5432") - il ne faut donc JAMAIS decouper "bêtement" sur
 * TOUS les deux-points, seulement sur les 2 PREMIERS.
 *
 * DriverManager.getConnection(url, utilisateur, motDePasse) est LA
 * boite magique qui lit cette adresse, trouve le bon "livreur"
 * (Driver) parmi ceux enregistres au demarrage (grace au JAR du
 * fournisseur present dans le classpath), et rend une vraie
 * Connection ouverte vers la base.
 *
 *
 * ==================================================================
 * TODO 1 : parseJdbcUrl(url)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * "jdbc:postgresql://localhost:5432/mydb" doit se decouper en
 * EXACTEMENT 3 morceaux : "jdbc", "postgresql",
 * "//localhost:5432/mydb" (le port 5432 RESTE entier dans le 3e
 * morceau, jamais decoupe a son tour).
 *
 * -- Le plan --
 *
 *   1. Renvoyer url.split(":", 3) (le "3" en 2e argument LIMITE le
 *      decoupage a 3 morceaux maximum, meme s'il reste d'autres
 *      deux-points plus loin dans le texte).
 *
 *
 * ==================================================================
 * TODO 2 : connectToH2InMemory(databaseName)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * H2 sait fonctionner "en memoire" (mem) : une base ENTIEREMENT
 * temporaire, jamais ecrite sur le disque, qui disparait des que le
 * programme s'arrete - parfaite pour s'entrainer sans jamais rien
 * installer ni laisser de trace.
 *
 * -- Le plan --
 *
 *   1. Renvoyer DriverManager.getConnection("jdbc:h2:mem:" + databaseName, "sa", "")
 *      ("sa" est l'utilisateur par defaut d'H2, mot de passe vide).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une ligne.
 *
 * Exemple a verifier : parseJdbcUrl("jdbc:h2:mem:testdb") ->
 * ["jdbc","h2","mem:testdb"]. connectToH2InMemory("exercice01") rend
 * une Connection VRAIMENT ouverte, dont
 * getMetaData().getDatabaseProductName() vaut "H2".
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - String.split(regex, limit) : avec un 'limit' positif, le tableau
 *     rendu a AU PLUS 'limit' elements - le DERNIER element contient
 *     alors TOUT LE RESTE non decoupe, meme s'il reste des
 *     occurrences du separateur dedans.
 *   - getConnection() lance SQLException (checked) - a declarer ou
 *     attraper.
 */
public class Exercise01_JdbcUrlAndDriverManager {

    public static String[] parseJdbcUrl(String url) {
        throw new UnsupportedOperationException("TODO 1 : implementer parseJdbcUrl()");
    }

    public static Connection connectToH2InMemory(String databaseName) throws SQLException {
        throw new UnsupportedOperationException("TODO 2 : implementer connectToH2InMemory()");
    }

    public static void main(String[] args) throws SQLException {
        String[] parts = parseJdbcUrl("jdbc:postgresql://localhost:5432/mydb");
        ExerciseChecker.check("parseJdbcUrl decoupe en EXACTEMENT 3 morceaux",
                parts.length == 3 && parts[0].equals("jdbc") && parts[1].equals("postgresql")
                        && parts[2].equals("//localhost:5432/mydb"));

        String[] h2Parts = parseJdbcUrl("jdbc:h2:mem:testdb");
        ExerciseChecker.check("parseJdbcUrl marche aussi pour une URL H2",
                h2Parts[0].equals("jdbc") && h2Parts[1].equals("h2") && h2Parts[2].equals("mem:testdb"));

        try (Connection connection = connectToH2InMemory("exercice01")) {
            ExerciseChecker.check("connectToH2InMemory() ouvre une vraie connexion H2",
                    connection.getMetaData().getDatabaseProductName().equals("H2"));
            ExerciseChecker.check("la connexion est valide", connection.isValid(2));
        }

        ExerciseChecker.summary();
    }
}
