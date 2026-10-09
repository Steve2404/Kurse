package ch19_final.projects.p01_json;

/**
 * Les donnees FOURNIES du projet 1 (ne pas modifier) : l'export d'un tableau de taches, tel que l'envoie
 * l'application de l'equipe. Dans un text block, \\ ecrit UN antislash : le JSON contient donc \\u00e9 et \\".
 */
public final class Data {

    private Data() {
    }

    public static final String BOARD = """
            {
              "board": "Atelier v\\u00e9los",
              "version": 3,
              "columns": ["A faire", "En cours", "Fini"],
              "wipLimit": 2,
              "tasks": [
                {"id": 1, "title": "Changer la cha\\u00eene", "column": "Fini", "points": 2, "done": true, "tags": ["urgent"]},
                {"id": 2, "title": "R\\u00e9gler les freins \\"V-brake\\"", "column": "En cours", "points": 3, "done": false, "tags": []},
                {"id": 3, "title": "Commander 12 chambres \\u00e0 air", "column": "A faire", "points": 0.5, "done": false, "tags": ["achat", "fournisseur"]},
                {"id": 4, "title": "Facture\\tClient\\\\Dupont", "column": "A faire", "points": 1e1, "done": false, "assignee": null}
              ]
            }
            """;

    /** Le meme export, abime par un copier-coller : il manque une virgule. */
    public static final String BROKEN = """
            {
              "board": "Atelier",
              "tasks": [
                {"id": 1, "title": "Pneu"}
                {"id": 2, "title": "Selle"}
              ]
            }
            """;
}
