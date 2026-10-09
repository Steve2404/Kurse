# Drill de rappel 3 — Un routeur et un serveur HTTP, de mémoire

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p03.

**Chrono cible :** 30 min, puis 15 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch19_final.drills.r03_http`.
- Les corps sont du **texte** (pas de JSON dans ce drill).
- Crée les types ci-dessous, **exactement** avec ces noms et ces signatures. `MiniRouter` ne connaît pas `HttpExchange`. Aucune méthode de plus de 18 lignes.
- Tu n'écris pas de tests : les tests de référence vérifient ton code, dont deux avec un vrai serveur et un vrai client.

**Les notions de ce drill ont été apprises dans :** projet 3 (étapes 2, 3 et 7).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : les défis dans l'ordre, `// D04 : ✗` après 3 minutes bloqué, `Check.java`, puis la carte mémoire, puis ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `public record HttpReply(int status, String body, Map<String, String> headers)` (en-têtes copiés ; `body` à `null` : pas de corps), avec `static HttpReply of(int status, String body)` (sans en-tête) et `HttpReply withHeader(String name, String value)` (une copie) ; `@FunctionalInterface public interface Route` : `HttpReply handle(Map<String, String> params, String body)` ; `public final class MiniRouter` : `MiniRouter add(String method, String template, Route route)` (rend `this`) et `HttpReply dispatch(String method, String path, String body)`. Un segment `{id}` correspond à n'importe quel segment **non vide**, et sa valeur arrive dans `params`.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** Aucune route pour ce chemin → `404`, corps `"aucune route pour /nope"` (un `/` final compte : `/tasks/` n'est pas `/tasks`) ; le chemin existe sans cette méthode → `405`, corps `"methode PUT non permise"`, et l'en-tête `Allow` avec les méthodes permises **triées**, séparées par `", "`.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** Les exceptions d'une route deviennent des codes : `IllegalArgumentException` → 400 (son message), `NoSuchElementException` → 404 (son message), toute autre `RuntimeException` → 500, corps `"erreur interne"` (jamais le message).
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `public final class Query`, `public static Map<String, String> parse(String raw)` : `null` ou vide → map vide ; couper sur `&`, puis sur le premier `=` ; décoder **après** avoir coupé (`URLDecoder.decode(…, StandardCharsets.UTF_8)`) ; sans `=`, la valeur est `""` ; en cas de doublon, le **premier** gagne.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `public final class MiniServer implements AutoCloseable`, `public static MiniServer start(int port, MiniRouter router)` (sur `127.0.0.1`, 2 fils), `int port()`, `close()` : chaque échange passe par `dispatch` (le chemin décodé, le corps lu en UTF-8) ; la réponse porte les en-têtes de la `HttpReply`, `Content-Type: text/plain; charset=utf-8` s'il y a un corps, le corps en UTF-8 ; sans corps, `sendResponseHeaders(code, -1)`.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** Un corps de plus de **1 024 octets** (la constante `MAX_BODY`) → `413`, corps `"corps trop gros"`, sans appeler le routeur ; 1 024 octets passent. Après `close()`, le port refuse les connexions.
  → `d06 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **Les segments** : `List.of(path.substring(1).split("/", -1))` ; le `-1` garde le segment vide final.
- **`match`** rend les variables, ou `null` si le modèle ne correspond pas (tailles différentes, ou un segment fixe différent).
- **`dispatch`** : un `try` autour de la recherche, et un `catch` par code, du plus précis au plus général. La recherche parcourt **toutes** les routes : chemin et méthode → la route ; chemin seul → la méthode dans un `TreeSet` ; à la fin, `TreeSet` vide → 404, sinon 405 + `String.join(", ", allowed)`.
- **Le serveur** : `HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0)`, `setExecutor(Executors.newFixedThreadPool(2))`, `createContext("/", exchange -> …)`, `start()` ; `close()` : `stop(0)` et `shutdownNow()`.
- **Un échange** : `try (exchange)`, `readNBytes(MAX_BODY + 1)` (plus que la limite → 413), les en-têtes **avant** `sendResponseHeaders`, puis le corps.

</details>
