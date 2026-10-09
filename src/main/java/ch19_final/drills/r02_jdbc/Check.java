package ch19_final.drills.r02_jdbc;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON code ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            "d01 : 1 executions, 1 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 1 executions, 1 reussies");

    static final List<String> API = List.of(
            "interface SqlWork", "class DataException extends RuntimeException", "final class Tx", "final class Schema",
            "final class Bank", "setAutoCommit(false)", "rollback()", "getSQLState()", "RETURN_GENERATED_KEYS", "ESCAPE",
            "version = version + 1", "!getMessage()", "!DriverManager", "max:method=18",
            "in:Bank.java!createStatement##la banque n'utilise que des PreparedStatement");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
