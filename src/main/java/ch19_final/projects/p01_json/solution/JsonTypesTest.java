package ch19_final.projects.p01_json.solution;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonTypesTest {

    @Test
    void valuesAreComparedByContent() {
        assertEquals(new JsonNull(), new JsonNull());
        assertEquals(new JsonString("a"), new JsonString("a"));
        assertEquals(JsonNumber.of(5), new JsonNumber(new BigDecimal("5")));
        assertNotEquals(new JsonNumber(new BigDecimal("1.0")), new JsonNumber(new BigDecimal("1.00")));
        assertNotEquals(new JsonString("1"), JsonNumber.of(1));
    }

    @Test
    void sealedInterfaceListsTheSixCases() {
        assertTrue(Json.class.isSealed());
        assertEquals(6, Json.class.getPermittedSubclasses().length);
    }

    @Test
    void nullComponentsAreRefused() {
        assertThrows(NullPointerException.class, () -> new JsonString(null));
        assertThrows(NullPointerException.class, () -> new JsonNumber(null));
        List<Json> withNull = new ArrayList<>();
        withNull.add(null);
        assertThrows(NullPointerException.class, () -> new JsonArray(withNull));
        Map<String, Json> nullValue = new LinkedHashMap<>();
        nullValue.put("a", null);
        assertThrows(NullPointerException.class, () -> new JsonObject(nullValue));
        Map<String, Json> nullKey = new LinkedHashMap<>();
        nullKey.put(null, new JsonNull());
        assertThrows(NullPointerException.class, () -> new JsonObject(nullKey));
    }

    @Test
    void arrayIsAnUnmodifiableCopy() {
        List<Json> source = new ArrayList<>(List.of(JsonNumber.of(1)));
        JsonArray array = new JsonArray(source);
        source.add(JsonNumber.of(2));
        assertEquals(1, array.values().size());
        assertThrows(UnsupportedOperationException.class, () -> array.values().add(new JsonNull()));
    }

    @Test
    void objectIsAnUnmodifiableOrderedCopy() {
        Map<String, Json> source = new LinkedHashMap<>();
        for (String key : List.of("z", "y", "x", "w", "v", "u", "t", "s")) {
            source.put(key, new JsonString(key));
        }
        JsonObject object = new JsonObject(source);
        source.put("extra", new JsonNull());
        assertEquals(List.of("z", "y", "x", "w", "v", "u", "t", "s"), List.copyOf(object.members().keySet()));
        assertThrows(UnsupportedOperationException.class, () -> object.members().put("k", new JsonNull()));
    }

    @Test
    void objectEqualityIgnoresOrder() {
        Json ab = JsonObject.builder().add("a", 1).add("b", 2).build();
        Json ba = JsonObject.builder().add("b", 2).add("a", 1).build();
        assertEquals(ab, ba);
        assertEquals(ab.hashCode(), ba.hashCode());
    }

    @Test
    void builderKeepsOrderAndRefusesDuplicates() {
        JsonObject.Builder builder = JsonObject.builder().add("title", "Pneu").add("points", 3).add("done", true)
                .add("tags", new JsonArray(List.of()));
        JsonObject o = builder.build();
        assertEquals(List.of("title", "points", "done", "tags"), List.copyOf(o.members().keySet()));
        assertEquals(new JsonString("Pneu"), o.members().get("title"));
        assertEquals(JsonNumber.of(3), o.members().get("points"));
        assertEquals(new JsonBool(true), o.members().get("done"));
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> builder.add("points", 4));
        assertEquals("cle en double : points", e.getMessage());
    }

    @Test
    void builtObjectDoesNotChangeWithTheBuilder() {
        JsonObject.Builder builder = JsonObject.builder().add("a", 1);
        JsonObject first = builder.build();
        builder.add("b", 2);
        assertEquals(1, first.members().size());
        assertEquals(2, builder.build().members().size());
    }

    @Test
    void typedGetters() {
        JsonObject o = (JsonObject) JsonParser.parse(
                "{\"title\":\"Pneu\",\"id\":42,\"big\":1e3,\"exact\":2.0,\"half\":1.5,\"done\":true,\"n\":null,"
                        + "\"huge\":12345678901234567890}");
        assertEquals("Pneu", o.getString("title"));
        assertEquals(42, o.getLong("id"));
        assertEquals(1000, o.getLong("big"));
        assertEquals(2, o.getLong("exact"));
        assertTrue(o.getBoolean("done"));
        assertEquals(new JsonNull(), o.get("n").orElseThrow());
        assertTrue(o.get("absent").isEmpty());
    }

    @Test
    void typedGettersRefuseWrongTypes() {
        JsonObject o = (JsonObject) JsonParser.parse(
                "{\"title\":\"Pneu\",\"id\":42,\"half\":1.5,\"done\":true,\"huge\":12345678901234567890}");
        assertEquals("champ id : chaine attendue",
                assertThrows(IllegalArgumentException.class, () -> o.getString("id")).getMessage());
        assertEquals("champ absent : chaine attendue",
                assertThrows(IllegalArgumentException.class, () -> o.getString("absent")).getMessage());
        assertEquals("champ title : nombre entier attendu",
                assertThrows(IllegalArgumentException.class, () -> o.getLong("title")).getMessage());
        assertEquals("champ half : nombre entier attendu",
                assertThrows(IllegalArgumentException.class, () -> o.getLong("half")).getMessage());
        assertEquals("champ huge : nombre entier attendu",
                assertThrows(IllegalArgumentException.class, () -> o.getLong("huge")).getMessage());
        assertEquals("champ id : booleen attendu",
                assertThrows(IllegalArgumentException.class, () -> o.getBoolean("id")).getMessage());
        assertEquals("champ absent : booleen attendu",
                assertThrows(IllegalArgumentException.class, () -> o.getBoolean("absent")).getMessage());
    }
}
