package ch19_final.projects.p01_json;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("JsonParser.java", "if (++depth > MAX_DEPTH)", "if (++depth >= MAX_DEPTH)"),
            new Mutant("JsonParser.java", "        depth--;\n        return new JsonArray(values);", "        return new JsonArray(values);"),
            new Mutant("JsonParser.java", "        depth--;\n        return new JsonObject(members);", "        return new JsonObject(members);"),
            new Mutant("JsonParser.java", "if (members.containsKey(key)) {", "if (false) {"),
            new Mutant("JsonParser.java", "if (c < 0x20) {", "if (c == '\\n') {"),
            new Mutant("JsonParser.java", "            case '/' -> '/';\n", ""),
            new Mutant("JsonParser.java", "case 'b' -> '\\b';", "case 'b' -> 'b';"),
            new Mutant("JsonParser.java", "code = code * 16 + digit;", "code = code * 10 + digit;"),
            new Mutant("JsonParser.java", "if (pos < text.length() && isDigit(text.charAt(pos))) {", "if (false) {"),
            new Mutant("JsonParser.java", "if (skip('.') && !digits()) {", "if (skip('.') && false) {"),
            new Mutant("JsonParser.java", "            if (!skip('+')) {\n                skip('-');\n            }\n", "            skip('+');\n"),
            new Mutant("JsonParser.java", "catch (NumberFormatException e)", "catch (IllegalStateException e)"),
            new Mutant("JsonParser.java", "if (parser.pos < text.length()) {", "if (false) {"),
            new Mutant("JsonParser.java", "\" \\t\\n\\r\"", "\" \\t\\n\""),
            new Mutant("JsonParser.java", "lineStart = i + 1;", "lineStart = i;"),
            new Mutant("JsonObject.java", "longValueExact()", "longValue()"),
            new Mutant("JsonObject.java", "Collections.unmodifiableMap(new LinkedHashMap<>(members))", "Map.copyOf(members)"),
            new Mutant("JsonObject.java", "Objects.requireNonNull(value, key)) != null)", "Objects.requireNonNull(value, key)) != null && false)"),
            new Mutant("JsonArray.java", "values = List.copyOf(values);", "values = java.util.Collections.unmodifiableList(values);"),
            new Mutant("JsonWriter.java", "case '\\r' -> out.append(\"\\\\r\");", "case '\\r' -> out.append(\"\\\\n\");"),
            new Mutant("JsonWriter.java", "\"\\\\u%04x\"", "\"\\\\u%04X\""),
            new Mutant("JsonWriter.java", "out.append(pretty ? \": \" : \":\");", "out.append(\": \");"),
            new Mutant("JsonWriter.java", "        if (array.values().isEmpty()) {\n            out.append(\"[]\");\n            return;\n        }\n", ""),
            new Mutant("JsonWriter.java", "            separator = \",\";\n", ""));

    static final List<String> API_CODE = List.of(
            "sealed interface Json permits", "record JsonNull(", "record JsonBool(", "record JsonNumber(",
            "record JsonString(", "record JsonArray(", "record JsonObject(", "class JsonException extends RuntimeException",
            "final class JsonParser", "final class JsonWriter", "final class JsonDemo", "BigDecimal", "StringBuilder",
            "List.copyOf(", "LinkedHashMap", "Data.BOARD", "!Pattern", "!matches(", "!split(", "!replace", "!Scanner",
            "max:method=18",
            "in:JsonParser.java=MAX_DEPTH##une profondeur maximale (MAX_DEPTH)",
            "in:JsonObject.java!Map.copyOf##Map.copyOf melange l'ordre des cles");

    static final List<String> API_TESTS = List.of(
            "@ParameterizedTest", "SplittableRandom", "@RepeatedTest", "assertThrows(", "assertTimeoutPreemptively(",
            "getMessage()", "!System.out", "!Thread.sleep", "!new Random(");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 60, MUTANTS, API_CODE, API_TESTS);
    }
}
