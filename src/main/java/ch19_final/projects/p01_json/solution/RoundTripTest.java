package ch19_final.projects.p01_json.solution;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Le test de PROPRIETE : au lieu de quelques exemples choisis a la main, des centaines de documents tires
 * au hasard, et une propriete qui doit toujours etre vraie : relire ce qu'on a ecrit redonne la meme valeur.
 */
class RoundTripTest {

    private static final String CHARS = "aZ 9\"\\/\n\r\t\b\f\u0000\u001fé€{}[],:";

    @RepeatedTest(300)
    void writeThenParseGivesBackTheSameValue(RepetitionInfo info) {
        Json original = randomValue(new SplittableRandom(info.getCurrentRepetition()), 0);
        assertEquals(original, JsonParser.parse(JsonWriter.compact(original)), JsonWriter.compact(original));
        assertEquals(original, JsonParser.parse(JsonWriter.pretty(original)), JsonWriter.compact(original));
    }

    @RepeatedTest(100)
    void compactOfParsedPrettyIsStable(RepetitionInfo info) {
        Json original = randomValue(new SplittableRandom(1000 + info.getCurrentRepetition()), 0);
        String compact = JsonWriter.compact(original);
        assertEquals(compact, JsonWriter.compact(JsonParser.parse(JsonWriter.pretty(original))));
    }

    @Test
    void generatorReachesEveryCase() {
        // Le generateur doit vraiment tirer les six cas (chapitre 18 : un hasard mal regle ne teste rien).
        int[] seen = new int[6];
        for (int seed = 1; seed <= 300; seed++) {
            count(randomValue(new SplittableRandom(seed), 0), seen);
        }
        for (int kind = 0; kind < 6; kind++) {
            assertEquals(true, seen[kind] > 20, "cas " + kind + " vu " + seen[kind] + " fois");
        }
    }

    private static void count(Json json, int[] seen) {
        if (json instanceof JsonObject o) {
            seen[5]++;
            o.members().values().forEach(v -> count(v, seen));
        } else if (json instanceof JsonArray a) {
            seen[4]++;
            a.values().forEach(v -> count(v, seen));
        } else if (json instanceof JsonString) {
            seen[3]++;
        } else if (json instanceof JsonNumber) {
            seen[2]++;
        } else if (json instanceof JsonBool) {
            seen[1]++;
        } else {
            seen[0]++;
        }
    }

    static Json randomValue(SplittableRandom random, int depth) {
        int kind = random.nextInt(depth >= 4 ? 4 : 6);
        return switch (kind) {
            case 0 -> new JsonNull();
            case 1 -> new JsonBool(random.nextBoolean());
            case 2 -> new JsonNumber(BigDecimal.valueOf(random.nextLong(-1_000_000, 1_000_000), random.nextInt(-2, 4)));
            case 3 -> new JsonString(randomString(random));
            case 4 -> randomArray(random, depth);
            default -> randomObject(random, depth);
        };
    }

    private static String randomString(SplittableRandom random) {
        StringBuilder sb = new StringBuilder();
        int length = random.nextInt(8);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    private static Json randomArray(SplittableRandom random, int depth) {
        List<Json> values = new ArrayList<>();
        int size = random.nextInt(5);
        for (int i = 0; i < size; i++) {
            values.add(randomValue(random, depth + 1));
        }
        return new JsonArray(values);
    }

    private static Json randomObject(SplittableRandom random, int depth) {
        Map<String, Json> members = new LinkedHashMap<>();
        int size = random.nextInt(5);
        for (int i = 0; i < size; i++) {
            members.put(randomString(random) + i, randomValue(random, depth + 1));
        }
        return new JsonObject(members);
    }
}
