package ch15_jdbc.projects.p06_report;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON ReportApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "base H2, pilote H2 JDBC Driver, utilisateur SA, lots true, points de sauvegarde true",
            "titre          | auteur | prix  | parution",
            "---------------+--------+-------+-----------",
            "Les Miserables | Hugo   | 12.90 | 1862-04-03",
            "La Peste       | Camus  |  9.50 | 1947-06-10",
            "Le Proces      | Kafka  |  7.20 | -",
            "L'Etranger     | Camus  |  6.80 | 1942-05-19",
            "(4 lignes)",
            "pays   | avis | moyenne",
            "-------+------+--------",
            "-      |    1 |     4.0",
            "France |    4 |     4.3",
            "(2 lignes)",
            "colonne 1 : label TITRE, nom TITLE, type CHARACTER VARYING, classe Java java.lang.String",
            "colonne 2 : label DOUBLE_PRIX, nom DOUBLE_PRIX, type NUMERIC, classe Java java.math.BigDecimal",
            "AUTHORS : cle ID, [ID INTEGER NOT NULL, NAME CHARACTER VARYING(30) NOT NULL, COUNTRY CHARACTER VARYING(20)], references {}",
            "BOOKS : cle ID, [ID INTEGER NOT NULL, TITLE CHARACTER VARYING(40) NOT NULL, AUTHOR_ID INTEGER NOT NULL, PUBLISHER_ID INTEGER, PRICE DECIMAL(6,2) NOT NULL, PUBLISHED DATE], references {AUTHOR_ID=AUTHORS, PUBLISHER_ID=PUBLISHERS}",
            "PUBLISHERS : cle ID, [ID INTEGER NOT NULL, NAME CHARACTER VARYING(30) NOT NULL], references {}",
            "REVIEWS : cle ID, [ID INTEGER NOT NULL, BOOK_ID INTEGER NOT NULL, STARS INTEGER NOT NULL, NOTE CHARACTER VARYING(40)], references {BOOK_ID=BOOKS}",
            "TAGS : cle ID, [ID INTEGER NOT NULL, LABEL CHARACTER VARYING(15) NOT NULL], references {}",
            "ordre de creation : [AUTHORS, PUBLISHERS, BOOKS, REVIEWS, TAGS]",
            "ordre de suppression : [TAGS, REVIEWS, BOOKS, PUBLISHERS, AUTHORS]",
            "dump : 21 ordres",
            "  CREATE TABLE AUTHORS (ID INTEGER NOT NULL, NAME CHARACTER VARYING(30) NOT NULL, COUNTRY CHARACTER VARYING(20), PRIMARY KEY (ID))",
            "  INSERT INTO AUTHORS VALUES (3, 'Kafka', NULL)",
            "  CREATE TABLE PUBLISHERS (ID INTEGER NOT NULL, NAME CHARACTER VARYING(30) NOT NULL, PRIMARY KEY (ID))",
            "  CREATE TABLE BOOKS (ID INTEGER NOT NULL, TITLE CHARACTER VARYING(40) NOT NULL, AUTHOR_ID INTEGER NOT NULL, PUBLISHER_ID INTEGER, PRICE DECIMAL(6,2) NOT NULL, PUBLISHED DATE, PRIMARY KEY (ID), FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS, FOREIGN KEY (PUBLISHER_ID) REFERENCES PUBLISHERS)",
            "  INSERT INTO BOOKS VALUES (3, 'Le Proces', 3, NULL, 7.20, NULL)",
            "  INSERT INTO BOOKS VALUES (4, 'L''Etranger', 1, 2, 6.80, DATE '1942-05-19')",
            "  CREATE TABLE REVIEWS (ID INTEGER NOT NULL, BOOK_ID INTEGER NOT NULL, STARS INTEGER NOT NULL, NOTE CHARACTER VARYING(40), PRIMARY KEY (ID), FOREIGN KEY (BOOK_ID) REFERENCES BOOKS)",
            "  INSERT INTO REVIEWS VALUES (2, 1, 4, NULL)",
            "  INSERT INTO REVIEWS VALUES (4, 4, 3, 'l''absurde')",
            "  INSERT INTO REVIEWS VALUES (5, 3, 4, NULL)",
            "  CREATE TABLE TAGS (ID INTEGER NOT NULL, LABEL CHARACTER VARYING(15) NOT NULL, PRIMARY KEY (ID))",
            "copie : 5/5 tables identiques",
            "vider AUTHORS en premier : 23503",
            "vider dans l'ordre de suppression : 16 lignes supprimees, il en reste 0");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.URL", "Data.COPY_URL", "Data.USER", "Data.PASSWORD",
            "Data.SCHEMA", "Data.INSERTS", "Data.REPORTS", "ResultSetMetaData",
            ".getMetaData()", ".getColumnCount()", ".getColumnLabel(", ".getColumnName(",
            ".getColumnType(", ".getColumnTypeName(", ".getColumnClassName(", ".getObject(",
            "DatabaseMetaData", ".getDatabaseProductName()", ".getDriverName()", ".getUserName()",
            ".supportsBatchUpdates()", ".supportsSavepoints()", ".getTables(", ".getColumns(",
            ".getPrimaryKeys(", ".getImportedKeys(", "\"TABLE_NAME\"", "\"COLUMN_SIZE\"",
            "\"DECIMAL_DIGITS\"", "DatabaseMetaData.columnNoNulls", "\"FKCOLUMN_NAME\"", "\"PKTABLE_NAME\"",
            "Types.VARCHAR", "Types.DECIMAL", "Types.DATE", ".pollFirst()",
            "IllegalStateException", "Collections.reverse(", ".stripTrailing()",
            // Crescendo : System.exit, printStackTrace et l heure reelle (sortie non deterministe), interdits au chapitre 15.
            "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "ReportApp", args, EXPECTED, API);
    }
}
