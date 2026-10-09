package ch19_final.drills.r01_json.solution;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Un mini JSON sur une ligne, avec des objets Java ordinaires : null, Boolean, Long, String, List, Map.
 * La descente recursive du projet 1, en version courte : une methode par regle, un curseur pos.
 */
public final class MiniJson {

    private final String text;
    private int pos;

    private MiniJson(String text) {
        this.text = text;
    }

    public static Object parse(String text) {
        MiniJson p = new MiniJson(text);
        Object value = p.value();
        p.spaces();
        if (p.pos < text.length()) {
            throw p.error("texte en trop");
        }
        return value;
    }

    private Object value() {
        char c = peek();
        if (c == '[') {
            return array();
        }
        if (c == '{') {
            return object();
        }
        if (c == '"') {
            return string();
        }
        if (c == '-' || Character.isDigit(c)) {
            return number();
        }
        return literal();
    }

    private Object literal() {
        for (String word : List.of("true", "false", "null")) {
            if (text.startsWith(word, pos)) {
                pos += word.length();
                return word.equals("null") ? null : Boolean.valueOf(word);
            }
        }
        throw error("valeur attendue");
    }

    private Long number() {
        int start = pos;
        if (text.charAt(pos) == '-') {
            pos++;
        }
        int digitsStart = pos;
        while (pos < text.length() && Character.isDigit(text.charAt(pos))) {
            pos++;
        }
        int digits = pos - digitsStart;
        if (digits == 0 || digits > 1 && text.charAt(digitsStart) == '0') {
            throw new MiniJsonException(start + 1, "nombre invalide");
        }
        try {
            return Long.parseLong(text.substring(start, pos));
        } catch (NumberFormatException e) {
            throw new MiniJsonException(start + 1, "nombre invalide"); // trop grand pour un long
        }
    }

    private String string() {
        pos++;
        StringBuilder sb = new StringBuilder();
        while (pos < text.length() && text.charAt(pos) != '"') {
            char c = text.charAt(pos++);
            sb.append(c == '\\' ? escape() : c);
        }
        if (pos >= text.length()) {
            throw error("chaine non terminee");
        }
        pos++;
        return sb.toString();
    }

    private char escape() {
        if (pos >= text.length()) {
            throw error("chaine non terminee");
        }
        char c = text.charAt(pos++);
        return switch (c) {
            case '"', '\\', '/' -> c;
            case 'n' -> '\n';
            case 't' -> '\t';
            case 'u' -> unicode();
            default -> throw new MiniJsonException(pos - 1, "echappement invalide");
        };
    }

    // L'erreur pointe l'antislash : il est deux caracteres avant pos (l'antislash, puis le u).
    private char unicode() {
        try {
            char c = (char) Integer.parseInt(text.substring(pos, pos + 4), 16);
            pos += 4;
            return c;
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new MiniJsonException(pos - 1, "echappement invalide");
        }
    }

    private List<Object> array() {
        pos++;
        List<Object> values = new ArrayList<>();
        if (peek() == ']') {
            pos++;
            return values;
        }
        do {
            values.add(value());
        } while (next(']', "',' ou ']' attendu"));
        return values;
    }

    private Map<String, Object> object() {
        pos++;
        Map<String, Object> members = new LinkedHashMap<>();
        if (peek() == '}') {
            pos++;
            return members;
        }
        do {
            if (peek() != '"') {
                throw error("cle attendue");
            }
            String key = string();
            if (peek() != ':') {
                throw error("':' attendu");
            }
            pos++;
            members.put(key, value());
        } while (next('}', "',' ou '}' attendu"));
        return members;
    }

    /** Apres un element : ',' -> vrai, le fermant -> faux, autre chose -> erreur. */
    private boolean next(char close, String expected) {
        char c = peek();
        if (c != ',' && c != close) {
            throw error(expected);
        }
        pos++;
        return c == ',';
    }

    private char peek() {
        spaces();
        if (pos >= text.length()) {
            throw error("fin du texte inattendue");
        }
        return text.charAt(pos);
    }

    private void spaces() {
        while (pos < text.length() && text.charAt(pos) == ' ') {
            pos++;
        }
    }

    private MiniJsonException error(String reason) {
        return new MiniJsonException(pos + 1, reason);
    }

    // ------------------------------------------------------------------ l'ecriture

    public static String write(Object value) {
        StringBuilder out = new StringBuilder();
        write(value, out);
        return out.toString();
    }

    private static void write(Object value, StringBuilder out) {
        if (value == null || value instanceof Boolean || value instanceof Long || value instanceof Integer) {
            out.append(value);
        } else if (value instanceof String s) {
            writeString(s, out);
        } else if (value instanceof List<?> list) {
            out.append('[');
            for (int i = 0; i < list.size(); i++) {
                out.append(i == 0 ? "" : ",");
                write(list.get(i), out);
            }
            out.append(']');
        } else if (value instanceof Map<?, ?> map) {
            writeObject(map, out);
        } else {
            throw new IllegalArgumentException("type non JSON : " + value.getClass().getSimpleName());
        }
    }

    private static void writeObject(Map<?, ?> map, StringBuilder out) {
        out.append('{');
        String separator = "";
        for (Map.Entry<?, ?> e : map.entrySet()) {
            out.append(separator);
            writeString(String.valueOf(e.getKey()), out);
            out.append(':');
            write(e.getValue(), out);
            separator = ",";
        }
        out.append('}');
    }

    private static void writeString(String s, StringBuilder out) {
        out.append('"');
        for (char c : s.toCharArray()) {
            if (c == '"' || c == '\\') {
                out.append('\\').append(c);
            } else if (c == '\n') {
                out.append("\\n");
            } else if (c == '\t') {
                out.append("\\t");
            } else if (c < 0x20) {
                out.append(String.format("\\u%04x", (int) c));
            } else {
                out.append(c);
            }
        }
        out.append('"');
    }
}
