package ch19_final.projects.p01_json.solution;

import ch19_final.projects.p01_json.Data;

/** La demonstration : lire l'export, le reecrire, et montrer une erreur bien localisee. */
public final class JsonDemo {

    private JsonDemo() {
    }

    public static void main(String[] args) {
        JsonObject board = (JsonObject) JsonParser.parse(Data.BOARD);
        System.out.println(board.getString("board") + " : version " + board.getLong("version"));
        System.out.println(JsonWriter.compact(board));
        System.out.println(JsonWriter.pretty(board.get("columns").orElseThrow()));
        try {
            JsonParser.parse(Data.BROKEN);
        } catch (JsonException e) {
            System.out.println("refus : " + e.getMessage());
        }
    }
}
