package ch5_methods.projects.p05_overload.solution;

/**
 * SOLUTION - un serialiseur JSON fait de surcharges.
 * Piege : la surcharge est choisie a la COMPILATION, d'apres le type DECLARE de l'argument.
 */
public class Json {

    static String toJson(int n) {
        return String.valueOf(n);
    }

    static String toJson(boolean b) {
        return String.valueOf(b);
    }

    static String toJson(double d) {
        return String.valueOf(d);
    }

    // Les guillemets et antislashs doivent etre echappes ; l'ordre compte (\ d'abord).
    static String toJson(String s) {
        if (s == null) {
            return "null";
        }
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }

    static String toJson(int[] values) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < values.length; i++) {
            sb.append(i == 0 ? "" : ",").append(toJson(values[i]));   // appelle toJson(int)
        }
        return sb.append(']').toString();
    }

    // Un tableau 2D est un tableau de int[] : chaque ligne reutilise toJson(int[]).
    static String toJson(int[][] rows) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < rows.length; i++) {
            sb.append(i == 0 ? "" : ",").append(toJson(rows[i]));
        }
        return sb.append(']').toString();
    }

    static String toJson(String[] values) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < values.length; i++) {
            sb.append(i == 0 ? "" : ",").append(toJson(values[i]));
        }
        return sb.append(']').toString();
    }

    // Une paire cle/valeur : la valeur est deja du JSON.
    static String field(String key, String json) {
        return toJson(key) + ":" + json;
    }

    // Un objet : varargs de champs deja formates.
    static String object(String... fields) {
        return "{" + String.join(",", fields) + "}";
    }

    // Le type DECLARE est Object : sans aide, c'est toujours CETTE version qui est appelee.
    // On redistribue donc a la main avec instanceof (pattern matching, chapitre 3).
    static String toJson(Object o) {
        if (o == null) {
            return "null";
        } else if (o instanceof String s) {
            return toJson(s);
        } else if (o instanceof Integer i) {
            return toJson(i.intValue());
        } else if (o instanceof Boolean b) {
            return toJson(b.booleanValue());
        } else if (o instanceof Double d) {
            return toJson(d.doubleValue());
        } else if (o instanceof int[] arr) {
            return toJson(arr);
        } else if (o instanceof String[] arr) {
            return toJson(arr);
        }
        return toJson(o.toString());
    }

    // Sans redistribution : la version naive montre le piege.
    static String naive(Object o) {
        return "\"" + o + "\"";
    }

    static String array(Object... values) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < values.length; i++) {
            sb.append(i == 0 ? "" : ",").append(toJson(values[i]));   // values[i] est un Object : toJson(Object)
        }
        return sb.append(']').toString();
    }
}
