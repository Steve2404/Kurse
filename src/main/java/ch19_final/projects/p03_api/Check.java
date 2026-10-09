package ch19_final.projects.p03_api;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("Router.java", "if (allowed.isEmpty()) {", "if (true) {"),
            new Mutant("Router.java", "TreeSet<String> allowed = new TreeSet<>();", "java.util.Set<String> allowed = new java.util.LinkedHashSet<>();"),
            new Mutant("Router.java", "if (variable && !path.get(i).isEmpty()) {", "if (variable) {"),
            new Mutant("Router.java", "if (template.size() != path.size()) {", "if (template.size() > path.size()) {"),
            new Mutant("Router.java", "return Response.error(409, e.getMessage());", "return Response.error(400, e.getMessage());"),
            new Mutant("Router.java", "return Response.error(500, \"erreur interne\");", "return Response.error(500, e.toString());"),
            new Mutant("Router.java", "            onInternalError.accept(e);\n", ""),
            new Mutant("TaskApi.java", "Response.json(201, TaskJson.toJson(created))", "Response.json(200, TaskJson.toJson(created))"),
            new Mutant("TaskApi.java", ".withHeader(\"Location\", \"/tasks/\" + created.id())", ".withHeader(\"Location\", \"/tasks\")"),
            new Mutant("TaskApi.java", "static final int DEFAULT_PAGE_SIZE = 20;", "static final int DEFAULT_PAGE_SIZE = 25;"),
            new Mutant("TaskApi.java", "        requireJson(request);\n        return Response.json(200", "        return Response.json(200"),
            new Mutant("TaskApi.java", "type.toLowerCase().startsWith(", "type.startsWith("),
            new Mutant("TaskApi.java", "if (!tasks.delete(id)) {", "if (!tasks.delete(id) && false) {"),
            new Mutant("TaskJson.java", "value < Integer.MIN_VALUE || ", ""),
            new Mutant("TaskJson.java", "throw new IllegalArgumentException(\"JSON invalide : \" + e.getMessage(), e);", "throw e;"),
            new Mutant("WebServer.java", "if (body.length > MAX_BODY_BYTES) {", "if (body.length > MAX_BODY_BYTES + 1) {"),
            new Mutant("WebServer.java", "URLDecoder.decode(value, StandardCharsets.UTF_8)", "value"),
            new Mutant("WebServer.java", "rawQuery.split(\"&\")", "URLDecoder.decode(rawQuery, StandardCharsets.UTF_8).split(\"&\")"),
            new Mutant("WebServer.java", "\"application/json; charset=utf-8\"", "\"application/json\""),
            new Mutant("WebServer.java", "getBytes(StandardCharsets.UTF_8)", "getBytes(StandardCharsets.ISO_8859_1)"),
            new Mutant("InMemoryTaskRepository.java", "if (current.version() != task.version()) {", "if (current.version() < task.version()) {"));

    static final List<String> API_CODE = List.of(
            "record JsonObject(", "final class JsonParser", "final class JsonWriter", "record Task(", "record NewTask(",
            "class ConflictException extends RuntimeException", "class ApiException extends RuntimeException",
            "interface TaskRepository", "final class InMemoryTaskRepository implements TaskRepository", "synchronized",
            "record Request(", "record Response(", "interface Handler", "final class Router", "final class TaskJson",
            "final class TaskApi", "final class WebServer implements AutoCloseable", "final class ApiDemo",
            "HttpServer.create(", "setExecutor(", "URLDecoder.decode(", "readNBytes(", "sendResponseHeaders(",
            "StandardCharsets.UTF_8", "\"Allow\"", "\"Location\"", "Data.SEED", "!printStackTrace", "!getStackTrace",
            "max:method=18",
            "in:Router.java!HttpExchange##le Router ne connait pas le serveur HTTP",
            "in:TaskApi.java!HttpExchange##l'API ne connait pas le serveur HTTP",
            "in:TaskApi.java!Response.error(##les handlers lancent des exceptions, le Router choisit le code",
            "in:WebServer.java!TaskApi##l'adaptateur HTTP ne connait que le Router");

    static final List<String> API_TESTS = List.of(
            "HttpClient", "WebServer.start(0", "@AfterEach", "sendAsync(", "Request.json(", "@ParameterizedTest",
            "assertTimeoutPreemptively(", "!System.out", "!Thread.sleep", "!8080");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 50, MUTANTS, API_CODE, API_TESTS);
    }
}
