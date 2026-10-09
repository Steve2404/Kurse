package ch19_final.projects.p01_json.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonWriterTest {

    private static final JsonObject TASK = JsonObject.builder()
            .add("id", 7)
            .add("title", "Pneu")
            .add("tags", new JsonArray(List.of(new JsonString("urgent"), new JsonString("achat"))))
            .add("done", false)
            .add("empty", new JsonArray(List.of()))
            .add("meta", new JsonObject(Map.of()))
            .add("owner", new JsonNull())
            .build();

    @Test
    void scalars() {
        assertEquals("null", JsonWriter.compact(new JsonNull()));
        assertEquals("true", JsonWriter.compact(new JsonBool(true)));
        assertEquals("false", JsonWriter.pretty(new JsonBool(false)));
        assertEquals("-12", JsonWriter.compact(JsonNumber.of(-12)));
        assertEquals("1.50", JsonWriter.compact(new JsonNumber(new BigDecimal("1.50"))));
        assertEquals("1E+3", JsonWriter.compact(new JsonNumber(new BigDecimal("1e3"))));
        assertEquals("\"velo\"", JsonWriter.pretty(new JsonString("velo")));
    }

    @ParameterizedTest
    @MethodSource
    void escapes(String value, String expected) {
        assertEquals(expected, JsonWriter.compact(new JsonString(value)));
    }

    static Stream<Arguments> escapes() {
        return Stream.of(
                Arguments.of("", "\"\""),
                Arguments.of("a\"b", "\"a\\\"b\""),
                Arguments.of("a\\b", "\"a\\\\b\""),
                Arguments.of("a/b", "\"a/b\""),
                Arguments.of("l1\nl2", "\"l1\\nl2\""),
                Arguments.of("\r\t\b\f", "\"\\r\\t\\b\\f\""),
                Arguments.of("\u0000", "\"\\u0000\""),
                Arguments.of("\u001f", "\"\\u001f\""),
                Arguments.of("\u0001x", "\"\\u0001x\""),
                Arguments.of(" ", "\" \""),
                Arguments.of("\u007f", "\"\u007f\""),
                Arguments.of("v\u00e9lo \u20ac", "\"v\u00e9lo \u20ac\""),
                Arguments.of("\ud83d\ude00", "\"\ud83d\ude00\""));
    }

    @Test
    void compactHasNoSpaces() {
        assertEquals("{\"id\":7,\"title\":\"Pneu\",\"tags\":[\"urgent\",\"achat\"],\"done\":false,\"empty\":[],"
                + "\"meta\":{},\"owner\":null}", JsonWriter.compact(TASK));
    }

    @Test
    void prettyIndentsTwoSpacesPerLevel() {
        String expected = """
                {
                  "id": 7,
                  "title": "Pneu",
                  "tags": [
                    "urgent",
                    "achat"
                  ],
                  "done": false,
                  "empty": [],
                  "meta": {},
                  "owner": null
                }""";
        assertEquals(expected, JsonWriter.pretty(TASK));
    }

    @Test
    void prettyNested() {
        Json nested = new JsonArray(List.of(
                new JsonObject(Map.of("a", new JsonArray(List.of(JsonNumber.of(1), new JsonArray(List.of(JsonNumber.of(2))))))),
                JsonNumber.of(3)));
        String expected = """
                [
                  {
                    "a": [
                      1,
                      [
                        2
                      ]
                    ]
                  },
                  3
                ]""";
        assertEquals(expected, JsonWriter.pretty(nested));
        assertEquals("[{\"a\":[1,[2]]},3]", JsonWriter.compact(nested));
    }

    @Test
    void emptyContainersStayOnOneLine() {
        assertEquals("[]", JsonWriter.pretty(new JsonArray(List.of())));
        assertEquals("{}", JsonWriter.pretty(new JsonObject(Map.of())));
        assertEquals("[\n  [],\n  {}\n]", JsonWriter.pretty(new JsonArray(List.of(new JsonArray(List.of()), new JsonObject(Map.of())))));
    }

    @Test
    void keysAreEscapedToo() {
        Json o = JsonObject.builder().add("a\"b", 1).add("c\nd", 2).build();
        assertEquals("{\"a\\\"b\":1,\"c\\nd\":2}", JsonWriter.compact(o));
    }

    @Test
    void writerFollowsInsertionOrder() {
        Json o = JsonObject.builder().add("z", 1).add("a", 2).add("m", 3).build();
        assertEquals("{\"z\":1,\"a\":2,\"m\":3}", JsonWriter.compact(o));
    }

    @Test
    void bigDocumentIsFast() {
        List<Json> items = new java.util.ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            items.add(JsonObject.builder().add("id", i).add("title", "t\u00e2che \"" + i + "\"").build());
        }
        String text = org.junit.jupiter.api.Assertions.assertTimeoutPreemptively(java.time.Duration.ofSeconds(3),
                () -> JsonWriter.compact(new JsonArray(items)));
        assertEquals(true, text.endsWith("{\"id\":99999,\"title\":\"t\u00e2che \\\"99999\\\"\"}]"));
    }
}
