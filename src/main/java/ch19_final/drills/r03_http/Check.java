package ch19_final.drills.r03_http;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
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
            "record HttpReply(", "interface Route", "final class MiniRouter", "final class Query",
            "final class MiniServer implements AutoCloseable", "HttpServer.create(", "URLDecoder.decode(", "readNBytes(",
            "sendResponseHeaders(", "StandardCharsets.UTF_8", "\"Allow\"", "max:method=18",
            "in:MiniRouter.java!HttpExchange##le routeur ne connait pas le serveur");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
