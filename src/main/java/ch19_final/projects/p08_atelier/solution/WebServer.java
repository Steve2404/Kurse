package ch19_final.projects.p08_atelier.solution;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * L'ADAPTATEUR HTTP (projet 3) : la seule classe qui connait com.sun.net.httpserver. Elle traduit l'echange en
 * Request (avec l'en-tete X-Client), demande la Response au RequestHandler (le Router et ses filtres), et la renvoie.
 */
public final class WebServer implements AutoCloseable {

    /** Au-dela, 413 : sans limite, un client pourrait envoyer 10 Go et remplir la memoire du serveur. */
    static final int MAX_BODY_BYTES = 64 * 1024;

    private final HttpServer server;
    private final ExecutorService executor;

    private WebServer(HttpServer server, ExecutorService executor) {
        this.server = server;
        this.executor = executor;
    }

    /** Demarre le serveur sur 127.0.0.1 ; le port 0 laisse le systeme choisir un port libre (ideal pour les tests). */
    public static WebServer start(int port, RequestHandler handler) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        // Plusieurs fils : une requete lente n'en bloque pas d'autres. Le depot doit donc etre sur pour les fils.
        ExecutorService executor = Executors.newFixedThreadPool(4);
        server.setExecutor(executor);
        server.createContext("/", exchange -> serve(exchange, handler));
        server.start();
        return new WebServer(server, executor);
    }

    public int port() {
        return server.getAddress().getPort();
    }

    @Override
    public void close() {
        server.stop(0);
        executor.shutdownNow();
    }

    private static void serve(HttpExchange exchange, RequestHandler handler) throws IOException {
        try (exchange) {
            byte[] body = exchange.getRequestBody().readNBytes(MAX_BODY_BYTES + 1);
            if (body.length > MAX_BODY_BYTES) {
                send(exchange, Response.error(413, "corps trop gros (" + MAX_BODY_BYTES + " octets au plus)"));
                return;
            }
            Request request = new Request(exchange.getRequestMethod(), exchange.getRequestURI().getPath(),
                    query(exchange.getRequestURI().getRawQuery()), new String(body, StandardCharsets.UTF_8),
                    exchange.getRequestHeaders().getFirst("Content-Type"), exchange.getRequestHeaders().getFirst("X-Client"));
            send(exchange, handler.handle(request));
        }
    }

    // ?column=A%20faire&page=1 -> {column=A faire, page=1}. On decode APRES avoir coupe : un %26 reste un & dans la valeur.
    static Map<String, String> query(String rawQuery) {
        Map<String, String> query = new HashMap<>();
        if (rawQuery == null || rawQuery.isEmpty()) {
            return query;
        }
        for (String pair : rawQuery.split("&")) {
            int eq = pair.indexOf('=');
            String name = eq < 0 ? pair : pair.substring(0, eq);
            String value = eq < 0 ? "" : pair.substring(eq + 1);
            query.putIfAbsent(URLDecoder.decode(name, StandardCharsets.UTF_8), URLDecoder.decode(value, StandardCharsets.UTF_8));
        }
        return query;
    }

    private static void send(HttpExchange exchange, Response response) throws IOException {
        response.headers().forEach((name, value) -> exchange.getResponseHeaders().add(name, value));
        if (response.body() == null) {
            exchange.sendResponseHeaders(response.status(), -1); // -1 : aucun corps
            return;
        }
        byte[] bytes = JsonWriter.compact(response.body()).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(response.status(), bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
