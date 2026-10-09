# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`RouterTest.java`](RouterTest.java), [`TaskApiTest.java`](TaskApiTest.java) et [`WebServerTest.java`](WebServerTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17.0.18**, **JUnit 5.11.4** et le `curl.exe` de Windows 11, le 9 octobre 2026.

---

## Étape 1 — HTTP en dix minutes

**Question — `GET` ne modifie rien :** beaucoup de programmes envoient des `GET` **sans que personne ne clique** : un navigateur qui précharge les liens d'une page pour aller plus vite, un robot d'indexation qui suit tous les liens, un antivirus qui vérifie les liens d'un mail, un cache qui rejoue une requête. Si `GET /tasks/4/delete` effaçait une tâche, le premier robot venu viderait l'atelier. C'est arrivé à de vraies applications.

**Question — l'idempotence :** quand la réponse se perd, le client ne sait pas si la requête est arrivée ; il la **renvoie**. Un `DELETE /tasks/4` reçu deux fois laisse la tâche effacée, comme une seule fois (la seconde réponse est un 404, mais l'état est le même). Un `PUT` reçu deux fois écrit deux fois la même chose. Un `POST /tasks` reçu deux fois crée **deux** tâches : il n'est pas idempotent. C'est pourquoi les clients HTTP réessaient volontiers un `GET`, un `PUT` ou un `DELETE`, et jamais un `POST` sans précaution (projet 4 : la clé d'idempotence).

---

## Étape 2 — Requête, réponse, routeur

Le code : [`Request.java`](Request.java), [`Response.java`](Response.java), [`Handler.java`](Handler.java), [`Router.java`](Router.java).

```java
private Response dispatch(Request request) {
    List<String> path = segments(request.path());
    TreeSet<String> allowed = new TreeSet<>();
    for (Route route : routes) {
        Optional<Map<String, String>> params = match(route.segments(), path);
        if (params.isPresent() && route.method().equals(request.method())) {
            return route.handler().handle(request, params.get());
        }
        params.ifPresent(p -> allowed.add(route.method()));
    }
    if (allowed.isEmpty()) {
        return Response.error(404, "aucune route pour " + request.path());
    }
    return Response.error(405, "methode " + request.method() + " non permise sur " + request.path())
            .withHeader("Allow", String.join(", ", allowed));
}
```

**Question — `split("/", -1)` :** `split` sans second argument **supprime les morceaux vides à la fin** : `"tasks/"` donnerait `[tasks]`, comme `"tasks"`. Les deux chemins se confondraient, et `/tasks/` afficherait la liste. Avec `-1`, les morceaux vides restent : `[tasks, ]` a deux segments et ne correspond pas à `/tasks`. Un `{id}` refuse en plus un segment vide : `/tasks/` ne correspond pas non plus à `/tasks/{id}`. D'où un 404 net, au lieu d'un comportement surprenant.

---

## Étape 3 — Un seul endroit pour les codes d'erreur

```java
public Response handle(Request request) {
    try {
        return dispatch(request);
    } catch (ApiException e) {
        return Response.error(e.status(), e.getMessage());
    } catch (IllegalArgumentException e) {
        return Response.error(400, e.getMessage());
    } catch (NoSuchElementException e) {
        return Response.error(404, e.getMessage());
    } catch (ConflictException e) {
        return Response.error(409, e.getMessage());
    } catch (RuntimeException e) {
        onInternalError.accept(e);
        return Response.error(500, "erreur interne");
    }
}
```

**Question — l'ordre des `catch` :** il compte. Java essaie les `catch` **dans l'ordre** et prend le premier qui convient. Placé en premier, `catch (RuntimeException e)` attraperait tout, et tout deviendrait 500. Le compilateur le refuse d'ailleurs : les `catch` suivants seraient inatteignables (`exception IllegalArgumentException has already been caught`, chapitre 11). La règle : du plus précis au plus général. Ici, les quatre premières exceptions sont sans lien d'héritage entre elles, et leur ordre relatif ne change rien.

---

## Étape 4 — Le port et le dépôt en mémoire

Le code : [`TaskRepository.java`](TaskRepository.java), [`InMemoryTaskRepository.java`](InMemoryTaskRepository.java).

**Question — `synchronized` partout :** un seul fil à la fois dans le dépôt, et chaque méthode dure quelques microsecondes en mémoire : 4 fils qui attendent quelques microsecondes chacun ne se voient pas. Ça deviendrait un problème si une méthode **attendait** quelque chose de lent pendant qu'elle tient le verrou (un disque, le réseau, une base) : tous les autres fils attendraient derrière elle. Alors on réduit la zone protégée, ou on utilise des structures faites pour la concurrence (`ConcurrentHashMap`, `AtomicLong`, chapitre 13). Avec le dépôt JDBC (projet 8), c'est la **base** qui gère la concurrence, avec ses transactions.

---

## Étape 5 — La frontière : du JSON aux objets

Le code : [`TaskJson.java`](TaskJson.java). Les dix corps faux du test de référence donnent tous un 400, avec le message attendu, et aucune tâche n'est créée (vérifié). Exemple : `{"title":"Pneu","column":"A faire","points":1` (l'accolade fermante manque) donne `JSON invalide : ligne 1, colonne 46 : fin du texte inattendue` : le texte fait 45 caractères, et la fin est à la colonne 46.

---

## Étape 6 — Les routes de l'API

Le code : [`TaskApi.java`](TaskApi.java).

```java
private Response create(Request request, Map<String, String> params) {
    requireJson(request);
    Task created = tasks.create(TaskJson.newTask(request.body()));
    return Response.json(201, TaskJson.toJson(created)).withHeader("Location", "/tasks/" + created.id());
}
```

**Question — la taille de page et la sécurité :** sans limite, **une seule** requête `GET /tasks?size=1000000` oblige le serveur à lire un million de tâches, à les garder en mémoire et à écrire des dizaines de mégaoctets de JSON. Quelques requêtes de ce genre envoyées en même temps, et le serveur s'effondre pour tout le monde : c'est un **déni de service**, à la portée de n'importe qui. Toute entrée qui fixe une quantité de travail (une taille, une profondeur comme au projet 1, la taille d'un corps comme à l'étape 7) doit avoir une limite.

---

## Étape 7 — L'adaptateur : le vrai serveur

Le code : [`WebServer.java`](WebServer.java).

```java
private static void serve(HttpExchange exchange, Router router) throws IOException {
    try (exchange) {
        byte[] body = exchange.getRequestBody().readNBytes(MAX_BODY_BYTES + 1);
        if (body.length > MAX_BODY_BYTES) {
            send(exchange, Response.error(413, "corps trop gros (" + MAX_BODY_BYTES + " octets au plus)"));
            return;
        }
        Request request = new Request(exchange.getRequestMethod(), exchange.getRequestURI().getPath(),
                query(exchange.getRequestURI().getRawQuery()), new String(body, StandardCharsets.UTF_8),
                exchange.getRequestHeaders().getFirst("Content-Type"));
        send(exchange, router.handle(request));
    }
}
```

Le test d'intégration complet tourne en moins d'une seconde (vérifié : environ 600 ms la première fois, 90 ms ensuite, le temps que la JVM « chauffe »).

**Expérience — sans `synchronized` sur le dépôt (vérifié) :** sur 10 lancements du test des 50 `POST`, **8 échecs** et 2 réussites. Six fois `expected: <50> but was: <49>` (deux réponses portent la même `Location` : deux tâches ont reçu le même identifiant, et la seconde a écrasé la première), une fois `expected: <51> but was: <49>` et une fois `expected: <51> but was: <50>` (des tâches perdues dans le dépôt). Une erreur de concurrence ne se montre **pas à chaque fois** : un test vert ne prouve pas qu'un code est sûr pour les fils. C'est pour cela que `Check` n'en fait pas un mutant (il survivrait une fois sur cinq), et qu'il vérifie seulement la présence de `synchronized`.

**Question — décoder après avoir coupé :** `q=x%26y` veut dire « le paramètre `q` vaut `x&y` » : le client a **encodé** son `&` (`%26`) précisément pour qu'il ne soit pas pris pour un séparateur. Si l'on décodait d'abord, on obtiendrait `q=x&y`, puis le découpage donnerait `q=x` et un paramètre `y` vide : la valeur du client serait coupée en deux. On découpe sur les séparateurs **réels**, puis on décode chaque morceau.

---

## Étape 8 — La démonstration, curl, et les mutants

Le code : [`ApiDemo.java`](ApiDemo.java).

**La sortie de `ApiDemo` (vérifiée) :**

```
GET /tasks -> 200 [{"id":3,"title":"Commander 12 chambres a air","column":"A faire","points":1,"version":0},{"id":4,"title":"Facture Dupont","column":"A faire","points":1,"version":0}]
POST /tasks -> 201 {"id":8,"title":"Vélo cargo","column":"A faire","points":3,"version":0}
GET /tasks/99 -> 404 {"error":"tache 99 introuvable"}
DELETE /tasks/1 -> 204
serveur ouvert : http://localhost:8080/tasks (Entree pour arreter)
```

**Expérience — `curl.exe -i http://localhost:8080/tasks/2` (vérifié) :**

```
HTTP/1.1 200 OK
Date: Fri, 09 Oct 2026 20:45:38 GMT
Content-type: application/json; charset=utf-8
Content-length: 79

{"id":2,"title":"Regler les freins","column":"En cours","points":3,"version":0}
```

Tu as écrit `Content-Type`, et le serveur du JDK envoie `Content-type` : il remet chaque nom d'en-tête en forme (majuscule au début, minuscules ensuite). Ce n'est pas grave : les noms d'en-têtes HTTP **ne tiennent pas compte de la casse**. C'est aussi pour cela que `HttpClient.headers().firstValue("Content-Type")` les trouve quand même. `Content-length: 79` est la longueur du corps en **octets** (ici, aucun accent : 79 caractères).

**Expérience — le `POST` avec curl (vérifié) :** avec l'en-tête, `HTTP/1.1 201 Created`, `Location: /tasks/9` et la tâche créée. **Sans** l'en-tête : `HTTP/1.1 415 Unsupported Media Type` et `{"error":"Content-Type application/json attendu"}`. Avec `--data-binary`, curl envoie par défaut `Content-Type: application/x-www-form-urlencoded` (le format des formulaires HTML), que l'API refuse. Et `PATCH` : `HTTP/1.1 405 Method Not Allowed`, avec `Allow: DELETE, GET, PUT`.

**Expérience — deux serveurs sur le port 8080 (vérifié) :** le second s'arrête aussitôt :

```
Exception in thread "main" java.net.BindException: Address already in use: bind
```

Un port ne peut être écouté que par **un** programme à la fois. C'est exactement pourquoi les tests utilisent le port 0.

**Les mutants :** avec les tests de référence, les **21 sont tués** (vérifié), chacun par le test attendu. Le `Check` complet prend environ 15 secondes.
