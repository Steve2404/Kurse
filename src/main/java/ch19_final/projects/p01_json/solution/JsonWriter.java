package ch19_final.projects.p01_json.solution;

import java.util.Map;

/**
 * L'ecrivain : le chemin inverse du parseur. compact pour le reseau (aucun espace), pretty pour les humains
 * (2 espaces par niveau). Un StringBuilder pour tout le document : concatener des String dans une boucle
 * recopierait tout le texte a chaque fois (n^2).
 */
public final class JsonWriter {

    private JsonWriter() {
    }

    public static String compact(Json json) {
        StringBuilder out = new StringBuilder();
        write(json, out, false, 0);
        return out.toString();
    }

    public static String pretty(Json json) {
        StringBuilder out = new StringBuilder();
        write(json, out, true, 0);
        return out.toString();
    }

    // Java 17 n'a pas encore le switch sur les types : une chaine de instanceof, et le dernier cas (JsonNull)
    // est le seul qui reste grace a sealed.
    private static void write(Json json, StringBuilder out, boolean pretty, int level) {
        if (json instanceof JsonObject o) {
            writeObject(o.members(), out, pretty, level);
        } else if (json instanceof JsonArray a) {
            writeArray(a, out, pretty, level);
        } else if (json instanceof JsonString s) {
            writeString(s.value(), out);
        } else if (json instanceof JsonNumber n) {
            out.append(n.value());
        } else if (json instanceof JsonBool b) {
            out.append(b.value());
        } else {
            out.append("null");
        }
    }

    private static void writeArray(JsonArray array, StringBuilder out, boolean pretty, int level) {
        if (array.values().isEmpty()) {
            out.append("[]");
            return;
        }
        out.append('[');
        for (int i = 0; i < array.values().size(); i++) {
            out.append(i == 0 ? "" : ",");
            newline(out, pretty, level + 1);
            write(array.values().get(i), out, pretty, level + 1);
        }
        newline(out, pretty, level);
        out.append(']');
    }

    private static void writeObject(Map<String, Json> members, StringBuilder out, boolean pretty, int level) {
        if (members.isEmpty()) {
            out.append("{}");
            return;
        }
        out.append('{');
        String separator = "";
        for (Map.Entry<String, Json> member : members.entrySet()) {
            out.append(separator);
            newline(out, pretty, level + 1);
            writeString(member.getKey(), out);
            out.append(pretty ? ": " : ":");
            write(member.getValue(), out, pretty, level + 1);
            separator = ",";
        }
        newline(out, pretty, level);
        out.append('}');
    }

    private static void newline(StringBuilder out, boolean pretty, int level) {
        if (pretty) {
            out.append('\n').append("  ".repeat(level));
        }
    }

    private static void writeString(String value, StringBuilder out) {
        out.append('"');
        for (int i = 0; i < value.length(); i++) {
            escape(value.charAt(i), out);
        }
        out.append('"');
    }

    // Seuls " et \ et les caracteres de controle (< 0x20) doivent etre echappes ; les accents restent tels quels.
    private static void escape(char c, StringBuilder out) {
        switch (c) {
            case '"' -> out.append("\\\"");
            case '\\' -> out.append("\\\\");
            case '\n' -> out.append("\\n");
            case '\r' -> out.append("\\r");
            case '\t' -> out.append("\\t");
            case '\b' -> out.append("\\b");
            case '\f' -> out.append("\\f");
            default -> out.append(c < 0x20 ? String.format("\\u%04x", (int) c) : String.valueOf(c));
        }
    }
}
