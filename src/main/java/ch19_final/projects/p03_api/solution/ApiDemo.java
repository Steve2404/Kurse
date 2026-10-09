package ch19_final.projects.p03_api.solution;

import ch19_final.projects.p03_api.Data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * La demonstration : le serveur sur le port 8080, quelques requetes d'un vrai client HTTP, puis le serveur
 * reste ouvert pour ton navigateur et curl.exe, jusqu'a ce que tu appuies sur Entree.
 */
public final class ApiDemo {

    private ApiDemo() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        InMemoryTaskRepository tasks = new InMemoryTaskRepository();
        for (String[] row : Data.SEED) {
            tasks.create(new NewTask(row[0], row[1], Integer.parseInt(row[2])));
        }
        try (WebServer server = WebServer.start(8080, TaskApi.routes(tasks, e -> System.err.println("erreur interne : " + e)))) {
            String base = "http://localhost:" + server.port();
            HttpClient client = HttpClient.newHttpClient();
            show(client, HttpRequest.newBuilder(URI.create(base + "/tasks?column=A%20faire&size=2")).build());
            show(client, HttpRequest.newBuilder(URI.create(base + "/tasks")).header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"title\":\"V\u00e9lo cargo\",\"column\":\"A faire\",\"points\":3}")).build());
            show(client, HttpRequest.newBuilder(URI.create(base + "/tasks/99")).build());
            show(client, HttpRequest.newBuilder(URI.create(base + "/tasks/1")).DELETE().build());
            System.out.println("serveur ouvert : " + base + "/tasks (Entree pour arreter)");
            System.in.read();
        }
    }

    private static void show(HttpClient client, HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(request.method() + " " + request.uri().getRawPath() + " -> " + response.statusCode() + " " + response.body());
    }
}
