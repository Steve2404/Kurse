package ch15_jdbc.projects.p01_library;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON LibraryApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "connexion : jdbc | h2 | mem:p01_library ; autoCommit true ; valide true",
            "schema : execute false, executeUpdate 0",
            "insertion : 10 livres, 10 lignes, 2 sans pages",
            "Camus : [L'Etranger (1942), La Peste (1947), Le Premier Homme (1994)]",
            "find 978-05 : Book[isbn=978-05, title=La Peste, author=Camus, pubYear=1947, pages=336]",
            "find 978-06 : pages null",
            "find 999 : Optional.empty",
            "recherche \"le\" page 1 : [Le Dernier Jour d'un condamne, Le Petit Prince, Le Premier Homme]",
            "recherche \"le\" page 2 : [Le Rouge et le Noir, Les Miserables]",
            "recherche \"le\" : arret a la page 3 (vide)",
            "par siecle : {1800=4, 1900=6}",
            "Hugo +10 pages : 3 lignes ; supprimes avant 1830 : 1 ; inconnu : 0 ; restent 9",
            "pieges :",
            "  getString avant next : 02000",
            "  colonne 0 : 90008",
            "  colonne inconnue : 42S22",
            "  executeUpdate d'un SELECT : 90001",
            "  executeQuery d'un DELETE : 90002",
            "  table inconnue : 42S02",
            "  1er ResultSet ferme par la 2e requete : true",
            "  next sur un ResultSet ferme : 90007",
            "injection :",
            "  Statement avec x' OR '1'='1 : 9 livres",
            "  PreparedStatement avec x' OR '1'='1 : 0 livre",
            "  Statement avec L'Etranger : SQLState 42000",
            "  PreparedStatement avec L'Etranger : 1 livre",
            "apres le try : Statement inutilisable, SQLState 90007");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.URL", "Data.USER", "Data.PASSWORD", "Data.SCHEMA",
            "Data.BOOKS", "Data.INJECTION", "Data.APOSTROPHE", "DriverManager.getConnection(",
            "try (Connection", ".getAutoCommit()", ".isValid(", ".split(\":\", 3)",
            "record Book(", "Integer pages", ".createStatement()", ".execute(",
            ".executeUpdate(", ".executeQuery(", ".prepareStatement(", "try (PreparedStatement",
            "try (ResultSet", ".setString(", ".setInt(", ".setNull(",
            "Types.INTEGER", "rs.next()", ".getString(", ".getInt(",
            ".wasNull()", "LIMIT ? OFFSET ?", "catch (SQLException", ".getSQLState()",
            ".isClosed()", "Optional<Book>",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "LibraryApp", args, EXPECTED, API);
    }
}
