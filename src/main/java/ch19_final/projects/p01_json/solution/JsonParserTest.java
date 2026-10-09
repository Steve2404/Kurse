package ch19_final.projects.p01_json.solution;

import ch19_final.projects.p01_json.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class JsonParserTest {

    private static JsonNumber num(String text) {
        return new JsonNumber(new BigDecimal(text));
    }

    // ------------------------------------------------------------------ les valeurs simples

    @Test
    void literals() {
        assertEquals(new JsonBool(true), JsonParser.parse("true"));
        assertEquals(new JsonBool(false), JsonParser.parse("false"));
        assertEquals(new JsonNull(), JsonParser.parse("null"));
    }

    @ParameterizedTest
    @MethodSource
    void numbers(String text, String expected) {
        assertEquals(num(expected), JsonParser.parse(text));
    }

    static Stream<Arguments> numbers() {
        return Stream.of(
                Arguments.of("0", "0"),
                Arguments.of("-0", "0"),
                Arguments.of("42", "42"),
                Arguments.of("-17", "-17"),
                Arguments.of("3.25", "3.25"),
                Arguments.of("1.50", "1.50"),
                Arguments.of("1e3", "1E+3"),
                Arguments.of("2E-2", "0.02"),
                Arguments.of("-1.5e+2", "-1.5E+2"),
                Arguments.of("12345678901234567890123", "12345678901234567890123"),
                Arguments.of("0.1", "0.1"));
    }

    @Test
    void numberKeepsScale() {
        // BigDecimal.equals compare l'echelle : 1.50 n'est pas egal a 1.5
        assertEquals(new BigDecimal("1.50"), ((JsonNumber) JsonParser.parse("1.50")).value());
        assertEquals(2, ((JsonNumber) JsonParser.parse("1.50")).value().scale());
    }

    @ParameterizedTest
    @MethodSource
    void strings(String text, String expected) {
        assertEquals(new JsonString(expected), JsonParser.parse(text));
    }

    static Stream<Arguments> strings() {
        return Stream.of(
                Arguments.of("\"\"", ""),
                Arguments.of("\"velo\"", "velo"),
                Arguments.of("\"v\u00e9lo\"", "v\u00e9lo"),
                Arguments.of("\"a\\\"b\"", "a\"b"),
                Arguments.of("\"a\\\\b\"", "a\\b"),
                Arguments.of("\"a\\/b\"", "a/b"),
                Arguments.of("\"\\b\\f\\n\\r\\t\"", "\b\f\n\r\t"),
                Arguments.of("\"\\u00e9t\\u00C9\"", "\u00e9t\u00c9"),
                Arguments.of("\"\\ud83d\\ude00\"", "\ud83d\ude00"),
                Arguments.of("\"[1, 2]\"", "[1, 2]"));
    }

    // ------------------------------------------------------------------ tableaux et objets

    @Test
    void arrays() {
        assertEquals(new JsonArray(List.of()), JsonParser.parse("[]"));
        assertEquals(new JsonArray(List.of()), JsonParser.parse(" [ \n ] "));
        assertEquals(new JsonArray(List.of(JsonNumber.of(1), new JsonString("a"), new JsonNull())),
                JsonParser.parse("[1,\"a\",null]"));
        assertEquals(new JsonArray(List.of(new JsonArray(List.of(JsonNumber.of(1))), new JsonArray(List.of()))),
                JsonParser.parse("[ [ 1 ] , [ ] ]"));
    }

    @Test
    void objects() {
        assertEquals(new JsonObject(Map.of()), JsonParser.parse("{}"));
        assertEquals(new JsonObject(Map.of()), JsonParser.parse("{ \t }"));
        assertEquals(new JsonObject(Map.of("a", JsonNumber.of(1), "b", new JsonArray(List.of(new JsonBool(true))))),
                JsonParser.parse("{\"a\":1,\"b\":[true]}"));
        assertEquals(new JsonObject(Map.of("x", new JsonObject(Map.of("y", new JsonNull())))),
                JsonParser.parse("\r\n{ \"x\" : { \"y\" : null } }\n"));
    }

    @Test
    void objectKeepsDocumentOrder() {
        JsonObject o = (JsonObject) JsonParser.parse("{\"z\":1,\"a\":2,\"m\":3,\"b\":4,\"y\":5}");
        assertEquals(List.of("z", "a", "m", "b", "y"), List.copyOf(o.members().keySet()));
    }

    @Test
    void emptyKeyIsAllowed() {
        assertEquals(new JsonObject(Map.of("", JsonNumber.of(1))), JsonParser.parse("{\"\":1}"));
    }

    @Test
    void dataBoard() {
        JsonObject board = (JsonObject) JsonParser.parse(Data.BOARD);
        assertEquals("Atelier v\u00e9los", board.getString("board"));
        assertEquals(3, board.getLong("version"));
        JsonArray tasks = (JsonArray) board.get("tasks").orElseThrow();
        assertEquals(4, tasks.values().size());
        JsonObject second = (JsonObject) tasks.values().get(1);
        assertEquals("R\u00e9gler les freins \"V-brake\"", second.getString("title"));
        JsonObject fourth = (JsonObject) tasks.values().get(3);
        assertEquals("Facture\tClient\\Dupont", fourth.getString("title"));
        assertEquals(num("1E+1"), fourth.get("points").orElseThrow());
        assertEquals(new JsonNull(), fourth.get("assignee").orElseThrow());
    }

    // ------------------------------------------------------------------ les erreurs : quoi et ou

    @ParameterizedTest
    @MethodSource
    void errors(String text, String message) {
        JsonException e = assertThrows(JsonException.class, () -> JsonParser.parse(text));
        assertEquals(message, e.getMessage());
    }

    static Stream<Arguments> errors() {
        return Stream.of(
                Arguments.of("", "ligne 1, colonne 1 : fin du texte inattendue"),
                Arguments.of("   ", "ligne 1, colonne 4 : fin du texte inattendue"),
                Arguments.of("tru", "ligne 1, colonne 1 : valeur attendue"),
                Arguments.of("nul", "ligne 1, colonne 1 : valeur attendue"),
                Arguments.of("True", "ligne 1, colonne 1 : valeur attendue"),
                Arguments.of("'a'", "ligne 1, colonne 1 : valeur attendue"),
                Arguments.of("+1", "ligne 1, colonne 1 : valeur attendue"),
                Arguments.of(".5", "ligne 1, colonne 1 : valeur attendue"),
                Arguments.of("-", "ligne 1, colonne 1 : nombre invalide"),
                Arguments.of("[-x]", "ligne 1, colonne 2 : nombre invalide"),
                Arguments.of("012", "ligne 1, colonne 1 : nombre invalide"),
                Arguments.of("-01", "ligne 1, colonne 1 : nombre invalide"),
                Arguments.of("1.", "ligne 1, colonne 1 : nombre invalide"),
                Arguments.of("1.e3", "ligne 1, colonne 1 : nombre invalide"),
                Arguments.of("1e", "ligne 1, colonne 1 : nombre invalide"),
                Arguments.of("[7, 1e+]", "ligne 1, colonne 5 : nombre invalide"),
                Arguments.of("1e99999999999", "ligne 1, colonne 1 : nombre invalide"),
                Arguments.of("\"abc", "ligne 1, colonne 5 : chaine non terminee"),
                Arguments.of("\"abc\\", "ligne 1, colonne 6 : chaine non terminee"),
                Arguments.of("\"a\tb\"", "ligne 1, colonne 3 : caractere de controle dans une chaine"),
                Arguments.of("\"a\nb\"", "ligne 1, colonne 3 : caractere de controle dans une chaine"),
                Arguments.of("\"a\\xb\"", "ligne 1, colonne 3 : echappement invalide"),
                Arguments.of("\"\\u12g4\"", "ligne 1, colonne 2 : echappement invalide"),
                Arguments.of("\"\\u12\"", "ligne 1, colonne 2 : echappement invalide"),
                Arguments.of("\"\\u12", "ligne 1, colonne 2 : echappement invalide"),
                Arguments.of("[", "ligne 1, colonne 2 : fin du texte inattendue"),
                Arguments.of("[1", "ligne 1, colonne 3 : fin du texte inattendue"),
                Arguments.of("[1,", "ligne 1, colonne 4 : fin du texte inattendue"),
                Arguments.of("[1 2]", "ligne 1, colonne 4 : ',' ou ']' attendu"),
                Arguments.of("[1,]", "ligne 1, colonne 4 : valeur attendue"),
                Arguments.of("[,1]", "ligne 1, colonne 2 : valeur attendue"),
                Arguments.of("[1}", "ligne 1, colonne 3 : ',' ou ']' attendu"),
                Arguments.of("{", "ligne 1, colonne 2 : fin du texte inattendue"),
                Arguments.of("{1:2}", "ligne 1, colonne 2 : cle attendue"),
                Arguments.of("{a:2}", "ligne 1, colonne 2 : cle attendue"),
                Arguments.of("{\"a\" 2}", "ligne 1, colonne 6 : ':' attendu"),
                Arguments.of("{\"a\"", "ligne 1, colonne 5 : fin du texte inattendue"),
                Arguments.of("{\"a\":}", "ligne 1, colonne 6 : valeur attendue"),
                Arguments.of("{\"a\":1,}", "ligne 1, colonne 8 : cle attendue"),
                Arguments.of("{\"a\":1 \"b\":2}", "ligne 1, colonne 8 : ',' ou '}' attendu"),
                Arguments.of("{\"a\":1]", "ligne 1, colonne 7 : ',' ou '}' attendu"),
                Arguments.of("{\"a\":1,\"b\":2,\"a\":3}", "ligne 1, colonne 14 : cle en double : \"a\""),
                Arguments.of("1 2", "ligne 1, colonne 3 : texte en trop apres la valeur"),
                Arguments.of("{} {}", "ligne 1, colonne 4 : texte en trop apres la valeur"),
                Arguments.of("truex", "ligne 1, colonne 5 : texte en trop apres la valeur"),
                Arguments.of("[\n  1,\n  2\n  3\n]", "ligne 4, colonne 3 : ',' ou ']' attendu"),
                Arguments.of("{\n\"a\": [\n\"x\",\n\"y\n\"]}", "ligne 4, colonne 3 : caractere de controle dans une chaine"),
                Arguments.of("\r\n\r\n  @", "ligne 3, colonne 3 : valeur attendue"));
    }

    @Test
    void exceptionGivesLineAndColumn() {
        JsonException e = assertThrows(JsonException.class,
                () -> JsonParser.parse(Data.BROKEN));
        assertEquals(5, e.line());
        assertEquals(5, e.column());
        assertEquals("ligne 5, colonne 5 : ',' ou ']' attendu", e.getMessage());
    }

    @Test
    void exceptionIsUnchecked() {
        RuntimeException e = assertThrows(RuntimeException.class, () -> JsonParser.parse("?"));
        assertEquals(JsonException.class, e.getClass());
    }

    // ------------------------------------------------------------------ la profondeur et la taille

    @Test
    void depthLimitIsExactly500() {
        String ok = "[".repeat(500) + "]".repeat(500);
        Json value = JsonParser.parse(ok);
        for (int i = 0; i < 499; i++) {
            value = ((JsonArray) value).values().get(0);
        }
        assertEquals(new JsonArray(List.of()), value);
        JsonException e = assertThrows(JsonException.class, () -> JsonParser.parse("[".repeat(501) + "]".repeat(501)));
        assertEquals("ligne 1, colonne 501 : trop de niveaux d'imbrication (500 au plus)", e.getMessage());
    }

    @Test
    void depthCountsObjectsToo() {
        String ok = "{\"a\":".repeat(250) + "[".repeat(250) + "]".repeat(250) + "}".repeat(250);
        JsonParser.parse(ok);
        String deep = "{\"a\":".repeat(250) + "[".repeat(251) + "]".repeat(251) + "}".repeat(250);
        JsonException e = assertThrows(JsonException.class, () -> JsonParser.parse(deep));
        assertEquals("ligne 1, colonne 1501 : trop de niveaux d'imbrication (500 au plus)", e.getMessage());
    }

    @Test
    void depthGoesBackDownAfterEachArray() {
        // 1000 tableaux voisins de profondeur 1 : la profondeur redescend apres chaque ']'
        String wide = "[" + "[],".repeat(999) + "[]]";
        assertEquals(1000, ((JsonArray) JsonParser.parse(wide)).values().size());
        String wideObjects = "[" + "{},".repeat(999) + "{}]";
        assertEquals(1000, ((JsonArray) JsonParser.parse(wideObjects)).values().size());
    }

    @Test
    void hugeNestingIsRefusedNotAStackOverflow() {
        String bomb = "[".repeat(100_000);
        JsonException e = assertThrows(JsonException.class, () -> JsonParser.parse(bomb));
        assertEquals(501, e.column());
    }

    @Test
    void bigDocumentIsFast() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 100_000; i++) {
            sb.append(i == 0 ? "" : ",").append("{\"id\":").append(i).append(",\"title\":\"t\\u00e2che ").append(i).append("\"}");
        }
        String big = sb.append(']').toString();
        Json parsed = assertTimeoutPreemptively(java.time.Duration.ofSeconds(3), () -> JsonParser.parse(big));
        JsonObject last = (JsonObject) ((JsonArray) parsed).values().get(99_999);
        assertEquals("t\u00e2che 99999", last.getString("title"));
    }

    @Test
    void parsedObjectIsAnOrderedCopy() {
        Map<String, Json> members = new LinkedHashMap<>();
        members.put("b", JsonNumber.of(2));
        members.put("a", JsonNumber.of(1));
        assertEquals(new JsonObject(members), JsonParser.parse("{\"a\":1,\"b\":2}"));
    }
}
