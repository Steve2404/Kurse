package ch19_final.drills.r01_json.solution;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Les tests de reference du drill 1. */
class Recall01Test {

    private static String error(String text) {
        return assertThrows(MiniJsonException.class, () -> MiniJson.parse(text)).getMessage();
    }

    @Test
    void d01() {
        assertEquals(true, MiniJson.parse("true"));
        assertEquals(false, MiniJson.parse(" false "));
        assertNull(MiniJson.parse("null"));
        assertEquals(42L, MiniJson.parse("42"));
        assertEquals(-7L, MiniJson.parse("-7"));
        assertEquals(0L, MiniJson.parse("0"));
        assertEquals(9_000_000_000L, MiniJson.parse("9000000000"));
    }

    @Test
    void d02() {
        assertEquals("", MiniJson.parse("\"\""));
        assertEquals("v\u00e9lo", MiniJson.parse("\"v\u00e9lo\""));
        assertEquals("a\"b\\c/d", MiniJson.parse("\"a\\\"b\\\\c\\/d\""));
        assertEquals("l1\nl2\tx", MiniJson.parse("\"l1\\nl2\\tx\""));
        assertEquals("\u00e9\u20ac", MiniJson.parse("\"\\u00e9\\u20AC\""));
    }

    @Test
    void d03() {
        assertEquals(List.of(), MiniJson.parse("[]"));
        assertEquals(List.of(), MiniJson.parse("[ ]"));
        assertEquals(Arrays.asList(1L, "a", true, null), MiniJson.parse("[1, \"a\", true, null]"));
        assertEquals(List.of(List.of(1L, List.of()), List.of(2L)), MiniJson.parse(" [ [1,[ ]] , [2] ] "));
    }

    @Test
    void d04() {
        Object parsed = MiniJson.parse("{\"z\": 1, \"a\": [true], \"m\": {\"x\": null}, \"b\": \"t\"}");
        Map<?, ?> map = (Map<?, ?>) parsed;
        assertEquals(List.of("z", "a", "m", "b"), new ArrayList<>(map.keySet()));
        assertEquals(1L, map.get("z"));
        assertEquals(List.of(true), map.get("a"));
        Map<String, Object> inner = new LinkedHashMap<>();
        inner.put("x", null);
        assertEquals(inner, map.get("m"));
        assertEquals(Map.of(), MiniJson.parse("{ }"));
    }

    @Test
    void d05() {
        assertEquals("colonne 1 : fin du texte inattendue", error(""));
        assertEquals("colonne 1 : valeur attendue", error("tru"));
        assertEquals("colonne 1 : nombre invalide", error("012"));
        assertEquals("colonne 2 : nombre invalide", error("[-]"));
        assertEquals("colonne 1 : nombre invalide", error("99999999999999999999"));
        assertEquals("colonne 4 : ',' ou ']' attendu", error("[1 2]"));
        assertEquals("colonne 4 : valeur attendue", error("[1,]"));
        assertEquals("colonne 3 : fin du texte inattendue", error("[1"));
        assertEquals("colonne 2 : cle attendue", error("{1:2}"));
        assertEquals("colonne 6 : ':' attendu", error("{\"a\" 1}"));
        assertEquals("colonne 7 : ',' ou '}' attendu", error("{\"a\":1]"));
        assertEquals("colonne 5 : chaine non terminee", error("\"abc"));
        assertEquals("colonne 2 : echappement invalide", error("\"\\x\""));
        assertEquals("colonne 2 : echappement invalide", error("\"\\u12g4\""));
        assertEquals("colonne 2 : echappement invalide", error("\"\\u12\""));
        assertEquals("colonne 3 : texte en trop", error("1 2"));
    }

    @Test
    void d06() {
        Map<String, Object> task = new LinkedHashMap<>();
        task.put("id", 7L);
        task.put("title", "Pneu \"avant\"\n");
        task.put("tags", List.of("a", "b"));
        task.put("done", false);
        task.put("owner", null);
        task.put("count", 3);
        String json = MiniJson.write(task);
        assertEquals("{\"id\":7,\"title\":\"Pneu \\\"avant\\\"\\n\",\"tags\":[\"a\",\"b\"],\"done\":false,\"owner\":null,\"count\":3}", json);
        Map<String, Object> back = new LinkedHashMap<>(task);
        back.put("count", 3L);
        assertEquals(back, MiniJson.parse(json));
        assertEquals("\"\\u0001\\\\\\t\"", MiniJson.write("\u0001\\\t"));
        assertEquals("type non JSON : Double", assertThrows(IllegalArgumentException.class, () -> MiniJson.write(1.5)).getMessage());
    }
}
