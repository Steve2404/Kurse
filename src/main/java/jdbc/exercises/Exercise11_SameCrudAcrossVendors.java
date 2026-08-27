package jdbc.exercises;

import jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 11 - LE MEME code CRUD sur H2, Postgres et MySQL (niveau : difficile, necessite Docker pour 2 des 3)
 * ==================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * jdbc.exercises.Exercise01_JdbcUrlAndDriverManager.java.
 *
 * IMPORTANT - Pour les 2 verifications Postgres/MySQL, demarre
 * d'abord les conteneurs (voir Exercise10). H2, lui, ne demande
 * jamais rien.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * C'est LE point cle de tout ce chapitre, et la vraie raison d'etre
 * de JDBC : ECRIRE le code UNE SEULE FOIS, et le faire tourner sur
 * N'IMPORTE QUEL fournisseur de base de donnees, SANS RIEN CHANGER,
 * juste en changeant l'URL de connexion. Cet exercice le PROUVE : la
 * MEME methode runCrudCycle() va etre appelee tour a tour avec une
 * Connection H2, une Connection Postgres, et une Connection MySQL -
 * et donnera EXACTEMENT le meme resultat a chaque fois.
 *
 *
 * ==================================================================
 * TODO : runCrudCycle(conn)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Avec un Statement simple : DROP TABLE IF EXISTS crud_demo
 *      (pour repartir de zero, meme si l'exercice a deja tourne
 *      avant sur une base PERSISTANTE comme Postgres/MySQL -
 *      contrairement a H2 en memoire, elles gardent leurs donnees
 *      d'une execution a l'autre !), PUIS CREATE TABLE crud_demo
 *      (id INT PRIMARY KEY, name VARCHAR(50)).
 *   2. Avec un PreparedStatement REUTILISE (prepareStatement() UNE
 *      FOIS, puis setInt()/setString()/executeUpdate() PLUSIEURS
 *      fois de suite, en changeant juste les valeurs) : inserer (1,
 *      "Steve") puis (2, "Alice").
 *   3. Avec un second PreparedStatement : SELECT id, name FROM
 *      crud_demo ORDER BY id, parcourir le ResultSet, construire une
 *      liste de chaines "id:name".
 *   4. Renvoyer cette liste.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule methode suffit - et c'est justement LE POINT de cet
 * exercice, qu'elle marche sans AUCUNE modification sur 3
 * fournisseurs differents.
 *
 * Exemple a verifier : sur les 3 fournisseurs (ceux disponibles),
 * runCrudCycle() rend EXACTEMENT ["1:Steve", "2:Alice"].
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - "DROP TABLE IF EXISTS ..." et "CREATE TABLE ... (id INT PRIMARY KEY, ...)"
 *     sont du SQL "standard", compris de la meme facon par H2,
 *     Postgres ET MySQL - c'est justement pour ca que ce plan
 *     fonctionne sans adaptation.
 */
public class Exercise11_SameCrudAcrossVendors {

    public static List<String> runCrudCycle(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("TODO : implementer runCrudCycle()");
    }

    private static void checkOrSkip(String label, String url, String user, String password) {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            List<String> result = runCrudCycle(conn);
            ExerciseChecker.check(label + " -> " + result, result.equals(List.of("1:Steve", "2:Alice")));
        } catch (SQLException e) {
            System.out.println("[SAUTE] " + label + " indisponible - lance 'docker compose up -d' dans jdbc-lab/. "
                    + "Detail : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        checkOrSkip("H2 (toujours disponible)", "jdbc:h2:mem:exercice11", "sa", "");
        checkOrSkip("Postgres (Docker)", "jdbc:postgresql://localhost:15432/kurse", "kurse", "kurse");
        checkOrSkip("MySQL (Docker)",
                "jdbc:mysql://localhost:13306/kurse?allowPublicKeyRetrieval=true&useSSL=false", "kurse", "kurse");

        ExerciseChecker.summary();
    }
}
