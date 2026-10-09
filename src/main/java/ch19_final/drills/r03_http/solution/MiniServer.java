package ch19_final.drills.r03_http.solution;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Le serveur du JDK devant le MiniRouter : corps texte en UTF-8, au plus 1 024 octets. */
public final class MiniServer implements AutoCloseable {

    static final int MAX_BODY = 1024;

    private final HttpServer server;
    private final ExecutorService executor;

    private MiniServer(HttpServer server, ExecutorService executor) {
        this.server = server;
        this.executor = executor;
    }

    public static MiniServer start(int port, MiniRouter router) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        server.setExecutor(executor);
        server.createContext("/", exchange -> serve(exchange, router));
        server.start();
        return new MiniServer(server, executor);
    }

    public int port() {
        return server.getAddress().getPort();
    }

    @Override
    public void close() {
        server.stop(0);
        executor.shutdownNow();
    }

    private static void serve(HttpExchange exchange, MiniRouter router) throws IOException {
        try (exchange) {
            byte[] body = exchange.getRequestBody().readNBytes(MAX_BODY + 1);
            HttpReply reply = body.length > MAX_BODY ? HttpReply.of(413, "corps trop gros")
                    : router.dispatch(exchange.getRequestMethod(), exchange.getRequestURI().getPath(),
                    new String(body, StandardCharsets.UTF_8));
            reply.headers().forEach((name, value) -> exchange.getResponseHeaders().add(name, value));
            if (reply.body() == null) {
                exchange.sendResponseHeaders(reply.status(), -1);
                return;
            }
            byte[] bytes = reply.body().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(reply.status(), bytes.length);
            try (OutputStream out = exchange.getResponseBody()) {
                out.write(bytes);
            }
        }
    }
}
