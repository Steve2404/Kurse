package ch19_final.projects.p01_json.solution;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Le parseur : une DESCENTE RECURSIVE. Une methode par regle de la grammaire (valeur, objet, tableau, chaine,
 * nombre), et un curseur pos qui avance dans le texte. Les regles s'appellent entre elles comme la grammaire :
 * un tableau contient des valeurs, une valeur peut etre un tableau...
 */
public final class JsonParser {

    /** Au-dela, on refuse : sans limite, "[[[[..." x 100 000 ferait deborder la pile (StackOverflowError). */
    static final int MAX_DEPTH = 500;

    private final String text;
    private int pos;
    private int depth;

    private JsonParser(String text) {
        this.text = text;
    }

    public static Json parse(String text) {
        JsonParser parser = new JsonParser(text);
        Json value = parser.value();
        parser.skipSpaces();
        if (parser.pos < text.length()) {
            throw parser.error("texte en trop apres la valeur");
        }
        return value;
    }

    // ------------------------------------------------------------------ les regles de la grammaire

    private Json value() {
        char c = peekOrFail();
        if (c == '{') {
            return object();
        }
        if (c == '[') {
            return array();
        }
        if (c == '"') {
            return new JsonString(string());
        }
        if (c == '-' || isDigit(c)) {
            return number();
        }
        return literal();
    }

    private Json literal() {
        if (text.startsWith("true", pos)) {
            pos += 4;
            return new JsonBool(true);
        }
        if (text.startsWith("false", pos)) {
            pos += 5;
            return new JsonBool(false);
        }
        if (text.startsWith("null", pos)) {
            pos += 4;
            return new JsonNull();
        }
        throw error("valeur attendue");
    }

    private JsonArray array() {
        enter();
        List<Json> values = new ArrayList<>();
        if (!closes(']')) {
            do {
                values.add(value());
            } while (separator(']', "',' ou ']' attendu"));
        }
        depth--;
        return new JsonArray(values);
    }

    private JsonObject object() {
        enter();
        Map<String, Json> members = new LinkedHashMap<>();
        if (!closes('}')) {
            do {
                member(members);
            } while (separator('}', "',' ou '}' attendu"));
        }
        depth--;
        return new JsonObject(members);
    }

    private void member(Map<String, Json> members) {
        if (peekOrFail() != '"') {
            throw error("cle attendue");
        }
        int keyStart = pos;
        String key = string();
        // La norme laisse le choix ; une cle en double cache presque toujours une erreur, on la refuse.
        if (members.containsKey(key)) {
            throw errorAt(keyStart, "cle en double : \"" + key + "\"");
        }
        if (peekOrFail() != ':') {
            throw error("':' attendu");
        }
        pos++;
        members.put(key, value());
    }

    private String string() {
        pos++; // le guillemet ouvrant
        StringBuilder sb = new StringBuilder();
        while (true) {
            if (pos >= text.length()) {
                throw error("chaine non terminee");
            }
            char c = text.charAt(pos);
            if (c == '"') {
                pos++;
                return sb.toString();
            }
            if (c < 0x20) {
                throw error("caractere de controle dans une chaine");
            }
            sb.append(c == '\\' ? escape() : text.charAt(pos++));
        }
    }

    private char escape() {
        int start = pos;
        pos++; // l'antislash
        if (pos >= text.length()) {
            throw error("chaine non terminee");
        }
        return switch (text.charAt(pos++)) {
            case '"' -> '"';
            case '\\' -> '\\';
            case '/' -> '/';
            case 'b' -> '\b';
            case 'f' -> '\f';
            case 'n' -> '\n';
            case 'r' -> '\r';
            case 't' -> '\t';
            case 'u' -> unicode(start);
            default -> throw errorAt(start, "echappement invalide");
        };
    }

    // é : 4 chiffres hexadecimaux. Un emoji est une paire (😀) : deux char Java, ajoutes l'un apres l'autre.
    private char unicode(int start) {
        int code = 0;
        for (int i = 0; i < 4; i++) {
            int digit = pos < text.length() ? Character.digit(text.charAt(pos), 16) : -1;
            if (digit < 0) {
                throw errorAt(start, "echappement invalide");
            }
            code = code * 16 + digit;
            pos++;
        }
        return (char) code;
    }

    // -?(0|[1-9][0-9]*)(\.[0-9]+)?([eE][+-]?[0-9]+)? : on verifie la forme, puis BigDecimal fait la conversion.
    private JsonNumber number() {
        int start = pos;
        skip('-');
        integerPart(start);
        if (skip('.') && !digits()) {
            throw errorAt(start, "nombre invalide");
        }
        exponent(start);
        try {
            return new JsonNumber(new BigDecimal(text.substring(start, pos)));
        } catch (NumberFormatException e) {
            throw errorAt(start, "nombre invalide"); // 1e9999999999 : exposant trop grand pour BigDecimal
        }
    }

    private void integerPart(int start) {
        if (skip('0')) {
            if (pos < text.length() && isDigit(text.charAt(pos))) {
                throw errorAt(start, "nombre invalide"); // 012 : pas de zero en tete
            }
        } else if (!digits()) {
            throw errorAt(start, "nombre invalide");
        }
    }

    private void exponent(int start) {
        if (skip('e') || skip('E')) {
            if (!skip('+')) {
                skip('-');
            }
            if (!digits()) {
                throw errorAt(start, "nombre invalide");
            }
        }
    }

    // ------------------------------------------------------------------ le curseur

    private void enter() {
        if (++depth > MAX_DEPTH) {
            throw error("trop de niveaux d'imbrication (" + MAX_DEPTH + " au plus)");
        }
        pos++; // '[' ou '{'
    }

    /** Apres l'ouverture : vrai (et avance) si le tableau ou l'objet est vide. */
    private boolean closes(char close) {
        skipSpaces();
        return skip(close);
    }

    /** Apres un element : vrai sur ',' (il en vient un autre), faux sur la fermeture, erreur sinon. */
    private boolean separator(char close, String expected) {
        char c = peekOrFail();
        if (c != ',' && c != close) {
            throw error(expected);
        }
        pos++;
        return c == ',';
    }

    private char peekOrFail() {
        skipSpaces();
        if (pos >= text.length()) {
            throw error("fin du texte inattendue");
        }
        return text.charAt(pos);
    }

    private boolean skip(char expected) {
        if (pos < text.length() && text.charAt(pos) == expected) {
            pos++;
            return true;
        }
        return false;
    }

    private boolean digits() {
        int start = pos;
        while (pos < text.length() && isDigit(text.charAt(pos))) {
            pos++;
        }
        return pos > start;
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void skipSpaces() {
        while (pos < text.length() && " \t\n\r".indexOf(text.charAt(pos)) >= 0) {
            pos++;
        }
    }

    private JsonException error(String reason) {
        return errorAt(pos, reason);
    }

    /** La ligne et la colonne (a partir de 1) de la position index. */
    private JsonException errorAt(int index, String reason) {
        int line = 1;
        int lineStart = 0;
        for (int i = 0; i < index; i++) {
            if (text.charAt(i) == '\n') {
                line++;
                lineStart = i + 1;
            }
        }
        return new JsonException(reason, line, index - lineStart + 1);
    }
}
