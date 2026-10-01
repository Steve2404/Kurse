package ch15_jdbc.exercises;

import ch15_jdbc.ExerciseChecker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * EXERCICE 5 - execute, executeQuery, executeUpdate et la lecture d'un ResultSet : ta regle comparee a H2 (niveau : difficile)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_JdbcUrlAndDriverManager.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Trois facons d'envoyer du SQL, trois types de retour :
 *
 *   execute(sql)        boolean : true si le resultat est un ResultSet (SELECT), false sinon
 *   executeQuery(sql)   ResultSet ; un ordre qui n'est PAS un SELECT -> SQLException
 *   executeUpdate(sql)  int : le nombre de lignes touchees (0 pour un CREATE) ; un SELECT -> SQLException
 *
 * Et les pieges de la lecture d'un ResultSet (messages reels de H2) :
 *
 *   lire avant next()                 -> SQLException "No data is available"
 *   getInt(0)                         -> SQLException (les colonnes commencent a 1)
 *   getInt("colonne inconnue")        -> SQLException "Column ... not found"
 *   getInt sur un NULL SQL            -> 0 (!), et wasNull() rend true ; getObject rend null
 *   getString sur un INT              -> "1" (conversion permise) ; getInt sur "abc" -> SQLException
 *   previous() sur un ResultSet par defaut (TYPE_FORWARD_ONLY) -> SQLException
 *   lire apres rs.close(), ou apres la fermeture du Statement -> SQLException "already closed"
 *   un 2e executeQuery sur le MEME Statement ferme le 1er ResultSet
 *   next() sur un resultat vide       -> false (pas d'exception)
 *   setInt(0, ...) ou un ? oublie     -> SQLException
 *
 * main() execute vraiment chaque cas sur une base H2 en memoire.
 *
 *
 * ==================================================================
 * TODO 1 : returnOf(method, sqlKind)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * method : "execute", "executeQuery", "executeUpdate". sqlKind : "SELECT",
 * "UPDATE" (qui touche 1 ligne), "CREATE". Rendre "true", "false",
 * "ResultSet", "rowCount" (un nombre > 0), "0" ou "SQLException".
 *
 * -- Le plan --
 *
 *   1. execute -> "true" pour SELECT, "false" sinon.
 *   2. executeQuery -> "ResultSet" pour SELECT, "SQLException" sinon.
 *   3. executeUpdate -> "SQLException" pour SELECT, "0" pour CREATE, "rowCount" pour UPDATE.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : readOutcome(situation)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une table t(v INT = 1, n INT = NULL, s VARCHAR = 'abc'). situation :
 * "beforeNext", "indexZero", "unknownColumn", "intOnNull", "objectOnNull",
 * "stringOnInt", "intOnText", "previousForwardOnly", "afterClose",
 * "statementClosed", "secondQuerySameStatement", "nextOnEmpty",
 * "paramIndexZero", "paramNotSet". Rendre ce que l'on obtient en texte
 * ("0", "null", "1", "false") ou "SQLException".
 *
 * -- Le plan --
 *
 *   1. Les 4 cas qui rendent une valeur : intOnNull -> "0", objectOnNull -> "null",
 *      stringOnInt -> "1", nextOnEmpty -> "false".
 *   2. Tous les autres -> "SQLException".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : nullableInt(rs, column)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * getInt ne sait pas dire "rien" : il rend 0. Rendre un Integer qui vaut
 * null quand la colonne est NULL en base, sinon sa valeur.
 *
 * -- Le plan --
 *
 *   1. int v = rs.getInt(column) ; si rs.wasNull() -> null ; sinon v.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - switch (method) { case "execute": return sqlKind.equals("SELECT") ? "true" : "false"; ... }
 *   - wasNull() parle de la DERNIERE colonne lue : l'appeler juste apres getInt.
 */
public class Exercise05_JdbcApiRules {

    public static String returnOf(String method, String sqlKind) {
        throw new UnsupportedOperationException("TODO 1 : implementer returnOf()");
    }

    public static String readOutcome(String situation) {
        throw new UnsupportedOperationException("TODO 2 : implementer readOutcome()");
    }

    public static Integer nullableInt(ResultSet rs, String column) throws SQLException {
        throw new UnsupportedOperationException("TODO 3 : implementer nullableInt()");
    }

    public static void main(String[] args) throws SQLException {
        int agree = 0;
        int total = 0;
        for (String method : List.of("execute", "executeQuery", "executeUpdate")) {
            for (String kind : List.of("SELECT", "UPDATE", "CREATE")) {
                total++;
                if (returnOf(method, kind).equals(jvmReturn(method, kind))) {
                    agree++;
                }
            }
        }
        ExerciseChecker.check("returnOf == H2 sur 9 cas (" + agree + " d'accord)", agree == total);

        List<String> situations = List.of("beforeNext", "indexZero", "unknownColumn", "intOnNull", "objectOnNull", "stringOnInt",
                "intOnText", "previousForwardOnly", "afterClose", "statementClosed", "secondQuerySameStatement", "nextOnEmpty",
                "paramIndexZero", "paramNotSet");
        agree = 0;
        String miss = "";
        for (String s : situations) {
            String real = jvmRead(s);
            if (readOutcome(s).equals(real)) {
                agree++;
            } else if (miss.isEmpty()) {
                miss = " ; 1er ecart : " + s + " -> " + readOutcome(s) + " au lieu de " + real;
            }
        }
        ExerciseChecker.check("readOutcome == H2 sur 14 cas (" + agree + " d'accord)" + miss, agree == 14);

        try (Connection c = fresh("ex05-null"); Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT v, n FROM t")) {
            rs.next();
            Integer v = nullableInt(rs, "v");
            Integer n = nullableInt(rs, "n");
            ExerciseChecker.check("nullableInt : v -> 1, n (NULL) -> null (et pas 0)", Integer.valueOf(1).equals(v) && n == null);
        }

        ExerciseChecker.summary();
    }

    // ---- Le juge : execute vraiment chaque cas sur H2 (ne pas modifier) ----

    static Connection fresh(String name) throws SQLException {
        Connection c = DriverManager.getConnection("jdbc:h2:mem:" + name);
        try (Statement st = c.createStatement()) {
            st.executeUpdate("CREATE TABLE t (v INT, n INT, s VARCHAR(10))");
            st.executeUpdate("INSERT INTO t VALUES (1, NULL, 'abc')");
        }
        return c;
    }

    static String jvmReturn(String method, String kind) throws SQLException {
        String sql = kind.equals("SELECT") ? "SELECT * FROM t" : kind.equals("UPDATE") ? "UPDATE t SET v = v + 1" : "CREATE TABLE u (x INT)";
        try (Connection c = fresh("ex05-" + method + kind); Statement st = c.createStatement()) {
            switch (method) {
                case "execute":
                    return String.valueOf(st.execute(sql));
                case "executeQuery":
                    st.executeQuery(sql).close();
                    return "ResultSet";
                default:
                    int n = st.executeUpdate(sql);
                    return n > 0 ? "rowCount" : String.valueOf(n);
            }
        } catch (SQLException e) {
            return "SQLException";
        }
    }

    static String jvmRead(String situation) throws SQLException {
        try (Connection c = fresh("ex05-" + situation)) {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(situation.equals("nextOnEmpty") ? "SELECT v FROM t WHERE 1 = 0" : "SELECT v, n, s FROM t");
            switch (situation) {
                case "beforeNext":
                    return String.valueOf(rs.getInt(1));
                case "nextOnEmpty":
                    return String.valueOf(rs.next());
                case "paramIndexZero": {
                    PreparedStatement ps = c.prepareStatement("SELECT v FROM t WHERE v = ?");
                    ps.setInt(0, 1);
                    return "OK";
                }
                case "paramNotSet": {
                    PreparedStatement ps = c.prepareStatement("SELECT v FROM t WHERE v = ?");
                    return String.valueOf(ps.executeQuery().next());
                }
                default:
                    break;
            }
            rs.next();
            switch (situation) {
                case "indexZero":
                    return String.valueOf(rs.getInt(0));
                case "unknownColumn":
                    return String.valueOf(rs.getInt("nope"));
                case "intOnNull":
                    return String.valueOf(rs.getInt("n"));
                case "objectOnNull":
                    return String.valueOf(rs.getObject("n"));
                case "stringOnInt":
                    return rs.getString("v");
                case "intOnText":
                    return String.valueOf(rs.getInt("s"));
                case "previousForwardOnly":
                    return String.valueOf(rs.previous());
                case "afterClose":
                    rs.close();
                    return String.valueOf(rs.getInt(1));
                case "statementClosed":
                    st.close();
                    return String.valueOf(rs.getInt(1));
                default:
                    st.executeQuery("SELECT v FROM t");
                    return String.valueOf(rs.getInt(1));
            }
        } catch (SQLException e) {
            return "SQLException";
        }
    }
}
