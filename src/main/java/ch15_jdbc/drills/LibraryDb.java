package ch15_jdbc.drills;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Les donnees partagees par TOUS les drills du chapitre 15.
 * ========================================================
 *
 * La bibliotheque du chapitre 10, chargee en tables SQL dans une base H2
 * EN MEMOIRE toute neuve a chaque appel de open() (aucun Docker, aucun
 * fichier). Fermer la connexion fait disparaitre la base.
 *
 *   books   (isbn, title, author, genre, pub_year, pages, price)
 *     B1 Dune / Herbert / SF / 1965 / 412 / 9.50          B5 1984 / Orwell / Dystopie / 1949 / 328 / 8.50
 *     B2 Fondation / Asimov / SF / 1951 / 255 / 8.00      B6 La Ferme des animaux / Orwell / Dystopie / 1945 / 112 / 5.50
 *     B3 Les Robots / Asimov / SF / 1950 / 253 / 7.50     B7 Neuromancien / Gibson / SF / 1984 / 271 / 9.00
 *     B4 Le Petit Prince / Saint-Exupery / Conte / 1943 / 96 / 6.00   B8 Le Hobbit / Tolkien / Fantasy / 1937 / 310 / 10.00
 *   members (id, name, age, email)   M1 Lea 17 lea@mail.fr ; M2 Hugo 34 NULL ; M3 Ines 52 ines@biblio.org ; M4 Tom 25 "  "
 *   loans   (member_id, isbn, loan_month, days_late)
 *     M1 B1 1 0 ; M2 B5 1 3 ; M1 B8 2 0 ; M3 B2 2 10 ; M2 B1 3 0 ; M3 B7 3 1 ; M1 B4 3 0 ; M2 B6 3 5
 *   procedure LATE_FEE(jours) -> jours x 50 (centimes), pour CallableStatement
 *
 * (Colonnes pub_year et loan_month : YEAR et MONTH sont des mots reserves pour H2.)
 */
public final class LibraryDb {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    public static Connection open() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:library" + COUNTER.incrementAndGet());
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE books (isbn VARCHAR(5) PRIMARY KEY, title VARCHAR(50), author VARCHAR(30),"
                    + " genre VARCHAR(20), pub_year INT, pages INT, price DECIMAL(5,2))");
            st.executeUpdate("CREATE TABLE members (id VARCHAR(5) PRIMARY KEY, name VARCHAR(30), age INT, email VARCHAR(50))");
            st.executeUpdate("CREATE TABLE loans (member_id VARCHAR(5), isbn VARCHAR(5), loan_month INT, days_late INT)");
            st.executeUpdate("INSERT INTO books VALUES"
                    + " ('B1', 'Dune', 'Herbert', 'SF', 1965, 412, 9.50), ('B2', 'Fondation', 'Asimov', 'SF', 1951, 255, 8.00),"
                    + " ('B3', 'Les Robots', 'Asimov', 'SF', 1950, 253, 7.50), ('B4', 'Le Petit Prince', 'Saint-Exupery', 'Conte', 1943, 96, 6.00),"
                    + " ('B5', '1984', 'Orwell', 'Dystopie', 1949, 328, 8.50), ('B6', 'La Ferme des animaux', 'Orwell', 'Dystopie', 1945, 112, 5.50),"
                    + " ('B7', 'Neuromancien', 'Gibson', 'SF', 1984, 271, 9.00), ('B8', 'Le Hobbit', 'Tolkien', 'Fantasy', 1937, 310, 10.00)");
            st.executeUpdate("INSERT INTO members VALUES ('M1', 'Lea', 17, 'lea@mail.fr'), ('M2', 'Hugo', 34, NULL),"
                    + " ('M3', 'Ines', 52, 'ines@biblio.org'), ('M4', 'Tom', 25, '  ')");
            st.executeUpdate("INSERT INTO loans VALUES ('M1', 'B1', 1, 0), ('M2', 'B5', 1, 3), ('M1', 'B8', 2, 0), ('M3', 'B2', 2, 10),"
                    + " ('M2', 'B1', 3, 0), ('M3', 'B7', 3, 1), ('M1', 'B4', 3, 0), ('M2', 'B6', 3, 5)");
            st.executeUpdate("CREATE ALIAS LATE_FEE FOR \"" + LibraryDb.class.getName() + ".lateFee\"");
        }
        return conn;
    }

    // La methode Java derriere la procedure SQL LATE_FEE.
    public static int lateFee(int days) {
        return days * 50;
    }

    private LibraryDb() {
    }
}
