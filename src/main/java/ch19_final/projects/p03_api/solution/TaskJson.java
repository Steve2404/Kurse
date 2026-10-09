package ch19_final.projects.p03_api.solution;

/**
 * La traduction entre le JSON du reseau et les objets du domaine. A la frontiere, on ne fait confiance a rien :
 * chaque champ est verifie, et chaque erreur devient une IllegalArgumentException avec un message pour le client.
 * Les champs inconnus sont ignores : un client plus recent peut envoyer plus que ce qu'on attend.
 */
public final class TaskJson {

    private TaskJson() {
    }

    public static JsonObject toJson(Task task) {
        return JsonObject.builder()
                .add("id", task.id())
                .add("title", task.title())
                .add("column", task.column())
                .add("points", task.points())
                .add("version", task.version())
                .build();
    }

    public static NewTask newTask(String body) {
        JsonObject o = object(body);
        return new NewTask(o.getString("title"), o.getString("column"), intField(o, "points"));
    }

    /** Pour un PUT : l'identifiant vient du chemin, la version du corps (le verrou optimiste). */
    public static Task task(long id, String body) {
        JsonObject o = object(body);
        return new Task(id, o.getString("title"), o.getString("column"), intField(o, "points"), intField(o, "version"));
    }

    private static JsonObject object(String body) {
        Json json;
        try {
            json = JsonParser.parse(body);
        } catch (JsonException e) {
            throw new IllegalArgumentException("JSON invalide : " + e.getMessage(), e);
        }
        if (json instanceof JsonObject o) {
            return o;
        }
        throw new IllegalArgumentException("objet JSON attendu");
    }

    // Un long hors des limites d'un int ne se convertit pas avec (int) : -3000000000 deviendrait 1294967296.
    private static int intField(JsonObject o, String key) {
        long value = o.getLong(key);
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("champ " + key + " : hors limites");
        }
        return (int) value;
    }
}
